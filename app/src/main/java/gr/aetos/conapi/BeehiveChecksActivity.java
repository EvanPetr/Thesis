package gr.aetos.conapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
    public static final String EXTRA_BEEHIVE_NUMBER =
            "gr.aetos.conapi.EXTRA_BEEHIVE_NUMBER";
    public static final String EXTRA_BEEHIVE_ID =
            "gr.aetos.conapi.EXTRA_BEEHIVE_ID";
    private static final int NEW_CHECK_BEEHIVE_ACTIVITY_REQUEST_CODE = 1;

    private BeehiveCheckViewModel beehiveCheckViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beehive_checks);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_BEEHIVE_NUMBER)){
            setTitle("Μελίσσι " + intent.getIntExtra(EXTRA_BEEHIVE_NUMBER, -1));
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview_check);
        final BeehiveChecksListAdapter adapter = new BeehiveChecksListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        beehiveCheckViewModel = new ViewModelProvider(this).get(BeehiveCheckViewModel.class);
        Objects.requireNonNull(beehiveCheckViewModel.getAllBeehiveChecks(intent.getIntExtra(EXTRA_BEEHIVE_ID, -1))).observe(this, new Observer<List<BeehiveCheck>>() {
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
                Intent intent = new Intent(BeehiveChecksActivity.this, NewBeehiveCheckActivity.class);
                startActivityForResult(intent, NEW_CHECK_BEEHIVE_ACTIVITY_REQUEST_CODE);
            }
        });

        adapter.setOnItemClickListener(new BeehiveChecksListAdapter.onItemClickListener() {
            @Override
            public void onItemCLick(BeehiveCheck beehiveCheck) {
                Intent intent_check = new Intent(BeehiveChecksActivity.this, BeehiveChecksActivity.class);
                intent_check.putExtra(BeehiveChecksActivity.EXTRA_BEEHIVE_NUMBER, beehiveCheck.date);
                intent_check.putExtra(BeehiveChecksActivity.EXTRA_BEEHIVE_ID, beehiveCheck.id);
                startActivity(intent_check);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("DESTROY1:", "yeah");
//        stopService(getIntent());
    }
}
