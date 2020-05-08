package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.KnowledgeBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: wzz
 * @date: 19-6-13 下午6:56
 * @description
 */
public interface KnowledgeBaseRepository extends JpaRepository<KnowledgeBaseEntity,Integer> {
}
