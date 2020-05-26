package com.yeoman.minispring.constants;

/**
 * @Description :
 * ---------------------------------
 * @Author : Yeoman
 * @Date : Create in 2020/5/26
 */
public enum ContentType {

    APPLICATION_JSON("application/json"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data"),

    ;

    ContentType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public static ContentType getInstance(String v) {
        if (APPLICATION_JSON.value.equalsIgnoreCase(v)) {
            return APPLICATION_JSON;
        } else if (APPLICATION_X_WWW_FORM_URLENCODED.value.equalsIgnoreCase(v)) {
            return APPLICATION_X_WWW_FORM_URLENCODED;
        } else if (MULTIPART_FORM_DATA.value.equalsIgnoreCase(v)){
            return MULTIPART_FORM_DATA;
        } else {
            throw new RuntimeException("不支持的请求类型");
        }
    }
}
