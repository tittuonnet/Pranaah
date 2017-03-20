package com.merclain.hackthon.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private EditText ETbhamashaid,ETpassword;
    private String bhamashahid,password;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        ETbhamashaid = (EditText) findViewById(R.id.ETbhamashaId);
        signUpButton = (Button) findViewById(R.id.signUpBtnComit);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupCheck(v);
            }
        });
    }

    public void SignupCheck(View v) {
        bhamashahid = ETbhamashaid.getText().toString();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            CheckId s = new CheckId();
            s.execute();
        } else {
            Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignupCheck(v);
                }
            }).show();
        }
    }

    public class CheckId extends AsyncTask<String, String, String> {

        private String err = "";
        private String response = "";
        ProgressDialog progress;

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String myUrlString = getResources().getString(R.string.base_url) + "signup.php";
            try {
                HttpRequest request = HttpRequest.post(myUrlString).acceptJson();
                request.header("Content-Type","application/json");
                request.part("action",("signup"));
                request.part("Bhamashah_ID" , (bhamashahid));
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
                    JSONObject data = obj.getJSONObject("data");
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);
                    String message = data.getString("name");
                    alertDialog.setMessage("Hi "+message+" Set Password For Pranaah");
                    alertDialog.setTitle("Bhamashah ID Found");
                    final EditText input = new EditText(Signup.this);
                    alertDialog.setView(input);
                    alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            password = input.getEditableText().toString();
                            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo info = cm.getActiveNetworkInfo();
                            if (info != null && info.isConnected()) {
                                passwordSet s = new passwordSet();
                                s.execute();
                            } else {
                               Toast.makeText(Signup.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                            }

                        }
                    }).show();

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);
                    alertDialog.setMessage(obj.getString("message"));
                    if(obj.getString("message").equals("Account already registered")){
                        alertDialog.setTitle("Account Already Exists");
                        alertDialog.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Signup.this,Login.class));
                                finish();
                            }
                        }).show();
                    }else if (obj.getString("message").equals("Invalid Bhamashah ID")){
                        alertDialog.setTitle("Invalid ID");
                        alertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ETbhamashaid.setText("");
                                ETbhamashaid.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(ETbhamashaid, InputMethodManager.SHOW_IMPLICIT);
                            }
                        }).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(Signup.this, e.getMessage()+"An error occured", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Signup.this);
            progress.setMessage("Signing In.");
            progress.show();
        }
    }

    @Override
    public void onClick(View view) {

    }


    public class passwordSet extends AsyncTask<String, String, String> {

        private String err = "";
        private String response = "";
        ProgressDialog progress;

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String myUrlString = getResources().getString(R.string.base_url) + "set_password.php";
            try {
                HttpRequest request = HttpRequest.post(myUrlString).acceptJson();
                request.header("Content-Type","application/json");
                request.part("action",("set_password"));
                request.part("Bhamashah_ID" , (bhamashahid));
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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);
                    alertDialog.setMessage(obj.getString("message"));
                    alertDialog.setPositiveButton("Login Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Signup.this,Login.class));
                        }
                    }).show();

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);
                    alertDialog.setMessage(obj.getString("message"));
                    alertDialog.show();
                }
            } catch (Exception e) {
                Toast.makeText(Signup.this, e.getMessage()+"An error occured", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Signup.this);
            progress.setMessage("");
            progress.show();
        }
    }

}
