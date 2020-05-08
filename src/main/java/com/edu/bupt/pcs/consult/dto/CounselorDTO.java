package com.edu.bupt.pcs.consult.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzz
 * @date: 19-7-7 下午9:28
 * @description
 */
@Data
public class CounselorDTO {
    private int id;
    private String username;
    private String truename;
    private String gender;
    @JsonFormat(timezone = "GMT+8",pattern= "yyyy-MM-dd")
    private Date birthday;
    private String phoneNumber;
    private String email;
    private String specialty;
    private List<String> areasOfExpertiseLists= new ArrayList<>();
    private String workingExperience;
    private String avatar;
    private String information;
    private Double score;
}
