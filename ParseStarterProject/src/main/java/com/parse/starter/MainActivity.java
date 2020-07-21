/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{


  boolean signUpModeActive = true;
  EditText usernameEditText;
  EditText passwordEditText;
  TextView loginTextView;

  public void showUserList(){
    Intent intent = new Intent(getApplicationContext(), UserList.class);
    startActivity(intent);
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
      signupClicked(view);
    }
    return false;
  }

  @Override
  public void onClick(View view) {
    if(view.getId() == R.id.LoginTextView){
      Button signUpButton = (Button) findViewById(R.id.SignupButton);
      if(signUpModeActive){
        signUpModeActive = false;
        signUpButton.setText("Log In");
        loginTextView.setText("Sign Up");
      } else {
        signUpModeActive = true;
        signUpButton.setText("Sign Up");
        loginTextView.setText("Log In");
      }
    } else if(view.getId() == R.id.instaText || view.getId()==R.id.background_layout){
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
    }
  }

  public void signupClicked(View view){
    if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
      Toast.makeText(this, "A username or password is required", Toast.LENGTH_SHORT).show();
    } else {
      if(signUpModeActive) {
        ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("SignUp", "Successs");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      } else {
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if(user!=null){
              Log.i("Logged in", "Success");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }

    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instagram");

    loginTextView = (TextView) findViewById(R.id.LoginTextView);
    loginTextView.setOnClickListener(this);
    usernameEditText = (EditText) findViewById(R.id.editTextUseraname);
    passwordEditText = (EditText) findViewById(R.id.editTextPassword);
    ImageView imageView = (ImageView) findViewById(R.id.instaText);
    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.background_layout);
    passwordEditText.setOnKeyListener(this);
    relativeLayout.setOnClickListener(this);
    imageView.setOnClickListener(this);

    /*if(ParseUser.getCurrentUser()!=null){
      showUserList();
    }*/

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}