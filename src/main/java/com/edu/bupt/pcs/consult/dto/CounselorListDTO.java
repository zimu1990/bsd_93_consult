package com.edu.bupt.pcs.consult.dto;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CounselorListDTO {
    private CounselorEntity counselorEntity;
    private String avatarurl;
    private String newcertificateurl;
    private String idImage1url;
    private String idImage2url;
    private List<String> counselorType;
    public CounselorListDTO()
    {

    }
    public CounselorListDTO(CounselorEntity counselorEntity, String avatarurl, String newcertificateurl, String idImage1url, String idImage2url, List<String> counselorType) {
        this.counselorEntity = counselorEntity;
        this.avatarurl = avatarurl;
        this.newcertificateurl = newcertificateurl;
        this.idImage1url = idImage1url;
        this.idImage2url = idImage2url;
        this.counselorType = counselorType;
    }

}
