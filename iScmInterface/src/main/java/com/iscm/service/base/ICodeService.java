package com.iscm.service.base;


import com.iscm.po.base.IScmCodeDetailEntity;
import com.iscm.po.base.IScmCodeHeaderEntity;

import java.util.List;
import java.util.Map;

public interface ICodeService {


    public List<IScmCodeHeaderEntity> getCodeHeader(Map<String, Object> map) throws Exception;

    public List<IScmCodeDetailEntity> getCodeDetailList(Long codeId) throws Exception;

    public IScmCodeHeaderEntity getCodeHeader(long id) throws Exception;

    public IScmCodeHeaderEntity getCodeHeader(String listName) throws Exception;

    public IScmCodeDetailEntity getCodeDetail(long id) throws Exception;

    public void updateCodeDetail(IScmCodeDetailEntity codeDetailEntity) throws Exception;

    public void updateCodeHeader(IScmCodeHeaderEntity codeHeaderEntity) throws Exception;

}
