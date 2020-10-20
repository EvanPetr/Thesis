package gr.aetos.conapi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import gr.aetos.speechtocommand.Command;
import gr.aetos.speechtocommand.CommandBuilder;
import gr.aetos.speechtocommand.CommandExecutor;
import gr.aetos.speechtocommand.InfiniteStreamRecognize;
import gr.aetos.speechtocommand.TranscriptMatcher;

public class NewEditBeehiveCheckActivity extends AppCompatActivity {
    public static final String EXTRA_BEEHIVE_CHECK_ID = "gr.aetos.conapi.EXTRA_BEEHIVE_CHECK_ID";
    public static final String EXTRA_BEEHIVE_CHECK_DATE = "gr.aetos.conapi.EXTRA_BEEHIVE_CHECK_DATE";
    public static final String EXTRA_BEEHIVE_CHECK_FRAMES = "gr.aetos.conapi.EXTRA_BEEHIVE_CHECK_FRAMES";
    public static final String EXTRA_BEEHIVE_CHECK_POPULATION = "gr.aetos.conapi.EXTRA_BEEHIVE_CHECK_POPULATION";
    public static final String EXTRA_BEEHIVE_CHECK_PROGENY = "gr.aetos.conapi.EXTRA_BEEHIVE_CHECK_PROGENY";
    public static final String EXTRA_BEEHIVE_CHECK_HONEY = "gr.aetos.conapi.EXTRA_BEEHIVE_CHECK_HONEY";
    public static final String EXTRA_BEEHIVE_CHECK_POLLEN = "gr.aetos.conapi.EXTRA_BEEHIVE_CHECK_POLLEN";
    public static final String EXTRA_BEEHIVE_CHECK_NOTE = "gr.aetos.conapi.EXTRA_BEEHIVE_CHECK_NOTE";

    public static final int NEW_CHECK_BEEHIVE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_CHECK_BEEHIVE_ACTIVITY_REQUEST_CODE = 2;

    private EditText editDateView, editFrames, editPopulation, editProgeny, editHoney, editPollen, editNotes;
    private DatePickerDialog datePickerDialog;
    private final List<String> framePhrases = Arrays.asList("Πλαισια 1", "Πλαισια 2", "Πλαισια 3", "Πλαισια 4", "Πλαισια 5", "Πλαισια 6", "Πλαισια 7", "Πλαισια 8", "Πλαισια 9", "Πλαισια 10", "Πλαίσια 1", "Πλαίσια 2", "Πλαίσια 3", "Πλαίσια 4", "Πλαίσια 5", "Πλαίσια 6", "Πλαίσια 7", "Πλαίσια 8", "Πλαίσια 9", "Πλαίσια 10", "πλαισια 1", "πλαισια 2", "πλαισια 3", "πλαισια 4", "πλαισια 5", "πλαισια 6", "πλαισια 7", "πλαισια 8", "πλαισια 9", "πλαισια 10", "πλαίσια 1", "πλαίσια 2", "πλαίσια 3", "πλαίσια 4", "πλαίσια 5", "πλαίσια 6", "πλαίσια 7", "πλαίσια 8", "πλαίσια 9", "πλαίσια 10");
    private final List<String> populationPhrases = Arrays.asList("Πληθυσμός 1", "Πληθυσμός 2", "Πληθυσμός 3", "Πληθυσμός 4", "Πληθυσμός 5", "Πληθυσμός 6", "Πληθυσμός 7", "Πληθυσμός 8", "Πληθυσμός 9", "Πληθυσμός 10", "Πληθυσμος 1", "Πληθυσμος 2", "Πληθυσμος 3", "Πληθυσμος 4", "Πληθυσμος 5", "Πληθυσμος 6", "Πληθυσμος 7", "Πληθυσμος 8", "Πληθυσμος 9", "Πληθυσμος 10", "πληθυσμός 1", "πληθυσμός 2", "πληθυσμός 3", "πληθυσμός 4", "πληθυσμός 5", "πληθυσμός 6", "πληθυσμός 7", "πληθυσμός 8", "πληθυσμός 9", "πληθυσμός 10", "πληθυσμος 1", "πληθυσμος 2", "πληθυσμος 3", "πληθυσμος 4", "πληθυσμος 5", "πληθυσμος 6", "πληθυσμος 7", "πληθυσμος 8", "πληθυσμος 9", "πληθυσμος 10");
    private final List<String> progenyPhrases = Arrays.asList("Γόνος 1", "Γόνος 2", "Γόνος 3", "Γόνος 4", "Γόνος 5", "Γόνος 6", "Γόνος 7", "Γόνος 8", "Γόνος 9", "Γόνος 10", "Γονος 1", "Γονος 2", "Γονος 3", "Γονος 4", "Γονος 5", "Γονος 6", "Γονος 7", "Γονος 8", "Γονος 9", "Γονος 10", "γόνος 1", "γόνος 2", "γόνος 3", "γόνος 4", "γόνος 5", "γόνος 6", "γόνος 7", "γόνος 8", "γόνος 9", "γόνος 10", "γονος 1", "γονος 2", "γονος 3", "γονος 4", "γονος 5", "γονος 6", "γονος 7", "γονος 8", "γονος 9", "γονος 10");
    private final List<String> honeyPhrases = Arrays.asList("Μέλι 1", "Μέλι 2", "Μέλι 3", "Μέλι 4", "Μέλι 5", "Μέλι 6", "Μέλι 7", "Μέλι 8", "Μέλι 9", "Μέλι 10", "Μελι 1", "Μελι 2", "Μελι 3", "Μελι 4", "Μελι 5", "Μελι 6", "Μελι 7", "Μελι 8", "Μελι 9", "Μελι 10", "μέλι 1", "μέλι 2", "μέλι 3", "μέλι 4", "μέλι 5", "μέλι 6", "μέλι 7", "μέλι 8", "μέλι 9", "μέλι 10", "μελι 1", "μελι 2", "μελι 3", "μελι 4", "μελι 5", "μελι 6", "μελι 7", "μελι 8", "μελι 9", "μελι 10");
    private final List<String> pollenPhrases = Arrays.asList("Γύρη 1", "Γύρη 2", "Γύρη 3", "Γύρη 4", "Γύρη 5", "Γύρη 6", "Γύρη 7", "Γύρη 8", "Γύρη 9", "Γύρη 10", "Γυρη 1", "Γυρη 2", "Γυρη 3", "Γυρη 4", "Γυρη 5", "Γυρη 6", "Γυρη 7", "Γυρη 8", "Γυρη 9", "Γυρη 10", "γύρη 1", "γύρη 2", "γύρη 3", "γύρη 4", "γύρη 5", "γύρη 6", "γύρη 7", "γύρη 8", "γύρη 9", "γύρη 10", "γυρη 1", "γυρη 2", "γυρη 3", "γυρη 4", "γυρη 5", "γυρη 6", "γυρη 7", "γυρη 8", "γυρη 9", "γυρη 10");
    private final List<String> notesPhrases = Arrays.asList("Παρατήρηση", "Παρατηρηση", "παρατήρηση", "παρατηρηση");
    private TranscriptMatcher transcriptMatcher;

