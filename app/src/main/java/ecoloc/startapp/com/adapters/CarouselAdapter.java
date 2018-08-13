package ecoloc.startapp.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ecoloc.startapp.com.R;
import ecoloc.startapp.com.constractors.Ads;

/**
 * Created by ghazi on 28/05/18.
 */

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private Context context;
    public List<Ads> adsList;

    public CarouselAdapter(Context context, List<Ads> adsList) {
        this.context = context;
        this.adsList = adsList;
    }


    public class CarouselViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ImageView img;

        public CarouselViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            img = mView.findViewById(R.id.ImgFolder);

        }

    }

    @Override
    public CarouselViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_ads_info_carousell, parent, false);
        return new CarouselViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CarouselViewHolder holder, int position) {

/*
        ArrayList<String> links = adsList.get(position).getLinks();
        String [] urls = links.toArray(new String[links.size()]);

        Glide.with(context)
                .load(adsList.get(position).getLinks().toArray())
                .into(holder.img);*/
    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }


}
