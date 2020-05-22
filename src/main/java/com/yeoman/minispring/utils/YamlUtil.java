package com.yeoman.minispring.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/19
 * @desc
 */
public class YamlUtil {

    private static Boolean handler = false;
    private static Map params = new HashMap<>();

    private static void init() {
        InputStream is = YamlUtil.class.getClassLoader().getResourceAsStream("config.yml");
        Yaml yaml = new Yaml();
        params = yaml.loadAs(is, Map.class);
    }

    public static Object get(String key) {
        if (!handler) {
            synchronized (handler) {
                handler = true;
                init();
            }
        }
        return params.get(key);
    }

    public static Object getOrDefault(String key, Object o) {
        if (!handler) {
            synchronized (handler) {
                handler = true;
                init();
            }
        }
        return params.getOrDefault(key, o);
    }


}
