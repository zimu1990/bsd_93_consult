package com.edu.bupt.pcs.consult.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: wzz
 * @date: 19-7-13 下午3:35
 * @description
 */
@Data
public class QuestionDTO2 {
    private String id;
    private String content;
    private String type;
    private Integer testeeId;
    @JsonFormat(pattern= "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;
    private Long score;
    private String title;
    private Integer isAnonymous;
    private String isPublic;
    private String truename;
}
