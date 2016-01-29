package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.l3info.utf.mm.MC.MCApplication;
import com.l3info.utf.mm.MC.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


//VERY IMPORTANT THINK IF YOU MAKE YOUR STYLE "APPTHEMEFUTUR" YOU MUST EXTENDS FROM ACTIVITY NO APPCOMYACTIVITY :):):)!
public class SignUpActivity extends AppCompatActivity {

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//Add before function setContentView and super
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        getApplicationContext().getTheme().applyStyle(R.style.Theme_Mc, true);

            /* Remove the Tooblar  :)*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setProgressBarIndeterminateVisibility(true);



        mUsername = (EditText)findViewById(R.id.usernameField);
        mPassword = (EditText)findViewById(R.id.passwordField);
        mEmail = (EditText)findViewById(R.id.emailField);
        mSignUpbtn = (Button) findViewById(R.id.buttonSignUp);
    }

    public void SignUp(View v){

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();

        username = username.trim();
        password = password.trim();
        email = email.trim();
        if(username.isEmpty() || password.isEmpty() || email.isEmpty()){

            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setMessage(R.string.signup_error_message)
                    .setTitle(R.string.signup_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{

            setProgressBarIndeterminateVisibility(true);
            ParseUser newUSer = new ParseUser();
            newUSer.setUsername(username);
            newUSer.setPassword(password);
            newUSer.setEmail(email);


            newUSer.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    setProgressBarIndeterminateVisibility(false);
                    if(e == null){
                        MCApplication.updateParseInstallation(ParseUser.getCurrentUser());
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage(e.getMessage());
                        builder.setTitle(R.string.signup_error_title);
                        builder.setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();


                    }
                }
            });

        }

    }
}
