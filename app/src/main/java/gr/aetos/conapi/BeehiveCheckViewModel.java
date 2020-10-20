package gr.aetos.conapi;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BeehiveCheckViewModel extends AndroidViewModel {
    private BeehiveCheckRepository beehiveCheckRepository;

    private LiveData<List<BeehiveCheck>> allBeehiveChecks;

    public BeehiveCheckViewModel(Application application) {
        super(application);
        beehiveCheckRepository = new BeehiveCheckRepository(application);
    }

    LiveData<List<BeehiveCheck>> getAllBeehiveChecks(int beehiveId) {
        allBeehiveChecks = beehiveCheckRepository.findBeehiveChecks(beehiveId);
        return allBeehiveChecks;
    }

    public void insert(BeehiveCheck beehiveCheck) { beehiveCheckRepository.insert(beehiveCheck); }

    public void update(BeehiveCheck beehiveCheck) { beehiveCheckRepository.update(beehiveCheck); }

    public void delete(BeehiveCheck beehiveCheck) { beehiveCheckRepository.delete(beehiveCheck); }
}
