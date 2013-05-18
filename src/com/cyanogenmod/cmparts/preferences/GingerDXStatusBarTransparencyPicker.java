package com.cyanogenmod.cmparts.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.provider.Settings;

public class GingerDXStatusBarTransparencyPicker extends GingerDXTransparencyPickerBase {
	
	private static final String KEY = Settings.System.ACHEP_STATUS_BAR_BACKGROUND_TRANSPARENCY;

	public GingerDXStatusBarTransparencyPicker(Context context, AttributeSet attrs) {
		super(context, attrs, KEY);
	}
	
}