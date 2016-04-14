package com.example.contactstest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.adapter.MyListAdapter;
import com.example.pojo.Contacts;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ListView listView = null;
    Button button = null;

    List<Contacts> contactsList = new ArrayList<Contacts>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(new MyListAdapter(MainActivity.this,R.layout.item_layout,contactsList));

        readContacts();

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ContentShareDBActivity.class);
                startActivity(intent);
            }
        });
    }

    private void readContacts() {
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                String displayName = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                // 获取联系人手机号
                String number = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(new Contacts(displayName,number));
            }while(cursor.moveToNext());
        }
        cursor.close();

    }

}
