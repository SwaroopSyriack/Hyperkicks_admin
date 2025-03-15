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

import java.util.ArrayList;

public class useradapter extends RecyclerView.Adapter<useradapter.MyVeiwholder2>{

    Context context;
    ArrayList<userModel> arrayList;

    public useradapter(Context context, ArrayList<userModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }




    @NonNull
    @Override
    public MyVeiwholder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list,parent,false);
        return new MyVeiwholder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVeiwholder2 holder, int position) {
        userModel model = arrayList.get(position);
        holder.username.setText(model.getUsername());
        holder.fname.setText(model.getFname());
        holder.email.setText(model.getEmail());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyVeiwholder2 extends RecyclerView.ViewHolder {

        TextView username,fname,email;
        public MyVeiwholder2(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name);
            fname = itemView.findViewById(R.id.user_fname);
            email = itemView.findViewById(R.id.email);
        }
    }
}
