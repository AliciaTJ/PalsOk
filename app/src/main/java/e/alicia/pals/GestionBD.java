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
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.adaptadores.AdapterPlanes;
import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.modelo.Plan;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Clase que carga el activity de gestio de base de datos
 */
public class GestionBD extends AppCompatActivity {


    FirebaseDatabase db = FirebaseDatabase.getInstance();
    List<Plan> planes = new ArrayList<>();
    List<Plan> planesDenunciados = new ArrayList<>();
    AdapterPlanes adapterPlanes, adapterPlanesDenuncias;
    RecyclerView rv, rvDenuncias;
    DatabaseReference dbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_bd);

        //se inician los elementos del activity
        rv = (RecyclerView) findViewById(R.id.rvPlanes);
        rvDenuncias = (RecyclerView) findViewById(R.id.rvDenunciados);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapterPlanes = new AdapterPlanes(GestionBD.this, planes);
        adapterPlanesDenuncias = new AdapterPlanes(GestionBD.this, planesDenunciados);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rvDenuncias.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapterPlanes);
        rvDenuncias.setAdapter(adapterPlanesDenuncias);
        adapterPlanesDenuncias.notifyDataSetChanged();
        adapterPlanes.notifyDataSetChanged();


        cargarPlanes();
        cargarPlanesDenunciados();
    }

    /**
     * Metodo que carga los planes cerrados en el recyclerview
     */
    public void cargarPlanes() {
        dbr = db.getReference("planescerrados");
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
                    comprobarVacios();
                } finally {


                    adapterPlanes = new AdapterPlanes(GestionBD.this, planes);
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
     * Metodo que carga los planes denunciados en el recycler view
     */
    public void cargarPlanesDenunciados() {
        dbr = db.getReference("denuncias");
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
                    comprobarVacios();
                } finally {


                    adapterPlanesDenuncias = new AdapterPlanes(GestionBD.this, planesDenunciados);
                    rvDenuncias.setAdapter(adapterPlanesDenuncias);
                    adapterPlanesDenuncias.notifyDataSetChanged();
                    comprobarVacios();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     * Metodo que borra los planes cerrados de la base de datos
     * @param view view
     */
    public void borrarPlanesCerrados(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.borrarcerrados)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbr = db.getReference("planescerrados");
                        for (int i = 0; i < planes.size(); i++) {
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


    /**
     * Metodo que comprueba si hay o no planes para mostrar la imagen "sin planes"
     */
    public void comprobarVacios() {
        ImageView ivSinPlanes = (ImageView) findViewById(R.id.ivSinPlanes);
        if (rv.getAdapter().getItemCount() == 0) {

            ivSinPlanes.setVisibility(VISIBLE);
            ivSinPlanes.setImageResource(R.drawable.sinplanes);
        } else {
            ivSinPlanes.setVisibility(INVISIBLE);
        }
        ImageView ivSinPlanes2 = (ImageView) findViewById(R.id.ivSinPlanes2);
        if (rvDenuncias.getAdapter().getItemCount() == 0) {

            ivSinPlanes2.setVisibility(VISIBLE);
            ivSinPlanes2.setImageResource(R.drawable.sinplanes);
        } else {
            ivSinPlanes2.setImageResource(R.color.places_text_white_alpha_26);
            ivSinPlanes2.setVisibility(INVISIBLE);
        }
    }

    /**
     * Metodo que vuelve al activity portada al pulsar volver
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityPortada.class);
        startActivity(intent);


    }
}
