package com.matthewthung.instagram;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private RecyclerView rvPosts;
    private Context mContext;
    private View view;
    private SwipeRefreshLayout swipeContainer;
    private List<Post> posts;
    private PostsAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the recycler view:
        rvPosts = view.findViewById(R.id.rvPosts);
        // Initialize the list of tweets and adapter:
        posts = new ArrayList<>();
        adapter = new PostsAdapter(mContext, posts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        // Recycler view setup: layout manager and adapter:
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);

        swipeContainer = view.findViewById(R.id.swipeContainer);

        // Configure the refreshing colors:
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data!");
                queryPosts();
            }
        });

        queryPosts();
        return view;
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for (Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                adapter.clear();
                adapter.addAll(posts);
                adapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }
        });
    }
}