package mx.itson.gpssimulator;

import android.content.Context;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class UbicacionWS {

    private RequestQueue requestQueue;
    private String URL = "http://192.168.0.17/potrobus/public/location/location/";

    public UbicacionWS(){

    }

    public void update(final Location location, final Context context)  {
        requestQueue = Volley.newRequestQueue(context);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", location.getLatitude());
            jsonObject.put("lng", location.getLongitude());
            System.out.println("Data to send : " + jsonObject.toString());
            StringRequest postReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Response from server:");
                    System.out.println(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error while POST:");
                    error.printStackTrace();
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "Accept: application/json";
                }

                @Override
                public byte[] getBody() {
                    return jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                }
            };
            System.out.println(postReq.getBodyContentType());
            requestQueue.add(postReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
