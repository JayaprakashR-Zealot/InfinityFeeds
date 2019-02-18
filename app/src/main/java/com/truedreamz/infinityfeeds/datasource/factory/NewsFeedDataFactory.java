package com.truedreamz.infinityfeeds.datasource.factory;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.truedreamz.infinityfeeds.InfinityFeedApp;
import com.truedreamz.infinityfeeds.datasource.NewsFeedDataSource;


public class NewsFeedDataFactory extends DataSource.Factory {

    private MutableLiveData<NewsFeedDataSource> feedDataSourceMutableLiveData;
    private NewsFeedDataSource newsFeedDataSource;
    private InfinityFeedApp appController;

    public NewsFeedDataFactory(InfinityFeedApp appController) {
        this.appController = appController;
        this.feedDataSourceMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        newsFeedDataSource = new NewsFeedDataSource(appController);
        feedDataSourceMutableLiveData.postValue(newsFeedDataSource);
        return newsFeedDataSource;
    }


    public MutableLiveData<NewsFeedDataSource> getFeedDataSourceMutableLiveData() {
        return feedDataSourceMutableLiveData;
    }
}
