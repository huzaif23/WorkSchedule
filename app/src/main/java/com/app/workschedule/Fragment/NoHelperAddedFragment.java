package com.app.workschedule.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.workschedule.Activity.Signup;
import com.app.workschedule.R;
import com.app.workschedule.Utils.Constants;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoHelperAddedFragment extends Fragment implements View.OnClickListener {


    MaterialButton btnAddHelper;

    public NoHelperAddedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_helper_added, container, false);

        btnAddHelper = view.findViewById(R.id.btn_add_helper);

        btnAddHelper.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_add_helper) {
            Intent intent = new Intent(getContext(), Signup.class);
            intent.putExtra(Constants.SHOULD_LOGIN_AFTER_SIGNUP,false);
            startActivity(intent);
        }
    }
}
