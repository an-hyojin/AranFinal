package com.example.aran2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity implements View.OnClickListener {
    final int TOTAL_CARD_NUM = 8;
    boolean isClicked;
    Card[] cards = new Card[TOTAL_CARD_NUM];
    int[] cardId = {R.id.card1,R.id.card2,R.id.card3,R.id.card4,R.id.card5,R.id.card6,R.id.card7,R.id.card8};
    long ClickTime=0;
    Card first, second;
    int SUCCESS_COUNT=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        LinearLayout top = findViewById(R.id.linearLayout1);
        LinearLayout bot = findViewById(R.id.linearLayout2);
        top.getLayoutParams().height = (int)((getApplicationContext().getResources().getDisplayMetrics().widthPixels/4)*1.4);
        bot.getLayoutParams().height = (int)((getApplicationContext().getResources().getDisplayMetrics().widthPixels/4)*1.4);
        ClickTime = SystemClock.currentThreadTimeMillis();
        setUpForGames();
    }

    public void setUpForGames(){
        for(int i=0; i<TOTAL_CARD_NUM; i++) {
            cards[i] = new Card(i/2);
            findViewById(cardId[i]).setOnClickListener(this);
            cards[i].card = findViewById(cardId[i]);
            cards[i].setBackImage();
        }
    }

    public void startGame(){
        for(int i = 0; i<TOTAL_CARD_NUM; i++){
            if(!cards[i].isBack()){
                cards[i].setBackImage();
            }
        }
        ArrayList<Integer> randomNum = new ArrayList<>();
        boolean[] numsIsIn = new boolean[TOTAL_CARD_NUM];
        boolean[] indexIsIn= new boolean[TOTAL_CARD_NUM];
        for(int i = 0; i<TOTAL_CARD_NUM;i++){
            if(!indexIsIn[i]) {
                int num = (int) ((Math.random() * 100) % TOTAL_CARD_NUM);
                if (!numsIsIn[num]) {
                    randomNum.add(num);
                    numsIsIn[num] = true;
                    int index = (int)((Math.random() * 100) % TOTAL_CARD_NUM);;
                    if(!indexIsIn[index]){
                        randomNum.add(index, num);
                    }
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        long clickTime = SystemClock.currentThreadTimeMillis();
        if(clickTime-ClickTime>=500){

        }
    }

    public void cardGame(View v){
        if(!isClicked) {
            long thisTime = SystemClock.currentThreadTimeMillis();
            if (thisTime-ClickTime>600) {
                for (int i=0; i<TOTAL_CARD_NUM; i++) {
                    if (cards[i].card == v) {
                        first = cards[i];
                        break;
                    }
                }
                if (first.isBack()) {
                    first.setFront();
                    isClicked = true;
                }
            }
        }else{
            ClickTime = SystemClock.currentThreadTimeMillis();
            for (int i=0; i<TOTAL_CARD_NUM; i++) {
                if (cards[i].card == v) {
                    second = cards[i];
                    break;
                }
            }
            if (second.isBack()) {
                second.setFront();
                if (first.value == second.value) {
                    SUCCESS_COUNT++;
                    if (SUCCESS_COUNT == TOTAL_CARD_NUM/2) {

                }
                else{

                    }
                }
                isClicked = false;
            }
        }
    }
}
