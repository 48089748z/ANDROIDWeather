package com.example.poblenou.eltemps;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

import com.example.poblenou.eltemps.json.Forecast;
import com.example.poblenou.eltemps.json.List;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.*;
import retrofit.*;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    ArrayAdapter<String> myAdapter;
    private ArrayList<String> items;
    private ListView miLista;
    private TextView misDias;
    private OpenWeatherMapService service;


    public MainActivityFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View fragmento = inflater.inflate(R.layout.fragment_main, container, false);

        items = new ArrayList<>();
        misDias = (TextView) fragmento.findViewById(R.id.TVdias);
        miLista = (ListView) fragmento.findViewById(R.id.LVmyList);
        myAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        miLista.setAdapter(myAdapter);
        miLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //items.remove(position);
                //myAdapter.notifyDataSetChanged();
                return true;
            }
        });
        myAdapter.add("Monday 19/10/2015: Cloudy");
        myAdapter.add("Tuesday 20/10/2015: Cloudy");
        myAdapter.add("Wednesday 21/10/2015: Cloudy");
        myAdapter.add("Thursday 22/10/2015: Cloudy");
        myAdapter.add("Friday 23/10/2015: Cloudy");
        myAdapter.add("Saturday 24/10/2015: Cloudy");
        myAdapter.add("Sunday 25/10/2015: Cloudy");

        return fragmento;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_fragment, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh)
        {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh()
    {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getContext());

        final String CITY = preferencias.getString("city", "Barcelona");
        final String SISTEMA = preferencias.getString("units", "metric");
        final int NUMDAYS = Integer.parseInt(preferencias.getString("numdays", "10"));

        final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/";
        final String APPID = "f3b53a805bc9ec413f57d26fdc30de46";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FORECAST_BASE_URL) //URL DEL JASON DEL TIEMPO EN BARCELONA
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(OpenWeatherMapService.class);

        Call<Forecast> call = service.dailyForecast(CITY, "json", SISTEMA, NUMDAYS, APPID);
        call.enqueue(new Callback<Forecast>()
        {
            @Override
            public void onResponse(Response<Forecast> response, Retrofit retrofit)
            {
                Forecast forecast = response.body();
                ArrayList<String> forecastStrings = new ArrayList<>();
                for (List list : forecast.getList())
                {
                    Date x = new Date(list.getDt()*1000);
                    String desc = list.getWeather().get(0).getDescription();
                    Long min = Math.round(list.getTemp().getMin());
                    Long max = Math.round(list.getTemp().getMax());
                    forecastStrings.add(String.valueOf(x)+" "+desc+" "+String.valueOf(min)+"º -"+String.valueOf(max)+"º");
                }
                myAdapter.clear();
                myAdapter.addAll(forecastStrings);
            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
    public interface OpenWeatherMapService
    {
        @GET("forecast/daily")
        Call<Forecast> dailyForecast(
                        @Query("q") String city,
                        @Query("mode") String format,
                        @Query("units") String units,
                        @Query ("cnt") Integer num,
                        @Query ("appid") String appid);
    }
}
