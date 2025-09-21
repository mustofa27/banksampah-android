package com.mustofa27.banksampah.model.datasource.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mustofa27.banksampah.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class ConnectionHandler {
    private static ConnectionHandler instance = null;
    private static Gson gson = null;

    public static ConnectionHandler getInstance(Context context) {
        if (instance == null) {
            instance = new ConnectionHandler(context);
        }
        return instance;
    }

    public static Gson getGson(){
        if(gson == null){
            gson = new GsonBuilder().create();
        }
        return gson;
    }

    HurlStack hurlStack;
    public static final int CONNECTION_TIMEOUT = 1000 * 90;
    public static String IMAGE_URL = "https://rnp.salesapp.my.id/";
    public String BASE_URL = "https://rnp.salesapp.my.id/api/",
            IMAGE_NEWS_URL = "http://pmpsmart.com/image_upload/news/",
            web = "http://pmpsmart.com/",
            response_message_success = "succes", response_message_error = "error", response_data = "data",
            response_message = "message", response_status = "status", response_pagination = "pagination",
            no_inet_error = "no internet error", auth_error = "authentication failed";

    public static int post_method = Request.Method.POST, get_method = Request.Method.GET, delete_method = Request.Method.DELETE;
    Context context;

    private ConnectionHandler(Context context) {
        this.context = context;
        hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };
        //hurlStack = new HurlStack();
    }

    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        /*CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getResources().openRawResource(R.raw.server); // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
        */

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        return sslContext.getSocketFactory();
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }
                }
        };
    }

    public void MakeConnection(int method, String route, JSONObject prop, Map<String, String> header, final JsonCallback jsonCallback) {
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(method, BASE_URL + route, prop, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonCallback.Done(response, response_message_success);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String tmp = error.networkResponse == null ? "No network" : new String(error.networkResponse.data);
                String msg = tmp;
                JSONObject jsonObject = null;
                if(tmp.charAt(0) == '{' && tmp.charAt(tmp.length()-1) == '}'){
                    try {
                        jsonObject = new JSONObject(tmp);
                        if (error instanceof NetworkError || error instanceof NoConnectionError) {
                            msg = no_inet_error;
                        } else if (error instanceof AuthFailureError) {
                            msg = auth_error;
                        } else {
                            msg = "network error";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else{
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message", msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                jsonCallback.Done(jsonObject, msg);
            }
        }, header);
        RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
        RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }

    public void MakeConnection(int method, String route, Map<String, Object> param, Map<String, MultipartFile> multipartFileMap
            , Map<String, String> header, final JsonCallback jsonCallback) {
        MultipartRequest multipartRequest = new MultipartRequest(method, BASE_URL + route, param, multipartFileMap
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonCallback.Done(response, response_message_success);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String tmp = error.networkResponse == null ? "No network" : new String(error.networkResponse.data);
                String msg = tmp;
                JSONObject jsonObject = null;
                if(tmp.charAt(0) == '{' && tmp.charAt(tmp.length()-1) == '}'){
                    try {
                        jsonObject = new JSONObject(tmp);
                        if (error instanceof NetworkError || error instanceof NoConnectionError) {
                            msg = no_inet_error;
                        } else if (error instanceof AuthFailureError) {
                            msg = auth_error;
                        } else {
                            msg = "network error";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else{
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message", msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                jsonCallback.Done(jsonObject, msg);
            }
        }, header);
        RequestQueue requestQueue = Volley.newRequestQueue(context, hurlStack);
        RetryPolicy policy = new DefaultRetryPolicy(CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        requestQueue.add(multipartRequest);
    }
}
