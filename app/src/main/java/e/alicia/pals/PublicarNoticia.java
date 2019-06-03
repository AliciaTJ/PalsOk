package e.alicia.pals;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import e.alicia.pals.baseDatos.DataBaseNoticia;
import e.alicia.pals.modelo.Noticia;


/**
 * Clase que carga el activity publicarnnoticia
 */
public class PublicarNoticia extends AppCompatActivity {


    private EditText etContenido, etTitulo;
    private ImageView imagen;
    private Noticia noticia = new Noticia();
    private DataBaseNoticia dataBaseNoticia;
    final int VALOR_RETORNO = 1;
    private TextInputLayout tilcontenido, tiltitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_noticia);
        iniciarActivity();

    }

    /**
     * Carga los elementos
     */

    public void iniciarActivity() {
        etContenido = (EditText) findViewById(R.id.etContenido);
        etTitulo = (EditText) findViewById(R.id.etTitulo);
        imagen = (ImageView) findViewById(R.id.ivImagen);
        dataBaseNoticia = new DataBaseNoticia();
        tilcontenido = (TextInputLayout) findViewById(R.id.tilcontenido);
        tiltitulo = (TextInputLayout) findViewById(R.id.tiltitulo);


    }


    /**
     * Metodo que comprueba la informacion introducida y graba la noticia en la base de datos
     * si es correcta
     * @param view View
     */
    public void publicarNoticia(View view) {
        String cuerpo = etContenido.getText().toString();
        String titular = etTitulo.getText().toString();
        if (esInfoValido(cuerpo) && esNombreValido(titular)) {
            noticia.setCuerpo(cuerpo);
            noticia.setTitular(titular);
            dataBaseNoticia.publicar(noticia);
            etContenido.setText("");
            etTitulo.setText("");
            imagen.setImageResource(R.drawable.foto);
            Snackbar.make(view, "Noticia publicada con exito", Snackbar.LENGTH_LONG).show();







        } else {
            Snackbar.make(view, "Error al publicar la noticia", Snackbar.LENGTH_LONG).show();
        }
    }


    /**
     * Metodo que llama a otro intent para acceder al archivo de fotos del movil
     * @param view View
     */
    public void cambiarFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Elige archivo"), VALOR_RETORNO);
    }


    /**
     * Metodo que recoge los datos de cambiarfoto() y los muestra
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((resultCode == RESULT_OK) && (requestCode == VALOR_RETORNO)) {

            Uri uri = data.getData();
            noticia.setCodigo(String.valueOf(System.currentTimeMillis()));

                    dataBaseNoticia.guardarFoto(uri, noticia);
                    imagen.setImageURI(uri);
                }

        }


    /**
     * Conprueba que el nombre es valido
     * @param nombre String
     * @return boolean
     */
    private boolean esNombreValido(String nombre) {
        if (nombre.length() > 50 || nombre.length() < 3) {
            tiltitulo.setError("El titulo debe tener entre 3 y 50 caracteres");
            return false;
        } else {
            tiltitulo.setError(null);
        }

        return true;
    }


    /**
     * Conprueba que la informacion es valida
     * @param nombre String
     * @return boolean
     */
    private boolean esInfoValido(String nombre) {

        if (nombre.length() < 40) {
            tilcontenido.setError("Introduce contenido (minimo 50 caracteres)");
            return false;
        } else {
            tilcontenido.setError(null);
        }

        return true;
    }


}
