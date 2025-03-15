package com.example.hperkicksadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OPEN,R.string.CLOSE);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();



        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new dashboard_frag()).commit();

        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new dashboard_frag()).commit();
                break;
            case R.id.add_slider:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new add_slider()).commit();
                break;
            case R.id.add_brands:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new add_brands()).commit();
                break;
            case R.id.add_products:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new add_product()).commit();
                break;
            case R.id.view_orders:
                startActivity(new Intent(MainActivity.this,orderview.class));
                finish();
            case R.id.nav_logout:
                Toast.makeText(this, "LOGINIG OUT..", Toast.LENGTH_SHORT).show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return  true;
    }


}