package cn.edkso.zd.dao;

import cn.edkso.zd.entry.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    public User findByUsername(String username);
    public User findByUsernameAndPassword(String username, String password);
}
