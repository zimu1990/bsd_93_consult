package com.edu.bupt.pcs.consult.dto;

import cn.edu.bupt.pcsmavenjpa.entity.FeedbackEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {
    private long id;
    private int createrId;
    private String createType;
    private String username;
    private String content;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public FeedbackDto(FeedbackEntity entity){
        this.id = entity.getId();
        this.createrId = entity.getCreaterId();
        this.createType = entity.getCreateType();
        this.username = null;
        this.content = entity.getContent();
        this.createTime = entity.getCreateTime();
    }
}
