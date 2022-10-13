package com.ft.time_converter.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ft.time_converter.database.Repository;
import com.ft.time_converter.modles.TimeZoneListModel;
import com.ft.time_converter.modles.TimeZoneSearchListModel;

import java.util.List;

public class TimeDateViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<TimeZoneListModel>> allList;
    private final List<TimeZoneListModel> checkList;

    private final List<TimeZoneSearchListModel> timeZoneSearchListModelList;
    private final List<TimeZoneSearchListModel> checkSearchDbList;

    public TimeDateViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        allList = repository.getListLiveData();
        checkList = repository.getCheckDbList();

        timeZoneSearchListModelList = repository.getTimeDateSearchList();
        checkSearchDbList = repository.getCheckSearchDbList();
    }

    //<------------------------------------------------------------------------------------------>//
    public List<TimeZoneSearchListModel> getAllSearchDataList() {
        return timeZoneSearchListModelList;
    }

    public List<TimeZoneSearchListModel> getCheckSearchDbList() {
        return checkSearchDbList;
    }

    public List<TimeZoneSearchListModel> searchDataList(String query) {
        return repository.searchDataList(query);
    }

    public void insertSearchData(TimeZoneSearchListModel... model) {
        repository.insertSearchData(model);
    }


    //<------------------------------------------------------------------------------------------>//
    public LiveData<List<TimeZoneListModel>> getAllList() {
        return allList;
    }

    public List<TimeZoneListModel> getFindCity(String city, String country, String title) {
        return repository.getFindByCity(city, country, title);
    }


    public List<TimeZoneListModel> getCheckList() {
        return checkList;
    }

    public void insert(TimeZoneListModel model) {
        repository.insert(model);
    }

    public void update(TimeZoneListModel model) {
        repository.update(model);
    }

    public void delete(TimeZoneListModel model) {
        repository.delete(model);
    }
}

