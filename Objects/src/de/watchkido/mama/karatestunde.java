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

public class karatestunde extends Activity implements B4AActivity{
	public static karatestunde mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.karatestunde");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (karatestunde).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.karatestunde");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.karatestunde", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (karatestunde) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (karatestunde) Resume **");
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
		return karatestunde.class;
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
        BA.LogInfo("** Activity (karatestunde) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (karatestunde) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _bildertimer = null;
public static anywheresoftware.b4a.objects.Timer _zaehltimer = null;
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
public static String _startverzeichnis = "";
public static String _aktuellesbild = "";
public static String _aktuellesbildanzeige = "";
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
public static int _seczaehler = 0;
public static int _count = 0;
public static int _mengeallerdateien = 0;
public static int _bildnummer = 0;
public static String[] _picsarray = null;
public anywheresoftware.b4a.phone.Phone.PhoneWakeState _pws = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _seblaut = null;
public anywheresoftware.b4a.objects.collections.List _listederbilder = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltrainingsanzeige = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
public static int _anzeigezeit = 0;
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
public de.watchkido.mama.einstellungentrainingstunde _einstellungentrainingstunde = null;
public de.watchkido.mama.benachrichtigung _benachrichtigung = null;
public de.watchkido.mama.downloadservice _downloadservice = null;
public de.watchkido.mama.kampfsportlexikon _kampfsportlexikon = null;
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
 //BA.debugLineNum = 36;BA.debugLine="Activity.LoadLayout(\"KarateStunde\")";
mostCurrent._activity.LoadLayout("KarateStunde",mostCurrent.activityBA);
 //BA.debugLineNum = 37;BA.debugLine="Activity.Title = \"Training Karate\"";
mostCurrent._activity.setTitle((Object)("Training Karate"));
 //BA.debugLineNum = 40;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 41;BA.debugLine="SP.Initialize(4)";
_sp.Initialize((int) (4));
 //BA.debugLineNum = 42;BA.debugLine="LoadId0 = SP.Load(File.DirRootExternal & Main.Unterordner17, \"ShakeHands2.mp3\")";
_loadid0 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner17,"ShakeHands2.mp3");
 //BA.debugLineNum = 43;BA.debugLine="LoadId1 = SP.Load(File.DirRootExternal & Main.Unterordner17, \"Fight2.mp3\")";
_loadid1 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner17,"Fight2.mp3");
 //BA.debugLineNum = 44;BA.debugLine="LoadId2 = SP.Load(File.DirRootExternal & Main.Unterordner17, \"Stop2.mp3\")";
_loadid2 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner17,"Stop2.mp3");
 //BA.debugLineNum = 45;BA.debugLine="LoadId3 = SP.Load(File.DirRootExternal & Main.Unterordner17, \"Break1.mp3\")";
_loadid3 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner17,"Break1.mp3");
 //BA.debugLineNum = 46;BA.debugLine="LoadId4 = SP.Load(File.DirRootExternal & Main.Unterordner17, \"Glocke1.wav\")";
_loadid4 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner17,"Glocke1.wav");
 //BA.debugLineNum = 47;BA.debugLine="LoadId5 = SP.Load(File.DirRootExternal & Main.Unterordner17, \"Glocke3.mp3\")";
_loadid5 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner17,"Glocke3.mp3");
 //BA.debugLineNum = 48;BA.debugLine="LoadId6 = SP.Load(File.DirRootExternal & Main.Unterordner17, \"Glocke7.wav\")";
_loadid6 = _sp.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner17,"Glocke7.wav");
 };
 //BA.debugLineNum = 51;BA.debugLine="sebLaut.Initialize(\"seblaut\")";
mostCurrent._seblaut.Initialize(mostCurrent.activityBA,"seblaut");
 //BA.debugLineNum = 52;BA.debugLine="Activity.Color = Colors.Green";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 53;BA.debugLine="pws.KeepAlive(True) ' angeschaltet lassen";
mostCurrent._pws.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 94;BA.debugLine="Bildnummer = 0";
_bildnummer = (int) (0);
 //BA.debugLineNum = 96;BA.debugLine="ListeDerBilder.Initialize";
mostCurrent._listederbilder.Initialize();
 //BA.debugLineNum = 97;BA.debugLine="StartVerzeichnis = File.DirRootExternal & Main.Unterordner6";
mostCurrent._startverzeichnis = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner6;
 //BA.debugLineNum = 98;BA.debugLine="ListeDerBilder = File.ListFiles (StartVerzeichnis)";
mostCurrent._listederbilder = anywheresoftware.b4a.keywords.Common.File.ListFiles(mostCurrent._startverzeichnis);
 //BA.debugLineNum = 99;BA.debugLine="If ListeDerBilder.Size > 0 Then";
