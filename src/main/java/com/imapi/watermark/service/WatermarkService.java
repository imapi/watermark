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
 * Created with IntelliJ IDEA.
 * User: ivan_bondarenko
 * Date: 12/4/13
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class WatermarkService {

    private static final HTreeMap<UUID, WatermarkFrame> QUEUE;
    private static final Configuration conf = Config.getConfig();

    static {
        DB db = createStorage().transactionDisable().closeOnJvmShutdown().make();
        QUEUE = db.createHashMap("queue").expireAfterAccess(conf.getLong("storage.expire", 1), TimeUnit.HOURS).makeOrGet();
        new WorkersBootstrap(QUEUE).bind();
    }

    private static DBMaker createStorage() {
        if (conf.getString("storage.type", "memory").equalsIgnoreCase("file")) {
            new File("temp").mkdir();
            return DBMaker.newAppendFileDB(new File("temp/data"));
        } else {
            return DBMaker.newMemoryDB();
        }
    }

    private WatermarkService() {
    }

    public static UUID submit(final Document document) {
        final UUID uuid = UUID.randomUUID();
        if (document.getWatermark().isEmpty()) {
            QUEUE.put(uuid, new WatermarkFrame(uuid, document, Status.WAITING));
        } else {
            QUEUE.put(uuid, new WatermarkFrame(uuid, document, Status.DONE));
        }
        return uuid;
    }

    public static Status poll(UUID uuid) {
        WatermarkFrame frame = QUEUE.get(uuid);
        if (frame == null) return Status.NOT_FOUND;
        return frame.getStatus();
    }

    public static Watermarkable pick(UUID uuid) {
        WatermarkFrame frame = QUEUE.get(uuid);
        if (frame == null) {
            throw new IllegalArgumentException("Wrong ticket id");
        }
        return frame.getWatermarkable();
    }


}
