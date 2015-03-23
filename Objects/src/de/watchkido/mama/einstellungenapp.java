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

public class einstellungenapp extends Activity implements B4AActivity{
	public static einstellungenapp mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.einstellungenapp");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (einstellungenapp).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.einstellungenapp");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.einstellungenapp", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (einstellungenapp) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (einstellungenapp) Resume **");
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
		return einstellungenapp.class;
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
        BA.LogInfo("** Activity (einstellungenapp) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (einstellungenapp) Resume **");
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
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _screenpersonal = null;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _screentraining = null;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _screennetzwerke = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public static int _anfangsgewicht = 0;
public static int _zielgewicht = 0;
public static int _kontrolltag = 0;
public static int _zeitzone = 0;
public static int _schriftfarbe = 0;
public static int _hintergrundfarbe = 0;
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
public static String _speicherort = "";
public anywheresoftware.b4a.objects.collections.List _liste1 = null;
public anywheresoftware.b4a.objects.collections.List _liste2 = null;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
public de.watchkido.mama.startabfragen _startabfragen = null;
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
 //BA.debugLineNum = 28;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 30;BA.debugLine="CreatePreferenceScreen";
_createpreferencescreen();
 //BA.debugLineNum = 31;BA.debugLine="If manager.GetAll.Size = 0 Then SetDefaults";
if (_manager.GetAll().getSize()==0) { 
_setdefaults();};
 };
 //BA.debugLineNum = 34;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 35;BA.debugLine="Dim label1 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="If File.ExternalWritable = False Then";
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 40;BA.debugLine="Msgbox(\"Ich kann nicht auf die SD-Card schreiben.\", \"A c h t u n g\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Ich kann nicht auf die SD-Card schreiben.","A c h t u n g",mostCurrent.activityBA);
 //BA.debugLineNum = 42;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 45;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 46;BA.debugLine="If File.Exists(File.DirRootExternal & AktuellerUnterordner, \"SetupApp.txt\") = True Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._aktuellerunterordner,"SetupApp.txt")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 48;BA.debugLine="Map1 = File.ReadMap(File.DirRootExternal & AktuellerUnterordner, \"SetupApp.txt\")";
_map1 = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._aktuellerunterordner,"SetupApp.txt");
 };
 //BA.debugLineNum = 53;BA.debugLine="ListView1.Initialize(\"ListView1\")";
mostCurrent._listview1.Initialize(mostCurrent.activityBA,"ListView1");
 //BA.debugLineNum = 54;BA.debugLine="label1 = ListView1.SingleLineLayout.Label 'set the label to the model label.";
_label1 = mostCurrent._listview1.getSingleLineLayout().Label;
 //BA.debugLineNum = 55;BA.debugLine="label1.TextSize = 30";
_label1.setTextSize((float) (30));
 //BA.debugLineNum = 56;BA.debugLine="label1.TextColor = Colors.Cyan";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 57;BA.debugLine="label1.Gravity = Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 58;BA.debugLine="label1.Typeface = Typeface.DEFAULT_BOLD";
_label1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 61;BA.debugLine="Zeitzone = Map1.GetDefault(\"Zeitzone\",2)'2";
_zeitzone = (int)(BA.ObjectToNumber(_map1.GetDefault((Object)("Zeitzone"),(Object)(2))));
 //BA.debugLineNum = 62;BA.debugLine="Anfangsgewicht = Map1.GetDefault(\"Anfangsgewicht\",99)'* startgewicht99";
_anfangsgewicht = (int)(BA.ObjectToNumber(_map1.GetDefault((Object)("Anfangsgewicht"),(Object)(99))));
 //BA.debugLineNum = 63;BA.debugLine="Zielgewicht =  Map1.GetDefault(\"Zielgewicht\",86)'86";
_zielgewicht = (int)(BA.ObjectToNumber(_map1.GetDefault((Object)("Zielgewicht"),(Object)(86))));
 //BA.debugLineNum = 64;BA.debugLine="Name = Map1.GetDefault(\"Name\",\"Avita\") 'Name = \"Avi\"";
mostCurrent._name = BA.ObjectToString(_map1.GetDefault((Object)("Name"),(Object)("Avita")));
 //BA.debugLineNum = 65;BA.debugLine="Ausgabepreis = Map1.GetDefault(\"Ausgabepreis\",300)'300";
_ausgabepreis = (double)(BA.ObjectToNumber(_map1.GetDefault((Object)("Ausgabepreis"),(Object)(300))));
 //BA.debugLineNum = 66;BA.debugLine="MinLohn = Map1.GetDefault(\"MinLohn\",0.2)'0.20";
_minlohn = (double)(BA.ObjectToNumber(_map1.GetDefault((Object)("MinLohn"),(Object)(0.2))));
 //BA.debugLineNum = 67;BA.debugLine="MaxLohn = Map1.GetDefault(\"MaxLohn\",5.0)'5";
_maxlohn = (double)(BA.ObjectToNumber(_map1.GetDefault((Object)("MaxLohn"),(Object)(5.0))));
 //BA.debugLineNum = 68;BA.debugLine="Gewicht = Map1.GetDefault(\"Gewicht\",\" kg\") ' kg oder lbs";
mostCurrent._gewicht = BA.ObjectToString(_map1.GetDefault((Object)("Gewicht"),(Object)(" kg")));
 //BA.debugLineNum = 69;BA.debugLine="Waehrung = Map1.GetDefault(\"Waehrung\",\"€\")";
mostCurrent._waehrung = BA.ObjectToString(_map1.GetDefault((Object)("Waehrung"),(Object)("€")));
 //BA.debugLineNum = 72;BA.debugLine="Kontrolltag = Map1.GetDefault(\"Kontrolltag\",1)'2";
_kontrolltag = (int)(BA.ObjectToNumber(_map1.GetDefault((Object)("Kontrolltag"),(Object)(1))));
 //BA.debugLineNum = 73;BA.debugLine="Starttag = Map1.GetDefault(\"Starttag\",\"04/07/2015\")'\"04/07/2012\" 'Starttag";
