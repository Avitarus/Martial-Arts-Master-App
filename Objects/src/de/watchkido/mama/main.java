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
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.main");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
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
public static int _type_startseite = 0;
public static int _type_einstellungen = 0;
public static int _type_ebene1 = 0;
public static int _type_ebene2 = 0;
public static int _type_ebene3 = 0;
public static int _type_ebene4 = 0;
public static int _fill_parent = 0;
public static int _wrap_content = 0;
public static int _currenttheme = 0;
public static int _currentpage = 0;
public static String _ueberschrift = "";
public static int _zeiterwaermung = 0;
public static int _zeitprouebung = 0;
public static int _zeitpause = 0;
public static int _zeit = 0;
public static boolean _businessversion = false;
public static String _namecheckliste = "";
public static String _aktuellerunterordner = "";
public static String _hauptordner0 = "";
public static String _unterordner1 = "";
public static String _unterordner2 = "";
public static String _unterordner3 = "";
public static String _unterordner4 = "";
public static String _unterordner5 = "";
public static String _unterordner6 = "";
public static String _unterordner7 = "";
public static String _unterordner8 = "";
public static String _unterordner9 = "";
public static String _unterordner10 = "";
public static String _unterordner11 = "";
public static String _unterordner12 = "";
public static String _unterordner13 = "";
public static String _unterordner14 = "";
public static String _unterordner15 = "";
public static String _unterordner16 = "";
public static String _unterordner17 = "";
public static String _unterordner18 = "";
public static String _unterordner19 = "";
public static String _unterordner20 = "";
public static String _versionsnummer = "";
public static String _mamawebseite = "";
public static String _activitytitel = "";
public static boolean _ickwarallhier = false;
public static String _meinenummer = "";
public static String _meineemail = "";
public static String _vorname = "";
public static String _nachname = "";
public static String _spitzname = "";
public static String _heimatort = "";
public static int _alter = 0;
public static String _altersklasse = "";
public static int _groeße = 0;
public static int _gewicht = 0;
public static String _gewichtsklasse = "";
public static String _schule = "";
public static String _abschluss = "";
public static String _sportart = "";
public static String _verein = "";
public static String _verband = "";
public static boolean _ueberwachung = false;
public static int _meldezeit = 0;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceManager _manager = null;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _screen = null;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _screenpersonal = null;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _screentraining = null;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _screennetzwerke = null;
public de.amberhome.viewpager.PageContainerAdapter _container = null;
public de.amberhome.viewpager.ViewPagerWrapper _pager = null;
public anywheresoftware.b4a.objects.PanelWrapper _line = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public de.amberhome.viewpager.ViewPagerTabsWrapper _tabs = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _sptheme = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnerwaermung = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spntraining = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnpause = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvebene1 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvebene2 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvebene3 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvebene4 = null;
public static String _ebene1 = "";
public static String _ebene2 = "";
public static String _ebene4 = "";
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public static int _anfangsgewicht = 0;
public static int _zielgewicht = 0;
public static int _kontrolltag = 0;
public static int _zeitzone = 0;
public static int _schriftfarbe = 0;
public static int _hintergrundfarbe = 0;
public static int _startbildschirmfarbe = 0;
public static double _maxlohn = 0;
public static double _minlohn = 0;
public static double _ausgabepreis = 0;
public static double _straflohn = 0;
public static String _name = "";
public static String _waehrung = "";
public static String _starttag = "";
public static String _zieltag = "";
public static String _startzeit = "";
public static String _zielzeit = "";
public static String _speicherort = "";
public anywheresoftware.b4a.objects.collections.List _liste1 = null;
public anywheresoftware.b4a.objects.collections.List _liste2 = null;
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
public de.watchkido.mama.tts _tts = null;
public de.watchkido.mama.lebensmittel _lebensmittel = null;
public de.watchkido.mama.statemanager _statemanager = null;
public de.watchkido.mama.tagebuch _tagebuch = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (spiele.mostCurrent != null);
vis = vis | (trainingkickboxen.mostCurrent != null);
vis = vis | (startabfragen.mostCurrent != null);
vis = vis | (einstellungenapp.mostCurrent != null);
vis = vis | (erfolgsmeldung.mostCurrent != null);
vis = vis | (checkliste.mostCurrent != null);
vis = vis | (wettkampf.mostCurrent != null);
vis = vis | (einstellungenwettkampf.mostCurrent != null);
vis = vis | (einstellungen.mostCurrent != null);
vis = vis | (einstellungenah.mostCurrent != null);
vis = vis | (training.mostCurrent != null);
vis = vis | (einstellungenstunde.mostCurrent != null);
vis = vis | (facebook.mostCurrent != null);
vis = vis | (einstellungentrainingkick.mostCurrent != null);
vis = vis | (einstellungentrainingstunde.mostCurrent != null);
vis = vis | (benachrichtigung.mostCurrent != null);
vis = vis | (kampfsportlexikon.mostCurrent != null);
vis = vis | (karatestunde.mostCurrent != null);
vis = vis | (tts.mostCurrent != null);
vis = vis | (lebensmittel.mostCurrent != null);
vis = vis | (tagebuch.mostCurrent != null);
return vis;}
public static class _panelinfo{
public boolean IsInitialized;
public int PanelType;
public boolean LayoutLoaded;
public void Initialize() {
IsInitialized = true;
PanelType = 0;
LayoutLoaded = false;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
String _txt1 = "";
String _txt = "";
int _result = 0;
anywheresoftware.b4a.objects.PanelWrapper[] _panels = null;
boolean _beulagelesen = false;
boolean _blizensbestätigt = false;
int _i = 0;
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _col = null;
 //BA.debugLineNum = 555;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 561;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 562;BA.debugLine="SetupbildschirmHerstellen";
_setupbildschirmherstellen();
 //BA.debugLineNum = 564;BA.debugLine="If manager.GetAll.Size = 0 Then SetzeSetupEinstellungen";
if (_manager.GetAll().getSize()==0) { 
_setzesetupeinstellungen();};
 //BA.debugLineNum = 567;BA.debugLine="If IckWarAllHier = False Then";
if (_ickwarallhier==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 568;BA.debugLine="Dim txt1, txt As String";
_txt1 = "";
_txt = "";
 //BA.debugLineNum = 569;BA.debugLine="txt1 = \"Um diese App benutzen zu können benötigen sie eine Datei mit dem Namen ´mama.zip´. Diese Datei können Sie jetzt gleich über WLAN downloaden oder sie laden ud installieren die Datei über ihren Computer.\"";
_txt1 = "Um diese App benutzen zu können benötigen sie eine Datei mit dem Namen ´mama.zip´. Diese Datei können Sie jetzt gleich über WLAN downloaden oder sie laden ud installieren die Datei über ihren Computer.";
 //BA.debugLineNum = 570;BA.debugLine="txt = \"A C H T U N G\"";
_txt = "A C H T U N G";
 //BA.debugLineNum = 571;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 572;BA.debugLine="result = Msgbox2(txt1,txt,\"JA, jetzt downloaden\", \"Abbruch\", \"NEIN, später\", Null)";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(_txt1,_txt,"JA, jetzt downloaden","Abbruch","NEIN, später",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 574;BA.debugLine="If result = DialogResponse.CANCEL Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 575;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 //BA.debugLineNum = 579;BA.debugLine="If result = DialogResponse.Positive Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 580;BA.debugLine="StartActivity(Startabfragen)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._startabfragen.getObject()));
 }else {
 };
 //BA.debugLineNum = 585;BA.debugLine="If result = DialogResponse.NEGATIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 586;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 };
 };
 //BA.debugLineNum = 595;BA.debugLine="Dim panels(7) As Panel";
_panels = new anywheresoftware.b4a.objects.PanelWrapper[(int) (7)];
{
int d0 = _panels.length;
for (int i0 = 0;i0 < d0;i0++) {
_panels[i0] = new anywheresoftware.b4a.objects.PanelWrapper();
}
}
;
 //BA.debugLineNum = 596;BA.debugLine="Dim bEULAgelesen As Boolean : bEULAgelesen = True";
_beulagelesen = false;
 //BA.debugLineNum = 596;BA.debugLine="Dim bEULAgelesen As Boolean : bEULAgelesen = True";
_beulagelesen = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 597;BA.debugLine="Dim bLIZENSbestätigt As Boolean : bLIZENSbestätigt = True";
_blizensbestätigt = false;
 //BA.debugLineNum = 597;BA.debugLine="Dim bLIZENSbestätigt As Boolean : bLIZENSbestätigt = True";
_blizensbestätigt = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 598;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 601;BA.debugLine="Activity.Title = ActivityTitel";
mostCurrent._activity.setTitle((Object)(_activitytitel));
 //BA.debugLineNum = 603;BA.debugLine="Button1.Initialize(\"Button1\")";
mostCurrent._button1.Initialize(mostCurrent.activityBA,"Button1");
 //BA.debugLineNum = 604;BA.debugLine="Button1.Gravity = Gravity.CENTER";
mostCurrent._button1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 605;BA.debugLine="Activity.AddView(Button1, 88dip, Activity.Height - 44dip, Activity.Width - 88dip - 88dip, 40dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._button1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (88)),(int) (mostCurrent._activity.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (44))),(int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (88))-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (88))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 619;BA.debugLine="container.Initialize";
mostCurrent._container.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 620;BA.debugLine="For i = 0 To 3";
{
final int step188 = 1;
final int limit188 = (int) (3);
for (_i = (int) (0); (step188 > 0 && _i <= limit188) || (step188 < 0 && _i >= limit188); _i = ((int)(0 + _i + step188))) {
 //BA.debugLineNum = 621;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 623;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 625;BA.debugLine="pan = CreatePanel(TYPE_STARTSEITE, \"MAMA\")";
_pan = _createpanel(_type_startseite,"MAMA");
 //BA.debugLineNum = 626;BA.debugLine="container.AddPage(pan,\"Martial Art Master App\" )";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"Martial Art Master App");
 break;
case 1:
 //BA.debugLineNum = 628;BA.debugLine="pan = CreatePanel(TYPE_EBENE1, \"Ebene 1\")";
_pan = _createpanel(_type_ebene1,"Ebene 1");
 //BA.debugLineNum = 629;BA.debugLine="container.AddPage(pan,\"Ebene 1\")";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"Ebene 1");
 break;
case 2:
 //BA.debugLineNum = 631;BA.debugLine="pan = CreatePanel(TYPE_EBENE2, \"Ebene 2\")";
_pan = _createpanel(_type_ebene2,"Ebene 2");
 //BA.debugLineNum = 632;BA.debugLine="container.AddPage(pan,\"Ebene 2\")";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"Ebene 2");
 //BA.debugLineNum = 640;BA.debugLine="pan = CreatePanel(TYPE_EINSTELLUNGEN,Ebene4 )";
_pan = _createpanel(_type_einstellungen,mostCurrent._ebene4);
 //BA.debugLineNum = 641;BA.debugLine="container.AddPage(pan,Ebene4)";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),mostCurrent._ebene4);
 break;
}
;
 }
};
 //BA.debugLineNum = 650;BA.debugLine="pager.Initialize(container, \"Pager\")";
mostCurrent._pager.Initialize(mostCurrent.activityBA,mostCurrent._container,"Pager");
 //BA.debugLineNum = 653;BA.debugLine="tabs.Initialize(pager)";
mostCurrent._tabs.Initialize(mostCurrent.activityBA,mostCurrent._pager);
 //BA.debugLineNum = 654;BA.debugLine="tabs.LineHeight = 5dip";
mostCurrent._tabs.setLineHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 655;BA.debugLine="tabs.UpperCaseTitle = True";
mostCurrent._tabs.setUpperCaseTitle(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 656;BA.debugLine="Activity.AddView(tabs, 0, 0, FILL_PARENT, WRAP_CONTENT)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tabs.getObject()),(int) (0),(int) (0),_fill_parent,_wrap_content);
 //BA.debugLineNum = 659;BA.debugLine="Dim col As ColorDrawable";
_col = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 660;BA.debugLine="col.Initialize(tabs.LineColorCenter, 0)";
_col.Initialize(mostCurrent._tabs.getLineColorCenter(),(int) (0));
 //BA.debugLineNum = 661;BA.debugLine="line.Initialize(\"\")";
mostCurrent._line.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 662;BA.debugLine="line.Background=col";
mostCurrent._line.setBackground((android.graphics.drawable.Drawable)(_col.getObject()));
 //BA.debugLineNum = 663;BA.debugLine="Activity.AddView(line, 0, 35dip, Activity.Width, 2dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._line.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35)),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 667;BA.debugLine="Activity.AddView(pager, 0, 35dip + 2dip, Activity.Width, Activity.Height-48dip-35dip-2dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pager.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35))+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))),mostCurrent._activity.getWidth(),(int) (mostCurrent._activity.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (48))-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35))-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 668;BA.debugLine="SetButtonText";
_setbuttontext();
 //BA.debugLineNum = 672;BA.debugLine="Activity.AddMenuItem(\"Seite hinzufügen\", \"NewPage\")";
mostCurrent._activity.AddMenuItem("Seite hinzufügen","NewPage");
 //BA.debugLineNum = 673;BA.debugLine="Activity.AddMenuItem(\"Lösche diese Seite\", \"DeletePage\")";
mostCurrent._activity.AddMenuItem("Lösche diese Seite","DeletePage");
 //BA.debugLineNum = 679;BA.debugLine="Activity.AddMenuItem(\"Einstellungen\",\"Button5\")";
mostCurrent._activity.AddMenuItem("Einstellungen","Button5");
 //BA.debugLineNum = 683;BA.debugLine="Activity.AddMenuItem(\"Fehler melden\",\"Button6\")";
mostCurrent._activity.AddMenuItem("Fehler melden","Button6");
 //BA.debugLineNum = 685;BA.debugLine="pager.GotoPage(0, True)";
mostCurrent._pager.GotoPage((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 691;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _antw = 0;
 //BA.debugLineNum = 1156;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 1157;BA.debugLine="Dim Antw As Int";
_antw = 0;
 //BA.debugLineNum = 1159;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then ' Hardware-Zurück Taste gedrückt";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 1161;BA.debugLine="If lvEbene1.Visible = True  Then";
if (mostCurrent._lvebene1.getVisible()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1162;BA.debugLine="Antw = Msgbox2(\"Probleme und Anregungen bitte an: www.watchkido.de\" & CRLF & CRLF & \"Wollen Sie das Programm verlassen und ihre Einstellungen abspeichern?\", \"A C H T U N G\", \"Ja\", \"\", \"Nein\",Null)";
_antw = anywheresoftware.b4a.keywords.Common.Msgbox2("Probleme und Anregungen bitte an: www.watchkido.de"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Wollen Sie das Programm verlassen und ihre Einstellungen abspeichern?","A C H T U N G","Ja","","Nein",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 1163;BA.debugLine="If Antw = DialogResponse.POSITIVE Then";
if (_antw==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 1164;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 1165;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 1167;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 }else {
 //BA.debugLineNum = 1170;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 };
 //BA.debugLineNum = 1173;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 702;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 710;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 693;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 694;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 698;BA.debugLine="pager.GotoPage(CurrentPage, False)";
mostCurrent._pager.GotoPage(_currentpage,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 700;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _pl = null;
int _ret = 0;
int _i = 0;
 //BA.debugLineNum = 1035;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 1036;BA.debugLine="Dim pl As List";
_pl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1037;BA.debugLine="Dim ret As Int";
_ret = 0;
 //BA.debugLineNum = 1038;BA.debugLine="pl.Initialize";
_pl.Initialize();
 //BA.debugLineNum = 1041;BA.debugLine="For i = 0 To container.Count - 1";
{
final int step399 = 1;
final int limit399 = (int) (mostCurrent._container.getCount()-1);
for (_i = (int) (0); (step399 > 0 && _i <= limit399) || (step399 < 0 && _i >= limit399); _i = ((int)(0 + _i + step399))) {
 //BA.debugLineNum = 1042;BA.debugLine="pl.Add(container.GetTitle(i))";
_pl.Add((Object)(mostCurrent._container.GetTitle(_i)));
 }
};
 //BA.debugLineNum = 1046;BA.debugLine="ret = InputList(pl, \"Bitte Ebene auswählen\", pager.CurrentPage)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(_pl,"Bitte Ebene auswählen",mostCurrent._pager.getCurrentPage(),mostCurrent.activityBA);
 //BA.debugLineNum = 1048;BA.debugLine="If ret = DialogResponse.CANCEL Then";
if (_ret==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 1049;BA.debugLine="ToastMessageShow(\"Abgebrochen\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Abgebrochen",anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 1052;BA.debugLine="pager.GotoPage(ret, True)";
mostCurrent._pager.GotoPage(_ret,anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 1054;BA.debugLine="End Sub";
return "";
}
public static String  _button5_click() throws Exception{
 //BA.debugLineNum = 2373;BA.debugLine="Sub Button5_click";
 //BA.debugLineNum = 2374;BA.debugLine="pager.GotoPage(4, True)";
mostCurrent._pager.GotoPage((int) (4),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2375;BA.debugLine="End Sub";
return "";
}
public static String  _button6_click() throws Exception{
 //BA.debugLineNum = 2377;BA.debugLine="Sub Button6_click";
 //BA.debugLineNum = 2378;BA.debugLine="StartActivity(Benachrichtigung)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._benachrichtigung.getObject()));
 //BA.debugLineNum = 2379;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.PanelWrapper  _createpanel(int _paneltype,String _title) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
de.watchkido.mama.main._panelinfo _pi = null;
anywheresoftware.b4a.objects.LabelWrapper _lab = null;
anywheresoftware.b4a.objects.LabelWrapper _lab1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
 //BA.debugLineNum = 713;BA.debugLine="Sub CreatePanel(PanelType As Int, Title As String) As Panel";
 //BA.debugLineNum = 714;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 715;BA.debugLine="Dim pi As PanelInfo";
_pi = new de.watchkido.mama.main._panelinfo();
 //BA.debugLineNum = 717;BA.debugLine="pi.Initialize";
_pi.Initialize();
 //BA.debugLineNum = 718;BA.debugLine="pi.LayoutLoaded = False";
_pi.LayoutLoaded = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 719;BA.debugLine="pi.PanelType = PanelType";
_pi.PanelType = _paneltype;
 //BA.debugLineNum = 720;BA.debugLine="Title = Ebene2 ' die grüne überschrift namentlich an die listview anpassen";
_title = mostCurrent._ebene2;
 //BA.debugLineNum = 726;BA.debugLine="pan.Initialize(\"\")";
_pan.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 727;BA.debugLine="Select PanelType";
switch (BA.switchObjectToInt(_paneltype,_type_startseite,_type_ebene1,_type_ebene2,_type_ebene3,_type_ebene4,_type_einstellungen)) {
case 0:
 //BA.debugLineNum = 729;BA.debugLine="Dim lab, lab1 As Label";
_lab = new anywheresoftware.b4a.objects.LabelWrapper();
_lab1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 733;BA.debugLine="Startbildschirmfarbe = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
_startbildschirmfarbe = anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)));
 //BA.debugLineNum = 734;BA.debugLine="pan.Color = Startbildschirmfarbe";
_pan.setColor(_startbildschirmfarbe);
 //BA.debugLineNum = 735;BA.debugLine="lab.Initialize(\"\")";
_lab.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 736;BA.debugLine="lab1.Initialize(\"\")";
_lab1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 738;BA.debugLine="lab.Text = \"Martial Arts Master\"  & CRLF & \"App\" & CRLF & CRLF &CRLF '& lab.Text";
_lab.setText((Object)("Martial Arts Master"+anywheresoftware.b4a.keywords.Common.CRLF+"App"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 739;BA.debugLine="lab.TextSize = 50";
_lab.setTextSize((float) (50));
 //BA.debugLineNum = 740;BA.debugLine="lab.Gravity = Gravity.CENTER";
_lab.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 742;BA.debugLine="lab1.TextSize = 20";
_lab1.setTextSize((float) (20));
 //BA.debugLineNum = 743;BA.debugLine="lab1.Gravity = Gravity.CENTER";
_lab1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 745;BA.debugLine="lab1.Text = Versionsnummer";
_lab1.setText((Object)(_versionsnummer));
 //BA.debugLineNum = 747;BA.debugLine="pan.AddView(lab, 0, 0,FILL_PARENT,FILL_PARENT)";
_pan.AddView((android.view.View)(_lab.getObject()),(int) (0),(int) (0),_fill_parent,_fill_parent);
 //BA.debugLineNum = 748;BA.debugLine="pan.AddView(lab1,0,100,FILL_PARENT,FILL_PARENT)";
_pan.AddView((android.view.View)(_lab1.getObject()),(int) (0),(int) (100),_fill_parent,_fill_parent);
 break;
case 1:
 //BA.debugLineNum = 753;BA.debugLine="lvEbene1.Initialize(\"lvEbene1\")";
mostCurrent._lvebene1.Initialize(mostCurrent.activityBA,"lvEbene1");
 //BA.debugLineNum = 754;BA.debugLine="lvEbene1.FastScrollEnabled = True 'schnellscrollgriff";
mostCurrent._lvebene1.setFastScrollEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 755;BA.debugLine="lvEbene1.SingleLineLayout.Label.Gravity = Gravity.CENTER";
mostCurrent._lvebene1.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 756;BA.debugLine="lvEbene1.SingleLineLayout.Label.TextSize = 20";
mostCurrent._lvebene1.getSingleLineLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 757;BA.debugLine="lvEbene1.SingleLineLayout.ItemHeight = 35dip";
mostCurrent._lvebene1.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35)));
 //BA.debugLineNum = 758;BA.debugLine="lvEbene1.SingleLineLayout.Label.TextColor = Colors.Blue'.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene1.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 759;BA.debugLine="lvEbene1.SingleLineLayout.Label.Color = Colors.Cyan'.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 760;BA.debugLine="lvEbene1.SingleLineLayout.Label.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._lvebene1.getSingleLineLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 764;BA.debugLine="lvEbene1.AddSingleLine2 (\"Sportarten\",0)";
mostCurrent._lvebene1.AddSingleLine2("Sportarten",(Object)(0));
 //BA.debugLineNum = 765;BA.debugLine="lvEbene1.AddTwoLines2 (\"Bodybuilding\",\"Kraftausdauer\",3 )";
mostCurrent._lvebene1.AddTwoLines2("Bodybuilding","Kraftausdauer",(Object)(3));
 //BA.debugLineNum = 766;BA.debugLine="lvEbene1.AddTwoLines2 (\"Karate\",\"Shotokan\",2 )";
mostCurrent._lvebene1.AddTwoLines2("Karate","Shotokan",(Object)(2));
 //BA.debugLineNum = 767;BA.debugLine="lvEbene1.AddTwoLines2 (\"Kickboxen\",\"Full Kontakt\",1 )";
mostCurrent._lvebene1.AddTwoLines2("Kickboxen","Full Kontakt",(Object)(1));
 //BA.debugLineNum = 768;BA.debugLine="lvEbene1.AddTwoLines2 (\"Ninjutsu\",\"Watchkido Ryu\",4 )";
mostCurrent._lvebene1.AddTwoLines2("Ninjutsu","Watchkido Ryu",(Object)(4));
 //BA.debugLineNum = 769;BA.debugLine="lvEbene1.AddTwoLines2 (\"Tae kwon do\",\"\",5 )";
mostCurrent._lvebene1.AddTwoLines2("Tae kwon do","",(Object)(5));
 //BA.debugLineNum = 770;BA.debugLine="lvEbene1.AddTwoLines2 (\"Manrikigusari\",\"\",6)";
mostCurrent._lvebene1.AddTwoLines2("Manrikigusari","",(Object)(6));
 //BA.debugLineNum = 771;BA.debugLine="lvEbene1.AddTwoLines2 (\"Nunchaku\",\"\",7)";
mostCurrent._lvebene1.AddTwoLines2("Nunchaku","",(Object)(7));
 //BA.debugLineNum = 772;BA.debugLine="lvEbene1.AddTwoLines2 (\"Kubotan\",\"\",8)";
mostCurrent._lvebene1.AddTwoLines2("Kubotan","",(Object)(8));
 //BA.debugLineNum = 773;BA.debugLine="If Businessversion Then";
if (_businessversion) { 
 //BA.debugLineNum = 774;BA.debugLine="lvEbene1.AddSingleLine2 (\"Martial Arts Master\",0)";
mostCurrent._lvebene1.AddSingleLine2("Martial Arts Master",(Object)(0));
 //BA.debugLineNum = 775;BA.debugLine="lvEbene1.AddTwoLines2 (\"Lebensmittel\",\"Datenbank \",11 )";
mostCurrent._lvebene1.AddTwoLines2("Lebensmittel","Datenbank ",(Object)(11));
 //BA.debugLineNum = 776;BA.debugLine="lvEbene1.AddTwoLines2 (\"Tagebuch\",\"Tagebuch\",12 )";
mostCurrent._lvebene1.AddTwoLines2("Tagebuch","Tagebuch",(Object)(12));
 //BA.debugLineNum = 777;BA.debugLine="lvEbene1.AddTwoLines2 (\"Erfolge Teilen\",\"Facebook, Twitter, WKW, Linkedin, Blog, ...\",9 )";
mostCurrent._lvebene1.AddTwoLines2("Erfolge Teilen","Facebook, Twitter, WKW, Linkedin, Blog, ...",(Object)(9));
 //BA.debugLineNum = 778;BA.debugLine="lvEbene1.AddTwoLines2 (\"Kampfsport Lexikon\",\"\",10)";
mostCurrent._lvebene1.AddTwoLines2("Kampfsport Lexikon","",(Object)(10));
 //BA.debugLineNum = 779;BA.debugLine="lvEbene1.AddTwoLines2 (\"Chef\",\"\",4000 )";
mostCurrent._lvebene1.AddTwoLines2("Chef","",(Object)(4000));
 //BA.debugLineNum = 780;BA.debugLine="lvEbene1.AddTwoLines2 (\"Leistungsdiagnostik\",\"\", 4100)";
mostCurrent._lvebene1.AddTwoLines2("Leistungsdiagnostik","",(Object)(4100));
 //BA.debugLineNum = 781;BA.debugLine="lvEbene1.AddTwoLines2 (\"Ordnung, Recht und Gesetz\",\"\",4200)";
mostCurrent._lvebene1.AddTwoLines2("Ordnung, Recht und Gesetz","",(Object)(4200));
 }else {
 //BA.debugLineNum = 783;BA.debugLine="lvEbene1.AddSingleLine2 (\"Martial Arts Master (FREE)\",4000)";
mostCurrent._lvebene1.AddSingleLine2("Martial Arts Master (FREE)",(Object)(4000));
 };
 //BA.debugLineNum = 799;BA.debugLine="pan.AddView(lvEbene1, 0, 0, FILL_PARENT, FILL_PARENT)";
_pan.AddView((android.view.View)(mostCurrent._lvebene1.getObject()),(int) (0),(int) (0),_fill_parent,_fill_parent);
 break;
case 2:
 //BA.debugLineNum = 805;BA.debugLine="lvEbene2.Initialize(\"lvEbene2\")";
mostCurrent._lvebene2.Initialize(mostCurrent.activityBA,"lvEbene2");
 //BA.debugLineNum = 807;BA.debugLine="lvEbene2.FastScrollEnabled = True 'schnellscrollgriff";
mostCurrent._lvebene2.setFastScrollEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 808;BA.debugLine="lvEbene2.SingleLineLayout.Label.Gravity = Gravity.CENTER";
mostCurrent._lvebene2.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 809;BA.debugLine="lvEbene2.SingleLineLayout.Label.TextSize = 20";
mostCurrent._lvebene2.getSingleLineLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 810;BA.debugLine="lvEbene2.SingleLineLayout.ItemHeight = 35dip";
mostCurrent._lvebene2.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35)));
 //BA.debugLineNum = 811;BA.debugLine="lvEbene2.SingleLineLayout.Label.TextColor = Colors.RGB(0, 128, 0)";
mostCurrent._lvebene2.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (128),(int) (0)));
 //BA.debugLineNum = 812;BA.debugLine="lvEbene2.SingleLineLayout.Label.Color = Colors.RGB(173, 255, 47)";
mostCurrent._lvebene2.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (173),(int) (255),(int) (47)));
 //BA.debugLineNum = 813;BA.debugLine="lvEbene2.SingleLineLayout.Label.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._lvebene2.getSingleLineLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 816;BA.debugLine="lvEbene2.AddSingleLine2(\"Bitte Auswahl treffen \",100)";
