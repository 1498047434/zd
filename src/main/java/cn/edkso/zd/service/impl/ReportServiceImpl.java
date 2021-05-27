package cn.edkso.zd.service.impl;

import cn.edkso.zd.dao.ReportDao;
import cn.edkso.zd.entry.Admin;
import cn.edkso.zd.entry.Report;
import cn.edkso.zd.entry.User;
import cn.edkso.zd.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;

    @Override
    public Page<Report> listByPage(Integer page, Integer limit, ThreadLocal<Map<String, Object>> tl) {

        Map<String, Object> map = tl.get();
        User user = (User) map.get("user");
        Admin admin = (Admin) map.get("admin");


        Pageable pageable = PageRequest.of(page -1,limit);
        Specification specification = (root, cq, cb) -> {
            Predicate predicate = cb.conjunction();
            //增加筛选条件0(土地地址模糊匹配)
            if (user != null){
                predicate.getExpressions().add(cb.equal(root.get("userId"), user.getId()));
            }
            if (admin != null){
                predicate.getExpressions().add(cb.equal(root.get("adminId"), admin.getId()));
            }

            return predicate;
        };
        Page<Report> reportPage = reportDao.findAll(specification, pageable);
        return reportPage;
    }

    @Override
    public Report add(Report report) {
        return reportDao.save(report);
    }

    @Override
    public Report update(Report report) {
        Report oldReport = reportDao.findById(report.getId()).get();

        if (report.getState() != null){
            oldReport.setState(report.getState());
        }
        //TODO 现在只需要修改状态 state 0,未处理，1已经处理，其他的以后有需要再处理

        return reportDao.save(oldReport);
    }
}
