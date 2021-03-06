package cn.edkso.zd.dao;

import cn.edkso.zd.entry.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    public List<User> findByUsername(String username);
    public User findByUsernameAndPassword(String username, String password);
}
