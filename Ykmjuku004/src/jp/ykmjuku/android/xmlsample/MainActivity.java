package jp.ykmjuku.android.xmlsample;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
    final String TAG = "XMLSample";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //画面に表示するTextViewを取得
        TextView textview1 = (TextView)findViewById(R.id.textview1);

        //TextViewに設定する文字列の入れ物を生成
        StringBuilder buf = new StringBuilder();
        
        try {
            //assetの下にあるsample.xmlのStreamから　Document　のインスタンスを取得
            //DocumentはXMLのデータ構造を内部でもっているオブジェクトです。これは別名DOMオブジェクトともいいます。
            //XMLの構造をもっているので、配列としてXML内のタグを取り出したり、値を設定することができます。
            Document doc = getDocument(getResources().getAssets().open("sample.xml"));
            
            //XML内に含まれるpoemというタグを全部取り出します
            NodeList nodelist = doc.getElementsByTagName("poem");
            
            for(int i=0; i<nodelist.getLength(); i++){
                //とりだしたpoemをFor文でぐるぐるまわして１つづつとりだしています
                Node poem = nodelist.item(i);
                
                //poemの下の階層のタグを全部取り出しています
                NodeList childs = poem.getChildNodes();
                for(int j=0; j<childs.getLength(); j++){
                    //poemの下の階層のタグをひとつずつチェックし mkana というタグがあったら画面に表示する文字列に追加します
                    Node child = childs.item(j);
                    if(child.getNodeName().equals("mkana")){
                        buf.append(child.getFirstChild().getNodeValue()).append("\n");
                    }
                }
            }
            
        } catch (IOException e) {
            Log.d(TAG, "I/O exeption: ", e);
        }
        finally{
            //画面に表示するTextViewに作成された文字列を設定
            textview1.setText(buf.toString());
        }
    }

    /***
     * StreamからDocumentを生成
     * @param is
     * @return
     */
    public Document getDocument(InputStream is) {
        Document doc = null;
        //XMLのパーサー（解析をするソフト）は世の中に色々あります。理由はXMLがオープンソースな仕様であったため
        //一社が独占してそれを作る状況じゃなかったからです。
        //一時期そのため様々な解析ソフトがあったのですが、アプリケーションを作る側にとってそれぞにあわせた初期化や
        //使い方をするのは面倒でした、そこでW3Cは使う部分や初期化の部分について統一していこうといううごきになり
        //次のDocumentBuilderFactoryという生成するためのクラスを準備することで、使う側にどのようなパーサーが
        //あっても問題ないようにしました。こういうのをオブジェクト指向では抽象化というのですが、まさにこれがそうです。
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            //DocumentBuilderFactoryにnewDocumentBuilderといえば、そのシステムで最適なXMLBuilderが
            //返却されます。そして、それをつかってパースすることで、XMLDOMオブジェクト（Document）が作成されます。
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(is);
            return doc;
        } catch (ParserConfigurationException e) {
            Log.d(TAG, "XML parse error: ", e);
            return null;
        } catch (SAXException e) {
            Log.d(TAG, "Wrong XML file structure: ", e);
            return null;
        } catch (IOException e) {
            Log.d(TAG, "I/O exeption: ", e);
            return null;
        }
    }

}