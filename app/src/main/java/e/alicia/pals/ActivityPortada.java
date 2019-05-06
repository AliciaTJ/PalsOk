package e.alicia.pals;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import e.alicia.pals.adaptadores.AdapterNoticias;
import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Noticia;


/**
 * Activity portada. Consta de un recycler view de noticias, el
 * menu lateral, el menu flotante y el boton flotante.
 */
public class ActivityPortada extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context context = this;
    FirebaseAuth user;
    private RecyclerView rvNot;
    Context con = this;
    AdapterNoticias adapterNoticias;
    FirebaseDatabase db;
    ArrayList<Noticia> noticias;
    DataBaseUsuario bdUsuario;
    DatabaseReference dbReference;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);

        //se carga la publicidad
        mAdView = findViewById(R.id.adView);
        MobileAds.initialize(this, "ca-app-pub-6032187278566198~3677017529");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //se carga la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //se carga el boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.enviar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, TipoPlan.class);
                startActivity(i);
            }
        });

        //se carga el menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.common_open_on_phone, R.string.abandonar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //se cargan los elementos necesarios
        user = FirebaseAuth.getInstance();
        rvNot = findViewById(R.id.rvNoticias);
        noticias = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        rvNot.setLayoutManager(new LinearLayoutManager(this));
        dbReference = db.getReference("usuarios");
        bdUsuario = new DataBaseUsuario(dbReference);
        cargarNoticias();
        adapterNoticias = new AdapterNoticias(ActivityPortada.this, noticias);
        rvNot.setAdapter(adapterNoticias);

        // comprobarNotificaciones();

    }


    /**
     * Metodo que lanza una notificacion push si el usuario tiene notificaciones
     * pendientes
      */
    public void comprobarNotificaciones() {

        NotificationCompat.Builder mBuilder;
        Intent i = new Intent(this, MisPlanes.class);
        NotificationManager namager = (NotificationManager) getApplicationContext()
                .getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Notificaciones")
                .setContentText("Texto")
                .setSmallIcon(R.drawable.users)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(ActivityPortada.this, 0, i, 0))
                .setVibrate(new long[]{100, 250, 100, 500});
        namager.notify(1, mBuilder.build());
        dbReference.child(user.getUid()).child("notificaciones").removeValue();


    }

    /**
     * Metodo que carga las noticias en el arraylist
     */
    public void cargarNoticias() {

        DatabaseReference dbr = db.getReference("noticias");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noticias.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Noticia noticia = dataSnapshot1.getValue(Noticia.class);
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


    /**
     * Al pulsar atras, se cierra el menu si esta abierto.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Nos permite seleccionar las opciones del menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//si el usuario es el administrador se le abre un layout distinto
        if (!user.getCurrentUser().getEmail().equalsIgnoreCase("aliciavisual@gmail.com")) {
            getMenuInflater().inflate(R.menu.activity_portada, menu);
        } else {
            getMenuInflater().inflate(R.menu.activity_portada_administrador, menu);

        }
        return true;
    }

    /**
     * Accede a la intent de la opcion de menu que se seleccione
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.opcionLogOut) {
            user.signOut();
            user = null;
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if (id == R.id.opcionNoticia) {
            Intent i = new Intent(this, PublicarNoticia.class);
            startActivity(i);
        }
        if (id == R.id.opcionBD) {
            Intent i = new Intent(this, GestionBD.class);
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