mostCurrent._lvebene2.AddSingleLine2("Bitte Auswahl treffen ",(Object)(100));
 //BA.debugLineNum = 818;BA.debugLine="pan.AddView(lvEbene2, 0, 0, FILL_PARENT, FILL_PARENT)";
_pan.AddView((android.view.View)(mostCurrent._lvebene2.getObject()),(int) (0),(int) (0),_fill_parent,_fill_parent);
 break;
case 3:
 //BA.debugLineNum = 824;BA.debugLine="lvEbene3.Initialize(\"lvEbene3\")";
mostCurrent._lvebene3.Initialize(mostCurrent.activityBA,"lvEbene3");
 //BA.debugLineNum = 825;BA.debugLine="lvEbene3.FastScrollEnabled = True 'schnellscrollgriff";
mostCurrent._lvebene3.setFastScrollEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 826;BA.debugLine="lvEbene3.SingleLineLayout.Label.Gravity = Gravity.CENTER";
mostCurrent._lvebene3.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 827;BA.debugLine="lvEbene3.SingleLineLayout.Label.TextSize = 20";
mostCurrent._lvebene3.getSingleLineLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 828;BA.debugLine="lvEbene3.SingleLineLayout.ItemHeight = 35dip";
mostCurrent._lvebene3.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35)));
 //BA.debugLineNum = 829;BA.debugLine="lvEbene3.SingleLineLayout.Label.TextColor = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene3.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 //BA.debugLineNum = 830;BA.debugLine="lvEbene3.SingleLineLayout.Label.Color = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene3.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 //BA.debugLineNum = 831;BA.debugLine="lvEbene3.SingleLineLayout.Label.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._lvebene3.getSingleLineLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 833;BA.debugLine="lvEbene3.AddSingleLine2(\"Bitte Auswahl treffen\",100)";
mostCurrent._lvebene3.AddSingleLine2("Bitte Auswahl treffen",(Object)(100));
 //BA.debugLineNum = 841;BA.debugLine="pan.AddView(lvEbene3, 0, 0, FILL_PARENT, FILL_PARENT)";
_pan.AddView((android.view.View)(mostCurrent._lvebene3.getObject()),(int) (0),(int) (0),_fill_parent,_fill_parent);
 break;
case 4:
 //BA.debugLineNum = 846;BA.debugLine="lvEbene4.Initialize(\"lvEbene4\")";
mostCurrent._lvebene4.Initialize(mostCurrent.activityBA,"lvEbene4");
 //BA.debugLineNum = 848;BA.debugLine="lvEbene4.AddSingleLine2(\"Bitte Auswahl treffen\",100)";
mostCurrent._lvebene4.AddSingleLine2("Bitte Auswahl treffen",(Object)(100));
 //BA.debugLineNum = 850;BA.debugLine="pan.AddView(lvEbene4, 0, 0, FILL_PARENT, FILL_PARENT)";
_pan.AddView((android.view.View)(mostCurrent._lvebene4.getObject()),(int) (0),(int) (0),_fill_parent,_fill_parent);
 break;
case 5:
 //BA.debugLineNum = 854;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 855;BA.debugLine="bmp = LoadBitmap(File.DirAssets,\"mamalogo.png\")";
_bmp = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamalogo.png");
 //BA.debugLineNum = 856;BA.debugLine="lvEbene4.Initialize(\"lvEbene4\")";
mostCurrent._lvebene4.Initialize(mostCurrent.activityBA,"lvEbene4");
 //BA.debugLineNum = 858;BA.debugLine="lvEbene4.Clear";
mostCurrent._lvebene4.Clear();
 //BA.debugLineNum = 859;BA.debugLine="lvEbene4.FastScrollEnabled = True 'schnellscrollgriff";
mostCurrent._lvebene4.setFastScrollEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 860;BA.debugLine="lvEbene4.SingleLineLayout.Label.Gravity = Gravity.CENTER";
mostCurrent._lvebene4.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 861;BA.debugLine="lvEbene4.SingleLineLayout.Label.TextSize = 20";
mostCurrent._lvebene4.getSingleLineLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 862;BA.debugLine="lvEbene4.SingleLineLayout.ItemHeight = 35dip";
mostCurrent._lvebene4.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35)));
 //BA.debugLineNum = 863;BA.debugLine="lvEbene4.SingleLineLayout.Label.TextColor = Colors.red'RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene4.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 864;BA.debugLine="lvEbene4.SingleLineLayout.Label.Color = Colors.RGB(255, 165, 0)";
mostCurrent._lvebene4.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (165),(int) (0)));
 //BA.debugLineNum = 865;BA.debugLine="lvEbene4.SingleLineLayout.Label.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._lvebene4.getSingleLineLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 883;BA.debugLine="lvEbene4.AddSingleLine(\"Personalien\")";
mostCurrent._lvebene4.AddSingleLine("Personalien");
 //BA.debugLineNum = 884;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Personalien\",\"Name, Wohnort, Verein, ...\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Personalien","Name, Wohnort, Verein, ...",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 885;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Training\",\"Intensität, Sportart, Ziele, ...\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Training","Intensität, Sportart, Ziele, ...",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 887;BA.debugLine="lvEbene4.AddSingleLine(\"Netzwerke\")";
mostCurrent._lvebene4.AddSingleLine("Netzwerke");
 //BA.debugLineNum = 888;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Netzwerke\",\"Facebook, Twitter, Google+, ...\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Netzwerke","Facebook, Twitter, Google+, ...",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 890;BA.debugLine="lvEbene4.AddSingleLine(\"Applikation\")";
mostCurrent._lvebene4.AddSingleLine("Applikation");
 //BA.debugLineNum = 891;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Einheiten\",\"Gewichte, Maße, Währungen\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Einheiten","Gewichte, Maße, Währungen",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 892;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Anfangsgewicht\",Anfangsgewicht & Gewicht,bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Anfangsgewicht",BA.NumberToString(_anfangsgewicht)+BA.NumberToString(_gewicht),(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 893;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Zielgewicht\",Zielgewicht & Gewicht,bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Zielgewicht",BA.NumberToString(_zielgewicht)+BA.NumberToString(_gewicht),(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 894;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Lohn\",\"Bezahlung für Kontrollöre\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Lohn","Bezahlung für Kontrollöre",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 895;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Zeiten\",\"Starttage,Trainingszeitenerien,...\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Zeiten","Starttage,Trainingszeitenerien,...",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 896;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Extras\",\"Eula,Farben,Lizensschlüssel\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Extras","Eula,Farben,Lizensschlüssel",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 897;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Spenden\",\"Paypal, ...\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Spenden","Paypal, ...",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 898;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Widget\",\"Anzeigen, ...\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Widget","Anzeigen, ...",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 925;BA.debugLine="lvEbene4.AddSingleLine(\"Extras\")";
mostCurrent._lvebene4.AddSingleLine("Extras");
 //BA.debugLineNum = 926;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"eMail-Feedback\",\"\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("eMail-Feedback","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 927;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"Eula\",\"\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Eula","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 928;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"Farben\",\"\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Farben","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 930;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"Lizensschlüssel\",\"\" ,bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Lizensschlüssel","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 931;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"Passwort\",\"\" ,bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Passwort","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 932;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"Spenden\",\"\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Spenden","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 933;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"Facebook\",\"\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Facebook","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 935;BA.debugLine="lvEbene4.AddSingleLine (\"Widgeds\")";
mostCurrent._lvebene4.AddSingleLine("Widgeds");
 //BA.debugLineNum = 936;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"Erfolge\",\"\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Erfolge","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 937;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap (\"Ziele\",\"\",bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Ziele","",(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 939;BA.debugLine="lvEbene4.AddSingleLine(\"Farben\")";
mostCurrent._lvebene4.AddSingleLine("Farben");
 //BA.debugLineNum = 941;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Hintergrundfarbe\",Hintergrundfarbe,bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Hintergrundfarbe",BA.NumberToString(_hintergrundfarbe),(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 942;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Schriftfarbe\",Schriftfarbe,bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Schriftfarbe",BA.NumberToString(_schriftfarbe),(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 943;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Startbildschirm\",Startbildschirmfarbe,bmp)";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Startbildschirm",BA.NumberToString(_startbildschirmfarbe),(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 946;BA.debugLine="lvEbene4.AddSingleLine(\"Speichern\")";
mostCurrent._lvebene4.AddSingleLine("Speichern");
 //BA.debugLineNum = 948;BA.debugLine="lvEbene4.AddTwoLinesAndBitmap(\"Jetzt Abspeichern\",\"Klicken Sie hier zum speichern\", LoadBitmap(File.DirAssets,\"mamaLogo.png\"))";
mostCurrent._lvebene4.AddTwoLinesAndBitmap("Jetzt Abspeichern","Klicken Sie hier zum speichern",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png").getObject()));
 //BA.debugLineNum = 950;BA.debugLine="pan.AddView(lvEbene4, 0, 0, FILL_PARENT, FILL_PARENT)";
_pan.AddView((android.view.View)(mostCurrent._lvebene4.getObject()),(int) (0),(int) (0),_fill_parent,_fill_parent);
 break;
}
;
 //BA.debugLineNum = 958;BA.debugLine="pan.Tag = pi";
_pan.setTag((Object)(_pi));
 //BA.debugLineNum = 959;BA.debugLine="Return pan";
if (true) return _pan;
 //BA.debugLineNum = 962;BA.debugLine="End Sub";
return null;
}
public static String  _deletepage_click() throws Exception{
 //BA.debugLineNum = 995;BA.debugLine="Sub DeletePage_Click";
 //BA.debugLineNum = 997;BA.debugLine="container.DeletePage(pager.CurrentPage)";
mostCurrent._container.DeletePage(mostCurrent._pager.getCurrentPage());
 //BA.debugLineNum = 999;BA.debugLine="tabs.NotifyDataChange";
mostCurrent._tabs.NotifyDataChange();
 //BA.debugLineNum = 1000;BA.debugLine="End Sub";
return "";
}
public static String  _erklaerungen() throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Erklaerungen";
 //BA.debugLineNum = 420;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
spiele._process_globals();
trainingkickboxen._process_globals();
startabfragen._process_globals();
einstellungenapp._process_globals();
erfolgsmeldung._process_globals();
checkliste._process_globals();
wettkampf._process_globals();
einstellungenwettkampf._process_globals();
einstellungen._process_globals();
einstellungenah._process_globals();
training._process_globals();
einstellungenstunde._process_globals();
facebook._process_globals();
multipartpost._process_globals();
einstellungentrainingkick._process_globals();
einstellungentrainingstunde._process_globals();
benachrichtigung._process_globals();
downloadservice._process_globals();
kampfsportlexikon._process_globals();
karatestunde._process_globals();
tts._process_globals();
lebensmittel._process_globals();
statemanager._process_globals();
tagebuch._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _globals() throws Exception{
 //BA.debugLineNum = 511;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 515;BA.debugLine="Dim container As AHPageContainer";
mostCurrent._container = new de.amberhome.viewpager.PageContainerAdapter();
 //BA.debugLineNum = 516;BA.debugLine="Dim pager As AHViewPager";
mostCurrent._pager = new de.amberhome.viewpager.ViewPagerWrapper();
 //BA.debugLineNum = 519;BA.debugLine="Dim line As Panel";
mostCurrent._line = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 524;BA.debugLine="Dim Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 527;BA.debugLine="Dim tabs As AHViewPagerTabs";
mostCurrent._tabs = new de.amberhome.viewpager.ViewPagerTabsWrapper();
 //BA.debugLineNum = 529;BA.debugLine="Dim spTheme, spnErwaermung, spnTraining, spnPause As Spinner";
mostCurrent._sptheme = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnerwaermung = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spntraining = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnpause = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 531;BA.debugLine="Dim lvEbene1 As ListView";
mostCurrent._lvebene1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 532;BA.debugLine="Dim lvEbene2 As ListView";
mostCurrent._lvebene2 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 533;BA.debugLine="Dim lvEbene3 As ListView";
mostCurrent._lvebene3 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 534;BA.debugLine="Dim lvEbene4 As ListView";
mostCurrent._lvebene4 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 536;BA.debugLine="Dim Ebene1 As String	: Ebene1 = \"Auswahl\"";
mostCurrent._ebene1 = "";
 //BA.debugLineNum = 536;BA.debugLine="Dim Ebene1 As String	: Ebene1 = \"Auswahl\"";
mostCurrent._ebene1 = "Auswahl";
 //BA.debugLineNum = 537;BA.debugLine="Dim Ebene2 As String	: Ebene2 = \"Ebene 2\"";
mostCurrent._ebene2 = "";
 //BA.debugLineNum = 537;BA.debugLine="Dim Ebene2 As String	: Ebene2 = \"Ebene 2\"";
mostCurrent._ebene2 = "Ebene 2";
 //BA.debugLineNum = 538;BA.debugLine="Dim Ebene4 As String	: Ebene4 = \"Einstellungen\"";
mostCurrent._ebene4 = "";
 //BA.debugLineNum = 538;BA.debugLine="Dim Ebene4 As String	: Ebene4 = \"Einstellungen\"";
mostCurrent._ebene4 = "Einstellungen";
 //BA.debugLineNum = 541;BA.debugLine="Dim ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 542;BA.debugLine="Dim Anfangsgewicht, Zielgewicht, Kontrolltag, Zeitzone As Int";
_anfangsgewicht = 0;
_zielgewicht = 0;
_kontrolltag = 0;
_zeitzone = 0;
 //BA.debugLineNum = 543;BA.debugLine="Dim Schriftfarbe, Hintergrundfarbe, Startbildschirmfarbe As Int";
_schriftfarbe = 0;
_hintergrundfarbe = 0;
_startbildschirmfarbe = 0;
 //BA.debugLineNum = 544;BA.debugLine="Dim MaxLohn, MinLohn, Ausgabepreis,StrafLohn As Double";
_maxlohn = 0;
_minlohn = 0;
_ausgabepreis = 0;
_straflohn = 0;
 //BA.debugLineNum = 545;BA.debugLine="Dim Name, Waehrung As String";
mostCurrent._name = "";
mostCurrent._waehrung = "";
 //BA.debugLineNum = 546;BA.debugLine="Dim Starttag, Zieltag, Startzeit, Zielzeit As String";
mostCurrent._starttag = "";
mostCurrent._zieltag = "";
mostCurrent._startzeit = "";
mostCurrent._zielzeit = "";
 //BA.debugLineNum = 547;BA.debugLine="Dim Speicherort As String";
mostCurrent._speicherort = "";
 //BA.debugLineNum = 548;BA.debugLine="Dim Liste1, Liste2 As List";
mostCurrent._liste1 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._liste2 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 553;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
 //BA.debugLineNum = 2543;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 2544;BA.debugLine="Log(manager.GetAll)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(_manager.GetAll()));
 //BA.debugLineNum = 2546;BA.debugLine="manager.GetString(\"Hintergrundfarbe\")";
_manager.GetString("Hintergrundfarbe");
 //BA.debugLineNum = 2563;BA.debugLine="End Sub";
return "";
}
public static String  _lvebene1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1175;BA.debugLine="Sub lvEbene1_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 1179;BA.debugLine="lvEbene1.FastScrollEnabled = True 'schnellscrollgriff";
mostCurrent._lvebene1.setFastScrollEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1180;BA.debugLine="lvEbene1.SingleLineLayout.Label.Gravity = Gravity.CENTER";
mostCurrent._lvebene1.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1181;BA.debugLine="lvEbene1.SingleLineLayout.Label.TextSize = 20";
mostCurrent._lvebene1.getSingleLineLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 1182;BA.debugLine="lvEbene1.SingleLineLayout.ItemHeight = 35dip";
mostCurrent._lvebene1.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35)));
 //BA.debugLineNum = 1183;BA.debugLine="lvEbene1.SingleLineLayout.Label.TextColor = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene1.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 //BA.debugLineNum = 1184;BA.debugLine="lvEbene1.SingleLineLayout.Label.Color = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 //BA.debugLineNum = 1185;BA.debugLine="lvEbene1.SingleLineLayout.Label.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._lvebene1.getSingleLineLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 1188;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)(1),(Object)(2),(Object)(3),(Object)(4),(Object)(5),(Object)(6),(Object)(7),(Object)(9),(Object)(10),(Object)(11),(Object)(12),(Object)(200),(Object)(300),(Object)(400),(Object)(500),(Object)(600),(Object)(700),(Object)(800),(Object)(900),(Object)(1000),(Object)(1100),(Object)(1200),(Object)(1300),(Object)(1400),(Object)(1500),(Object)(1600),(Object)(1700),(Object)(1800),(Object)(1900),(Object)(3000),(Object)(3200),(Object)(3300),(Object)(3301),(Object)(3400),(Object)(3500),(Object)(3600),(Object)(3601),(Object)(3700),(Object)(3701),(Object)(3800),(Object)(3900),(Object)(4000),(Object)(4100),(Object)(4200),(Object)(4300),(Object)(4301),(Object)(4302),(Object)(4303),(Object)(4304),(Object)(4305),(Object)(4306),(Object)(4307),(Object)(4308),(Object)(4309),(Object)(4400),(Object)(4401),(Object)(4500))) {
case 0:
 //BA.debugLineNum = 1191;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 1192;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1193;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1195;BA.debugLine="lvEbene2.AddSingleLine2 (\"Kickboxen (Stunde)\",0)";
mostCurrent._lvebene2.AddSingleLine2("Kickboxen (Stunde)",(Object)(0));
 //BA.debugLineNum = 1196;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kickboxen \",\"Vollständiges Training\", 2000)";
mostCurrent._lvebene2.AddTwoLines2("Kickboxen ","Vollständiges Training",(Object)(2000));
 //BA.debugLineNum = 1198;BA.debugLine="lvEbene2.AddSingleLine2 (\"Kickboxen (Speziell)\",0)";
mostCurrent._lvebene2.AddSingleLine2("Kickboxen (Speziell)",(Object)(0));
 //BA.debugLineNum = 1199;BA.debugLine="lvEbene2.AddTwoLines2 (\"Erwärmung Allgemein\",\"\",100)";
mostCurrent._lvebene2.AddTwoLines2("Erwärmung Allgemein","",(Object)(100));
 //BA.debugLineNum = 1200;BA.debugLine="lvEbene2.AddTwoLines2 (\"Erwärmung Kickboxen\",\"\", 200 )";
mostCurrent._lvebene2.AddTwoLines2("Erwärmung Kickboxen","",(Object)(200));
 //BA.debugLineNum = 1201;BA.debugLine="lvEbene2.AddTwoLines2 (\"Einzelübungen\",\"\", 300 )";
