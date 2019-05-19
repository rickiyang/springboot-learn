package com.rickiyang.learn.utils;

import com.rickiyang.learn.common.CodeEnum;
import com.rickiyang.learn.exception.BizException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtil {

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    static int MID_SIZE = 50 * 1024;//50k
    static int LOW_SIZE = 20 * 1024;//20k
    static int HIGH_HEIGHT = 1024;
    static int MID_HEIGHT = 800;
    static int LOW_HEIGHT = 500;

    static float AC_THRESHOLD = 0.53F;
    static float AC_STEP = 0.9F;
    static float AC_STEP_SLOW = 0.95F;

    /**
     * create thumb for pic files.
     *
     * @param srcPath     source picture file path
     * @param dstPath     dest picture file path.
     * @param maxFileSize max file size of dest picture file path.
     */
    public static void createThumb(String srcPath, String dstPath, long maxFileSize) {
        File file = new File(srcPath);
        boolean isFileNormal = file.exists() && file.isFile();
        if (!isFileNormal) {
            throw new BizException(CodeEnum.SYSTEM_ERROR.getMessage());
        }

        try {
            File srcFileJPG = new File(srcPath);
            long srcFileSizeJPG = srcFileJPG.length();
            if (srcFileSizeJPG <= maxFileSize) {
                FileUtils.copyFile(srcFileJPG, new File(dstPath));
                return;
            }

            commpressPicCycle(srcPath, dstPath, maxFileSize, 0.9);
        } catch (IOException e) {
            throw new BizException(CodeEnum.SYSTEM_ERROR.getMessage());
        }
    }

    private static void commpressPicCycle(String srcPath, String dstPath, long maxFileSize,
                                          double accuracy) throws IOException {
        File srcFileJPG = new File(srcPath);
        // 计算宽高
        BufferedImage bim = ImageIO.read(srcFileJPG);
        int srcWidth = bim.getWidth();
        int srcHeigh = bim.getHeight();

        int dstHeigh = LOW_HEIGHT;
        if (maxFileSize > LOW_SIZE && maxFileSize <= MID_SIZE) {
            dstHeigh = MID_HEIGHT;
        } else if (maxFileSize > MID_SIZE) {
            dstHeigh = HIGH_HEIGHT;
        }

        if (accuracy < AC_THRESHOLD) {
            dstHeigh = (int) (dstHeigh * (accuracy / AC_THRESHOLD));
        }

        int dstWidth = srcWidth * dstHeigh / srcHeigh;

        Thumbnails.of(srcPath).size(dstWidth, dstHeigh).outputQuality(accuracy).toFile(dstPath);
        File dstFileJPG = new File(dstPath);

        if (dstFileJPG.length() > maxFileSize) {
            if (accuracy >= AC_THRESHOLD) {
                accuracy = accuracy * AC_STEP;
            } else {
                accuracy = accuracy * AC_STEP_SLOW;
            }
            commpressPicCycle(srcPath, dstPath, maxFileSize, accuracy);
        }
    }

    /**
     * todo: improve quality compress image file to specified size.
     *
     * @param original input image file
     * @param resized  image file after compress
     * @param size     the size to compressed to
     */
    public static void compress(String original, String resized, long size) throws Exception {
        double scale = 1;
        long tsize = new File(original).length();
        while (tsize > size) {
            resize(original, resized, scale, 0.5F);
            tsize = new File(resized).length();
            scale = scale * 0.9;
        }
    }

    /**
     * 缩放图片
     *
     * @param original 原文件路径
     * @param resized  压缩目标文件路径
     * @param quality  压缩质量（越高质量越好）
     * @param scale    缩放比例;  1等大.
     */
    public static void resize(String original, String resized, double scale, float quality)
            throws IOException {
        File originalFile = new File(original);
        File resizedFile = new File(resized);
        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
        Image i = ii.getImage();

        int iWidth = (int) (i.getWidth(null) * scale);
        int iHeight = (int) (i.getHeight(null) * scale);
        //在这你可以自定义 返回图片的大小 iWidth iHeight
        Image resizedImage = i.getScaledInstance(iWidth, iHeight, Image.SCALE_SMOOTH);
        // 获取图片中的所有像素
        Image temp = new ImageIcon(resizedImage).getImage();
        // 创建缓冲
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
        // 复制图片到缓冲流中
        Graphics g = bufferedImage.createGraphics();
        // 清除背景并开始画图
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);

        g.dispose();
        // 柔和图片.
        float softenFactor = 0.05f;
        float[] softenArray = {0, softenFactor, 0, softenFactor,
                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);
        FileOutputStream out = new FileOutputStream(resizedFile);

        saveAsJPEG(bufferedImage, quality, out);
        bufferedImage.flush();
        out.close();
    }


    public static void saveAsJPEG(BufferedImage ImageBuffer, float JPEGcompression,
                                  FileOutputStream fos) throws IOException {
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(
                new ImageTypeSpecifier(ImageBuffer), null);
        ImageWriteParam jpegParams = null;
        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {
            jpegParams = imageWriter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(JPEGcompression);

        }
        imageWriter.write(imageMetaData,
                new IIOImage(ImageBuffer, null, null), jpegParams);
        ios.close();
        imageWriter.dispose();
    }
}
