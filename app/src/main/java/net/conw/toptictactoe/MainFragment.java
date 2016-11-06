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


public class MainFragment extends Fragment implements View.OnClickListener {
    private AlertDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        rootView.findViewById(R.id.continue_button).setOnClickListener(this);
        rootView.findViewById(R.id.new_button).setOnClickListener(this);
        rootView.findViewById(R.id.about_button).setOnClickListener(this);

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
        if(v.getId() == R.id.continue_button) {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            intent.putExtra(GameActivity.KEY_RESTORE, true);
            getActivity().startActivity(intent);
        } else if(v.getId() == R.id.new_button) {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            getActivity().startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.about_title);
            builder.setMessage(R.string.about_text);
            //builder.setCancelable(false);
            builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //...
                }
            });
            mDialog = builder.show();
        }
    }
}
