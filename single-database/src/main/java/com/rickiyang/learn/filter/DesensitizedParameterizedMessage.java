package com.rickiyang.learn.filter;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringBuilderFormattable;

import java.text.SimpleDateFormat;
import java.util.*;

public class DesensitizedParameterizedMessage implements Message, StringBuilderFormattable {
    // Should this be configurable?
    private static final int DEFAULT_STRING_BUILDER_SIZE = 255;

    /**
     * Prefix for recursion.
     */
    public static final String RECURSION_PREFIX = ParameterFormatter.RECURSION_PREFIX;
    /**
     * Suffix for recursion.
     */
    public static final String RECURSION_SUFFIX = ParameterFormatter.RECURSION_SUFFIX;

    /**
     * Prefix for errors.
     */
    public static final String ERROR_PREFIX = ParameterFormatter.ERROR_PREFIX;

    /**
     * Separator for errors.
     */
    public static final String ERROR_SEPARATOR = ParameterFormatter.ERROR_SEPARATOR;

    /**
     * Separator for error messages.
     */
    public static final String ERROR_MSG_SEPARATOR = ParameterFormatter.ERROR_MSG_SEPARATOR;

    /**
     * Suffix for errors.
     */
    public static final String ERROR_SUFFIX = ParameterFormatter.ERROR_SUFFIX;

    private static final long serialVersionUID = -665975803997290697L;

    private static final int HASHVAL = 31;

    // storing JDK classes in ThreadLocals does not cause memory leaks in web apps, so this is okay
    private static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<>();

    private String messagePattern;
    private transient Object[] argArray;

    private String formattedMessage;
    private transient Throwable throwable;
    private int[] indices;
    private int usedCount;

    public DesensitizedParameterizedMessage(final String messagePattern, final Object[] arguments, final Throwable throwable) {
        this.argArray = arguments;
        this.throwable = throwable;
        init(messagePattern);
    }

    public DesensitizedParameterizedMessage(final String messagePattern, final Object... arguments) {
        this.argArray = arguments;
        init(messagePattern);
    }

    /**
     * Constructor with a pattern and a single parameter.
     *
     * @param messagePattern The message pattern.
     * @param arg            The parameter.
     */
    public DesensitizedParameterizedMessage(final String messagePattern, final Object arg) {
        this(messagePattern, new Object[]{arg});
    }

    public DesensitizedParameterizedMessage(final String messagePattern, final Object arg0, final Object arg1) {
        this(messagePattern, new Object[]{arg0, arg1});
    }

    private void init(final String messagePattern) {
        this.messagePattern = messagePattern;
        final int len = Math.max(1, messagePattern == null ? 0 : messagePattern.length() >> 1); // divide by 2
        this.indices = new int[len]; // LOG4J2-1542 ensure non-zero array length
        final int placeholders = ParameterFormatter.countArgumentPlaceholders2(messagePattern, indices);
        initThrowable(argArray, placeholders);
        this.usedCount = Math.min(placeholders, argArray == null ? 0 : argArray.length);
    }

    private void initThrowable(final Object[] params, final int usedParams) {
        if (params != null) {
            final int argCount = params.length;
            if (usedParams < argCount && this.throwable == null && params[argCount - 1] instanceof Throwable) {
                this.throwable = (Throwable) params[argCount - 1];
            }
        }
    }

    @Override
    public String getFormat() {
        return messagePattern;
    }

