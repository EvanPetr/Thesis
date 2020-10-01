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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class NewBeehiveActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "gr.aetos.conapi.REPLY";
    public static final String DATE_REPLY = "gr.aetos.conapi.QUEEN_DATE";

    private EditText editBeehiveView;
    private EditText editDateView;
    private DatePickerDialog datePickerDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beehive);
        editBeehiveView = findViewById(R.id.edit_beehive);
        editDateView = findViewById(R.id.edit_date);
        editDateView.setInputType(InputType.TYPE_NULL);

        editDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(NewBeehiveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editDateView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editBeehiveView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    int number = Integer.parseInt(editBeehiveView.getText().toString());
                    String queenDate = editDateView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, number);
                    replyIntent.putExtra(DATE_REPLY, queenDate);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
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
            replyIntent.putExtra(EXTRA_REPLY, number);
            replyIntent.putExtra(DATE_REPLY, queenDate);
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