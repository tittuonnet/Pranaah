package com.merclain.hackthon.Screen;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.merclain.hackthon.Adaptor.HomeAdaptor;
import com.merclain.hackthon.Model.PranaahList;
import com.merclain.hackthon.R;
import com.merclain.hackthon.Utilities.HttpRequest;
import com.merclain.hackthon.Utilities.PranaPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ajith on 20/3/17.
 */

public class Health extends Fragment {
    private List<PranaahList> favEventList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private CircleImageView UserProfileImage;
    private String bhamashahid,englishname,hindiname;
    private TextView userDOB,userAdharNo,FMCOunt,UserName;
    JSONArray familymembers = null;

    public static Health getInstance(){
        Health fragment = new Health();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userhealthtab, container, false);
        UserProfileImage = (CircleImageView) view.findViewById(R.id.user_profilescreen_photo);
        userDOB = (TextView) view.findViewById(R.id.TVUserDOB);
        userAdharNo = (TextView) view.findViewById(R.id.TVUserAdhar);
        FMCOunt = (TextView) view.findViewById(R.id.familyCount);
        UserName  = (TextView) view.findViewById(R.id.TVUserName);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        favEventList = new ArrayList<>();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(isNetworkStatusAvialable (getContext())) {
            userProfileDetails s = new userProfileDetails();
            s.execute();
        } else {
//            offlineSync();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isNetworkStatusAvialable (getContext())) {
            userProfileDetails s = new userProfileDetails();
            s.execute();
        } else {
            Snackbar.make(getView(), "Ooops No Network Connection", Snackbar.LENGTH_LONG).show();

        }

    }

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }

    public class userProfileDetails extends AsyncTask<String, String, String> {

        private String err = "";
        private String response = "";
        private String token = "";

        @Override
        protected String doInBackground(String... params) {
            String myUrlString = getResources().getString(R.string.base_url) + "profile.php";
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PranaPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            bhamashahid = sharedPreferences.getString(PranaPreferences.Prana_Name_BhaId,"");
            try {
                HttpRequest request = HttpRequest.post(myUrlString).acceptJson();
                request.header("Content-Type","application/json");
                request.part("action",("profile"));
                request.part("Bhamashah_ID",(bhamashahid));

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
            favEventList.clear();
            try {
                JSONObject obj = new JSONObject(response);
                if (Boolean.parseBoolean(obj.getString("status")) == true) {
                    FMCOunt.setText(obj.getString("family_count"));
                    JSONObject data = obj.getJSONObject("data");
                    JSONObject hof = data.getJSONObject("HOF");
                    userDOB.setText("DOB : "+ hof.getString("DOB"));
                    userAdharNo.setText("AADHAR ID : "+ hof.getString("AADHAR_ID"));
                    byte[] decodedString = Base64.decode(obj.getString("profile_photo"),Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    UserProfileImage.setImageBitmap(decodedByte);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PranaPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    englishname = sharedPreferences.getString(PranaPreferences.Prana_Name_English,"");
                    hindiname = sharedPreferences.getString(PranaPreferences.Prana_Name_Hindi,"");
                    UserName.setText(hindiname+"/"+englishname);
                    familymembers = data.optJSONArray("Family_Members");
                    for (int i = 0; i < familymembers.length(); i++) {
                        JSONObject json = familymembers.getJSONObject(i);
                        PranaahList superHero = new PranaahList();
                        superHero.setName(json.getString("NAME_ENG"));
                        superHero.setGender(json.getString("GENDER"));
                        superHero.setDob(json.getString("DOB"));
                        superHero.setUid(json.getString("uid"));
                        favEventList.add(superHero);
                    }
                } else {

                }
            } catch (Exception e) {

            }
            adapter = new HomeAdaptor(favEventList, getContext());
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
