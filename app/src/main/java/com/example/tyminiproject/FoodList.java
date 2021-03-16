package com.example.tyminiproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Food;
import com.example.tyminiproject.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodList extends AppCompatActivity {

    private static final String TAG = "FoodList";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference foodList;
    StorageReference storageReference;
    String messId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    FloatingActionButton fabAddNewFood;
    EditText etMenuName, etDesc, etPrice;
    Button btn_select, btn_upload;

    Food newFood;
    Uri saveUri;
    private final int PICK_IMG_REQ = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        fabAddNewFood = findViewById(R.id.fabAddNewFood);
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null)
            messId = getIntent().getStringExtra("MessId");
        Log.e(TAG, "inside onCreate : messId---" + messId);
        if (!messId.isEmpty() && messId != null) {
            loadFoodList(messId);
        }

        fabAddNewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Add New Food");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_new_food_layout, null);

        etMenuName = add_food_layout.findViewById(R.id.et_MenuName);
        etDesc = add_food_layout.findViewById(R.id.et_MenuDesc);
        etPrice = add_food_layout.findViewById(R.id.et_price);
        btn_select = add_food_layout.findViewById(R.id.btn_select);
        btn_upload = add_food_layout.findViewById(R.id.btn_upload);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgFromGallery();
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(FoodList.this, " UPLOADING!!! ", Toast.LENGTH_LONG).show();
                uploadImage();
            }
        });
        alertDialog.setView(add_food_layout);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //here , just created new category
                if (newFood != null) {
                    foodList.push().setValue(newFood);
                    Toast.makeText(FoodList.this, "New Food Added : " + newFood.getName(), Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void getImgFromGallery() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Image of Food"), PICK_IMG_REQ);
    }


    private void uploadImage() {

        if (saveUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("uploading");
            progressDialog.show();

            String imgName = UUID.randomUUID().toString();
            Log.d(TAG, "uploadImage: imgName : " + imgName);
            StorageReference imgFolder = storageReference.child("food_images/" + imgName);
            Log.d(TAG, "uploadImage: imgFolder : " + imgFolder);
            imgFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(FoodList.this, "UPLOADED!!!", Toast.LENGTH_LONG).show();
                    imgFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new category if image upload and we can get downloadd link
                            String str_Foodname = etMenuName.getText().toString();
                            String strDesc = etDesc.getText().toString();
                            String strPrice = etPrice.getText().toString();
                            String str_uri = uri.toString();
                            String discount = " ";
                            Log.d(TAG, "onSuccess:  str_name : " + str_Foodname);
                            Log.d(TAG, "onSuccess:  strDesc : " + strDesc);
                            Log.d(TAG, "onSuccess:  strPrice : " + strPrice);
                            Log.d(TAG, "onSuccess: str_uri : " + str_uri);
                            newFood = new Food(str_Foodname, str_uri, strPrice, strDesc, discount, messId);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(FoodList.this, "Faild!!!" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double prgrss = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded" + prgrss + "%");

                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            btn_select.setText("Food Img Selected!!");

        }
    }

    private void loadFoodList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,
                FoodViewHolder.class, foodList.orderByChild("menuId").equalTo(categoryId)     // (select * from foods where menuId=categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food model, int i) {
                foodViewHolder.FoodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(foodViewHolder.FoodImage);

                final Food local = model;
                foodViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, "" + local.getName(), Toast.LENGTH_LONG).show();
                        String foodName = local.getName();
                        String foodPrice = local.getPrice();
                        String foodDesc = local.getDescription();
                        String foodDiscont = local.getDiscount();
                        Log.e(TAG, "inside loadFoodList : fname : " + foodName);
                        Log.e(TAG, "inside loadFoodList: fprice : " + foodPrice);
                        Log.e(TAG, "inside loadFoodList : fdiscount : " + foodDiscont);
                        Log.e(TAG, "inside loadFoodList : fdesc : " + foodDesc);
                        Intent i = new Intent(FoodList.this, FoodDetail.class);
                        i.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(i);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}