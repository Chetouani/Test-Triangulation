package uchrony.test_triangulation;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final String MEMOIRE_VALEUR = "Memoire valeurs";

    double bAlat;
    double bAlong;
    double bBlat;
    double bBlong;
    double bClat;
    double bClong;

    double distanceA;
    double distanceB;
    double distanceC;

    double positionX;
    double positionY;


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
        restaurationValeur();


    }

    private void restaurationValeur() {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(MEMOIRE_VALEUR, 0);
        bAlat = settings.getFloat("bAlat",0);
        bAlong = settings.getFloat("bAlong",0);
        bBlat = settings.getFloat("bBlat",0);
        bBlong = settings.getFloat("bBlong",0);
        bClat = settings.getFloat("bClat",0);
        bClong = settings.getFloat("bClong",0);

        distanceA = settings.getFloat("distanceA",0);
        distanceB = settings.getFloat("distanceB",0);
        distanceC = settings.getFloat("distanceC",0);

        positionX = settings.getFloat("positionX",0);
        positionY = settings.getFloat("positionY",0);

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

        xB1.setText(""+ (int) bAlat);
        yB1.setText(""+ (int) bAlong);
        xB2.setText(""+ (int) bBlat);
        yB2.setText(""+ (int) bBlong);
        xB3.setText(""+ (int) bClat);
        yB3.setText(""+ (int) bClong);
        dB1.setText(""+distanceA);
        dB2.setText(""+distanceB);
        dB3.setText(""+distanceC);
        xGsm.setText(""+String.format("%.2f",positionX));
        yGsm.setText(""+String.format("%.2f",positionY));
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

        bAlat = Double.parseDouble(xB1.getText().toString());
        bAlong = Double.parseDouble(yB1.getText().toString());
        bBlat = Double.parseDouble(xB2.getText().toString());
        bBlong = Double.parseDouble(yB2.getText().toString());
        bClat = Double.parseDouble(xB3.getText().toString());
        bClong = Double.parseDouble(yB3.getText().toString());

        distanceA = Double.parseDouble(dB1.getText().toString());
        distanceB = Double.parseDouble(dB2.getText().toString());
        distanceC = Double.parseDouble(dB3.getText().toString());

        double W, Z, foundBeaconLat, foundBeaconLong, foundBeaconLongFilter;
        W = distanceA * distanceA - distanceB * distanceB - bAlat * bAlat - bAlong * bAlong + bBlat * bBlat + bBlong * bBlong;
        Z = distanceB * distanceB - distanceC * distanceC - bBlat * bBlat - bBlong * bBlong + bClat * bClat + bClong * bClong;

        foundBeaconLat = (W * (bClong - bBlong) - Z * (bBlong - bAlong)) / (2 * ((bBlat - bAlat) * (bClong - bBlong) - (bClat - bBlat) * (bBlong - bAlong)));
        foundBeaconLong = (W - 2 * foundBeaconLat * (bBlat - bAlat)) / (2 * (bBlong - bAlong));
        //`foundBeaconLongFilter` is a second measure of `foundBeaconLong` to mitigate errors
        foundBeaconLongFilter = (Z - 2 * foundBeaconLat * (bClat - bBlat)) / (2 * (bClong - bBlong));

        foundBeaconLong = (foundBeaconLong + foundBeaconLongFilter) / 2;

        positionX = foundBeaconLat;
        positionY = foundBeaconLong;

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

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(MEMOIRE_VALEUR, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putFloat("bAlat", (float) bAlat);
        editor.putFloat("bAlong", (float) bAlong);
        editor.putFloat("bBlat", (float) bBlat);
        editor.putFloat("bBlong", (float) bBlong);
        editor.putFloat("bClat", (float) bClat);
        editor.putFloat("bClong", (float) bClong);

        editor.putFloat("distanceA", (float) distanceA);
        editor.putFloat("distanceB", (float) distanceB);
        editor.putFloat("distanceC", (float) distanceC);

        editor.putFloat("positionX", (float) positionX);
        editor.putFloat("positionY", (float) positionY);

        // Commit the edits!
        editor.commit();
    }
}
