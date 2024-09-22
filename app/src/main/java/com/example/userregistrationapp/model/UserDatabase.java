package com.example.userregistrationapp.model;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDatabase {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String USER_LIST_KEY = "userList";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public UserDatabase(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void printAllUsers() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }

    public void saveUser(User user) {
        List<User> userList = getUsers(); // Obtener la lista existente
        userList.add(user); // Agregar el nuevo usuario
        String json = gson.toJson(userList); // Convertir la lista a JSON
        sharedPreferences.edit().putString(USER_LIST_KEY, json).apply(); // Guardar el JSON
    }

    public List<User> getUsers() {
        String json = sharedPreferences.getString(USER_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type); // Retornar la lista de usuarios
    }

    public boolean validateUser(String username, String password) {
        List<User> userList = getUsers(); // Obtener la lista de usuarios
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // Usuario válido
            }
        }
        return false; // Usuario no encontrado
    }
}
