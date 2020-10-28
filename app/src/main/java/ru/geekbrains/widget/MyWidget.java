package ru.geekbrains.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.widget.model.Currency;
import ru.geekbrains.widget.net.JsonAPI;

public class MyWidget extends AppWidgetProvider {

    private final static String ExtraMsg = "msg";
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        // Здесь обновим текст, будем показывать номер виджета
        views.setTextViewText(R.id.appwidget_text, "Refresh please!");

        Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(ACTION_WIDGET_RECEIVER);
        intent.putExtra("message_key", "");

        PendingIntent actionPendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.btnWidget, actionPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        getCurrencyFromServer(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateCurrencyFromServer(context, appWidgetManager);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        final String message = "";
        if (ACTION_WIDGET_RECEIVER.equals(action)){
            getCurrencyFromServer(context);
        }

        super.onReceive(context, intent);
    }

    private void getCurrencyFromServer(Context context) {
        try {
            String url = "http://api.currencylayer.com/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonAPI api = retrofit.create(JsonAPI.class);
            api.getCurrency("USD,RUB").enqueue(new Callback<Currency>() {
                @Override
                public void onResponse(Call<Currency> call, Response<Currency> response) {

                    if (response.body() != null) {
                        String result = "1 USD = "+response.body().getQuotes().getUSDRUB() + " Р";
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
                        ComponentName thisWidget = new ComponentName(context, MyWidget.class);
                        remoteViews.setTextViewText(R.id.appwidget_text,  result);
                        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
                    }
                }

                @Override
                public void onFailure(Call<Currency> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCurrencyFromServer(Context context, AppWidgetManager appWidgetManager) {
        try {
            String url = "http://api.currencylayer.com/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonAPI api = retrofit.create(JsonAPI.class);
            api.getCurrency("USD,RUB").enqueue(new Callback<Currency>() {
                @Override
                public void onResponse(Call<Currency> call, Response<Currency> response) {
                    if (response.body() != null) {
                        String result = "1 USD = " + response.body().getQuotes().getUSDRUB() + " Р";
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
                        ComponentName thisWidget = new ComponentName(context, MyWidget.class);
                        remoteViews.setTextViewText(R.id.appwidget_text, result);
                        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
                    }
                }

                @Override
                public void onFailure(Call<Currency> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

