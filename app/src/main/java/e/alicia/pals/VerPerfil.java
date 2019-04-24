package e.alicia.pals;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import e.alicia.pals.adaptadores.AdapterUsuario;
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }


}
