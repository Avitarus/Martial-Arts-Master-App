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

public class wettkampf extends Activity implements B4AActivity{
	public static wettkampf mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.wettkampf");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (wettkampf).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.wettkampf");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.wettkampf", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (wettkampf) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (wettkampf) Resume **");
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
		return wettkampf.class;
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
        BA.LogInfo("** Activity (wettkampf) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (wettkampf) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _tmrtimer1 = null;
public static anywheresoftware.b4a.objects.Timer _tmrshakehands = null;
public static anywheresoftware.b4a.objects.Timer _tmrtimedelay = null;
public static anywheresoftware.b4a.objects.Timer _tmrkampfzeit = null;
public static anywheresoftware.b4a.objects.Timer _tmrpausenzeit = null;
public static int _loadid0 = 0;
public static int _playid0 = 0;
public static int _loadid2 = 0;
public static int _playid2 = 0;
public static int _loadid3 = 0;
public static int _playid3 = 0;
public static int _loadid4 = 0;
public static int _playid4 = 0;
public static int _loadid5 = 0;
public static int _playid5 = 0;
public static int _loadid1 = 0;
public static int _playid1 = 0;
public static int _loadid7 = 0;
public static int _playid7 = 0;
public static int _loadid8 = 0;
public static int _playid8 = 0;
public static int _loadid6 = 0;
public static int _playid6 = 0;
public static anywheresoftware.b4a.audio.SoundPoolWrapper _sp = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblueberschrift = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcounter = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldaten = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgesamtzeit = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllaut = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnbeenden = null;
public static int _kampfende = 0;
public static int _counter = 0;
public static int _runde = 0;
public static int _kampfpause = 0;
public static String _strlabeltext = "";
public anywheresoftware.b4a.phone.Phone _p = null;
public static int _i = 0;
public anywheresoftware.b4a.objects.SeekBarWrapper _seblaut = null;
public anywheresoftware.b4a.phone.Phone.PhoneWakeState _pws = null;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
public de.watchkido.mama.startabfragen _startabfragen = null;
public de.watchkido.mama.einstellungenapp _einstellungenapp = null;
public de.watchkido.mama.erfolgsmeldung _erfolgsmeldung = null;
public de.watchkido.mama.checkliste _checkliste = null;
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
 //BA.debugLineNum = 38;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 40;BA.debugLine="Activity.LoadLayout(\"Wettkampf\")";
mostCurrent._activity.LoadLayout("Wettkampf",mostCurrent.activityBA);
 //BA.debugLineNum = 41;BA.debugLine="Activity.Title = \"MAMA´s Wettkampf Timer\"";
mostCurrent._activity.setTitle((Object)("MAMA´s Wettkampf Timer"));
 //BA.debugLineNum = 43;BA.debugLine="pws.KeepAlive(True) ' in Activity-Create WACHBLEIBEN";
mostCurrent._pws.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 45;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 46;BA.debugLine="SP.Initialize(6)";
_sp.Initialize((int) (6));
 //BA.debugLineNum = 47;BA.debugLine="LoadId0 = SP.Load(File.DirAssets, \"ShakeHands1.mp3\")";
_loadid0 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ShakeHands1.mp3");
 //BA.debugLineNum = 48;BA.debugLine="LoadId1 = SP.Load(File.DirAssets, \"Fight1.mp3\")";
_loadid1 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Fight1.mp3");
 //BA.debugLineNum = 49;BA.debugLine="LoadId2 = SP.Load(File.DirAssets, \"Break1.mp3\")";
_loadid2 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Break1.mp3");
 //BA.debugLineNum = 50;BA.debugLine="LoadId3 = SP.Load(File.DirAssets, \"Glocke1.wav\")";
_loadid3 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Glocke1.wav");
 //BA.debugLineNum = 51;BA.debugLine="LoadId4 = SP.Load(File.DirAssets, \"Glocke3.mp3\")";
_loadid4 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Glocke3.mp3");
 //BA.debugLineNum = 52;BA.debugLine="LoadId5 = SP.Load(File.DirAssets, \"Glocke7.wav\")";
_loadid5 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Glocke7.wav");
 //BA.debugLineNum = 53;BA.debugLine="LoadId6 = SP.Load(File.DirAssets, \"Stop1.mp3\")";
_loadid6 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Stop1.mp3");
 };
 //BA.debugLineNum = 59;BA.debugLine="lblUeberschrift.TextSize = 40";
