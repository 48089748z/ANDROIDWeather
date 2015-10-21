package com.example.poblenou.eltemps;

import android.content.ClipData;
import android.graphics.pdf.PdfRenderer;
import android.os.*;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

import com.example.poblenou.eltemps.json.Forecast;
import com.example.poblenou.eltemps.json.List;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;

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

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/") //URL DEL JASON DEL TIEMPO EN BARCELONA
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public interface OpenWeatherMapService
    {
        @GET("forecast/city?id=3128760&APPID=f3b53a805bc9ec413f57d26fdc30de46")
        Call<Forecast> getPrevisiones();
    }

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

        service = retrofit.create(OpenWeatherMapService.class);
        service.getPrevisiones();

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



        //System.out.println(service.getPrevisiones().toString());

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
            myAdapter.clear();
            Call<Forecast> call = service.getPrevisiones();
            call.enqueue(new Callback<Forecast>() {
                @Override
                public void onResponse(Response<Forecast> response, Retrofit retrofit) {
                    Forecast forecast = response.body();
                    for (List list : forecast.getList())
                    {
                        Long dt = list.getDt();
                        String desc = list.getWeather().get(0).getDescription();
                        Double min = list.getTemp().getMin();
                        Double max = list.getTemp().getMax();

                        //myAdapter.add(String.valueOf(Log.w("list", String.format("%s - %s - %s/%s", dt, desc, min, max))));
                        //Esto aun no funciona

                    }

                }
                @Override
                public void onFailure(Throwable t) {

                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
