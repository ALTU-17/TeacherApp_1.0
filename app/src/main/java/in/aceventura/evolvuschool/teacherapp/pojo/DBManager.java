package in.aceventura.evolvuschool.teacherapp.pojo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by preeti on 16/8/18.
 */

public class DBManager extends SQLiteOpenHelper {
    private Context mcontext;
    private int DATABASE_VERSION=1;
    private String DATABASE_NAME="UserData.db";
    public static final String AddNoticeTable = "AddNoticeTable";
  //  private String CREATE_TABLE_STUDENTS= " CREATE TABLE " + TABLE_USER_DATA + "(" + COLUMN_USER_MOBILENO + " TEXT)";

    private static final String CreateAddNoticeTable = "create table if not exists " + AddNoticeTable
            + "(" + BaseColumn.addStudentBasecolumn.id + " integer primary key ,"

            + BaseColumn.addStudentBasecolumn.subject + " text ,"
            + BaseColumn.addStudentBasecolumn.noticedate + " text ,"
            + BaseColumn.addStudentBasecolumn.createdby + " text );";


    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mcontext=context;
        this.DATABASE_NAME=name;
        this.DATABASE_VERSION=version;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CreateAddNoticeTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL(CreateAddStudentTable);
        onCreate(db);*/
    }
   /* public void clearDatabase(String TABLE_NAME) {
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }*/
    public void deletetable(){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();

        String query=null;
        Cursor cursor = null;

        query="delete from "+ AddNoticeTable ;
        cursor = sqLiteDatabase.rawQuery(query, null);

    }
    public long insertStudent(NoticePojo addStudentBean) {
        long i = 0;
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BaseColumn.addStudentBasecolumn.subject, addStudentBean.getSubject());
        contentValues.put(BaseColumn.addStudentBasecolumn.createdby, addStudentBean.getName());
        contentValues.put(BaseColumn.addStudentBasecolumn.id, addStudentBean.getNotice_id());
        contentValues.put(BaseColumn.addStudentBasecolumn.noticedate, addStudentBean.getNotice_date());

     i = sqLiteDatabase.insert(AddNoticeTable, null, contentValues);

          /*  if (ismobNoPresent(addStudentBean.getMobile_No()))
            {
                i = sqLiteDatabase.update(AddStudentTable, contentValues, "Mobile_No=?", new String[]{addStudentBean.getMobile_No()});
            }

        else
    {
        i = sqLiteDatabase.insert(AddStudentTable, null, contentValues);
    }*/
           return i;
    }

   /* public long update(AddStudentBean addStudentBean){
        long i = 0;
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BaseColumn.addStudentBasecolumn.MobileNo, addStudentBean.getMobile_No());
        contentValues.put(BaseColumn.addStudentBasecolumn.Otp, addStudentBean.getOTP());
        contentValues.put(BaseColumn.addStudentBasecolumn.isSync, addStudentBean.getIsSync());
        contentValues.put(BaseColumn.addStudentBasecolumn.Int_ID, addStudentBean.getInt_Id());
        i = sqLiteDatabase.update(AddStudentTable, contentValues, "Mobile_No=?", new String[]{addStudentBean.getMobile_No()});

        return i;
    }

    public boolean ismobNoPresent(String mobno) {
        boolean b = false;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = null;
        String query = null;
        try {
            query = "select *  from " + AddStudentTable + " where Mobile_No='"+mobno+"'";
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                {
                    b = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return b;
    }*/
    public ArrayList<AddStudentBean> getAllMobileNo() {
        ArrayList<AddStudentBean> list=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor = null;
        String mobno=null;
        String query = null;
        try {
            query = "select * from " + AddNoticeTable;
            cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                {

                        do {
                            AddStudentBean contact = new AddStudentBean();
                            //contact.setMobileNo(Integer.parseInt(cursor.getString(0)));
                            contact.setNoticedate(cursor.getString(cursor.getColumnIndex(BaseColumn.addStudentBasecolumn.noticedate)));
                            contact.setCreatedby(cursor.getString(cursor.getColumnIndex(BaseColumn.addStudentBasecolumn.createdby)));
                            contact.setId(cursor.getString(cursor.getColumnIndex(BaseColumn.addStudentBasecolumn.id)));
                            contact.setSubject(cursor.getString(cursor.getColumnIndex(BaseColumn.addStudentBasecolumn.subject)));

                            // Adding contact to list
                            list.add(contact);
                        } while (cursor.moveToNext());

                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }

  /*  public boolean delete() {
        boolean b = false;

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor = null;
        String mobno=null;
        String query = null;
        query = "DELETE from " + AddStudentTable + " where Mobile_No='"+"9848464644"+"'";
        cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            {
                b = true;
            }

        }
        return b;
    }*/
}
