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

public class einstellungentrainingstunde extends Activity implements B4AActivity{
	public static einstellungentrainingstunde mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.einstellungentrainingstunde");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (einstellungentrainingstunde).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.einstellungentrainingstunde");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.einstellungentrainingstunde", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (einstellungentrainingstunde) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (einstellungentrainingstunde) Resume **");
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
		return einstellungentrainingstunde.class;
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
        BA.LogInfo("** Activity (einstellungentrainingstunde) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (einstellungentrainingstunde) Resume **");
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
public static int[] _zeit = null;
public static int _anzahldereinstellungen = 0;
public static int _panelhoehe = 0;
public anywheresoftware.b4a.objects.PanelWrapper _panels = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnspeichern = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnladen = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnhoch = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnweiter = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvhintergrund = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _gesamtzeitanzeige = null;
public static int _gesamttrainingszeit = 0;
public static int _pausen = 0;
public static int _partneruebungen = 0;
public static int _dehnung = 0;
public static int _cooldown = 0;
public static String _setupdatei = "";
public anywheresoftware.b4a.objects.LabelWrapper[] _lbltafel = null;
public anywheresoftware.b4a.objects.LabelWrapper[] _lblzeitanzeige = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.ToggleButtonWrapper[] _toggeljanein = null;
public static int[] _sebzeitvalue = null;
public static int _ausgleichszahl = 0;
public anywheresoftware.b4a.objects.LabelWrapper _lblgesamtzeit = null;
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
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb = null;
anywheresoftware.b4a.objects.collections.Map _map2 = null;
int _result = 0;
anywheresoftware.b4a.objects.LabelWrapper _lblueberschrift = null;
int _i = 0;
 //BA.debugLineNum = 32;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 34;BA.debugLine="Activity.Title = \"Einstellungen Training Stunde\"";
mostCurrent._activity.setTitle((Object)("Einstellungen Training Stunde"));
 //BA.debugLineNum = 36;BA.debugLine="If File.ExternalWritable = False Then";
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 37;BA.debugLine="ToastMessageShow(\"Ich kann nicht von der SD-Card lesen.\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Ich kann nicht von der SD-Card lesen.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 38;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 42;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Dim Map2 As Map";
_map2 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 44;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 45;BA.debugLine="Dim lblUeberschrift As Label";
_lblueberschrift = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Map2.Initialize";
_map2.Initialize();
 //BA.debugLineNum = 48;BA.debugLine="If File.Exists(File.DirRootExternal & Main.Unterordner1, Setupdatei) = True Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner1,mostCurrent._setupdatei)==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 49;BA.debugLine="Map2 = File.ReadMap(File.DirRootExternal & Main.Unterordner1, Setupdatei)";
_map2 = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner1,mostCurrent._setupdatei);
 }else {
 //BA.debugLineNum = 51;BA.debugLine="Msgbox(\"Standardwerte werden genutzt\",\"A C H T U N G\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Standardwerte werden genutzt","A C H T U N G",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 54;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 55;BA.debugLine="sb.Append(\"Die Werte im Speicher sind:\").Append(CRLF)";
_sb.Append("Die Werte im Speicher sind:").Append(anywheresoftware.b4a.keywords.Common.CRLF);
 //BA.debugLineNum = 56;BA.debugLine="For i = 0 To Map2.Size - 1";
{
final int step42 = 1;
final int limit42 = (int) (_map2.getSize()-1);
for (_i = (int) (0); (step42 > 0 && _i <= limit42) || (step42 < 0 && _i >= limit42); _i = ((int)(0 + _i + step42))) {
 //BA.debugLineNum = 58;BA.debugLine="sb.Append(Map2.GetKeyAt(i)).append(\" = \").Append(Map2.GetValueAt(i)).append(CRLF)";
_sb.Append(BA.ObjectToString(_map2.GetKeyAt(_i))).Append(" = ").Append(BA.ObjectToString(_map2.GetValueAt(_i))).Append(anywheresoftware.b4a.keywords.Common.CRLF);
 }
};
 //BA.debugLineNum = 62;BA.debugLine="sebZeitValue(0) = Map2.GetDefault(\"Gesamttrainingzeit\",3600000)";
_sebzeitvalue[(int) (0)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("Gesamttrainingzeit"),(Object)(3600000))));
 //BA.debugLineNum = 63;BA.debugLine="sebZeitValue(1) = Map2.GetDefault(\"ErwaermungAllgemein\",600000)";
_sebzeitvalue[(int) (1)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("ErwaermungAllgemein"),(Object)(600000))));
 //BA.debugLineNum = 64;BA.debugLine="sebZeitValue(2) = Map2.GetDefault(\"ErwaermungAllgemeinUebung\",15000)";
_sebzeitvalue[(int) (2)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("ErwaermungAllgemeinUebung"),(Object)(15000))));
 //BA.debugLineNum = 65;BA.debugLine="sebZeitValue(3) = Map2.GetDefault(\"ErwaermungSpeziell\",900000)";
_sebzeitvalue[(int) (3)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("ErwaermungSpeziell"),(Object)(900000))));
 //BA.debugLineNum = 66;BA.debugLine="sebZeitValue(4) = Map2.GetDefault(\"ErwaermungSpeziellUebung\",15000)";
_sebzeitvalue[(int) (4)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("ErwaermungSpeziellUebung"),(Object)(15000))));
 //BA.debugLineNum = 67;BA.debugLine="sebZeitValue(5) = Map2.GetDefault(\"EinzelUebungen\",600000)";
_sebzeitvalue[(int) (5)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("EinzelUebungen"),(Object)(600000))));
 //BA.debugLineNum = 68;BA.debugLine="sebZeitValue(6) = Map2.GetDefault(\"EinzelUebungenPausen\",60000)";
_sebzeitvalue[(int) (6)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("EinzelUebungenPausen"),(Object)(60000))));
 //BA.debugLineNum = 69;BA.debugLine="sebZeitValue(7) = Map2.GetDefault(\"EinzelUebungenUebungen\",60000)";
_sebzeitvalue[(int) (7)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("EinzelUebungenUebungen"),(Object)(60000))));
 //BA.debugLineNum = 70;BA.debugLine="sebZeitValue(8) = Map2.GetDefault(\"PartnerUebungen\",600000)";
_sebzeitvalue[(int) (8)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("PartnerUebungen"),(Object)(600000))));
 //BA.debugLineNum = 71;BA.debugLine="sebZeitValue(9) = Map2.GetDefault(\"PartnerUebungenPausen\",60000)";
