package e.alicia.pals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Clase que carga el activity de ayuda
 */
public class Ayuda extends AppCompatActivity {
    private TextView tvMensaje;
    private Spinner spOpciones;
    private String[] opciones = {"¿En qué podemos ayudarte?", "Dar de alta un plan", "Borrar un plan", "Apuntarse a un plan",
            "Acceder al chat", "Desactivar notificaciones", "Darse de baja", "Modificar un plan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        spOpciones = (Spinner) findViewById(R.id.spOpciones);
        tvMensaje = (TextView) findViewById(R.id.tvOpcionAyuda);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opciones);



        //se establece una accion de cambio de texto para cada posicion de spinner
        spOpciones.setAdapter(adapter);
        spOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    tvMensaje.setText(R.string.ayudaalta);
                } else if (position == 2) {
                    tvMensaje.setText(R.string.ayudaborrar);
                } else if (position == 3) {
                    tvMensaje.setText(R.string.ayudaapuntarse);
                } else if (position == 4) {
                    tvMensaje.setText(R.string.ayudachat);
                } else if (position == 5) {
                    tvMensaje.setText(R.string.ayudanotificaciones);

                } else if (position == 6) {
                    tvMensaje.setText(R.string.ayudabaja);

                } else if (position == 7) {
                    tvMensaje.setText(R.string.ayudamodificar);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
