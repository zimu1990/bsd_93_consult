package com.edu.bupt.pcs.consult.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 咨询过程参数接口类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultProcessDto {
    private int id;
    private String counselor;
    private String username;
    @JsonFormat(timezone = "GMT+8",pattern= "yyyy-MM-dd HH:mm:ss")
    private Timestamp startTime;
    @JsonFormat(timezone = "GMT+8",pattern= "yyyy-MM-dd HH:mm:ss")
    private Timestamp endTime;
    private long appointmentDuration;
    private long actualDuration;
    private EvaluationDto evaluation;
    private String status;

}
