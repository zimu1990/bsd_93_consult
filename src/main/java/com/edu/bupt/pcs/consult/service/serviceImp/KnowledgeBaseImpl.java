package com.edu.bupt.pcs.consult.service.serviceImp;

import cn.edu.bupt.pcsmavenjpa.entity.KnowledgeBaseEntity;
import com.edu.bupt.pcs.consult.repository.KnowledgeBaseRepository;
import com.edu.bupt.pcs.consult.service.KnowledgeBase;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**
 * @author: wzz
 * @date: 19-6-13 下午7:16
 * @description
 */
@Service
public class KnowledgeBaseImpl implements KnowledgeBase {

    @Autowired
    private KnowledgeBaseRepository knowledgeBaseRepository;
    @Override
    public CommonResult getCommonQuestion(Integer page,Integer size) {
        Sort.Direction sort =  Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, sort, "createDate");
        Page<KnowledgeBaseEntity> pages = knowledgeBaseRepository.findAll(pageable);
        HashMap<String,Object> pageMap = new HashMap<String,Object>();
        pageMap.put("totalPage",pages.getTotalPages());
        pageMap.put("content", pages.getContent());
        return new CommonResult().success(pageMap);
    }
}
