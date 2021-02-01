package com.example.tyminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Category;
import com.example.tyminiproject.Model.Food;
import com.example.tyminiproject.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView, userName;
    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setTitle("Menu");
        //  setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Home.this, "FAB   Click", Toast.LENGTH_LONG).show();
            }
        });


        navigationView.setCheckedItem(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.userName);
        userName.setText(Common.currentUser.getName());

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();

    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category model, int i) {
                menuViewHolder.MenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(menuViewHolder.MenuImage);

                Category clickItem = model;
                menuViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Home.this, "" + clickItem.getName(), Toast.LENGTH_LONG).show();
                        //Get categoryId & sent it to new activity
                        Intent i = new Intent(Home.this, FoodList.class);
                        i.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(i);
                    }
                });

            }


        };

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_menu:
                Toast.makeText(Home.this, "home   Click", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Home.this, Home.class);
                startActivity(intent);
                break;
            case R.id.nav_cart:
                Toast.makeText(Home.this, "bus Favorite Click", Toast.LENGTH_LONG).show();


/*
                Intent i = new Intent(Home.this, Customer.class);
                startActivity(i);
*/

                break;
          /*  case R.id.nav_login:
                menu.findItem(R.id.nav_logout).setVisible(true);
                menu.findItem(R.id.nav_profile).setVisible(true);
                menu.findItem(R.id.nav_login).setVisible(false);
                break;
            case R.id.nav_logout:
                menu.findItem(R.id.nav_logout).setVisible(false);
                menu.findItem(R.id.nav_profile).setVisible(false);
                menu.findItem(R.id.nav_login).setVisible(true);
                break;
    */
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
