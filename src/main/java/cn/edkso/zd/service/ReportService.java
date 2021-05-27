package cn.edkso.zd.service;


import cn.edkso.zd.entry.Land;
import cn.edkso.zd.entry.Report;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ReportService {

    Page<Report> listByPage(Integer page, Integer limit, ThreadLocal<Map<String, Object>> tl);

    public Report add(Report report);

    public Report update(Report report);
}
