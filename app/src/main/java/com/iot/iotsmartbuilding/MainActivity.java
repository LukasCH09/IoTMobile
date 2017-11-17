package com.iot.iotsmartbuilding;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.iot.iotsmartbuilding.R.id.fragment_container;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        WriteFragment.OnFragmentInteractionListener,
        ReadFragment.OnFragmentInteractionListener{

    WriteFragment writeFragment;
        ReadFragment readFragment;
        public static android.app.FragmentManager fragmentManager;
        String TAG = "testIoT";
        public static final int WRITE_DRAWER = 1;
        public static final int READ_DRAWER = 2;
        public static String storeID="1";
        public static String radiatorID="1";
        public static String dimmerID="4";
        public static String sensorID="3";
        public static String floorID="4";

        Spinner spinner;
        public static List exempleList;
        public static int spinnerPosition=0;
        public static boolean selection=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(fragment_container)!= null){
            if(savedInstanceState != null){
                return;
            }
            Log.i(TAG, "onCreate: findViewById");

            writeFragment = WriteFragment.newInstance("TEST1", "TEST2");

            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, writeFragment);
            fragmentTransaction.commit();


            if(writeFragment == null){
                Log.i(TAG, "onCreate: mapFragment == null");
            } else{
                Log.i(TAG, "onCreate: mapFragment != null");
            }
            readFragment = new ReadFragment();

            //Initialisation de la valeur pour le fragment write
            writeFragment.aValue=50;

        }

        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        exempleList = new ArrayList();
        exempleList.add("Room 1");
        exempleList.add("Room 2");

        //-----------------------------------------------------------------------------------
        // Toolbar / drawer / floating action button
        //-----------------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_write) {
            onFragmentInteraction(1,WRITE_DRAWER);
        } else if (id == R.id.nav_read) {
            onFragmentInteraction(2,READ_DRAWER);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(int position, int fragmentCaller ) {

        Fragment fragmentToCall = null;

        switch (fragmentCaller){
            case READ_DRAWER:
                fragmentToCall = readFragment;
                Log.i("WriteFragment", "MainActivity:  set selection to false ");
                selection=false;
                break;
            case WRITE_DRAWER:
                fragmentToCall = writeFragment;
                Log.i("WriteFragment", "MainActivity:  set selection to false ");
                selection=false;
                break;
        }

        Log.i(TAG, "onCreate: listFragment==null");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this listFragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(fragment_container, fragmentToCall);
        transaction.addToBackStack(null);
        Log.i(TAG, "onCreate: addToBackStack");
        // Commit the transaction
        transaction.commit();
        Log.i(TAG, "onCreate: commit");
        Log.i(TAG, "onCreate: listFragment!=null");
    }
}
