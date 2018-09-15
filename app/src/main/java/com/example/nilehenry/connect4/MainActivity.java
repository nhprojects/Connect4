package com.example.nilehenry.connect4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.lang.Thread;

public class MainActivity extends AppCompatActivity {
    Board gameBoard;
    HashMap<Integer,HashMap<Integer,Integer>> find; //finds index in gridlayout of desired piece;
    GridLayout layoutPieces;
    boolean gameIsComplete;

    public void buttonPressed(View view){
        switch(view.getId()){
            case R.id.b1:
                gameBoard.fillSpot(0);
                break;
            case R.id.b2:
                gameBoard.fillSpot(1);
                break;
            case R.id.b3:
                gameBoard.fillSpot(2);
                break;
            case R.id.b4:
                gameBoard.fillSpot(3);
                break;
            case R.id.b5:
                gameBoard.fillSpot(4);
                break;
            case R.id.b6:
                gameBoard.fillSpot(5);
                break;
            case R.id.b7:
                gameBoard.fillSpot(6);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }

    public class Board{

        private String[][] board; //stores where pieces are located  [row][column]
        private int turn=0; //red=1, yellow=1
        private HashMap<Integer,Integer> positions; //stores next available row in column


        public Board(){ //constructor
            board= new String[6][7]; //rows,columns
            positions= new HashMap<Integer,Integer>();
            turn=1; //red starts
            /*for (int i=0; i<board.length;i=i+1){
                positions.put(i,5);
                for (int j=0; j<board[0].length;j=j+1){
                    board[i][j]="empty";
                }
            }*/

            for (int j=0; j<board[0].length;j=j+1){
                positions.put(j,5);
                for (int i=0; i<board.length;i=i+1){
                    board[i][j]="empty";

                }
            }
        }

        public void fillSpot(int column){  //places string in indicated spot of double array
            if (gameIsComplete==false) {
                int row = positions.get(column);
                if (row == -1) { //row complete
                    Toast.makeText(MainActivity.this, "No more spots in this column", Toast.LENGTH_SHORT);
                } else {
                    if (turn == 0) {
                        showPiece("yellow", row, column);
                        fillSpotYellow(row, column);
                        gameIsComplete=gameIsComplete("yellow",row,column);

                        turn = 1; //change turns
                    } else {
                        showPiece("red", row, column);
                        fillSpotRed(row, column);
                        gameIsComplete=gameIsComplete("red",row,column);
                        turn = 0; //change turns
                    }
                    row = row - 1;
                    positions.put(column, row);
                }
            }
            else{
                Toast.makeText(MainActivity.this,"Can't fill spot, game is complete",Toast.LENGTH_SHORT).show();
            }

        }
        public void fillSpotRed(int row, int column){
            board[row][column]="red";
        }
        public void fillSpotYellow(int row, int column){
            board[row][column]="yellow";
        }



        public void showPiece(String color, int row, int column){
            int colorDrawable;
            if (color.equals("red")){
                colorDrawable=R.drawable.redpiece;
                //gameIsComplete(color,row,column);
            }
            else{
                colorDrawable=R.drawable.yellowpiece;
                //gameIsComplete(color,row,column);

            }
            int index= find.get(row).get(column);

            ImageView currentPiece=  (ImageView) layoutPieces.getChildAt(index);

            currentPiece.setImageResource(colorDrawable);
            currentPiece.animate().translationYBy(1500f).setDuration(400);
        }
        public boolean gameIsComplete(String color, int row, int column){
            if (gameIsWon(color,row,column)){
                Toast.makeText(MainActivity.this,color + " wins",Toast.LENGTH_SHORT).show();
                return true;
            }
            for (int i=0; i<7;i=i+1){
                if ((positions.get(i)==-1)==false){
                    return false;
                }
            }
            Toast.makeText(MainActivity.this,"Tie game", Toast.LENGTH_LONG);
            //newGame();
            return true;
        }
        public boolean gameIsWon(String color,int row, int column){
            if ((checkVertical(color,row,column))|| (checkHorizontal(color,row,column))|| (checkDiagonal(color,row,column))){
                return true;
            }
            return false;
        }

        public boolean checkVertical(String color, int row, int column){
            return (1+checkVerticalUp(color,row,column)+checkVerticalDown(color,row,column)==4);
        }
        public int checkVerticalUp(String color, int row, int column){
            int count=0;
            try{
                for (int i=1; i<6;i=i+1){
                    if (board[row-i][column].equals(color)==false){
                        break;
                    }
                    count=count+1;
                }
            }
            catch (IndexOutOfBoundsException ibe){

            }
            return count;
        }
        public int checkVerticalDown(String color, int row, int column){
            int count=0;
            try{
                for (int i=1; i<6;i=i+1){
                    if (board[row+i][column].equals(color)==false){
                        break;
                    }
                    count=count+1;
                }
            }
            catch (IndexOutOfBoundsException ibe){

            }
            return count;
        }


        public boolean checkHorizontal(String color, int row, int column){
            return (1+checkHorizontalRight(color,row,column)+checkHorizontalLeft(color,row,column)>=4);

        }
        public int checkHorizontalRight(String color, int row, int column){
            int count=0;
            try{
                for (int i=1; i<7;i=i+1){
                    if (board[row][column+i].equals(color)==false){
                        break;
                    }
                    count=count+1;
                }
            }
            catch (IndexOutOfBoundsException ibe){

            }
            return count;
        }
        public int checkHorizontalLeft(String color, int row, int column){
            int count=0;
            try{
                for (int i=1; i<7;i=i+1){
                    if (board[row][column-i].equals(color)==false){
                        break;
                    }
                    count=count+1;
                }
            }
            catch (IndexOutOfBoundsException ibe){

            }
            return count;
        }

        public boolean checkDiagonal(String color, int row, int column){
            return (((checkDiagonalRightUp(color,row,column)+checkDiagonalLeftDown(color,row,column)+1)>=4)
                    || ((checkDiagonalRightDown(color,row,column)+checkDiagonalLeftUp(color,row,column)+1)>=4));
    }
        public int checkDiagonalRightUp(String color, int row, int column){
            int count=0;
            try{
                for (int i=1; i<6;i=i+1){
                    if (board[row-i][column+i].equals(color)==false){
                        break;
                    }
                    count=count+1;
                }
            }
            catch (IndexOutOfBoundsException ibe){

            }
            return count;
        }
        public int checkDiagonalRightDown(String color, int row, int column){
            int count=0;
            try{
                for (int i=1; i<6;i=i+1){
                    if (board[row+i][column+i].equals(color)==false){
                        break;
                    }
                    count=count+1;
                }
            }
            catch (IndexOutOfBoundsException ibe){

            }
            return count;
        }
        public int checkDiagonalLeftUp(String color, int row, int column){
            int count=0;
            try{
                for (int i=1; i<6;i=i+1){
                    if (board[row-i][column-i].equals(color)==false){
                        break;
                    }
                    count=count+1;
                }
            }
            catch(IndexOutOfBoundsException ibe){

            }
            return count;
        }
        public int checkDiagonalLeftDown(String color, int row, int column){
            int count=0;
            try{
                for (int i=1; i<6;i=i+1){
                    if (board[row+i][column-i].equals(color)==false){
                        break;
                    }
                    count=count+1;
                }
            }
            catch(IndexOutOfBoundsException ibe){

            }
            return count;
        }

        public void newGame(){
            setContentView(R.layout.activity_main);

            gameBoard= new Board();
            layoutPieces= (GridLayout) findViewById(R.id.grid);
            gameIsComplete=false;

            find= new HashMap<Integer,HashMap<Integer, Integer>>();
            int findRow=0;
            int findColumn=0;

            for (int i=0; i<layoutPieces.getChildCount();i=i+1){
                ImageView currentPiece= (ImageView) layoutPieces.getChildAt(i);
                currentPiece.setTranslationY(-1500f);




                if (find.containsKey(findRow)==true){
                    HashMap<Integer,Integer> temp2= find.get(findRow);
                    temp2.put(findColumn,i);
                    find.put(findRow,temp2);
                }
                else{
                    HashMap<Integer,Integer> temp= new HashMap<Integer,Integer>();
                    temp.put(findColumn,i);
                    find.put(findRow,temp);
                }


                findColumn=findColumn+1;
                if (findColumn==7){
                    findColumn=0;
                    findRow=findRow+1;
                }
            }
            Log.i("hashmap",find.toString());
            Log.i("positions", gameBoard.positions.toString());

        }


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameBoard= new Board();
        layoutPieces= (GridLayout) findViewById(R.id.grid);

        find= new HashMap<Integer,HashMap<Integer, Integer>>();
        int findRow=0;
        int findColumn=0;

        for (int i=0; i<layoutPieces.getChildCount();i=i+1){
            ImageView currentPiece= (ImageView) layoutPieces.getChildAt(i);
            currentPiece.setTranslationY(-1500f);




            if (find.containsKey(findRow)==true){
                HashMap<Integer,Integer> temp2= find.get(findRow);
                temp2.put(findColumn,i);
                find.put(findRow,temp2);
            }
            else{
                HashMap<Integer,Integer> temp= new HashMap<Integer,Integer>();
                temp.put(findColumn,i);
                find.put(findRow,temp);
            }


            findColumn=findColumn+1;
            if (findColumn==7){
                findColumn=0;
                findRow=findRow+1;
            }
        }
        Log.i("hashmap",find.toString());
        Log.i("positions", gameBoard.positions.toString());

    }
}
