package net.conw.toptictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ControlFragment extends Fragment implements View.OnClickListener {
    private AlertDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_control, container, false);

        rootView.findViewById(R.id.button_main).setOnClickListener(this);
        rootView.findViewById(R.id.button_restart).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mDialog != null)
            mDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_main) {
            getActivity().finish();
        } else {
            ((GameActivity)getActivity()).restartGame();
        }
    }
}
