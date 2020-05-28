package com.yeoman.minispring.support.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeoman.minispring.support.AbstractReqParamHelper;
import io.netty.handler.codec.http.FullHttpRequest;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/27
 * @desc
 */
public class ApplicationJsonReqParamHelper extends AbstractReqParamHelper {

    @Override
    protected void getReqParamValueByListParamName(FullHttpRequest request, String[] names) {

        int length = names.length;
        String jsonStr = request.content().toString(StandardCharsets.UTF_8);
        JSONObject params = JSON.parseObject(jsonStr);
        for (String name : names) {
            super.setValue(name, params.getOrDefault(name, null));
        }
    }
}
