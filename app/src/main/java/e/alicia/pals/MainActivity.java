package e.alicia.pals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Usuario;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    private DataBaseUsuario dataBaseUsuario;
    private EditText etEmail;
    private EditText etPass;
    private View view;
    private int RC_SIGN_IN = 1;
    private final String ID_TOKEN = "445597789329-l1nb5dtif2eed45qjl5bjq1t31ctdblv.apps.googleusercontent.com";
    private GoogleSignInOptions gso;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("usuarios");
        dataBaseUsuario = new DataBaseUsuario(databaseReference);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        view = this.findViewById(android.R.id.content);
        sharedPreferences=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        String correo=sharedPreferences.getString("correo","");
        String pass=sharedPreferences.getString("pass","");
        etEmail.setText(correo);
        etPass.setText(pass);

    }


    @Override
    public void onStart() {
        super.onStart();
/*
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            updateUI(currentUser);
        }
*/
    }

    public void google(View view) {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Usuario usuario = new Usuario();
                usuario.setCodigo(user.getUid());
                usuario.setEmail(user.getEmail());
                usuario.setNombre(user.getDisplayName());
                usuario.setFoto(String.valueOf(user.getPhotoUrl()));
                dataBaseUsuario.guardar(usuario);
                dataBaseUsuario.modificar(usuario);

                updateUI(user);
            } else {
                Snackbar sb = Snackbar.make(view, "Error al registrar al usuario", Snackbar.LENGTH_LONG);
                View snackBarView = sb.getView();
                snackBarView.setBackgroundColor(Color.RED);
                sb.show();
            }
        }
    }

    public void logearse() {
        Intent i = new Intent(this, ActivityPortada.class);
        startActivity(i);

    }

    public void logIn(String email, String password) {
        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                logearse();
                            } else {

                                Snackbar sb = Snackbar.make(view, "Error al loguear", Snackbar.LENGTH_LONG);
                                View snackBarView = sb.getView();

                                snackBarView.setBackgroundColor(Color.RED);
                                sb.show();
                            }


                        }


                    });
        } else {
            Snackbar.make(view, "Introduce usuario y contraseña", Toast.LENGTH_LONG).show();
        }

    }


    private void updateUI(FirebaseUser user) {

        if (user != null) {
            logearse();
        } else {

        }
    }


    public void acceder(View view) {
        logIn(etEmail.getText().toString(), etPass.getText().toString());
    }


    public void registro(View view) {
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
