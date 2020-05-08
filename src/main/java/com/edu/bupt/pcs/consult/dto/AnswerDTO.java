package com.edu.bupt.pcs.consult.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: wzz
 * @date: 19-7-7 下午2:54
 * @description
 */
@Data
public class AnswerDTO {
    private String id;
    private String questionId;
    private String content;
    private Integer responderId;
    private String responderType;
    @JsonFormat(pattern= "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;
    private String truename;
    private String avatar;

}
