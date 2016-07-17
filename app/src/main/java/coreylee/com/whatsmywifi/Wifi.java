package coreylee.com.whatsmywifi;

/**
 * Created by coreylee on 16-07-13.
 */
public class Wifi {

    private String wifiName;
    private String wifiPassword;
    private WifiType wifiType;

    public Wifi() {

    }

    public Wifi(String wifiName, String wifiPassword, WifiType wifiType) {
        this.wifiName = wifiName;
        this.wifiPassword = wifiPassword;
        this.wifiType = wifiType;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public WifiType getWifiType() {
        return wifiType;
    }

    public void setWifiType(WifiType wifiType) {
        this.wifiType = wifiType;
    }

}


