package com.example.droidcafetextfields;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String ORDER_ID = "com.example.droidcafe.MainActivity.ORDER";
    public static final String ORDER_ITEMS = "com.example.droidcafe.MainActivity.ORDER_ITEMS";

    private ArrayList<String> mOrderedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
        {
            mOrderedItems = savedInstanceState.getStringArrayList(ORDER_ITEMS);
            Log.d("SAVED LIST", "RELOADED ORDER ITEMS");
        }
    }

    public void submitOrder(View view)
    {
        Intent intent = new Intent(this, OrderActivity.class);

        // Add on the ordered items
        intent.putExtra(ORDER_ID, mOrderedItems);

        startActivity(intent);
    }

    /**
     * Print a short message regarding the item. Append the item description
     * to the member ArrayList so that it can be sent to the OrderActivity.
     *
     * @param view
     */
    public void order(View view) {

        // Make this support all the images by checking something

        String message;
        String itemDescription;

        switch (view.getId())
        {
            case R.id.donutImage:
                message = getString(R.string.donut_order_message);
                itemDescription = "Donut";
                break;
            case R.id.ice_cream_image:
                message = getString(R.string.ice_cream_order_message);
                itemDescription = "Ice Cream";
                break;
            case R.id.froyo_image:
                message = getString(R.string.froyo_order_message);
                itemDescription = "Frozen Yogurt";
                break;
            default:
                message = "Couldn't get text";
                itemDescription = null;
        }

        if(itemDescription != null)
        {
            mOrderedItems.add(itemDescription);
        }

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(ORDER_ITEMS, mOrderedItems);
    }
}
