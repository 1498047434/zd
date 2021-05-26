package cn.edkso.zd.service;

import cn.edkso.zd.entry.Land;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface LandService {

    Page<Land> listByPage(Integer page, Integer limit, Land land, ThreadLocal<Map<String, Object>> tl);

    public Land update(Land land);

    public Land add(Land land);

    void delById(Integer id);

}
