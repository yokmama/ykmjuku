package ykmjuku.android.sample.app001;
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
    
    public void clear(){
        mResult = 0;
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
