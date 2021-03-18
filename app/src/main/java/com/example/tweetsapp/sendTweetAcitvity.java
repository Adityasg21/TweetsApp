package com.example.tweetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class sendTweetAcitvity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTweet;
    private Button btnViewTweets;
    private ListView ViewTweets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet_acitvity);
        edtTweet=findViewById(R.id.edtSendTweet);
        btnViewTweets=findViewById(R.id.btnViewTweet);
        ViewTweets=findViewById(R.id.ViewTweet);
        btnViewTweets.setOnClickListener(this);


    }

    public void sendTweet(View view){

        ParseObject parseObject=new ParseObject("Tweet");
      parseObject.put("Tweet",edtTweet.getText().toString());
      parseObject.put("user",ParseUser.getCurrentUser().getUsername());
      final ProgressDialog progressDialog=new ProgressDialog(this);
      progressDialog.setMessage("loading...");
      progressDialog.show();

      parseObject.saveInBackground(new SaveCallback() {
          @Override
          public void done(ParseException e) {
              if(e==null){
                  FancyToast.makeText(sendTweetAcitvity.this,ParseUser.getCurrentUser().getUsername()+" Tweets saved",
                          Toast.LENGTH_LONG,FancyToast.INFO,true).show();
              }else
              {
                  FancyToast.makeText(sendTweetAcitvity.this,e.getMessage(),
                          Toast.LENGTH_LONG,FancyToast.INFO,true).show();
              }

              progressDialog.dismiss();
          }

      });

    }

    @Override
    public void onClick(View v) {

        final ArrayList<HashMap<String,String>> Tweetlist=new ArrayList<>();
        final SimpleAdapter adapter=new SimpleAdapter(sendTweetAcitvity.this,Tweetlist,android.R.layout.simple_list_item_2,
                new String[]{"tweetUsername","tweetValue"},new int[]{android.R.id.text1,android.R.id.text2});

        try {
            ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("Tweet");
            parseQuery.whereContainedIn("user",ParseUser.getCurrentUser().getList("following"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0&&e==null){
                        for(ParseObject tweetObject:objects){
                            HashMap<String,String> userTweet= new HashMap<>();
                            userTweet.put("tweetUsername",tweetObject.getString("user"));
                            userTweet.put("tweetValue",tweetObject.getString("Tweet"));
                            Tweetlist.add(userTweet);

                        }
                        ViewTweets.setAdapter(adapter);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}