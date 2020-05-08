package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {

    @Query(value = "select id from unit u where u.parent_id in ?1", nativeQuery = true)
    List<Integer> findAllByParentId(List<Integer> parentIds);
}
