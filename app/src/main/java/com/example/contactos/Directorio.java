package com.example.contactos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Directorio extends AppCompatActivity implements View.OnClickListener{

    private ContactoAdapter adapter;
    private ListView listaContactos;
    private EditText telefono;
    private Button agregar;
    private EditText nombreContacto;
    private FirebaseDatabase db;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directorio);


        db = FirebaseDatabase.getInstance();
        listaContactos = findViewById(R.id.contactos);
        nombreContacto = findViewById(R.id.inputName);
        telefono = findViewById(R.id.inputTel);
        agregar = findViewById(R.id.agregarButton);


        id = getIntent().getExtras().getString("id");

        adapter = new ContactoAdapter();
        listaContactos.setAdapter(adapter);
        agregar.setOnClickListener(this);

        loadData();

    }

    public void loadData(){

         db.getReference().child("Directorio").child("contactos").child(id).orderByChild("id").equalTo(id).addValueEventListener(

                 new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot data) {

                         adapter.clear();
                         for(DataSnapshot child : data.getChildren()){
                             Contacto contac = child.getValue(Contacto.class);
                             adapter.addContacto(contac);

                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 }

         );
         
    }



    @Override
    public void onClick(View view) {


        String id2 = db.getReference().child("usuario").push().getKey();
        DatabaseReference reference = db.getReference().child("Directorio").child("contactos").child(id).child(id2);

        Contacto contac = new Contacto(

                id,
                nombreContacto.getText().toString(),
                telefono.getText().toString()

        );

        reference.setValue(contac);

    }

    @Override
    protected void onPause() {


        super.onPause();
    }
}