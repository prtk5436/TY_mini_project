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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Category;
import com.example.tyminiproject.Model.MessUser;
import com.example.tyminiproject.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    //view
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<MessUser, MenuViewHolder> adapter;
    String strMob;
    Button btn_ViewMess;

    //add new menu layout
    EditText et_name;
    Button btn_upload, btn_select;

    Category newCategory;
    Uri saveUri;
    private final int PICK_IMG_REQ = 71;

    public static MessUser currentMessUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_owner_home);

        drawerLayout1 = findViewById(R.id.drawer_layout1);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout1, toolbar, R.string.open, R.string.close);
        drawerLayout1.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setTitle("Mess Market");

        //  setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fabMess);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessOwnerHome.this, "FAB MESS  Click", Toast.LENGTH_LONG).show();
                showDialog();
            }
        });


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
        category = database.getReference("MessUser");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();

    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MessOwnerHome.this);
        alertDialog.setTitle("Add new category");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout, null);

        et_name = add_menu_layout.findViewById(R.id.et_name);
        btn_select = add_menu_layout.findViewById(R.id.btn_select);
        btn_upload = add_menu_layout.findViewById(R.id.btn_upload);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgFromGallery();
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MessOwnerHome.this, "UPLOADING!!!", Toast.LENGTH_LONG).show();
                uploadImage();
            }
        });
        alertDialog.setView(add_menu_layout);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //here , just created new category
                if (newCategory != null) {
                    category.push().setValue(newCategory);
                    Toast.makeText(MessOwnerHome.this, "Published : " + newCategory.getName(), Toast.LENGTH_LONG).show();
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

    private void uploadImage() {

        if (saveUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("uploading");
            progressDialog.show();

            String imgName = UUID.randomUUID().toString();
            Log.d(TAG, "uploadImage: imgName : " + imgName);
            StorageReference imgFolder = storageReference.child("images/" + imgName);
            Log.d(TAG, "uploadImage: imgFolder : " + imgFolder);
            imgFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(MessOwnerHome.this, "UPLOADED!!!", Toast.LENGTH_LONG).show();
                    imgFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new category if image upload and we can get downloadd link
                            String str_name = et_name.getText().toString();
                            String str_uri = uri.toString();
                            Log.d(TAG, "onSuccess:  str_name : " + str_name);
                            Log.d(TAG, "onSuccess: str_uri : " + str_uri);
                            newCategory = new Category(str_name, str_uri);
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
            btn_select.setText("Img Selected!!");

        }
    }

    private void getImgFromGallery() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Image"), PICK_IMG_REQ);
    }

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            case R.id.nav_addMenu:
                Toast.makeText(MessOwnerHome.this, "View Menu", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MessOwnerHome.this, FoodList.class);
                intent.putExtra("MessId", strMob);
                startActivity(intent);
                break;

            case R.id.nav_orders:
                Toast.makeText(MessOwnerHome.this, "Order Click", Toast.LENGTH_LONG).show();
               /* Intent i = new Intent(MessOwnerHome.this, Cart.class);
                startActivity(i);
              */
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
}