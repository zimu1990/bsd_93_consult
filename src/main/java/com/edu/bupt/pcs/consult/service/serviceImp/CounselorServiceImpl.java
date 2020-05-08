package com.edu.bupt.pcs.consult.service.serviceImp;

import cn.edu.bupt.pcsmavenjpa.entity.*;
import cn.edu.bupt.pcsmavenoss.OSSUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edu.bupt.pcs.consult.dto.*;
import com.edu.bupt.pcs.consult.feign.CounselorTypeFeign;
import com.edu.bupt.pcs.consult.repository.*;
import com.edu.bupt.pcs.consult.service.CounselorService;
import com.edu.bupt.pcs.consult.service.MessageService;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import com.edu.bupt.pcs.consult.utils.StatusUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * @author: wzz
 * @date: 19-6-12 下午2:33
 * @description
 */
@Service
@Slf4j
public class CounselorServiceImpl implements CounselorService {
    @Autowired
    public CounselorRepository counselorRepository;
    @Autowired
    private CounselorTypeFeign counselorTypeFeign;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private CounselorDictDetailRepository counselorDictDetailRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private CounselorUpdateApplicationRepository counselorUpdateApplicationRepository;
    @Autowired
    private CounselorConfigRepository counselorConfigRepository;
    @Autowired
    private TesteeRepository testeeRepository;
    @Autowired
    private CounselorScheduleRepository counselorScheduleRepository;
    @Autowired
    private ReserveRecordRepository reserveRecordRepository;
    @Autowired
    private DictDetailRepository dictDetailRepository;
    @Autowired
    private ChatSessionRepository chatSessionRepository;
    @Autowired
    private ChatRecordRepository chatRecordRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private MessageService messageService;

    /**
     * 门户端咨询师自行注册
     * @param phoneNumber
     * @param password
     * @param inputCode
     * @return
     */
    @Override
    public CommonResult registerCounselor(String phoneNumber, String password, String inputCode) {
        CounselorEntity counselorEntity = new CounselorEntity();
        if (counselorRepository.findByPhoneNumber(phoneNumber) != null) {
            return new CommonResult().failed("你已注册");
        }
        log.info("验证码是否不正确"+String.valueOf(messageService.compareInputCodeWithVerification(phoneNumber,inputCode)));
        int flag = messageService.compareInputCodeWithVerification(phoneNumber,inputCode);
        if (flag == 0){
            return new CommonResult().failed("验证码不正确");
        }else if(flag == 2){
            return new CommonResult().failed("验证码已失效");
        }
        Date date = new Date();
        //得到一个timestamp格式的时间，存入mysql中的时间格式为"yyyy-MM-dd HH:mm:ss"
        Timestamp timestamp = new Timestamp(date.getTime());
        counselorEntity.setPhoneNumber(phoneNumber);
        counselorEntity.setPassword(new BCryptPasswordEncoder().encode(password));
        counselorEntity.setUsername(phoneNumber);
//        counselorEntity.setStatus("未审核");
        counselorEntity.setStatus("未提交");
        counselorEntity.setUnitId(1);
        counselorEntity.setScore(5.0);
        counselorEntity.setCreateTime(timestamp);
        counselorRepository.save(counselorEntity);
        return new CommonResult().success("注册成功");
    }

    @Override
    public CommonResult getCounselorInfo(Integer counselorId) {
        return new CommonResult().success(counselorRepository.findById(counselorId));
    }

