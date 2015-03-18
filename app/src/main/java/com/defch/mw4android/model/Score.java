package com.defch.mw4android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DiegoFranco on 3/17/15.
 */
public class Score implements Parcelable{

    private String username;
    private String time;
    private String winner;

    public Score(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    protected Score(Parcel in) {
        username = in.readString();
        time = in.readString();
        winner = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(time);
        dest.writeString(winner);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Score> CREATOR = new Parcelable.Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
}