_sebzeitvalue[(int) (9)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("PartnerUebungenPausen"),(Object)(60000))));
 //BA.debugLineNum = 72;BA.debugLine="sebZeitValue(10) = Map2.GetDefault(\"PartnerUebungenUebungen\",60000)";
_sebzeitvalue[(int) (10)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("PartnerUebungenUebungen"),(Object)(60000))));
 //BA.debugLineNum = 73;BA.debugLine="sebZeitValue(11) = Map2.GetDefault(\"KraftUebungen\",600000)";
_sebzeitvalue[(int) (11)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("KraftUebungen"),(Object)(600000))));
 //BA.debugLineNum = 74;BA.debugLine="sebZeitValue(12) = Map2.GetDefault(\"KraftUebungenPausen\",60000)";
_sebzeitvalue[(int) (12)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("KraftUebungenPausen"),(Object)(60000))));
 //BA.debugLineNum = 75;BA.debugLine="sebZeitValue(13) = Map2.GetDefault(\"KraftUebungenUebungen\",120000)";
_sebzeitvalue[(int) (13)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("KraftUebungenUebungen"),(Object)(120000))));
 //BA.debugLineNum = 76;BA.debugLine="sebZeitValue(14) = Map2.GetDefault(\"Dehnung\",300000)";
_sebzeitvalue[(int) (14)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("Dehnung"),(Object)(300000))));
 //BA.debugLineNum = 77;BA.debugLine="sebZeitValue(15) = Map2.GetDefault(\"DehnungUebung\",15000)";
_sebzeitvalue[(int) (15)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("DehnungUebung"),(Object)(15000))));
 //BA.debugLineNum = 78;BA.debugLine="sebZeitValue(16) = Map2.GetDefault(\"CoolDown\",300000)";
_sebzeitvalue[(int) (16)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("CoolDown"),(Object)(300000))));
 //BA.debugLineNum = 79;BA.debugLine="sebZeitValue(17) = Map2.GetDefault(\"CooldownUebung\",30000)";
_sebzeitvalue[(int) (17)] = (int)(BA.ObjectToNumber(_map2.GetDefault((Object)("CooldownUebung"),(Object)(30000))));
 //BA.debugLineNum = 82;BA.debugLine="Panels.Initialize(\"panels\")";
mostCurrent._panels.Initialize(mostCurrent.activityBA,"panels");
 //BA.debugLineNum = 83;BA.debugLine="Panels.Color = Colors.Black ' RGB(Rnd(250, 255), Rnd(250, 255), Rnd(250, 255))";
mostCurrent._panels.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 84;BA.debugLine="lblUeberschrift.Initialize(\"\")";
_lblueberschrift.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 85;BA.debugLine="lblUeberschrift.Text = \"Einstellungen\" & \" Kickboxen\"";
_lblueberschrift.setText((Object)("Einstellungen"+" Kickboxen"));
 //BA.debugLineNum = 86;BA.debugLine="lblUeberschrift.Gravity = Gravity.CENTER_HORIZONTAL";
_lblueberschrift.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 87;BA.debugLine="lblUeberschrift.TextSize = 25";
_lblueberschrift.setTextSize((float) (25));
 //BA.debugLineNum = 88;BA.debugLine="lblUeberschrift.TextColor = Colors.White";
