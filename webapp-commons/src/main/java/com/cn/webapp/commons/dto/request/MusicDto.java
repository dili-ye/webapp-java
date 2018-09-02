package com.cn.webapp.commons.dto.request;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MusicDto {
    private String musicUrl;

    private String author;

    private String musicName;

    private String album;

    private String style;

    private String sourceFrom;

    private String sourcepath;

    private Boolean needpay;

    private Integer validstatus;

    private Date createTime;

    private Date updateTime;
}
