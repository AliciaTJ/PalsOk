package e.alicia.pals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
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

public class ActivityPortada extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context context = this;
    FirebaseAuth user;
    private View view;
    private RecyclerView rv, rvNot;
    ArrayList<Plan> planes;
    DataBasePlan database;
    Context con = this;
    AdapterPlanes adapterPlanes;
    AdapterNoticias adapterNoticias;
    FirebaseDatabase db;
    ArrayList<Noticia> noticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = FirebaseAuth.getInstance();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.enviar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, TipoPlan.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.common_open_on_phone, R.string.abandonar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        rv = findViewById(R.id.rvCercanos);
        rvNot = findViewById(R.id.rvNoticias);
        noticias = new ArrayList<>();
        planes = new ArrayList<>();

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        db = FirebaseDatabase.getInstance();
        rvNot.setLayoutManager(new LinearLayoutManager(this));

        cargarPlanes();
        cargarNoticias();


    }

    public void cargarPlanes() {

        DatabaseReference dbr = db.getReference("planes");
        database = new DataBasePlan(dbr);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Plan user = dataSnapshot1.getValue(Plan.class);
                    planes.add(user);
                }
                adapterPlanes = new AdapterPlanes(ActivityPortada.this, planes);
                adapterPlanes.notifyDataSetChanged();
                System.out.println(adapterPlanes.getItemCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void cargarNoticias() {

        DatabaseReference dbr = db.getReference("noticias");
        database = new DataBasePlan(dbr);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noticias.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Noticia user = new Noticia();
                    user.setTitular(dataSnapshot1.child("titular").getValue(String.class));
                    user.setImagen(dataSnapshot1.child("imagen").getValue(String.class));
                    user.setContenido(dataSnapshot1.child("cuerpo").getValue(String.class));
                    noticias.add(user);
                }
                adapterNoticias = new AdapterNoticias(ActivityPortada.this, noticias);
                rvNot.setAdapter(adapterNoticias);
                adapterNoticias.notifyDataSetChanged();
                System.out.println(adapterNoticias.getItemCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_portada, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.opcionLogOut) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.op_buscarplanes) {
            Intent i = new Intent(this, Planes.class);
            startActivity(i);
        } else if (id == R.id.op_nuevoplan) {
            Intent i = new Intent(this, TipoPlan.class);
            startActivity(i);
        } else if (id == R.id.opcionLogOut) {
            user.signOut();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.op_inicio) {
            Intent i = new Intent(this, ActivityPortada.class);
            startActivity(i);
        } else if (id == R.id.op_perfil) {
            Intent i = new Intent(this, VerPerfil.class);
            startActivity(i);
        } else if (id == R.id.op_opciones) {
            Intent i = new Intent(this, Opciones.class);
            startActivity(i);
        } else if (id == R.id.op_misplanes) {
            Intent i = new Intent(this, MisPlanes.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
