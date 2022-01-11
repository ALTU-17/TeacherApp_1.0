package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import in.aceventura.evolvuschool.teacherapp.pojo.BaseColumn;
import in.aceventura.evolvuschool.teacherapp.pojo.NoticePojo;

public class DBConnection {
    static SQLiteDatabase database;
    static SQLDB sql;
    public Context context;
    int registrationStatus = 0;
    boolean status = false;
    ContentValues values;
    Long l;
    Cursor cursor;
    int i = 0;
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "farmcon.db";
    public static final String Reporttable = "Reporttable";


    private static final String CreateReporttable = "create table if not exists " + Reporttable
            + "(" + BaseColumn.AddReport.noticeid + " integer primary key ,"
            + BaseColumn.AddReport.subject + " text );";

    public DBConnection(Context context) {
        this.context = context;
        sql = new SQLDB(context);
        openReadable();


    }

    public DBConnection openReadable() throws SQLException {
        sql = new SQLDB(context);
        database = sql.getReadableDatabase();
        return this;
    }

    public void closeDatabase() {
        sql.close();
    }

    public long insertReport(NoticePojo reportPojo) {
        openReadable();
        long i = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(BaseColumn.AddReport.noticeid, reportPojo.getNotice_id());
        contentValues.put(BaseColumn.AddReport.subject, reportPojo.getSubject());

        i = database.insert(Reporttable, null, contentValues);

        closeDatabase();
        return i;
    }

    class SQLDB extends SQLiteOpenHelper {


        public SQLDB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }



        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateReporttable);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Reporttable);

        }
    }

}