if (mostCurrent._listederbilder.getSize()>0) { 
 //BA.debugLineNum = 100;BA.debugLine="MengeAllerDateien = ListeDerBilder.Size";
_mengeallerdateien = mostCurrent._listederbilder.getSize();
 };
 //BA.debugLineNum = 115;BA.debugLine="Activity.AddMenuItem(\"Starten\",\"Button0\")";
mostCurrent._activity.AddMenuItem("Starten","Button0");
 //BA.debugLineNum = 117;BA.debugLine="Activity.AddMenuItem(\"Bild weiter\",\"Button2\")";
mostCurrent._activity.AddMenuItem("Bild weiter","Button2");
 //BA.debugLineNum = 118;BA.debugLine="Activity.AddMenuItem(\"Einstellungen\",\"Button5\")";
mostCurrent._activity.AddMenuItem("Einstellungen","Button5");
 //BA.debugLineNum = 119;BA.debugLine="Activity.AddMenuItem(\"Timer Stop\",\"Button3\")";
mostCurrent._activity.AddMenuItem("Timer Stop","Button3");
 //BA.debugLineNum = 120;BA.debugLine="Activity.AddMenuItem(\"Timer weiter\",\"Button4\")";
mostCurrent._activity.AddMenuItem("Timer weiter","Button4");
 //BA.debugLineNum = 121;BA.debugLine="Activity.AddMenuItem(\"Fehler melden\",\"Button6\")";
mostCurrent._activity.AddMenuItem("Fehler melden","Button6");
 //BA.debugLineNum = 123;BA.debugLine="Bildertimer.Initialize(\"Bildertimer\", Anzeigezeit)";
_bildertimer.Initialize(processBA,"Bildertimer",(long) (_anzeigezeit));
 //BA.debugLineNum = 124;BA.debugLine="Zaehltimer.Initialize(\"Zaehltimer\",1000)";
