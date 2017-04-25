package bg.nemetschek.nbastats.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    public static Retrofit.Builder retrofit() {
        return new Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8080")
            .addConverterFactory(GsonConverterFactory.create());
    }
}
