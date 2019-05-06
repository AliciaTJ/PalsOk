package e.alicia.pals.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.R;
import e.alicia.pals.modelo.Noticia;


/**
 * Clase que carga los elementos noticia en una plantilla para el recycler view
 */
public class AdapterNoticias extends RecyclerView.Adapter<AdapterNoticias.ItemViewHolder> {
    private List<Noticia> mUserLsit;
    private Context mContext;


    /**
     * Clase que carga la plantilla del modelo noticia
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public AdapterNoticias.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelnoticias, parent, false);
        return new ItemViewHolder(view);
    }

    public AdapterNoticias(Context mContext, List<Noticia> mUserLsit) {
        this.mContext = mContext;
        this.mUserLsit = mUserLsit;
    }

    /**
     * Establece el valor de cada elemento del arraylist
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final AdapterNoticias.ItemViewHolder holder, int position) {
        Noticia noticia = mUserLsit.get(position);
        holder.tvTitular.setText(noticia.getTitular());
        holder.tvCuerpo.setText(noticia.getCuerpo());
        StorageReference storage= FirebaseStorage.getInstance().getReference("noticias/"+noticia.getCodigo());

                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        Glide
                                .with(mContext)
                                .load(uri)
                                .into(holder.ivNoticia);
                    }

                });




    }


    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    /**
     * Carga los elementos de la plantilla
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitular, tvCuerpo;
        ImageView ivNoticia;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvCuerpo = itemView.findViewById(R.id.tvCuerpo);
            tvTitular = itemView.findViewById(R.id.tvTitular);
            ivNoticia = itemView.findViewById(R.id.ivNoticia);

        }
    }
}

