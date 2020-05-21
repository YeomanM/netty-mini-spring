package com.yeoman.minispring.support;

import com.yeoman.minispring.MiniSpringStarter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/20
 * @desc
 */
public class ScanPackageHelper {

    private static DefaultFilter filter;
    private static Byte lock = 1;


    public static List<String> scan(List<String> baseDirs) {
        return ScanPackageHelper.scan(baseDirs, ScanPackageHelper.getDefaultFilter());
    }

    public static List<String> scan(List<String> baseDir, Filter filter) {
        List<String> result = new ArrayList<>();
        for (String dir : baseDir) {
            result.addAll(ScanPackageHelper.scan(dir, filter));
        }
        return result;
    }

    public static List<String> scan(String dir) {
        return ScanPackageHelper.scan(dir, ScanPackageHelper.getDefaultFilter());
    }

    public static List<String> scan(String dir, Filter filter) {
        String file = "file", jar = "jar";
        List<String> result = new ArrayList<>();
        String fileDir = dir.replaceAll("\\.", "/");
        Enumeration<URL> dirs = null;
        try {
            dirs = ScanPackageHelper.class.getClassLoader().getResources(fileDir);
            URL parent = null;
            while (dirs.hasMoreElements()) {
                parent = dirs.nextElement();
                String protocol = parent.getProtocol();
                if (protocol.equalsIgnoreCase(file)) {
                    String parentDir = URLDecoder.decode(parent.getFile(), "UTF-8");
                    result.addAll(ScanPackageHelper.scanFile(parentDir, filter, dir));
                } else if (protocol.equalsIgnoreCase(jar)) {
                    result.addAll(ScanPackageHelper.scanJar(parent, fileDir));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<String> scanJar(URL parent,String parentDir) {
        JarFile jarFile = null;
        List<String> result = new ArrayList<>();

        try {
            jarFile = ((JarURLConnection) parent.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();

            JarEntry entry;
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                String name = entry.getName();

                if (name.charAt(0) == '/') {
                    name = name.substring(1);
                }

                if (name.startsWith(parentDir)) {
                    if (name.endsWith(".class") && !entry.isDirectory()) {
                        result.add(name.replaceAll("/", "."));
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<String> scanFile(String parentDir, Filter filter, String preDir) {
        List<String> result = new ArrayList<>();
        File file = new File(parentDir);
        File[] files = file.listFiles(filter);
        String fileName = "";
        assert files != null;
        for (File f : files) {
            if (f.isDirectory()) {
                result.addAll(ScanPackageHelper.scanFile(f.getAbsolutePath(), filter, preDir + "." + f.getName()));
            } else {
                if ((fileName = f.getName()).endsWith(".class")) {
                    fileName = fileName.substring(0, fileName.length() - 6);
                }
                result.add(preDir + "." + fileName);
            }
        }
        return result;
    }


    public static Filter getDefaultFilter() {
        if (filter == null) {
            synchronized (lock) {
                if (filter == null) {
                    filter = new DefaultFilter();
                }
            }
        }
        return filter;
    }

    private static class DefaultFilter extends Filter {

        @Override
        public boolean accept(File f) {
            return true;
        }
    }

    public abstract static class Filter implements FileFilter {
    }



}