    private int beehiveCheckId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beehive_check);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        editDateView = findViewById(R.id.editTextDate);
        editFrames = findViewById(R.id.editTextNumberSigned);
        editPopulation = findViewById(R.id.populationEditTextNumberSigned);
        editProgeny = findViewById(R.id.progenyEditTextNumberSigned);
        editHoney = findViewById(R.id.honeyEditTextNumberSigned);
        editPollen = findViewById(R.id.pollenEditTextNumberSigned);
        editNotes = findViewById(R.id.notesEditText);

        Intent intent = getIntent();
        beehiveCheckId = intent.getIntExtra(EXTRA_BEEHIVE_CHECK_ID, 0);
        if(intent.hasExtra(EXTRA_BEEHIVE_CHECK_DATE)){
            String beehiveCheckDate = intent.getStringExtra(EXTRA_BEEHIVE_CHECK_DATE);
            setTitle("Έλεγχος " + beehiveCheckDate);
            editDateView.setText(beehiveCheckDate);
        }
        else {
            Date currentTime = Calendar.getInstance().getTime();
            String date = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(currentTime);
            setTitle("Νέος έλεγχος " + date);
            editDateView.setText(date);
        }

        if(intent.hasExtra(EXTRA_BEEHIVE_CHECK_FRAMES)){
            int beehiveCheckFrames = intent.getIntExtra(EXTRA_BEEHIVE_CHECK_FRAMES, 0);
            editFrames.setText(String.valueOf(beehiveCheckFrames));
        }

        if(intent.hasExtra(EXTRA_BEEHIVE_CHECK_POPULATION)){
            int beehiveCheckPopulation = intent.getIntExtra(EXTRA_BEEHIVE_CHECK_POPULATION, 0);
            editPopulation.setText(String.valueOf(beehiveCheckPopulation));
        }

        if(intent.hasExtra(EXTRA_BEEHIVE_CHECK_PROGENY)){
            int beehiveCheckProgeny = intent.getIntExtra(EXTRA_BEEHIVE_CHECK_PROGENY, 0);
            editProgeny.setText(String.valueOf(beehiveCheckProgeny));
        }

        if(intent.hasExtra(EXTRA_BEEHIVE_CHECK_HONEY)){
            int beehiveCheckHoney = intent.getIntExtra(EXTRA_BEEHIVE_CHECK_HONEY, 0);
            editHoney.setText(String.valueOf(beehiveCheckHoney));
        }

        if(intent.hasExtra(EXTRA_BEEHIVE_CHECK_POLLEN)){
            int beehiveCheckPollen = intent.getIntExtra(EXTRA_BEEHIVE_CHECK_POLLEN, 0);
            editPollen.setText(String.valueOf(beehiveCheckPollen));
        }

        if(intent.hasExtra(EXTRA_BEEHIVE_CHECK_NOTE)){
            String beehiveCheckNote = intent.getStringExtra(EXTRA_BEEHIVE_CHECK_NOTE);
            editNotes.setText(beehiveCheckNote);
        }

        List<Command> commands = new ArrayList<Command>(){};
        Command frameCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(final List<String> list) {
                Log.i("Πλαισια:", list.get(1));
                NewEditBeehiveCheckActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        editFrames.setText(list.get(1));
                        Toast.makeText(NewEditBeehiveCheckActivity.this, "Πλαίσιο " + list.get(1) + "!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setRegex("πλα[ιί]σι[οα]\\s+(\\d+)").setFlags(Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .setPhrases(framePhrases).build();
        Command populationCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(final List<String> list) {
                Log.i("Πληθυσμός:", list.get(1));
                NewEditBeehiveCheckActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        editPopulation.setText(list.get(1));
                        Toast.makeText(NewEditBeehiveCheckActivity.this, "Πληθυσμός " + list.get(1) + "!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setRegex("πληθυσμ[οό]ς\\s+(\\d+)").setFlags(Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .setPhrases(populationPhrases).build();
        Command progenyCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(final List<String> list) {
                Log.i("ΓΟΝΟΣ:", list.get(1));
                NewEditBeehiveCheckActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        editProgeny.setText(list.get(1));
                        Toast.makeText(NewEditBeehiveCheckActivity.this, "ΓΟΝΟΣ " + list.get(1) + "!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setRegex("γ[όο]νος\\s+(\\d+)").setFlags(Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .setPhrases(progenyPhrases).build();
        Command honeyCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(final List<String> list) {
                Log.i("Μέλι:", list.get(1));
                NewEditBeehiveCheckActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        editHoney.setText(list.get(1));
                        Toast.makeText(NewEditBeehiveCheckActivity.this, "Μέλι " + list.get(1) + "!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setRegex("μ[έε]λι\\s+(\\d+)").setFlags(Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .setPhrases(honeyPhrases).build();
        Command pollenCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(final List<String> list) {
                Log.i("Γύρη:", list.get(1));
                NewEditBeehiveCheckActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        editPollen.setText(list.get(1));
                        Toast.makeText(NewEditBeehiveCheckActivity.this, "Γύρη " + list.get(1) + "!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setRegex("γ[ύυ]ρη\\s+(\\d+)").setFlags(Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .setPhrases(pollenPhrases).build();
        Command noteCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(final List<String> list) {
                Log.i("Παρατήρηση:", list.get(1));
                NewEditBeehiveCheckActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if(editNotes.getText().length() > 0){
                            editNotes.append("," + list.get(1));
                        }
                        else{
                            editNotes.setText(list.get(1));
                        }
                        Toast.makeText(NewEditBeehiveCheckActivity.this, "Παρατήρηση " + list.get(1) + "!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setRegex("παρατ[ήη]ρηση\\s+(.*)").setFlags(Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .setPhrases(notesPhrases).build();
        commands.add(frameCommand);
        commands.add(populationCommand);
        commands.add(progenyCommand);
        commands.add(honeyCommand);
        commands.add(pollenCommand);
        commands.add(noteCommand);
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
                datePickerDialog = new DatePickerDialog(NewEditBeehiveCheckActivity.this,
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
    public void onBackPressed() {
        InfiniteStreamRecognize.removeTranscriptMatcher(transcriptMatcher);

        Intent replyIntent = new Intent();
        String date = editDateView.getText().toString();
        String frames = editFrames.getText().toString();
        String population = editPopulation.getText().toString();
        String progeny = editProgeny.getText().toString();
        String honey = editHoney.getText().toString();
        String pollen = editPollen.getText().toString();
        String note = editNotes.getText().toString();
        replyIntent.putExtra(EXTRA_BEEHIVE_CHECK_ID, beehiveCheckId);
        replyIntent.putExtra(EXTRA_BEEHIVE_CHECK_DATE, date);
        replyIntent.putExtra(EXTRA_BEEHIVE_CHECK_FRAMES, frames);
        replyIntent.putExtra(EXTRA_BEEHIVE_CHECK_POPULATION, population);
        replyIntent.putExtra(EXTRA_BEEHIVE_CHECK_PROGENY, progeny);
        replyIntent.putExtra(EXTRA_BEEHIVE_CHECK_HONEY, honey);
        replyIntent.putExtra(EXTRA_BEEHIVE_CHECK_POLLEN, pollen);
        replyIntent.putExtra(EXTRA_BEEHIVE_CHECK_NOTE, note);
        setResult(RESULT_OK, replyIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        onBackPressed();
        Log.i("PAUSE NEW EDIT", "onPause: ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("STOP NEW EDIT", "onSTOP: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("DESTROY NEW EDIT", "onDestroy: ");
        super.onDestroy();
    }
}
