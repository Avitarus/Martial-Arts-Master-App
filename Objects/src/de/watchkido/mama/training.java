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

public class training extends Activity implements B4AActivity{
	public static training mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.training");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (training).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.training");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.training", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (training) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (training) Resume **");
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
		return training.class;
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
        BA.LogInfo("** Activity (training) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (training) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _tmrcounter = null;
public static anywheresoftware.b4a.objects.Timer _tmrgesamttrainingszeit = null;
public static anywheresoftware.b4a.objects.Timer _tmrgruppe = null;
public static anywheresoftware.b4a.objects.Timer _tmruebung = null;
public static anywheresoftware.b4a.objects.Timer _tmrpause = null;
public static anywheresoftware.b4a.audio.SoundPoolWrapper _sp = null;
public static int _loadid0 = 0;
public static int _playid0 = 0;
public static int _loadid1 = 0;
public static int _playid1 = 0;
public static int _loadid2 = 0;
public static int _playid2 = 0;
public static int _loadid3 = 0;
public static int _playid3 = 0;
public static int _loadid4 = 0;
public static int _playid4 = 0;
public static int _loadid5 = 0;
public static int _playid5 = 0;
public static int _loadid6 = 0;
public static int _playid6 = 0;
public static String _dateiverzeichnis = "";
public static String _aktuellerunterordner = "";
public anywheresoftware.b4a.objects.collections.List _listederbilder = null;
public static int _bildnummer = 0;
public static int _mengeallerdateien = 0;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
public anywheresoftware.b4a.phone.Phone.PhoneWakeState _pws = null;
public static String _aktuellesbild = "";
public static int _gesamttrainingszeit = 0;
public static int _erwärmungallgemein = 0;
public static int _zeitproerwärmungsuebunga = 0;
public static int _erwärmungspeziell = 0;
public static int _zeitproerwärmungsuebungs = 0;
public static int _training = 0;
public static int _trainingspausen = 0;
public static int _zeitprouebungtraining = 0;
public static int _partneruebungen = 0;
public static int _pausenzwischenpartneruebungen = 0;
public static int _zeitpropartneruebung = 0;
public static int _kreiskrafttraining = 0;
public static int _pausenzwischendenkraftuebungen = 0;
public static int _zeitprouebungkraft = 0;
public static int _dehnung = 0;
public static int _zeitdehnungsuebung = 0;
public static int _cooldown = 0;
public static int _cooldownuebung = 0;
public static int _zeitgruppe = 0;
public static int _zeituebung = 0;
public static int _zeitpause = 0;
public static int _counter1 = 0;
public static int _counter2 = 0;
public static int _startzeit0 = 0;
public static int _startzeit1 = 0;
public static int _startzeit2 = 0;
public static int _startzeit3 = 0;
public static int _startzeit4 = 0;
public static int _startzeit5 = 0;
public static int _startzeit6 = 0;
public static int _startzeit7 = 0;
public static int _startzeit8 = 0;
public anywheresoftware.b4a.objects.LabelWrapper _lbltrainingsanzeige = null;
public static String _aktuellesbildanzeige = "";
public anywheresoftware.b4a.objects.ButtonWrapper _btnstart = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblprogressbar1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblzeiten = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcounter = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _seblaut = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpfad = null;
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
String _i = "";
 //BA.debugLineNum = 47;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 49;BA.debugLine="Dim i As String";
_i = "";
 //BA.debugLineNum = 51;BA.debugLine="Activity.LoadLayout(\"TrainingEinfach\")";
mostCurrent._activity.LoadLayout("TrainingEinfach",mostCurrent.activityBA);
 //BA.debugLineNum = 52;BA.debugLine="Activity.Title = \"Trainingsstunde\"";
mostCurrent._activity.setTitle((Object)("Trainingsstunde"));
 //BA.debugLineNum = 55;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 56;BA.debugLine="SP.Initialize(4)";
_sp.Initialize((int) (4));
 //BA.debugLineNum = 57;BA.debugLine="LoadId0 = SP.Load(File.DirAssets, \"ShakeHands1.mp3\")";
_loadid0 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ShakeHands1.mp3");
 //BA.debugLineNum = 58;BA.debugLine="LoadId1 = SP.Load(File.DirAssets, \"Fight1.mp3\")";
_loadid1 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Fight1.mp3");
 //BA.debugLineNum = 59;BA.debugLine="LoadId2 = SP.Load(File.DirAssets, \"Break1.mp3\")";
_loadid2 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Break1.mp3");
 //BA.debugLineNum = 60;BA.debugLine="LoadId3 = SP.Load(File.DirAssets, \"Glocke1.wav\")";
_loadid3 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Glocke1.wav");
 //BA.debugLineNum = 61;BA.debugLine="LoadId4 = SP.Load(File.DirAssets, \"Glocke3.mp3\")";
_loadid4 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Glocke3.mp3");
 //BA.debugLineNum = 62;BA.debugLine="LoadId5 = SP.Load(File.DirAssets, \"Glocke7.wav\")";
_loadid5 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Glocke7.wav");
 //BA.debugLineNum = 63;BA.debugLine="LoadId6 = SP.Load(File.DirAssets, \"Stop1.mp3\")";
_loadid6 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Stop1.mp3");
 };
 //BA.debugLineNum = 66;BA.debugLine="sebLaut.Value = p.GetVolume(p.VOLUME_MUSIC)";
mostCurrent._seblaut.setValue(mostCurrent._p.GetVolume(mostCurrent._p.VOLUME_MUSIC));
 //BA.debugLineNum = 67;BA.debugLine="Counter2 = 0 ' der counter für die Gruppe in der wir gerade sind";
_counter2 = (int) (0);
 //BA.debugLineNum = 68;BA.debugLine="pws.KeepAlive(True) ' angeschaltet lassen";
mostCurrent._pws.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 71;BA.debugLine="Gesamttrainingszeit = EinstellungenTrainingStunde.Zeit(0)";
_gesamttrainingszeit = mostCurrent._einstellungentrainingstunde._zeit[(int) (0)];
 //BA.debugLineNum = 72;BA.debugLine="ErwärmungAllgemein = EinstellungenTrainingStunde.Zeit(1)";
