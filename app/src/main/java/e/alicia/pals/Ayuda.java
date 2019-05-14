package e.alicia.pals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Ayuda extends AppCompatActivity {
    private TextView tvMensaje;
    private Spinner spOpciones;
    private String[] opciones = {"¿En qué podemos ayudarte?","Dar de alta un plan", "Borrar un plan", "Apuntarse a un plan",
            "Acceder al chat", "Desactivar notificaciones", "Darse de baja", "Modificar un plan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        spOpciones = (Spinner) findViewById(R.id.spOpciones);
        tvMensaje = (TextView) findViewById(R.id.tvOpcionAyuda);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opciones);

        spOpciones.setAdapter(adapter);
        spOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {

                } else if (position == 2) {

                } else if (position == 3) {

                } else if (position == 4) {

                } else if (position == 5) {

                } else if (position == 6) {

                } else if (position == 7) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
