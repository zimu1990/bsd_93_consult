package com.edu.bupt.pcs.consult.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author: wzz
 * @date: 19-7-20 上午11:47
 * @description
 */
@Data
public class ConselorDTO2 {
    private int id;
    private String username;
    private String oldpassword;
    private String password;
    private String trueName;
    private String gender;
    private Date birthday;
    private String phoneNumber;
    private String email;
    private String maritalStatus;
    private String specialty;
    private String certificate;
    private String workingExperience;
    private String status;
    private String avatar;
    private String information;
    @JsonFormat(timezone = "GMT+8",pattern= "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    private String idImage1;
    private String idImage2;
    private String auditFeedback;
    private Integer auditorId;
    @JsonFormat(timezone = "GMT+8",pattern= "yyyy-MM-dd HH:mm:ss")
    private Timestamp auditTime;
    private Integer unitId;

    private ArrayList<Integer> types = new ArrayList<>();

}
