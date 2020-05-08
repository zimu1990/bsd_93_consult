package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.ChatRecordEntity;
import com.edu.bupt.pcs.consult.dto.ChatRecordDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRecordRepository extends JpaRepository<ChatRecordEntity, Integer> {
    //sessionIds在传入前需要判断是否为空，否则会报ParseException
    @Query(value = "select new com.edu.bupt.pcs.consult.dto.ChatRecordDto(cre.id, cre.directionId, cre.senderName," +
            "cre.receiverName, cre.content, cre.contentType, cre.sessionId, cre.createTime) " +
            "from ChatRecordEntity cre where cre.sessionId in ?1 order by cre.createTime")
    List<ChatRecordDto> findContentBySessionId(List<Integer> sessionIds);
}
