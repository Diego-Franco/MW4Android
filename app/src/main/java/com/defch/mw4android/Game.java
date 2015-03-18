package com.defch.mw4android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.defch.mw4android.db.ScoreDB;
import com.defch.mw4android.model.Score;
import com.defch.mw4android.model.Square;
import com.defch.mw4android.ui.GridBoard;

/**
 * Created by DiegoFranco on 3/16/15.
 */
public class Game extends Activity implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "GAME";

    private ScoreDB scoreDB;

    EditText editText;
    GridBoard gridBoard;

    int rows, columns, numOfMines, numSquares;

    private boolean gameover;
    private boolean winner;
    private int mineActive;

    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeS = 0L;
    long updatedTime = 0L;
    String timerValueString;
    String user;

    TextView timer, counterMine;
    Button btnImage;
    MW4AndroidApplication application;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game);
        scoreDB = new ScoreDB(this);
        application = (MW4AndroidApplication) getApplication();

        int diff = getIntent().getIntExtra(Environment.KEY_DIFFICULTY, Environment.EASY);

        if(savedInstanceState != null) {
            gridBoard = savedInstanceState.getParcelable(Environment.GRIDBOARD);

            columns = gridBoard.getWidth();
            rows = gridBoard.getHeight();
            numSquares = columns*rows;
            numOfMines = gridBoard.getNbrMines();

            gameover = savedInstanceState.getByte(Environment.GAMEOVER)  != 0;
            winner = savedInstanceState.getByte(Environment.WINNER)  != 0;
            mineActive = savedInstanceState.getInt(Environment.MINE_ACTIVE);
            timeS = savedInstanceState.getLong(Environment.TIME);


        } else {
            switch (diff) {
                case 0:
                    rows = Environment.EASY_ROWS;
                    columns = Environment.EASY_COLUMNS;
                    numOfMines = Environment.EASY_MINE;
                    break;
                case 1:
                    rows = Environment.MEDIUM_ROWS;
                    columns = Environment.MEDIUM_COLUMNS;
                    numOfMines = Environment.MEDIUM_MINE;
                    break;
                case 2:
                    rows = Environment.HARD_ROWS;
                    columns = Environment.HARD_COLUMNS;
                    numOfMines = Environment.HARD_MINE;
                    break;
            }
            numSquares = columns*rows;
            gridBoard = new GridBoard(columns, rows, numOfMines);
        }

        timer = (TextView) findViewById(R.id.timer);
        counterMine = (TextView) findViewById(R.id.counter_mine);
        btnImage = (Button) findViewById(R.id.smile);
        btnImage.setOnClickListener(clickSmile);

        counterMine.setText(Integer.toString(gridBoard.getNbrMines()-gridBoard.getNbrFlags()));
        createGrid();
    }

    private void createGrid() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.gridContainer);
        for(int i = 0; i < rows; i++) {
            LinearLayout lRows = new LinearLayout(this);
            lRows.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 15;
            params.gravity = Gravity.CENTER;
            lRows.setLayoutParams(params);

            LinearLayout.LayoutParams paramsSq = new LinearLayout.LayoutParams(80,80);
            for(int j = 0; j < columns; j++) {
                ImageView s = new ImageView(this);
                int id = j + (i * columns);
                s.setLayoutParams(paramsSq);

                Square currentSq = gridBoard.getCell(id);
                if(currentSq.isDiscovered()) {
                    int minesAround = currentSq.getNbrOfMinesArround();
                    Log.i(TAG, "number: " + minesAround);
                    s.setImageResource(this.getResources().getIdentifier("num" + minesAround, "drawable", getPackageName()));
                } else if(currentSq.isFlag()) {
                    if (gameover) {
                        if (currentSq.isMine()) {
                            s.setImageResource(R.mipmap.ic_flag);
                        } else {
                            s.setImageResource(R.mipmap.ic_bomb_cancel);
                        }
                    } else {
                        s.setImageResource(R.mipmap.ic_flag);
                    }
                }else {
                    s.setImageResource(R.mipmap.ic_sq);
                }

                    s.setId(id);
                    s.setScaleType(ImageView.ScaleType.FIT_XY);
                    s.setPadding(0,0,0,0);
                    s.setOnClickListener(this);
                    s.setOnLongClickListener(this);
                    lRows.addView(s);
                }
                layout.addView(lRows);
            }
        if(gameover) {
            ((ImageView)findViewById(mineActive)).setImageResource(R.mipmap.ic_explosion);
            discoverMines();
            View content = findViewById(android.R.id.content);
            showDialog(content);

        }

    }

    View.OnClickListener clickSmile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetGame();
        }
    };

    @Override
    protected void onStart(){
        super.onStart();

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void resetGame(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(!gameover) {
            clickSquare(v);
        }
    }

    private void clickSquare(View v) {
        int id = v.getId();
        ImageView imgBtn = (ImageView)findViewById(id);

        Square currentSquare = gridBoard.getCell(id);
        if(currentSquare.isDiscovered() || currentSquare.isFlag())
            return;

        if(currentSquare.isMine()) {
            imgBtn.setImageResource(R.mipmap.ic_explosion);
            gridBoard.discoverCell(currentSquare);
            discoverMines();
            gameover = true;
            mineActive = id;
            showDialog(v);
            timeS += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
        }
        else {
            int mineArround = gridBoard.getCountMineArround(currentSquare);
            gridBoard.discoverCell(currentSquare);
            currentSquare.setminesArroundNumber(mineArround);
            imgBtn.setImageResource(getResources().getIdentifier("num" + Integer.toString(mineArround), "drawable", getPackageName()));

            if(mineArround < 1){
                int currentRow = id/columns;
                int currentCol = id/rows;

                boolean right = false;
                boolean left = false;
                boolean top = false;
                boolean bottom = false;

                if((id+1)/rows==currentRow && (id+1)<numSquares) {
                    (findViewById(id+1)).performClick();
                    right = true;
                }
                if((id-1)/rows==currentRow && (id-1)>=0) {
                    (findViewById(id-1)).performClick();
                    left = true;
                }
                if(id-columns >= 0) {
                    (findViewById(id-columns)).performClick();
                    top = true;
                }
                if(id+columns < numSquares) {
                    (findViewById(id+columns)).performClick();
                    bottom = true;
                }

                if(right) {
                    if(top)
                        (findViewById(id-columns+1)).performClick();
                    if(bottom)
                        (findViewById(id+columns+1)).performClick();
                }

                if(left) {
                    if(top)
                        (findViewById(id-columns-1)).performClick();
                    if(bottom)
                        (findViewById(id+columns-1)).performClick();
                }
            }

            if(gridBoard.getNbrDiscover()==numSquares-numOfMines && Integer.parseInt(counterMine.getText().toString())==0) {
                winner = true;
                gameover = true;
                showDialog(v);
            }
        }
    }

    public void discoverMines() {
        for (int i=0; i<numSquares; i++) {
            if((gridBoard.getCell(i)).isMine() && !(gridBoard.getCell(i)).isFlag()  && !(gridBoard.getCell(i)).isDiscovered())
                ((ImageView)findViewById(i)).setImageResource(R.mipmap.ic_bomb);
            if(gridBoard.getCell(i).isFlag() && !(gridBoard.getCell(i)).isMine())
                ((ImageView)findViewById(i)).setImageResource(R.mipmap.ic_bomb_cancel);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();

        Square currentSquare = gridBoard.getCell(id);
        if(!currentSquare.isDiscovered() && !currentSquare.isFlag())  {
            ImageView imgBtn = (ImageView)findViewById(id);
            gridBoard.setFlagCell(currentSquare, true);
            imgBtn.setImageResource(R.mipmap.ic_flag);
        }
        else if(!currentSquare.isDiscovered() && currentSquare.isFlag())  {
            ImageView imgBtn = (ImageView)findViewById(id);
            gridBoard.setFlagCell(currentSquare, false);
            imgBtn.setImageResource(R.mipmap.ic_sq);
        }

        counterMine.setText(Integer.toString(gridBoard.getNbrMines() - gridBoard.getNbrFlags()));

        if(gridBoard.getNbrDiscover()==numSquares-numOfMines && Integer.parseInt(counterMine.getText().toString())==0)  {
            winner = true;
            gameover = true;
            showDialog(v);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(Environment.GRIDBOARD, gridBoard);

        savedInstanceState.putByte(Environment.GAMEOVER,(byte) (gameover ? 1 : 0));
        savedInstanceState.putByte(Environment.WINNER,(byte) (winner ? 1 : 0));
        savedInstanceState.putInt(Environment.MINE_ACTIVE, mineActive);

        if(gameover) {
            user = editText.getText().toString();
        }

        savedInstanceState.putString(Environment.NAME, user);

        timeS += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        savedInstanceState.putLong(Environment.TIME, timeS);

        super.onSaveInstanceState(savedInstanceState);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeS + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timer.setText("" + mins + ":"+ String.format("%02d", secs));
            timerValueString = "" + mins + ":"+ String.format("%02d", secs);
            customHandler.postDelayed(this, 0);
        }
    };

    public void showDialog(View v) {
        final Score score = new Score();
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.edit_score, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if(winner) {
            alert.setTitle(getString(R.string.winner));
        }
        else {
            alert.setTitle(getString(R.string.loser));
        }

        alert.setMessage(R.string.high_board_msg);
        alert.setView(textEntryView);

        editText = (EditText) textEntryView.findViewById(R.id.nameuser);

        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                user = editText.getText().toString();
                score.setTime(timerValueString);
                score.setUsername(user);
                if(winner) {
                    score.setWinner(getString(R.string.winner));
                } else {
                    score.setWinner(getString(R.string.loser));
                }
                saveScore(score);
                dialog.dismiss();

            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void saveScore(Score score) {
        scoreDB.open();
        scoreDB.insertScore(score);
        scoreDB.close();
    }

    private void alertExit() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(R.string.exit);
        dlg.setMessage(R.string.exit_msg);
        dlg.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dlg.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Game.this.finish();
            }
        });
        dlg.show();
    }

    @Override
    public void onBackPressed() {
        alertExit();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateTimerThread = null;
    }
}
