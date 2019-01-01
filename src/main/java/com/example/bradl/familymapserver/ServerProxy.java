package com.example.bradl.familymapserver;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Models.Person;
import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Request.RegisterRequest;
import Response.EventResponse;
import Response.LoginResponse;
import Response.PersonResponse;
import Response.RegisterResponse;

public class ServerProxy {

    static String serverHost;
    static String serverPort;
    static String authToken;

    // The client's "main" method.
    // The "args" parameter should contain two command-line arguments:
    // 1. The IP address or domain name of the machine running the server
    // 2. The port number on which the server is accepting client connections


    public ServerProxy(String host, String port){
        serverHost = host;
        serverPort = port;
    }

    public void setAuthToken(String auth){
        authToken = auth;
    }

    // The getGameList method calls the server's "/games/list" operation to
    // retrieve a list of games running in the server in JSON format
    public static LoginResponse Login(LoginRequest lr) {

        // This method shows how to send a GET request to a server
        LoginResponse response = new LoginResponse();
        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("POST");
            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(true);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            // This is the JSON string we will send in the HTTP request body
            Gson gson = new Gson();

            String reqData = gson.toJson(lr);

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            // Write the JSON data to the request body
            writeString(reqData, reqBody);
            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();
                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);

                response = gson.fromJson(respData, LoginResponse.class);
                // Display the JSON data returned from the server
                return response;
            }
            else {
                response.setMessage(http.getResponseMessage());
                return response;
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
//                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            response.setMessage(e.getMessage());
            return response;
//            e.printStackTrace();
        }
    }

    public static RegisterResponse Register(RegisterRequest rR) {

        // This method shows how to send a GET request to a server
        RegisterResponse response = new RegisterResponse();
        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("POST");
            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(true);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            // This is the JSON string we will send in the HTTP request body
            Gson gson = new Gson();

            String reqData = gson.toJson(rR);

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            // Write the JSON data to the request body
            writeString(reqData, reqBody);
            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();
                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);

                response = gson.fromJson(respData, RegisterResponse.class);
                // Display the JSON data returned from the server
                return response;
            }
            else {
                response.setMessage(http.getResponseMessage());
                return response;
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
//                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            response.setMessage(e.getMessage());
            return response;
//            e.printStackTrace();
        }
    }

    public static PersonResponse getPeople(PersonRequest pR) {

        // This method shows how to send a GET request to a server
        PersonResponse response = new PersonResponse();

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("GET");
            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", pR.getAuthToken());

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();
            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();
                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);

                Gson gson = new Gson();
                response = gson.fromJson(respData, PersonResponse.class);

                return response;
                // Display the JSON data returned from the server
//                System.out.println(respData);
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                response.setMessage(http.getResponseMessage());

                return response;
//                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            response.setMessage(e.getMessage());

            return response;
//            e.printStackTrace();
        }
    }


    public static EventResponse getEvents(EventRequest eR) {

        // This method shows how to send a GET request to a server
        EventResponse response = new EventResponse();

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("GET");
            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", eR.getAuthToken());

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();
            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();
                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);

                Gson gson = new Gson();
                response = gson.fromJson(respData, EventResponse.class);

                return response;
                // Display the JSON data returned from the server
//                System.out.println(respData);
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                response.setErrorMessage(http.getResponseMessage());

                return response;
//                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            response.setErrorMessage(e.getMessage());

            return response;
//            e.printStackTrace();
        }
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException {
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
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
