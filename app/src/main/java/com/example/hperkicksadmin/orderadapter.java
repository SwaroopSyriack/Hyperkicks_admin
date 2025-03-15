package com.example.hperkicksadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class orderadapter extends RecyclerView.Adapter<Viewholder3>{

    Context context;
    ArrayList<OrderModel> arrayList;

    public orderadapter(Context context, ArrayList<OrderModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public Viewholder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new Viewholder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder3 holder, int position) {
        OrderModel add = arrayList.get(position);
        holder.name.setText(add.getCustomer_fname());
        holder.address.setText(add.getCustomer_address());
        holder.city.setText(add.getCustomer_place());
        holder.ordernumber.setText(add.getOrdernumber());


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.deliver.setText("Delivered");
            }
        });

        cartadapter cartAdapter = new cartadapter(context);
        holder.order_recycler.setAdapter(cartAdapter);
        holder.order_recycler.setLayoutManager(new LinearLayoutManager(context));
        FirebaseFirestore.getInstance().collection("orderProducts")
                .whereEqualTo("ordernumber",add.getOrdernumber()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot documentSnapshot:documentSnapshotList){
                            cartModel CartModel = documentSnapshot.toObject(cartModel.class);
                            cartAdapter.add_product(CartModel);
                        }

                    }
                });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
class Viewholder3 extends RecyclerView.ViewHolder{


    TextView name;
    TextView address;
    TextView city;
    TextView ordernumber;

    TextView deliver;
    Button button;
    RecyclerView order_recycler;


    public Viewholder3(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.ordername);
        address = itemView.findViewById(R.id.orderaddress);
        city = itemView.findViewById(R.id.ordercity);
        ordernumber = itemView.findViewById(R.id.orderno);
        button = itemView.findViewById(R.id.button);
        deliver = itemView.findViewById(R.id.textView9);
        order_recycler = itemView.findViewById(R.id.recyclerView2);
    }
}
