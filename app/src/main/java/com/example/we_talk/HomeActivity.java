package com.example.we_talk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private TextToSpeech mTTS;
    TextView txt1;

    ImageView i1,i2;
    TextView txt2;
    String text1, text2;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txt1= findViewById(R.id.welcome);
        txt2 = findViewById(R.id.welcome2);
        i1=findViewById(R.id.imagespeak);
        i2=findViewById(R.id.imagenext);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        Log.e("Status","Language supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text1= txt1.getText().toString();
                text2 = txt2.getText().toString();
                text = text1+" "+text2;
                speak();
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,Page3.class);
                startActivity(intent);
            }
        });
        text1= txt1.getText().toString();
        text2 = txt2.getText().toString();
        text = text1+" "+text2;
        speak();

        Log.e("status","reached");


    }

    @Override
    protected void onStart() {
        text1= txt1.getText().toString();
        text2 = txt2.getText().toString();
        speak();
        super.onStart();
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

    @Override
    protected void onResume() {
        text1= txt1.getText().toString();
        text2 = txt2.getText().toString();
        speak();
        super.onStart();
        super.onResume();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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