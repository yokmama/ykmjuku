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

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceView;

/***
 * ゲームの世界
 * このクラスは箱庭です。時計をすすめるとそれに応じて世界に存在する
 * 物体、あるいは生き物そういったものを動かしてください。
 * また、動かしたことによって起こる出来事も処理してください。
 * 世界は左上が（０，０）右下が（width, height)になっています。
 * 
 * @author yokmama
 *
 */
public class GameWorld {
    //物体の状態
    //Memo:enumは列挙された数値です。文字のほほうが分かりやすい場合に使います。
    enum ObjectState {
        STOPING, MOVING, OUTOFSCREEN, COLLISION,
    };

    //移動した後に何が起きたのかを判断する状態
    enum ResultType {
        FINE, CRASH, DESTROY
    }

    //ハチの画像
    private Bitmap beeImg;
    //ドロイド君の画像
    private Bitmap droidImg;
    //世界に存在する全てのハチ
    private ArrayList<Bee> bees;
    //世界に存在する全てのドロイド君
    private ArrayList<Droid> droids;
    //初期化がされていればTrue,そうでなけれFalseが設定されるフラグ
    private boolean initialized;
    //便利な乱数発生装置
    private Random rand;
    //世界の広さ（縦）
    private int height;
    //世界の広さ（幅）
    private int width;
    //効果音
    private SoundManager soundmgr;
    //得点
    private long score;

    /***
     * 世界に存在するオブジェクトを表すクラス
     * Memo：abstractは抽象クラスを表す属性です
     * Q:抽象クラスを作るメリットをなんでしょう？
     * 
     * @author yokmama
     *
     */
    private abstract class GameObject {
        //X方向の加速度
        float dx;
        //Y方向の加速度
        float dy;
        //オブジェクトの現在地X
        int x;
        //オブジェクトの現在地Y
        int y;
        //オブジェクトの中心からの幅
        int hwidth;
        //オブジェクトの中心からの高さ
        int hheight;
        //現在のオブジェクトの状態
        ObjectState state;

        /***
         * デフォルトのコンストラクタ
         * Memo:引数がついてますね。このクラスには引数がついていないコンストラクタはありません。
         * このようなクラスは引数なしで生成することができません。
         * すなわち、かならず引数をつけないと生成できないので、hwidth, hheightは
         * かならず値が入っていると保証されます。
         * すなわち、プログラム内で保証がされているものはチェックをしなくてもよいというわけです。
         * 
         * @param w
         * @param h
         */
        GameObject(int w, int h) {
            hwidth = w / 2;
            hheight = h / 2;
            state = ObjectState.MOVING;
        }

        //メソッドにabstractをつけると抽象メソッドになります
        //抽象メソッドは中身を書かなくてもよいかわり、このクラスを継承した
        //クラスは必ずこのメソッドの中身を実装しなくてはなりません。
        //もし、実装しないとコンパイルエラーになります、そのためプログラマーは実装の必要性に気づくし
        //またなんのためにこのメソッドを実装しないといけないかという点で考えさせるきっかけになります
        //このようにクラスの設計はプログラムの手順を促す役目もあります
        public abstract ResultType move(long elapsedtime);
    }

    /***
     * ハチの実装
     * 
     * @author yokmama
     *
     */
    class Bee extends GameObject {
        //ハチとドロイド君がぶつかった時間
        long crashtime;

        /***
         * ハチのコンストラクタ
         * 
         * @param w
         * @param h
         */
        public Bee(int w, int h) {
            super(w, h);
            dx = 0;
            dy = -0.3f;
        }

        /***
         * ハチを動かす処理
         * 実は手をぬいてます、本来は遅いコンピュータのことも考えて経過時間から
         * どれだけ動いたか？を考えるべきなのですが。
         * 処理が難しくなるので、1/50秒でどのコンピュータも一回呼ばれるという前提で
         * 処理をしています。
         */
        public ResultType move(long elapsedtime) {
            //ハチとドロイド君が衝突をしている状態もありますので、
            //衝突中の場合と、そうでない場合で処理をわけています
            if (state != ObjectState.COLLISION) {
                //こちらは衝突しないで元気に飛んでいる、移動を少し進めて
                //もし画面外にでちゃったらDestroyを返すようにしています
                y += (dy*elapsedtime);
                if (y < 0) {
                    state = ObjectState.OUTOFSCREEN;
                    return ResultType.DESTROY;
                }
            } else {
                //衝突中の処理です、すぐ消えちゃうとわかりにくいので
                //爆発のアニメーション等もいれれるように衝突してからある一定時間（ここでは0.5秒）
                //そこに表示し続けるようにしています。
                //時間が経過したらDestoryを返しています。
                long span = System.nanoTime() - crashtime;
                if (span > 500000000) {
                    
                    return ResultType.DESTROY;
                }
            }
            return ResultType.FINE;
        }

