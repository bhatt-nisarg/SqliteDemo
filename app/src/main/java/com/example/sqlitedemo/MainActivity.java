package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<country> countries;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Button btnsubmit;
    PopupWindow pwindow;
    Activity activity;
    ListView listView;
    CustomCountryList customCountryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(this);
        listView =  findViewById(R.id.list);
        btnsubmit =  findViewById(R.id.btnSubmit);
        btnsubmit.setOnClickListener(view -> addPopUp());
        Log.d("MainActivity :", "Before Reading MainActivity ");
        countries = (ArrayList) sqLiteDatabaseHandler.getAllCountries();

        for (country country : countries){
            String log = "Id : " + country.getId() + ",Name : " +  country.getCountryName() + ", Population : " + country.getPopulation();
            Log.d("Name : ",log);
        }
        CustomCountryList customCountryList = new CustomCountryList(this,countries,sqLiteDatabaseHandler);
        listView.setAdapter(customCountryList);
        listView.setOnItemClickListener((adapterView, view, i, l) -> Toast.makeText(getApplicationContext(),"you selected" + countries.get(i) + "as Country ",Toast.LENGTH_SHORT).show());
    }
    public void addPopUp(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.edit_popup,(ViewGroup) activity.findViewById(R.id.popup_element));
        pwindow = new PopupWindow(layout,600,670,true);
        pwindow.showAtLocation(layout, Gravity.CENTER,0,0);
        final EditText CounrtyEdit = (EditText) layout.findViewById(R.id.editTextCountry);
        final EditText populationEdit = (EditText) layout.findViewById(R.id.editTextPopulation);

        Button save = (Button) layout.findViewById(R.id.save_popup);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String CountryStr = CounrtyEdit.getText().toString();
                String population = populationEdit.getText().toString();

                if (!CountryStr.isEmpty() && !population.isEmpty()) {
                    country country = new country(CountryStr, Long.parseLong(population));

                    sqLiteDatabaseHandler.addCountry(country);
                    if (customCountryList == null) {
                        customCountryList = new CustomCountryList(activity, countries, sqLiteDatabaseHandler);
                        listView.setAdapter(customCountryList);
                    }
                    customCountryList.countries = (ArrayList) sqLiteDatabaseHandler.getAllCountries();
                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                    for (country country1 : countries) {
                        String log = " Id :" + country1.getId() + ", Name : " + country1.getCountryName() + ",Population :" + country1.getPopulation();
                        Log.d("Name : ", log);
                    }
                    pwindow.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),"Enter Detail!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}