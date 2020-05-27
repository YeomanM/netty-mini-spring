package com.yeoman.minispring.support.request;

import com.yeoman.minispring.support.AbstractReqParamHelper;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/27
 * @desc
 */
public class QueryStringReqParamHelper extends AbstractReqParamHelper {
    @Override
    public Object[] getReqParamValueByListParamName(FullHttpRequest request, String[] names) {
        return new Object[0];
    }
}
