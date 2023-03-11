/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package restapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class RestAPI {

    public static final String AUTHORIZATION_KEY = "authorization";
    private String jwtToken;

    public static enum RequestMethod {
        GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
    };

    public void removeToken() {
        this.jwtToken = null;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Response sendGet(String url, QueryParam parmater) {
        return send(RequestMethod.GET, url, parmater, null);
    }

    public Response sendGet(String url) {
        return send(RequestMethod.GET, url, null, null);
    }

    public Response sendPost(String url, JsonBodyAPI body) {
        return sendJsonAPI(RequestMethod.POST, url, body);
    }
    
    public Response sendPost(String url, String body) {
        return sendJsonAPI(RequestMethod.POST, url, body);
    }

    public Response sendJsonAPI(RequestMethod method, String url, JsonBodyAPI body) {
        return send(method, url, null, body.toJSONString());
    }

    public Response sendJsonAPI(RequestMethod method, String url, String body) {
        return send(method, url, null, body);
    }

    public Response sendJsonAPI(RequestMethod method, String url, QueryParam queryParam, JsonBodyAPI jsonBodyAPI) {
        String body = jsonBodyAPI == null ? null : jsonBodyAPI.toJSONString();
        return send(method, url, queryParam, body);
    }

    protected Response send(RequestMethod method, String url, QueryParam parmater, String command) {
        if (method == null || url == null) {
            return null;
        }
        HttpURLConnection con = null;
        try {
            con = getConnection(method, url, parmater);
            if (!method.equals(RequestMethod.GET) && command != null) {
                sendCommand(con, command);
            }
            int code = con.getResponseCode();
            if (code >= 300) {
                return new Response(code, readError(con));
            } else {
                return new Response(code, readReponse(con));
            }
        } catch (IOException e) {
            return new Response(-1,
                    String.format("{\"Exception\":\"%s\"}", e.getMessage()));
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private HttpURLConnection getConnection(RequestMethod method, String url, QueryParam parmater) throws IOException {
        if (parmater != null) {
            return getHttpConncetion(method, String.format("%s%s", url, parmater));
        }
        return getHttpConncetion(method, url);
    }

    private HttpURLConnection getHttpConncetion(RequestMethod method, String url) throws ProtocolException, IOException {
        HttpURLConnection con;
        con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestMethod(method.name());
        con.setRequestProperty(AUTHORIZATION_KEY, jwtToken);
        return con;
    }

    private String readReponse(HttpURLConnection con) throws IOException {
        StringBuilder response;
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    private String readError(HttpURLConnection con) throws IOException {
        StringBuilder response;
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"))) {
            response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    private void sendCommand(HttpURLConnection con, String command) throws IOException {
        if (command == null) {
            return;
        }
        con.setDoOutput(true);
        try ( OutputStream os = con.getOutputStream()) {
            byte[] input = command.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        }
    }

}
