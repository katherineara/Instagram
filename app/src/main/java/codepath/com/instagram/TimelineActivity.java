package codepath.com.instagram;

import android.os.Bundle;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

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
}
