package ykmjuku.android.sample007.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SubMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submain);
        
        TextView textview = (TextView)findViewById(R.id.textView1);
        textview.setText(getIntent().getStringExtra("label"));
        
        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                TextView textview = (TextView)findViewById(R.id.textView1);
                Intent intent = new Intent();
                intent.putExtra("result", textview.getText());
                setResult(RESULT_OK, intent);
                
                finish();
            }
        });
    }

}
