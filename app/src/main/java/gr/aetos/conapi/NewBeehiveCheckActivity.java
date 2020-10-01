package gr.aetos.conapi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import gr.aetos.speechtocommand.Command;
import gr.aetos.speechtocommand.CommandBuilder;
import gr.aetos.speechtocommand.CommandExecutor;
import gr.aetos.speechtocommand.InfiniteStreamRecognize;
import gr.aetos.speechtocommand.TranscriptMatcher;

public class NewBeehiveCheckActivity extends AppCompatActivity {
    private EditText editDateView, editFrames;
    private DatePickerDialog datePickerDialog;
    private List<String> phrases = Arrays.asList("Πλαισια", "Πλαίσια", "πλαισια", "πλαίσια");
    private TranscriptMatcher transcriptMatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beehive_check);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        editDateView = findViewById(R.id.editTextDate);
        editFrames = findViewById(R.id.editTextNumberSigned);

        List<Command> commands = new ArrayList<Command>() {};
        Command plaisioCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(final List<String> list) {
                Log.i("Πλαισια:", list.get(1));
                NewBeehiveCheckActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        editFrames.setText(list.get(1));
                        Toast.makeText(NewBeehiveCheckActivity.this, "Πλαίσιο " + list.get(1) + "!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setRegex("πλα[ιί]σι[οα]\\s+(\\d+)").setFlags(Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .setPhrases(phrases).build();
        commands.add(plaisioCommand);
        transcriptMatcher = new TranscriptMatcher(commands);
        InfiniteStreamRecognize.addTranscriptMatcher(transcriptMatcher);


        editDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(NewBeehiveCheckActivity.this,
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("DESTROY:", "yeah");
        InfiniteStreamRecognize.removeTranscriptMatcher(transcriptMatcher);
//        if(InfiniteStreamRecognize.transcriptMatcherList.indexOf(transcriptMatcher) != -1) {
//            InfiniteStreamRecognize.transcriptMatcherList.remove(transcriptMatcher);
//        }
    }
}
