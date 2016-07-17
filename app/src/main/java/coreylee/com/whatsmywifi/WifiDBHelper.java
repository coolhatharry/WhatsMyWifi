package coreylee.com.whatsmywifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coreylee on 16-07-14.
 */
public class WifiDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WifiData.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WifiReaderContract.WifiEntry.TABLE_NAME + "(" +
                    WifiReaderContract.WifiEntry._ID + " INTEGER PRIMARY KEY, " +
                    WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_NAME + TEXT_TYPE + COMMA_SEP +
                    WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_TYPE + TEXT_TYPE + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WifiReaderContract.WifiEntry.TABLE_NAME;


    public WifiDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }

    public boolean insertWifi(Wifi wifi) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_NAME, wifi.getWifiName());
        contentValues.put(WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_PASSWORD, wifi.getWifiPassword());
        contentValues.put(WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_TYPE, wifi.getWifiType().toString());

        sqLiteDatabase.insert(WifiReaderContract.WifiEntry.TABLE_NAME, null, contentValues);

        return true;
    }

    public boolean updateWifi(Integer id, Wifi wifi) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_NAME, wifi.getWifiName());
        contentValues.put(WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_PASSWORD, wifi.getWifiPassword());
        contentValues.put(WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_TYPE, wifi.getWifiType().toString());

        sqLiteDatabase.update(WifiReaderContract.WifiEntry.TABLE_NAME,
                contentValues,
                WifiReaderContract.WifiEntry._ID + " = ? ",
                new String[]{Integer.toString(id)});

        return true;
    }

    public Integer deleteWifi(Integer id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(WifiReaderContract.WifiEntry.TABLE_NAME,
                WifiReaderContract.WifiEntry._ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    public Cursor getWifi(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + WifiReaderContract.WifiEntry.TABLE_NAME +
                        " WHERE " + WifiReaderContract.WifiEntry._ID + "=?",
                new String[]{Integer.toString(id)});
    }

    public Cursor getAllWifis() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + WifiReaderContract.WifiEntry.TABLE_NAME,
                null);
    }

    public int getNumberOfRows() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, WifiReaderContract.WifiEntry.TABLE_NAME);
    }


}
