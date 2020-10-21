package com.seanzx.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * http 请求记录日志
 * @author zhaoxin
 * @date 2020/10/21
 */
@Table(name = "t_http_request")
public class HttpRequestPO {

    @Id
    private String id;

    /**
     * 请求接收时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 请求URI
     */
    private String uri;

    /**
     * 请求方式
     */
    @Column(name = "request_method")
    private String requestMethod;

    /**
     * 请求 IP
     */
    @Column(name = "request_ip")
    private String requestIp;

    /**
     * 异常返回
     */
    @Column(name = "exception_message")
    private String exceptionMessage;

    /**
     * 请求持续时间
     */
    private Long duration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