mostCurrent._starttag = BA.ObjectToString(_map1.GetDefault((Object)("Starttag"),(Object)("04/07/2015")));
 //BA.debugLineNum = 74;BA.debugLine="Startzeit = Map1.GetDefault(\"Startzeit\",\"00:00:01\")'\"00:00:01\"";
mostCurrent._startzeit = BA.ObjectToString(_map1.GetDefault((Object)("Startzeit"),(Object)("00:00:01")));
 //BA.debugLineNum = 75;BA.debugLine="Zieltag = Map1.GetDefault(\"Zieltag\",\"07/31/2015\")'\"07/31/2012\" 'gibts den tag?";
mostCurrent._zieltag = BA.ObjectToString(_map1.GetDefault((Object)("Zieltag"),(Object)("07/31/2015")));
 //BA.debugLineNum = 76;BA.debugLine="Zielzeit = Map1.GetDefault(\"Zielzeit\",\"23:59:59\")'\"23:59:59\"";
mostCurrent._zielzeit = BA.ObjectToString(_map1.GetDefault((Object)("Zielzeit"),(Object)("23:59:59")));
 //BA.debugLineNum = 77;BA.debugLine="StrafLohn = Map1.GetDefault(\"StrafLohn\",1)'1";
_straflohn = (double)(BA.ObjectToNumber(_map1.GetDefault((Object)("StrafLohn"),(Object)(1))));
 //BA.debugLineNum = 79;BA.debugLine="Auswahl'sprung zum listview";
_auswahl();
 //BA.debugLineNum = 81;BA.debugLine="Activity.AddView(ListView1, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._listview1.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 636;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 638;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 613;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 614;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 615;BA.debugLine="End Sub";
return "";
}
public static String  _auswahl() throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
 //BA.debugLineNum = 96;BA.debugLine="Sub Auswahl";
 //BA.debugLineNum = 98;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 99;BA.debugLine="bmp = LoadBitmap(File.dirassets,\"mamalogo.png\")";
_bmp = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamalogo.png");
 //BA.debugLineNum = 102;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 104;BA.debugLine="ListView1.AddSingleLine(\"Personalien\")";
mostCurrent._listview1.AddSingleLine("Personalien");
 //BA.debugLineNum = 105;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"Personalien\",\"Name, Wohnort, Verein, ...\",bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Personalien","Name, Wohnort, Verein, ...",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 106;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"Training\",\"Intensität, Sportart, Ziele, ...\",bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Training","Intensität, Sportart, Ziele, ...",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 111;BA.debugLine="ListView1.AddSingleLine(\"Applikation\")";
mostCurrent._listview1.AddSingleLine("Applikation");
 //BA.debugLineNum = 113;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"Anfangsgewicht\",Anfangsgewicht & Gewicht,bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Anfangsgewicht",BA.NumberToString(_anfangsgewicht)+mostCurrent._gewicht,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 114;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"Zielgewicht\",Zielgewicht & Gewicht,bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Zielgewicht",BA.NumberToString(_zielgewicht)+mostCurrent._gewicht,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 146;BA.debugLine="ListView1.AddSingleLine(\"Extras\")";
mostCurrent._listview1.AddSingleLine("Extras");
 //BA.debugLineNum = 147;BA.debugLine="ListView1.AddTwoLinesAndBitmap (\"Eula\",\"\",bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Eula","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 148;BA.debugLine="ListView1.AddTwoLinesAndBitmap (\"Farben\",\"\",bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Farben","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 149;BA.debugLine="ListView1.AddTwoLinesAndBitmap (\"eMail-Feedback\",\"\",bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("eMail-Feedback","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 150;BA.debugLine="ListView1.AddTwoLinesAndBitmap (\"Lizensschlüssel\",\"\" ,bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Lizensschlüssel","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 151;BA.debugLine="ListView1.AddTwoLinesAndBitmap (\"Passwort\",\"\" ,bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Passwort","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 152;BA.debugLine="ListView1.AddTwoLinesAndBitmap (\"Spenden\",\"\",bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Spenden","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 157;BA.debugLine="ListView1.AddSingleLine(\"Farben\")";
mostCurrent._listview1.AddSingleLine("Farben");
 //BA.debugLineNum = 159;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"Hintergrundfarbe\",Hintergrundfarbe,bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Hintergrundfarbe",BA.NumberToString(_hintergrundfarbe),(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 160;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"Schriftfarbe\",Schriftfarbe,bmp)";