mostCurrent._lvebene2.AddTwoLines2("Einzelübungen","",(Object)(300));
 //BA.debugLineNum = 1202;BA.debugLine="lvEbene2.AddTwoLines2 (\"Partnerübungen\",\"\", 400)";
mostCurrent._lvebene2.AddTwoLines2("Partnerübungen","",(Object)(400));
 //BA.debugLineNum = 1203;BA.debugLine="lvEbene2.AddTwoLines2 (\"BodyWeightExercices\",\"\", 500 )";
mostCurrent._lvebene2.AddTwoLines2("BodyWeightExercices","",(Object)(500));
 //BA.debugLineNum = 1204;BA.debugLine="lvEbene2.AddTwoLines2 (\"Dehnung \",\"\",1800)";
mostCurrent._lvebene2.AddTwoLines2("Dehnung ","",(Object)(1800));
 //BA.debugLineNum = 1205;BA.debugLine="lvEbene2.AddTwoLines2 (\"Cool Down\",\"\",1600)";
mostCurrent._lvebene2.AddTwoLines2("Cool Down","",(Object)(1600));
 //BA.debugLineNum = 1207;BA.debugLine="lvEbene2.AddSingleLine2 (\"Extras\",100)";
mostCurrent._lvebene2.AddSingleLine2("Extras",(Object)(100));
 //BA.debugLineNum = 1208;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kampfsport-Spiele\",\"\",1700)";
mostCurrent._lvebene2.AddTwoLines2("Kampfsport-Spiele","",(Object)(1700));
 //BA.debugLineNum = 1209;BA.debugLine="lvEbene2.AddTwoLines2 (\"Wettkampf-Timer\",\"\",1900)";
mostCurrent._lvebene2.AddTwoLines2("Wettkampf-Timer","",(Object)(1900));
 break;
case 1:
 //BA.debugLineNum = 1215;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 1217;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1218;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1219;BA.debugLine="lvEbene2.AddSingleLine2 (\"Karate (Stunde)\",0)";
mostCurrent._lvebene2.AddSingleLine2("Karate (Stunde)",(Object)(0));
 //BA.debugLineNum = 1220;BA.debugLine="lvEbene2.AddTwoLines2 (\"Karate\",\"Vollständiges Training\", 1000)";
mostCurrent._lvebene2.AddTwoLines2("Karate","Vollständiges Training",(Object)(1000));
 //BA.debugLineNum = 1222;BA.debugLine="lvEbene2.AddSingleLine2 (\"Karate (Speziell)\",0)";
mostCurrent._lvebene2.AddSingleLine2("Karate (Speziell)",(Object)(0));
 //BA.debugLineNum = 1223;BA.debugLine="lvEbene2.AddTwoLines2 (\"Erwärmung Allgemein\",\"\",1010)";
mostCurrent._lvebene2.AddTwoLines2("Erwärmung Allgemein","",(Object)(1010));
 //BA.debugLineNum = 1224;BA.debugLine="lvEbene2.AddTwoLines2 (\"Erwärmung Karate\",\"\", 1020 )";
mostCurrent._lvebene2.AddTwoLines2("Erwärmung Karate","",(Object)(1020));
 //BA.debugLineNum = 1225;BA.debugLine="lvEbene2.AddTwoLines2 (\"Einzelübungen\",\"\",1030)";
mostCurrent._lvebene2.AddTwoLines2("Einzelübungen","",(Object)(1030));
 //BA.debugLineNum = 1226;BA.debugLine="lvEbene2.AddTwoLines2 (\"Dachi\",\"\",1040)";
mostCurrent._lvebene2.AddTwoLines2("Dachi","",(Object)(1040));
 //BA.debugLineNum = 1227;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kata\",\"\",1050)";
mostCurrent._lvebene2.AddTwoLines2("Kata","",(Object)(1050));
 //BA.debugLineNum = 1228;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kumite\",\"\",1060)";
mostCurrent._lvebene2.AddTwoLines2("Kumite","",(Object)(1060));
 //BA.debugLineNum = 1229;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kihon\",\"\",1070)";
mostCurrent._lvebene2.AddTwoLines2("Kihon","",(Object)(1070));
 //BA.debugLineNum = 1230;BA.debugLine="lvEbene2.AddTwoLines2 (\"Selbstverteidigung\",\"\",1080)";
mostCurrent._lvebene2.AddTwoLines2("Selbstverteidigung","",(Object)(1080));
 //BA.debugLineNum = 1231;BA.debugLine="lvEbene2.AddTwoLines2 (\"Cool Down\",\"\",1090)";
mostCurrent._lvebene2.AddTwoLines2("Cool Down","",(Object)(1090));
 //BA.debugLineNum = 1232;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kampfsport-Spiele\",\"\",1700)";
mostCurrent._lvebene2.AddTwoLines2("Kampfsport-Spiele","",(Object)(1700));
 //BA.debugLineNum = 1234;BA.debugLine="lvEbene2.AddSingleLine2 (\"Nach Leistungsstand\",0)";
mostCurrent._lvebene2.AddSingleLine2("Nach Leistungsstand",(Object)(0));
 //BA.debugLineNum = 1235;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppenzusammenstellung\",\"\",1220)";
mostCurrent._lvebene2.AddTwoLines2("Gruppenzusammenstellung","",(Object)(1220));
 //BA.debugLineNum = 1236;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gemischt\",\"\",1230)";
mostCurrent._lvebene2.AddTwoLines2("Gemischt","",(Object)(1230));
 //BA.debugLineNum = 1237;BA.debugLine="lvEbene2.AddTwoLines2 (\"Grundkurs\",\"\",1240)";
mostCurrent._lvebene2.AddTwoLines2("Grundkurs","",(Object)(1240));
 //BA.debugLineNum = 1238;BA.debugLine="lvEbene2.AddTwoLines2 (\"10. Kyu\",\"\",1110)";
mostCurrent._lvebene2.AddTwoLines2("10. Kyu","",(Object)(1110));
 //BA.debugLineNum = 1239;BA.debugLine="lvEbene2.AddTwoLines2 (\"9. Kyu\",\"\",1120)";
mostCurrent._lvebene2.AddTwoLines2("9. Kyu","",(Object)(1120));
 //BA.debugLineNum = 1240;BA.debugLine="lvEbene2.AddTwoLines2 (\"8. Kyu\",\"\",1130)";
mostCurrent._lvebene2.AddTwoLines2("8. Kyu","",(Object)(1130));
 //BA.debugLineNum = 1241;BA.debugLine="lvEbene2.AddTwoLines2 (\"7. Kyu\",\"\",1140)";
mostCurrent._lvebene2.AddTwoLines2("7. Kyu","",(Object)(1140));
 //BA.debugLineNum = 1242;BA.debugLine="lvEbene2.AddTwoLines2 (\"6. Kyu\",\"\",1150)";
mostCurrent._lvebene2.AddTwoLines2("6. Kyu","",(Object)(1150));
 //BA.debugLineNum = 1243;BA.debugLine="lvEbene2.AddTwoLines2 (\"5. Kyu\",\"\",1160)";
mostCurrent._lvebene2.AddTwoLines2("5. Kyu","",(Object)(1160));
 //BA.debugLineNum = 1244;BA.debugLine="lvEbene2.AddTwoLines2 (\"4. Kyu\",\"\",1170)";
mostCurrent._lvebene2.AddTwoLines2("4. Kyu","",(Object)(1170));
 //BA.debugLineNum = 1245;BA.debugLine="lvEbene2.AddTwoLines2 (\"3. Kyu\",\"\",1180)";
mostCurrent._lvebene2.AddTwoLines2("3. Kyu","",(Object)(1180));
 //BA.debugLineNum = 1246;BA.debugLine="lvEbene2.AddTwoLines2 (\"2. Kyu\",\"\",1190)";
mostCurrent._lvebene2.AddTwoLines2("2. Kyu","",(Object)(1190));
 //BA.debugLineNum = 1247;BA.debugLine="lvEbene2.AddTwoLines2 (\"1. Kyu\",\"\",1200)";
mostCurrent._lvebene2.AddTwoLines2("1. Kyu","",(Object)(1200));
 //BA.debugLineNum = 1248;BA.debugLine="lvEbene2.AddTwoLines2 (\"Meisterschüler\",\"\",1210)";
mostCurrent._lvebene2.AddTwoLines2("Meisterschüler","",(Object)(1210));
 //BA.debugLineNum = 1249;BA.debugLine="lvEbene2.AddTwoLines2 (\"Meister (1. Dan)\",\"\",1330)";
mostCurrent._lvebene2.AddTwoLines2("Meister (1. Dan)","",(Object)(1330));
 //BA.debugLineNum = 1250;BA.debugLine="lvEbene2.AddTwoLines2 (\"Meister (2. Dan)\",\"\",1340)";
mostCurrent._lvebene2.AddTwoLines2("Meister (2. Dan)","",(Object)(1340));
 //BA.debugLineNum = 1251;BA.debugLine="lvEbene2.AddTwoLines2 (\"Meister (3. Dan)\",\"\",1350)";
mostCurrent._lvebene2.AddTwoLines2("Meister (3. Dan)","",(Object)(1350));
 //BA.debugLineNum = 1253;BA.debugLine="lvEbene2.AddSingleLine2(\"Champions Club\",0)";
mostCurrent._lvebene2.AddSingleLine2("Champions Club",(Object)(0));
 //BA.debugLineNum = 1254;BA.debugLine="lvEbene2.AddTwoLines2 (\"Training Ausgeglichenheit\",\"\",1250)";
mostCurrent._lvebene2.AddTwoLines2("Training Ausgeglichenheit","",(Object)(1250));
 //BA.debugLineNum = 1255;BA.debugLine="lvEbene2.AddTwoLines2 (\"Wettkampf\",\"\",1270)";
mostCurrent._lvebene2.AddTwoLines2("Wettkampf","",(Object)(1270));
 //BA.debugLineNum = 1256;BA.debugLine="lvEbene2.AddTwoLines2 (\"Abhärtung\",\"\",1280)";
mostCurrent._lvebene2.AddTwoLines2("Abhärtung","",(Object)(1280));
 //BA.debugLineNum = 1257;BA.debugLine="lvEbene2.AddTwoLines2 (\"Finten, Tricks, Strategie\",\"\",1290)";
mostCurrent._lvebene2.AddTwoLines2("Finten, Tricks, Strategie","",(Object)(1290));
 //BA.debugLineNum = 1258;BA.debugLine="lvEbene2.AddTwoLines2 (\"Periodisierung\",\"\",1300)";
mostCurrent._lvebene2.AddTwoLines2("Periodisierung","",(Object)(1300));
 //BA.debugLineNum = 1260;BA.debugLine="lvEbene2.AddSingleLine2 (\"Black Belt Club\",0)";
mostCurrent._lvebene2.AddSingleLine2("Black Belt Club",(Object)(0));
 //BA.debugLineNum = 1261;BA.debugLine="lvEbene2.AddTwoLines2 (\"Praktische Eigensicherung\",\"lt. Wachschutzrichtlinie\",1310)";
mostCurrent._lvebene2.AddTwoLines2("Praktische Eigensicherung","lt. Wachschutzrichtlinie",(Object)(1310));
 //BA.debugLineNum = 1262;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gesetzeskunde\",\"Nach Wachschutzrichtlinie\",1400)";
mostCurrent._lvebene2.AddTwoLines2("Gesetzeskunde","Nach Wachschutzrichtlinie",(Object)(1400));
 //BA.debugLineNum = 1264;BA.debugLine="lvEbene2.AddTwoLines2 (\"Komplexe Leistungsdiagnostik\",\"Wissenschaftlich\",1360)";
mostCurrent._lvebene2.AddTwoLines2("Komplexe Leistungsdiagnostik","Wissenschaftlich",(Object)(1360));
 //BA.debugLineNum = 1265;BA.debugLine="lvEbene2.AddTwoLines2 (\"Trainingslehre\",\"wissenschaftlich\",1370)";
mostCurrent._lvebene2.AddTwoLines2("Trainingslehre","wissenschaftlich",(Object)(1370));
 //BA.debugLineNum = 1266;BA.debugLine="lvEbene2.AddTwoLines2 (\"Krafttraining\",\"\",1380)";
mostCurrent._lvebene2.AddTwoLines2("Krafttraining","",(Object)(1380));
 //BA.debugLineNum = 1267;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ideomotorisches Training\",\"Nutze die Macht der Gedanken\", 1390)";
mostCurrent._lvebene2.AddTwoLines2("Ideomotorisches Training","Nutze die Macht der Gedanken",(Object)(1390));
 //BA.debugLineNum = 1268;BA.debugLine="lvEbene2.AddTwoLines2 (\"Mixed Martial Art\",\"\",1320)";
mostCurrent._lvebene2.AddTwoLines2("Mixed Martial Art","",(Object)(1320));
 //BA.debugLineNum = 1269;BA.debugLine="lvEbene2.AddTwoLines2 (\"Clubkalkulator\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Clubkalkulator","",(Object)(700));
 break;
case 2:
 //BA.debugLineNum = 1274;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 1275;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1276;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1278;BA.debugLine="lvEbene2.AddSingleLine2 (\"Bodybuilding (Stunde)\",0)";
mostCurrent._lvebene2.AddSingleLine2("Bodybuilding (Stunde)",(Object)(0));
 //BA.debugLineNum = 1279;BA.debugLine="lvEbene2.AddTwoLines2 (\"Bodybuilding \",\"Vollständiges Training\", 5000)";
mostCurrent._lvebene2.AddTwoLines2("Bodybuilding ","Vollständiges Training",(Object)(5000));
 //BA.debugLineNum = 1280;BA.debugLine="lvEbene2.AddTwoLines2 (\"Beine, Waden, Unterarme \",\"Split 1\", 5010)";
mostCurrent._lvebene2.AddTwoLines2("Beine, Waden, Unterarme ","Split 1",(Object)(5010));
 //BA.debugLineNum = 1281;BA.debugLine="lvEbene2.AddTwoLines2 (\"Brust, Trizeps, Schultern \",\"Split 2\", 5020)";
mostCurrent._lvebene2.AddTwoLines2("Brust, Trizeps, Schultern ","Split 2",(Object)(5020));
 //BA.debugLineNum = 1282;BA.debugLine="lvEbene2.AddTwoLines2 (\"Rücken, Bauch, Biezeps \",\"Split 3\", 5030)";
mostCurrent._lvebene2.AddTwoLines2("Rücken, Bauch, Biezeps ","Split 3",(Object)(5030));
 //BA.debugLineNum = 1284;BA.debugLine="lvEbene2.AddSingleLine2 (\"Bodybuilding (Speziell)\",0)";
mostCurrent._lvebene2.AddSingleLine2("Bodybuilding (Speziell)",(Object)(0));
 //BA.debugLineNum = 1285;BA.debugLine="lvEbene2.AddTwoLines2 (\"Erwärmung (Allgemein)\",\"\",5040)";
mostCurrent._lvebene2.AddTwoLines2("Erwärmung (Allgemein)","",(Object)(5040));
 //BA.debugLineNum = 1286;BA.debugLine="lvEbene2.AddTwoLines2 (\"Erwärmung (Speziell)\",\"\", 5050 )";
mostCurrent._lvebene2.AddTwoLines2("Erwärmung (Speziell)","",(Object)(5050));
 //BA.debugLineNum = 1287;BA.debugLine="lvEbene2.AddTwoLines2 (\"Trizeps \",\"\", 5060 )";
mostCurrent._lvebene2.AddTwoLines2("Trizeps ","",(Object)(5060));
 //BA.debugLineNum = 1288;BA.debugLine="lvEbene2.AddTwoLines2 (\"Bizeps \",\"\", 5070)";
mostCurrent._lvebene2.AddTwoLines2("Bizeps ","",(Object)(5070));
 //BA.debugLineNum = 1289;BA.debugLine="lvEbene2.AddTwoLines2 (\"Schulter \",\"\", 5080 )";
mostCurrent._lvebene2.AddTwoLines2("Schulter ","",(Object)(5080));
 //BA.debugLineNum = 1290;BA.debugLine="lvEbene2.AddTwoLines2 (\"Brust \",\"\",5090)";
mostCurrent._lvebene2.AddTwoLines2("Brust ","",(Object)(5090));
 //BA.debugLineNum = 1291;BA.debugLine="lvEbene2.AddTwoLines2 (\"Rücken \",\"\",5100)";
mostCurrent._lvebene2.AddTwoLines2("Rücken ","",(Object)(5100));
 //BA.debugLineNum = 1292;BA.debugLine="lvEbene2.AddTwoLines2 (\"Unterarme \",\"\",5110)";
mostCurrent._lvebene2.AddTwoLines2("Unterarme ","",(Object)(5110));
 //BA.debugLineNum = 1293;BA.debugLine="lvEbene2.AddTwoLines2 (\"Bauch \",\"\", 5120 )";
mostCurrent._lvebene2.AddTwoLines2("Bauch ","",(Object)(5120));
 //BA.debugLineNum = 1294;BA.debugLine="lvEbene2.AddTwoLines2 (\"Po \",\"\", 5130 )";
mostCurrent._lvebene2.AddTwoLines2("Po ","",(Object)(5130));
 //BA.debugLineNum = 1295;BA.debugLine="lvEbene2.AddTwoLines2 (\"Oberschenkel \",\"\", 5140)";
mostCurrent._lvebene2.AddTwoLines2("Oberschenkel ","",(Object)(5140));
 //BA.debugLineNum = 1296;BA.debugLine="lvEbene2.AddTwoLines2 (\"Unterschenkel \",\"\", 5150 )";
mostCurrent._lvebene2.AddTwoLines2("Unterschenkel ","",(Object)(5150));
 //BA.debugLineNum = 1297;BA.debugLine="lvEbene2.AddTwoLines2 (\"Cardio \",\"\",5160)";
mostCurrent._lvebene2.AddTwoLines2("Cardio ","",(Object)(5160));
 //BA.debugLineNum = 1299;BA.debugLine="lvEbene2.AddSingleLine2 (\"Extras\",100)";
mostCurrent._lvebene2.AddSingleLine2("Extras",(Object)(100));
 //BA.debugLineNum = 1301;BA.debugLine="lvEbene2.AddTwoLines2 (\"Wettkampf-Timer\",\"\",1900)";
mostCurrent._lvebene2.AddTwoLines2("Wettkampf-Timer","",(Object)(1900));
 break;
case 3:
 break;
case 4:
 //BA.debugLineNum = 1316;BA.debugLine="StartActivity(EinstellungenAH)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungenah.getObject()));
 break;
case 5:
 //BA.debugLineNum = 1320;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._facebook.getObject()));
 break;
case 6:
 //BA.debugLineNum = 1324;BA.debugLine="AktuellerUnterordner =  Unterordner16";
_aktuellerunterordner = _unterordner16;
 //BA.debugLineNum = 1325;BA.debugLine="Ueberschrift = \"Nunchaku\"";
_ueberschrift = "Nunchaku";
 //BA.debugLineNum = 1326;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 7:
 //BA.debugLineNum = 1330;BA.debugLine="StartActivity(ErfolgsMeldung)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._erfolgsmeldung.getObject()));
 break;
case 8:
 //BA.debugLineNum = 1339;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 1341;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1342;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1343;BA.debugLine="lvEbene2.AddSingleLine2 (\"Lexikon Allgemein\",0)";
mostCurrent._lvebene2.AddSingleLine2("Lexikon Allgemein",(Object)(0));
 //BA.debugLineNum = 1344;BA.debugLine="lvEbene2.AddTwoLines2 (\"Sportlexikon\",\"\", 0)";
mostCurrent._lvebene2.AddTwoLines2("Sportlexikon","",(Object)(0));
 //BA.debugLineNum = 1345;BA.debugLine="lvEbene2.AddSingleLine2 (\"Lexikon Speziell\",0)";
mostCurrent._lvebene2.AddSingleLine2("Lexikon Speziell",(Object)(0));
 //BA.debugLineNum = 1346;BA.debugLine="lvEbene2.AddTwoLines2 (\"Karate\",\"\", 0)";
mostCurrent._lvebene2.AddTwoLines2("Karate","",(Object)(0));
 //BA.debugLineNum = 1347;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kickboxen\",\"\",0)";
mostCurrent._lvebene2.AddTwoLines2("Kickboxen","",(Object)(0));
 //BA.debugLineNum = 1348;BA.debugLine="lvEbene2.AddTwoLines2 (\"Tae kwon do\",\"\", 0 )";
mostCurrent._lvebene2.AddTwoLines2("Tae kwon do","",(Object)(0));
 //BA.debugLineNum = 1349;BA.debugLine="lvEbene2.AddTwoLines2 (\"Nunchaku\",\"\",0)";
mostCurrent._lvebene2.AddTwoLines2("Nunchaku","",(Object)(0));
 //BA.debugLineNum = 1353;BA.debugLine="StartActivity(Kampfsportlexikon)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._kampfsportlexikon.getObject()));
 break;
case 9:
 //BA.debugLineNum = 1359;BA.debugLine="ProgressDialogShow(\"Lebensmitteldatenbank laden ...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Lebensmitteldatenbank laden ...");
 //BA.debugLineNum = 1360;BA.debugLine="StartActivity(Lebensmittel)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._lebensmittel.getObject()));
 break;
case 10:
 //BA.debugLineNum = 1362;BA.debugLine="ProgressDialogShow(\"Tagebuch laden ...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Tagebuch laden ...");
 //BA.debugLineNum = 1363;BA.debugLine="StartActivity(Tagebuch)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._tagebuch.getObject()));
 break;
case 11:
 break;
case 12:
 break;
case 13:
 break;
case 14:
 break;
case 15:
 break;
case 16:
 break;
case 17:
 break;
case 18:
 break;
case 19:
 break;
case 20:
 break;
case 21:
 break;
case 22:
 break;
case 23:
 break;
case 24:
 break;
case 25:
 break;
case 26:
 break;
case 27:
 break;
case 28:
 break;
case 29:
 //BA.debugLineNum = 1386;BA.debugLine="Ebene2 = \"Training KODOKAN\"";
mostCurrent._ebene2 = "Training KODOKAN";
 //BA.debugLineNum = 1387;BA.debugLine="lvEbene1.Clear";
mostCurrent._lvebene1.Clear();
 //BA.debugLineNum = 1389;BA.debugLine="lvEbene1.AddSingleLine2 (\"Training KODOKAN\",1)";
mostCurrent._lvebene1.AddSingleLine2("Training KODOKAN",(Object)(1));
 //BA.debugLineNum = 1390;BA.debugLine="lvEbene1.AddTwoLines2 (\"Start\",\"\",1 )";
mostCurrent._lvebene1.AddTwoLines2("Start","",(Object)(1));
 //BA.debugLineNum = 1392;BA.debugLine="If Businessversion Then";
if (_businessversion) { 
 //BA.debugLineNum = 1393;BA.debugLine="lvEbene1.AddSingleLine2 (\"Businessversion\",3000)";
mostCurrent._lvebene1.AddSingleLine2("Businessversion",(Object)(3000));
 }else {
 //BA.debugLineNum = 1395;BA.debugLine="lvEbene1.AddSingleLine2 (\"Basisversion\",3000)";
mostCurrent._lvebene1.AddSingleLine2("Basisversion",(Object)(3000));
 };
 //BA.debugLineNum = 1397;BA.debugLine="lvEbene1.AddTwoLines2 (\"Personendatenbank\",\"\", 3200)";
