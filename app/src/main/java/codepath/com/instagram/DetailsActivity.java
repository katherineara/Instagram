package codepath.com.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.instagram.model.Post;

public class DetailsActivity extends AppCompatActivity {

    Post post;
    @BindView(R.id.tvUsername) public TextView tvUsername;
    @BindView(R.id.ivImage) public ImageView ivImage;
    @BindView(R.id.tvDescription) public TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        // populate the views according to this data
        tvUsername.setText(post.getUser().getUsername().toString());
        tvDescription.setText(post.getDescription().toString());

        // TODO - GlideApp or Glide
        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(ivImage);
    }
}
