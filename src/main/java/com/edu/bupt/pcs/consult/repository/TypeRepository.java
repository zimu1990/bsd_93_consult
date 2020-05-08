package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.DictDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: wzz
 * @date: 19-6-12 下午1:44
 * @description
 */
@Repository
public interface TypeRepository extends JpaRepository<DictDetailEntity,Integer> {
//    List<DictDetailEntity> findByIdIN(List<Integer> id);
//
//    List<DictDetailEntity> findByIdIn(List<Integer> types);

    List<DictDetailEntity> findByIdIn(List<Integer> Id);


}