_erwärmungallgemein = mostCurrent._einstellungentrainingstunde._zeit[(int) (1)];
 //BA.debugLineNum = 73;BA.debugLine="ZeitproErwärmungsuebungA = EinstellungenTrainingStunde.Zeit(2)";
_zeitproerwärmungsuebunga = mostCurrent._einstellungentrainingstunde._zeit[(int) (2)];
 //BA.debugLineNum = 75;BA.debugLine="ErwärmungSpeziell = EinstellungenTrainingStunde.Zeit(3)";
_erwärmungspeziell = mostCurrent._einstellungentrainingstunde._zeit[(int) (3)];
 //BA.debugLineNum = 76;BA.debugLine="ZeitproErwärmungsuebungS = EinstellungenTrainingStunde.Zeit(4)";
_zeitproerwärmungsuebungs = mostCurrent._einstellungentrainingstunde._zeit[(int) (4)];
 //BA.debugLineNum = 78;BA.debugLine="Training = EinstellungenTrainingStunde.Zeit(5)";
_training = mostCurrent._einstellungentrainingstunde._zeit[(int) (5)];
 //BA.debugLineNum = 79;BA.debugLine="Trainingspausen = EinstellungenTrainingStunde.Zeit(6)";
_trainingspausen = mostCurrent._einstellungentrainingstunde._zeit[(int) (6)];
 //BA.debugLineNum = 80;BA.debugLine="ZeitProUebungTraining = EinstellungenTrainingStunde.Zeit(7)";
_zeitprouebungtraining = mostCurrent._einstellungentrainingstunde._zeit[(int) (7)];
 //BA.debugLineNum = 82;BA.debugLine="Partneruebungen = EinstellungenTrainingStunde.Zeit(8)";
_partneruebungen = mostCurrent._einstellungentrainingstunde._zeit[(int) (8)];
 //BA.debugLineNum = 83;BA.debugLine="PausenzwischenPartneruebungen = EinstellungenTrainingStunde.Zeit(9)";
_pausenzwischenpartneruebungen = mostCurrent._einstellungentrainingstunde._zeit[(int) (9)];
 //BA.debugLineNum = 84;BA.debugLine="ZeitProPartneruebung = EinstellungenTrainingStunde.Zeit(10)";
_zeitpropartneruebung = mostCurrent._einstellungentrainingstunde._zeit[(int) (10)];
 //BA.debugLineNum = 86;BA.debugLine="KreisKrafttraining = EinstellungenTrainingStunde.Zeit(11)";
_kreiskrafttraining = mostCurrent._einstellungentrainingstunde._zeit[(int) (11)];
 //BA.debugLineNum = 87;BA.debugLine="PausenzwischendenKraftuebungen = EinstellungenTrainingStunde.Zeit(12)";
_pausenzwischendenkraftuebungen = mostCurrent._einstellungentrainingstunde._zeit[(int) (12)];
 //BA.debugLineNum = 88;BA.debugLine="ZeitProUebungKraft = EinstellungenTrainingStunde.Zeit(13)";
_zeitprouebungkraft = mostCurrent._einstellungentrainingstunde._zeit[(int) (13)];
 //BA.debugLineNum = 90;BA.debugLine="Dehnung = EinstellungenTrainingStunde.Zeit(14)";
_dehnung = mostCurrent._einstellungentrainingstunde._zeit[(int) (14)];
 //BA.debugLineNum = 91;BA.debugLine="ZeitDehnungsuebung = EinstellungenTrainingStunde.Zeit(15)";
_zeitdehnungsuebung = mostCurrent._einstellungentrainingstunde._zeit[(int) (15)];
 //BA.debugLineNum = 93;BA.debugLine="CoolDown = EinstellungenTrainingStunde.Zeit(16)";
_cooldown = mostCurrent._einstellungentrainingstunde._zeit[(int) (16)];
 //BA.debugLineNum = 94;BA.debugLine="CoolDownuebung = EinstellungenTrainingStunde.Zeit(17)";
_cooldownuebung = mostCurrent._einstellungentrainingstunde._zeit[(int) (17)];
 //BA.debugLineNum = 97;BA.debugLine="If Gesamttrainingszeit < 1000 Then Gesamttrainingszeit = 10000";
if (_gesamttrainingszeit<1000) { 
_gesamttrainingszeit = (int) (10000);};
 //BA.debugLineNum = 98;BA.debugLine="If ErwärmungAllgemein < 1000 Then ErwärmungAllgemein = 1000";
if (_erwärmungallgemein<1000) { 
_erwärmungallgemein = (int) (1000);};
 //BA.debugLineNum = 99;BA.debugLine="If ZeitproErwärmungsuebungA < 1000 Then ZeitproErwärmungsuebungA = 1000";
if (_zeitproerwärmungsuebunga<1000) { 
_zeitproerwärmungsuebunga = (int) (1000);};
 //BA.debugLineNum = 100;BA.debugLine="If ErwärmungSpeziell < 1000 Then ErwärmungSpeziell  = 1000";
if (_erwärmungspeziell<1000) { 
_erwärmungspeziell = (int) (1000);};
 //BA.debugLineNum = 101;BA.debugLine="If ZeitproErwärmungsuebungS < 1000 Then ZeitproErwärmungsuebungS = 1000";
if (_zeitproerwärmungsuebungs<1000) { 
_zeitproerwärmungsuebungs = (int) (1000);};
 //BA.debugLineNum = 102;BA.debugLine="If Training < 1000 Then Training = 1000";
if (_training<1000) { 
_training = (int) (1000);};
 //BA.debugLineNum = 103;BA.debugLine="If Trainingspausen < 1000 Then Trainingspausen = 1000";
if (_trainingspausen<1000) { 
_trainingspausen = (int) (1000);};
 //BA.debugLineNum = 104;BA.debugLine="If ZeitProUebungTraining < 1000 Then ZeitProUebungTraining = 1000";
if (_zeitprouebungtraining<1000) { 
_zeitprouebungtraining = (int) (1000);};
 //BA.debugLineNum = 105;BA.debugLine="If Partneruebungen < 1000 Then Partneruebungen = 1000";
if (_partneruebungen<1000) { 
_partneruebungen = (int) (1000);};
 //BA.debugLineNum = 106;BA.debugLine="If PausenzwischenPartneruebungen < 1000 Then PausenzwischenPartneruebungen = 1000";
