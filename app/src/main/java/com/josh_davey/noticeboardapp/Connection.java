package com.josh_davey.noticeboardapp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//HTTP POST & GET Methods for communicating with external PHP scripts.
public class Connection {
    protected String connectionPost(URL url, JSONObject data) {
        try {
            //Sets the connection.
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            //Creates the output stream and buffered writer to write the string data to and send to the server.
            OutputStream oStream = con.getOutputStream();
            BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(oStream, "UTF-8"));

            //Converts data to string and writes it to the buffer ready to be sent.
            buffer.write(data.toString());
            //Closes the buffer. This automatically runs the .flush() method which sends the data.
            buffer.close();
            //Closes the stream. All data has been sent to the connected URL.
            oStream.close();

            //Gets response from the server. Reads inputstream and builds a string response.
            InputStream iStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);

            //Closes reader and inputstream.
            reader.close();
            iStream.close();

            //Returns string response from the server.
            return response.toString();
        } catch (Exception e) {
            return null;
        }
    }

    protected String connectionGet(URL url) {
        try {
            //Sets the connection.
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //Added timeouts to cater for extremely slow network connections.
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            //Gets response from the server. Reads inputstream and builds a string response.
            InputStream iStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);

            //Closes reader and inputstream.
            reader.close();
            iStream.close();

            //Returns string response from the server.
            return response.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
