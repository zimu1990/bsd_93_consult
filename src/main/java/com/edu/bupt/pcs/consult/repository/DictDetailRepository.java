package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.DictDetailEntity;
import com.edu.bupt.pcs.consult.dto.SpecialtyDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface DictDetailRepository extends JpaRepository<DictDetailEntity, String> {

    DictDetailEntity findFirstByLabel(String label);

    @Query(value = "select new com.edu.bupt.pcs.consult.dto.SpecialtyDto(d.id, d.label) from DictDetailEntity d where d.dictId = ?1")
    List<SpecialtyDto> findSpecialtyByDictId(int dictId);
}
