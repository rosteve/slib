package com.firebase.cardslib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import adapters.CafeteriaAdapter;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardGridView;
import models.Cafeteria;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
//    CafeteriaAdapter cafeteriaAdapter;
    DatabaseReference rootReference;
    ArrayList<Cafeteria> cafeteriaArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cafeteriaArrayList = new ArrayList<>();

        rootReference = FirebaseDatabase.getInstance().getReference().child("cafeterias");
        rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    Cafeteria cafeteria = singleSnapshot.getValue(Cafeteria.class);

                    cafeteriaArrayList.add(cafeteria);

                    Log.e("arraylist size",String.valueOf(cafeteriaArrayList.size()));

                }

//                cafeteriaAdapter.notifyDataSetChanged();
                processData(cafeteriaArrayList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database Error", databaseError.toString());
            }
        });

//        cafeteriaAdapter = new CafeteriaAdapter(cafeteriaArrayList,MainActivity.this);

        /*recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(cafeteriaAdapter);*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Going to next Recycler View Type", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                Intent startSecond = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(startSecond);
            }
        });

    }

    private void processData(ArrayList<Cafeteria> cafeteriaArrayList) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < cafeteriaArrayList.size(); i++) {

            Cafeteria cafe=cafeteriaArrayList.get(i);



            GplayGridCard card = new GplayGridCard(getBaseContext());

            card.headerTitle = cafe.getName();
            card.secondaryTitle =cafe.getPhone();
            card.url = cafe.getImage();
            card.mTitle=cafe.getQueue();
            card.setId(cafe.getEmail());
            card.setTitle(cafe.getName());
            CardThumbnail thumb = new CardThumbnail(getBaseContext());

            //Set URL resource
            thumb.setUrlResource(cafe.getImage());

            //Error Resource ID
            thumb.setErrorResource(R.drawable.card_menu_button_expand_material);

            //Add thumbnail to a card
            card.addCardThumbnail(thumb);
            //Only for test, change some icons
//                                if ((i % 6 == 0)) {
//
//                                } else if ((i % 6 == 1)) {
//                                    card.resourceIdThumbnail = R.drawable.ic_launcher;
//                                } else if ((i % 6 == 2)) {
//                                    card.resourceIdThumbnail = R.drawable.ic_launcher;
//                                } else if ((i % 6 == 3)) {
//                                    card.resourceIdThumbnail = R.drawable.ic_launcher;
//                                } else if ((i % 6 == 4)) {
//                                    card.resourceIdThumbnail = R.drawable.ic_launcher;
//
            card.init();
            cards.add(card);

        }

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

        CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class GplayGridCard extends Card {

        protected String mTitle;
        protected TextView mSecondaryTitle;
        protected RatingBar mRatingBar;
        protected int resourceIdThumbnail = -1;
        protected int count;
        protected String url;

        protected String headerTitle;
        protected String secondaryTitle;
        protected float rating;

        public GplayGridCard(Context context) {
            super(context, R.layout.inner_content);
        }


        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {


            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle);
            header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
//                  TODO do your processing here when the three dot menu is clicked
                }
            });

            addCardHeader(header);
            Log.e("URL", url);



            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //Do something
                    String selected= card.getId();
                   //TODO do your processing when your card is clicked from here above is the id of the card. Currently i made it to your email since i didnt see any id on your model class
                }
            });
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
            title.setText(mTitle);

            TextView subtitle = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_subtitle);
            subtitle.setText(secondaryTitle);
//            NetworkImageView thumbnail =(NetworkImageView)view.findViewById(R.id.card_thumbnail_image);
//            thumbnail.setImageUrl(url,imageLoader);


        }


    }
}
