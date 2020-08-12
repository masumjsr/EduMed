package com.edu.med.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.edu.med.R;
import com.edu.med.mr.model.PostModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    RecyclerView recycler;
    ProgressDialog progressDialog;
    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View view= inflater.inflate(R.layout.fragment_post, container, false);
        recycler=view.findViewById(R.id.recyclerview);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.show();

        setupDatabase();
    return  view;}

    private void setupDatabase() {
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(manager);
        Query query= FirebaseDatabase.getInstance().getReference("approve");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<PostModel>().setQuery(query, new SnapshotParser<PostModel>() {
            @NonNull
            @Override
            public PostModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new PostModel(
                        getData(snapshot.child("name").getValue().toString()),
                        getData(snapshot.child("image").getValue().toString()),
                        getData(snapshot.child("post_id").getValue().toString()),
                        getData(snapshot.child("text").getValue().toString()),
                        getData(snapshot.child("user_id").getValue().toString()),
                        getData(snapshot.child("category").getValue().toString()),
                        getData(snapshot.child("post_image").exists() ? snapshot.child("post_image").getValue().toString() : ""),
                        getData2(snapshot.child("time").getValue(Long.class)));
            }
        }).build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<PostModel, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post22,parent,false));
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressDialog.dismiss();
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull PostModel postModel) {
                viewHolder.setDetails(postModel.getName(), postModel.getImage(), postModel.getPost_id(), postModel.getText(), postModel.getTime(), postModel.getUser_id(), postModel.getCategory(),postModel);


            }


        };
        recycler.setAdapter(adapter);
        adapter.startListening();

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



    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_txt, time_txt, text_txt, comment_text, category_text;
        ImageView photo,post_image;
        RelativeLayout image_holder;
        Context context;
        String total_comment;
        Button approve,delete;
        FrameLayout frameLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_text = itemView.findViewById(R.id.category);

            name_txt = itemView.findViewById(R.id.post_name);
            text_txt = itemView.findViewById(R.id.post_text);
            time_txt = itemView.findViewById(R.id.post_time);
            photo = itemView.findViewById(R.id.post_icon);
            post_image=itemView.findViewById(R.id.post_image);
            image_holder=itemView.findViewById(R.id.image_holder);
            approve=itemView.findViewById(R.id.approve);
            delete=itemView.findViewById(R.id.delete);
            frameLayout=itemView.findViewById(R.id.img_frame);

            context=itemView.getContext();
        }


        public void setDetails(String name, String image, final String post_id, String text, long time, String user_id, String category, PostModel model) {


//            post_image.setImageDrawable(itemView.getContext().getDrawable(R.drawable.empty));
            image_holder.setVisibility(View.GONE);
            Log.i("123321", "text:" + text + "  image:" + model.getPost_image());
            name_txt.setText(name);
            if (!model.getPost_image().equals("")) {
                image_holder.setVisibility(View.VISIBLE);

                    setUpImage(model.getPost_image(),frameLayout);
              //  Picasso.get().load(model.getPost_image()).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(post_image);

            }
            time_txt.setText(reformat(time));
            category_text.setText(category);

            try {
                text_txt.setText(Html.fromHtml(text));
            } catch (Exception e) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("loading");
                    progressDialog.show();
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("approve").child(post_id);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("123321", dataSnapshot+"");

                            Notify2.notify(dataSnapshot.child("post_id").getValue().toString(),"post","post",dataSnapshot.child("user_id").getValue().toString(),"Your post Successfully approved by an admin");
                            DatabaseReference databaseReference0=FirebaseDatabase.getInstance().getReference("post");
                            databaseReference0.child(dataSnapshot.child("post_id").getValue().toString()).setValue(dataSnapshot.getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("approve").child(post_id);
                                    databaseReference.removeValue();
                                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("loading");
                    progressDialog.show();
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("approve").child(post_id);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("123321", dataSnapshot+"");
                            Notify2.notify(dataSnapshot.child("post_id").getValue().toString(),"post","post",dataSnapshot.child("user_id").getValue().toString(),"Sorry! Your post not follow our rules");

                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("approve").child(post_id);
                            databaseReference.removeValue();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });






            Picasso.get().load(image).placeholder(R.drawable.ic_user).into(photo);
            time_txt.setText(reformat(time));



        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void setUpImage(String post_image, FrameLayout frameLayout) {

            List<String> elephantList = Arrays.asList(post_image.split(","));

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int sst = width - 20;
            Log.d("Width is", String.valueOf(sst));
            Log.d("Width is", String.valueOf(width));
            int i = elephantList.size();
            if (i == 1) {
                ImageView imageView = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        1000));

                frameLayout.addView(imageView);
            }
            if (i == 2) {
                ImageView imageView = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        1000));
                ImageView imageView1 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setX(width / 2);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        1000));
                frameLayout.addView(imageView);
                frameLayout.addView(imageView1);
            }
            if (i == 3) {
                ImageView imageView = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(0)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView);

                imageView.setPadding(0, 5, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        1000));
                ImageView imageView1 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setX(width / 2);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        500));
                ImageView imageView2 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);

                //  imageView2.setImageResource(R.drawable.img3);
                imageView2.setX(width / 2);
                imageView2.setY(500);
                imageView2.setPadding(5, 5, 0, 0);
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        500));
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
                        1000));
                ImageView imageView1 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(1)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView1);

                imageView1.setX(width / 2);
                imageView1.setPadding(5, 5, 0, 0);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        500));
                ImageView imageView2 = new ImageView(getActivity());
                Picasso.get().load(elephantList.get(2)).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(imageView2);
                imageView2.setForeground(getActivity().getDrawable(R.drawable.forground));
                //  imageView2.setImageResource(R.drawable.img3);
                imageView2.setX(width / 2);
                imageView2.setY(500);
                imageView2.setPadding(5, 5, 0, 0);
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        500));

                TextView textView;
                textView = new TextView(getActivity());
                textView.setText("+ "+(elephantList.size()-3));
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
        }
    }
    private String reformat(long time) {
        long system = System.currentTimeMillis();
        long difference = system - time;
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
