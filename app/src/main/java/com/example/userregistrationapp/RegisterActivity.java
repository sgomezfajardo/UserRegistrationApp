package com.example.userregistrationapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.userregistrationapp.model.User;
import com.example.userregistrationapp.model.UserDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    // Código de solicitud de permiso para ubicación
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // Declaración de variables para los elementos de la interfaz
    private EditText fullNameEditText, usernameEditText, emailEditText, addressEditText, passwordEditText, confirmPasswordEditText, birthDateEditText;
    private Spinner roleSpinner; // Spinner para seleccionar el rol
    private RadioGroup genderRadioGroup; // Grupo para seleccionar el género
    private UserDatabase userDatabase; // Instancia de la base de datos de usuarios
    private double lat, lng; // Variables para almacenar la latitud y longitud

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Establece el layout de la actividad

        // Inicialización de los elementos de la interfaz
        fullNameEditText = findViewById(R.id.fullNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        Button registerButton = findViewById(R.id.registerButton);
        Button locationButton = findViewById(R.id.locationButton);

        userDatabase = new UserDatabase(this); // Inicializa la base de datos de usuarios

        // comportamiento del botón de registro
        registerButton.setOnClickListener(v -> registerUser());
        // comportamiento del botón de ubicación
        locationButton.setOnClickListener(v -> getLocation());

        // del Spinner para los roles (Ciudadano y Gestor)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
    }

    // Método para obtener la ubicación
    private void getLocation() {
        // Verifica si el permiso de ubicación ha sido concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no, solicita el permiso
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Si el permiso ya fue concedido, obtiene la ubicación
            retrieveLocation();
        }
    }

    // Método para recuperar la ubicación
    private void retrieveLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); // Obtiene el servicio de ubicación
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // latitud y longitud al cambiar la ubicación
                lat = location.getLatitude();
                lng = location.getLongitude();
                // ubicación en el campo de dirección
                addressEditText.setText("Lat: " + lat + ", Lng: " + lng);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        // Verifica nuevamente los permisos antes de solicitar actualizaciones de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Solicita actualizaciones de ubicación
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    // Manejo de resultados de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Si el permiso fue concedido, obtiene la ubicación
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                retrieveLocation();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para registrar un nuevo usuario
    private void registerUser() {
        // Obtiene los datos ingresados en el formulario
        String fullName = fullNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String role = roleSpinner.getSelectedItem().toString(); // Obtiene el rol seleccionado
        String birthDate = birthDateEditText.getText().toString();
        int selectedId = genderRadioGroup.getCheckedRadioButtonId(); // Obtiene el ID del botón de género seleccionado
        RadioButton genderRadioButton = findViewById(selectedId); // Encuentra el botón de género seleccionado
        String gender = genderRadioButton != null ? genderRadioButton.getText().toString() : ""; // Obtiene el texto del botón de género

        // Verifica si los campos están vacíos
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(birthDate)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica si las contraseñas coinciden
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica si el correo electrónico es válido
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica si el usuario es mayor de edad
        if (isUnderage(birthDate)) {
            Toast.makeText(this, "Debes ser mayor de 18 años", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crea una nueva instancia de usuario y lo guarda en la base de datos
        User user = new User(fullName, username, email, address, password, role, birthDate, gender);
        userDatabase.saveUser(user);
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        finish(); // Cierra la actividad
    }

    // Método para validar el formato del correo electrónico
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); // Verifica si el correo tiene un formato válido
    }

    // Método para verificar si el usuario es menor de edad
    private boolean isUnderage(String birthDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Define el formato de fecha
        try {
            Date date = sdf.parse(birthDate); // Intenta analizar la fecha
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -18); // Calcula la fecha de hace 18 años
            return date != null && date.after(calendar.getTime()); // Verifica si la fecha es posterior a hace 18 años
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Devuelve false si errores de análisis
        }
    }
}
