package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: wzz
 * @date: 19-6-12 下午1:44
 * @description
 */
@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity,String> {


    List<ReplyEntity> findAllByAnswerIdOrderByCreateTimeDesc(String answerId);
}