        /***
         * 衝突設定
         */
        public void setCrash() {
            state = ObjectState.COLLISION;
            crashtime = System.nanoTime();
        }
    }

    /***
     * ドロイド君の実装
     * 
     * @author yokmama
     *
     */
    class Droid extends GameObject {
        /***
         * ドロイド君のコンストラクタ
         * 
         * @param w
         * @param h
         */
        public Droid(int w, int h) {
            super(w, h);
            dx = 0;
            dy = 0;
            state = ObjectState.MOVING;
        }

        /***
         * ドロイド君の移動処理
         * ドロイド君の移動処理では衝突チェックもしています。
         */
        public ResultType move(long elapsedtime) {
            //まずはドロイド君を移動
            x += (dx*elapsedtime);

            //ドロイド君の当たり判定用の箱を計算
            //中心座標から半径分の幅、高さを加えたA1,A2です
            int ax1 = x - hwidth;
            int ay1 = y - hheight;
            int ax2 = x + hwidth;
            int ay2 = y + hheight;

            //すべてのハチにたいして当たり判定を行います
            //こういう当たり判定処理は数が少ない場合は全部総当りで行ってもかまいませんが。
            //大規模になってくるととてもじゃないと総当りは無理です。そのため色々
            //工夫する仕組みもあります。ここでは手抜きしてます
            for (Bee obj : bees) {
                //ハチの当たり判定用の箱を計算
                //中心座標から半径分の幅、高さを加えたB1,B2です
                int bx1 = obj.x - obj.hwidth;
                int by1 = obj.y - obj.hheight;
                int bx2 = obj.x + obj.hwidth;
                int by2 = obj.y + obj.hheight;
                //箱同士で当たり判定を行います
                if (Utils.isCrash(ax1, ay1, ax2, ay2, bx1, by1, bx2, by2)) {
                    //当たっているなら衝突設定をハチに設定し自身は消滅する
                    obj.setCrash();
                    
                    return ResultType.CRASH;
                }
            }

            //ハチにあたってなければ、次は画面の外にでていないか判定し、
            //外にでていれば消滅するためDestoryを返す
            if (x < 0) {
                state = ObjectState.OUTOFSCREEN;
                return ResultType.DESTROY;
            }

            return ResultType.FINE;
        }

    }

    /***
     * ようやくゲーム世界のコンストラクタがでてきました
     * 基本的にインナークラスの宣言は上にまとめるか、下にまとめるかします。
     * 外部にみせる必要がない場合は下にまとめるケースが多いです。
     */
    public GameWorld() {
        droids = new ArrayList<Droid>();
        bees = new ArrayList<Bee>();
        initialized = false;
    }

    /***
     * ゲーム世界の処理
     * 全てのハチ、全てのドロイド君をそれぞれ個別にMoveを呼び出し、
     * Destoryが返却されたものを消滅するという処理をしています
     * そして、最後に新しいドロイド君の生成処理を行っています
     * @param elapsedtime
     */
    public void process(long elapsedtime) {
        //Memo:synchronizedは同期処理です、他の箇所にもこのような箇所があります。
        //この処理はその処理とここの処理が同時に実行されないようにするためのガードになっています。
        //Q:なぜ、同期処理が必要なの？
        synchronized (bees) {
            //削除予定のオブジェクトのリスト
            ArrayList<GameObject> destoryobj = new ArrayList<GameObject>();
            //全てのハチに対して処理を行っています
            //Q:削除はその都度しているのではなく、あとでまとめています
            //この理由はどうしてでしょう？
            for (Bee obj : bees) {
                if (obj.move(elapsedtime) == ResultType.DESTROY) {
                    destoryobj.add(obj);
                }
            }
            for (GameObject obj : destoryobj) {
                bees.remove(obj);
            }

            //全てのドロイド君に対して処理を行う
            destoryobj.clear();
            for (Droid obj : droids) {
                ResultType ret = obj.move(elapsedtime);
                if (ret == ResultType.DESTROY) {
                    //画面外にでた、あるいは消滅したので消す
                    destoryobj.add(obj);
                }
                else if(ret == ResultType.CRASH){
                    //得点をつける
                    score++;
                    //衝突音を鳴らし、消滅する
                    soundmgr.play(R.raw.explo2, 50);
                    destoryobj.add(obj);
                }
            }
            for (GameObject obj : destoryobj) {
                droids.remove(obj);
            }

            //新しいドロイド君の生成
            genDroid();
        }
    }

