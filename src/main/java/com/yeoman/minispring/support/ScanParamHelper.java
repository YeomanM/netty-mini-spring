package com.yeoman.minispring.support;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import com.yeoman.minispring.handler.DispatcherServlet;
import jdk.internal.org.objectweb.asm.*;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/25
 * @desc 遍历参数
 */
public class ScanParamHelper {


    public static String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) throws IOException {

        final Class<?>[] paramsType = method.getParameterTypes();
        if (paramsType.length == 0) {
            return null;
        }
        final Type[] types = new Type[paramsType.length];
        for (int i = 0; i < paramsType.length; i++) {
            types[i] = Type.getType(paramsType[i]);
            System.out.println(types[i].getClassName());
        }

        final String[] names = new String[types.length];

        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = clazz.getResourceAsStream(className);
        ClassReader reader = new ClassReader(is);
        reader.accept(new ClassVisitor(Opcodes.ASM4){
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

                Type[] argTypes = Type.getArgumentTypes(desc);
                if (!method.getName().equalsIgnoreCase(name) || !same(argTypes, types)) {
                    return null;
                }

                return new MethodVisitor(Opcodes.ASM4) {
                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        if (Modifier.isStatic(method.getModifiers())) {
                            if (index < names.length && index >= 0) {
                                names[index] = name;
                            }
                        } else {
                            if (index <= names.length && index > 0) {
                                names[index - 1] = name;
                            }
                        }
                    }
                };
            }
        }, 0);
        return names;
    }

    private static boolean same(Type[] types, Type[] originTypes) {
        if (!Arrays.equals(originTypes, types)){
            return false;
        }
        for (int i = 0, len = types.length; i < len; i++) {
            if (!types[i].getClassName().equalsIgnoreCase(originTypes[i].getClassName())) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        Class c = DispatcherServlet.class;
        Method[] methods = c.getDeclaredMethods();
        Method method = null;
        for (Method method1 : methods) {
            if (method1.getName().equalsIgnoreCase("channelRead")){
                method = method1;
                break;
            }
        }

        String[] ss = ScanParamHelper.getMethodParameterNamesByAsm4(c, method);
        for (String s : ss) {
            System.out.println(s);
        }
    }

}
