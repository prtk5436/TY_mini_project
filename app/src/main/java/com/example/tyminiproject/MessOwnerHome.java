package com.example.tyminiproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Food;
import com.example.tyminiproject.Model.MessUser;
import com.example.tyminiproject.ViewHolder.FoodViewHolder;
import com.example.tyminiproject.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class MessOwnerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MessOwnerHome";
    DrawerLayout drawerLayout1;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView tvRegNo, tvMessName;
    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseStorage storage;
    StorageReference storageReference;

    int f = 0;
    //view
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<MessUser, MenuViewHolder> adapter;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter1;
    String strMob, strMessMob, foodChildName;
    Button btn_ViewMess;

    String messName = "", str_Foodname;
    Food newFood;
    Uri saveUri;
    private final int PICK_IMG_REQ = 71;
    //add new menu layout
    EditText et_name;
    EditText etMenuName, etDesc, etPrice;
    Button btn_upload, btn_select;
    FirebaseDatabase database1;
    DatabaseReference foodList;
    FirebaseStorage storage1;
    StorageReference storageReference1;

    FloatingActionButton fabAddNewFood;

    ProgressBar progressBar;
    TextView tvNODATAFOUND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_owner_home);

        tvNODATAFOUND = findViewById(R.id.tvNOTFOUND);
        progressBar = findViewById(R.id.progressbar);
        strMessMob = Common.currentMessUser.getPhone();

        drawerLayout1 = findViewById(R.id.drawer_layout1);
        navigationView = findViewById(R.id.nav_view);/*
        toolbar = findViewById(R.id.toolbar);*/
        navigationView.bringToFront();

        ImageView img_ViewDrawerMess = findViewById(R.id.img_ViewDrawerMess);
        img_ViewDrawerMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout1.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout1.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout1.openDrawer(GravityCompat.START);
                }
            }
        });
        /*
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout1, toolbar, R.string.open, R.string.close);
        drawerLayout1.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setTitle("Mess Market");
*/
        //fabAddNewFood = findViewById(R.id.fabAddNewFood);

        navigationView.setCheckedItem(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        tvMessName = headerView.findViewById(R.id.MessName);
        tvMessName.setText(Common.currentMessUser.getName());
        tvRegNo = headerView.findViewById(R.id.tv_RegNO);
        tvRegNo.setText(Common.currentMessUser.getRegNo());

        if (getIntent() != null)
            strMob = getIntent().getStringExtra("mobno");
        Log.e(TAG, "inside onCreate : messId---" + strMob);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Food");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        database1 = FirebaseDatabase.getInstance();
        foodList = database1.getReference("Food");
        storage1 = FirebaseStorage.getInstance();
        storageReference1 = storage1.getReference();

        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        messName = Common.currentMessUser.getName();
        strMessMob = Common.currentMessUser.getPhone();
        Log.d(TAG, "onCreate: messName--- " + messName);
        //  loadMenu();
        //loadFoodList(strMessMob);

        ImageView addNewMenu = findViewById(R.id.addNewMenu);
        addNewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });


        EditText etFname = findViewById(R.id.etFoodName);
        Button delete = findViewById(R.id.btnDeleteFood);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = etFname.getText().toString();
                if (fName.isEmpty()) {
                    etFname.setError("please enter food name");
                } else {
                    foodList.child(strMessMob).child(fName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {

                                Toast.makeText(MessOwnerHome.this, "Food Not Found", Toast.LENGTH_LONG).show();

                            } else {
                                foodList.child(strMessMob).child(fName).removeValue();
                                Toast.makeText(MessOwnerHome.this, "Food Deleted!!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        category.child(strMessMob).orderByChild("menuId").equalTo(strMessMob).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    tvNODATAFOUND.setVisibility(View.GONE);
                    loadFoodList(strMessMob);
                    // Toast.makeText(FoodList.this, "data exists", Toast.LENGTH_SHORT).show();

                } else {

                    tvNODATAFOUND.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    // Toast.makeText(FoodList.this, "No data exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void loadFoodList(String categoryId) {
        adapter1 = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,
                FoodViewHolder.class, foodList.child(strMessMob).orderByChild("menuId").equalTo(categoryId)     // (select * from foods where menuId=categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food model, int i) {
                foodViewHolder.FoodName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(foodViewHolder.FoodImage);

                final Food local = model;
                foodViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MessOwnerHome.this, "" + local.getName(), Toast.LENGTH_LONG).show();
                        String foodName = local.getName();
                        String foodPrice = local.getPrice();
                        String foodDesc = local.getDescription();
                        String foodDiscont = local.getDiscount();
                        String MessPhone = local.getMenuId();
                        Log.e(TAG, "inside loadFoodList : fname : " + foodName);
                        Log.e(TAG, "inside loadFoodList: fprice : " + foodPrice);
                        Log.e(TAG, "inside loadFoodList : fdiscount : " + foodDiscont);
                        Log.e(TAG, "inside loadFoodList : fdesc : " + foodDesc);
                        Intent i = new Intent(MessOwnerHome.this, FoodDetail.class);
                        i.putExtra("FoodId", adapter1.getRef(position).getKey());
                        i.putExtra("MessPhone", MessPhone);
                        startActivity(i);
                    }
                });
            }
        };
        adapter1.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter1);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent i = new Intent(MessOwnerHome.this, MessOwnerHome.class);
                startActivity(i);
                break;