mostCurrent._listview1.AddTwoLinesAndBitmap("Schriftfarbe",BA.NumberToString(_schriftfarbe),(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 162;BA.debugLine="ListView1.AddSingleLine(\"Speichern\")";
mostCurrent._listview1.AddSingleLine("Speichern");
 //BA.debugLineNum = 164;BA.debugLine="ListView1.AddTwoLinesAndBitmap(\"Jetzt Abspeichern\",\"Klicken Sie hier zum speichern\", LoadBitmap(File.DirAssets,\"mamaLogo.png\"))";
mostCurrent._listview1.AddTwoLinesAndBitmap("Jetzt Abspeichern","Klicken Sie hier zum speichern",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png").getObject()));
 //BA.debugLineNum = 172;BA.debugLine="End Sub";
return "";
}
public static String  _createpreferencescreen() throws Exception{
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _intentcat = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _cat1 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _cat2 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _katnetzwerke = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _katpersonal = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _kattraining = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _intentscreen = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _intentscreen1 = null;
anywheresoftware.b4a.objects.IntentWrapper _in = null;
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
anywheresoftware.b4a.objects.collections.List _pl = null;
anywheresoftware.b4a.phone.Phone.PhoneIntents _pi = null;
anywheresoftware.b4a.objects.collections.Map _m = null;
anywheresoftware.b4a.objects.collections.Map _mapintensität = null;
 //BA.debugLineNum = 476;BA.debugLine="Sub CreatePreferenceScreen";
 //BA.debugLineNum = 477;BA.debugLine="screen.Initialize(\"Einstellungen\", \"xxx\")";
_screen.Initialize("Einstellungen","xxx");
 //BA.debugLineNum = 478;BA.debugLine="screenPersonal.Initialize(\"Personalien\",\"\")";
_screenpersonal.Initialize("Personalien","");
 //BA.debugLineNum = 479;BA.debugLine="screenTraining.Initialize(\"screen1\",\"\")";
_screentraining.Initialize("screen1","");
 //BA.debugLineNum = 483;BA.debugLine="Dim intentCat, cat1, cat2,KatNetzwerke, KatPersonal, KatTraining As AHPreferenceCategory";
_intentcat = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat1 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat2 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_katnetzwerke = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_katpersonal = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_kattraining = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 484;BA.debugLine="Dim intentscreen, intentscreen1 As AHPreferenceScreen";
_intentscreen = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_intentscreen1 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 486;BA.debugLine="intentCat.Initialize(\"Geräte einstellungen\")";
_intentcat.Initialize("Geräte einstellungen");
 //BA.debugLineNum = 487;BA.debugLine="intentCat.AddCheckBox(\"intentenable\", \"Enable Intent Settings\", \"Intent settings are enabled\", \"Intent settings are disabled\", True, \"\")";
_intentcat.AddCheckBox("intentenable","Enable Intent Settings","Intent settings are enabled","Intent settings are disabled",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 488;BA.debugLine="intentscreen.Initialize(\"Intents\", \"Examples with Intents\")";
_intentscreen.Initialize("Intents","Examples with Intents");
 //BA.debugLineNum = 489;BA.debugLine="intentscreen.AddCheckBox(\"chkwifi\", \"Enable Wifi Settings\", \"Wifi settings enabled\", \"Wifi settings disabled\", False, \"\")";
_intentscreen.AddCheckBox("chkwifi","Enable Wifi Settings","Wifi settings enabled","Wifi settings disabled",anywheresoftware.b4a.keywords.Common.False,"");
 //BA.debugLineNum = 492;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 493;BA.debugLine="In.Initialize(\"android.settings.WIFI_SETTINGS\", \"\")";
_in.Initialize("android.settings.WIFI_SETTINGS","");
 //BA.debugLineNum = 494;BA.debugLine="intentscreen.AddIntent( \"Wifi Settings\", \"Example for custom Intent\", In, \"chkwifi\")";
_intentscreen.AddIntent("Wifi Settings","Example for custom Intent",(android.content.Intent)(_in.getObject()),"chkwifi");
 //BA.debugLineNum = 500;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 501;BA.debugLine="Dim pl As List";
_pl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 502;BA.debugLine="pl = pm.GetInstalledPackages";
_pl = _pm.GetInstalledPackages();
 //BA.debugLineNum = 504;BA.debugLine="If pl.IndexOf(\"com.android.calculator2\") > 0 Then";
if (_pl.IndexOf((Object)("com.android.calculator2"))>0) { 
 //BA.debugLineNum = 505;BA.debugLine="intentscreen.AddIntent(\"Calculator\", \"Open calculator\", pm.GetApplicationIntent(\"com.android.calculator2\"), \"\")";
_intentscreen.AddIntent("Calculator","Open calculator",(android.content.Intent)(_pm.GetApplicationIntent("com.android.calculator2").getObject()),"");
 };
 //BA.debugLineNum = 509;BA.debugLine="Dim pi As PhoneIntents";
_pi = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 510;BA.debugLine="intentscreen.AddIntent(\"Browser\", \"Open http://www.google.de\", pi.OpenBrowser(\"http://www.google.com\"), \"\")";
_intentscreen.AddIntent("Browser","Open http://www.google.de",_pi.OpenBrowser("http://www.google.com"),"");
 //BA.debugLineNum = 513;BA.debugLine="intentCat.AddPreferenceScreen(intentscreen, \"intentenable\")";
_intentcat.AddPreferenceScreen(_intentscreen,"intentenable");
 //BA.debugLineNum = 515;BA.debugLine="cat1.Initialize(\"Examples\")";
_cat1.Initialize("Examples");
 //BA.debugLineNum = 516;BA.debugLine="cat1.AddCheckBox(\"check1\", \"Checkbox1\", \"This is Checkbox1 without second summary\", \"\", True, \"\")";
_cat1.AddCheckBox("check1","Checkbox1","This is Checkbox1 without second summary","",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 517;BA.debugLine="cat1.AddEditText(\"edit1\", \"EditText1\", \"This is EditText1\", \"\", \"check1\")";
_cat1.AddEditText("edit1","EditText1","This is EditText1","","check1");
 //BA.debugLineNum = 518;BA.debugLine="cat1.AddPassword(\"pwd1\", \"Password1\", \"This is a password\", \"\", \"\")";
_cat1.AddPassword("pwd1","Password1","This is a password","","");
 //BA.debugLineNum = 519;BA.debugLine="cat1.AddRingtone(\"ring1\", \"Ringtone1\", \"This is a Ringtone\", \"\", \"\", cat1.RT_NOTIFICATION)";
_cat1.AddRingtone("ring1","Ringtone1","This is a Ringtone","","",_cat1.RT_NOTIFICATION);
 //BA.debugLineNum = 521;BA.debugLine="cat2.Initialize(\"Set Background Color\")";
_cat2.Initialize("Set Background Color");
 //BA.debugLineNum = 522;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 523;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 524;BA.debugLine="m.Put(\"black\", \"I want a black background\")";
_m.Put((Object)("black"),(Object)("I want a black background"));
 //BA.debugLineNum = 525;BA.debugLine="m.Put(\"red\", \"No, make it red\")";
_m.Put((Object)("red"),(Object)("No, make it red"));
 //BA.debugLineNum = 526;BA.debugLine="m.Put(\"green\", \"I like it green\")";
_m.Put((Object)("green"),(Object)("I like it green"));
 //BA.debugLineNum = 527;BA.debugLine="m.Put(\"blue\", \"Blue is best\")";
_m.Put((Object)("blue"),(Object)("Blue is best"));
 //BA.debugLineNum = 528;BA.debugLine="cat2.AddList2(\"Background Color\", \"Choose color\", \"Choose color\", \"black\", \"\", m)";
_cat2.AddList2("Background Color","Choose color","Choose color","black","",_m);
 //BA.debugLineNum = 534;BA.debugLine="cat1.Initialize(\"Examples\")";
_cat1.Initialize("Examples");
 //BA.debugLineNum = 535;BA.debugLine="cat1.AddCheckBox(\"check1\", \"Checkbox1\", \"This is Checkbox1 without second summary\", \"\", True, \"\")";
_cat1.AddCheckBox("check1","Checkbox1","This is Checkbox1 without second summary","",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 536;BA.debugLine="cat1.AddEditText(\"edit1\", \"EditText1\", \"This is EditText1\", \"\", \"check1\")";
_cat1.AddEditText("edit1","EditText1","This is EditText1","","check1");
 //BA.debugLineNum = 537;BA.debugLine="cat1.AddPassword(\"pwd1\", \"Password1\", \"This is a password\", \"\", \"\")";
_cat1.AddPassword("pwd1","Password1","This is a password","","");
 //BA.debugLineNum = 538;BA.debugLine="cat1.AddRingtone(\"ring1\", \"Ringtone1\", \"This is a Ringtone\", \"\", \"\", cat1.RT_NOTIFICATION)";
_cat1.AddRingtone("ring1","Ringtone1","This is a Ringtone","","",_cat1.RT_NOTIFICATION);
 //BA.debugLineNum = 546;BA.debugLine="KatPersonal.Initialize(\"Personalien\")";
_katpersonal.Initialize("Personalien");
 //BA.debugLineNum = 547;BA.debugLine="KatPersonal.AddEditText(\"Vorname\", \"Vorname\", \"Hier den Vornamen eintragen\", \"\", \"\")";
_katpersonal.AddEditText("Vorname","Vorname","Hier den Vornamen eintragen","","");
 //BA.debugLineNum = 548;BA.debugLine="KatPersonal.AddEditText(\"Nachname\", \"Nachname\", \"Hier den Nachnamen eintragen\", \"\", \"\")";
_katpersonal.AddEditText("Nachname","Nachname","Hier den Nachnamen eintragen","","");
 //BA.debugLineNum = 549;BA.debugLine="KatPersonal.AddEditText(\"Telefonnummer\", \"Telefonnummer\", \"HIer die Telefonnummer eintragen\", \"\", \"\")";
_katpersonal.AddEditText("Telefonnummer","Telefonnummer","HIer die Telefonnummer eintragen","","");
 //BA.debugLineNum = 550;BA.debugLine="KatPersonal.AddPassword(\"Passwort\", \"Passwort\", \"Hier das Passwort eintragen\", \"\", \"\")";
_katpersonal.AddPassword("Passwort","Passwort","Hier das Passwort eintragen","","");
 //BA.debugLineNum = 551;BA.debugLine="KatTraining.Initialize(\"Training\")";
_kattraining.Initialize("Training");
 //BA.debugLineNum = 552;BA.debugLine="Dim mapIntensität As Map";
_mapintensität = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 553;BA.debugLine="mapIntensität.Initialize";
_mapintensität.Initialize();
 //BA.debugLineNum = 554;BA.debugLine="mapIntensität.Put(0, \"Abtrainieren - Modus\")";
_mapintensität.Put((Object)(0),(Object)("Abtrainieren - Modus"));
 //BA.debugLineNum = 555;BA.debugLine="mapIntensität.Put(1, \"Mobilisieren - Modus\")";
_mapintensität.Put((Object)(1),(Object)("Mobilisieren - Modus"));
 //BA.debugLineNum = 556;BA.debugLine="mapIntensität.Put(2, \"Ohne Geräte - Modus\")";
_mapintensität.Put((Object)(2),(Object)("Ohne Geräte - Modus"));
 //BA.debugLineNum = 557;BA.debugLine="mapIntensität.Put(3, \"Fitness - Modus\")";
_mapintensität.Put((Object)(3),(Object)("Fitness - Modus"));
 //BA.debugLineNum = 558;BA.debugLine="mapIntensität.Put(4, \"Knallhart - Modus\")";
_mapintensität.Put((Object)(4),(Object)("Knallhart - Modus"));
 //BA.debugLineNum = 559;BA.debugLine="mapIntensität.Put(5, \"Wettkampf - Modus\")";
_mapintensität.Put((Object)(5),(Object)("Wettkampf - Modus"));
 //BA.debugLineNum = 560;BA.debugLine="mapIntensität.Put(6, \"Bundeslandmeister - Modus\")";
_mapintensität.Put((Object)(6),(Object)("Bundeslandmeister - Modus"));
 //BA.debugLineNum = 561;BA.debugLine="mapIntensität.Put(7, \"Deutscher Meister - Modus\")";
_mapintensität.Put((Object)(7),(Object)("Deutscher Meister - Modus"));
 //BA.debugLineNum = 562;BA.debugLine="mapIntensität.Put(8, \"Bruce Lee - Modus\")";
_mapintensität.Put((Object)(8),(Object)("Bruce Lee - Modus"));
 //BA.debugLineNum = 563;BA.debugLine="mapIntensität.Put(9, \"Europameister - Modus\")";
_mapintensität.Put((Object)(9),(Object)("Europameister - Modus"));
 //BA.debugLineNum = 564;BA.debugLine="mapIntensität.Put(10, \"Muhammad Ali - WeltmeisterModus\")";
_mapintensität.Put((Object)(10),(Object)("Muhammad Ali - WeltmeisterModus"));
 //BA.debugLineNum = 565;BA.debugLine="mapIntensität.Put(11, \"Chuck Norris - GalaxyModus\")";
_mapintensität.Put((Object)(11),(Object)("Chuck Norris - GalaxyModus"));
 //BA.debugLineNum = 566;BA.debugLine="KatTraining.AddList2(\"Intensitaet\", \"Intensität\", \"Wie hart soll das Training sein?\", 3, \"\", mapIntensität)";
_kattraining.AddList2("Intensitaet","Intensität","Wie hart soll das Training sein?",BA.NumberToString(3),"",_mapintensität);
 //BA.debugLineNum = 570;BA.debugLine="KatNetzwerke.Initialize(\"Netzwerke\")";
_katnetzwerke.Initialize("Netzwerke");
 //BA.debugLineNum = 571;BA.debugLine="KatNetzwerke.AddCheckBox(\"Facebook\", \"Facebook\", \"Facebook wird genutzt\", \"Facebook wird nicht genutzt\", True, \"\")";
_katnetzwerke.AddCheckBox("Facebook","Facebook","Facebook wird genutzt","Facebook wird nicht genutzt",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 572;BA.debugLine="intentscreen1.Initialize(\"Facebook Einstellungen\", \"Zugang, Erlaubnis, ...\")";
_intentscreen1.Initialize("Facebook Einstellungen","Zugang, Erlaubnis, ...");
 //BA.debugLineNum = 598;BA.debugLine="screen.AddPreferenceCategory(intentCat)";
_screen.AddPreferenceCategory(_intentcat);
 //BA.debugLineNum = 599;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 600;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 604;BA.debugLine="screenPersonal.AddPreferenceCategory(KatPersonal)";
_screenpersonal.AddPreferenceCategory(_katpersonal);
 //BA.debugLineNum = 607;BA.debugLine="screenTraining.AddPreferenceCategory(KatTraining)";
_screentraining.AddPreferenceCategory(_kattraining);
 //BA.debugLineNum = 611;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 17;BA.debugLine="Dim Schriftfarbe, Hintergrundfarbe As Int";
_schriftfarbe = 0;
_hintergrundfarbe = 0;
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
 //BA.debugLineNum = 22;BA.debugLine="Dim Speicherort As String";
mostCurrent._speicherort = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim Liste1, Liste2 As List";
mostCurrent._liste1 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._liste2 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
 //BA.debugLineNum = 617;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 618;BA.debugLine="Log(manager.GetAll)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(_manager.GetAll()));
 //BA.debugLineNum = 619;BA.debugLine="Select manager.GetString(\"Background Color\")";
switch (BA.switchObjectToInt(_manager.GetString("Background Color"),"black","red","green","blue")) {
case 0:
 //BA.debugLineNum = 621;BA.debugLine="Activity.Color = Colors.Black";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 break;
case 1:
 //BA.debugLineNum = 623;BA.debugLine="Activity.Color = Colors.Red";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 break;
case 2:
 //BA.debugLineNum = 625;BA.debugLine="Activity.Color = Colors.Green";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 break;
case 3:
 //BA.debugLineNum = 627;BA.debugLine="Activity.Color = Colors.Blue";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 break;
}
;
 //BA.debugLineNum = 630;BA.debugLine="If manager.GetString(\"ring1\") <> \"\" Then";
if ((_manager.GetString("ring1")).equals("") == false) { 
 };
 //BA.debugLineNum = 634;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
String _txt = "";
anywheresoftware.b4a.agraham.dialogs.InputDialog.ColorDialogHSV _cd = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.ColorDialogHSV _cdsf = null;
int _color = 0;
int _ret = 0;
anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog _fd = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog _dd = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.TimeDialog _td = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.NumberDialog _nd = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog _id = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
int _result = 0;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 177;BA.debugLine="Sub ListView1_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 180;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 181;BA.debugLine="Dim cd, cdsf As ColorDialogHSV";
_cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.ColorDialogHSV();
_cdsf = new anywheresoftware.b4a.agraham.dialogs.InputDialog.ColorDialogHSV();
 //BA.debugLineNum = 182;BA.debugLine="Dim color As Int";
_color = 0;
 //BA.debugLineNum = 183;BA.debugLine="Dim ret As Int";
_ret = 0;
 //BA.debugLineNum = 184;BA.debugLine="Dim fd As FileDialog";
_fd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog();
 //BA.debugLineNum = 185;BA.debugLine="Dim Dd As DateDialog";
_dd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog();
 //BA.debugLineNum = 186;BA.debugLine="Dim td As TimeDialog";
_td = new anywheresoftware.b4a.agraham.dialogs.InputDialog.TimeDialog();
 //BA.debugLineNum = 187;BA.debugLine="Dim nd As NumberDialog";
_nd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.NumberDialog();
 //BA.debugLineNum = 188;BA.debugLine="Dim Id As InputDialog";
_id = new anywheresoftware.b4a.agraham.dialogs.InputDialog();
 //BA.debugLineNum = 189;BA.debugLine="Dim Bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 190;BA.debugLine="Bmp.Initialize(File.DirAssets, \"mamaLogo.png\")";
_bmp.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png");
 //BA.debugLineNum = 192;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)("Personalien"),(Object)("Training"),(Object)("Netzwerke"),(Object)("Zeiten"),(Object)("Gewichte"),(Object)("screen0"),(Object)("screen1"),(Object)("Gewicht"),(Object)("Währung"),(Object)("Zeitzone"),(Object)("Anfangsgewicht"),(Object)("Zielgewicht"),(Object)("Name"),(Object)("Preise"),(Object)("Ausgabepreis gesamt"),(Object)("StrafLohn"),(Object)("Minimaler Lohn"),(Object)("Maximaler Lohn"),(Object)("Kontrolltag Rythmus"),(Object)("Starttag (MM/TT/JJJJ)"),(Object)("Startzeit"),(Object)("Zieltag (MM/TT/JJJJ)"),(Object)("Zielzeit"),(Object)("Farben"),(Object)("Hintergrundfarbe"),(Object)("Schriftfarbe"),(Object)("Speichern"),(Object)("Speicherort"),(Object)("Jetzt Abspeichern"))) {
case 0:
 //BA.debugLineNum = 197;BA.debugLine="StartActivity(screenPersonal.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screenpersonal.CreateIntent()));
 break;
case 1:
 //BA.debugLineNum = 202;BA.debugLine="StartActivity(screenTraining.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screentraining.CreateIntent()));
 break;
case 2:
 //BA.debugLineNum = 205;BA.debugLine="StartActivity(screenNetzwerke.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screennetzwerke.CreateIntent()));
 break;
case 3:
 break;
case 4:
 break;
case 5:
 //BA.debugLineNum = 220;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screen.CreateIntent()));
 break;
case 6:
 //BA.debugLineNum = 225;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screen.CreateIntent()));
 break;
case 7:
 //BA.debugLineNum = 231;BA.debugLine="Liste1.Initialize";
mostCurrent._liste1.Initialize();
 //BA.debugLineNum = 232;BA.debugLine="Liste1.Add(\"kg\")";
mostCurrent._liste1.Add((Object)("kg"));
 //BA.debugLineNum = 233;BA.debugLine="Liste1.Add(\"lbs\")";
mostCurrent._liste1.Add((Object)("lbs"));
 //BA.debugLineNum = 234;BA.debugLine="ret = InputList(Liste1, \"Einheit\",0)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(mostCurrent._liste1,"Einheit",(int) (0),mostCurrent.activityBA);
 //BA.debugLineNum = 236;BA.debugLine="Select ret";
switch (_ret) {
case 0:
 //BA.debugLineNum = 238;BA.debugLine="Gewicht = \" kg\"";
mostCurrent._gewicht = " kg";
 break;
case 1:
 //BA.debugLineNum = 240;BA.debugLine="Gewicht = \" lbs\"";
mostCurrent._gewicht = " lbs";
 break;
default:
 //BA.debugLineNum = 242;BA.debugLine="Msgbox(\"?\",\"?\")";
anywheresoftware.b4a.keywords.Common.Msgbox("?","?",mostCurrent.activityBA);
 break;
}
;
 break;
case 8:
 //BA.debugLineNum = 247;BA.debugLine="Liste2.Initialize";
mostCurrent._liste2.Initialize();
 //BA.debugLineNum = 248;BA.debugLine="Liste2.Add(\"Euro\")";
mostCurrent._liste2.Add((Object)("Euro"));
 //BA.debugLineNum = 249;BA.debugLine="Liste2.Add(\"Dollar\")";
mostCurrent._liste2.Add((Object)("Dollar"));
 //BA.debugLineNum = 250;BA.debugLine="Liste2.Add(\"Pounds\")";
mostCurrent._liste2.Add((Object)("Pounds"));
 //BA.debugLineNum = 251;BA.debugLine="ret = InputList(Liste2, \"Geld Einheit\",0)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(mostCurrent._liste2,"Geld Einheit",(int) (0),mostCurrent.activityBA);
 //BA.debugLineNum = 253;BA.debugLine="Select ret";
switch (_ret) {
case 0:
 //BA.debugLineNum = 255;BA.debugLine="Waehrung = \" €\"";
mostCurrent._waehrung = " €";
 break;
case 1:
 //BA.debugLineNum = 257;BA.debugLine="Waehrung = \" $\"";
mostCurrent._waehrung = " $";
 break;
case 2:
 //BA.debugLineNum = 259;BA.debugLine="Waehrung = \" £\"";
mostCurrent._waehrung = " £";
 break;
default:
 //BA.debugLineNum = 261;BA.debugLine="Msgbox(\"?\",\"?\")";
anywheresoftware.b4a.keywords.Common.Msgbox("?","?",mostCurrent.activityBA);
 break;
}
;
 break;
case 9:
 //BA.debugLineNum = 266;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 267;BA.debugLine="nd.Number = Zeitzone";
_nd.setNumber(_zeitzone);
 //BA.debugLineNum = 268;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 269;BA.debugLine="nd.ShowSign = True";
_nd.setShowSign(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 270;BA.debugLine="ret = nd.Show(\"Zeitzone\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Zeitzone","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 272;BA.debugLine="Zeitzone = nd.Number";
_zeitzone = _nd.getNumber();
 break;
case 10:
 //BA.debugLineNum = 276;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 277;BA.debugLine="nd.Number = Anfangsgewicht";
_nd.setNumber(_anfangsgewicht);
 //BA.debugLineNum = 278;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 280;BA.debugLine="ret = nd.Show(\"Anfangsgewicht\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Anfangsgewicht","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 282;BA.debugLine="Anfangsgewicht = nd.number / Power(10, nd.Decimal)";
_anfangsgewicht = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 11:
 //BA.debugLineNum = 285;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 286;BA.debugLine="nd.Number = Zielgewicht";
_nd.setNumber(_zielgewicht);
 //BA.debugLineNum = 287;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 289;BA.debugLine="ret = nd.Show(\"Zielgewicht\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Zielgewicht","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 291;BA.debugLine="Zielgewicht = nd.number / Power(10, nd.Decimal)";
_zielgewicht = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 12:
 //BA.debugLineNum = 294;BA.debugLine="Id.PasswordMode = False";
_id.setPasswordMode(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 298;BA.debugLine="Id.InputType = Id.INPUT_TYPE_TEXT";
_id.setInputType(_id.INPUT_TYPE_TEXT);
 //BA.debugLineNum = 299;BA.debugLine="Id.input = Name";
_id.setInput(mostCurrent._name);
 //BA.debugLineNum = 300;BA.debugLine="Id.Hint = \"Bitte Name eingeben!\"";
_id.setHint("Bitte Name eingeben!");
 //BA.debugLineNum = 301;BA.debugLine="Id.HintColor = Colors.ARGB(196, 255, 140, 0)";
_id.setHintColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (196),(int) (255),(int) (140),(int) (0)));
 //BA.debugLineNum = 302;BA.debugLine="ret = DialogResponse.CANCEL";
_ret = anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL;
 //BA.debugLineNum = 303;BA.debugLine="ret = Id.Show(\"Geben sie einen kurzen Text ein\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _id.Show("Geben sie einen kurzen Text ein","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 306;BA.debugLine="Name = Id.input";
mostCurrent._name = _id.getInput();
 break;
case 13:
 break;
case 14:
 //BA.debugLineNum = 312;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 313;BA.debugLine="nd.Number = Ausgabepreis";
_nd.setNumber((int) (_ausgabepreis));
 //BA.debugLineNum = 314;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 316;BA.debugLine="ret = nd.Show(\"Ausgabepreis gesamt\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Ausgabepreis gesamt","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 318;BA.debugLine="Ausgabepreis = nd.Number / Power(10, nd.Decimal)";
_ausgabepreis = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 15:
 //BA.debugLineNum = 321;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 322;BA.debugLine="nd.Number = StrafLohn";
_nd.setNumber((int) (_straflohn));
 //BA.debugLineNum = 323;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 325;BA.debugLine="ret = nd.Show(\"Starflohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Starflohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 327;BA.debugLine="StrafLohn = nd.Number / Power(10, nd.Decimal)";
_straflohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 16:
 //BA.debugLineNum = 331;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 332;BA.debugLine="nd.Number = MinLohn";
_nd.setNumber((int) (_minlohn));
 //BA.debugLineNum = 333;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 335;BA.debugLine="ret = nd.Show(\"Minimaler Tageslohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Minimaler Tageslohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 337;BA.debugLine="MinLohn = nd.number / Power(10, nd.Decimal)";
_minlohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 17:
 //BA.debugLineNum = 341;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 342;BA.debugLine="nd.Number = MaxLohn";
_nd.setNumber((int) (_maxlohn));
 //BA.debugLineNum = 343;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 345;BA.debugLine="ret = nd.Show(\"Maximaler Tageslohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Maximaler Tageslohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 347;BA.debugLine="MaxLohn = nd.Number / Power(10, nd.Decimal)";
_maxlohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 18:
 //BA.debugLineNum = 351;BA.debugLine="nd.Digits = 2";
_nd.setDigits((int) (2));
 //BA.debugLineNum = 352;BA.debugLine="nd.Number = Kontrolltag";
_nd.setNumber(_kontrolltag);
 //BA.debugLineNum = 353;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 355;BA.debugLine="ret = nd.Show(\"Kontrolltag Rythmus\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Kontrolltag Rythmus","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 357;BA.debugLine="Kontrolltag = nd.number / Power(10, nd.Decimal)";
_kontrolltag = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 19:
 //BA.debugLineNum = 360;BA.debugLine="Dd.Year = DateTime.GetYear(DateTime.Now)";
_dd.setYear(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 361;BA.debugLine="Dd.Month = DateTime.GetMonth(DateTime.Now)";
_dd.setMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 362;BA.debugLine="Dd.DayOfMonth = DateTime.GetDayOfMonth(DateTime.Now)";
_dd.setDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 363;BA.debugLine="ret = Dd.Show(\"Setzen Sie den Starttag\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _dd.Show("Setzen Sie den Starttag","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 365;BA.debugLine="Starttag = Dd.Month & \"/\" & Dd.DayOfMonth & \"/\" & Dd.Year";
mostCurrent._starttag = BA.NumberToString(_dd.getMonth())+"/"+BA.NumberToString(_dd.getDayOfMonth())+"/"+BA.NumberToString(_dd.getYear());
 break;
case 20:
 //BA.debugLineNum = 368;BA.debugLine="td.Hour = DateTime.GetHour(DateTime.Now)";
_td.setHour(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 369;BA.debugLine="td.Minute = DateTime.GetMinute(DateTime.Now)";
_td.setMinute(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 370;BA.debugLine="td.Is24Hours = True";
_td.setIs24Hours(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 371;BA.debugLine="ret = td.Show(\"Setzen Sie die Startzeit\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _td.Show("Setzen Sie die Startzeit","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 374;BA.debugLine="Startzeit = td.Hour & \":\" & td.Minute";
mostCurrent._startzeit = BA.NumberToString(_td.getHour())+":"+BA.NumberToString(_td.getMinute());
 break;
case 21:
 //BA.debugLineNum = 377;BA.debugLine="Dd.Year = DateTime.GetYear(DateTime.Now)";
_dd.setYear(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 378;BA.debugLine="Dd.Month = DateTime.GetMonth(DateTime.Now)";
_dd.setMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 379;BA.debugLine="Dd.DayOfMonth = DateTime.GetDayOfMonth(DateTime.Now)";
_dd.setDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 380;BA.debugLine="ret = Dd.Show(\"Setze das Zieldatum\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _dd.Show("Setze das Zieldatum","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 382;BA.debugLine="Zieltag = Dd.Month & \"/\" & Dd.DayOfMonth & \"/\" & Dd.Year";
mostCurrent._zieltag = BA.NumberToString(_dd.getMonth())+"/"+BA.NumberToString(_dd.getDayOfMonth())+"/"+BA.NumberToString(_dd.getYear());
 break;
case 22:
 //BA.debugLineNum = 386;BA.debugLine="td.Hour = DateTime.GetHour(DateTime.Now)";
_td.setHour(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 387;BA.debugLine="td.Minute = DateTime.GetMinute(DateTime.Now)";
_td.setMinute(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 388;BA.debugLine="td.Is24Hours = True";
_td.setIs24Hours(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 389;BA.debugLine="ret = td.Show(\"Setzen Sie die Zielzeit\", \"Fette Euro#s\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _td.Show("Setzen Sie die Zielzeit","Fette Euro#s","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 391;BA.debugLine="Zielzeit = td.Hour & \":\" & td.Minute";
mostCurrent._zielzeit = BA.NumberToString(_td.getHour())+":"+BA.NumberToString(_td.getMinute());
 break;
case 23:
 break;
case 24:
 //BA.debugLineNum = 399;BA.debugLine="cd.Hue = 180";
_cd.setHue((float) (180));
 //BA.debugLineNum = 400;BA.debugLine="cd.Saturation = 0.5";
_cd.setSaturation((float) (0.5));
 //BA.debugLineNum = 401;BA.debugLine="cd.Value = 0.5";
_cd.setValue((float) (0.5));
 //BA.debugLineNum = 402;BA.debugLine="ret = cd.Show(\"Hintergrundfarbe\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _cd.Show("Hintergrundfarbe","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 404;BA.debugLine="Activity.color = cd.RGB";
mostCurrent._activity.setColor(_cd.getRGB());
 //BA.debugLineNum = 405;BA.debugLine="Hintergrundfarbe = cd.rgb";
_hintergrundfarbe = _cd.getRGB();
 break;
case 25:
 //BA.debugLineNum = 410;BA.debugLine="cdsf.Hue = 180";
_cdsf.setHue((float) (180));
 //BA.debugLineNum = 411;BA.debugLine="cdsf.Saturation = 0.5";
_cdsf.setSaturation((float) (0.5));
 //BA.debugLineNum = 412;BA.debugLine="cdsf.Value = 0.5";
_cdsf.setValue((float) (0.5));
 //BA.debugLineNum = 413;BA.debugLine="ret = cdsf.Show(\"Schriftfarbe\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _cdsf.Show("Schriftfarbe","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 415;BA.debugLine="Schriftfarbe = cdsf.rgb";
_schriftfarbe = _cdsf.getRGB();
 break;
case 26:
 break;
case 27:
 //BA.debugLineNum = 423;BA.debugLine="fd.FastScroll = True";
_fd.setFastScroll(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 424;BA.debugLine="fd.FilePath = File.DirRootExternal & AktuellerUnterordner ' also sets ChosenName to an emtpy string";
_fd.setFilePath(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 426;BA.debugLine="fd.FileFilter = \"*.*\" ' for example or \".jpg,.png\" for multiple file types";
_fd.setFileFilter("*.*");
 //BA.debugLineNum = 427;BA.debugLine="ret = fd.Show(\"Datei Abspeichern\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _fd.Show("Datei Abspeichern","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 428;BA.debugLine="ToastMessageShow(ret & \" : Path : \" & fd.FilePath & CRLF & \"File : \" & fd.ChosenName, False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.NumberToString(_ret)+" : Path : "+_fd.getFilePath()+anywheresoftware.b4a.keywords.Common.CRLF+"File : "+_fd.getChosenName(),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 429;BA.debugLine="Speicherort = fd.FilePath & \"/\" & fd.ChosenName";
mostCurrent._speicherort = _fd.getFilePath()+"/"+_fd.getChosenName();
 break;
case 28:
 //BA.debugLineNum = 432;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 434;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 435;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 436;BA.debugLine="Map1.Put(\"Name\",Name)";
_map1.Put((Object)("Name"),(Object)(mostCurrent._name));
 //BA.debugLineNum = 437;BA.debugLine="Map1.Put(\"Gewicht\",Gewicht)";
_map1.Put((Object)("Gewicht"),(Object)(mostCurrent._gewicht));
 //BA.debugLineNum = 438;BA.debugLine="Map1.Put(\"Anfangsgewicht\",Anfangsgewicht)";
_map1.Put((Object)("Anfangsgewicht"),(Object)(_anfangsgewicht));
 //BA.debugLineNum = 439;BA.debugLine="Map1.Put(\"Zielgewicht\",Zielgewicht)";
_map1.Put((Object)("Zielgewicht"),(Object)(_zielgewicht));
 //BA.debugLineNum = 445;BA.debugLine="Map1.Put(\"Starttag\",Starttag)";
_map1.Put((Object)("Starttag"),(Object)(mostCurrent._starttag));
 //BA.debugLineNum = 446;BA.debugLine="Map1.Put(\"Startzeit\",Startzeit)";
_map1.Put((Object)("Startzeit"),(Object)(mostCurrent._startzeit));
 //BA.debugLineNum = 447;BA.debugLine="Map1.Put(\"Zieltag\",Zieltag)";
_map1.Put((Object)("Zieltag"),(Object)(mostCurrent._zieltag));
 //BA.debugLineNum = 448;BA.debugLine="Map1.Put(\"Zielzeit\",Zielzeit)";
_map1.Put((Object)("Zielzeit"),(Object)(mostCurrent._zielzeit));
 //BA.debugLineNum = 449;BA.debugLine="Map1.Put(\"Zeitzone\",Zeitzone)";
_map1.Put((Object)("Zeitzone"),(Object)(_zeitzone));
 //BA.debugLineNum = 451;BA.debugLine="Map1.Put(\"Hintergrundfarbe\",Hintergrundfarbe)";
_map1.Put((Object)("Hintergrundfarbe"),(Object)(_hintergrundfarbe));
 //BA.debugLineNum = 452;BA.debugLine="Map1.Put(\"Schriftfarbe\",Schriftfarbe)";
_map1.Put((Object)("Schriftfarbe"),(Object)(_schriftfarbe));
 //BA.debugLineNum = 456;BA.debugLine="result = Msgbox2(\"Starttag = \" & Starttag& CRLF & \"Startzeit = \" & Startzeit& CRLF & \"Zieltag = \" & Zieltag& CRLF & \"Zielzeit = \" & Zielzeit& CRLF & \"Zeitzone = \" & Zeitzone , \"Speichern und Beenden\" , \"Ja\",\"Nein\",\"\",LoadBitmap(File.DirAssets,\"mamaLogo.png\"))";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2("Starttag = "+mostCurrent._starttag+anywheresoftware.b4a.keywords.Common.CRLF+"Startzeit = "+mostCurrent._startzeit+anywheresoftware.b4a.keywords.Common.CRLF+"Zieltag = "+mostCurrent._zieltag+anywheresoftware.b4a.keywords.Common.CRLF+"Zielzeit = "+mostCurrent._zielzeit+anywheresoftware.b4a.keywords.Common.CRLF+"Zeitzone = "+BA.NumberToString(_zeitzone),"Speichern und Beenden","Ja","Nein","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 457;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 458;BA.debugLine="File.WriteMap(File.DirRootExternal & AktuellerUnterordner, \"SetupApp.txt\", Map1)";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._aktuellerunterordner,"SetupApp.txt",_map1);
 //BA.debugLineNum = 459;BA.debugLine="ToastMessageShow(\"Erfolgreich gespeichert\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Erfolgreich gespeichert",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 460;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 };
 break;
default:
 //BA.debugLineNum = 466;BA.debugLine="ToastMessageShow(\"Noch in Bearbeitung\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Noch in Bearbeitung",anywheresoftware.b4a.keywords.Common.False);
 break;
}
;
 //BA.debugLineNum = 470;BA.debugLine="Auswahl";
_auswahl();
 //BA.debugLineNum = 473;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim manager As AHPreferenceManager";
_manager = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceManager();
 //BA.debugLineNum = 9;BA.debugLine="Dim screen, screenPersonal, screenTraining, screenNetzwerke As AHPreferenceScreen";
_screen = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_screenpersonal = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_screentraining = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_screennetzwerke = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 87;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 89;BA.debugLine="manager.SetBoolean(\"check1\", True)";
_manager.SetBoolean("check1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 90;BA.debugLine="manager.SetBoolean(\"check2\", False)";
_manager.SetBoolean("check2",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 91;BA.debugLine="manager.SetString(\"edit1\", \"Hello!\")";
_manager.SetString("edit1","Hello!");
 //BA.debugLineNum = 92;BA.debugLine="manager.SetString(\"list1\", \"Black\")";
_manager.SetString("list1","Black");
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return "";
}
}