_lblueberschrift.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 89;BA.debugLine="Panels.AddView(lblUeberschrift, 5dip, 10dip, 100%x - 5dip, 60dip)";
mostCurrent._panels.AddView((android.view.View)(_lblueberschrift.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 90;BA.debugLine="Activity.AddView(Panels, 0, 0, 100%x, 100%y) 'add the panel to the layout";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panels.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 93;BA.debugLine="scvHintergrund.Initialize(500)";
mostCurrent._scvhintergrund.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 94;BA.debugLine="Panels.AddView(scvHintergrund, 0, 50dip, 100%x - 5dip, 100%y - 150dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._scvhintergrund.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (150))));
 //BA.debugLineNum = 97;BA.debugLine="btnSpeichern.Initialize (\"btnSpeichern\")";
mostCurrent._btnspeichern.Initialize(mostCurrent.activityBA,"btnSpeichern");
 //BA.debugLineNum = 98;BA.debugLine="btnHoch.Initialize (\"btnHoch\")";
mostCurrent._btnhoch.Initialize(mostCurrent.activityBA,"btnHoch");
 //BA.debugLineNum = 99;BA.debugLine="btnWeiter.Initialize (\"btnWeiter\")";
mostCurrent._btnweiter.Initialize(mostCurrent.activityBA,"btnWeiter");
 //BA.debugLineNum = 100;BA.debugLine="GesamtzeitAnzeige.Initialize (\"Prozessbar1\")";
mostCurrent._gesamtzeitanzeige.Initialize(mostCurrent.activityBA,"Prozessbar1");
 //BA.debugLineNum = 101;BA.debugLine="lblGesamtzeit.Initialize(\"lblGesamtzeit\")";
mostCurrent._lblgesamtzeit.Initialize(mostCurrent.activityBA,"lblGesamtzeit");
 //BA.debugLineNum = 102;BA.debugLine="GesamtzeitAnzeige.Progress = 50";
mostCurrent._gesamtzeitanzeige.setProgress((int) (50));
 //BA.debugLineNum = 104;BA.debugLine="btnSpeichern.Tag=1";
mostCurrent._btnspeichern.setTag((Object)(1));
 //BA.debugLineNum = 105;BA.debugLine="btnHoch.Tag=2";
mostCurrent._btnhoch.setTag((Object)(2));
 //BA.debugLineNum = 106;BA.debugLine="btnWeiter.Tag=3";
mostCurrent._btnweiter.setTag((Object)(3));
 //BA.debugLineNum = 107;BA.debugLine="btnSpeichern.Text=\"Speichern\"";
mostCurrent._btnspeichern.setText((Object)("Speichern"));
 //BA.debugLineNum = 108;BA.debugLine="btnHoch.Text=\"Nach Oben\"";
mostCurrent._btnhoch.setText((Object)("Nach Oben"));
 //BA.debugLineNum = 109;BA.debugLine="btnWeiter.Text=\"Weiter\"";
mostCurrent._btnweiter.setText((Object)("Weiter"));
 //BA.debugLineNum = 111;BA.debugLine="lblGesamtzeit.Gravity = Gravity.CENTER_HORIZONTAL";
mostCurrent._lblgesamtzeit.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 112;BA.debugLine="lblGesamtzeit.TextColor = Colors.black";
mostCurrent._lblgesamtzeit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 113;BA.debugLine="lblGesamtzeit.TextSize = 18";
mostCurrent._lblgesamtzeit.setTextSize((float) (18));
 //BA.debugLineNum = 116;BA.debugLine="Panels.AddView (GesamtzeitAnzeige, 10, 100%y - 80dip, 100%x - 20dip, 25dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._gesamtzeitanzeige.getObject()),(int) (10),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80))),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25)));
 //BA.debugLineNum = 117;BA.debugLine="Panels.AddView (lblGesamtzeit, 10, 100%y - 80dip, 100%x - 20dip, 25dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._lblgesamtzeit.getObject()),(int) (10),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80))),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25)));
 //BA.debugLineNum = 118;BA.debugLine="Panels.AddView (btnSpeichern, 0, 100%y - 50dip, 33%x, 50dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._btnspeichern.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 119;BA.debugLine="Panels.AddView (btnHoch, 33%x, 100%y - 50dip, 33%x, 50dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._btnhoch.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 120;BA.debugLine="Panels.AddView (btnWeiter, 66%X, 100%y - 50dip, 33%x, 50dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._btnweiter.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (66),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 123;BA.debugLine="Listenerzeugung";
_listenerzeugung();
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
return "";
}
public static String  _btnhoch_click() throws Exception{
 //BA.debugLineNum = 406;BA.debugLine="Sub btnHoch_click";
 //BA.debugLineNum = 408;BA.debugLine="scvHintergrund.ScrollPosition = 0";
mostCurrent._scvhintergrund.setScrollPosition((int) (0));
 //BA.debugLineNum = 409;BA.debugLine="End Sub";
return "";
}
public static String  _btnspeichern_click() throws Exception{
int _result = 0;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 364;BA.debugLine="Sub btnSpeichern_Click";
 //BA.debugLineNum = 368;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 370;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 371;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 372;BA.debugLine="Map1.Put(\"Gesamttrainingzeit\",Zeit(0))";
_map1.Put((Object)("Gesamttrainingzeit"),(Object)(_zeit[(int) (0)]));
 //BA.debugLineNum = 373;BA.debugLine="Map1.Put(\"ErwaermungAllgemein\",Zeit(1))";
_map1.Put((Object)("ErwaermungAllgemein"),(Object)(_zeit[(int) (1)]));
 //BA.debugLineNum = 374;BA.debugLine="Map1.Put(\"ErwaermungAllgemeinUebung\",Zeit(2))";
_map1.Put((Object)("ErwaermungAllgemeinUebung"),(Object)(_zeit[(int) (2)]));
 //BA.debugLineNum = 375;BA.debugLine="Map1.Put(\"ErwaermungSpeziell\",Zeit(3))";
_map1.Put((Object)("ErwaermungSpeziell"),(Object)(_zeit[(int) (3)]));
 //BA.debugLineNum = 376;BA.debugLine="Map1.Put(\"ErwaermungSpeziellUebung\",Zeit(4))";
_map1.Put((Object)("ErwaermungSpeziellUebung"),(Object)(_zeit[(int) (4)]));
 //BA.debugLineNum = 377;BA.debugLine="Map1.Put(\"EinzelUebungen\",Zeit(5))";
_map1.Put((Object)("EinzelUebungen"),(Object)(_zeit[(int) (5)]));
 //BA.debugLineNum = 378;BA.debugLine="Map1.Put(\"EinzelUebungenPausen\",Zeit(6))";
_map1.Put((Object)("EinzelUebungenPausen"),(Object)(_zeit[(int) (6)]));
 //BA.debugLineNum = 379;BA.debugLine="Map1.Put(\"EinzelUebungenUebungen\",Zeit(7))";
_map1.Put((Object)("EinzelUebungenUebungen"),(Object)(_zeit[(int) (7)]));
 //BA.debugLineNum = 380;BA.debugLine="Map1.Put(\"PartnerUebungen\",Zeit(8))";
_map1.Put((Object)("PartnerUebungen"),(Object)(_zeit[(int) (8)]));
 //BA.debugLineNum = 381;BA.debugLine="Map1.Put(\"PartnerUebungenPausen\",Zeit(9))";
_map1.Put((Object)("PartnerUebungenPausen"),(Object)(_zeit[(int) (9)]));
 //BA.debugLineNum = 382;BA.debugLine="Map1.Put(\"PartnerUebungenUebungen\",Zeit(10))";
_map1.Put((Object)("PartnerUebungenUebungen"),(Object)(_zeit[(int) (10)]));
 //BA.debugLineNum = 383;BA.debugLine="Map1.Put(\"KraftUebungen\",Zeit(11))";
_map1.Put((Object)("KraftUebungen"),(Object)(_zeit[(int) (11)]));
 //BA.debugLineNum = 384;BA.debugLine="Map1.Put(\"KraftUebungenPausen\",Zeit(12))";
_map1.Put((Object)("KraftUebungenPausen"),(Object)(_zeit[(int) (12)]));
 //BA.debugLineNum = 385;BA.debugLine="Map1.Put(\"KraftUebungenUebungen\",Zeit(13))";
_map1.Put((Object)("KraftUebungenUebungen"),(Object)(_zeit[(int) (13)]));
 //BA.debugLineNum = 386;BA.debugLine="Map1.Put(\"Dehnung\",Zeit(14))";
_map1.Put((Object)("Dehnung"),(Object)(_zeit[(int) (14)]));
 //BA.debugLineNum = 387;BA.debugLine="Map1.Put(\"DehnungUebung\",Zeit(15))";
_map1.Put((Object)("DehnungUebung"),(Object)(_zeit[(int) (15)]));
 //BA.debugLineNum = 388;BA.debugLine="Map1.Put(\"CoolDown\",Zeit(16))";
_map1.Put((Object)("CoolDown"),(Object)(_zeit[(int) (16)]));
 //BA.debugLineNum = 389;BA.debugLine="Map1.Put(\"CooldownUebung\",Zeit(17))";
_map1.Put((Object)("CooldownUebung"),(Object)(_zeit[(int) (17)]));
 //BA.debugLineNum = 392;BA.debugLine="result = Msgbox2(\" Gesamttrainingzeit= \" & (Zeit(0)/60000) & CRLF & \" ErwaermungAllgemein= \" & (Zeit(1)/60000) & CRLF & \" ErwaermungAllgemeinUebung= \" & (Zeit(2)/60000) & CRLF & \" ErwaermungSpeziell= \" & (Zeit(3)/60000) & CRLF & \" ErwaermungSpeziellUebung= \" & (Zeit(4)/60000) & CRLF & \" EinzelUebungen= \" & (Zeit(5)/60000) & CRLF & \" EinzelUebungenPausen= \" & (Zeit(6)/60000) & CRLF & \" EinzelUebungenUebungen= \" & (Zeit(7)/60000) & CRLF & \" PartnerUebungen= \" & (Zeit(8)/60000) & CRLF & \" PartnerUebungenPausen= \" & (Zeit(9)/60000) & CRLF & \" PartnerUebungenUebungen= \" & (Zeit(10)/60000) & CRLF & \" KraftUebungen= \" & (Zeit(11)/60000) & CRLF & \" KraftUebungenPausen= \" & (Zeit(12)/60000) & CRLF & \" KraftUebungenUebungen= \" & (Zeit(13)/60000) & CRLF & \" Dehnung= \" & (Zeit(14)/60000) & CRLF & \" DehnungUebung= \" & (Zeit(15)/60000) & CRLF & \" CoolDown= \" & (Zeit(16)/60000) & CRLF & \" CooldownUebung= \" & (Zeit(17)/60000), \"Speichern und Weiter\" , \"Ja\",\"Nein\",\"\",LoadBitmap(File.DirAssets,\"mamaLogo.png\"))";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(" Gesamttrainingzeit= "+BA.NumberToString((_zeit[(int) (0)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" ErwaermungAllgemein= "+BA.NumberToString((_zeit[(int) (1)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" ErwaermungAllgemeinUebung= "+BA.NumberToString((_zeit[(int) (2)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" ErwaermungSpeziell= "+BA.NumberToString((_zeit[(int) (3)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" ErwaermungSpeziellUebung= "+BA.NumberToString((_zeit[(int) (4)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" EinzelUebungen= "+BA.NumberToString((_zeit[(int) (5)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" EinzelUebungenPausen= "+BA.NumberToString((_zeit[(int) (6)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" EinzelUebungenUebungen= "+BA.NumberToString((_zeit[(int) (7)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" PartnerUebungen= "+BA.NumberToString((_zeit[(int) (8)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" PartnerUebungenPausen= "+BA.NumberToString((_zeit[(int) (9)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" PartnerUebungenUebungen= "+BA.NumberToString((_zeit[(int) (10)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" KraftUebungen= "+BA.NumberToString((_zeit[(int) (11)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" KraftUebungenPausen= "+BA.NumberToString((_zeit[(int) (12)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" KraftUebungenUebungen= "+BA.NumberToString((_zeit[(int) (13)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" Dehnung= "+BA.NumberToString((_zeit[(int) (14)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" DehnungUebung= "+BA.NumberToString((_zeit[(int) (15)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" CoolDown= "+BA.NumberToString((_zeit[(int) (16)]/(double)60000))+anywheresoftware.b4a.keywords.Common.CRLF+" CooldownUebung= "+BA.NumberToString((_zeit[(int) (17)]/(double)60000)),"Speichern und Weiter","Ja","Nein","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mamaLogo.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 395;BA.debugLine="If result = DialogResponse.POSITIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 396;BA.debugLine="File.WriteMap(File.DirRootExternal & Main.Unterordner1, Setupdatei, Map1)";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner1,mostCurrent._setupdatei,_map1);
 //BA.debugLineNum = 397;BA.debugLine="ToastMessageShow(\"Erfolgreich gespeichert\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Erfolgreich gespeichert",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 398;BA.debugLine="StartActivity(Training)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._training.getObject()));
 }else {
 };
 //BA.debugLineNum = 402;BA.debugLine="End Sub";
return "";
}
public static String  _btnweiter_click() throws Exception{
 //BA.debugLineNum = 412;BA.debugLine="Sub btnWeiter_Click";
 //BA.debugLineNum = 413;BA.debugLine="StartActivity(Training)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._training.getObject()));
 //BA.debugLineNum = 414;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 14;BA.debugLine="Dim AnzahlDerEinstellungen As Int			: AnzahlDerEinstellungen = 18 ' auch bei Zeit oben ändern!!!!";
_anzahldereinstellungen = 0;
 //BA.debugLineNum = 14;BA.debugLine="Dim AnzahlDerEinstellungen As Int			: AnzahlDerEinstellungen = 18 ' auch bei Zeit oben ändern!!!!";
_anzahldereinstellungen = (int) (18);
 //BA.debugLineNum = 15;BA.debugLine="Dim Panelhoehe As Int						: Panelhoehe = 200dip";
_panelhoehe = 0;
 //BA.debugLineNum = 15;BA.debugLine="Dim Panelhoehe As Int						: Panelhoehe = 200dip";
_panelhoehe = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (200));
 //BA.debugLineNum = 16;BA.debugLine="Dim Panels As Panel";
