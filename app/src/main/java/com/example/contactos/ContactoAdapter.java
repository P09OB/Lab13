package com.example.contactos;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactoAdapter extends BaseAdapter {

    private ArrayList <Contacto> contacto;

    public  ContactoAdapter() {
        contacto = new ArrayList<>();


    }

    public void addContacto(Contacto contac){

        contacto.add(contac);
        notifyDataSetChanged();

    }

    public void clear(){
        contacto.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contacto.size();
    }

    @Override
    public Object getItem(int position) {
        return contacto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View renglon, ViewGroup lista) {

        LayoutInflater inflater = LayoutInflater.from(lista.getContext());
        View renglonView = inflater.inflate(R.layout.row, null);

        Contacto contact =contacto.get(pos);

        Button llamar = renglonView.findViewById(R.id.call);
        Button borrar = renglonView.findViewById(R.id.delate);
        TextView name = renglonView.findViewById(R.id.nombre);
        TextView telefono = renglonView.findViewById(R.id.Tel);


        name.setText(contact.getNombre());
        telefono.setText(contact.getTelefono());

        borrar.setOnClickListener(

                (v)->{
                    String id = contact.getId();
                    String idPush = contact.getIdPush();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Directorio").child("contactos").child(idPush).child(id);
                    ref.setValue(null);

                }
        );

        ActivityCompat.requestPermissions((Activity) lista.getContext(), new  String[]{

                Manifest.permission.CALL_PHONE
        },1);

        llamar.setOnClickListener(


                (v)->{

                    String tel = "tel:"+contact.getTelefono();
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse(tel));

                    lista.getContext().startActivity(i);


                }
        );





        return renglonView;
    }
}
