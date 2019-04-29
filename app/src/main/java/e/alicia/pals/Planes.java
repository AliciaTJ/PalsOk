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
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import e.alicia.pals.adaptadores.AdapterPlanes;
import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.modelo.Plan;

public class Planes extends AppCompatActivity {
    private RecyclerView rv;
    private ArrayList<Plan> planes;
    private Context con = this;
    private AdapterPlanes adapterPlanes;
    private FirebaseDatabase db;
    private Calendar c= Calendar.getInstance();
    private Spinner spTipo;
    private final String[] tipos={"Filtrar por tipo","Freak","Cultura", "Musica",
            "Cine", "Turismo", "Deportes", "Fiesta","Otros"};
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_planes);
        rv = findViewById(R.id.rv);
        planes = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance();
        spTipo=(Spinner)findViewById(R.id.spTipo);

        adapter=new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos);
        spTipo.setAdapter(adapter);
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    cargarPlanes("nada");
                }else{
                    cargarPlanes(tipos[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cargarPlanes("nada");

            }
        });



    }

    public void cargarPlanes(final String tipo) {

        DatabaseReference dbr = db.getReference("planes");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Plan plan = dataSnapshot1.getValue(Plan.class);

                        if (tipo.equalsIgnoreCase("nada")) {
                            planes.add(plan);
                        }else{
                            if (plan.getTipo().equalsIgnoreCase(tipo)){
                                planes.add(plan);
                            }
                        }

                    }
                    adapterPlanes = new AdapterPlanes(Planes.this, planes);
                    rv.setAdapter(adapterPlanes);
                    adapterPlanes.notifyDataSetChanged();

                } catch (NullPointerException npe) {

                } finally {


                    adapterPlanes = new AdapterPlanes(Planes.this, planes);
                    rv.setAdapter(adapterPlanes);
                    adapterPlanes.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, ActivityPortada.class);
        startActivity(i);
    }
}