mostCurrent._lvebene1.AddTwoLines2("Personendatenbank","",(Object)(3200));
 //BA.debugLineNum = 1398;BA.debugLine="lvEbene1.AddTwoLines2 (\"Pläne\",\"\", 3300 )";
mostCurrent._lvebene1.AddTwoLines2("Pläne","",(Object)(3300));
 //BA.debugLineNum = 1399;BA.debugLine="lvEbene1.AddTwoLines2 (\"Shop\",\"\", 3400 )";
mostCurrent._lvebene1.AddTwoLines2("Shop","",(Object)(3400));
 //BA.debugLineNum = 1400;BA.debugLine="lvEbene1.AddTwoLines2 (\"Statistik\",\"\", 3500)";
mostCurrent._lvebene1.AddTwoLines2("Statistik","",(Object)(3500));
 //BA.debugLineNum = 1401;BA.debugLine="lvEbene1.AddtwoLines2 (\"Tauschring\",\"\",3600)";
mostCurrent._lvebene1.AddTwoLines2("Tauschring","",(Object)(3600));
 //BA.debugLineNum = 1402;BA.debugLine="lvEbene1.AddTwoLines2 (\"Training\",\"\",3700)";
mostCurrent._lvebene1.AddTwoLines2("Training","",(Object)(3700));
 //BA.debugLineNum = 1403;BA.debugLine="lvEbene1.AddTwoLines2 (\"Trainingstagebuch\",\"\", 3800 )";
mostCurrent._lvebene1.AddTwoLines2("Trainingstagebuch","",(Object)(3800));
 //BA.debugLineNum = 1405;BA.debugLine="If Businessversion Then";
if (_businessversion) { 
 //BA.debugLineNum = 1406;BA.debugLine="lvEbene1.AddTwoLines2 (\"1. Hilfe\",\"\",3900)";
mostCurrent._lvebene1.AddTwoLines2("1. Hilfe","",(Object)(3900));
 //BA.debugLineNum = 1407;BA.debugLine="lvEbene1.AddTwoLines2 (\"Chef\",\"\",4000 )";
mostCurrent._lvebene1.AddTwoLines2("Chef","",(Object)(4000));
 //BA.debugLineNum = 1408;BA.debugLine="lvEbene1.AddTwoLines2 (\"Leistungsdiagnostik\",\"\", 4100)";
mostCurrent._lvebene1.AddTwoLines2("Leistungsdiagnostik","",(Object)(4100));
 //BA.debugLineNum = 1409;BA.debugLine="lvEbene1.AddTwoLines2 (\"Ordnung, Recht und Gesetz\",\"\",4200)";
mostCurrent._lvebene1.AddTwoLines2("Ordnung, Recht und Gesetz","",(Object)(4200));
 };
 //BA.debugLineNum = 1412;BA.debugLine="lvEbene1.AddSingleLine2 (\"Einstellungen\",3000)";
mostCurrent._lvebene1.AddSingleLine2("Einstellungen",(Object)(3000));
 //BA.debugLineNum = 1413;BA.debugLine="lvEbene1.AddTwoLines2 (\"Einstellungen App\",\"\",4300)";
mostCurrent._lvebene1.AddTwoLines2("Einstellungen App","",(Object)(4300));
 //BA.debugLineNum = 1414;BA.debugLine="lvEbene1.AddTwoLines2 (\"Einstellungen Training\",\"\",4400 )";
mostCurrent._lvebene1.AddTwoLines2("Einstellungen Training","",(Object)(4400));
 //BA.debugLineNum = 1416;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 break;
case 30:
 break;
case 31:
 //BA.debugLineNum = 1424;BA.debugLine="Ebene2 = \"Pläne\"";
mostCurrent._ebene2 = "Pläne";
 //BA.debugLineNum = 1425;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1428;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ferien\",\"\",3001)";
mostCurrent._lvebene2.AddTwoLines2("Ferien","",(Object)(3001));
 //BA.debugLineNum = 1429;BA.debugLine="lvEbene2.AddTwoLines2 (\"Leistungsprognose\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Leistungsprognose","",(Object)(3001));
 //BA.debugLineNum = 1430;BA.debugLine="lvEbene2.AddTwoLines2 (\"Wettkampfplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Wettkampfplan","",(Object)(3001));
 //BA.debugLineNum = 1431;BA.debugLine="lvEbene2.AddTwoLines2 (\"Wochenplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Wochenplan","",(Object)(3001));
 //BA.debugLineNum = 1432;BA.debugLine="lvEbene2.AddTwoLines2 (\"Monatsplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Monatsplan","",(Object)(3001));
 //BA.debugLineNum = 1433;BA.debugLine="lvEbene2.AddTwoLines2 (\"Jahresplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Jahresplan","",(Object)(3001));
 //BA.debugLineNum = 1434;BA.debugLine="lvEbene2.AddTwoLines2 (\"4 Jahresplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("4 Jahresplan","",(Object)(3001));
 //BA.debugLineNum = 1435;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 break;
case 32:
 break;
case 33:
 break;
case 34:
 break;
case 35:
 //BA.debugLineNum = 1449;BA.debugLine="Ebene2 = \"Tauschring\"";
mostCurrent._ebene2 = "Tauschring";
 //BA.debugLineNum = 1450;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1451;BA.debugLine="lvEbene2.AddSingleLine2 (\"Werbung1\",3601)";
mostCurrent._lvebene2.AddSingleLine2("Werbung1",(Object)(3601));
 //BA.debugLineNum = 1453;BA.debugLine="lvEbene2.AddTwoLines2 (\"Mein Tauschring\",\"\",3601)";
mostCurrent._lvebene2.AddTwoLines2("Mein Tauschring","",(Object)(3601));
 //BA.debugLineNum = 1455;BA.debugLine="lvEbene2.AddTwoLines2 (\"Konto\",\"\",3601 )";
mostCurrent._lvebene2.AddTwoLines2("Konto","",(Object)(3601));
 //BA.debugLineNum = 1457;BA.debugLine="lvEbene2.AddTwoLines2 (\"Verwaltung\",\"\",3601 )";
mostCurrent._lvebene2.AddTwoLines2("Verwaltung","",(Object)(3601));
 //BA.debugLineNum = 1459;BA.debugLine="lvEbene2.AddTwoLines2 (\"Hilfe\",\"\",3601 )";
mostCurrent._lvebene2.AddTwoLines2("Hilfe","",(Object)(3601));
 //BA.debugLineNum = 1461;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 break;
case 36:
 break;
case 37:
 //BA.debugLineNum = 1468;BA.debugLine="Ebene2 = \"Training Karate\"";
mostCurrent._ebene2 = "Training Karate";
 //BA.debugLineNum = 1469;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1472;BA.debugLine="lvEbene2.AddTwoLines2 (\"1 Person\",\"\",3701)";
mostCurrent._lvebene2.AddTwoLines2("1 Person","",(Object)(3701));
 //BA.debugLineNum = 1473;BA.debugLine="lvEbene2.AddTwoLines2 (\"2 Personen\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("2 Personen","",(Object)(3701));
 //BA.debugLineNum = 1474;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppe gemischt\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Gruppe gemischt","",(Object)(3701));
 //BA.debugLineNum = 1475;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppe homogen\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Gruppe homogen","",(Object)(3701));
 //BA.debugLineNum = 1476;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppe 1\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Gruppe 1","",(Object)(3701));
 //BA.debugLineNum = 1478;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppe 2\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Gruppe 2","",(Object)(3701));
 //BA.debugLineNum = 1479;BA.debugLine="lvEbene2.AddTwoLines2 (\"Frontalunterricht\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Frontalunterricht","",(Object)(3701));
 //BA.debugLineNum = 1480;BA.debugLine="lvEbene2.AddTwoLines2 (\"Freies Training\",\"\",3701)";
mostCurrent._lvebene2.AddTwoLines2("Freies Training","",(Object)(3701));
 //BA.debugLineNum = 1481;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kinder bis 6 Jahre\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Kinder bis 6 Jahre","",(Object)(3701));
 //BA.debugLineNum = 1482;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kinder 6-10 Jahre\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Kinder 6-10 Jahre","",(Object)(3701));
 //BA.debugLineNum = 1484;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 break;
case 38:
 //BA.debugLineNum = 1487;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 39:
 break;
case 40:
 break;
case 41:
 break;
case 42:
 break;
case 43:
 break;
case 44:
 //BA.debugLineNum = 1506;BA.debugLine="StartActivity(EinstellungenApp)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungenapp.getObject()));
 break;
case 45:
 //BA.debugLineNum = 1512;BA.debugLine="StartActivity(Startabfragen)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._startabfragen.getObject()));
 break;
case 46:
 //BA.debugLineNum = 1516;BA.debugLine="StartActivity(EinstellungenApp)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungenapp.getObject()));
 break;
case 47:
 break;
case 48:
 //BA.debugLineNum = 1524;BA.debugLine="StartActivity(Startabfragen)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._startabfragen.getObject()));
 break;
case 49:
 //BA.debugLineNum = 1528;BA.debugLine="StartActivity(Startabfragen)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._startabfragen.getObject()));
 break;
case 50:
 break;
case 51:
 break;
case 52:
 break;
case 53:
 break;
case 54:
 //BA.debugLineNum = 1545;BA.debugLine="Ebene2 = \"Einstellungen 1\"";
mostCurrent._ebene2 = "Einstellungen 1";
 //BA.debugLineNum = 1546;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1549;BA.debugLine="lvEbene2.AddTwoLines2 (\"Trainer Aufgaben\",\"\",4401)";
mostCurrent._lvebene2.AddTwoLines2("Trainer Aufgaben","",(Object)(4401));
 //BA.debugLineNum = 1550;BA.debugLine="lvEbene2.AddTwoLines2 (\"Intensität\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Intensität","",(Object)(4401));
 //BA.debugLineNum = 1551;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kommunikation\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Kommunikation","",(Object)(4401));
 //BA.debugLineNum = 1552;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ort\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Ort","",(Object)(4401));
 //BA.debugLineNum = 1553;BA.debugLine="lvEbene2.AddTwoLines2 (\"Reize\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Reize","",(Object)(4401));
 //BA.debugLineNum = 1555;BA.debugLine="lvEbene2.AddTwoLines2 (\"Sportart\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Sportart","",(Object)(4401));
 //BA.debugLineNum = 1556;BA.debugLine="lvEbene2.AddTwoLines2 (\"Voraussetzungen\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Voraussetzungen","",(Object)(4401));
 //BA.debugLineNum = 1557;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ziele\",\"\",4401)";
mostCurrent._lvebene2.AddTwoLines2("Ziele","",(Object)(4401));
 //BA.debugLineNum = 1558;BA.debugLine="lvEbene2.AddTwoLines2 (\"Zeit\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Zeit","",(Object)(4401));
 //BA.debugLineNum = 1560;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 break;
case 55:
 //BA.debugLineNum = 1563;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 56:
 //BA.debugLineNum = 1567;BA.debugLine="Ebene2 = \"Listen\"";
mostCurrent._ebene2 = "Listen";
 //BA.debugLineNum = 1569;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1571;BA.debugLine="lvEbene2.AddSingleLine2 (\"Sonstiges\",4500)";
mostCurrent._lvebene2.AddSingleLine2("Sonstiges",(Object)(4500));
 //BA.debugLineNum = 1572;BA.debugLine="lvEbene2.AddTwoLines2 (\"Wie finde ich den richtigen Club?\",\"Checkliste\",4501)";
mostCurrent._lvebene2.AddTwoLines2("Wie finde ich den richtigen Club?","Checkliste",(Object)(4501));
 //BA.debugLineNum = 1573;BA.debugLine="lvEbene2.AddTwoLines2 (\"Einhornliste\",\"Fragen über den Wettkampfgegner\",4502)";
mostCurrent._lvebene2.AddTwoLines2("Einhornliste","Fragen über den Wettkampfgegner",(Object)(4502));
 //BA.debugLineNum = 1574;BA.debugLine="lvEbene2.AddTwoLines2 (\"Welches ist der richtige Sport für mich?\",\"Auswahlliste\",4503 )";
mostCurrent._lvebene2.AddTwoLines2("Welches ist der richtige Sport für mich?","Auswahlliste",(Object)(4503));
 //BA.debugLineNum = 1575;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ausrichtung Meisterschaft\",\"Checkliste\",4504 )";
mostCurrent._lvebene2.AddTwoLines2("Ausrichtung Meisterschaft","Checkliste",(Object)(4504));
 //BA.debugLineNum = 1576;BA.debugLine="lvEbene2.AddTwoLines2 (\"Marketing\",\"Chekliste\",4505 )";
mostCurrent._lvebene2.AddTwoLines2("Marketing","Chekliste",(Object)(4505));
 //BA.debugLineNum = 1578;BA.debugLine="lvEbene2.AddTwoLines2 (\"\",\"\",4506 )";
mostCurrent._lvebene2.AddTwoLines2("","",(Object)(4506));
 //BA.debugLineNum = 1579;BA.debugLine="lvEbene2.AddTwoLines2 (\"\",\"\",4507 )";
mostCurrent._lvebene2.AddTwoLines2("","",(Object)(4507));
 //BA.debugLineNum = 1580;BA.debugLine="lvEbene2.AddSingleLine2 (\"\",4508)";
mostCurrent._lvebene2.AddSingleLine2("",(Object)(4508));
 //BA.debugLineNum = 1581;BA.debugLine="lvEbene2.AddTwoLines2 (\"\",\"\",4509)";
mostCurrent._lvebene2.AddTwoLines2("","",(Object)(4509));
 //BA.debugLineNum = 1582;BA.debugLine="lvEbene2.AddTwoLines2 (\"\",\"\",4510 )";
mostCurrent._lvebene2.AddTwoLines2("","",(Object)(4510));
 //BA.debugLineNum = 1583;BA.debugLine="pager.GotoPage(2, True)";
mostCurrent._pager.GotoPage((int) (2),anywheresoftware.b4a.keywords.Common.True);
 break;
default:
 //BA.debugLineNum = 1591;BA.debugLine="Msgbox(\"in lvEbene1_itemclick\",\"F e h l e r \")";
anywheresoftware.b4a.keywords.Common.Msgbox("in lvEbene1_itemclick","F e h l e r ",mostCurrent.activityBA);
 break;
}
;
 //BA.debugLineNum = 1596;BA.debugLine="End Sub";
return "";
}
public static String  _lvebene2_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1599;BA.debugLine="Sub lvEbene2_Itemclick (Position As Int, Value As Object)";
 //BA.debugLineNum = 1604;BA.debugLine="lvEbene2.FastScrollEnabled = True 'schnellscrollgriff";
mostCurrent._lvebene2.setFastScrollEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1605;BA.debugLine="lvEbene2.SingleLineLayout.Label.Gravity = Gravity.CENTER";
mostCurrent._lvebene2.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1606;BA.debugLine="lvEbene2.SingleLineLayout.Label.TextSize = 20";
mostCurrent._lvebene2.getSingleLineLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 1607;BA.debugLine="lvEbene2.SingleLineLayout.ItemHeight = 35dip";
mostCurrent._lvebene2.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35)));
 //BA.debugLineNum = 1608;BA.debugLine="lvEbene2.SingleLineLayout.Label.TextColor = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene2.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 //BA.debugLineNum = 1609;BA.debugLine="lvEbene2.SingleLineLayout.Label.Color = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._lvebene2.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 //BA.debugLineNum = 1610;BA.debugLine="lvEbene2.SingleLineLayout.Label.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._lvebene2.getSingleLineLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 1620;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)(0),(Object)(1),(Object)(2),(Object)(100),(Object)(200),(Object)(300),(Object)(400),(Object)(500),(Object)(600),(Object)(700),(Object)(800),(Object)(900),(Object)(1000),(Object)(1010),(Object)(1100),(Object)(1200),(Object)(1300),(Object)(1400),(Object)(1500),(Object)(1600),(Object)(1700),(Object)(1800),(Object)(1900),(Object)(2000),(Object)(4000),(Object)(3200),(Object)(3300),(Object)(3301),(Object)(3400),(Object)(3500),(Object)(3600),(Object)(3601),(Object)(3700),(Object)(3701),(Object)(3800),(Object)(3900),(Object)(4000),(Object)(4100),(Object)(4200),(Object)(4300),(Object)(4301),(Object)(4302),(Object)(4303),(Object)(4304),(Object)(4305),(Object)(4306),(Object)(4307),(Object)(4308),(Object)(4309),(Object)(4400),(Object)(4401),(Object)(4500),(Object)(4501),(Object)(4502),(Object)(4503),(Object)(4504),(Object)(4505),(Object)(4506),(Object)(4507),(Object)(4508),(Object)(4509),(Object)(4510),(Object)(5000),(Object)(5010))) {
case 0:
 break;
case 1:
 //BA.debugLineNum = 1631;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1633;BA.debugLine="lvEbene2.AddTwoLines2 (\"01. Erwärmung\",\"Ohne Pause\",100)";
mostCurrent._lvebene2.AddTwoLines2("01. Erwärmung","Ohne Pause",(Object)(100));
 //BA.debugLineNum = 1634;BA.debugLine="lvEbene2.AddTwoLines2 (\"02. Erwärmung Kickboxen\",\"Ohne Pause\", 200 )";
mostCurrent._lvebene2.AddTwoLines2("02. Erwärmung Kickboxen","Ohne Pause",(Object)(200));
 //BA.debugLineNum = 1635;BA.debugLine="lvEbene2.AddTwoLines2 (\"03. Dehnung \",\"Ohne Pause\",1800)";
mostCurrent._lvebene2.AddTwoLines2("03. Dehnung ","Ohne Pause",(Object)(1800));
 //BA.debugLineNum = 1636;BA.debugLine="lvEbene2.AddTwoLines2 (\"04. BodyWeightExercices\",\"Ohne Pause\", 300 )";
mostCurrent._lvebene2.AddTwoLines2("04. BodyWeightExercices","Ohne Pause",(Object)(300));
 //BA.debugLineNum = 1637;BA.debugLine="lvEbene2.AddTwoLines2 (\"05. Anti Terror Straßenkampf\",\"\", 400 )";
mostCurrent._lvebene2.AddTwoLines2("05. Anti Terror Straßenkampf","",(Object)(400));
 //BA.debugLineNum = 1638;BA.debugLine="lvEbene2.AddTwoLines2 (\"06.Chi Gung\",\"\", 00 )";
mostCurrent._lvebene2.AddTwoLines2("06.Chi Gung","",(Object)(00));
 //BA.debugLineNum = 1639;BA.debugLine="lvEbene2.AddTwoLines2 (\"06.Frauenselbstverteidigung / SV\",\"\",500)";
mostCurrent._lvebene2.AddTwoLines2("06.Frauenselbstverteidigung / SV","",(Object)(500));
 //BA.debugLineNum = 1640;BA.debugLine="lvEbene2.AddTwoLines2 (\"08.Ideomotorisches Training\",\"\", 600)";
mostCurrent._lvebene2.AddTwoLines2("08.Ideomotorisches Training","",(Object)(600));
 //BA.debugLineNum = 1641;BA.debugLine="lvEbene2.AddTwoLines2 (\"08. Karate\",\"\", 700)";
mostCurrent._lvebene2.AddTwoLines2("08. Karate","",(Object)(700));
 //BA.debugLineNum = 1642;BA.debugLine="lvEbene2.AddTwoLines2 (\"09. Kickboxen (Partnerübungen\",\"Ohne Pause\", 800)";
mostCurrent._lvebene2.AddTwoLines2("09. Kickboxen (Partnerübungen","Ohne Pause",(Object)(800));
 //BA.debugLineNum = 1643;BA.debugLine="lvEbene2.AddTwoLines2 (\"09.Ninjutsu\",\"\",900)";
mostCurrent._lvebene2.AddTwoLines2("09.Ninjutsu","",(Object)(900));
 //BA.debugLineNum = 1644;BA.debugLine="lvEbene2.AddTwoLines2 (\"10.Nunchaku-Do\",\"\",1000)";
mostCurrent._lvebene2.AddTwoLines2("10.Nunchaku-Do","",(Object)(1000));
 //BA.debugLineNum = 1645;BA.debugLine="lvEbene2.AddTwoLines2 (\"11.Survival - Überlebenstraining\",\"\",1100)";
mostCurrent._lvebene2.AddTwoLines2("11.Survival - Überlebenstraining","",(Object)(1100));
 //BA.debugLineNum = 1646;BA.debugLine="lvEbene2.AddTwoLines2 (\"12.Taekwondo\",\"\",1200 )";
mostCurrent._lvebene2.AddTwoLines2("12.Taekwondo","",(Object)(1200));
 //BA.debugLineNum = 1647;BA.debugLine="lvEbene2.AddTwoLines2 (\"13.Tai Chi\",\"\",1300)";
mostCurrent._lvebene2.AddTwoLines2("13.Tai Chi","",(Object)(1300));
 //BA.debugLineNum = 1648;BA.debugLine="lvEbene2.AddTwoLines2 (\"14. Tensegrity\",\"\",1400 )";
mostCurrent._lvebene2.AddTwoLines2("14. Tensegrity","",(Object)(1400));
 //BA.debugLineNum = 1649;BA.debugLine="lvEbene2.AddTwoLines2 (\"15. Krafttraining\",\"\",1500)";
mostCurrent._lvebene2.AddTwoLines2("15. Krafttraining","",(Object)(1500));
 //BA.debugLineNum = 1650;BA.debugLine="lvEbene2.AddTwoLines2 (\"16.Training Ausgeglichenheit\",\"\",1600)";
mostCurrent._lvebene2.AddTwoLines2("16.Training Ausgeglichenheit","",(Object)(1600));
 //BA.debugLineNum = 1651;BA.debugLine="lvEbene2.AddTwoLines2 (\"17. Cool Down\",\"\",1700)";
mostCurrent._lvebene2.AddTwoLines2("17. Cool Down","",(Object)(1700));
 //BA.debugLineNum = 1652;BA.debugLine="lvEbene2.AddTwoLines2 (\"18. Kampfsport-Spiele\",\"\",1800)";
mostCurrent._lvebene2.AddTwoLines2("18. Kampfsport-Spiele","",(Object)(1800));
 //BA.debugLineNum = 1653;BA.debugLine="lvEbene2.AddTwoLines2 (\"19. SportClubKalkulator\",\"\",1900)";
mostCurrent._lvebene2.AddTwoLines2("19. SportClubKalkulator","",(Object)(1900));
 break;
case 2:
 //BA.debugLineNum = 1657;BA.debugLine="StartActivity(KarateStunde)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._karatestunde.getObject()));
 break;
case 3:
 //BA.debugLineNum = 1662;BA.debugLine="AktuellerUnterordner =  Unterordner5";
_aktuellerunterordner = _unterordner5;
 //BA.debugLineNum = 1663;BA.debugLine="Ueberschrift = \"Erwärmung (Allg.)\"";
_ueberschrift = "Erwärmung (Allg.)";
 //BA.debugLineNum = 1664;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 4:
 //BA.debugLineNum = 1668;BA.debugLine="AktuellerUnterordner =  Unterordner12";
_aktuellerunterordner = _unterordner12;
 //BA.debugLineNum = 1669;BA.debugLine="Ueberschrift = \"Erwärmung (Spez.)\"";
_ueberschrift = "Erwärmung (Spez.)";
 //BA.debugLineNum = 1670;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 5:
 //BA.debugLineNum = 1674;BA.debugLine="AktuellerUnterordner =  Unterordner13";
_aktuellerunterordner = _unterordner13;
 //BA.debugLineNum = 1675;BA.debugLine="Ueberschrift = \"Kickboxen (Einzel)\"";
