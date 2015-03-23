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

public class einstellungenah extends Activity implements B4AActivity{
	public static einstellungenah mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.einstellungenah");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (einstellungenah).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.einstellungenah");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.einstellungenah", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (einstellungenah) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (einstellungenah) Resume **");
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
		return einstellungenah.class;
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
        BA.LogInfo("** Activity (einstellungenah) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (einstellungenah) Resume **");
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
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceManager _manager = null;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _screen = null;
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
public de.watchkido.mama.tts _tts = null;
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
 //BA.debugLineNum = 23;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 24;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 25;BA.debugLine="CreatePreferenceScreen";
_createpreferencescreen();
 //BA.debugLineNum = 27;BA.debugLine="If manager.GetAll.Size = 0 Then SetDefaults";
if (_manager.GetAll().getSize()==0) { 
_setdefaults();};
 };
 //BA.debugLineNum = 36;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screen.CreateIntent()));
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 133;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 111;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}
public static String  _btn_click() throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Sub btn_Click";
 //BA.debugLineNum = 49;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screen.CreateIntent()));
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _createpreferencescreen() throws Exception{
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _intentcat = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _cat1 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _cat2 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _intentscreen = null;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
anywheresoftware.b4a.objects.collections.List _pl = null;
anywheresoftware.b4a.phone.Phone.PhoneIntents _pi = null;
anywheresoftware.b4a.objects.collections.Map _m = null;
 //BA.debugLineNum = 53;BA.debugLine="Sub CreatePreferenceScreen";
 //BA.debugLineNum = 54;BA.debugLine="screen.Initialize(\"Settings\", \"\")";
_screen.Initialize("Settings","");
 //BA.debugLineNum = 55;BA.debugLine="Dim intentCat, cat1, cat2 As AHPreferenceCategory";
_intentcat = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat1 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat2 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Dim intentScreen As AHPreferenceScreen";
_intentscreen = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 58;BA.debugLine="intentCat.Initialize(\"Intent Settings\")";
_intentcat.Initialize("Intent Settings");
 //BA.debugLineNum = 59;BA.debugLine="intentCat.AddCheckBox(\"intentenable\", \"Enable Intent Settings\", \"Intent settings are enabled\", \"Intent settings are disabled\", True, \"\")";
_intentcat.AddCheckBox("intentenable","Enable Intent Settings","Intent settings are enabled","Intent settings are disabled",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 60;BA.debugLine="intentScreen.Initialize(\"Intents\", \"Examples with Intents\")";
_intentscreen.Initialize("Intents","Examples with Intents");
 //BA.debugLineNum = 61;BA.debugLine="intentScreen.AddCheckBox(\"chkwifi\", \"Enable Wifi Settings\", \"Wifi settings enabled\", \"Wifi settings disabled\", False, \"\")";
_intentscreen.AddCheckBox("chkwifi","Enable Wifi Settings","Wifi settings enabled","Wifi settings disabled",anywheresoftware.b4a.keywords.Common.False,"");
 //BA.debugLineNum = 64;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 65;BA.debugLine="In.Initialize(\"android.settings.WIFI_SETTINGS\", \"\")";
_in.Initialize("android.settings.WIFI_SETTINGS","");
 //BA.debugLineNum = 66;BA.debugLine="intentScreen.AddIntent( \"Wifi Settings\", \"Example for custom Intent\", In, \"chkwifi\")";
_intentscreen.AddIntent("Wifi Settings","Example for custom Intent",(android.content.Intent)(_in.getObject()),"chkwifi");
 //BA.debugLineNum = 72;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Dim pl As List";
_pl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 74;BA.debugLine="pl = pm.GetInstalledPackages";
_pl = _pm.GetInstalledPackages();
 //BA.debugLineNum = 76;BA.debugLine="If pl.IndexOf(\"com.android.calculator2\") > 0 Then";
if (_pl.IndexOf((Object)("com.android.calculator2"))>0) { 
 //BA.debugLineNum = 77;BA.debugLine="intentScreen.AddIntent(\"Calculator\", \"Open calculator\", pm.GetApplicationIntent(\"com.android.calculator2\"), \"\")";
_intentscreen.AddIntent("Calculator","Open calculator",(android.content.Intent)(_pm.GetApplicationIntent("com.android.calculator2").getObject()),"");
 };
 //BA.debugLineNum = 81;BA.debugLine="Dim pi As PhoneIntents";
_pi = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 82;BA.debugLine="intentScreen.AddIntent(\"Browser\", \"Open http://www.google.de\", pi.OpenBrowser(\"http://www.google.com\"), \"\")";
_intentscreen.AddIntent("Browser","Open http://www.google.de",_pi.OpenBrowser("http://www.google.com"),"");
 //BA.debugLineNum = 85;BA.debugLine="intentCat.AddPreferenceScreen(intentScreen, \"intentenable\")";
_intentcat.AddPreferenceScreen(_intentscreen,"intentenable");
 //BA.debugLineNum = 87;BA.debugLine="cat1.Initialize(\"Examples\")";
_cat1.Initialize("Examples");
 //BA.debugLineNum = 88;BA.debugLine="cat1.AddCheckBox(\"Schalter1\", \"Checkbox1\", \"This is Checkbox1 without second summary\", \"\", True, \"\")";
_cat1.AddCheckBox("Schalter1","Checkbox1","This is Checkbox1 without second summary","",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 89;BA.debugLine="cat1.AddEditText(\"Text1\", \"EditText1\", \"This is EditText1\", \"\", \"Schalter1\")";
_cat1.AddEditText("Text1","EditText1","This is EditText1","","Schalter1");
 //BA.debugLineNum = 90;BA.debugLine="cat1.AddPassword(\"pwd1\", \"Password1\", \"This is a password\", \"\", \"\")";
_cat1.AddPassword("pwd1","Password1","This is a password","","");
 //BA.debugLineNum = 91;BA.debugLine="cat1.AddRingtone(\"ring1\", \"Ringtone1\", \"This is a Ringtone\", \"\", \"\", cat1.RT_NOTIFICATION)";
_cat1.AddRingtone("ring1","Ringtone1","This is a Ringtone","","",_cat1.RT_NOTIFICATION);
 //BA.debugLineNum = 93;BA.debugLine="cat2.Initialize(\"Set Background Color\")";
_cat2.Initialize("Set Background Color");
 //BA.debugLineNum = 94;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 95;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 96;BA.debugLine="m.Put(\"black\", \"I want a black background\")";
_m.Put((Object)("black"),(Object)("I want a black background"));
 //BA.debugLineNum = 97;BA.debugLine="m.Put(\"red\", \"No, make it red\")";
_m.Put((Object)("red"),(Object)("No, make it red"));
 //BA.debugLineNum = 98;BA.debugLine="m.Put(\"green\", \"I like it green\")";
_m.Put((Object)("green"),(Object)("I like it green"));
 //BA.debugLineNum = 99;BA.debugLine="m.Put(\"blue\", \"Blue is best\")";
_m.Put((Object)("blue"),(Object)("Blue is best"));
 //BA.debugLineNum = 100;BA.debugLine="cat2.AddList2(\"Background Color\", \"Choose color\", \"Choose color\", \"black\", \"\", m)";
_cat2.AddList2("Background Color","Choose color","Choose color","black","",_m);
 //BA.debugLineNum = 104;BA.debugLine="screen.AddPreferenceCategory(intentCat)";
_screen.AddPreferenceCategory(_intentcat);
 //BA.debugLineNum = 105;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 106;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
 //BA.debugLineNum = 114;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 115;BA.debugLine="Log(manager.GetAll)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(_manager.GetAll()));
 //BA.debugLineNum = 116;BA.debugLine="Select manager.GetString(\"Background Color\")";
switch (BA.switchObjectToInt(_manager.GetString("Background Color"),"black","red","green","blue")) {
case 0:
 //BA.debugLineNum = 118;BA.debugLine="Activity.Color = Colors.Black";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 break;
case 1:
 //BA.debugLineNum = 120;BA.debugLine="Activity.Color = Colors.Red";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 break;
case 2:
 //BA.debugLineNum = 122;BA.debugLine="Activity.Color = Colors.Green";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 break;
case 3:
 //BA.debugLineNum = 124;BA.debugLine="Activity.Color = Colors.Blue";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 break;
}
;
 //BA.debugLineNum = 127;BA.debugLine="If manager.GetString(\"ring1\") <> \"\" Then";
if ((_manager.GetString("ring1")).equals("") == false) { 
 };
 //BA.debugLineNum = 131;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim manager As AHPreferenceManager";
_manager = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceManager();
 //BA.debugLineNum = 9;BA.debugLine="Dim screen As AHPreferenceScreen";
_screen = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 42;BA.debugLine="manager.SetBoolean(\"Schalter1\", True)";
_manager.SetBoolean("Schalter1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 43;BA.debugLine="manager.SetBoolean(\"Schalter2\", False)";
_manager.SetBoolean("Schalter2",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 44;BA.debugLine="manager.SetString(\"Text1\", \"Hello!\")";
_manager.SetString("Text1","Hello!");
 //BA.debugLineNum = 45;BA.debugLine="manager.SetString(\"Auswahl1\", \"Black\")";
_manager.SetString("Auswahl1","Black");
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
}
