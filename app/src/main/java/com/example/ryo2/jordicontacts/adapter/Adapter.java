package com.example.ryo2.jordicontacts.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryo2.jordicontacts.R;
import com.example.ryo2.jordicontacts.model.ItemContact;

import java.util.List;

import io.realm.Realm;

/**
 * Created by ryo2 on 25/04/2018.
 */

public class Adapter extends BaseAdapter{
    Context context;
    List<ItemContact> list;
    int layout;

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
            vh.name = (TextView) view.findViewById(R.id.contact_name);
            vh.card = view.findViewById(R.id.cards);
            view.setTag(vh);
        }else{
            vh =(ViewHolder) view.getTag();
        }

        vh.name.setText(list.get(pos).getName());
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

        builder.setPositiveButton("Vale", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                itemContact.deleteFromRealm();
                realm.commitTransaction();
            }
        });

        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Cancelado...", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void dialogoEdit(ItemContact itemContact){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edita el contacto");
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.dialog_add,null);
        builder.setView(viewInflate);

        final EditText names = viewInflate.findViewById(R.id.name);
        final EditText numbers = viewInflate.findViewById(R.id.number);

        names.setText(itemContact.getName());
        numbers.setText(itemContact.getNumber());

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nameStr = names.getText().toString().trim();
                String numberStr = numbers.getText().toString().trim();

                if (nameStr.length() >0 && numbers.length() > 0) {
                    //
                    Toast.makeText(context, "Editado!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(context, "Los campos estan vacios :(", Toast.LENGTH_SHORT).show();
                //adapter.notifyDataSetChanged();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class ViewHolder{
        CardView card;
        TextView name;
    }
}
