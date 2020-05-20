package com.yeoman.minispring;

import com.yeoman.minispring.support.ScanPackageHelper;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/19
 * @desc
 */
public class MiniSpringStarter {

    public static void main(String[] args) {
        ScanPackageHelper.scan(MiniSpringStarter.class.getPackage().getName()).forEach( System.out::println);
    }

}
