package com.example.hperkicksadmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class view_brands extends Fragment {

    RecyclerView recyclerView;
    ArrayList<brandModel> arrayList;

    brandAdapter adapter1;

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_brands, container, false);
        recyclerView = view.findViewById(R.id.recyclerbrand);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList  = new ArrayList<>();
        adapter1 = new brandAdapter(arrayList,getContext());
        recyclerView.setAdapter(adapter1);
        image_viewer();


        return view;
    }

    private void image_viewer() {

        firestore.collection("brands").addSnapshotListener((value, error) -> {
            if (error != null)
            {
                Log.e("Firebase error",error.getMessage());

            }
            for(DocumentChange dc : value.getDocumentChanges()){


                if (dc.getType() == DocumentChange.Type.ADDED)
                {
                    arrayList.add(dc.getDocument().toObject(brandModel.class));
                }
                adapter1.notifyDataSetChanged();
            }
            Log.e("msg","OnEvevt executed");


        });


    }
}