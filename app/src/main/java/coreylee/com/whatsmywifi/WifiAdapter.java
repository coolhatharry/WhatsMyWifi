package coreylee.com.whatsmywifi;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * TODO
 * Created by coreylee on 16-07-13.
 */
public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyViewHolder> {

    private CursorAdapter mCursorAdapter;

    private Context mContext;

    private Cursor mCursor;


    public WifiAdapter(Context context, Cursor cursor) {

        mContext = context;

        mCursor = cursor;

        // Wrap CursorAdapter
        mCursorAdapter = new CursorAdapter(mContext, mCursor, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return LayoutInflater.from(context).inflate(R.layout.wifi_list_row, viewGroup, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textWifiName = (TextView) view.findViewById(R.id.wifi_list_name);
                TextView textWifiPassword = (TextView) view.findViewById(R.id.wifi_list_password);

                //Extract from cursor
                String wifiName = cursor.getString(cursor.getColumnIndexOrThrow(WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_NAME));
                String wifiPassword = cursor.getString(cursor.getColumnIndexOrThrow(WifiReaderContract.WifiEntry.COLUMN_NAME_WIFI_PASSWORD));

                textWifiName.setText(wifiName);
                textWifiPassword.setText(wifiPassword);
            }
        };
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        mCursorAdapter.swapCursor(newCursor);

        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView wifiName;
        public TextView wifiPassword;
        public TextView wifiType;

        public MyViewHolder(View view) {
            super(view);

            wifiName = (TextView) view.findViewById(R.id.wifi_list_name);
            wifiPassword = (TextView) view.findViewById(R.id.wifi_list_password);
        }
    }
}
