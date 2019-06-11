package com.rickiyang.learn.common;


public abstract class LogBuffer {

    protected final String logId;
    protected final String description;

    /**
     * 创建线程不安全的LogBuffer
     * 
     * @param description
     * @return
     */
    public static LogBuffer create(String description) {
        return create(null, description);
    }

    /**
     * 创建线程不安全的LogBuffer
     * 
     * @param logId 日志ID 可以为null
     * @param description 日志描述,会将“ ”替换为“_” （空格替换为下划线） 可以为null
     * @return
     */
    public static LogBuffer create(String logId, String description) {
        return create(logId, description, false);
    }

    /**
     * 创建LogBuffer
     * 
     * @param logId 日志ID 可以为null
     * @param description 日志描述， 日志描述,会将“ ”替换为“_” （空格替换为下划线） 可以为null
     * @param isThreadSafe 是否线程安全
     * @return
     */
    public static LogBuffer create(String logId, String description, boolean isThreadSafe) {
        if (isThreadSafe) {
            return new ThreadSafeBuffer(logId, description);
        } else {
            return new ThreadUnSafeBuffer(logId, description);
        }
    }

    /**
     * 创建方法前
     * 
     * @param logId
     * @param methodName
     * @return
     */
    public static LogBuffer createMethodBefore(String logId, String methodName) {
        return create(logId, methodName + " begin");
    }

    /**
     * 创建方法后
     * 
     * @param logId
     * @param methodName
     * @param beginTime
     * @param endTime
     * @return
     */
    public static LogBuffer createMethodAfter(String logId, String methodName, long beginTime, long endTime) {
        return create(logId, methodName + " end").append("use time", endTime - beginTime);
    }

    /**
     * 追加日志类容，日志以"key1=value1 key2=value2 ……"的形式打印
     * 
     * @param key 请不要输入"="或" "（等号或空格）
     * @param value 请不要输入"="或" "（等号或空格）
     * @return
     */
    public abstract LogBuffer append(CharSequence key, CharSequence value);

    public abstract LogBuffer append(CharSequence key, int value);

    public abstract LogBuffer append(CharSequence key, long value);

    LogBuffer(String logId, String description) {
        this.logId = logId;
        if (description != null) {
            this.description = description.trim().replaceAll(" ", "_");
        } else {
            this.description = null;
        }

    }

    public String getDescription() {
        return description;
    }

    public String getLogId() {
        return logId;
    }

    static final class ThreadSafeBuffer extends LogBuffer {
        private StringBuffer sb;

        ThreadSafeBuffer(String logId, String description) {
            super(logId, description);
            this.sb = new StringBuffer();
            if (logId != null) {
                this.sb.append("logId=" + this.logId);
            }
            if (description != null) {
                this.sb.append(" description=" + this.description);
            }

        }

        @Override
        public LogBuffer append(CharSequence key, CharSequence value) {
            this.sb.append(" ");
            this.sb.append(key).append("=").append(value);
            return this;
        }

        @Override
        public String toString() {
            return this.sb.toString();
        }

        @Override
        public LogBuffer append(CharSequence key, int value) {
            this.sb.append(" ");
            this.sb.append(key).append("=").append(value);
            return this;
        }

        @Override
        public LogBuffer append(CharSequence key, long value) {
            this.sb.append(" ");
            this.sb.append(key).append("=").append(value);
            return this;
        }
    }

    static final class ThreadUnSafeBuffer extends LogBuffer {
        private StringBuilder sb;

        ThreadUnSafeBuffer(String logId, String description) {
            super(logId, description);
            this.sb = new StringBuilder();
            if (logId != null) {
                this.sb.append("logId=" + this.logId);
            }
            if (description != null) {
                this.sb.append(" description=" + this.description);
            }
        }

        @Override
        public LogBuffer append(CharSequence key, CharSequence value) {
            this.sb.append(" ");
            this.sb.append(key).append("=").append(value);
            return this;
        }

        @Override
        public String toString() {
            return this.sb.toString();
        }

        @Override
        public LogBuffer append(CharSequence key, int value) {
            this.sb.append(" ");
            this.sb.append(key).append("=").append(value);
            return this;
        }

        @Override
        public LogBuffer append(CharSequence key, long value) {
            this.sb.append(" ");
            this.sb.append(key).append("=").append(value);
            return this;
        }
    }

    public abstract String toString();
}
