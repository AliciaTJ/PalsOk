package e.alicia.pals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Plan;
import e.alicia.pals.modelo.Usuario;

import static android.view.View.INVISIBLE;

public class VerPlan extends AppCompatActivity {


    TextView tvNombre, tvUbicacion, tvFecha, tvUsuarios, tvCreador, tvInformacion;
    Button botonApuntar, botonChat, botonDejar;
    ImageView ivRandom;
    DataBaseUsuario usuario;
    DataBasePlan dataBasePlan;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Plan plan;
    String codigo;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_plan);

        codigo = getIntent().getStringExtra("codigo");
        iniciarActivity();
        cargarPlan();



    }


    public void iniciarActivity() {


        botonDejar = (Button) findViewById(R.id.botonDejar);
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvUbicacion = (TextView) findViewById(R.id.tvUbicacion);
        tvFecha = (TextView) findViewById(R.id.tvFecha);
        tvUsuarios = (TextView) findViewById(R.id.tvUsuarios);
        tvCreador = (TextView) findViewById(R.id.tvCreador);
        tvInformacion = (TextView) findViewById(R.id.tvInformacion);
        botonApuntar = (Button) findViewById(R.id.botonApuntarse);
        botonChat = (Button) findViewById(R.id.botonChat);
        ivRandom = (ImageView) findViewById(R.id.ivRandom);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("planes");
        dataBasePlan = new DataBasePlan(databaseReference);
        plan = dataBasePlan.buscar(codigo);
        usuario = new DataBaseUsuario(firebaseDatabase.getReference("usuarios"));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //falta random imagenes
    }


    public void cargarPlan() {
        tvNombre.setText(plan.getNombre());
        tvUbicacion.setText(plan.getLugar());
        tvUsuarios.setText(R.string.apuntados + plan.getCantidadUsuarios());
        tvCreador.setText(R.string.creador + plan.getUsuariocreador());
        tvInformacion.setText(plan.getInformacion());
        if (apuntado()) {
            botonChat.setVisibility(View.VISIBLE);
            botonApuntar.setVisibility(INVISIBLE);
            botonDejar.setVisibility(View.VISIBLE);
        } else {
            botonDejar.setVisibility(INVISIBLE);
            botonApuntar.setVisibility(View.VISIBLE);
            botonDejar.setVisibility(INVISIBLE);
        }


    }


    public void abandonarPlan(View view) {

        Usuario miUsuario = usuario.buscar(firebaseUser.getUid());

        for (int i = 0; i < miUsuario.getPlanes().size(); i++) {
            if (miUsuario.getPlanes().get(i).equals(plan.getCodigo())) {
                miUsuario.getPlanes().remove(i);
            }
        }


    }

    public boolean apuntado() {
        Usuario miUsuario = usuario.buscar(firebaseUser.getUid());

        for (int i = 0; i < miUsuario.getPlanes().size(); i++) {
            if (miUsuario.getPlanes().get(i).equals(plan.getCodigo())) {
                return true;
            }
        }
        return false;
    }


    public void apuntarPlan(View view) {


    }


    public void abrirChat(View view) {
        Intent i = new Intent(this, Chat.class);
        startActivity(i);
    }







}
