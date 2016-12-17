package com.example.abud.mylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Auto extends AppCompatActivity {

    ListView autoList;
    ArrayAdapter<String> adapter = null;
    MaterialSearchView searchView;
    ImageButton floatButton;
    String newItem;
    String db ="AutoArray";
    String key ="Values";
    String selectedItem;
    String item;
    ArrayList<String> shoppingListCheck = new ArrayList<>();
    ArrayList<String> shoppingList = new ArrayList<>();
    String[] items;
    List<String> myItemList;
    ArrayList<String> itemsList;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Auto");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        autoList = (ListView)findViewById(R.id.item_view);
        autoList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        floatButton = (ImageButton)findViewById(R.id.imageButton);
        items=getResources().getStringArray(R.array.List);
      myItemList=Arrays.asList(items);
      itemsList = new ArrayList<String>();
        itemsList = getArrayVal(getApplicationContext(),db,key);
        if(itemsList.isEmpty()){
          itemsList.addAll(myItemList);
        }
        Collections.sort(itemsList);
        adapter = new ArrayAdapter(this, R.layout.rowlayout,R.id.txt_lan, itemsList);
        autoList.setAdapter(adapter);
        autoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = ((TextView) view).getText().toString();
                if (shoppingListCheck.contains(selectedItem)) {
                    shoppingListCheck.remove(selectedItem);

                } else
                    shoppingListCheck.add(selectedItem);
                storeArrayVal(shoppingListCheck,getApplicationContext(),"test","test");
            }
        });
        autoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                item=((TextView) view).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(Auto.this);
                builder.setTitle("Clear Selected Item");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        adapter.remove(item);
                        storeArrayVal(itemsList,getApplicationContext(),db,key);
                        adapter.notifyDataSetChanged();
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
            }
        });

        searchView = (MaterialSearchView)findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener(){
        @Override
        public void onSearchViewShown(){

        }
        @Override
        public void onSearchViewClosed(){
// if Search Closed, autoView return default
            adapter = new ArrayAdapter(Auto.this, R.layout.rowlayout,R.id.txt_lan, itemsList);
            Collections.sort(itemsList);
            autoList.setAdapter(adapter);
        }
    });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                 newItem = newText;
                if(newText != null && !newText.isEmpty()){
                List<String> Found = new ArrayList<String>();
                    for(String item:itemsList) {

                        if (item.contains(preferredCase(newText)))
                            Found.add(item);
                        floatButton.setVisibility(View.VISIBLE);
                    }


                        adapter = new ArrayAdapter(Auto.this, R.layout.rowlayout, R.id.txt_lan, Found);
                    Collections.sort(Found);
                        autoList.setAdapter(adapter);
                    }
                else {
                    floatButton.setVisibility(View.INVISIBLE);
                    adapter = new ArrayAdapter(Auto.this, R.layout.rowlayout,R.id.txt_lan, itemsList);
                    Collections.sort(itemsList);
                    autoList.setAdapter(adapter);
                }
                return true;
            }
        });

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemsList.add(preferredCase(newItem));
                 storeArrayVal(itemsList,getApplicationContext(),db,key);
                shoppingListCheck.add(preferredCase(newItem));
                Toast.makeText(getApplicationContext(), preferredCase(newItem)+" Is Added", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auto, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.done){
            Intent intent = new Intent();
            intent.setClass(Auto.this, Auto_Second.class);
            shoppingList=getArrayVal(getApplicationContext(),"ShopingList","Values");
            if(shoppingList.contains(selectedItem)){
                shoppingListCheck.remove(selectedItem);
            }
            intent.putStringArrayListExtra("shopList",shoppingListCheck);
            startActivity(intent);
            finish();

                }

            return  true;
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
