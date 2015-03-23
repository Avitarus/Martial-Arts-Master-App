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

public class auswahl1 extends Activity implements B4AActivity{
	public static auswahl1 mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.b4a.mama", "de.b4a.mama.auswahl1");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (auswahl1).");
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
		activityBA = new BA(this, layout, processBA, "de.b4a.mama", "de.b4a.mama.auswahl1");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.b4a.mama.auswahl1", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (auswahl1) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (auswahl1) Resume **");
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
		return auswahl1.class;
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
        BA.LogInfo("** Activity (auswahl1) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (auswahl1) Resume **");
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
public anywheresoftware.b4a.objects.ListViewWrapper _listview2 = null;
public static boolean _clubbesitzer = false;
public de.b4a.mama.main _main = null;
public de.b4a.mama.personendatenbank _personendatenbank = null;
public de.b4a.mama.trainingstagebuch _trainingstagebuch = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 18;BA.debugLine="Activity.Title = \"Sie sind hier: Startseite > \" & Main.strListview1Ergebnis";
mostCurrent._activity.setTitle((Object)("Sie sind hier: Startseite > "+mostCurrent._main._strlistview1ergebnis));
 //BA.debugLineNum = 19;BA.debugLine="ListView2.Initialize(\"ListView2\")";
mostCurrent._listview2.Initialize(mostCurrent.activityBA,"ListView2");
 //BA.debugLineNum = 21;BA.debugLine="If Main.strListview1Ergebnis = \"Training\" Then Training";
if ((mostCurrent._main._strlistview1ergebnis).equals("Training")) { 
_training();};
 //BA.debugLineNum = 22;BA.debugLine="If Main.strListview1Ergebnis = \"Statistik\" Then Statistik";
if ((mostCurrent._main._strlistview1ergebnis).equals("Statistik")) { 
_statistik();};
 //BA.debugLineNum = 23;BA.debugLine="If Main.strListview1Ergebnis = \"Pläne\" Then Plaene";
if ((mostCurrent._main._strlistview1ergebnis).equals("Pläne")) { 
_plaene();};
 //BA.debugLineNum = 24;BA.debugLine="If Main.strListview1Ergebnis = \"Einstellungen Training\" Then EinstellungenTraining";
if ((mostCurrent._main._strlistview1ergebnis).equals("Einstellungen Training")) { 
_einstellungentraining();};
 //BA.debugLineNum = 25;BA.debugLine="If Main.strListview1Ergebnis = \"Einstellungen App\" Then EinstellungenApp";
if ((mostCurrent._main._strlistview1ergebnis).equals("Einstellungen App")) { 
_einstellungenapp();};
 //BA.debugLineNum = 26;BA.debugLine="If Main.strListview1Ergebnis = \"1.Hilfe / Psychologie\" Then Psychologie";
if ((mostCurrent._main._strlistview1ergebnis).equals("1.Hilfe / Psychologie")) { 
_psychologie();};
 //BA.debugLineNum = 27;BA.debugLine="If Main.strListview1Ergebnis = \"Leistungsdiagnostik\" Then Leistungsdiagnostik";
if ((mostCurrent._main._strlistview1ergebnis).equals("Leistungsdiagnostik")) { 
_leistungsdiagnostik();};
 //BA.debugLineNum = 28;BA.debugLine="If Main.strListview1Ergebnis = \"Recht und Gesetz\" Then Recht";
if ((mostCurrent._main._strlistview1ergebnis).equals("Recht und Gesetz")) { 
_recht();};
 //BA.debugLineNum = 29;BA.debugLine="If Main.strListview1Ergebnis = \"Business\" Then Business";
if ((mostCurrent._main._strlistview1ergebnis).equals("Business")) { 
_business();};
 //BA.debugLineNum = 33;BA.debugLine="Activity.AddView(ListView2, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._listview2.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 253;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 249;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 251;BA.debugLine="End Sub";
