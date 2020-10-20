package gr.aetos.conapi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import gr.aetos.speechtocommand.Command;
import gr.aetos.speechtocommand.CommandBuilder;
import gr.aetos.speechtocommand.CommandExecutor;
import gr.aetos.speechtocommand.InfiniteStreamRecognizeArguments;
import gr.aetos.speechtocommand.InfiniteStreamRecognize;
import gr.aetos.speechtocommand.MicrophoneRecorder;
import gr.aetos.speechtocommand.TranscriptMatcher;


public class MainActivity extends AppCompatActivity {
    private BeehiveViewModel beehiveViewModel;
    private BeehiveRoomDatabase db;
    private final List<String> beehivePhrases = Arrays.asList("Μελίσσι 1", "Μελίσσι 2", "Μελίσσι 3", "Μελίσσι 4", "Μελίσσι 5", "Μελίσσι 6", "Μελίσσι 7", "Μελίσσι 8", "Μελίσσι 9", "Μελίσσι 10", "Μελισσι 1", "Μελισσι 2", "Μελισσι 3", "Μελισσι 4", "Μελισσι 5", "Μελισσι 6", "Μελισσι 7", "Μελισσι 8", "Μελισσι 9", "Μελισσι 10", "μελίσσι 1", "μελίσσι 2", "μελίσσι 3", "μελίσσι 4", "μελίσσι 5", "μελίσσι 6", "μελίσσι 7", "μελίσσι 8", "μελίσσι 9", "μελίσσι 10", "μελισσι 1", "μελισσι 2", "μελισσι 3", "μελισσι 4", "μελισσι 5", "μελισσι 6", "μελισσι 7", "μελισσι 8", "μελισσι 9", "μελισσι 10");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionRequest();
        MicrophoneRecorder.main();
        db = BeehiveRoomDatabase.getDatabase(MainActivity.this);
        List<Command> commands = new ArrayList<Command>(){};
        Command melissiCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(final List<String> list) {
                Beehive beehive = db.beehiveDao().findBeehiveId(Integer.parseInt(list.get(1)));
                if(beehive != null) {
                    Intent intent = new Intent(MainActivity.this, BeehiveChecksActivity.class);
                    intent.putExtra(BeehiveChecksActivity.EXTRA_BEEHIVE_NUMBER, beehive.number);
                    intent.putExtra(BeehiveChecksActivity.EXTRA_BEEHIVE_ID, beehive.id);
                    intent.putExtra(BeehiveChecksActivity.EXTRA_COMMAND_REQUEST, 0);
                    MainActivity.this.startActivity(intent);
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Μελίσσι "+list.get(1)+" δεν υπάρχει!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).setRegex("μελ[ιί]σσι\\s+(\\d+)").setFlags(Pattern.CASE_INSENSITIVE|Pattern.DOTALL)
                .setPhrases(beehivePhrases).build();
        commands.add(melissiCommand);
        TranscriptMatcher transcriptMatcher = new TranscriptMatcher(commands);
        InfiniteStreamRecognize.addTranscriptMatcher(transcriptMatcher);
        InfiniteStreamRecognizeArguments infiniteStreamRecognizeArguments =
                new InfiniteStreamRecognizeArguments(getResources()
                        .openRawResource(R.raw.credentials));
        InfiniteStreamRecognize infiniteStreamRecognize = new InfiniteStreamRecognize();
        infiniteStreamRecognize.execute(infiniteStreamRecognizeArguments);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BeehiveListAdapter adapter = new BeehiveListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        beehiveViewModel = new ViewModelProvider(this).get(BeehiveViewModel.class);
        beehiveViewModel.getAllBeehives().observe(this, new Observer<List<Beehive>>() {
            @Override
            public void onChanged(@Nullable final List<Beehive> beehives) {
                // Update the cached copy of the words in the adapter.
                adapter.setBeehives(beehives);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEditBeehiveActivity.class);
                startActivityForResult(intent, NewEditBeehiveActivity.NEW_BEEHIVE_ACTIVITY_REQUEST_CODE);
            }
        });
        adapter.setOnItemClickListener(new BeehiveListAdapter.onItemClickListener() {
            @Override
            public void onItemCLick(Beehive beehive) {
                Intent intent = new Intent(MainActivity.this, BeehiveChecksActivity.class);
                intent.putExtra(BeehiveChecksActivity.EXTRA_BEEHIVE_NUMBER, beehive.number);
                intent.putExtra(BeehiveChecksActivity.EXTRA_BEEHIVE_ID, beehive.id);
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new BeehiveListAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongCLick(final Beehive beehive) {
                final CharSequence[] items = {"Διαγραφή", "Αλλαγή"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            beehiveViewModel.delete(beehive);
                        }
                        else if(item == 1){
                            Intent intent = new Intent(MainActivity.this, NewEditBeehiveActivity.class);
                            intent.putExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_ID, beehive.id);
                            intent.putExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_NUMBER, beehive.number);
                            intent.putExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_QUEEN_DATE, beehive.beeQueenAge);
                            startActivityForResult(intent, NewEditBeehiveActivity.EDIT_BEEHIVE_ACTIVITY_REQUEST_CODE);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NewEditBeehiveActivity.NEW_BEEHIVE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Beehive beehive = new Beehive(data.getIntExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_ID, 0), data.getIntExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_NUMBER, 0), data.getStringExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_QUEEN_DATE));
            beehiveViewModel.insert(beehive);
        }
        else if (requestCode == NewEditBeehiveActivity.EDIT_BEEHIVE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Beehive beehive = new Beehive(data.getIntExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_ID, 0), data.getIntExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_NUMBER, 0), data.getStringExtra(NewEditBeehiveActivity.EXTRA_BEEHIVE_QUEEN_DATE));
            beehiveViewModel.update(beehive);
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    protected void permissionRequest(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }
}