if (_pausenzwischenpartneruebungen<1000) { 
_pausenzwischenpartneruebungen = (int) (1000);};
 //BA.debugLineNum = 107;BA.debugLine="If ZeitProPartneruebung < 1000 Then  ZeitProPartneruebung = 1000";
if (_zeitpropartneruebung<1000) { 
_zeitpropartneruebung = (int) (1000);};
 //BA.debugLineNum = 108;BA.debugLine="If KreisKrafttraining < 1000 Then KreisKrafttraining = 1000";
if (_kreiskrafttraining<1000) { 
_kreiskrafttraining = (int) (1000);};
 //BA.debugLineNum = 109;BA.debugLine="If PausenzwischendenKraftuebungen < 1000 Then PausenzwischendenKraftuebungen  = 1000";
if (_pausenzwischendenkraftuebungen<1000) { 
_pausenzwischendenkraftuebungen = (int) (1000);};
 //BA.debugLineNum = 121;BA.debugLine="Startzeit0 = 0 ' beginn ErwärmungAllgemein";
_startzeit0 = (int) (0);
 //BA.debugLineNum = 122;BA.debugLine="Startzeit1 = Startzeit0 + ErwärmungAllgemein/1000' beginn ErwärmungSpeziell";
_startzeit1 = (int) (_startzeit0+_erwärmungallgemein/(double)1000);
 //BA.debugLineNum = 123;BA.debugLine="Startzeit2 = Startzeit1 + ErwärmungSpeziell/1000 ' beginn Training";
_startzeit2 = (int) (_startzeit1+_erwärmungspeziell/(double)1000);
 //BA.debugLineNum = 124;BA.debugLine="Startzeit3 = Startzeit2 + Training /1000' beginn Partneruebungen";
_startzeit3 = (int) (_startzeit2+_training/(double)1000);
 //BA.debugLineNum = 125;BA.debugLine="Startzeit4 = Startzeit3 + Partneruebungen /1000' beginn KreisKrafttraining";
_startzeit4 = (int) (_startzeit3+_partneruebungen/(double)1000);
 //BA.debugLineNum = 126;BA.debugLine="Startzeit5 = Startzeit4 + KreisKrafttraining /1000' beginn Dehnung";
_startzeit5 = (int) (_startzeit4+_kreiskrafttraining/(double)1000);
 //BA.debugLineNum = 127;BA.debugLine="Startzeit6 = Startzeit5 + Dehnung /1000' beginn CoolDown";
_startzeit6 = (int) (_startzeit5+_dehnung/(double)1000);
 //BA.debugLineNum = 128;BA.debugLine="Startzeit7 = Startzeit6 + CoolDown/1000 ' Ende des trainings";
_startzeit7 = (int) (_startzeit6+_cooldown/(double)1000);
 //BA.debugLineNum = 136;BA.debugLine="tmrCounter.Initialize(\"tmrCounter\",1000)";
_tmrcounter.Initialize(processBA,"tmrCounter",(long) (1000));
 //BA.debugLineNum = 137;BA.debugLine="tmrGesamttrainingszeit.Initialize(\"tmrGesamttrainingszeit\",EinstellungenTrainingStunde.Zeit(0))";
_tmrgesamttrainingszeit.Initialize(processBA,"tmrGesamttrainingszeit",(long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (0)]));
 //BA.debugLineNum = 138;BA.debugLine="tmrGruppe.Initialize(\"tmrGruppe\",EinstellungenTrainingStunde.Zeit(10))";
_tmrgruppe.Initialize(processBA,"tmrGruppe",(long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (10)]));
 //BA.debugLineNum = 139;BA.debugLine="tmrUebung.Initialize(\"tmrUebung\",EinstellungenTrainingStunde.Zeit(10))";
_tmruebung.Initialize(processBA,"tmrUebung",(long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (10)]));
 //BA.debugLineNum = 140;BA.debugLine="tmrPause.Initialize(\"tmrPause\",EinstellungenTrainingStunde.Zeit(10))";
_tmrpause.Initialize(processBA,"tmrPause",(long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (10)]));
 //BA.debugLineNum = 144;BA.debugLine="tmrCounter.Enabled = False";
_tmrcounter.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="tmrGesamttrainingszeit.Enabled = False";
_tmrgesamttrainingszeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 146;BA.debugLine="tmrGruppe.Enabled = False";
_tmrgruppe.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 147;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 148;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 559;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 564;BA.debugLine="pws.ReleaseKeepAlive";
mostCurrent._pws.ReleaseKeepAlive();
 //BA.debugLineNum = 565;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 553;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 555;BA.debugLine="pws.KeepAlive(False)' ausgeschaltet beim beenden?";
