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
    private String pushID;

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
        Intent i = new Intent(this, Directorio.class);

        db.getReference().child("Directorio").child("usuario").orderByChild("name").equalTo(name.getText().toString()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot data) {

                        if(data.exists()){

                            for(DataSnapshot child: data.getChildren()){

                                User usuario = child.getValue(User.class);
                                pushID = usuario.getId();
                            }

                        } else {

                            String id = db.getReference().child("usuario").push().getKey();
                            DatabaseReference reference = db.getReference().child("Directorio").child("usuario").child(id);

                            Map<String,String> user = new HashMap<>();
                            user.put("name", name.getText().toString());
                            user.put("id",id);
                            pushID = id;
                            reference.setValue(user);
                            Log.e("dataNueva","name"+name.getText().toString());



                        }

                        i.putExtra("id",pushID);
                        startActivity(i);
                        name.setText("");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }

        );


    }

    @Override
    public void onClick(View view) {

        LoadData();

    }


}