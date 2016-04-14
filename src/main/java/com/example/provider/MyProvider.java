package com.example.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.sqlUtil.MyDatabaseHelper;

/**
 * Created by Administrator on 2016/4/14 0014.
 */
public class MyProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    public static final String AUTHORITY = "com.example.provider";

    private static UriMatcher uriMatcher;
    private SQLiteOpenHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /*addURI()方法，这个方法接收三个参数，可以分别把权限、路径和一个自定义代码传进去*/
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.provider.category";
            default:
                break;
        }
        return null;
    }

    /*初始化内容提供器的时候调用。通常会在这里完成对数据库的创建和升级等操作;
    注意，只有当存在ContentResolver(Context 调用getContentResolver()) 尝试访问我们程序中的数据时，
    内容提供器才会被初始化
    */
    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(),"BookStore.db", null, 2);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
// 查询book表中的所有数据
                cursor = db.rawQuery("select * from Book",null);
                break;
            case BOOK_ITEM:
// 查询book表中的单条数据
                /*
                getPathSegments()方法，它会将内容URI 权限之后的部分以“/”符号进行分割，并把分割后
                的结果放入到一个字符串列表中，那这个列表的第0 个位置存放的就是路径，第1 个位置存
                放的就是id 了*/
                String bookId = uri.getPathSegments().get(1);
                cursor = db.rawQuery("select * from Book where id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
// 查询category表中的所有数据
                cursor = db.rawQuery("select * from Category",null);
                break;
            case CATEGORY_ITEM:
// 查询category表中的单条数据
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.rawQuery("select * from Category where id=?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri uriReturn = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" +
                        newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" +
                        newCategoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows = db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Book", "id = ?", new String[] { bookId });
                break;
            case CATEGORY_DIR:
                deletedRows = db.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Category", "id = ?", new String[]
                        { categoryId });
                break;
            default:
                break;
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updatedRows = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = db.update("Book", values, "id = ?", new String[]
                        { bookId });
                break;
            case CATEGORY_DIR:
                updatedRows = db.update("Category", values, selection,
                        selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = db.update("Category", values, "id = ?", new String[]
                        { categoryId });
                break;
            default:
                break;
        }
        return updatedRows;
    }
}
