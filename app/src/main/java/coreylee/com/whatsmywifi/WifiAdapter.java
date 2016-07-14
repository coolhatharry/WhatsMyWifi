package coreylee.com.whatsmywifi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by coreylee on 16-07-13.
 */
public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyViewHolder> {

    private List<Wifi> wifiList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView wifiName;
        public TextView wifiPassword;
        public TextView wifiType;

        public MyViewHolder(View view) {
            super(view);

            wifiName = (TextView) view.findViewById(R.id.wifi_list_name);
            wifiPassword = (TextView) view.findViewById(R.id.wifi_list_password);
            wifiType = (TextView) view.findViewById(R.id.wifi_list_type);
        }
    }


    public WifiAdapter(List<Wifi> wifiList) {
        this.wifiList = wifiList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wifi_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Wifi wifi = wifiList.get(position);

        holder.wifiName.setText(wifi.getWifiName());
        holder.wifiPassword.setText(wifi.getWifiPassword());
        holder.wifiType.setText(wifi.getWifiType().toString());
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }
}
