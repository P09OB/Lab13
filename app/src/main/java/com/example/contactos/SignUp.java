package com.example.contactos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText name;
    private EditText email;
    private EditText tel;
    private TextView login;
    private EditText password;
    private EditText confirmPassword;
    private Button signUp;
    private FirebaseAuth auth;
    private FirebaseDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.Editname);
        email = findViewById(R.id.Editcorreo);
        tel = findViewById(R.id.EditTelefono);
        password = findViewById(R.id.EditPassword);
        confirmPassword = findViewById(R.id.EditPassword2);
        signUp = findViewById(R.id.bttSingUp);
        login = findViewById(R.id.goLogin);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        signUp.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.goLogin:

                Intent i = new Intent(this,Login.class);
                startActivity(i);
                finish();

                break;

            case R.id.bttSingUp:

                if( name.getText().toString().equals("") || email.getText().toString().equals("") || tel.getText().toString().equals("") ||  password.getText().toString().equals("")|| confirmPassword.getText().toString().equals("")){

                    Toast.makeText(this,"Debe llenar todos los campos",Toast.LENGTH_LONG).show();


                } else {
                    if(password.getText().toString().equals(confirmPassword.getText().toString() )){

                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(

                                    task -> {

                                        if (task.isSuccessful()) {
                                            String id = auth.getCurrentUser().getUid();

                                            User user = new User(

                                                    id,
                                                    name.getText().toString(),
                                                    email.getText().toString(),
                                                    tel.getText().toString(),
                                                    password.getText().toString()

                                            );

                                            db.getReference().child("Lab14").child("users").child(id).setValue(user).addOnCompleteListener(

                                                    taskdb -> {

                                                        if (taskdb.isSuccessful()) {

                                                            Intent o = new Intent(this, MainActivity.class);
                                                            startActivity(o);
                                                            finish();

                                                        }
                                                    }
                                            );
                                        } else {
                                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }


                                    }
                            );
                    } else {

                        Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();

                    }
                }
                break;
        }



    }
}