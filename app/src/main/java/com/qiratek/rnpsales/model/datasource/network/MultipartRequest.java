package com.qiratek.rnpsales.model.datasource.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends JsonObjectRequest {

    private final String BOUNDARY = "-Multipart-" + "-" + String.valueOf(System.currentTimeMillis());
    private final String HYPENS = "--";
    private final String ENDL = "\r\n";

    private Map<String, Object> mRequestObject;
    private Response.Listener<JSONObject> mResponseListener;
    private Map<String, String> header;
    private Map<String, MultipartFile> multipartFileMap;

    public MultipartRequest(int method, String url, Map<String, Object> param, Map<String, MultipartFile> multipartFileMap, Response.Listener<JSONObject> responseListener,
                            Response.ErrorListener errorListener, Map<String, String> header) {
        super(method, url, null, responseListener, errorListener);
        mResponseListener = responseListener;
        mRequestObject = param;
        this.multipartFileMap = multipartFileMap;
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
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            try {
                return Response.success(new JSONObject(json),
                        HttpHeaderParser.parseCacheHeaders(response));
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

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            for (Map.Entry<String, Object> textEntry : mRequestObject.entrySet()) {
                dataOutputStream.writeBytes(HYPENS + BOUNDARY + ENDL);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + textEntry.getKey() + "\""
                        + ENDL + ENDL);
                dataOutputStream.writeBytes(textEntry.getValue() + ENDL);
            }

            for (Map.Entry<String, MultipartFile> fileEntry : multipartFileMap.entrySet()) {
                if(fileEntry.getValue() != null) {
                    dataOutputStream.writeBytes(HYPENS + BOUNDARY + ENDL);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey() + "\"; " +
                            "filename=\"" + fileEntry.getValue().getFilename() + "\"" + ENDL);
                    dataOutputStream.writeBytes("Content-Type: " + fileEntry.getValue().getMimeType() + ENDL + ENDL);
                    dataOutputStream.write(fileEntry.getValue().getPayload());
                    dataOutputStream.writeBytes(ENDL);
                }
            }
            dataOutputStream.writeBytes(HYPENS + BOUNDARY + HYPENS + ENDL);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
