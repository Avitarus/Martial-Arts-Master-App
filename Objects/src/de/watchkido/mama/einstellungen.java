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

public class einstellungen extends Activity implements B4AActivity{
	public static einstellungen mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.einstellungen");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (einstellungen).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.einstellungen");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.einstellungen", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (einstellungen) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (einstellungen) Resume **");
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
		return einstellungen.class;
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
        BA.LogInfo("** Activity (einstellungen) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (einstellungen) Resume **");
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
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public static int _anfangsgewicht = 0;
public static int _zielgewicht = 0;
public static int _kontrolltag = 0;
public static int _zeitzone = 0;
public static double _maxlohn = 0;
public static double _minlohn = 0;
public static double _ausgabepreis = 0;
public static double _straflohn = 0;
public static String _name = "";
public static String _gewicht = "";
public static String _waehrung = "";
public static String _starttag = "";
public static String _zieltag = "";
public static String _startzeit = "";
public static String _zielzeit = "";
public static String _aktuellerunterordner = "";
public anywheresoftware.b4a.objects.collections.List _liste1 = null;
public anywheresoftware.b4a.objects.collections.List _liste2 = null;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
public de.watchkido.mama.startabfragen _startabfragen = null;
public de.watchkido.mama.einstellungenapp _einstellungenapp = null;
public de.watchkido.mama.erfolgsmeldung _erfolgsmeldung = null;
public de.watchkido.mama.checkliste _checkliste = null;
public de.watchkido.mama.wettkampf _wettkampf = null;
public de.watchkido.mama.einstellungenwettkampf _einstellungenwettkampf = null;
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
anywheresoftware.b4a.objects.collections.Map _map1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
 //BA.debugLineNum = 27;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 30;BA.debugLine="Dim label1 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="If File.ExternalWritable = False Then";
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 35;BA.debugLine="Msgbox(\"Ich kann nicht auf die SD-Card schreiben.\", \"A c h t u n g\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Ich kann nicht auf die SD-Card schreiben.","A c h t u n g",mostCurrent.activityBA);
 //BA.debugLineNum = 37;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 40;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 41;BA.debugLine="If File.Exists(File.DirRootExternal & AktuellerUnterordner, \"SetupApp.txt\") = True Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._aktuellerunterordner,"SetupApp.txt")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 43;BA.debugLine="Map1 = File.ReadMap(File.DirRootExternal & AktuellerUnterordner, \"SetupApp.txt\")";
_map1 = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._aktuellerunterordner,"SetupApp.txt");
 };
 //BA.debugLineNum = 48;BA.debugLine="ListView1.Initialize(\"ListView1\")";
mostCurrent._listview1.Initialize(mostCurrent.activityBA,"ListView1");
 //BA.debugLineNum = 49;BA.debugLine="label1 = ListView1.SingleLineLayout.Label 'set the label to the model label.";
_label1 = mostCurrent._listview1.getSingleLineLayout().Label;
 //BA.debugLineNum = 50;BA.debugLine="label1.TextSize = 30";
_label1.setTextSize((float) (30));
 //BA.debugLineNum = 51;BA.debugLine="label1.TextColor = Colors.blue";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 52;BA.debugLine="label1.Gravity = Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 55;BA.debugLine="Zeitzone = Map1.GetDefault(\"Zeitzone\",2)'2";
_zeitzone = (int)(BA.ObjectToNumber(_map1.GetDefault((Object)("Zeitzone"),(Object)(2))));
 //BA.debugLineNum = 56;BA.debugLine="Anfangsgewicht = Map1.GetDefault(\"Anfangsgewicht\",99)'* startgewicht99";
_anfangsgewicht = (int)(BA.ObjectToNumber(_map1.GetDefault((Object)("Anfangsgewicht"),(Object)(99))));
 //BA.debugLineNum = 57;BA.debugLine="Zielgewicht =  Map1.GetDefault(\"Zielgewicht\",86)'86";
_zielgewicht = (int)(BA.ObjectToNumber(_map1.GetDefault((Object)("Zielgewicht"),(Object)(86))));
 //BA.debugLineNum = 58;BA.debugLine="Name = Map1.GetDefault(\"Name\",\"Avita\") 'Name = \"Avi\"";
mostCurrent._name = BA.ObjectToString(_map1.GetDefault((Object)("Name"),(Object)("Avita")));
 //BA.debugLineNum = 59;BA.debugLine="Ausgabepreis = Map1.GetDefault(\"Ausgabepreis\",300)'300";
_ausgabepreis = (double)(BA.ObjectToNumber(_map1.GetDefault((Object)("Ausgabepreis"),(Object)(300))));
 //BA.debugLineNum = 60;BA.debugLine="MinLohn = Map1.GetDefault(\"MinLohn\",0.2)'0.20";
_minlohn = (double)(BA.ObjectToNumber(_map1.GetDefault((Object)("MinLohn"),(Object)(0.2))));
 //BA.debugLineNum = 61;BA.debugLine="MaxLohn = Map1.GetDefault(\"MaxLohn\",5.0)'5";
