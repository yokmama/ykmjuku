package ykmjuku.android.sample.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Ykmjuku002Activity extends Activity implements TextWatcher,
        OnClickListener {
    EditText mEditText1;
    TextView mTextView1;
    Button mButton1;
    Calculater mCalculater = new Calculater();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextView1 = (TextView) findViewById(R.id.textView1);
        mEditText1 = (EditText) findViewById(R.id.editText1);
        mButton1 = (Button) findViewById(R.id.button1);

        mEditText1.addTextChangedListener(this);
        mButton1.setOnClickListener(this);
    }

    public void afterTextChanged(Editable s) {
        String input = s.toString();
        Log.d("Ykmjuku002Activity", "input=" + input);
        if (input.length() > 0) {
            String dispText = mCalculater.putInput(input);
            if (dispText != null) {
                mTextView1.setText(dispText);
            }
            s.clear();
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    public void onClick(View v) {
        String dispText = mCalculater.putInput("=");
        if (dispText != null) {
            mTextView1.setText(dispText);
        }
        mEditText1.setText(null);
    }
}