return "";
}
public static String  _business() throws Exception{
 //BA.debugLineNum = 225;BA.debugLine="Sub Business";
 //BA.debugLineNum = 228;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 229;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 230;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 231;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 232;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 233;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 234;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 235;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 236;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 237;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 238;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 239;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 240;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 241;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public static String  _einstellungenapp() throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub EinstellungenApp";
 //BA.debugLineNum = 139;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 140;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 141;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 142;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 143;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 144;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 145;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 146;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 147;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 148;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 149;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 150;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 151;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 152;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static String  _einstellungentraining() throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub EinstellungenTraining";
 //BA.debugLineNum = 116;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 117;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 118;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 119;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 120;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 121;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 122;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 123;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 124;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 125;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 126;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 127;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 128;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 129;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim ListView2 As ListView";
mostCurrent._listview2 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim Clubbesitzer As Boolean : Clubbesitzer = True";
_clubbesitzer = false;
 //BA.debugLineNum = 13;BA.debugLine="Dim Clubbesitzer As Boolean : Clubbesitzer = True";
_clubbesitzer = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _leistungsdiagnostik() throws Exception{
 //BA.debugLineNum = 181;BA.debugLine="Sub Leistungsdiagnostik";
 //BA.debugLineNum = 184;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 185;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 186;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 187;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 188;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 189;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 190;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 191;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 192;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 193;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 194;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 195;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 196;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 197;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 202;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Sub ListView1_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 36;BA.debugLine="Activity.Title = Value";
mostCurrent._activity.setTitle(_value);
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _plaene() throws Exception{
 //BA.debugLineNum = 88;BA.debugLine="Sub Plaene";
 //BA.debugLineNum = 89;BA.debugLine="ListView2.Initialize(\"Listview2\")";
mostCurrent._listview2.Initialize(mostCurrent.activityBA,"Listview2");
 //BA.debugLineNum = 91;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 92;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 93;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 94;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 95;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 96;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 97;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 98;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 99;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 100;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 101;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 102;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 103;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 104;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 109;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _psychologie() throws Exception{
 //BA.debugLineNum = 158;BA.debugLine="Sub Psychologie";
 //BA.debugLineNum = 161;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 162;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 163;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 164;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 165;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 166;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 167;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 168;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 169;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 170;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 171;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 172;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 173;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 174;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 179;BA.debugLine="End Sub";
return "";
}
public static String  _recht() throws Exception{
 //BA.debugLineNum = 203;BA.debugLine="Sub Recht";
 //BA.debugLineNum = 206;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 207;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 208;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 209;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 210;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 211;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 212;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 213;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 214;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 215;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 216;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 217;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 218;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 219;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public static String  _statistik() throws Exception{
 //BA.debugLineNum = 44;BA.debugLine="Sub Statistik";
 //BA.debugLineNum = 45;BA.debugLine="ListView2.Initialize(\"Listview2\")";
mostCurrent._listview2.Initialize(mostCurrent.activityBA,"Listview2");
 //BA.debugLineNum = 47;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 50;BA.debugLine="ListView2.AddSingleLine(\"Training\")";
mostCurrent._listview2.AddSingleLine("Training");
 //BA.debugLineNum = 51;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 52;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 53;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 54;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 55;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 56;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 57;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 58;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 59;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 60;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 61;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 62;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 63;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static String  _training() throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub Training";
 //BA.debugLineNum = 70;BA.debugLine="ListView2.AddSingleLine(\"Statistik\")";
mostCurrent._listview2.AddSingleLine("Statistik");
 //BA.debugLineNum = 71;BA.debugLine="ListView2.AddSingleLine(\"Pläne\")";
mostCurrent._listview2.AddSingleLine("Pläne");
 //BA.debugLineNum = 72;BA.debugLine="ListView2.AddSingleLine(\"Personendatenbank\")";
mostCurrent._listview2.AddSingleLine("Personendatenbank");
 //BA.debugLineNum = 73;BA.debugLine="ListView2.AddSingleLine(\"Trainingstagebuch\")";
mostCurrent._listview2.AddSingleLine("Trainingstagebuch");
 //BA.debugLineNum = 74;BA.debugLine="ListView2.AddSingleLine(\"Tauschring\")";
mostCurrent._listview2.AddSingleLine("Tauschring");
 //BA.debugLineNum = 75;BA.debugLine="ListView2.AddSingleLine(\"Shop\")";
mostCurrent._listview2.AddSingleLine("Shop");
 //BA.debugLineNum = 76;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen Training\")";
mostCurrent._listview2.AddSingleLine("Einstellungen Training");
 //BA.debugLineNum = 77;BA.debugLine="ListView2.AddSingleLine(\"Einstellungen App\")";
mostCurrent._listview2.AddSingleLine("Einstellungen App");
 //BA.debugLineNum = 78;BA.debugLine="If Clubbesitzer Then";
if (_clubbesitzer) { 
 //BA.debugLineNum = 79;BA.debugLine="ListView2.AddSingleLine(\"1. Hilfe / Psychologie\")";
mostCurrent._listview2.AddSingleLine("1. Hilfe / Psychologie");
 //BA.debugLineNum = 80;BA.debugLine="ListView2.AddSingleLine(\"Leistungsdiagnostik\")";
mostCurrent._listview2.AddSingleLine("Leistungsdiagnostik");
 //BA.debugLineNum = 81;BA.debugLine="ListView2.AddSingleLine(\"Recht und Gesetz\")";
mostCurrent._listview2.AddSingleLine("Recht und Gesetz");
 //BA.debugLineNum = 82;BA.debugLine="ListView2.AddSingleLine(\"Business\")";
mostCurrent._listview2.AddSingleLine("Business");
 };
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
}
