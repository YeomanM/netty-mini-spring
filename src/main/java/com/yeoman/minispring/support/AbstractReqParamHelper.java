package com.yeoman.minispring.support;


import com.yeoman.minispring.constants.ContentType;
import com.yeoman.minispring.support.request.ApplicationJsonReqParamHelper;
import com.yeoman.minispring.support.request.FormReqParamHelper;
import com.yeoman.minispring.support.request.QueryStringReqParamHelper;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/27
 * @desc
 */
public abstract class AbstractReqParamHelper {

    private Object[] values;
    private Map<String, Integer> name2Index;

    public Object[] getReqParamValueByArrayParamName(FullHttpRequest request, String[] names) {
        int length;
        if (names == null || (length = names.length) == 0) {
            return new Object[0];
        }
        values = new Object[length];
        name2Index = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            name2Index.put(names[i], i);
        }
        getReqParamValueByListParamName(request, names);
        return values;
    }

    public void setValue(String name, Object value) {
        int index = name2Index.getOrDefault(name, -1);
        if (index >= 0) {
            values[index] = value;
        }
    }

    public boolean needSet(String name) {
        return name2Index.containsKey(name);
    }

    /**
     * 根据参数名称获取请求参数的值
     *
     * @param request
     * @param names 参数名称列表
     * @return 值对应的列表
     */
    protected abstract void getReqParamValueByListParamName(FullHttpRequest request, String[] names);

    public static AbstractReqParamHelper getInstance(FullHttpRequest request) {
        String contentType = request.headers().get("Content-type");
        String method = request.method().name();

        if (contentType != null) {
            contentType = contentType.replaceAll(" ", "");
        }

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
