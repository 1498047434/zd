package cn.edkso.zd.VO;

import lombok.Data;

@Data
public class ResultVO {
    private Integer code;   // 正常为0;
    private String msg;
    private Long count;     //分页总记录数
    private Object data;    //操作结果，查询结构
    private Long timestamp; //时间戳
}
