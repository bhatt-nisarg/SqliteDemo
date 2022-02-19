package com.example.sqlitedemo;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomCountryList extends BaseAdapter {

    private final Activity context;
    ArrayList<country> countries;
    private PopupWindow pwindow;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    BaseAdapter baseAdapter;

    public CustomCountryList(Activity context, ArrayList countries,SQLiteDatabaseHandler sqLiteDatabaseHandler){
        this.countries = countries;
        this.context = context;
        this.sqLiteDatabaseHandler = sqLiteDatabaseHandler;
    }
    public static class ViewHolder{
        TextView textViewId;
        TextView textViewCountry;
        TextView textViewPopulation;
        Button editButton;
        Button deleteButton;
    }
    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            row = inflater.inflate(R.layout.row_item,null,true);
            viewHolder.textViewId = (TextView) row.findViewById(R.id.textViewId);
            viewHolder.textViewCountry = (TextView) row.findViewById(R.id.textViewCountry);
            viewHolder.textViewPopulation = (TextView) row.findViewById(R.id.textViewPopulation);
            viewHolder.deleteButton = (Button) row.findViewById(R.id.delete);
            viewHolder.editButton = (Button) row.findViewById(R.id.edit);

        //store the holder with view
            row.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textViewCountry.setText(countries.get(i).getCountryName());
        viewHolder.textViewId.setText("" +  countries.get(i).getId());
        viewHolder.textViewPopulation.setText("" + countries.get(i).getPopulation());

        final int positionPopup = i;
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("save : ","" + positionPopup);
                editPopup(positionPopup);
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Last Index :","" + positionPopup);

                sqLiteDatabaseHandler.deleteCountry(countries.get(positionPopup));
                countries = (ArrayList) sqLiteDatabaseHandler.getAllCountries();
                Log.d("Countries size ","" + countries.size());
                notifyDataSetChanged();
            }
        });
        return row;
    }
    public void editPopup(final int positionPopup){
        LayoutInflater inflater = context.getLayoutInflater();
        View layput = inflater.inflate(R.layout.edit_popup,(ViewGroup) context.findViewById(R.id.popup_element));
        pwindow = new PopupWindow(layput,600,670,true);
        pwindow.showAtLocation(layput, Gravity.CENTER,0,0);
        final EditText countryEdit = (EditText) layput.findViewById(R.id.editTextCountry);
        final EditText populationEdit = (EditText) layput.findViewById(R.id.editTextPopulation);

        countryEdit.setText(countries.get(positionPopup).getCountryName());
        populationEdit.setText("" +  countries.get(positionPopup).getPopulation());
        Button save = (Button) layput.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryStr = countryEdit.getText().toString();
                String population = populationEdit.getText().toString();
                if (!countryStr.isEmpty() && !population.isEmpty()) {
                    country country = countries.get(positionPopup);
                    country.setCountryName(countryStr);
                    country.setPopulation(Long.parseLong(population));
                    sqLiteDatabaseHandler.updateCountry(country);
                    countries = (ArrayList) sqLiteDatabaseHandler.getAllCountries();
                    notifyDataSetChanged();
                    for (country country1 : countries) {
                        String log = " Id : " + country1.getId() + ", Name :  " + country1.getCountryName() + ", Population : " + country1.getPopulation();
                        Log.d("Name :", log);
                    }
                    pwindow.dismiss();
                }else {
                    Toast.makeText(context.getApplicationContext(),"Enter Update Detail!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
