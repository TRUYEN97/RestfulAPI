/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restapi;

import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class Response {

    private final int code;
    private final String response;
    private JSONObject json;

    public Response(int code, String response) {
        this.code = code;
        this.response = response;
        try {
            this.json = JSONObject.parseObject(response);
        } catch (Exception e) {
        }
    }

    public int getResponseCode() {
        return code;
    }
    
    public String getMessage(){
        if (this.json == null) {
            return null;
        }
        String value = this.json.getString("message");
        return value == null ? this.json.getString("Message"): value;
    }
    
    public <T> T getData(){
        if (this.json == null) {
            return null;
        }
        Object value = this.json.get("data");
        if (value == null) {
            return null;
        }
        return (T) value;
    }

    public String getResponse() {
        return response;
    }

    public boolean getStatus() {
        return this.json != null && (getBoolean("result", false) 
                || getStringEqualsIgnoreCase("result", "pass"));
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            return json.getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public boolean getStringEquals(String key, String target) {
        try {
            String value = this.json.getString(key);
            return value != null && value.equals(target);
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean getStringEqualsIgnoreCase(String key, String target) {
        try {
            String value = this.json.getString(key);
            return value != null && value.equalsIgnoreCase(target);
        } catch (Exception e) {
            return false;
        }
    }

}
