package zw.co.ncmp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zw.co.ncmp.business.Facility;
import zw.co.ncmp.business.Period;
import zw.co.ncmp.business.StatForm;
import zw.co.ncmp.util.AppUtil;

public class StatFormActivity extends MenuBar implements View.OnClickListener {

    Spinner facility;
    EditText dateCreated;
    Spinner period;
    EditText name;
    EditText numerator;
    EditText denominator;
    EditText knownHIV;
    EditText positiveHIV;
    EditText negativeHIV;

    Button btn_completed;
    Button btn_submit;

    Button btn_save;
    private StatForm form;
    private DatePickerDialog datePickerDialog;
    Button btn_question_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_form_activity);

        Intent intent = getIntent();
        Long form_id = intent.getLongExtra(AppUtil.ID, 0);

        facility = (Spinner) findViewById(R.id.facility);
        name = (EditText) findViewById(R.id.name);
        period = (Spinner) findViewById(R.id.period);
        numerator = (EditText) findViewById(R.id.numerator);
        denominator = (EditText) findViewById(R.id.denominator);
        knownHIV = (EditText) findViewById(R.id.knownHIV);
        positiveHIV = (EditText) findViewById(R.id.positiveHIV);
        negativeHIV = (EditText) findViewById(R.id.negativeHIV);
        dateCreated = (EditText) findViewById(R.id.dateCreated);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                updateLabel(newDate.getTime());
            }

        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        dateCreated.setOnClickListener(this);

        ArrayAdapter<Facility> facilityArrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, Facility.getAll());
        facilityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facility.setAdapter(facilityArrayAdapter);

        ArrayAdapter<Period> periodArrayAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, Period.getAll());
        periodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period.setAdapter(periodArrayAdapter);


        if (form_id != 0) {
            form = StatForm.get(form_id);
            name.setText(form.name);

            numerator.setText(AppUtil.getLongValue(form.numerator));
            denominator.setText(AppUtil.getLongValue(form.denominator));
            knownHIV.setText(AppUtil.getLongValue(form.knownHIV));
            positiveHIV.setText(AppUtil.getLongValue(form.positiveHIV));
            negativeHIV.setText(AppUtil.getLongValue(form.negativeHIV));

            updateLabel(form.dateCreated);

            int i = 0;
            for (Facility s : Facility.getAll()) {
                if (form.facility.equals(facility.getItemAtPosition(i))) {
                    facility.setSelection(i);
                    break;
                }
                i++;
            }

            i = 0;
            for (Period s : Period.getAll()) {
                if (form.period.equals(period.getItemAtPosition(i))) {
                    period.setSelection(i);
                    break;
                }
                i++;
            }

            setSupportActionBar(createToolBar("TB_STAT Update"));
        } else {
            form = new StatForm();
            setSupportActionBar(createToolBar("TB_STAT"));
        }


        btn_question_one = (Button) findViewById(R.id.btn_question_one);
        btn_question_one.setOnClickListener(this);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_save.setBackgroundResource(R.drawable.finish_background);

        btn_completed = (Button) findViewById(R.id.btn_completed);
        btn_completed.setVisibility(View.GONE);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        btn_submit.setVisibility(View.GONE);
        btn_submit.setBackgroundResource(R.drawable.finish_background);

        if (form.dateCreated != null) {
            btn_submit.setVisibility(View.VISIBLE);
        }

        if (form.dateSubmitted != null) {
            btn_submit.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
            btn_completed.setVisibility(View.VISIBLE);
        }

        upDateForm();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btn_question_one.getId()) {
            questionOne();
        }

        if (v.getId() == btn_save.getId()) {
            if (validate()) {
                form.facility = (Facility) facility.getSelectedItem();
                form.name = name.getText().toString();
                form.period = (Period) period.getSelectedItem();
                form.numerator = AppUtil.getLongValue(numerator.getText().toString());
                form.denominator = AppUtil.getLongValue(denominator.getText().toString());

                form.knownHIV = AppUtil.getLongValue(knownHIV.getText().toString());
                form.positiveHIV = AppUtil.getLongValue(positiveHIV.getText().toString());
                form.negativeHIV = AppUtil.getLongValue(negativeHIV.getText().toString());
                form.dateCreated = AppUtil.getDate(dateCreated.getText().toString());

                form.save();
                btn_submit.setVisibility(View.VISIBLE);
                AppUtil.createShortNotification(StatFormActivity.this, "Saved");
            } else {
                return;
            }
        }

        if (v.getId() == btn_submit.getId()) {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to submit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (validate()) {
                                form.dateSubmitted = new Date();
                                form.save();
                                AppUtil.createLongNotification(StatFormActivity.this, "Submitted for Upload to Server");
                                Intent intent = new Intent(StatFormActivity.this, StatFormListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }

        if (v.getId() == dateCreated.getId()) {
            datePickerDialog.show();
        }

    }

    private void updateLabel(Date date) {
        dateCreated.setText(AppUtil.getStringDate(date));
    }


    public void questionOne() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dsd_question_activity);

        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
        txt_name.setText(R.string.disaggregation);

        final TextView maleTotal = (TextView) dialog.findViewById(R.id.maleTotal);
        maleTotal.setText(AppUtil.getLongValue(form.maleQuestion1()));

        final TextView femaleTotal = (TextView) dialog.findViewById(R.id.femaleTotal);
        femaleTotal.setText(AppUtil.getLongValue(form.femaleQuestion1()));

        final EditText maleLessThanOne = (EditText) dialog.findViewById(R.id.maleLessThanOne);
        final EditText femaleLessThanOne = (EditText) dialog.findViewById(R.id.femaleLessThanOne);
        final EditText maleOneToFour = (EditText) dialog.findViewById(R.id.maleOneToFour);
        final EditText femaleOneToFour = (EditText) dialog.findViewById(R.id.femaleOneToFour);
        final EditText maleFiveToNine = (EditText) dialog.findViewById(R.id.maleFiveToNine);
        final EditText femaleFiveToNine = (EditText) dialog.findViewById(R.id.femaleFiveToNine);
        final EditText maleTenToFourteen = (EditText) dialog.findViewById(R.id.maleTenToFourteen);
        final EditText femaleTenToFourteen = (EditText) dialog.findViewById(R.id.femaleTenToFourteen);
        final EditText maleFifteenToNineteen = (EditText) dialog.findViewById(R.id.maleFifteenToNineteen);
        final EditText femaleFifteenToNineteen = (EditText) dialog.findViewById(R.id.femaleFifteenToNineteen);
        final EditText maleTwentyToTwentyFour = (EditText) dialog.findViewById(R.id.maleTwentyToTwentyFour);
        final EditText femaleTwentyToTwentyFour = (EditText) dialog.findViewById(R.id.femaleTwentyToTwentyFour);
        final EditText maleTwentyFiveToFortyNine = (EditText) dialog.findViewById(R.id.maleTwentyFiveToFortyNine);
        final EditText femaleTwentyFiveToFortyNine = (EditText) dialog.findViewById(R.id.femaleTwentyFiveToFortyNine);
        final EditText maleFiftyPlus = (EditText) dialog.findViewById(R.id.maleFiftyPlus);
        final EditText femaleFiftyPlus = (EditText) dialog.findViewById(R.id.femaleFiftyPlus);

        if (form != null) {
            maleLessThanOne.setText(AppUtil.getLongValue(form.maleLessThanOne));
            femaleLessThanOne.setText(AppUtil.getLongValue(form.femaleLessThanOne));
            maleOneToFour.setText(AppUtil.getLongValue(form.maleOneToFour));
            femaleOneToFour.setText(AppUtil.getLongValue(form.femaleOneToFour));
            maleFiveToNine.setText(AppUtil.getLongValue(form.maleFiveToNine));
            femaleFiveToNine.setText(AppUtil.getLongValue(form.femaleFiveToNine));
            maleTenToFourteen.setText(AppUtil.getLongValue(form.maleTenToFourteen));
            femaleTenToFourteen.setText(AppUtil.getLongValue(form.femaleTenToFourteen));
            maleFifteenToNineteen.setText(AppUtil.getLongValue(form.maleFifteenToNineteen));
            femaleFifteenToNineteen.setText(AppUtil.getLongValue(form.femaleFifteenToNineteen));
            maleTwentyToTwentyFour.setText(AppUtil.getLongValue(form.maleTwentyToTwentyFour));
            femaleTwentyToTwentyFour.setText(AppUtil.getLongValue(form.femaleTwentyToTwentyFour));
            maleTwentyFiveToFortyNine.setText(AppUtil.getLongValue(form.maleTwentyFiveToFortyNine));
            femaleTwentyFiveToFortyNine.setText(AppUtil.getLongValue(form.femaleTwentyFiveToFortyNine));
            maleFiftyPlus.setText(AppUtil.getLongValue(form.maleFiftyPlus));
            femaleFiftyPlus.setText(AppUtil.getLongValue(form.femaleFiftyPlus));
        }

        List<EditText> list = new ArrayList<>();
        list.add(maleLessThanOne);
        list.add(maleOneToFour);
        list.add(maleFiveToNine);
        list.add(maleTenToFourteen);
        list.add(maleFifteenToNineteen);
        list.add(maleTwentyToTwentyFour);
        list.add(maleTwentyFiveToFortyNine);
        list.add(maleFiftyPlus);

        for (EditText editText : list) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {

                        form.maleLessThanOne = AppUtil.getLongValue(maleLessThanOne.getText().toString());
                        form.maleOneToFour = AppUtil.getLongValue(maleOneToFour.getText().toString());
                        form.maleFiveToNine = AppUtil.getLongValue(maleFiveToNine.getText().toString());
                        form.maleTenToFourteen = AppUtil.getLongValue(maleTenToFourteen.getText().toString());
                        form.maleFifteenToNineteen = AppUtil.getLongValue(maleFifteenToNineteen.getText().toString());
                        form.maleTwentyToTwentyFour = AppUtil.getLongValue(maleTwentyToTwentyFour.getText().toString());
                        form.maleTwentyFiveToFortyNine = AppUtil.getLongValue(maleTwentyFiveToFortyNine.getText().toString());
                        form.maleFiftyPlus = AppUtil.getLongValue(maleFiftyPlus.getText().toString());

                        maleTotal.setText(AppUtil.getLongValue(form.maleQuestion1()));
                    }

                }
            });
        }

        list = new ArrayList<>();
        list.add(femaleLessThanOne);
        list.add(femaleOneToFour);
        list.add(femaleFiveToNine);
        list.add(femaleTenToFourteen);
        list.add(femaleFifteenToNineteen);
        list.add(femaleTwentyToTwentyFour);
        list.add(femaleTwentyFiveToFortyNine);
        list.add(femaleFiftyPlus);

        for (EditText editText : list) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {

                        form.femaleLessThanOne = AppUtil.getLongValue(femaleLessThanOne.getText().toString());
                        form.femaleOneToFour = AppUtil.getLongValue(femaleOneToFour.getText().toString());
                        form.femaleFiveToNine = AppUtil.getLongValue(femaleFiveToNine.getText().toString());
                        form.femaleTenToFourteen = AppUtil.getLongValue(femaleTenToFourteen.getText().toString());
                        form.femaleFifteenToNineteen = AppUtil.getLongValue(femaleFifteenToNineteen.getText().toString());
                        form.femaleTwentyToTwentyFour = AppUtil.getLongValue(femaleTwentyToTwentyFour.getText().toString());
                        form.femaleTwentyFiveToFortyNine = AppUtil.getLongValue(femaleTwentyFiveToFortyNine.getText().toString());
                        form.femaleFiftyPlus = AppUtil.getLongValue(femaleFiftyPlus.getText().toString());

                        femaleTotal.setText(AppUtil.getLongValue(form.femaleQuestion1()));
                    }

                }
            });
        }

        Button saveButton = (Button) dialog.findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                form.maleLessThanOne = AppUtil.getLongValue(maleLessThanOne.getText().toString());
                form.femaleLessThanOne = AppUtil.getLongValue(femaleLessThanOne.getText().toString());

                form.maleOneToFour = AppUtil.getLongValue(maleOneToFour.getText().toString());
                form.femaleOneToFour = AppUtil.getLongValue(femaleOneToFour.getText().toString());

                form.maleFiveToNine = AppUtil.getLongValue(maleFiveToNine.getText().toString());
                form.femaleFiveToNine = AppUtil.getLongValue(femaleFiveToNine.getText().toString());

                form.maleTenToFourteen = AppUtil.getLongValue(maleTenToFourteen.getText().toString());
                form.femaleTenToFourteen = AppUtil.getLongValue(femaleTenToFourteen.getText().toString());

                form.maleFifteenToNineteen = AppUtil.getLongValue(maleFifteenToNineteen.getText().toString());
                form.femaleFifteenToNineteen = AppUtil.getLongValue(femaleFifteenToNineteen.getText().toString());

                form.maleTwentyToTwentyFour = AppUtil.getLongValue(maleTwentyToTwentyFour.getText().toString());
                form.femaleTwentyToTwentyFour = AppUtil.getLongValue(femaleTwentyToTwentyFour.getText().toString());

                form.maleTwentyFiveToFortyNine = AppUtil.getLongValue(maleTwentyFiveToFortyNine.getText().toString());
                form.femaleTwentyFiveToFortyNine = AppUtil.getLongValue(femaleTwentyFiveToFortyNine.getText().toString());

                form.maleFiftyPlus = AppUtil.getLongValue(maleFiftyPlus.getText().toString());
                form.femaleFiftyPlus = AppUtil.getLongValue(femaleFiftyPlus.getText().toString());

                upDateForm();
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();

    }

    public boolean validate() {
        boolean valid = true;

        String name = numerator.getText().toString().toString();

        if (name.isEmpty()) {
            numerator.setError("Required");
            valid = false;
        } else {
            numerator.setError(null);
        }

        name = denominator.getText().toString().toString();

        if (name.isEmpty()) {
            denominator.setError("Required");
            valid = false;
        } else {
            denominator.setError(null);
        }

        name = dateCreated.getText().toString().toString();

        if (name.isEmpty()) {
            dateCreated.setError("Required");
            valid = false;
        } else {
            dateCreated.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(context)
                .setMessage("Exit Form?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(StatFormActivity.this, StatFormListActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void upDateForm() {

        btn_question_one.setText("3. " + this.getString(R.string.dsd_indvidual_question_one)
                + " [ " + (form.maleQuestion1() + form.femaleQuestion1()) + " ]");

    }

}
