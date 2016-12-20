package com.example.abud.mylist;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;


public class Saved extends AppCompatActivity {
    ListView savedItemsList;
    ArrayList<String> savedListsNames = new ArrayList<>();
    ArrayList<String> savedListsNamesManual = new ArrayList<>();
    ArrayAdapter<String> adapter = null;
    String savedListsKey = "ListName";//key name or parameter
    String dbSave ="SavedListsNames";//file name
    String savedListsKeyAuto = "SaveAutoKey";//key name or parameter
    String dbSaveAuto ="SaveAuto";//file name
    Manual manual = new Manual();
    String selectedListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        savedItemsList=(ListView)findViewById(R.id.listView_save);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saved Lists");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        savedListsNames.addAll(manual.getArrayVal(getApplicationContext(), dbSave, savedListsKey));
        savedListsNames.addAll(manual.getArrayVal(getApplicationContext(), dbSaveAuto, savedListsKeyAuto));
        savedListsNamesManual.addAll(manual.getArrayVal(getApplicationContext(), dbSave, savedListsKey));


        Collections.sort(savedListsNames);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedListsNames);
        savedItemsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        savedItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = ((TextView) view).getText().toString();
                Intent intent = new Intent();
                Intent Auto = new Intent();
                if(savedListsNamesManual.contains(name)) {

                    intent.setClass(Saved.this, Manual.class);
                    intent.putExtra("theListName", name);
                    intent.putExtra("num", 1);
                    startActivity(intent);
                    finish();

                } else{

                    Auto.setClass(Saved.this, Auto_Second.class);
                    Auto.putExtra("theListName", name);
                    Auto.putExtra("num", 1);
                    startActivity(Auto);
                    finish();
                }


            }
        });

       savedItemsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedListName=((TextView) view).getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(Saved.this);
                builder.setTitle("Clear Selected Item");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        adapter.remove(selectedListName);
                        manual.storeArrayVal(savedListsNames, getApplicationContext(), dbSave, savedListsKey);
                        manual.storeArrayVal(savedListsNames, getApplicationContext(), dbSaveAuto, savedListsKeyAuto);
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
    }
    }
