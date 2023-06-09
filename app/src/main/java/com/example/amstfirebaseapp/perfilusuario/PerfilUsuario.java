package com.example.amstfirebaseapp.perfilusuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amstfirebaseapp.MainActivity;
import com.example.amstfirebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PerfilUsuario extends AppCompatActivity {

    TextView txt_id, txt_name, txt_email, txt_phone, txt_tweet;
    ImageView imv_photo;
    DatabaseReference db_reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        txt_id = findViewById(R.id.txt_userId);
        txt_name = findViewById(R.id.txt_nombre);
        txt_email = findViewById(R.id.txt_correo);
        txt_phone = findViewById(R.id.txt_phone);
        imv_photo = findViewById(R.id.imv_foto);
        txt_tweet = findViewById(R.id.txt_tweet);
        Intent intent = getIntent();
        HashMap<String, String> info_user = (HashMap<String, String>) intent.getSerializableExtra("info_user");

        PerfilUsuarioViewModel perfilUserModel = new ViewModelProvider(this).get(PerfilUsuarioViewModel.class);
        perfilUserModel.getUiState().observe(this, userUiState -> {

            Log.d("User", info_user.toString());
            txt_id.setText(info_user.get("user_id"));
            txt_name.setText(info_user.get("user_name"));
            txt_email.setText(info_user.get("user_email"));
            txt_phone.setText(info_user.get("user_phone"));
            String photo = info_user.get("user_photo");
            Picasso.get().load(photo).into(imv_photo);
        });

        iniciarBaseDeDatos();
        leerTweets();
        Button btnSendTweets = findViewById(R.id.btn_enviar);

        btnSendTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet(info_user.get("user_name"));
            }
        });

        //escribirTweets("Nicolas Segovia");
    }

    public void publicarTweet(String autor) {
        String tweetText = txt_tweet.getText().toString();

        if (!tweetText.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fecha = dateFormat.format(new Date());
            escribirTweets(autor, tweetText, fecha);
            Toast.makeText(this, "Tweet enviado correctamente", Toast.LENGTH_LONG).show();
            txt_tweet.setText("");
        } else {
            Toast.makeText(this, "Ingrese un tweet", Toast.LENGTH_SHORT).show();
        }

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

    //Función nueva
    public void escribirTweets(String autor, String tweet, String fecha) {
        Map<String, String> hola_tweet = new HashMap<String, String>();
        hola_tweet.put("autor", autor);
        hola_tweet.put("fecha", fecha);
        DatabaseReference tweets = db_reference.child("Grupo6").child("Tweets");
        DatabaseReference nuevoTweetRef = tweets.push();
        nuevoTweetRef.child(tweet).child("autor").setValue(autor);
        nuevoTweetRef.child(tweet).child("fecha").setValue(fecha);
    }

    //Función hecha en clase
    /**public void escribirTweets(String autor){
        String tweet = "hola mundo firebase por ultima vez";
        String fecha = "01-06-2023"; //Fecha actual
        Map<String, String> hola_tweet = new HashMap<String, String>();
        hola_tweet.put("Autor", autor);
        hola_tweet.put("Fecha", fecha);
        DatabaseReference tweets = db_reference.child("Grupo6").child("Tweets");
        tweets.setValue(tweet);
        tweets.child(tweet).child("Autor").setValue(autor);
        tweets.child(tweet).child("Fecha").setValue(fecha);
    }*/
}