package ykmjuku.android.sample.app;

public class Calculater {
    StringBuilder mInputNumber = new StringBuilder();
    String mOperator;
    int mResult = 0;

    private boolean isNumber(String key) {
        try {
            Integer.parseInt(key);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    private boolean isSupportedOperator(String key) {
        if (key.equals("+")) {
            return true;
        } else if (key.equals("-")) {
            return true;
        } else if (key.equals("*")) {
            return true;
        } else if (key.equals("/")) {
            return true;
        }
        return false;
    }

    private void doCalculation(String ope) {
        if (ope.equals("+")) {
            mResult = mResult + Integer.parseInt(mInputNumber.toString());
        } else if (ope.equals("-")) {
            mResult = mResult - Integer.parseInt(mInputNumber.toString());
        } else if (ope.equals("*")) {
            mResult = mResult * Integer.parseInt(mInputNumber.toString());
        } else if (ope.equals("/")) {
            mResult = mResult / Integer.parseInt(mInputNumber.toString());
        }
        mInputNumber = new StringBuilder();
    }

    public String putInput(String key) {
        if (isNumber(key)) {
            mInputNumber.append(key);
            return mInputNumber.toString();
        } else if (isSupportedOperator(key)) {
            if (mOperator != null) {
                doCalculation(mOperator);
                mOperator = null;
            } else if (mInputNumber.length() > 0) {
                mResult = Integer.parseInt(mInputNumber.toString());
                mInputNumber = new StringBuilder();
            }
            mOperator = key;
            return mOperator;
        } else if (key.equals("=")) {
            if (mOperator != null) {
                doCalculation(mOperator);
                mOperator = null;
            }
            return Integer.toString(mResult);
        } else {
            // 入力エラー（数値とサポートされていないオペレータをいれた操作は間違った操作）
            return null;
        }
    }
}
