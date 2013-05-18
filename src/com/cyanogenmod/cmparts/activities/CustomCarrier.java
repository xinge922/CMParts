package com.cyanogenmod.cmparts.activities;

import com.cyanogenmod.cmparts.R;
import android.content.Context;
import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.os.Bundle;
import android.app.Dialog;

/**
 * Dialog to set custom carrier text
 */
 
public class CustomCarrier extends Activity implements View.OnClickListener {
    private final static String TAG = "CustomCarrier";

    private EditText mCarrierTextField;
    private Button mSaveButton;
    
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
		
        setContentView(R.layout.carrier);

		// Initialize buttons
        mSaveButton = (Button)findViewById(R.id.ok_button);
        mSaveButton.setOnClickListener(this);
        final Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

		// Initialize Edit Text
        mCarrierTextField = (EditText) findViewById(R.id.carrier);
		mCarrierTextField.setText(Settings.System.getString(getContentResolver(), Settings.System.CUSTOM_CARRIER_TEXT));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
    }
	
	@Override
    public void onClick(View v) {
		if (v == mSaveButton )
			Settings.System.putString(getContentResolver(), Settings.System.CUSTOM_CARRIER_TEXT, mCarrierTextField.getText().toString());
		finish();
    }
	
}

