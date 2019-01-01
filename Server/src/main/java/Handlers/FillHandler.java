package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.EventRequest;
import Request.FillRequest;
import Response.EventResponse;
import Response.FillResponse;
import Services.EventService;
import Services.FillService;

public class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        FillResponse response;
        String jsonStr = null;
        try {

            if (exchange.getRequestMethod().toLowerCase().equals("post")) {


                String uri = exchange.getRequestURI().getPath();

                // split the string by "/" to see if there is an eventID
                String[] split = uri.split("/");

//                int gen = 4;
                String gen = "4";
                String userN = split[2];

                if (split.length == 4) {
                    // number of generations is specified
//                    gen = Integer.parseInt(split[3]);
                    gen = split[3];
                }


                FillService fService = new FillService();
                // make request object
                FillRequest fRequest = new FillRequest(userN, gen);
                response = fService.fill(fRequest);

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
        } catch (IOException e) {
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
