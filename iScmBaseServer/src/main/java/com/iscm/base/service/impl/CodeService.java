package com.iscm.base.service.impl;

import com.iscm.base.dao.CodeDetailDao;
import com.iscm.base.dao.CodeHeaderDao;
import com.iscm.base.redis.RedisTemplate;
import com.iscm.common.constants.RedisConstants;
import com.iscm.common.utils.JSONUtils;
import com.iscm.core.db.service.BaseService;
import com.iscm.dto.IScmCodeDetailDto;
import com.iscm.po.base.IScmCodeDetailEntity;
import com.iscm.po.base.IScmCodeHeaderEntity;
import com.iscm.service.base.ICodeService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CodeService extends BaseService implements ICodeService {


    private static final Logger _LOG = LoggerFactory.getLogger(CodeService.class);

    @Autowired
    private CodeHeaderDao codeHeaderDao;

    @Autowired
    private CodeDetailDao codeDetailDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<IScmCodeHeaderEntity> getCodeHeader(Map<String, Object> map) throws Exception {
        return codeHeaderDao.getCodeHeader(map);
    }

    @Override
    public List<IScmCodeDetailEntity> getCodeDetailList(Long codeId) throws Exception {
        IScmCodeHeaderEntity codeHeaderEntity = getCodeHeader(codeId);
        if (null != codeHeaderEntity) {
            List<IScmCodeDetailEntity> codeDetailEntityList = JSONUtils.deserializes(redisTemplate.hget(RedisConstants.CODE_DATA, codeHeaderEntity.getListName()), IScmCodeDetailEntity.class);
            if (CollectionUtils.isNotEmpty(codeDetailEntityList)) {
                return codeDetailEntityList;
            }
            return codeDetailDao.getCodeDetailList(codeId);
        }
        return null;
    }

    @Override
    public IScmCodeHeaderEntity getCodeHeader(long id) throws Exception {
        return codeHeaderDao.get(id);
    }

    @Override
    public IScmCodeHeaderEntity getCodeHeader(String listName) throws Exception {
        return codeHeaderDao.getCodeHeader(listName);
    }

    @Override
    public IScmCodeDetailEntity getCodeDetail(long id) throws Exception {
        return codeDetailDao.get(id);
    }

    @Override
    public void updateCodeDetail(IScmCodeDetailEntity codeDetailEntity) throws Exception {
        IScmCodeDetailDto dto = new IScmCodeDetailDto();
        BeanUtils.copyProperties(dto, codeDetailEntity);
        redisTemplate.hset(RedisConstants.CODE_DATA,getCodeHeader(codeDetailEntity.getCodeId()).getListName(),JSONUtils.serialize(dto));
        codeDetailDao.saveOrUpdate(codeDetailEntity);
    }

    @Override
    public void updateCodeHeader(IScmCodeHeaderEntity codeHeaderEntity) throws Exception {
        codeHeaderDao.saveOrUpdate(codeHeaderEntity);
    }
}
