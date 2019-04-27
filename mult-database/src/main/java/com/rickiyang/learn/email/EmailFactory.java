package com.rickiyang.learn.email;

import com.alibaba.fastjson.JSONObject;
import com.rickiyang.learn.utils.LoggerUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * @Author yangyue
 * @Modified by:
 * @Description:
 **/
@Component
public class EmailFactory {

    @Value("${email.sender.host}")
    private String host;

    @Value("${email.sender.account}")
    private String account;

    @Value("${email.sender.password}")
    private String password;

    private Properties properties = new Properties();

    private EmailAuthenticator authenticator;

    private Session session;

    private static final String contentType = "text/html;charset=utf-8";


    @PostConstruct
    public EmailFactory build() {
        if (StringUtils.isAnyBlank(host, account, password)) {
            LoggerUtils.error("邮件服务必传参数未设置");
            return null;
        }
        // 初始化properties
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.host", host);
        // 验证
        authenticator = new EmailAuthenticator(account, password);
        // 创建session
        session = Session.getInstance(properties, authenticator);
        return this;
    }

    /**
     * 发送邮件之前建立连接
     *
     * @return
     */
    private Transport connect() {
        Transport ts = null;
        try {
            String[] split = account.split("@");
            ts = session.getTransport();
            ts.connect(host, split[0], password);
        } catch (MessagingException e) {
            LoggerUtils.error("链接邮箱失败", e);
        }
        return ts;
    }

    /**
     * 发送/抄送邮件
     *
     * @param email
     * @throws AddressException
     * @throws MessagingException
     */
    public void send(Email email) throws MessagingException {
        Transport ts = connect();
        if (ts == null) {
            LoggerUtils.error("获取连接失败");
            return;
        }
        final MimeMessage message = copyToSetting(email);
        // 设置邮件内容
        message.setContent(email.getContent(), contentType);
        // 发送
        ts.sendMessage(message, message.getAllRecipients());
        //关闭连接
        ts.close();
        LoggerUtils.info("邮件发送成功，email={}", JSONObject.toJSONString(email));
    }

    /**
     * 发送/抄送邮件 - 附件
     *
     * @param email
     * @param files 文件
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(Email email, File[] files) throws MessagingException, UnsupportedEncodingException {
        Transport ts = connect();
        if (ts == null) {
            LoggerUtils.error("获取连接失败");
            return;
        }
        final MimeMessage message = copyToSetting(email);
        // 设置邮件内容
        accessorySetting(email, files, message);
        // 发送
        ts.sendMessage(message, message.getAllRecipients());
        //关闭连接
        ts.close();
        LoggerUtils.info("邮件发送成功，email={}", JSONObject.toJSONString(email));
    }

    /**
     * @param email
     * @param files
     * @param message
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private void accessorySetting(Email email, File[] files, final MimeMessage message)
            throws MessagingException, UnsupportedEncodingException {
        Multipart multipart = new MimeMultipart();
        {
            BodyPart bodyPart = new MimeBodyPart();
            // html 文本内容描述
            bodyPart.setContent(email.getContent(), contentType);
            multipart.addBodyPart(bodyPart);
            if (null != files) {
                for (File file : files) {
                    bodyPart = new MimeBodyPart();
                    bodyPart.setDataHandler(new DataHandler(new FileDataSource(file)));
                    bodyPart.setFileName(MimeUtility.encodeText(file.getName()));
                    multipart.addBodyPart(bodyPart);
                }
            }
        }
        message.setContent(multipart);
    }

    /**
     * @param email
     * @return
     * @throws MessagingException
     * @throws AddressException
     */
    private MimeMessage copyToSetting(Email email) throws MessagingException {
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        message.setFrom(new InternetAddress(authenticator.getUsername()));
        // 设置收件人
        if (CollectionUtils.isEmpty(email.getRecipient())) {
            LoggerUtils.error("收件人不能为null");
            return message;
        }
        message.setRecipient(RecipientType.TO, new InternetAddress(email.getRecipient().get(0)));

        List<String> copyTos = email.getCopyTo();
        if (CollectionUtils.isNotEmpty(copyTos)) {
            // 为每个邮件接收者创建一个地址
            Address[] copyToAdresses = new InternetAddress[copyTos.size()];
            for (int i = 0; i < copyTos.size(); i++) {
                copyToAdresses[i] = new InternetAddress(copyTos.get(i));
            }
            // 将抄送者信息设置到邮件信息中，注意类型为Message.RecipientType.CC
            message.setRecipients(RecipientType.CC, copyToAdresses);
        }

        // 设置主题
        message.setSubject(email.getTheme());
        return message;
    }

    /**
     * 群发邮件 - 附件
     *
     * @param email
     * @param files
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendToAll(Email email, File[] files)
            throws MessagingException, UnsupportedEncodingException {
        Transport ts = connect();
        if (ts == null) {
            LoggerUtils.error("获取连接失败");
            return;
        }
        final MimeMessage message = massSetting(email.getRecipient(), email.getTheme());
        // 设置邮件内容
        Multipart multipart = new MimeMultipart();
        {
            BodyPart bodyPart = new MimeBodyPart();
            // html 文本内容描述
            bodyPart.setContent(email.getContent(), contentType);
            multipart.addBodyPart(bodyPart);
            if (null != files) {
                for (File file : files) {
                    bodyPart = new MimeBodyPart();
                    {
                        bodyPart.setDataHandler(new DataHandler(new FileDataSource(file)));
                        bodyPart.setFileName(MimeUtility.encodeText(file.getName()));
                        multipart.addBodyPart(bodyPart);
                    }
                }
            }
        }
        message.setContent(multipart);
        // 发送
        ts.sendMessage(message, message.getAllRecipients());
        //关闭连接
        ts.close();
    }

    /**
     * @param recipients
     * @param subject
     * @return
     * @throws MessagingException
     * @throws AddressException
     */
    private MimeMessage massSetting(List<String> recipients, String subject) throws MessagingException {
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        message.setFrom(new InternetAddress(authenticator.getUsername()));
        // 设置收件人们
        final int num = recipients.size();
        InternetAddress[] addresses = new InternetAddress[num];
        for (int i = 0; i < num; i++) {
            addresses[i] = new InternetAddress(recipients.get(i));
        }
        message.setRecipients(RecipientType.TO, addresses);
        // 设置主题
        message.setSubject(subject);
        return message;
    }

    /**
     * 群发邮件
     *
     * @param email
     * @throws MessagingException
     */
    public void sendToAll(Email email) throws MessagingException {
        Transport ts = connect();
        if (ts == null) {
            LoggerUtils.error("获取连接失败");
            return;
        }
        final MimeMessage message = massSetting(email.getRecipient(), email.getTheme());
        // 设置邮件内容
        message.setContent(email.getContent(), contentType);
        // 发送
        ts.sendMessage(message, message.getAllRecipients());
        //关闭连接
        ts.close();
    }

}
