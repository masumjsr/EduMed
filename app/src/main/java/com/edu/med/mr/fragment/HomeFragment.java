package com.edu.med.mr.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.devs.readmoreoption.ReadMoreOption;
import com.edu.med.PostingActivity2;
import com.edu.med.mr.utils.Constant;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.edu.med.EditActivity;
import com.edu.med.PostingActivity;
import com.edu.med.R;
import com.edu.med.mr.MainActivity;
import com.edu.med.mr.Notify;
import com.edu.med.mr.SignInActivity;
import com.edu.med.mr.model.Model;
import com.edu.med.mr.model.PostModel;
import com.edu.med.mr.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ProgressBar progressBar2;
    NestedScrollView nested;
    TextView textView15;
    ImageView more;
    ImageView likeIcon;
    TextView postLike;
    CardView like;
    ImageView likeIcon2;
    TextView postLike2;
    CardView improve;
    ImageView commentIcon;
    TextView postComment;
    CardView comment;
    TextView category;
    TextView postText;
    ImageView postImage;
    RelativeLayout imageHolder;
    TextView postName;
    TextView postTime;
    CircleImageView postIcon;
    LinearLayout pin;

    private boolean mIsLoading = false;
    TextView postEdit;
    CircleImageView profile;
    private FirebaseUser user;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerViewContainer;
    SwipeRefreshLayout swipe;
    LinearLayout frameLayout;
    TextView textView9;
    TextView pinTitle;
    ImageView pinImage;
    LinearLayout top;
    ScrollView scrollView2;
    PostAdapter adapter;
    private ImageView photo;
    private ImageView remove;
    private String image_link = "";
    private Intent data;
    String total_like, total_comment;
    String total_like2, total_comment2;
    boolean liked = false;
    boolean liked2 = false;


    OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener;
    PowerMenu powerMenu;

    public HomeFragment() {
        // Required empty public constructor
    }

    private ProgressBar imageUploadProgress;
    private List<String> item = new ArrayList<>();
    private List<String> key = new ArrayList<>();
    private List<PostModel> postList = new ArrayList<>();
    private boolean isLoading = false;
    private Bitmap bitmap;
    private View dialogView;


    //oncreate
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        frameLayout=view.findViewById(R.id.frameLayout);
        ButterKnife.bind(getActivity(), view);
        likeIcon=view.findViewById(R.id.like_icon);
        more=view.findViewById(R.id.more);
        nested=view.findViewById(R.id.nested);
        recyclerView=view.findViewById(R.id.recycler_view);
        profile=view.findViewById(R.id.profile);
        scrollView2=view.findViewById(R.id.scrollView2);
        pinTitle=view.findViewById(R.id.pin_title);
        swipe=view.findViewById(R.id.swipe);
        top=view.findViewById(R.id.top);
        postIcon=view.findViewById(R.id.post_icon);
        pinImage=view.findViewById(R.id.pin_image);
        progressBar2=view.findViewById(R.id.progressBar2);
        textView15=view.findViewById(R.id.textView15);
        postLike=view.findViewById(R.id.post_like);
        like=view.findViewById(R.id.like);
        pin=view.findViewById(R.id.pin);
        postTime=view.findViewById(R.id.post_time);
        improve=view.findViewById(R.id.improve);
        textView9=view.findViewById(R.id.textView9);
        postEdit=view.findViewById(R.id.post_edit);
        postImage=view.findViewById(R.id.post_image);
        imageHolder=view.findViewById(R.id.image_holder);
        postName=view.findViewById(R.id.post_name);
        postText=view.findViewById(R.id.post_text);
        category=view.findViewById(R.id.category);
        comment=view.findViewById(R.id.comment);
        postComment=view.findViewById(R.id.post_comment);
        commentIcon=view.findViewById(R.id.comment_icon);
        postLike2=view.findViewById(R.id.post_like2);
        likeIcon2=view.findViewById(R.id.like_icon2);
        shimmerViewContainer=view.findViewById(R.id.shimmer_view_container);


        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        submit();
        //Toolbar toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
       // Spinner spinner = toolbar.findViewById(R.id.spinner2);
        BottomNavigationView navBar = getActivity().findViewById(R.id.navigation);
        navBar.setVisibility(View.VISIBLE);

        frameLayout.requestFocus();

        user = FirebaseAuth.getInstance().getCurrentUser();
        //initScrollListener();
        postSetup();
        setPinPost();
        postEdit.setOnClickListener(v -> {
            {
                Log.i("123321", "318:" + image_link);
                image_link = "";
                if (user != null)
                    startActivity(new Intent(getActivity(), PostingActivity2.class));

                else {
                    Builder builder = new Builder(getActivity());
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().startActivity(new Intent(getActivity(), SignInActivity.class));
                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();

                }
            }
        });
        //   setupRecycler1();
        nested.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    if (item.size() != 0)
                        setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().startAt(item.get(item.size() - 1)).limitToFirst(10));
                    progressBar2.setVisibility(View.VISIBLE);
                    isLoading = true;
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
        setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(10));


        swipe.setOnRefreshListener(() -> {
            key.clear();
            item.clear();
            postList.clear();
            adapter.notifyDataSetChanged();
            setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(15));
        });
        Model.setI(0);

     /*   spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((TextView) parent.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (position == 0) {
                    setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(10));
                }


                //  setupRecycler1();

                else {
                   // String txt = spinner.getSelectedItem().toString();
                    setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(10));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
      */  adapter = new PostAdapter(getActivity(), postList);
        adapter.setHasStableIds(true);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapter);
        return view;
    }




    private void setPinPost() {
        pin.setVisibility(View.GONE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/pinPost");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("post_id").exists())
                {
                   DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("post").child(snapshot.child("post_id").getValue().toString());
                   databaseReference1.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if (dataSnapshot.exists()) {
                               pin.setVisibility(View.VISIBLE);
                               setDetails(
                                       dataSnapshot.child("name").getValue().toString(),
                                       dataSnapshot.child("image").getValue().toString(),
                                       dataSnapshot.child("post_id").getValue().toString(),
                                       dataSnapshot.child("text").getValue().toString(),
                                       dataSnapshot.child("time").getValue(Long.class),
                                       dataSnapshot.child("user_id").getValue().toString(),
                                       dataSnapshot.child("category").getValue().toString(),
                                       dataSnapshot.child("post_image").getValue().toString()

                               );
                           }
                           else
                           pin.setVisibility(View.GONE);
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
                }
                else pin.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setDetails(String name, String image, String post_id, String text, long time, String user_id, String Scategory, String post_image) {
        postImage.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        Context context = getActivity();
        imageHolder.setVisibility(View.GONE);

        if (text.equals("")) {
            postText.setVisibility(View.GONE);
        } else {
            postText.setVisibility(View.VISIBLE);
        }
        postName.setText(name);
        if (!post_image.equals("")) {
            imageHolder.setVisibility(View.VISIBLE);


            Picasso.get().load(post_image).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(postImage, new Callback() {
                @Override

                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.i("123321", "675:" + e.getMessage());
                }
            });

        }
        // text_txt.setText(Html.fromHtml(text));
        postTime.setText(reformat(time));
        category.setText(Scategory);

        postImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra("text", text);
            intent.putExtra("name", name);
            intent.putExtra("image", image);
            intent.putExtra("post_id", post_id);
            intent.putExtra("total_comment", postComment.getText().toString());
            intent.putExtra("total_like", total_like);
            intent.putExtra("time", reformat(time));
            intent.putExtra("user_id", user_id);
            intent.putExtra(Utils.post_image, post_image);
            assert context != null;
            context.startActivity(intent);
        });
        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(getActivity())
                .moreLabel("Read more")
                .moreLabelColor(getResources().getColor(R.color.colorPrimary2))
                .expandAnimation(true)
                .build();
        readMoreOption.addReadMoreTo(postText, Html.fromHtml(text));


        DatabaseReference comment_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("comment");
        comment_count.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    total_comment = dataSnapshot.getChildrenCount() + "";

                    postComment.setText(total_comment);
                } catch (Exception e) {
                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference like_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like");
        like_count.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_like = dataSnapshot.getChildrenCount() + "";

                postLike.setText(total_like);
                if (user != null) {


                    if (dataSnapshot.child(user.getUid() + "").exists()) {
                        liked = true;
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart2));


                    } else {
                        liked = false;
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference like_database = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference like_database2 = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like2").child(user.getUid());


        DatabaseReference like_count2 = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like2");
        like_count2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_like2 = dataSnapshot.getChildrenCount() + "";
                postLike2.setText(total_like2);
                if (user != null) {


                    if (dataSnapshot.child(user.getUid()).exists()) {
                        liked2 = true;
                        likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_active));

                    } else {
                        liked2 = false;
                        likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_normal));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        like.setOnClickListener(v ->

        {

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("loading");
            progressDialog.show();
            if (user != null) {


                //                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                if (liked) {
                    likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                    liked = false;
                    like_database.removeValue();


                } else {
                    if (liked2) {

                        likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_normal));
                        liked2 = false;
                        like_database2.removeValue();


                    }
                    liked = true;
                    // like_database.child(user_id).setValue(name);
                    likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart2));
                    like_database.setValue(user.getDisplayName()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("123321", "491: liked =" + liked);
                            Notify.notify(post_id, post_id, total_like, "like", "post", name, user_id, "");
                        }
                    });


                }
                progressDialog.dismiss();

            } else {
                progressDialog.dismiss();
                Builder builder = new Builder(context);
                builder.setTitle("Sign In Required");
                builder.setMessage("You need to Sign In to use the feature");
                builder.setPositiveButton("ok", (dialog, which) -> context.startActivity(new Intent(context, SignInActivity.class)));
                builder.setNegativeButton("cancel", null);
                builder.show();

            }

        });


        improve.setOnClickListener(v ->

        {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("loading");
            progressDialog.show();
            if (user != null) {


                //                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                if (liked2) {

                    likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_normal));
                    liked2 = false;
                    like_database2.removeValue();


                } else {
                    if (liked) {
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                        liked = false;
                        like_database.removeValue();


                    }
                    liked2 = true;
                    likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_active));
                    like_database2.setValue(user.getDisplayName()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("123321", "491: liked =" + liked);
                            Notify.notify(post_id, post_id, total_like2, "like2", "post", name, user_id, "");
                        }
                    });


                }
                progressDialog.dismiss();

            } else {
                progressDialog.dismiss();
                Builder builder = new Builder(context);
                builder.setTitle("Sign In Required");
                builder.setMessage("You need to Sign In to use the feature");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(context, SignInActivity.class));
                    }
                });
                builder.setNegativeButton("cancel", null);
                builder.show();

            }

        });


        postIcon.setOnClickListener(v -> intentUser(user_id, name, image));


        Picasso.get().load(image).placeholder(R.drawable.ic_user).into(postIcon);
        postTime.setText(reformat(time));
        comment.setOnClickListener(v -> {

            MainActivity activity = (MainActivity) (context);
            activity.setComment(true);
            PostDetailActivity nextFrag = new PostDetailActivity();

            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("text", text);
            intent.putExtra("name", name);
            intent.putExtra("image", image);
            intent.putExtra("post_id", post_id);
            intent.putExtra("total_comment", postComment.getText().toString());
            intent.putExtra("total_like", total_like);
            intent.putExtra("time", reformat(time));
            intent.putExtra("user_id", user_id);
            intent.putExtra(Utils.post_image, post_image);
            context.startActivity(intent);


            // OR using options to customize


        });
        if(!Utils.role.equals("admin"))
            more.setVisibility(View.GONE);

        more.setOnClickListener(v ->
        {


            powerMenu = new PowerMenu.Builder(context)
                    // .addItemList(list) // list has "Novel", "Poerty", "Art"
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(10f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextColor(Color.GRAY)
                    .setTextGravity(Gravity.CENTER)
                    .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                    .setSelectedTextColor(Color.WHITE)
                    .setMenuColor(Color.WHITE)
                    .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setOnMenuItemClickListener(onMenuItemClickListener)
                    .build();
            //    powerMenu.addItem(new PowerMenuItem());
            if (user.getUid().equals(user_id)) {
                powerMenu.addItem(new PowerMenuItem("Edit"));
                powerMenu.addItem(new PowerMenuItem("Unpin getActivity() post"));
                powerMenu.addItem(new PowerMenuItem("Delete"));
            } else {
                powerMenu.addItem(new PowerMenuItem("Delete"));
                powerMenu.addItem(new PowerMenuItem("Report"));
                powerMenu.addItem(new PowerMenuItem("Unpin getActivity() post"));
            }
            powerMenu.showAsDropDown(v);
        });

        onMenuItemClickListener = (position, items) -> {
            //        Toast.makeText(context, item.getTitle() + position, Toast.LENGTH_SHORT).show();
            powerMenu.setSelectedPosition(position); // change selected item
            switch (items.getTitle()) {
                case "Unpin getActivity() post":
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("admin/pinPost");
                    databaseReference1.removeValue();

                break;
                case "Report": {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(post_id);
                    databaseReference.child("user_id").setValue(user.getUid());
                    Toast.makeText(context, "Report Success", Toast.LENGTH_SHORT).show();
                }
                break;
                case "Delete":
                    Builder builder = new Builder(context);
                    builder.setTitle("Delete Post");
                    builder.setMessage("Are you Sure !");
                    builder.setPositiveButton("ok", (dialog, which) -> {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(post_id);
                        databaseReference.removeValue();
                        key.clear();
                        item.clear();
                        postList.clear();
                        adapter.notifyDataSetChanged();
                        setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(15));
                        Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();
                    break;
                case "Edit": {
                    Intent intent = new Intent(getActivity(), EditActivity.class);
                    String finalText = text.replace("<br>", "\n");
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("text", finalText);
                    intent.putExtra("image", post_image);
                    startActivity(intent);
                }


                break;
            }
            powerMenu.dismiss();
        };
    }

    private void intentUser(String user_id, String name, String image) {

        Intent intent = new Intent(getActivity(), UserActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("user_name", name);
        intent.putExtra("user_profile", image);
        startActivity(intent);
    }

    private void setupRecycler(Query query) {
        shimmerViewContainer.startShimmerAnimation();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() >= 1) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("post_id").exists()) {
                            PostModel model = new PostModel(
                                    snapshot.child("name").exists() ? getData(snapshot.child("name").getValue().toString()) : "",
                                    snapshot.child("image").exists() ? getData(snapshot.child("image").getValue().toString()) : "",
                                    getData(snapshot.child("post_id").getValue().toString()),
                                    getData(snapshot.child("text").getValue().toString()),
                                    getData(snapshot.child("user_id").getValue().toString()),
                                    getData(snapshot.child("category").getValue().toString()),
                                    getData(snapshot.child("post_image").exists() ? snapshot.child("post_image").getValue().toString() : ""),
                                    getData2(snapshot.child("time").getValue(Long.class))
                            );
                            if (!key.contains(snapshot.getKey())) {
                                key.add(snapshot.getKey());
                                postList.add(model);
                                item.add(snapshot.getKey());
                            }
                        } else {
                            Log.i("123321", "290:" + snapshot.getKey());
                        }
                    }

                }


                //    shimmerViewContainer.stopShimmerAnimation();
                // shimmerViewContainer.setVisibility(View.GONE);
                swipe.setRefreshing(false);

                if (mIsLoading) {
                    Log.i("123321", "318:" + item.size());
                    adapter.notifyDataSetChanged();
                } else {

                    mIsLoading = true;
                    adapter.notifyDataSetChanged();
                }
                progressBar2.setVisibility(View.GONE);
                if (dataSnapshot.getChildrenCount() >= 10)
                    isLoading = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!item.isEmpty()) {
            if (mIsLoading) {
                Log.i("123321", "318");
                adapter.notifyDataSetChanged();
            } else {
                Log.i("123321", "322");
                mIsLoading = true;
                adapter = new PostAdapter(getActivity(), postList);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);
            }

        }
    }

    private long getData2(Long time) {
        if (time == null)
            return 0;
        else return time;
    }

    private String getData(String name) {
        if (name == null || name.isEmpty() || name.equals(""))
            return "";
        else
            return name;
    }


    private void postSetup() {
        if (user != null) {


            Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.ic_user).into(profile);
        }
    }



    @OnClick(R.id.more)
    public void abc() {
        setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().startAt(item.get(item.size() - 1)).limitToFirst(5));
    }

    @Override
    public void onStart() {
        super.onStart();
        //mAdapter.startListening();
    }

    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
