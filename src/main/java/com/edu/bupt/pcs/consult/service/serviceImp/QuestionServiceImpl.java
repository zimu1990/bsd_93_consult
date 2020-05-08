package com.edu.bupt.pcs.consult.service.serviceImp;

import cn.edu.bupt.pcsmavenjpa.entity.*;
import cn.edu.bupt.pcsmavenoss.OSSUtils;
import com.edu.bupt.pcs.consult.dto.*;
import com.edu.bupt.pcs.consult.feign.CounselorTypeFeign;
import com.edu.bupt.pcs.consult.repository.*;
import com.edu.bupt.pcs.consult.service.QuestionService;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.awt.print.Book;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: wzz
 * @date: 19-6-13 下午9:02
 * @description
 */
@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionCounselorRepository questionCounselorRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private CounselorRepository counselorRepository;
    @Autowired
    private TesteeRepository testeeRepository;
    @Autowired
    private CounselorTypeFeign counselorTypeFeign;
    @Autowired
    private TypeRepository typeRepository;

    /**
     * 发帖留言
     *
     * @param questionDTO
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    @Override
    public CommonResult addQuestion(QuestionDTO questionDTO) {
        QuestionEntity questionEntity = new QuestionEntity();
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        questionEntity.setTitle(questionDTO.getTitle());
        questionEntity.setContent(questionDTO.getContent());
        questionEntity.setType(new Integer(questionDTO.getType()));
        questionEntity.setIsAnonymous(questionDTO.getIsAnonymous());
        questionEntity.setIsPublic(questionDTO.getIsPublic());
        questionEntity.setCreateTime(timestamp);
        questionEntity.setTesteeId(questionDTO.getTesteeId());
        questionEntity.setStatus(1);
        questionEntity.setScore(20L);
        QuestionEntity questionEntity1 = questionRepository.save(questionEntity);
        if (questionDTO.getIsPublic().equals("false")) {
            ArrayList<QuestionCounselorEntity> questionCounselorEntityArrayList = new ArrayList<QuestionCounselorEntity>();
            //counselorId的数据格式是["1,2","1,23","12,22] 由于前端组件问题传过来的数据类型是字符串数组，前者是类别id，后者是咨询师id
            for (String counselorId : questionDTO.getCounselorId()) {
                QuestionCounselorEntity questionCounselorEntity = new QuestionCounselorEntity();
                questionCounselorEntity.setQuestionId(questionEntity1.getId());
                Integer index = counselorId.indexOf(',');
                log.info("咨询师id"+new Integer(counselorId.substring(index+1)));
                questionCounselorEntity.setCounselorId(new Integer(counselorId.substring(index+1)));
                questionCounselorEntityArrayList.add(questionCounselorEntity);

            }
            questionCounselorRepository.saveAll(questionCounselorEntityArrayList);
        }
        return new CommonResult().success("成功");
    }

    /**
     * 咨询师回答问题
     *
     * @param replyEntity
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    @Override
    public CommonResult addReply(ReplyEntity replyEntity) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        replyEntity.setCreateTime(timestamp);
        return new CommonResult().success(replyRepository.save(replyEntity));
    }

    /**
     * 回答问题下的回复
     *
     * @param answerEntity
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    @Override
    public CommonResult addAnswer(AnswerEntity answerEntity) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        answerEntity.setCreateTime(timestamp);
        answerRepository.save(answerEntity);
        return new CommonResult().success("成功");
    }

    /**
     * 获取被指定咨询师下的问题
     *
     * @param counselorId
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    @Override
    public CommonResult getAppointQuestions(Integer counselorId) {
        ArrayList<QuestionCounselorEntity> questionCounselorArrayList = questionCounselorRepository.findByCounselorId(counselorId);
        ArrayList<String> questionIdList = new ArrayList<>();
        for (QuestionCounselorEntity questionCounselorEntity : questionCounselorArrayList) {
            questionIdList.add(questionCounselorEntity.getQuestionId());
        }
        System.out.println(questionIdList);
        List<QuestionEntity> questionEntities = questionRepository.findAllByIdInOrderByCreateTimeDesc(questionIdList);
        List<QuestionDTO2> questionDTO2s = translateQuestionDTO2(questionEntities);
        return new CommonResult().success(questionDTO2s);
    }

//    获取问题列表


    @Override
    public Page<QuestionDTO2> getQuestionPage(QueryQuestionDTO queryQuestionDTO) {

        Specification<QuestionEntity> specification = (Specification<QuestionEntity>)(root, query, cb) ->{
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotBlank(queryQuestionDTO.getTitle())){
                Predicate title = cb.like(root.get("title").as(String.class), "%"+queryQuestionDTO.getTitle()+"%");
                predicates.add(title);
            }
            if(StringUtils.isNotBlank(queryQuestionDTO.getContent())){
                Predicate content = cb.like(root.get("content").as(String.class), "%"+queryQuestionDTO.getContent()+"%");
                predicates.add(content);
            }
            Predicate status = cb.equal(root.get("status").as(Integer.class), 1);
            predicates.add(status);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(queryQuestionDTO.getPage() - 1, queryQuestionDTO.getSize(), Sort.Direction.DESC, "createTime");
        Page<QuestionEntity> questionEntities = questionRepository.findAll(specification,pageable);
        List<QuestionDTO2> list = translateQuestionDTO2(questionEntities.getContent());
        return new PageImpl<>(list, pageable, questionEntities.getTotalElements());

    }

    /**
     * 获取问题下的答案
     *
     * @param questionId    问题ID
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    @Override
    public CommonResult getAnswers(String questionId) {

        List<AnswerEntity> answerEntityList = answerRepository.findByQuestionIdOrderByCreateTimeDesc(questionId);
        System.out.println(answerEntityList);
        //过滤掉脏数据
        answerEntityList = answerEntityList.stream().filter(answerEntity -> answerEntity.getResponderId() != null
                && StringUtils.isNotBlank(answerEntity.getQuestionId()) && StringUtils.isNotBlank(answerEntity.getResponderType())).collect(Collectors.toList());
        List<AnswerDTO> answerDTOList = new ArrayList<>();
        for (AnswerEntity answerEntity : answerEntityList) {
            AnswerDTO answerDTO = new AnswerDTO();
            CounselorEntity counselorEntity;
            Optional<CounselorEntity> optional = counselorRepository.findById(answerEntity.getResponderId());
            if(!optional.isPresent())
                continue;
            counselorEntity = optional.get();
            answerDTO.setId(answerEntity.getId());
            answerDTO.setAvatar(OSSUtils.getUrl(counselorEntity.getAvatar(), 5));
            answerDTO.setContent(answerEntity.getContent());
            answerDTO.setCreateTime(answerEntity.getCreateTime());
            answerDTO.setTruename(counselorEntity.getTrueName());
            answerDTO.setResponderId(answerEntity.getResponderId());
            answerDTO.setResponderType(answerEntity.getResponderType());
            answerDTO.setQuestionId(answerEntity.getQuestionId());
            answerDTOList.add(answerDTO);
        }
        return new CommonResult().success(answerDTOList);
    }

    /**
     * 获取问题下的回复
     *
     * @param answerId  问题回复ID
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    @Override
    public CommonResult getReplies(String answerId) {

        List<ReplyEntity> replyEntityList = replyRepository.findAllByAnswerIdOrderByCreateTimeDesc(answerId);
        List<ReplyDto> replyDtoList = new ArrayList<>();
        //过滤掉脏数据
        replyEntityList = replyEntityList.stream().filter(replyEntity -> replyEntity.getAnswerId() != null && replyEntity.getResponderId() != null &&
                StringUtils.isNotBlank(replyEntity.getResponderType())).collect(Collectors.toList());
        for (ReplyEntity replyEntity : replyEntityList) {
            ReplyDto replyDto = new ReplyDto();
            Integer responderId = replyEntity.getResponderId();
            String responderType = replyEntity.getResponderType();

            if ("咨询者".equals(responderType)) {
                QuestionEntity questionRepositoryOne = questionRepository.getOne(answerRepository.getOne(replyEntity.getAnswerId()).getQuestionId());
                //1代表匿名用户
                if (questionRepositoryOne.getIsAnonymous().equals(1)) {
                    replyDto.setTruename("匿名用户");
                } else {
                    Optional<TesteeEntity> optionalTesteeEntity = testeeRepository.findById(responderId);
                    optionalTesteeEntity.ifPresent(testeeEntity -> replyDto.setTruename(testeeEntity.getTrueName()));
                }
            } else {
                Optional<CounselorEntity> optionalCounselorEntity = counselorRepository.findById(responderId);
                optionalCounselorEntity.ifPresent(counselorEntity -> replyDto.setTruename(counselorEntity.getTrueName()));
            }

            if(replyDto.getTruename() == null)
                continue;
            replyDto.setId(replyEntity.getId());
            replyDto.setAnswerId(replyEntity.getAnswerId());
            replyDto.setCreateTime(replyEntity.getCreateTime());
            replyDto.setReplyContent(replyEntity.getReplyContent());
            replyDto.setResponderType(replyEntity.getResponderType());
            replyDto.setResponderId(replyEntity.getResponderId());
            replyDtoList.add(replyDto);
        }
        return new CommonResult().success(replyDtoList);
    }

    /**
     * 被试者获取全部问题
     *
     * @param  testeeId 被试用户ID
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    @Override
    public CommonResult getPublicQuestions(Integer testeeId) {

        //List<QuestionEntity> questionEntityList = questionRepository.findByTesteeIdOrIsPublicOrderByCreateTimeDesc(testeeId, "true");
        List<QuestionEntity> questionEntityList = questionRepository.findByTesteeIdOrderByCreateTimeDesc(testeeId);
        List<QuestionDTO2> questionDTO2s = translateQuestionDTO2(questionEntityList);
        return new CommonResult().success(questionDTO2s);
        // return null;
    }

    @Override
    public CommonResult getPublicQuestions2(Integer pageNum, Integer pageSize) {
        List<Object> objectList = questionRepository.findAllOrderByCreateTimeDesc();
        List<QuestionDTO> questionDTOs=new ArrayList<>();
        for(Object o: objectList){
            Object[] obj= (Object[]) o;
            QuestionDTO dto = new QuestionDTO();
            dto.setId((String) obj[0]);
            dto.setTitle((String) obj[1]);
            dto.setContent((String) obj[2]);
            String type = null;
            Optional<DictDetailEntity> dictDetailEntity = typeRepository.findById((Integer) obj[3]);
            if(dictDetailEntity.isPresent())
                type = dictDetailEntity.get().getLabel();
            dto.setType(type);
            dto.setTesteeId((Integer) obj[4]);
            Integer isAnonymous = (Integer) obj[7];
            if(isAnonymous == null || isAnonymous == 1){
                dto.setTruename("匿名用户");
            } else {
                Optional<TesteeEntity> testeeEntity = testeeRepository.findById(dto.getTesteeId());
                if(testeeEntity.isPresent())
                    dto.setTruename((String) obj[5]);
                else
                    dto.setTruename("未知用户");
            }
            dto.setImg_url((String) obj[6]);
            dto.setIsAnonymous((Integer) obj[7]);
            dto.setIsPublic((String) obj[8]);
            dto.setCreateTime((Timestamp) obj[9]);
            dto.setScore((BigInteger) obj[10]);
            dto.setStatus((Integer) obj[11]);
            dto.setUpdateTime((Timestamp) obj[12]);
            questionDTOs.add(dto);
        }
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        int totalElements =questionDTOs.size();
        int fromIndex = pageable.getPageSize()*pageable.getPageNumber();
        int toIndex = pageable.getPageSize()*(pageable.getPageNumber()+1);
        if(toIndex>totalElements) toIndex = totalElements;
        List<QuestionDTO> indexObjects = questionDTOs.subList(fromIndex,toIndex);
        Page<QuestionDTO> questionDTOsPage=new PageImpl<>(indexObjects, pageable ,totalElements);
        return new CommonResult().success(questionDTOsPage);
    }

    @Override
    public CommonResult getQuestionType() {
        return new CommonResult().success(counselorTypeFeign.getCounselorType("擅长领域"));
    }

    /**
     * 设置问题的下线
     * @param questionId 问题ID
     * @return  更改信息
     */
    @Transactional
    @Override
    public CommonResult updateQuestionStatus(String questionId) {
        Optional<QuestionEntity> question = questionRepository.findById(questionId);
        if(question.isPresent()){
            QuestionEntity entity = question.get();
            if(entity.getStatus() != null && entity.getStatus() == 1){
                entity.setStatus(0);
                questionRepository.save(entity);
                return new CommonResult().success("问题已下线");
            }
            else
                return new CommonResult().success("问题未上线");
        }
        else
            return new CommonResult().failed("问题不存在");
    }

    public List<QuestionDTO2> translateQuestionDTO2(List<QuestionEntity> questionEntities) {

        List<QuestionDTO2> questionDTO2s = new ArrayList<>();
        for (QuestionEntity questionEntity : questionEntities) {
            if (questionEntity.getStatus() == 1){
                QuestionDTO2 questionDTO2 = new QuestionDTO2();

                String truename;
                if (questionEntity.getIsAnonymous() == null || questionEntity.getIsAnonymous() == 1) {
                    truename = "匿名用户";
                } else {
                    Optional<TesteeEntity> testeeEntity = testeeRepository.findById(questionEntity.getTesteeId());
                    if(testeeEntity.isPresent())
                        truename = testeeEntity.get().getTrueName();
                    else
                        truename = "未知用户";
                }
                String type = null;
                Optional<DictDetailEntity> dictDetailEntity = typeRepository.findById(questionEntity.getType());
                if (dictDetailEntity.isPresent())
                    type = dictDetailEntity.get().getLabel();

                questionDTO2.setTruename(truename);
                questionDTO2.setContent(questionEntity.getContent());
                questionDTO2.setCreateTime(questionEntity.getCreateTime());
                questionDTO2.setId(questionEntity.getId());
                questionDTO2.setIsAnonymous(questionEntity.getIsAnonymous());
                questionDTO2.setScore(questionEntity.getScore());
                questionDTO2.setIsPublic(questionEntity.getIsPublic());
                questionDTO2.setType(type);
                questionDTO2.setTitle(questionEntity.getTitle());
                questionDTO2.setTesteeId(questionEntity.getTesteeId());
                questionDTO2s.add(questionDTO2);
            }

        }
        return questionDTO2s;
    }

}