mostCurrent._lblueberschrift.setTextSize((float) (40));
 //BA.debugLineNum = 60;BA.debugLine="lblCounter.TextSize = 60";
mostCurrent._lblcounter.setTextSize((float) (60));
 //BA.debugLineNum = 61;BA.debugLine="lblGesamtzeit.textsize = 30";
mostCurrent._lblgesamtzeit.setTextSize((float) (30));
 //BA.debugLineNum = 62;BA.debugLine="lblDaten.TextSize = 12";
mostCurrent._lbldaten.setTextSize((float) (12));
 //BA.debugLineNum = 67;BA.debugLine="tmrTimer1.Initialize(\"tmrTimer1\", 1000 )' 1 sec";
_tmrtimer1.Initialize(processBA,"tmrTimer1",(long) (1000));
 //BA.debugLineNum = 68;BA.debugLine="tmrTimedelay.Initialize(\"tmrTimedelay\", EinstellungenWettkampf.Timedelay) '3000 = die abgezogene Shakehandzeit";
_tmrtimedelay.Initialize(processBA,"tmrTimedelay",(long) (mostCurrent._einstellungenwettkampf._timedelay));
 //BA.debugLineNum = 69;BA.debugLine="tmrKampfZeit.Initialize(\"tmrKampfZeit\", EinstellungenWettkampf.Kampfzeit )";
_tmrkampfzeit.Initialize(processBA,"tmrKampfZeit",(long) (mostCurrent._einstellungenwettkampf._kampfzeit));
 //BA.debugLineNum = 70;BA.debugLine="tmrPausenZeit.Initialize(\"tmrPausenZeit\", EinstellungenWettkampf.Pause )";
_tmrpausenzeit.Initialize(processBA,"tmrPausenZeit",(long) (mostCurrent._einstellungenwettkampf._pause));
 //BA.debugLineNum = 71;BA.debugLine="tmrShakeHands.Initialize(\"tmrShakeHands\", 2000)";
_tmrshakehands.Initialize(processBA,"tmrShakeHands",(long) (2000));
 //BA.debugLineNum = 72;BA.debugLine="Kampfzeitberechnung ' brechnung der gesamtKampfzeit";
_kampfzeitberechnung();
 //BA.debugLineNum = 74;BA.debugLine="strLabelText = \"Vorbereitung\"";
mostCurrent._strlabeltext = "Vorbereitung";
 //BA.debugLineNum = 76;BA.debugLine="lblDaten.Text = \"Gesamtkampfzeit:  \"& Kampfende &\" sec\"& CRLF &\"Zeit zum Kampf:  \"& (EinstellungenWettkampf.Timedelay/1000) &\" sec\"& CRLF & \"Kampfzeit:  \" & (EinstellungenWettkampf.Kampfzeit/1000) & \" sec\"& CRLF & \"Pausenzeit:  \" & (EinstellungenWettkampf.Pause/1000) & \" sec\"& CRLF & \"Runden:  \" & EinstellungenWettkampf.Runden";