_ueberschrift = "Kickboxen (Einzel)";
 //BA.debugLineNum = 1676;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 6:
 //BA.debugLineNum = 1680;BA.debugLine="AktuellerUnterordner =  Unterordner14";
_aktuellerunterordner = _unterordner14;
 //BA.debugLineNum = 1681;BA.debugLine="Ueberschrift = \"Kickboxen (Partner)\"";
_ueberschrift = "Kickboxen (Partner)";
 //BA.debugLineNum = 1682;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 7:
 //BA.debugLineNum = 1689;BA.debugLine="AktuellerUnterordner =  Unterordner2";
_aktuellerunterordner = _unterordner2;
 //BA.debugLineNum = 1690;BA.debugLine="Ueberschrift = \"BodyWeightExercices\"";
_ueberschrift = "BodyWeightExercices";
 //BA.debugLineNum = 1691;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 8:
 break;
case 9:
 //BA.debugLineNum = 1697;BA.debugLine="Ebene2 = \"Training KARATE\"";
mostCurrent._ebene2 = "Training KARATE";
 //BA.debugLineNum = 1699;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1700;BA.debugLine="lvEbene2.AddTwoLines2 (\"Erwärmung\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Erwärmung","",(Object)(700));
 //BA.debugLineNum = 1701;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppenzusammenstellung\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Gruppenzusammenstellung","",(Object)(700));
 //BA.debugLineNum = 1702;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gemischt\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Gemischt","",(Object)(700));
 //BA.debugLineNum = 1703;BA.debugLine="lvEbene2.AddTwoLines2 (\"Grundkurs\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Grundkurs","",(Object)(700));
 //BA.debugLineNum = 1704;BA.debugLine="lvEbene2.AddTwoLines2 (\"10. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("10. Kyu","",(Object)(700));
 //BA.debugLineNum = 1705;BA.debugLine="lvEbene2.AddTwoLines2 (\"9. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("9. Kyu","",(Object)(700));
 //BA.debugLineNum = 1706;BA.debugLine="lvEbene2.AddTwoLines2 (\"8. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("8. Kyu","",(Object)(700));
 //BA.debugLineNum = 1707;BA.debugLine="lvEbene2.AddTwoLines2 (\"7. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("7. Kyu","",(Object)(700));
 //BA.debugLineNum = 1708;BA.debugLine="lvEbene2.AddTwoLines2 (\"6. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("6. Kyu","",(Object)(700));
 //BA.debugLineNum = 1709;BA.debugLine="lvEbene2.AddTwoLines2 (\"5. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("5. Kyu","",(Object)(700));
 //BA.debugLineNum = 1710;BA.debugLine="lvEbene2.AddTwoLines2 (\"4. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("4. Kyu","",(Object)(700));
 //BA.debugLineNum = 1711;BA.debugLine="lvEbene2.AddTwoLines2 (\"3. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("3. Kyu","",(Object)(700));
 //BA.debugLineNum = 1712;BA.debugLine="lvEbene2.AddTwoLines2 (\"2. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("2. Kyu","",(Object)(700));
 //BA.debugLineNum = 1713;BA.debugLine="lvEbene2.AddTwoLines2 (\"1. Kyu\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("1. Kyu","",(Object)(700));
 //BA.debugLineNum = 1714;BA.debugLine="lvEbene2.AddTwoLines2 (\"Meisterschüler\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Meisterschüler","",(Object)(700));
 //BA.debugLineNum = 1715;BA.debugLine="lvEbene2.AddTwoLines2 (\"Meister (1. Dan)\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Meister (1. Dan)","",(Object)(700));
 //BA.debugLineNum = 1716;BA.debugLine="lvEbene2.AddTwoLines2 (\"Meister (2. Dan)\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Meister (2. Dan)","",(Object)(700));
 //BA.debugLineNum = 1717;BA.debugLine="lvEbene2.AddTwoLines2 (\"Meister (3. Dan)\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Meister (3. Dan)","",(Object)(700));
 //BA.debugLineNum = 1718;BA.debugLine="lvEbene2.AddTwoLines2 (\"Dachi\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Dachi","",(Object)(700));
 //BA.debugLineNum = 1719;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kihon\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Kihon","",(Object)(700));
 //BA.debugLineNum = 1720;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kata\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Kata","",(Object)(700));
 //BA.debugLineNum = 1721;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kumite\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Kumite","",(Object)(700));
 //BA.debugLineNum = 1722;BA.debugLine="lvEbene2.AddTwoLines2 (\"Champions Club\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Champions Club","",(Object)(700));
 //BA.debugLineNum = 1723;BA.debugLine="lvEbene2.AddTwoLines2 (\"Black Belt Club\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Black Belt Club","",(Object)(700));
 //BA.debugLineNum = 1724;BA.debugLine="lvEbene2.AddTwoLines2 (\"Erwärmung\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Erwärmung","",(Object)(700));
 //BA.debugLineNum = 1725;BA.debugLine="lvEbene2.AddTwoLines2 (\"Clubkalkulator\",\"\",700)";
mostCurrent._lvebene2.AddTwoLines2("Clubkalkulator","",(Object)(700));
 //BA.debugLineNum = 1728;BA.debugLine="AktuellerUnterordner =  Unterordner11";
_aktuellerunterordner = _unterordner11;
 //BA.debugLineNum = 1729;BA.debugLine="Ueberschrift = \"Karate\"";
_ueberschrift = "Karate";
 //BA.debugLineNum = 1730;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 10:
 //BA.debugLineNum = 1734;BA.debugLine="AktuellerUnterordner =  Unterordner12";
_aktuellerunterordner = _unterordner12;
 //BA.debugLineNum = 1735;BA.debugLine="Ueberschrift = \"Kickboxen (Ganze Stunde)\"";
_ueberschrift = "Kickboxen (Ganze Stunde)";
 //BA.debugLineNum = 1736;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 11:
 break;
case 12:
 //BA.debugLineNum = 1743;BA.debugLine="StartActivity(KarateStunde)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._karatestunde.getObject()));
 break;
case 13:
 //BA.debugLineNum = 1749;BA.debugLine="AktuellerUnterordner =  Unterordner5";
_aktuellerunterordner = _unterordner5;
 //BA.debugLineNum = 1750;BA.debugLine="Ueberschrift = \"Erwärmung (Allg.)\"";
_ueberschrift = "Erwärmung (Allg.)";
 //BA.debugLineNum = 1751;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 14:
 break;
case 15:
 break;
case 16:
 break;
case 17:
 //BA.debugLineNum = 1758;BA.debugLine="AktuellerUnterordner =  Unterordner14";
_aktuellerunterordner = _unterordner14;
 //BA.debugLineNum = 1759;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 18:
 break;
case 19:
 //BA.debugLineNum = 1765;BA.debugLine="AktuellerUnterordner =  Unterordner3";
_aktuellerunterordner = _unterordner3;
 //BA.debugLineNum = 1766;BA.debugLine="Ueberschrift = \"Cool Down\"";
_ueberschrift = "Cool Down";
 //BA.debugLineNum = 1767;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 20:
 //BA.debugLineNum = 1770;BA.debugLine="Ueberschrift = \"Kickboxen Spiele\"";
_ueberschrift = "Kickboxen Spiele";
 //BA.debugLineNum = 1771;BA.debugLine="StartActivity(Spiele)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._spiele.getObject()));
 break;
case 21:
 //BA.debugLineNum = 1774;BA.debugLine="AktuellerUnterordner =  Unterordner4";
_aktuellerunterordner = _unterordner4;
 //BA.debugLineNum = 1775;BA.debugLine="Ueberschrift = \"Dehnung\"";
_ueberschrift = "Dehnung";
 //BA.debugLineNum = 1776;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 22:
 //BA.debugLineNum = 1781;BA.debugLine="StartActivity(EinstellungenWettkampf)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungenwettkampf.getObject()));
 break;
case 23:
 //BA.debugLineNum = 1786;BA.debugLine="StartActivity(EinstellungenTrainingStunde)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingstunde.getObject()));
 break;
case 24:
 //BA.debugLineNum = 1790;BA.debugLine="lvEbene1.Clear";
mostCurrent._lvebene1.Clear();
 //BA.debugLineNum = 1791;BA.debugLine="lvEbene1.AddSingleLine2 (\"Training Kickboxen\",1)";
mostCurrent._lvebene1.AddSingleLine2("Training Kickboxen",(Object)(1));
 //BA.debugLineNum = 1792;BA.debugLine="lvEbene1.AddTwoLines2 (\"Start\",\"\",1 )";
mostCurrent._lvebene1.AddTwoLines2("Start","",(Object)(1));
 //BA.debugLineNum = 1794;BA.debugLine="lvEbene1.AddSingleLine2 (\"Basisversion\",3000)";
mostCurrent._lvebene1.AddSingleLine2("Basisversion",(Object)(3000));
 //BA.debugLineNum = 1795;BA.debugLine="lvEbene1.AddTwoLines2 (\"Personendatenbank\",\"\", 3200)";
mostCurrent._lvebene1.AddTwoLines2("Personendatenbank","",(Object)(3200));
 //BA.debugLineNum = 1796;BA.debugLine="lvEbene1.AddTwoLines2 (\"Pläne\",\"\", 3300 )";
mostCurrent._lvebene1.AddTwoLines2("Pläne","",(Object)(3300));
 //BA.debugLineNum = 1797;BA.debugLine="lvEbene1.AddTwoLines2 (\"Shop\",\"\", 3400 )";
mostCurrent._lvebene1.AddTwoLines2("Shop","",(Object)(3400));
 //BA.debugLineNum = 1798;BA.debugLine="lvEbene1.AddTwoLines2 (\"Statistik\",\"\", 3500)";
mostCurrent._lvebene1.AddTwoLines2("Statistik","",(Object)(3500));
 //BA.debugLineNum = 1799;BA.debugLine="lvEbene1.AddtwoLines2 (\"Tauschring\",\"\",3600)";
mostCurrent._lvebene1.AddTwoLines2("Tauschring","",(Object)(3600));
 //BA.debugLineNum = 1800;BA.debugLine="lvEbene1.AddTwoLines2 (\"Training\",\"\",3700)";
mostCurrent._lvebene1.AddTwoLines2("Training","",(Object)(3700));
 //BA.debugLineNum = 1801;BA.debugLine="lvEbene1.AddTwoLines2 (\"Trainingstagebuch\",\"\", 3800 )";
mostCurrent._lvebene1.AddTwoLines2("Trainingstagebuch","",(Object)(3800));
 //BA.debugLineNum = 1803;BA.debugLine="lvEbene1.AddSingleLine2 (\"Businessversion\",3000)";
mostCurrent._lvebene1.AddSingleLine2("Businessversion",(Object)(3000));
 //BA.debugLineNum = 1804;BA.debugLine="lvEbene1.AddTwoLines2 (\"1. Hilfe\",\"\",3900)";
mostCurrent._lvebene1.AddTwoLines2("1. Hilfe","",(Object)(3900));
 //BA.debugLineNum = 1805;BA.debugLine="lvEbene1.AddTwoLines2 (\"Chef\",\"\",4000 )";
mostCurrent._lvebene1.AddTwoLines2("Chef","",(Object)(4000));
 //BA.debugLineNum = 1806;BA.debugLine="lvEbene1.AddTwoLines2 (\"Leistungsdiagnostik\",\"\", 4100)";
mostCurrent._lvebene1.AddTwoLines2("Leistungsdiagnostik","",(Object)(4100));
 //BA.debugLineNum = 1807;BA.debugLine="lvEbene1.AddTwoLines2 (\"Ordnung, Recht und Gesetz\",\"\",4200)";
mostCurrent._lvebene1.AddTwoLines2("Ordnung, Recht und Gesetz","",(Object)(4200));
 //BA.debugLineNum = 1809;BA.debugLine="lvEbene1.AddSingleLine2 (\"Einstellungen\",3000)";
mostCurrent._lvebene1.AddSingleLine2("Einstellungen",(Object)(3000));
 //BA.debugLineNum = 1810;BA.debugLine="lvEbene1.AddTwoLines2 (\"Einstellungen App\",\"\",4300)";
mostCurrent._lvebene1.AddTwoLines2("Einstellungen App","",(Object)(4300));
 //BA.debugLineNum = 1811;BA.debugLine="lvEbene1.AddTwoLines2 (\"Einstellungen Training\",\"\",4400 )";
mostCurrent._lvebene1.AddTwoLines2("Einstellungen Training","",(Object)(4400));
 break;
case 25:
 break;
case 26:
 //BA.debugLineNum = 1820;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1822;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ferien\",\"\",3001)";
mostCurrent._lvebene2.AddTwoLines2("Ferien","",(Object)(3001));
 //BA.debugLineNum = 1823;BA.debugLine="lvEbene2.AddTwoLines2 (\"Leistungsprognose\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Leistungsprognose","",(Object)(3001));
 //BA.debugLineNum = 1824;BA.debugLine="lvEbene2.AddTwoLines2 (\"Wettkampfplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Wettkampfplan","",(Object)(3001));
 //BA.debugLineNum = 1825;BA.debugLine="lvEbene2.AddTwoLines2 (\"Wochenplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Wochenplan","",(Object)(3001));
 //BA.debugLineNum = 1826;BA.debugLine="lvEbene2.AddTwoLines2 (\"Monatsplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Monatsplan","",(Object)(3001));
 //BA.debugLineNum = 1827;BA.debugLine="lvEbene2.AddTwoLines2 (\"Jahresplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("Jahresplan","",(Object)(3001));
 //BA.debugLineNum = 1828;BA.debugLine="lvEbene2.AddTwoLines2 (\"4 Jahresplan\",\"\",3001 )";
mostCurrent._lvebene2.AddTwoLines2("4 Jahresplan","",(Object)(3001));
 break;
case 27:
 break;
case 28:
 break;
case 29:
 break;
case 30:
 //BA.debugLineNum = 1844;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1846;BA.debugLine="lvEbene2.AddTwoLines2 (\"Mein Tauschring\",\"\",3601)";
mostCurrent._lvebene2.AddTwoLines2("Mein Tauschring","",(Object)(3601));
 //BA.debugLineNum = 1847;BA.debugLine="lvEbene2.AddTwoLines2 (\"Konto\",\"\",3601 )";
mostCurrent._lvebene2.AddTwoLines2("Konto","",(Object)(3601));
 //BA.debugLineNum = 1848;BA.debugLine="lvEbene2.AddTwoLines2 (\"Verwaltung\",\"\",3601 )";
mostCurrent._lvebene2.AddTwoLines2("Verwaltung","",(Object)(3601));
 //BA.debugLineNum = 1849;BA.debugLine="lvEbene2.AddTwoLines2 (\"Hilfe\",\"\",3601 )";
mostCurrent._lvebene2.AddTwoLines2("Hilfe","",(Object)(3601));
 break;
case 31:
 break;
case 32:
 //BA.debugLineNum = 1858;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1860;BA.debugLine="lvEbene2.AddTwoLines2 (\"1 Person\",\"\",3701)";
mostCurrent._lvebene2.AddTwoLines2("1 Person","",(Object)(3701));
 //BA.debugLineNum = 1861;BA.debugLine="lvEbene2.AddTwoLines2 (\"2 Personen\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("2 Personen","",(Object)(3701));
 //BA.debugLineNum = 1862;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppe gemischt\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Gruppe gemischt","",(Object)(3701));
 //BA.debugLineNum = 1863;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppe homogen\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Gruppe homogen","",(Object)(3701));
 //BA.debugLineNum = 1864;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppe 1\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Gruppe 1","",(Object)(3701));
 //BA.debugLineNum = 1865;BA.debugLine="lvEbene2.AddTwoLines2 (\"Gruppe 2\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Gruppe 2","",(Object)(3701));
 //BA.debugLineNum = 1866;BA.debugLine="lvEbene2.AddTwoLines2 (\"Frontalunterricht\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Frontalunterricht","",(Object)(3701));
 //BA.debugLineNum = 1868;BA.debugLine="lvEbene2.AddTwoLines2 (\"Freies Training\",\"\",3701)";
mostCurrent._lvebene2.AddTwoLines2("Freies Training","",(Object)(3701));
 //BA.debugLineNum = 1869;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kinder bis 6 Jahre\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Kinder bis 6 Jahre","",(Object)(3701));
 //BA.debugLineNum = 1870;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kinder 6-10 Jahre\",\"\",3701 )";
mostCurrent._lvebene2.AddTwoLines2("Kinder 6-10 Jahre","",(Object)(3701));
 break;
case 33:
 //BA.debugLineNum = 1873;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 34:
 break;
case 35:
 break;
case 36:
 break;
case 37:
 break;
case 38:
 break;
case 39:
 //BA.debugLineNum = 1892;BA.debugLine="Ebene2 = \"App Einstellungen\"";
mostCurrent._ebene2 = "App Einstellungen";
 //BA.debugLineNum = 1893;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1895;BA.debugLine="lvEbene2.AddTwoLines2 (\"Eula\",\"\",4301)";
mostCurrent._lvebene2.AddTwoLines2("Eula","",(Object)(4301));
 //BA.debugLineNum = 1896;BA.debugLine="lvEbene2.AddTwoLines2 (\"Farben\",\"\",4302 )";
mostCurrent._lvebene2.AddTwoLines2("Farben","",(Object)(4302));
 //BA.debugLineNum = 1897;BA.debugLine="lvEbene2.AddTwoLines2 (\"Feedback\",\"\",4303 )";
mostCurrent._lvebene2.AddTwoLines2("Feedback","",(Object)(4303));
 //BA.debugLineNum = 1898;BA.debugLine="lvEbene2.AddTwoLines2 (\"Lizensschlüssel\",\"\",4304 )";
mostCurrent._lvebene2.AddTwoLines2("Lizensschlüssel","",(Object)(4304));
 //BA.debugLineNum = 1899;BA.debugLine="lvEbene2.AddTwoLines2 (\"Passwort\",\"\",4305 )";
mostCurrent._lvebene2.AddTwoLines2("Passwort","",(Object)(4305));
 //BA.debugLineNum = 1900;BA.debugLine="lvEbene2.AddTwoLines2 (\"Spenden\",\"\",4306 )";
mostCurrent._lvebene2.AddTwoLines2("Spenden","",(Object)(4306));
 //BA.debugLineNum = 1902;BA.debugLine="lvEbene2.AddTwoLines2 (\"Widget1\",\"\",4308 )";
mostCurrent._lvebene2.AddTwoLines2("Widget1","",(Object)(4308));
 //BA.debugLineNum = 1903;BA.debugLine="lvEbene2.AddTwoLines2 (\"Widget2\",\"\",4309)";
mostCurrent._lvebene2.AddTwoLines2("Widget2","",(Object)(4309));
 break;
case 40:
 //BA.debugLineNum = 1907;BA.debugLine="StartActivity(Startabfragen)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._startabfragen.getObject()));
 break;
case 41:
 //BA.debugLineNum = 1911;BA.debugLine="StartActivity(EinstellungenApp)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungenapp.getObject()));
 break;
case 42:
 break;
case 43:
 //BA.debugLineNum = 1919;BA.debugLine="StartActivity(Startabfragen)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._startabfragen.getObject()));
 break;
case 44:
 //BA.debugLineNum = 1923;BA.debugLine="StartActivity(Startabfragen)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._startabfragen.getObject()));
 break;
case 45:
 break;
case 46:
 break;
case 47:
 break;
case 48:
 break;
case 49:
 //BA.debugLineNum = 1941;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1943;BA.debugLine="lvEbene2.AddTwoLines2 (\"Trainer Aufgaben\",\"\",4401)";
mostCurrent._lvebene2.AddTwoLines2("Trainer Aufgaben","",(Object)(4401));
 //BA.debugLineNum = 1944;BA.debugLine="lvEbene2.AddTwoLines2 (\"Intensität\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Intensität","",(Object)(4401));
 //BA.debugLineNum = 1945;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kommunikation\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Kommunikation","",(Object)(4401));
 //BA.debugLineNum = 1946;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ort\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Ort","",(Object)(4401));
 //BA.debugLineNum = 1947;BA.debugLine="lvEbene2.AddTwoLines2 (\"Reize\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Reize","",(Object)(4401));
 //BA.debugLineNum = 1948;BA.debugLine="lvEbene2.AddTwoLines2 (\"Sportart\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Sportart","",(Object)(4401));
 //BA.debugLineNum = 1949;BA.debugLine="lvEbene2.AddTwoLines2 (\"Voraussetzungen\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Voraussetzungen","",(Object)(4401));
 //BA.debugLineNum = 1951;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ziele\",\"\",4401)";
mostCurrent._lvebene2.AddTwoLines2("Ziele","",(Object)(4401));
 //BA.debugLineNum = 1952;BA.debugLine="lvEbene2.AddTwoLines2 (\"Zeit\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Zeit","",(Object)(4401));
 break;
case 50:
 //BA.debugLineNum = 1955;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 51:
 //BA.debugLineNum = 1958;BA.debugLine="lvEbene2.Clear";
mostCurrent._lvebene2.Clear();
 //BA.debugLineNum = 1960;BA.debugLine="lvEbene2.AddTwoLines2 (\"Trainer Aufgaben\",\"\",4401)";
mostCurrent._lvebene2.AddTwoLines2("Trainer Aufgaben","",(Object)(4401));
 //BA.debugLineNum = 1961;BA.debugLine="lvEbene2.AddTwoLines2 (\"Intensität\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Intensität","",(Object)(4401));
 //BA.debugLineNum = 1962;BA.debugLine="lvEbene2.AddTwoLines2 (\"Kommunikation\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Kommunikation","",(Object)(4401));
 //BA.debugLineNum = 1963;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ort\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Ort","",(Object)(4401));
 //BA.debugLineNum = 1964;BA.debugLine="lvEbene2.AddTwoLines2 (\"Reize\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Reize","",(Object)(4401));
 //BA.debugLineNum = 1965;BA.debugLine="lvEbene2.AddTwoLines2 (\"Sportart\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Sportart","",(Object)(4401));
 //BA.debugLineNum = 1966;BA.debugLine="lvEbene2.AddTwoLines2 (\"Voraussetzungen\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Voraussetzungen","",(Object)(4401));
 //BA.debugLineNum = 1968;BA.debugLine="lvEbene2.AddTwoLines2 (\"Ziele\",\"\",4401)";
mostCurrent._lvebene2.AddTwoLines2("Ziele","",(Object)(4401));
 //BA.debugLineNum = 1969;BA.debugLine="lvEbene2.AddTwoLines2 (\"Zeit\",\"\",4401 )";
mostCurrent._lvebene2.AddTwoLines2("Zeit","",(Object)(4401));
 break;
case 52:
 //BA.debugLineNum = 1973;BA.debugLine="NameCheckliste = \"Club\"";
_namecheckliste = "Club";
 //BA.debugLineNum = 1974;BA.debugLine="StartActivity(Checkliste)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._checkliste.getObject()));
 break;
case 53:
 //BA.debugLineNum = 1979;BA.debugLine="NameCheckliste = \"Einhorn\"";
_namecheckliste = "Einhorn";
 //BA.debugLineNum = 1980;BA.debugLine="StartActivity(Checkliste)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._checkliste.getObject()));
 break;
case 54:
 //BA.debugLineNum = 1984;BA.debugLine="NameCheckliste = \"Sport\"";
_namecheckliste = "Sport";
 //BA.debugLineNum = 1985;BA.debugLine="StartActivity(Checkliste)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._checkliste.getObject()));
 break;
