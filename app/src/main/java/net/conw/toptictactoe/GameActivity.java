package net.conw.toptictactoe;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameFragment = (GameFragment) getFragmentManager().findFragmentById(R.id.fragment_game);

        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        if(restore) {
            String gameData = getPreferences(MODE_PRIVATE).getString(PREF_RESTORE, null);
            if(gameData != null) {
                mGameFragment.putState(gameData);
            }
        }
        Log.d("T4", "restore = " + restore);
    }

    @Override
    protected void onPause() {
        super.onPause();

        String gameData = mGameFragment.getState();
        getPreferences(MODE_APPEND).edit().putString(PREF_RESTORE, gameData).commit();
        Log.d("T4", "state = " + gameData);
    }

    public void restartGame() {
        mGameFragment.restartGame();
    }

    public void reportWinner(final Tile.Owner winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.declare_winner, winner));
        //builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
        mGameFragment.initGame();
    }

}
