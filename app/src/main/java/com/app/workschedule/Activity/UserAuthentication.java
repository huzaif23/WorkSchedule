package com.app.workschedule.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.app.workschedule.Model.ResponseFromServerLogin;
import com.app.workschedule.Retrofit.WebService;
import com.app.workschedule.Retrofit.WebServiceFactory;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.CustomProgressBar;
import com.app.workschedule.Utils.SharedPreferencHelperClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;

import com.app.workschedule.R;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAuthentication extends AppCompatActivity implements View.OnClickListener {

    MaterialButton btnSignUp;
    MaterialButton btnLogin;
    TextInputEditText editTextUserID;
    TextInputEditText editTextPassword;
    ConstraintLayout rootLayout;

    CustomProgressBar customProgressBar;

    private String[] permissions = {Manifest.permission.RECORD_AUDIO
            , Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.SEND_SMS
    };

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    boolean checkPermission = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);

        editTextUserID = findViewById(R.id.edit_text_user_id);
        editTextPassword = findViewById(R.id.edit_text_user_password);
        btnLogin = findViewById(R.id.btn_submit);
        btnSignUp = findViewById(R.id.btn_signup);
        rootLayout = findViewById(R.id.root_layout);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        for (String x : permissions)
            if (ActivityCompat.checkSelfPermission(this, x) != PackageManager.PERMISSION_GRANTED) {
                checkPermission = false;
            }

        if (!checkPermission) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        customProgressBar = CustomProgressBar.getInstance(UserAuthentication.this);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit:
                if (!editTextUserID.getText().toString().isEmpty())
                    if (!editTextPassword.getText().toString().isEmpty())
                        checkLogin();
                    else
                        editTextPassword.setError("Mandatory");
                else
                    editTextUserID.setError("Mandatory");
                break;
            case R.id.btn_signup:
                Intent intent = new Intent(UserAuthentication.this,Signup.class);
                intent.putExtra(Constants.SHOULD_LOGIN_AFTER_SIGNUP,true);
                startActivity(intent);
                break;
        }
    }

    private void checkLogin() {
        customProgressBar.addActivity(UserAuthentication.this);
        customProgressBar.showDialog();

        WebService webService = WebServiceFactory.getInstance();
        webService.getLoginDetails(editTextUserID.getText().toString(), editTextPassword.getText().toString())
                .enqueue(new Callback<ResponseFromServerLogin>() {
                    @Override
                    public void onResponse(Call<ResponseFromServerLogin> call, Response<ResponseFromServerLogin> response) {
                        customProgressBar.dismissDialog();
                        if(response.body().getStatus()) {
                            SharedPreferencHelperClass sharedPreferencHelperClass = SharedPreferencHelperClass.getInstance(UserAuthentication.this);
                            sharedPreferencHelperClass.setUserId(response.body().getId());
                            sharedPreferencHelperClass.setUserType(response.body().getType());
                            sharedPreferencHelperClass.setEmail(response.body().getEmail());
                            sharedPreferencHelperClass.setEmployerId(response.body().getEmployerId() == null ? "" : response.body().getEmployerId());
                            sharedPreferencHelperClass.setMobile(response.body().getMobile());
                            sharedPreferencHelperClass.setUserName(response.body().getUsername());
                            sharedPreferencHelperClass.setIsUserLoggedIn(true);
                            sharedPreferencHelperClass.setHelperId(response.body().getHelperId());
                            Intent intent = new Intent(UserAuthentication.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Snackbar.make(rootLayout,"User does not exist",Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseFromServerLogin> call, Throwable t) {
                        customProgressBar.dismissDialog();
                        Snackbar.make(rootLayout,"Error while connecting to server",Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
