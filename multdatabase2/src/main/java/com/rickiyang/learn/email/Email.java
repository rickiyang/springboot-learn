package com.rickiyang.learn.email;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yangyue
 * @Modified by:
 * @Description:
 **/
public class Email implements Serializable {

    private static final long serialVersionUID = -153944622405879676L;

    /**
     * 收件人
     */
    private List<String> recipient;

    /**
     * 抄送
     */
    private List<String> copyTo;

    /**
     * 主题
     */
    private String theme;

    /**
     * 内容
     */
    private String content;


    public List<String> getRecipient() {
        return recipient;
    }

    public void setRecipient(List<String> recipient) {
        this.recipient = recipient;
    }

    public List<String> getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(List<String> copyTo) {
        this.copyTo = copyTo;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Email [recipient=" + recipient + ", copyTo=" + copyTo + ", theme=" + theme + ", content=" + content
                + "]";
    }

}