mostCurrent._panels = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim btnSpeichern, btnLaden, btnHoch, btnWeiter As Button";
mostCurrent._btnspeichern = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnladen = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnhoch = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnweiter = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim scvHintergrund As ScrollView";
mostCurrent._scvhintergrund = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim GesamtzeitAnzeige As ProgressBar";
mostCurrent._gesamtzeitanzeige = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim Gesamttrainingszeit As Int";
_gesamttrainingszeit = 0;
 //BA.debugLineNum = 21;BA.debugLine="Dim Pausen, Partneruebungen, Dehnung, CoolDown As Int";
_pausen = 0;
_partneruebungen = 0;
_dehnung = 0;
_cooldown = 0;
 //BA.debugLineNum = 22;BA.debugLine="Dim Setupdatei As String					:	Setupdatei = \"EinstellungenTrainingStunde.txt\"";
mostCurrent._setupdatei = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim Setupdatei As String					:	Setupdatei = \"EinstellungenTrainingStunde.txt\"";
mostCurrent._setupdatei = "EinstellungenTrainingStunde.txt";
 //BA.debugLineNum = 23;BA.debugLine="Dim lblTafel(AnzahlDerEinstellungen) As Label";
mostCurrent._lbltafel = new anywheresoftware.b4a.objects.LabelWrapper[_anzahldereinstellungen];
{
int d0 = mostCurrent._lbltafel.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._lbltafel[i0] = new anywheresoftware.b4a.objects.LabelWrapper();
}
}
;
 //BA.debugLineNum = 24;BA.debugLine="Dim lblZeitanzeige(AnzahlDerEinstellungen) As Label";
