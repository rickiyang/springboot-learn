package com.rickiyang.learn;

import java.lang.instrument.Instrumentation;

/**
 * @author rickiyang
 * @date 2019-08-06
 * @Desc
 */
public class AgentTest {

    private static Instrumentation inst = null;

    /**
     * 命令行启动
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("pre main running");
        inst = instrumentation;
    }

    /**
     * 命令行启动
     */
    public static void premain(String agentArgs) {
        System.out.println("pre main running");
    }

    /**
     * 类加载调用
     */
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("agent main running");
        inst = instrumentation;
        instrumentation.addTransformer(new MyClassTransformer());
    }

    public static Instrumentation instrumentation() {
        return inst;
    }
}
