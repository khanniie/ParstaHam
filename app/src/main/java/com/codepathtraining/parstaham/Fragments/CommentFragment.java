package com.codepathtraining.parstaham.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepathtraining.parstaham.Adapters.CommentAdapter;
import com.codepathtraining.parstaham.Models.Comment;
import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void onReturnToHome(int commentNum, int array_pos);
    }

    private Post post;
    private CommentAdapter adapter;
    private RecyclerView rv_comments;
    private ArrayList<Comment> comments;
    private int array_pos;
    private Context context;
    private OnFragmentInteractionListener mListener;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance(Post p, int array_pos) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelable("POST", p);
        args.putInt("POS", array_pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = getArguments().getParcelable("POST");
            array_pos = getArguments().getInt("POS");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rv_comments = view.findViewById(R.id.rv_Comments);
        comments = new ArrayList<>();

        adapter = new CommentAdapter(comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(context));
        rv_comments.setAdapter(adapter);
        loadComments();

        final EditText et_comment = view.findViewById(R.id.et_comment);
        Button btn_comment = view.findViewById(R.id.btn_comment);

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Comment comment = new Comment();
                comment.setOwner(ParseUser.getCurrentUser());
                comment.setPost(post);
                comment.setCommentText(et_comment.getText().toString());
                comment.saveInBackground(
                        new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    comments.add(comment);
                                    adapter.notifyItemChanged(comments.size() - 1);
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onAttach(Context c) {
        context = c;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void loadComments(){
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.getQuery(Comment.class).whereEqualTo("post", post).findInBackground(
                new FindCallback<Comment>() {
                    @Override
                    public void done(List<Comment> objects, ParseException e) {
                        if (e == null) {
                            comments.addAll(objects);
                            adapter.notifyDataSetChanged();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
