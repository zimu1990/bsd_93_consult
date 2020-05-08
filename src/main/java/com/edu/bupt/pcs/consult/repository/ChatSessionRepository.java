package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, Integer> {
    @Query(value = "select cs.id from chat_session cs where cs.prediction_id = ?1", nativeQuery = true)
    List<Integer> findAllByPredictionId(Integer reserveId);
}
