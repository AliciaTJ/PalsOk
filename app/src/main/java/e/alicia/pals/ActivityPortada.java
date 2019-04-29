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
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
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

import static android.support.constraint.solver.widgets.ConstraintWidget.VISIBLE;


public class ActivityPortada extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context context = this;
    FirebaseAuth user;
    private RecyclerView rv, rvNot;
    ArrayList<Plan> planes;
    Context con = this;
    AdapterPlanes adapterPlanes;
    AdapterNoticias adapterNoticias;
    FirebaseDatabase db;
    ArrayList<Noticia> noticias;
   // private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);
        /*
        mAdView=findViewById(R.id.adView);
        MobileAds.initialize(this, "ca-app-pub-6032187278566198~3677017529");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

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


        rvNot = findViewById(R.id.rvNoticias);
        noticias = new ArrayList<>();


        db = FirebaseDatabase.getInstance();
        rvNot.setLayoutManager(new LinearLayoutManager(this));


        cargarNoticias();
        adapterNoticias = new AdapterNoticias(ActivityPortada.this, noticias);
        rvNot.setAdapter(adapterNoticias);

    }


    public void cargarNoticias() {

        DatabaseReference dbr = db.getReference("noticias");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noticias.clear();
                   for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                       Noticia noticia=dataSnapshot1.getValue(Noticia.class);
                       noticias.add(noticia);
                   }


                adapterNoticias = new AdapterNoticias(ActivityPortada.this, noticias);
                rvNot.setAdapter(adapterNoticias);
                adapterNoticias.notifyDataSetChanged();

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



        if (!user.getCurrentUser().getEmail().equalsIgnoreCase("aliciavisual@gmail.com")){
            getMenuInflater().inflate(R.menu.activity_portada, menu);
        }else{
            getMenuInflater().inflate(R.menu.activity_portada_administrador, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.opcionLogOut) {
            user.signOut();
            user=null;
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        }
        if (id==R.id.opcionNoticia){
            Intent i = new Intent(this, PublicarNoticia.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

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
            user=null;
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
