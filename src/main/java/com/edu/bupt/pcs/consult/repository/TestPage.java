package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.EvaluationPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: wzz
 * @date: 19-6-13 下午7:59
 * @description
 */
@Repository
public interface TestPage extends JpaRepository<EvaluationPlanEntity,Integer> {
}
