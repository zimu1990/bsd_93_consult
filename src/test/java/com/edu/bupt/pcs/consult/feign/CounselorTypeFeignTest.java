package com.edu.bupt.pcs.consult.feign;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author: wzz
 * @date: 19-7-7 下午7:11
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CounselorTypeFeignTest {

    @Autowired
    private CounselorTypeFeign counselorTypeFeign;
     @Test
     public void test1(){
         System.out.println(counselorTypeFeign.getCounselorType("咨询师领域"));
     }
}