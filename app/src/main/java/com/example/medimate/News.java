package com.example.medimate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Model>> {

    private static final String NEWS_API =
            "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json";
    private static final int LOADER = 1;
    RecyclerView recyclerView;
    TheAdapter mAdapter;
    List<Model> newsList;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView mEmptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_news);

        newsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TheAdapter(this, newsList);
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(News.this, "UPDATED EVERY 3 HOURS", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ConnectivityManager con = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (con!=null) {
            NetworkInfo networkInfo = con.getActiveNetworkInfo();

            if (networkInfo!=null && networkInfo.isConnected()) {
                LoaderManager loaderManager = getSupportLoaderManager();

                loaderManager.initLoader(LOADER, null, this);
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });

                if (loaderManager.restartLoader(LOADER, null, this).isReset()) {
                    swipeRefreshLayout.setRefreshing(false);


                }

            } else {
                swipeRefreshLayout.setRefreshing(false);
                mEmptyStateView.setText("Check Your INTERNET CONNECTION");
                mEmptyStateView.setVisibility(View.VISIBLE);

            }
        }
    }


    @Override
    public Loader<List<Model>> onCreateLoader(int id, Bundle args) {
        return new Reloader(this, NEWS_API);
    }

    @Override
    public void onLoadFinished( Loader<List<Model>> loader, List<Model> newsList) {

        swipeRefreshLayout.setRefreshing(false);

        if (newsList!=null && !newsList.isEmpty()) {
            mAdapter = new TheAdapter(this, newsList);
            recyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public void onLoaderReset( Loader<List<Model>> loader) {
        swipeRefreshLayout.setRefreshing(false);


    }
  // for the search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem etSearch = menu.findItem(R.id.search_bar);
        android.widget.SearchView searchView = (android.widget.SearchView) etSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}