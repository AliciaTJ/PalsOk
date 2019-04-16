package e.alicia.pals;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import e.alicia.pals.adaptadores.AdapterPlanes;
import e.alicia.pals.adaptadores.ItemClickListener;
import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.modelo.Plan;

public class Planes extends AppCompatActivity {
    private RecyclerView rv;
    ArrayList<Plan> planes;
    DataBasePlan database;
    Context con = this;
    AdapterPlanes adapterPlanes;
    FirebaseDatabase db;

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
        database = new DataBasePlan(dbr);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Plan user = dataSnapshot1.getValue(Plan.class);
                    planes.add(user);
                }
                adapterPlanes = new AdapterPlanes(Planes.this, planes);
                rv.setAdapter(adapterPlanes);
                adapterPlanes.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
