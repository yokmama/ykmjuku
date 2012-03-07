package ykmjuku.android.sample007.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ykmjuku007Activity extends Activity {
    private final int REQUEST_ID = 0;
    private int mCount=0;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Ykmjuku007Activity.this, SubMain.class);
                intent.putExtra("label", "Ykmjuku007が呼び出した数:"+(++mCount));
                startActivity(intent);
            }
        });
        
        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Ykmjuku007Activity.this, SubMain.class);
                intent.putExtra("label", "Ykmjuku007が呼び出した数:"+(++mCount));
                startActivityForResult(intent, REQUEST_ID);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ID){
            Button btn = (Button)findViewById(R.id.button2);
            if(resultCode == RESULT_OK){
                btn.setText("OK:"+data.getStringExtra("result"));
            }
            else{
                btn.setText("ERROR:"+data.getStringExtra("result"));
            }
        }
    }
}