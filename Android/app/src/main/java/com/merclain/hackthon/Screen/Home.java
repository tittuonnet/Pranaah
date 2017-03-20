package com.merclain.hackthon.Screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.merclain.hackthon.R;

/**
 * Created by ajith on 21/3/17.
 */

public class Home extends Fragment {
    public static Home getInstance(){
        Home fragment = new Home();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hometab, container, false);
    }
}