_maxlohn = (double)(BA.ObjectToNumber(_map1.GetDefault((Object)("MaxLohn"),(Object)(5.0))));
 //BA.debugLineNum = 62;BA.debugLine="Gewicht = Map1.GetDefault(\"Gewicht\",\" kg\") ' kg oder lbs";
mostCurrent._gewicht = BA.ObjectToString(_map1.GetDefault((Object)("Gewicht"),(Object)(" kg")));
 //BA.debugLineNum = 63;BA.debugLine="Waehrung = Map1.GetDefault(\"Waehrung\",\"€\")";
mostCurrent._waehrung = BA.ObjectToString(_map1.GetDefault((Object)("Waehrung"),(Object)("€")));
 //BA.debugLineNum = 66;BA.debugLine="Kontrolltag = Map1.GetDefault(\"Kontrolltag\",1)'2";
_kontrolltag = (int)(BA.ObjectToNumber(_map1.GetDefault((Object)("Kontrolltag"),(Object)(1))));
 //BA.debugLineNum = 67;BA.debugLine="Starttag = Map1.GetDefault(\"Starttag\",\"04/07/2012\")'\"04/07/2012\" 'Starttag";
mostCurrent._starttag = BA.ObjectToString(_map1.GetDefault((Object)("Starttag"),(Object)("04/07/2012")));
 //BA.debugLineNum = 68;BA.debugLine="Startzeit = Map1.GetDefault(\"Startzeit\",\"00:00:01\")'\"00:00:01\"";
mostCurrent._startzeit = BA.ObjectToString(_map1.GetDefault((Object)("Startzeit"),(Object)("00:00:01")));
 //BA.debugLineNum = 69;BA.debugLine="Zieltag = Map1.GetDefault(\"Zieltag\",\"07/31/2012\")'\"07/31/2012\" 'gibts den tag?";
mostCurrent._zieltag = BA.ObjectToString(_map1.GetDefault((Object)("Zieltag"),(Object)("07/31/2012")));
 //BA.debugLineNum = 70;BA.debugLine="Zielzeit = Map1.GetDefault(\"Zielzeit\",\"23:59:59\")'\"23:59:59\"";
mostCurrent._zielzeit = BA.ObjectToString(_map1.GetDefault((Object)("Zielzeit"),(Object)("23:59:59")));
 //BA.debugLineNum = 71;BA.debugLine="StrafLohn = Map1.GetDefault(\"StrafLohn\",1)'1";
_straflohn = (double)(BA.ObjectToNumber(_map1.GetDefault((Object)("StrafLohn"),(Object)(1))));
 //BA.debugLineNum = 73;BA.debugLine="Auswahl'sprung zum listview";
_auswahl();
 //BA.debugLineNum = 75;BA.debugLine="Activity.AddView(ListView1, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._listview1.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 77;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 420;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 422;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 416;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 418;BA.debugLine="End Sub";