mostCurrent._pws.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 557;BA.debugLine="End Sub";
return "";
}
public static String  _btnstart_click() throws Exception{
 //BA.debugLineNum = 623;BA.debugLine="Sub btnStart_Click";
 //BA.debugLineNum = 626;BA.debugLine="MengeAllerDateien =0";
_mengeallerdateien = (int) (0);
 //BA.debugLineNum = 628;BA.debugLine="AktuellerUnterordner = Main.Unterordner5";
mostCurrent._aktuellerunterordner = mostCurrent._main._unterordner5;
 //BA.debugLineNum = 631;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 632;BA.debugLine="ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 633;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 634;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 //BA.debugLineNum = 635;BA.debugLine="BildNummer =-1";
_bildnummer = (int) (-1);
 //BA.debugLineNum = 636;BA.debugLine="tmrCounter.Enabled = True";
_tmrcounter.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 642;BA.debugLine="Dim AktuellesBild As String";
mostCurrent._aktuellesbild = "";
 //BA.debugLineNum = 643;BA.debugLine="BildNummer = BildNummer + 1";
_bildnummer = (int) (_bildnummer+1);
 //BA.debugLineNum = 644;BA.debugLine="If BildNummer +1 > MengeAllerDateien Then";
if (_bildnummer+1>_mengeallerdateien) { 
 //BA.debugLineNum = 645;BA.debugLine="BildNummer=0";
_bildnummer = (int) (0);
 };
 //BA.debugLineNum = 647;BA.debugLine="AktuellesBild= ListeDerBilder.Get (BildNummer)";
mostCurrent._aktuellesbild = BA.ObjectToString(mostCurrent._listederbilder.Get(_bildnummer));
 //BA.debugLineNum = 648;BA.debugLine="ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner, AktuellesBild)";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner,mostCurrent._aktuellesbild).getObject()));
 //BA.debugLineNum = 650;BA.debugLine="AktuellesBildAnzeige = AktuellesBild.SubString(3)";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbild.substring((int) (3));
 //BA.debugLineNum = 651;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".jpg\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".jpg","");
 //BA.debugLineNum = 652;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".gif\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".gif","");
 //BA.debugLineNum = 653;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".png\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".png","");
 //BA.debugLineNum = 654;BA.debugLine="lbltrainingsanzeige.Text = AktuellesBildAnzeige";
mostCurrent._lbltrainingsanzeige.setText((Object)(mostCurrent._aktuellesbildanzeige));
 //BA.debugLineNum = 659;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 596;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 599;BA.debugLine="BildNummer = BildNummer - 1";
_bildnummer = (int) (_bildnummer-1);
 //BA.debugLineNum = 600;BA.debugLine="If BildNummer -1 > MengeAllerDateien Then";
if (_bildnummer-1>_mengeallerdateien) { 
 //BA.debugLineNum = 601;BA.debugLine="BildNummer=0";
_bildnummer = (int) (0);
 };
 //BA.debugLineNum = 603;BA.debugLine="AktuellesBild= ListeDerBilder.Get (BildNummer)";
mostCurrent._aktuellesbild = BA.ObjectToString(mostCurrent._listederbilder.Get(_bildnummer));
 //BA.debugLineNum = 604;BA.debugLine="ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner ,AktuellesBild)";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner,mostCurrent._aktuellesbild).getObject()));
 //BA.debugLineNum = 607;BA.debugLine="AktuellesBildAnzeige = AktuellesBild.SubString(3)";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbild.substring((int) (3));
 //BA.debugLineNum = 608;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".jpg\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".jpg","");
 //BA.debugLineNum = 609;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".gif\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".gif","");
 //BA.debugLineNum = 610;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".png\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".png","");
 //BA.debugLineNum = 611;BA.debugLine="lbltrainingsanzeige.Text = AktuellesBildAnzeige";
mostCurrent._lbltrainingsanzeige.setText((Object)(mostCurrent._aktuellesbildanzeige));
 //BA.debugLineNum = 617;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
 //BA.debugLineNum = 568;BA.debugLine="Sub Button2_Click";
 //BA.debugLineNum = 570;BA.debugLine="BildNummer = BildNummer + 1";
_bildnummer = (int) (_bildnummer+1);
 //BA.debugLineNum = 571;BA.debugLine="If BildNummer +1 > MengeAllerDateien Then";
if (_bildnummer+1>_mengeallerdateien) { 
 //BA.debugLineNum = 572;BA.debugLine="BildNummer=0";
_bildnummer = (int) (0);
 };
 //BA.debugLineNum = 574;BA.debugLine="AktuellesBild= ListeDerBilder.Get (BildNummer)";
mostCurrent._aktuellesbild = BA.ObjectToString(mostCurrent._listederbilder.Get(_bildnummer));
 //BA.debugLineNum = 575;BA.debugLine="ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner ,AktuellesBild)";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner,mostCurrent._aktuellesbild).getObject()));
 //BA.debugLineNum = 577;BA.debugLine="AktuellesBildAnzeige = AktuellesBild.SubString(3)";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbild.substring((int) (3));
 //BA.debugLineNum = 578;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".jpg\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".jpg","");
 //BA.debugLineNum = 579;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".gif\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".gif","");
 //BA.debugLineNum = 580;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".png\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".png","");
 //BA.debugLineNum = 581;BA.debugLine="lbltrainingsanzeige.Text = AktuellesBildAnzeige";
mostCurrent._lbltrainingsanzeige.setText((Object)(mostCurrent._aktuellesbildanzeige));
 //BA.debugLineNum = 587;BA.debugLine="End Sub";
return "";
}
public static String  _button3_click() throws Exception{
 //BA.debugLineNum = 589;BA.debugLine="Sub Button3_Click";
 //BA.debugLineNum = 590;BA.debugLine="tmrCounter.Enabled = False";
_tmrcounter.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 591;BA.debugLine="End Sub";
return "";
}
public static String  _button4_click() throws Exception{
 //BA.debugLineNum = 593;BA.debugLine="Sub Button4_Click";
 //BA.debugLineNum = 594;BA.debugLine="tmrCounter.Enabled = True";
_tmrcounter.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 595;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim Dateiverzeichnis As String : Dateiverzeichnis = File.DirRootExternal";
mostCurrent._dateiverzeichnis = "";
 //BA.debugLineNum = 15;BA.debugLine="Dim Dateiverzeichnis As String : Dateiverzeichnis = File.DirRootExternal";
mostCurrent._dateiverzeichnis = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal();
 //BA.debugLineNum = 16;BA.debugLine="Dim AktuellerUnterordner As String";
mostCurrent._aktuellerunterordner = "";
 //BA.debugLineNum = 17;BA.debugLine="Dim ListeDerBilder As List";
mostCurrent._listederbilder = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 18;BA.debugLine="Dim BildNummer As Int";
_bildnummer = 0;
 //BA.debugLineNum = 19;BA.debugLine="Dim MengeAllerDateien As Int";
_mengeallerdateien = 0;
 //BA.debugLineNum = 20;BA.debugLine="Dim ImageView1 As ImageView";
mostCurrent._imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim pws As PhoneWakeState";
mostCurrent._pws = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 22;BA.debugLine="Dim AktuellesBild As String";
mostCurrent._aktuellesbild = "";
 //BA.debugLineNum = 24;BA.debugLine="Dim Gesamttrainingszeit, ErwärmungAllgemein, ZeitproErwärmungsuebungA, ErwärmungSpeziell As Int";
_gesamttrainingszeit = 0;
_erwärmungallgemein = 0;
_zeitproerwärmungsuebunga = 0;
_erwärmungspeziell = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim ZeitproErwärmungsuebungS, Training, Trainingspausen, ZeitProUebungTraining, Partneruebungen As Int";
_zeitproerwärmungsuebungs = 0;
_training = 0;
_trainingspausen = 0;
_zeitprouebungtraining = 0;
_partneruebungen = 0;
 //BA.debugLineNum = 26;BA.debugLine="Dim PausenzwischenPartneruebungen, ZeitProPartneruebung, KreisKrafttraining, PausenzwischendenKraftuebungen As Int";
_pausenzwischenpartneruebungen = 0;
_zeitpropartneruebung = 0;
_kreiskrafttraining = 0;
_pausenzwischendenkraftuebungen = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim ZeitProUebungKraft, Dehnung, ZeitDehnungsuebung, CoolDown, CoolDownuebung As Int";
_zeitprouebungkraft = 0;
_dehnung = 0;
_zeitdehnungsuebung = 0;
_cooldown = 0;
_cooldownuebung = 0;
 //BA.debugLineNum = 28;BA.debugLine="Dim zeitGruppe, ZeitUebung, ZeitPause As Int";
_zeitgruppe = 0;
_zeituebung = 0;
_zeitpause = 0;
 //BA.debugLineNum = 29;BA.debugLine="Dim Counter1, Counter2 As Int";
_counter1 = 0;
_counter2 = 0;
 //BA.debugLineNum = 30;BA.debugLine="Dim Startzeit0, Startzeit1, Startzeit2, Startzeit3, Startzeit4, Startzeit5, Startzeit6, Startzeit7, Startzeit8 As Int";
_startzeit0 = 0;
_startzeit1 = 0;
_startzeit2 = 0;
_startzeit3 = 0;
_startzeit4 = 0;
_startzeit5 = 0;
_startzeit6 = 0;
_startzeit7 = 0;
_startzeit8 = 0;
 //BA.debugLineNum = 32;BA.debugLine="Dim lbltrainingsanzeige As Label";
mostCurrent._lbltrainingsanzeige = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim AktuellesBild, AktuellesBildAnzeige As String";
mostCurrent._aktuellesbild = "";
mostCurrent._aktuellesbildanzeige = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim btnStart As Button";
mostCurrent._btnstart = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Dim ProgressBar1 As ProgressBar";
mostCurrent._progressbar1 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim lblProgressbar1 As Label";
mostCurrent._lblprogressbar1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim lblZeiten As Label";
mostCurrent._lblzeiten = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim lblCounter As Label";
mostCurrent._lblcounter = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim sebLaut As SeekBar";
mostCurrent._seblaut = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 42;BA.debugLine="Dim lblPfad As Label";
mostCurrent._lblpfad = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim tmrCounter, tmrGesamttrainingszeit, tmrGruppe, tmrUebung, tmrPause As Timer";
_tmrcounter = new anywheresoftware.b4a.objects.Timer();
_tmrgesamttrainingszeit = new anywheresoftware.b4a.objects.Timer();
_tmrgruppe = new anywheresoftware.b4a.objects.Timer();
_tmruebung = new anywheresoftware.b4a.objects.Timer();
_tmrpause = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 9;BA.debugLine="Dim SP As SoundPool";
_sp = new anywheresoftware.b4a.audio.SoundPoolWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Dim LoadId0, PlayId0, LoadId1, PlayId1, LoadId2, PlayId2, LoadId3, PlayId3 As Int";
_loadid0 = 0;
_playid0 = 0;
_loadid1 = 0;
_playid1 = 0;
_loadid2 = 0;
_playid2 = 0;
_loadid3 = 0;
_playid3 = 0;
 //BA.debugLineNum = 11;BA.debugLine="Dim LoadId4, PlayId4, LoadId5, PlayId5, LoadId6, PlayId6 As Int";
_loadid4 = 0;
_playid4 = 0;
_loadid5 = 0;
_playid5 = 0;
_loadid6 = 0;
_playid6 = 0;
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _seblaut_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 662;BA.debugLine="Sub sebLaut_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 669;BA.debugLine="p.SetVolume(p.VOLUME_MUSIC, Value, False)";
mostCurrent._p.SetVolume(mostCurrent._p.VOLUME_MUSIC,_value,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 681;BA.debugLine="End Sub";
return "";
}
public static String  _tmrcounter_tick() throws Exception{
 //BA.debugLineNum = 163;BA.debugLine="Sub tmrCounter_tick";
 //BA.debugLineNum = 189;BA.debugLine="ProgressBar1.Progress = Counter1 *(100/Startzeit7)";
mostCurrent._progressbar1.setProgress((int) (_counter1*(100/(double)_startzeit7)));
 //BA.debugLineNum = 193;BA.debugLine="If Counter1 = Startzeit0 Then ' beginn erwärmung allgemein";
if (_counter1==_startzeit0) { 
 //BA.debugLineNum = 195;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 196;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 197;BA.debugLine="tmrGesamttrainingszeit.Interval = Gesamttrainingszeit";
_tmrgesamttrainingszeit.setInterval((long) (_gesamttrainingszeit));
 //BA.debugLineNum = 198;BA.debugLine="tmrGesamttrainingszeit.Enabled = True";
_tmrgesamttrainingszeit.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 199;BA.debugLine="AktuellerUnterordner = Main.Unterordner5";
mostCurrent._aktuellerunterordner = mostCurrent._main._unterordner5;
 //BA.debugLineNum = 200;BA.debugLine="tmrUebung.Interval = ZeitproErwärmungsuebungA";
_tmruebung.setInterval((long) (_zeitproerwärmungsuebunga));
 //BA.debugLineNum = 201;BA.debugLine="tmrUebung.Enabled = True";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 202;BA.debugLine="tmrPause.Interval = 1";
_tmrpause.setInterval((long) (1));
 //BA.debugLineNum = 207;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 208;BA.debugLine="ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 209;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 210;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 //BA.debugLineNum = 211;BA.debugLine="BildNummer =-1";
_bildnummer = (int) (-1);
 };
 //BA.debugLineNum = 214;BA.debugLine="Log(Counter1 & AktuellerUnterordner)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_counter1)+mostCurrent._aktuellerunterordner);
 }else {
 };
 //BA.debugLineNum = 220;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 222;BA.debugLine="If Counter1 = Startzeit1 Then ' beginn erwärmung speziell";
if (_counter1==_startzeit1) { 
 //BA.debugLineNum = 223;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 224;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 225;BA.debugLine="AktuellerUnterordner = Main.Unterordner12";
mostCurrent._aktuellerunterordner = mostCurrent._main._unterordner12;
 //BA.debugLineNum = 226;BA.debugLine="tmrUebung.Interval = ZeitproErwärmungsuebungS";
_tmruebung.setInterval((long) (_zeitproerwärmungsuebungs));
 //BA.debugLineNum = 227;BA.debugLine="tmrUebung.Enabled = True";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 228;BA.debugLine="tmrPause.Interval = 1";
_tmrpause.setInterval((long) (1));
 //BA.debugLineNum = 230;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 231;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 233;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 234;BA.debugLine="ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 235;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 236;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 //BA.debugLineNum = 237;BA.debugLine="BildNummer =-1";
_bildnummer = (int) (-1);
 };
 //BA.debugLineNum = 239;BA.debugLine="Log(Counter1 & AktuellerUnterordner)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_counter1)+mostCurrent._aktuellerunterordner);
 }else {
 };
 //BA.debugLineNum = 245;BA.debugLine="If Counter1 = Startzeit2 Then ' tRAINING EINZEL";
