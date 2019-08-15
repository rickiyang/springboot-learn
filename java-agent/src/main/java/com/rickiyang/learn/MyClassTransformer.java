package com.rickiyang.learn;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author rickiyang
 * @date 2019-08-06
 * @Desc
 */
public class MyClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(
            final ClassLoader loader,
            final String className,
            final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer) throws IllegalClassFormatException {
        // 仅仅操作HttpURLConnection类
        if (className.endsWith("sun/net/www/protocol/http/HttpURLConnection")) {
            try {
                // 从ClassPool获得CtClass对象
                final ClassPool classPool = ClassPool.getDefault();
                final CtClass clazz = classPool.get("sun.net.www.protocol.http.HttpURLConnection");
                // 修改构造函数，在其结尾添加日志记录逻辑
                for (final CtConstructor constructor : clazz.getConstructors()) {
                    constructor.insertAfter("System.out.println(this.getURL());");
                }
                // 返回字节码，并且detachCtClass对象
                byte[] byteCode = clazz.toBytecode();
                clazz.detach();

                return byteCode;
            } catch (final NotFoundException | CannotCompileException | IOException ex) {
                ex.printStackTrace();
            }
        }
        // 如果返回null则字节码不会被修改
        return null;
    }
}
