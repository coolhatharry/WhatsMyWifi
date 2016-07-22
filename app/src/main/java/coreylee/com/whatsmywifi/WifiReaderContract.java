package coreylee.com.whatsmywifi;

import android.provider.BaseColumns;

/**
 * Can be used in other classes to obtain SQL database information.
 * 
 * Created by coreylee on 16-07-14.
 */
public final class WifiReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public WifiReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class WifiEntry implements BaseColumns {
        public static final String TABLE_NAME = "wifitable";
        public static final String COLUMN_NAME_WIFI_NAME = "wifiname";
        public static final String COLUMN_NAME_WIFI_PASSWORD = "wifipassword";

    }


}
