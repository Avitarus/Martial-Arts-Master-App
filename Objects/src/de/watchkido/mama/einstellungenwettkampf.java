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

public class einstellungenwettkampf extends Activity implements B4AActivity{
	public static einstellungenwettkampf mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.einstellungenwettkampf");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (einstellungenwettkampf).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.einstellungenwettkampf");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.einstellungenwettkampf", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (einstellungenwettkampf) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (einstellungenwettkampf) Resume **");
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
		return einstellungenwettkampf.class;
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
        BA.LogInfo("** Activity (einstellungenwettkampf) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (einstellungenwettkampf) Resume **");
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
public static int _timedelay = 0;
public static int _runden = 0;
public static int _kampfzeit = 0;
public static int _pause = 0;
public static int _wettkampftimerlautstaerke = 0;
public de.amberhome.viewpager.ViewPagerTabsWrapper _tabs = null;
public anywheresoftware.b4a.objects.PanelWrapper _line = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnvorgabe = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spntimedelay = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnkampfzeit = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnpause = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnrunden = null;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
public de.watchkido.mama.startabfragen _startabfragen = null;
public de.watchkido.mama.einstellungenapp _einstellungenapp = null;
public de.watchkido.mama.erfolgsmeldung _erfolgsmeldung = null;
public de.watchkido.mama.checkliste _checkliste = null;
public de.watchkido.mama.wettkampf _wettkampf = null;
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
 //BA.debugLineNum = 32;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 33;BA.debugLine="Activity.title = \"Einstellungen Timer\"";
mostCurrent._activity.setTitle((Object)("Einstellungen Timer"));
 //BA.debugLineNum = 34;BA.debugLine="Activity.LoadLayout(\"EinstellungenWettkampf\")";
mostCurrent._activity.LoadLayout("EinstellungenWettkampf",mostCurrent.activityBA);
 //BA.debugLineNum = 36;BA.debugLine="spnVorgabe.Prompt = \"Vorgegebene Zeiten:\"";
mostCurrent._spnvorgabe.setPrompt("Vorgegebene Zeiten:");
 //BA.debugLineNum = 37;BA.debugLine="spnVorgabe.Add(\"06:30 = 5 Runden á 30s\")";
mostCurrent._spnvorgabe.Add("06:30 = 5 Runden á 30s");
 //BA.debugLineNum = 38;BA.debugLine="spnVorgabe.Add(\"08:00 = 6 Runden á 30s\")";
mostCurrent._spnvorgabe.Add("08:00 = 6 Runden á 30s");
 //BA.debugLineNum = 39;BA.debugLine="spnVorgabe.Add(\"09:30 = 7 Runden á 30s\")";
mostCurrent._spnvorgabe.Add("09:30 = 7 Runden á 30s");
 //BA.debugLineNum = 40;BA.debugLine="spnVorgabe.Add(\"11:00 = 8 Runden á 30s\")";
mostCurrent._spnvorgabe.Add("11:00 = 8 Runden á 30s");
 //BA.debugLineNum = 41;BA.debugLine="spnVorgabe.Add(\"12:30 = 9 Runden á 30s\")";
mostCurrent._spnvorgabe.Add("12:30 = 9 Runden á 30s");
 //BA.debugLineNum = 42;BA.debugLine="spnVorgabe.Add(\"14:00 = 10 Runden á 30s\")";
mostCurrent._spnvorgabe.Add("14:00 = 10 Runden á 30s");
 //BA.debugLineNum = 43;BA.debugLine="spnVorgabe.Add(\"15:30 = 11 Runden á 30s\")";
mostCurrent._spnvorgabe.Add("15:30 = 11 Runden á 30s");
 //BA.debugLineNum = 44;BA.debugLine="spnVorgabe.Add(\"17:00 = 12 Runden á 30s\")";
mostCurrent._spnvorgabe.Add("17:00 = 12 Runden á 30s");
 //BA.debugLineNum = 45;BA.debugLine="spnVorgabe.Add(\"01:30 = 1 Runden á 1 min\")";
mostCurrent._spnvorgabe.Add("01:30 = 1 Runden á 1 min");
 //BA.debugLineNum = 46;BA.debugLine="spnVorgabe.Add(\"02:30 = 1 Runden á 2 min\")";
mostCurrent._spnvorgabe.Add("02:30 = 1 Runden á 2 min");
 //BA.debugLineNum = 47;BA.debugLine="spnVorgabe.Add(\"03:30 = 1 Runden á 3 min\")";
mostCurrent._spnvorgabe.Add("03:30 = 1 Runden á 3 min");
 //BA.debugLineNum = 48;BA.debugLine="spnVorgabe.Add(\"03:30 = 2 Runden á 1 min\")";
mostCurrent._spnvorgabe.Add("03:30 = 2 Runden á 1 min");
 //BA.debugLineNum = 49;BA.debugLine="spnVorgabe.Add(\"04:30 = 1 Runden á 4 min\")";
mostCurrent._spnvorgabe.Add("04:30 = 1 Runden á 4 min");
 //BA.debugLineNum = 50;BA.debugLine="spnVorgabe.Add(\"05:30 = 2 Runden á 2 min\")";
mostCurrent._spnvorgabe.Add("05:30 = 2 Runden á 2 min");
 //BA.debugLineNum = 51;BA.debugLine="spnVorgabe.Add(\"05:30 = 3 Runden á 1 min\")";
