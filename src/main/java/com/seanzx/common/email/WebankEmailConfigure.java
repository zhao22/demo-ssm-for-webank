package com.seanzx.common.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 发件箱配置（通过配置文件设置）
 * @author zhaoxin
 * @date 2020/10/24
 */
@Component
@ConfigurationProperties(prefix = "webank.email.configuration")
public class WebankEmailConfigure extends EmailConfigure{

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

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String getEncryption() {
        return encryption;
    }

    @Override
    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
}