mostCurrent._lblzeitanzeige = new anywheresoftware.b4a.objects.LabelWrapper[_anzahldereinstellungen];
{
int d0 = mostCurrent._lblzeitanzeige.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._lblzeitanzeige[i0] = new anywheresoftware.b4a.objects.LabelWrapper();
}
}
;
 //BA.debugLineNum = 25;BA.debugLine="Dim ToggelJaNein(AnzahlDerEinstellungen) As ToggleButton";
mostCurrent._toggeljanein = new anywheresoftware.b4a.objects.CompoundButtonWrapper.ToggleButtonWrapper[_anzahldereinstellungen];
{
int d0 = mostCurrent._toggeljanein.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._toggeljanein[i0] = new anywheresoftware.b4a.objects.CompoundButtonWrapper.ToggleButtonWrapper();
}
}
;
 //BA.debugLineNum = 26;BA.debugLine="Dim sebZeitValue(19) As Int";
_sebzeitvalue = new int[(int) (19)];
;
 //BA.debugLineNum = 27;BA.debugLine="Dim Ausgleichszahl As Int 					: Ausgleichszahl = -3599950";
_ausgleichszahl = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim Ausgleichszahl As Int 					: Ausgleichszahl = -3599950";
_ausgleichszahl = (int) (-3599950);
 //BA.debugLineNum = 28;BA.debugLine="Dim lblGesamtzeit As Label";
