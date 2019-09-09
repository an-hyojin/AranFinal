package com.example.aran2;

import android.graphics.drawable.Drawable;
import android.widget.Button;

public class Card {
    int value;
    boolean isBack;
    Button card;
    Drawable[] frontDrawableIds =
            {};
            //        {R.drawable.smile, R.drawable.angry, R.drawable.sad, R.drawable.disgust, R.drawable.heart, R.drawable.surprised, R.drawable.full, R.drawable.scary};
    String[] info = {"행복", "화남", "슬픔", "싫어함","사랑", "놀람", "뿌듯함", "무서움"};
    Card(int val){
        this.value = val;
    }

    public boolean isBack(){
        return this.isBack;
    }

    public void setIsBack(boolean isBack){
        this.isBack = isBack;
    }

    public void setBackImage(){
        if(!isBack){
            card.setCompoundDrawables(null, null,null,null);
            card.setBackgroundResource(R.drawable.back);
            card.setText("");
            setIsBack(true);
        }
    }

    public void setFront(){
        if(isBack){
//            card.setBackgroundResource(R.drawable.front);
            card.setText(info[value]);
            card.setCompoundDrawables(null,frontDrawableIds[value] ,null,null);
            card.setCompoundDrawablePadding(5);
            setIsBack(false);
        }
    }
}
