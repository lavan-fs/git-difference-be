package dev.lavan.git.difference.Models;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class Hunk {
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<LineChange> getLines() {
        return lines;
    }

    public void setLines(List<LineChange> lines) {
        this.lines = lines;
    }

    private String header;
    private List<LineChange> lines;
}
