package com.example.aran2;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class OneButtonDialog extends Dialog {
    ConstraintLayout constraintLayout;
    DialogOnClickListener dialogOnClickListener;
    public OneButtonDialog(@NonNull Context context, String titleText, String mainText, String buttonText) {
        super(context);
        setContentView(R.layout.onebuttondialog);
        constraintLayout = findViewById(R.id.oneButtonDialog_constraintLayout);
        constraintLayout.getLayoutParams().height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.4);
        constraintLayout.getLayoutParams().width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.6);
        TextView tv_title = findViewById(R.id.oneButtonDialog_title);
        tv_title.setText(titleText);
        TextView tv_content = findViewById(R.id.oneButtonDialog_content);
        tv_content.setText(mainText);
        Button btn_admit = findViewById(R.id.oneButtonDialog_button);
        btn_admit.setText(buttonText);
        btn_admit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogOnClickListener!=null){
                    dialogOnClickListener.onPositiveClicked();
                }
                dismiss();
            }
        });
    }
    public void setDialogOnClickListener(DialogOnClickListener dialogOnClickListener){
        this.dialogOnClickListener = dialogOnClickListener;
    }
}