if (_counter1==_startzeit2) { 
 //BA.debugLineNum = 246;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 247;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 248;BA.debugLine="AktuellerUnterordner = Main.Unterordner13";
mostCurrent._aktuellerunterordner = mostCurrent._main._unterordner13;
 //BA.debugLineNum = 249;BA.debugLine="tmrUebung.Interval = ZeitProUebungTraining";
_tmruebung.setInterval((long) (_zeitprouebungtraining));
 //BA.debugLineNum = 250;BA.debugLine="tmrUebung.Enabled = True";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 251;BA.debugLine="tmrPause.Interval = Trainingspausen";
_tmrpause.setInterval((long) (_trainingspausen));
 //BA.debugLineNum = 253;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 254;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 256;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 257;BA.debugLine="ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 258;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 259;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 //BA.debugLineNum = 260;BA.debugLine="BildNummer =-1";
_bildnummer = (int) (-1);
 };
 //BA.debugLineNum = 262;BA.debugLine="Log(Counter1 & AktuellerUnterordner)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_counter1)+mostCurrent._aktuellerunterordner);
 }else {
 };
 //BA.debugLineNum = 266;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 267;BA.debugLine="If Counter1 = Startzeit3 Then ' beginn Partnerübungen";
if (_counter1==_startzeit3) { 
 //BA.debugLineNum = 268;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 269;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 270;BA.debugLine="AktuellerUnterordner = Main.Unterordner14";
mostCurrent._aktuellerunterordner = mostCurrent._main._unterordner14;
 //BA.debugLineNum = 271;BA.debugLine="tmrUebung.Interval = ZeitProUebungTraining";
_tmruebung.setInterval((long) (_zeitprouebungtraining));
 //BA.debugLineNum = 272;BA.debugLine="tmrUebung.Enabled = True";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 273;BA.debugLine="tmrPause.Interval = Trainingspausen";
_tmrpause.setInterval((long) (_trainingspausen));
 //BA.debugLineNum = 275;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 276;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 278;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 279;BA.debugLine="ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 280;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 281;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 //BA.debugLineNum = 282;BA.debugLine="BildNummer =-1";
_bildnummer = (int) (-1);
 };
 //BA.debugLineNum = 284;BA.debugLine="Log(Counter1 & AktuellerUnterordner)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_counter1)+mostCurrent._aktuellerunterordner);
 }else {
 };
 //BA.debugLineNum = 288;BA.debugLine="If Counter1 = Startzeit4 Then 'bODYWEIGHTEXERCISES";
