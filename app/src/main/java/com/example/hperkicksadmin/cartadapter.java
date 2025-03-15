package com.example.hperkicksadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class cartadapter extends RecyclerView.Adapter<cartadapter.MyVeiwholder2> {


    Context context;
    ArrayList<cartModel> arrayList;

    public cartadapter(Context context) {
        this.context = context;
        arrayList = new ArrayList<>();
    }

    public void add_product(cartModel CartModel){
        arrayList.add(CartModel);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyVeiwholder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        return new MyVeiwholder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVeiwholder2 holder, int position) {
        cartModel add = arrayList.get(position);
        Glide.with(context).load(arrayList.get(position).getProductImageUrl()).into(holder.productimg);
        holder.productname.setText(add.getProductName());
        holder.qty.setText(add.getProductQty());
        holder.size.setText(add.getProductsize());
        holder.price.setText(add.getProductPrice());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyVeiwholder2 extends RecyclerView.ViewHolder {

        ImageView productimg;
        TextView productname,price,qty,size;
        private final LinearLayout main;

        public MyVeiwholder2(@NonNull View itemView) {
            super(itemView);
            productimg = itemView.findViewById(R.id.cartitemImage1);
            productname = itemView.findViewById(R.id.productName1);
            price = itemView.findViewById(R.id.cartproductPrice);
            qty =itemView.findViewById(R.id.cartproductQuantity1);
            size = itemView.findViewById(R.id.size1);
            main = itemView.findViewById(R.id.thelayout2);
        }
    }
}


