package com.imapi.watermark.service;
import com.imapi.watermark.domain.Document;
import org.pcollections.HashPMap;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WatermarkService {
    public static enum  Status {
        PROGRESS,
        READY,
        FAIL
    }

    private static ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

    private static void intialize() throws IOException {
        PMap<String, Document> QUEUE = HashTreePMap.empty();
        QUEUE.minus()
    }

    public static Long submit(Document document) {
        Long uuid = UUID.randomUUID().getMostSignificantBits();

        return uuid;
    }

    public static Status poll(Long uuid) {
        return Status.FAIL;
    }
}
