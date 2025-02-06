package dev.lavan.git.difference.Models;

import org.springframework.stereotype.Component;

@Component

public class LineChange {
    private int baseLineNumber;
    private int headLineNumber;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public int getBaseLineNumber() {
        return baseLineNumber;
    }

    public void setBaseLineNumber(int baseLineNumber) {
        this.baseLineNumber = baseLineNumber;
    }

    public int getHeadLineNumber() {
        return headLineNumber;
    }

    public void setHeadLineNumber(int headLineNumber) {
        this.headLineNumber = headLineNumber;
    }
}
