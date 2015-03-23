package de.b4a.mama;

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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.b4a.mama", "de.b4a.mama.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "de.b4a.mama", "de.b4a.mama.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.b4a.mama.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (main) Resume **");
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
public static boolean _bolclubbesitzer = false;
public static String _strlistview1ergebnis = "";
public static String _strspracheid = "";
public static String _spracheid = "";
public static String _strmeineemail = "";
public static String _meineemail = "";
public static String _strmeinenummer = "";
public static String _meinenummer = "";
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
public de.b4a.mama.auswahl1 _auswahl1 = null;
public de.b4a.mama.personendatenbank _personendatenbank = null;
public de.b4a.mama.trainingstagebuch _trainingstagebuch = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (auswahl1.mostCurrent != null);
vis = vis | (personendatenbank.mostCurrent != null);
vis = vis | (trainingstagebuch.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 30;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 31;BA.debugLine="ListView1.Initialize(\"ListView1\")";
mostCurrent._listview1.Initialize(mostCurrent.activityBA,"ListView1");
 //BA.debugLineNum = 33;BA.debugLine="ListView1.AddSingleLine(\"Training\")";
mostCurrent._listview1.AddSingleLine("Training");
 //BA.debugLineNum = 34;BA.debugLine="ListView1.AddSingleLine(\"Statistik\")";
mostCurrent._listview1.AddSingleLine("Statistik");
 //BA.debugLineNum = 35;BA.debugLine="ListView1.AddSingleLine(\"Pläne\")";
mostCurrent._listview1.AddSingleLine("Pläne");
 //BA.debugLineNum = 36;BA.debugLine="ListView1.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview1.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 37;BA.debugLine="ListView1.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview1.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 38;BA.debugLine="ListView1.AddSingleLine(\"Tauschring\")";
mostCurrent._listview1.AddSingleLine("Tauschring");
 //BA.debugLineNum = 39;BA.debugLine="ListView1.AddSingleLine(\"Shop\")";
mostCurrent._listview1.AddSingleLine("Shop");
 //BA.debugLineNum = 40;BA.debugLine="ListView1.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview1.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 41;BA.debugLine="ListView1.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview1.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 42;BA.debugLine="If bolClubbesitzer Then";
if (_bolclubbesitzer) { 
 //BA.debugLineNum = 43;BA.debugLine="ListView1.AddSingleLine(\"1. Hilfe - Psychologie\")";
mostCurrent._listview1.AddSingleLine("1. Hilfe - Psychologie");
 //BA.debugLineNum = 44;BA.debugLine="ListView1.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview1.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 45;BA.debugLine="ListView1.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview1.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 46;BA.debugLine="ListView1.AddSingleLine(\"Business\")";
mostCurrent._listview1.AddSingleLine("Business");
 };
 //BA.debugLineNum = 50;BA.debugLine="Activity.AddView(ListView1, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._listview1.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 84;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
auswahl1._process_globals();
personendatenbank._process_globals();
trainingstagebuch._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _globals() throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Dim ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim p As PhoneIntents";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 53;BA.debugLine="Sub ListView1_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 54;BA.debugLine="Activity.Title = \"Sie sind hier: Startseite\"";
mostCurrent._activity.setTitle((Object)("Sie sind hier: Startseite"));
 //BA.debugLineNum = 59;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)("Personendatenbank"),(Object)("Trainingstagebuch"),(Object)("Tauschring"),(Object)("Shop"))) {
case 0:
 //BA.debugLineNum = 62;BA.debugLine="StartActivity(Personendatenbank)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._personendatenbank.getObject()));
 break;
case 1:
 //BA.debugLineNum = 64;BA.debugLine="StartActivity(Trainingstagebuch)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._trainingstagebuch.getObject()));
 break;
case 2:
 //BA.debugLineNum = 66;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.tauschen-ohne-geld.de/23041/der-tauschring-des-bushido-sport-club-neustadt-weinstrasse/?martial-arts-master-app\"))";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.tauschen-ohne-geld.de/23041/der-tauschring-des-bushido-sport-club-neustadt-weinstrasse/?martial-arts-master-app")));
 break;
case 3:
 //BA.debugLineNum = 68;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.budokonzept.de/?martial-arts-master-app\"))";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.budokonzept.de/?martial-arts-master-app")));
 break;
default:
 //BA.debugLineNum = 70;BA.debugLine="strListview1Ergebnis = Value";
_strlistview1ergebnis = BA.ObjectToString(_value);
 //BA.debugLineNum = 71;BA.debugLine="StartActivity(Auswahl1)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._auswahl1.getObject()));
 break;
}
;
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim bolClubbesitzer As Boolean : bolClubbesitzer = True";
_bolclubbesitzer = false;
 //BA.debugLineNum = 17;BA.debugLine="Dim bolClubbesitzer As Boolean : bolClubbesitzer = True";
_bolclubbesitzer = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 18;BA.debugLine="Dim strListview1Ergebnis As String";
_strlistview1ergebnis = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim strSpracheID As String: SpracheID = \"de\"";
_strspracheid = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim strSpracheID As String: SpracheID = \"de\"";
_spracheid = "de";
 //BA.debugLineNum = 20;BA.debugLine="Dim strMeineEmail As String : MeineEmail = \"mama@watchkido.de\"";
_strmeineemail = "";
 //BA.debugLineNum = 20;BA.debugLine="Dim strMeineEmail As String : MeineEmail = \"mama@watchkido.de\"";
_meineemail = "mama@watchkido.de";
 //BA.debugLineNum = 21;BA.debugLine="Dim strMeineNummer As String : MeineNummer = \"015258120499\"";
_strmeinenummer = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim strMeineNummer As String : MeineNummer = \"015258120499\"";
_meinenummer = "015258120499";
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
}
