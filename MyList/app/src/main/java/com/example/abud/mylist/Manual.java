package com.example.abud.mylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Manual extends AppCompatActivity {
    ArrayList<String> shoppingList = null;
    ArrayList<String> shoppingListCheck = new ArrayList<>();
    ArrayList<String> saveList = null;
    ArrayList<String> mySavedList = new ArrayList<>();
    ArrayAdapter<String> adapter = null;
    ArrayAdapter<String> adapter2 = null;
    ListView lv = null;
    String selectedItem;
    AlertDialog.Builder builder;
    String savedListName;
    String savedListsKey = "ListName";//key name or parameter
    String dbSave ="SavedListsNames";//file name
    String activityKey="myArray";//key name or parameter
    String activityArrayValues = "dbArrayValues";//file name
    int i =0;
    String name;
    TextView x;
    ArrayList<String> shoppingListTemp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.listView);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Intent intent = getIntent();
        name = intent.getStringExtra("theListName");
        i = intent.getIntExtra("num",0);

        if (i == 0) {
            shoppingList = getArrayVal(getApplicationContext(), activityArrayValues, activityKey);
            Collections.sort(shoppingList);
            adapter = new ArrayAdapter(this, R.layout.rowlayout,R.id.txt_lan, shoppingList);
            lv.setAdapter(adapter);

        } else if (i == 1) {
            shoppingList = getArrayVal(getApplicationContext(),dbSave, name);
            Collections.sort(shoppingList);
            adapter = new ArrayAdapter(this, R.layout.rowlayout,R.id.txt_lan, shoppingList);
            lv.setAdapter(adapter);

        } if (shoppingList.isEmpty()) {
            Add_New();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, final int position, long id) {
                 x = (TextView) view;
                selectedItem = ((TextView) view).getText().toString();
                if (shoppingListCheck.contains(selectedItem)) {
                    shoppingListCheck.remove(selectedItem);

                } else
                    shoppingListCheck.add(selectedItem);
                if (lv.isItemChecked(position)){
                    x.setPaintFlags(x.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    adapter.notifyDataSetChanged();

                }
                else
                    x.setPaintFlags(0);
                adapter.notifyDataSetChanged();


            }

        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_add){
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Item");
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    shoppingList.add(preferredCase(input.getText().toString()));
                    Collections.sort(shoppingList);
                    storeArrayVal(shoppingList, getApplicationContext(), activityArrayValues, activityKey);
                    Toast.makeText(getApplicationContext(), preferredCase(input.getText().toString()) + " Is added", Toast.LENGTH_SHORT).show();
                    lv.setAdapter(adapter);
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
        if (id == R.id.action_clear) {
            if (!shoppingListCheck.isEmpty()) {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Clear Selected Items");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray checked = lv.getCheckedItemPositions();
                        for (int i = lv.getCount() - 1; i >= 0; i--) {

                            if (checked.get(i) == true) {
                                adapter.remove(shoppingList.get(i));
                                x.setPaintFlags(0);
                            }
                        }
                        storeArrayVal(shoppingList, getApplicationContext(), activityArrayValues, activityKey);

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
    public static void storeArrayVal( ArrayList<String> inArrayList, Context context, String db, String key)
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

    public void Add_New() {

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Item");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingList.add(preferredCase(input.getText().toString()));
                Collections.sort(shoppingList);
                storeArrayVal(shoppingList, getApplicationContext(), activityArrayValues, activityKey);
                Toast.makeText(getApplicationContext(),preferredCase(input.getText().toString())+ " Is added", Toast.LENGTH_SHORT).show();
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

}
