package com.seanzx.common.email;

/**
 * 邮件配置(可提到公用依赖中)
 * @author zhaoxin
 * @date  2020/10/24
 */
public class EmailConfigure {
    /**
     * 发件人
     */
    private String from;
    /**
     * 发件人用户名
     */
    private String username;
    /**
     * 发件人密码
     */
    private String password;
    /**
     * 邮件服务器
     */
    private String host;
    /**
     * 邮件服务器端口
     */
    private String port;
    /**
     * 加密方式
     */
    private String encryption;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
}