    @Override
    public Object[] getParameters() {
        return argArray;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String getFormattedMessage() {
        if (formattedMessage == null) {
            final StringBuilder buffer = getThreadLocalStringBuilder();
            formatTo(buffer);
            formattedMessage = buffer.toString();
        }
        return formattedMessage;
    }

    private static StringBuilder getThreadLocalStringBuilder() {
        StringBuilder buffer = threadLocalStringBuilder.get();
        if (buffer == null) {
            buffer = new StringBuilder(DEFAULT_STRING_BUILDER_SIZE);
            threadLocalStringBuilder.set(buffer);
        }
        buffer.setLength(0);
        return buffer;
    }

    @Override
    public void formatTo(final StringBuilder buffer) {
        if (formattedMessage != null) {
            buffer.append(formattedMessage);
        } else {
            if (indices[0] < 0) {
                ParameterFormatter.formatMessage(buffer, messagePattern, argArray, usedCount);
            } else {
                ParameterFormatter.formatMessage2(buffer, messagePattern, argArray, usedCount, indices);
            }
        }
    }

    public static String format(final String messagePattern, final Object[] arguments) {
        return ParameterFormatter.format(messagePattern, arguments);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DesensitizedParameterizedMessage that = (DesensitizedParameterizedMessage) o;

        if (messagePattern != null ? !messagePattern.equals(that.messagePattern) : that.messagePattern != null) {
            return false;
        }
        if (!Arrays.equals(this.argArray, this.argArray)) {
            return false;
        }
        //if (throwable != null ? !throwable.equals(that.throwable) : that.throwable != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messagePattern != null ? messagePattern.hashCode() : 0;
        result = HASHVAL * result + (argArray != null ? Arrays.hashCode(argArray) : 0);
        return result;
    }

    public static int countArgumentPlaceholders(final String messagePattern) {
        return ParameterFormatter.countArgumentPlaceholders(messagePattern);
    }

    public static String deepToString(final Object o) {
        return ParameterFormatter.deepToString(o);
    }

    public static String identityToString(final Object obj) {
        return ParameterFormatter.identityToString(obj);
    }

    @Override
    public String toString() {
        return "ParameterizedMessage[messagePattern=" + messagePattern + ", stringArgs=" +
                Arrays.toString(argArray) + ", throwable=" + throwable + ']';
    }


    static class ParameterFormatter {
        /**
         * Prefix for recursion.
         */
        static final String RECURSION_PREFIX = "[...";
        /**
         * Suffix for recursion.
         */
        static final String RECURSION_SUFFIX = "...]";

        /**
         * Prefix for errors.
         */
        static final String ERROR_PREFIX = "[!!!";
        /**
         * Separator for errors.
         */
        static final String ERROR_SEPARATOR = "=>";
        /**
         * Separator for error messages.
         */
        static final String ERROR_MSG_SEPARATOR = ":";
        /**
         * Suffix for errors.
         */
        static final String ERROR_SUFFIX = "!!!]";

        private static final char DELIM_START = '{';
        private static final char DELIM_STOP = '}';
        private static final char ESCAPE_CHAR = '\\';

        private static ThreadLocal<SimpleDateFormat> threadLocalSimpleDateFormat = new ThreadLocal<>();

        private ParameterFormatter() {
        }

        static int countArgumentPlaceholders(final String messagePattern) {
            if (messagePattern == null) {
                return 0;
            }
            final int length = messagePattern.length();
            int result = 0;
            boolean isEscaped = false;
            for (int i = 0; i < length - 1; i++) {
                final char curChar = messagePattern.charAt(i);
                if (curChar == ESCAPE_CHAR) {
                    isEscaped = !isEscaped;
                } else if (curChar == DELIM_START) {
                    if (!isEscaped && messagePattern.charAt(i + 1) == DELIM_STOP) {
                        result++;
                        i++;
                    }
                    isEscaped = false;
                } else {
                    isEscaped = false;
                }
            }
            return result;
        }

        static int countArgumentPlaceholders2(final String messagePattern, final int[] indices) {
            if (messagePattern == null) {
                return 0;
            }
            final int length = messagePattern.length();
            int result = 0;
            boolean isEscaped = false;
            for (int i = 0; i < length - 1; i++) {
                final char curChar = messagePattern.charAt(i);
                if (curChar == ESCAPE_CHAR) {
                    isEscaped = !isEscaped;
                    indices[0] = -1; // escaping means fast path is not available...
                    result++;
                } else if (curChar == DELIM_START) {
                    if (!isEscaped && messagePattern.charAt(i + 1) == DELIM_STOP) {
                        indices[result] = i;
                        result++;
                        i++;
                    }
                    isEscaped = false;
                } else {
                    isEscaped = false;
                }
            }
            return result;
        }



        static String format(final String messagePattern, final Object[] arguments) {
            final StringBuilder result = new StringBuilder();
            final int argCount = arguments == null ? 0 : arguments.length;
            formatMessage(result, messagePattern, arguments, argCount);
            return result.toString();
        }


        static void formatMessage2(final StringBuilder buffer, final String messagePattern,
                                   final Object[] arguments, final int argCount, final int[] indices) {
            if (messagePattern == null || arguments == null || argCount == 0) {
                buffer.append(messagePattern);
                return;
            }
            int previous = 0;
            for (int i = 0; i < argCount; i++) {
                String word = messagePattern.substring(previous, indices[i]);
                buffer.append(messagePattern, previous, indices[i]);
                previous = indices[i] + 2;
                StringBuilder builder = new StringBuilder();
                recursiveDeepToString(arguments[i], builder, null);
                buffer.append(DesensitizedWords.desensitize(word, builder.toString()));
            }
            buffer.append(messagePattern, previous, messagePattern.length());
        }

        static void formatMessage3(final StringBuilder buffer, final char[] messagePattern, final int patternLength,
                                   final Object[] arguments, final int argCount, final int[] indices) {
            if (messagePattern == null) {
                return;
            }
            if (arguments == null || argCount == 0) {
                buffer.append(messagePattern);
                return;
            }
            int previous = 0;
            for (int i = 0; i < argCount; i++) {
                buffer.append(messagePattern, previous, indices[i]);
                previous = indices[i] + 2;
                recursiveDeepToString(arguments[i], buffer, null);
            }
            buffer.append(messagePattern, previous, patternLength);
        }

        static void formatMessage(final StringBuilder buffer, final String messagePattern,
                                  final Object[] arguments, final int argCount) {
            if (messagePattern == null || arguments == null || argCount == 0) {
                buffer.append(messagePattern);
                return;
            }
            int escapeCounter = 0;
            int currentArgument = 0;
            int i = 0;
            final int len = messagePattern.length();
            for (; i < len - 1; i++) { // last char is excluded from the loop
                final char curChar = messagePattern.charAt(i);
                if (curChar == ESCAPE_CHAR) {
                    escapeCounter++;
                } else {
                    if (isDelimPair(curChar, messagePattern, i)) { // looks ahead one char
                        i++;

                        // write escaped escape chars
                        writeEscapedEscapeChars(escapeCounter, buffer);

                        if (isOdd(escapeCounter)) {
                            // i.e. escaped: write escaped escape chars
                            writeDelimPair(buffer);
                        } else {
                            // unescaped
                            writeArgOrDelimPair(arguments, argCount, currentArgument, buffer);
                            currentArgument++;
                        }
                    } else {
                        handleLiteralChar(buffer, escapeCounter, curChar);
                    }
                    escapeCounter = 0;
                }
            }
            handleRemainingCharIfAny(messagePattern, len, buffer, escapeCounter, i);
        }

        private static boolean isDelimPair(final char curChar, final String messagePattern, final int curCharIndex) {
            return curChar == DELIM_START && messagePattern.charAt(curCharIndex + 1) == DELIM_STOP;
        }


        private static void handleRemainingCharIfAny(final String messagePattern, final int len,
                                                     final StringBuilder buffer, final int escapeCounter, final int i) {
            if (i == len - 1) {
                final char curChar = messagePattern.charAt(i);
                handleLastChar(buffer, escapeCounter, curChar);
            }
        }

        private static void handleLastChar(final StringBuilder buffer, final int escapeCounter, final char curChar) {
            if (curChar == ESCAPE_CHAR) {
                writeUnescapedEscapeChars(escapeCounter + 1, buffer);
            } else {
                handleLiteralChar(buffer, escapeCounter, curChar);
            }
        }


        private static void handleLiteralChar(final StringBuilder buffer, final int escapeCounter, final char curChar) {

            writeUnescapedEscapeChars(escapeCounter, buffer);
            buffer.append(curChar);
        }

        private static void writeDelimPair(final StringBuilder buffer) {
            buffer.append(DELIM_START);
            buffer.append(DELIM_STOP);
        }

        private static boolean isOdd(final int number) {
            return (number & 1) == 1;
        }


        private static void writeEscapedEscapeChars(final int escapeCounter, final StringBuilder buffer) {
            final int escapedEscapes = escapeCounter >> 1; // divide by two
            writeUnescapedEscapeChars(escapedEscapes, buffer);
        }


        private static void writeUnescapedEscapeChars(int escapeCounter, final StringBuilder buffer) {
            while (escapeCounter > 0) {
                buffer.append(ESCAPE_CHAR);
                escapeCounter--;
            }
        }

        private static void writeArgOrDelimPair(final Object[] arguments, final int argCount, final int currentArgument,
                                                final StringBuilder buffer) {
            if (currentArgument < argCount) {
                recursiveDeepToString(arguments[currentArgument], buffer, null);
            } else {
                writeDelimPair(buffer);
            }
        }

        static String deepToString(final Object o) {
            if (o == null) {
                return null;
            }
            if (o instanceof String) {
                return (String) o;
            }
            final StringBuilder str = new StringBuilder();
            final Set<String> dejaVu = new HashSet<>(); // that's actually a neat name ;)
            recursiveDeepToString(o, str, dejaVu);
            return str.toString();
        }

        private static void recursiveDeepToString(final Object o, final StringBuilder str, final Set<String> dejaVu) {
            if (appendSpecialTypes(o, str)) {
                return;
            }
            if (isMaybeRecursive(o)) {
                appendPotentiallyRecursiveValue(o, str, dejaVu);
            } else {
                tryObjectToString(o, str);
            }
        }

        private static boolean appendSpecialTypes(final Object o, final StringBuilder str) {
            if (o == null || o instanceof String) {
                str.append((String) o);
                return true;
            } else if (o instanceof CharSequence) {
                str.append((CharSequence) o);
                return true;
            } else if (o instanceof StringBuilderFormattable) {
                ((StringBuilderFormattable) o).formatTo(str);
                return true;
            } else if (o instanceof Integer) { // LOG4J2-1415 unbox auto-boxed primitives to avoid calling toString()
                str.append(((Integer) o).intValue());
                return true;
            } else if (o instanceof Long) {
                str.append(((Long) o).longValue());
                return true;
            } else if (o instanceof Double) {
                str.append(((Double) o).doubleValue());
                return true;
            } else if (o instanceof Boolean) {
                str.append(((Boolean) o).booleanValue());
                return true;
            } else if (o instanceof Character) {
                str.append(((Character) o).charValue());
                return true;
            } else if (o instanceof Short) {
                str.append(((Short) o).shortValue());
                return true;
            } else if (o instanceof Float) {
                str.append(((Float) o).floatValue());
                return true;
            }
            return appendDate(o, str);
        }

        private static boolean appendDate(final Object o, final StringBuilder str) {
            if (!(o instanceof Date)) {
                return false;
            }
            final Date date = (Date) o;
            final SimpleDateFormat format = getSimpleDateFormat();
            str.append(format.format(date));
            return true;
        }

        private static SimpleDateFormat getSimpleDateFormat() {
            SimpleDateFormat result = threadLocalSimpleDateFormat.get();
            if (result == null) {
                result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                threadLocalSimpleDateFormat.set(result);
            }
            return result;
        }

        private static boolean isMaybeRecursive(final Object o) {
            return o.getClass().isArray() || o instanceof Map || o instanceof Collection;
        }

        private static void appendPotentiallyRecursiveValue(final Object o, final StringBuilder str,
                                                            final Set<String> dejaVu) {
            final Class<?> oClass = o.getClass();
            if (oClass.isArray()) {
                appendArray(o, str, dejaVu, oClass);
            } else if (o instanceof Map) {
                appendMap(o, str, dejaVu);
            } else if (o instanceof Collection) {
                appendCollection(o, str, dejaVu);
            }
        }

        private static void appendArray(final Object o, final StringBuilder str, Set<String> dejaVu,
                                        final Class<?> oClass) {
            if (oClass == byte[].class) {
                str.append(Arrays.toString((byte[]) o));
            } else if (oClass == short[].class) {
                str.append(Arrays.toString((short[]) o));
            } else if (oClass == int[].class) {
                str.append(Arrays.toString((int[]) o));
            } else if (oClass == long[].class) {
                str.append(Arrays.toString((long[]) o));
            } else if (oClass == float[].class) {
                str.append(Arrays.toString((float[]) o));
            } else if (oClass == double[].class) {
                str.append(Arrays.toString((double[]) o));
            } else if (oClass == boolean[].class) {
                str.append(Arrays.toString((boolean[]) o));
            } else if (oClass == char[].class) {
                str.append(Arrays.toString((char[]) o));
            } else {
                if (dejaVu == null) {
                    dejaVu = new HashSet<>();
                }
                // special handling of container Object[]
                final String id = identityToString(o);
                if (dejaVu.contains(id)) {
                    str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
                } else {
                    dejaVu.add(id);
                    final Object[] oArray = (Object[]) o;
                    str.append('[');
                    boolean first = true;
                    for (final Object current : oArray) {
                        if (first) {
                            first = false;
                        } else {
                            str.append(", ");
                        }
                        recursiveDeepToString(current, str, new HashSet<>(dejaVu));
                    }
                    str.append(']');
                }
                //str.append(Arrays.deepToString((Object[]) o));
            }
        }

        private static void appendMap(final Object o, final StringBuilder str, Set<String> dejaVu) {
            // special handling of container Map
            if (dejaVu == null) {
                dejaVu = new HashSet<>();
            }
            final String id = identityToString(o);
            if (dejaVu.contains(id)) {
                str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
            } else {
                dejaVu.add(id);
                final Map<?, ?> oMap = (Map<?, ?>) o;
                str.append('{');
                boolean isFirst = true;
                for (final Object o1 : oMap.entrySet()) {
                    final Map.Entry<?, ?> current = (Map.Entry<?, ?>) o1;
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        str.append(", ");
                    }
                    final Object key = current.getKey();
                    final Object value = current.getValue();
                    recursiveDeepToString(key, str, new HashSet<>(dejaVu));
                    str.append('=');
                    recursiveDeepToString(value, str, new HashSet<>(dejaVu));
                }
                str.append('}');
            }
        }

        private static void appendCollection(final Object o, final StringBuilder str, Set<String> dejaVu) {
            // special handling of container Collection
            if (dejaVu == null) {
                dejaVu = new HashSet<>();
            }
            final String id = identityToString(o);
            if (dejaVu.contains(id)) {
                str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
            } else {
                dejaVu.add(id);
                final Collection<?> oCol = (Collection<?>) o;
                str.append('[');
                boolean isFirst = true;
                for (final Object anOCol : oCol) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        str.append(", ");
                    }
                    recursiveDeepToString(anOCol, str, new HashSet<>(dejaVu));
                }
                str.append(']');
            }
        }

        private static void tryObjectToString(final Object o, final StringBuilder str) {
            // it's just some other Object, we can only use toString().
            try {
                str.append(o.toString());
            } catch (final Throwable t) {
                handleErrorInObjectToString(o, str, t);
            }
        }

        private static void handleErrorInObjectToString(final Object o, final StringBuilder str, final Throwable t) {
            str.append(ERROR_PREFIX);
            str.append(identityToString(o));
            str.append(ERROR_SEPARATOR);
            final String msg = t.getMessage();
            final String className = t.getClass().getName();
            str.append(className);
            if (!className.equals(msg)) {
                str.append(ERROR_MSG_SEPARATOR);
                str.append(msg);
            }
            str.append(ERROR_SUFFIX);
        }

        static String identityToString(final Object obj) {
            if (obj == null) {
                return null;
            }
            return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
        }

    }
}
