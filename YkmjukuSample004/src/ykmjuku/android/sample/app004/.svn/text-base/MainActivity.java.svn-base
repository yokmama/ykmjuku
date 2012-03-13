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

//Q:パッケージとインポートて何？
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/***
 * ゲームの画面クラス
 * 
 * Q: classの宣言の方法は？
 * Q:　extends, implementsの違いは？
 * 
 * Memo: authorは作成者です。OSの利用者の名前が自動で設定されます。
 * 変更したい場合はEclipseの起動時に-Duser.name=＜変更する名前＞を入れてください。
 * @author yokmama
 *
 */
public class MainActivity extends Activity implements OnTouchListener {    
    // ゲームのメイン処理のインスタンス
    // Q:インスタンスって何？オブジェクトや変数という言葉もありますね
    private GameMain gamemain;
    
    private TextView txtTime;
    private TextView txtScore;

    /***
     * 画面の作成処理
     * Memo： Overridはこのメソッドはオーバーライドされてますという意味です
     * Q: オーバーライドって何？またオーバーロードという言葉もありますね
     * Q: Overrideを付けるメリットは？
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Q: superて何？
        //レイアウトファイルmainをもとに画面を作成
        setContentView(R.layout.main);
        
        txtTime = (TextView)findViewById(R.id.txtTime);
        txtScore = (TextView)findViewById(R.id.txtScore);

        //surfaceviewのインスタンスを取得して、GameMainを作成
        SurfaceView surfaceview = (SurfaceView) findViewById(R.id.SurfaceView1);
        gamemain = new GameMain(surfaceview);
        //画面タッチしたときのイベントをうけとれるように設定
        //Q: thisて何でしょう？
        surfaceview.setOnTouchListener(this);
    }

    /***
     * 画面の終了処理
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /***
     * 画面の一時停止処理
     */
    @Override
    protected void onPause() {
        super.onPause();
        //ディスプレイOFFになったときや、アプリケーションを切り替えるときなどに呼ばれます。
        //画面に表示されていないので裏でうごくのも気持ちわるいので、停止しています。
        gamemain.stop();
    }

    /***
     * 画面の表示処理
     */
    @Override
    protected void onResume() {
        super.onResume();
        //このメソッドは、初期化した後も、また一時停止からの復帰からも呼ばれます。
        //そのため、それぞれで処理を変えるべきですが、ここでは変えていないので
        //一時停止からの復帰からでもリセットされてしまいます。気になる方は工夫してみましょう
        gamemain.start();
    }

    /***
     * 画面タッチ処理
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //画面がタッチされたので、その位置にハチを生成します
        //ただしゲーム世界が生成されていない場合は生成しても意味がないのでその生成済みチェックもしています
        if (event.getAction() == MotionEvent.ACTION_DOWN && gamemain.getWorld().isInit()) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            //さぁ（X,Y）の座標にハチを生成したまえ！
            gamemain.getWorld().genBee(x, y);
        }
        return false;
    }

    /***
     * ゲームのメイン処理
     * 
     * Memo:クラスの中に定義するクラスをインナークラスと呼びます
     * Q:インナークラスのメリットは何でしょう？
     * 
     * @author yokmama
     *
     */
    private class GameMain implements Runnable {
        //メイン処理を停止するかどうかを表すフラグ
        private boolean isStop;
        //ゲーム画面を表示するView
        private SurfaceView surfaceview;
        //ゲームの世界
        private GameWorld world;
        //開始時間（経過時間を図るために使用）
        private long starttime;
        //スレッド内から主スレッド(UIスレッド)で実行したい場合に使用
        private Handler handler = new Handler();

        /***
         * ゲームのメイン処理のコンストラクタ
         * 
         * Memo: コンストラクタというのはそのクラスを生成するときに呼ばれるメソッドです。
         * ただし生成のときに使うものなので他のメソッドとは区別されています。
         * Q:コンストラクタとonCreateは似たことをしていますが位置づけが異なります。
         * どう違うのでしょうか？
         * @param surfaceview1
         */
        private GameMain(SurfaceView surfaceview) {
            //Q:どうしてこういうふうにthis.surfaceviewてしているの？
            this.surfaceview = surfaceview;
            world = new GameWorld();
            isStop = true;
        }

        /***
         * onTouchでGameWorldにアクセスできるようにしています。
         * Memo:このようなgetがつくメソッドをgetterメソッドと呼び、
         * setがつくようなものをsetterメソッドと呼びます。
         * @return
         */
        private GameWorld getWorld() {
            return world;
        }

        /***
         * ゲームの世界をスタートさせます
         */
        private void start() {
            if(isStop){
                isStop = false;
                //契約は成立だ。君の祈りは、エントロピーを凌駕した。さあ、解き放ってごらん。その新しい力を！
                (new Thread(this)).start();
            }
        }

        /***
         * ゲームの世界を停止します
         * ここではフラグの設定だけをしています。
         * Q:どうしてフラグの設定によって停止するような仕組みにしているのでしょうか？
         * じゃあ一緒に見届けようか――鹿目まどかという、存在の結末を
         */
        private void stop() {
            isStop = true;
        }
        
        public void run() {
            //あとで経過時間で使うために開始時間を覚えておく
            starttime = System.currentTimeMillis();
            //ゲーム内の経過時間を刻むために開始時間をメモしておく
            long lasttime = 0;
            //これから繰り返し処理をします
            while (!isStop) {
                long now = System.currentTimeMillis();
                //処理を開始するまえに、いつまでに終わらなきゃいけない処理なのか決めましょう。
                //宇宙全体のエネルギー（CPUの使用量）は、目減りしていく一方なんだからね
                long nextTime = now + Consts.FRAME_RATE;
                
                //そうはいっても世界が受け入れる準備をしていなければどうしようもない
                //isInitでチェックをしてまだなら初期化処理を呼びましょう
                if (world.isInit()) {
                    //経過時間を求める
                    long elapsedtime = 0;
                    if(lasttime!=0){
                        elapsedtime = now - lasttime;
                    }
                    //時計は進んでいるのでこの時間における処理を行います
                    world.process(elapsedtime);
                    
                    //次の経過時間のために値を設定する
                    lasttime = now;
                    //処理に応じて、世界を再描画します
                    world.drawView(surfaceview);
                }
                else{
                    world.init(surfaceview);
                }
                
                
                //画面情報の描画
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        long span = System.currentTimeMillis() - starttime;
                        txtTime.setText(Utils.formatTime(span));
                        txtScore.setText(Long.toString(world.getScore()));
                    }
                });

                //世界は１/60秒で刻まれています
                //どのような早いコンピュータでもそれは等価でなくてはならない。
                //だから処理が早くおわったときは次の時間まで待ってあげるのです。
                while (true) {
                    long sleeptime = nextTime - System.currentTimeMillis();
                    if(sleeptime<0){
                        break;
                    }
                    else{
                        try {
                            Thread.sleep(sleeptime);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }
}