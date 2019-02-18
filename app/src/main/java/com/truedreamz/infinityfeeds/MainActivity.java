package com.truedreamz.infinityfeeds;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.truedreamz.infinityfeeds.adapters.FeedListAdapter;
import com.truedreamz.infinityfeeds.databinding.MainActivityBinding;
import com.truedreamz.infinityfeeds.viewmodel.NewsFeedViewModel;

public class MainActivity extends AppCompatActivity {

    private FeedListAdapter feedListAdapter;
    private NewsFeedViewModel newsFeedViewModel;
    private MainActivityBinding mainActivityBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assigning layout for the activity through DataBinding
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Initialize the ViewModel
        newsFeedViewModel = new NewsFeedViewModel(InfinityFeedApp.create(this));
        // Assigning feedListAdapter class for the RecyclerView
        mainActivityBinding.listFeed.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        feedListAdapter = new FeedListAdapter(getApplicationContext());
        // Using LiveData for new feed sync up, we have to call submitList() method from PagedListAdapter
        newsFeedViewModel.getFeedsLiveData().observe(this, pagedList -> {
            feedListAdapter.submitList(pagedList);
        });
        // the network state sync up is taken care by LiveData
        newsFeedViewModel.getNetworkState().observe(this, networkState -> {
            feedListAdapter.setNetworkState(networkState);
        });
        mainActivityBinding.listFeed.setAdapter(feedListAdapter);
    }
}
