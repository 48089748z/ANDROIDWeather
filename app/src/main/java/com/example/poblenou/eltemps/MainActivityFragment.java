package com.example.poblenou.eltemps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    ArrayAdapter<String> myAdapter;
    private ArrayList<String> items;
    private ListView miLista;
    private TextView misDias;

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
                             Bundle savedInstanceState) {
        View fragmento = inflater.inflate(R.layout.fragment_main, container, false);

        items = new ArrayList<>();
        misDias = (TextView) fragmento.findViewById(R.id.TVdias);
        miLista = (ListView) fragmento.findViewById(R.id.LVmyList);
        myAdapter = new ArrayAdapter <String> (getContext(), android.R.layout.simple_list_item_1, items);
        miLista.setAdapter(myAdapter);
        miLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                myAdapter.notifyDataSetChanged();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_fragment, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
