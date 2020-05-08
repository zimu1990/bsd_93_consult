package com.edu.bupt.pcs.consult.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzz
 * @date: 19-7-3 下午2:09
 * @description
 */
@Data
public class QuestionDTO {
    private String id;
    private String content;
    private String type;
    private Integer testeeId;
    @JsonFormat(timezone = "GMT+8",pattern= "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    private BigInteger score;
    private String title;
    private Integer isAnonymous;
    private String isPublic;
    private List<String> counselorId = new ArrayList<String>();
//    private ArrayList<Integer> counselorId = new ArrayList<Integer>();
    @JsonFormat(timezone = "GMT+8",pattern= "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
    private String truename;
    private Integer status;
    private String img_url;
}