if (_counter1==_startzeit4) { 
 //BA.debugLineNum = 289;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 290;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 291;BA.debugLine="AktuellerUnterordner = Main.Unterordner2";
mostCurrent._aktuellerunterordner = mostCurrent._main._unterordner2;
 //BA.debugLineNum = 292;BA.debugLine="tmrUebung.Interval = ZeitProUebungKraft";
_tmruebung.setInterval((long) (_zeitprouebungkraft));
 //BA.debugLineNum = 293;BA.debugLine="tmrUebung.Enabled = True";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 294;BA.debugLine="tmrPause.Interval = PausenzwischendenKraftuebungen";
_tmrpause.setInterval((long) (_pausenzwischendenkraftuebungen));
 //BA.debugLineNum = 296;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 297;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 299;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 300;BA.debugLine="ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 301;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 302;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 //BA.debugLineNum = 303;BA.debugLine="BildNummer =-1";
_bildnummer = (int) (-1);
 };
 //BA.debugLineNum = 305;BA.debugLine="Log(Counter1 & AktuellerUnterordner)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_counter1)+mostCurrent._aktuellerunterordner);
 }else {
 };
 //BA.debugLineNum = 309;BA.debugLine="If Counter1 = Startzeit5 Then ' beginn dehnung";
if (_counter1==_startzeit5) { 
 //BA.debugLineNum = 310;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 311;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 312;BA.debugLine="AktuellerUnterordner = Main.Unterordner4";
mostCurrent._aktuellerunterordner = mostCurrent._main._unterordner4;
 //BA.debugLineNum = 313;BA.debugLine="tmrUebung.Interval = ZeitProUebungKraft";
_tmruebung.setInterval((long) (_zeitprouebungkraft));
 //BA.debugLineNum = 314;BA.debugLine="tmrUebung.Enabled = True";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 315;BA.debugLine="tmrPause.Interval = PausenzwischendenKraftuebungen";
_tmrpause.setInterval((long) (_pausenzwischendenkraftuebungen));
 //BA.debugLineNum = 317;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 318;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 320;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 321;BA.debugLine="ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 322;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 323;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 //BA.debugLineNum = 324;BA.debugLine="BildNummer =-1";
_bildnummer = (int) (-1);
 };
 //BA.debugLineNum = 326;BA.debugLine="Log(Counter1 & AktuellerUnterordner)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_counter1)+mostCurrent._aktuellerunterordner);
 }else {
 };
 //BA.debugLineNum = 330;BA.debugLine="If Counter1 = Startzeit6 Then ' COOL DOWN";
