package com.seanzx.common.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

/**
 * 邮件发送底层类(可提到公用依赖)
 * @author zhaoxin
 * @date 2020/10/24
 */
public class EmailSender {


    public static final String RESPONSE_HEADER = "Message-ID";
    public static final String SSL_ENCRYPTION = "ssl";
    public static final String TLS_ENCRYPTION = "tls";

    private EmailConfigure emailConfigure;


    public EmailSender(EmailConfigure emailConfigure) {
        this.emailConfigure = emailConfigure;
    }


    /**
     * 发送邮件
     * @param toList 收件人列表
     * @param copyList 抄送列表
     * @param subject 发送主题
     * @param beforeSend 发送前需要为邮件添加内容
     * @param connectTimeout 连接超时时间
     * @param timeout 超时时间
     * @return
     * @throws MessagingException
     */
    public String sendMessage(List<String> toList,
                               List<String> copyList,
                               String subject,
                               MessageExceptionConsumer<Message> beforeSend,
                               int connectTimeout,
                               int timeout) throws MessagingException{
        // 1. 设置基本属性
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        if (SSL_ENCRYPTION.equals(this.emailConfigure.getEncryption())) {
            props.put("mail.smtp.ssl.enable", "true");
        } else if (TLS_ENCRYPTION.equals(this.emailConfigure.getEncryption())) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        props.put("mail.smtp.host", this.emailConfigure.getHost());
        props.put("mail.smtp.port", this.emailConfigure.getPort());
        props.put("mail.smtp.connectiontimeout", connectTimeout);
        props.put("mail.smtp.timeout", timeout);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailSender.this.emailConfigure.getUsername(),
                        EmailSender.this.emailConfigure.getPassword());
            }
        });
        // 2. 创建Message对象
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.emailConfigure.getFrom()));
        // 3. 添加收件人
        for (String to : toList) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        }
        // 4. 添加抄送人
        for (String copy : copyList) {
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(copy));
        }
        // 5. 设置主题
        message.setSubject(subject);
        // 6. 执行Consumer，添加内容
        if (beforeSend != null) {
            beforeSend.accept(message);
        }
        // 7. 发送邮件
        Transport.send(message);
        return message.getHeader(RESPONSE_HEADER)[0];
    }

    /**
     * 根据contentType 将字符串封装为对应Multipart对象
     * @param contentType
     * @param content
     * @return
     * @throws MessagingException
     */
    public Multipart buildMultipartBody(String contentType, String content) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        BodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(content, contentType);
        multipart.addBodyPart(htmlPart);
        return multipart;
    }

    /**
     * 可以抛出MessagingException 的Consumer对象。
     * @param <T>
     */
    public interface MessageExceptionConsumer<T> {
        /**
         * 和Consumer使用一致，不过该方法可以抛出 MessagingException
         * @param t 传递参数，一般是message
         * @throws MessagingException
         */
        void accept(T t) throws MessagingException;
    }
}
