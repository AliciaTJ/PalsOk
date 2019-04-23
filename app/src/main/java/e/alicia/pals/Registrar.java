package e.alicia.pals;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Usuario;

public class Registrar extends AppCompatActivity {

    EditText etNombre, etEmail, etPass;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    DataBaseUsuario dataBaseUsuario;
    Button botonGuardar;
    private View view;
    private ImageView image;
    String imagenUsuario="https://firebasestorage.googleapis.com/v0/b/pals-fae71.appspot.com/o/usuarios%2Fuser.png?alt=media&token=e928a126-f91b-40fb-a852-4164f15148ed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        view = this.findViewById(android.R.id.content);
        etEmail = findViewById(R.id.etEmail);
        etNombre = findViewById(R.id.etNombre);
        etPass = findViewById(R.id.etPass);
        botonGuardar = findViewById(R.id.botonGuardar);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        image=(ImageView)findViewById(R.id.ivRandom);
        cambioImagen(image);
        databaseReference = db.getReference("usuarios");
        dataBaseUsuario = new DataBaseUsuario(databaseReference);


    }

    public void cambioImagen(ImageView iv){
        ArrayList<Integer> imagenes=new ArrayList<>();
        imagenes.add(R.drawable.a1);
        imagenes.add(R.drawable.a2);
        imagenes.add(R.drawable.a3);
        imagenes.add(R.drawable.a4);
        imagenes.add(R.drawable.a5);
        imagenes.add(R.drawable.a6);
        imagenes.add(R.drawable.a7);
        imagenes.add(R.drawable.a8);
        iv.setImageResource(R.drawable.a1);
        int imagen= (int)Math.random()*7+1;
        iv.setImageResource(imagenes.get(imagen));
    }


    public void creaUsuario(final String email, String password) {
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
                            dataBaseUsuario.save(usuario);
                            dataBaseUsuario.modificar(usuario);
                            Snackbar sb = Snackbar.make(view, "Usuario registrado con éxito", Snackbar.LENGTH_LONG);
                            View snackBarView = sb.getView();
                            snackBarView.setBackgroundColor(Color.GREEN);
                            sb.show();


                        } else {

                            Snackbar sb = Snackbar.make(view, "Error al registrar el usuario", Snackbar.LENGTH_LONG);
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
        } else {

        }

        return true;
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

        if (esNombreValido(etNombre.getText().toString())){
           if ( esPassValido(etPass.getText().toString())){
               if (validarEmail(etEmail.getText().toString())){
                   creaUsuario(etEmail.getText().toString(), etPass.getText().toString());
               }else{
                   Snackbar.make(view,"Formato email no valido", Snackbar.LENGTH_LONG).show();
               }
            }else{
               Snackbar.make(view,"Contraseña minimo 6 caracteres", Snackbar.LENGTH_LONG).show();
           }
        } else{
            Snackbar.make(view,"Nombre entre 3 y 15 caracteres", Snackbar.LENGTH_LONG).show();
        }

    }

}