case 55:
 //BA.debugLineNum = 1990;BA.debugLine="NameCheckliste = \"Meisterschaft\"";
_namecheckliste = "Meisterschaft";
 //BA.debugLineNum = 1991;BA.debugLine="StartActivity(Checkliste)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._checkliste.getObject()));
 break;
case 56:
 //BA.debugLineNum = 1995;BA.debugLine="NameCheckliste = \"Marketing\"";
_namecheckliste = "Marketing";
 //BA.debugLineNum = 1996;BA.debugLine="StartActivity(Checkliste)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._checkliste.getObject()));
 break;
case 57:
 //BA.debugLineNum = 2001;BA.debugLine="Msgbox(\"Baustelle\",\"Baustelle\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Baustelle","Baustelle",mostCurrent.activityBA);
 break;
case 58:
 //BA.debugLineNum = 2005;BA.debugLine="Msgbox(\"Baustelle\",\"Baustelle\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Baustelle","Baustelle",mostCurrent.activityBA);
 break;
case 59:
 //BA.debugLineNum = 2009;BA.debugLine="Msgbox(\"Baustelle\",\"Baustelle\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Baustelle","Baustelle",mostCurrent.activityBA);
 break;
case 60:
 //BA.debugLineNum = 2013;BA.debugLine="Msgbox(\"Baustelle\",\"Baustelle\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Baustelle","Baustelle",mostCurrent.activityBA);
 break;
case 61:
 //BA.debugLineNum = 2017;BA.debugLine="Msgbox(\"Baustelle\",\"Baustelle\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Baustelle","Baustelle",mostCurrent.activityBA);
 break;
case 62:
 //BA.debugLineNum = 2021;BA.debugLine="AktuellerUnterordner =  Unterordner15";
_aktuellerunterordner = _unterordner15;
 //BA.debugLineNum = 2022;BA.debugLine="Ueberschrift = \"Bodybuilding\"";
_ueberschrift = "Bodybuilding";
 //BA.debugLineNum = 2023;BA.debugLine="StartActivity(EinstellungenTrainingKick)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._einstellungentrainingkick.getObject()));
 break;
case 63:
 break;
default:
 //BA.debugLineNum = 2031;BA.debugLine="Msgbox(\"in lvEbene2_itemclick\",\"F e h l e r \")";
anywheresoftware.b4a.keywords.Common.Msgbox("in lvEbene2_itemclick","F e h l e r ",mostCurrent.activityBA);
 break;
}
;
 //BA.debugLineNum = 2037;BA.debugLine="End Sub";
return "";
}
public static String  _lvebene4_itemclick(int _position,Object _value) throws Exception{
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
String _gewicht1 = "";
 //BA.debugLineNum = 2039;BA.debugLine="Sub lvEbene4_Itemclick (Position As Int, Value As Object)";
 //BA.debugLineNum = 2081;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 2082;BA.debugLine="Dim cd, cdsf As ColorDialogHSV";
_cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.ColorDialogHSV();
_cdsf = new anywheresoftware.b4a.agraham.dialogs.InputDialog.ColorDialogHSV();
 //BA.debugLineNum = 2083;BA.debugLine="Dim color As Int";
_color = 0;
 //BA.debugLineNum = 2084;BA.debugLine="Dim ret As Int";
_ret = 0;
 //BA.debugLineNum = 2085;BA.debugLine="Dim fd As FileDialog";
_fd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog();
 //BA.debugLineNum = 2086;BA.debugLine="Dim Dd As DateDialog";
_dd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog();
 //BA.debugLineNum = 2087;BA.debugLine="Dim td As TimeDialog";
_td = new anywheresoftware.b4a.agraham.dialogs.InputDialog.TimeDialog();
 //BA.debugLineNum = 2088;BA.debugLine="Dim nd As NumberDialog";
_nd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.NumberDialog();
 //BA.debugLineNum = 2089;BA.debugLine="Dim Id As InputDialog";
_id = new anywheresoftware.b4a.agraham.dialogs.InputDialog();
 //BA.debugLineNum = 2090;BA.debugLine="Dim Bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 2091;BA.debugLine="Bmp.Initialize(File.DirAssets, \"mamaLogo.png\")";
_bmp.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png");
 //BA.debugLineNum = 2093;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)("Facebook"),(Object)("Personalien"),(Object)("Training"),(Object)("Netzwerke"),(Object)("Zeiten"),(Object)("Gewichte"),(Object)("screen0"),(Object)("screen1"),(Object)("Gewicht"),(Object)("Währung"),(Object)("Zeitzone"),(Object)("Anfangsgewicht"),(Object)("Zielgewicht"),(Object)("Name"),(Object)("Preise"),(Object)("Ausgabepreis gesamt"),(Object)("StrafLohn"),(Object)("eMail-Feedback"),(Object)("Minimaler Lohn"),(Object)("Maximaler Lohn"),(Object)("Kontrolltag Rythmus"),(Object)("Starttag (MM/TT/JJJJ)"),(Object)("Startzeit"),(Object)("Zieltag (MM/TT/JJJJ)"),(Object)("Zielzeit"),(Object)("Farben"),(Object)("Hintergrundfarbe"),(Object)("Schriftfarbe"),(Object)("Speichern"),(Object)("Speicherort"),(Object)("Jetzt Abspeichern"))) {
case 0:
 //BA.debugLineNum = 2096;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._facebook.getObject()));
 break;
case 1:
 //BA.debugLineNum = 2099;BA.debugLine="StartActivity(screenPersonal.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screenpersonal.CreateIntent()));
 break;
case 2:
 //BA.debugLineNum = 2102;BA.debugLine="StartActivity(screenTraining.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screentraining.CreateIntent()));
 break;
case 3:
 //BA.debugLineNum = 2105;BA.debugLine="StartActivity(screenNetzwerke.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screennetzwerke.CreateIntent()));
 break;
case 4:
 break;
case 5:
 break;
case 6:
 //BA.debugLineNum = 2114;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screen.CreateIntent()));
 break;
case 7:
 //BA.debugLineNum = 2117;BA.debugLine="StartActivity(screen.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_screen.CreateIntent()));
 break;
case 8:
 //BA.debugLineNum = 2120;BA.debugLine="Dim Gewicht1 As String";
_gewicht1 = "";
 //BA.debugLineNum = 2121;BA.debugLine="Liste1.Initialize";
mostCurrent._liste1.Initialize();
 //BA.debugLineNum = 2122;BA.debugLine="Liste1.Add(\"kg\")";
mostCurrent._liste1.Add((Object)("kg"));
 //BA.debugLineNum = 2123;BA.debugLine="Liste1.Add(\"lbs\")";
mostCurrent._liste1.Add((Object)("lbs"));
 //BA.debugLineNum = 2124;BA.debugLine="ret = InputList(Liste1, \"Einheit\",0)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(mostCurrent._liste1,"Einheit",(int) (0),mostCurrent.activityBA);
 //BA.debugLineNum = 2126;BA.debugLine="Select ret";
switch (_ret) {
case 0:
 //BA.debugLineNum = 2128;BA.debugLine="Gewicht1 = \" kg\"";
_gewicht1 = " kg";
 break;
case 1:
 //BA.debugLineNum = 2130;BA.debugLine="Gewicht1 = \" lbs\"";
_gewicht1 = " lbs";
 break;
default:
 //BA.debugLineNum = 2132;BA.debugLine="Msgbox(\"?\",\"?\")";
anywheresoftware.b4a.keywords.Common.Msgbox("?","?",mostCurrent.activityBA);
 break;
}
;
 break;
case 9:
 //BA.debugLineNum = 2137;BA.debugLine="Liste2.Initialize";
mostCurrent._liste2.Initialize();
 //BA.debugLineNum = 2138;BA.debugLine="Liste2.Add(\"Euro\")";
mostCurrent._liste2.Add((Object)("Euro"));
 //BA.debugLineNum = 2139;BA.debugLine="Liste2.Add(\"Dollar\")";
mostCurrent._liste2.Add((Object)("Dollar"));
 //BA.debugLineNum = 2140;BA.debugLine="Liste2.Add(\"Pounds\")";
mostCurrent._liste2.Add((Object)("Pounds"));
 //BA.debugLineNum = 2141;BA.debugLine="ret = InputList(Liste2, \"Geld Einheit\",0)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(mostCurrent._liste2,"Geld Einheit",(int) (0),mostCurrent.activityBA);
 //BA.debugLineNum = 2143;BA.debugLine="Select ret";
switch (_ret) {
case 0:
 //BA.debugLineNum = 2145;BA.debugLine="Waehrung = \" €\"";
mostCurrent._waehrung = " €";
 break;
case 1:
 //BA.debugLineNum = 2147;BA.debugLine="Waehrung = \" $\"";
mostCurrent._waehrung = " $";
 break;
case 2:
 //BA.debugLineNum = 2149;BA.debugLine="Waehrung = \" £\"";
mostCurrent._waehrung = " £";
 break;
default:
 //BA.debugLineNum = 2151;BA.debugLine="Msgbox(\"?\",\"?\")";
anywheresoftware.b4a.keywords.Common.Msgbox("?","?",mostCurrent.activityBA);
 break;
}
;
 break;
case 10:
 //BA.debugLineNum = 2156;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 2157;BA.debugLine="nd.Number = Zeitzone";
_nd.setNumber(_zeitzone);
 //BA.debugLineNum = 2158;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 2159;BA.debugLine="nd.ShowSign = True";
_nd.setShowSign(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2160;BA.debugLine="ret = nd.Show(\"Zeitzone\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Zeitzone","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2162;BA.debugLine="Zeitzone = nd.Number";
_zeitzone = _nd.getNumber();
 break;
case 11:
 //BA.debugLineNum = 2166;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 2167;BA.debugLine="nd.Number = Anfangsgewicht";
_nd.setNumber(_anfangsgewicht);
 //BA.debugLineNum = 2168;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 2170;BA.debugLine="ret = nd.Show(\"Anfangsgewicht\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Anfangsgewicht","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2172;BA.debugLine="Anfangsgewicht = nd.number / Power(10, nd.Decimal)";
_anfangsgewicht = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 12:
 //BA.debugLineNum = 2175;BA.debugLine="nd.Digits = 3";
_nd.setDigits((int) (3));
 //BA.debugLineNum = 2176;BA.debugLine="nd.Number = Zielgewicht";
_nd.setNumber(_zielgewicht);
 //BA.debugLineNum = 2177;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 2179;BA.debugLine="ret = nd.Show(\"Zielgewicht\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Zielgewicht","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2181;BA.debugLine="Zielgewicht = nd.number / Power(10, nd.Decimal)";
_zielgewicht = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 13:
 //BA.debugLineNum = 2184;BA.debugLine="Id.PasswordMode = False";
_id.setPasswordMode(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2188;BA.debugLine="Id.InputType = Id.INPUT_TYPE_TEXT";
_id.setInputType(_id.INPUT_TYPE_TEXT);
 //BA.debugLineNum = 2189;BA.debugLine="Id.input = Name";
_id.setInput(mostCurrent._name);
 //BA.debugLineNum = 2190;BA.debugLine="Id.Hint = \"Bitte Name eingeben!\"";
_id.setHint("Bitte Name eingeben!");
 //BA.debugLineNum = 2191;BA.debugLine="Id.HintColor = Colors.ARGB(196, 255, 140, 0)";
_id.setHintColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (196),(int) (255),(int) (140),(int) (0)));
 //BA.debugLineNum = 2192;BA.debugLine="ret = DialogResponse.CANCEL";
_ret = anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL;
 //BA.debugLineNum = 2193;BA.debugLine="ret = Id.Show(\"Geben sie einen kurzen Text ein\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _id.Show("Geben sie einen kurzen Text ein","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2196;BA.debugLine="Name = Id.input";
mostCurrent._name = _id.getInput();
 break;
case 14:
 break;
case 15:
 //BA.debugLineNum = 2202;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 2203;BA.debugLine="nd.Number = Ausgabepreis";
_nd.setNumber((int) (_ausgabepreis));
 //BA.debugLineNum = 2204;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 2206;BA.debugLine="ret = nd.Show(\"Ausgabepreis gesamt\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Ausgabepreis gesamt","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2208;BA.debugLine="Ausgabepreis = nd.Number / Power(10, nd.Decimal)";
_ausgabepreis = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 16:
 //BA.debugLineNum = 2211;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 2212;BA.debugLine="nd.Number = StrafLohn";
_nd.setNumber((int) (_straflohn));
 //BA.debugLineNum = 2213;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 2215;BA.debugLine="ret = nd.Show(\"Starflohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Starflohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2217;BA.debugLine="StrafLohn = nd.Number / Power(10, nd.Decimal)";
_straflohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 17:
 //BA.debugLineNum = 2220;BA.debugLine="StartActivity(Benachrichtigung)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._benachrichtigung.getObject()));
 break;
case 18:
 //BA.debugLineNum = 2224;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 2225;BA.debugLine="nd.Number = MinLohn";
_nd.setNumber((int) (_minlohn));
 //BA.debugLineNum = 2226;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 2228;BA.debugLine="ret = nd.Show(\"Minimaler Tageslohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Minimaler Tageslohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2230;BA.debugLine="MinLohn = nd.number / Power(10, nd.Decimal)";
_minlohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 19:
 //BA.debugLineNum = 2234;BA.debugLine="nd.Digits = 5";
_nd.setDigits((int) (5));
 //BA.debugLineNum = 2235;BA.debugLine="nd.Number = MaxLohn";
_nd.setNumber((int) (_maxlohn));
 //BA.debugLineNum = 2236;BA.debugLine="nd.Decimal = 2";
_nd.setDecimal((int) (2));
 //BA.debugLineNum = 2238;BA.debugLine="ret = nd.Show(\"Maximaler Tageslohn\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Maximaler Tageslohn","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2240;BA.debugLine="MaxLohn = nd.Number / Power(10, nd.Decimal)";
_maxlohn = _nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal());
 break;
case 20:
 //BA.debugLineNum = 2244;BA.debugLine="nd.Digits = 2";
_nd.setDigits((int) (2));
 //BA.debugLineNum = 2245;BA.debugLine="nd.Number = Kontrolltag";
_nd.setNumber(_kontrolltag);
 //BA.debugLineNum = 2246;BA.debugLine="nd.Decimal = 0";
_nd.setDecimal((int) (0));
 //BA.debugLineNum = 2248;BA.debugLine="ret = nd.Show(\"Kontrolltag Rythmus\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _nd.Show("Kontrolltag Rythmus","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2250;BA.debugLine="Kontrolltag = nd.number / Power(10, nd.Decimal)";
_kontrolltag = (int) (_nd.getNumber()/(double)anywheresoftware.b4a.keywords.Common.Power(10,_nd.getDecimal()));
 break;
case 21:
 //BA.debugLineNum = 2253;BA.debugLine="Dd.Year = DateTime.GetYear(DateTime.Now)";
