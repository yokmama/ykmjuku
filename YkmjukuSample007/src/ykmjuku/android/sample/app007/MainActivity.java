package ykmjuku.android.sample.app007;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
    public static final String APIKEY="35A45708A82318800D4CE6BC19C52C0F04C22ED9";
    
    private EditText mEdit1;
    private EditText mEdit2;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //画面のレイアウトを読み込む
        setContentView(R.layout.main);

        //変換元のEditTextを取得
        mEdit1 = (EditText)findViewById(R.id.editText1);
        //変換先のEditTextを取得
        mEdit2 = (EditText)findViewById(R.id.editText2);
        
        //ボタンのインスタンスを取得引数のR.id.button1はレイアウトファイルの中で宣言している
        //ボタンのIDです。画面のインスタンスはプログラム内ではIDを通じてアクセスすることができます。
        Button btn = (Button)findViewById(R.id.button1);
        //ボタンをクリックしたときの処理を設定
        btn.setOnClickListener(this);
    }
    

    public void onClick(View v) {
        if(v.getId() == R.id.button1){
            //変換元に文字列がはいっているなら、変換をして変換先に設定する
            final String text = mEdit1.getText().toString();
            if(text.length()>0){
                
                AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){

                    @Override
                    protected String doInBackground(Void... params) {
                        return translate(text);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        mEdit2.setText(result);
                    }
                };
                task.execute();
            }
        }
    }
    
    /***
     * 本メソッドを使用する場合は、別途Bing APIのApplication IDを取得してください。
     * Bing Developer Center(https://ssl.bing.com/webmaster/developers/appids.aspx)
     * 
     * Micorsoft翻訳サービスをもちいて、日本語を英語に翻訳してくれます。
     * 翻訳を変更したい場合は引数の　fromの値とtoの値を変更してください
     * 
     * @param str
     * @return
     */
    private String translate(String str){

        InputStream in = null;
        try{
            //Microsoft翻訳へのURLとそのパラメータを設定したURL文字列を生成
            StringBuilder sb = new StringBuilder();
            sb.append("http://api.microsofttranslator.com/v2/Http.svc/Translate?");
            sb.append("appId="+APIKEY);
            sb.append("&text=").append(URLEncoder.encode(str, "UTF-8"));
            sb.append("&from=ja&to=en");
            
            //URLをオープンする
            URL url = new URL(sb.toString());
            URLConnection con = url.openConnection();

            //HTTPから取得されるストリームを生成
            in = con.getInputStream();

            //<string xmlns="http://schemas.microsoft.com/2003/10/Serialization/">Good afternoon</string>
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            int eventType;
            while((eventType = parser.next()) != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG && "string".equals(parser.getName())){
                    parser.next();
                    return parser.getText();
                }
            }
            return null;
        } catch (IOException e) {
            Log.e("Sample", "translate", e);
        } catch (XmlPullParserException e) {
            Log.e("Sample", "xml parse", e);
        }
        finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {}
            }
        }
        
        return "";
    }    
}