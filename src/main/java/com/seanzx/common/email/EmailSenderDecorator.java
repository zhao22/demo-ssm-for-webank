package com.seanzx.common.email;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.util.Arrays;
import java.util.Collections;

/**
 * 邮件发送装饰类
 * 保留EmailSender类的扩展性，同时为该类扩展一些易用的方法
 * @author zhaoxin
 * @date 2020/10/24
 */
public class EmailSenderDecorator {

    public static final int DEFAULT_CONNECT_TIMEOUT = 30000;

    public static final int DEFAULT_TIMEOUT = 20000;


    private final EmailSender emailSender;

    public EmailSenderDecorator(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * 根据 字符串创建HTML对象
     */
    public Multipart buildMultipartBody(String content) {
        try {
            return emailSender.buildMultipartBody("text/html; charset=utf-8", content);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    public String sendEmail(String to,
                            String subject,
                            String content) {
        return this.sendEmail(to, subject, content, DEFAULT_CONNECT_TIMEOUT, DEFAULT_TIMEOUT);
    }

    /**
     * 发送邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    public String sendEmail(String to,
                            String subject,
                            Multipart content) {
        return this.sendEmail(to, subject, content, DEFAULT_CONNECT_TIMEOUT, DEFAULT_TIMEOUT);
    }

    /**
     * 发送邮件，
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param connectTimeout 连接超时时间
     * @param timeout 超时时间
     */
    public String sendEmail(String to,
                            String subject,
                            String content,
                            int connectTimeout,
                            int timeout) {
        try {
            return emailSender.sendMessage(Arrays.asList(to),
                    Collections.emptyList(),
                    subject,
                    message -> message.setText(content),
                    connectTimeout,
                    timeout);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送邮件，
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param connectTimeout 连接超时时间
     * @param timeout 超时时间
     */
    public String sendEmail(String to,
                            String subject,
                            Multipart content,
                            int connectTimeout,
                            int timeout) {
        try {
            return emailSender.sendMessage(Arrays.asList(to),
                    Collections.emptyList(),
                    subject,
                    message -> message.setContent(content),
                    connectTimeout,
                    timeout);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