if (_counter1==_startzeit6) { 
 //BA.debugLineNum = 331;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 332;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 333;BA.debugLine="AktuellerUnterordner = Main.Unterordner3";
mostCurrent._aktuellerunterordner = mostCurrent._main._unterordner3;
 //BA.debugLineNum = 334;BA.debugLine="tmrUebung.Interval = CoolDownuebung";
_tmruebung.setInterval((long) (_cooldownuebung));
 //BA.debugLineNum = 335;BA.debugLine="tmrUebung.Enabled = True";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 336;BA.debugLine="tmrPause.Interval = 1";
_tmrpause.setInterval((long) (1));
 //BA.debugLineNum = 337;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 338;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 340;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 341;BA.debugLine="ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner);
 //BA.debugLineNum = 342;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 343;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 //BA.debugLineNum = 344;BA.debugLine="BildNummer =-1";
_bildnummer = (int) (-1);
 };
 //BA.debugLineNum = 346;BA.debugLine="Log(Counter1 & AktuellerUnterordner)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_counter1)+mostCurrent._aktuellerunterordner);
 }else {
 };
 //BA.debugLineNum = 362;BA.debugLine="If Counter1 = Startzeit7 Then ' ende der Trainingszeit";
if (_counter1==_startzeit7) { 
 //BA.debugLineNum = 363;BA.debugLine="tmrGesamttrainingszeit.Enabled = False";
_tmrgesamttrainingszeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 };
 //BA.debugLineNum = 367;BA.debugLine="If Counter1 = Startzeit8 Then";
if (_counter1==_startzeit8) { 
 }else {
 };
 //BA.debugLineNum = 373;BA.debugLine="Log(Counter1)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_counter1));
 //BA.debugLineNum = 375;BA.debugLine="Counter1 = Counter1 + 1";
_counter1 = (int) (_counter1+1);
 //BA.debugLineNum = 378;BA.debugLine="lblPfad.Text = AktuellerUnterordner";
mostCurrent._lblpfad.setText((Object)(mostCurrent._aktuellerunterordner));
 //BA.debugLineNum = 382;BA.debugLine="lblCounter.Text = DateTime.Time(-3599950 + Counter1*1000)";
mostCurrent._lblcounter.setText((Object)(anywheresoftware.b4a.keywords.Common.DateTime.Time((long) (-3599950+_counter1*1000))));
 //BA.debugLineNum = 383;BA.debugLine="End Sub";
return "";
}
public static String  _tmrgesamttrainingszeit_tick() throws Exception{
 //BA.debugLineNum = 525;BA.debugLine="Sub tmrGesamttrainingszeit_Tick";
 //BA.debugLineNum = 529;BA.debugLine="tmrCounter.Enabled = False";
_tmrcounter.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 530;BA.debugLine="tmrGesamttrainingszeit.Enabled = False";
_tmrgesamttrainingszeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 531;BA.debugLine="tmrGruppe.Enabled = False";
_tmrgruppe.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 532;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 533;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 535;BA.debugLine="lbltrainingsanzeige.Text = \"Training beendet\"";
mostCurrent._lbltrainingsanzeige.setText((Object)("Training beendet"));
 //BA.debugLineNum = 537;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 538;BA.debugLine="PlayId5 = SP.Play(LoadId5, 1, 1, 1, 0, 1) ' Glocke7";
_playid5 = _sp.Play(_loadid5,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 540;BA.debugLine="AktuellesBildAnzeige = AktuellesBild.SubString(3)";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbild.substring((int) (3));
 //BA.debugLineNum = 541;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".jpg\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".jpg","");
 //BA.debugLineNum = 542;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".gif\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".gif","");
 //BA.debugLineNum = 543;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".png\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".png","");
 //BA.debugLineNum = 544;BA.debugLine="lbltrainingsanzeige.Text = AktuellesBildAnzeige";
mostCurrent._lbltrainingsanzeige.setText((Object)(mostCurrent._aktuellesbildanzeige));
 //BA.debugLineNum = 549;BA.debugLine="End Sub";
return "";
}
public static String  _tmrgruppe_tick() throws Exception{
 //BA.debugLineNum = 472;BA.debugLine="Sub tmrGruppe_Tick";
 //BA.debugLineNum = 474;BA.debugLine="Select Counter2";
switch (_counter2) {
case 0:
 //BA.debugLineNum = 477;BA.debugLine="tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(1)";
_tmrgruppe.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (1)]));
 //BA.debugLineNum = 478;BA.debugLine="tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(2)";
_tmruebung.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (2)]));
 //BA.debugLineNum = 479;BA.debugLine="tmrPause.Interval = 1";
_tmrpause.setInterval((long) (1));
 break;
case 1:
 //BA.debugLineNum = 481;BA.debugLine="tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(3)";
_tmrgruppe.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (3)]));
 //BA.debugLineNum = 482;BA.debugLine="tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(4)";
_tmruebung.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (4)]));
 //BA.debugLineNum = 483;BA.debugLine="tmrPause.Interval = 1";
_tmrpause.setInterval((long) (1));
 break;
case 2:
 //BA.debugLineNum = 485;BA.debugLine="tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(5)";
_tmrgruppe.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (5)]));
 //BA.debugLineNum = 486;BA.debugLine="tmrPause.Interval = EinstellungenTrainingStunde.Zeit(6)";
_tmrpause.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (6)]));
 //BA.debugLineNum = 487;BA.debugLine="tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(7)";
_tmruebung.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (7)]));
 break;
case 3:
 //BA.debugLineNum = 489;BA.debugLine="tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(8)";
_tmrgruppe.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (8)]));
 //BA.debugLineNum = 490;BA.debugLine="tmrPause.Interval = EinstellungenTrainingStunde.Zeit(9)";
_tmrpause.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (9)]));
 //BA.debugLineNum = 491;BA.debugLine="tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(10)";
_tmruebung.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (10)]));
 break;
case 4:
 //BA.debugLineNum = 493;BA.debugLine="tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(11)";
_tmrgruppe.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (11)]));
 //BA.debugLineNum = 494;BA.debugLine="tmrPause.Interval = EinstellungenTrainingStunde.Zeit(12)";
_tmrpause.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (12)]));
 //BA.debugLineNum = 495;BA.debugLine="tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(13)";
_tmruebung.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (13)]));
 break;
