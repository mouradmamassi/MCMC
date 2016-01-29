package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.l3info.utf.mm.MC.MCApplication;
import com.l3info.utf.mm.MC.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    protected TextView mSignUpTextView;

    protected EditText mUsername;
    protected EditText mPassword;

    protected Button mLoginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//Add before function setContentView and Super :)
        super.onCreate(savedInstanceState);

        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_login);

//        ActionBar mactionbar = getSupportActionBar();
//        mactionbar.hide();
        mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        /* Remove the Tooblar  :)*/
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        mUsername = (EditText)findViewById(R.id.UsernameFiled);
        mPassword = (EditText)findViewById(R.id.passwordField);
        mLoginbtn = (Button) findViewById(R.id.login_btn);
    }

    public void Login(View v){

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();


        username = username.trim();
        password = password.trim();

        if(username.isEmpty() || password.isEmpty() ){

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage(R.string.login_error_message)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{

            setProgressBarIndeterminateVisibility(true);

            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    setProgressBarIndeterminateVisibility(false);
                    if (user != null) {

                        MCApplication.updateParseInstallation(user);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(e.getMessage())
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            });

        }

    }

}
