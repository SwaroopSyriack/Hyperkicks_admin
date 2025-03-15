package com.example.hperkicksadmin;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class add_brands extends Fragment {

    ImageView upload_img_brand;
    EditText  upload_brand_name;

    Uri brand_uri;

    String id;

    Button upload_brand_btn;


    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_brands, container, false);

        upload_img_brand = view.findViewById(R.id.uploadImage_brand);
        upload_brand_name = view.findViewById(R.id.uploadBrand);
        upload_brand_btn = view.findViewById(R.id.uploadButtonbrand);


        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            if (o.getResultCode() == Activity.RESULT_OK)
            {
                Intent data = o.getData();
                brand_uri = data.getData();
                upload_img_brand.setImageURI(brand_uri);


            }

            else{
                Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }

        });

        upload_img_brand.setOnClickListener(view1 -> {
            Intent photopicker = new Intent();
            photopicker.setAction(Intent.ACTION_GET_CONTENT);
            photopicker.setType("image/*");
            resultLauncher.launch(photopicker);
        });

        upload_brand_btn.setOnClickListener(view12 -> {
            if (brand_uri != null)
            {
                upload_firebase(brand_uri);
            }
            else{
                Toast.makeText(getActivity(), "Please select a image", Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }

    private void upload_firebase(Uri uri){

        String brand_name = upload_brand_name.getText().toString();
        id = UUID.randomUUID().toString();
        StorageReference imagereference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        imagereference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            imagereference.getDownloadUrl().addOnSuccessListener(uri1 -> {

                brandModel brandModel = new brandModel(id,uri1.toString(),brand_name);
                firestore.collection("brands")
                        .document(id)
                        .set(brandModel);
                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();

            });

        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show());

    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }


}