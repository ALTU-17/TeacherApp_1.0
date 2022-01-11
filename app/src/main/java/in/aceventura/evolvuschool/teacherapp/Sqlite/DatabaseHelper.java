package in.aceventura.evolvuschool.teacherapp.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "shortname_table";
    private static final String COL_1 = "ID";
    private static final String COL_2  = "shortname";
    private static final String COL_3  = "url";
    private static final String COL_4  = "tUrl";
    private static final String COL_5  = "pUrl";
    private ContentValues contentValues = new ContentValues();


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    //Creating the Database...
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT,"
                + COL_5 + " TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE EXISTS" + TABLE_NAME);
        onCreate(db);

    }



    public boolean addDetails(String sName, String url, String dUrl, String pUrl){
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues.put(COL_2,sName);
        contentValues.put(COL_3,url);
        contentValues.put(COL_4,dUrl);
        contentValues.put(COL_5,pUrl);

        long resultSN = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        //if data is inserted incorrectly  it will return -1
        return resultSN != -1;
    }

    //Retrieving shortname from the Database...
    public String getName(long id) {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME, null, whereclause, whereargs, null, null, null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            if (csr!=null){
                csr.close();
                db.close();
            }
        }
        return rv;
    }

    //Retrieving url from the Database...
    public String getURL(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_3));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
                db.close();
            }
        }
        return rv;
    }

    //Retrieving tUrl from the Database...
    public String getTURL(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_4));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
                db.close();
            }
        }
        return rv;
    }

    //Retrieving pUrl from the Database...
    public String getPURL(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_5));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
                db.close();
            }
        }
        return rv;
    }


public void clearData(){
        /*String clear = "DELETE FROM " + TABLE_NAME;
        String clear = "DROP TABLE " + TABLE_NAME;*/
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL(clear);
    db.delete(TABLE_NAME, null, null);

}

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }
}


