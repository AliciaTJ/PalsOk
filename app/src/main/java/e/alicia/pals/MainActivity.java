package e.alicia.pals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ArrayListMultimap;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Usuario;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    DataBaseUsuario dataBaseUsuario;
    EditText etEmail;
    EditText etPass;
    View view;
    private ImageView image;
    private int RC_SIGN_IN=1;
    private final String ID_TOKEN="445597789329-l1nb5dtif2eed45qjl5bjq1t31ctdblv.apps.googleusercontent.com";
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        databaseReference=db.getReference("usuarios");
        dataBaseUsuario =new DataBaseUsuario(databaseReference);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPass=(EditText)findViewById(R.id.etPass);
        view = this.findViewById(android.R.id.content);

        image=(ImageView)findViewById(R.id.ivRandom);

        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ID_TOKEN)
                .requestEmail()

                .build();
        gso.getAccount();



    }

    public void cambioImagen(ImageView iv){
        ArrayList<Integer> imagenes=new ArrayList<>();
        imagenes.add(R.drawable.a1);
        imagenes.add(R.drawable.a2);
        imagenes.add(R.drawable.a3);
        imagenes.add(R.drawable.a4);
        imagenes.add(R.drawable.a5);
        imagenes.add(R.drawable.a6);
        imagenes.add(R.drawable.a7);
        imagenes.add(R.drawable.a8);
        iv.setImageResource(R.drawable.a1);
        int imagen= (int)Math.random()*7+1;
        iv.setImageResource(imagenes.get(imagen));
    }


    @Override
    public void onStart() {
        super.onStart();
/*
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

         updateUIG(account);
*/
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            updateUI(currentUser);
        }

    }

    public void google(View view) {
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                Usuario usuario = new Usuario();
                usuario.setCodigo(account.getId());
                usuario.setEmail(account.getEmail());
                usuario.setNombre(account.getDisplayName());
                usuario.setFoto(account.getPhotoUrl().toString());
                dataBaseUsuario.save(usuario);
         for(int i=0; i< mAuth.getCurrentUser().getProviderData().size(); i++){

         }

            updateUIG(account);

        } catch (ApiException e) {
            updateUI(null);
        }
    }



    public void logearse(){
        Intent i= new Intent(this, ActivityPortada.class);
        startActivity(i);

    }

    public void logIn (String email, String password){
        if (email.length()>0 && password.length()>0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                logearse();
                            } else {

                                Snackbar sb = Snackbar.make(view, "Error al loguear", Snackbar.LENGTH_LONG);
                                View snackBarView = sb.getView();

                                snackBarView.setBackgroundColor(Color.RED);
                                sb.show();
                            }


                        }


                    });
        }else{
            Snackbar.make(view, "Introduce usuario y contraseña", Toast.LENGTH_LONG).show();
        }

    }



    private void updateUI(FirebaseUser user) {

        if (user != null) {
            logearse();
        } else {

        }
    }
    private void updateUIG(GoogleSignInAccount user) {

        if (user != null) {
            logearse();
        } else {

        }
    }


    public void acceder(View view) {
        logIn(etEmail.getText().toString(), etPass.getText().toString());
    }



    public void registro(View view) {
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }

}