case 5:
 //BA.debugLineNum = 497;BA.debugLine="tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(14)";
_tmrgruppe.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (14)]));
 //BA.debugLineNum = 498;BA.debugLine="tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(15)";
_tmruebung.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (15)]));
 //BA.debugLineNum = 499;BA.debugLine="tmrPause.Interval = 1";
_tmrpause.setInterval((long) (1));
 break;
case 6:
 //BA.debugLineNum = 501;BA.debugLine="CoolDown = EinstellungenTrainingStunde.Zeit(16)";
_cooldown = mostCurrent._einstellungentrainingstunde._zeit[(int) (16)];
 //BA.debugLineNum = 502;BA.debugLine="tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(17)";
_tmruebung.setInterval((long) (mostCurrent._einstellungentrainingstunde._zeit[(int) (17)]));
 //BA.debugLineNum = 503;BA.debugLine="tmrPause.Interval = 1";
_tmrpause.setInterval((long) (1));
 break;
default:
 //BA.debugLineNum = 505;BA.debugLine="tmrCounter.Enabled = False";
_tmrcounter.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 506;BA.debugLine="tmrGesamttrainingszeit.Enabled = False";
_tmrgesamttrainingszeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 507;BA.debugLine="tmrGruppe.Enabled = False";
_tmrgruppe.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 508;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 509;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 511;BA.debugLine="lbltrainingsanzeige.Text = \"Training beendet\"";
mostCurrent._lbltrainingsanzeige.setText((Object)("Training beendet"));
 //BA.debugLineNum = 513;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 514;BA.debugLine="PlayId5 = SP.Play(LoadId5, 1, 1, 1, 0, 1) ' Glocke7";
_playid5 = _sp.Play(_loadid5,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 break;
}
;
 //BA.debugLineNum = 519;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 521;BA.debugLine="Counter2 = Counter2 + 1";
_counter2 = (int) (_counter2+1);
 //BA.debugLineNum = 523;BA.debugLine="End Sub";
return "";
}
public static String  _tmrpause_tick() throws Exception{
anywheresoftware.b4a.audio.Beeper _b = null;
 //BA.debugLineNum = 430;BA.debugLine="Sub tmrPause_tick";
 //BA.debugLineNum = 445;BA.debugLine="tmrUebung.Enabled = True";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 446;BA.debugLine="tmrPause.Enabled = False";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 449;BA.debugLine="If tmrPause.Interval = 1 Then";
if (_tmrpause.getInterval()==1) { 
 //BA.debugLineNum = 450;BA.debugLine="Dim b As Beeper";
_b = new anywheresoftware.b4a.audio.Beeper();
 //BA.debugLineNum = 451;BA.debugLine="b.Initialize(300, 600)";
_b.Initialize((int) (300),(int) (600));
 //BA.debugLineNum = 452;BA.debugLine="b.Beep";
_b.Beep();
 }else {
 //BA.debugLineNum = 454;BA.debugLine="PlayId3 = SP.Play(LoadId3, 1, 1, 1, 0, 1) ' Glocke1";
_playid3 = _sp.Play(_loadid3,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 455;BA.debugLine="PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1) ' fight";
_playid1 = _sp.Play(_loadid1,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 };
 //BA.debugLineNum = 458;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 460;BA.debugLine="Activity.Color = Colors.Green";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 470;BA.debugLine="End Sub";
return "";
}
public static String  _tmruebung_tick() throws Exception{
 //BA.debugLineNum = 387;BA.debugLine="Sub tmrUebung_tick";
 //BA.debugLineNum = 390;BA.debugLine="If AktuellerUnterordner = Main.Unterordner13 OR AktuellerUnterordner = Main.Unterordner14 Then";
if ((mostCurrent._aktuellerunterordner).equals(mostCurrent._main._unterordner13) || (mostCurrent._aktuellerunterordner).equals(mostCurrent._main._unterordner14)) { 
 //BA.debugLineNum = 392;BA.debugLine="BildNummer = Rnd(0,MengeAllerDateien)";
_bildnummer = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),_mengeallerdateien);
 }else {
 //BA.debugLineNum = 394;BA.debugLine="BildNummer = BildNummer + 1";
_bildnummer = (int) (_bildnummer+1);
 //BA.debugLineNum = 395;BA.debugLine="If BildNummer +1 > MengeAllerDateien Then";
if (_bildnummer+1>_mengeallerdateien) { 
 //BA.debugLineNum = 396;BA.debugLine="BildNummer=0";
_bildnummer = (int) (0);
 };
 };
 //BA.debugLineNum = 400;BA.debugLine="AktuellesBild= ListeDerBilder.Get (BildNummer)";
mostCurrent._aktuellesbild = BA.ObjectToString(mostCurrent._listederbilder.Get(_bildnummer));
 //BA.debugLineNum = 401;BA.debugLine="ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner  ,AktuellesBild)";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._dateiverzeichnis+mostCurrent._aktuellerunterordner,mostCurrent._aktuellesbild).getObject()));
 //BA.debugLineNum = 407;BA.debugLine="If tmrPause.Interval = 1 Then";
if (_tmrpause.getInterval()==1) { 
 }else {
 //BA.debugLineNum = 410;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 411;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glocke3";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 };
 //BA.debugLineNum = 414;BA.debugLine="DoEvents";
anywheresoftware.b4a.keywords.Common.DoEvents();
 //BA.debugLineNum = 416;BA.debugLine="Activity.Color = Colors.Red";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 418;BA.debugLine="AktuellesBildAnzeige = AktuellesBild.SubString(3)";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbild.substring((int) (3));
 //BA.debugLineNum = 419;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".jpg\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".jpg","");
 //BA.debugLineNum = 420;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".gif\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".gif","");
 //BA.debugLineNum = 421;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".png\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".png","");
 //BA.debugLineNum = 422;BA.debugLine="lbltrainingsanzeige.Text = AktuellesBildAnzeige";
mostCurrent._lbltrainingsanzeige.setText((Object)(mostCurrent._aktuellesbildanzeige));
 //BA.debugLineNum = 424;BA.debugLine="tmrUebung.Enabled = False";
_tmruebung.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 425;BA.debugLine="tmrPause.Enabled = True";
_tmrpause.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 428;BA.debugLine="End Sub";
return "";
}
}
