package com.edu.med.mr;

import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edu.med.BuildConfig;
import com.edu.med.MainCategoryFragment;
import com.edu.med.R;
import com.edu.med.admin.AdminActivity;
import com.edu.med.mr.fragment.HomeFragment;
import com.edu.med.mr.fragment.TipsCategoryFragment;
import com.edu.med.mr.fragment.NotificationFragment;
import com.edu.med.mr.fragment.ProfileFragment;
import com.edu.med.mr.fragment.UserActivity;
import com.edu.med.mr.model.Model;
import com.edu.med.mr.utils.Constant;
import com.edu.med.mr.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends AppCompatActivity {
    String currentFragment;

    Toolbar toolbar;
    DrawerLayout drawer;
    BottomNavigationView navigation;
    final Fragment home = new HomeFragment();
    final Fragment live = new TipsCategoryFragment();
    final Fragment test = new MainCategoryFragment();
    final Fragment notification = new NotificationFragment();
    final Fragment profile = new ProfileFragment();
    FragmentManager fm = getSupportFragmentManager();
    Fragment active = home;
    FirebaseUser user;
    TextView headerName, headerEmail;
    FrameLayout mainContainer;
    NavigationView drawerNav;
    ImageView imageView;
    boolean comment = true;
    List list = new ArrayList();
    Button admin;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String version;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        drawer=findViewById(R.id.drawer);
        navigation=findViewById(R.id.navigation);
        mainContainer=findViewById(R.id.main_container);
        drawerNav=findViewById(R.id.drawer_nav);
        admin=findViewById(R.id.admin);
        admin.setOnClickListener(v ->         startActivity(new Intent(this, AdminActivity.class)));
        ButterKnife.bind(this);
if(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()){
    Log.i("123321", "118:empty "+ FirebaseAuth.getInstance().getCurrentUser().getProviderId());

}

    submit();
        if (!Utils.role.equals("member")) {
            admin.setVisibility(View.VISIBLE);
            admin.setText(Utils.role);
        }
        else {
            admin.setVisibility(View.GONE);
        }
        user=FirebaseAuth.getInstance().getCurrentUser();
    if(user!=null) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
    databaseReference1.child("email").setValue(user.getEmail());
    //databaseReference1.child("role").setValue("admin");
    }
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if (user == null) {
            Menu nav_Menu = drawerNav.getMenu();
            nav_Menu.findItem(R.id.logout).setVisible(false);
            nav_Menu.findItem(R.id.profile).setVisible(false);
        } else {
            Menu nav_Menu = drawerNav.getMenu();
            nav_Menu.findItem(R.id.signin).setVisible(false);
        }
        drawerSetup();
        bottomSetup();
        navHeaderSetup();
        ImageView imageView = toolbar.findViewById(R.id.drawerIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        fm.addOnBackStackChangedListener(() -> {
            List<Fragment> f = fm.getFragments();
            Fragment frag = f.get(0);
            currentFragment = frag.getClass().getSimpleName();
        });

    }



    private void navHeaderSetup() {
        headerName = drawerNav.getHeaderView(0).findViewById(R.id.name);
        headerEmail = drawerNav.getHeaderView(0).findViewById(R.id.email);
        imageView = drawerNav.getHeaderView(0).findViewById(R.id.icon);
        if (user != null) {
            headerName.setText(user.getDisplayName());
            headerEmail.setText(user.getEmail());
            Picasso.get().load(user.getPhotoUrl()).into(imageView);

        }
    }

    private void drawerSetup() {
        //    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        //    drawer.addDrawerListener(toggle);

        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setHomeButtonEnabled(true);
        //    toggle.syncState();


        drawerNav.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {

                case R.id.logout:

                    drawer.closeDrawers();
                    Builder builder2 = new Builder(this);
                    builder2.setTitle("Are You Sure?");
                    builder2.setMessage("Are You Want To Sign Out?");
                    builder2.setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    });
                    builder2.setNegativeButton("cancel", null);
                    builder2.show();
                    break;


                case R.id.profile:
                    Intent intent1 = new Intent(getApplicationContext(), UserActivity.class);
                    intent1.putExtra("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    intent1.putExtra("user_name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    intent1.putExtra("user_profile", FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
                    startActivity(intent1);

                    break;
                case R.id.policy:
                    String url = "https://www.websitepolicies.com/policies/view/P6GrlShF";
                    Intent i = new Intent(ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    drawer.closeDrawers();
                    break;
                case R.id.signin:
                    startActivity(new Intent(this, SignInActivity.class));
                    break;
                case R.id.about:
                    Builder builder = new Builder(this);
                    builder.setTitle("About Us");
                    builder.setMessage("\uD83C\uDF39\uD83C\uDF39 বড় হয়ে ডাক্তার হবো\" এই স্বপ্ন নিয়ে বেড়ে উঠি অধিকাংশ আমরা। মেধা এবং শ্রম দুটো থাকা সত্ত্বেও অনেকেই এই প্রতিযোগিতায় পেরে উঠি না। কারন একটাই, সঠিক দিক নির্দেশনা।\n" +
                            "\n" +
                            "                            আবার অনেক ক্ষেত্রে সামাজিক প্রতিকূলতায় সম্ভব হয়ে ওঠে না সবার মেডিকেল কোচিং করা।\n" +
                            "\n" +
                            "                            ঘরে বসে মেডিকেল কোচিং-এর সকল সেবা নিয়ে এবং মেডিকেল ভর্তি পরীক্ষার সঠিক দিক নির্দেশনা দিয়ে তোমাদের পাশে আছে  EduMed-Medical Admission help।\n" +
                            "\n" +
                            "                            যদি লক্ষ্য থাকে অটুট\n" +
                            "                            বিশ্বাস হৃদয়ে\n" +
                            "                            হবেই হবেই দেখা\n" +
                            "                            দেখা হবে বিজয়ে,,,\n" +
                            "\n" +
                            "                            \uD83C\uDF39\uD83C\uDF39সকলের জন্যই রইলো আমাদের শুভকামনা \uD83D\uDE42\uD83D\uDE42"



                    );
                    builder.setPositiveButton("ok", null);
                    builder.show();
                    drawer.closeDrawers();
                    break;
                case R.id.share:
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                    drawer.closeDrawers();
                    break;
                case R.id.contact:
                    Builder builder1 = new Builder(this);
                    builder1.setTitle("Contact Us");
                    builder1.setMessage("তোমাদের ডাক্তার হবার স্বপ্নের পথচলায় EduMed-Medical Admission help সবসময় আছে তোমাদের পাশে। \n" +
                            "\n" +
                            "প্রয়োজনে হোমপেজে পোস্ট করতে পারো অথবা আমাদের email করতে পারো এই ঠিকানায়         edumedbdofficial@gmail.com");
                    builder1.setPositiveButton("ok", null);
                    builder1.show();
                    drawer.closeDrawers();

            }
            return false;
        });

    }

    private void bottomSetup() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //fm.beginTransaction().add(R.id.main_container, grammer, "grammer").hide(grammer).commit();
        //   fm.beginTransaction().add(R.id.main_container, profile, "profile").hide(profile).commit();
        fm.beginTransaction().add(R.id.main_container, home, "home").commit();

        fm.beginTransaction().add(R.id.main_container, test, "test").hide(test).commit();
        fm.beginTransaction().add(R.id.main_container, profile, "profile").hide(profile).commit();
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(4);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.homescreen_count, bottomNavigationMenuView, false);
        TextView tv = badge.findViewById(R.id.notification_badge);
        tv.setVisibility(View.GONE);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).child("count");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        tv.setVisibility(View.VISIBLE);
                        String s = dataSnapshot.getChildrenCount() + "";
                        tv.setText(s);
                        try {
                            itemView.addView(badge);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        tv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        if (getIntent().getStringExtra("name") != null) {
            Log.i("123321", "296");
            DatabaseReference databaseReference0 = FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).child("count");
            databaseReference0.removeValue();
            if (list.contains(notification)) {
                comment = true;
                fm.beginTransaction().hide(active).show(notification).commit();
                active = notification;

            } else {
                list.add(notification);
                fm.beginTransaction().add(R.id.main_container, notification, "profile").hide(notification).commit();
                comment = true;
                fm.beginTransaction().hide(active).show(notification).commit();
                active = notification;

            }
        } else {
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {


        switch (item.getItemId()) {
            case R.id.feed:

                fm.beginTransaction().hide(active).show(home).commit();
                active = home;
                return true;

            case R.id.live:
                if (user != null) {
                    if (!list.contains(live)) {
                        list.add(live);
                        fm.beginTransaction().add(R.id.main_container, live, "live").hide(live).commit();
                    }
                    {
                        fm.beginTransaction().hide(active).show(live).commit();
                        active = live;
                        comment = true;
                    }
                } else {
                    Builder builder = new Builder(MainActivity.this);
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();

                }
                return true;

            case R.id.test:
                if (user != null) {


                    fm.beginTransaction().hide(active).show(test).commit();
                    active = test;
                } else {
                    Builder builder = new Builder(MainActivity.this);
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();

                }
                return true;

            case R.id.profile:
                if (user != null) {
                    fm.beginTransaction().hide(active).show(profile).commit();
                    active = profile;
                    comment = true;
                } else {
                    Builder builder = new Builder(this);
                    builder.setTitle("Sign in requied");
                    builder.setMessage("you must sign in to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));

                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();
                }
                return true;

            case R.id.notification:
                if (user != null) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).child("count");
                    databaseReference.removeValue();
                    if (list.contains(notification)) {
                        comment = true;
                        fm.beginTransaction().hide(active).show(notification).commit();
                        active = notification;
                        return true;
                    } else {
                        list.add(notification);
                        fm.beginTransaction().add(R.id.main_container, notification, "profile").hide(notification).commit();
                        comment = true;
                        fm.beginTransaction().hide(active).show(notification).commit();
                        active = notification;
                        return true;
                    }
                } else {
                    Builder builder = new Builder(this);
                    builder.setTitle("Sign in requied");
                    builder.setMessage("you must sign in to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));

                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();
                }
        }

        return false;
    };


    public void setActive(Fragment fragment) {
        active = fragment;
    }

    public Fragment getActive() {
        return active;
    }

    @Override
    public void onBackPressed() {


        if (active == home) {
            Builder builder = new Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you Sure ?" +
                    "");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                    System.exit(0);

                }
            });
            builder.setNegativeButton("cancel", null);
            builder.show();
        }

        // Toast.makeText(this, ""+Model.getI(), Toast.LENGTH_SHORT).show();
        if (Model.getI() == 1) {
            // super.onBackPressed();
        } else {
            navigation.setVisibility(View.VISIBLE);
        }

    }

    public void setComment(Boolean b) {
        // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        comment = b;
    }




    private  void submit(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://mrenglish.xyz/pharmacy/" + "home_api.php",
                response -> {

                    try {
                        JSONObject object=new JSONObject(response);
                        JSONObject jsonObject=object.getJSONArray("data").getJSONObject(0);
                        String sVersion=jsonObject.getString("version");
                        Constant.message=jsonObject.getString("message");
                        Constant.image=jsonObject.getString("image");
                        if (!sVersion.equals(version)) {
                            Builder builder = new Builder(MainActivity.this);
                            if (!Utils.role.equals("member")) {
                                builder.setCancelable(true);
                            }
                            else builder.setCancelable(false);
                            builder.setTitle("Update required");
                            builder.setMessage("You are using version "+version+"\n .A new Version "+ sVersion+" is available on Play store.Please update ");
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String appPackageName = getPackageName();

// getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        finishAffinity();
                                        System.exit(0);
                                    } catch (ActivityNotFoundException anfe) {
                                        startActivity(new Intent(ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        finishAffinity();
                                        System.exit(0);
                                    }
                                }
                            });

                            builder.show();
                        }
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
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}