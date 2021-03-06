package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

public class PostsAdapter extends ArrayAdapter<Posts> {

    //Variables.
    Activity activity;
    String loggedInUser;
    private final ViewBinderHelper binderHelper;

    public PostsAdapter(Activity activity, ArrayList<Posts> postList, String loggedInUser) {
        super(activity, 0, postList);
        this.activity = activity;
        this.loggedInUser = loggedInUser;

        binderHelper = new ViewBinderHelper();
        binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater taskInflater = LayoutInflater.from(getContext());
        if (convertView == null) {
            convertView = taskInflater.inflate(R.layout.posts, parent, false);
        }

        //Get current list item.
        final Posts filtered = getItem(position);

        //Set details for list item.
        TextView taskTitle = (TextView) convertView.findViewById(R.id.postTitle);
        TextView taskDesc = (TextView) convertView.findViewById(R.id.postDesc);
        final TextView taskAuthor = (TextView) convertView.findViewById(R.id.postUser);

        taskTitle.setText(filtered.getPostTitle());
        taskDesc.setText(filtered.getPostDesc());
        taskAuthor.setText(filtered.getPostUser());

        //Get post number.
        final String postNumber = filtered.getPostNum();

        //Get swipe layout.
        SwipeRevealLayout swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swla);
        //Bind swipe layout with binder parameters set previously.
        binderHelper.bind(swipeLayout, filtered.getPostNum());

        //Get both aspects of the post layout (list item and swipe layout view), and get image button.
        final RelativeLayout deleteView = (RelativeLayout)convertView.findViewById(R.id.deletePostBtn);
        final RelativeLayout listItemView = (RelativeLayout)convertView.findViewById(R.id.postlayout);
        final ImageView deleteImg = (ImageView)convertView.findViewById(R.id.deleteImg);

        //If the logged in user matches the user of the post, a 'swipe left delete button' is visible, else it is hidden.
        if (loggedInUser.equals(filtered.getPostUser())) {
            //Unlock drag, allowing layout to be swiped out.
            swipeLayout.setLockDrag(false);

            //Check if the heights match between the swipelayout for current list item and the current list item.
            swipeLayout.post(new Runnable() {
                @Override public void run() {
                    //Get heights for each layout in posts.
                    int h1 = deleteView.getMeasuredHeight();
                    int h2 = listItemView.getMeasuredHeight();

                    //If the swipe layout height is smaller than the list item height.
                    if (h1 < h2)
                    {
                        //Update layout1 height to match layout2.
                        ViewGroup.LayoutParams params = deleteView.getLayoutParams();
                        params.height = h2;
                        deleteView.setLayoutParams(params);

                        //Center the delete button image after its container has been resized.
                        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)deleteImg.getLayoutParams();
                        params2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                        deleteImg.setLayoutParams(params2);
                    }
                }
            });

            //OnClickListener for delete button. Runs asynctask to attempt to delete list item.
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Run asynctask.
                    RemovePostsAsync removePost = new RemovePostsAsync(getContext());
                    removePost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, postNumber);

                    RemovePostsAsync.Ifdeleted deleteItem = new RemovePostsAsync.Ifdeleted() {
                        @Override
                        public void successdeleted(final Boolean deleted) {
                            if (deleted) {
                                remove(getItem(position));
                            }
                        }
                    };

                    removePost.setIfdeleted(deleteItem);
                }

            });
        } else {
            //Lock drag on posts not owned by current user, preventing them from deleting them.
            swipeLayout.setLockDrag(true);
        }

        return convertView;
    }
}

