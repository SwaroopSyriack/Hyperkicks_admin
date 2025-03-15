package com.example.hperkicksadmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class brandAdapter extends RecyclerView.Adapter<brandAdapter.MyViewHolder> {

    private static ArrayList<brandModel> arrayList;
    private final Context context;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public brandAdapter(ArrayList<brandModel> dataList, Context context) {
        this.arrayList = dataList;
        this.context = context;
    }


    // Adapter of Brand
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brand_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).getImageUrl()).into(holder.recycler_img);
        holder.brand_name.setText(arrayList.get(position).getBrandname());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // Used to delete the item from the Recycler view
    public void delete(int position){
        if (arrayList.size() != 0){
        brandModel delete = arrayList.get(position);
        String id = delete.getId();
        delete_from_firebase(id,position);
        }
        else{
            Log.d("Error","Cannot delete all items");
        }
    }

    private void delete_from_firebase(String id,int position) {
        firestore.collection("brands").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    arrayList.remove(position);
                    notifyItemRemoved(position);
                }

            }
        });

    }


    //Class for viewholder
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView recycler_img;
        ImageButton button;

        TextView brand_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recycler_img = itemView.findViewById(R.id.recyclerImage);
            brand_name = itemView.findViewById(R.id.textViewbrand);
            button = itemView.findViewById(R.id.imageButton);
            button.setVisibility(View.INVISIBLE);

            //to delete the item from the array list
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (position != RecyclerView.NO_POSITION) {
                                // Call a method to delete the item
                                delete(position);
                            }
                        }
                    });
                    return false;
                }
            });
        }
    }
}


