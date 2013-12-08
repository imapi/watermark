package com.imapi.watermark.transformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imapi.watermark.domain.Book;
import com.imapi.watermark.domain.Document;
import com.imapi.watermark.domain.Journal;
import spark.ResponseTransformerRoute;

public abstract class JsonResponseTransformer extends ResponseTransformerRoute {

    private static final RuntimeTypeAdapterFactory<Document> rta = RuntimeTypeAdapterFactory
            .of(Document.class, "type")
            .registerSubtype(Journal.class, "journal")
            .registerSubtype(Book.class, "book");

    private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(rta).create();

    protected JsonResponseTransformer(String path, String acceptType) {
        super(path, acceptType);
    }

    @Override
    public String render(Object object) {
        return GSON.toJson(object);
    }

    public Document fromJson(String input) {
        Document document = GSON.fromJson(input, Document.class);
        return document;
    }
}
