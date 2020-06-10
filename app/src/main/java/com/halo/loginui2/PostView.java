package com.halo.loginui2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class PostView extends AppCompatActivity implements View.OnClickListener {
    private Post currentPost;
    private TextView postTitle;
    private ListView commentsList;
    private TextView postTime;
    private TextView posterName;
    private Toolbar mToolbar;
    private TextView timeType;
    static String currentPostFollowing;
    public static FirebaseUser user;
    private EditText commentInput;
    private TextView postDescription;
    private Intent serviceIntent;
    private ImageView addComment;
    private ActionBar actionBar;
    private boolean following;

    private static final String TAG = "PostView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_post_view);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        posterName = findViewById(R.id.posterName);
        timeType = findViewById(R.id.postViewType);
        commentsList = findViewById(R.id.commentsList);
        commentInput = findViewById(R.id.commentBodyInput);
        postTitle = findViewById(R.id.post_view_title);
        postTime = findViewById(R.id.post_view_time);
        addComment = findViewById(R.id.addComment);
//        serviceIntent = new Intent(this, NotificationService.class);

        postDescription = findViewById(R.id.post_view_description);
        postDescription.setMovementMethod(new ScrollingMovementMethod());


        initialize();

        addComment.setOnClickListener(this);
        /*
        if(currentPost.getImg() != null) {
            postImg.setVisibility(View.VISIBLE);
            postImg.setOnClickListener(this);
        }
        */

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts/"+currentPost.getPostId()).child("comments");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Comment> listComments = new ArrayList<>();
                final long[] pendingLoadCount = { dataSnapshot.getChildrenCount() };
                for(final DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    final Comment current = childSnapshot.getValue(Comment.class);
                    FirebaseDatabase.getInstance().getReference("Users/" + current.getCommenterUser()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            current.setCurrentFullName(dataSnapshot.getValue(UserInformation.class).getFullName());

                            listComments.add(current);

                            pendingLoadCount[0] = pendingLoadCount[0] - 1;

                            if(pendingLoadCount[0] == 0){
                                Log.d(TAG, "onDataChange: " + listComments.size());
                                Collections.reverse(listComments);
                                CommentsAdapter adapter = new CommentsAdapter(PostView.this, R.layout.forum_row_comment, listComments);
                                commentsList.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(PostView.this, "Post Loading Failed", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PostView.this, "Post Loading Failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        /*commentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment currentComment = (Comment) parent.getItemAtPosition(position);

                if(!currentComment.getCommenterUser().equals(user.getUid())) {
                    sendMessage(currentComment.getCommenterEmail(), currentComment.getCurrentPhone(), currentComment.isTechnician());
                }
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forum_action_bar, menu);

        if(currentPost.getUserId().equals(user.getUid())) {
            menu.findItem(R.id.menu_delete).setVisible(true);
            Log.d(TAG, "onCreateOptionsMenu: current post solved? " + currentPost.isSolved());
            if (!currentPost.isSolved()) {
                menu.findItem(R.id.menu_mark_solved).setVisible(true);
            }else {
                menu.findItem(R.id.menu_mark_solved).setVisible(false);
            }
        }

        if(currentPostFollowing != null){
            if(currentPostFollowing.equals(currentPost.getPostId())) {
                Log.d(TAG, "onCreateOptionsMenu: the current post is followed");
                menu.findItem(R.id.menu_follow_post).setIcon(R.drawable.menu_unfollow).setTitle("Unfollow Post");
                following = true;
            }else{
                menu.findItem(R.id.menu_follow_post).setVisible(false);
            }
        }
        return true;
    }

    private void initialize(){
        currentPost = new Post(getIntent().getStringExtra("postTitle"),getIntent().getStringExtra("postDescription"), getIntent().getStringExtra("userId"), getIntent().getStringExtra("postId"), getIntent().getStringExtra("postEmail"));
        currentPost.setTimestamp(getIntent().getLongExtra("postTime", 0));
        currentPost.setImg(getIntent().getStringExtra("postImg"));
        currentPost.setSolved(getIntent().getBooleanExtra("postSolved", false));
        currentPost.setEdited(getIntent().getBooleanExtra("postEdited", false));
        getSupportActionBar().setTitle(currentPost.getTitle());
        if(currentPost.isSolved()){

            timeType.setText("Solved On:");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightGreen)));
            if(Build.VERSION.SDK_INT >= 21){
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.darkGreen));
            }

        }
        else if(currentPost.isEdited()){
            timeType.setText("Edited On:");
        }

        /*FirebaseDatabase.getInstance().getReference("users/"+currentPost.getUserId()).child("img")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(String.class) != null){
                            Glide.with(PostView.this).load(dataSnapshot.getValue(String.class)).apply(RequestOptions.circleCropTransform())
                                    .into(posterImg);
                        }
                        else{
                            posterImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_profile));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
        posterName.setText(currentPost.getEmail());


        postTitle.setText(currentPost.getTitle());
        postDescription.setText(currentPost.getDescription());
        setPostTime(currentPost.getTimestamp());

    }

    private void setPostTime(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy HH:mm",cal).toString();

        postTime.setText(date);
    }

    public void addCommentToDatabase(final Comment comment){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts/"+currentPost.getPostId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Log.d(TAG, "onDataChange: " + post.toString());
                post.addComment(comment);
                reference.setValue(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_delete:
                AlertDialog deleteAlert = new AlertDialog.Builder(this)
                        .setTitle("Delete Post")
                        .setMessage("Are You Sure You Want To Delete This Post?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePost();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                deleteAlert.show();
                deleteAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
                deleteAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                return true;
            case R.id.menu_mark_solved:
                markPostSolved();
                return true;
            case R.id.menu_follow_post:
                if(following){
                    unFollowPost();
                    item.setIcon(R.drawable.menu_follow).setTitle("Follow Post");
                }else{
                    followPost();
                    item.setIcon(R.drawable.menu_unfollow).setTitle("Unfollow Post");
                }
                return true;
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }

    private void followPost(){
        currentPostFollowing = currentPost.getPostId();
        startService(serviceIntent);
        following = true;
    }

    private void unFollowPost(){
        stopService(serviceIntent);
        following = false;
    }

    private void markPostSolved(){
        AlertDialog solvedDialog = new AlertDialog.Builder(this)
                .setTitle("Mark Post as Solved")
                .setMessage("Are you sure you want to mark this post as solved?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog solvedProgress = new ProgressDialog(PostView.this);
                        solvedProgress.setMessage("Loading");
                        solvedProgress.setCancelable(false);
                        solvedProgress.show();

                        final DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts/" +currentPost.getPostId());
                        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Post post = dataSnapshot.getValue(Post.class);
                                post.setSolved(true);
                                final long solvedTime = Calendar.getInstance().getTimeInMillis();
                                post.setTimestamp(solvedTime);
                                postRef.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        solvedProgress.dismiss();
                                        if(task.isSuccessful()){
                                            Toast.makeText(PostView.this, "Post Marked as Solved", Toast.LENGTH_SHORT).show();
                                            currentPost.setSolved(true);
                                            timeType.setText("Solved On:");
                                            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightGreen)));
                                            if(Build.VERSION.SDK_INT >= 21){
                                                Window window = getWindow();
                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                                window.setStatusBarColor(getResources().getColor(R.color.darkGreen));
                                            }
                                            supportInvalidateOptionsMenu();
                                            setPostTime(solvedTime);
                                        }else{
                                            Toast.makeText(PostView.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        solvedDialog.show();
        solvedDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        solvedDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }

    private void deletePost() {
        final ProgressDialog deleteProgress = new ProgressDialog(this);
        deleteProgress.setMessage("Deleting Post...");
        deleteProgress.show();
        deleteProgress.setCancelable(false);
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts/" + currentPost.getPostId());
        postRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    if(currentPost.getImg() != null) {
//                        StorageReference postImgRef = FirebaseStorage.getInstance().getReference("posts/" + currentPost.getPostId());
//                        postImgRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                deleteProgress.dismiss();
//                                if (task.isSuccessful()) {
//                                    finish();
//                                    Toast.makeText(PostView.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(PostView.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }else {
                        deleteProgress.dismiss();
                        finish();
                        Toast.makeText(PostView.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    deleteProgress.dismiss();
                    Toast.makeText(PostView.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == addComment){
            if(TextUtils.isEmpty(commentInput.getText().toString().trim())){
                commentInput.setError("Comment Can't Be Empty");
            }else {
                Log.d(TAG, "onClick: " + user.getDisplayName());
                String[] currentUserDetails = user.getDisplayName().split(Pattern.quote("|"),4);
                addCommentToDatabase(new Comment(user.getUid(), commentInput.getText().toString(), false, user.getEmail()));

                commentInput.setText("");
                commentInput.clearFocus();
                ((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
            }
        }
    }
}
