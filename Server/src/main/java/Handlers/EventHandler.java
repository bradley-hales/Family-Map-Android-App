package Handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.EventRequest;
import Response.EventResponse;
import Services.EventService;

public class EventHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        EventResponse response;
        String jsonStr = null;
        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow GET requests for this operation.
            // This operation requires a GET request, because the
            // client is "getting" information from the server, and
            // the operation is "read only" (i.e., does not modify the
            // state of the server).
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {

                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");
                    String uri = exchange.getRequestURI().getPath();

                    // split the string by "/" to see if there is an eventID
                    String[] split = uri.split("/");


                    if (split.length == 3){
                        // get eventID
                        String eID =  split[2];
                        EventService eService = new EventService();
                        // make request object
                        EventRequest eRequest = new EventRequest(eID, authToken);
                        response = eService.getEvent(eRequest);
                    }
                    else{
                        EventService eService = new EventService();
                        // make request object
                        EventRequest eRequest = new EventRequest(authToken);
                        response = eService.getEvents(eRequest);
                    }

                    Gson gson = new Gson();

                    // Convert object to JSON string
                    jsonStr = gson.toJson(response);


                    // Start sending the HTTP response to the client, starting with
                    // the status code and any defined headers.
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    // Now that the status code and headers have been sent to the client,
                    // next we send the JSON data in the HTTP response body.

                    // Get the response body output stream.
                    OutputStream respBody = exchange.getResponseBody();
                    // Write the JSON string to the output stream.
                    writeString(jsonStr, respBody);
                    // Close the output stream.  This is how Java knows we are done
                    // sending data and the response is complete/
                    respBody.close();

                    success = true;

                }
            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                // Write the JSON string to the output stream.
                writeString(jsonStr, respBody);
                // Close the output stream.  This is how Java knows we are done
                // sending data and the response is complete/
                respBody.close();
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            writeString(jsonStr, respBody);
            // Close the output stream.  This is how Java knows we are done
            // sending data and the response is complete/
            respBody.close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
