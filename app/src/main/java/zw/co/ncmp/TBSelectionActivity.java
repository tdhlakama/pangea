package zw.co.ncmp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TBSelectionActivity extends MenuBar implements View.OnClickListener {

    Button btn_option_one;
    Button btn_option_two;
    Button btn_option_three;
    Button btn_option_four;
    Button btn_option_five;
    Button btn_option_six;
    Button btn_option_seven;
    Button btn_option_eight;
    Button btn_option_nine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_activity);

        btn_option_one = (Button) findViewById(R.id.btn_option_one);
        btn_option_one.setOnClickListener(this);
        btn_option_one.setBackgroundResource(R.drawable.finish_background);
        btn_option_one.setText("TB ART");

        btn_option_two = (Button) findViewById(R.id.btn_option_two);
        btn_option_two.setOnClickListener(this);
        btn_option_two.setBackgroundResource(R.drawable.finish_background);
        btn_option_two.setText("TB SCREENDX");

        btn_option_three = (Button) findViewById(R.id.btn_option_three);
        btn_option_three.setOnClickListener(this);
        btn_option_three.setBackgroundResource(R.drawable.finish_background);
        btn_option_three.setText("TB STAT");

        btn_option_four = (Button) findViewById(R.id.btn_option_four);
        btn_option_four.setOnClickListener(this);
        btn_option_four.setBackgroundResource(R.drawable.finish_background);
        btn_option_four.setText("PMTCT_FO");

        btn_option_five = (Button) findViewById(R.id.btn_option_five);
        btn_option_five.setOnClickListener(this);
        btn_option_five.setBackgroundResource(R.drawable.finish_background);
        btn_option_five.setText("PMTCT_ART");

        btn_option_six = (Button) findViewById(R.id.btn_option_six);
        btn_option_six.setOnClickListener(this);
        btn_option_six.setBackgroundResource(R.drawable.finish_background);
        btn_option_six.setText("TX_RET");

        btn_option_seven = (Button) findViewById(R.id.btn_option_seven);
        btn_option_seven.setOnClickListener(this);
        btn_option_seven.setBackgroundResource(R.drawable.finish_background);
        btn_option_seven.setText("TX_PVLS");

        btn_option_eight = (Button) findViewById(R.id.btn_option_eight);
        btn_option_eight.setOnClickListener(this);
        btn_option_eight.setBackgroundResource(R.drawable.finish_background);
        btn_option_eight.setText("TX_CURR");

        btn_option_nine = (Button) findViewById(R.id.btn_option_nine);
        btn_option_nine.setOnClickListener(this);
        btn_option_nine.setBackgroundResource(R.drawable.finish_background);
        btn_option_nine.setText("Supplementary Indicator Data");

        setSupportActionBar(createToolBar("Abstraction Selection"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == btn_option_one.getId()) {
            intent = new Intent(this, ARTFormListActivity.class);
        }

        if (v.getId() == btn_option_two.getId()) {
            intent = new Intent(this, ScreenFormListActivity.class);
        }

        if (v.getId() == btn_option_three.getId()) {
            intent = new Intent(this, StatFormListActivity.class);
        }

        if (v.getId() == btn_option_four.getId()) {
            intent = new Intent(this, PMTCTFOFormListActivity.class);
        }

        if (v.getId() == btn_option_five.getId()) {
            intent = new Intent(this, PMTCTARTFormListActivity.class);
        }

        if (v.getId() == btn_option_six.getId()) {
            intent = new Intent(this, TXRETFormListActivity.class);
        }

        if (v.getId() == btn_option_seven.getId()) {
            intent = new Intent(this, TXPLSVFormListActivity.class);
        }

        if (v.getId() == btn_option_eight.getId()) {
            intent = new Intent(this, TXCURRFormListActivity.class);
        }

        if (v.getId() == btn_option_nine.getId()) {
            intent = new Intent(this, SupplementaryIndicatorFormListActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
