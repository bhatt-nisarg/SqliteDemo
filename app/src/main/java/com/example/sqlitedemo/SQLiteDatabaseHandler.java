package com.example.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {


    //DB VERSION
    private static final int DATABASE_VERSION = 1;

    //DB NAME
    private static final String DATABASE_NAME = "countryData";

    //Country table data
    private static final String TABLE_COUNTRY = "Country";

    //Country table column name
    private static final String KEY_ID = "id";
    private static final String COUNTRY_NAME = "CountryName";
    private static final String POPULATION = "Population";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //CREATE TABLE
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_COUNTRY_TABLE = " CREATE TABLE " + TABLE_COUNTRY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + COUNTRY_NAME + " TEXT,"
                + POPULATION + " LONG" + ")";
        sqLiteDatabase.execSQL(CREATE_COUNTRY_TABLE);
    }

    //UPGRADING TABLE
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" drop table if exists " + TABLE_COUNTRY);
        onCreate(sqLiteDatabase);
    }

    //adding new country
    void addCountry(country country) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNTRY_NAME, country.getCountryName());//country name
        contentValues.put(POPULATION, country.getPopulation()); // population
        sqLiteDatabase.insert(TABLE_COUNTRY, null, contentValues);
        sqLiteDatabase.close(); // closing db connection

    }

//    //getting single country name
//    country getCountry(int id) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursor = sqLiteDatabase.query(TABLE_COUNTRY, new String[]{KEY_ID, COUNTRY_NAME, POPULATION}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        country country = new country(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getLong(2));
//        //return country
//        return country;
//    }

    //getting all countries
    public List getAllCountries() {
        List countryList = new ArrayList();
        //select all query
        String selectQuery = " select * from " + TABLE_COUNTRY;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {
                country country = new country();
                country.setId(Integer.parseInt(cursor.getString(0)));
                country.setCountryName((cursor.getString(1)));
                country.setPopulation((cursor.getLong(2)));

                //Adding country to list
                countryList.add(country);
            } while (cursor.moveToNext());
        }
        //return country list
        return countryList;
    }

    //updating single country
    public int updateCountry(country country) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNTRY_NAME, country.getCountryName());
        contentValues.put(POPULATION, country.getPopulation());

        //updating row
        return sqLiteDatabase.update(TABLE_COUNTRY, contentValues, KEY_ID + "=?", new String[]{String.valueOf(country.getId())});
    }

    //deleting single country
    public void deleteCountry(country country) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_COUNTRY, KEY_ID + " = ?", new String[]{String.valueOf(country.getId())});
        sqLiteDatabase.close();
    }
    //deleting all countries
//    public void deleteAllCountries(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.delete(TABLE_COUNTRY,null,null);
//        sqLiteDatabase.close();
//    }
    //Getting Countries count
//    public int getCountriesCount(){
//        String countQuery = "select * from " + TABLE_COUNTRY;
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(countQuery,null);
//        cursor.close();
//
//        //return count
//        return cursor.getCount();
//    }

}
