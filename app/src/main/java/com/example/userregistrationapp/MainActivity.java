package com.example.userregistrationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userregistrationapp.R;
import com.example.userregistrationapp.model.UserDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        Button viewUsersButton = findViewById(R.id.viewUsersButton);

        userDatabase = new UserDatabase(this);

        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        viewUsersButton.setOnClickListener(v -> viewUsers());
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (userDatabase.validateUser(username, password)) {
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewUsers() {
        userDatabase.printAllUsers(); // Agrega esta función en UserDatabase
    }
}
