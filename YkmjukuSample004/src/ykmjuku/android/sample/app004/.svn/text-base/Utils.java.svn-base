package jp.co.kayo.android.flydroid;
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

/***
 * 便利な関数たち
 * 
 * @author yokmama
 *
 */
public class Utils {
    /***
     * 矩形Aと矩形Bが交差しているかチェックする
     * 
     * @param ax1
     * @param ay1
     * @param ax2
     * @param ay2
     * @param bx1
     * @param by1
     * @param bx2
     * @param by2
     * @return
     */
    public static boolean isCrash(int ax1, int ay1, int ax2, int ay2, int bx1,
            int by1, int bx2, int by2) {
        if (ax1 < bx2 && ay1 < by2 && ax2 > bx1 && ay2 > by1) {
            return true;
        }
        return false;
    }
    
    /***
     * ミリ秒を　分：秒：ミリ　の表示の文字列にする
     * 
     * @param millis
     * @return
     */
    public static String formatTime(long millis) {
        int tsec = (int)(millis/1000);
        long min = (tsec/60)%60;
        long sec = tsec%60;
        long msec =  (millis - (sec*1000))%1000;
        
        StringBuilder buf = new StringBuilder();
        if(millis>=60000){
            buf.append(min).append(":");
        }
        if(millis>=1000){
            buf.append(String.format("%1$02d", sec)).append(":");
        }
        if(msec>0){
            buf.append(String.format("%1$03d", msec));
        }
        
        return buf.toString();
    }
}
