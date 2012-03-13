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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/***
 * ウィジェットの初期化を行うクラスです。
 * 画面に配置されたとき、または端末が起動したときに一度だけよばれます。
 * 基本的にはそうですが、xml/appwidget.xmlにて
 * android:updatePeriodMillis の値を０以外にした場合、定期的に更新がよばれます。
 * この値はミリ秒で設定することが可能ですが、一番短い感覚として３０分となっています。
 * そのため３０分以下の値を設定しても強制的に３０分になりますので注意してください。
 * 
 * 本クラスはAndroidManifest.xmlに追加しないと機能しませんので、次の宣言を追加しています。
 * <receiver android:name=".MyAppWidget" android:label="MyAppWidget">
 *     <intent-filter>
 *         <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
 *     </intent-filter>
 *     <meta-data android:name="android.appwidget.provider"
 *         android:resource="@xml/appwidget" />
 * </receiver>
 * 
 * @author yokmama
 *
 */
public class MyAppWidget extends AppWidgetProvider {
    /***
     * ウィジェットが配置されると、よばれます。
     * 引数のappWidgetIdsは配置されたウィジェットのIDです。
     * AppWidgetManagerを使ってウィジェットのアイテムを更新することができますが。
     * IDを指定すると個別に更新ができます。
     * もし指定されない場合はすべてのウィジェットが更新対象になるので注意してください。
     * 本メソッドでは、処理を簡単にするために全て更新しています。
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

        //AppWidgetManagerを取得
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        //AppWidgetに関連付けされているViewを取得
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        
        //ここでは、ウィジェットをクリックするとアニメーションを行うサービスを呼び出すようにしたいため、
        //Viewにクリック時に呼び出すサービスへのIntentを設定しています。
        Intent intent = new Intent(context, MyService.class);
        
        PendingIntent pending = PendingIntent.getService(context, 0, intent, 0);
        view.setOnClickPendingIntent(R.id.imageView1, pending);
        
        //設定をしたら更新
        ComponentName widget = new ComponentName(context, MyAppWidget.class);
        manager.updateAppWidget(widget, view);
    }
}