_zaehltimer.Initialize(processBA,"Zaehltimer",(long) (1000));
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 144;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 146;BA.debugLine="pws.ReleaseKeepAlive ' Activity_Pause WACHBLEIBEN";
mostCurrent._pws.ReleaseKeepAlive();
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 138;BA.debugLine="pws.KeepAlive(False)' ausgeschaltet beim beenden?";
mostCurrent._pws.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 139;BA.debugLine="Zaehltimer.Enabled = False";
_zaehltimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 140;BA.debugLine="Bildertimer.Enabled = False";
_bildertimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return "";
}
public static String  _bildertimer_tick() throws Exception{
 //BA.debugLineNum = 156;BA.debugLine="Sub Bildertimer_Tick";
 //BA.debugLineNum = 158;BA.debugLine="If Count + 1 > MengeAllerDateien Then";
if (_count+1>_mengeallerdateien) { 
 //BA.debugLineNum = 159;BA.debugLine="Count = 0";
_count = (int) (0);
 }else {
 };
 //BA.debugLineNum = 164;BA.debugLine="AktuellesBild = ListeDerBilder.Get (Count)";
mostCurrent._aktuellesbild = BA.ObjectToString(mostCurrent._listederbilder.Get(_count));
 //BA.debugLineNum = 165;BA.debugLine="ImageView1.Bitmap = LoadBitmap(StartVerzeichnis,AktuellesBild)";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._startverzeichnis,mostCurrent._aktuellesbild).getObject()));
 //BA.debugLineNum = 168;BA.debugLine="AktuellesBildAnzeige = AktuellesBild.SubString(3)";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbild.substring((int) (3));
 //BA.debugLineNum = 169;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".jpg\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".jpg","");
 //BA.debugLineNum = 170;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".gif\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".gif","");
 //BA.debugLineNum = 171;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".png\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".png","");
 //BA.debugLineNum = 172;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".JPG\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".JPG","");
 //BA.debugLineNum = 173;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".GIF\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".GIF","");
 //BA.debugLineNum = 174;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".PNG\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".PNG","");
 //BA.debugLineNum = 175;BA.debugLine="lbltrainingsanzeige.Text = Count & \". \"& AktuellesBildAnzeige";
mostCurrent._lbltrainingsanzeige.setText((Object)(BA.NumberToString(_count)+". "+mostCurrent._aktuellesbildanzeige));
 //BA.debugLineNum = 177;BA.debugLine="Count = Count + 1";
_count = (int) (_count+1);
 //BA.debugLineNum = 178;BA.debugLine="SecZaehler = 0";
_seczaehler = (int) (0);
 //BA.debugLineNum = 180;BA.debugLine="PlayId0 = SP.Play(LoadId0, 1, 1, 1, 0, 1)";
_playid0 = _sp.Play(_loadid0,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 181;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1)";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return "";
}
public static String  _button0_click() throws Exception{
 //BA.debugLineNum = 209;BA.debugLine="Sub Button0_click";
 //BA.debugLineNum = 212;BA.debugLine="Bildertimer.Enabled = True";
_bildertimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 213;BA.debugLine="Zaehltimer.Enabled = True";
_zaehltimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 214;BA.debugLine="PlayId0 = SP.Play(LoadId0, 1, 1, 1, 0, 1)";
_playid0 = _sp.Play(_loadid0,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 215;BA.debugLine="PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1)";
_playid4 = _sp.Play(_loadid4,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 218;BA.debugLine="Bildertimer_Tick 'Start the rotation right away";
_bildertimer_tick();
 //BA.debugLineNum = 223;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 229;BA.debugLine="Bildnummer = Bildnummer - 1";
_bildnummer = (int) (_bildnummer-1);
 //BA.debugLineNum = 230;BA.debugLine="If Bildnummer -1 < 0 Then";
if (_bildnummer-1<0) { 
 //BA.debugLineNum = 231;BA.debugLine="Bildnummer = MengeAllerDateien";
_bildnummer = _mengeallerdateien;
 };
 //BA.debugLineNum = 233;BA.debugLine="AktuellesBild = ListeDerBilder.Get (Bildnummer)";
mostCurrent._aktuellesbild = BA.ObjectToString(mostCurrent._listederbilder.Get(_bildnummer));
 //BA.debugLineNum = 234;BA.debugLine="ImageView1.Bitmap=LoadBitmap(StartVerzeichnis,AktuellesBild)";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._startverzeichnis,mostCurrent._aktuellesbild).getObject()));
 //BA.debugLineNum = 238;BA.debugLine="AktuellesBildAnzeige = AktuellesBild.SubString(3)";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbild.substring((int) (3));
 //BA.debugLineNum = 239;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".jpg\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".jpg","");
 //BA.debugLineNum = 240;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".gif\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".gif","");
 //BA.debugLineNum = 241;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".png\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".png","");
 //BA.debugLineNum = 242;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".JPG\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".JPG","");
 //BA.debugLineNum = 243;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".GIF\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".GIF","");
 //BA.debugLineNum = 244;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".PNG\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".PNG","");
 //BA.debugLineNum = 245;BA.debugLine="lbltrainingsanzeige.Text = Count & \". \" & AktuellesBildAnzeige";
mostCurrent._lbltrainingsanzeige.setText((Object)(BA.NumberToString(_count)+". "+mostCurrent._aktuellesbildanzeige));
 //BA.debugLineNum = 248;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
 //BA.debugLineNum = 250;BA.debugLine="Sub Button2_Click";
 //BA.debugLineNum = 253;BA.debugLine="Bildnummer = Bildnummer + 1";
_bildnummer = (int) (_bildnummer+1);
 //BA.debugLineNum = 254;BA.debugLine="If Bildnummer +1 > MengeAllerDateien Then";
if (_bildnummer+1>_mengeallerdateien) { 
 //BA.debugLineNum = 255;BA.debugLine="Bildnummer = 0";
_bildnummer = (int) (0);
 };
 //BA.debugLineNum = 257;BA.debugLine="AktuellesBild = ListeDerBilder.Get (Bildnummer)";
mostCurrent._aktuellesbild = BA.ObjectToString(mostCurrent._listederbilder.Get(_bildnummer));
 //BA.debugLineNum = 258;BA.debugLine="ImageView1.Bitmap=LoadBitmap(StartVerzeichnis,AktuellesBild)";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._startverzeichnis,mostCurrent._aktuellesbild).getObject()));
 //BA.debugLineNum = 261;BA.debugLine="AktuellesBildAnzeige = AktuellesBild.SubString(3)";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbild.substring((int) (3));
 //BA.debugLineNum = 262;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".jpg\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".jpg","");
 //BA.debugLineNum = 263;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".gif\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".gif","");
 //BA.debugLineNum = 264;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".png\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".png","");
 //BA.debugLineNum = 265;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".JPG\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".JPG","");
 //BA.debugLineNum = 266;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".GIF\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".GIF","");
 //BA.debugLineNum = 267;BA.debugLine="AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(\".PNG\",\"\")";
mostCurrent._aktuellesbildanzeige = mostCurrent._aktuellesbildanzeige.replace(".PNG","");
 //BA.debugLineNum = 268;BA.debugLine="lbltrainingsanzeige.Text = Count & \". \" & AktuellesBildAnzeige";
mostCurrent._lbltrainingsanzeige.setText((Object)(BA.NumberToString(_count)+". "+mostCurrent._aktuellesbildanzeige));
 //BA.debugLineNum = 275;BA.debugLine="End Sub";
return "";
}
public static String  _button3_click() throws Exception{
 //BA.debugLineNum = 277;BA.debugLine="Sub Button3_Click";
 //BA.debugLineNum = 278;BA.debugLine="Bildertimer.Enabled = False";
_bildertimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 279;BA.debugLine="Zaehltimer.Enabled = False";
_zaehltimer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 280;BA.debugLine="End Sub";
return "";
}
public static String  _button4_click() throws Exception{
 //BA.debugLineNum = 282;BA.debugLine="Sub Button4_Click";
 //BA.debugLineNum = 283;BA.debugLine="Bildertimer.Enabled = True";
_bildertimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 284;BA.debugLine="Zaehltimer.Enabled = True";
_zaehltimer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 285;BA.debugLine="End Sub";
return "";
}
public static String  _button5_click() throws Exception{
 //BA.debugLineNum = 288;BA.debugLine="Sub Button5_click";
 //BA.debugLineNum = 290;BA.debugLine="End Sub";
return "";
}
public static String  _button6_click() throws Exception{
 //BA.debugLineNum = 293;BA.debugLine="Sub Button6_click";
 //BA.debugLineNum = 294;BA.debugLine="StartActivity(Benachrichtigung)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._benachrichtigung.getObject()));
 //BA.debugLineNum = 295;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 19;BA.debugLine="Dim StartVerzeichnis, AktuellesBild, AktuellesBildAnzeige As String";
mostCurrent._startverzeichnis = "";
mostCurrent._aktuellesbild = "";
mostCurrent._aktuellesbildanzeige = "";
 //BA.debugLineNum = 20;BA.debugLine="Dim ImageView1 As ImageView";
mostCurrent._imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim SecZaehler, Count, MengeAllerDateien, Bildnummer  As Int";
_seczaehler = 0;
_count = 0;
_mengeallerdateien = 0;
_bildnummer = 0;
 //BA.debugLineNum = 22;BA.debugLine="Dim PicsArray() As String";
mostCurrent._picsarray = new String[(int) (0)];
java.util.Arrays.fill(mostCurrent._picsarray,"");
 //BA.debugLineNum = 23;BA.debugLine="Dim pws As PhoneWakeState";
mostCurrent._pws = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 24;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 25;BA.debugLine="Dim sebLaut As SeekBar";
mostCurrent._seblaut = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim ListeDerBilder As List";
mostCurrent._listederbilder = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 27;BA.debugLine="Dim lbltrainingsanzeige, Label1, Label2, Label3 As Label";
mostCurrent._lbltrainingsanzeige = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim Anzeigezeit As Int : Anzeigezeit = 20000";
_anzeigezeit = 0;
 //BA.debugLineNum = 28;BA.debugLine="Dim Anzeigezeit As Int : Anzeigezeit = 20000";
_anzeigezeit = (int) (20000);
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Dim Bildertimer, Zaehltimer As Timer";
_bildertimer = new anywheresoftware.b4a.objects.Timer();
_zaehltimer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 11;BA.debugLine="Dim SP As SoundPool";
_sp = new anywheresoftware.b4a.audio.SoundPoolWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim LoadId0, PlayId0, LoadId1, PlayId1, LoadId2, PlayId2, LoadId3, PlayId3 As Int";
_loadid0 = 0;
_playid0 = 0;
_loadid1 = 0;
_playid1 = 0;
_loadid2 = 0;
_playid2 = 0;
_loadid3 = 0;
_playid3 = 0;
 //BA.debugLineNum = 13;BA.debugLine="Dim LoadId4, PlayId4, LoadId5, PlayId5, LoadId6, PlayId6 As Int";
_loadid4 = 0;
_playid4 = 0;
_loadid5 = 0;
_playid5 = 0;
_loadid6 = 0;
_playid6 = 0;
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public static String  _seblaut_valuechanged(int _value,boolean _userchanged) throws Exception{
String _aktuellerunterordner = "";
int _result = 0;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 188;BA.debugLine="Sub sebLaut_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 190;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
_aktuellerunterordner = "";
 //BA.debugLineNum = 190;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner = \"/mama/Daten\"";
_aktuellerunterordner = "/mama/Daten";
 //BA.debugLineNum = 191;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 192;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 195;BA.debugLine="p.SetVolume(p.VOLUME_MUSIC, Value, False)";
mostCurrent._p.SetVolume(mostCurrent._p.VOLUME_MUSIC,_value,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public static String  _zaehltimer_tick() throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub Zaehltimer_tick";
 //BA.debugLineNum = 150;BA.debugLine="Label2.Text = SecZaehler";
mostCurrent._label2.setText((Object)(_seczaehler));
 //BA.debugLineNum = 151;BA.debugLine="SecZaehler = Anzeigezeit/1000 -1";
_seczaehler = (int) (_anzeigezeit/(double)1000-1);
 //BA.debugLineNum = 154;BA.debugLine="End Sub";
return "";
}
}
