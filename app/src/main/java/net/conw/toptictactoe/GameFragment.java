package net.conw.toptictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.HashSet;
import java.util.Set;


public class GameFragment extends Fragment {

    private int[] mLargeIds = {R.id.large1, R.id.large2, R.id.large3, R.id.large4, R.id.large5,
            R.id.large6, R.id.large7, R.id.large8, R.id.large9};

    private int[] mSmallIds = {R.id.small1, R.id.small2, R.id.small3, R.id.small4, R.id.small5,
            R.id.small6, R.id.small7, R.id.small8, R.id.small9};


    private Tile mEntireBoard = new Tile(this);
    private Tile mLargeTiles[] = new Tile[9];
    private Tile mSmallTiles[][] = new Tile[9][9];
    private Tile.Owner mPlayer = Tile.Owner.X;
    private Set<Tile> mAvailable = new HashSet<Tile>();
    private int mLastLarge;
    private int mLastSmall;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initGame();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.large_board, container, false);
        initViews(rootView);
        updateAllTiles();

        return rootView;
    }

    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);
        for(int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            for(int small = 0; small < 9; small++) {
                ImageButton inner = (ImageButton) outer.findViewById(mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final Tile smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isAvailable(smallTile)) {
                            makeMove(fLarge, fSmall);
                            //switchTurns();
                            think();
                        }
                    }
                });
            }
        }
    }

    ///在第large个棋盘的第small个空位下棋
    public void makeMove(int large, int small) {
        mLastLarge = large;
        mLastSmall = small;

        Tile smallTile = mSmallTiles[large][small];
        Tile largeTile = mLargeTiles[large];
        smallTile.setOwner(mPlayer);
        setAvailableFromLastMove(small);
        Tile.Owner oldWinner = largeTile.getOwner();
        Tile.Owner winner = largeTile.findWinner();
        if(winner != oldWinner) {
            largeTile.setOwner(winner);
        }
        winner = mEntireBoard.findWinner();
        mEntireBoard.setOwner(winner);
        updateAllTiles();
        if(winner != Tile.Owner.NEITHER) {
            ((GameActivity)getActivity()).reportWinner(winner);
        }
    }


    private void switchTurns() {
        mPlayer = (mPlayer == Tile.Owner.X) ? Tile.Owner.O : Tile.Owner.X;
    }

    private void updateAllTiles() {
        mEntireBoard.updateDrawableState();
        for(int large = 0; large < 9; large++) {
            mLargeTiles[large].updateDrawableState();
            for(int small = 0; small < 9; small++)
                mSmallTiles[large][small].updateDrawableState();
        }
    }

    public void initGame() {
        mEntireBoard = new Tile(this);

        for(int large = 0; large < 9; large++) {
            mLargeTiles[large] = new Tile(this);
            for(int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new Tile(this);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        mLastSmall = -1;
        mLastLarge = -1;
        setAvailableFromLastMove(mLastSmall);
    }

    public String getState() {
        return null;
    }

    public void restartGame() {
        initGame();
        initViews(getView());
        updateAllTiles();
    }

    private void clearAvailable() {
        mAvailable.clear();
    }

    private void addAvailable(Tile tile) {
        mAvailable.add(tile);
    }

    public boolean isAvailable(Tile tile) {
        return mAvailable.contains(tile);
    }

    private void setAvailableFromLastMove(int small) {
        clearAvailable();
        if(small != -1) {
            for(int dest = 0; dest < 9; dest++) {
                Tile tile = mSmallTiles[small][dest];
                if(tile.getOwner() == Tile.Owner.NEITHER) {
                    addAvailable(tile);
                }
            }
        }
        if(mAvailable.isEmpty()) {
            setAllAvailable();
        }
    }

    private void setAllAvailable() {
        for(int large = 0; large < 9; large++) {
            for(int small = 0; small < 9; small++) {
                Tile tile = mSmallTiles[large][small];
                if(tile.getOwner() == Tile.Owner.NEITHER) {
                    addAvailable(tile);
                }
            }
        }
    }

    public void putState(String gameData) {
        String[] fields = gameData.split(".");
        int index = 0;
        mLastLarge = Integer.parseInt(fields[index++]);
        mLastSmall = Integer.parseInt(fields[index++]);
        for(int large = 0; large < 9; large++) {
            for(int small = 0; small < 9; small++) {
                Tile.Owner owner = Tile.Owner.valueOf(fields[index++]);
                mSmallTiles[large][small].setOwner(owner);
            }
        }
        setAvailableFromLastMove(mLastSmall);
        updateAllTiles();
    }

    private Handler mHandler = new Handler();

    private void think() {
        //((GameActivity)getActivity()).startThinking();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getActivity() == null) return ;
                if(mEntireBoard.getOwner() == Tile.Owner.NEITHER) {
                    int move[] = new int[2];
                    pickMove(move);
                    if(move[0] != -1 && move[1] != -1) {
                        switchTurns();
                        makeMove(move[0], move[1]);
                        switchTurns();
                    }
                }
                //((GameActivity)getActivity()).stopThinking();
            }
        }, 1000);
    }

    private void pickMove(int move[]) {
        Tile.Owner opponent = (mPlayer == Tile.Owner.X) ? Tile.Owner.O : Tile.Owner.X;
        int bestLarge = -1;
        int bestSmall = -1;
        int bestValue = Integer.MAX_VALUE;
        for(int large = 0; large < 9; large++) {
            for(int small = 0; small < 9; small++) {
                Tile smallTile = mSmallTiles[large][small];
                if(isAvailable(smallTile)) {
                    Tile newBoard = mEntireBoard.deepCopy();
                    newBoard.getSubTiles()[large].getSubTiles()[small].setOwner(opponent);
                    int value = newBoard.evaluate();
                    if(value < bestValue) {
                        bestLarge = large;
                        bestSmall = small;
                        bestValue = value;
                    }
                }
            }
        }
        move[0] = bestLarge;
        move[1] = bestSmall;
    }

}
