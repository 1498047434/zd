package cn.edkso.zd.service;

import cn.edkso.zd.entry.User;

public interface UserService {

    public User login(String username, String password);


    User update(User user);

    User register(User user);
}
