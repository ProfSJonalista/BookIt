package main.bookit.model.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import main.bookit.service.SearchResultService;
import main.bookit.ui.customs.CustomAdapter;

public class SearchResultViewModel extends ViewModel {

    private MutableLiveData<CustomAdapter> customAdapterLD;
    private SearchResultService searchResultService = new SearchResultService();

    public LiveData<CustomAdapter> getCustomAdapter()
    {
        if(customAdapterLD == null){
            customAdapterLD = new MutableLiveData<>();
        }

        loadCustomAdapter();

        return customAdapterLD;
    }

    private void loadCustomAdapter() {
        customAdapterLD = searchResultService.getCustomAdapter();
    }
}
