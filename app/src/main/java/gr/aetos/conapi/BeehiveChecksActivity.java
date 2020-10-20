package gr.aetos.conapi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class BeehiveChecksActivity extends AppCompatActivity {
    public static final String EXTRA_COMMAND_REQUEST = "gr.aetos.conapi.EXTRA_COMMAND_REQUEST";
    public static final String EXTRA_BEEHIVE_NUMBER = "gr.aetos.conapi.EXTRA_BEEHIVE_NUMBER";
    public static final String EXTRA_BEEHIVE_ID = "gr.aetos.conapi.EXTRA_BEEHIVE_ID";

    private BeehiveCheckViewModel beehiveCheckViewModel;
    private int beehiveId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beehive_checks);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_BEEHIVE_NUMBER)){
            setTitle("Μελίσσι " + intent.getIntExtra(EXTRA_BEEHIVE_NUMBER, -1));
        }
        beehiveId = intent.getIntExtra(EXTRA_BEEHIVE_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_check);
        final BeehiveChecksListAdapter adapter = new BeehiveChecksListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        beehiveCheckViewModel = new ViewModelProvider(this).get(BeehiveCheckViewModel.class);
        Objects.requireNonNull(beehiveCheckViewModel.getAllBeehiveChecks(beehiveId)).observe(this, new Observer<List<BeehiveCheck>>() {
            @Override
            public void onChanged(@Nullable final List<BeehiveCheck> beehiveChecks) {
                // Update the cached copy of the words in the adapter.
                adapter.setBeehiveChecks(beehiveChecks);
            }
        });

        FloatingActionButton fab = findViewById(R.id.check_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeehiveChecksActivity.this, NewEditBeehiveCheckActivity.class);
                startActivityForResult(intent, NewEditBeehiveCheckActivity.NEW_CHECK_BEEHIVE_ACTIVITY_REQUEST_CODE);
            }
        });
        adapter.setOnItemClickListener(new BeehiveChecksListAdapter.onItemClickListener() {
            @Override
            public void onItemCLick(BeehiveCheck beehiveCheck) {
                Intent intent_check = new Intent(BeehiveChecksActivity.this, NewEditBeehiveCheckActivity.class);
                intent_check.putExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_ID, beehiveCheck.id);
                intent_check.putExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_DATE, beehiveCheck.date);
                intent_check.putExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_FRAMES, beehiveCheck.frames);
                intent_check.putExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_HONEY, beehiveCheck.honey);
                intent_check.putExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_POPULATION, beehiveCheck.population);
                intent_check.putExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_NOTE, beehiveCheck.note);
                intent_check.putExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_POLLEN, beehiveCheck.pollen);
                intent_check.putExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_PROGENY, beehiveCheck.progeny);
                startActivityForResult(intent_check, NewEditBeehiveCheckActivity.EDIT_CHECK_BEEHIVE_ACTIVITY_REQUEST_CODE);
            }
        });
        adapter.setOnItemLongClickListener(new BeehiveChecksListAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongCLick(final BeehiveCheck beehiveCheck) {
                final CharSequence[] items = {"Διαγραφή"};
                AlertDialog.Builder builder = new AlertDialog.Builder(BeehiveChecksActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        beehiveCheckViewModel.delete(beehiveCheck);
                    }
                });
                builder.show();
            }
        });
        if(intent.hasExtra(EXTRA_COMMAND_REQUEST)) {
            fab.callOnClick();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int frames, population, progeny, honey, pollen;

        int beehiveCheckId = data.getIntExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_ID, 0);
        String date = data.getStringExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_DATE);
        try {
            frames = Integer.parseInt(data.getStringExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_FRAMES));
        }catch (NumberFormatException nFE){
            frames = 0;
        }

        try {
            population = Integer.parseInt(data.getStringExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_POPULATION));
        }catch (NumberFormatException nFE){
            population = 0;
        }

        try {
            progeny = Integer.parseInt(data.getStringExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_PROGENY));
        }catch (NumberFormatException nFE){
            progeny = 0;
        }

        try {
            honey = Integer.parseInt(data.getStringExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_HONEY));
        }catch (NumberFormatException nFE){
            honey = 0;
        }

        try {
            pollen = Integer.parseInt(data.getStringExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_POLLEN));
        }catch (NumberFormatException nFE){
            pollen = 0;
        }

        String note = data.getStringExtra(NewEditBeehiveCheckActivity.EXTRA_BEEHIVE_CHECK_NOTE);
        BeehiveCheck beehiveCheck = new BeehiveCheck(beehiveCheckId, date, frames, population, progeny, honey, pollen, beehiveId, note);

        if (requestCode == NewEditBeehiveCheckActivity.NEW_CHECK_BEEHIVE_ACTIVITY_REQUEST_CODE) {
            beehiveCheckViewModel.insert(beehiveCheck);
        }
        else if(requestCode == NewEditBeehiveCheckActivity.EDIT_CHECK_BEEHIVE_ACTIVITY_REQUEST_CODE){
            beehiveCheckViewModel.update(beehiveCheck);
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.beehive_check_empty,
                    Toast.LENGTH_LONG).show();
        }
    }
}
