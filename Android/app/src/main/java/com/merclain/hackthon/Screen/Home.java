package com.merclain.hackthon.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.merclain.hackthon.R;
import com.merclain.hackthon.treatmentHistory;

/**
 * Created by ajith on 21/3/17.
 */

public class Home extends Fragment {
    private ImageView next;
    public static Home getInstance(){
        Home fragment = new Home();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hometab, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        next = (ImageView) view.findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),treatmentHistory.class));
            }
        });

    }

}
