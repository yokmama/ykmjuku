package ykmjuku.hungout.android.app1.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ykmjuku.hungout.android.app1.bean.Area;
import ykmjuku.hungout.android.app1.bean.Geo;
import ykmjuku.hungout.android.app1.bean.Info;

import android.test.AndroidTestCase;
import android.util.Log;

public class XmlPullParserTest extends AndroidTestCase {
    private static final String TAG = "Test";

    public void testParse(){
        System.out.println("this is system.out");
        Log.d(TAG, "this is test");
        
        InputStream in = null;
        try {
            URLConnection con = new URL("http://www.drk7.jp/weather/xml/28.xml").openConnection();
            
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            
            in = con.getInputStream();
            parser.setInput(in, "UTF-8");
            List<Area> areas = new ArrayList<Area>();
            Area area = null;
            int type = parser.getEventType();
            while(type != XmlPullParser.END_DOCUMENT){
                if(type == XmlPullParser.START_TAG){
                    String tag = parser.getName();
                    if(tag.equals("area")){
                        area = new Area();
                    }
                    else if(area != null){
                        if(tag.equals("geo")){
                            area.geo = new Geo();
                        }
                        if(tag.equals("long")){
                            area.geo.lng = Double.parseDouble(parser.nextText());
                        }
                        if(tag.equals("lat")){
                            area.geo.lat = Double.parseDouble(parser.nextText());
                        }
                    }
                }
                else if(type == XmlPullParser.END_TAG){
                    String tag = parser.getName();
                    if(tag.equals("area")){
                        areas.add(area);
                    }
                }
                type = parser.next();
            }
            
            for(Area o : areas){
                Log.d(TAG, "get="+o.geo.lat+","+o.geo.lng);
            }
            
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
