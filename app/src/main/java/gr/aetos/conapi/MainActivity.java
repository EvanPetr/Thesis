package gr.aetos.conapi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
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
    public static final int NEW_BEEHIVE_ACTIVITY_REQUEST_CODE = 1;
    private BeehiveViewModel beehiveViewModel;
    TextView tv1;
    List<String> phrases = Arrays.asList("Μελίσσι", "Μελισσι", "μελίσσι", "μελισσι");
    BeehiveRoomDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionRequest();
        MicrophoneRecorder.main();
        tv1 = findViewById(R.id.transcriptTextView);
        db = BeehiveRoomDatabase.getDatabase(MainActivity.this);
        List<Command> commands = new ArrayList<Command>(){};
        Command melissiCommand = new CommandBuilder().setCommandExecutor(new CommandExecutor() {
            @Override
            public void execute(List<String> list) {
                Log.i("Mellissi:", list.get(1));
                Beehive beehive = db.beehiveDao().findBeehiveId(Integer.parseInt(list.get(1)));
                if(beehive != null) {
                    Intent intent = new Intent(MainActivity.this, BeehiveChecksActivity.class);
                    intent.putExtra(BeehiveChecksActivity.EXTRA_BEEHIVE_NUMBER, beehive.number);
                    intent.putExtra(BeehiveChecksActivity.EXTRA_BEEHIVE_ID, beehive.id);
                    MainActivity.this.startActivity(intent);
                }
                else {
//                    Log.w("Beehive does not exist!", "Μελίσσι "+list.get(1)+" δεν υπάρχει!");
                    Toast.makeText(MainActivity.this, "Μελίσσι "+list.get(1)+" δεν υπάρχει!", Toast.LENGTH_LONG).show();
                }
            }
        }).setRegex("μελ[ιί]σσι\\s+(\\d+)").setFlags(Pattern.CASE_INSENSITIVE|Pattern.DOTALL)
                .setPhrases(new ArrayList<>(Arrays.asList("Μελίσσι", "Μελισσι", "μελίσσι", "μελισσι"))).build();
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
                Intent intent = new Intent(MainActivity.this, NewBeehiveActivity.class);
                startActivityForResult(intent, NEW_BEEHIVE_ACTIVITY_REQUEST_CODE);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("RESUME", "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("STOP", "onStop: ");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BEEHIVE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Beehive beehive = new Beehive(data.getIntExtra(NewBeehiveActivity.EXTRA_REPLY, 0), data.getStringExtra(NewBeehiveActivity.DATE_REPLY));
            beehiveViewModel.insert(beehive);
        } else {
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