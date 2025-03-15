package com.example.hperkicksadmin;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;


public class view_products extends Fragment {
    RecyclerView recyclerView1;
    ArrayList<productModel> arrayList;
    productAdapter adapter2;
    
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_products, container, false);

        recyclerView1 = view.findViewById(R.id.recycler_products);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList  = new ArrayList<productModel>();
        adapter2 = new productAdapter(getContext(),arrayList);
        recyclerView1.setAdapter(adapter2);
        image_viewer();




        return view;
    }


    void image_viewer(){
        firestore.collection("products").addSnapshotListener((value, error) -> {
            if (error != null)
            {
                Log.e("Firebase error",error.getMessage());

            }
            for(DocumentChange dc : value.getDocumentChanges()){


                if (dc.getType() == DocumentChange.Type.ADDED)
                {
                    arrayList.add(dc.getDocument().toObject(productModel.class));
                }
                adapter2.notifyDataSetChanged();
            }


        });

    }
}