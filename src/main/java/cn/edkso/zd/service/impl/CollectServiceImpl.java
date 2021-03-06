package cn.edkso.zd.service.impl;

import cn.edkso.zd.dao.CollectDao;
import cn.edkso.zd.entry.Collect;
import cn.edkso.zd.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectDao collectDao;

    @Override
    public void delByLandId(Integer landId) {
        collectDao.deleteByLandId(landId);
    }

    @Override
    public void delById(Integer id) {
        collectDao.deleteById(id);
    }

    @Override
    public Collect add(Collect collect) {
        return collectDao.save(collect);
    }

    @Override
    public List<Collect> findAllByUserId(Integer userId) {
        return collectDao.findAllByUserId(userId);
    }
}
