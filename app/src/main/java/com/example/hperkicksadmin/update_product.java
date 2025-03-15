package com.example.hperkicksadmin;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class update_product extends Fragment {
    EditText product_name_up;
    EditText product_desc_up;
    EditText product_mrp_up;
    EditText product_sp_up;
    Spinner cat_spinner_up;
    ImageView product_img_up;
    Button product_img_update;
    Button product_update;

    Button product_delete;

    String document_id;

    ArrayList<String> datalist;
    Uri product_uri;
    String brand_selected_up;
    String product_id;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_product, container, false);
        product_name_up = view.findViewById(R.id.Product_name_up);
        product_desc_up = view.findViewById(R.id.Product_description_up);
        product_mrp_up = view.findViewById(R.id.Product_MRP_up);
        product_sp_up = view.findViewById(R.id.Product_sp_up);
        cat_spinner_up = view.findViewById(R.id.cat_spinner_up);

        product_img_up = view.findViewById(R.id.product_img_view_up);
        product_img_update = view.findViewById(R.id.product_img_btn_up);
        product_update = view.findViewById(R.id.product_submit_btn_up);
        get_spinner_up();


        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            if (o.getResultCode() == Activity.RESULT_OK)
            {
                Intent data = o.getData();
                product_uri = data.getData();
                product_img_up.setImageURI(product_uri);
            }
            else{
                Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        });



        product_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = getArguments();
                if (args != null) {
                    document_id = args.getString("key");
                    // Use the received data as needed
                }
                String name = product_name_up.getText().toString().trim();
                String Description = product_desc_up.getText().toString().trim();
                String mrp = product_mrp_up.getText().toString().trim();
                String sp = product_sp_up.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    product_name_up.setError("Pls enter the Title");
                } else if (TextUtils.isEmpty(Description)) {
                    product_desc_up.setError("Pls enter the Email");
                } else if (TextUtils.isEmpty(mrp)) {
                    product_mrp_up.setError("Pls enter the Ocuppation");
                } else if (TextUtils.isEmpty(sp)) {
                    product_sp_up.setError("Pls enter the bio");}
                else {
                    upload_product_tofirebase(document_id,name,Description,mrp,sp,brand_selected_up);
                }
            }
        });


        product_img_up.setOnClickListener(view1 -> {
            Intent photopicker = new Intent();
            photopicker.setAction(Intent.ACTION_GET_CONTENT);
            photopicker.setType("image/*");
            resultLauncher.launch(photopicker);
        });

        product_img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product_uri != null){
                    update_img_to_firebase(product_uri);
                }
                else{
                    Toast.makeText(getActivity(), "Select a Image", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void update_img_to_firebase(Uri product_uri) {
        StorageReference imagereference = storageReference.child(System.currentTimeMillis()+"."+getfileextension(product_uri));
        imagereference.putFile(product_uri).addOnSuccessListener(taskSnapshot -> imagereference.getDownloadUrl().addOnSuccessListener(uri1 -> {
            firestore.collection("products").
                    document(product_id).
                    update("imgUrl", uri1.toString());
            Toast.makeText(getActivity(), "Image Updated", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Failed to upload Image", Toast.LENGTH_SHORT).show();
            Log.e("Error msg ",e.getMessage());
        });
    }




    private void upload_product_tofirebase(String product_id, String name, String description, String mrp, String sp, String brand_selected_up) {
        //product_id,name,description,mrp,sp,brand,ImgUrl

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("product_id", product_id);
        updatedData.put("description", description);
        updatedData.put("mrp", name);
        updatedData.put("sp", name);
        updatedData.put("brand", name);

        firestore.collection("products").document(product_id).update(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireActivity(), "DATA UPDATED", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("msg",e.getMessage());

            }
        });

    }

    private void get_spinner_up() {

        datalist = new ArrayList<>();
        firestore.collection("brands").get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult())
                {
                    String fieldvalue = queryDocumentSnapshot.getString("brandname");
                    datalist.add(fieldvalue);
                }
                datalist.add(0,"Select The Brand");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,datalist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cat_spinner_up.setAdapter(adapter);
                cat_spinner_up.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        brand_selected_up  = datalist.get(i);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getActivity(), "No items selected ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(getActivity(), "Error Occuired", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getfileextension(Uri uri) {
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}