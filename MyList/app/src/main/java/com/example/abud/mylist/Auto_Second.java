package com.example.abud.mylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Auto_Second extends AppCompatActivity {
    ListView autoSecondList;
    ArrayList<String> shoppingList = new ArrayList<>();
    ArrayList<String> shoppingListCheck = new ArrayList<>();
    ArrayList<String> saveList = null;
    ArrayList<String> mySavedList = new ArrayList<>();
    ArrayAdapter<String> adapter = null;
    String db="ShopingList";
    String key="Values";
    String dbSave="SaveAuto";
    String savedListsKey="SaveAutoKey";
    String selectedItem;
    String savedListName;
    int i =0;
    String name;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto__second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shopping List");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        autoSecondList=(ListView)findViewById(R.id.listView_Auto);
        autoSecondList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Intent intent2 = getIntent();
        name = intent2.getStringExtra("theListName");
        i = intent2.getIntExtra("num",0);
        if(i==0) {
            shoppingList=getArrayVal(getApplicationContext(),db,key);
           shoppingList.addAll((ArrayList<String>) getIntent().getSerializableExtra("shopList"));
           Collections.sort(shoppingList);
           adapter = new ArrayAdapter(this, R.layout.rowlayout,R.id.txt_lan, shoppingList);
           autoSecondList.setAdapter(adapter);
            storeArrayVal(shoppingList, getApplicationContext(), db, key);
        }else if (i==1){

            shoppingList = getArrayVal(getApplicationContext(),dbSave, name);
             shoppingList.add(name);

            adapter = new ArrayAdapter(this, R.layout.rowlayout,R.id.txt_lan, shoppingList);
            autoSecondList.setAdapter(adapter);

        }

        autoSecondList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, final int position, long id) {

                selectedItem = ((TextView) view).getText().toString();
                if (shoppingListCheck.contains(selectedItem)) {
                    shoppingListCheck.remove(selectedItem);

                } else
                    shoppingListCheck.add(selectedItem);

            }

        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auto_second, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_add){
           Intent intent = new Intent();
            intent.setClass(Auto_Second.this,Auto.class);
            startActivity(intent);
            return  true;
        }
        if (id == R.id.action_clear) {
            if (!shoppingListCheck.isEmpty()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Clear Selected Items");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray checked = autoSecondList.getCheckedItemPositions();
                        for (int i = autoSecondList.getCount() - 1; i >= 0; i--) {

                            if (checked.get(i) == true) {
                                adapter.remove(shoppingList.get(i));
                            }
                        }
                        storeArrayVal(shoppingList, getApplicationContext(), db, key);
                        checked.clear();
                        adapter.notifyDataSetChanged();
                        shoppingListCheck.clear();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }else
                Toast.makeText(getApplicationContext(), "Select item/s to delete", Toast.LENGTH_SHORT).show();

        }

        if(id==R.id.action_save){
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Add List Name");
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveList=getArrayVal(getApplicationContext(), dbSave, savedListsKey);
                    savedListName=preferredCase(input.getText().toString());
                    mySavedList.addAll(shoppingList);
                    saveList.add(savedListName);
                    storeArrayVal(saveList, getApplicationContext(), dbSave, savedListsKey);
                    storeArrayVal(mySavedList, getApplicationContext(), dbSave, savedListName);
                    Toast.makeText(getApplicationContext(), savedListName + " Is Saved", Toast.LENGTH_SHORT).show();


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }
    public static String preferredCase(String original)
    {
        if (original.isEmpty())
            return original;

        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    public static void storeArrayVal(ArrayList<String> inArrayList, Context context, String db, String key)
    {
        Set<String> WhatToWrite = new HashSet<String>(inArrayList);
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences(db, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet(key, WhatToWrite);
        prefEditor.commit();
    }
    public static ArrayList getArrayVal( Context dan, String db, String key)
    {
        SharedPreferences WordSearchGetPrefs = dan.getSharedPreferences(db,Activity.MODE_PRIVATE);
        Set<String> tempSet = new HashSet<String>();
        tempSet = WordSearchGetPrefs.getStringSet(key, tempSet);
        return new ArrayList<String>(tempSet);
    }
}
