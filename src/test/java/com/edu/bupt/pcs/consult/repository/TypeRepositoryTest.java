package com.edu.bupt.pcs.consult.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TypeRepositoryTest {

    @Autowired
    private TypeRepository typeRepository;
    @Test
    public void test(){
        System.out.println(typeRepository.findById(11).get().getLabel());
    }
}