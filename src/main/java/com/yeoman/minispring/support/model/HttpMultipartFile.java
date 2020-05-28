package com.yeoman.minispring.support.model;


import java.nio.charset.Charset;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/28
 * @desc
 */
public class HttpMultipartFile {

    private byte[] content;
    private String originName;
    private String name;
    private Charset charset;
    private long length;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
