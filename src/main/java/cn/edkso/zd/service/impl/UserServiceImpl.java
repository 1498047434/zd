package cn.edkso.zd.service.impl;

import cn.edkso.zd.constant.ExceptionDefault;
import cn.edkso.zd.dao.UserDao;
import cn.edkso.zd.entry.User;
import cn.edkso.zd.exception.CDException;
import cn.edkso.zd.service.UserService;
import cn.edkso.zd.utils.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public User login(String username, String password) {
        User user = userDao.findByUsernameAndPassword(username, MD5Utils.md5(password));
        if(user != null){
            return user;
        }
        return null;
    }

    @Override
    public User register(String username, String password, String name) {
        User resUser = userDao.findByUsername(username);
        if (resUser != null){
            throw new CDException(ExceptionDefault.RECORD_EXIST);
        }
        User user = new User();
        user.setState(1);
        user.setName(name);
        user.setUsername(username);
        user.setPassword(MD5Utils.md5(password));
        resUser = userDao.save(user);
        return resUser;
    }

    @Override
    public User update(User user) {

        User oldUser = userDao.findById(user.getId()).get();
        if (StringUtils.isNotBlank(user.getPassword())){
            oldUser.setPassword(MD5Utils.md5(user.getPassword()));
        }
        if (StringUtils.isNotBlank(user.getName())){
            oldUser.setName(user.getName());
        }
        if (StringUtils.isNotBlank(user.getIdcard())){
            oldUser.setIdcard(user.getIdcard());
        }
        if (StringUtils.isNotBlank(user.getIdcardImg())){
            oldUser.setIdcardImg(user.getIdcardImg());
        }
        if (user.getState() != null){
            oldUser.setState(user.getState());
        }
        return userDao.save(oldUser);
    }
}
