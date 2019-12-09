package com.example.droidcafetextfields;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private final String LOG_TAG = OrderActivity.class.getSimpleName();

    private LinearLayout mMainListLayout;

    private Spinner mSpinner;

    class EditorPhoneAction implements TextView.OnEditorActionListener
    {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            boolean handled = false;
            if (i == EditorInfo.IME_ACTION_SEND)
            {
                dialNumber();
                handled = true;
            }
            return handled;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mMainListLayout = findViewById(R.id.itemList_LinearLayout);

        setupSpinner();

        // Setup handler for entering/calling phone number
        setupPhoneTextAction();

        setupOrderList();

    }

    // Need some kinda method to generate TextFields and put em
    // in the linear layout
    private TextView createTextView(String string)
    {
        TextView textView = new TextView(this);

        // Set LinearLayout-specific params
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(8,8,8,8);

        textView.setLayoutParams(layoutParams);
        textView.setText(string);
        textView.setPadding(8,8,8,8);
        textView.setBackgroundResource(R.color.colorPrimaryDark);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        return textView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String spinnerLabel = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, spinnerLabel, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    private void setupSpinner()
    {
        mSpinner = findViewById(R.id.phone_spinner);
        if (mSpinner != null)
        {
            mSpinner.setOnItemSelectedListener(this);
        }

        // Apply the string array to the spinner adapter
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.labels_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        if (mSpinner != null)
        {
            mSpinner.setAdapter(adapter);
        }
    }

    private void setupPhoneTextAction()
    {
        EditText phoneText = findViewById(R.id.phone_text);
        if (phoneText != null)
        {
            phoneText.setOnEditorActionListener(new EditorPhoneAction());
        }
    }

    private void dialNumber()
    {
        EditText phoneText = findViewById(R.id.phone_text);
        String phoneUri = null;

        if (phoneText != null)
        {
            // Create the phone URI
            phoneUri = "tel:" + phoneText.getText().toString();
        }

        // Create the implicit intent
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneUri));

        // If an app was found to use the phone #, start it
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
        else
        {
            Log.d("ImplicitIntents", "Couldn't find app for URI: " + phoneUri);
        }
    }

    private void setupOrderList()
    {
        // Get the intent and get the string list out of it
        ArrayList<String> orderList =
                getIntent().getStringArrayListExtra(MainActivity.ORDER_ID);

        // Create a map of itemDesc to count
        HashMap<String,Integer> itemCount = new HashMap<>();

        for (String orderDesc : orderList)
        {
            Log.d(LOG_TAG, "GOT ITEM: " + orderDesc);
            if(!itemCount.containsKey(orderDesc))
            {
                itemCount.put(orderDesc,1);
            }
            else
            {
                Integer count = itemCount.get(orderDesc);
                if(count != null)
                {
                    count++;
                }
                itemCount.put(orderDesc, count);
            }
        }

        // Grab the names and the ints and concatenate them to display
        for (Map.Entry<String, Integer> pair : itemCount.entrySet())
        {
            mMainListLayout.addView(
                    createTextView(pair.getKey() + " x" + pair.getValue().toString()));
        }
    }
}
