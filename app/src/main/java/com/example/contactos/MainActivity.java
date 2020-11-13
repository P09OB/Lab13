package com.example.contactos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase db;
    private Button ingresarBu;
    private EditText name;
    private String pushID,nameData;
    private boolean cambio = false;
    private boolean create = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        ingresarBu = findViewById(R.id.ingresarButton);
        name = findViewById(R.id.inputUser);


        ingresarBu.setOnClickListener(this);
    }

    public void LoadData(){

        db.getReference().child("Directorio").child("usuario").orderByChild("name").equalTo(name.getText().toString()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot data) {

                        for(DataSnapshot child: data.getChildren()){

                            User usuario = child.getValue(User.class);
                            pushID = usuario.getId();
                            nameData = usuario.getName()+"";
                            Log.e("data2",pushID);
                        }
                            String input = name.getText().toString();

                    if(nameData != null){

                        if(nameData.equals(input)){
                            create = false;
                            cambio = true;
                        } else {
                            create = true;
                            cambio = true;
                        }
                    }else {
                        create = true;
                        cambio = true;
                    }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }

        );


        validar();

    }

    public void validar(){

        if(create == true){

            String id = db.getReference().child("usuario").push().getKey();
            DatabaseReference reference = db.getReference().child("Directorio").child("usuario").child(id);

            Map<String,String> user = new HashMap<>();
            user.put("name", name.getText().toString());
            user.put("id",id);

            reference.setValue(user);
            Log.e("dataNueva","name"+name.getText().toString());
            cambio = true;

        }

        if(cambio == true){
            Intent i = new Intent(this, Directorio.class);
            i.putExtra("id",pushID);
            startActivity(i);
        }

    }

    @Override
    public void onClick(View view) {


        LoadData();

    }


}