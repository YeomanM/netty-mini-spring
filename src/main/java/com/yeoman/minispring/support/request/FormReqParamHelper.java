package com.yeoman.minispring.support.request;

import com.yeoman.minispring.support.AbstractReqParamHelper;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/27
 * @desc
 */
public class FormReqParamHelper extends AbstractReqParamHelper {
    @Override
    public Object[] getReqParamValueByListParamName(FullHttpRequest request, String[] names) {
        int length;
        if (names == null || (length = names.length) == 0) {
            return new Object[0];
        }
        Object[] values = new Object[length];
        Map<String, Integer> name2Index = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            name2Index.put(names[i], i);
        }

        HttpPostMultipartRequestDecoder decoder = null;
        try {
            String k,v;
            int index = -1;
            FileUpload fileUpload;
            Attribute attribute = null;
            InterfaceHttpData data = null;
            decoder = new HttpPostMultipartRequestDecoder(request);
            while (decoder.hasNext()) {
                data = decoder.next();
                if (data instanceof Attribute) {
                    attribute = (Attribute) data;
                    k = attribute.getName();
                    index = name2Index.getOrDefault(k, -1);
                    if (index < 0) {
                        continue;
                    }
                    values[index] = attribute.getValue();
                } else if (data instanceof FileUpload) {
                    fileUpload = (FileUpload) data;
                    fileUpload.get()
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (decoder != null) {
                decoder.destroy();
            }
        }

    }
}
