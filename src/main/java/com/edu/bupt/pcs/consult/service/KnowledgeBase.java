package com.edu.bupt.pcs.consult.service;

import com.edu.bupt.pcs.consult.utils.CommonResult;

/**
 * @author: wzz
 * @date: 19-6-13 下午7:16
 * @description
 */
public interface KnowledgeBase {
    public CommonResult getCommonQuestion(Integer page,Integer size);
}
