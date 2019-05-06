package e.alicia.pals;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.adaptadores.AdapterChat;
import e.alicia.pals.adaptadores.AdapterVerPlan;
import e.alicia.pals.baseDatos.DataBaseChat;
import e.alicia.pals.modelo.Mensaje;

public class Chat extends AppCompatActivity {

    private String codigo, nombre;
    private String[] usuarios;
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
    private TextInputLayout tilMensaje;

    private SharedPreferences sharedPreferences;
    private int vibrar, sonar, notificaciones;
    private TextView tvInformacion, tvUsuarios;
private SoundPool sp;
private Vibrator viService;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        rv = (RecyclerView)findViewById(R.id.listamensajes);
        mensajes = new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        tvInformacion=(TextView)findViewById(R.id.tvInformacion);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        tilMensaje=(TextInputLayout)findViewById(R.id.tilMensaje);
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        sharedPreferences= getSharedPreferences("opciones", Context.MODE_PRIVATE);
        linearLayout.setStackFromEnd(true);
        rv.setLayoutManager(linearLayout);
        vibrar=sharedPreferences.getInt("vibracion", 1);
        sonar=sharedPreferences.getInt("sonido", 1);
        if (vibrar==1){
            viService = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        }
        if (sonar==1){
          sp=new SoundPool(1,R.raw.boing,1);
        }
        iniciarActivity();
        codigo = getIntent().getStringExtra("codigo");
        nombre=getIntent().getStringExtra("nombre");
        usuarios=getIntent().getStringArrayExtra("usuarios");
        tvInformacion.setText(nombre);

       tvUsuarios.setText("Usuarios: " );
       for (int i=0; i<usuarios.length; i++){
           tvUsuarios.append(usuarios[i]);
       }
        mostrarMensajes(codigo);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.enviar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etEnviar.getText().toString().length()<1){
                    tilMensaje.setError("Mensaje sin texto");
                }else {
                    tilMensaje.setError(null);
                    Mensaje mensaje = new Mensaje();
                    mensaje.setFechaHora(System.currentTimeMillis());
                    mensaje.setMensaje(etEnviar.getText().toString());
                    mensaje.setUsuario(firebaseUser.getDisplayName());
                    mandarMensaje(mensaje, codigo);

                }
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

public void reproducir(){

    sp=new SoundPool(1,1,1);
    sp.load(this, R.raw.boing, 1);
    sp.play(1,1,1,1,1,1);
}
    public void mostrarMensajes(final String codigo) {

        databaseReference.child(codigo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensajes.clear();
                if (vibrar==1){
                    viService.vibrate(30);
                }
                if (sonar==1){
                 reproducir();
                }
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    Mensaje mensaje = dataSnapshot1.getValue(Mensaje.class);
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