    /**
     * 获取已审核的咨询师列表
     * @return  审核通过的咨询师列表信息
     */
    @Override
    public CommonResult getCounselorList(Integer pageNum, Integer pageSize) {

        List<CounselorEntity> counselorEntities = counselorRepository.findByStatus("审核通过");
        List<CounselorDTO> counselorDTOList = new ArrayList<>();
        for (CounselorEntity counselorEntity : counselorEntities) {
            CounselorDTO counselorDTO = new CounselorDTO();
            counselorDTO.setId(counselorEntity.getId());
            counselorDTO.setGender(counselorEntity.getGender());
            counselorDTO.setTruename(counselorEntity.getTrueName());

            List<CounselorDictDetailEntity> byCounselorId = counselorDictDetailRepository.findByCounselorId(counselorEntity.getId());
            List<Integer> types = byCounselorId.stream().map(CounselorDictDetailEntity::getTypeId).collect(Collectors.toList());
            List<DictDetailEntity> allById = typeRepository.findByIdIn(types);
            log.info("获取类型信息：" + allById.stream().map(DictDetailEntity::getLabel).collect(Collectors.toList()));
            counselorDTO.setAreasOfExpertiseLists(allById.stream().map(DictDetailEntity::getLabel).collect(Collectors.toList()));
            counselorDTO.setAvatar((counselorEntity.getAvatar() != null ) ? OSSUtils.getUrl(counselorEntity.getAvatar(), 5) : null);
            counselorDTO.setBirthday(counselorEntity.getBirthday());
            counselorDTO.setEmail(counselorEntity.getEmail());
            counselorDTO.setSpecialty(counselorEntity.getSpecialty());
            counselorDTO.setWorkingExperience(counselorEntity.getWorkingExperience());
            counselorDTO.setScore(counselorEntity.getScore());
            counselorDTO.setPhoneNumber(counselorEntity.getPhoneNumber());
            counselorDTO.setInformation(counselorEntity.getInformation());
            counselorDTO.setUsername(counselorEntity.getUsername());
            counselorDTOList.add(counselorDTO);
        }
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        int totalElements =counselorDTOList.size();
        int fromIndex = pageable.getPageSize()*pageable.getPageNumber();
        int toIndex = pageable.getPageSize()*(pageable.getPageNumber()+1);
        if(toIndex>totalElements) toIndex = totalElements;
        List<CounselorDTO> indexObjects = counselorDTOList.subList(fromIndex,toIndex);
        Page<CounselorDTO> questionDTOsPage=new PageImpl<>(indexObjects, pageable ,totalElements);
        return new CommonResult().success(questionDTOsPage);
    }

    @Override
    public CommonResult getCounselorType() {
        return new CommonResult().success(counselorTypeFeign.getCounselorType("擅长领域"));
    }

    @Override
    public CommonResult getCounselorByType(Integer typeId) {
        List<CounselorDictDetailEntity> counselorDictDetailEntities = counselorDictDetailRepository.findByTypeId(typeId);
        //根据类型查看审核通过咨询师列表
        List<CounselorEntity> counselorEntityList = counselorRepository.findByIdInAndStatus(counselorDictDetailEntities.stream().map(CounselorDictDetailEntity::getCounselorId).collect(Collectors.toList()), "审核通过");
        for (CounselorEntity counselorEntity : counselorEntityList) {
            counselorEntity.setAvatar(counselorEntity.getAvatar() != null ? OSSUtils.getUrl(counselorEntity.getAvatar(),3) : null);
        }
        return new CommonResult().success(getCounselorDTOList(counselorEntityList));
    }

    @Override
    public CommonResult addFeedback(FeedbackEntity feedbackEntity) {
        Date date = new Date();
        //得到一个timestamp格式的时间，存入mysql中的时间格式为"yyyy-MM-dd HH:mm:ss"
        Timestamp timestamp = new Timestamp(date.getTime());
        feedbackEntity.setCreateTime(timestamp);
        feedbackRepository.save(feedbackEntity);
        return new CommonResult().success("提交成功");
    }

