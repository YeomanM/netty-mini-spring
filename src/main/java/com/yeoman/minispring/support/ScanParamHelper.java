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
 * @desc
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
                if (!method.getName().equalsIgnoreCase(name) || !Arrays.equals(argTypes, types)) {
                    return null;
                }

                return new MethodVisitor(Opcodes.ASM4) {
                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        if (Modifier.isStatic(method.getModifiers())) {
                            names[index] = name;
                        } else {
                            names[index - 1] = name;
                        }
                    }
                };
            }
        }, 0);
        return names;
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
