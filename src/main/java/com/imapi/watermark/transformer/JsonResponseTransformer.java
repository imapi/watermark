package com.imapi.watermark.transformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imapi.watermark.domain.Book;
import com.imapi.watermark.domain.Document;
import com.imapi.watermark.domain.Journal;
import spark.ResponseTransformerRoute;

/**
 * JSON request/response transformer. Internally uses GSON.
 * All additional domain classes should be registered here for correct serialization.
 */
public abstract class JsonResponseTransformer extends ResponseTransformerRoute {

    private static final RuntimeTypeAdapterFactory<Document> rta = RuntimeTypeAdapterFactory
            .of(Document.class, "type")
            .registerSubtype(Journal.class, "journal")
            .registerSubtype(Book.class, "book");

    private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(rta).create();

    /**
     * {@inheritDoc}
     */
    protected JsonResponseTransformer(String path, String acceptType) {
        super(path, acceptType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Helper for deserialization from JSON.
     *
     * @param input String json
     * @return Object of supertype Document
     */
    public Document fromJson(String input) {
        return GSON.fromJson(input, Document.class);
    }
}