_dd.setYear(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2254;BA.debugLine="Dd.Month = DateTime.GetMonth(DateTime.Now)";
_dd.setMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2255;BA.debugLine="Dd.DayOfMonth = DateTime.GetDayOfMonth(DateTime.Now)";
_dd.setDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2256;BA.debugLine="ret = Dd.Show(\"Setzen Sie den Starttag\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _dd.Show("Setzen Sie den Starttag","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2258;BA.debugLine="Starttag = Dd.Month & \"/\" & Dd.DayOfMonth & \"/\" & Dd.Year";
mostCurrent._starttag = BA.NumberToString(_dd.getMonth())+"/"+BA.NumberToString(_dd.getDayOfMonth())+"/"+BA.NumberToString(_dd.getYear());
 break;
case 22:
 //BA.debugLineNum = 2261;BA.debugLine="td.Hour = DateTime.GetHour(DateTime.Now)";
_td.setHour(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2262;BA.debugLine="td.Minute = DateTime.GetMinute(DateTime.Now)";
_td.setMinute(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2263;BA.debugLine="td.Is24Hours = True";
_td.setIs24Hours(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2264;BA.debugLine="ret = td.Show(\"Setzen Sie die Startzeit\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _td.Show("Setzen Sie die Startzeit","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2267;BA.debugLine="Startzeit = td.Hour & \":\" & td.Minute";
mostCurrent._startzeit = BA.NumberToString(_td.getHour())+":"+BA.NumberToString(_td.getMinute());
 break;
case 23:
 //BA.debugLineNum = 2270;BA.debugLine="Dd.Year = DateTime.GetYear(DateTime.Now)";
_dd.setYear(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2271;BA.debugLine="Dd.Month = DateTime.GetMonth(DateTime.Now)";
_dd.setMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2272;BA.debugLine="Dd.DayOfMonth = DateTime.GetDayOfMonth(DateTime.Now)";
_dd.setDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2273;BA.debugLine="ret = Dd.Show(\"Setze das Zieldatum\", \"Fette Euro's\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _dd.Show("Setze das Zieldatum","Fette Euro's","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2275;BA.debugLine="Zieltag = Dd.Month & \"/\" & Dd.DayOfMonth & \"/\" & Dd.Year";
mostCurrent._zieltag = BA.NumberToString(_dd.getMonth())+"/"+BA.NumberToString(_dd.getDayOfMonth())+"/"+BA.NumberToString(_dd.getYear());
 break;
case 24:
 //BA.debugLineNum = 2279;BA.debugLine="td.Hour = DateTime.GetHour(DateTime.Now)";
_td.setHour(anywheresoftware.b4a.keywords.Common.DateTime.GetHour(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2280;BA.debugLine="td.Minute = DateTime.GetMinute(DateTime.Now)";
_td.setMinute(anywheresoftware.b4a.keywords.Common.DateTime.GetMinute(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 2281;BA.debugLine="td.Is24Hours = True";
_td.setIs24Hours(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2282;BA.debugLine="ret = td.Show(\"Setzen Sie die Zielzeit\", \"Fette Euro#s\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _td.Show("Setzen Sie die Zielzeit","Fette Euro#s","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2284;BA.debugLine="Zielzeit = td.Hour & \":\" & td.Minute";
mostCurrent._zielzeit = BA.NumberToString(_td.getHour())+":"+BA.NumberToString(_td.getMinute());
 break;
case 25:
 break;
case 26:
 //BA.debugLineNum = 2292;BA.debugLine="cd.Hue = 180";
_cd.setHue((float) (180));
 //BA.debugLineNum = 2293;BA.debugLine="cd.Saturation = 0.5";
_cd.setSaturation((float) (0.5));
 //BA.debugLineNum = 2294;BA.debugLine="cd.Value = 0.5";
_cd.setValue((float) (0.5));
 //BA.debugLineNum = 2295;BA.debugLine="ret = cd.Show(\"Hintergrundfarbe\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _cd.Show("Hintergrundfarbe","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2297;BA.debugLine="Activity.color = cd.RGB";
mostCurrent._activity.setColor(_cd.getRGB());
 //BA.debugLineNum = 2298;BA.debugLine="manager.SetString(\"Hintergrundfarbe\",cd.rgb)";
_manager.SetString("Hintergrundfarbe",BA.NumberToString(_cd.getRGB()));
 break;
case 27:
 //BA.debugLineNum = 2303;BA.debugLine="cdsf.Hue = 180";
_cdsf.setHue((float) (180));
 //BA.debugLineNum = 2304;BA.debugLine="cdsf.Saturation = 0.5";
_cdsf.setSaturation((float) (0.5));
 //BA.debugLineNum = 2305;BA.debugLine="cdsf.Value = 0.5";
_cdsf.setValue((float) (0.5));
 //BA.debugLineNum = 2306;BA.debugLine="ret = cdsf.Show(\"Schriftfarbe\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _cdsf.Show("Schriftfarbe","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2308;BA.debugLine="manager.SetString(\"Schriftfarbe\",cdsf.rgb)";
_manager.SetString("Schriftfarbe",BA.NumberToString(_cdsf.getRGB()));
 break;
case 28:
 break;
case 29:
 //BA.debugLineNum = 2316;BA.debugLine="fd.FastScroll = True";
_fd.setFastScroll(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2317;BA.debugLine="fd.FilePath = File.DirRootExternal & AktuellerUnterordner ' also sets ChosenName to an emtpy string";
_fd.setFilePath(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+_aktuellerunterordner);
 //BA.debugLineNum = 2319;BA.debugLine="fd.FileFilter = \"*.*\" ' for example or \".jpg,.png\" for multiple file types";
_fd.setFileFilter("*.*");
 //BA.debugLineNum = 2320;BA.debugLine="ret = fd.Show(\"Datei Abspeichern\", \"Eingabe\", \"Abbruch\", \"\", Bmp)";
_ret = _fd.Show("Datei Abspeichern","Eingabe","Abbruch","",mostCurrent.activityBA,(android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2321;BA.debugLine="ToastMessageShow(ret & \" : Path : \" & fd.FilePath & CRLF & \"File : \" & fd.ChosenName, False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.NumberToString(_ret)+" : Path : "+_fd.getFilePath()+anywheresoftware.b4a.keywords.Common.CRLF+"File : "+_fd.getChosenName(),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2322;BA.debugLine="Speicherort = fd.FilePath & \"/\" & fd.ChosenName";
mostCurrent._speicherort = _fd.getFilePath()+"/"+_fd.getChosenName();
 break;
case 30:
 //BA.debugLineNum = 2325;BA.debugLine="HandleSettings";
_handlesettings();
 break;
default:
 //BA.debugLineNum = 2361;BA.debugLine="ToastMessageShow(\"Noch in Bearbeitung\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Noch in Bearbeitung",anywheresoftware.b4a.keywords.Common.False);
 break;
}
;
 //BA.debugLineNum = 2372;BA.debugLine="End Sub";
return "";
}
public static String  _newpage_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _names = null;
int _ret = 0;
 //BA.debugLineNum = 964;BA.debugLine="Sub NewPage_Click";
 //BA.debugLineNum = 965;BA.debugLine="Dim names As List";
_names = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 966;BA.debugLine="Dim ret As Int";
_ret = 0;
 //BA.debugLineNum = 968;BA.debugLine="names.Initialize";
_names.Initialize();
 //BA.debugLineNum = 969;BA.debugLine="names.Add(\"Startseite\")";
_names.Add((Object)("Startseite"));
 //BA.debugLineNum = 970;BA.debugLine="names.Add(\"Einstellungen\")";
_names.Add((Object)("Einstellungen"));
 //BA.debugLineNum = 971;BA.debugLine="names.Add(\"Einstellungen2\")";
_names.Add((Object)("Einstellungen2"));
 //BA.debugLineNum = 972;BA.debugLine="names.Add(\"Auswahl\")";
_names.Add((Object)("Auswahl"));
 //BA.debugLineNum = 975;BA.debugLine="ret = InputList(names, \"Choose page type\", -1)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(_names,"Choose page type",(int) (-1),mostCurrent.activityBA);
 //BA.debugLineNum = 977;BA.debugLine="If ret = DialogResponse.CANCEL Then";
if (_ret==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 978;BA.debugLine="ToastMessageShow(\"Abbruch\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Abbruch",anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 981;BA.debugLine="container.AddPageAt(CreatePanel(ret + 1, names.Get(ret)), names.Get(ret), pager.CurrentPage + 1)";
mostCurrent._container.AddPageAt((android.view.View)(_createpanel((int) (_ret+1),BA.ObjectToString(_names.Get(_ret))).getObject()),BA.ObjectToString(_names.Get(_ret)),(int) (mostCurrent._pager.getCurrentPage()+1));
 //BA.debugLineNum = 985;BA.debugLine="tabs.NotifyDataChange";
mostCurrent._tabs.NotifyDataChange();
 //BA.debugLineNum = 988;BA.debugLine="pager.GotoPage(pager.CurrentPage + 1, True)";
mostCurrent._pager.GotoPage((int) (mostCurrent._pager.getCurrentPage()+1),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 990;BA.debugLine="End Sub";
return "";
}
public static String  _pager_pagechanged(int _position) throws Exception{
 //BA.debugLineNum = 1004;BA.debugLine="Sub Pager_PageChanged (Position As Int)";
 //BA.debugLineNum = 1005;BA.debugLine="Log (\"Page Changed to \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("Page Changed to "+BA.NumberToString(_position));
 //BA.debugLineNum = 1006;BA.debugLine="CurrentPage = Position";
_currentpage = _position;
 //BA.debugLineNum = 1007;BA.debugLine="SetButtonText";
_setbuttontext();
 //BA.debugLineNum = 1008;BA.debugLine="End Sub";
return "";
}
public static String  _pager_pagecreated(int _position,Object _page) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
de.watchkido.mama.main._panelinfo _pi = null;
 //BA.debugLineNum = 1013;BA.debugLine="Sub Pager_PageCreated (Position As Int, Page As Object)";
 //BA.debugLineNum = 1014;BA.debugLine="Log (\"Seite erstellen \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("Seite erstellen "+BA.NumberToString(_position));
 //BA.debugLineNum = 1016;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1017;BA.debugLine="Dim pi As PanelInfo";
_pi = new de.watchkido.mama.main._panelinfo();
 //BA.debugLineNum = 1019;BA.debugLine="pan = Page";
_pan.setObject((android.view.ViewGroup)(_page));
 //BA.debugLineNum = 1020;BA.debugLine="pi = pan.Tag";
_pi = (de.watchkido.mama.main._panelinfo)(_pan.getTag());
 //BA.debugLineNum = 1023;BA.debugLine="End Sub";
return "";
}
public static String  _pager_pagedestroyed(int _position,Object _page) throws Exception{
 //BA.debugLineNum = 1026;BA.debugLine="Sub Pager_PageDestroyed (Position As Int, Page As Object)";
 //BA.debugLineNum = 1027;BA.debugLine="Log(\"Seite zerstört \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("Seite zerstört "+BA.NumberToString(_position));
 //BA.debugLineNum = 1028;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 426;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 429;BA.debugLine="Dim TYPE_STARTSEITE As Int : TYPE_STARTSEITE = 1";
_type_startseite = 0;
 //BA.debugLineNum = 429;BA.debugLine="Dim TYPE_STARTSEITE As Int : TYPE_STARTSEITE = 1";
_type_startseite = (int) (1);
 //BA.debugLineNum = 430;BA.debugLine="Dim TYPE_EINSTELLUNGEN As Int : TYPE_EINSTELLUNGEN = 2";
_type_einstellungen = 0;
 //BA.debugLineNum = 430;BA.debugLine="Dim TYPE_EINSTELLUNGEN As Int : TYPE_EINSTELLUNGEN = 2";
_type_einstellungen = (int) (2);
 //BA.debugLineNum = 431;BA.debugLine="Dim TYPE_EBENE1 As Int : TYPE_EBENE1 = 3";
_type_ebene1 = 0;
 //BA.debugLineNum = 431;BA.debugLine="Dim TYPE_EBENE1 As Int : TYPE_EBENE1 = 3";
_type_ebene1 = (int) (3);
 //BA.debugLineNum = 432;BA.debugLine="Dim TYPE_EBENE2 As Int : TYPE_EBENE2 =4";
_type_ebene2 = 0;
 //BA.debugLineNum = 432;BA.debugLine="Dim TYPE_EBENE2 As Int : TYPE_EBENE2 =4";
_type_ebene2 = (int) (4);
 //BA.debugLineNum = 433;BA.debugLine="Dim TYPE_EBENE3 As Int : TYPE_EBENE3 = 5";
_type_ebene3 = 0;
 //BA.debugLineNum = 433;BA.debugLine="Dim TYPE_EBENE3 As Int : TYPE_EBENE3 = 5";
_type_ebene3 = (int) (5);
 //BA.debugLineNum = 434;BA.debugLine="Dim TYPE_EBENE4 As Int : TYPE_EBENE4 = 6";
_type_ebene4 = 0;
 //BA.debugLineNum = 434;BA.debugLine="Dim TYPE_EBENE4 As Int : TYPE_EBENE4 = 6";
_type_ebene4 = (int) (6);
 //BA.debugLineNum = 435;BA.debugLine="Dim FILL_PARENT As Int : FILL_PARENT = -1";
_fill_parent = 0;
 //BA.debugLineNum = 435;BA.debugLine="Dim FILL_PARENT As Int : FILL_PARENT = -1";
_fill_parent = (int) (-1);
 //BA.debugLineNum = 436;BA.debugLine="Dim WRAP_CONTENT As Int : WRAP_CONTENT = -2";
_wrap_content = 0;
 //BA.debugLineNum = 436;BA.debugLine="Dim WRAP_CONTENT As Int : WRAP_CONTENT = -2";
_wrap_content = (int) (-2);
 //BA.debugLineNum = 439;BA.debugLine="Type PanelInfo (PanelType As Int, LayoutLoaded As Boolean)";
;
 //BA.debugLineNum = 441;BA.debugLine="Dim CurrentTheme As Int : CurrentTheme = 0";
_currenttheme = 0;
 //BA.debugLineNum = 441;BA.debugLine="Dim CurrentTheme As Int : CurrentTheme = 0";
_currenttheme = (int) (0);
 //BA.debugLineNum = 442;BA.debugLine="Dim CurrentPage As Int : CurrentPage = 0";
_currentpage = 0;
 //BA.debugLineNum = 442;BA.debugLine="Dim CurrentPage As Int : CurrentPage = 0";
_currentpage = (int) (0);
 //BA.debugLineNum = 444;BA.debugLine="Dim Ueberschrift As String";
_ueberschrift = "";
 //BA.debugLineNum = 445;BA.debugLine="Dim ZeitErwaermung As Int : ZeitErwaermung = 0";
_zeiterwaermung = 0;
 //BA.debugLineNum = 445;BA.debugLine="Dim ZeitErwaermung As Int : ZeitErwaermung = 0";
_zeiterwaermung = (int) (0);
 //BA.debugLineNum = 446;BA.debugLine="Dim ZeitProUebung As Int : ZeitProUebung = 3";
_zeitprouebung = 0;
 //BA.debugLineNum = 446;BA.debugLine="Dim ZeitProUebung As Int : ZeitProUebung = 3";
_zeitprouebung = (int) (3);
 //BA.debugLineNum = 447;BA.debugLine="Dim ZeitPause As Int : ZeitPause = 3";
_zeitpause = 0;
 //BA.debugLineNum = 447;BA.debugLine="Dim ZeitPause As Int : ZeitPause = 3";
_zeitpause = (int) (3);
 //BA.debugLineNum = 448;BA.debugLine="Dim Zeit As Int : Zeit = 20000";
_zeit = 0;
 //BA.debugLineNum = 448;BA.debugLine="Dim Zeit As Int : Zeit = 20000";
_zeit = (int) (20000);
 //BA.debugLineNum = 450;BA.debugLine="Dim Businessversion As Boolean : Businessversion = True";
_businessversion = false;
 //BA.debugLineNum = 450;BA.debugLine="Dim Businessversion As Boolean : Businessversion = True";
_businessversion = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 451;BA.debugLine="Dim NameCheckliste As String";
_namecheckliste = "";
 //BA.debugLineNum = 454;BA.debugLine="Dim AktuellerUnterordner As String   : AktuellerUnterordner =  \"/mama/KickErwaermung\"";
_aktuellerunterordner = "";
 //BA.debugLineNum = 454;BA.debugLine="Dim AktuellerUnterordner As String   : AktuellerUnterordner =  \"/mama/KickErwaermung\"";
_aktuellerunterordner = "/mama/KickErwaermung";
 //BA.debugLineNum = 455;BA.debugLine="Dim Hauptordner0 As String : Hauptordner0 = \"/mama\"";
_hauptordner0 = "";
 //BA.debugLineNum = 455;BA.debugLine="Dim Hauptordner0 As String : Hauptordner0 = \"/mama\"";
_hauptordner0 = "/mama";
 //BA.debugLineNum = 456;BA.debugLine="Dim Unterordner1 As String : Unterordner1 = \"/mama/Daten\"";
_unterordner1 = "";
 //BA.debugLineNum = 456;BA.debugLine="Dim Unterordner1 As String : Unterordner1 = \"/mama/Daten\"";
_unterordner1 = "/mama/Daten";
 //BA.debugLineNum = 457;BA.debugLine="Dim Unterordner2 As String : Unterordner2 = \"/mama/AllgBodyWeightExercices\"";
_unterordner2 = "";
 //BA.debugLineNum = 457;BA.debugLine="Dim Unterordner2 As String : Unterordner2 = \"/mama/AllgBodyWeightExercices\"";
_unterordner2 = "/mama/AllgBodyWeightExercices";
 //BA.debugLineNum = 458;BA.debugLine="Dim Unterordner3 As String : Unterordner3 = \"/mama/AllgCoolDown\"";
_unterordner3 = "";
 //BA.debugLineNum = 458;BA.debugLine="Dim Unterordner3 As String : Unterordner3 = \"/mama/AllgCoolDown\"";
_unterordner3 = "/mama/AllgCoolDown";
 //BA.debugLineNum = 459;BA.debugLine="Dim Unterordner4 As String : Unterordner4 = \"/mama/AllgDehnung\"";
_unterordner4 = "";
 //BA.debugLineNum = 459;BA.debugLine="Dim Unterordner4 As String : Unterordner4 = \"/mama/AllgDehnung\"";
_unterordner4 = "/mama/AllgDehnung";
 //BA.debugLineNum = 460;BA.debugLine="Dim Unterordner5 As String : Unterordner5 = \"/mama/AllgErwaermung\"";
_unterordner5 = "";
 //BA.debugLineNum = 460;BA.debugLine="Dim Unterordner5 As String : Unterordner5 = \"/mama/AllgErwaermung\"";
_unterordner5 = "/mama/AllgErwaermung";
 //BA.debugLineNum = 461;BA.debugLine="Dim Unterordner6 As String : Unterordner6 = \"/mama/KarateEinzel\"";
_unterordner6 = "";
 //BA.debugLineNum = 461;BA.debugLine="Dim Unterordner6 As String : Unterordner6 = \"/mama/KarateEinzel\"";
_unterordner6 = "/mama/KarateEinzel";
 //BA.debugLineNum = 462;BA.debugLine="Dim Unterordner7 As String : Unterordner7 = \"/mama/KarateErwaermung\"";
_unterordner7 = "";
 //BA.debugLineNum = 462;BA.debugLine="Dim Unterordner7 As String : Unterordner7 = \"/mama/KarateErwaermung\"";
_unterordner7 = "/mama/KarateErwaermung";
 //BA.debugLineNum = 463;BA.debugLine="Dim Unterordner8 As String : Unterordner8 = \"/mama/KarateKata\"";
_unterordner8 = "";
 //BA.debugLineNum = 463;BA.debugLine="Dim Unterordner8 As String : Unterordner8 = \"/mama/KarateKata\"";
_unterordner8 = "/mama/KarateKata";
 //BA.debugLineNum = 464;BA.debugLine="Dim Unterordner9 As String : Unterordner9 = \"/mama/KarateKumite\"";
_unterordner9 = "";
 //BA.debugLineNum = 464;BA.debugLine="Dim Unterordner9 As String : Unterordner9 = \"/mama/KarateKumite\"";
_unterordner9 = "/mama/KarateKumite";
 //BA.debugLineNum = 465;BA.debugLine="Dim Unterordner10 As String : Unterordner10 = \"/mama/KarateKihon\"";
_unterordner10 = "";
 //BA.debugLineNum = 465;BA.debugLine="Dim Unterordner10 As String : Unterordner10 = \"/mama/KarateKihon\"";
_unterordner10 = "/mama/KarateKihon";
 //BA.debugLineNum = 466;BA.debugLine="Dim Unterordner11 As String : Unterordner11 = \"/mama/Karate\"";
_unterordner11 = "";
 //BA.debugLineNum = 466;BA.debugLine="Dim Unterordner11 As String : Unterordner11 = \"/mama/Karate\"";
_unterordner11 = "/mama/Karate";
 //BA.debugLineNum = 467;BA.debugLine="Dim Unterordner12 As String	: Unterordner12 = \"/mama/KickErwaermung\"";
_unterordner12 = "";
 //BA.debugLineNum = 467;BA.debugLine="Dim Unterordner12 As String	: Unterordner12 = \"/mama/KickErwaermung\"";
_unterordner12 = "/mama/KickErwaermung";
 //BA.debugLineNum = 468;BA.debugLine="Dim Unterordner13 As String	: Unterordner13 = \"/mama/KickEinzel\"";
_unterordner13 = "";
 //BA.debugLineNum = 468;BA.debugLine="Dim Unterordner13 As String	: Unterordner13 = \"/mama/KickEinzel\"";
_unterordner13 = "/mama/KickEinzel";
 //BA.debugLineNum = 469;BA.debugLine="Dim Unterordner14 As String	: Unterordner14 = \"/mama/KickPartner\"";
_unterordner14 = "";
 //BA.debugLineNum = 469;BA.debugLine="Dim Unterordner14 As String	: Unterordner14 = \"/mama/KickPartner\"";
_unterordner14 = "/mama/KickPartner";
 //BA.debugLineNum = 470;BA.debugLine="Dim Unterordner15 As String	: Unterordner15 = \"/mama/Bodybuilding\"";
_unterordner15 = "";
 //BA.debugLineNum = 470;BA.debugLine="Dim Unterordner15 As String	: Unterordner15 = \"/mama/Bodybuilding\"";
_unterordner15 = "/mama/Bodybuilding";
 //BA.debugLineNum = 471;BA.debugLine="Dim Unterordner16 As String	: Unterordner16 = \"/mama/Nunchaku\"";
_unterordner16 = "";
 //BA.debugLineNum = 471;BA.debugLine="Dim Unterordner16 As String	: Unterordner16 = \"/mama/Nunchaku\"";
_unterordner16 = "/mama/Nunchaku";
 //BA.debugLineNum = 472;BA.debugLine="Dim Unterordner17 As String	: Unterordner17 = \"/mama/Sounds\"";
_unterordner17 = "";
 //BA.debugLineNum = 472;BA.debugLine="Dim Unterordner17 As String	: Unterordner17 = \"/mama/Sounds\"";
_unterordner17 = "/mama/Sounds";
 //BA.debugLineNum = 473;BA.debugLine="Dim Unterordner18 As String	: Unterordner18 = \"/mama/Datenbanken\"";
_unterordner18 = "";
 //BA.debugLineNum = 473;BA.debugLine="Dim Unterordner18 As String	: Unterordner18 = \"/mama/Datenbanken\"";
_unterordner18 = "/mama/Datenbanken";
 //BA.debugLineNum = 474;BA.debugLine="Dim Unterordner19 As String	: Unterordner19 = \"/mama/\"";
_unterordner19 = "";
 //BA.debugLineNum = 474;BA.debugLine="Dim Unterordner19 As String	: Unterordner19 = \"/mama/\"";
_unterordner19 = "/mama/";
 //BA.debugLineNum = 475;BA.debugLine="Dim Unterordner20 As String	: Unterordner20 = \"/mama/ATS\"";
_unterordner20 = "";
 //BA.debugLineNum = 475;BA.debugLine="Dim Unterordner20 As String	: Unterordner20 = \"/mama/ATS\"";
_unterordner20 = "/mama/ATS";
 //BA.debugLineNum = 478;BA.debugLine="Dim Versionsnummer As String: Versionsnummer = \"Version vom 23.03.2015\"";
_versionsnummer = "";
 //BA.debugLineNum = 478;BA.debugLine="Dim Versionsnummer As String: Versionsnummer = \"Version vom 23.03.2015\"";
_versionsnummer = "Version vom 23.03.2015";
 //BA.debugLineNum = 479;BA.debugLine="Dim MAMAWebseite As String 	: MAMAWebseite = \"http://www.watchkido.de\"";
_mamawebseite = "";
 //BA.debugLineNum = 479;BA.debugLine="Dim MAMAWebseite As String 	: MAMAWebseite = \"http://www.watchkido.de\"";
_mamawebseite = "http://www.watchkido.de";
 //BA.debugLineNum = 480;BA.debugLine="Dim ActivityTitel As String : ActivityTitel = \"Martial Arts Master App\"";
_activitytitel = "";
 //BA.debugLineNum = 480;BA.debugLine="Dim ActivityTitel As String : ActivityTitel = \"Martial Arts Master App\"";
_activitytitel = "Martial Arts Master App";
 //BA.debugLineNum = 481;BA.debugLine="Dim IckWarAllHier As Boolean : IckWarAllHier = True 'ausschalten der StartabfragenActivity";
_ickwarallhier = false;
 //BA.debugLineNum = 481;BA.debugLine="Dim IckWarAllHier As Boolean : IckWarAllHier = True 'ausschalten der StartabfragenActivity";
_ickwarallhier = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 482;BA.debugLine="Dim MeineNummer As String	: MeineNummer = \"+4915258120499\"";
_meinenummer = "";
 //BA.debugLineNum = 482;BA.debugLine="Dim MeineNummer As String	: MeineNummer = \"+4915258120499\"";
_meinenummer = "+4915258120499";
 //BA.debugLineNum = 483;BA.debugLine="Dim MeineEmail As String	: MeineEmail = \"mamaintern@watchkido.de\"";
_meineemail = "";
 //BA.debugLineNum = 483;BA.debugLine="Dim MeineEmail As String	: MeineEmail = \"mamaintern@watchkido.de\"";
_meineemail = "mamaintern@watchkido.de";
 //BA.debugLineNum = 484;BA.debugLine="Dim Vorname As String		: Vorname = \"Frank\"";
_vorname = "";
 //BA.debugLineNum = 484;BA.debugLine="Dim Vorname As String		: Vorname = \"Frank\"";
_vorname = "Frank";
 //BA.debugLineNum = 485;BA.debugLine="Dim Nachname As String		: Nachname = \"Albrecht\"";
_nachname = "";
 //BA.debugLineNum = 485;BA.debugLine="Dim Nachname As String		: Nachname = \"Albrecht\"";
_nachname = "Albrecht";
 //BA.debugLineNum = 486;BA.debugLine="Dim Spitzname As String		: Spitzname = \"Avita\"";
_spitzname = "";
 //BA.debugLineNum = 486;BA.debugLine="Dim Spitzname As String		: Spitzname = \"Avita\"";
_spitzname = "Avita";
 //BA.debugLineNum = 487;BA.debugLine="Dim Heimatort As String		: Heimatort = \"Neustadt/W\"";
_heimatort = "";
 //BA.debugLineNum = 487;BA.debugLine="Dim Heimatort As String		: Heimatort = \"Neustadt/W\"";
_heimatort = "Neustadt/W";
 //BA.debugLineNum = 488;BA.debugLine="Dim Alter As Int			: Alter = 47";
_alter = 0;
 //BA.debugLineNum = 488;BA.debugLine="Dim Alter As Int			: Alter = 47";
_alter = (int) (47);
 //BA.debugLineNum = 489;BA.debugLine="Dim Altersklasse As String	: Altersklasse = \"Senioren\"";
_altersklasse = "";
 //BA.debugLineNum = 489;BA.debugLine="Dim Altersklasse As String	: Altersklasse = \"Senioren\"";
_altersklasse = "Senioren";
 //BA.debugLineNum = 490;BA.debugLine="Dim Groeße As Int			: Groeße = 188";
_groeße = 0;
 //BA.debugLineNum = 490;BA.debugLine="Dim Groeße As Int			: Groeße = 188";
_groeße = (int) (188);
 //BA.debugLineNum = 491;BA.debugLine="Dim Gewicht As Int			: Gewicht = 90";
_gewicht = 0;
 //BA.debugLineNum = 491;BA.debugLine="Dim Gewicht As Int			: Gewicht = 90";
_gewicht = (int) (90);
 //BA.debugLineNum = 492;BA.debugLine="Dim Gewichtsklasse As String: Gewichtsklasse = \"Schwergewicht\"";
_gewichtsklasse = "";
 //BA.debugLineNum = 492;BA.debugLine="Dim Gewichtsklasse As String: Gewichtsklasse = \"Schwergewicht\"";
_gewichtsklasse = "Schwergewicht";
 //BA.debugLineNum = 493;BA.debugLine="Dim Schule As String		: Schule = \"Realschule\"";
_schule = "";
 //BA.debugLineNum = 493;BA.debugLine="Dim Schule As String		: Schule = \"Realschule\"";
_schule = "Realschule";
 //BA.debugLineNum = 494;BA.debugLine="Dim Abschluss As String		: Abschluss = \"Dreher, Schlosser, Karatelehrer, Autor, Youtuber\"";
_abschluss = "";
 //BA.debugLineNum = 494;BA.debugLine="Dim Abschluss As String		: Abschluss = \"Dreher, Schlosser, Karatelehrer, Autor, Youtuber\"";
_abschluss = "Dreher, Schlosser, Karatelehrer, Autor, Youtuber";
 //BA.debugLineNum = 495;BA.debugLine="Dim Sportart As String		: Sportart = \"Kickboxen, Karate\"";
_sportart = "";
 //BA.debugLineNum = 495;BA.debugLine="Dim Sportart As String		: Sportart = \"Kickboxen, Karate\"";
_sportart = "Kickboxen, Karate";
 //BA.debugLineNum = 496;BA.debugLine="Dim Verein As String		: Verein = \"Bushido Sport Club\"";
_verein = "";
 //BA.debugLineNum = 496;BA.debugLine="Dim Verein As String		: Verein = \"Bushido Sport Club\"";
_verein = "Bushido Sport Club";
 //BA.debugLineNum = 497;BA.debugLine="Dim Verband As String		: Verband = \"MAA-I\"";
_verband = "";
 //BA.debugLineNum = 497;BA.debugLine="Dim Verband As String		: Verband = \"MAA-I\"";
_verband = "MAA-I";
 //BA.debugLineNum = 502;BA.debugLine="Dim Ueberwachung As Boolean : Ueberwachung = True";
_ueberwachung = false;
 //BA.debugLineNum = 502;BA.debugLine="Dim Ueberwachung As Boolean : Ueberwachung = True";
_ueberwachung = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 503;BA.debugLine="Dim Meldezeit As Int		: Meldezeit = 60000 * 10 '1 min mal 100";
_meldezeit = 0;
 //BA.debugLineNum = 503;BA.debugLine="Dim Meldezeit As Int		: Meldezeit = 60000 * 10 '1 min mal 100";
_meldezeit = (int) (60000*10);
 //BA.debugLineNum = 506;BA.debugLine="Dim manager As AHPreferenceManager";
_manager = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceManager();
 //BA.debugLineNum = 507;BA.debugLine="Dim screen, screenPersonal, screenTraining, screenNetzwerke As AHPreferenceScreen";
_screen = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_screenpersonal = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_screentraining = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_screennetzwerke = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 509;BA.debugLine="End Sub";
return "";
}
public static String  _setbuttontext() throws Exception{
 //BA.debugLineNum = 1030;BA.debugLine="Sub SetButtonText";
 //BA.debugLineNum = 1031;BA.debugLine="Button1.Text = \"Ebene \" & pager.CurrentPage";
mostCurrent._button1.setText((Object)("Ebene "+BA.NumberToString(mostCurrent._pager.getCurrentPage())));
 //BA.debugLineNum = 1032;BA.debugLine="End Sub";
return "";
}
public static String  _settheme(int _theme) throws Exception{
 //BA.debugLineNum = 1062;BA.debugLine="Sub SetTheme(Theme As Int)";
 //BA.debugLineNum = 1063;BA.debugLine="Select Theme";
switch (_theme) {
case 0:
 //BA.debugLineNum = 1065;BA.debugLine="tabs.Color = Colors.Black";
mostCurrent._tabs.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1066;BA.debugLine="tabs.BackgroundColorPressed = Colors.Blue";
mostCurrent._tabs.setBackgroundColorPressed(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 1067;BA.debugLine="tabs.LineColorCenter = Colors.Green";
mostCurrent._tabs.setLineColorCenter(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1068;BA.debugLine="tabs.TextColor = Colors.LightGray";
mostCurrent._tabs.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1069;BA.debugLine="tabs.TextColorCenter = Colors.Green";
mostCurrent._tabs.setTextColorCenter(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 1070;BA.debugLine="line.Color = Colors.Green";
mostCurrent._line.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 break;
case 1:
 //BA.debugLineNum = 1072;BA.debugLine="tabs.Color = Colors.White";
mostCurrent._tabs.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1073;BA.debugLine="tabs.BackgroundColorPressed = Colors.Blue";
mostCurrent._tabs.setBackgroundColorPressed(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 1074;BA.debugLine="tabs.LineColorCenter = Colors.DarkGray";
mostCurrent._tabs.setLineColorCenter(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 1075;BA.debugLine="tabs.TextColor = Colors.LightGray";
mostCurrent._tabs.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1076;BA.debugLine="tabs.TextColorCenter = Colors.DarkGray";
mostCurrent._tabs.setTextColorCenter(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 1077;BA.debugLine="line.Color = Colors.DarkGray";
mostCurrent._line.setColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 break;
}
;
 //BA.debugLineNum = 1079;BA.debugLine="End Sub";
return "";
}
public static String  _setupbildschirmherstellen() throws Exception{
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
String _kcal1 = "";
anywheresoftware.b4a.objects.collections.Map _mapintensität = null;
 //BA.debugLineNum = 2384;BA.debugLine="Sub SetupbildschirmHerstellen";
 //BA.debugLineNum = 2385;BA.debugLine="screen.Initialize(\"Einstellungen\", \"Einstellungen\")";
_screen.Initialize("Einstellungen","Einstellungen");
 //BA.debugLineNum = 2386;BA.debugLine="screenPersonal.Initialize(\"Personalien\",\"Personalien\")";
_screenpersonal.Initialize("Personalien","Personalien");
 //BA.debugLineNum = 2387;BA.debugLine="screenTraining.Initialize(\"screen1\",\"Bildschirm 1\")";
_screentraining.Initialize("screen1","Bildschirm 1");
 //BA.debugLineNum = 2391;BA.debugLine="Dim intentCat, cat1, cat2,KatNetzwerke, KatPersonal, KatTraining As AHPreferenceCategory";
_intentcat = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat1 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_cat2 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_katnetzwerke = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_katpersonal = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_kattraining = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 2392;BA.debugLine="Dim intentscreen, intentscreen1 As AHPreferenceScreen";
_intentscreen = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_intentscreen1 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 2394;BA.debugLine="intentCat.Initialize(\"Geräte einstellungen\")";
_intentcat.Initialize("Geräte einstellungen");
 //BA.debugLineNum = 2395;BA.debugLine="intentCat.AddCheckBox(\"intentenable\", \"Enable Intent Settings\", \"Intent settings are enabled\", \"Intent settings are disabled\", True, \"\")";
_intentcat.AddCheckBox("intentenable","Enable Intent Settings","Intent settings are enabled","Intent settings are disabled",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 2396;BA.debugLine="intentscreen.Initialize(\"Intents\", \"Examples with Intents\")";
_intentscreen.Initialize("Intents","Examples with Intents");
 //BA.debugLineNum = 2397;BA.debugLine="intentscreen.AddCheckBox(\"chkwifi\", \"Enable Wifi Settings\", \"Wifi settings enabled\", \"Wifi settings disabled\", False, \"\")";
_intentscreen.AddCheckBox("chkwifi","Enable Wifi Settings","Wifi settings enabled","Wifi settings disabled",anywheresoftware.b4a.keywords.Common.False,"");
 //BA.debugLineNum = 2400;BA.debugLine="Dim In As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 2401;BA.debugLine="In.Initialize(\"android.settings.WIFI_SETTINGS\", \"\")";
_in.Initialize("android.settings.WIFI_SETTINGS","");
 //BA.debugLineNum = 2402;BA.debugLine="intentscreen.AddIntent( \"Wifi Settings\", \"Example for custom Intent\", In, \"chkwifi\")";
_intentscreen.AddIntent("Wifi Settings","Example for custom Intent",(android.content.Intent)(_in.getObject()),"chkwifi");
 //BA.debugLineNum = 2408;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 2409;BA.debugLine="Dim pl As List";
_pl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 2410;BA.debugLine="pl = pm.GetInstalledPackages";
_pl = _pm.GetInstalledPackages();
 //BA.debugLineNum = 2412;BA.debugLine="If pl.IndexOf(\"com.android.calculator2\") > 0 Then";
if (_pl.IndexOf((Object)("com.android.calculator2"))>0) { 
 //BA.debugLineNum = 2413;BA.debugLine="intentscreen.AddIntent(\"Calculator\", \"Open calculator\", pm.GetApplicationIntent(\"com.android.calculator2\"), \"\")";
_intentscreen.AddIntent("Calculator","Open calculator",(android.content.Intent)(_pm.GetApplicationIntent("com.android.calculator2").getObject()),"");
 };
 //BA.debugLineNum = 2417;BA.debugLine="Dim pi As PhoneIntents";
_pi = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 2418;BA.debugLine="intentscreen.AddIntent(\"Browser\", \"Open http://www.google.de\", pi.OpenBrowser(\"http://www.google.com\"), \"\")";
_intentscreen.AddIntent("Browser","Open http://www.google.de",_pi.OpenBrowser("http://www.google.com"),"");
 //BA.debugLineNum = 2421;BA.debugLine="intentCat.AddPreferenceScreen(intentscreen, \"intentenable\")";
_intentcat.AddPreferenceScreen(_intentscreen,"intentenable");
 //BA.debugLineNum = 2423;BA.debugLine="cat1.Initialize(\"Examples\")";
_cat1.Initialize("Examples");
 //BA.debugLineNum = 2424;BA.debugLine="cat1.AddCheckBox(\"check1\", \"Checkbox1\", \"This is Checkbox1 without second summary\", \"\", True, \"\")";
_cat1.AddCheckBox("check1","Checkbox1","This is Checkbox1 without second summary","",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 2425;BA.debugLine="cat1.AddEditText(\"edit1\", \"EditText1\", \"This is EditText1\", \"\", \"check1\")";
_cat1.AddEditText("edit1","EditText1","This is EditText1","","check1");
 //BA.debugLineNum = 2426;BA.debugLine="cat1.AddPassword(\"pwd1\", \"Password1\", \"This is a password\", \"\", \"\")";
_cat1.AddPassword("pwd1","Password1","This is a password","","");
 //BA.debugLineNum = 2427;BA.debugLine="cat1.AddRingtone(\"ring1\", \"Ringtone1\", \"This is a Ringtone\", \"\", \"\", cat1.RT_NOTIFICATION)";
_cat1.AddRingtone("ring1","Ringtone1","This is a Ringtone","","",_cat1.RT_NOTIFICATION);
 //BA.debugLineNum = 2429;BA.debugLine="cat2.Initialize(\"Set Background Color\")";
_cat2.Initialize("Set Background Color");
 //BA.debugLineNum = 2430;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 2431;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 2432;BA.debugLine="m.Put(\"black\", \"I want a black background\")";
_m.Put((Object)("black"),(Object)("I want a black background"));
 //BA.debugLineNum = 2433;BA.debugLine="m.Put(\"red\", \"No, make it red\")";
_m.Put((Object)("red"),(Object)("No, make it red"));
 //BA.debugLineNum = 2434;BA.debugLine="m.Put(\"green\", \"I like it green\")";
_m.Put((Object)("green"),(Object)("I like it green"));
 //BA.debugLineNum = 2435;BA.debugLine="m.Put(\"blue\", \"Blue is best\")";
_m.Put((Object)("blue"),(Object)("Blue is best"));
 //BA.debugLineNum = 2436;BA.debugLine="cat2.AddList2(\"Background Color\", \"Choose color\", \"Choose color\", \"black\", \"\", m)";
_cat2.AddList2("Background Color","Choose color","Choose color","black","",_m);
 //BA.debugLineNum = 2442;BA.debugLine="cat1.Initialize(\"Examples\")";
_cat1.Initialize("Examples");
 //BA.debugLineNum = 2443;BA.debugLine="cat1.AddCheckBox(\"check1\", \"Checkbox1\", \"This is Checkbox1 without second summary\", \"\", True, \"\")";
_cat1.AddCheckBox("check1","Checkbox1","This is Checkbox1 without second summary","",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 2444;BA.debugLine="cat1.AddEditText(\"edit1\", \"EditText1\", \"This is EditText1\", \"\", \"check1\")";
_cat1.AddEditText("edit1","EditText1","This is EditText1","","check1");
 //BA.debugLineNum = 2445;BA.debugLine="cat1.AddPassword(\"pwd1\", \"Password1\", \"This is a password\", \"\", \"\")";
_cat1.AddPassword("pwd1","Password1","This is a password","","");
 //BA.debugLineNum = 2446;BA.debugLine="cat1.AddRingtone(\"ring1\", \"Ringtone1\", \"This is a Ringtone\", \"\", \"\", cat1.RT_NOTIFICATION)";
_cat1.AddRingtone("ring1","Ringtone1","This is a Ringtone","","",_cat1.RT_NOTIFICATION);
 //BA.debugLineNum = 2454;BA.debugLine="KatPersonal.Initialize(\"Personalien\")";
_katpersonal.Initialize("Personalien");
 //BA.debugLineNum = 2455;BA.debugLine="KatPersonal.AddEditText(\"Vorname\", \"Vorname\", \"Hier den Vornamen eintragen\", \"Frank\", \"\")";
_katpersonal.AddEditText("Vorname","Vorname","Hier den Vornamen eintragen","Frank","");
 //BA.debugLineNum = 2456;BA.debugLine="KatPersonal.AddEditText(\"Nachname\", \"Nachname\", \"Hier den Nachnamen eintragen\", \"Albrecht\", \"\")";
_katpersonal.AddEditText("Nachname","Nachname","Hier den Nachnamen eintragen","Albrecht","");
 //BA.debugLineNum = 2457;BA.debugLine="KatPersonal.AddEditText(\"Alter\", \"Alter\", \"Hier das Alter eintragen\", \"45\", \"\")";
_katpersonal.AddEditText("Alter","Alter","Hier das Alter eintragen","45","");
 //BA.debugLineNum = 2458;BA.debugLine="KatPersonal.AddEditText(\"Gewicht\", \"Gewicht\", \"Hier das aktuelle Gewicht eintragen\", \"90\", \"\")";
_katpersonal.AddEditText("Gewicht","Gewicht","Hier das aktuelle Gewicht eintragen","90","");
 //BA.debugLineNum = 2459;BA.debugLine="Dim KCAL1 As String";
_kcal1 = "";
 //BA.debugLineNum = 2460;BA.debugLine="KCAL1 = Gewicht * 27";
_kcal1 = BA.NumberToString(_gewicht*27);
 //BA.debugLineNum = 2461;BA.debugLine="KatPersonal.AddEditText(\"kCal\", \"kCal\", \"Hier die notwendigen kCal eintragen\", KCAL1, \"\")";
_katpersonal.AddEditText("kCal","kCal","Hier die notwendigen kCal eintragen",_kcal1,"");
 //BA.debugLineNum = 2462;BA.debugLine="KatPersonal.AddPassword(\"Passwort\", \"Passwort\", \"Hier das Passwort eintragen\", \"\", \"\")";
_katpersonal.AddPassword("Passwort","Passwort","Hier das Passwort eintragen","","");
 //BA.debugLineNum = 2463;BA.debugLine="KatPersonal.AddEditText(\"Telefonnummer\", \"Telefonnummer\", \"Hier die Telefonnummer eintragen\", \"\", \"\")";
_katpersonal.AddEditText("Telefonnummer","Telefonnummer","Hier die Telefonnummer eintragen","","");
 //BA.debugLineNum = 2467;BA.debugLine="KatTraining.Initialize(\"Training\")";
_kattraining.Initialize("Training");
 //BA.debugLineNum = 2468;BA.debugLine="Dim mapIntensität As Map";
_mapintensität = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 2469;BA.debugLine="mapIntensität.Initialize";
_mapintensität.Initialize();
 //BA.debugLineNum = 2470;BA.debugLine="mapIntensität.Put(0, \"Abtrainieren - Modus\")";
_mapintensität.Put((Object)(0),(Object)("Abtrainieren - Modus"));
 //BA.debugLineNum = 2471;BA.debugLine="mapIntensität.Put(1, \"Mobilisieren - Modus\")";
_mapintensität.Put((Object)(1),(Object)("Mobilisieren - Modus"));
 //BA.debugLineNum = 2472;BA.debugLine="mapIntensität.Put(2, \"Ohne Geräte - Modus\")";
_mapintensität.Put((Object)(2),(Object)("Ohne Geräte - Modus"));
 //BA.debugLineNum = 2473;BA.debugLine="mapIntensität.Put(3, \"Fitness - Modus\")";
_mapintensität.Put((Object)(3),(Object)("Fitness - Modus"));
 //BA.debugLineNum = 2474;BA.debugLine="mapIntensität.Put(4, \"Knallhart - Modus\")";
_mapintensität.Put((Object)(4),(Object)("Knallhart - Modus"));
 //BA.debugLineNum = 2475;BA.debugLine="mapIntensität.Put(5, \"Wettkampf - Modus\")";
_mapintensität.Put((Object)(5),(Object)("Wettkampf - Modus"));
 //BA.debugLineNum = 2476;BA.debugLine="mapIntensität.Put(6, \"Bundeslandmeister - Modus\")";
_mapintensität.Put((Object)(6),(Object)("Bundeslandmeister - Modus"));
 //BA.debugLineNum = 2477;BA.debugLine="mapIntensität.Put(7, \"Deutscher Meister - Modus\")";
_mapintensität.Put((Object)(7),(Object)("Deutscher Meister - Modus"));
 //BA.debugLineNum = 2478;BA.debugLine="mapIntensität.Put(8, \"Bruce Lee - Modus\")";
_mapintensität.Put((Object)(8),(Object)("Bruce Lee - Modus"));
 //BA.debugLineNum = 2479;BA.debugLine="mapIntensität.Put(9, \"Europameister - Modus\")";
_mapintensität.Put((Object)(9),(Object)("Europameister - Modus"));
 //BA.debugLineNum = 2480;BA.debugLine="mapIntensität.Put(10, \"Muhammad Ali - WeltmeisterModus\")";
_mapintensität.Put((Object)(10),(Object)("Muhammad Ali - WeltmeisterModus"));
 //BA.debugLineNum = 2481;BA.debugLine="mapIntensität.Put(11, \"Chuck Norris - GalaxyModus\")";
_mapintensität.Put((Object)(11),(Object)("Chuck Norris - GalaxyModus"));
 //BA.debugLineNum = 2482;BA.debugLine="KatTraining.AddList2(\"Intensitaet\", \"Intensität\", \"Wie hart soll das Training sein?\", 3, \"\", mapIntensität)";
_kattraining.AddList2("Intensitaet","Intensität","Wie hart soll das Training sein?",BA.NumberToString(3),"",_mapintensität);
 //BA.debugLineNum = 2486;BA.debugLine="KatNetzwerke.Initialize(\"Netzwerke\")";
_katnetzwerke.Initialize("Netzwerke");
 //BA.debugLineNum = 2487;BA.debugLine="KatNetzwerke.AddCheckBox(\"Facebook\", \"Facebook\", \"Facebook wird genutzt\", \"Facebook wird nicht genutzt\", True, \"\")";
_katnetzwerke.AddCheckBox("Facebook","Facebook","Facebook wird genutzt","Facebook wird nicht genutzt",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 2488;BA.debugLine="intentscreen1.Initialize(\"Facebook Einstellungen\", \"Zugang, Erlaubnis, ...\")";
_intentscreen1.Initialize("Facebook Einstellungen","Zugang, Erlaubnis, ...");
 //BA.debugLineNum = 2512;BA.debugLine="screen.AddPreferenceCategory(intentCat)";
_screen.AddPreferenceCategory(_intentcat);
 //BA.debugLineNum = 2513;BA.debugLine="screen.AddPreferenceCategory(cat1)";
_screen.AddPreferenceCategory(_cat1);
 //BA.debugLineNum = 2514;BA.debugLine="screen.AddPreferenceCategory(cat2)";
_screen.AddPreferenceCategory(_cat2);
 //BA.debugLineNum = 2518;BA.debugLine="screenPersonal.AddPreferenceCategory(KatPersonal)";
_screenpersonal.AddPreferenceCategory(_katpersonal);
 //BA.debugLineNum = 2521;BA.debugLine="screenTraining.AddPreferenceCategory(KatTraining)";
_screentraining.AddPreferenceCategory(_kattraining);
 //BA.debugLineNum = 2527;BA.debugLine="End Sub";
return "";
}
public static String  _setzesetupeinstellungen() throws Exception{
 //BA.debugLineNum = 2529;BA.debugLine="Sub SetzeSetupEinstellungen";
 //BA.debugLineNum = 2531;BA.debugLine="manager.SetBoolean(\"check1\", True)";
_manager.SetBoolean("check1",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 2532;BA.debugLine="manager.SetBoolean(\"check2\", False)";
_manager.SetBoolean("check2",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2533;BA.debugLine="manager.SetString(\"edit1\", \"Hello!\")";
_manager.SetString("edit1","Hello!");
 //BA.debugLineNum = 2534;BA.debugLine="manager.SetString(\"list1\", \"Black\")";
_manager.SetString("list1","Black");
 //BA.debugLineNum = 2535;BA.debugLine="manager.SetString(\"Hintergrundfarbe\", 0)";
_manager.SetString("Hintergrundfarbe",BA.NumberToString(0));
 //BA.debugLineNum = 2536;BA.debugLine="manager.SetString(\"Schriftfarbe\",0)";
_manager.SetString("Schriftfarbe",BA.NumberToString(0));
 //BA.debugLineNum = 2537;BA.debugLine="manager.SetString(\"kCal\",1400)";
_manager.SetString("kCal",BA.NumberToString(1400));
 //BA.debugLineNum = 2538;BA.debugLine="manager.SetString(\"Alter\",47)";
_manager.SetString("Alter",BA.NumberToString(47));
 //BA.debugLineNum = 2539;BA.debugLine="manager.SetString(\"Gewicht\",90)";
_manager.SetString("Gewicht",BA.NumberToString(90));
 //BA.debugLineNum = 2541;BA.debugLine="End Sub";
return "";
}
public static String  _setzezeiterwaermung(int _i) throws Exception{
 //BA.debugLineNum = 1086;BA.debugLine="Sub SetzeZeitErwaermung(i As Int)";
 //BA.debugLineNum = 1088;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 1090;BA.debugLine="Zeit = 20000";
_zeit = (int) (20000);
 break;
case 1:
 //BA.debugLineNum = 1092;BA.debugLine="Zeit = 30000";
_zeit = (int) (30000);
 break;
case 2:
 //BA.debugLineNum = 1094;BA.debugLine="Zeit = 40000";
_zeit = (int) (40000);
 break;
case 3:
 //BA.debugLineNum = 1096;BA.debugLine="Zeit = 50000";
_zeit = (int) (50000);
 break;
case 4:
 //BA.debugLineNum = 1098;BA.debugLine="Zeit = 60000";
_zeit = (int) (60000);
 break;
default:
 //BA.debugLineNum = 1100;BA.debugLine="Zeit = 5000";
_zeit = (int) (5000);
 break;
}
;
 //BA.debugLineNum = 1103;BA.debugLine="End Sub";
return "";
}
public static String  _setzezeitpause(int _i) throws Exception{
 //BA.debugLineNum = 1134;BA.debugLine="Sub SetzeZeitPause(i As Int)";
 //BA.debugLineNum = 1136;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 1138;BA.debugLine="Zeit = 30000";
_zeit = (int) (30000);
 break;
case 1:
 //BA.debugLineNum = 1140;BA.debugLine="Zeit = 40000";
_zeit = (int) (40000);
 break;
case 2:
 //BA.debugLineNum = 1142;BA.debugLine="Zeit = 50000";
_zeit = (int) (50000);
 break;
case 3:
 //BA.debugLineNum = 1144;BA.debugLine="Zeit = 60000";
_zeit = (int) (60000);
 break;
case 4:
 //BA.debugLineNum = 1146;BA.debugLine="Zeit = 90000";
_zeit = (int) (90000);
 break;
case 5:
 //BA.debugLineNum = 1148;BA.debugLine="Zeit = 120000";
_zeit = (int) (120000);
 break;
default:
 //BA.debugLineNum = 1151;BA.debugLine="Zeit = 5000";
_zeit = (int) (5000);
 break;
}
;
 //BA.debugLineNum = 1154;BA.debugLine="End Sub";
return "";
}
public static String  _setzezeittraining(int _i) throws Exception{
 //BA.debugLineNum = 1111;BA.debugLine="Sub SetzeZeitTraining(i As Int)";
 //BA.debugLineNum = 1113;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 1115;BA.debugLine="Zeit = 60000";
_zeit = (int) (60000);
 break;
case 1:
 //BA.debugLineNum = 1117;BA.debugLine="Zeit = 120000";
_zeit = (int) (120000);
 break;
case 2:
 //BA.debugLineNum = 1119;BA.debugLine="Zeit = 180000";
_zeit = (int) (180000);
 break;
case 3:
 //BA.debugLineNum = 1121;BA.debugLine="Zeit = 240000";
_zeit = (int) (240000);
 break;
default:
 //BA.debugLineNum = 1123;BA.debugLine="Zeit = 5000";
_zeit = (int) (5000);
 break;
}
;
 //BA.debugLineNum = 1126;BA.debugLine="End Sub";
return "";
}
public static String  _spnerwaermung_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1081;BA.debugLine="Sub spnErwaermung_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 1082;BA.debugLine="ZeitErwaermung = Position";
_zeiterwaermung = _position;
 //BA.debugLineNum = 1084;BA.debugLine="End Sub";
return "";
}
public static String  _spnpause_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1128;BA.debugLine="Sub spnPause_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 1130;BA.debugLine="ZeitPause = Position";
_zeitpause = _position;
 //BA.debugLineNum = 1132;BA.debugLine="End Sub";
return "";
}
public static String  _spntraining_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1105;BA.debugLine="Sub spnTraining_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 1107;BA.debugLine="ZeitProUebung = Position";
_zeitprouebung = _position;
 //BA.debugLineNum = 1109;BA.debugLine="End Sub";
return "";
}
public static String  _sptheme_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1057;BA.debugLine="Sub spTheme_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 1058;BA.debugLine="CurrentTheme = Position";
_currenttheme = _position;
 //BA.debugLineNum = 1059;BA.debugLine="SetTheme(CurrentTheme)";
_settheme(_currenttheme);
 //BA.debugLineNum = 1060;BA.debugLine="End Sub";
return "";
}
}
