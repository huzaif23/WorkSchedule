package com.app.workschedule.Activity;

import android.os.Bundle;

import com.app.workschedule.Fragment.AddTaskFragment;
import com.app.workschedule.Fragment.DashboardFragment;
import com.app.workschedule.Fragment.NoHelperAddedFragment;
import com.app.workschedule.Interface.ToggleToolbar;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.FragmentTransactionHelperClass;
import com.app.workschedule.Utils.SharedPreferencHelperClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.View;

import com.app.workschedule.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        launchDefaultFragment();

    }

    private void launchDefaultFragment() {
        toolbar.setVisibility(View.GONE);
        Fragment fragment;
        if (SharedPreferencHelperClass.getInstance(this).getHelperId().isEmpty()
                &&
                SharedPreferencHelperClass.getInstance(this).getUserType().equals(Constants.USER_TYPE_EMPLOYER)
        )
            fragment = new NoHelperAddedFragment();
        else
            fragment = new DashboardFragment();

        FragmentTransactionHelperClass.getInstance().changeFragment(fragment, this, Constants.DASHBOARD_FRAGMENT, false);
    }

    public void toggleToolbar(boolean status, String title) {
        if (status) {
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle(title);
        } else
            toolbar.setVisibility(View.GONE);
    }

}
