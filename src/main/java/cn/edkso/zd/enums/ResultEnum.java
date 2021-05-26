package cn.edkso.zd.enums;

public enum ResultEnum {

    REGISTER_ERROR(411, "注册失败"),
    LOGIN_ERROR(421, "登录失败，账号或密码错误或者没有认证"),
    NOT_LOGGED_IN(422, "未登录！"),
    OLD_PASWORD_ERROR(423, "原始密码错误！"),
    PARAMS_ERROR_OR_SYSTEM_EXCEPTION(432, "参数错误或系统异常"),
    UPLOAD_ERROR(441, "上传出现异常"),

    //和土地有关
    BIDDING_IS_NOT_END(451,"竞价未结束"),
    BIDDING_IS_NOT_FOR_YOU(452,"竞价最高者不是您！"),
            ;
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
