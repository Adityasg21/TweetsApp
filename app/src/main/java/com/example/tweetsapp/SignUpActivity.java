package com.example.tweetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_main);
        setTitle("SignUP");
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUP);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUp);
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSignUP:
                if (edtEmail.getText().toString().equals("")
                        || edtUsername.getText().toString().equals("")
                        || edtPassword.getText().toString().equals("")) {
                    FancyToast.makeText(SignUpActivity.this, "username,email,password required", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();

                } else {
                    final ParseUser AppUser = new ParseUser();
                    AppUser.setEmail(edtEmail.getText().toString());
                    AppUser.setUsername(edtUsername.getText().toString());
                    AppUser.setPassword(edtPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(edtUsername.getText().toString() + "is signing up");
                    progressDialog.show();

                    AppUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUpActivity.this, AppUser.get("username") + " is signed up", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                transitionToSocialMedia();
                            } else {
                                FancyToast.makeText(SignUpActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }

                            progressDialog.dismiss();

                        }
                    });
                }
                break;


            case R.id.btnLogin:

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void transitionToSocialMedia(){

        Intent intent=new Intent(SignUpActivity.this,TwitterUsers.class);
        startActivity(intent);
        finish();

    }

    public void rootLayoutIsTapped(View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}