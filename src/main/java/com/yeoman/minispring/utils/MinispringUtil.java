package com.yeoman.minispring.utils;

import com.yeoman.minispring.annitation.Autowired;
import com.yeoman.minispring.annitation.Controller;
import com.yeoman.minispring.annitation.RequestMapping;
import com.yeoman.minispring.annitation.Service;
import com.yeoman.minispring.support.ScanPackageHelper;

import java.lang.reflect.Field;
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

    public static void init(Class clazz) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object tmp = YamlUtil.get(BASE_PACKAGE_KEY_WORD);
        if (tmp != null) {
            basePackage = String.valueOf(tmp);
        } else {
            basePackage = clazz.getPackage().getName();
        }

        packageNames = ScanPackageHelper.scan(basePackage);
        instance();
        ioc();



    }

    private static void instance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (packageNames.isEmpty()) {
            return;
        }

        Class c;
        for (String packageName : packageNames) {
            c =Class.forName(packageName);

            String name = "";
            if (c.isAnnotationPresent(Controller.class)) {
                Controller controller = (Controller) c.getAnnotation(Controller.class);
                name = getName(controller.value(), packageName);
            } else if (c.isAnnotationPresent(Service.class)) {
                Service service = (Service) c.getAnnotation(Service.class);
                name = getName(service.value(), packageName);
            }

            if (!name.isEmpty()) {
                instanceMap.put(name, c.newInstance());
                nameMap.put(packageName, name);
            }
        }

    }

    private static void ioc() throws IllegalAccessException {
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String name = autowired.value();
                    if (name.isEmpty()) {
                        name = field.getType().getName();
                        int index = name.lastIndexOf(".");
                        if (index >= 0) {
                            name = name.substring(index + 1);
                        }
                    }
                    field.setAccessible(true);
                    field.set(entry.getValue(), instanceMap.get(name));
                    field.setAccessible(false);
                }
            }
        }
    }

    private static String  getName(String value, String packageName){
        if (value.isEmpty()) {
            int index = packageName.lastIndexOf(".");
            if (index >= 0) {
                packageName = packageName.substring(index + 1);
            }
            value = packageName;
        }
        return value;
    }

}
