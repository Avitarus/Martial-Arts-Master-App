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

public class einstellungenstunde extends Activity implements B4AActivity{
	public static einstellungenstunde mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.einstellungenstunde");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (einstellungenstunde).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.einstellungenstunde");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.einstellungenstunde", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (einstellungenstunde) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (einstellungenstunde) Resume **");
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
		return einstellungenstunde.class;
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
        BA.LogInfo("** Activity (einstellungenstunde) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (einstellungenstunde) Resume **");
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
public de.amberhome.viewpager.ViewPagerTabsWrapper _tabs = null;
public anywheresoftware.b4a.objects.PanelWrapper _line = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _sptheme = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnerwaermung = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spntraining = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnpause = null;
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
 //BA.debugLineNum = 24;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 103;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _btnspeichern_click() throws Exception{
 //BA.debugLineNum = 243;BA.debugLine="Sub btnSpeichern_Click";
 //BA.debugLineNum = 244;BA.debugLine="StartActivity(Training)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._training.getObject()));
 //BA.debugLineNum = 247;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim tabs As AHViewPagerTabs";
mostCurrent._tabs = new de.amberhome.viewpager.ViewPagerTabsWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim line As Panel";
mostCurrent._line = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim spTheme, spnErwaermung, spnTraining, spnPause As Spinner";
mostCurrent._sptheme = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnerwaermung = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spntraining = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnpause = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _settheme(int _theme) throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub SetTheme(Theme As Int)";
 //BA.debugLineNum = 114;BA.debugLine="Select Theme";
