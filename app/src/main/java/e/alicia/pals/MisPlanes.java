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
import android.widget.ImageView;

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

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Clase que carga el activity misplanes
 */
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

        //se inician los elementos del activity
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
        comprobarVacios();


    }

    /**
     * Metodo que carga la informacion de la base de datos planes en el recycler view.
     * Carga los planes a los que estamoso apuntados
     */
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
                } catch (NullPointerException npe) {
                    comprobarVacios();
                } finally {
                    adapterCreados = new AdapterPlanes(MisPlanes.this, planes);
                    rvPlanesCreados.setAdapter(adapterCreados);
                    adapterCreados.notifyDataSetChanged();
                    comprobarVacios();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     * Metodo que carga los planes creados por el usuario en el recycler view
     */
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
                } catch (NullPointerException npe) {
                    comprobarVacios();
                } finally {
                    adapterApuntado = new AdapterPlanes(MisPlanes.this, planesApuntados);
                    rvPlanesApuntados.setAdapter(adapterApuntado);
                    adapterApuntado.notifyDataSetChanged();
                    comprobarVacios();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    /**
     * Metodo que comprueba si hay datos. En caso contrario, muestra la imagen "sin planes"
     */

    public void comprobarVacios() {
        try {
            ImageView ivSinPlanes = (ImageView) findViewById(R.id.ivSinPlanes);
            ImageView ivSinPlanes2 = (ImageView) findViewById(R.id.ivSinPlanes2);
            if (rvPlanesApuntados.getAdapter().getItemCount() == 0) {

                ivSinPlanes.setVisibility(VISIBLE);
                ivSinPlanes.setImageResource(R.drawable.sinplanes);
            } else {
                ivSinPlanes.setVisibility(INVISIBLE);
            }
            if (rvPlanesCreados.getAdapter().getItemCount() == 0) {

                ivSinPlanes2.setVisibility(VISIBLE);
                ivSinPlanes2.setImageResource(R.drawable.sinplanes);
            } else {
                ivSinPlanes2.setVisibility(INVISIBLE);
            }

        } catch (NullPointerException npe) {

        }
    }

    /**
     * Metodo que vuelve al activityportada al pulsar volver.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, ActivityPortada.class);
        startActivity(i);
    }

}
