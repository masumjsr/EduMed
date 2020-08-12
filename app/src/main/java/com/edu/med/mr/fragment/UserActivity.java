package com.edu.med.mr.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devs.readmoreoption.ReadMoreOption;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.edu.med.EditActivity;
import com.edu.med.R;
import com.edu.med.mr.SignInActivity;
import com.edu.med.mr.model.PostModel;
import com.edu.med.mr.utils.Utils;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter adapter;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    FirebaseUser user;
    String total_like, total_comment, total_like2;


    String user_id, user_name, user_profile;
    @BindView(R.id.toolbar2)
    Toolbar toolbar2;
    @BindView(R.id.profile)
    CircleImageView profile;
    @BindView(R.id.name)
    TextView name;

    public UserActivity() {
        // Required empty public constructor

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile2);
        ButterKnife.bind(this);
        user_id = getIntent().getStringExtra("user_id");
        user_name = getIntent().getStringExtra("user_name");
        user_profile = getIntent().getStringExtra("user_profile");
        name.setText(user_name);
        Picasso.get().load(user_profile).placeholder(R.drawable.ic_user).into(profile);
        TextView textView = toolbar2.findViewById(R.id.name_user_tool);
        textView.setText(user_name);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            user = FirebaseAuth.getInstance().getCurrentUser();

            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(manager);
            setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByChild("user_id").equalTo(user_id));

        }

    }


    private void setupRecycler(Query query) {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<PostModel>().setQuery(
                query, snapshot -> new PostModel(snapshot.child("name").getValue().toString(), snapshot.child("image").getValue().toString(), snapshot.child("post_id").getValue().toString(),
                        snapshot.child("text").getValue().toString(), snapshot.child("user_id").getValue().toString(), snapshot.child("category").getValue().toString(), snapshot.child("post_image").getValue().toString(), snapshot.child("time").getValue(Long.class))).build();

        adapter = new FirebaseRecyclerAdapter<PostModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull PostModel postModel) {

                viewHolder.setDetails(postModel.getName(), postModel.getImage(), postModel.getPost_id(), postModel.getText(), postModel.getTime(), postModel.getUser_id(), postModel.getCategory(), postModel);
            }


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false));
            }

            @Override
            public void onDataChanged() {
                //   Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_txt, time_txt, text_txt, comment_text, like_text, category_text, like_text2;
        ImageView photo, comment_icon, like_icon, more, like_icon2, post_image;
        boolean liked = false;
        boolean liked2 = false;
        PowerMenu powerMenu;
        CardView like, improve, comment;

        RelativeLayout image_holder;

        FrameLayout frameLayout;

        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_text = itemView.findViewById(R.id.category);
            more = itemView.findViewById(R.id.more);
            name_txt = itemView.findViewById(R.id.post_name);
            text_txt = itemView.findViewById(R.id.post_text);
            like_icon = itemView.findViewById(R.id.like_icon);
            like_icon2 = itemView.findViewById(R.id.like_icon2);
            time_txt = itemView.findViewById(R.id.post_time);
            photo = itemView.findViewById(R.id.post_icon);
            comment_icon = itemView.findViewById(R.id.comment_icon);
            comment_text = itemView.findViewById(R.id.post_comment);
            like_text = itemView.findViewById(R.id.post_like);
            like_text2 = itemView.findViewById(R.id.post_like2);
            like = itemView.findViewById(R.id.like);
            improve = itemView.findViewById(R.id.improve);
            comment = itemView.findViewById(R.id.comment);
            post_image = itemView.findViewById(R.id.post_image);
            image_holder = itemView.findViewById(R.id.image_holder);
            frameLayout=itemView.findViewById(R.id.frame);



        }

        private void setUpImage(String post_image, FrameLayout frameLayout) {

            List<String> elephantList = Arrays.asList(post_image.split(","));

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x-60;
            int sst = width - 20;
            Log.d("Width is", String.valueOf(sst));
            Log.d("Width is", String.valueOf(width));
            int i = elephantList.size();
            if (i == 1) {


                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(5, 5, 5, 5);

                imageView.setLayoutParams(new FrameLayout.LayoutParams(width, 400));
                frameLayout.addView(imageView);
            }
            if (i == 2) {

                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(5, 5, 5, 5);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width ,
                        800/2));


                ImageView imageView1 = new ImageView(UserActivity.this);
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
                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);
                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width ,
                        800/2));

                ImageView imageView1 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setY(400);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams((width / 2)-3,
                        400));

                ImageView imageView2 = new ImageView(UserActivity.this);
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
                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);
                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams((width/2 )-3,
                        800/2));

