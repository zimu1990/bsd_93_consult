package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.TesteeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: wzz
 * @date: 19-6-12 下午1:44
 * @description
 */
@Repository
public interface TesteeRepository extends JpaRepository<TesteeEntity,Integer> {

    List<TesteeEntity> findByTrueName(String trueName);

    @Query(value = "select t.true_name from testee t where t.id =?1",nativeQuery = true)
    String findTrueNameById(Integer id);
}
