package codepath.com.instagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.instagram.model.Post;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {
    private List<Post> mPosts;
    Context context;

    public InstaAdapter(List<Post> posts) {
        mPosts = posts;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // get the data according to position
        final Post post = mPosts.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(post.getUser().getUsername().toString());
        holder.tvUsername2.setText("@" + post.getUser().getUsername().toString());
        holder.tvDescription.setText(post.getDescription().toString());

        String time = post.getCreatedAt().toString().substring(0, 11);
        holder.tvTime.setText("Created on " + time);

        final String numberOfLikes = post.getLikes();
        holder.tvNumber.setText(numberOfLikes + " ");

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((holder.ivHeart.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.ufi_heart).getConstantState()))) {
                    holder.ivHeart.setImageResource(R.drawable.ufi_heart_active);
                    Integer moreLikes = Integer.parseInt(post.getLikes());
                    moreLikes = moreLikes + 1;
                    post.setLikes(Integer.toString(moreLikes));
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("InstaAdapter", "Like post success");
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                    holder.tvNumber.setText(Integer.toString(moreLikes) + " ");
                } else {
                    holder.ivHeart.setImageResource(R.drawable.ufi_heart);
                    Integer lessLikes = Integer.parseInt(post.getLikes());
                    lessLikes = lessLikes - 1;
                    post.setLikes(Integer.toString(lessLikes));
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("InstaAdapter", "Like post success");
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                    holder.tvNumber.setText(Integer.toString(lessLikes) + " ");
                }
            }
        });

        holder.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        });

        holder.tvUsername2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        });

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivImage);

        ParseFile profileImage = post.getUser().getParseFile("profilePic");
        if (profileImage != null) {
            Glide.with(context)
                    .load(profileImage.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivProfile);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        @BindView(R.id.tvUsername) public TextView tvUsername;
        @BindView(R.id.tvUsername2) public TextView tvUsername2;
        @BindView(R.id.ivImage) public ImageView ivImage;
        @BindView(R.id.tvDescription) public TextView tvDescription;
        @BindView(R.id.tvTime) public TextView tvTime;
        @BindView(R.id.ivProfile) public ImageView ivProfile;
        @BindView(R.id.tvNumber) public TextView tvNumber;
        @BindView(R.id.likeButton) public Button likeButton;
        @BindView(R.id.ivHeart) public ImageView ivHeart;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = mPosts.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
