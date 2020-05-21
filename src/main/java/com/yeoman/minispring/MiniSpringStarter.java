package com.yeoman.minispring;

import com.yeoman.minispring.support.ScanPackageHelper;

import java.lang.reflect.Field;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/19
 * @desc
 */
public class MiniSpringStarter {

    private String s;
    public static void main(String[] args) {
//        ScanPackageHelper.scan(MiniSpringStarter.class.getPackage().getName()).forEach( System.out::println);
        Class c = MiniSpringStarter.class;
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getType().getName());
        }
    }

}
