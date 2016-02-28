package com.monstarz.golive;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });

        setupNavDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void setupNavDrawer() {
        AccountHeader header = new AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.color.primary_dark)
            .addProfiles(
                new ProfileDrawerItem()
                    .withName("Phil Oliver")
                    .withEmail("sortofrican90@gmail.com")
                    .withIcon(getResources().getDrawable(android.R.drawable.sym_def_app_icon))
            )
            .build();

        drawer = new DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withAccountHeader(header)
            .addDrawerItems(
                new ExpandableDrawerItem()
                    .withName("People")
                    .withIcon(GoogleMaterial.Icon.gmd_people)
                    .withSubItems(
                        new SecondaryDrawerItem()
                            .withName("Friends")
                            .withIcon(android.R.color.transparent),
                        new SecondaryDrawerItem()
                            .withName("Seeking Match")
                            .withIcon(android.R.color.transparent)
                    )
                    .withIsExpanded(true),
                new ExpandableDrawerItem()
                    .withName("Matches")
                    .withIcon(GoogleMaterial.Icon.gmd_grid_on)
                    .withSubItems(
                        new SecondaryDrawerItem()
                            .withName("My Turn")
                            .withIcon(android.R.color.transparent)
                            .withBadge("3"),
                        new SecondaryDrawerItem()
                            .withName("Public")
                            .withIcon(android.R.color.transparent),
                        new SecondaryDrawerItem()
                            .withName("Find a Match")
                            .withIcon(android.R.color.transparent),
                        new SecondaryDrawerItem()
                            .withName("Create a Match")
                            .withIcon(android.R.color.transparent)
                    )
                    .withIsExpanded(true)
            )
            .addStickyDrawerItems(
                new PrimaryDrawerItem()
                    .withName("Settings")
                    .withIcon(GoogleMaterial.Icon.gmd_settings),
                new PrimaryDrawerItem()
                    .withName("Sign off")
                    .withIcon(GoogleMaterial.Icon.gmd_lock)
            )
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    return false;
                }
            })
            .build();
    }
}
