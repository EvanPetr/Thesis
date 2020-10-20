package gr.aetos.conapi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class NewEditBeehiveActivity extends AppCompatActivity {
    public static final int NEW_BEEHIVE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BEEHIVE_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_BEEHIVE_ID = "gr.aetos.conapi.EXTRA_BEEHIVE_ID";
    public static final String EXTRA_BEEHIVE_NUMBER = "gr.aetos.conapi.EXTRA_BEEHIVE_NUMBER";
    public static final String EXTRA_BEEHIVE_QUEEN_DATE = "gr.aetos.conapi.EXTRA_BEEHIVE_QUEEN_DATE";

    private EditText editBeehiveView;
    private EditText editDateView;
    private DatePickerDialog datePickerDialog;

    private int beehiveId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beehive);
        editBeehiveView = findViewById(R.id.edit_beehive);
        editDateView = findViewById(R.id.edit_date);
        editDateView.setInputType(InputType.TYPE_NULL);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_BEEHIVE_ID)){
            beehiveId = intent.getIntExtra(EXTRA_BEEHIVE_ID, 0);
            setTitle("Ενημέρωση Μελισσιού");
        }
        else{
            setTitle("Νέο Μελίσσι");
        }
        if(intent.hasExtra(EXTRA_BEEHIVE_NUMBER)){
            int beehiveNumber = intent.getIntExtra(EXTRA_BEEHIVE_NUMBER, 0);
            editBeehiveView.setText(String.valueOf(beehiveNumber));
        }

        if(intent.hasExtra(EXTRA_BEEHIVE_QUEEN_DATE)){
            String beehiveQueenDate = intent.getStringExtra(EXTRA_BEEHIVE_QUEEN_DATE);
            editDateView.setText(beehiveQueenDate);
        }


        editDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(NewEditBeehiveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editDateView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void saveNote(){
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(editBeehiveView.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            int number = Integer.parseInt(editBeehiveView.getText().toString());
            String queenDate = editDateView.getText().toString();
            replyIntent.putExtra(EXTRA_BEEHIVE_ID, beehiveId);
            replyIntent.putExtra(EXTRA_BEEHIVE_NUMBER, number);
            replyIntent.putExtra(EXTRA_BEEHIVE_QUEEN_DATE, queenDate);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_beehive_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}