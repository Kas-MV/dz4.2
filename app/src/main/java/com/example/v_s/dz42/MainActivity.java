package com.example.v_s.dz42;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private List<String> getGoogleReposName() throws RuntimeException {
        final List<String> name = new ArrayList<>();
        URL url = null;
        try {
            url  = new URL("https://api.github.com  /user/google/repos");
        }catch (final MalformedURLException e){
            throw new RuntimeException(e);
        }
        final HttpURLConnection urlConnection;
        final StringBuilder sb =new StringBuilder();
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            do {
                line  = reader.readLine();
                sb.append(line);
            }while (line !=null);
        }catch (final IOException e){
            throw new RuntimeException(e);
        }
        urlConnection.disconnect();
        List<String> names = null;
        try{
            final JSONArray reposJsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < reposJsonArray.length(); i++) {
                names.add(reposJsonArray.getJSONObject(i).getString("name"));
            }
        }catch (final JSONException e){
            throw new RuntimeException(e);
        }
        return  names;
    }

    private static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
        private final LayoutInflater mInflater;
        private String [] mData;

        public ListAdapter(final Context context){
            mInflater = LayoutInflater.from(context);
        }
        public  void setData (final String [] data){
            mData = data;
        }

        /*public class ViewHolder {
            public BreakIterator titleTextView;

            public ViewHolder(View view) { 

            }
        }*/

        @Override
        public ViewHolder onCreateViewHolder (final ViewGroup parent, final int viewType){
            final View view = mInflater.inflate(R.layout.view_list_item, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position){
            holder.titleTextView.setText(mData[position]);
        }
        @Override
        public int getItemCount(){
            return  mData == null ? 0 : mData.length;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView titleTextView;
            public ViewHolder(final View itemView){
                super(itemView);
                titleTextView = itemView.findViewById(R.id.view_title);
            }
        }
    }
}
