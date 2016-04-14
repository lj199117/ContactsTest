package com.example.sqlUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table book ("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "price real, "
            + "pages integer, "
            + "name text)";
    public static final String CREATE_CATEGORY = "create table Category ("
            + "id integer primary key autoincrement, "
            + "category_name text, "
            + "category_code integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context,
                            String name,
                            SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    //当表不存在的时候调用getReadableDatabase()或getWritableDatabase()方法会得到执行
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "onCreate succeeded", Toast.LENGTH_SHORT).show();
    }

    //之前我们传入的是1，现在只要传入一个比1 大的数，就可以让onUpgrade()方法得到执行了
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            //请注意一个非常重要的细节，switch 中每一个case 的最后都是没有使用break 的
            case 1:
                db.execSQL(CREATE_CATEGORY);
            case 2:
                db.execSQL("alter table Book add column category_id integer");
            default:
        }
        Toast.makeText(mContext, "onUpgrade succeeded", Toast.LENGTH_SHORT).show();
    }
}
