package mx.itson.gpssimulator.WebService.Interfaces;

import java.util.List;

import mx.itson.gpssimulator.Entidades.Location;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LocationJsonAPI {

    @GET("locations")
    Call<List<Location>> getLocations();

    @POST("location")
    Call<Location> postLocation(@Body Location location);

}
