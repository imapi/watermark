package com.imapi.watermark.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all documents (book, journals). Partially implements {@link Watermarkable} interface.
 * Used in serialization, therefore accessors/mutators added for all fields and default constructor.
 *
 * @author Ivan Bondarenko
 */
public abstract class Document implements Watermarkable {
    private String title;
    private String author;
    private Map<String, String> watermark = new HashMap<String, String>(3);

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(Map<String, String> watermark) {
        this.watermark = watermark;
    }

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

    /**
     * Generates watermark for this base document, helper method for the child classes.
     *
     * @return Partially constructed watermark
     */
    protected Map<String, String> generateBaseWatermark() {
        Map<String, String> watermark = new HashMap<String, String>(3);
        watermark.put("title", title);
        watermark.put("author", author);
        return watermark;
    }
}
