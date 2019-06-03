package e.alicia.pals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Usuario;

public class Registrar extends AppCompatActivity {

    private EditText etNombre, etEmail, etPass, etPass2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    private DataBaseUsuario dataBaseUsuario;
    private Button botonGuardar;
    private View view;
    private ImageView image;
    private String imagenUsuario = "https://firebasestorage.googleapis.com/v0/b/pals-fae71.appspot.com/o/usuarios%2Fuser.png?alt=media&token=e928a126-f91b-40fb-a852-4164f15148ed";
   private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        iniciarActivity();



    }

    public void iniciarActivity() {
        etPass2 = (EditText) findViewById(R.id.etPass2);
        view = this.findViewById(android.R.id.content);
        etEmail = findViewById(R.id.etEmail);
        etNombre = findViewById(R.id.etNombre);
        etPass = findViewById(R.id.etPass);
        botonGuardar = findViewById(R.id.botonGuardar);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        image = (ImageView) findViewById(R.id.ivRandom);
        cambioImagen(image);
        databaseReference = db.getReference("usuarios");
        dataBaseUsuario = new DataBaseUsuario(databaseReference);
        sharedPreferences=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
    }


    public void cambioImagen(ImageView iv) {
        ArrayList<Integer> imagenes = new ArrayList<>();
        imagenes.add(R.drawable.a1);
        imagenes.add(R.drawable.a2);
        imagenes.add(R.drawable.a3);
        imagenes.add(R.drawable.a4);
        imagenes.add(R.drawable.a5);
        imagenes.add(R.drawable.a6);
        imagenes.add(R.drawable.a7);
        imagenes.add(R.drawable.a8);
        iv.setImageResource(R.drawable.a3);
        int imagen = (int) (Math.random() * 7) + 1;
        iv.setImageResource(imagenes.get(imagen));
    }


    public void creaUsuario(final String email, final String password) throws FirebaseAuthUserCollisionException {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Usuario usuario = new Usuario();
                            usuario.setCodigo(user.getUid());
                            usuario.setEmail(email);
                            usuario.setNombre(etNombre.getText().toString());
                            usuario.setFoto(imagenUsuario);
                            usuario.setNotificacion(false);
                            dataBaseUsuario.guardar(usuario);
                            dataBaseUsuario.modificar(usuario);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("correo", email);
                            editor.putString("pass", password);
                            editor.commit();
                            Snackbar sb = Snackbar.make(view, "Usuario registrado con éxito", Snackbar.LENGTH_LONG);
                            View snackBarView = sb.getView();
                            snackBarView.setBackgroundColor(Color.GREEN);
                            sb.show();


                        } else {
                            Snackbar sb = Snackbar.make(view, "No ha sido posible registrar al usuario", Snackbar.LENGTH_LONG);
                            View snackBarView = sb.getView();
                            snackBarView.setBackgroundColor(Color.RED);
                            sb.show();

                        }


                    }
                });

    }


    //-------------------------validacion de datos

    private boolean esNombreValido(String nombre) {
        if (nombre.length() > 15 || nombre.length() < 3) {

            return false;
        } else {

        }

        return true;
    }

    private boolean esPassValido(String nombre) {
        if (nombre.length() > 15 || nombre.length() < 5) {

            return false;
        }

        return true;
    }

    private boolean passIgual(String pass, String pass2) {
        if (pass.equalsIgnoreCase(pass2))
            return true;
        else
            return false;

    }

    private boolean validarEmail(String email) {

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(email).matches()) {
            return false;
        } else {

        }
        return true;
    }


    public void validarCrearUsuario(View view) {

        if (esNombreValido(etNombre.getText().toString())) {
            if (esPassValido(etPass.getText().toString())) {
                if (passIgual(etPass.getText().toString(), etPass2.getText().toString())) {

                    if (validarEmail(etEmail.getText().toString())) {

                        try {
                            creaUsuario(etEmail.getText().toString(), etPass.getText().toString());

                        } catch (FirebaseAuthUserCollisionException e) {
                            Snackbar.make(view, "Email utilizado por otro usuario", Snackbar.LENGTH_LONG).show();

                        }
                    } else {
                        Snackbar.make(view, "Formato email no valido", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(view, "Las contraseñas no coinciden", Snackbar.LENGTH_LONG).show();

                }
            } else {
                Snackbar.make(view, "Contraseña minimo 6 caracteres", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view, "Nombre entre 3 y 15 caracteres", Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }
}



