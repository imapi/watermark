package com.imapi.watermark;

import com.imapi.watermark.domain.Document;
import com.imapi.watermark.service.Status;
import com.imapi.watermark.service.WatermarkService;
import com.imapi.watermark.transformer.JsonResponseTransformer;
import org.apache.commons.configuration.Configuration;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static spark.Spark.*;

/**
 *
 */
public class Server {

    private static final Configuration conf = Config.getConfig();

    public static void main(String[] args) {


        externalStaticFileLocation("static");

        setPort(conf.getInt("server.port", 8080));

        post(new JsonResponseTransformer("/documents", "application/json") {
            @Override
            public Object handle(Request request, Response response) {
                Document document;
                try {
                    document = fromJson(request.body());
                } catch (Exception ex) {
                    response.status(400);
                    return "Malformatted json object";
                }
                UUID id = WatermarkService.submit(document);
                response.status(201);
                return id;
            }
        });

        get(new JsonResponseTransformer("/documents/:id", "application/json") {
            @Override
            public Object handle(Request request, Response response) {
                UUID id;
                try {
                    id = UUID.fromString(request.params("id"));
                } catch (IllegalArgumentException ex) {
                    response.status(404);
                    return "Wrong id";
                }
                Status status = WatermarkService.poll(id);
                if (status == Status.DONE) {
                    return WatermarkService.pick(id);
                } else {
                    response.status(202);
                    return status;
                }
            }
        });
    }
}
