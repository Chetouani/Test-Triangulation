package uchrony.test_triangulation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btValider = (Button) findViewById(R.id.bt_valider);

        btValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triangulation();
            }
        });
    }

    private void triangulation() {
        EditText xB1 = (EditText) findViewById(R.id.xB1);
        EditText yB1 = (EditText) findViewById(R.id.yB1);
        EditText xB2 = (EditText) findViewById(R.id.xB2);
        EditText yB2 = (EditText) findViewById(R.id.yB2);
        EditText xB3 = (EditText) findViewById(R.id.xB3);
        EditText yB3 = (EditText) findViewById(R.id.yB3);
        EditText dB1 = (EditText) findViewById(R.id.dB1);
        EditText dB2 = (EditText) findViewById(R.id.dB2);
        EditText dB3 = (EditText) findViewById(R.id.dB3);

        TextView xGsm = (TextView) findViewById(R.id.xGSM);
        TextView yGsm = (TextView) findViewById(R.id.yGSM);

        double bAlat = Double.parseDouble(xB1.getText().toString());
        double bAlong = Double.parseDouble(yB1.getText().toString());
        double bBlat = Double.parseDouble(xB2.getText().toString());
        double bBlong = Double.parseDouble(yB2.getText().toString());
        double bClat = Double.parseDouble(xB3.getText().toString());
        double bClong = Double.parseDouble(yB3.getText().toString());

        double distanceA = Double.parseDouble(dB1.getText().toString());
        double distanceB = Double.parseDouble(dB2.getText().toString());
        double distanceC = Double.parseDouble(dB3.getText().toString());

        double W, Z, foundBeaconLat, foundBeaconLong, foundBeaconLongFilter;
        W = distanceA * distanceA - distanceB * distanceB - bAlat * bAlat - bAlong * bAlong + bBlat * bBlat + bBlong * bBlong;
        Z = distanceB * distanceB - distanceC * distanceC - bBlat * bBlat - bBlong * bBlong + bClat * bClat + bClong * bClong;

        foundBeaconLat = (W * (bClong - bBlong) - Z * (bBlong - bAlong)) / (2 * ((bBlat - bAlat) * (bClong - bBlong) - (bClat - bBlat) * (bBlong - bAlong)));
        foundBeaconLong = (W - 2 * foundBeaconLat * (bBlat - bAlat)) / (2 * (bBlong - bAlong));
        //`foundBeaconLongFilter` is a second measure of `foundBeaconLong` to mitigate errors
        foundBeaconLongFilter = (Z - 2 * foundBeaconLat * (bClat - bBlat)) / (2 * (bClong - bBlong));

        foundBeaconLong = (foundBeaconLong + foundBeaconLongFilter) / 2;

        xGsm.setText(String.format("%.2f",foundBeaconLat));
        yGsm.setText(String.format("%.2f",foundBeaconLong));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
