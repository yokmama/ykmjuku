package ykmjuku.android.sample.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/***
 * AndroidManifest.xmlに最初に起動するアプリの設定をListMainに変更しています。
 * 間違えやすいのでご注意ください。
 * 
 * @author yokmama
 *
 */
public class ListMain extends Activity {
    final int REQ_CALC = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmain);
        
        ((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                Intent intent = new Intent(ListMain.this, Ykmjuku002Activity.class);
                startActivityForResult(intent, REQ_CALC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CALC && resultCode == RESULT_OK){
            String result = data.getStringExtra("result");
            EditText edittext = (EditText)findViewById(R.id.editText1);
            edittext.setText(result);
        }
    }
    
}
