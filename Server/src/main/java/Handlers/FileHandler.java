package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

import Request.EventRequest;
import Response.EventResponse;
import Services.EventService;

public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        String Root_Path = "C:/Users/bradl/Documents/CS_240/FamilyMapServer/Server/web";
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {


                String uri = exchange.getRequestURI().getPath();

                if (uri.length()==0 || uri.equals("/")){
                    uri = uri + "/index.html";
                }

                String path = Root_Path + uri;

                File file = new File(path);

                if (file.exists() && Files.isReadable(file.toPath())){

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    OutputStream respBody = exchange.getResponseBody();

                    Files.copy(file.toPath(), respBody);

                    respBody.close();

                    success = true;

                }


            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }
}