mostCurrent._spnvorgabe.Add("05:30 = 3 Runden á 1 min");
 //BA.debugLineNum = 52;BA.debugLine="spnVorgabe.Add(\"07:30 = 2 Runden á 3 min\")";
mostCurrent._spnvorgabe.Add("07:30 = 2 Runden á 3 min");
 //BA.debugLineNum = 53;BA.debugLine="spnVorgabe.Add(\"07:30 = 4 Runden á 1 min\")";
mostCurrent._spnvorgabe.Add("07:30 = 4 Runden á 1 min");
 //BA.debugLineNum = 54;BA.debugLine="spnVorgabe.Add(\"08:30 = 3 Runden á 2 min\")";
mostCurrent._spnvorgabe.Add("08:30 = 3 Runden á 2 min");
 //BA.debugLineNum = 55;BA.debugLine="spnVorgabe.Add(\"09:30 = 2 Runden á 4 min\")";
mostCurrent._spnvorgabe.Add("09:30 = 2 Runden á 4 min");
 //BA.debugLineNum = 56;BA.debugLine="spnVorgabe.Add(\"09:30 = 5 Runden á 1 min\")";
mostCurrent._spnvorgabe.Add("09:30 = 5 Runden á 1 min");
 //BA.debugLineNum = 57;BA.debugLine="spnVorgabe.Add(\"11:30 = 3 Runden á 3 min\")";
mostCurrent._spnvorgabe.Add("11:30 = 3 Runden á 3 min");
 //BA.debugLineNum = 58;BA.debugLine="spnVorgabe.Add(\"11:30 = 4 Runden á 2 min\")";
mostCurrent._spnvorgabe.Add("11:30 = 4 Runden á 2 min");
 //BA.debugLineNum = 59;BA.debugLine="spnVorgabe.Add(\"11:30 = 6 Runden á 1 min\")";
mostCurrent._spnvorgabe.Add("11:30 = 6 Runden á 1 min");
 //BA.debugLineNum = 60;BA.debugLine="spnVorgabe.Add(\"13:30 = 7 Runden á 1 min\")";
mostCurrent._spnvorgabe.Add("13:30 = 7 Runden á 1 min");
 //BA.debugLineNum = 61;BA.debugLine="spnVorgabe.Add(\"14:30 = 3 Runden á 4 min\")";
mostCurrent._spnvorgabe.Add("14:30 = 3 Runden á 4 min");
 //BA.debugLineNum = 62;BA.debugLine="spnVorgabe.Add(\"14:30 = 5 Runden á 2 min\")";
mostCurrent._spnvorgabe.Add("14:30 = 5 Runden á 2 min");
 //BA.debugLineNum = 63;BA.debugLine="spnVorgabe.Add(\"15:30 = 4 Runden á 3 min\")";
mostCurrent._spnvorgabe.Add("15:30 = 4 Runden á 3 min");
 //BA.debugLineNum = 64;BA.debugLine="spnVorgabe.Add(\"17:30 = 6 Runden á 2 min\")";
mostCurrent._spnvorgabe.Add("17:30 = 6 Runden á 2 min");
 //BA.debugLineNum = 65;BA.debugLine="spnVorgabe.Add(\"19:30 = 4 Runden á 4 min\")";
mostCurrent._spnvorgabe.Add("19:30 = 4 Runden á 4 min");
 //BA.debugLineNum = 66;BA.debugLine="spnVorgabe.Add(\"19:30 = 5 Runden á 3 min\")";
mostCurrent._spnvorgabe.Add("19:30 = 5 Runden á 3 min");
 //BA.debugLineNum = 67;BA.debugLine="spnVorgabe.Add(\"20:30 = 7 Runden á 2 min\")";
mostCurrent._spnvorgabe.Add("20:30 = 7 Runden á 2 min");
 //BA.debugLineNum = 68;BA.debugLine="spnVorgabe.Add(\"23:30 = 6 Runden á 3 min\")";
mostCurrent._spnvorgabe.Add("23:30 = 6 Runden á 3 min");
 //BA.debugLineNum = 69;BA.debugLine="spnVorgabe.Add(\"24:30 = 5 Runden á 4 min\")";
mostCurrent._spnvorgabe.Add("24:30 = 5 Runden á 4 min");
 //BA.debugLineNum = 70;BA.debugLine="spnVorgabe.Add(\"27:30 = 7 Runden á 3 min\")";
mostCurrent._spnvorgabe.Add("27:30 = 7 Runden á 3 min");
 //BA.debugLineNum = 71;BA.debugLine="spnVorgabe.Add(\"29:30 = 6 Runden á 4 min\")";
mostCurrent._spnvorgabe.Add("29:30 = 6 Runden á 4 min");
 //BA.debugLineNum = 72;BA.debugLine="spnVorgabe.Add(\"34:30 = 7 Runden á 4 min\")";
mostCurrent._spnvorgabe.Add("34:30 = 7 Runden á 4 min");
 //BA.debugLineNum = 73;BA.debugLine="spnVorgabe.SelectedIndex = 23";
mostCurrent._spnvorgabe.setSelectedIndex((int) (23));
 //BA.debugLineNum = 75;BA.debugLine="spnTimedelay.Prompt = \"Kampfbeginn:\"";
mostCurrent._spntimedelay.setPrompt("Kampfbeginn:");
 //BA.debugLineNum = 76;BA.debugLine="spnTimedelay.add(\"in 3 sec\")";
