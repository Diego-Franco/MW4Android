package com.defch.mw4android;

/**
 * Created by DiegoFranco on 3/16/15.
 */
public class Environment {

    public static final String PREFS_NAME = "MW4ANDROID";
    public static final String SCORE = "scores";
    public static final String NAME = "username";
    public static final String TIME = "time";

    public static final String KEY_DIFFICULTY = "com.defch.mw4android.difficult";
    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;

    public static final int EASY_ROWS = 8;
    public static final int EASY_COLUMNS = 8;
    public static final int EASY_MINE = 10;

    public static final int MEDIUM_ROWS = 13;
    public static final int MEDIUM_COLUMNS = 13;
    public static final int MEDIUM_MINE = 30;

    public static final int HARD_ROWS = 17;
    public static final int HARD_COLUMNS = 13;
    public static final int HARD_MINE = 40;

    public static final String GRIDBOARD = "gridboard";
    public static final String GAMEOVER = "gameover";
    public static final String WINNER = "winner";
    public static final String MINE_ACTIVE = "mineActive";

    //Database
    public static final String TABLE_SCORES = "table_scores";
    public static final String _ID = "_id";
    public static final String COL_NAME = "Name";
    public static final String COL_TIME = "Time";
    public static final String COL_WIN = "Win";

}
