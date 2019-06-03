package e.alicia.pals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Opciones extends AppCompatActivity {
    private Switch swNotificaciones, swVibracion, swSonido;

    private View view;
    private FirebaseUser user;
    private SharedPreferences sharedPreferences;
    private MediaPlayer mp;
    private Vibrator viService;
    private int vibrar, sonar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        //se inician los elementos del activity
        swNotificaciones = (Switch) findViewById(R.id.swNotificaciones);
        swSonido = (Switch) findViewById(R.id.swSonido);
        swVibracion = (Switch) findViewById(R.id.swVibracion);
        view = this.findViewById(android.R.id.content);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //se guarda en shared preference si se quiere vibracion y sonido
        sharedPreferences = getSharedPreferences("opciones", Context.MODE_PRIVATE);
        vibrar=sharedPreferences.getInt("vibracion", 1);
        sonar=sharedPreferences.getInt("sonido", 1);
        if (vibrar==1){
            swVibracion.setChecked(true);
        }if (sonar==1){
            swSonido.setChecked(true);
        }

        //accion del sswitch vibracion
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        swVibracion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar.make(view, "Vibracion activada", Snackbar.LENGTH_LONG).show();
                    editor.putInt("vibracion", 1);
                }else{
                    Snackbar.make(view, "Vibracion desactivada", Snackbar.LENGTH_LONG).show();
                    editor.putInt("vibracion", 0);
                }
                editor.commit();
            }
        });

        //accion del switch sonido
        swSonido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar.make(view, "Sonido activado", Snackbar.LENGTH_LONG).show();
                    editor.putInt("sonido", 1);
                } else {
                    Snackbar.make(view, "Sonido desactivado", Snackbar.LENGTH_LONG).show();
                    editor.putInt("sonido", 0);
                }
                editor.commit();
            }
        });

        //accion del switch notificaciones
        swNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar.make(view, "Notificaciones activadas", Snackbar.LENGTH_LONG).show();
                    editor.putInt("notificaciones", 1);
                } else {
                    Snackbar.make(view, "Notificaciones desactivadas", Snackbar.LENGTH_LONG).show();
                    editor.putInt("notificaciones", 0);
                }
                editor.commit();
            }
        });
    }


    /**
     * Metodo que envia un email al administrador
     * @param view
     */
    public void enviarEmail(View view) {
        String[] TO = {"aliciavisual@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ayuda Pals!");


        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            finish();
        } catch (android.content.ActivityNotFoundException anf) {
            Snackbar.make(view, "No tienes clientes de email instalados", Snackbar.LENGTH_LONG).show();
        }

    }

    /**
     * Metodo que devuelve al acctivity portada si se pulsa volver
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, ActivityPortada.class);
        startActivity(i);
    }
}
