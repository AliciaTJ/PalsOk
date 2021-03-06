package e.alicia.pals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import e.alicia.pals.adaptadores.AdapterPlanes;
import e.alicia.pals.modelo.Plan;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Planes extends AppCompatActivity {
    private RecyclerView rv;
    private ArrayList<Plan> planes;
    private Context con = this;
    private AdapterPlanes adapterPlanes;
    private FirebaseDatabase db;
    private Calendar c = Calendar.getInstance();
    private Spinner spTipo;
    private RadioButton rbProvincia, rbTipos;
    private final String[] filtros = {"Filtrar búsqueda", "Filtrar por tipo", "Filtrar por provincia"};

    private final String[] tipos = {"Filtrar por tipo", "Freak", "Cultura", "Musica",
            "Cine", "Turismo", "Deportes", "Fiesta", "Otros"};
    private ArrayAdapter adapter;
    private final String[] provincias = {"Selecciona la provincia", "Alava", "Albacete", "Alicante", "Almería", "Asturias", "Avila", "Badajoz", "Barcelona", "Burgos", "Cáceres",
            "Cádiz", "Cantabria", "Castellón", "Ciudad Real", "Córdoba", "La Coruña", "Cuenca", "Gerona", "Granada", "Guadalajara",
            "Guipúzcoa", "Huelva", "Huesca", "Islas Baleares", "Jaén", "León", "Lérida", "Lugo", "Madrid", "Málaga", "Murcia", "Navarra",
            "Orense", "Palencia", "Las Palmas", "Pontevedra", "La Rioja", "Salamanca", "Segovia", "Sevilla", "Soria", "Tarragona",
            "Santa Cruz de Tenerife", "Teruel", "Toledo", "Valencia", "Valladolid", "Vizcaya", "Zamora", "Zaragoza"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_planes);

        //se inician los elementos
        rv = findViewById(R.id.rvPlanes);
        planes = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance();
        spTipo = (Spinner) findViewById(R.id.spTipo);
        rbProvincia = (RadioButton) findViewById(R.id.rbProvincia);
        rbTipos = (RadioButton) findViewById(R.id.rbTipo);
        rbTipos.setSelected(true);


        adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, filtros);
        spTipo.setAdapter(adapter);

        //modifica los planes cargados dependiendo del diltro seleccionado
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    cargarPlanes("nada");
                } else if (rbTipos.isChecked()) {
                    cargarPlanes(tipos[position]);
                } else if (rbProvincia.isChecked()) {
                    cargarPlanesProvincia(provincias[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cargarPlanes("nada");

            }
        });


    }

    /**
     * Carga los planes. Recibe el tipo para poder filtrar
     *
     * @param tipo String
     */
    public void cargarPlanes(final String tipo) {
        DatabaseReference dbr = db.getReference("planes");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Plan plan = dataSnapshot1.getValue(Plan.class);

                        if (tipo.equalsIgnoreCase("nada") || tipo.isEmpty()) {
                            planes.add(plan);
                        } else {
                            if (plan.getTipo().equalsIgnoreCase(tipo)) {
                                planes.add(plan);
                            }
                        }

                    }
                    adapterPlanes = new AdapterPlanes(Planes.this, planes);
                    rv.setAdapter(adapterPlanes);
                    adapterPlanes.notifyDataSetChanged();

                } catch (NullPointerException npe) {
                    comprobarVacios();
                } finally {


                    adapterPlanes = new AdapterPlanes(Planes.this, planes);
                    rv.setAdapter(adapterPlanes);
                    adapterPlanes.notifyDataSetChanged();
                    comprobarVacios();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     * Carga los planes. Recibe la provincia para poder filtrar.
     *
     * @param provincias String
     */

    public void cargarPlanesProvincia(final String provincias) {
        DatabaseReference dbr = db.getReference("planes");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Plan plan = dataSnapshot1.getValue(Plan.class);

                        if (provincias.isEmpty() || plan == null) {
                            planes.add(plan);
                        } else {

                            if (plan.getProvincia().equalsIgnoreCase(provincias)) {
                                planes.add(plan);
                            }
                        }

                    }
                    adapterPlanes = new AdapterPlanes(Planes.this, planes);
                    rv.setAdapter(adapterPlanes);
                    adapterPlanes.notifyDataSetChanged();

                } catch (NullPointerException npe) {
                    comprobarVacios();
                } finally {


                    adapterPlanes = new AdapterPlanes(Planes.this, planes);
                    rv.setAdapter(adapterPlanes);
                    adapterPlanes.notifyDataSetChanged();
                    comprobarVacios();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     * Devuelve a la activityPortada si se pulsa volver
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, ActivityPortada.class);
        startActivity(i);
    }

    public void comprobarVacios() {
        ImageView ivSinPlanes = (ImageView) findViewById(R.id.ivSinPlanes);
        try {
            if (rv.getAdapter().getItemCount() == 0) {

                ivSinPlanes.setVisibility(VISIBLE);
                ivSinPlanes.setImageResource(R.drawable.sinplanes);

            } else {
                ivSinPlanes.setVisibility(INVISIBLE);
                ivSinPlanes.setImageResource(R.drawable.places_powered_by_google_light);
            }
        } catch (NullPointerException npe) {

        }

    }


    /**
     * Metodo que modifica el spinner de filtro dependiendo del radiobutton seleccionado
     *
     * @param view
     */
    public void cambiarSpinner(View view) {
        if (rbTipos.isChecked()) {
            adapter = new ArrayAdapter(con, android.R.layout.simple_dropdown_item_1line, tipos);
            spTipo.setAdapter(adapter);
        }
        if (rbProvincia.isChecked()) {
            adapter = new ArrayAdapter(con, android.R.layout.simple_dropdown_item_1line, provincias);
            spTipo.setAdapter(adapter);
        }

    }
}
