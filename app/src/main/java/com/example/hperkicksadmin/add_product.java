package com.example.hperkicksadmin;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class add_product extends Fragment {
    EditText product_name;
    EditText product_desc;
    EditText product_mrp;
    EditText product_sp;
    Spinner  cat_spinner;
    ImageView product_img;
    Button product_img_upload;
    Button product_submit;
    ArrayList<String> datalist;
    Uri product_uri;
    String brand_selected;
    String product_id;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_product, container, false);
        product_name = view.findViewById(R.id.Product_name);
        product_desc = view.findViewById(R.id.Product_description);
        product_mrp = view.findViewById(R.id.Product_MRP);
        product_sp = view.findViewById(R.id.Product_sp);
        cat_spinner = view.findViewById(R.id.cat_spinner);

        product_img = view.findViewById(R.id.product_img_view);
        product_img_upload = view.findViewById(R.id.product_img_btn);
        product_submit = view.findViewById(R.id.product_submit_btn);
        get_spinner();
        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            if (o.getResultCode() == Activity.RESULT_OK)
            {
                Intent data = o.getData();
                product_uri = data.getData();
                product_img.setImageURI(product_uri);
            }
            else{
                Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        });
        product_submit.setOnClickListener(view13 -> {
            product_id  = UUID.randomUUID().toString();
            String name = product_name.getText().toString().trim();
            String Description = product_desc.getText().toString().trim();
            String mrp = product_mrp.getText().toString().trim();
            String sp = product_sp.getText().toString().trim();
            String brand = brand_selected;
            if (TextUtils.isEmpty(name)) {
                product_name.setError("Pls enter the Title");
            } else if (TextUtils.isEmpty(Description)) {
                product_desc.setError("Pls enter the Email");
            } else if (TextUtils.isEmpty(mrp)) {
                product_mrp.setError("Pls enter the Ocuppation");
            } else if (TextUtils.isEmpty(sp)) {
                product_sp.setError("Pls enter the bio");}
            else {
                upload_product_tofirebase(product_id,name,Description,mrp,sp,brand);
            }
        });
        product_img.setOnClickListener(view1 -> {
            Intent photopicker = new Intent();
            photopicker.setAction(Intent.ACTION_GET_CONTENT);
            photopicker.setType("image/*");
            resultLauncher.launch(photopicker);
        });
        product_img_upload.setOnClickListener(view12 -> {
            if (product_uri != null){
                upload_img_to_firebase(product_uri);
            }
            else{
                Toast.makeText(getActivity(), "Select a Image", Toast.LENGTH_SHORT).show();
            }

        });
        return  view;
    }
    private void upload_img_to_firebase(Uri uri) {
        StorageReference imagereference = storageReference.child(System.currentTimeMillis()+"."+getfileextension(uri));
        imagereference.putFile(uri).addOnSuccessListener(taskSnapshot -> imagereference.getDownloadUrl().addOnSuccessListener(uri1 -> {
            firestore.collection("products").
                    document(product_id).
                    update("imgUrl", uri1.toString());
            Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Failed to upload Image", Toast.LENGTH_SHORT).show();
            Log.e("Error msg ",e.getMessage());
        });
    }

    private String getfileextension(Uri uri) {
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void upload_product_tofirebase(String product_id,String name,String Description,String mrp,String sp,String brand) {
        productModel productmodel = new productModel(product_id,name,Description,mrp,sp,brand,null);
        firestore.collection("products").document(product_id).set(productmodel).addOnCompleteListener(task -> Toast.makeText(getActivity(), "Product data Uploaded", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();
            Log.e("error",e.getMessage());
        });
    }

    private void get_spinner(){
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
                cat_spinner.setAdapter(adapter);
                cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        brand_selected  = datalist.get(i).trim();
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
}