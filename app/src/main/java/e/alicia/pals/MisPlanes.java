package e.alicia.pals;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import e.alicia.pals.adaptadores.AdapterNoticias;
import e.alicia.pals.adaptadores.AdapterPlanes;
import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.modelo.Noticia;
import e.alicia.pals.modelo.Plan;

public class MisPlanes extends AppCompatActivity {
    Context context = this;
    FirebaseUser user;
    private View view;
    private RecyclerView rvPlanesCreados, rvPlanesApuntados;
    ArrayList<Plan> planes;
    DataBasePlan database;
    Context con = this;
    AdapterPlanes adapterCreados;
    AdapterPlanes adapterApuntado;
    FirebaseDatabase db;
    ArrayList<Plan> planesApuntados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_planes);
        user = FirebaseAuth.getInstance().getCurrentUser();
        rvPlanesCreados = findViewById(R.id.rvCreados);
        rvPlanesApuntados = findViewById(R.id.rvApuntado);
        planesApuntados = new ArrayList<>();
        planes = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        rvPlanesApuntados.setLayoutManager(new LinearLayoutManager(this));
        rvPlanesCreados.setLayoutManager(new LinearLayoutManager(this));

        cargarPlanes();
        cargarMisPlanes();


    }

    public void cargarPlanes() {

            DatabaseReference dbr = db.getReference("planes");
            database = new DataBasePlan(dbr);

            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    planes.clear();
                    try {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            Plan plan = dataSnapshot1.getValue(Plan.class);
                            if (plan.getUsuariocreador().equalsIgnoreCase(user.getUid())) {
                                planes.add(plan);

                            }
                        }
                    }catch(NullPointerException npe){

                    }
                    adapterCreados = new AdapterPlanes(MisPlanes.this, planes);
                    rvPlanesCreados.setAdapter(adapterCreados);
                    adapterCreados.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }

    public void cargarMisPlanes() {

        DatabaseReference dbr = db.getReference("planes");
        database = new DataBasePlan(dbr);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planesApuntados.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Plan plan = dataSnapshot1.getValue(Plan.class);
                        if (plan.getUsuariosapuntados().contains(user.getUid()) && !plan.getUsuariocreador().equalsIgnoreCase(user.getUid()) &&
                                !plan.getEstado().equalsIgnoreCase("cerrado")) {
                            planesApuntados.add(plan);
                        }


                    }
                }catch(NullPointerException npe){

                }
                adapterApuntado = new AdapterPlanes(MisPlanes.this, planesApuntados);
                rvPlanesApuntados.setAdapter(adapterApuntado);
                adapterApuntado.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
