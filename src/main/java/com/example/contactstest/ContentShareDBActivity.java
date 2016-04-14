package com.example.contactstest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2016/4/14 0014.
 */
public class ContentShareDBActivity extends Activity {

    private String newId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contet_sharedb_layout);

        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 添加一条数据,并得到插入数据的id
                Uri uri = Uri.parse("content://com.example.provider/book");
                ContentValues values = new ContentValues();
                values.put("name", "A Clash of Kings");
                values.put("author", "George Martin");
                values.put("pages", 1040);
                values.put("price", 22.85);
                Uri newUri = getContentResolver().insert(uri, values);
                /*注意insert()方法会返回一个Uri 对象，这个对象中包含了新增数据的id，我们通过
getPathSegments()方法将这个id 取出，稍后会用到它*/
                newId = newUri.getPathSegments().get(1);
            }
        });
        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 查询所有数据
                Uri uri = Uri.parse("content://com.example.provider/book");
                /*getContentResolver() 会从配置好的AndroidManifest.xml中拿到自定义的内容提供者*/
                Cursor cursor = getContentResolver().query(uri, null, null,
                                null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.
                                getColumnIndex("name"));
                        String author = cursor.getString(cursor.
                                getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex
                                ("pages"));
                        double price = cursor.getDouble(cursor.
                                getColumnIndex("price"));
                        Log.d("MainActivity", "book name is " + name);
                        Log.d("MainActivity", "book author is " + author);
                        Log.d("MainActivity", "book pages is " + pages);
                        Log.d("MainActivity", "book price is " + price);
                    }
                    cursor.close();
                }
            }
        });
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 更新某条数据
                Uri uri = Uri.parse("content://com.example.provider/book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name", "A Storm of Swords");
                values.put("pages", 1216);
                values.put("price", 24.05);
                getContentResolver().update(uri, values, null, null);
            }
        });
        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 删除某条数据
                Uri uri = Uri.parse("content://com.example.provider/book/" + newId);
                getContentResolver().delete(uri, null, null);
            }
        });
    }
}