    /***
     * ゲーム画面を描画します
     * 
     * SurfaceViewが画面になります。画面には直接絵をかくことができません、まずは画面に書くための
     * Canvasを取得し、そのCanvasを使って絵をかきます。
     * 各オブジェクトは、中心座標をもっています、Bitmapをの描画はdrawBitmapで行われますが
     * 書かれるのは指定された座標を起点にして描画されるため、単純にオブジェクトの（X,Y)を使ってしまうと
     * 実際の当たり判定とは違うように描画されてします。それで座標を半径分だけずらしています。
     * こういう処理をオフセット処理といったり、いわなかったり。
     * たまに変数名がoffsetとかになってるのを見かけませんか？
     * 
     * @param surfaceview
     */
    public void drawView(SurfaceView surfaceview) {
        Canvas canvas = null;
        try {
            //SurfaceViewのCanvasを取得
            canvas = surfaceview.getHolder().lockCanvas();
            if (canvas != null) {
                //以前にかかれている絵を一度クリア
                canvas.drawColor(Color.BLACK);

                //全てのドロイド君を描画
                for (Droid obj : droids) {
                    int x = obj.x - obj.hwidth;
                    int y = obj.y - obj.hheight;
                    canvas.drawBitmap(droidImg, x, y, null);
                }

                //全てのハチを描画
                synchronized (bees) {
                    for (Bee obj : bees) {
                        int x = obj.x - obj.hwidth;
                        int y = obj.y - obj.hheight;
                        canvas.drawBitmap(beeImg, x, y, null);
                    }
                }
            }
        } finally {
            //取得したCanvasを開放します
            //こういう解放処理は開放し忘れが致命的な問題なるケースがあります。
            //そのため、finallyの中で実装するのがよいでしょう。
            //Q:finallyの特徴はなんでしょうか？
            if (canvas != null) {
                surfaceview.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    /***
     * ハチの生成
     * 指でタップした座標が引数にはいってきます。
     * その位置にハチを生成するもよし、そうでなくてもよし
     * 
     * @param w
     * @param h
     */
    public void genBee(int w, int h) {
        synchronized (bees) {
            if (bees.size() < 4) {
                Bee baroon = new Bee(beeImg.getWidth(),
                        beeImg.getHeight());
                //(X,Y)の位置にハチを生成しろっていわれたけど、Yの座標は一番したで固定がいいので引数を無視してます
                baroon.x = w;
                baroon.y = height;
                bees.add(baroon);
                
                //発射音を鳴らす
                soundmgr.play(R.raw.gun01, 50);
            }
        }
    }
    
    /***
     * 得点を返却
     * 
     * @return
     */
    public long getScore(){
        return score;
    }

    /***
     * ドロイド君の生成
     * 生成する高さをランダムにしています。が、あんまり下すぎるとアレなんで
     * だいたい、画面の3/4以内の位置におさまるようにしています
     */
    public void genDroid() {
        if (droids.size() < 10) {
            //こういう？で書く書き方ってわかりにくいですか？
            //すみません、わかりにくいですね。このメリットif文で３，４行かけて書くよりも
            //行数を減らすことができるために重宝します。ですが、あまりよい書き方ではありません、
            //とはいっても、他人が書いたものも時として読めなければならないので慣れておくのも必要です
            //ちなみに　hoge ? A : hogehoge? : B ? hogehoge... と多段になるのはいくらなんでもやめましょう。
            //とても見にくいです。
            int si = droids.size() == 0 ? 7 : rand.nextInt(64);
            //サイコロの目が７の時だけ処理をする（1/64の確率）
            if (si == 7) {
                int sy = (height / 4) * 3;
                //速度もランダムで生成(0ではじまるので１を足してます）
                int speed = rand.nextInt(2) + 1;
                Droid bird = new Droid(droidImg.getWidth(), droidImg.getHeight());
                bird.dx = speed * -0.1f;
                bird.x = width;
                bird.y = rand.nextInt(sy);
                droids.add(bird);
            }
        }
    }

    /***
     * ゲーム世界の初期化
     * 
     * @param surfaceview
     */
    public void init(SurfaceView surfaceview) {
        width = surfaceview.getWidth();
        height = surfaceview.getHeight();
        //画面の幅、高さが０になる場合があります。
        //まだ画面が表示されていないからでしょう。
        //その場合はキャンセルして次回に持越します
        if (width > 0 && height > 0) {
            rand = new Random(System.currentTimeMillis());
            droidImg = BitmapFactory.decodeResource(surfaceview.getResources(),
                    R.drawable.bird);
            beeImg = BitmapFactory.decodeResource(
                    surfaceview.getResources(), R.drawable.baroon);
            soundmgr = new SoundManager();
            soundmgr.init(surfaceview.getContext());
            score = 0;
            initialized = true;
        }
    }

    /***
     * ゲーム世界が初期化されているならTrue
     * 
     * @return
     */
    public boolean isInit() {
        return initialized;
    }

    /***
     * ゲーム世界で利用されている画像を開放します
     * Initで生成されたものを開放するのが目的です
     * 
     */
    public void release() {
        if (droidImg != null) {
            droidImg.recycle();
            droidImg = null;
        }
        if (beeImg != null) {
            beeImg.recycle();
            beeImg = null;
        }
        if(soundmgr!=null){
            soundmgr.release();
        }
        initialized = false;
    }

}
