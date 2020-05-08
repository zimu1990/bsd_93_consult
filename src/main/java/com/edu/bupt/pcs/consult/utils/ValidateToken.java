//package com.edu.bupt.pcsconsult.utils;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.bupt.pcs.examservice.entity.TesteeEntity;
//import com.bupt.pcs.examservice.repository.TestEERepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
///**
// * @author: wzz
// * @date: 19-4-20 22:04
// * @description:
// */
//@Component
//public class ValidateToken {
//    @Autowired
//    private TestEERepository testEERepository;
//    private String token;
//    public boolean validateToken(String token){
//        if (token == null) {
//            return false;
//        }
//        // 获取 token 中的 user id
//        String userId;
//        try {
//            userId = JWT.decode(token).getAudience().get(0);
//        } catch (JWTDecodeException j) {
//            throw new RuntimeException("401");
//        }
//        Optional<TesteeEntity> testee = testEERepository.findById(Long.parseLong(userId));
//
//        if (testee == null) {
//            return false;
//        }
//        // 验证 token
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(testee.get().getPassword())).build();
//        try {
//            jwtVerifier.verify(token);
//        } catch (JWTVerificationException e) {
//            throw new RuntimeException("401");
//        }
//        return true;
//    }
//}