mostCurrent._spntimedelay.Add("in 3 sec");
 //BA.debugLineNum = 77;BA.debugLine="spnTimedelay.add(\"in 10 sec\")";
mostCurrent._spntimedelay.Add("in 10 sec");
 //BA.debugLineNum = 78;BA.debugLine="spnTimedelay.add(\"in 20 sec\")";
mostCurrent._spntimedelay.Add("in 20 sec");
 //BA.debugLineNum = 79;BA.debugLine="spnTimedelay.add(\"in 30 sec\")";
mostCurrent._spntimedelay.Add("in 30 sec");
 //BA.debugLineNum = 80;BA.debugLine="spnTimedelay.add(\"in 40 sec\")";
mostCurrent._spntimedelay.Add("in 40 sec");
 //BA.debugLineNum = 81;BA.debugLine="spnTimedelay.add(\"in 50 sec\")";
mostCurrent._spntimedelay.Add("in 50 sec");
 //BA.debugLineNum = 82;BA.debugLine="spnTimedelay.add(\"in 1 min\")";
mostCurrent._spntimedelay.Add("in 1 min");
 //BA.debugLineNum = 83;BA.debugLine="spnTimedelay.SelectedIndex = 0";
mostCurrent._spntimedelay.setSelectedIndex((int) (0));
 //BA.debugLineNum = 85;BA.debugLine="spnRunden.Prompt = \"Rundenanzahl:\"";
mostCurrent._spnrunden.setPrompt("Rundenanzahl:");
 //BA.debugLineNum = 86;BA.debugLine="spnRunden.add(\"1 Runde\")";
mostCurrent._spnrunden.Add("1 Runde");
 //BA.debugLineNum = 87;BA.debugLine="spnRunden.add(\"2 Runden\")";
mostCurrent._spnrunden.Add("2 Runden");
 //BA.debugLineNum = 88;BA.debugLine="spnRunden.add(\"3 Runden\")";
mostCurrent._spnrunden.Add("3 Runden");
 //BA.debugLineNum = 89;BA.debugLine="spnRunden.add(\"4 Runden\")";
mostCurrent._spnrunden.Add("4 Runden");
 //BA.debugLineNum = 90;BA.debugLine="spnRunden.add(\"5 Runden\")";
mostCurrent._spnrunden.Add("5 Runden");
 //BA.debugLineNum = 91;BA.debugLine="spnRunden.add(\"6 Runden\")";
mostCurrent._spnrunden.Add("6 Runden");
 //BA.debugLineNum = 92;BA.debugLine="spnRunden.add(\"7 Runden\")";
mostCurrent._spnrunden.Add("7 Runden");
 //BA.debugLineNum = 93;BA.debugLine="spnRunden.add(\"8 Runden\")";
mostCurrent._spnrunden.Add("8 Runden");
 //BA.debugLineNum = 94;BA.debugLine="spnRunden.add(\"9 Runden\")";
mostCurrent._spnrunden.Add("9 Runden");
 //BA.debugLineNum = 95;BA.debugLine="spnRunden.add(\"10 Runden\")";
mostCurrent._spnrunden.Add("10 Runden");
 //BA.debugLineNum = 96;BA.debugLine="spnRunden.add(\"11 Runden\")";
mostCurrent._spnrunden.Add("11 Runden");
 //BA.debugLineNum = 97;BA.debugLine="spnRunden.add(\"12 Runden\")";
mostCurrent._spnrunden.Add("12 Runden");
 //BA.debugLineNum = 98;BA.debugLine="spnRunden.add(\"13 Runden\")";
mostCurrent._spnrunden.Add("13 Runden");
 //BA.debugLineNum = 99;BA.debugLine="spnRunden.add(\"14 Runden\")";
mostCurrent._spnrunden.Add("14 Runden");
 //BA.debugLineNum = 100;BA.debugLine="spnRunden.add(\"15 Runden\")";
mostCurrent._spnrunden.Add("15 Runden");
 //BA.debugLineNum = 101;BA.debugLine="spnRunden.add(\"16 Runden\")";
mostCurrent._spnrunden.Add("16 Runden");
 //BA.debugLineNum = 102;BA.debugLine="spnRunden.add(\"17 Runden\")";
mostCurrent._spnrunden.Add("17 Runden");
 //BA.debugLineNum = 103;BA.debugLine="spnRunden.add(\"18 Runden\")";
mostCurrent._spnrunden.Add("18 Runden");
 //BA.debugLineNum = 104;BA.debugLine="spnRunden.add(\"19 Runden\")";
mostCurrent._spnrunden.Add("19 Runden");
 //BA.debugLineNum = 105;BA.debugLine="spnRunden.SelectedIndex = 2";
mostCurrent._spnrunden.setSelectedIndex((int) (2));
 //BA.debugLineNum = 107;BA.debugLine="spnKampfzeit.Prompt = \"Kampfzeit:\"";
mostCurrent._spnkampfzeit.setPrompt("Kampfzeit:");
 //BA.debugLineNum = 108;BA.debugLine="spnKampfzeit.Add(\"10 sec\")";
mostCurrent._spnkampfzeit.Add("10 sec");
 //BA.debugLineNum = 109;BA.debugLine="spnKampfzeit.Add(\"20 sec\")";
mostCurrent._spnkampfzeit.Add("20 sec");
 //BA.debugLineNum = 110;BA.debugLine="spnKampfzeit.Add(\"30 sec\")";
