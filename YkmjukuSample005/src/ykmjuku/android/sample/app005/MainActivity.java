package ykmjuku.android.sample.app005;
/***
 * Copyright (c) 2010-2012 Re:Kayo-System, Ltd. All rights reserved.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/***
 * 音声認識Soriです。
 * 言葉を入力すると”こんにちは”にだけ反応して返事をします。
 * 
 * @author yokmama
 *
 */
public class MainActivity extends Activity implements OnInitListener {
    private TextToSpeech mTts;
    private TextView mDisplayText;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mTts = new TextToSpeech(this, this);
        mDisplayText = (TextView)findViewById(R.id.textView1);
        
        ((ImageButton)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startSori();
            }
        });
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mTts) {
            mTts.shutdown();
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = Locale.JAPANESE;
            if (mTts.isLanguageAvailable(locale) >=
                TextToSpeech.LANG_AVAILABLE) {
                mTts.setLanguage(locale);
                mTts.setPitch(1.5f);
                mTts.setSpeechRate(1.5f);
                return;
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == RESULT_OK){
            List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(result!=null){
                for(String s : result){
                    if("こんにちは".equals(s)){
                        speech("わんわん！");
                        return;
                    }
                }
                //not much
                speech("何を言っているかわかりません");
            }
        }
    }
    
    private void speech(String text){
        mDisplayText.setText(text);
        if (mTts.isSpeaking()) {
            mTts.stop();
        }
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void startSori(){
        Intent intent = new Intent();
        intent.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt));
        startActivityForResult(intent, 0);
    }
}