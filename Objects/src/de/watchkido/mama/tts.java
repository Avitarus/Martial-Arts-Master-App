package de.watchkido.mama;

import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class tts extends Activity implements B4AActivity{
	public static tts mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.tts");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (tts).");
				p.finish();
			}
		}
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
		BA.handler.postDelayed(new WaitForLayout(), 5);

	}
	private static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.tts");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.tts", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (tts) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (tts) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
		return true;
	}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return tts.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
		this.setIntent(intent);
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (tts) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (tts) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.obejcts.TTS _tts1 = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _barpitch = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _barspeechrate = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnspeak = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext1 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnrlanguages = null;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
public de.watchkido.mama.startabfragen _startabfragen = null;
public de.watchkido.mama.einstellungenapp _einstellungenapp = null;
public de.watchkido.mama.erfolgsmeldung _erfolgsmeldung = null;
public de.watchkido.mama.checkliste _checkliste = null;
public de.watchkido.mama.wettkampf _wettkampf = null;
public de.watchkido.mama.einstellungenwettkampf _einstellungenwettkampf = null;
public de.watchkido.mama.einstellungen _einstellungen = null;
public de.watchkido.mama.einstellungenah _einstellungenah = null;
public de.watchkido.mama.training _training = null;
public de.watchkido.mama.einstellungenstunde _einstellungenstunde = null;
public de.watchkido.mama.facebook _facebook = null;
public de.watchkido.mama.multipartpost _multipartpost = null;
public de.watchkido.mama.einstellungentrainingkick _einstellungentrainingkick = null;
public de.watchkido.mama.einstellungentrainingstunde _einstellungentrainingstunde = null;
public de.watchkido.mama.benachrichtigung _benachrichtigung = null;
public de.watchkido.mama.downloadservice _downloadservice = null;
public de.watchkido.mama.kampfsportlexikon _kampfsportlexikon = null;
public de.watchkido.mama.karatestunde _karatestunde = null;
public de.watchkido.mama.lebensmittel _lebensmittel = null;
public de.watchkido.mama.statemanager _statemanager = null;
public de.watchkido.mama.tagebuch _tagebuch = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 19;BA.debugLine="Activity.LoadLayout(\"1\")";
mostCurrent._activity.LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 20;BA.debugLine="spnrLanguages.AddAll(Array As String(\"en\", \"fr\", \"de\"))";
mostCurrent._spnrlanguages.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"en","fr","de"}));
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 40;BA.debugLine="TTS1.Release";
_tts1.Release();
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 34;BA.debugLine="If TTS1.IsInitialized = False Then";
if (_tts1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 35;BA.debugLine="TTS1.Initialize(\"TTS1\")";
_tts1.Initialize(processBA,"TTS1");
 };
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _barpitch_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 54;BA.debugLine="Sub barPitch_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 55;BA.debugLine="If UserChanged Then";
if (_userchanged) { 
 //BA.debugLineNum = 56;BA.debugLine="TTS1.Pitch = Value / 10";
_tts1.setPitch((float) (_value/(double)10));
 };
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _barspeechrate_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 49;BA.debugLine="Sub barSpeechRate_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 50;BA.debugLine="If UserChanged Then";
if (_userchanged) { 
 //BA.debugLineNum = 51;BA.debugLine="TTS1.SpeechRate = Value / 10";
_tts1.setSpeechRate((float) (_value/(double)10));
 };
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static String  _btnspeak_click() throws Exception{
 //BA.debugLineNum = 43;BA.debugLine="Sub btnSpeak_Click";
 //BA.debugLineNum = 44;BA.debugLine="If EditText1.Text.Length > 0 Then";
if (mostCurrent._edittext1.getText().length()>0) { 
 //BA.debugLineNum = 45;BA.debugLine="TTS1.Speak(EditText1.Text, True)";
_tts1.Speak(mostCurrent._edittext1.getText(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 46;BA.debugLine="EditText1.SelectAll";
mostCurrent._edittext1.SelectAll();
 };
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 11;BA.debugLine="Dim barPitch As SeekBar";
mostCurrent._barpitch = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim barSpeechRate As SeekBar";
mostCurrent._barspeechrate = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim btnSpeak As Button";
mostCurrent._btnspeak = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim EditText1 As EditText";
mostCurrent._edittext1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim spnrLanguages As Spinner";
mostCurrent._spnrlanguages = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim TTS1 As TTS";
_tts1 = new anywheresoftware.b4a.obejcts.TTS();
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static String  _spnrlanguages_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 59;BA.debugLine="Sub spnrLanguages_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 60;BA.debugLine="If btnSpeak.Enabled = False Then Return";
if (mostCurrent._btnspeak.getEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
if (true) return "";};
 //BA.debugLineNum = 61;BA.debugLine="If TTS1.SetLanguage(Value, \"\") = False Then";
if (_tts1.SetLanguage(BA.ObjectToString(_value),"")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 62;BA.debugLine="ToastMessageShow(\"Language data not found.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Language data not found.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 63;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _tts1_ready(boolean _success) throws Exception{
int _i = 0;
 //BA.debugLineNum = 22;BA.debugLine="Sub TTS1_Ready (Success As Boolean)";
 //BA.debugLineNum = 23;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 25;BA.debugLine="For i = 0 To Activity.NumberOfViews - 1";
{
final int step16 = 1;
final int limit16 = (int) (mostCurrent._activity.getNumberOfViews()-1);
for (_i = (int) (0); (step16 > 0 && _i <= limit16) || (step16 < 0 && _i >= limit16); _i = ((int)(0 + _i + step16))) {
 //BA.debugLineNum = 26;BA.debugLine="Activity.GetView(i).Enabled = True";
mostCurrent._activity.GetView(_i).setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }
};
 //BA.debugLineNum = 28;BA.debugLine="btnSpeak_Click 'play first sentence";
_btnspeak_click();
 }else {
 //BA.debugLineNum = 30;BA.debugLine="Msgbox(\"Error initializing TTS engine.\", \"\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Error initializing TTS engine.","",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
}