mostCurrent._spnkampfzeit.Add("30 sec");
 //BA.debugLineNum = 111;BA.debugLine="spnKampfzeit.Add(\"40 sec\")";
mostCurrent._spnkampfzeit.Add("40 sec");
 //BA.debugLineNum = 112;BA.debugLine="spnKampfzeit.Add(\"50 sec\")";
mostCurrent._spnkampfzeit.Add("50 sec");
 //BA.debugLineNum = 113;BA.debugLine="spnKampfzeit.Add(\"1 min\")";
mostCurrent._spnkampfzeit.Add("1 min");
 //BA.debugLineNum = 114;BA.debugLine="spnKampfzeit.Add(\"2 min\")";
mostCurrent._spnkampfzeit.Add("2 min");
 //BA.debugLineNum = 115;BA.debugLine="spnKampfzeit.Add(\"3 min\")";
mostCurrent._spnkampfzeit.Add("3 min");
 //BA.debugLineNum = 116;BA.debugLine="spnKampfzeit.Add(\"4 min\")";
mostCurrent._spnkampfzeit.Add("4 min");
 //BA.debugLineNum = 117;BA.debugLine="spnKampfzeit.Add(\"5 min\")";
mostCurrent._spnkampfzeit.Add("5 min");
 //BA.debugLineNum = 118;BA.debugLine="spnKampfzeit.SelectedIndex = 6";
mostCurrent._spnkampfzeit.setSelectedIndex((int) (6));
 //BA.debugLineNum = 120;BA.debugLine="spnPause.Prompt = \"Pausenzeit:\"";
mostCurrent._spnpause.setPrompt("Pausenzeit:");
 //BA.debugLineNum = 121;BA.debugLine="spnPause.Add(\"15 sec\")";
mostCurrent._spnpause.Add("15 sec");
 //BA.debugLineNum = 122;BA.debugLine="spnPause.Add(\"30 sec\")";
mostCurrent._spnpause.Add("30 sec");
 //BA.debugLineNum = 123;BA.debugLine="spnPause.Add(\"45 sec\")";
mostCurrent._spnpause.Add("45 sec");
 //BA.debugLineNum = 124;BA.debugLine="spnPause.Add(\"01:00 min\")";
mostCurrent._spnpause.Add("01:00 min");
 //BA.debugLineNum = 125;BA.debugLine="spnPause.Add(\"01:15 min\")";
mostCurrent._spnpause.Add("01:15 min");
 //BA.debugLineNum = 126;BA.debugLine="spnPause.Add(\"01:30 min\")";
mostCurrent._spnpause.Add("01:30 min");
 //BA.debugLineNum = 127;BA.debugLine="spnPause.Add(\"01:45 min\")";
mostCurrent._spnpause.Add("01:45 min");
 //BA.debugLineNum = 128;BA.debugLine="spnPause.Add(\"02:00 min\")";
mostCurrent._spnpause.Add("02:00 min");
 //BA.debugLineNum = 129;BA.debugLine="spnPause.Add(\"02:15 min\")";
mostCurrent._spnpause.Add("02:15 min");
 //BA.debugLineNum = 130;BA.debugLine="spnPause.Add(\"02:30 min\")";
mostCurrent._spnpause.Add("02:30 min");
 //BA.debugLineNum = 131;BA.debugLine="spnPause.Add(\"02:45 min\")";
mostCurrent._spnpause.Add("02:45 min");
 //BA.debugLineNum = 132;BA.debugLine="spnPause.Add(\"03:00 min\")";
mostCurrent._spnpause.Add("03:00 min");
 //BA.debugLineNum = 133;BA.debugLine="spnPause.Add(\"03:15 min\")";
mostCurrent._spnpause.Add("03:15 min");
 //BA.debugLineNum = 134;BA.debugLine="spnPause.Add(\"03:30 min\")";
mostCurrent._spnpause.Add("03:30 min");
 //BA.debugLineNum = 135;BA.debugLine="spnPause.Add(\"03:45 min\")";
mostCurrent._spnpause.Add("03:45 min");
 //BA.debugLineNum = 136;BA.debugLine="spnPause.Add(\"04:00 min\")";
mostCurrent._spnpause.Add("04:00 min");
 //BA.debugLineNum = 137;BA.debugLine="spnPause.Add(\"04:15 min\")";
mostCurrent._spnpause.Add("04:15 min");
 //BA.debugLineNum = 138;BA.debugLine="spnPause.Add(\"04:30 min\")";
mostCurrent._spnpause.Add("04:30 min");
 //BA.debugLineNum = 139;BA.debugLine="spnPause.Add(\"04:45 min\")";
mostCurrent._spnpause.Add("04:45 min");
 //BA.debugLineNum = 140;BA.debugLine="spnPause.Add(\"05:00 min\")";
mostCurrent._spnpause.Add("05:00 min");
 //BA.debugLineNum = 141;BA.debugLine="spnPause.SelectedIndex = 3";
mostCurrent._spnpause.setSelectedIndex((int) (3));
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 145;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _btnladen_click() throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb = null;
anywheresoftware.b4a.objects.collections.Map _map2 = null;
String _aktuellerunterordner = "";
int _result = 0;
int _i = 0;
 //BA.debugLineNum = 355;BA.debugLine="Sub btnLaden_click";
 //BA.debugLineNum = 357;BA.debugLine="If File.ExternalWritable = False Then";
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 358;BA.debugLine="ToastMessageShow(\"Ich kann nicht von der SD-Card lesen.\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Ich kann nicht von der SD-Card lesen.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 359;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 363;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 364;BA.debugLine="Dim Map2 As Map";
_map2 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 365;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
_aktuellerunterordner = "";
 //BA.debugLineNum = 365;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
