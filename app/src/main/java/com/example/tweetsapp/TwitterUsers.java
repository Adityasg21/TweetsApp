package com.example.tweetsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        FancyToast.makeText(TwitterUsers.this, "welcome "+ParseUser.getCurrentUser().getUsername(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
        listView=findViewById(R.id.listview);
        tUsers=new ArrayList<>();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        try {


            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {

                    if(objects.size()>0 && e==null){

                        for(ParseUser users:objects){
                            tUsers.add(users.getUsername());

                        }
                        listView.setAdapter(adapter);

                        for(String twitterUsers:tUsers){
                            if(ParseUser.getCurrentUser().getList("following")!=null) {
                                if (ParseUser.getCurrentUser().getList("following").contains(twitterUsers)) {
                                    listView.setItemChecked(tUsers.indexOf(twitterUsers), true);
                                }
                            }
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logoutUserItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent=new Intent(TwitterUsers.this,SignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;

            case R.id.send:
                Intent intent=new Intent(TwitterUsers.this,sendTweetAcitvity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView =(CheckedTextView) view;

        if(checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this, tUsers.get(position)+ " is now followed", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().add("following",tUsers.get(position));
        }else{
            FancyToast.makeText(TwitterUsers.this, tUsers.get(position)+ " is not  followed", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().getList("following").remove(tUsers.get(position));
            List currentUserFanOf=ParseUser.getCurrentUser().getList("following");
            ParseUser.getCurrentUser().remove("following");
            ParseUser.getCurrentUser().put("following",currentUserFanOf);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(TwitterUsers.this, "saved", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                }
            }
        });

    }
}