package com.example.amstfirebaseapp.perfilusuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amstfirebaseapp.MainActivity;
import com.example.amstfirebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PerfilUsuario extends AppCompatActivity {

    TextView txt_id, txt_name, txt_email;
    ImageView imv_photo;
    DatabaseReference db_reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        txt_id = findViewById(R.id.txt_userId);
        txt_name = findViewById(R.id.txt_nombre);
        txt_email = findViewById(R.id.txt_correo);
        imv_photo = findViewById(R.id.imv_foto);

        PerfilUsuarioViewModel perfilUserModel = new ViewModelProvider(this).get(PerfilUsuarioViewModel.class);
        perfilUserModel.getUiState().observe(this, userUiState -> {
            Intent intent = getIntent();
            HashMap<String, String> info_user = (HashMap<String, String>) intent.getSerializableExtra("info_user");
            txt_id.setText(info_user.get("user_id"));
            txt_name.setText(info_user.get("user_name"));
            txt_email.setText(info_user.get("user_email"));
            String photo = info_user.get("user_photo");
            Picasso.get().load(photo).into(imv_photo);
        });

        iniciarBaseDeDatos();
        escribirTweets("Nicolas Segovia");
    }

    public void cerrarSesion(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", "cerrarSesion");
        startActivity(intent);
    }

    public void iniciarBaseDeDatos() {
        db_reference = FirebaseDatabase.getInstance().getReference().child("Grupos");
    }

    public void leerTweets() {
        db_reference.child("Grupo6").child("Tweets").addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        System.out.println(snapshot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println(error.toException());
                }
            }
        );
    }

    public void escribirTweets(String autor){
        String tweet = "hola mundo firebase por ultima vez";
        String fecha = "01-06-2023"; //Fecha actual
        Map<String, String> hola_tweet = new HashMap<String, String>();
        hola_tweet.put("Autor", autor);
        hola_tweet.put("Fecha", fecha);
        DatabaseReference tweets = db_reference.child("Grupo6").child("Tweets");
        tweets.setValue(tweet);
        tweets.child(tweet).child("Autor").setValue(autor);
        tweets.child(tweet).child("Fecha").setValue(fecha);
    }
}