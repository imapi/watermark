package com.imapi.watermark.domain;

import java.util.HashMap;
import java.util.Map;

public abstract class Document implements Watermarkable<Map<String, String>> {
    private String title;
    private String author;
    private Map<String, String> watermark;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getWatermark() {
        return watermark;
    }

    public void setWatermark(Map<String, String> watermark) {
        this.watermark = watermark;
    }

    @Override
    public Map<String, String> generateWatermark() {
        Map<String, String> watermark = new HashMap<String, String>(3);
        watermark.put("title", title);
        watermark.put("author", author);
        return watermark;
    }
}
