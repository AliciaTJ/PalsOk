package e.alicia.pals;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.adaptadores.AdapterChat;
import e.alicia.pals.adaptadores.AdapterVerPlan;
import e.alicia.pals.baseDatos.DataBaseChat;
import e.alicia.pals.modelo.Mensaje;

public class Chat extends AppCompatActivity {

    private String codigo;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DataBaseChat dataBaseChat;
    private List<Mensaje> mensajes;
    private AdapterChat adapterChat;
    private RecyclerView rv;
    private EditText etEnviar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseAnalytics mFirebaseAnalytics;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        rv = (RecyclerView)findViewById(R.id.listamensajes);
        mensajes = new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
       mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);

        linearLayout.setStackFromEnd(true);
        rv.setLayoutManager(linearLayout);

        iniciarActivity();
        codigo = getIntent().getStringExtra("codigo");

        mostrarMensajes(codigo);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.enviar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mensaje mensaje = new Mensaje();
                mensaje.setFechaHora(System.currentTimeMillis());
                mensaje.setMensaje(etEnviar.getText().toString());
                mensaje.setUsuario(firebaseUser.getDisplayName());

                mandarMensaje(mensaje, codigo);
            }
        });


    }

    public void iniciarActivity() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebaseUser.getDisplayName());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,  firebaseUser.getEmail());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("chats");
        dataBaseChat = new DataBaseChat();
        etEnviar = (EditText) findViewById(R.id.mensajeEnviar);
        dataBaseChat = new DataBaseChat();


    }


    public void mostrarMensajes(final String codigo) {

        databaseReference.child(codigo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensajes.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    Mensaje mensaje = dataSnapshot1.getValue(Mensaje.class);
                                    System.out.println(mensaje.getMensaje());
                                    mensajes.add(mensaje);



                        }

                    adapterChat = new AdapterChat(Chat.this, mensajes);
                    rv.setAdapter(adapterChat);
                    adapterChat.notifyDataSetChanged();

                } catch (NullPointerException npe) {


                } finally {

                    adapterChat = new AdapterChat(Chat.this, mensajes);
                    rv.setAdapter(adapterChat);
                    adapterChat.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void mandarMensaje(Mensaje mensaje, String codigo) {
        dataBaseChat.enviar(mensaje, codigo);
        adapterChat.notifyDataSetChanged();
        etEnviar.setText("");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, VerPlan.class);
        i.putExtra("codigo", codigo);
        startActivity(i);
    }


}
