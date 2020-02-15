package com.app.workschedule.Utils;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.app.workschedule.R;

public class FragmentTransactionHelperClass {


    private FragmentTransaction fragmentTransaction;
    private static FragmentTransactionHelperClass instance;

    private FragmentTransactionHelperClass() {

    }

    public static synchronized FragmentTransactionHelperClass getInstance() {
        if (instance == null)
            instance = new FragmentTransactionHelperClass();
        return instance;
    }

    public void changeFragment(Fragment fragment, Context context, String TAG, boolean shouldAddToBackStack) {

        fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment, TAG);
        if (shouldAddToBackStack)
            fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

    public void changeFragment(Fragment fragment, int layoutId, Context context, String TAG, boolean shouldAddToBackStack) {

        fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(layoutId, fragment, TAG);
        if (shouldAddToBackStack)
            fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }


}
