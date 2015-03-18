package com.defch.mw4android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DiegoFranco on 3/16/15.
 */
public class Square implements Parcelable {
    private int _x;
    private int _y;

    private boolean isMine;
    private boolean isFlag;
    private boolean isDiscovered;
    private int minesArroundNumber;

    public Square(int x, int y, boolean mine){
        _x = x;
        _y = y;
        isMine = mine;
        isDiscovered = false;
        minesArroundNumber = 0;
    }

    public boolean isMine(){
        return isMine;
    }

    public boolean isFlag(){
        return isFlag;
    }

    public void setFlag(boolean flag){
        isFlag = flag;
    }

    public void setMine(){
        isMine = true;
    }

    public void setDiscover(){
        isDiscovered = true;
    }

    public boolean isDiscovered(){
        return isDiscovered;
    }

    public void setminesArroundNumber(int i){
        minesArroundNumber = i;
    }

    public int getNbrOfMinesArround(){
        return minesArroundNumber;
    }

    public int getX(){
        return _x;
    }

    public int getY(){
        return _y;
    }


    // PARCELABLE

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(_x);
        dest.writeInt(_y);
        dest.writeInt(minesArroundNumber);

        dest.writeByte((byte) (isMine ? 1 : 0));
        dest.writeByte((byte) (isFlag ? 1 : 0));
        dest.writeByte((byte) (isDiscovered ? 1 : 0));
    }

    public static final Parcelable.Creator<Square> CREATOR = new Parcelable.Creator<Square>()
    {
        @Override
        public Square createFromParcel(Parcel source)
        {
            return new Square(source);
        }

        @Override
        public Square[] newArray(int size)
        {
            return new Square[size];
        }
    };

    public Square(Parcel in) {

        this._x = in.readInt();
        this._y = in.readInt();
        this.minesArroundNumber = in.readInt();


        this.isMine = in.readByte() != 0;
        this.isFlag = in.readByte() != 0;
        this.isDiscovered = in.readByte() != 0;
    }
}
