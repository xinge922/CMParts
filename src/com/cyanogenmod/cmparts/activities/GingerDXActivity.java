package com.cyanogenmod.cmparts.activities;

import com.cyanogenmod.cmparts.R;
////import com.cyanogenmod.cmparts.preferences;

import android.content.Context;

import java.io.DataOutputStream;

import android.content.pm.IPackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.database.ContentObserver;
import android.content.ContentResolver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.provider.CmSystem;
import android.os.SystemProperties;

public class GingerDXActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	private final String TAG="gingerdx";

    private static final String LED_DISABLED_PREF = "pref_led_disabled";
    private static final String LED_DISABLED_FROM_PREF = "pref_led_disabled_from";
    private static final String LED_DISABLED_TO_PREF = "pref_led_disabled_to";

    private static final String FLIPPING_DOWN_MUTES_RINGER_PREF = "pref_flipping_mutes_ringer";
	private static final String BACK_BUTTON_ENDS_CALL_PREF = "pref_back_button_ends_call";
	private static final String MENU_BUTTON_ANSWERS_CALL_PREF = "pref_menu_button_answers_call";
	//private static final String TRANSPARENT_STATUS_BAR_PREF = "pref_transparent_status_bar";
	private static final String HIDE_AVATAR_MESSAGE_PREF = "pref_hide_avatar_message";
	private static final String QUICK_COPY_PASTE_PREF = "pref_quick_copy_paste";
//	private static final String DO_PROFILE_SCROLLING_PREF = "pref_do_profile_scrolling";
//	private static final String DO_PROFILE_FLINGING_PREF = "pref_do_profile_flinging";
    private static final String CALL_ME_LOUDER_PREF = "pref_call_me_louder";
    private static final String RINGER_LOOP_PREF = "pref_ringer_loop";
    private static final String ABOUT_PREF = "pref_gingerdx_about";
    private static final String SENSE3_LOCKSCREEN_PREF = "pref_sense3_lockscreen";
//  private static final String SMART_DIALER_PREF = "pref_smart_dialer";
//  private static final String RECENT_APPS_STATUS_BAR_PREF = "pref_recent_apps_status_bar";
    private static final String CENTER_CLOCK_STATUS_BAR_PREF = "pref_center_clock_status_bar";
    private static final String UPDATE_CHECK_HOUR_PREF = "pref_update_check_hour";
    private static final String PICK_UP_TO_CALL_PREF = "pref_pick_up_to_call";
    
    
    // AChep's alarm options
    private static final String ACHEP_ALARM_FLIP_ACTION_PREF = "pref_achep_alarm_flip_action";
    private static final String ACHEP_ALARM_SHAKE_ACTION_PREF = "pref_achep_alarm_shake_action";
    private static final String ACHEP_ALARM_MATH_QUESTIONS_ENABLED_PREF = "pref_achep_alarm_math_questions_enabled";
    private static final String ACHEP_ALARM_INCREASING_VOLUME_ENABLED_PREF = "pref_achep_alarm_increasing_volume_enabled";

    // Jellychep
    private static final String ACHEP_JB_STATUS_BAR_PREF = "pref_achep_jb_status_bar";
    // private static final String ACHEP_JB_STATUS_BAR_PANEL_BACKGROUND_TRANSPARENCY_PREF = "pref_achep_jb_status_bar_panel_background_transparency";
    private static final String ACHEP_JB_STATUS_BAR_NOTIFICATION_PREF = "pref_achep_jb_status_bar_notification";
    private static final String ACHEP_JB_STATUS_BAR_NOTIFICATION_BIGGER_PREF = "pref_achep_jb_status_bar_notification_bigger";
    // private static final String ACHEP_JB_STATUS_BAR_SOFT_BUTTONS_PREF = "pref_achep_jb_status_bar_soft_buttons";

    // Ring locker
    private static final String ACHEP_RING_LOCKSCREEN_PREF = "pref_achep_ring_lockscreen";

    // Ultra-brightness	
    private static final String ULTRA_BRIGHTNESS_PREF = "pref_ultra_brightness";
    private static final String ULTRA_BRIGHTNESS_PROP = "sys.ultrabrightness";
    private static final String ULTRA_BRIGHTNESS_PERSIST_PROP = "persist.sys.ultrabrightness";
    private static final int ULTRA_BRIGHTNESS_DEFAULT = 0;
    

    static Context mContext;
	private SettingsObserver mSettingsObserver;
    
    // AChep's alarm options
    private ListPreference mAlarmShakeActionPref;
    private ListPreference mAlarmFlipActionPref;
    private CheckBoxPreference mAlarmIncreasingVolume;
    private CheckBoxPreference mAlarmMathQuestions;

    // Jellychep
    private CheckBoxPreference mJellyStatusbar;
    // private GingerDXTransparencyPickerBase mJellyStatusbarPanelTransparency;
    private CheckBoxPreference mJellyStatusbarNotification;
    private CheckBoxPreference mJellyStatusbarNotificationBigger;
    // private CheckBoxPreference mJellyStatusBarSoftButtons;

    // Ring locker
    private CheckBoxPreference mRingLockscreen;

    // Ultra brightess	
    private CheckBoxPreference mUltraBrightnessPref;

    private ListPreference mLedDisabledFromPref;
    private ListPreference mLedDisabledToPref;
    private CheckBoxPreference mLedDisabledPref;
    private ListPreference mTransparentStatusBarPref;
    private ListPreference mUpdateCheckHourPref;
    private CheckBoxPreference mFlippingDownMutesRinger;
    private CheckBoxPreference mBackButtonEndsCall;
    private CheckBoxPreference mMenuButtonAnswersCall;
    private CheckBoxPreference mPickUpToCall;
    private CheckBoxPreference mHideAvatarMessage;
    private CheckBoxPreference mQuickCopyPaste;
