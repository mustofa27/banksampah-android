package com.qiratek.rnpsales.model.datasource.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CustomJsonRequest extends JsonObjectRequest {
    private JSONObject mRequestObject;
    private Response.Listener<JSONObject> mResponseListener;
    private Map<String, String> header;

    public CustomJsonRequest(int method, String url, JSONObject requestObject, Response.Listener<JSONObject> responseListener,
                             Response.ErrorListener errorListener, Map<String, String> header) {
        super(method, url, requestObject, responseListener, errorListener);
        mResponseListener = responseListener;
        mRequestObject = requestObject;
        this.header = header;
        if(header == null){
            this.header = new HashMap<>();
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            try {
                if(json.substring(0,1).equalsIgnoreCase("[")){
                    JSONArray jsonArray = new JSONArray(json);
                    return Response.success(jsonArray.getJSONObject(0),
                            HttpHeaderParser.parseCacheHeaders(response));
                } else if(json.substring(0,1).equalsIgnoreCase("{")){
                    return Response.success(new JSONObject(json),
                            HttpHeaderParser.parseCacheHeaders(response));
                } else{
                    return Response.error(new VolleyError(new NetworkResponse(json.getBytes())));
                }
            } catch (JSONException e) {
                return Response.error(new ParseError(e));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mResponseListener.onResponse(response);
    }
}
