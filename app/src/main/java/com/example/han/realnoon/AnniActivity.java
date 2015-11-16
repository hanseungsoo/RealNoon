package com.example.han.realnoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by kku on 2015-11-17.
 */
public class AnniActivity extends FragmentActivity implements AdapterView.OnItemClickListener {
    ListView listview;
    ArrayAdapter<String> adapter;
    ArrayList<Anni> Annis;
    ArrayList<String> names;
    Button btn;

    @Override
    protected void onResume() {
        super.onResume();
        DBHandler dh = DBHandler.open(MainActivity.mContext);
        Annis = dh.selectAnni();
        if(Annis ==null)
        {}
        else {
            for (int i=0;i<Annis.size();i++){
                names.add(Annis.get(i).getSubject());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anni);
        DBHandler dh = DBHandler.open(MainActivity.mContext);
        names = new ArrayList<>();
        Annis = dh.selectAnni();
        if(Annis ==null)
            names.add("기념일을 입력해주세요.");
        else{
            for (int i=0;i<Annis.size();i++){
                names.add(Annis.get(i).getSubject());
            }
        }
        listview = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);
        adapter.notifyDataSetChanged();
        // setAdapter �� �̿��ؼ� monthsListView �� ����� ����
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        btn = (Button)findViewById(R.id.addAnniButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnniActivity.this,AnniEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
