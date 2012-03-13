package ykmjuku.android.sample.app001;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/***
 * このサンプルプログラムは夜子まま塾サンプルプログラム集001の電卓プログラムです
 * 
 * @author yokmama
 *
 */
public class MainActivity extends Activity implements OnClickListener {
    EditText mEditText1;
    Calculater mCalculater = new Calculater();
    final int[] _btnlist = new int[] { R.id.button1, R.id.button2,
            R.id.button3, R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9, R.id.button10,
            R.id.button11, R.id.button12, R.id.button13, R.id.button14,
            R.id.button15, R.id.button16, R.id.button17 };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mEditText1 = (EditText) findViewById(R.id.editText1);
               
        for(int id : _btnlist){
            Button btn = (Button) findViewById(id);
            btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button)v;
        String input = btn.getText().toString();
        if("C".equals(input)){
            mCalculater.clear();
            mEditText1.setText(null);
        }
        else{
            calcInput(input);
        }
    }
    
    private void calcInput(String input){
        if (input.length() > 0) {
            String dispText = mCalculater.putInput(input);
            if (dispText != null) {
                mEditText1.setText(dispText);
            }
        }
    }
}