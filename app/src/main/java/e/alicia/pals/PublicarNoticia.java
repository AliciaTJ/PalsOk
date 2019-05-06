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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import e.alicia.pals.baseDatos.DataBaseNoticia;
import e.alicia.pals.modelo.Noticia;
import e.alicia.pals.modelo.Usuario;

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


    public void iniciarActivity() {
        etContenido = (EditText) findViewById(R.id.etContenido);
        etTitulo = (EditText) findViewById(R.id.etTitulo);
        imagen = (ImageView) findViewById(R.id.ivImagen);
        dataBaseNoticia = new DataBaseNoticia();
        tilcontenido = (TextInputLayout) findViewById(R.id.tilcontenido);
        tiltitulo = (TextInputLayout) findViewById(R.id.tiltitulo);


    }

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

    public void cambiarFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Elige archivo"), VALOR_RETORNO);
    }


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



    private boolean esNombreValido(String nombre) {
        if (nombre.length() > 30 || nombre.length() < 3) {
            tiltitulo.setError("El titulo debe tener entre 3 y 50 caracteres");
            return false;
        } else {
            tiltitulo.setError(null);
        }

        return true;
    }

    private boolean esInfoValido(String nombre) {

        if (nombre.length() < 10) {
            tilcontenido.setError("Introduce contenido (minimo 50 caracteres)");
            return false;
        } else {
            tilcontenido.setError(null);
        }

        return true;
    }


}
