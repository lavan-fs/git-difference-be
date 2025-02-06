package dev.lavan.git.difference.Models;

import org.springframework.stereotype.Component;

@Component
public class FileDetail {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
