package com.iscm.main.service.impl;

import com.iscm.core.db.service.BaseService;
import com.iscm.main.dao.UserDao;
import com.iscm.po.main.IScmUserEntity;
import com.iscm.service.main.IUserService;
import com.iscm.vo.CurrenUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService extends BaseService implements IUserService {

    private static final Logger _LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Map getUserPage(Map<String, Object> map) throws Exception {
        return userDao.getUserPage(map);
    }

    @Override
    public List<IScmUserEntity> getUserList(Map<String, Object> map) throws Exception {
        return userDao.getUserList(map);
    }

    @Override
    public IScmUserEntity getUser(String userName, String password){
        return userDao.getUser(userName,password);
    }

    @Override
    public IScmUserEntity getUser(long id) throws Exception {
        return userDao.get(id);
    }

    @Override
    public void deleteUser(long id) throws Exception {
        userDao.delete(id);
    }

    @Override
    public IScmUserEntity saveOrUpdate(IScmUserEntity userEntity) throws Exception {
        return userDao.saveOrUpdate(userEntity);
    }

    @Override
    public String updatePassword(long userId, String passwordOld, String password, CurrenUserEntity currentUser) throws Exception {
        return null;
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) throws Exception {

    }

    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds) throws Exception {

    }

    @Override
    public IScmUserEntity findByUserName(String userName){
        return userDao.findByUserName(userName);
    }

    @Override
    public Set<String> findRoles(String username) {
        return null;
    }

    @Override
    public Set<String> findPermissions(String username) {
        return null;
    }
}
