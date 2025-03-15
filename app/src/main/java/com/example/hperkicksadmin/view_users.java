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

import java.util.ArrayList;

public class view_users extends AppCompatActivity {

    useradapter adapter2;

    ArrayList<userModel> userlist;

    RecyclerView recyclerView;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        recyclerView = findViewById(R.id.userrecycler);
        backarrow = findViewById(R.id.backarrow12);

        userlist = new ArrayList<>();
        adapter2 = new useradapter(this,userlist);
        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(view_users.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adapter2);
        import_user();

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new dashboard_frag()).commit();
            }
        });


    }

    private void import_user() {
        firestore.collection("users").addSnapshotListener((value, error) -> {
            if (error != null)
            {
                Log.e("Firebase error",error.getMessage());

            }
            for(DocumentChange dc : value.getDocumentChanges()){


                if (dc.getType() == DocumentChange.Type.ADDED)
                {
                    userlist.add(dc.getDocument().toObject(userModel.class));
                }
                adapter2.notifyDataSetChanged();
            }


        });

    }
}