//        mAdapter.stopListening();
    }

    public void onActivityResult(int i, int j, Intent data) {
        this.data = data;

        if (j == Activity.RESULT_OK) {
            image_link = "";
            uploadToServer("http://nul.com");

            uploadToServer(ImagePicker.Companion.getFilePath(data));

            try {
                Uri selectedImg = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImg);
                imageUploadProgress.setVisibility(View.VISIBLE);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(i, j, data);


    }

    private void uploadToServer(String url) {
        Log.i("123321", "Uploading.................................");
        MediaManager.get().upload(url)
                .unsigned("sample_preset")
                .option("resource_type", "auto")
                .option("folder", "image")
                .option("public_id", "IMG_" + System.currentTimeMillis())
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.i("123321", "start:" + requestId);

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                        Log.i("123321", "total:" + totalBytes + " byte:" + bytes);
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        Log.i("123321", "41:" + resultData);
                        imageUploadProgress.setVisibility(View.GONE);
                        remove.setVisibility(View.VISIBLE);
                        image_link = Objects.requireNonNull(resultData.get("url")).toString();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Log.i("123321", "42:" + error.getDescription());
                        if (!error.getDescription().equals("File 'http://nul.com' does not exist"))
                            Snackbar.make(dialogView, "Something want's wrong!", Snackbar.LENGTH_INDEFINITE).setAction("Retry", v -> {
                                uploadToServer("http://null.com");
                                uploadToServer(ImagePicker.Companion.getFilePath(data));
                            }).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.i("123321", "error:::" + error.getDescription());

                    }
                })
                .dispatch();
    }

    public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ProductViewHolder> {


        private Context mCtx;
        private List<PostModel> productList;

        public PostAdapter(Context mCtx, List<PostModel> productList) {
            this.mCtx = mCtx;
            this.productList = productList;
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.post, null);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            PostModel postModel = productList.get(position);
            holder.setDetails(postModel.getName(), postModel.getImage(), postModel.getPost_id(), postModel.getText(), postModel.getTime(), postModel.getUser_id(), postModel.getCategory(), postModel);
            //loading the image

        }

        @Override
        public int getItemCount() {

            return productList.size();

        }

        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView name_txt, time_txt, text_txt, comment_text, like_text, category_text, like_text2;
            ImageView photo, comment_icon, like_icon, more, like_icon2;
            boolean liked = false;
            boolean liked2 = false;
            RelativeLayout image_holder;

            CardView like, improve, comment;
            Context context;
            String total_like, total_comment;
            String total_like2, total_comment2;
            FirebaseUser user;
            OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener;
            PowerMenu powerMenu;
            FrameLayout frameLayout;

            public ProductViewHolder(View itemView) {
                super(itemView);
                category_text = itemView.findViewById(R.id.category);
                more = itemView.findViewById(R.id.more);
                name_txt = itemView.findViewById(R.id.post_name);
                text_txt = itemView.findViewById(R.id.post_text);
                like_icon = itemView.findViewById(R.id.like_icon);
                like_icon2 = itemView.findViewById(R.id.like_icon2);
                time_txt = itemView.findViewById(R.id.post_time);
                photo = itemView.findViewById(R.id.post_icon);
                image_holder = itemView.findViewById(R.id.image_holder);
                comment_icon = itemView.findViewById(R.id.comment_icon);
                comment_text = itemView.findViewById(R.id.post_comment);
                like_text = itemView.findViewById(R.id.post_like);
                like_text2 = itemView.findViewById(R.id.post_like2);
                like = itemView.findViewById(R.id.like);
                improve = itemView.findViewById(R.id.improve);
                comment = itemView.findViewById(R.id.comment);
                context = itemView.getContext();
                user = FirebaseAuth.getInstance().getCurrentUser();
                frameLayout=itemView.findViewById(R.id.frame);

            }

            private String reformat(long time) {
                long system = System.currentTimeMillis();
                long difference = system - time;
                if (difference < 800 * 60 * 60) {
                    return difference / (800 * 60) + " min ago";
                } else if (difference < 800 * 60 * 60 * 24) {
                    return difference / (800 * 60 * 60) + " hour ago";
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM hh:mm a");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(time);
                    return format.format(calendar.getTime());
                }
            }

            private void intentUser(String user_id, String name, String image) {

                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("user_name", name);
                intent.putExtra("user_profile", image);
                context.startActivity(intent);
            }

            public void setDetails(String name, String image, String post_id, String text, long time, String user_id, String category, PostModel model) {

                image_holder.setVisibility(View.GONE);
                if (text.equals("")) {
                    text_txt.setVisibility(View.GONE);
                } else {
                    text_txt.setVisibility(View.VISIBLE);
                }
                name_txt.setText(name);
                if (!model.getPost_image().equals("")) {
                    image_holder.setVisibility(View.VISIBLE);

                    setUpImage(model.getPost_image(),frameLayout);

                }
                // text_txt.setText(Html.fromHtml(text));
                time_txt.setText(reformat(time));
                category_text.setText(category);
                name_txt.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("text", text);
                    intent.putExtra("name", name);
                    intent.putExtra("image", image);
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("total_comment", comment_text.getText().toString());
                    intent.putExtra("total_like", total_like);
                    intent.putExtra("time", reformat(time));
                    intent.putExtra("user_id", user_id);
                    intent.putExtra(Utils.post_image, model.getPost_image());
                    context.startActivity(intent);
                });
                image_holder.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("text", text);
                    intent.putExtra("name", name);
                    intent.putExtra("image", image);
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("total_comment", comment_text.getText().toString());
                    intent.putExtra("total_like", total_like);
                    intent.putExtra("time", reformat(time));
                    intent.putExtra("user_id", user_id);
                    intent.putExtra(Utils.post_image, model.getPost_image());
                    context.startActivity(intent);
                });
                ReadMoreOption readMoreOption = new ReadMoreOption.Builder(getActivity()).build();
                readMoreOption.addReadMoreTo(text_txt, Html.fromHtml(text));
                try {
                    //text_txt.setText(Html.fromHtml(text));
                } catch (Exception e) {
                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                DatabaseReference comment_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("comment");
                comment_count.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {
                            total_comment = dataSnapshot.getChildrenCount() + "";

                            comment_text.setText(total_comment);
                        } catch (Exception e) {
                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference like_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like");
                like_count.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        total_like = dataSnapshot.getChildrenCount() + "";
                        like_text.setText(total_like);
                        if (user != null) {


                            if (dataSnapshot.child(user.getUid() + "").exists()) {
                                liked = true;
                                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart2));


                            } else {
                                liked = false;
                                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference like_database = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                DatabaseReference like_database2 = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like2").child(user.getUid());
                DatabaseReference like_count2 = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like2");
                like_count2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        total_like2 = dataSnapshot.getChildrenCount() + "";
                        like_text2.setText(total_like2);
                        if (user != null) {


                            if (dataSnapshot.child(user.getUid()).exists()) {
                                liked2 = true;
                                like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_active));

                            } else {
                                liked2 = false;
                                like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_normal));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                like.setOnClickListener(v ->

                {

                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("loading");
                    progressDialog.show();
                    if (user != null) {


                        //                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                        if (liked) {
                            like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                            liked = false;
                            like_database.removeValue();


                        } else {
                            if(liked2) {
                                like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_normal));
                                liked2 = false;


                            like_database2.removeValue(); }
                            liked = true;
                            // like_database.child(user_id).setValue(name);
                            like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart2));
                            like_database.setValue(user.getDisplayName()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.i("123321", "491: liked =" + liked);
                                    Notify.notify(post_id, post_id, total_like, "like", "post", name, user_id, "");



                                }
                            });


                        }
                        progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        Builder builder = new Builder(context);
                        builder.setTitle("Sign In Required");
                        builder.setMessage("You need to Sign In to use the feature");
                        builder.setPositiveButton("ok", (dialog, which) -> context.startActivity(new Intent(context, SignInActivity.class)));
                        builder.setNegativeButton("cancel", null);
                        builder.show();

                    }

                });


                improve.setOnClickListener(v ->

                {
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("loading");
                    progressDialog.show();
                    if (user != null) {


                        //                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                        if (liked2) {

                            like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_normal));
                            liked2 = false;
                            like_database2.removeValue();


                        } else {
                            if(liked)
                            {
                            like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                            liked = false;
                            like_database.removeValue();}
                            liked2 = true;
                            like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlike_active));
                            like_database2.setValue(user.getDisplayName()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.i("123321", "491: liked =" + liked);

                                }Notify.notify(post_id, post_id, total_like2, "like2", "post", name, user_id, "");

                            });


                        }
                        progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        Builder builder = new Builder(context);
                        builder.setTitle("Sign In Required");
                        builder.setMessage("You need to Sign In to use the feature");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, SignInActivity.class));
                            }
                        });
                        builder.setNegativeButton("cancel", null);
                        builder.show();

                    }

                });


                photo.setOnClickListener(v -> intentUser(user_id, name, image));


                Picasso.get().load(image).placeholder(R.drawable.ic_user).into(photo);
                time_txt.setText(reformat(time));
                comment.setOnClickListener(v -> {

                    MainActivity activity = (MainActivity) (context);
                    activity.setComment(true);
                    PostDetailActivity nextFrag = new PostDetailActivity();

                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("text", text);
                    intent.putExtra("name", name);
                    intent.putExtra("image", image);
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("total_comment", comment_text.getText().toString());
                    intent.putExtra("total_like", total_like);
                    intent.putExtra("time", reformat(time));
                    intent.putExtra("user_id", user_id);
                    intent.putExtra(Utils.post_image, model.getPost_image());
                    context.startActivity(intent);


                    // OR using options to customize


                });

                more.setOnClickListener(v ->
                {


                    powerMenu = new PowerMenu.Builder(context)
                            // .addItemList(list) // list has "Novel", "Poerty", "Art"
                            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                            .setMenuRadius(10f) // sets the corner radius.
                            .setMenuShadow(10f) // sets the shadow.
                            .setTextColor(Color.GRAY)
                            .setTextGravity(Gravity.CENTER)
                            .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                            .setSelectedTextColor(Color.WHITE)
                            .setMenuColor(Color.WHITE)
                            .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .setOnMenuItemClickListener(onMenuItemClickListener)
                            .build();
                    //    powerMenu.addItem(new PowerMenuItem());
                    if (user.getUid().equals(user_id)) {
                        powerMenu.addItem(new PowerMenuItem("Edit"));
                        powerMenu.addItem(new PowerMenuItem("Delete"));
                    } else {
                        powerMenu.addItem(new PowerMenuItem("Report"));
                    }
                    Log.i("123321", "1279:" + Utils.role);
                    if (Utils.role.equals("admin") && !user.getUid().equals(user_id)) {

                        powerMenu.addItem(new PowerMenuItem("Delete"));
                        powerMenu.addItem(new PowerMenuItem("Pin getActivity() post"));

                    }
                    if (Utils.role.equals("admin") && user.getUid().equals(user_id)) {
                        powerMenu.addItem(new PowerMenuItem("Pin getActivity() post"));

                    }
                    powerMenu.showAsDropDown(v);
                });

                onMenuItemClickListener = (position, items) -> {
                    //        Toast.makeText(context, item.getTitle() + position, Toast.LENGTH_SHORT).show();
                    powerMenu.setSelectedPosition(position); // change selected item
                    switch (items.getTitle()) {
                        case "Pin getActivity() post": {
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("admin/pinPost");
                        databaseReference1.child("post_id").setValue(post_id).addOnSuccessListener(aVoid ->
                                Toast.makeText(context, "Post Pin Success", Toast.LENGTH_SHORT).show());

                        }
                        break;
                        case "Report": {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(post_id);
                            databaseReference.child("user_id").setValue(user.getUid());
                            Toast.makeText(context, "Report Success", Toast.LENGTH_SHORT).show();
                        }
                        break;
                        case "Delete":
                            Builder builder = new Builder(context);
                            builder.setTitle("Delete Post");
                            builder.setMessage("Are you Sure !");
                            builder.setPositiveButton("ok", (dialog, which) -> {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(post_id);
                                databaseReference.removeValue();
                                key.clear();
                                item.clear();
                                postList.clear();
                                adapter.notifyDataSetChanged();
                                setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(15));
                                Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                            });
                            builder.setNegativeButton("cancel", null);
                            builder.show();
                            break;
                        case "Edit": {
                            Intent intent = new Intent(getActivity(), EditActivity.class);
                            String finalText = text.replace("<br>", "\n");
                            intent.putExtra("post_id", post_id);
                            intent.putExtra("text", finalText);
                            intent.putExtra("image", model.getPost_image());
                            startActivity(intent);
                        }


                        break;
                    }
                    powerMenu.dismiss();
                };
            }


        }
    }

    private String reformat(long time) {
        long system = System.currentTimeMillis();
        long difference = system - time;
        if (difference < 800 * 60 * 60) {
            return difference / (800 * 60) + " min ago";
        } else if (difference < 800 * 60 * 60 * 24) {
            return difference / (800 * 60 * 60) + " hour ago";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM hh:mm a");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return format.format(calendar.getTime());
        }
    }

    private  void submit(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://mrenglish.xyz/pharmacy/" + "home_api.php",
                response -> {


                    JSONObject object= null;
                    try {
                        object = new JSONObject(response);

                    JSONObject jsonObject=object.getJSONArray("data").getJSONObject(0);
                    String sVersion=jsonObject.getString("version");
                    Constant.message=jsonObject.getString("message");
                    Constant.image=jsonObject.getString("image");

                    pinTitle.setText(jsonObject.getString("message"));

        Picasso.get().load(jsonObject.getString("image")).into(pinImage);
                } catch (JSONException e) {
            e.printStackTrace();
        }

                },
                error -> {
                    Log.i("123321", "164:" + error.toString());
                    // As of f605da3 the following should work
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            Log.i("123321", "157:" + res);
                            JSONObject obj = new JSONObject(res);
                            Log.i("123321", "158:" + obj);
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            Log.i("123321", "161:" + e1.getMessage());
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            Log.i("123321", "165:" + e2.getMessage());
                            e2.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("access_key","6808");
                parameters.put("get_home_content","1");
                return parameters;
            }
        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    private void setUpImage(String post_image, FrameLayout frameLayout) {

        List<String> elephantList = Arrays.asList(post_image.split(","));

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x-60;
        int sst = width - 20;
        Log.d("Width is", String.valueOf(sst));
        Log.d("Width is", String.valueOf(width));
       int i = elephantList.size();
        if (i == 1) {


            ImageView imageView = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);
imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
           
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width, 800));
            frameLayout.addView(imageView);
        }
        if (i == 2) {

            ImageView imageView = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

            imageView.setPadding(5, 5, 5, 5);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width ,
                    800/2));


            ImageView imageView1 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

            imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(width ,
                    800/2));
            imageView1.setY(400+5);


            imageView1.setPadding(5, 5, 0, 0);
            frameLayout.addView(imageView);
            //   frameLayout.setLayoutParams(new FrameLayout.LayoutParams(width,800));
            frameLayout.addView(imageView1);
        }
        if (i == 3) {
            ImageView imageView = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);
            imageView.setPadding(0, 5, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width ,
                    800/2));

            ImageView imageView1 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

            imageView1.setY(400);
            imageView1.setPadding(5, 5, 0, 0);
            imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams((width / 2)-3,
                    400));

            ImageView imageView2 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);
            imageView2.setX((width / 2)+3);
            imageView2.setY(400);
            imageView2.setPadding(5, 5, 0, 0);
            imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                    400));
            frameLayout.addView(imageView);
            frameLayout.addView(imageView1);
            frameLayout.addView(imageView2);
        }

        if (i == 4) {


            //**********1*****************
            ImageView imageView = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);
            imageView.setPadding(0, 5, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new FrameLayout.LayoutParams((width/2 )-3,
                    800/2));

