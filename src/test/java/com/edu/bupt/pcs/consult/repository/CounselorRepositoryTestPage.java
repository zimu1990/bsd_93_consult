package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorEntity;
import lombok.ToString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author: wzz
 * @date: 19-6-12 下午2:17
 * @description
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CounselorRepositoryTestPage {

    @Autowired
    public CounselorRepository counselorRepository;
    @Test
    public void findByPhoneNumber() {
        System.out.println(counselorRepository.findByPhoneNumber("1583279758"));
    }
    @Test
    public void save(){
        @ToString
        class P{
            Integer age;
            String name;
            String pws;

            public P(int age, String name, String pws) {
                this.age = age;
                this.name = name;
                this.pws = pws;
            }

            public String getPws() {
                return pws;
            }

            public void setPws(String pws) {
                this.pws = pws;
            }

            public P(Integer age) {
                this.age = age;
            }
            public P(Integer age, String name) {
                this.age = age;
                this.name = name;
            }

            public Integer getAge() {
                return age;
            }

            public void setAge(Integer age) {
                this.age = age;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
        @ToString

        class T{
            Integer age;
            Integer score;
            String name;

            public T(Integer age, Integer score, String name) {
                this.age = age;
                this.score = score;
                this.name = name;
            }

            public T(Integer score, String name) {
                this.score = score;
                this.name = name;
            }

            public Integer getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Integer getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }
        }

        P p = new P(1,"t","3tt");
        T t = new T(20,"li");
        BeanUtils.copyProperties(t,p);
        System.out.println(p);
    }
}