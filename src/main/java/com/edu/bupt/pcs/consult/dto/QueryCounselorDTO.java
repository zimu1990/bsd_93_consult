package com.edu.bupt.pcs.consult.dto;

import lombok.Data;

@Data
public class QueryCounselorDTO {

    private String username;

    private Integer specialty;

    private int page;

    private int size;
}
