package com.example.contactos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ContactoAdapter adapter;
    private ListView listaContactos;
    private EditText telefono;
    private Button agregar;
    private Button logOut;
    private EditText nombreContacto;
    private TextView nameUser;
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()== null){

            goToLgin();

        } else {

            listaContactos = findViewById(R.id.contactos);
            nombreContacto = findViewById(R.id.inputName);
            telefono = findViewById(R.id.inputTel);
            agregar = findViewById(R.id.agregarButton);
            logOut = findViewById(R.id.logOut);
            nameUser = findViewById(R.id.nameUser);
            id = auth.getCurrentUser().getUid();



            adapter = new ContactoAdapter();
            listaContactos.setAdapter(adapter);
            agregar.setOnClickListener(this);
            logOut.setOnClickListener(this);


            loadData();


        }

    }

    private void goToLgin() {

        Intent i = new Intent(this,Login.class);
        startActivity(i);
        finish();


    }

    public void loadData(){

        if(auth.getCurrentUser() != null){
            id = auth.getCurrentUser().getUid();

            db.getReference().child("Lab14").child("users").child(id).addListenerForSingleValueEvent(

                    new ValueEventListener() {
                        @Override
                        public void onDataChange( DataSnapshot snapshot) {

                            User user = snapshot.getValue(User.class);
                            nameUser.setText("Bienvenido"+" "+user.getNombre());

                        }

                        @Override
                        public void onCancelled( DatabaseError error) {

                        }
                    }
            );


                    db.getReference().child("Lab14").child("contactos").child(id).addValueEventListener(

                    new ValueEventListener() {
                        @Override
                        public void onDataChange( DataSnapshot data) {

                            adapter.clear();
                            for(DataSnapshot child : data.getChildren()){
                                Contacto contac = child.getValue(Contacto.class);
                                adapter.addContacto(contac);

                            }
                        }

                        @Override
                        public void onCancelled( DatabaseError error) {

                        }
                    }

            );
        }



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.logOut:

                AlertDialog.Builder builder = new AlertDialog.Builder(this)

                        .setTitle("Cerra sesión")
                        .setMessage("¿Estas seguro que desea cerrar sesión?")
                        .setNegativeButton("No",(dialog, id) ->{

                            dialog.dismiss();

                        } )

                        .setPositiveButton("Si",(dialog, id) -> {

                            auth.signOut();
                            goToLgin();

                        });

                builder.show();
                break;


            case R.id.agregarButton:

                String id2 = db.getReference().child("usuario").push().getKey();
                DatabaseReference reference = db.getReference().child("Lab14").child("contactos").child(id).child(id2);

                Contacto contac = new Contacto(

                        id2,
                        id,
                        nombreContacto.getText().toString(),
                        telefono.getText().toString()

                );

                reference.setValue(contac);
                nombreContacto.setText("");
                telefono.setText("");

                break;
        }




    }

    @Override
    protected void onPause() {


        super.onPause();
    }


}