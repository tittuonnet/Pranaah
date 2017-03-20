package com.merclain.hackthon.Screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.merclain.hackthon.R;

/**
 * Created by ajith on 20/3/17.
 */

public class Lab extends Fragment {
    public static Lab getInstance(){
        Lab fragment = new Lab();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.labdetailstab, container, false);
    }
}