_aktuellerunterordner = "/mama/Daten";
 //BA.debugLineNum = 366;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 370;BA.debugLine="Map2.Initialize";
_map2.Initialize();
 //BA.debugLineNum = 371;BA.debugLine="If File.Exists(File.DirRootExternal & AktuellerUnterordner, \"Wettkampf.txt\") = True Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+_aktuellerunterordner,"Wettkampf.txt")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 372;BA.debugLine="Map2 = File.ReadMap(File.DirRootExternal & AktuellerUnterordner, \"Wettkampf.txt\")";
_map2 = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+_aktuellerunterordner,"Wettkampf.txt");
 };
 //BA.debugLineNum = 375;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 376;BA.debugLine="sb.Append(\"Die Werte in der Map2 sind:\").Append(CRLF)";
_sb.Append("Die Werte in der Map2 sind:").Append(anywheresoftware.b4a.keywords.Common.CRLF);
 //BA.debugLineNum = 377;BA.debugLine="For i = 0 To Map2.Size - 1";
{
final int step302 = 1;
final int limit302 = (int) (_map2.getSize()-1);
for (_i = (int) (0); (step302 > 0 && _i <= limit302) || (step302 < 0 && _i >= limit302); _i = ((int)(0 + _i + step302))) {
 //BA.debugLineNum = 379;BA.debugLine="sb.Append(Map2.GetKeyAt(i)).append(\" = \").Append(Map2.GetValueAt(i)).append(CRLF)";
_sb.Append(BA.ObjectToString(_map2.GetKeyAt(_i))).Append(" = ").Append(BA.ObjectToString(_map2.GetValueAt(_i))).Append(anywheresoftware.b4a.keywords.Common.CRLF);
 }
};
 //BA.debugLineNum = 385;BA.debugLine="Timedelay = Map2.GetDefault(\"Timedelay\",100)'2";
_timedelay = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("Timedelay"),(Object)(100))));
 //BA.debugLineNum = 386;BA.debugLine="Runden = Map2.GetDefault(\"Runden\",17)'* startgewicht99";
_runden = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("Runden"),(Object)(17))));
 //BA.debugLineNum = 387;BA.debugLine="Kampfzeit =  Map2.GetDefault(\"Kampfzeit\",120000)'86";
_kampfzeit = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("Kampfzeit"),(Object)(120000))));
 //BA.debugLineNum = 388;BA.debugLine="Pause = Map2.GetDefault(\"Pause\",60000) 'Name = \"Avi\"";
_pause = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("Pause"),(Object)(60000))));
 //BA.debugLineNum = 389;BA.debugLine="WettkampftimerLautstaerke = Map2.GetDefault(\"WettkampftimerLautstaerke\",5) 'Name = \"Avi\"";
_wettkampftimerlautstaerke = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("WettkampftimerLautstaerke"),(Object)(5))));
 //BA.debugLineNum = 391;BA.debugLine="result = Msgbox2(\"Timedelay = \" & (Timedelay/1000) & \" s\"& CRLF & 	\"Runden = \" & Runden & CRLF & \"Kampfzeit = \" & (Kampfzeit/1000) & \" s\"& CRLF & \"Pause = \" & (Pause/1000)& \" s\", \"Geladene Daten\" , \"Ja\",\"Nein\",\"\",LoadBitmap(File.DirAssets,\"mamaLogo.png\"))";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2("Timedelay = "+BA.NumberToString((_timedelay/(double)1000))+" s"+anywheresoftware.b4a.keywords.Common.CRLF+"Runden = "+BA.NumberToString(_runden)+anywheresoftware.b4a.keywords.Common.CRLF+"Kampfzeit = "+BA.NumberToString((_kampfzeit/(double)1000))+" s"+anywheresoftware.b4a.keywords.Common.CRLF+"Pause = "+BA.NumberToString((_pause/(double)1000))+" s","Geladene Daten","Ja","Nein","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 392;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 393;BA.debugLine="ToastMessageShow(\"Erfolgreich geladen\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Erfolgreich geladen",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 394;BA.debugLine="StartActivity(Wettkampf)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._wettkampf.getObject()));
 }else {
 };
 //BA.debugLineNum = 404;BA.debugLine="End Sub";
