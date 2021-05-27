package cn.edkso.zd.service.impl;

import cn.edkso.zd.constant.ExceptionDefault;
import cn.edkso.zd.dao.AdminDao;
import cn.edkso.zd.entry.Admin;
import cn.edkso.zd.exception.CDException;
import cn.edkso.zd.service.AdminService;
import cn.edkso.zd.utils.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public Admin login(String username, String password) {
        Admin admin = adminDao.findByUsernameAndPassword(username, MD5Utils.md5(password));
        if(admin != null){
            return admin;
        }
        return null;
    }

    @Override
    public Admin update(Admin admin) {
        Admin oldAdmin = adminDao.findById(admin.getId()).get();
        if (StringUtils.isNotBlank(admin.getName())){
            oldAdmin.setName(admin.getName());
        }
        if (admin.getState() != null){
            oldAdmin.setState(admin.getState());
        }
        if (admin.getGrade() != null){
            oldAdmin.setGrade(admin.getGrade());
        }
        if (StringUtils.isNotBlank(admin.getPassword())){
            oldAdmin.setPassword(MD5Utils.md5(admin.getPassword()));
        }
        return adminDao.save(oldAdmin);
    }

    @Override
    public Admin add(Admin admin) {
        Admin resAdmin = adminDao.findByUsername(admin.getName());
        if (resAdmin != null){
            throw new CDException(ExceptionDefault.RECORD_EXIST);
        }

        admin.setPassword(MD5Utils.md5(admin.getPassword()));
        resAdmin = adminDao.save(admin);
        return resAdmin;
    }
}
