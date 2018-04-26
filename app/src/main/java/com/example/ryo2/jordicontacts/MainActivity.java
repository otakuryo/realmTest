package com.example.ryo2.jordicontacts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ryo2.jordicontacts.adapter.Adapter;
import com.example.ryo2.jordicontacts.model.ItemContact;
import com.example.ryo2.jordicontacts.preferencias.Prefs;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<ItemContact>>{

    Realm realm;
    Adapter adapter;
    ListView listView;
    RealmResults<ItemContact> results;
    boolean asc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Añadimos el toque
        findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //fin...

        //abrimos realm
        realm = Realm.getDefaultInstance();
        results = realm.where(ItemContact.class).findAll();
        results.addChangeListener(this);
        //instanciamos los campos
        listView = findViewById(R.id.listView);
        //fin de instancia



        adapter = new Adapter(this,results,R.layout.item_contact);
        listView.setAdapter(adapter);
        FloatingActionButton add = findViewById(R.id.fab);
        FloatingActionButton edadOrder = findViewById(R.id.extras_order);
        FloatingActionButton edadBet = findViewById(R.id.extras_between);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoAdd();
            }
        });
        edadOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order();
            }
        });
        edadBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMorF();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewContact(String name, String number,int edad, boolean genero){
        realm.beginTransaction();
        ItemContact itemContact = new ItemContact(1+System.currentTimeMillis(),name,number,edad,genero);
        realm.copyToRealm(itemContact);
        realm.commitTransaction();
    }

    private void dialogoAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añade un contacto");
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.dialog_add,null);
        builder.setView(viewInflate);

        final EditText names = viewInflate.findViewById(R.id.name);
        final EditText numbers = viewInflate.findViewById(R.id.number);
        final EditText edad = viewInflate.findViewById(R.id.edad);
        final ToggleButton sexo = viewInflate.findViewById(R.id.sexo);


        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nameStr = names.getText().toString().trim();
                String numberStr = numbers.getText().toString().trim();
                String edadStr = edad.getText().toString().trim();
                boolean sexoBoolean = sexo.getText().toString().equals("Hombre");

                if (nameStr.length() >0 && numbers.length() > 0 && edadStr.length() > 0){
                    int edadInt = Integer.parseInt(edad.getText().toString());
                    createNewContact(nameStr,numberStr,edadInt,sexoBoolean);
                }else if (nameStr.length() >0 && numbers.length() > 0){
                    createNewContact(nameStr,numberStr,0,sexoBoolean);
                }
                else Toast.makeText(MainActivity.this, "Algún campo esta vacio :(", Toast.LENGTH_SHORT).show();
                //adapter.notifyDataSetChanged();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    void order(){
        if (asc) {
            results = realm.where(ItemContact.class).findAll().sort("edad");
            asc = false;
        }else {
            results = realm.where(ItemContact.class).findAll().sort("edad",Sort.DESCENDING);
            asc=true;
        }
        adapter = new Adapter(this,results,R.layout.item_contact);
        listView.setAdapter(adapter);
    }
    void showonly(int min, int max){
        results = realm.where(ItemContact.class)
                .greaterThan("edad", min)
                .lessThan("edad",max)
                .findAll();
        adapter = new Adapter(this,results,R.layout.item_contact);
        listView.setAdapter(adapter);

    }
    boolean gen=true;
    void showMorF(){
        results = realm.where(ItemContact.class)
                .equalTo("genero",gen)
                .findAll();
        gen=!gen;
        adapter = new Adapter(this,results,R.layout.item_contact);
        listView.setAdapter(adapter);
    }

    @Override
    public void onChange(RealmResults<ItemContact> element) {
        adapter.notifyDataSetChanged();
    }
}
