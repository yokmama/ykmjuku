package ykmjuku.android.sample.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Ykmjuku010Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                EditText edittext = (EditText)findViewById(R.id.editText1);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, edittext.getText().toString());
                startActivity(intent);
            }
        });
    }
}