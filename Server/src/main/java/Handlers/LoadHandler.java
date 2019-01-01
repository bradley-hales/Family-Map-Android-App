package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.LoadRequest;
import Request.LoginRequest;
import Response.LoadResponse;
import Response.LoginResponse;
import Services.LoadService;
import Services.LoginService;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        LoadResponse response;
        LoadRequest request;
        String jsonStr = null;

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                // Extract the JSON string from the HTTP request body

                // Get the request body input stream
                InputStream reqBody = exchange.getRequestBody();
                // Read JSON string from the input stream
                String reqData = readString(reqBody);

                Gson gson = new Gson();
                // Convert JSON string to object
                request = gson.fromJson(reqData, LoadRequest.class);

                // load data and get a response back
                LoadService load = new LoadService();
                response = load.load(request);

                // convert response into json string
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
        The readString method shows how to read a String from an InputStream.
    */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
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
