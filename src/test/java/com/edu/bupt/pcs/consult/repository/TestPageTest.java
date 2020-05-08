package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.EvaluationPlanEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: wzz
 * @date: 19-6-13 下午8:02
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPageTest {

    @Autowired
    private TestPage testPage;
    @Test
    public void test1(){
        Sort.Direction sort =  Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(0, 5, sort, "id");
        Page<EvaluationPlanEntity> all = testPage.findAll(pageable);
        System.out.println(all.getTotalPages());
        System.out.println(all.getTotalElements());
        System.out.println(all.getContent());
    }
}