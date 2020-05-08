package com.edu.bupt.pcs.consult.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {
    private String professional;
    private String attitude;
    private String overall;
    private String isOnTime;
    private String isWillNext;
}
