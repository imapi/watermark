package com.imapi.watermark.service;

import com.imapi.watermark.Config;
import com.imapi.watermark.domain.Document;
import com.imapi.watermark.domain.Watermarkable;
import org.apache.commons.configuration.Configuration;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Main Watermark service API class which should be used for watermark computations.
 */
public class WatermarkService {

    /**
     * Through this method a document could be submitted to processing.
     *
     * @param document Document instance
     * @return Unique job id
     */
    public static UUID submit(final Document document) {
        final UUID uuid = UUID.randomUUID();
        if (document.getWatermark().isEmpty()) {
            QUEUE.put(uuid, new WatermarkFrame(uuid, document, Status.WAITING));
        } else {
            QUEUE.put(uuid, new WatermarkFrame(uuid, document, Status.DONE));
        }
        return uuid;
    }

    /**
     * Used for checking periodically the progress of watermark job.
     * When status will turn to {@link Status#DONE} {@link #pick} method will return watermarked document.
     *
     * @param uuid Unique id of the job
     * @return Status of the job or {@link Status#NOT_FOUND} in case of wrong id
     */
    public static Status poll(UUID uuid) {
        WatermarkFrame frame = QUEUE.get(uuid);
        if (frame == null) return Status.NOT_FOUND;
        return frame.getStatus();
    }

    /**
     * Method for taking watermarked document.
     * Note, you should check that poll returns the correct status indicating finishing watermark processing.
     *
     * @param uuid Unique id of the job
     * @return Watermarked document, or alternatively if progress is not {@link Status#DONE} will
     *         return the same document as it sent to processing
     * @throws IllegalArgumentException in case of wrong id
     */
    public static Watermarkable pick(UUID uuid) {
        WatermarkFrame frame = QUEUE.get(uuid);
        if (frame == null) {
            throw new IllegalArgumentException("Wrong ticket id");
        }
        return frame.getWatermarkable();
    }

    private static final HTreeMap<UUID, WatermarkFrame> QUEUE;
    private static final Configuration CONF = Config.getConfig();

    static {
        DB db = createStorage().transactionDisable().closeOnJvmShutdown().make();
        QUEUE = db.createHashMap("queue").expireAfterAccess(CONF.getLong("storage.expire", 1), TimeUnit.HOURS).makeOrGet();
        new WorkersBootstrap(QUEUE).bind();
    }

    private static DBMaker createStorage() {
        if (CONF.getString("storage.type", "memory").equalsIgnoreCase("file")) {
            new File("temp").mkdir();
            return DBMaker.newAppendFileDB(new File("temp/data"));
        } else {
            return DBMaker.newMemoryDB();
        }
    }

    private WatermarkService() {
        //just static helper
    }
}
