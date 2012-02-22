package ykmjuku.hungout.android.app1;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyListFragment extends ListFragment implements LoaderCallbacks<Cursor>{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);
        
        getLoaderManager().initLoader(R.layout.list, null, this);
        
        return view;
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        
        return new CursorLoader(
                getActivity(), 
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        View empty = getListView().getEmptyView();
        if(empty!=null){
            empty.setVisibility(View.GONE);
        }
        
        getListView().setAdapter(new MyCursorAdapter(getActivity(), arg1));
    }

    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        
    }
    
    class MyCursorAdapter extends CursorAdapter{

        public MyCursorAdapter(Context context, Cursor c) {
            super(context, c);
        }
        
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            
            if(position%2 == 0){
                view.setBackgroundColor(Color.GRAY);
            }
            else{
                view.setBackgroundColor(Color.BLACK);
            }
            
            return view;
        }



        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder)view.getTag();
            
            holder.text1.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            holder.text2.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.row, null, false);
            
            ViewHolder holder = new ViewHolder();
            holder.text1 = (TextView)view.findViewById(R.id.textView1);
            holder.text2 = (TextView)view.findViewById(R.id.textView2);
            view.setTag(holder);
            
            return view;
        }
        
        class ViewHolder {
            TextView text1;
            TextView text2;
        }
        
    }

}
