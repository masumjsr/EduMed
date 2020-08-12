package com.edu.med;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class VideoActivityx extends AppCompatActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    ProgressDialog progressDialog;
    String path;
     String     link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

     path= getIntent().getStringExtra("name");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        setupDatabase();
        Log.i("123321", "my "+path);


    }

    private void setupDatabase() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);
        Query query = FirebaseDatabase.getInstance().getReference(path);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<VideoModel>().setQuery(query, snapshot -> {
            try {
                return new VideoModel(snapshot.child("image").getValue().toString(), snapshot.child("name").getValue().toString(), snapshot.child("video").getValue(Integer.class));

            } catch (Exception e) {
                return new VideoModel(snapshot.child("image").getValue().toString(), snapshot.child("name").getValue().toString(),0);

            }
        }).build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<VideoModel, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layoutx, parent, false));
            }

            @Override
            public void onDataChanged() {
                progressDialog.dismiss();
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull VideoModel VideoModel) {
                viewHolder.details(VideoModel);
            }
        };

        recycler.setAdapter(adapter);
        adapter.startListening();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
ImageView cover;ImageView play;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           cover = itemView.findViewById(R.id.nice_video_player);
            play=itemView.findViewById(R.id.imageButton); }

        public void details(VideoModel videoModel) {
            Log.i("123321", "82"+videoModel.getImage()+"\n"+videoModel.getName()+"\n"+videoModel.getVideo());

          // or NiceVideoPlayer.TYPE_NATIVE


            //controller.setImage(getResources().getDrawable(R.drawable.ic_launcher_background));
            Log.i("123321", ""+videoModel.getImage());
            Glide.with(getApplicationContext())
                    .load(videoModel.getImage())
                    .placeholder(R.drawable.logo)
                    .transition(withCrossFade())
                    .into(cover);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent intent=new Intent(getApplicationContext(), Main3Activity_x.class);
                      //  Intent intent=new Intent(getApplicationContext(), FullscreenActivity.class);
                    getLink(videoModel.getVideo());

                    }
            });

            }

    }
    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器

    }
    @Override
    public void onBackPressed() {
finish();
        super.onBackPressed();
    }


    private void getLink(int video_id) {
        String url="https://player.vimeo.com/video/"+video_id+"/config";
     progressDialog.setTitle("Getting Video Info");
     progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.GET, url, (Response.Listener<String>) response -> {

            Log.i("123321", "30:"+response);
            try {
                progressDialog.dismiss();
                JSONObject object=new JSONObject(response).getJSONObject("request").getJSONObject("files");
                JSONArray array=object.getJSONArray("progressive");
                Intent intent=new Intent(getApplicationContext(), Main3Activity_x.class);

                link=array.getJSONObject(2).getString("url");
                intent.putExtra("link",array.getJSONObject(2).getString("url"));

                    startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("123321", "440:"+e.getMessage());
                progressDialog.dismiss();
            }
        }, error -> {
            Log.i("123321", "32:"+error);
            Toast.makeText(this, "Fail :( \n"+error, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

}

