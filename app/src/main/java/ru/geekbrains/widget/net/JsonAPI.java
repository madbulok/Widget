package ru.geekbrains.widget.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.widget.model.Currency;

public interface JsonAPI {

    @GET("live?access_key=b7b5f17ec9aa0d3c74ea94acf208cf0c&format=1")
    Call<Currency> getCurrency(@Query("currencies") String currencies);

}