return "";
}
public static String  _btnspeichern_click() throws Exception{
String _aktuellerunterordner = "";
int _result = 0;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 325;BA.debugLine="Sub btnSpeichern_Click";
 //BA.debugLineNum = 327;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
_aktuellerunterordner = "";
 //BA.debugLineNum = 327;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
_aktuellerunterordner = "/mama/Daten";
 //BA.debugLineNum = 329;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 331;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 332;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 333;BA.debugLine="Map1.Put(\"Timedelay\",Timedelay)";
_map1.Put((Object)("Timedelay"),(Object)(_timedelay));
 //BA.debugLineNum = 334;BA.debugLine="Map1.Put(\"Runden\",Runden)";
_map1.Put((Object)("Runden"),(Object)(_runden));
 //BA.debugLineNum = 335;BA.debugLine="Map1.Put(\"Kampfzeit\",Kampfzeit)";
_map1.Put((Object)("Kampfzeit"),(Object)(_kampfzeit));
 //BA.debugLineNum = 336;BA.debugLine="Map1.Put(\"Pause\",Pause)";
_map1.Put((Object)("Pause"),(Object)(_pause));
 //BA.debugLineNum = 339;BA.debugLine="result = Msgbox2(\"Timedelay = \" & (Timedelay/1000) &\" s\"& CRLF & 	\"Runden = \" & Runden& CRLF & \"Kampfzeit = \" & (Kampfzeit/1000) &\" s\"&  CRLF & \"Pause = \" & (Pause/1000)&\" s\", \"Speichern und Weiter\" , \"Ja\",\"Nein\",\"\",LoadBitmap(File.DirAssets,\"mamaLogo.png\"))";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2("Timedelay = "+BA.NumberToString((_timedelay/(double)1000))+" s"+anywheresoftware.b4a.keywords.Common.CRLF+"Runden = "+BA.NumberToString(_runden)+anywheresoftware.b4a.keywords.Common.CRLF+"Kampfzeit = "+BA.NumberToString((_kampfzeit/(double)1000))+" s"+anywheresoftware.b4a.keywords.Common.CRLF+"Pause = "+BA.NumberToString((_pause/(double)1000))+" s","Speichern und Weiter","Ja","Nein","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 340;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 341;BA.debugLine="File.WriteMap(File.DirRootExternal & AktuellerUnterordner, \"Wettkampf.txt\", Map1)";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+_aktuellerunterordner,"Wettkampf.txt",_map1);
 //BA.debugLineNum = 342;BA.debugLine="ToastMessageShow(\"Erfolgreich gespeichert\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Erfolgreich gespeichert",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 343;BA.debugLine="StartActivity(Wettkampf)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._wettkampf.getObject()));
 }else {
 };
 //BA.debugLineNum = 352;BA.debugLine="End Sub";
return "";
}
public static String  _btnweiter_click() throws Exception{
 //BA.debugLineNum = 407;BA.debugLine="Sub btnWeiter_Click";
 //BA.debugLineNum = 409;BA.debugLine="StartActivity(Wettkampf)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._wettkampf.getObject()));
 //BA.debugLineNum = 412;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Dim tabs As AHViewPagerTabs";
mostCurrent._tabs = new de.amberhome.viewpager.ViewPagerTabsWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim line As Panel";
mostCurrent._line = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim spnVorgabe, spnTimedelay, spnKampfzeit, spnPause, spnRunden As Spinner";
mostCurrent._spnvorgabe = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spntimedelay = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnkampfzeit = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnpause = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnrunden = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim Timedelay As Int 	: 	Timedelay = 3000";
_timedelay = 0;
 //BA.debugLineNum = 15;BA.debugLine="Dim Timedelay As Int 	: 	Timedelay = 3000";
_timedelay = (int) (3000);
 //BA.debugLineNum = 16;BA.debugLine="Dim Runden As Int 		:	Runden = 3";
_runden = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim Runden As Int 		:	Runden = 3";
_runden = (int) (3);
 //BA.debugLineNum = 17;BA.debugLine="Dim Kampfzeit As Int	:	Kampfzeit = 10000 '120000 = 2 min";
_kampfzeit = 0;
 //BA.debugLineNum = 17;BA.debugLine="Dim Kampfzeit As Int	:	Kampfzeit = 10000 '120000 = 2 min";
_kampfzeit = (int) (10000);
 //BA.debugLineNum = 18;BA.debugLine="Dim Pause As Int		:	Pause = 10000 ' 60000 = 1 min ; 1000 = 1 sec";
_pause = 0;
 //BA.debugLineNum = 18;BA.debugLine="Dim Pause As Int		:	Pause = 10000 ' 60000 = 1 min ; 1000 = 1 sec";
_pause = (int) (10000);
 //BA.debugLineNum = 19;BA.debugLine="Dim WettkampftimerLautstaerke As Int";
_wettkampftimerlautstaerke = 0;
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _spnkampfzeit_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 293;BA.debugLine="Sub spnKampfzeit_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 296;BA.debugLine="Select Position";
switch (_position) {
case 0:
case 1:
case 2:
case 3:
case 4:
case 5:
 //BA.debugLineNum = 298;BA.debugLine="Kampfzeit = (Position + 1) * 10000";
_kampfzeit = (int) ((_position+1)*10000);
 break;
case 6:
 //BA.debugLineNum = 300;BA.debugLine="Kampfzeit = 2*60000";
_kampfzeit = (int) (2*60000);
 break;
case 7:
 //BA.debugLineNum = 302;BA.debugLine="Kampfzeit = 3*60000";
_kampfzeit = (int) (3*60000);
 break;
case 8:
 //BA.debugLineNum = 304;BA.debugLine="Kampfzeit = 4*60000";
_kampfzeit = (int) (4*60000);
 break;
case 9:
 //BA.debugLineNum = 306;BA.debugLine="Kampfzeit = 5*60000";
_kampfzeit = (int) (5*60000);
 break;
default:
 //BA.debugLineNum = 308;BA.debugLine="Msgbox(\"\",\"Fehler\")";
anywheresoftware.b4a.keywords.Common.Msgbox("","Fehler",mostCurrent.activityBA);
 break;
}
;
 //BA.debugLineNum = 314;BA.debugLine="End Sub";
return "";
}
public static String  _spnpause_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 318;BA.debugLine="Sub spnPause_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 320;BA.debugLine="Pause = (Position + 1) * 15000";
_pause = (int) ((_position+1)*15000);
 //BA.debugLineNum = 323;BA.debugLine="End Sub";
