package com.pixectra.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by prashu on 2/7/2018.
 * for multiple address recycler view
 */

public class multiple_shipp_add extends AppCompatActivity {

    RecyclerView recyclerview;
    DatabaseReference dataref;
    ArrayList list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_shipping);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Delivery Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list = new ArrayList<shipaddClass>();
  //extract list from firebase
        // below is sudo list
       list.add(new shipaddClass("prashu gupta","285,Gali no.10","pemwshwar gate","283203","firozabad","up","8439034578",null  ));
        list.add(new shipaddClass("prashu gupta","Room no 221 Raman A","Iet lucknow","226021","lucknow","up","8439034578" ,null ));
        list.add(new shipaddClass("prashu gupta","285,Gali no.10","pemwshwar gate","283203","firozabad","up","8439034578",null  ));
        list.add(new shipaddClass("prashu gupta","Room no 221 Raman A","Iet lucknow","226021","lucknow","up","8439034578" ,null ));
        list.add(new shipaddClass("prashu gupta","285,Gali no.10","pemwshwar gate","283203","firozabad","up","8439034578" ,null ));
        list.add(new shipaddClass("prashu gupta","Room no 221 Raman A","Iet lucknow","226021","lucknow","up","8439034578" ,null ));


        RecyclerView recycler = (RecyclerView)findViewById(R.id.multi_ship_recycler);
                adapter adap = new adapter(list);
                recycler.setAdapter(adap);
                LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
                recycler.setLayoutManager(layout);

         /*   }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        findViewById(R.id.another_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(multiple_shipp_add.this,shipping_address.class);
                intent.putExtra("status",1);//1 indicates shipping_address calls from Add new Address
                startActivity(intent);
            }
        });
    }
}
