package mx.itson.gpssimulator;

import android.content.Context;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class UbicacionWS {

    private RequestQueue requestQueue;
    private String URL = "http://192.168.0.17/potrobus/public/location/location/";

    public UbicacionWS(){

    }

    public void update(final Location location, final Context context)  {
        requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", location.getLatitude());
            jsonObject.put("lng", location.getLongitude());
            System.out.println("Data sending : " + jsonObject.toString());
            JsonObjectRequest putReq = new JsonObjectRequest(Request.Method.PUT, URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());
                }
            }, null);
            System.out.println("Content-Type: "+putReq.getBodyContentType());
            System.out.println("Body: "+ new String(putReq.getBody()));
            requestQueue.add(putReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
