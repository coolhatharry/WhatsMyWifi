package coreylee.com.whatsmywifi;

/**
 * Created by coreylee on 16-07-13.
 */
public class Wifi {

    private String wifiName;
    private String wifiPassword;

    public Wifi() {

    }

    public Wifi(String wifiName, String wifiPassword) {
        this.wifiName = wifiName;
        this.wifiPassword = wifiPassword;
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

}


