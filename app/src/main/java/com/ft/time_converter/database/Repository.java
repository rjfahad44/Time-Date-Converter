package com.ft.time_converter.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ft.time_converter.dao.TimeDateDao;
import com.ft.time_converter.dao.TimeDateSearchDao;
import com.ft.time_converter.modles.TimeZoneListModel;
import com.ft.time_converter.modles.TimeZoneSearchListModel;

import java.util.List;


public class Repository {
    private TimeDateDao timeDateDao;
    private LiveData<List<TimeZoneListModel>> listLiveData;
    private List<TimeZoneListModel> checkDbList;

    private TimeDateSearchDao timeDateSearchDao;
    private List<TimeZoneSearchListModel> timeDateSearchList;
    private List<TimeZoneSearchListModel> checkSearchDbList;

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        timeDateDao = db.timeDateDao();
        listLiveData = timeDateDao.getAllData();
        checkDbList = timeDateDao.checkDb();

        timeDateSearchDao = db.timeDateSearchDao();
        timeDateSearchList = timeDateSearchDao.findAllData();
        checkSearchDbList = timeDateSearchDao.checkSearchDb();
    }

    //-------------------------------time_zone_search_list_table-------------------------------//
    public List<TimeZoneSearchListModel> getTimeDateSearchList() {
        return timeDateSearchList;
    }

    public List<TimeZoneSearchListModel> getCheckSearchDbList() {
        return checkSearchDbList;
    }

    public List<TimeZoneSearchListModel> searchDataList(String query) {
        return timeDateSearchDao.findBySearchText(query);
    }


    public void insertSearchData(TimeZoneSearchListModel... model) {
        new insertSearchAsyncTask(timeDateSearchDao).execute(model);
    }

    private static class insertSearchAsyncTask extends AsyncTask<TimeZoneSearchListModel, Void, Void> {
        private TimeDateSearchDao timeDateSearchAsyncTaskDao;

        insertSearchAsyncTask(TimeDateSearchDao dao) {
            this.timeDateSearchAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TimeZoneSearchListModel... params) {
            timeDateSearchAsyncTaskDao.insertSearchDataList(params[0]);
            return null;
        }
    }


    //-------------------------------time_zone_list_table-------------------------------//
    public LiveData<List<TimeZoneListModel>> getListLiveData() {
        return listLiveData;
    }

    public List<TimeZoneListModel> getCheckDbList() {
        return checkDbList;
    }

    public List<TimeZoneListModel> getFindByCity(String city, String country, String title) {
        return timeDateDao.findByCity(city, country, title);
    }

    public void insert(TimeZoneListModel model) {
        new insertAsyncTask(timeDateDao).execute(model);
    }

    public void delete(TimeZoneListModel model) {
        new DeleteProductAsyncTask(timeDateDao).execute(model);
    }

    public void update(TimeZoneListModel model) {
        new UpdateProductAsyncTask(timeDateDao).execute(model);
    }

    private static class insertAsyncTask extends AsyncTask<TimeZoneListModel, Void, Void> {
        private TimeDateDao mAsyncTaskDao;

        insertAsyncTask(TimeDateDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TimeZoneListModel... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateProductAsyncTask extends AsyncTask<TimeZoneListModel, Void, Void> {
        private TimeDateDao timeDateDao;

        private UpdateProductAsyncTask(TimeDateDao timeDateDao) {
            this.timeDateDao = timeDateDao;
        }

        @Override
        protected Void doInBackground(TimeZoneListModel... models) {
            timeDateDao.update(models[0]);
            return null;
        }
    }

    private static class DeleteProductAsyncTask extends AsyncTask<TimeZoneListModel, Void, Void> {
        private TimeDateDao timeDateDao;

        private DeleteProductAsyncTask(TimeDateDao timeDateDao) {
            this.timeDateDao = timeDateDao;
        }

        @Override
        protected Void doInBackground(TimeZoneListModel... models) {
            timeDateDao.delete(models[0]);
            return null;
        }
    }
}