switch (_theme) {
case 0:
 //BA.debugLineNum = 116;BA.debugLine="tabs.Color = Colors.Black";
mostCurrent._tabs.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 117;BA.debugLine="tabs.BackgroundColorPressed = Colors.Blue";
mostCurrent._tabs.setBackgroundColorPressed(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 118;BA.debugLine="tabs.LineColorCenter = Colors.Green";
mostCurrent._tabs.setLineColorCenter(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 119;BA.debugLine="tabs.TextColor = Colors.LightGray";
mostCurrent._tabs.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 120;BA.debugLine="tabs.TextColorCenter = Colors.Green";
mostCurrent._tabs.setTextColorCenter(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 121;BA.debugLine="line.Color = Colors.Green";
mostCurrent._line.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 break;
case 1:
 //BA.debugLineNum = 123;BA.debugLine="tabs.Color = Colors.White";
mostCurrent._tabs.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 124;BA.debugLine="tabs.BackgroundColorPressed = Colors.Blue";
mostCurrent._tabs.setBackgroundColorPressed(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 125;BA.debugLine="tabs.LineColorCenter = Colors.DarkGray";
mostCurrent._tabs.setLineColorCenter(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 126;BA.debugLine="tabs.TextColor = Colors.LightGray";
mostCurrent._tabs.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 127;BA.debugLine="tabs.TextColorCenter = Colors.DarkGray";
mostCurrent._tabs.setTextColorCenter(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 128;BA.debugLine="line.Color = Colors.DarkGray";
mostCurrent._line.setColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 break;
}
;
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _setzezeiterwaermung(int _i) throws Exception{
 //BA.debugLineNum = 137;BA.debugLine="Sub SetzeZeitErwaermung(i As Int)";
 //BA.debugLineNum = 139;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 141;BA.debugLine="Main.Zeit = 10000";
mostCurrent._main._zeit = (int) (10000);
 break;
case 1:
 //BA.debugLineNum = 143;BA.debugLine="Main.Zeit = 15000";
mostCurrent._main._zeit = (int) (15000);
 break;
case 2:
 //BA.debugLineNum = 145;BA.debugLine="Main.Zeit = 20000";
mostCurrent._main._zeit = (int) (20000);
 break;
case 3:
 //BA.debugLineNum = 147;BA.debugLine="Main.Zeit = 30000";
mostCurrent._main._zeit = (int) (30000);
 break;
case 4:
 //BA.debugLineNum = 149;BA.debugLine="Main.Zeit = 40000";
mostCurrent._main._zeit = (int) (40000);
 break;
case 5:
 //BA.debugLineNum = 151;BA.debugLine="Main.Zeit = 50000";
mostCurrent._main._zeit = (int) (50000);
 break;
case 6:
 //BA.debugLineNum = 153;BA.debugLine="Main.Zeit = 60000";
mostCurrent._main._zeit = (int) (60000);
 break;
case 7:
 //BA.debugLineNum = 155;BA.debugLine="Main.Zeit = 120000";
mostCurrent._main._zeit = (int) (120000);
 break;
case 8:
 //BA.debugLineNum = 157;BA.debugLine="Main.Zeit = 180000";
mostCurrent._main._zeit = (int) (180000);
 break;
case 9:
 //BA.debugLineNum = 159;BA.debugLine="Main.Zeit = 240000";
mostCurrent._main._zeit = (int) (240000);
 break;
case 10:
 //BA.debugLineNum = 161;BA.debugLine="Main.Zeit = 300000";
mostCurrent._main._zeit = (int) (300000);
 break;
default:
 //BA.debugLineNum = 164;BA.debugLine="Main.Zeit = 5000";
mostCurrent._main._zeit = (int) (5000);
 break;
}
;
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public static String  _setzezeitpause(int _i) throws Exception{
 //BA.debugLineNum = 212;BA.debugLine="Sub SetzeZeitPause(i As Int)";
 //BA.debugLineNum = 214;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 216;BA.debugLine="Main.Zeit = 10000";
mostCurrent._main._zeit = (int) (10000);
 break;
case 1:
 //BA.debugLineNum = 218;BA.debugLine="Main.Zeit = 15000";
mostCurrent._main._zeit = (int) (15000);
 break;
case 2:
 //BA.debugLineNum = 220;BA.debugLine="Main.Zeit = 20000";
mostCurrent._main._zeit = (int) (20000);
 break;
case 3:
 //BA.debugLineNum = 222;BA.debugLine="Main.Zeit = 30000";
mostCurrent._main._zeit = (int) (30000);
 break;
case 4:
 //BA.debugLineNum = 224;BA.debugLine="Main.Zeit = 40000";
mostCurrent._main._zeit = (int) (40000);
 break;
case 5:
 //BA.debugLineNum = 226;BA.debugLine="Main.Zeit = 50000";
mostCurrent._main._zeit = (int) (50000);
 break;
case 6:
 //BA.debugLineNum = 228;BA.debugLine="Main.Zeit = 60000";
mostCurrent._main._zeit = (int) (60000);
 break;
case 7:
 //BA.debugLineNum = 230;BA.debugLine="Main.Zeit = 120000";
mostCurrent._main._zeit = (int) (120000);
 break;
case 8:
 //BA.debugLineNum = 232;BA.debugLine="Main.Zeit = 180000";
mostCurrent._main._zeit = (int) (180000);
 break;
case 9:
 //BA.debugLineNum = 234;BA.debugLine="Main.Zeit = 240000";
mostCurrent._main._zeit = (int) (240000);
 break;
case 10:
 //BA.debugLineNum = 236;BA.debugLine="Main.Zeit = 300000";
mostCurrent._main._zeit = (int) (300000);
 break;
default:
 //BA.debugLineNum = 238;BA.debugLine="Main.Zeit = 5000";
mostCurrent._main._zeit = (int) (5000);
 break;
}
;
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _setzezeittraining(int _i) throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Sub SetzeZeitTraining(i As Int)";
 //BA.debugLineNum = 177;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 179;BA.debugLine="Main.Zeit = 10000";
mostCurrent._main._zeit = (int) (10000);
 break;
case 1:
 //BA.debugLineNum = 181;BA.debugLine="Main.Zeit = 15000";
mostCurrent._main._zeit = (int) (15000);
 break;
case 2:
 //BA.debugLineNum = 183;BA.debugLine="Main.Zeit = 20000";
mostCurrent._main._zeit = (int) (20000);
 break;
case 3:
 //BA.debugLineNum = 185;BA.debugLine="Main.Zeit = 30000";
mostCurrent._main._zeit = (int) (30000);
 break;
case 4:
 //BA.debugLineNum = 187;BA.debugLine="Main.Zeit = 40000";
mostCurrent._main._zeit = (int) (40000);
 break;
case 5:
 //BA.debugLineNum = 189;BA.debugLine="Main.Zeit = 50000";
mostCurrent._main._zeit = (int) (50000);
 break;
case 6:
 //BA.debugLineNum = 191;BA.debugLine="Main.Zeit = 60000";
mostCurrent._main._zeit = (int) (60000);
 break;
case 7:
 //BA.debugLineNum = 193;BA.debugLine="Main.Zeit = 120000";
mostCurrent._main._zeit = (int) (120000);
 break;
case 8:
 //BA.debugLineNum = 195;BA.debugLine="Main.Zeit = 180000";
mostCurrent._main._zeit = (int) (180000);
 break;
case 9:
 //BA.debugLineNum = 197;BA.debugLine="Main.Zeit = 240000";
mostCurrent._main._zeit = (int) (240000);
 break;
case 10:
 //BA.debugLineNum = 199;BA.debugLine="Main.Zeit = 300000";
mostCurrent._main._zeit = (int) (300000);
 break;
default:
 //BA.debugLineNum = 201;BA.debugLine="Main.Zeit = 5000";
mostCurrent._main._zeit = (int) (5000);
 break;
}
;
 //BA.debugLineNum = 204;BA.debugLine="End Sub";
return "";
}
public static String  _spnerwaermung_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub spnErwaermung_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 133;BA.debugLine="Main.ZeitErwaermung = Position";
mostCurrent._main._zeiterwaermung = _position;
 //BA.debugLineNum = 134;BA.debugLine="SetzeZeitErwaermung(Main.ZeitErwaermung)";
_setzezeiterwaermung(mostCurrent._main._zeiterwaermung);
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _spnpause_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 206;BA.debugLine="Sub spnPause_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 208;BA.debugLine="Main.ZeitPause = Position";
mostCurrent._main._zeitpause = _position;
 //BA.debugLineNum = 209;BA.debugLine="SetzeZeitPause(Main.ZeitPause)";
_setzezeitpause(mostCurrent._main._zeitpause);
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return "";
}
public static String  _spntraining_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 169;BA.debugLine="Sub spnTraining_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 171;BA.debugLine="Main.ZeitProUebung = Position";
mostCurrent._main._zeitprouebung = _position;
 //BA.debugLineNum = 172;BA.debugLine="SetzeZeitTraining(Main.ZeitProUebung)";
_setzezeittraining(mostCurrent._main._zeitprouebung);
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _sptheme_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 108;BA.debugLine="Sub spTheme_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 109;BA.debugLine="Main.CurrentTheme = Position";
mostCurrent._main._currenttheme = _position;
 //BA.debugLineNum = 110;BA.debugLine="SetTheme(Main.CurrentTheme)";
_settheme(mostCurrent._main._currenttheme);
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
}
