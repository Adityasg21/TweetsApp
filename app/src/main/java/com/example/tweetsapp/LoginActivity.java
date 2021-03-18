package com.example.tweetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginEmail,edtLoginPassword;
    Button btnLoginLoginActivity,btnSignUpLoginActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("LogIn");
        edtLoginEmail=findViewById(R.id.edtEmailLoginAcitvity);
        edtLoginPassword=findViewById(R.id.edtPasswordLoginActivity);
        btnLoginLoginActivity=findViewById(R.id.btnLoginLoginAcitvity);
        btnSignUpLoginActivity=findViewById(R.id.btnSignUpLoginActivity);


        btnSignUpLoginActivity.setOnClickListener(this);
        btnLoginLoginActivity.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            ParseUser.getCurrentUser().logOut();
        }

        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnLoginLoginActivity);
                }

                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.btnLoginLoginAcitvity:
                if (edtLoginPassword.getText().toString().equals("")
                        || edtLoginEmail.getText().toString().equals("")
                ) {
                    FancyToast.makeText(LoginActivity.this, "email,password required", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                }
                else{
                    ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user!=null&&e==null){
                                FancyToast.makeText(LoginActivity.this,user.get("username")+ " is logged in",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                transitionToSocialMedia();
                            }
                        }
                    });


                    break;
                }

            case R.id.btnSignUpLoginActivity:
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void rootLayoutIsTapped(View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMedia(){

        Intent intent=new Intent(LoginActivity.this,TwitterUsers.class);
        startActivity(intent);
        finish();

    }
}