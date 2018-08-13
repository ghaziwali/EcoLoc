package ecoloc.startapp.com.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ecoloc.startapp.com.adPageActivity;
import ecoloc.startapp.com.constractors.Ads;
import ecoloc.startapp.com.R;


/**
 * Created by ghazi on 22/05/18.
 */

public class HomeAdsListAdapter extends RecyclerView.Adapter<HomeAdsListAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore;

    public List<Ads> adsList;
    public Context context;

    public HomeAdsListAdapter(Context context, List<Ads> adsList) {
        this.adsList = adsList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView titleTxt;
        public TextView priceTxt;
        public CircleImageView adImG;

        public ViewHolder(View itemView) {

            super(itemView);
            mView = itemView;
            titleTxt = (TextView) mView.findViewById(R.id.title_txt);
            priceTxt = (TextView) mView.findViewById(R.id.price_txt);
            adImG = (CircleImageView) mView.findViewById(R.id.ad_img);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ads, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HomeAdsListAdapter.ViewHolder holder, final int position) {
        mFirestore = FirebaseFirestore.getInstance();
        String link = adsList.get(position).getLink();
        holder.titleTxt.setText(adsList.get(position).getTitle());
        holder.priceTxt.setText(adsList.get(position).getPrice());
        Picasso.with(context)
                .load(link)
                .fit()
                .centerCrop()
                .into(holder.adImG);


        final String ad_id = adsList.get(position).adsID;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, adPageActivity.class);
                intent.putExtra("id", ad_id);
                intent.putExtra("adTitle", adsList.get(position).getTitle());
                intent.putExtra("user_id",adsList.get(position).getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

}
