package com.example.applicationtest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    DBController dBmain;
    SQLiteDatabase sqLiteDatabase;
    String[] fname, lname;
    int[] id;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dBmain = new DBController(this);
        findid();
        displaydata();

    }

    private void displaydata() {
        sqLiteDatabase = dBmain.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select *from person", null);
        if (cursor.getCount() > 0) {
            id = new int[cursor.getCount()];
            fname = new String[cursor.getCount()];
            lname = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext()) {
                id[i] = cursor.getInt(0);
                fname[i] = cursor.getString(1);
                lname[i] = cursor.getString(2);
                i++;
            }
            CustAdapter custAdapter = new CustAdapter();
            lv.setAdapter(custAdapter);
        }
    }

    private void findid() {
        lv = (ListView) findViewById(R.id.lv);
    }

    private class CustAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return fname.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView txtfname, txtlname;
            ImageButton txt_edit, txt_delete;
            CardView cardview;
            convertView = LayoutInflater.from(MainActivity2.this).inflate(R.layout.singledata, parent, false);
            txtfname = convertView.findViewById(R.id.txt_fname);
            txtlname = convertView.findViewById(R.id.txt_lname);
            txt_edit = convertView.findViewById(R.id.txt_edti);
            txt_delete = convertView.findViewById(R.id.txt_delete);
            cardview = convertView.findViewById(R.id.cardview);
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //background random color
                    Random random = new Random();
                    cardview.setCardBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                    txt_delete.setVisibility(View.VISIBLE);
                    txt_edit.setVisibility(View.VISIBLE);
                    txtfname.setVisibility(View.GONE);
                    txtlname.setVisibility(View.GONE);
                }
            });
            txtfname.setText(fname[position]);
            txtlname.setText(lname[position]);
            txt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id[position]);
                    bundle.putString("fname", fname[position]);
                    bundle.putString("lname", lname[position]);
                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    intent.putExtra("studata", bundle);
                    startActivity(intent);
                }
            });
            txt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqLiteDatabase = dBmain.getReadableDatabase();
                    long recremove = sqLiteDatabase.delete("person", "id=" + id[position], null);
                    if (recremove != -1) {
                        Toast.makeText(MainActivity2.this, "successfully delete", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity2.this, MainActivity.class));
                        displaydata();
                    }
                }
            });
            return convertView;
        }
    }
}