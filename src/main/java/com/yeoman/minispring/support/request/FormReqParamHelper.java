package com.yeoman.minispring.support.request;

import com.yeoman.minispring.support.AbstractReqParamHelper;
import com.yeoman.minispring.support.model.HttpMultipartFile;
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
    protected void getReqParamValueByListParamName(FullHttpRequest request, String[] names) {

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
                    super.setValue(k, attribute.getValue());
                } else if (data instanceof FileUpload) {
                    fileUpload = (FileUpload) data;

                    if (needSet(fileUpload.getName())) {
                        HttpMultipartFile file = new HttpMultipartFile();
                        file.setContent(fileUpload.get());
                        file.setOriginName(fileUpload.getFilename());
                        file.setName(fileUpload.getName());
                        file.setCharset(fileUpload.getCharset());
                        file.setLength(fileUpload.length());
                        super.setValue(file.getName(), file);
                    }
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
