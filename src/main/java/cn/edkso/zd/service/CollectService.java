package cn.edkso.zd.service;

import cn.edkso.zd.entry.Collect;
import io.swagger.models.auth.In;

import java.util.List;

public interface CollectService {

    public void delByLandId(Integer landId);

    public void delById(Integer id);

    Collect add(Collect collect);

    List<Collect> findAllByUserId(Integer userId);
}
