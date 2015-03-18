package com.defch.mw4android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.defch.mw4android.db.ScoreDB;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private ScoreDB scoreDB;
    private Cursor cursor;
    private Button btnNGame, btnHScore, btnRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        scoreDB = new ScoreDB(this);
        scoreDB.open();

        cursor = scoreDB.getAllScores();

        btnNGame = (Button)findViewById(R.id.new_game);
        btnHScore = (Button)findViewById(R.id.high_score);
        btnRules = (Button)findViewById(R.id.rules);
        btnNGame.setOnClickListener(this);
        btnHScore.setOnClickListener(this);
        btnRules.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_game:
                createNewGame();
                break;
            case R.id.high_score:
                if(cursor != null) {
                    showHighScores();
                }
                break;
            case R.id.rules:
                showRules();
                break;
        }
    }

    private void createNewGame() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.mode_game)
                .setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                startNewGame(i);
                            }
                        }).show();
    }

    private void startNewGame(int level) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(Environment.KEY_DIFFICULTY, level);
        startActivity(intent);
    }

    private void showRules() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.rules)
                .setMessage(R.string.game_rules)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showHighScores() {
        String[] dbCol = new String[]{
                Environment.COL_NAME,
                Environment.COL_TIME,
                Environment.COL_WIN
        };
        int[] idObjs = new int[]{
                R.id.item_name,
                R.id.item_time,
                R.id.item_img,

        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_scores, null);
        dialogBuilder.setView(dialogView);
        ListView list = (ListView)dialogView.findViewById(R.id.list_score);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.score_item, cursor, dbCol, idObjs, 0);
        list.setAdapter(adapter);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle(R.string.scores);
        alertDialog.show();

    }

}
