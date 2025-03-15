package com.example.hperkicksadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

import java.util.ArrayList;

public class orderview extends AppCompatActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    ArrayList<OrderModel> orderList;

    orderadapter adapter;

    RecyclerView recyclerView;

    ImageView arrow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderview);

        arrow = findViewById(R.id.backarrow1);
        recyclerView = findViewById(R.id.orderrecycler);
        orderList = new ArrayList<>();
        adapter = new orderadapter(this,orderList);
        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(orderview.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adapter);
        import_order();

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new dashboard_frag()).commit();
            }
        });

    }

    private void import_order() {
        firestore.collection("orders").addSnapshotListener((value, error) -> {
            if (error != null)
            {
                Log.e("Firebase error",error.getMessage());

            }
            for(DocumentChange dc : value.getDocumentChanges()){


                if (dc.getType() == DocumentChange.Type.ADDED)
                {
                    orderList.add(dc.getDocument().toObject(OrderModel.class));
                }
                adapter.notifyDataSetChanged();
            }
            Log.e("msg","OnEvevt executed");


        });

    }
}