return "";
}
public static String  _auswahl() throws Exception{
 //BA.debugLineNum = 349;BA.debugLine="Sub Auswahl";
 //BA.debugLineNum = 353;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 356;BA.debugLine="ListView1.AddSingleLine(\"Einstellungen\")";
mostCurrent._listview1.AddSingleLine("Einstellungen");
 //BA.debugLineNum = 357;BA.debugLine="ListView1.AddTwoLines(\"Name\",Name)";
mostCurrent._listview1.AddTwoLines("Name",mostCurrent._name);
 //BA.debugLineNum = 358;BA.debugLine="ListView1.AddSingleLine(\"Gewichte\")";
mostCurrent._listview1.AddSingleLine("Gewichte");
 //BA.debugLineNum = 359;BA.debugLine="ListView1.AddTwoLines(\"Gewicht\", Gewicht)";
mostCurrent._listview1.AddTwoLines("Gewicht",mostCurrent._gewicht);
 //BA.debugLineNum = 360;BA.debugLine="ListView1.AddTwoLines(\"Anfangsgewicht\",Anfangsgewicht & Gewicht)";
mostCurrent._listview1.AddTwoLines("Anfangsgewicht",BA.NumberToString(_anfangsgewicht)+mostCurrent._gewicht);
 //BA.debugLineNum = 361;BA.debugLine="ListView1.AddTwoLines(\"Zielgewicht\",Zielgewicht & Gewicht)";
mostCurrent._listview1.AddTwoLines("Zielgewicht",BA.NumberToString(_zielgewicht)+mostCurrent._gewicht);
 //BA.debugLineNum = 362;BA.debugLine="ListView1.AddSingleLine(\"Preise\")";
mostCurrent._listview1.AddSingleLine("Preise");
 //BA.debugLineNum = 363;BA.debugLine="ListView1.AddTwoLines(\"Währung\", Waehrung)";
mostCurrent._listview1.AddTwoLines("Währung",mostCurrent._waehrung);
 //BA.debugLineNum = 364;BA.debugLine="ListView1.AddTwoLines(\"Ausgabepreis gesamt\",Ausgabepreis & Waehrung)";
mostCurrent._listview1.AddTwoLines("Ausgabepreis gesamt",BA.NumberToString(_ausgabepreis)+mostCurrent._waehrung);
 //BA.debugLineNum = 365;BA.debugLine="ListView1.AddTwoLines(\"Minimaler Lohn\",MinLohn & Waehrung)";
mostCurrent._listview1.AddTwoLines("Minimaler Lohn",BA.NumberToString(_minlohn)+mostCurrent._waehrung);
 //BA.debugLineNum = 366;BA.debugLine="ListView1.AddTwoLines(\"Maximaler Lohn\",MaxLohn & Waehrung)";
mostCurrent._listview1.AddTwoLines("Maximaler Lohn",BA.NumberToString(_maxlohn)+mostCurrent._waehrung);
 //BA.debugLineNum = 367;BA.debugLine="ListView1.AddTwoLines(\"StrafLohn\",StrafLohn & Waehrung)";
mostCurrent._listview1.AddTwoLines("StrafLohn",BA.NumberToString(_straflohn)+mostCurrent._waehrung);
 //BA.debugLineNum = 368;BA.debugLine="ListView1.AddSingleLine(\"Zeiten\")";
mostCurrent._listview1.AddSingleLine("Zeiten");
 //BA.debugLineNum = 369;BA.debugLine="ListView1.AddTwoLines(\"Kontrolltag Rythmus\",Kontrolltag)";
mostCurrent._listview1.AddTwoLines("Kontrolltag Rythmus",BA.NumberToString(_kontrolltag));
 //BA.debugLineNum = 370;BA.debugLine="ListView1.AddTwoLines(\"Starttag (MM/TT/JJJJ)\",Starttag)";
mostCurrent._listview1.AddTwoLines("Starttag (MM/TT/JJJJ)",mostCurrent._starttag);
 //BA.debugLineNum = 371;BA.debugLine="ListView1.AddTwoLines(\"Startzeit\",Startzeit)";
mostCurrent._listview1.AddTwoLines("Startzeit",mostCurrent._startzeit);
 //BA.debugLineNum = 372;BA.debugLine="ListView1.AddTwoLines(\"Zieltag (MM/TT/JJJJ)\",Zieltag)";
mostCurrent._listview1.AddTwoLines("Zieltag (MM/TT/JJJJ)",mostCurrent._zieltag);
 //BA.debugLineNum = 373;BA.debugLine="ListView1.AddTwoLines(\"Zielzeit\",Zielzeit)";
mostCurrent._listview1.AddTwoLines("Zielzeit",mostCurrent._zielzeit);
 //BA.debugLineNum = 374;BA.debugLine="ListView1.AddTwoLines(\"Zeitzone\",Zeitzone)";
mostCurrent._listview1.AddTwoLines("Zeitzone",BA.NumberToString(_zeitzone));
 //BA.debugLineNum = 375;BA.debugLine="ListView1.AddSingleLine(\"Extras\")";
mostCurrent._listview1.AddSingleLine("Extras");
 //BA.debugLineNum = 376;BA.debugLine="ListView1.AddTwoLines2 (\"Eula\",\"\",4301)";
mostCurrent._listview1.AddTwoLines2("Eula","",(Object)(4301));
 //BA.debugLineNum = 377;BA.debugLine="ListView1.AddTwoLines2 (\"Farben\",\"\",4302 )";
mostCurrent._listview1.AddTwoLines2("Farben","",(Object)(4302));
 //BA.debugLineNum = 378;BA.debugLine="ListView1.AddTwoLines2 (\"eMail-Feedback\",\"\",4303 )";
mostCurrent._listview1.AddTwoLines2("eMail-Feedback","",(Object)(4303));
 //BA.debugLineNum = 379;BA.debugLine="ListView1.AddTwoLines2 (\"Lizensschlüssel\",\"\",4304 )";
mostCurrent._listview1.AddTwoLines2("Lizensschlüssel","",(Object)(4304));
 //BA.debugLineNum = 380;BA.debugLine="ListView1.AddTwoLines2 (\"Passwort\",\"\",4305 )";
mostCurrent._listview1.AddTwoLines2("Passwort","",(Object)(4305));
 //BA.debugLineNum = 381;BA.debugLine="ListView1.AddTwoLines2 (\"Spenden\",\"\",4306)";
mostCurrent._listview1.AddTwoLines2("Spenden","",(Object)(4306));
 //BA.debugLineNum = 382;BA.debugLine="ListView1.AddSingleLine2 (\"Widgeds\",3000)";
mostCurrent._listview1.AddSingleLine2("Widgeds",(Object)(3000));
 //BA.debugLineNum = 383;BA.debugLine="ListView1.AddTwoLines2 (\"Erfolge\",\"\",4308 )";
mostCurrent._listview1.AddTwoLines2("Erfolge","",(Object)(4308));
 //BA.debugLineNum = 384;BA.debugLine="ListView1.AddTwoLines2 (\"Ziele\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Ziele","",(Object)(4309));
 //BA.debugLineNum = 385;BA.debugLine="ListView1.AddSingleLine2 (\"Soziale Netzwerke\",3000)";
mostCurrent._listview1.AddSingleLine2("Soziale Netzwerke",(Object)(3000));
 //BA.debugLineNum = 386;BA.debugLine="ListView1.AddTwoLines2 (\"Facebook\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Facebook","",(Object)(4309));
 //BA.debugLineNum = 387;BA.debugLine="ListView1.AddTwoLines2 (\"Wer-kennt-Wen\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Wer-kennt-Wen","",(Object)(4309));
 //BA.debugLineNum = 388;BA.debugLine="ListView1.AddTwoLines2 (\"Xing\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Xing","",(Object)(4309));
 //BA.debugLineNum = 389;BA.debugLine="ListView1.AddTwoLines2 (\"Linkedin\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Linkedin","",(Object)(4309));
 //BA.debugLineNum = 390;BA.debugLine="ListView1.AddTwoLines2 (\"Twitter\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Twitter","",(Object)(4309));
 //BA.debugLineNum = 391;BA.debugLine="ListView1.AddTwoLines2 (\"Kampfkunstforum\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Kampfkunstforum","",(Object)(4309));
 //BA.debugLineNum = 392;BA.debugLine="ListView1.AddTwoLines2 (\"Internetseite\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Internetseite","",(Object)(4309));
 //BA.debugLineNum = 393;BA.debugLine="ListView1.AddTwoLines2 (\"Youtube\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Youtube","",(Object)(4309));
 //BA.debugLineNum = 394;BA.debugLine="ListView1.AddTwoLines2 (\"KampfsportTV\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("KampfsportTV","",(Object)(4309));
 //BA.debugLineNum = 395;BA.debugLine="ListView1.AddTwoLines2 (\"OpenSports\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("OpenSports","",(Object)(4309));
 //BA.debugLineNum = 396;BA.debugLine="ListView1.AddTwoLines2 (\"Skype\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Skype","",(Object)(4309));
 //BA.debugLineNum = 397;BA.debugLine="ListView1.AddTwoLines2 (\"Jappi\",\"\",4309)";
mostCurrent._listview1.AddTwoLines2("Jappi","",(Object)(4309));
 //BA.debugLineNum = 403;BA.debugLine="ListView1.AddSingleLine(\"Speichern\")";
mostCurrent._listview1.AddSingleLine("Speichern");
 //BA.debugLineNum = 405;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"Jetzt Abspeichern\",\"Klicken Sie hier zum speichern\", LoadBitmap(File.DirAssets,\"mamaLogo.png\"))";
mostCurrent._listview1.AddTwoLinesAndBitmap("Jetzt Abspeichern","Klicken Sie hier zum speichern",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png").getObject()));
 //BA.debugLineNum = 413;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim Anfangsgewicht, Zielgewicht, Kontrolltag, Zeitzone As Int";
_anfangsgewicht = 0;
_zielgewicht = 0;
_kontrolltag = 0;
_zeitzone = 0;
 //BA.debugLineNum = 18;BA.debugLine="Dim MaxLohn, MinLohn, Ausgabepreis,StrafLohn As Double";
_maxlohn = 0;
_minlohn = 0;
_ausgabepreis = 0;
_straflohn = 0;
 //BA.debugLineNum = 19;BA.debugLine="Dim Name, Gewicht, Waehrung As String";
mostCurrent._name = "";
mostCurrent._gewicht = "";
mostCurrent._waehrung = "";
 //BA.debugLineNum = 20;BA.debugLine="Dim Starttag, Zieltag, Startzeit, Zielzeit As String";
mostCurrent._starttag = "";
mostCurrent._zieltag = "";
mostCurrent._startzeit = "";
mostCurrent._zielzeit = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
mostCurrent._aktuellerunterordner = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
mostCurrent._aktuellerunterordner = "/mama/Daten";
 //BA.debugLineNum = 23;BA.debugLine="Dim Liste1, Liste2 As List";
mostCurrent._liste1 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._liste2 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
int _ret = 0;
anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog _fd = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog _dd = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.TimeDialog _td = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.NumberDialog _nd = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog _id = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
int _result = 0;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 79;BA.debugLine="Sub ListView1_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 85;BA.debugLine="Dim ret As Int";
_ret = 0;
 //BA.debugLineNum = 86;BA.debugLine="Dim fd As FileDialog";
_fd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog();
 //BA.debugLineNum = 87;BA.debugLine="Dim Dd As DateDialog";
_dd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog();
 //BA.debugLineNum = 88;BA.debugLine="Dim td As TimeDialog";
_td = new anywheresoftware.b4a.agraham.dialogs.InputDialog.TimeDialog();
 //BA.debugLineNum = 89;BA.debugLine="Dim nd As NumberDialog";
_nd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.NumberDialog();
 //BA.debugLineNum = 90;BA.debugLine="Dim Id As InputDialog";
_id = new anywheresoftware.b4a.agraham.dialogs.InputDialog();
 //BA.debugLineNum = 91;BA.debugLine="Dim Bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Bmp.Initialize(File.DirAssets, \"mamaLogo.png\")";
_bmp.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png");
 //BA.debugLineNum = 94;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)("Einstellungen"),(Object)("Zeiten"),(Object)("Gewichte"),(Object)("Gewicht"),(Object)("Währung"),(Object)("Zeitzone"),(Object)("Anfangsgewicht"),(Object)("Zielgewicht"),(Object)("Name"),(Object)("Preise"),(Object)("Ausgabepreis gesamt"),(Object)("StrafLohn"),(Object)("Minimaler Lohn"),(Object)("Maximaler Lohn"),(Object)("Kontrolltag Rythmus"),(Object)("Starttag (MM/TT/JJJJ)"),(Object)("Startzeit"),(Object)("Zieltag (MM/TT/JJJJ)"),(Object)("Zielzeit"),(Object)("Speichern"),(Object)("Jetzt Abspeichern"))) {
case 0:
 //BA.debugLineNum = 96;BA.debugLine="Msgbox(\":\",\"Einstellungen\")";
anywheresoftware.b4a.keywords.Common.Msgbox(":","Einstellungen",mostCurrent.activityBA);
 break;
case 1:
 //BA.debugLineNum = 99;BA.debugLine="Msgbox(\":\",\"Erklärung der Zeiten\")";
anywheresoftware.b4a.keywords.Common.Msgbox(":","Erklärung der Zeiten",mostCurrent.activityBA);
 break;
case 2:
 //BA.debugLineNum = 102;BA.debugLine="Msgbox(\":\",\"Erklärung der Gewichte\")";
anywheresoftware.b4a.keywords.Common.Msgbox(":","Erklärung der Gewichte",mostCurrent.activityBA);
 break;
case 3:
 //BA.debugLineNum = 105;BA.debugLine="Liste1.Initialize";
mostCurrent._liste1.Initialize();
 //BA.debugLineNum = 106;BA.debugLine="Liste1.Add(\"kg\")";
mostCurrent._liste1.Add((Object)("kg"));
 //BA.debugLineNum = 107;BA.debugLine="Liste1.Add(\"lbs\")";
mostCurrent._liste1.Add((Object)("lbs"));
 //BA.debugLineNum = 108;BA.debugLine="ret = InputList(Liste1, \"Einheit\",0)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(mostCurrent._liste1,"Einheit",(int) (0),mostCurrent.activityBA);
 //BA.debugLineNum = 110;BA.debugLine="Select ret";
switch (_ret) {
case 0:
 //BA.debugLineNum = 112;BA.debugLine="Gewicht = \" kg\"";
mostCurrent._gewicht = " kg";
 break;
case 1:
 //BA.debugLineNum = 114;BA.debugLine="Gewicht = \" lbs\"";
mostCurrent._gewicht = " lbs";
 break;
default:
 //BA.debugLineNum = 116;BA.debugLine="Msgbox(\"?\",\"?\")";
anywheresoftware.b4a.keywords.Common.Msgbox("?","?",mostCurrent.activityBA);
 break;
}
;
 break;
case 4:
 //BA.debugLineNum = 121;BA.debugLine="Liste2.Initialize";
mostCurrent._liste2.Initialize();
 //BA.debugLineNum = 122;BA.debugLine="Liste2.Add(\"Euro\")";
mostCurrent._liste2.Add((Object)("Euro"));
 //BA.debugLineNum = 123;BA.debugLine="Liste2.Add(\"Dollar\")";
mostCurrent._liste2.Add((Object)("Dollar"));
 //BA.debugLineNum = 124;BA.debugLine="Liste2.Add(\"Pounds\")";
mostCurrent._liste2.Add((Object)("Pounds"));
 //BA.debugLineNum = 125;BA.debugLine="ret = InputList(Liste2, \"Geld Einheit\",0)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(mostCurrent._liste2,"Geld Einheit",(int) (0),mostCurrent.activityBA);
 //BA.debugLineNum = 127;BA.debugLine="Select ret";
switch (_ret) {
case 0:
 //BA.debugLineNum = 129;BA.debugLine="Waehrung = \" €\"";
mostCurrent._waehrung = " €";
 break;
case 1:
 //BA.debugLineNum = 131;BA.debugLine="Waehrung = \" $\"";
mostCurrent._waehrung = " $";
 break;
case 2:
 //BA.debugLineNum = 133;BA.debugLine="Waehrung = \" £\"";
mostCurrent._waehrung = " £";
 break;
default:
 //BA.debugLineNum = 135;BA.debugLine="Msgbox(\"?\",\"?\")";
anywheresoftware.b4a.keywords.Common.Msgbox("?","?",mostCurrent.activityBA);
 break;
}
;
 break;
case 5:
 //BA.debugLineNum = 140;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 141;BA.debugLine="nd.Number = Zeitzone";
_nd.setNumber(_zeitzone);
 //BA.debugLineNum = 142;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 143;BA.debugLine="nd.ShowSign = True";
_nd.setShowSign(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 144;BA.debugLine="ret = nd.Show(\"Zeitzone\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Zeitzone","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 146;BA.debugLine="Zeitzone = nd.Number";
_zeitzone = _nd.getNumber();
 break;
case 6:
 //BA.debugLineNum = 150;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 151;BA.debugLine="nd.Number = Anfangsgewicht";
_nd.setNumber(_anfangsgewicht);
 //BA.debugLineNum = 152;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 154;BA.debugLine="ret = nd.Show(\"Anfangsgewicht\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Anfangsgewicht","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 156;BA.debugLine="Anfangsgewicht = nd.number / Power(10, nd.Decimal)";
_anfangsgewicht = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 7:
 //BA.debugLineNum = 159;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 160;BA.debugLine="nd.Number = Zielgewicht";
_nd.setNumber(_zielgewicht);
 //BA.debugLineNum = 161;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 163;BA.debugLine="ret = nd.Show(\"Zielgewicht\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Zielgewicht","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 165;BA.debugLine="Zielgewicht = nd.number / Power(10, nd.Decimal)";
_zielgewicht = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 8:
 //BA.debugLineNum = 168;BA.debugLine="Id.PasswordMode = False";
_id.setPasswordMode(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 172;BA.debugLine="Id.InputType = Id.INPUT_TYPE_TEXT";
_id.setInputType(_id.INPUT_TYPE_TEXT);
 //BA.debugLineNum = 173;BA.debugLine="Id.input = Name";
_id.setInput(mostCurrent._name);
 //BA.debugLineNum = 174;BA.debugLine="Id.Hint = \"Bitte Name eingeben!\"";
_id.setHint("Bitte Name eingeben!");
 //BA.debugLineNum = 175;BA.debugLine="Id.HintColor = Colors.ARGB(196, 255, 140, 0)";
_id.setHintColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (196),(int) (255),(int) (140),(int) (0)));
 //BA.debugLineNum = 176;BA.debugLine="ret = DialogResponse.CANCEL";
_ret = anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL;
 //BA.debugLineNum = 177;BA.debugLine="ret = Id.Show(\"Geben sie einen kurzen Text ein\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _id.Show("Geben sie einen kurzen Text ein","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 180;BA.debugLine="Name = Id.input";
mostCurrent._name = _id.getInput();
 break;
case 9:
 //BA.debugLineNum = 183;BA.debugLine="Msgbox(\":\",\"Preise\")";
anywheresoftware.b4a.keywords.Common.Msgbox(":","Preise",mostCurrent.activityBA);
 break;
case 10:
 //BA.debugLineNum = 186;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 187;BA.debugLine="nd.Number = Ausgabepreis";
_nd.setNumber((int) (_ausgabepreis));
 //BA.debugLineNum = 188;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 190;BA.debugLine="ret = nd.Show(\"Ausgabepreis gesamt\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Ausgabepreis gesamt","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 192;BA.debugLine="Ausgabepreis = nd.Number / Power(10, nd.Decimal)";
_ausgabepreis = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 11:
 //BA.debugLineNum = 195;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 196;BA.debugLine="nd.Number = StrafLohn";
_nd.setNumber((int) (_straflohn));
 //BA.debugLineNum = 197;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 199;BA.debugLine="ret = nd.Show(\"Starflohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Starflohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 201;BA.debugLine="StrafLohn = nd.Number / Power(10, nd.Decimal)";
_straflohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 12:
 //BA.debugLineNum = 205;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 206;BA.debugLine="nd.Number = MinLohn";
_nd.setNumber((int) (_minlohn));
 //BA.debugLineNum = 207;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 209;BA.debugLine="ret = nd.Show(\"Minimaler Tageslohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Minimaler Tageslohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 211;BA.debugLine="MinLohn = nd.number / Power(10, nd.Decimal)";
_minlohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 13:
 //BA.debugLineNum = 215;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 216;BA.debugLine="nd.Number = MaxLohn";
_nd.setNumber((int) (_maxlohn));
 //BA.debugLineNum = 217;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 219;BA.debugLine="ret = nd.Show(\"Maximaler Tageslohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Maximaler Tageslohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 221;BA.debugLine="MaxLohn = nd.Number / Power(10, nd.Decimal)";
_maxlohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 14:
 //BA.debugLineNum = 225;BA.debugLine="nd.Digits = 2";
_nd.setDigits((int) (2));
 //BA.debugLineNum = 226;BA.debugLine="nd.Number = Kontrolltag";
_nd.setNumber(_kontrolltag);
 //BA.debugLineNum = 227;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 229;BA.debugLine="ret = nd.Show(\"Kontrolltag Rythmus\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Kontrolltag Rythmus","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 231;BA.debugLine="Kontrolltag = nd.number / Power(10, nd.Decimal)";
_kontrolltag = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 15:
 //BA.debugLineNum = 234;BA.debugLine="Dd.Year = DateTime.GetYear(DateTime.Now)";
_dd.setYear(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 235;BA.debugLine="Dd.Month = DateTime.GetMonth(DateTime.Now)";
_dd.setMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 236;BA.debugLine="Dd.DayOfMonth = DateTime.GetDayOfMonth(DateTime.Now)";
_dd.setDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 237;BA.debugLine="ret = Dd.Show(\"Setzen Sie den Starttag\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _dd.Show("Setzen Sie den Starttag","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 239;BA.debugLine="Starttag = Dd.Month & \"/\" & Dd.DayOfMonth & \"/\" & Dd.Year";
mostCurrent._starttag = BA.NumberToString(_dd.getMonth())+"/"+BA.NumberToString(_dd.getDayOfMonth())+"/"+BA.NumberToString(_dd.getYear());
 break;
case 16:
 //BA.debugLineNum = 242;BA.debugLine="td.Hour = DateTime.GetHour(DateTime.Now)";
_td.setHour(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 243;BA.debugLine="td.Minute = DateTime.GetMinute(DateTime.Now)";
_td.setMinute(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 244;BA.debugLine="td.Is24Hours = True";
_td.setIs24Hours(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 245;BA.debugLine="ret = td.Show(\"Setzen Sie die Startzeit\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _td.Show("Setzen Sie die Startzeit","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 248;BA.debugLine="Startzeit = td.Hour & \":\" & td.Minute";
mostCurrent._startzeit = BA.NumberToString(_td.getHour())+":"+BA.NumberToString(_td.getMinute());
 break;
case 17:
 //BA.debugLineNum = 251;BA.debugLine="Dd.Year = DateTime.GetYear(DateTime.Now)";
_dd.setYear(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 252;BA.debugLine="Dd.Month = DateTime.GetMonth(DateTime.Now)";
_dd.setMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 253;BA.debugLine="Dd.DayOfMonth = DateTime.GetDayOfMonth(DateTime.Now)";
_dd.setDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 254;BA.debugLine="ret = Dd.Show(\"Setze das Zieldatum\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _dd.Show("Setze das Zieldatum","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 256;BA.debugLine="Zieltag = Dd.Month & \"/\" & Dd.DayOfMonth & \"/\" & Dd.Year";
mostCurrent._zieltag = BA.NumberToString(_dd.getMonth())+"/"+BA.NumberToString(_dd.getDayOfMonth())+"/"+BA.NumberToString(_dd.getYear());
 break;
case 18:
 //BA.debugLineNum = 260;BA.debugLine="td.Hour = DateTime.GetHour(DateTime.Now)";
_td.setHour(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 261;BA.debugLine="td.Minute = DateTime.GetMinute(DateTime.Now)";
_td.setMinute(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 262;BA.debugLine="td.Is24Hours = True";
_td.setIs24Hours(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 263;BA.debugLine="ret = td.Show(\"Setzen Sie die Zielzeit\", \"Fette Euro#s\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _td.Show("Setzen Sie die Zielzeit","Fette Euro#s","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 265;BA.debugLine="Zielzeit = td.Hour & \":\" & td.Minute";
mostCurrent._zielzeit = BA.NumberToString(_td.getHour())+":"+BA.NumberToString(_td.getMinute());
 break;
case 19:
 //BA.debugLineNum = 292;BA.debugLine="Msgbox(\":\",\"Abspeichern?\")";
anywheresoftware.b4a.keywords.Common.Msgbox(":","Abspeichern?",mostCurrent.activityBA);
 break;
case 20:
 //BA.debugLineNum = 306;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 308;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 309;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 310;BA.debugLine="Map1.Put(\"Name\",Name)";
_map1.Put((Object)("Name"),(Object)(mostCurrent._name));
 //BA.debugLineNum = 311;BA.debugLine="Map1.Put(\"Gewicht\",Gewicht)";
_map1.Put((Object)("Gewicht"),(Object)(mostCurrent._gewicht));
 //BA.debugLineNum = 312;BA.debugLine="Map1.Put(\"Anfangsgewicht\",Anfangsgewicht)";
_map1.Put((Object)("Anfangsgewicht"),(Object)(_anfangsgewicht));
 //BA.debugLineNum = 313;BA.debugLine="Map1.Put(\"Zielgewicht\",Zielgewicht)";
_map1.Put((Object)("Zielgewicht"),(Object)(_zielgewicht));
 //BA.debugLineNum = 314;BA.debugLine="Map1.Put(\"Waehrung\",Waehrung)";
_map1.Put((Object)("Waehrung"),(Object)(mostCurrent._waehrung));
 //BA.debugLineNum = 315;BA.debugLine="Map1.Put(\"Ausgabepreis\",Ausgabepreis)";
_map1.Put((Object)("Ausgabepreis"),(Object)(_ausgabepreis));
 //BA.debugLineNum = 316;BA.debugLine="Map1.Put(\"MinLohn\",MinLohn)";
_map1.Put((Object)("MinLohn"),(Object)(_minlohn));
 //BA.debugLineNum = 317;BA.debugLine="Map1.Put(\"MaxLohn\",MaxLohn)";
_map1.Put((Object)("MaxLohn"),(Object)(_maxlohn));
 //BA.debugLineNum = 318;BA.debugLine="Map1.Put(\"Kontrolltag\",Kontrolltag)";
_map1.Put((Object)("Kontrolltag"),(Object)(_kontrolltag));
 //BA.debugLineNum = 319;BA.debugLine="Map1.Put(\"Starttag\",Starttag)";
_map1.Put((Object)("Starttag"),(Object)(mostCurrent._starttag));
 //BA.debugLineNum = 320;BA.debugLine="Map1.Put(\"Startzeit\",Startzeit)";
_map1.Put((Object)("Startzeit"),(Object)(mostCurrent._startzeit));
 //BA.debugLineNum = 321;BA.debugLine="Map1.Put(\"Zieltag\",Zieltag)";
_map1.Put((Object)("Zieltag"),(Object)(mostCurrent._zieltag));
 //BA.debugLineNum = 322;BA.debugLine="Map1.Put(\"Zielzeit\",Zielzeit)";
_map1.Put((Object)("Zielzeit"),(Object)(mostCurrent._zielzeit));
 //BA.debugLineNum = 323;BA.debugLine="Map1.Put(\"Zeitzone\",Zeitzone)";
_map1.Put((Object)("Zeitzone"),(Object)(_zeitzone));
 //BA.debugLineNum = 324;BA.debugLine="Map1.Put(\"Straflohn\",StrafLohn)";
_map1.Put((Object)("Straflohn"),(Object)(_straflohn));
 //BA.debugLineNum = 330;BA.debugLine="result = Msgbox2(\"Name = \" & Name& CRLF & 	\"Gewicht = \" & Gewicht& CRLF & \"Anfangsgewicht = \" & Anfangsgewicht& CRLF & \"Zielgewicht = \" & Zielgewicht& CRLF & \"Waehrung = \" & Waehrung& CRLF & \"Ausgabepreis = \" & Ausgabepreis& CRLF &\"MinLohn = \" & MinLohn& CRLF & \"MaxLohn = \" & MaxLohn& CRLF & \"Kontrolltag = \" & Kontrolltag& CRLF & \"Starttag = \" & Starttag& CRLF & \"Startzeit = \" & Startzeit& CRLF & \"Zieltag = \" & Zieltag& CRLF & \"Zielzeit = \" & Zielzeit& CRLF & \"Zeitzone = \" & Zeitzone & CRLF & \"Straflohn = \" & StrafLohn, \"Speichern und Beenden\" , \"Ja\",\"Nein\",\"\",LoadBitmap(File.DirAssets,\"mamaLogo.png\"))";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2("Name = "+mostCurrent._name+anywheresoftware.b4a.keywords.Common.CRLF+"Gewicht = "+mostCurrent._gewicht+anywheresoftware.b4a.keywords.Common.CRLF+"Anfangsgewicht = "+BA.NumberToString(_anfangsgewicht)+anywheresoftware.b4a.keywords.Common.CRLF+"Zielgewicht = "+BA.NumberToString(_zielgewicht)+anywheresoftware.b4a.keywords.Common.CRLF+"Waehrung = "+mostCurrent._waehrung+anywheresoftware.b4a.keywords.Common.CRLF+"Ausgabepreis = "+BA.NumberToString(_ausgabepreis)+anywheresoftware.b4a.keywords.Common.CRLF+"MinLohn = "+BA.NumberToString(_minlohn)+anywheresoftware.b4a.keywords.Common.CRLF+"MaxLohn = "+BA.NumberToString(_maxlohn)+anywheresoftware.b4a.keywords.Common.CRLF+"Kontrolltag = "+BA.NumberToString(_kontrolltag)+anywheresoftware.b4a.keywords.Common.CRLF+"Starttag = "+mostCurrent._starttag+anywheresoftware.b4a.keywords.Common.CRLF+"Startzeit = "+mostCurrent._startzeit+anywheresoftware.b4a.keywords.Common.CRLF+"Zieltag = "+mostCurrent._zieltag+anywheresoftware.b4a.keywords.Common.CRLF+"Zielzeit = "+mostCurrent._zielzeit+anywheresoftware.b4a.keywords.Common.CRLF+"Zeitzone = "+BA.NumberToString(_zeitzone)+anywheresoftware.b4a.keywords.Common.CRLF+"Straflohn = "+BA.NumberToString(_straflohn),"Speichern und Beenden","Ja","Nein","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 331;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 332;BA.debugLine="File.WriteMap(File.DirRootExternal & AktuellerUnterordner, \"SetupApp.txt\", Map1)";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._aktuellerunterordner,"SetupApp.txt",_map1);
 //BA.debugLineNum = 333;BA.debugLine="ToastMessageShow(\"Erfolgreich gespeichert\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Erfolgreich gespeichert",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 334;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 };
 break;
default:
 //BA.debugLineNum = 340;BA.debugLine="ToastMessageShow(\"Fehler\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Fehler",anywheresoftware.b4a.keywords.Common.False);
 break;
}
;
 //BA.debugLineNum = 344;BA.debugLine="Auswahl";
_auswahl();
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
}
