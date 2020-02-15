package com.app.workschedule.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.app.workschedule.Model.ResponseFromServerLogin;
import com.app.workschedule.Model.ResponseFromServerSignup;
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

import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.app.workschedule.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText editTextUserName;
    TextInputEditText editTextUserEmail;
    TextInputEditText editTextUserMobile;
    TextInputEditText editTextUserPassword;
    MaterialButton btnSignUp;
    ConstraintLayout rootLayout;
    RadioGroup radioGroup;

    CustomProgressBar customProgressBar;
    boolean shouldLogin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUserName = findViewById(R.id.edit_text_user_id);
        editTextUserEmail = findViewById(R.id.edit_text_user_email);
        editTextUserMobile = findViewById(R.id.edit_text_user_mobile);
        editTextUserPassword = findViewById(R.id.edit_text_user_password);
        btnSignUp = findViewById(R.id.btn_signup);
        rootLayout = findViewById(R.id.root_layout);
        radioGroup = findViewById(R.id.radio_group_user_type);

        btnSignUp.setOnClickListener(this);

        customProgressBar = CustomProgressBar.getInstance(Signup.this);

        if (getIntent() != null)
            shouldLogin = getIntent().getExtras().getBoolean(Constants.SHOULD_LOGIN_AFTER_SIGNUP);

        if(!shouldLogin) {
            radioGroup.check(R.id.radio_helper);
            findViewById(R.id.radio_employer).setEnabled(false);
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_signup:
                if (!editTextUserName.getText().toString().isEmpty()) {
                    if (!editTextUserEmail.getText().toString().isEmpty()) {
                        if (!editTextUserMobile.getText().toString().isEmpty()) {
                            if (!editTextUserPassword.getText().toString().isEmpty()) {
                                if (radioGroup.getCheckedRadioButtonId() != -1)
                                    checkSignUp();
                                else
                                    Snackbar.make(rootLayout, "Please select user type", Snackbar.LENGTH_SHORT).show();
                            } else {
                                editTextUserPassword.setError("Mandatory");
                            }
                        } else
                            editTextUserMobile.setError("Mandatory");
                    } else
                        editTextUserEmail.setError("Mandatory");

                } else
                    editTextUserName.setError("Mandatory");
        }
    }


    private void checkSignUp() {

        customProgressBar.addActivity(Signup.this);
        customProgressBar.showDialog();

        String userName = editTextUserName.getText().toString();
        String userEmail = editTextUserEmail.getText().toString();
        String userMobile = editTextUserMobile.getText().toString();
        String userPassword = editTextUserPassword.getText().toString();
        String type = radioGroup.getCheckedRadioButtonId() == R.id.radio_employer ? "1" : "2";
        String employerId = SharedPreferencHelperClass.getInstance(this).getUserId();
        WebService webService = WebServiceFactory.getInstance();
        webService.signUpUser(userName, userEmail, type, userPassword, userMobile,employerId)
                .enqueue(new Callback<ResponseFromServerSignup>() {
                    @Override
                    public void onResponse(Call<ResponseFromServerSignup> call, Response<ResponseFromServerSignup> response) {
                        customProgressBar.dismissDialog();

                        if (response.body().isStatus()) {
                            if(response.body().getHelperId()!= null && !response.body().getHelperId().isEmpty())
                                SharedPreferencHelperClass.getInstance(Signup.this).setHelperId(response.body().getHelperId());
                            if (shouldLogin)
                                checkLogin();
                            else {
                                Intent intent = new Intent(Signup.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Snackbar.make(rootLayout, "User already existed", Snackbar.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseFromServerSignup> call, Throwable t) {
                        customProgressBar.dismissDialog();
                        Snackbar.make(rootLayout, "Error while registering user", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkLogin() {

        customProgressBar.addActivity(Signup.this);
        customProgressBar.showDialog();

        WebService webService = WebServiceFactory.getInstance();
        webService.getLoginDetails(editTextUserName.getText().toString(), editTextUserPassword.getText().toString())
                .enqueue(new Callback<ResponseFromServerLogin>() {
                    @Override
                    public void onResponse(Call<ResponseFromServerLogin> call, Response<ResponseFromServerLogin> response) {
                        customProgressBar.dismissDialog();
                        if (response.body().getStatus()) {
                            SharedPreferencHelperClass sharedPreferencHelperClass = SharedPreferencHelperClass.getInstance(Signup.this);
                            sharedPreferencHelperClass.setUserId(response.body().getId());
                            sharedPreferencHelperClass.setUserType(response.body().getType());
                            sharedPreferencHelperClass.setEmail(response.body().getEmail());
                            sharedPreferencHelperClass.setEmployerId(response.body().getEmployerId() == null ? "" : response.body().getEmployerId());
                            sharedPreferencHelperClass.setMobile(response.body().getMobile());
                            sharedPreferencHelperClass.setUserName(response.body().getUsername());
                            sharedPreferencHelperClass.setIsUserLoggedIn(true);
                            Intent intent = new Intent(Signup.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(rootLayout, "User does not exist", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseFromServerLogin> call, Throwable t) {
                        customProgressBar.dismissDialog();
                        Snackbar.make(rootLayout, "Error while connecting to server", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
