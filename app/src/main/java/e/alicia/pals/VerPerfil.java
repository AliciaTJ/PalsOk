package e.alicia.pals;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.adaptadores.AdapterUsuario;
import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Usuario;

@RequiresApi(api = Build.VERSION_CODES.N)

/**
 * Clase que carga la activity ver perfil
 */

public class VerPerfil extends AppCompatActivity {
    private Usuario usuario;
    private FirebaseAuth mAuth;
    private DataBaseUsuario dbUsuario;
    private AdapterUsuario adapterUsuario;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;
    private List<Usuario>usuarios=new ArrayList<>();
    private RecyclerView rv;
    private Usuario miUsuario;
    private SharedPreferences sharedPreferences;
    private int vibrar, sonar;
    private MediaPlayer mp;
    private Vibrator viService;
    private ImageView ivFoto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);
        iniciarActivity();


    }


    /**
     * Metodo que carga los elementos y variables necesarias
     *
     */

    public void iniciarActivity() {
        ivFoto=(ImageView)findViewById(R.id.ivImagen);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("usuarios");
        dbUsuario = new DataBaseUsuario(firebaseReference);
        sharedPreferences = getSharedPreferences("opciones", Context.MODE_PRIVATE);
        rv = findViewById(R.id.rvPlanes);
        rv.setLayoutManager(new LinearLayoutManager(this));
        cargarPerfil(mAuth.getCurrentUser().getUid());
        vibrar=sharedPreferences.getInt("vibracion", 1);
        sonar=sharedPreferences.getInt("sonido", 1);

        if (vibrar==1){
            viService = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        }
        if (sonar==1){
            mp = MediaPlayer.create(this, R.raw.boing);
        }
    }


    /**
     * Metodo que accede a la base de datos y carga el recycler view con un unico elemento.
     * El elemento usuario que recibe.
     * @param codigo String
     */
        public void cargarPerfil(final String codigo) {
            firebaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    usuarios.clear();
                    try {
                        if (vibrar==1){
                            viService.vibrate(30);
                        }
                        if (sonar==1){
                            mp.start();
                        }
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            usuario = dataSnapshot1.getValue(Usuario.class);

                            if (usuario.getCodigo().equalsIgnoreCase(codigo)) {
                                usuarios.add(usuario);
                                miUsuario=usuario;
                            }
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

    /**
     * Metodo que llama a otra activity para acceder a los arcchivos del movil. Cambia la foto del perfil
     * @param view
     */
    public void cambiarFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Choose File"), VALOR_RETORNO);

    }


    /**
     * Metodo que trata con los datos llamados en cambiarfoto() y modifica el usuario para
     * establecerle la nueva imagen
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {

        }

        if ((resultCode == RESULT_OK) && (requestCode == VALOR_RETORNO)) {

            Uri uri = data.getData();
            dbUsuario.guardarFoto(uri, miUsuario);
            usuario.setFoto(uri.toString());
            dbUsuario.modificar(miUsuario);

        }

    }

    /**
     * Metodo que devuelve a activityportada al pulsar volver
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, ActivityPortada.class);
        startActivity(i);
    }


}