/////////********************3
            ImageView imageView4 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView4);

            imageView4.setPadding(0, 5, 0, 0);
            imageView4.setX((width / 2)+3);
            imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView4.setLayoutParams(new FrameLayout.LayoutParams(width/2 ,
                    800/2));




            ImageView imageView1 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

            imageView1.setY(400);
            imageView1.setPadding(5, 5, 0, 0);
            imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams((width / 2)-3,
                    400));

            ImageView imageView2 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(3)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);


            imageView2.setX((width / 2)+3);
            imageView2.setY(400);
            imageView2.setPadding(5, 5, 0, 0);
            imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                    400));
            frameLayout.addView(imageView);
            frameLayout.addView(imageView1);
            frameLayout.addView(imageView2);
            frameLayout.addView(imageView4);
        }
        if (i >= 5) {
            //x=0,y=0

            ImageView imageView = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);
            imageView.setPadding(0, 5, 3, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new FrameLayout.LayoutParams((width/2)-3 ,
                    800/2));



            ImageView imageView4 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView4);

            imageView4.setPadding(3, 5, 0, 0);
            imageView4.setX((width / 2)+3);
            imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView4.setLayoutParams(new FrameLayout.LayoutParams((width/2 )-3,
                    800/2));



            ImageView imageView1 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

            imageView1.setY(400);
            imageView1.setPadding(5, 5, 0, 0);
            imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams((width / 2)-3,
                    400));



            ImageView imageView2 = new ImageView(getActivity());
            Picasso.get().load(elephantList.get(3)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);

            imageView2.setX((width / 2)+3);
            imageView2.setY(400);
            imageView2.setPadding(5, 5, 5, 0);
            imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                    400));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageView2.setForeground(getActivity().getDrawable(R.drawable.forground));
            }
            TextView textView;
            textView = new TextView(getActivity());
            textView.setText("+ " + (i - 3));
            textView.setX(sst / 2);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setTypeface(null, Typeface.BOLD);
            textView.setY(400);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(30);
            textView.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400));
            frameLayout.addView(imageView);
            frameLayout.addView(imageView1);
            frameLayout.addView(imageView2);
            frameLayout.addView(imageView4);
            frameLayout.addView(textView);
        }
      /*  if(elephantList.size()>1) {
            int i = 2;
            if (i == 1) {
                ImageView imageView = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        800));

                frameLayout.addView(imageView);
            }
            if (i == 2) {
                ImageView imageView = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width,
                        800 / 2));
                ImageView imageView1 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setY(400);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams(width,
                        800 / 2));
                frameLayout.addView(imageView);
                frameLayout.addView(imageView1);
            }
            if (i == 3) {
                ImageView imageView = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        800));
                ImageView imageView1 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setX(width / 2);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        400));
                ImageView imageView2 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);

                //  imageView2.setImageResource(R.drawable.img3);
                imageView2.setX(width / 2);
                imageView2.setY(400);
                imageView2.setPadding(5, 5, 0, 0);
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        400));
                frameLayout.addView(imageView);
                frameLayout.addView(imageView1);
                frameLayout.addView(imageView2);
            }
            if (i >= 4) {
                //x=0,y=0
                ImageView imageView = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        800));
                ImageView imageView1 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setX(width / 2);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        400));
                ImageView imageView2 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageView2.setForeground(getActivity().getDrawable(R.drawable.forground));
                }
                //  imageView2.setImageResource(R.drawable.img3);
                imageView2.setX(width / 2);
                imageView2.setY(400);
                imageView2.setPadding(5, 5, 0, 0);
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        400));

                TextView textView;
                textView = new TextView(getActivity());
                textView.setText("+ " + (elephantList.size() - 3));
                textView.setX(sst / 2);
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setTypeface(null, Typeface.BOLD);
                textView.setY(900 / 3 + 900 / 3);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(30);
                textView.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 900 / 3));
                frameLayout.addView(imageView);
                frameLayout.addView(imageView1);
                frameLayout.addView(imageView2);
                frameLayout.addView(textView);
            }
        }*/
    }

}