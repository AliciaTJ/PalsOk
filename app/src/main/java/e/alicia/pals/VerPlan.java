package e.alicia.pals;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.adaptadores.AdapterVerPlan;
import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Plan;

public class VerPlan extends AppCompatActivity {


    ImageView ivRandom;
    DataBaseUsuario usuario;
    DataBasePlan dataBasePlan;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Plan plan;
    String codigo;
    FirebaseUser firebaseUser;
    RecyclerView rv;
    List<Plan> planes = new ArrayList<>();
    AdapterVerPlan adapterVerPlan;
    ImageView ivFoto;
    Button botonDenunciar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verplan);
        iniciarActivity();
        codigo = getIntent().getStringExtra("codigo");
        cargarPlan(codigo);

    }


    public void iniciarActivity() {
        botonDenunciar=(Button)findViewById(R.id.botonAdministrador);
        ivFoto = (ImageView) findViewById(R.id.ivFoto);
        rv = (RecyclerView) findViewById(R.id.rvCerrados);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("planes");
        dataBasePlan = new DataBasePlan(databaseReference);
        usuario = new DataBaseUsuario(firebaseDatabase.getReference("usuarios"));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rv.setLayoutManager(new LinearLayoutManager(this));


    }

    public void cargarPlan(final String codigo) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        plan = dataSnapshot1.getValue(Plan.class);
                        if (plan.getCodigo().equals(codigo)) {
                            planes.add(plan);
                        }
                    }
                    adapterVerPlan = new AdapterVerPlan(VerPlan.this, planes);
                    rv.setAdapter(adapterVerPlan);
                    adapterVerPlan.notifyDataSetChanged();
                } catch (NullPointerException npe) {

                } finally {
                    adapterVerPlan = new AdapterVerPlan(VerPlan.this, planes);
                    rv.setAdapter(adapterVerPlan);
                    adapterVerPlan.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void denunciarPlan(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Context context=this;
        builder.setMessage("¿Estas seguro de querer denunciar el plan? Denuncia si es inapropiado, " +
                "incita al odio o la violencia o contiene spam.")
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      dataBasePlan.denunciarPlan(plan, firebaseUser.getUid());
                        Toast.makeText(context, "Has denunciado el plan", Toast.LENGTH_LONG).show();


                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MisPlanes.class);
        startActivity(i);
    }
}
