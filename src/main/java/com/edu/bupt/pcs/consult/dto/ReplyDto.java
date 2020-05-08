package com.edu.bupt.pcs.consult.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: wzz
 * @date: 19-7-7 下午5:36
 * @description
 */
@Data
public class ReplyDto {
    private String id;
    private String replyContent;
    @JsonFormat(pattern= "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;
    private Integer responderId;
    private String responderType;
    private String answerId;
    private String truename;
}
