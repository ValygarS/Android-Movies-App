package com.kkmovies.kankanmovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.kkmovies.kankanmovies.R;
import com.kkmovies.kankanmovies.Models.ResponseMultiSearch;
import com.kkmovies.kankanmovies.Adapters.SearchAdapter;
import com.kkmovies.kankanmovies.Models.SearchResult;
import com.kkmovies.kankanmovies.MoviesApi.TmdbApi;
import com.kkmovies.kankanmovies.MoviesApi.TmdbApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity{

    private final String TAG = "Search-Activity";

    private RecyclerView mSearchResultsRV;
    private SearchAdapter mSearchAdapter;
    private GridLayoutManager mGridLayoutManager;

    private String mQuery;

    List<SearchResult> mSearchResults;

    Call<ResponseMultiSearch> multiSearchCall;

    private int resultsPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // getting search query
        Intent intentQuery = getIntent();
        if (Intent.ACTION_SEARCH.equals(intentQuery.getAction())) {
            mQuery = intentQuery.getStringExtra("query");
        }

        if (mQuery==null || mQuery.isEmpty()){
            finish();
        }

        setTitle(getResources().getString(R.string.search_title) + mQuery);

        mSearchResults = new ArrayList<>();

        mSearchResultsRV = (RecyclerView) findViewById(R.id.search_results_rv);
        mSearchAdapter = new SearchAdapter(SearchActivity.this, mSearchResults);

        mGridLayoutManager = new GridLayoutManager(SearchActivity.this, 2);

        mSearchResultsRV.setAdapter(mSearchAdapter);
        mSearchResultsRV.setLayoutManager(mGridLayoutManager);

        loadSearchResults(mQuery);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (multiSearchCall!=null){
            multiSearchCall.cancel();
        }
    }

    protected void loadSearchResults(String query){

        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);
        multiSearchCall = tmdbService.getSearchResults(getResources().getString(R.string.tmdb_api_key), query, resultsPage);
        multiSearchCall.enqueue(new Callback<ResponseMultiSearch>() {
            @Override
            public void onResponse(Call<ResponseMultiSearch> call, Response<ResponseMultiSearch> response) {
                if (!response.isSuccessful()){
                    Log.d(TAG, "Response not successfull");
                    multiSearchCall = call.clone();
                    multiSearchCall.enqueue(this);
                    return;

                }
                if (response.body().equals(null)){
                    Log.d(TAG, "Response body is empty");
                    Toast.makeText(getApplicationContext(), "No search results", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (SearchResult result: response.body().getResults()){
                        if (result != null && result.getPoster() != null && result.getMediaType()!=null){
                            if (result.getMediaType().equals("movie") || result.getMediaType().equals("tv")){
                                mSearchResults.add(result);
                            }
                        }
                    }
                    mSearchAdapter.notifyDataSetChanged();
                    resultsPage +=1;
                }

            }

            @Override
            public void onFailure(Call<ResponseMultiSearch> call, Throwable t) {

            }
        });

    }
}
