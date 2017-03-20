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

public class History extends Fragment {
    public static History getInstance(){
        History fragment = new History();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.treatmenttab, container, false);
    }
}
