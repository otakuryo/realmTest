package com.example.ryo2.jordicontacts.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ryo2.jordicontacts.R;
import com.example.ryo2.jordicontacts.model.ItemContact;

import java.util.List;

import io.realm.Realm;

/**
   Created by ryo2 on 25/04/2018.
 */

public class Adapter extends BaseAdapter{
    private Context context;
    private List<ItemContact> list;
    private int layout;

    public Adapter(Context contextExt, List<ItemContact> listExt, int layoutExt){
        this.context = contextExt;
        this.list = listExt;
        this.layout = layoutExt;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {

        ViewHolder vh;
        if (view == null){
            view = LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.card = view.findViewById(R.id.cards);
            vh.name = view.findViewById(R.id.contact_name);
            vh.edad = view.findViewById(R.id.contact_edad);
            vh.sexo = view.findViewById(R.id.contact_sex);
            view.setTag(vh);
        }else{
            vh =(ViewHolder) view.getTag();
        }

        vh.name.setText(list.get(pos).getName());
        vh.edad.setText(String.valueOf("Edad: "+list.get(pos).getEdad()));
        if (list.get(pos).isGenero()){
            vh.sexo.setText(String.valueOf("Genero: Masculino"));
        }else{
            vh.sexo.setText(String.valueOf("Genero: Femenino"));
        }

        vh.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoEdit(list.get(pos));
            }
        });
        vh.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialogodel(list.get(pos));
                return false;
            }
        });
        return view;
    }
    private void dialogodel(final ItemContact itemContact){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Borrar contacto?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                itemContact.deleteFromRealm();
                realm.commitTransaction();
            }
        });

        builder.setNegativeButton("No",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Cancelado...", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void dialogoEdit(final ItemContact itemContact){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edita el contacto");
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_add,null);
        builder.setView(viewInflate);

        final EditText names = viewInflate.findViewById(R.id.name);
        final EditText numbers = viewInflate.findViewById(R.id.number);
        final EditText edads = viewInflate.findViewById(R.id.edad);
        final ToggleButton sexo = viewInflate.findViewById(R.id.sexo);

        names.setText(itemContact.getName());
        numbers.setText(itemContact.getNumber());
        edads.setText(String.valueOf(itemContact.getEdad()));
        sexo.setChecked(!itemContact.isGenero());

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nameStr = names.getText().toString().trim();
                String numberStr = numbers.getText().toString().trim();
                int edadInt = Integer.parseInt(edads.getText().toString());
                boolean sexoBoolean = sexo.getText().toString().equals("Hombre");

                if (nameStr.length() >0 && numbers.length() > 0) {
                    //
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    itemContact.setName(nameStr);
                    itemContact.setNumber(numberStr);
                    itemContact.setEdad(edadInt);
                    itemContact.setGenero(sexoBoolean);
                    realm.copyToRealmOrUpdate(itemContact);
                    realm.commitTransaction();
                    Toast.makeText(context, "Editado!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(context, "Los campos estan vacios :(", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class ViewHolder{
        CardView card;
        TextView name;
        TextView edad;
        TextView sexo;
    }
}
