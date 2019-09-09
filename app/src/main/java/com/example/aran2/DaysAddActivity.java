package com.example.aran2;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    String date, inputDate;
    ViewPageAdapter viewPageAdapter;
    ArrayList<String> modiList;
    Button  btn_removePhoto, btn_addDiary;
    ImageButton btn_addPhoto,btn_explain;
    ArrayList<Uri> imageList;
    RadioGroup.OnCheckedChangeListener OnCheckedChangeListener_positive,OnCheckedChangeListener_negative,OnCheckedChangeListener_input;
    RadioGroup rg_selectPositiveGroup, rg_selectNegativeGroup, rg_selectStateGroup;
    RadioGroup rg_input;
    SimpleDateFormat dateInfo = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    Map<String, String> saveList = new HashMap<>();
    boolean isLoading = false;
    ViewPager vp_imageViews;
    ProgressDialog p;
    boolean photoModi = false;
    EditText babyDiary;
    boolean isModi;
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
        editTextParam.height = (int)(getApplicationContext().getResources().getDisplayMetrics().widthPixels*0.4);
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
        isModi = getIntent().getBooleanExtra("isModi", false);

        rg_selectPositiveGroup = findViewById(R.id.addDiary_positiveRadioGroup);
        rg_selectNegativeGroup = findViewById(R.id.addDiary_negativeRadioGroup);
        rg_input = findViewById(R.id.addDiary_input);
        babyDiary = findViewById(R.id.addDiary_babyDiary);
        if(!isModi){
            RadioButton radioButton = findViewById(R.id.emotion_happy);
            radioButton.setChecked(true);
            beforeIdEmotion = R.id.emotion_happy;
            radioButton.setBackgroundResource(R.drawable.custom_radio_onclick);
            emotionEditText.setText(radioButton.getText());
            emotionEditText.setClickable(false);
            emotionEditText.setEnabled(false);

            RadioButton stateRadio = findViewById(R.id.state_health);
            stateRadio.setChecked(true);
            beforIdState = R.id.state_health;
            stateRadio.setBackgroundResource(R.drawable.custom_radio_onclick);
            stateEditText.setText(stateRadio.getText());

        }else{
            babyDiary.setText(getIntent().getStringExtra("baby"));
            todayContext.setText(getIntent().getStringExtra("content"));
            int emoId = getIntent().getIntExtra("emotionId", R.id.emotion_happy);
            RadioButton radioButton = findViewById(emoId);
            radioButton.setChecked(true);
            beforeIdEmotion = emoId;
            emotionEditText.setText(radioButton.getText());
            radioButton.setBackgroundResource(R.drawable.custom_radio_onclick);
            if(radioButton.getText().equals("직접 입력")){
                emotionEditText.setText(getIntent().getStringExtra("emotion"));
                emotionEditText.setEnabled(true);
                emotionEditText.setClickable(true);

            }else{
                emotionEditText.setEnabled(false);
                emotionEditText.setClickable(false);
            }
            int stateId = getIntent().getIntExtra("stateId", R.id.state_health);
            RadioButton stateRadio = findViewById(stateId);
            stateRadio.setChecked(true);
            beforIdState = stateId;
            stateRadio.setBackgroundResource(R.drawable.custom_radio_onclick);
            stateEditText.setText(stateRadio.getText());
            if(stateRadio.getText().equals("직접 입력")){
                stateEditText.setText(getIntent().getStringExtra("state"));
                stateEditText.setEnabled(true);
                stateEditText.setClickable(true);
            }else{
                stateEditText.setEnabled(false);
                stateEditText.setClickable(false);
            }
        }
        modiList = getIntent().getStringArrayListExtra("imageUri");
        btn_removePhoto = findViewById(R.id.addDiary_removePhoto);
        imageList = new ArrayList<>();
        if(modiList==null||modiList.size()==0){
            viewPageAdapter = new ViewPageAdapter(getApplicationContext(), imageList);
            vp_imageViews.setAdapter(viewPageAdapter);
            photoModi= true;
            ViewGroup.LayoutParams vp_imageViewsParam = vp_imageViews.getLayoutParams();
            vp_imageViewsParam.height = 0;

            vp_imageViews.setLayoutParams(vp_imageViewsParam);

        }else{
            photoModi = false;
            DiaryViewpageAdapter TempviewPageAdapter = new DiaryViewpageAdapter(getApplicationContext(), modiList, false);
            vp_imageViews.setAdapter(TempviewPageAdapter);
            ViewGroup.LayoutParams vp_imageViewsParam = vp_imageViews.getLayoutParams();
            vp_imageViewsParam.height = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            vp_imageViews.setLayoutParams(vp_imageViewsParam);
            btn_removePhoto.setVisibility(View.VISIBLE);
        }
        btn_addDiary = findViewById(R.id.addDiary_btn);
        btn_addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputExist()){
                    btn_addDiary.setClickable(false);
                    isLoading = true;
                    saveInfo();
                }
            }
        });


        btn_removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoModi = true;
                ViewGroup.LayoutParams vp_imageViewsParam = vp_imageViews.getLayoutParams();
                vp_imageViewsParam.height = 0;
                vp_imageViews.setLayoutParams(vp_imageViewsParam);
                btn_removePhoto.setVisibility(View.GONE);
                vp_imageViews.setAdapter(null);
                imageList.clear();
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
               final ImageButton addDiary_back = findViewById(R.id.addDiary_backBtn);
        addDiary_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_explain = findViewById(R.id.addDiary_explainPhoto);
        btn_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneButtonDialog oneButtonDialog = new OneButtonDialog(DaysAddActivity.this, "사진 추가 방법", "1. 사진 추가 버튼을 누릅니다.\n2.두 개 이상의 사진을 고르고 싶을 땐 꾹 눌러서 추가해주세요.\n※세 장까지 추가할 수 있습니다.",
                        "확인");
                oneButtonDialog.show();
            }
        });

    }

    public boolean checkInputExist(){
        if(emotionEditText.getText().toString().equals("")){
            OneButtonDialog showDialog = new OneButtonDialog(DaysAddActivity.this
                    , "아이의 감정이 입력되지 않았어요!", "감정을 입력해주세요", "확인");
            showDialog.show();
            return false;
        }else if(stateEditText.getText().toString().equals("")){
            OneButtonDialog showDialog = new OneButtonDialog(DaysAddActivity.this, "아이의 상태가 입력되지 않았어요!", "상태를 입력해주세요", "확인");
            showDialog.show();
            return false;
        }else if(babyDiary.getText().toString().equals("")){
            OneButtonDialog showDialog = new OneButtonDialog(DaysAddActivity.this, "아이가 어떤 일이 있었는 지 입력되지 않았어요!", "오늘 있던 일을 입력해주세요", "확인");
            showDialog.show();
            return false;
        }else{
            return true;
        }
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
                        photoModi=true;
                        imageList.clear();

                        ClipData clipData = data.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            if(i>=3){
                                break;
                            }
                            imageList.add(clipData.getItemAt(i).getUri());
                        }

                        ViewGroup.LayoutParams vp_imageViewsParam = vp_imageViews.getLayoutParams();
                        vp_imageViewsParam.height = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
                        vp_imageViews.setLayoutParams(vp_imageViewsParam);
                        viewPageAdapter = new ViewPageAdapter(getApplicationContext(), imageList);
                        vp_imageViews.setAdapter(viewPageAdapter);

                    }else if(data.getData()!=null){
                        imageList.clear();
                        imageList.add(data.getData());
                        photoModi = true;

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
    public void onBackPressed(){
        if(!isLoading){
            super.onBackPressed();
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

        String emotion = emotionEditText.getText().toString();
        String state = stateEditText.getText().toString();
        String content = todayContext.getText().toString();
        saveList.clear();
        Date today = new Date();
        date = data.format(today);
        inputDate = dateInfo.format(today);
        if(isModi){
            date = getIntent().getStringExtra("id");
            inputDate = getIntent().getStringExtra("day");
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("id").child(date);
        final Hashtable<String, Object> diaryContent = new Hashtable<>();
        diaryContent.put("id", date);
        diaryContent.put("emotion", emotion);
        diaryContent.put("state", state);
        diaryContent.put("content", content);
        diaryContent.put("baby", babyDiary.getText().toString());
        diaryContent.put("emotionId", beforeIdEmotion);
        diaryContent.put("stateId", beforIdState);

        diaryContent.put("day", inputDate);
        final FirebaseStorage storage = FirebaseStorage.getInstance();

        p = new ProgressDialog(DaysAddActivity.this);
        p.setCancelable(false);
        p.setTitle("클라우드에 올리는 중입니다.");
        p.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        p.show();
        final ArrayList<String> imageUri =new ArrayList<>();
        if(!photoModi){
            diaryContent.put("imageUri", modiList);
            databaseReference.setValue(diaryContent);
            p.dismiss();
            OneButtonDialog saveFinishDialog = new OneButtonDialog(DaysAddActivity.this, "일기 저장", "일기가 성공적으로 저장되었습니다.", "확인");
            saveFinishDialog.setDialogOnClickListener(new DialogOnClickListener() {
                @Override
                public void onPositiveClicked() {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id", date);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

                @Override
                public void onNegativeClicked() {

                }
            });
            saveFinishDialog.show();
        }else {
            for (int i = 0; i < imageList.size(); i++) {
                final StorageReference storageReference = storage.getReference().child("id/" + date + "_" + i + ".jpg");
                final String saveFile = "id/" + date + "_" + i + ".jpg";
                UploadTask uploadTask = storageReference.putFile(imageList.get(i));

                final int position = i;
                p.setMessage(i + "/" + imageList.size());
                double progress = 100.0 * (i / imageList.size());
                p.setProgress((int) progress);

                Task<Uri> uriTask = uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        p.setProgress((int) progress);

                        System.out.println("Upload is " + progress + "% done" + position);
                    }
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                            imageUri.add(saveFile);
                            if (position == imageList.size() - 1) {
                                diaryContent.put("imageUri", imageUri);
                                databaseReference.setValue(diaryContent);
                                p.dismiss();
                                OneButtonDialog saveFinishDialog = new OneButtonDialog(DaysAddActivity.this, "일기 저장", "일기가 성공적으로 저장되었습니다.", "확인");
                                saveFinishDialog.setDialogOnClickListener(new DialogOnClickListener() {
                                    @Override
                                    public void onPositiveClicked() {
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("id", date);
                                        setResult(RESULT_OK, resultIntent);
                                        finish();
                                    }

                                    @Override
                                    public void onNegativeClicked() {

                                    }
                                });
                                saveFinishDialog.show();
                            }
                        }
                    }
                });
            }
            if (imageList.size() <= 0) {

                databaseReference.setValue(diaryContent);
                p.dismiss();

                OneButtonDialog saveFinishDialog = new OneButtonDialog(DaysAddActivity.this, "일기 저장", "일기가 성공적으로 저장되었습니다.", "확인");
                saveFinishDialog.setDialogOnClickListener(new DialogOnClickListener() {
                    @Override

                    public void onPositiveClicked() {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("id", date);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                saveFinishDialog.show();
            }

        }

    }
    @Override
    public void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.slide_right_show,R.anim.slide_right_remove);
    }
}
