package com.example.abud.mylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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
    SparseBooleanArray checked;
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
        getSupportActionBar().setTitle("Auto List");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        autoSecondList=(ListView)findViewById(R.id.listView_Auto);
        autoSecondList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Intent intent2 = getIntent();
        name = intent2.getStringExtra("theListName");
        i = intent2.getIntExtra("num",0);
        if (i==1){
            shoppingList.clear();

            shoppingList = getArrayVal(getApplicationContext(),dbSave, name);
            Collections.sort(shoppingList);
            adapter = new ArrayAdapter(this, R.layout.rowlayout,R.id.txt_lan, shoppingList);
            autoSecondList.setAdapter(adapter);
            storeArrayVal(shoppingList, getApplicationContext(), db, key);

        }
        if(i==0) {
            shoppingList=getArrayVal(getApplicationContext(),db,key);
           shoppingList.addAll((ArrayList<String>) getIntent().getSerializableExtra("shopList"));
           Collections.sort(shoppingList);
           adapter = new ArrayAdapter(this, R.layout.rowlayout,R.id.txt_lan, shoppingList);
           autoSecondList.setAdapter(adapter);
            storeArrayVal(shoppingList, getApplicationContext(), db, key);
        }

        autoSecondList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, final int position, long id) {
                TextView x = (TextView) view;
                selectedItem = ((TextView) view).getText().toString();
                if (shoppingListCheck.contains(selectedItem)) {
                    shoppingListCheck.remove(selectedItem);

                } else
                    shoppingListCheck.add(selectedItem);

                x.setPaintFlags(x.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            }

        });

        autoSecondList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                for ( int j=autoSecondList.getCount()-1;j>= 0; j--) {
                    autoSecondList.setItemChecked(j, true);
                   shoppingListCheck.add("nothing");

                }

                return true;
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_auto_second, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
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
                     checked = autoSecondList.getCheckedItemPositions();
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
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.setClass(Auto_Second.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
