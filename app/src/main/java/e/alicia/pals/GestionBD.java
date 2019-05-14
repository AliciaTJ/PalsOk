package e.alicia.pals;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.adaptadores.AdapterPlanes;
import e.alicia.pals.modelo.Plan;

public class GestionBD extends AppCompatActivity {

    FirebaseDatabase db= FirebaseDatabase.getInstance();
    List<Plan> planes=new ArrayList<>();
    List<Plan> planesDenunciados=new ArrayList<>();
    AdapterPlanes adapterPlanes, adapterPlanesDenuncias;
    RecyclerView rv, rvDenuncias;
    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_bd);
        rv=(RecyclerView)findViewById(R.id.rvCerrados);
        rvDenuncias=(RecyclerView)findViewById(R.id.rvDenunciados);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapterPlanes = new AdapterPlanes(GestionBD.this, planes);
        adapterPlanesDenuncias = new AdapterPlanes(GestionBD.this, planesDenunciados);
        rv.setAdapter(adapterPlanes);
        rvDenuncias.setAdapter(adapterPlanesDenuncias);
        adapterPlanesDenuncias.notifyDataSetChanged();
        adapterPlanes.notifyDataSetChanged();
        //rv.setEnabled(false);
        cargarPlanes();
        cargarPlanesDenunciados();
    }

    public void cargarPlanes() {

       dbr= db.getReference("planescerrados");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Plan plan = dataSnapshot1.getValue(Plan.class);
                            planes.add(plan);
                    }
                    adapterPlanes = new AdapterPlanes(GestionBD.this, planes);
                    rv.setAdapter(adapterPlanes);
                    adapterPlanes.notifyDataSetChanged();

                } catch (NullPointerException npe) {

                } finally {


                    adapterPlanes = new AdapterPlanes(GestionBD.this, planes);
                    rv.setAdapter(adapterPlanes);
                    adapterPlanes.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    public void cargarPlanesDenunciados() {

        dbr= db.getReference("denuncias");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planesDenunciados.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Plan plan = dataSnapshot1.getValue(Plan.class);
                        planesDenunciados.add(plan);
                    }
                    adapterPlanesDenuncias = new AdapterPlanes(GestionBD.this, planesDenunciados);
                    rvDenuncias.setAdapter(adapterPlanesDenuncias);
                    adapterPlanesDenuncias.notifyDataSetChanged();

                } catch (NullPointerException npe) {
                    planesDenunciados.add(new Plan("No hay ningun plan denunciado"));

                                    } finally {


                    adapterPlanesDenuncias = new AdapterPlanes(GestionBD.this, planesDenunciados);
                    rvDenuncias.setAdapter(adapterPlanes);
                    adapterPlanesDenuncias.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    public void borrarPlanesCerrados(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.borrarcerrados)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbr= db.getReference("planescerrados");
                        for (int i=0; i<planes.size(); i++) {
                            dbr.child(planes.get(i).getCodigo()).removeValue();
                        }

                        Snackbar.make(view, "Se han borrado todos los planes cerrados", Snackbar.LENGTH_LONG);

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityPortada.class);
        startActivity(intent);


    }
}
