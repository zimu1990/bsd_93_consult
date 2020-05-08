package com.edu.bupt.pcs.consult.service;

import com.edu.bupt.pcs.consult.utils.CommonResult;

public interface MessageService {
    public CommonResult sendVerificationCode(String phoneNumber);

    public int compareInputCodeWithVerification(String phoneNumber,String inputCode);
}
