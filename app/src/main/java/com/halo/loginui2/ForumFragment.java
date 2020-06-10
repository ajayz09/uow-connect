package com.halo.loginui2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ForumFragment extends Fragment implements SearchView.OnQueryTextListener{

    private SearchView searchView;
    private ListView mPostsList;
    List<Post> postList;
    PostAdapter adapter;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private static Post postForEdit;
    private String[] userDetails;
    private RelativeLayout noPosts;
    public static FirebaseUser user;
    private static final String TAG = "ForumFragment";

    public ForumFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).setActionBarTitle("Discussions Forum");
        if(user == null){
            user = FirebaseAuth.getInstance().getCurrentUser();
        }
//        userDetails = user.getDisplayName().split(Pattern.quote("|"),4);
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        reference = FirebaseDatabase.getInstance().getReference("posts");
        mPostsList = view.findViewById(R.id.list_post);
        postList = new ArrayList<>();
        adapter = new PostAdapter(getContext(), R.layout.forum_row_post, postList);
        progressDialog = new ProgressDialog(getContext());
        noPosts = view.findViewById(R.id.noPosts);
        progressDialog.setMessage("Loading Posts...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        reference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    postList.add(childSnapshot.getValue(Post.class));
                }
                Log.d(TAG, "onDataChange: " + postList.size());
                Collections.reverse(postList);
                if (searchView != null)
                    adapter.getFilter().filter(searchView.getQuery());
                if(!postList.isEmpty()) {
                    mPostsList.setAdapter(adapter);
                    noPosts.setVisibility(View.GONE);
                }else{
                    noPosts.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mPostsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),PostView.class);

                Post currentClicked = (Post) parent.getItemAtPosition(position);
                intent.putExtra("postTitle", currentClicked.getTitle());
                intent.putExtra("postDescription", currentClicked.getDescription());
                intent.putExtra("postTime", currentClicked.getTimestamp());
                intent.putExtra("userId", currentClicked.getUserId());
                intent.putExtra("postId", currentClicked.getPostId());
               // intent.putExtra("postImg", currentClicked.getImg());
                intent.putExtra("postEmail", currentClicked.getEmail());
                intent.putExtra("postSolved", currentClicked.isSolved());
                intent.putExtra("postEdited", currentClicked.isEdited());
                startActivity(intent);
            }
        });


        mPostsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(TAG, "onItemLongClick: userId is " + user.getUid());
                if (!((Post) parent.getItemAtPosition(position)).getUserId().equals(user.getUid())) {
                    Toast.makeText(getContext(), "You Can Only Edit Your Posts", Toast.LENGTH_SHORT).show();
                } else {
                    postForEdit = (Post) parent.getItemAtPosition(position);

                    Log.d(TAG, "onItemLongClick: permissions granted");
                    AddPostDialog editDialog = new AddPostDialog();
                    editDialog.setPermissionsGranted(true);
                    editDialog.setPost(postForEdit);
                    editDialog.show(getFragmentManager(), getString(R.string.dialog_post));

                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem addPost = menu.findItem(R.id.action_addPost);
        addPost.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AddPostDialog dialog = new AddPostDialog();
                dialog.setPermissionsGranted(true);
                String a = getString(R.string.dialog_post);

                dialog.showNow(getChildFragmentManager(), getString(R.string.dialog_post));
                return true;
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
