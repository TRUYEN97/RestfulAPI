/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restapi;

/**
 *
 * @author Administrator
 */
public class Response {
    private final int code;
    private final String response;

    public Response(int code, String response) {
        this.code = code;
        this.response = response;
    }

    public int getResponseCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }
    
    
}
