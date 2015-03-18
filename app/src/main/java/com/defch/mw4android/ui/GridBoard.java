package com.defch.mw4android.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.defch.mw4android.model.Square;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 3/17/15.
 */
public class GridBoard implements Parcelable {
    private ArrayList<Square> board;
    private int mWidth;
    private int mHeight;
    private int minesNo;
    private int discoverNo;
    private int flagsNo;

    public GridBoard(int width, int height, int nbrMines){
        mWidth = width;
        mHeight = height;
        minesNo = nbrMines;
        discoverNo = 0;
        flagsNo = 0;
        board = new ArrayList<>();

        int countMine = 0;

        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++) {
                board.add(new Square(j, i, false));
            }
        }

        while (countMine< minesNo) {
            int rand = (int)(Math.random()*(mWidth * mHeight));

            Square cell = getCell(rand);
            if(!cell.isMine()){
                cell.setMine();
                countMine++;
            }
        }

    }

    public void displayGrid(){
        for (int i = 0; i < board.size(); i++) {
            if(getCell(i).isMine()){
                System.out.print("X");
            }
            else System.out.print("-");
            if((i+1)% mWidth == 0){
                System.out.print("\n");
            }
        }
    }


    public Square getCell(int pos){
        if(pos >= 0 && pos < mWidth * mHeight)
            return board.get(pos);
        else return new Square(0, 0, false);
    }

    public Square getCell(int x, int y){
        if(x >= 0 && y >= 0 && x < mWidth && y < mHeight) {
            return board.get((y * mWidth) + x);
        }
        else return new Square(0, 0, false);
    }


    public int getCountMineArround(Square cell){
        int nbBomb = 0;
        int xTest = cell.getX();
        int yTest = cell.getY();

        if(getCell(xTest + 1, yTest).isMine()) { nbBomb ++; }
        if(getCell(xTest + 1, yTest + 1).isMine()) { nbBomb ++; }
        if(getCell(xTest, yTest + 1).isMine()) { nbBomb ++; }
        if(getCell(xTest - 1, yTest + 1).isMine()) { nbBomb ++; }
        if(getCell(xTest - 1, yTest - 1).isMine()) { nbBomb ++; }
        if(getCell(xTest + 1, yTest - 1).isMine()) { nbBomb ++; }
        if(getCell(xTest, yTest - 1).isMine()) { nbBomb ++; }
        if(getCell(xTest - 1, yTest).isMine()) { nbBomb ++; }

        return nbBomb;
    }

    public int getWidth(){
        return mWidth;
    }

    public int getHeight(){
        return mHeight;
    }

    public void discoverCell(Square cell){
        cell.setDiscover();
        discoverNo++;
    }

    public void setFlagCell(Square cell, boolean flag){
        cell.setFlag(flag);
        if(flag)
            flagsNo++;
        else
            flagsNo--;
    }

    public int getNbrFlags(){
        return flagsNo;
    }


    public int getNbrDiscover(){
        return discoverNo;
    }

    public int getNbrMines() {
        return minesNo;
    }


    //PARCELABLE

    public GridBoard(Parcel in ) {
        readFromParcel( in );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GridBoard createFromParcel(Parcel in ) {
            return new GridBoard(in);
        }

        public GridBoard[] newArray(int size) {
            return new GridBoard[size];
        }
    };

    public void readFromParcel(Parcel in){
        mWidth = in.readInt();
        mHeight = in.readInt();
        minesNo = in.readInt();
        discoverNo = in.readInt();
        flagsNo = in.readInt();


        Square[] _tab = (Square[]) in.readParcelableArray(Square.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelableArray(board.toArray(new Square[board.size()]), flags);

        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
        dest.writeInt(minesNo);
        dest.writeInt(discoverNo);
        dest.writeInt(flagsNo);

    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
}
