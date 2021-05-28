package cn.edkso.zd.service.impl;

import cn.edkso.zd.dao.LandDao;
import cn.edkso.zd.entry.Admin;
import cn.edkso.zd.entry.Collect;
import cn.edkso.zd.entry.Land;
import cn.edkso.zd.entry.User;
import cn.edkso.zd.service.CollectService;
import cn.edkso.zd.service.LandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LandServiceImpl implements LandService {

    @Autowired
    private LandDao landDao;
    @Autowired
    private CollectService collectService;

    @Override
    public Page<Land> listByPage(Integer page, Integer limit, Land land, ThreadLocal<Map<String, Object>> tl) {
        Map<String, Object> map = tl.get();
        Integer contractPeriodStart = (Integer) map.get("contractPeriodStart");
        Integer contractPeriodEnd = (Integer) map.get("contractPeriodEnd");
        User user = (User) map.get("user");
        Admin admin = (Admin) map.get("admin");

        Pageable pageable = PageRequest.of(page -1,limit);
        Specification specification = (root, cq, cb) -> {
            Predicate predicate = cb.conjunction();
            //增加筛选条件0(土地地址模糊匹配)
            if (StringUtils.isNotBlank(land.getAddress())){
                predicate.getExpressions().add(cb.like(root.get("address"), land.getAddress()));
            }
            //增加筛选条件1(土地分类精确匹配)
            if (land.getClassify() != null && land.getClassify() != -1){
                predicate.getExpressions().add(cb.equal(root.get("classify"), land.getAddress()));
            }
            //增加筛选条件2(土地出价精确匹配)
            if (land.getIsBidding() != null && land.getIsBidding() != -1){
                predicate.getExpressions().add(cb.equal(root.get("isBidding"), land.getAddress()));
            }
            //增加筛选条件3(土地承包年限最低 - 土地承包年限最高)
            {
                if(contractPeriodStart != null && contractPeriodEnd != null){
                    predicate.getExpressions()
                            .add(cb.between(root.get("contractPeriod"), contractPeriodStart, contractPeriodEnd));
                }else if(contractPeriodStart != null){
                    predicate.getExpressions()
                            .add(cb.between(root.get("contractPeriod"), contractPeriodStart, 99));
                }else if(contractPeriodEnd != null){
                    predicate.getExpressions().add(cb.between(root.get("contractPeriod"), 0, contractPeriodEnd));
                }
            }

            //增加筛选条件4(土地状态精确匹配)
            predicate.getExpressions().add(cb.equal(root.get("state"), 1));


            //增加筛选条件5(土地管理员精确匹配)
            if (admin != null && admin.getGrade() == 2){  //若是超级管理员，获取到的是所有管理员发布的土地
                predicate.getExpressions().add(cb.equal(root.get("adminId"), admin.getId()));
            }

            return predicate;
        };

        Page<Land> landPage = landDao.findAll(specification, pageable);

        if (land.getIsCollect() == 1){
            List<Collect> collectList = collectService.findAllByUserId(user.getId());
            List<Integer> landIdList = new ArrayList<>();
            for (Collect collect : collectList) {
                landIdList.add(collect.getLandId());
            }

            for (Land curLand : landPage.getContent()) {
                if (landIdList.contains(curLand.getId())){
                    curLand.setIsCollect(1);
                }
            }
        }

        return landPage;
    }

    @Override
    public Land update(Land land) {
        Land oldLand = landDao.findById(land.getId()).get();
        if (StringUtils.isNotBlank(land.getAddress())){
            oldLand.setAddress(land.getAddress());
        }
        if (land.getClassify() != null){
            oldLand.setClassify(land.getClassify());
        }
        if (StringUtils.isNotBlank(land.getAddress())){
            oldLand.setAddress(land.getAddress());
        }
        if (land.getArea() != null){
            oldLand.setArea(land.getArea());
        }
        if (land.getPrice() != null){
            oldLand.setPrice(land.getPrice());
        }
        if (land.getContractPeriod() != null){
            oldLand.setContractPeriod(land.getContractPeriod());
        }
        if (land.getIntroduction() != null){
            oldLand.setIntroduction(land.getIntroduction());
        }
        if (land.getAdminId() != null){
            oldLand.setAdminId(land.getAdminId());
        }
        if (StringUtils.isNotBlank(land.getAdminName())){
            oldLand.setAdminName(land.getAdminName());
        }
        if (land.getUserId() != null){
            oldLand.setUserId(land.getUserId());
        }
        if (StringUtils.isNotBlank(land.getUserName())){
            oldLand.setUserName(land.getUserName());
        }
        if (land.getIsBidding() != null){
            oldLand.setIsBidding(land.getIsBidding());
        }
        if (land.getBidPrice() != null){
            oldLand.setBidPrice(land.getBidPrice());
        }
        if (land.getBidUserId() != null){
            oldLand.setBidUserId(land.getBidUserId());
        }
        if (StringUtils.isNotBlank(land.getBidUserName())){
            oldLand.setBidUserName(land.getBidUserName());
        }
        if (land.getState() != null){
            oldLand.setState(land.getState());
        }
        if (StringUtils.isNotBlank(land.getAgreementImg())){
            oldLand.setAgreementImg(land.getAgreementImg());
        }
        return landDao.save(land);
    }

    @Override
    public Land add(Land land) {
        land.setState(1);
        return landDao.save(land);
    }

    @Override
    public void delById(Integer id) {
        landDao.deleteById(id);
    }
}
