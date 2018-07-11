package codepath.com.instagram;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import codepath.com.instagram.model.Post;

public class TimelineActivity extends AppCompatActivity {

    InstaAdapter instaAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    Post post;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // find the RecyclerView
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        // init the arraylist (data source)
        posts = new ArrayList<>();
        // construct the adapter from this datasource
        instaAdapter = new InstaAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvPosts.setAdapter(instaAdapter);
        populateTimeline();
    }

    private void populateTimeline() {
            final Post.Query postsQuery = new Post.Query();
            postsQuery.getTop().withUser();

            postsQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> objects, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < objects.size(); ++i) {
                            try {
                                Log.d("HomeActivity", "Post(" + i + ") " +
                                        objects.get(i).getDescription() + "\nusername = "
                                        + objects.get(i).getUser().fetchIfNeeded().getUsername()
                                );
                                Post post = objects.get(i);
                                posts.add(post);
                                instaAdapter.notifyItemChanged(posts.size() - 1);

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
    }

    public void fetchTimelineAsync(int page) {
            // Remember to CLEAR OUT old items before appending in the new ones
            instaAdapter.clear();
            final Post.Query postsQuery = new Post.Query();
            postsQuery.getTop().withUser();

            postsQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> objects, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < objects.size(); ++i) {
                            try {
                                Log.d("HomeActivity", "Post(" + i + ") " +
                                        objects.get(i).getDescription() + "\nusername = "
                                        + objects.get(i).getUser().fetchIfNeeded().getUsername()
                                );
                                Post post = objects.get(i);
                                posts.add(post);
                                instaAdapter.notifyItemChanged(posts.size() - 1);

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            // Now we call setRefreshing(false) to signal refresh has finished
            swipeContainer.setRefreshing(false);
    }
}
