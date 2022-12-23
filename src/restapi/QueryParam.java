package restapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Administrator
 */
public class QueryParam {
    
    private final List<String[]> params;

    public static QueryParam builder() {
        return new QueryParam();
    }

    private QueryParam() {
        this.params = new ArrayList<>();
    }

    public QueryParam addParam(String key, String... values) {
        if (key != null && values != null && values.length > 0) {
            for (String value : values) {
                this.params.add(new String[]{key, value});
            }
        }else if(values == null || values.length == 0){
            this.params.add(new String[]{key, null});
        }
        return this;
    }
    
    public String toURL(){
        StringBuilder builder = new StringBuilder("?");
        for (String[] elem : this.params) {
            builder.append(elem[0]);
            String value = elem[1];
            if (value != null) {
                builder.append("=").append(value);
            }
            builder.append("&");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString().trim();
    }

    @Override
    public String toString() {
        return toURL();
    }

}