//  private CheckBoxPreference mDoProfileScrolling;
//  private CheckBoxPreference mDoProfileFlinging;
    private CheckBoxPreference mCallMeLouder;
    private CheckBoxPreference mRingerLoop;
    private CheckBoxPreference mSense3Lockscreen;
    private CheckBoxPreference mSmartDialer;
    private CheckBoxPreference mRecentAppsStatusBar;
    private CheckBoxPreference mCenterClockStatusBar;
	private Preference mAbout;    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this.getBaseContext();
        
        setTitle(R.string.gingerdx_settings_title);
        addPreferencesFromResource(R.xml.gingerdx_settings);
        
        PreferenceScreen prefSet = getPreferenceScreen();
        
        // Alarm
        mAlarmShakeActionPref = (ListPreference) prefSet.findPreference(ACHEP_ALARM_SHAKE_ACTION_PREF);
        mAlarmShakeActionPref.setValue(String.valueOf(Settings.System.getInt(mContext.getContentResolver(),
        	Settings.System.ACHEP_ALARM_SHAKE_ACTION, 0)));
        mAlarmShakeActionPref.setOnPreferenceChangeListener(this);        
        mAlarmFlipActionPref = (ListPreference) prefSet.findPreference(ACHEP_ALARM_FLIP_ACTION_PREF);
        mAlarmFlipActionPref.setValue(String.valueOf(Settings.System.getInt(mContext.getContentResolver(),
        	Settings.System.ACHEP_ALARM_FLIP_ACTION, 0)));
        mAlarmFlipActionPref.setOnPreferenceChangeListener(this);        
        mAlarmMathQuestions = (CheckBoxPreference) prefSet.findPreference(ACHEP_ALARM_MATH_QUESTIONS_ENABLED_PREF);
        mAlarmIncreasingVolume = (CheckBoxPreference) prefSet.findPreference(ACHEP_ALARM_INCREASING_VOLUME_ENABLED_PREF);
        mAlarmMathQuestions.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.ACHEP_ALARM_MATH_QUESTIONS_ENABLED, 0) == 1);
        mAlarmIncreasingVolume.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.ACHEP_ALARM_INCREASING_VOLUME_ENABLED, 0) == 1);
       
	   // Jelly Beans
        mJellyStatusbar = (CheckBoxPreference) prefSet.findPreference(ACHEP_JB_STATUS_BAR_PREF);
        mJellyStatusbarNotification = (CheckBoxPreference) prefSet.findPreference(ACHEP_JB_STATUS_BAR_NOTIFICATION_PREF);
        mJellyStatusbarNotificationBigger = (CheckBoxPreference) prefSet.findPreference(ACHEP_JB_STATUS_BAR_NOTIFICATION_BIGGER_PREF);
  	// mJellyStatusbarPanelTransparency = (GingerDXTransparencyPickerBase) prefSet.findPreference(ACHEP_JB_STATUS_BAR_PANEL_BACKGROUND_TRANSPARENCY_PREF);
        mJellyStatusbar.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.ACHEP_JB_STATUS_BAR, 0) == 1);
 	// updateJellyStatusbarPanelTransparencyPref(mJellyStatusbar.isChecked());
        mJellyStatusbarNotification.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.ACHEP_JB_STATUS_BAR_NOTIFICATION, 0) == 1);
        mJellyStatusbarNotificationBigger.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.ACHEP_JB_STATUS_BAR_NOTIFICATION_BIGGER, 0) == 1);
        updateJellyStatusbarNotificationPref(mJellyStatusbarNotification.isChecked());
        
	// Additional options
	// mJellyStatusBarSoftButtons = (CheckBoxPreference) prefSet.findPreference(ACHEP_JB_STATUS_BAR_SOFT_BUTTONS_PREF);
	// mJellyStatusBarSoftButtons.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.ACHEP_JB_STATUS_BAR_SOFT_BUTTONS, 0) == 1);
		
	mRingLockscreen = (CheckBoxPreference) prefSet.findPreference(ACHEP_RING_LOCKSCREEN_PREF);

        /* Ultra brightness */
        mUltraBrightnessPref = (CheckBoxPreference) prefSet.findPreference(ULTRA_BRIGHTNESS_PREF);
		updateUltraBrightnessPrefState();
        
		// LED
        mLedDisabledPref = (CheckBoxPreference) prefSet.findPreference(LED_DISABLED_PREF);
        mLedDisabledPref.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.NOTIFICATION_LIGHT_DISABLED, 0) == 1);        
        mLedDisabledFromPref = (ListPreference) prefSet.findPreference(LED_DISABLED_FROM_PREF);
        mLedDisabledFromPref.setValue(String.valueOf(Settings.System.getInt(mContext.getContentResolver(),
        	Settings.System.NOTIFICATION_LIGHT_DISABLED_START, 23)));
        mLedDisabledFromPref.setOnPreferenceChangeListener(this);            
        mLedDisabledToPref = (ListPreference) prefSet.findPreference(LED_DISABLED_FROM_PREF);
        mLedDisabledToPref.setValue(String.valueOf(Settings.System.getInt(mContext.getContentResolver(),
        	Settings.System.NOTIFICATION_LIGHT_DISABLED_END, 6)));
        mLedDisabledToPref.setOnPreferenceChangeListener(this);
        updateLedDisabledPref(mLedDisabledPref.isChecked());
                    
        mFlippingDownMutesRinger = (CheckBoxPreference) prefSet.findPreference(FLIPPING_DOWN_MUTES_RINGER_PREF);
        mFlippingDownMutesRinger.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.FLIPPING_DOWN_MUTES_RINGER, 1) == 1);

        mBackButtonEndsCall = (CheckBoxPreference) prefSet.findPreference(BACK_BUTTON_ENDS_CALL_PREF);
        mBackButtonEndsCall.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.BACK_BUTTON_ENDS_CALL, 0) == 1);

		mMenuButtonAnswersCall = (CheckBoxPreference) prefSet.findPreference(MENU_BUTTON_ANSWERS_CALL_PREF);
        mMenuButtonAnswersCall.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.MENU_BUTTON_ANSWERS_CALL, 0) == 1);
        
        mPickUpToCall = (CheckBoxPreference) prefSet.findPreference(PICK_UP_TO_CALL_PREF);
        mPickUpToCall.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.PICK_UP_TO_CALL, 0) == 1);

		mHideAvatarMessage = (CheckBoxPreference) prefSet.findPreference(HIDE_AVATAR_MESSAGE_PREF);
		mHideAvatarMessage.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.HIDE_AVATAR_MESSAGE, 0) == 1);

		mQuickCopyPaste = (CheckBoxPreference) prefSet.findPreference(QUICK_COPY_PASTE_PREF);
		mQuickCopyPaste.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.QUICK_COPY_PASTE, 1) == 1);
	    
	    mUpdateCheckHourPref = (ListPreference) prefSet.findPreference(UPDATE_CHECK_HOUR_PREF);
	    mUpdateCheckHourPref.setValue(String.valueOf(Settings.System.getInt(mContext.getContentResolver(),
			Settings.System.UPDATE_CHECK_HOUR, 0)));
		mUpdateCheckHourPref.setOnPreferenceChangeListener(this);
	    
	    mCallMeLouder = (CheckBoxPreference) prefSet.findPreference(CALL_ME_LOUDER_PREF);
	    mCallMeLouder.setChecked((Settings.System.getInt(getContentResolver(), Settings.System.CALL_ME_LOUDER, 0) == 1));

	    mRingerLoop = (CheckBoxPreference) prefSet.findPreference(RINGER_LOOP_PREF);
	    mRingerLoop.setChecked((Settings.System.getInt(getContentResolver(), Settings.System.RINGER_LOOP, 1) == 1));
	    
	    mAbout = (Preference) prefSet.findPreference(ABOUT_PREF);
		mAbout.setSummary("GingerDX " + android.os.SystemProperties.get("ro.build.display.id"));
        mAbout.setEnabled(true);

	    mSense3Lockscreen = (CheckBoxPreference) prefSet.findPreference(SENSE3_LOCKSCREEN_PREF);
	    mSense3Lockscreen.setChecked((Settings.System.getInt(getContentResolver(), Settings.System.USE_SENSE3_LOCKSCREEN, 0) == 1));

	    /*mSmartDialer = (CheckBoxPreference) prefSet.findPreference(SMART_DIALER_PREF);
	    mSmartDialer.setChecked((Settings.System.getInt(getContentResolver(), Settings.System.SMART_DIALER, 1) == 1));*/

	    /*mRecentAppsStatusBar = (CheckBoxPreference) prefSet.findPreference(RECENT_APPS_STATUS_BAR_PREF);
	    if (mRecentAppsStatusBar != null) {
    	    mRecentAppsStatusBar.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.RECENT_APPS_STATUS_BAR, 1) == 1);
	    }*/

	    mCenterClockStatusBar = (CheckBoxPreference) prefSet.findPreference(CENTER_CLOCK_STATUS_BAR_PREF);
	    if (mCenterClockStatusBar != null) {
    	    mCenterClockStatusBar.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.CENTER_CLOCK_STATUS_BAR, 0) == 1);
	    }

