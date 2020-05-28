package com.yeoman.minispring.support.request;

import com.yeoman.minispring.support.AbstractReqParamHelper;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/27
 * @desc
 */
public class QueryStringReqParamHelper extends AbstractReqParamHelper {
    @Override
    protected void getReqParamValueByListParamName(FullHttpRequest request, String[] names) {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> params = decoder.parameters();
        for (String name : names) {
            super.setValue(name, params.getOrDefault(name, null));
        }
    }
}