/////////********************3
                ImageView imageView4 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView4);

                imageView4.setPadding(0, 5, 0, 0);
                imageView4.setX((width / 2)+3);
                imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView4.setLayoutParams(new FrameLayout.LayoutParams(width/2 ,
                        800/2));




                ImageView imageView1 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setY(400);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams((width / 2)-3,
                        400));

                ImageView imageView2 = new ImageView(UserActivity.this);
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

                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);
                imageView.setPadding(0, 5, 3, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams((width/2)-3 ,
                        800/2));



                ImageView imageView4 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView4);

                imageView4.setPadding(3, 5, 0, 0);
                imageView4.setX((width / 2)+3);
                imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView4.setLayoutParams(new FrameLayout.LayoutParams((width/2 )-3,
                        800/2));



                ImageView imageView1 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setY(400);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams((width / 2)-3,
                        400));



                ImageView imageView2 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(3)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);

                imageView2.setX((width / 2)+3);
                imageView2.setY(400);
                imageView2.setPadding(5, 5, 5, 0);
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        400));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageView2.setForeground(UserActivity.this.getDrawable(R.drawable.forground));
                }
                TextView textView;
                textView = new TextView(UserActivity.this);
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
                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        800));

                frameLayout.addView(imageView);
            }
            if (i == 2) {
                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width,
                        800 / 2));
                ImageView imageView1 = new ImageView(UserActivity.this);
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
                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        800));
                ImageView imageView1 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setX(width / 2);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        400));
                ImageView imageView2 = new ImageView(UserActivity.this);
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
                ImageView imageView = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        800));
                ImageView imageView1 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setX(width / 2);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        400));
                ImageView imageView2 = new ImageView(UserActivity.this);
                Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageView2.setForeground(UserActivity.this.getDrawable(R.drawable.forground));
                }
                //  imageView2.setImageResource(R.drawable.img3);
                imageView2.setX(width / 2);
                imageView2.setY(400);
                imageView2.setPadding(5, 5, 0, 0);
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        400));

                TextView textView;
                textView = new TextView(UserActivity.this);
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


        public void setDetails(String name, String image, String post_id, String text, long time, String user_id, String category, PostModel model) {
            if (text.equals("")) {
                text_txt.setVisibility(View.GONE);
            } else {
                text_txt.setVisibility(View.VISIBLE);
            }
            name_txt.setText(name);
            text_txt.setText(Html.fromHtml(text));
            time_txt.setText(reformat(time));
            if (!model.getPost_image().equals("")) {
                image_holder.setVisibility(View.VISIBLE);

                setUpImage(model.getPost_image(),frameLayout);

            }else {
                image_holder.setVisibility(View.GONE);
            }
            category_text.setText(category);
            DatabaseReference comment_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("comment");
            comment_count.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        total_comment = dataSnapshot.getChildrenCount() + "";
                        comment_text.setText(total_comment);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference like_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like");
            like_count.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    total_like = dataSnapshot.getChildrenCount() + "";
                    like_text.setText(total_like);
                    if (user != null) {


                        if (dataSnapshot.child(user.getUid() + "").exists()) {
                            liked = true;
                            like_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart2));

                        } else {
                            liked = false;
                            like_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DatabaseReference like_database = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like").child(user.getUid());
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
                            like_icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike_active));

                        } else {
                            liked2 = false;
                            like_icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike_normal));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            like.setOnClickListener(v ->

            {
                if (user != null) {


//                like_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                    if (liked) {
                        like_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                        liked = false;
                        like_database.removeValue();


                    } else {
                        if (liked2) {

                            like_icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike_active));
                            liked2 = false;
                            like_database2.removeValue();


                        }
                        liked = true;
                        like_database.child(user_id).setValue("true");
                        like_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart2));
                        like_database.setValue(true);

                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getApplicationContext().startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();

                }

            });


            improve.setOnClickListener(v ->

            {
                if (user != null) {


//                like_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                    if (liked2) {

                        like_icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike_active));
                        liked2 = false;
                        like_database2.removeValue();


                    } else {
                        if (liked) {
                            like_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                            liked = false;
                            like_database.removeValue();


                        }
                        liked2 = true;
                        like_database2.child(user_id).setValue("true");
                        like_icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike_normal));


                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getApplicationContext().startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();

                }

            });


            frameLayout.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent.putExtra("text", text);
                intent.putExtra("name", name);
                intent.putExtra("image", image);
                intent.putExtra("post_id", post_id);
                intent.putExtra("total_comment", comment_text.getText().toString());
                intent.putExtra("total_like", total_like);
                intent.putExtra("time", reformat(time));
                intent.putExtra("user_id", user_id);
                intent.putExtra(Utils.post_image, model.getPost_image());
                startActivity(intent);
            });


            Picasso.get().load(image).placeholder(R.drawable.ic_user).into(photo);
            time_txt.setText(reformat(time));
            comment.setOnClickListener(v -> {


                Intent intent = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent.putExtra("text", text);
                intent.putExtra("name", name);
                intent.putExtra("image", image);
                intent.putExtra("post_id", post_id);
                intent.putExtra("total_comment", total_comment);
                intent.putExtra("total_like", total_like);
                intent.putExtra("time", reformat(time));
                intent.putExtra(Utils.post_image, model.getPost_image());
                startActivity(intent);


                // OR using options to customize

                ReadMoreOption readMoreOption = new ReadMoreOption.Builder(getApplicationContext())
                        .textLength(200, ReadMoreOption.TYPE_CHARACTER) // OR
                        //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
                        .moreLabel("MORE")
                        .lessLabel("LESS")
                        .moreLabelColor(Color.RED)
                        .lessLabelColor(Color.BLUE)
                        .labelUnderLine(true)
                        .expandAnimation(true)
                        .build();

                readMoreOption.addReadMoreTo(text_txt, Html.fromHtml(text));


            });

            more.setOnClickListener(v ->
            {
                Context context = getApplicationContext();

                powerMenu = new PowerMenu.Builder(UserActivity.this)
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
                } else powerMenu.addItem(new PowerMenuItem("Report"));
                powerMenu.showAsDropDown(v);
            });

            onMenuItemClickListener = (position, item) -> {
                Toast.makeText(getApplicationContext(), item.getTitle() + position, Toast.LENGTH_SHORT).show();
                powerMenu.setSelectedPosition(position); // change selected item
                switch (item.getTitle()) {
                    case "Report": {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(post_id);
                        databaseReference.child("user_id").setValue(user.getUid());
                        Toast.makeText(getApplicationContext(), "Report Success", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case "Delete":
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                        builder.setTitle("Delete Post");
                        builder.setMessage("Are you Sure !");
                        builder.setPositiveButton("ok", (dialog, which) -> {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(post_id);
                            databaseReference.removeValue();
                            Toast.makeText(getApplicationContext(), "Post Deleted", Toast.LENGTH_SHORT).show();
                        });
                        builder.setNegativeButton("cancel", null);
                        builder.show();
                        break;
                    case "Edit": {
                        Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                        intent.putExtra("post_id", post_id);
                        intent.putExtra("text", text);
                        intent.putExtra("image", model.getPost_image());
                        startActivity(intent);
                    }


                    break;
                }
                powerMenu.dismiss();
            };
        }

        private String reformat(long time) {
            long system = System.currentTimeMillis();
            long difference = system - time;
            //   Log.i("123321", difference + " dif");
            if (difference < 1000 * 60 * 60) {
                return difference / (1000 * 60) + " min ago";
            } else if (difference < 1000 * 60 * 60 * 24) {
                return difference / (1000 * 60 * 60) + " hour ago";
            } else {
                SimpleDateFormat format = new SimpleDateFormat("dd MMM hh:mm a");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                return format.format(calendar.getTime());
            }
        }
    }
}