    /**
     * 获取意见反馈列表
     * @param queryFeedbackDto  查询请求的参数，包含内容，用户类型，起止时间以及分页参数
     * @return  意见反馈的分页列表
     */
    @Override
    public Page<FeedbackDto> getFeedbackList(QueryFeedbackDto queryFeedbackDto) {

        Specification<FeedbackEntity> specification = (Specification<FeedbackEntity>)(root, query, cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotBlank(queryFeedbackDto.getContent())){
                Predicate content = cb.like(root.get("content").as(String.class), "%"+queryFeedbackDto.getContent()+"%");
                predicates.add(content);
            }
            if(StringUtils.isNotBlank(queryFeedbackDto.getType())){
                Predicate type = cb.equal(root.get("createType").as(String.class), queryFeedbackDto.getType());
                predicates.add(type);
            }
            if (queryFeedbackDto.getStartTime() != null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class),queryFeedbackDto.getStartTime()));
            }
            if (queryFeedbackDto.getEndTime() != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class),queryFeedbackDto.getEndTime()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        //添加分页
        Pageable pageable = PageRequest.of(queryFeedbackDto.getPage() - 1, queryFeedbackDto.getSize(), Direction.DESC, "createTime");
        Page<FeedbackEntity> feedbackDtos = feedbackRepository.findAll(specification, pageable);

        //获取意见反馈者的用户名
        List<FeedbackDto> list = feedbackDtos.getContent().stream().map(feedbackItem -> {
            FeedbackDto feedbackDto = new FeedbackDto(feedbackItem);
            if("咨询者".equals(feedbackItem.getCreateType())){
                Optional<TesteeEntity> testeeEntity = testeeRepository.findById(feedbackItem.getCreaterId());
                testeeEntity.ifPresent(it -> {JSONObject jsonObject = JSON.parseObject(it.getUsername());
                String username = jsonObject.getString("name");
                    feedbackDto.setUsername(username);});
            }

            else if("咨询师".equals(feedbackItem.getCreateType())){
                Optional<CounselorEntity> counselorEntity = counselorRepository.findById(feedbackItem.getCreaterId());
                counselorEntity.ifPresent(it -> feedbackDto.setUsername(it.getUsername()));
            }
            return feedbackDto;
        }).collect(toList());

        return new PageImpl<>(list, pageable, feedbackDtos.getTotalElements());
    }

    public List<CounselorDTO> getCounselorDTOList(List<CounselorEntity> counselorEntityList) {
        List<CounselorDTO> counselorDTOList = new ArrayList<>();
        for (CounselorEntity counselorEntity : counselorEntityList) {
            CounselorDTO counselorDTO = new CounselorDTO();
            counselorDTO.setAvatar(counselorEntity.getAvatar());
            counselorDTO.setBirthday(counselorEntity.getBirthday());
            counselorDTO.setEmail(counselorEntity.getEmail());
            counselorDTO.setGender(counselorEntity.getGender());
            counselorDTO.setId(counselorEntity.getId());
            counselorDTO.setPhoneNumber(counselorEntity.getPhoneNumber());
            counselorDTO.setSpecialty(counselorEntity.getSpecialty());
            counselorDTO.setTruename(counselorEntity.getTrueName());
            counselorDTO.setWorkingExperience(counselorEntity.getWorkingExperience());
            counselorDTO.setInformation(counselorEntity.getInformation());
            counselorDTOList.add(counselorDTO);
        }
        return counselorDTOList;

    }

    @Override
    public Object submitAudite(ConselorDTO2 conselorDTO2) {

        CounselorEntity counselor = counselorRepository.getOne(conselorDTO2.getId());

        //conselorDTO2.setStatus("审核通过");
        String nowStatus = counselor.getStatus();
        conselorDTO2.setCreateTime(counselor.getCreateTime());
        conselorDTO2.setUsername(counselor.getUsername());
        conselorDTO2.setPhoneNumber(counselor.getPhoneNumber());
        conselorDTO2.setPassword(counselor.getPassword());
        //第一次提交改成待复审
        conselorDTO2.setStatus("待审核");
        conselorDTO2.setAuditTime(counselor.getAuditTime());
        conselorDTO2.setAuditFeedback(counselor.getAuditFeedback());
        CounselorEntity counselorEntity = new CounselorEntity();
        if(nowStatus.equals("审核不通过")){
            conselorDTO2.setStatus("待复审");
        }
        BeanUtils.copyProperties(conselorDTO2, counselorEntity);
        counselorRepository.save(counselorEntity);
        conselorDTO2.getTypes().forEach(c -> counselorDictDetailRepository.save(new CounselorDictDetailEntity(counselor.getId(), c)));
        return new CommonResult().success("提交成功！");
    }

    /**
     * 获取可查询的所有的咨询师数据
     * @param queryCounselorDTO 查询条件，包括姓名，擅长领域，分页规格
     * @return  咨询师列表
     */
    @Override
    public Page<CounselorListDTO> getCounselorPage(QueryCounselorDTO queryCounselorDTO, List<Integer> unitIds) {
        Specification<CounselorEntity> specification = (Specification<CounselorEntity>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>(); //所有的断言
            if (StringUtils.isNotBlank(queryCounselorDTO.getUsername())) { //添加断言
                Predicate userName = cb.equal(root.get("trueName").as(String.class), queryCounselorDTO.getUsername());
                predicates.add(userName);
            }
            if(unitIds.size() == 1)
                predicates.add(cb.equal(root.get("unitId").as(Integer.class), unitIds.get(0)));
            else if(unitIds.size() > 1){
                CriteriaBuilder.In<Object> in = cb.in(root.get("unitId").as(Integer.class));
                unitIds.forEach(in :: value);
                predicates.add(cb.and(in));
            }

            query.orderBy(cb.desc(root.get("createTime")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        List<CounselorEntity> counselorEntityList = this.counselorRepository.findAll(specification);
        List<CounselorListDTO> list;
        if (queryCounselorDTO.getSpecialty()!=null) {
            list = counselorEntityList.stream().map(counselorEntity -> {
                List<CounselorDictDetailEntity> counselorDictDetailEntityList = counselorDictDetailRepository.findByCounselorId(counselorEntity.getId());
                List<Integer> typeList = counselorDictDetailEntityList.stream().map(CounselorDictDetailEntity::getTypeId).collect(Collectors.toList());
                List<String> newTypeList = typeList.stream().map(id -> {
                    DictDetailEntity dictDetailEntity = typeRepository.getOne(id);
                    return dictDetailEntity.getLabel();
                }).collect(toList());
                if (!typeList.contains(queryCounselorDTO.getSpecialty())) {
                    return new CounselorListDTO();
                }
                return new CounselorListDTO(counselorEntity, counselorEntity.getAvatar() != null ? OSSUtils.getUrl(counselorEntity.getAvatar(), 3) : null, counselorEntity.getCertificate() != null ? OSSUtils.getUrl(counselorEntity.getCertificate(), 3) : null,
                        counselorEntity.getIdImage1() != null ? OSSUtils.getUrl(counselorEntity.getIdImage1(), 3) : null, counselorEntity.getIdImage2() != null ? OSSUtils.getUrl(counselorEntity.getIdImage2(), 3) : null, newTypeList);
            }).filter(counselorListDTO -> counselorListDTO.getCounselorEntity() != null).collect(toList());
        } else {
            list = counselorEntityList.stream().map(counselorEntity -> {
                List<CounselorDictDetailEntity> counselorDictDetailEntityList = counselorDictDetailRepository.findByCounselorId(counselorEntity.getId());
                List<Integer> typeList = counselorDictDetailEntityList.stream().map(CounselorDictDetailEntity::getTypeId).collect(Collectors.toList());
                List<String> newTypeList = typeList.stream().map(id -> {
                    DictDetailEntity dictDetailEntity = typeRepository.getOne(id);
                    return dictDetailEntity.getLabel();
                }).collect(toList());
                return new CounselorListDTO(counselorEntity, counselorEntity.getAvatar() != null ? OSSUtils.getUrl(counselorEntity.getAvatar(), 3) : null, counselorEntity.getCertificate() != null ? OSSUtils.getUrl(counselorEntity.getCertificate(), 3) : null,
                        counselorEntity.getIdImage1() != null ? OSSUtils.getUrl(counselorEntity.getIdImage1(), 3) : null, counselorEntity.getIdImage2() != null ? OSSUtils.getUrl(counselorEntity.getIdImage2(), 3) : null, newTypeList);
            }).collect(toList());
        }
        Pageable pageable = PageRequest.of(queryCounselorDTO.getPage() - 1, queryCounselorDTO.getSize());
        int start = (int) pageable.getOffset();
        // 当前页最后一条数据在List中的位置
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }


    @Override
    public CommonResult modifyCounselor(Integer id) {
        CounselorEntity counselorEntity = counselorRepository.getOne(id);
        counselorEntity.setStatus("废除");
        counselorRepository.save(counselorEntity);
        return new CommonResult().success("提交成功");
    }

    @Override
    public CommonResult getAuditInfo(Integer page, Integer size) {
        Sort.Direction sort = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(0, 5, sort, "applyTime");
        Page<CounselorUpdateApplicationEntity> all = counselorUpdateApplicationRepository.findAll(pageable);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", all.getTotalPages());
        map.put("content", all.getContent());
        //System.out.println(all.getTotalPages());
        //System.out.println(all.getTotalElements());
        //System.out.println(all.getContent());
        return new CommonResult().success(map);
    }

    /**
     * 获取咨询过程列表
     * @param processDto    查询条件实体类
     * @return  分页后的咨询过程实体类
     */
    @Override
    public Page<ConsultProcessDto> getProcessList(QueryProcessDto processDto) {
        List<CounselorEntity> counselors;
        List<CounselorScheduleEntity> scheduleList;
        List<Integer> userIdList = new ArrayList<>();
        List<Integer> scheduleIdList = new ArrayList<>();
        /*查询processDto中的被试用户和咨询师是否存在。*/
        if(StringUtils.isNotBlank(processDto.getUsername())) {
            List<TesteeEntity> list = testeeRepository.findByTrueName(processDto.getUsername());
            userIdList = list.stream().map(TesteeEntity::getId).collect(toList());
            if(userIdList.isEmpty())
                return new PageImpl<>(new ArrayList<>());
        }
        if(StringUtils.isNotBlank(processDto.getCounselor())){
            counselors = counselorRepository.findByTrueName(processDto.getCounselor());
            List<Integer> cIdlist = counselors.stream().map(CounselorEntity::getId).collect(toList());
            if(!cIdlist.isEmpty()){
                scheduleList = counselorScheduleRepository.findByCounselorIdIn(cIdlist);
                //咨询师预约日程的ID列表，空则返回空
                scheduleIdList= scheduleList.stream().map(CounselorScheduleEntity::getId).collect(toList());
                if(scheduleIdList.isEmpty())
                    return new PageImpl<>(new ArrayList<>());
            }
            else
                return new PageImpl<>(new ArrayList<>());
        }

        List<Integer> userIds = userIdList;
        List<Integer> scheduleIds = scheduleIdList;
        /*添加查询断言*/
        Specification<ReserveRecordEntity> specification = (Specification<ReserveRecordEntity>) (root, query, cb) ->{
            List<Predicate> predicates = new ArrayList<>();
            if(!userIds.isEmpty()){
                CriteriaBuilder.In<Object> in = cb.in(root.get("userId"));
                userIds.forEach(in::value);
                predicates.add(cb.and(in));
            }
            if(!scheduleIds.isEmpty()){
                CriteriaBuilder.In<Object> in = cb.in(root.get("counselorScheduleId"));
                scheduleIds.forEach(in::value);
                predicates.add(cb.and(in));
            }
            if(processDto.getStartTime() != null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("chatStartTime").as(Date.class), processDto.getStartTime()));
            }
            if(processDto.getEndTime() != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("chatEndTime").as(Date.class), processDto.getEndTime()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(processDto.getPage() - 1, processDto.getSize(), Direction.DESC, "createTime");
        Page<ReserveRecordEntity> recordEntities = reserveRecordRepository.findAll(specification, pageable);
        /*装饰ConsultProcessDto实体类*/
        List<ConsultProcessDto> list = recordEntities.getContent().stream().map(recordEntity -> {
            long appointmentDuration = 0;
            Optional<TesteeEntity> testeeEntity = testeeRepository.findById(recordEntity.getUserId());
            Optional<CounselorScheduleEntity> scheduleEntity =
                    counselorScheduleRepository.findById(recordEntity.getCounselorScheduleId());
            Optional<CounselorEntity> counselorEntity = Optional.empty();
            if(scheduleEntity.isPresent()){
                CounselorScheduleEntity counselorScheduleEntity = scheduleEntity.get();
                counselorEntity = counselorRepository.findById(counselorScheduleEntity.getCounselorId());
                appointmentDuration = (counselorScheduleEntity.getEndTime().getTime()
                        - counselorScheduleEntity.getStartTime().getTime())/(1000*60);
            }
            long actualDuration = 0;
            if(recordEntity.getChatStartTime() != null && recordEntity.getChatEndTime() != null){
                actualDuration = (recordEntity.getChatEndTime().getTime()
                        - recordEntity.getChatStartTime().getTime())/(1000*60);
            }
            //解析JSON格式的数据，并赋值给EvaluationDto类对象
            EvaluationDto evaluationDto = null;
            if(StringUtils.isNotBlank(recordEntity.getEvaluation())){
                JSONObject jsonObject = JSON.parseObject(recordEntity.getEvaluation());
                String evaluation = jsonObject.getString("evaluationJson");
                JSONObject jsonEvaluation = JSON.parseObject(evaluation);
                evaluationDto = new EvaluationDto(jsonEvaluation.getString("professional"),
                        jsonEvaluation.getString("attitude"),jsonEvaluation.getString("overall"),
                        jsonEvaluation.getString("isOnTime"),jsonEvaluation.getString("isWillNext"));
            }
            
            if(counselorEntity!=null && counselorEntity.isPresent() && testeeEntity.isPresent())
                return new ConsultProcessDto(recordEntity.getId(), counselorEntity.get().getTrueName(), testeeEntity.get().getTrueName(),
                        recordEntity.getChatStartTime(), recordEntity.getChatEndTime(), appointmentDuration,
                        actualDuration, evaluationDto, new StatusUtil().getReserveStatus(recordEntity.getStatus()));
            else
                return new ConsultProcessDto();
        }).collect(toList());

        return new PageImpl<>(list, pageable, recordEntities.getTotalElements());
    }

    /**
     * 获取咨询过程的具体聊天记录
     * @param reserveId 预约ID
     * @return  包含聊天记录的String类型字符串
     */
    @Override
    public String getConsultChatRecord(Integer reserveId, String userAgent) {
        List<Integer> list = chatSessionRepository.findAllByPredictionId(reserveId);
        if(list.isEmpty())
            return null;
        List<ChatRecordDto> chatRecordDtos = chatRecordRepository.findContentBySessionId(list);
        //对操作系统进行换行符的适配
        String line = "\r\n";
        if(userAgent.contains("Mac"))
            line = "\r";
        else if(userAgent.contains("Unix") || userAgent.contains("Linux"))
            line = "\n";
        return generateRecord(chatRecordDtos, line);
    }

    /**
     * 获取咨询师预约配置信息
     * 因为数据库只会存在一条配置信息，所以返回List的第一个元素
     * @return  咨询师预约配置信息实体类
     */
    @Override
    public CounselorConfigEntity getConfiguration() {
        List<CounselorConfigEntity> counselorConfigEntities = counselorConfigRepository.findAll();
        return counselorConfigEntities.isEmpty() ? null : counselorConfigEntities.get(0);
    }

    /**
     * 更新咨询师预约配置信息
     * @param entity  最新的预约配置信息
     * @return  成功返回0
     */
    @Transactional
    @Override
    public int updateConfiguration(CounselorConfigEntity entity) {
        /*校验参数范围是否规范*/
        entity.setId(1);
        if(entity.getPerdayNumber() <= 0 || entity.getPerdayNumber() > 20)
            entity.setPerdayNumber(20);
        if(entity.getCancelLimit() <= 0 || entity.getCancelLimit() > 10)
            entity.setCancelLimit(10);
        if(entity.getCancelPunish() <= 0 || entity.getCancelPunish() > 21)
            entity.setCancelPunish(21);
        if(entity.getSanction() <= 0 || entity.getSanction() > 21)
            entity.setSanction(21);
        if(entity.getDuration() <= 0 || entity.getDuration() > 21)
            entity.setDuration(7);
        counselorConfigRepository.save(entity);
        return 0;
    }

    @Override
    public List<SpecialtyDto> getSpecialtyList(int dictId) {
        return dictDetailRepository.findSpecialtyByDictId(dictId);
    }

    @Override
    public Object updateConselorPass(ConselorDTO2 conselorDTO2) {
        CounselorEntity counselor = counselorRepository.getOne(conselorDTO2.getId());

        String oldPassword = counselor.getPassword();
        String newPassword = conselorDTO2.getPassword();
        String inputOldPassword = conselorDTO2.getOldpassword();
        boolean flag = new BCryptPasswordEncoder().matches(newPassword, oldPassword);
        boolean flagofOldPass = new BCryptPasswordEncoder().matches(inputOldPassword, oldPassword);
        if(!flagofOldPass){
            return new CommonResult().failed("旧密码输入错误");
        }
        if(flag){
            return new CommonResult().failed("新密码不能和旧密码相同");
        }else{
            counselor.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            counselorRepository.save(counselor);
            return new CommonResult().success("修改密码成功！");
        }
    }

    /**
     * 将数据库数据转换成字符串
     * @param list  数据库聊天记录信息数组
     * @return  生成的字符串
     */
    private String generateRecord(List<ChatRecordDto> list, String line){
        StringBuffer str = new StringBuffer();
        list.forEach(chatRecordDto -> {
            StringBuilder buffer = new StringBuilder();
            if(StringUtils.isBlank(chatRecordDto.getSenderName()) || StringUtils.isBlank(chatRecordDto.getReceiverName())){
                String counselorName = null, testeeName = null;
                Optional<ChatSessionEntity> optional = chatSessionRepository.findById(chatRecordDto.getSessionId());
                ChatSessionEntity session = new ChatSessionEntity();
                if(optional.isPresent())
                    session = optional.get();
                if(StringUtils.isBlank(chatRecordDto.getSenderName())){
                    counselorName = counselorRepository.findTrueNameById(session.getStaffId());
                }
                if(StringUtils.isBlank(chatRecordDto.getReceiverName())){
                    testeeName = testeeRepository.findTrueNameById(session.getUserId());
                }
                if(chatRecordDto.getDirectionId().equals(0)){
                    chatRecordDto.setSenderName(testeeName);
                    chatRecordDto.setReceiverName(counselorName);
                }else{
                    chatRecordDto.setSenderName(counselorName);
                    chatRecordDto.setReceiverName(testeeName);
                }
            }
            String senderRole;
            String receiverRole;
            if(chatRecordDto.getDirectionId().equals(0)){
                senderRole = "咨询者";
                receiverRole = "咨询师";
            }else{
                senderRole = "咨询师";
                receiverRole = "咨询者";
            }
            buffer.append(chatRecordDto.getSenderName());
            buffer.append("(");
            buffer.append(senderRole);
            buffer.append(")");
            buffer.append(" 发送给 ");
            buffer.append(chatRecordDto.getReceiverName());
            buffer.append("(");
            buffer.append(receiverRole);
            buffer.append(") ");
            buffer.append(chatRecordDto.getCreateTime().toString());
            buffer.append(":");
            buffer.append(line);
            buffer.append(chatRecordDto.getContent());
            buffer.append(line);
            str.append(buffer);
        });
        return str.toString();
    }

    /**
     * 根据权限级别获取用户可查询的机构列表
     * @param unitId    用户机构ID
     * @param privilegeLevel    用户权限级别，1 全局 2 同下级 3 同级
     * @return  机构列表
     */
    public List<Integer> getUnitIdList(int unitId, String privilegeLevel){
        List<Integer> unitIdList = new ArrayList<>();
        unitIdList.add(unitId);
        //其他情况一律按照默认同级处理
        if(!StringUtils.isEmpty(privilegeLevel))
            switch (privilegeLevel) {
                case "1":
                    System.out.println("全局");
                    return new ArrayList<>();                                          //全局返回空，代表所有
                case "2":
                    System.out.println("同下级");
                    List<Integer> list =  unitIdList;
                    while (!list.isEmpty()){
                        list = unitRepository.findAllByParentId(list);
                        unitIdList.addAll(list);       //同下级返回同级及其下级机构
                    }
                    break;
                case "3":
                    System.out.println("同级");
                    break;
            }
        return unitIdList;
    }

//    @Override
//    public Page<FeedbackDTO> getFeedBackPage(int pageNum, Integer pageSize,String startTime,String endTime,String content) {
//        Sort sort = new Sort(new Sort.Order(Direction.DESC,"createTime"));
////        PageRequest pageRequest = new PageRequest(pageNum-1,pageSize,sort);
//        Specification<FeedbackEntity> querySpec = new Specification<FeedbackEntity>() {
//            @Override
//            public Predicate toPredicate(Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicates = new ArrayList<>();
//                if (StringUtils.isNotBlank(startTime)){
//                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("create_time"),startTime));
//                }
//                if (StringUtils.isNotBlank(endTime)){
//                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("create_time"),endTime));
//                }
//                if (StringUtils.isNotBlank(content)){
//                    predicates.add(criteriaBuilder.like(root.get("content"),"%"+content+"%"));
//                }
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        };
//        List<FeedbackEntity> feedbackEntityList = feedbackRepository.findAll(querySpec,sort);
//        List<FeedbackDTO> feedbackDTOList = new ArrayList<>();
//        for (FeedbackEntity feedbackEntity : feedbackEntityList) {
//            FeedbackDTO feedbackDTO = new FeedbackDTO();
//            feedbackDTO.setId(feedbackEntity.getId());
//            feedbackDTO.setContent(feedbackEntity.getContent());
//            feedbackDTO.setCreateTime(feedbackEntity.getCreateTime());
//            feedbackDTO.setCreaterId(feedbackEntity.getCreaterId());
//            if (feedbackEntity.getCreateType().equals("咨询者")) {
//                feedbackDTO.setTrueName(testeeRepository.findNameById(feedbackEntity.getCreaterId()));
//            }else if (feedbackEntity.getCreateType().equals("咨询师")) {
//                feedbackDTO.setTrueName(counselorRepository.findTrueNameById(feedbackEntity.getCreaterId()));
//            }
//            feedbackDTOList.add(feedbackDTO);
//        }
//        Pageable pageable = PageRequest.of(pageNum, pageSize);
//        int totalElements =feedbackDTOList.size();
//        int fromIndex = pageable.getPageSize()*pageable.getPageNumber();
//        int toIndex = pageable.getPageSize()*(pageable.getPageNumber()+1);
//        if(toIndex>totalElements) toIndex = totalElements;
//        List<FeedbackDTO> indexObjects = feedbackDTOList.subList(fromIndex,toIndex);
//        Page<FeedbackDTO> feedbackDTOPage =new PageImpl<>(indexObjects, pageable ,totalElements);
//        return feedbackDTOPage;
//    }


    @Override
    public CommonResult resetNumber(String phoneNumber, String newPassword,String inputCode) {
        CounselorEntity counselor = counselorRepository.findByPhoneNumber(phoneNumber);
        System.out.println(counselor);
        if (counselor == null) {
            return new CommonResult().failed("你尚未注册");
        }else{
            log.info("验证码是否不正确"+String.valueOf(messageService.compareInputCodeWithVerification(phoneNumber,inputCode)));
            int flag = messageService.compareInputCodeWithVerification(phoneNumber,inputCode);
            if (flag == 0){
                return new CommonResult().failed("验证码不正确");
            }else if(flag == 2){
                return new CommonResult().failed("验证码已失效");
            }
            String oldPassword = counselor.getPassword();
            log.info("旧密码是"+oldPassword);
//            boolean ifSame = new BCryptPasswordEncoder().matches(newPassword, oldPassword);
//            log.info("ifSame"+ifSame);
//            if(ifSame){
//                return new CommonResult().failed("旧密码不能和新密码相同");
//            }else{
            counselor.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            counselorRepository.save(counselor);
            return new CommonResult().success("修改密码成功");
//            }
        }
    }

    @Override
    public CounselorEntity getCounselorInformation(Integer id) {
        Optional<CounselorEntity> counselorEntityOptional = counselorRepository.findById(id);
        return counselorEntityOptional.orElse(null);
    }
}
