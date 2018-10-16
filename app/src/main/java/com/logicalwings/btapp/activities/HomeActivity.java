package com.logicalwings.btapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.fragments.FavouritesFragment;
import com.logicalwings.btapp.fragments.HomeFragment;
import com.logicalwings.btapp.fragments.OrdersFragment;
import com.logicalwings.btapp.fragments.ProfileFragment;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView toolSearch, toolCart, toolSave;
    LinearLayout drawerLinear;
    protected LibFile libFile;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    FrameLayout frameLayout;
    TextView textCartCounter, textHeaderName, textHeaderEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.home_activity);
        }
        toolCart = findViewById(R.id.toolbar_cart);
        toolSave = findViewById(R.id.toolbar_save);
        toolSearch = findViewById(R.id.toolbar_search);
        frameLayout = findViewById(R.id.frame_layout_cart);
        textCartCounter = findViewById(R.id.text_cart_counter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        drawerLinear = header.findViewById(R.id.drawer_linear);
        textHeaderEmail = header.findViewById(R.id.text_header_email);
        textHeaderName = header.findViewById(R.id.text_header_name);
        navigationView.getMenu().getItem(0).setChecked(true);

        setCartCounter();

        initData();

        drawerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProfileFragment()).commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        toolSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SearchProductActivity.class);
                startActivity(i);
            }
        });

        toolCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new CartFragment()).commit();
                Intent i = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            android.support.v4.app.Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (currentFragment instanceof HomeFragment) {
                showExitDialog();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProfileFragment()).commit();
        } else if (id == R.id.nav_contact) {
            Intent i = new Intent(HomeActivity.this, ContactUsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "BTAPP");
            String sAux = "\nLet me recommend you this application\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose One"));
        } else if (id == R.id.nav_favourites) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FavouritesFragment()).commit();
        } else if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();

        } else if (id == R.id.nav_orders) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new OrdersFragment()).commit();
        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            confirmLogoutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initData() {
        libFile = LibFile.getInstance(getApplication());
        toolSearch.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        toolSave.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }

        String headerName = libFile.getString(AppConstants.SAP_CUSTOMER_NAME);
        String headerEmail = libFile.getString(AppConstants.EMAIl);

        Log.e("nameEmail", headerEmail + "/" + headerName);
        if (!headerName.equals("")) {
            textHeaderName.setText(headerName);
        }

        if (!headerEmail.equals("")) {
            textHeaderEmail.setText(headerEmail);
        }
    }

    public void confirmLogoutDialog() {
        final Database base = new Database(HomeActivity.this);
        base.open();
        AlertDialog alertDialog = new AlertDialog.Builder(
                HomeActivity.this).create();
        alertDialog.setIcon(R.drawable.ic_logout);

        alertDialog.setTitle("Logout !");

        alertDialog.setMessage("Are you sure?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                libFile.setBoolean(AppConstants.IS_LOGIN, false);
                String mobile = libFile.getString(AppConstants.MOBILE_NO);
                String email = libFile.getString(AppConstants.EMAIl);
                if (!mobile.equals("")) {
                    base.deleteCart(mobile);
                    base.close();
                } else {
                    base.deleteCart(email);
                    base.close();
                }
                Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                ActivityCompat.finishAffinity(HomeActivity.this);
                startActivity(intentLogin);
                dialogInterface.dismiss();

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }

    public void showExitDialog() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finish();
            return;
        } else {
            Toast.makeText(getBaseContext(), "press again to close app", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    private void setCartCounter() {
        int cartCounter = AppUtils.getCartCounter(HomeActivity.this);
        Log.e("cartListSize", String.valueOf(cartCounter));

        if (cartCounter != 0) {
            textCartCounter.setText(String.valueOf(cartCounter));
        } else {
            textCartCounter.setVisibility(View.GONE);
        }
    }

}
