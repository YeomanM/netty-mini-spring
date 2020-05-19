package com.yeoman.minispring.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/19
 * @desc
 */
public class MinispringUtil {

    private static boolean init = false;
    private static String basePackage = "";
    public final static String BASE_PACKAGE_KEY_WORD = "minispring.base-package";
    private static List<String> packageNames = new ArrayList<String>();
    private static Map<String, Object> instanceMap = new HashMap<>();
    private static Map<String, String> nameMap = new HashMap<>();
    private static Map<String, Method> urlMethodMap = new HashMap<>();
    private static Map<Method, String> methodPackageMap = new HashMap<>();

    public static void init(Class clazz) {
        Object tmp = YamlUtil.get(BASE_PACKAGE_KEY_WORD);
        if (tmp != null) {
            basePackage = String.valueOf(tmp);
        }

    }

}
