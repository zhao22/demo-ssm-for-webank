package com.seanzx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author zhaoxin
 * @date 2020/10/21
 */
@ApiModel(value="http请求模型")
public class HttpRequestVO {

    @ApiModelProperty(name = "请求id")
    private String id;

    @ApiModelProperty(name = "请求接收时间")
    private Date startTime;

    @ApiModelProperty(name = "请求URI")
    private String uri;

    @ApiModelProperty(name = "请求方式")
    private String requestMethod;

    @ApiModelProperty(name = "请求 IP")
    private String requestIp;

    @ApiModelProperty(name = "异常返回")
    private String exceptionMessage;

    @ApiModelProperty(name = "请求持续时间")
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

    @Override
    public String toString() {
        return "HttpRequestVO{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", uri='" + uri + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestIp='" + requestIp + '\'' +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                ", duration=" + duration +
                '}';
    }
}
