package com.yeoman.minispring.support;


import com.yeoman.minispring.constants.ContentType;
import com.yeoman.minispring.support.request.ApplicationJsonReqParamHelper;
import com.yeoman.minispring.support.request.FormReqParamHelper;
import com.yeoman.minispring.support.request.QueryStringReqParamHelper;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/27
 * @desc
 */
public abstract class AbstractReqParamHelper {

    /**
     * 根据参数名称获取请求参数的值
     *
     * @param request
     * @param names 参数名称列表
     * @return 值对应的列表
     */
    public abstract Object[] getReqParamValueByListParamName(FullHttpRequest request, String[] names);

    public static AbstractReqParamHelper getInstance(FullHttpRequest request) {
        String contentType = request.headers().get("Content-type").replaceAll(" ", "");
        String method = request.method().name();
        if (method.equalsIgnoreCase(HttpMethod.GET.name()) || contentType.startsWith(ContentType.APPLICATION_X_WWW_FORM_URLENCODED.getValue())) {
            return new QueryStringReqParamHelper();
        } else if (contentType.startsWith(ContentType.APPLICATION_JSON.getValue())){
            return new ApplicationJsonReqParamHelper();
        } else if (contentType.startsWith(ContentType.MULTIPART_FORM_DATA.getValue())) {
            return new FormReqParamHelper();
        }
        throw new RuntimeException("不存在的请求方式！");
    }
}