/*
            case R.id.nav_ViewMenu:
                Toast.makeText(MessOwnerHome.this, "View Menu", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MessOwnerHome.this, FoodList.class);
                intent.putExtra("strMessMob", strMessMob);
                startActivity(intent);

                //  showAddFoodDialog();
                break;

            case R.id.nav_deleteFood:
                Toast.makeText(MessOwnerHome.this, "delete Click", Toast.LENGTH_LONG).show();
                Intent i3 = new Intent(MessOwnerHome.this, DeleteFood.class);
                i3.putExtra("strMessMob", strMessMob);
                startActivity(i3);
                break;
*/

            case R.id.nav_orders:
                Toast.makeText(MessOwnerHome.this, "Order Click", Toast.LENGTH_LONG).show();
                Intent i4 = new Intent(MessOwnerHome.this, OrderStatus.class);
                i4.putExtra("messName", messName);
                startActivity(i4);

                break;

            case R.id.nav_logOut:
                Intent i2 = new Intent(MessOwnerHome.this, SignIn.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
                break;

        }

        drawerLayout1.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MessOwnerHome.this);
        alertDialog.setTitle("Add New Food");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_new_food_layout, null);

        etMenuName = add_food_layout.findViewById(R.id.et_MenuName);
        etDesc = add_food_layout.findViewById(R.id.et_MenuDesc);
        etPrice = add_food_layout.findViewById(R.id.et_price);
        btn_select = add_food_layout.findViewById(R.id.btn_select);
        btn_upload = add_food_layout.findViewById(R.id.btn_upload);

        btn_upload.setEnabled(false);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgFromGallery();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MessOwnerHome.this, " UPLOADING!!! ", Toast.LENGTH_LONG).show();
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
                    //foodList-----------table name
                    //   foodList.push().setValue(newFood);
                    addNewFood();
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
            progressDialog.setMessage("Uploading");
            progressDialog.show();

            String imgName = UUID.randomUUID().toString();
            Log.d(TAG, "uploadImage: imgName : " + imgName);
            if (imgName.isEmpty()) {
                Toast.makeText(MessOwnerHome.this, "imgName empty", Toast.LENGTH_LONG).show();
            }
            StorageReference imgFolder1 = storageReference1.child("food_images/" + imgName);
            Log.d(TAG, "uploadImage: imgFolder : " + imgFolder1);
            imgFolder1.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    btn_upload.setText("Uploaded");
                    btn_upload.setEnabled(false);
                    btn_select.setEnabled(false);
                    Toast.makeText(MessOwnerHome.this, "UPLOADED!!!", Toast.LENGTH_LONG).show();
                    imgFolder1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new category if image upload and we can get downloadd link
                            str_Foodname = etMenuName.getText().toString();
                            String strDesc = etDesc.getText().toString();
                            String strPrice = etPrice.getText().toString();
                            String str_uri = uri.toString();
                            String discount = " ";
                            String strMessName = tvMessName.getText().toString();
                            Log.d(TAG, "onSuccess:  str_name : " + str_Foodname);
                            Log.d(TAG, "onSuccess:  strDesc : " + strDesc);
                            Log.d(TAG, "onSuccess:  strPrice : " + strPrice);
                            Log.d(TAG, "onSuccess: str_uri : " + str_uri);
                            Log.d(TAG, "onSuccess: strMob/messid : " + strMessMob);//menu id
                            Log.d(TAG, "onSuccess: strMob/messid : " + strMessName);//mess name


                            if (str_Foodname.isEmpty() || strPrice.isEmpty() || str_uri.isEmpty() || strMessName.isEmpty()) {
                                Toast.makeText(MessOwnerHome.this, "please enter all details/select image", Toast.LENGTH_LONG).show();
                            } else {
                                newFood = new Food(str_Foodname, str_uri, strPrice, strDesc, discount, strMessMob, strMessName);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MessOwnerHome.this, "Faild!!!" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double prgrss = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading " + prgrss + "%");

                }
            });
        } else {
            Toast.makeText(MessOwnerHome.this, "please select image and upload", Toast.LENGTH_LONG).show();

        }
    }

    private void addNewFood() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = rootRef.child("Food").child(strMessMob).child(str_Foodname);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //create new food
                    foodList.child(strMessMob).child(str_Foodname).setValue(newFood);
                    Toast.makeText(MessOwnerHome.this, "New Food Added : " + newFood.getName(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MessOwnerHome.this, "Food is already exists!!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            btn_select.setText("Food Img Selected!!");
            btn_upload.setEnabled(true);

        } else {
            Toast.makeText(MessOwnerHome.this, "please select image and upload", Toast.LENGTH_LONG).show();
            btn_upload.setEnabled(false);
        }
    }


    //mess list
    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<MessUser, MenuViewHolder>(MessUser.class, R.layout.mess_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, MessUser model, int i) {
                menuViewHolder.MessName.setText(model.getName());
                menuViewHolder.tvOwmner.setText(model.getOwner());
                menuViewHolder.tvTime.setText(model.getTime());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(menuViewHolder.MenuImage);

                MessUser clickItem = model;
                menuViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MessOwnerHome.this, "" + clickItem.getName(), Toast.LENGTH_LONG).show();
                        //Get categoryId & sent it to new activity
                        strMob = adapter.getRef(position).getKey();
                        Intent i = new Intent(MessOwnerHome.this, FoodList.class);
                        i.putExtra("MessId", strMob);
                        startActivity(i);
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
    }


}