package com.rickiyang.learn.javaagent;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;


/**
 * @author rickiyang
 * @date 2019-08-08
 * @Desc
 */
public class TestPhantomReference {
    public static boolean isRun = true;

    public static void main(String[] args) throws Exception {
        String str = new String("123");
        System.out.println(str.getClass() + "@" + str.hashCode());
        final ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        new Thread(() -> {
            while (isRun) {
                Object obj = referenceQueue.poll();
                if (obj != null) {
                    try {
                        Field rereferent = Reference.class.getDeclaredField("referent");
                        rereferent.setAccessible(true);
                        Object result = rereferent.get(obj);
                        System.out.println("gc will collect："
                                + result.getClass() + "@"
                                + result.hashCode() + "\t"
                                + result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        PhantomReference<String> weakRef = new PhantomReference<>(str, referenceQueue);
        str = null;
        Thread.currentThread().sleep(2000);
        System.gc();
        Thread.currentThread().sleep(2000);
        isRun = false;
    }
}