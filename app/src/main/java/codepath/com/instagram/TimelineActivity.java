package codepath.com.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import codepath.com.instagram.model.Post;

public class TimelineActivity extends AppCompatActivity {

    InstaAdapter instaAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    Post post;
    private SwipeRefreshLayout swipeContainer;
    // TODO: Magic Numbers
    public static final int REQUEST_CODE = 1;


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
                        for (int i = objects.size() - 1; i >= 0; --i) {
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
                        for (int i = objects.size() - 1; i >= 0; --i) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
            posts.add(0, post);
            instaAdapter.notifyItemInserted(0);
            rvPosts.scrollToPosition(0);
        }
    }

    public void launchCreate(View view) {
        Intent intent = new Intent(TimelineActivity.this, HomeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void launchProfile(View view) {
        Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
