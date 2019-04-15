package e.alicia.pals;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Plan;
import e.alicia.pals.modelo.Usuario;

@RequiresApi(api = Build.VERSION_CODES.N)
public class VerPerfil extends AppCompatActivity {




    Uri urlFoto;
    Usuario usuario;
    Button botonGuardar, botonEditar, cambiarFoto;
    private FirebaseAuth mAuth;
    private TextInputLayout tilNombre;
    private TextInputLayout tilInformacion;
    private TextInputLayout tilEmail;
    private Switch sw;
    private ImageView ivFoto;
    private DataBaseUsuario dbUsuario;
    private EditText
            etNombre, etEmail, etDescripcion;
    private AdapterUsuario adapterUsuario;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;
    List<Usuario>usuarios=new ArrayList<>();
    RecyclerView rv;
    ArrayList<Plan> planes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);


        iniciarActivity();


    }






    public void iniciarActivity() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("usuarios");
        dbUsuario = new DataBaseUsuario(firebaseReference);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        cargarPerfil(mAuth.getCurrentUser().getUid());
    }



        public void cargarPerfil(final String codigo) {


            firebaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    usuarios.clear();
                    try {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            usuario = dataSnapshot1.getValue(Usuario.class);

                            if (usuario.getCodigo().equalsIgnoreCase(codigo))
                                usuarios.add(usuario);

                        }
                        adapterUsuario = new AdapterUsuario(VerPerfil.this, usuarios);
                        rv.setAdapter(adapterUsuario);
                        adapterUsuario.notifyDataSetChanged();
                    }catch(NullPointerException npe){

                    }finally{
                        adapterUsuario = new AdapterUsuario(VerPerfil.this, usuarios);
                        rv.setAdapter(adapterUsuario);
                        adapterUsuario.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    int VALOR_RETORNO =2 ;

    public void cambiarFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Choose File"), VALOR_RETORNO);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {

        }

        if ((resultCode == RESULT_OK) && (requestCode == VALOR_RETORNO)) {

            Uri uri = data.getData();
            usuario.setFoto(uri.toString());
            dbUsuario.guardarFoto(uri, usuario);

        }

    }

    private boolean esNombreValido(String nombre) {
        if (nombre.length() > 50) {
            tilNombre.setError("Nombre demasiado largo");
            return false;
        } else {
            tilNombre.setError(null);
        }

        return true;
    }

    private boolean esInfoValido(String nombre) {

        if (nombre.length() > 50) {
            tilInformacion.setError("Introduce información (maximo 50 caracteres)");
            return false;
        } else {
            tilInformacion.setError(null);
        }

        return true;
    }
/*
    private boolean validarDatos() {
        String nombre = etNombre.getText().toString();
        String info = etDescripcion.getText().toString();


        boolean a = esNombreValido(nombre);
        boolean b = esInfoValido(info);

        if (a && b) {

            Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
*/



}