mostCurrent._lbldaten.setText((Object)("Gesamtkampfzeit:  "+BA.NumberToString(_kampfende)+" sec"+anywheresoftware.b4a.keywords.Common.CRLF+"Zeit zum Kampf:  "+BA.NumberToString((mostCurrent._einstellungenwettkampf._timedelay/(double)1000))+" sec"+anywheresoftware.b4a.keywords.Common.CRLF+"Kampfzeit:  "+BA.NumberToString((mostCurrent._einstellungenwettkampf._kampfzeit/(double)1000))+" sec"+anywheresoftware.b4a.keywords.Common.CRLF+"Pausenzeit:  "+BA.NumberToString((mostCurrent._einstellungenwettkampf._pause/(double)1000))+" sec"+anywheresoftware.b4a.keywords.Common.CRLF+"Runden:  "+BA.NumberToString(mostCurrent._einstellungenwettkampf._runden)));
 //BA.debugLineNum = 81;BA.debugLine="sebLaut.Value = p.GetVolume(p.VOLUME_MUSIC)";
mostCurrent._seblaut.setValue(mostCurrent._p.GetVolume(mostCurrent._p.VOLUME_MUSIC));
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 217;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 219;BA.debugLine="pws.ReleaseKeepAlive ' Activity_Pause WACHBLEIBEN";
mostCurrent._pws.ReleaseKeepAlive();
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 210;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 213;BA.debugLine="pws.KeepAlive(False)' Activity Resume WACHBLEIBEN";
mostCurrent._pws.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
public static String  _btnbeenden_click() throws Exception{
 //BA.debugLineNum = 199;BA.debugLine="Sub btnBeenden_Click";
 //BA.debugLineNum = 200;BA.debugLine="tmrTimer1.Enabled = False";
_tmrtimer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 201;BA.debugLine="tmrPausenZeit.Enabled = False";
_tmrpausenzeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 202;BA.debugLine="tmrKampfZeit.Enabled = False";
_tmrkampfzeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 203;BA.debugLine="tmrShakeHands.Enabled = False";
_tmrshakehands.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 204;BA.debugLine="tmrTimedelay.Enabled = False";
_tmrtimedelay.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 206;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public static String  _btnneustart_click() throws Exception{
 //BA.debugLineNum = 232;BA.debugLine="Sub btnNeustart_Click";
 //BA.debugLineNum = 235;BA.debugLine="tmrTimer1.Enabled = False";
_tmrtimer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 236;BA.debugLine="tmrKampfZeit.Enabled = False";
_tmrkampfzeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 237;BA.debugLine="tmrPausenZeit.Enabled = False";
_tmrpausenzeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 238;BA.debugLine="tmrShakeHands.Enabled = False";
_tmrshakehands.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 239;BA.debugLine="tmrTimedelay.Enabled = False";
_tmrtimedelay.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 240;BA.debugLine="Kampfzeitberechnung ' berechnung der gesamtkampfzeit";
_kampfzeitberechnung();
 //BA.debugLineNum = 241;BA.debugLine="Counter = EinstellungenWettkampf.Timedelay /1000";
_counter = (int) (mostCurrent._einstellungenwettkampf._timedelay/(double)1000);
 //BA.debugLineNum = 242;BA.debugLine="Counter = Counter + 3 '3 sec shakehands";
_counter = (int) (_counter+3);
 //BA.debugLineNum = 243;BA.debugLine="lblUeberschrift.Text = \"Vorbereiten!\"";
mostCurrent._lblueberschrift.setText((Object)("Vorbereiten!"));
 //BA.debugLineNum = 244;BA.debugLine="Runde = 1";
_runde = (int) (1);
 //BA.debugLineNum = 245;BA.debugLine="Kampfpause = 1";
_kampfpause = (int) (1);
 //BA.debugLineNum = 246;BA.debugLine="Activity.Color = Colors.Blue";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 247;BA.debugLine="lblCounter.TextColor = Colors.White";
mostCurrent._lblcounter.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 248;BA.debugLine="lblUeberschrift.TextColor = Colors.White";
mostCurrent._lblueberschrift.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 249;BA.debugLine="lblDaten.TextColor = Colors.White";
mostCurrent._lbldaten.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 250;BA.debugLine="lblGesamtzeit.TextColor = Colors.white";
mostCurrent._lblgesamtzeit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 251;BA.debugLine="lblLaut.TextColor = Colors.White";
mostCurrent._lbllaut.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 252;BA.debugLine="tmrTimer1.Enabled = True";
_tmrtimer1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 253;BA.debugLine="tmrTimedelay.enabled = True";
_tmrtimedelay.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 254;BA.debugLine="lblCounter.TextSize = 120";
mostCurrent._lblcounter.setTextSize((float) (120));
 //BA.debugLineNum = 260;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="Dim lblUeberschrift, lblCounter, lblDaten,lblGesamtzeit, lblLaut  As Label";
mostCurrent._lblueberschrift = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblcounter = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lbldaten = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblgesamtzeit = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lbllaut = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim btnBeenden As Button";
mostCurrent._btnbeenden = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim Kampfende, Counter, Runde, Kampfpause As Int";
_kampfende = 0;
_counter = 0;
_runde = 0;
_kampfpause = 0;
 //BA.debugLineNum = 26;BA.debugLine="Dim strLabelText As String";
mostCurrent._strlabeltext = "";
 //BA.debugLineNum = 27;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 28;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 29;BA.debugLine="Dim sebLaut As SeekBar";
mostCurrent._seblaut = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim pws As PhoneWakeState ' in Sub Globals WACHBLEIBEN";
mostCurrent._pws = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return "";
}
public static String  _kampfzeitberechnung() throws Exception{
 //BA.debugLineNum = 263;BA.debugLine="Sub Kampfzeitberechnung";
 //BA.debugLineNum = 265;BA.debugLine="Counter = EinstellungenWettkampf.Timedelay /1000";
_counter = (int) (mostCurrent._einstellungenwettkampf._timedelay/(double)1000);
 //BA.debugLineNum = 266;BA.debugLine="Kampfende = (EinstellungenWettkampf.Timedelay + 2000)+(EinstellungenWettkampf.Kampfzeit * EinstellungenWettkampf.Runden)";
_kampfende = (int) ((mostCurrent._einstellungenwettkampf._timedelay+2000)+(mostCurrent._einstellungenwettkampf._kampfzeit*mostCurrent._einstellungenwettkampf._runden));
 //BA.debugLineNum = 267;BA.debugLine="Kampfende = Kampfende + EinstellungenWettkampf.Pause * (EinstellungenWettkampf.Runden - 1)";
_kampfende = (int) (_kampfende+mostCurrent._einstellungenwettkampf._pause*(mostCurrent._einstellungenwettkampf._runden-1));
 //BA.debugLineNum = 268;BA.debugLine="Kampfende = (Kampfende/1000)'/60";
_kampfende = (int) ((_kampfende/(double)1000));
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
return "";
}
public static String  _message() throws Exception{
String _a = "";
 //BA.debugLineNum = 272;BA.debugLine="Sub message";
 //BA.debugLineNum = 273;BA.debugLine="Dim a As String";
_a = "";
 //BA.debugLineNum = 279;BA.debugLine="a = a & \"Runden gesamt = \"& EinstellungenWettkampf.Runden & CRLF";
_a = _a+"Runden gesamt = "+BA.NumberToString(mostCurrent._einstellungenwettkampf._runden)+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 280;BA.debugLine="a = a & \"Kampfpause = \"& Kampfpause& CRLF";
_a = _a+"Kampfpause = "+BA.NumberToString(_kampfpause)+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 282;BA.debugLine="a = a & \"Noch Runden = \" & Runde & CRLF";
_a = _a+"Noch Runden = "+BA.NumberToString(_runde)+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 283;BA.debugLine="a = a & \" sebLautstärke\"& sebLaut.Value& CRLF";
_a = _a+" sebLautstärke"+BA.NumberToString(mostCurrent._seblaut.getValue())+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 292;BA.debugLine="ToastMessageShow(a,False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(_a,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 294;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 13;BA.debugLine="Dim tmrTimer1, tmrShakeHands, tmrTimedelay, tmrKampfZeit, tmrPausenZeit As Timer";
_tmrtimer1 = new anywheresoftware.b4a.objects.Timer();
_tmrshakehands = new anywheresoftware.b4a.objects.Timer();
_tmrtimedelay = new anywheresoftware.b4a.objects.Timer();
_tmrkampfzeit = new anywheresoftware.b4a.objects.Timer();
_tmrpausenzeit = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 14;BA.debugLine="Dim LoadId0, PlayId0, LoadId2, PlayId2, LoadId3, PlayId3 As Int";
_loadid0 = 0;
_playid0 = 0;
_loadid2 = 0;
_playid2 = 0;
_loadid3 = 0;
_playid3 = 0;
 //BA.debugLineNum = 15;BA.debugLine="Dim LoadId4, PlayId4, LoadId5, PlayId5, LoadId1, PlayId1 As Int";
_loadid4 = 0;
_playid4 = 0;
_loadid5 = 0;
_playid5 = 0;
_loadid1 = 0;
_playid1 = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim LoadId7, PlayId7, LoadId8, PlayId8, LoadId6, PlayId6 As Int";
_loadid7 = 0;
_playid7 = 0;
_loadid8 = 0;
_playid8 = 0;
_loadid6 = 0;
_playid6 = 0;
 //BA.debugLineNum = 17;BA.debugLine="Dim SP As SoundPool";
_sp = new anywheresoftware.b4a.audio.SoundPoolWrapper();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _seblaut_valuechanged(int _value,boolean _userchanged) throws Exception{
String _aktuellerunterordner = "";
int _result = 0;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 296;BA.debugLine="Sub sebLaut_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 298;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
_aktuellerunterordner = "";
 //BA.debugLineNum = 298;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
_aktuellerunterordner = "/mama/Daten";
 //BA.debugLineNum = 299;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 300;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 303;BA.debugLine="p.SetVolume(p.VOLUME_MUSIC, Value, False)";
mostCurrent._p.SetVolume(mostCurrent._p.VOLUME_MUSIC,_value,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 315;BA.debugLine="End Sub";
return "";
}
public static String  _tmrkampfzeit_tick() throws Exception{
 //BA.debugLineNum = 138;BA.debugLine="Sub tmrKampfZeit_Tick";
 //BA.debugLineNum = 139;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 140;BA.debugLine="i= 1";
_i = (int) (1);
 //BA.debugLineNum = 143;BA.debugLine="If Runde <= EinstellungenWettkampf.Runden - 1 Then";
if (_runde<=mostCurrent._einstellungenwettkampf._runden-1) { 
 //BA.debugLineNum = 144;BA.debugLine="tmrKampfZeit.Enabled = False";
_tmrkampfzeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="tmrPausenZeit.Enabled = True";
_tmrpausenzeit.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="lblUeberschrift.Text = \"Pause \"& Kampfpause & \"/\"& ( EinstellungenWettkampf.Runden - 1)";
mostCurrent._lblueberschrift.setText((Object)("Pause "+BA.NumberToString(_kampfpause)+"/"+BA.NumberToString((mostCurrent._einstellungenwettkampf._runden-1))));
 //BA.debugLineNum = 147;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 148;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glocke3";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 149;BA.debugLine="Activity.Color = Colors.Red";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 150;BA.debugLine="Counter = EinstellungenWettkampf.Pause /1000";
_counter = (int) (mostCurrent._einstellungenwettkampf._pause/(double)1000);
 //BA.debugLineNum = 151;BA.debugLine="strLabelText = \"Pause\"";
mostCurrent._strlabeltext = "Pause";
 //BA.debugLineNum = 152;BA.debugLine="Runde = Runde + 1";
_runde = (int) (_runde+1);
 //BA.debugLineNum = 154;BA.debugLine="Activity.Color = Colors.Red";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 }else {
 //BA.debugLineNum = 157;BA.debugLine="tmrPausenZeit.Enabled = False";
_tmrpausenzeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 158;BA.debugLine="tmrKampfZeit.Enabled = False";
_tmrkampfzeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 159;BA.debugLine="PlayId5 = SP.Play(LoadId5, 1, 1, 1, 0, 1) ' Glocke";
_playid5 = _sp.Play(_loadid5,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 160;BA.debugLine="PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop";
_playid6 = _sp.Play(_loadid6,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 161;BA.debugLine="PlayId5 = SP.Play(LoadId5, 1, 1, 1, 0, 1) ' Glocke7";
_playid5 = _sp.Play(_loadid5,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 162;BA.debugLine="lblCounter.Text = \"Ende\"";
mostCurrent._lblcounter.setText((Object)("Ende"));
 //BA.debugLineNum = 163;BA.debugLine="tmrTimer1.Enabled = False";
_tmrtimer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 164;BA.debugLine="lblUeberschrift.Text = \"Kampfende\"";
mostCurrent._lblueberschrift.setText((Object)("Kampfende"));
 //BA.debugLineNum = 165;BA.debugLine="Activity.Color = Colors.Black";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 166;BA.debugLine="lblCounter.TextSize = 60";
mostCurrent._lblcounter.setTextSize((float) (60));
 };
 //BA.debugLineNum = 169;BA.debugLine="lblCounter.TextColor = Colors.white";
mostCurrent._lblcounter.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 170;BA.debugLine="lblUeberschrift.TextColor = Colors.white";
mostCurrent._lblueberschrift.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 171;BA.debugLine="lblDaten.TextColor = Colors.White";
mostCurrent._lbldaten.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 172;BA.debugLine="lblGesamtzeit.TextColor = Colors.white";
mostCurrent._lblgesamtzeit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 173;BA.debugLine="lblLaut.TextColor = Colors.White";
mostCurrent._lbllaut.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}
public static String  _tmrpausenzeit_tick() throws Exception{
 //BA.debugLineNum = 178;BA.debugLine="Sub tmrPausenZeit_Tick";
 //BA.debugLineNum = 180;BA.debugLine="PlayId3 = SP.Play(LoadId3, 1, 1, 1, 0, 1) ' Glocke1";
_playid3 = _sp.Play(_loadid3,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 181;BA.debugLine="PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1) ' fight";
_playid1 = _sp.Play(_loadid1,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 182;BA.debugLine="tmrPausenZeit.Enabled = False";
_tmrpausenzeit.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 183;BA.debugLine="tmrKampfZeit.Enabled = True";
_tmrkampfzeit.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 184;BA.debugLine="lblUeberschrift.Text = \"Fight \"& (Runde)& \"/\"& EinstellungenWettkampf.Runden";
mostCurrent._lblueberschrift.setText((Object)("Fight "+BA.NumberToString((_runde))+"/"+BA.NumberToString(mostCurrent._einstellungenwettkampf._runden)));
 //BA.debugLineNum = 185;BA.debugLine="Counter = EinstellungenWettkampf.Kampfzeit /1000";
_counter = (int) (mostCurrent._einstellungenwettkampf._kampfzeit/(double)1000);
 //BA.debugLineNum = 186;BA.debugLine="Kampfpause = Kampfpause + 1";
_kampfpause = (int) (_kampfpause+1);
 //BA.debugLineNum = 187;BA.debugLine="strLabelText = \"Runde\"";
mostCurrent._strlabeltext = "Runde";
 //BA.debugLineNum = 188;BA.debugLine="Activity.Color = Colors.Green";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 189;BA.debugLine="lblCounter.TextColor = Colors.Black";
mostCurrent._lblcounter.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 190;BA.debugLine="lblUeberschrift.TextColor = Colors.Black";
mostCurrent._lblueberschrift.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 191;BA.debugLine="lblDaten.TextColor = Colors.Black";
mostCurrent._lbldaten.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 192;BA.debugLine="lblGesamtzeit.TextColor = Colors.Black";
mostCurrent._lblgesamtzeit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 193;BA.debugLine="lblLaut.TextColor = Colors.Black";
mostCurrent._lbllaut.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 195;BA.debugLine="End Sub";
return "";
}
public static String  _tmrshakehands_tick() throws Exception{
 //BA.debugLineNum = 119;BA.debugLine="Sub tmrShakeHands_Tick";
 //BA.debugLineNum = 121;BA.debugLine="PlayId3 = SP.Play(LoadId3, 1, 1, 1, 0, 1) ' Glocke1";
_playid3 = _sp.Play(_loadid3,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 122;BA.debugLine="PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1) ' fight";
_playid1 = _sp.Play(_loadid1,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 123;BA.debugLine="tmrShakeHands.Enabled = False";
_tmrshakehands.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 124;BA.debugLine="tmrKampfZeit.Enabled = True";
_tmrkampfzeit.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 125;BA.debugLine="lblUeberschrift.Text = \"Fight \"& (Runde)& \"/\"& EinstellungenWettkampf.Runden";
mostCurrent._lblueberschrift.setText((Object)("Fight "+BA.NumberToString((_runde))+"/"+BA.NumberToString(mostCurrent._einstellungenwettkampf._runden)));
 //BA.debugLineNum = 126;BA.debugLine="Counter = EinstellungenWettkampf.Kampfzeit /1000";
_counter = (int) (mostCurrent._einstellungenwettkampf._kampfzeit/(double)1000);
 //BA.debugLineNum = 127;BA.debugLine="strLabelText = \"Runde\"";
mostCurrent._strlabeltext = "Runde";
 //BA.debugLineNum = 128;BA.debugLine="Activity.Color = Colors.Green";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 129;BA.debugLine="lblCounter.TextColor = Colors.Black";
mostCurrent._lblcounter.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 130;BA.debugLine="lblUeberschrift.TextColor = Colors.Black";
mostCurrent._lblueberschrift.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 131;BA.debugLine="lblDaten.TextColor = Colors.Black";
mostCurrent._lbldaten.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 132;BA.debugLine="lblGesamtzeit.TextColor = Colors.Black";
mostCurrent._lblgesamtzeit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 133;BA.debugLine="lblLaut.TextColor = Colors.Black";
mostCurrent._lbllaut.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _tmrtimedelay_tick() throws Exception{
 //BA.debugLineNum = 105;BA.debugLine="Sub tmrTimedelay_Tick";
 //BA.debugLineNum = 108;BA.debugLine="PlayId0 = SP.Play(LoadId0, 1, 1, 1, 0, 1) 'shake hands";
_playid0 = _sp.Play(_loadid0,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 109;BA.debugLine="tmrTimedelay.Enabled = False";
_tmrtimedelay.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 110;BA.debugLine="tmrShakeHands.Enabled =  True";
_tmrshakehands.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 111;BA.debugLine="lblUeberschrift.Text = \"Vorbereiten!\"";
mostCurrent._lblueberschrift.setText((Object)("Vorbereiten!"));
 //BA.debugLineNum = 112;BA.debugLine="strLabelText = \"Vorbereitung\"";
mostCurrent._strlabeltext = "Vorbereitung";
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}
public static String  _tmrtimer1_tick() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Sub tmrTimer1_Tick";
 //BA.debugLineNum = 92;BA.debugLine="Kampfende = Kampfende - 1";
_kampfende = (int) (_kampfende-1);
 //BA.debugLineNum = 93;BA.debugLine="Counter = Counter - 1";
_counter = (int) (_counter-1);
 //BA.debugLineNum = 95;BA.debugLine="lblCounter.Text = Counter  & \" s \"' &  strLabelText";
mostCurrent._lblcounter.setText((Object)(BA.NumberToString(_counter)+" s "));
 //BA.debugLineNum = 96;BA.debugLine="lblGesamtzeit.Text = \"Noch: \"  & (Kampfende) & \" sec\"";
mostCurrent._lblgesamtzeit.setText((Object)("Noch: "+BA.NumberToString((_kampfende))+" sec"));
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
}
