package com.kkmovies.kankanmovies.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kkmovies.kankanmovies.Adapters.WishlistTvAdapter;
import com.kkmovies.kankanmovies.Database.MovieFirebase;
import com.kkmovies.kankanmovies.Database.TvFirebase;
import com.kkmovies.kankanmovies.R;
import com.kkmovies.kankanmovies.Adapters.WishlistAdapter;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView mWishlistRecyclerView;
    private WishlistAdapter mWishlistAdapter;
    private WishlistTvAdapter mWishlistTvAdapter;

    private List<MovieFirebase> mFirebaseMovies;
    private List<TvFirebase> mFirebaseTvs;

    final String TAG = "Wishlist-Activity";

    // firebase database
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    FirebaseUser user;

    private DatabaseReference rootRef, moviesRef, tvsRef;
    //private ValueEventListener mWishlistListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setTitle("Your Wishlist");

        mWishlistRecyclerView = (RecyclerView) findViewById(R.id.wishlist_recycler_view);

        mFirebaseMovies = new ArrayList<>();
        mFirebaseTvs = new ArrayList<>();

        // load saved data from firebase, method will populate List
        //loadWishlist();

        mWishlistAdapter = new WishlistAdapter(WishlistActivity.this, mFirebaseMovies);
        mWishlistRecyclerView.setAdapter(mWishlistAdapter);
        mWishlistRecyclerView.setLayoutManager(new LinearLayoutManager(WishlistActivity.this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mFirebaseMovies.isEmpty()){
            mFirebaseMovies.clear();
        }
        loadWishlist();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // remove listeners
       // moviesRef.removeEventListener(mWishlistListener);
    }

    // load data from Firebase
    protected void loadWishlist(){
        rootRef = mDatabase.getReference();
        String uid = user.getUid();
        moviesRef = rootRef.child(uid).child("movies");

        moviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    MovieFirebase movie = postSnapshot.getValue(MovieFirebase.class);

                    mFirebaseMovies.add(movie);
                }

                mWishlistAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error: " + databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Read failed", Toast.LENGTH_SHORT).show();
            }
        });

        tvsRef = rootRef.child(uid).child("tvs");

        tvsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TvFirebase tvShow = postSnapshot.getValue(TvFirebase.class);

                    mFirebaseTvs.add(tvShow);
                }

                mWishlistAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error: " + databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Read failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
