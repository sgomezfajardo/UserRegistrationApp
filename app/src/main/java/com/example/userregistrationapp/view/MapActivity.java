package com.example.userregistrationapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.userregistrationapp.R;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button selectLocationButton = findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(v -> selectLocation());
    }

    private void selectLocation() {
        // Aquí puedes implementar la lógica para seleccionar la ubicación
        // Luego, pasa los valores de latitud y longitud a RegisterActivity

        double lat = 0; // Cambia esto a la latitud seleccionada
        double lng = 0; // Cambia esto a la longitud seleccionada

        Intent intent = new Intent();
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        setResult(RESULT_OK, intent);
        finish();
    }
}
