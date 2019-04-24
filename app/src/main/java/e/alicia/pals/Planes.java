package e.alicia.pals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    ArrayList<Plan> planes;

    Context con = this;
    AdapterPlanes adapterPlanes;
    FirebaseDatabase db;
    Calendar c= Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_planes);
        rv = findViewById(R.id.rv);
        planes = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance();
        cargarPlanes();

    }

    public void cargarPlanes() {

        DatabaseReference dbr = db.getReference("planes");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Plan user = dataSnapshot1.getValue(Plan.class);


                        planes.add(user);
                            /*
                        }else{
                                String[] fecha = user.getFecha().split("/");
                                int dia = Integer.parseInt(fecha[0]);
                                int mes = Integer.parseInt(fecha[1]);
                                int anio = Integer.parseInt(fecha[2]);
                                c.set(anio, mes, dia);
                            if (c.getTimeInMillis()<System.currentTimeMillis()-82800000){
                                user.setEstado("vencido");
                                database.guardar(user);*/

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
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
