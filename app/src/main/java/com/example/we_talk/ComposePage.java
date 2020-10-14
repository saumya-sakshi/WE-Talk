package com.example.we_talk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ComposePage extends AppCompatActivity {
    ImageView t1,  t3;
    TextView t2;
    EditText e1;
    private TextToSpeech mTTS;
    String text;
    static String sent_to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_page);
        t1 = findViewById(R.id.txt_next);
        t2 = findViewById(R.id.txt1);
        t3 = findViewById(R.id.txt_speak);
        e1 = findViewById(R.id.et_to);
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        Log.e("Status", "Language supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text= t2.getText().toString();

                speak();

            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComposePage.this,SubjectMail.class);
                startActivity(intent);
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi,speak!");
                try{
                    startActivityForResult(intent,1);
                }catch (Exception e){
                    Toast.makeText(ComposePage.this,"Error Occured!!!!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String res="";

            if(result.get(0).contains("@gmail.com")){
                ArrayList<Character> ch=new ArrayList<>();
                char c[]=new char[1000];
                c=result.get(0).toCharArray();
                for(char x : c){ ch.add(x);}
                for(int i =0;i<ch.size();i++){
                    if(ch.get(i)==' '){ ch.remove(" ");}
                    else res= res+ch.get(i);
                }
            }
            e1.setText(res);
            speak2();
            sent_to = e1.getText().toString();

        }

    }
    @Override
    protected void onRestart() {
        if (mTTS != null) {
            mTTS.stop();
            //  mTTS.shutdown();
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (mTTS != null) {
            mTTS.stop();
            // mTTS.shutdown();
        }
        super.onStop();
    }
    private void speak2() {

        float pitch=0.5f;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = 0.9f;
        if (speed < 0.1) speed = 0.1f;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak("Click on top of screen to proceed",TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            mTTS.speak("Click on top of screen to proceed", TextToSpeech.QUEUE_ADD, null);
        }

    }



    private void speak() {

        float pitch=0.5f;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = 0.9f;
        if (speed < 0.1) speed = 0.1f;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
        }

    }



    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}
