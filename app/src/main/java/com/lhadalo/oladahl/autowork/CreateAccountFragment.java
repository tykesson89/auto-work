package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CreateAccountFragment extends Fragment {
    OnFragmentInteraction callback;

    interface OnFragmentInteraction{

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        return view;
    }

    private void initComponents(View view){

    }

    private void initListeners(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnFragmentInteraction)context;
    }
}
