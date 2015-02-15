package com.lee.lastpass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.lee.lastpass.types.Data;
import com.lee.lastpass.util.CSVFileUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    public CSVFileUtil csvFileUtil;
    public ListView listView;
    public static Data data;
    private ArrayList<Data> fullData;
    private ArrayList<Data> searchFullData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);
        try {
            csvFileUtil = new CSVFileUtil("/sdcard/data.csv");
        } catch (Exception e){
            Log.i("readfile", e.toString());
        }

        try{
            fullData = csvFileUtil.getFullData();
            ArrayAdapter<Data> adapter = new ArrayAdapter<Data>(this,
                    android.R.layout.simple_list_item_1, (List)fullData);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Log.i("readfile", e.toString());
        }
        handleIntent(getIntent());
        listView.setOnItemClickListener(mMessageClickedHandler);
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("query",query);
            Toast.makeText(getApplicationContext(),query, Toast.LENGTH_SHORT);
            searchFullData = csvFileUtil.getFullData(query);
            ArrayAdapter<Data> adapter = new ArrayAdapter<Data>(this,
                    android.R.layout.simple_list_item_1, (List)searchFullData);
            listView.setAdapter(adapter);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Do something in response to the click
            data = (Data)parent.getItemAtPosition(position);
            dialogFragment.show(getFragmentManager(), "tag");
        }
    };

    public DialogFragment dialogFragment = new OperatorDialogFragment();

    public  static class OperatorDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("操作")
                    .setItems(R.array.operator , new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            ClipboardManager clipboard = (ClipboardManager)
                                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip;
                            switch (which) {
                                case 0:
                                    clip = ClipData.newPlainText("username", data.getUserName());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(getActivity().getApplicationContext(),"复制用户名成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    clip = ClipData.newPlainText("password", data.getPassword());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(getActivity().getApplicationContext(),"复制密码成功", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
            return builder.create();
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {

        return super.onOptionsItemSelected(item);
    }
}
