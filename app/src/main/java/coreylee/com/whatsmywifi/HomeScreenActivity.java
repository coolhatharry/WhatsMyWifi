package coreylee.com.whatsmywifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The main screen for the app
 */
public class HomeScreenActivity extends AppCompatActivity {
    private Intent mAddWifiIntent;
//    private List<Wifi> mWifiList = new ArrayList<>();
    private WifiDBHelper mWifiDBHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WifiAdapter mWifiAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddWifiIntent = new Intent(new Intent(this, AddWifiActivity.class));

        setupAddWifiButton();

        setupRecyclerView();

        prepareWifiData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            this.prepareWifiData();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupAddWifiButton() {
        FloatingActionButton addWifiButton = (FloatingActionButton) findViewById(R.id.add_wifi);
        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mAddWifiIntent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mWifiDBHelper = new WifiDBHelper(this);
        mWifiAdapter = new WifiAdapter(this, mWifiDBHelper.getAllWifis());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mWifiAdapter);
    }

    private void prepareWifiData() {

        mWifiAdapter.swapCursor(mWifiDBHelper.getAllWifis());

    }
}
