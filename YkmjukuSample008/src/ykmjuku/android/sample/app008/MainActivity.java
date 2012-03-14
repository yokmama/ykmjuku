package ykmjuku.android.sample.app008;
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

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/***
 * 簡単な連絡帳アプリです
 * コンテンツプロバイダから、電話帳のデータベースにアクセスしています。
 * 電話帳のアクセスには次のパーミッションが必要なためAndroidManifest.xmlに追加されています。
 * android.permission.READ_CONTACTS
 * 電話帳から取得した連絡先をリストで表示し、選択すると電話がかかります。
 * 電話の発信には次のパーミッションが必要なためAndroidManifest.xmlに追加されています。
 * android.permission.CALL_PHONE
 * 
 * プログラムは一番表示としてはシンプルなSimpleCursorAdapterを用いて、電話帳の情報から
 * 連絡先と電話番号を表示しています。そのため見た目はとてもシンプルです。
 * もし、さらに詳細な情報を表示したい場合は、このSimpleCursorAdapterクラスを継承するよりも
 * CursorAdapterを継承し独自のViewを表示するほうが簡単でしょう。
 * 
 * @author yokmama
 *
 */
public class MainActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layout/main.xmlを読み込み画面を構成する
        setContentView(R.layout.main);
        
        //連絡帳の情報を読み込み画面に設定する関数を呼び出す
        setAddressList();
    }

    /***
     * リストに表示された連絡先をクリックすると呼ばれます
     * このメソッドは本クラスの親クラスであるListActivityをオーバライドしていますが、
     * 中身はListViewのonItemClickを委譲したメソッドになっているため、直接getListView等して
     * 取得したListViewにリスナーを設定するのと同じ意味になります。
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Cursor c = (Cursor)getListAdapter().getItem(position);
        //選択した位置のカーソルから電話番号を取得する
        String number = c.getString(
                c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        
        //電話をかける
        Intent intent = new Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:"+number));

        startActivity(intent);
    }
    
    /***
     * 電話帳を読み込み、ListViewに設定をしています。
     * このような本クラス内で使うようなメソッドはprivateにしておくとよいでしょう。
     * 理由は、もし不要になったときにどこにも参照されていない場合はワーニングが表示されるため
     * 便利だからです。
     */
    private void setAddressList(){
        //電話帳検索処理のパラメータ宣言
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = null;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        
        //表示用のアダプター生成時のパラメーター宣言
        Context context = this;
        int layout = android.R.layout.simple_list_item_2;
        String[] from = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME, 
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        
        //電話帳検索を実施しカーソルを取得
        Cursor c = managedQuery(
                uri, 
                projection, 
                selection, 
                selectionArgs, 
                sortOrder);
        
        //カーソルをもとに表示用アダプターを生成
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                context, 
                layout,
                c,
                from,
                to);
        
        //表示用アダプターをリストに設定
        setListAdapter(adapter);
    }
}