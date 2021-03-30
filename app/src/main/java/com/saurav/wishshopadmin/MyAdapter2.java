package com.saurav.wishshopadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.AdapterViewHolder> {
    private Context mContext;
    private List<Main2Modal> main2modaldata2;
    private int lastPosition=-1;

    public MyAdapter2(Context mContext, List<Main2Modal> main2modaldata2) {
        this.mContext = mContext;
        this.main2modaldata2 = main2modaldata2;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item,parent,false);
        return new AdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Glide.with(mContext)
                .load(main2modaldata2.get(position).getItemImage())
                .into(holder.imageView);
        // holder.imageView.setImageResource(main2modaldata.get(position).getItemImage());
        holder.mTitle.setText(main2modaldata2.get(position).getItemName());
        holder.mDescription.setText(main2modaldata2.get(position).getItemDescription());
        holder.mPrice.setText(main2modaldata2.get(position).getItemPrice());

        holder.mcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("Image",main2modaldata2.get(holder.getAdapterPosition()).getItemImage());
                intent.putExtra("Description",main2modaldata2.get(holder.getAdapterPosition()).getItemDescription());
                intent.putExtra("Name",main2modaldata2.get(holder.getAdapterPosition()).getItemName());
                intent.putExtra("Price",main2modaldata2.get(holder.getAdapterPosition()).getItemPrice());
                intent.putExtra("keyValue",main2modaldata2.get(holder.getAdapterPosition()).getKey());
                mContext.startActivity(intent);
            }
        });
        setAnimation(holder.itemView,position);



    }
    public void setAnimation(View viewToAnimate,int position) {
        if (position > lastPosition) {
            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.05f);
            animation.setDuration(1500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    public void filteredList(ArrayList<Main2Modal> filterList) {
        main2modaldata2=filterList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return main2modaldata2.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mTitle,mDescription,mPrice;
        CardView mcardView;

        public AdapterViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivImage);
            mTitle=itemView.findViewById(R.id.tvTitle);
            mDescription=itemView.findViewById(R.id.tvDescription);
            mPrice=itemView.findViewById(R.id.tvPrice);

            mcardView = itemView.findViewById(R.id.myCardView);
        }
    }


}
