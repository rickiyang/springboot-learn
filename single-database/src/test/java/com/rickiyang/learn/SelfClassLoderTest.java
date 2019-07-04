package com.rickiyang.learn;

import com.google.common.collect.Sets;
import com.rickiyang.learn.utils.SelfClassLoder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author rickiyang
 * @date 2019-07-04
 * @Desc  测试自己的类加载器
 *  注意这里的SelfClassLoder必须要和Test类在同一包下，不然找不到Test类的路径
 *
 *  测试方式：
 *  启动定时任务首先会打印：当前版本是2哦
 *  然后你手动将 2 改为1 编译一下Test类，或者提前编译好，替换掉Test.class，
 *  就可以看到自动加载了这个新的类文件了
 *  这种方式可以用于我们热更新某些类文件
 *
 */
public class SelfClassLoderTest {

    public static void main(String[] args) {
        //创建一个2s执行一次的定时任务
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String swapPath = SelfClassLoder.class.getResource("").getPath();
                String className = "com.rickiyang.learn.Test";

                //每次都实例化一个ClassLoader，这里传入swap路径，和需要特殊加载的类名
                SelfClassLoder myClassLoader = new SelfClassLoder(swapPath, Sets.newHashSet(className));
                try {
                    //使用自定义的ClassLoader加载类，并调用printVersion方法。
                    Object o = myClassLoader.loadClass(className).newInstance();
                    o.getClass().getMethod("printVersion").invoke(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0,2000);
    }
}
