package jp.co.kayo.android.flydroid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/***
 * 効果音の再生を行う
 * 効果音はあらかじめロードしておいてゲーム中はそれを使い回しします
 * 
 * @author yokmama
 *
 */
public class SoundManager {
    //利用する効果音を列挙する
    private static final int[] SoundList = {R.raw.gun01, R.raw.explo2};
    //効果音を鳴らすクラス
    private SoundPool mSoundPool;
    //ロードされたときに取得される効果音のIDとリソースのIDの紐付け
    private HashMap<Integer, Integer> sounds = new HashMap<Integer, Integer>();
    
    /***
     * 効果音の読み込み
     * 
     * @param context
     */
    public void init(Context context){
        //SoundPoolの初期化
        mSoundPool = new SoundPool(SoundList.length, AudioManager.STREAM_MUSIC, 0);
        //SoundPoolを使って効果音をロードし、戻り値のIDと効果音のリソースIDをペアにしてMapに登録
        for(int i=0; i<SoundList.length; i++){
            sounds.put(SoundList[i], mSoundPool.load(context, SoundList[i], 1));
        }
    }
    
    /***
     * 効果音の解放
     * 
     */
    public void release(){
        //読み込まれていた効果音を解放します
        Iterator<Entry<Integer, Integer>> ite = sounds.entrySet().iterator();
        for(;ite.hasNext();){
            Entry<Integer, Integer> entry = ite.next();
            mSoundPool.unload(entry.getValue());
        }
        mSoundPool.release();
    }
    
    /***
     * 効果音の再生
     * Rawの下に置いてあるリソースIDの名前で再生をします
     * 
     * @param res
     * @param vol
     */
    public void play(int res, int vol){
        //効果音の再生を再生する、ここではVolを左右同じにして再生しています。
        //引数１:鳴らす効果音のID
        //引数２:左のボリューム
        //引数３:右のボリューム
        //引数４　優先順位０が一番高い
        //引数５　ループ回数（-1の場合は無限にループ、0の場合はループしない）
        //引数６　再生速度（0.5〜2.0：1.0で通常の速度）
        mSoundPool.play(sounds.get(res), vol, vol, 0, 0, 1.0f);
    }
}
