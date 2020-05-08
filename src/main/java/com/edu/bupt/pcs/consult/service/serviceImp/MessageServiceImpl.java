package com.edu.bupt.pcs.consult.service.serviceImp;

import com.alibaba.druid.sql.parser.Lexer;
import com.edu.bupt.pcs.consult.service.MessageService;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import com.edu.bupt.pcs.consult.utils.SmsUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送验证码短信并把验证码存入redis
     * @param phoneNumber
     * @return
     */
    @Override
    public CommonResult sendVerificationCode(String phoneNumber) {
        String code = RandomStringUtils.randomNumeric(6);
        log.info("code:"+code);
        SmsUtils.sendSms(code ,phoneNumber);
        redisTemplate.opsForValue().set(phoneNumber,code);
        redisTemplate.expire(phoneNumber,2, TimeUnit.MINUTES);
        String nowCode = (String) redisTemplate.opsForValue().get(phoneNumber);
        log.info("nowCode:"+nowCode);
        return new CommonResult().success(null);
    }
    public int compareInputCodeWithVerification(String phoneNumber,String inputCode) {
        //1为验证码正确，0为验证码不正确，2验证码失效
        if(redisTemplate.hasKey(phoneNumber)){
            if (redisTemplate.opsForValue().get(phoneNumber).equals(inputCode)){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 2;
        }
    }
}
