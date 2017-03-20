package com.merclain.hackthon.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.merclain.hackthon.MainActivity;
import com.merclain.hackthon.R;
import com.merclain.hackthon.Utilities.HttpRequest;
import com.merclain.hackthon.Utilities.PranaPreferences;

import org.json.JSONObject;

public class Login extends Activity implements View.OnClickListener{

    private Button loginButton,signUpButton;
    private EditText ETbhamashahID,ETpassword;
    private String bhamashahid,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        loginButton = (Button) findViewById(R.id.loginBtnComit);
        signUpButton = (Button) findViewById(R.id.signUpBtnComit);
        ETbhamashahID = (EditText) findViewById(R.id.ETSigninbhamashahID);
        ETpassword = (EditText) findViewById(R.id.ETSigninPass);
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(v);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Signup.class));
            }
        });
    }

    public void Login(View v) {
        bhamashahid = ETbhamashahID.getText().toString();
        password = ETpassword.getText().toString();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
                SendLogin s = new SendLogin();
                s.execute();
        } else {
            Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Login(v);
                }
            }).show();
        }
    }

    public class SendLogin extends AsyncTask<String, String, String> {

        private String err = "";
        private String response = "";
        ProgressDialog progress;

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String myUrlString = getResources().getString(R.string.base_url) + "login.php";
            try {
                HttpRequest request = HttpRequest.post(myUrlString).acceptJson();
                request.header("Content-Type","application/json");
                request.part("action",("login"));
                request.part("Bhamashah_ID",(bhamashahid));
                request.part("Password" , (password));
                if (request.ok()) {
                    response = request.body();
                }
            } catch (Exception e) {
                err = e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();

            try {
                JSONObject obj = new JSONObject(response);
                if (Boolean.parseBoolean(obj.getString("status")) == true) {
                    SharedPreferences sharedPreferences = Login.this.getSharedPreferences(PranaPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PranaPreferences.LOGGEDIN_SHARED_PREF, "true");
                    editor.putString(PranaPreferences.Prana_Name_BhaId,bhamashahid);
                    JSONObject data = obj.getJSONObject("data");
                    editor.putString(PranaPreferences.Prana_Name_English, data.getString("NAME_ENG"));
                    editor.putString(PranaPreferences.Prana_Name_Hindi, data.getString("NAME_HND"));
                    startActivity(new Intent(Login.this, MainActivity.class));
                    editor.commit();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
                    alertDialog.setMessage(obj.getString("message"));
                    alertDialog.setTitle("Signin Failed");
                    alertDialog.setNegativeButton("Sign Up", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Login.this,Signup.class));
                            finish();
                        }
                    });
                    alertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ETpassword.setText("");
                            ETpassword.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(ETpassword, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }).show();
                }
            } catch (Exception e) {
                Toast.makeText(Login.this, e.getMessage()+"An error occured", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Login.this);
            progress.setMessage("Signing In.");
            progress.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(PranaPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String loggedIn = sharedPreferences.getString(PranaPreferences.LOGGEDIN_SHARED_PREF, "");
        if(loggedIn.equals("true")){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {


    }
}