//      mDoProfileScrolling = (CheckBoxPreference) prefSet.findPreference(DO_PROFILE_SCROLLING_PREF);
//      if (mDoProfileScrolling != null)
//		    mDoProfileScrolling.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.DO_PROFILE_SCROLLING, 0) == 1);

//      mDoProfileFlinging = (CheckBoxPreference) prefSet.findPreference(DO_PROFILE_FLINGING_PREF);
//      if (mDoProfileFlinging != null)
//		    mDoProfileFlinging.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.DO_PROFILE_FLINGING, 0) == 1);

        mSettingsObserver = new SettingsObserver(new Handler());
        mSettingsObserver.observe();
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
        mSettingsObserver.deserve();
    }
        
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) { 
	boolean handled = true;
	if (preference == mUltraBrightnessPref) {
        	boolean value = mUltraBrightnessPref.isChecked();
        	if (value==true) {
        		SystemProperties.set(ULTRA_BRIGHTNESS_PERSIST_PROP, "1");
            		DisplayActivity.writeOneLine("/sys/devices/platform/i2c-adapter/i2c-0/0-0036/mode", "i2c_pwm");
                	Settings.System.putInt(getContentResolver(),
                	    Settings.System.ACHEP_ULTRA_BRIGHTNESS, 1);
            	} else {
            		SystemProperties.set(ULTRA_BRIGHTNESS_PERSIST_PROP, "0");
            		DisplayActivity.writeOneLine("/sys/devices/platform/i2c-adapter/i2c-0/0-0036/mode", "i2c_pwm_als");
                	Settings.System.putInt(getContentResolver(),
                	    Settings.System.ACHEP_ULTRA_BRIGHTNESS, 0);
        	}
        } else/* if (preference == mJellyStatusBarSoftButtons) {
            	Settings.System.putInt(getContentResolver(),
            	    Settings.System.ACHEP_JB_STATUS_BAR_SOFT_BUTTONS, mJellyStatusBarSoftButtons.isChecked() ? 1 : 0);
            	return true;
        } else*/ if (preference == mJellyStatusbar) {
            	Settings.System.putInt(getContentResolver(),
            	    Settings.System.ACHEP_JB_STATUS_BAR, mJellyStatusbar.isChecked() ? 1 : 0);
            	// updateJellyStatusbarPanelTransparencyPref(mJellyStatusbar.isChecked());
        } else if (preference == mJellyStatusbarNotification) {
            	updateJellyStatusbarNotificationPref(mJellyStatusbarNotification.isChecked());
            	Settings.System.putInt(getContentResolver(),
            	    Settings.System.ACHEP_JB_STATUS_BAR_NOTIFICATION, mJellyStatusbarNotification.isChecked() ? 1 : 0);
        } else if (preference == mRingLockscreen) {
            	Settings.System.putInt(getContentResolver(),
            	    Settings.System.ACHEP_RING_LOCKSCREEN, mRingLockscreen.isChecked() ? 1 : 0);
        } else if (preference == mJellyStatusbarNotificationBigger) {
            	Settings.System.putInt(getContentResolver(),
            	    Settings.System.ACHEP_JB_STATUS_BAR_NOTIFICATION_BIGGER, mJellyStatusbarNotificationBigger.isChecked() ? 1 : 0);
        } else if (preference == mAlarmMathQuestions) {
            	Settings.System.putInt(getContentResolver(),
            	    Settings.System.ACHEP_ALARM_MATH_QUESTIONS_ENABLED, mAlarmMathQuestions.isChecked() ? 1 : 0);
        } else if (preference == mAlarmIncreasingVolume) {
            	Settings.System.putInt(getContentResolver(),
            	    Settings.System.ACHEP_ALARM_INCREASING_VOLUME_ENABLED, mAlarmIncreasingVolume.isChecked() ? 1 : 0);
        } else if (preference == mLedDisabledPref) {
            	updateLedDisabledPref(mLedDisabledPref.isChecked());
            	Settings.System.putInt(getContentResolver(),
            	    Settings.System.NOTIFICATION_LIGHT_DISABLED, mLedDisabledPref.isChecked() ? 1 : 0);
        } else if (preference == mFlippingDownMutesRinger) {
            	Settings.System.putInt(getContentResolver(), Settings.System.FLIPPING_DOWN_MUTES_RINGER, mFlippingDownMutesRinger.isChecked() ? 1 : 0);
        } else if (preference == mBackButtonEndsCall) {
            	Settings.System.putInt(getContentResolver(), Settings.System.BACK_BUTTON_ENDS_CALL, mBackButtonEndsCall.isChecked() ? 1 : 0);
        } else if (preference == mMenuButtonAnswersCall) {
            	Settings.System.putInt(getContentResolver(), Settings.System.MENU_BUTTON_ANSWERS_CALL, mMenuButtonAnswersCall.isChecked() ? 1 : 0);
        } else if (preference == mPickUpToCall) {
            	Settings.System.putInt(getContentResolver(), Settings.System.PICK_UP_TO_CALL, mPickUpToCall.isChecked() ? 1 : 0);
        } else if (preference == mHideAvatarMessage) {
            	Settings.System.putInt(getContentResolver(), Settings.System.HIDE_AVATAR_MESSAGE, mHideAvatarMessage.isChecked() ? 1 : 0);
        } else if (preference == mQuickCopyPaste) {
            	Settings.System.putInt(getContentResolver(), Settings.System.QUICK_COPY_PASTE, mQuickCopyPaste.isChecked() ? 1 : 0);
        } else if (preference == mCallMeLouder) {
            	Settings.System.putInt(getContentResolver(), Settings.System.CALL_ME_LOUDER, mCallMeLouder.isChecked() ? 1 : 0);
        } else if (preference == mRingerLoop) {
            	Settings.System.putInt(getContentResolver(), Settings.System.RINGER_LOOP, mRingerLoop.isChecked() ? 1 : 0);
        } else if (preference == mSense3Lockscreen) {
            	Settings.System.putInt(getContentResolver(), Settings.System.USE_SENSE3_LOCKSCREEN, mSense3Lockscreen.isChecked() ? 1 : 0);
        }/*else if (preference == mSmartDialer) {
            	Settings.System.putInt(getContentResolver(), Settings.System.SMART_DIALER, mSmartDialer.isChecked() ? 1 : 0);
        }*/ else if (preference == mCenterClockStatusBar) {
            	Settings.System.putInt(getContentResolver(), Settings.System.CENTER_CLOCK_STATUS_BAR, mCenterClockStatusBar.isChecked() ? 1 : 0);
        }/* else if (preference == mRecentAppsStatusBar) {
        	Settings.System.putInt(getContentResolver(), Settings.System.RECENT_APPS_STATUS_BAR, mRecentAppsStatusBar.isChecked() ? 1 : 0);
        } else if (preference == mDoProfileScrolling) {
        	Settings.System.putInt(getContentResolver(), Settings.System.DO_PROFILE_SCROLLING, mDoProfileScrolling.isChecked() ? 1 : 0);
        } else if (preference == mDoProfileFlinging) {
        	Settings.System.putInt(getContentResolver(), Settings.System.DO_PROFILE_FLINGING, mDoProfileFlinging.isChecked() ? 1 : 0);
        }*/ else if (preference == mAbout) {
        	// increase the number of clicks
		aboutClickerMonkey();
        } else {
		handled = false;	
	}
        return handled;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
    	if (newValue == null) 
		return false;
    	// we have a list preference change?
	if (preference == mAlarmShakeActionPref) {
		int val = Integer.parseInt(String.valueOf(newValue));
       		Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACHEP_ALARM_SHAKE_ACTION, val);
        	mAlarmShakeActionPref.setValue(String.valueOf(val));
        } else if (preference == mAlarmFlipActionPref) {
		int val = Integer.parseInt(String.valueOf(newValue));
        	Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACHEP_ALARM_FLIP_ACTION, val);
        	mAlarmFlipActionPref.setValue(String.valueOf(val));
        } else if (preference == mLedDisabledFromPref) {
		int val = Integer.parseInt(String.valueOf(newValue));
        	Settings.System.putInt(mContext.getContentResolver(), Settings.System.NOTIFICATION_LIGHT_DISABLED_START, val);
        	mLedDisabledFromPref.setValue(String.valueOf(val));
       	} else if (preference == mLedDisabledToPref) {
		int val = Integer.parseInt(String.valueOf(newValue));
        	Settings.System.putInt(mContext.getContentResolver(), Settings.System.NOTIFICATION_LIGHT_DISABLED_END, val);
        	mLedDisabledToPref.setValue(String.valueOf(val));
        } else if (preference == mUpdateCheckHourPref) {
		int val = Integer.parseInt(String.valueOf(newValue));
        	Settings.System.putInt(mContext.getContentResolver(), Settings.System.UPDATE_CHECK_HOUR, val);
        	mUpdateCheckHourPref.setValue(String.valueOf(val));
        }        
        return false;
    }

	// Easter eggs
	private void aboutClickerMonkey(){
        	int numClicked = Settings.System.getInt(getContentResolver(), Settings.System.ABOUT_CLICKED, 0) + 1;
	       	Settings.System.putInt(getContentResolver(), Settings.System.ABOUT_CLICKED, numClicked);
			
		if (numClicked % 5 == 0) {
			// Do eggs :D			
			renameFifteenPuzzle();
		}
	}

	private void renameFifteenPuzzle(){		  
		try {  
			// Preform su to get root privledges  
			Process p = Runtime.getRuntime().exec("su");   

			// Attempt to write a file to a root-only  
			DataOutputStream os = new DataOutputStream(p.getOutputStream());  
			os.writeBytes("mv system/app/.EasterEggs.bin system/app/FifteenPuzzles.apk\n");  

			// Close the terminal  
			os.writeBytes("exit\n");  
			os.flush();  
			
			p.waitFor();

		} catch (Exception e) {  }  
	}

   private void updateJellyStatusbarNotificationPref(boolean checked){
      mJellyStatusbarNotificationBigger.setEnabled(checked);
   }

  /* private void updateJellyStatusbarPanelTransparencyPref(boolean checked){
      mJellyStatusbarPanelTransparency.setEnabled(checked);
   }*/

   private void updateLedDisabledPref(boolean checked){
      mLedDisabledToPref.setEnabled(checked);
      mLedDisabledFromPref.setEnabled(checked);
   } 
	
   private void updateUltraBrightnessPrefState(){
		mUltraBrightnessPref.setChecked(SystemProperties.getInt(ULTRA_BRIGHTNESS_PERSIST_PROP, ULTRA_BRIGHTNESS_DEFAULT) == 1);
   }

   class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(
                    Settings.System.getUriFor(Settings.System.ACHEP_ULTRA_BRIGHTNESS), false, this);
        }
        void deserve() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.unregisterContentObserver(this);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateUltraBrightnessPrefState();
        }
    }
   
}
