package com.example.aran2;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TwoButtonDialog extends Dialog {
    DialogOnClickListener dialogOnClickListener;
    ConstraintLayout constraintLayout;
    public TwoButtonDialog(@NonNull Context context, String titleTxt, String textTxt, String positiveTxt, String negativeTxt) {
        super(context);
        setContentView(R.layout.twobuttondialog);
        constraintLayout = findViewById(R.id.twoButtonDialog_constraintLayout);
        constraintLayout.getLayoutParams().height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.4);
        constraintLayout.getLayoutParams().width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.6);
        TextView title = findViewById(R.id.twobuttondialog_title);
        TextView text = findViewById(R.id.twobuttondialog_text);
        Button positive = findViewById(R.id.twobuttondialog_positiveBtn);
        Button negative = findViewById(R.id.twobuttondialog_negativeBtn);
        title.setText(titleTxt);
        text.setText(textTxt);
        positive.setText(positiveTxt);
        negative.setText(negativeTxt);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogOnClickListener!=null){
                    dialogOnClickListener.onPositiveClicked();
                }
                dismiss();
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogOnClickListener!=null){
                    dialogOnClickListener.onNegativeClicked();
                }
                dismiss();
            }
        });
    }
    public void setDialogOnClickListener(DialogOnClickListener dialogOnClickListener){
        this.dialogOnClickListener = dialogOnClickListener;
    }
}
