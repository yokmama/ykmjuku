package ykmjuku.android.sample.app002;
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

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/***
 * ウィジェットの絵を紙芝居的に更新するサービスです。
 * ウィジェットがクリックされたときに呼び出されます。
 * サービスは、AndroidManifest.xmlに宣言されていないと機能しないため、次の宣言が追加されています。
 * <service android:name=".MyService">
 * 
 * @author yokmama
 *
 */
public class MyService extends Service {
    //排他制御用のフラグ
    private Boolean _lock = new Boolean(true);
    
    //ペンギンの画像(著作権は右記サイトの作者に帰属します　URL：http://www.pesoguin.jp/）
    static final int[] imgs = new int[]{R.drawable.pen1_03_1, R.drawable.pen1_03_2, R.drawable.pen1_03_3, R.drawable.pen1_03_4, R.drawable.pen1_01_1};

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /***
     * サービスが呼ばれると呼ばれる処理です
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        //AppWidgetManagerを取得
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        //AppWidgetに関連付けされているViewを取得
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.appwidget);
        
        //本処理はクリックされる度によばれます、もしアニメーション中によばれると、この処理がまた呼ばれてしまい
        //更新処理が何個も呼ばれてしまいますので、順次処理をおこなうために
        //synchronized宣言を用いて、以降の処理が同時に処理されないように制御をしています。
        synchronized(_lock){
            //画像の数だけパラパラと１００ミリ秒の感覚で画像を差し替える。
            for(int i=0; i<imgs.length; i++){
                view.setImageViewResource(R.id.imageView1, imgs[i]);
                ComponentName widget = new ComponentName(this, MyAppWidget.class);
                manager.updateAppWidget(widget, view);
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }
        }
        
    }

}
