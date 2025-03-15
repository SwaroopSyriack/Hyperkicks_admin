package com.example.hperkicksadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class productAdapter extends RecyclerView.Adapter<MyViewHolder1> {

    Context context;
    ArrayList<productModel> arrayList;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public productAdapter(Context context, ArrayList<productModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list,parent,false);
        return new MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, @SuppressLint("RecyclerView") int position) {
        productModel add = arrayList.get(position);
        Glide.with(context).load(arrayList.get(position).getImgUrl()).into(holder.img);
        holder.product_name.setText(add.getBrand());
        holder.product_mrp.setText(add.getMrp());
        holder.product_stock.setText(add.getSp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("product id", add.product_id);
                Fragment fragment = new update_product();
                fragment.setArguments(bundle);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null) // Optional: Add to back stack for fragment navigation
                        .commit();

            }
        });

        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = arrayList.get(position).product_id;
                delete_from_firebase(id,position);
            }
        });


    }

    private void delete_from_firebase( String id,int position) {
        firestore.collection("products").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    arrayList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(context, "ERROR WHILE DELETING", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}


class MyViewHolder1 extends RecyclerView.ViewHolder {

    ImageView img;
    TextView product_name;
    TextView product_mrp;
    TextView product_stock;

    ImageButton delete_item;

    public MyViewHolder1(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.img1);
        product_name = itemView.findViewById(R.id.product_name);
        product_mrp = itemView.findViewById(R.id.mrp);
        product_stock = itemView.findViewById(R.id.stock);
        delete_item = itemView.findViewById(R.id.delete_item);


    }

}