mostCurrent._lblgesamtzeit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _listenerzeugung() throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.PanelWrapper _pnleinheit = null;
anywheresoftware.b4a.objects.EditTextWrapper _edteingabefeld = null;
anywheresoftware.b4a.objects.SeekBarWrapper[] _sebzeit = null;
 //BA.debugLineNum = 129;BA.debugLine="Sub Listenerzeugung";
 //BA.debugLineNum = 130;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 132;BA.debugLine="For i = 0 To AnzahlDerEinstellungen - 1 'originalzeile";
{
final int step98 = 1;
final int limit98 = (int) (_anzahldereinstellungen-1);
for (_i = (int) (0); (step98 > 0 && _i <= limit98) || (step98 < 0 && _i >= limit98); _i = ((int)(0 + _i + step98))) {
 //BA.debugLineNum = 134;BA.debugLine="Dim pnlEinheit As Panel";
_pnleinheit = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 135;BA.debugLine="Dim edtEingabefeld As EditText";
_edteingabefeld = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 136;BA.debugLine="Dim sebZeit(AnzahlDerEinstellungen) As SeekBar";
_sebzeit = new anywheresoftware.b4a.objects.SeekBarWrapper[_anzahldereinstellungen];
{
int d0 = _sebzeit.length;
for (int i0 = 0;i0 < d0;i0++) {
_sebzeit[i0] = new anywheresoftware.b4a.objects.SeekBarWrapper();
}
}
;
 //BA.debugLineNum = 141;BA.debugLine="pnlEinheit.Initialize(\"pnlEinheit\")";
_pnleinheit.Initialize(mostCurrent.activityBA,"pnlEinheit");
 //BA.debugLineNum = 142;BA.debugLine="scvHintergrund.Panel.AddView(pnlEinheit, 0 , i * Panelhoehe, 100%x - 5dip, Panelhoehe)'5dip + i * Panelhoehe, 100%x - 5dip, Panelhoehe)";
mostCurrent._scvhintergrund.getPanel().AddView((android.view.View)(_pnleinheit.getObject()),(int) (0),(int) (_i*_panelhoehe),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),_panelhoehe);
 //BA.debugLineNum = 143;BA.debugLine="pnlEinheit.Tag = i";
_pnleinheit.setTag((Object)(_i));
 //BA.debugLineNum = 146;BA.debugLine="lblTafel(i).Initialize(\"lblTafel\")";
mostCurrent._lbltafel[_i].Initialize(mostCurrent.activityBA,"lblTafel");
 //BA.debugLineNum = 147;BA.debugLine="pnlEinheit.AddView(lblTafel(i), 3%x, 0dip, 95%x, Panelhoehe- 5dip)";
_pnleinheit.AddView((android.view.View)(mostCurrent._lbltafel[_i].getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (95),mostCurrent.activityBA),(int) (_panelhoehe-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 148;BA.debugLine="lblTafel(i).Tag=i";
mostCurrent._lbltafel[_i].setTag((Object)(_i));
 //BA.debugLineNum = 149;BA.debugLine="lblTafel(i).TextSize=20";
mostCurrent._lbltafel[_i].setTextSize((float) (20));
 //BA.debugLineNum = 150;BA.debugLine="lblTafel(i).color = Colors.LightGray";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 151;BA.debugLine="lblTafel(i).textColor = Colors.Black";
mostCurrent._lbltafel[_i].setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 152;BA.debugLine="lblTafel(i).Text = i";
mostCurrent._lbltafel[_i].setText((Object)(_i));
 //BA.debugLineNum = 155;BA.debugLine="ToggelJaNein(i).Initialize(\"ToggelJaNein\")";
mostCurrent._toggeljanein[_i].Initialize(mostCurrent.activityBA,"ToggelJaNein");
 //BA.debugLineNum = 156;BA.debugLine="ToggelJaNein(i).Checked = True";
mostCurrent._toggeljanein[_i].setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 157;BA.debugLine="ToggelJaNein(i).TextOff = \"AUS\"";
mostCurrent._toggeljanein[_i].setTextOff("AUS");
 //BA.debugLineNum = 158;BA.debugLine="ToggelJaNein(i).TextOn = \"AN\"";
mostCurrent._toggeljanein[_i].setTextOn("AN");
 //BA.debugLineNum = 160;BA.debugLine="If i =0 Then";
if (_i==0) { 
 //BA.debugLineNum = 162;BA.debugLine="sebZeit(i).Initialize(\"SeekbarZeit\")";
_sebzeit[_i].Initialize(mostCurrent.activityBA,"SeekbarZeit");
 //BA.debugLineNum = 163;BA.debugLine="pnlEinheit.AddView(sebZeit(i), 5%x, 140dip, 90%x, 30dip)";
_pnleinheit.AddView((android.view.View)(_sebzeit[_i].getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 164;BA.debugLine="sebZeit(i).Tag = i";
_sebzeit[_i].setTag((Object)(_i));
 }else {
 //BA.debugLineNum = 170;BA.debugLine="If i = 1 OR i = 3 OR i = 5 OR i = 8 OR i = 11 OR i = 14 OR i = 16 Then";
if (_i==1 || _i==3 || _i==5 || _i==8 || _i==11 || _i==14 || _i==16) { 
 //BA.debugLineNum = 175;BA.debugLine="pnlEinheit.AddView (ToggelJaNein(i), 30%x, 65dip, 70dip, 60dip)";
_pnleinheit.AddView((android.view.View)(mostCurrent._toggeljanein[_i].getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (65)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (70)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 }else {
 };
 //BA.debugLineNum = 181;BA.debugLine="sebZeit(i).Initialize(\"SeekbarZeit\")";
_sebzeit[_i].Initialize(mostCurrent.activityBA,"SeekbarZeit");
 //BA.debugLineNum = 182;BA.debugLine="pnlEinheit.AddView(sebZeit(i), 30%x, 140dip, 65%x,30dip)";
_pnleinheit.AddView((android.view.View)(_sebzeit[_i].getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (65),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 183;BA.debugLine="sebZeit(i).Tag = i";
_sebzeit[_i].setTag((Object)(_i));
 };
 //BA.debugLineNum = 188;BA.debugLine="lblZeitanzeige(i).Initialize(\"lblZeitanzeige\")";
mostCurrent._lblzeitanzeige[_i].Initialize(mostCurrent.activityBA,"lblZeitanzeige");
 //BA.debugLineNum = 189;BA.debugLine="pnlEinheit.AddView(lblZeitanzeige(i), 30%x + 75dip, 75dip, 44%x ,40dip)";
_pnleinheit.AddView((android.view.View)(mostCurrent._lblzeitanzeige[_i].getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA)+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (75))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (75)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (44),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 190;BA.debugLine="lblZeitanzeige(i).tag = i";
mostCurrent._lblzeitanzeige[_i].setTag((Object)(_i));
 //BA.debugLineNum = 191;BA.debugLine="lblZeitanzeige(i).Color = Colors.black";
mostCurrent._lblzeitanzeige[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 192;BA.debugLine="lblZeitanzeige(i).TextSize = 30";
mostCurrent._lblzeitanzeige[_i].setTextSize((float) (30));
 //BA.debugLineNum = 193;BA.debugLine="lblZeitanzeige(i).Gravity = Gravity.CENTER";
mostCurrent._lblzeitanzeige[_i].setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 196;BA.debugLine="DateTime.TimeFormat = \"HH:mm:ss\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("HH:mm:ss");
 //BA.debugLineNum = 200;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 203;BA.debugLine="lblTafel(i).Color = Colors.White";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 204;BA.debugLine="sebZeit(i).Max = 120 * 60000";
_sebzeit[_i].setMax((int) (120*60000));
 //BA.debugLineNum = 205;BA.debugLine="sebZeit(i).Value = sebZeitValue(0)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (0)]);
 //BA.debugLineNum = 206;BA.debugLine="lblTafel(i).Text = \"  Gesamttrainingszeit: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Gesamttrainingszeit: "));
 //BA.debugLineNum = 207;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 1:
 //BA.debugLineNum = 209;BA.debugLine="lblTafel(i).Color = Colors.Green";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 210;BA.debugLine="sebZeit(i).Max = 20 * 60000";
_sebzeit[_i].setMax((int) (20*60000));
 //BA.debugLineNum = 211;BA.debugLine="sebZeit(i).Value = sebZeitValue(1)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (1)]);
 //BA.debugLineNum = 212;BA.debugLine="lblTafel(i).Text = \"  Erwärmung Allgemein: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Erwärmung Allgemein: "));
 //BA.debugLineNum = 213;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 2:
 //BA.debugLineNum = 217;BA.debugLine="lblTafel(i).Color = Colors.Green";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 218;BA.debugLine="sebZeit(i).Max = 40000";
_sebzeit[_i].setMax((int) (40000));
 //BA.debugLineNum = 219;BA.debugLine="sebZeit(i).Value = sebZeitValue(2)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (2)]);
 //BA.debugLineNum = 220;BA.debugLine="lblTafel(i).Text = \"  Zeit pro Erwärmungsübung(A): \"";
mostCurrent._lbltafel[_i].setText((Object)("  Zeit pro Erwärmungsübung(A): "));
 //BA.debugLineNum = 221;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 3:
 //BA.debugLineNum = 224;BA.debugLine="lblTafel(i).Color = Colors.Yellow";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 225;BA.debugLine="sebZeit(i).Max = 20 * 60000";
_sebzeit[_i].setMax((int) (20*60000));
 //BA.debugLineNum = 226;BA.debugLine="sebZeit(i).Value = sebZeitValue(3)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (3)]);
 //BA.debugLineNum = 227;BA.debugLine="lblTafel(i).Text = \"  Erwärmung Speziell\"";
mostCurrent._lbltafel[_i].setText((Object)("  Erwärmung Speziell"));
 //BA.debugLineNum = 228;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 4:
 //BA.debugLineNum = 232;BA.debugLine="lblTafel(i).Color = Colors.Yellow";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 233;BA.debugLine="sebZeit(i).Max = 60000";
_sebzeit[_i].setMax((int) (60000));
 //BA.debugLineNum = 234;BA.debugLine="sebZeit(i).Value = sebZeitValue(4)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (4)]);
 //BA.debugLineNum = 235;BA.debugLine="lblTafel(i).Text = \"  Zeit pro Erwärmungsübung(S) \"";
mostCurrent._lbltafel[_i].setText((Object)("  Zeit pro Erwärmungsübung(S) "));
 //BA.debugLineNum = 236;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 5:
 //BA.debugLineNum = 241;BA.debugLine="lblTafel(i).Color = Colors.Red";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 242;BA.debugLine="sebZeit(i).Max = 60 * 60000";
_sebzeit[_i].setMax((int) (60*60000));
 //BA.debugLineNum = 243;BA.debugLine="sebZeit(i).Value = sebZeitValue(5)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (5)]);
 //BA.debugLineNum = 244;BA.debugLine="lblTafel(i).Text = \"  Einzelübungen: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Einzelübungen: "));
 //BA.debugLineNum = 245;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 6:
 //BA.debugLineNum = 248;BA.debugLine="lblTafel(i).Color = Colors.Red";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 249;BA.debugLine="sebZeit(i).Max = 3 * 60000";
_sebzeit[_i].setMax((int) (3*60000));
 //BA.debugLineNum = 250;BA.debugLine="sebZeit(i).Value = sebZeitValue(6)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (6)]);
 //BA.debugLineNum = 251;BA.debugLine="lblTafel(i).Text = \"  Pausen zwischen Übungen: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Pausen zwischen Übungen: "));
 //BA.debugLineNum = 252;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 7:
 //BA.debugLineNum = 256;BA.debugLine="lblTafel(i).Color = Colors.Red";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 257;BA.debugLine="sebZeit(i).Max = 5 * 60000";
_sebzeit[_i].setMax((int) (5*60000));
 //BA.debugLineNum = 258;BA.debugLine="sebZeit(i).Value = sebZeitValue(7)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (7)]);
 //BA.debugLineNum = 259;BA.debugLine="lblTafel(i).Text = \"  Zeit pro Übung: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Zeit pro Übung: "));
 //BA.debugLineNum = 260;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 8:
 //BA.debugLineNum = 264;BA.debugLine="lblTafel(i).Color = Colors.Blue";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 265;BA.debugLine="sebZeit(i).Max = 60 * 60000";
_sebzeit[_i].setMax((int) (60*60000));
 //BA.debugLineNum = 266;BA.debugLine="sebZeit(i).Value = sebZeitValue(8)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (8)]);
 //BA.debugLineNum = 267;BA.debugLine="lblTafel(i).Text = \"  Partnerübungen: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Partnerübungen: "));
 //BA.debugLineNum = 268;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 9:
 //BA.debugLineNum = 271;BA.debugLine="lblTafel(i).Color = Colors.Blue";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 272;BA.debugLine="sebZeit(i).Max = 3 * 60000";
_sebzeit[_i].setMax((int) (3*60000));
 //BA.debugLineNum = 273;BA.debugLine="sebZeit(i).Value = sebZeitValue(9)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (9)]);
 //BA.debugLineNum = 274;BA.debugLine="lblTafel(i).Text = \"  Pausen zwischen den Partnerübungen: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Pausen zwischen den Partnerübungen: "));
 //BA.debugLineNum = 275;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 10:
 //BA.debugLineNum = 279;BA.debugLine="lblTafel(i).Color = Colors.Blue";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 280;BA.debugLine="sebZeit(i).Max = 5 * 60000";
_sebzeit[_i].setMax((int) (5*60000));
 //BA.debugLineNum = 281;BA.debugLine="sebZeit(i).Value = sebZeitValue(10)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (10)]);
 //BA.debugLineNum = 282;BA.debugLine="lblTafel(i).Text = \"  Zeit pro Übung: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Zeit pro Übung: "));
 //BA.debugLineNum = 283;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 11:
 //BA.debugLineNum = 288;BA.debugLine="lblTafel(i).Color = Colors.Cyan";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 289;BA.debugLine="sebZeit(i).Max = 60 * 60000";
_sebzeit[_i].setMax((int) (60*60000));
 //BA.debugLineNum = 290;BA.debugLine="sebZeit(i).Value = sebZeitValue(11)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (11)]);
 //BA.debugLineNum = 291;BA.debugLine="lblTafel(i).Text = \"  Kreis / Kraftübungen: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Kreis / Kraftübungen: "));
 //BA.debugLineNum = 292;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 12:
 //BA.debugLineNum = 295;BA.debugLine="lblTafel(i).Color = Colors.Cyan";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 296;BA.debugLine="sebZeit(i).Max = 3 * 60000";
_sebzeit[_i].setMax((int) (3*60000));
 //BA.debugLineNum = 297;BA.debugLine="sebZeit(i).Value = sebZeitValue(12)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (12)]);
 //BA.debugLineNum = 298;BA.debugLine="lblTafel(i).Text = \"  Pausen: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Pausen: "));
 //BA.debugLineNum = 299;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 13:
 //BA.debugLineNum = 303;BA.debugLine="lblTafel(i).Color = Colors.cyan";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 304;BA.debugLine="sebZeit(i).Max = 5 * 60000";
_sebzeit[_i].setMax((int) (5*60000));
 //BA.debugLineNum = 305;BA.debugLine="sebZeit(i).Value = sebZeitValue(13)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (13)]);
 //BA.debugLineNum = 306;BA.debugLine="lblTafel(i).Text = \"  Zeit pro Kraftübung: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Zeit pro Kraftübung: "));
 //BA.debugLineNum = 307;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 14:
 //BA.debugLineNum = 312;BA.debugLine="lblTafel(i).Color = Colors.Yellow";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 313;BA.debugLine="sebZeit(i).Max = 10 * 60000";
_sebzeit[_i].setMax((int) (10*60000));
 //BA.debugLineNum = 314;BA.debugLine="sebZeit(i).Value = sebZeitValue(14)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (14)]);
 //BA.debugLineNum = 315;BA.debugLine="lblTafel(i).Text = \"  Dehnung: \"";
mostCurrent._lbltafel[_i].setText((Object)("  Dehnung: "));
 //BA.debugLineNum = 316;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 15:
 //BA.debugLineNum = 319;BA.debugLine="lblTafel(i).Color = Colors.Yellow";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 320;BA.debugLine="sebZeit(i).Max = 60000";
_sebzeit[_i].setMax((int) (60000));
 //BA.debugLineNum = 321;BA.debugLine="sebZeit(i).Value = sebZeitValue(15)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (15)]);
 //BA.debugLineNum = 322;BA.debugLine="lblTafel(i).Text = \"  Zeit pro Dehnungsübung \"";
mostCurrent._lbltafel[_i].setText((Object)("  Zeit pro Dehnungsübung "));
 //BA.debugLineNum = 323;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 16:
 //BA.debugLineNum = 326;BA.debugLine="lblTafel(i).Color = Colors.Green";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 327;BA.debugLine="sebZeit(i).Max = 10 * 60000";
_sebzeit[_i].setMax((int) (10*60000));
 //BA.debugLineNum = 328;BA.debugLine="sebZeit(i).Value = sebZeitValue(16)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (16)]);
 //BA.debugLineNum = 329;BA.debugLine="lblTafel(i).Text = \"  Cool Down \"";
mostCurrent._lbltafel[_i].setText((Object)("  Cool Down "));
 //BA.debugLineNum = 330;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 17:
 //BA.debugLineNum = 333;BA.debugLine="lblTafel(i).Color = Colors.Green";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 334;BA.debugLine="sebZeit(i).Max = 60000";
_sebzeit[_i].setMax((int) (60000));
 //BA.debugLineNum = 335;BA.debugLine="sebZeit(i).Value = sebZeitValue(17)";
_sebzeit[_i].setValue(_sebzeitvalue[(int) (17)]);
 //BA.debugLineNum = 336;BA.debugLine="lblTafel(i).Text = \"  Zeit pro  Cool Down Übung\"";
mostCurrent._lbltafel[_i].setText((Object)("  Zeit pro  Cool Down Übung"));
 //BA.debugLineNum = 337;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
case 18:
 //BA.debugLineNum = 341;BA.debugLine="lblTafel(i).Color = Colors.Black";
mostCurrent._lbltafel[_i].setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 342;BA.debugLine="sebZeit(i).Max = 6000 'KleineZeit";
_sebzeit[_i].setMax((int) (6000));
 //BA.debugLineNum = 343;BA.debugLine="sebZeit(i).Value = 6000 'KleineZeit";
_sebzeit[_i].setValue((int) (6000));
 //BA.debugLineNum = 344;BA.debugLine="lblTafel(i).Text = \"  11 \"";
mostCurrent._lbltafel[_i].setText((Object)("  11 "));
 //BA.debugLineNum = 345;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_sebzeit[_i].getValue()))));
 break;
default:
 //BA.debugLineNum = 348;BA.debugLine="Msgbox(\"Listenerzeugung\",\"Fehler in:\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Listenerzeugung","Fehler in:",mostCurrent.activityBA);
 break;
}
;
 }
};
 //BA.debugLineNum = 360;BA.debugLine="scvHintergrund.Panel.Height = AnzahlDerEinstellungen * Panelhoehe";
mostCurrent._scvhintergrund.getPanel().setHeight((int) (_anzahldereinstellungen*_panelhoehe));
 //BA.debugLineNum = 361;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim Zeit(18) As Int";
_zeit = new int[(int) (18)];
;
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _seekbarzeit_valuechanged(int _value,boolean _userchanged) throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.SeekBarWrapper _sb = null;
 //BA.debugLineNum = 418;BA.debugLine="Sub SeekbarZeit_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 420;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 421;BA.debugLine="Dim sb As SeekBar";
_sb = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 422;BA.debugLine="sb = Sender";
_sb.setObject((android.widget.SeekBar)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 423;BA.debugLine="i = sb.Tag";
_i = (int)(BA.ObjectToNumber(_sb.getTag()));
 //BA.debugLineNum = 425;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 428;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 429;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 430;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 1:
 //BA.debugLineNum = 432;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 433;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 434;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 2:
 //BA.debugLineNum = 436;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 437;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 438;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 3:
 //BA.debugLineNum = 440;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 441;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 442;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 4:
 //BA.debugLineNum = 444;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 445;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 446;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 5:
 //BA.debugLineNum = 448;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 449;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 450;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 6:
 //BA.debugLineNum = 452;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 453;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 454;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 7:
 //BA.debugLineNum = 456;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 457;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 458;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 8:
 //BA.debugLineNum = 460;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 461;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 462;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 9:
 //BA.debugLineNum = 464;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 465;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 466;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 10:
 //BA.debugLineNum = 468;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 469;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 470;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 11:
 //BA.debugLineNum = 472;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 473;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 474;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 12:
 //BA.debugLineNum = 476;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 477;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 478;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 13:
 //BA.debugLineNum = 480;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 481;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 482;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 14:
 //BA.debugLineNum = 484;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 485;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 486;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 15:
 //BA.debugLineNum = 488;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 489;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 490;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 16:
 //BA.debugLineNum = 492;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 493;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 494;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 17:
 //BA.debugLineNum = 496;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 497;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 498;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
case 18:
 //BA.debugLineNum = 500;BA.debugLine="Zeit(i) = Value";
_zeit[_i] = _value;
 //BA.debugLineNum = 501;BA.debugLine="If ToggelJaNein(i).Checked = False Then Zeit(i) = 1";
if (mostCurrent._toggeljanein[_i].getChecked()==anywheresoftware.b4a.keywords.Common.False) { 
_zeit[_i] = (int) (1);};
 //BA.debugLineNum = 502;BA.debugLine="lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))";
mostCurrent._lblzeitanzeige[_i].setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[_i]))));
 break;
default:
 break;
}
;
 //BA.debugLineNum = 509;BA.debugLine="i = Zeit(1) + Zeit(3) + Zeit(5) + Zeit(8) + Zeit(11) + Zeit(14) + Zeit(16)";
_i = (int) (_zeit[(int) (1)]+_zeit[(int) (3)]+_zeit[(int) (5)]+_zeit[(int) (8)]+_zeit[(int) (11)]+_zeit[(int) (14)]+_zeit[(int) (16)]);
 //BA.debugLineNum = 510;BA.debugLine="GesamtzeitAnzeige.Progress = (100/Zeit(0)) * (Zeit(1) + Zeit(3) + Zeit(5) +  Zeit(8) + Zeit(11) + Zeit(14) + Zeit(16))";
mostCurrent._gesamtzeitanzeige.setProgress((int) ((100/(double)_zeit[(int) (0)])*(_zeit[(int) (1)]+_zeit[(int) (3)]+_zeit[(int) (5)]+_zeit[(int) (8)]+_zeit[(int) (11)]+_zeit[(int) (14)]+_zeit[(int) (16)])));
 //BA.debugLineNum = 511;BA.debugLine="lblGesamtzeit.Gravity = Gravity.CENTER";
mostCurrent._lblgesamtzeit.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 512;BA.debugLine="lblGesamtzeit.Text = \"IST: \" & DateTime.Time(Ausgleichszahl + i) & \"      SOLL: \" & DateTime.Time(Ausgleichszahl + Zeit(0))";
mostCurrent._lblgesamtzeit.setText((Object)("IST: "+anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_i))+"      SOLL: "+anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (_ausgleichszahl+_zeit[(int) (0)]))));
 //BA.debugLineNum = 515;BA.debugLine="End Sub";
return "";
}
}
