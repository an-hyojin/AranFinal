package com.example.aran2;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class  DaysAddActivity extends AppCompatActivity implements View.OnClickListener{
    EditText stateEditText, emotionEditText, todayContext;
    LinearLayout stateLayout, emotionLayout;
    CheckBox showBtn_state, showBtn_emotion;
    ViewPageAdapter viewPageAdapter;
    Button btn_addPhoto, btn_removePhoto, btn_addDiary;
    ArrayList<Uri> imageList;
    RadioGroup.OnCheckedChangeListener OnCheckedChangeListener_positive,OnCheckedChangeListener_negative,OnCheckedChangeListener_input;
    RadioGroup rg_selectPositiveGroup, rg_selectNegativeGroup, rg_selectStateGroup;
    RadioGroup rg_input;
    SimpleDateFormat dateInfo = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    Map<String, String> saveList = new HashMap<>();
    ViewPager vp_imageViews;
    ArrayList<File> files = new ArrayList<>();
    int beforIdState, beforeIdEmotion;
    final static int SHOW_IMAGES = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_add);
        showBtn_state = findViewById(R.id.addDiary_showState);
        showBtn_emotion = findViewById(R.id.addDiary_showEmotion);
        stateEditText = findViewById(R.id.addDiary_stateEditText);
        emotionEditText = findViewById(R.id.addDiary_emotionEditText);
        stateLayout = findViewById(R.id.addDiary_stateContainer);
        emotionLayout = findViewById(R.id.addDiary_emotionContainer);
        btn_addPhoto = findViewById(R.id.addDiary_addPhotoBtn);
        todayContext =findViewById(R.id.addDiary_Contents);
        ViewGroup.LayoutParams editTextParam = todayContext.getLayoutParams();
        editTextParam.height = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        todayContext.setLayoutParams(editTextParam);
        btn_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"사진을 골라주세요"), SHOW_IMAGES);
            }
        });
        showBtn_emotion.setChecked(true);
        showBtn_emotion.setOnClickListener(this);
        showBtn_state.setChecked(true);
        showBtn_state.setOnClickListener(this);
        vp_imageViews = findViewById(R.id.addDiary_photoPager);
        rg_selectPositiveGroup = findViewById(R.id.addDiary_positiveRadioGroup);
        rg_selectNegativeGroup = findViewById(R.id.addDiary_negativeRadioGroup);
        rg_input = findViewById(R.id.addDiary_input);
        RadioButton radioButton = findViewById(R.id.emotion_happy);
        radioButton.setChecked(true);
        beforeIdEmotion = R.id.emotion_happy;
        ((RadioButton)findViewById(R.id.emotion_happy)).setChecked(true);
        ((RadioButton)findViewById(R.id.emotion_happy)).setBackgroundResource(R.drawable.custom_radio_onclick);
        emotionEditText.setText(((RadioButton) findViewById(R.id.emotion_happy)).getText());
        ((RadioButton)findViewById(R.id.state_health)).setChecked(true);
        ((RadioButton)findViewById(R.id.state_health)).setBackgroundResource(R.drawable.custom_radio_onclick);
        imageList = new ArrayList<>();
        viewPageAdapter = new ViewPageAdapter(getApplicationContext(), imageList);
        stateEditText.setText(((RadioButton) findViewById(R.id.state_health)).getText());
        btn_addDiary = findViewById(R.id.addDiary_btn);
        btn_addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
        vp_imageViews.setAdapter(viewPageAdapter);
        beforIdState = R.id.state_health;
        emotionEditText.setClickable(false);
        emotionEditText.setEnabled(false);
        btn_removePhoto = findViewById(R.id.addDiary_removePhoto);
        btn_removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams vp_imageViewsParam = vp_imageViews.getLayoutParams();
                vp_imageViewsParam.height = 0;
                vp_imageViews.setLayoutParams(vp_imageViewsParam);
                btn_removePhoto.setVisibility(View.GONE);
            }
        });
        OnCheckedChangeListener_positive = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               findViewById(checkedId).setBackgroundResource(R.drawable.custom_radio_onclick);
               findViewById(beforeIdEmotion).setBackgroundResource(R.drawable.custom_radio_btn);
               beforeIdEmotion = checkedId;
                emotionEditText.setClickable(false);
                emotionEditText.setEnabled(false);
                RadioButton radioButton = findViewById(rg_selectPositiveGroup.getCheckedRadioButtonId());
                emotionEditText.setText(radioButton.getText());
                System.out.print(radioButton.getText());
                rg_selectNegativeGroup.setOnCheckedChangeListener(null);
                rg_selectNegativeGroup.clearCheck();
                rg_input.setOnCheckedChangeListener(null);
                rg_input.clearCheck();
                rg_selectNegativeGroup.setOnCheckedChangeListener(OnCheckedChangeListener_negative);
                rg_input.setOnCheckedChangeListener(OnCheckedChangeListener_input);
            }
        };
        OnCheckedChangeListener_negative = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                findViewById(checkedId).setBackgroundResource(R.drawable.custom_radio_onclick);
                findViewById(beforeIdEmotion).setBackgroundResource(R.drawable.custom_radio_btn);
                beforeIdEmotion = checkedId;
                emotionEditText.setClickable(false);
                emotionEditText.setEnabled(false);
                RadioButton radioButton = findViewById(rg_selectNegativeGroup.getCheckedRadioButtonId());
                emotionEditText.setText(radioButton.getText());
                System.out.print(radioButton.getText());
                rg_selectPositiveGroup.setOnCheckedChangeListener(null);
                rg_selectPositiveGroup.clearCheck();
                rg_input.setOnCheckedChangeListener(null);
                rg_input.clearCheck();
                rg_selectPositiveGroup.setOnCheckedChangeListener(OnCheckedChangeListener_positive);
                rg_input.setOnCheckedChangeListener(OnCheckedChangeListener_input);
            }
        };
        OnCheckedChangeListener_input = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                findViewById(checkedId).setBackgroundResource(R.drawable.custom_radio_onclick);
                findViewById(beforeIdEmotion).setBackgroundResource(R.drawable.custom_radio_btn);
                beforeIdEmotion = checkedId;
                emotionEditText.setEnabled(true);
                 emotionEditText.setClickable(true);
                 emotionEditText.setText("");
                 emotionEditText.setHint("직접 감정을 입력하세요");
                rg_selectNegativeGroup.setOnCheckedChangeListener(null);
                rg_selectNegativeGroup.clearCheck();
                rg_selectPositiveGroup.setOnCheckedChangeListener(null);
                rg_selectPositiveGroup.clearCheck();
                rg_selectPositiveGroup.setOnCheckedChangeListener(OnCheckedChangeListener_positive);
                rg_selectNegativeGroup.setOnCheckedChangeListener(OnCheckedChangeListener_negative);
            }
        };
        rg_selectNegativeGroup.setOnCheckedChangeListener(OnCheckedChangeListener_negative);
        rg_selectPositiveGroup.setOnCheckedChangeListener(OnCheckedChangeListener_positive);
        rg_input.setOnCheckedChangeListener(OnCheckedChangeListener_input);
        rg_selectStateGroup = findViewById(R.id.addDiary_selectStateGroup);
        rg_selectStateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                findViewById(checkedId).setBackgroundResource(R.drawable.custom_radio_onclick);
                findViewById(beforIdState).setBackgroundResource(R.drawable.custom_radio_btn);
                beforIdState = checkedId;
                stateEditText.setEnabled(false);
                stateEditText.setClickable(false);
                stateEditText.setText(((RadioButton)findViewById(checkedId)).getText());
                if(((RadioButton)findViewById(checkedId)).getText().equals("직접 입력")){
                    stateEditText.setEnabled(true);
                    stateEditText.setClickable(true);
                    stateEditText.setHint("아이의 상태를 입력하세요");
                    stateEditText.setText("");
                }
            }
        });
        ViewGroup.LayoutParams vp_imageViewsParam = vp_imageViews.getLayoutParams();
        vp_imageViewsParam.height = 0;
        vp_imageViews.setLayoutParams(vp_imageViewsParam);
        final Button addDiary_back = findViewById(R.id.addDiary_backBtn);
        addDiary_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        System.out.print("OK?아님?");
        if(resultCode == RESULT_OK){
            System.out.print("OK");
            switch (requestCode){
                case SHOW_IMAGES:
                    System.out.print("내꺼 ");
                    btn_removePhoto.setVisibility(View.VISIBLE);
                    if(data.getClipData()!=null) {
                        imageList.clear();
                        files.clear();
                        ClipData clipData = data.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            if(i>=3){
                                break;
                            }
                            imageList.add(clipData.getItemAt(i).getUri());
                            files.add(new File(imageList.get(i).getPath()));
                        }

                        ViewGroup.LayoutParams vp_imageViewsParam = vp_imageViews.getLayoutParams();
                        vp_imageViewsParam.height = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
                        vp_imageViews.setLayoutParams(vp_imageViewsParam);
                        viewPageAdapter = new ViewPageAdapter(getApplicationContext(), imageList);
                        vp_imageViews.setAdapter(viewPageAdapter);

                    }else if(data.getData()!=null){
                        imageList.clear();
                        imageList.add(data.getData());
                        files.clear();

                        files.add(new File(data.getData().getPath()));
                        vp_imageViews.setAdapter(new ViewPageAdapter(getApplicationContext(), imageList));
                        ViewGroup.LayoutParams vp_imageViewsParam = vp_imageViews.getLayoutParams();
                        vp_imageViewsParam.height = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
                        vp_imageViews.setLayoutParams(vp_imageViewsParam);
                    }

                    break;
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addDiary_showEmotion:
                if(showBtn_emotion.isChecked()){
                    emotionLayout.setVisibility(View.VISIBLE);
                }else{
                    emotionLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.addDiary_showState:
                if(showBtn_state.isChecked()){
                    stateLayout.setVisibility(View.VISIBLE);
                }else{
                    stateLayout.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    public void saveInfo(){
        String emotion = "#"+emotionEditText.getText().toString();
        String state = "#"+stateEditText.getText().toString();
        String content = todayContext.getText().toString();
        saveList.clear();
        Date today = new Date();
        final String date = data.format(today);
        final String inputDate = dateInfo.format(today);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("id").child(date);
        final Hashtable<String, Object> diaryContent = new Hashtable<>();
        diaryContent.put("emotion", emotion);
        diaryContent.put("state", state);
        diaryContent.put("content", content);
        final FirebaseStorage storage = FirebaseStorage.getInstance();


        for(int i = 0 ; i< imageList.size();i++){
            final StorageReference storageReference = storage.getReference().child("id/"+date+"_"+i+".jpg");
            final String saveFile = "id/"+date+"_"+i+".jpg";
            UploadTask uploadTask = storageReference.putFile(imageList.get(i));
            final int position = i;
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        saveList.put(String.valueOf(position), saveFile);
                        if(position==imageList.size()-1){
                            DatabaseReference imageReference = databaseReference.child("imageUri");
                            imageReference.setValue(saveList);
                        }
                    }
                }
            });
            diaryContent.put("day", inputDate);
            databaseReference.setValue(diaryContent);
        }
        if(imageList.size()<=0) {

            diaryContent.put("day", inputDate);
            databaseReference.setValue(diaryContent);
            DatabaseReference imageReference=  databaseReference.child("imageUri");
            Map<Integer, String> temp = new HashMap<>();
            imageReference.setValue(temp);
        }

        OneButtonDialog saveFinishDialog = new OneButtonDialog(DaysAddActivity.this, "일기 저장", "일기가 성공적으로 저장되었습니다.", "확인");
        saveFinishDialog.setDialogOnClickListener(new DialogOnClickListener() {
            @Override
            public void onPositiveClicked() {
                onBackPressed();
            }

            @Override
            public void onNegativeClicked() {

            }
        });
        saveFinishDialog.show();

    }
}
