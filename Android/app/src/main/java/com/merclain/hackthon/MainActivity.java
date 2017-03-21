package com.merclain.hackthon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.merclain.hackthon.DetailView.MedicalData;
import com.merclain.hackthon.Login.Login;
import com.merclain.hackthon.NavItem.BloodDonation;
import com.merclain.hackthon.NavItem.CrowdSourcing;
import com.merclain.hackthon.NavItem.DoctorsList;
import com.merclain.hackthon.NavItem.EmergencyNumbers;
import com.merclain.hackthon.NavItem.HospitalsList;
import com.merclain.hackthon.NavItem.Insurance;
import com.merclain.hackthon.NavItem.OnlineMedicalStores;
import com.merclain.hackthon.Screen.Health;
import com.merclain.hackthon.Screen.History;
import com.merclain.hackthon.Screen.Home;
import com.merclain.hackthon.Screen.Lab;
import com.merclain.hackthon.Utilities.PranaPreferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView TVusername;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,appoinment.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        SharedPreferences sharedPreferences = getSharedPreferences(PranaPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        TVusername = (TextView) header.findViewById(R.id.welcomeUser);
        TVusername.setText("Welcome "+sharedPreferences.getString(PranaPreferences.Prana_Name_English,"")+" !");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();

    }

//    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home(), "Medic");
        adapter.addFragment(new Health(), "Health");
        adapter.addFragment(new Lab(), "Lab");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
//            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_medicalStores) {
            startActivity(new Intent(this, OnlineMedicalStores.class));
        }else if (id == R.id.nav_appointment) {
            startActivity(new Intent(this, appoinment.class));
        }else if (id == R.id.nav_medicalId) {
            startActivity(new Intent(this, MedicalData.class));
        } else if (id == R.id.nav_payment) {
            startActivity(new Intent(this, Insurance.class));
        }else if (id == R.id.blood_donation) {
            startActivity(new Intent(this, BloodDonation.class));
        }else if (id == R.id.treatment_status) {

        }else if (id == R.id.emergancy_numbers) {
            startActivity(new Intent(this, EmergencyNumbers.class));
        }else if (id == R.id.nav_crowdsourcing) {
            startActivity(new Intent(this, CrowdSourcing.class));
        }else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = this.getSharedPreferences(PranaPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PranaPreferences.Prana_Name_BhaId, "");
            editor.putString(PranaPreferences.LOGGEDIN_SHARED_PREF, "false");
            editor.commit();
            startActivity(new Intent(this, Login.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