return "";
}
public static String  _spnrunden_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 288;BA.debugLine="Sub spnRunden_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 290;BA.debugLine="Runden = Position + 1";
_runden = (int) (_position+1);
 //BA.debugLineNum = 292;BA.debugLine="End Sub";
return "";
}
public static String  _spntimedelay_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 280;BA.debugLine="Sub spnTimedelay_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 282;BA.debugLine="Timedelay = Position * 10000";
_timedelay = (int) (_position*10000);
 //BA.debugLineNum = 283;BA.debugLine="If Position = 0 Then Timedelay = 3000";
if (_position==0) { 
_timedelay = (int) (3000);};
 //BA.debugLineNum = 286;BA.debugLine="End Sub";
return "";
}
public static String  _spnvorgabe_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 154;BA.debugLine="Sub spnVorgabe_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 156;BA.debugLine="Timedelay = 10000 '10 sec";
_timedelay = (int) (10000);
 //BA.debugLineNum = 157;BA.debugLine="Pause = 60000'60 sec";
_pause = (int) (60000);
 //BA.debugLineNum = 159;BA.debugLine="Select Position";
switch (_position) {
case 0:
 //BA.debugLineNum = 162;BA.debugLine="Runden = 5";
_runden = (int) (5);
 //BA.debugLineNum = 163;BA.debugLine="Kampfzeit = 30000";
_kampfzeit = (int) (30000);
 break;
case 1:
 //BA.debugLineNum = 165;BA.debugLine="Runden = 6";
_runden = (int) (6);
 //BA.debugLineNum = 166;BA.debugLine="Kampfzeit = 30000";
_kampfzeit = (int) (30000);
 break;
case 2:
 //BA.debugLineNum = 168;BA.debugLine="Runden = 7";
_runden = (int) (7);
 //BA.debugLineNum = 169;BA.debugLine="Kampfzeit = 30000";
_kampfzeit = (int) (30000);
 break;
case 3:
 //BA.debugLineNum = 171;BA.debugLine="Runden = 8";
_runden = (int) (8);
 //BA.debugLineNum = 172;BA.debugLine="Kampfzeit = 30000";
_kampfzeit = (int) (30000);
 break;
case 4:
 //BA.debugLineNum = 174;BA.debugLine="Runden = 9";
_runden = (int) (9);
 //BA.debugLineNum = 175;BA.debugLine="Kampfzeit = 30000";
_kampfzeit = (int) (30000);
 break;
case 5:
 //BA.debugLineNum = 177;BA.debugLine="Runden = 10";
_runden = (int) (10);
 //BA.debugLineNum = 178;BA.debugLine="Kampfzeit = 30000";
_kampfzeit = (int) (30000);
 break;
case 6:
 //BA.debugLineNum = 180;BA.debugLine="Runden = 11";
_runden = (int) (11);
 //BA.debugLineNum = 181;BA.debugLine="Kampfzeit = 30000";
_kampfzeit = (int) (30000);
 break;
case 7:
 //BA.debugLineNum = 183;BA.debugLine="Runden = 12";
_runden = (int) (12);
 //BA.debugLineNum = 184;BA.debugLine="Kampfzeit = 30000";
_kampfzeit = (int) (30000);
 break;
case 8:
 //BA.debugLineNum = 186;BA.debugLine="Runden = 1";
_runden = (int) (1);
 //BA.debugLineNum = 187;BA.debugLine="Kampfzeit = 60000";
_kampfzeit = (int) (60000);
 break;
case 9:
 //BA.debugLineNum = 189;BA.debugLine="Runden = 1";
_runden = (int) (1);
 //BA.debugLineNum = 190;BA.debugLine="Kampfzeit = 120000";
_kampfzeit = (int) (120000);
 break;
case 10:
 //BA.debugLineNum = 192;BA.debugLine="Runden = 1";
_runden = (int) (1);
 //BA.debugLineNum = 193;BA.debugLine="Kampfzeit = 180000";
_kampfzeit = (int) (180000);
 break;
case 11:
 //BA.debugLineNum = 195;BA.debugLine="Runden = 2";
_runden = (int) (2);
 //BA.debugLineNum = 196;BA.debugLine="Kampfzeit = 60000";
_kampfzeit = (int) (60000);
 break;
case 12:
 //BA.debugLineNum = 198;BA.debugLine="Runden = 1";
_runden = (int) (1);
 //BA.debugLineNum = 199;BA.debugLine="Kampfzeit = 240000";
_kampfzeit = (int) (240000);
 break;
case 13:
 //BA.debugLineNum = 201;BA.debugLine="Runden = 2";
_runden = (int) (2);
 //BA.debugLineNum = 202;BA.debugLine="Kampfzeit = 120000";
_kampfzeit = (int) (120000);
 break;
case 14:
 //BA.debugLineNum = 204;BA.debugLine="Runden = 3";
_runden = (int) (3);
 //BA.debugLineNum = 205;BA.debugLine="Kampfzeit = 60000";
_kampfzeit = (int) (60000);
 break;
case 15:
 //BA.debugLineNum = 207;BA.debugLine="Runden = 2";
_runden = (int) (2);
 //BA.debugLineNum = 208;BA.debugLine="Kampfzeit = 180000";
_kampfzeit = (int) (180000);
 break;
case 16:
 //BA.debugLineNum = 210;BA.debugLine="Runden = 4";
_runden = (int) (4);
 //BA.debugLineNum = 211;BA.debugLine="Kampfzeit = 60000";
_kampfzeit = (int) (60000);
 break;
case 17:
 //BA.debugLineNum = 213;BA.debugLine="Runden = 3";
_runden = (int) (3);
 //BA.debugLineNum = 214;BA.debugLine="Kampfzeit = 120000";
_kampfzeit = (int) (120000);
 break;
case 18:
 //BA.debugLineNum = 216;BA.debugLine="Runden = 2";
_runden = (int) (2);
 //BA.debugLineNum = 217;BA.debugLine="Kampfzeit = 240000";
_kampfzeit = (int) (240000);
 break;
case 19:
 //BA.debugLineNum = 219;BA.debugLine="Runden = 5";
_runden = (int) (5);
 //BA.debugLineNum = 220;BA.debugLine="Kampfzeit = 60000";
_kampfzeit = (int) (60000);
 break;
case 20:
 //BA.debugLineNum = 222;BA.debugLine="Runden = 3";
_runden = (int) (3);
 //BA.debugLineNum = 223;BA.debugLine="Kampfzeit = 180000";
_kampfzeit = (int) (180000);
 break;
case 21:
 //BA.debugLineNum = 225;BA.debugLine="Runden = 4";
_runden = (int) (4);
 //BA.debugLineNum = 226;BA.debugLine="Kampfzeit = 120000";
_kampfzeit = (int) (120000);
 break;
case 22:
 //BA.debugLineNum = 228;BA.debugLine="Runden = 6";
_runden = (int) (6);
 //BA.debugLineNum = 229;BA.debugLine="Kampfzeit = 60000";
_kampfzeit = (int) (60000);
 break;
case 23:
 //BA.debugLineNum = 231;BA.debugLine="Runden = 7";
_runden = (int) (7);
 //BA.debugLineNum = 232;BA.debugLine="Kampfzeit = 60000";
_kampfzeit = (int) (60000);
 break;
case 24:
 //BA.debugLineNum = 234;BA.debugLine="Runden = 3";
_runden = (int) (3);
 //BA.debugLineNum = 235;BA.debugLine="Kampfzeit = 240000";
_kampfzeit = (int) (240000);
 break;
case 25:
 //BA.debugLineNum = 237;BA.debugLine="Runden = 5";
_runden = (int) (5);
 //BA.debugLineNum = 238;BA.debugLine="Kampfzeit = 120000";
_kampfzeit = (int) (120000);
 break;
case 26:
 //BA.debugLineNum = 240;BA.debugLine="Runden = 4";
_runden = (int) (4);
 //BA.debugLineNum = 241;BA.debugLine="Kampfzeit = 180000";
_kampfzeit = (int) (180000);
 break;
case 27:
 //BA.debugLineNum = 243;BA.debugLine="Runden = 6";
_runden = (int) (6);
 //BA.debugLineNum = 244;BA.debugLine="Kampfzeit = 120000";
_kampfzeit = (int) (120000);
 break;
case 28:
 //BA.debugLineNum = 246;BA.debugLine="Runden = 4";
_runden = (int) (4);
 //BA.debugLineNum = 247;BA.debugLine="Kampfzeit = 240000";
_kampfzeit = (int) (240000);
 break;
case 29:
 //BA.debugLineNum = 249;BA.debugLine="Runden = 5";
_runden = (int) (5);
 //BA.debugLineNum = 250;BA.debugLine="Kampfzeit = 180000";
_kampfzeit = (int) (180000);
 break;
case 30:
 //BA.debugLineNum = 252;BA.debugLine="Runden = 7";
_runden = (int) (7);
 //BA.debugLineNum = 253;BA.debugLine="Kampfzeit = 120000";
_kampfzeit = (int) (120000);
 break;
case 31:
 //BA.debugLineNum = 255;BA.debugLine="Runden = 6";
_runden = (int) (6);
 //BA.debugLineNum = 256;BA.debugLine="Kampfzeit = 180000";
_kampfzeit = (int) (180000);
 break;
case 32:
 //BA.debugLineNum = 258;BA.debugLine="Runden = 5";
_runden = (int) (5);
 //BA.debugLineNum = 259;BA.debugLine="Kampfzeit = 240000";
_kampfzeit = (int) (240000);
 break;
case 33:
 //BA.debugLineNum = 261;BA.debugLine="Runden = 7";
_runden = (int) (7);
 //BA.debugLineNum = 262;BA.debugLine="Kampfzeit = 180000";
_kampfzeit = (int) (180000);
 break;
case 34:
 //BA.debugLineNum = 264;BA.debugLine="Runden = 6";
_runden = (int) (6);
 //BA.debugLineNum = 265;BA.debugLine="Kampfzeit = 240000";
_kampfzeit = (int) (240000);
 break;
case 35:
 //BA.debugLineNum = 267;BA.debugLine="Runden = 7";
_runden = (int) (7);
 //BA.debugLineNum = 268;BA.debugLine="Kampfzeit = 240000";
_kampfzeit = (int) (240000);
 break;
default:
 //BA.debugLineNum = 270;BA.debugLine="Msgbox(\"\",\"fehler\")";
anywheresoftware.b4a.keywords.Common.Msgbox("","fehler",mostCurrent.activityBA);
 break;
}
;
 //BA.debugLineNum = 277;BA.debugLine="End Sub";
return "";
}
}
