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

public class tagebuch extends Activity implements B4AActivity{
	public static tagebuch mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.tagebuch");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (tagebuch).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.tagebuch");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.tagebuch", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (tagebuch) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (tagebuch) Resume **");
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
		return tagebuch.class;
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
        BA.LogInfo("** Activity (tagebuch) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (tagebuch) Resume **");
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
public static anywheresoftware.b4a.objects.collections.Map _curvalues = null;
public static long _dob = 0L;
public anywheresoftware.b4a.objects.IME _ime = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _svallgemein = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _svnahrung = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _svmedizin = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _svantropometrie = null;
public de.watchkido.mama.viewmgr _vma = null;
public de.watchkido.mama.viewmgr _vmn = null;
public de.watchkido.mama.viewmgr _vmm = null;
public de.watchkido.mama.viewmgr _vml = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog _dd = null;
public anywheresoftware.b4a.objects.TabHostWrapper _tabhost1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
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
public de.watchkido.mama.karatestunde _karatestunde = null;
public de.watchkido.mama.tts _tts = null;
public de.watchkido.mama.lebensmittel _lebensmittel = null;
public de.watchkido.mama.statemanager _statemanager = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp2 = null;
String _datetext = "";
anywheresoftware.b4a.objects.collections.List _list1 = null;
anywheresoftware.b4a.objects.collections.List _list2 = null;
anywheresoftware.b4a.objects.collections.List _list3 = null;
anywheresoftware.b4a.objects.collections.List _list4 = null;
anywheresoftware.b4a.objects.collections.List _list5 = null;
anywheresoftware.b4a.objects.collections.List _list6 = null;
int _i = 0;
 //BA.debugLineNum = 32;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 37;BA.debugLine="Dim bmp1, bmp2 As Bitmap";
_bmp1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_bmp2 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 38;BA.debugLine="bmp1 = LoadBitmap(File.DirAssets, \"package-x-generic.png\")";
_bmp1 = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"package-x-generic.png");
 //BA.debugLineNum = 39;BA.debugLine="bmp2 = LoadBitmap(File.DirAssets, \"package-x-generic-2.png\")";
_bmp2 = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"package-x-generic-2.png");
 //BA.debugLineNum = 40;BA.debugLine="Tabhost1.Initialize(\"Tabhost1\")";
mostCurrent._tabhost1.Initialize(mostCurrent.activityBA,"Tabhost1");
 //BA.debugLineNum = 51;BA.debugLine="Panel1.Initialize(\"www\")";
mostCurrent._panel1.Initialize(mostCurrent.activityBA,"www");
 //BA.debugLineNum = 53;BA.debugLine="Dim DateText As String";
_datetext = "";
 //BA.debugLineNum = 54;BA.debugLine="If FirstTime = True Then curValues.Initialize";
if (_firsttime==anywheresoftware.b4a.keywords.Common.True) { 
_curvalues.Initialize();};
 //BA.debugLineNum = 55;BA.debugLine="DateTime.TimeFormat = \"yyyy-MM-dd HH:mm\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("yyyy-MM-dd HH:mm");
 //BA.debugLineNum = 56;BA.debugLine="DateTime.DateFormat = \"MM/dd/yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MM/dd/yyyy");
 //BA.debugLineNum = 57;BA.debugLine="If curValues.Size = 0 Then";
if (_curvalues.getSize()==0) { 
 //BA.debugLineNum = 58;BA.debugLine="DOB = DateTime.Now";
_dob = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 };
 //BA.debugLineNum = 60;BA.debugLine="IME.Initialize(\"IME\")";
mostCurrent._ime.Initialize("IME");
 //BA.debugLineNum = 61;BA.debugLine="IME.AddHeightChangedEvent";
mostCurrent._ime.AddHeightChangedEvent(mostCurrent.activityBA);
 //BA.debugLineNum = 62;BA.debugLine="If DOB = 9223372036854775807 Then DateText= \"N/A\" Else DateText= DateTime.Date(DOB)";
if (_dob==9223372036854775807L) { 
_datetext = "N/A";}
else {
_datetext = anywheresoftware.b4a.keywords.Common.DateTime.Date(_dob);};
 //BA.debugLineNum = 63;BA.debugLine="Activity.Color = Colors.RGB(255, 222, 173) ' Navajo White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (222),(int) (173)));
 //BA.debugLineNum = 64;BA.debugLine="Activity.Title = \"Diary\"";
mostCurrent._activity.setTitle((Object)("Diary"));
 //BA.debugLineNum = 65;BA.debugLine="svAllgemein.Initialize(Activity.Height)";
mostCurrent._svallgemein.Initialize(mostCurrent.activityBA,mostCurrent._activity.getHeight());
 //BA.debugLineNum = 66;BA.debugLine="svNahrung.Initialize(Activity.Height -20%y)";
mostCurrent._svnahrung.Initialize(mostCurrent.activityBA,(int) (mostCurrent._activity.getHeight()-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA)));
 //BA.debugLineNum = 67;BA.debugLine="svMedizin.Initialize(Activity.Height -20%y)";
mostCurrent._svmedizin.Initialize(mostCurrent.activityBA,(int) (mostCurrent._activity.getHeight()-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA)));
 //BA.debugLineNum = 68;BA.debugLine="svAntropometrie.Initialize(Activity.Height -20%y)";
mostCurrent._svantropometrie.Initialize(mostCurrent.activityBA,(int) (mostCurrent._activity.getHeight()-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA)));
 //BA.debugLineNum = 74;BA.debugLine="Tabhost1.AddTab2 (\"Allgemein\",svAllgemein)', 0, 0, Activity.Width, Activity.Height)";
mostCurrent._tabhost1.AddTab2("Allgemein",(android.view.View)(mostCurrent._svallgemein.getObject()));
 //BA.debugLineNum = 75;BA.debugLine="Tabhost1.AddTab2 (\"Nahrung\",svNahrung)";
mostCurrent._tabhost1.AddTab2("Nahrung",(android.view.View)(mostCurrent._svnahrung.getObject()));
 //BA.debugLineNum = 76;BA.debugLine="Tabhost1.AddTab2 (\"Medizin\",svMedizin)";
mostCurrent._tabhost1.AddTab2("Medizin",(android.view.View)(mostCurrent._svmedizin.getObject()));
 //BA.debugLineNum = 77;BA.debugLine="Tabhost1.AddTab2 (\"Antropometrie\",svAntropometrie)";
mostCurrent._tabhost1.AddTab2("Antropometrie",(android.view.View)(mostCurrent._svantropometrie.getObject()));
 //BA.debugLineNum = 80;BA.debugLine="Activity.AddView(Tabhost1,0, 0, Activity.Width, Activity.Height)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tabhost1.getObject()),(int) (0),(int) (0),mostCurrent._activity.getWidth(),mostCurrent._activity.getHeight());
 //BA.debugLineNum = 82;BA.debugLine="VMA.Initialize(svAllgemein.Panel, Activity.Width, True, True, True)";
mostCurrent._vma._initialize(mostCurrent.activityBA,mostCurrent._svallgemein.getPanel(),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 83;BA.debugLine="VMN.Initialize(svNahrung.Panel, Activity.Width, True, True, True)";
mostCurrent._vmn._initialize(mostCurrent.activityBA,mostCurrent._svnahrung.getPanel(),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 84;BA.debugLine="VMM.Initialize(svMedizin.Panel, Activity.Width, True, True, True)";
mostCurrent._vmm._initialize(mostCurrent.activityBA,mostCurrent._svmedizin.getPanel(),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 85;BA.debugLine="VML.Initialize(svAntropometrie.Panel, Activity.Width, True, True, True)";
mostCurrent._vml._initialize(mostCurrent.activityBA,mostCurrent._svantropometrie.getPanel(),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 88;BA.debugLine="VMA.Padding = 8dip";
mostCurrent._vma._padding = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8));
 //BA.debugLineNum = 89;BA.debugLine="VMN.Padding = 8dip";
mostCurrent._vmn._padding = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8));
 //BA.debugLineNum = 90;BA.debugLine="VMM.Padding = 8dip";
mostCurrent._vmm._padding = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8));
 //BA.debugLineNum = 91;BA.debugLine="VML.Padding = 8dip";
mostCurrent._vml._padding = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8));
 //BA.debugLineNum = 94;BA.debugLine="Dim List1,list2,List3, List4, List5, List6 As List";
_list1 = new anywheresoftware.b4a.objects.collections.List();
_list2 = new anywheresoftware.b4a.objects.collections.List();
_list3 = new anywheresoftware.b4a.objects.collections.List();
_list4 = new anywheresoftware.b4a.objects.collections.List();
_list5 = new anywheresoftware.b4a.objects.collections.List();
_list6 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 95;BA.debugLine="List1.Initialize";
_list1.Initialize();
 //BA.debugLineNum = 96;BA.debugLine="list2.Initialize";
_list2.Initialize();
 //BA.debugLineNum = 97;BA.debugLine="List3.Initialize";
_list3.Initialize();
 //BA.debugLineNum = 98;BA.debugLine="List4.Initialize";
_list4.Initialize();
 //BA.debugLineNum = 99;BA.debugLine="List5.Initialize";
_list5.Initialize();
 //BA.debugLineNum = 100;BA.debugLine="List6.Initialize";
_list6.Initialize();
 //BA.debugLineNum = 102;BA.debugLine="List1.AddAll(Array As String(\"Physisch:\",\"Emotional:\",\"Mental:\",\"Mondphase:\",\"Alter\",\"Telefonnummern\",\"Eltern\",\"Notrufnummer\",\"Privat\",\"Adresse\",\"Vorname\",\"2. Vorname\",\"Rufname\",\"Nachname\",\"Strasse\",\"Hausummer\",\"Stadt\",\"Bundesland\",\"Land\",\"Schule\",\"Rang-Gruppe\",\"Geworbene Mitglieder\",\"Patenschaften\",\"Lehrgänge\",\"Teamleiter\",\"Ideomot. Training\",\"Wettkämpfe\",\"Kampfrichterliz.\",\"Prüferlizens\",\"Gruppenmitglied\",\"Verein\",\"Verband\",\"Trainer\",\"Vorsitzender\",\"Telefonnummer\",\"Kampfsportart\"))";
_list1.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Physisch:","Emotional:","Mental:","Mondphase:","Alter","Telefonnummern","Eltern","Notrufnummer","Privat","Adresse","Vorname","2. Vorname","Rufname","Nachname","Strasse","Hausummer","Stadt","Bundesland","Land","Schule","Rang-Gruppe","Geworbene Mitglieder","Patenschaften","Lehrgänge","Teamleiter","Ideomot. Training","Wettkämpfe","Kampfrichterliz.","Prüferlizens","Gruppenmitglied","Verein","Verband","Trainer","Vorsitzender","Telefonnummer","Kampfsportart"}));
 //BA.debugLineNum = 103;BA.debugLine="list2.AddAll(Array As String(\"Körpergewicht\",\"Körperfettanteil\",\"Aufwach-Ruhepuls\",\"Bettruhe\",\"sehr leichte Tätigkeit\",\"leichte Tätigkeit\",\"mittelschwere Arbeit\",\"schwere Arbeit\",\"Training\",\"Nährwerte\",\"Gewichtszunahme in 30 Tage\",\"Kilo Kalorien\",\"Kilo Joule\",\"Eiweiss (g)\",\"Kohlenhydrate (g)\",\"Fett (g)\",\"Cholesterin\",\"EW/kg Körpergewicht\",\"Öl Zugabe (g)\",\"Eiweiss (%)\",\"Kohlenhydrate (%)\",\"Fett (%)\",\"Tierische Nahrung\",\"Pflanzlich Nahrung\",\"Tierische Eiweisse\",\"Pflanzliche Eiweisse\",\"Makronährstoffe (g)\",\"Nahrung Gewicht (g)\",\"Flüssigkeitsaufnahme\",\"Nahrungspreis\",\"Preis Körperfett:\",\"Fett reicht (Tage):\",\"Alkohol (g)\",\"Aluminium (mg)\",\"Amonium (mg)\",\"Antimon (mg)\",\"Arsen (mg)\",\"Ballaststoffe (g)\",\"Blei (mg)\",\"Bor (mg)\",\"Cadmium (mg)\",\"Carotin (mg)\",\"Chlorid (mg)\",\"Chrom (µg)\",\"Cyanid (mg)\",\"Eisen (mg)\",\"Ephidrin (mg)\",\"Fettsäuren\",\" mehrfach ungesättigte (g)\",\"Fluor (µg)\",\"Fructose (g)\",\"Glucose (g)\",\"Harnsäure (mg)\",\"Jod (µg)\",\"Kalium (mg)\",\"Kalzium (mg)\",\"Koffein (mg)\",\"Kohlenwasserstoffe (mg)\",\"Kupfer (mg)\",\"Lactose (mg)\",\"Lutein (mg)\",\"Magnesium (mg)\",\"Maltose (mg)\",\"Mangan (mg)\",\"Molybdän (µg)\",\"Natrium (mg)\",\"Nickel (mg)\",\"Nitrat (mg)\",\"Nitrit (mg)\",\"Omega-3-Fett (g)\",\"Omega-6-Fett (g)\",\"Phospholipide (g)\",\"Phosphor (mg)\",\"Quecksilber (mg)\",\"Saccharose (g)\",\"Selen (µg)\",\"Silizium (mg)\",\"Stickstoff (mg)\",\"Sulfat (mg)\",\"Vitamin A -Retinol (µg)\",\"Vitamin B 01-Thiamin (mg)\",\"Vitamin B 02-Riboflavin(mg)\",\"Vitamin B 03-Niazin (mg)\",\"Vitamin B 05-Pantothensäure (µg)\",\"Vitamin B 06-Pyridoxin (mg)\",\"Vitamin B 07-Biotin (µg)\",\"Vitamin B 09-Folsäure (µg)\",\"Vitamin B 12)Kobalamin (µg)\",\"Vitamin C-Ascorbinsäure (mg)\",\"Vitamin D Cholekalziferol (µg)\",\"Vitamin E Tokopherol (mg)\",\"Vitamin K Phyllochinon (µg)\",\"Zink (mg)\",\"a-Linolensäure\",\"Isoleucin\",\"Leucin\",\"Lysin\",\"Methionin\",\"Phenylalanin\",\"Threonin\",\"Tryptophan\",\"Valin\",\"Arginin\",\"Cystein\",\"Histidin\",\"Tyrosin\",\"Alanin\",\"Asparaginsäure\",\"Glutaminsäure\",\"Glycin\",\"Proli9n\",\"Serin\",\"Grundumsatz In kcal:\",\"Arbeitsumsatz In kcal:\",\"Maximalumsatz zum Muskelaufbau\",\"Minimalumsatz für den Waschbrettbauch\"))";
_list2.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Körpergewicht","Körperfettanteil","Aufwach-Ruhepuls","Bettruhe","sehr leichte Tätigkeit","leichte Tätigkeit","mittelschwere Arbeit","schwere Arbeit","Training","Nährwerte","Gewichtszunahme in 30 Tage","Kilo Kalorien","Kilo Joule","Eiweiss (g)","Kohlenhydrate (g)","Fett (g)","Cholesterin","EW/kg Körpergewicht","Öl Zugabe (g)","Eiweiss (%)","Kohlenhydrate (%)","Fett (%)","Tierische Nahrung","Pflanzlich Nahrung","Tierische Eiweisse","Pflanzliche Eiweisse","Makronährstoffe (g)","Nahrung Gewicht (g)","Flüssigkeitsaufnahme","Nahrungspreis","Preis Körperfett:","Fett reicht (Tage):","Alkohol (g)","Aluminium (mg)","Amonium (mg)","Antimon (mg)","Arsen (mg)","Ballaststoffe (g)","Blei (mg)","Bor (mg)","Cadmium (mg)","Carotin (mg)","Chlorid (mg)","Chrom (µg)","Cyanid (mg)","Eisen (mg)","Ephidrin (mg)","Fettsäuren"," mehrfach ungesättigte (g)","Fluor (µg)","Fructose (g)","Glucose (g)","Harnsäure (mg)","Jod (µg)","Kalium (mg)","Kalzium (mg)","Koffein (mg)","Kohlenwasserstoffe (mg)","Kupfer (mg)","Lactose (mg)","Lutein (mg)","Magnesium (mg)","Maltose (mg)","Mangan (mg)","Molybdän (µg)","Natrium (mg)","Nickel (mg)","Nitrat (mg)","Nitrit (mg)","Omega-3-Fett (g)","Omega-6-Fett (g)","Phospholipide (g)","Phosphor (mg)","Quecksilber (mg)","Saccharose (g)","Selen (µg)","Silizium (mg)","Stickstoff (mg)","Sulfat (mg)","Vitamin A -Retinol (µg)","Vitamin B 01-Thiamin (mg)","Vitamin B 02-Riboflavin(mg)","Vitamin B 03-Niazin (mg)","Vitamin B 05-Pantothensäure (µg)","Vitamin B 06-Pyridoxin (mg)","Vitamin B 07-Biotin (µg)","Vitamin B 09-Folsäure (µg)","Vitamin B 12)Kobalamin (µg)","Vitamin C-Ascorbinsäure (mg)","Vitamin D Cholekalziferol (µg)","Vitamin E Tokopherol (mg)","Vitamin K Phyllochinon (µg)","Zink (mg)","a-Linolensäure","Isoleucin","Leucin","Lysin","Methionin","Phenylalanin","Threonin","Tryptophan","Valin","Arginin","Cystein","Histidin","Tyrosin","Alanin","Asparaginsäure","Glutaminsäure","Glycin","Proli9n","Serin","Grundumsatz In kcal:","Arbeitsumsatz In kcal:","Maximalumsatz zum Muskelaufbau","Minimalumsatz für den Waschbrettbauch"}));
 //BA.debugLineNum = 104;BA.debugLine="List3.AddAll(Array As String(\"Krankheit\",\"Art\",\"Grad\",\"Ort\",\"Beginn\",\"Ende\",\"Arzt:\",\"Trainingsbeginn\",\"Wettkampfbeginn\",\"Verletzung\",\"Art\",\"Körperteil\",\"Grad\",\"Beginn\",\"Ende\",\"Arztname\",\"Trainingsbeginn\",\"Wettkampfbeginn\",\"Therapie\",\"Erneute Wettkampfteilname\",\"Gesundheitskontrolle\",\"Erneute Trainingsaufnahme\",\"Antidoping Test\",\"Schmerztagebuch\",\"Organ\",\"Folgen\",\"Seite\",\"Körperteil\",\"Auswirkungen\",\"Seite\",\"Schmerzart\",\"Zeit\",\"Verursacher\",\"Bemerkungen\",\"Blutbild\",\"Labor\",\"Arzt\",\"Arzt Adresse\"))";
_list3.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Krankheit","Art","Grad","Ort","Beginn","Ende","Arzt:","Trainingsbeginn","Wettkampfbeginn","Verletzung","Art","Körperteil","Grad","Beginn","Ende","Arztname","Trainingsbeginn","Wettkampfbeginn","Therapie","Erneute Wettkampfteilname","Gesundheitskontrolle","Erneute Trainingsaufnahme","Antidoping Test","Schmerztagebuch","Organ","Folgen","Seite","Körperteil","Auswirkungen","Seite","Schmerzart","Zeit","Verursacher","Bemerkungen","Blutbild","Labor","Arzt","Arzt Adresse"}));
 //BA.debugLineNum = 105;BA.debugLine="List6.AddAll(Array As String(\"Leukozyten In Tsd/µl\",\"Erythrozyten In Mio/µl\",\"Hämoglobin In g/dl\",\"Hämatokrit In %\",\"MCV (mittl. Ery\",\"Vol In fl\",\"MCH (HbE In pg/Ery)\",\"MCHC (mittl.Hb-Konz In ´-g/dl)\",\"Thrombozyten In Tsd/µl\",\"Neutrophile (CD In ´-%)\",\"Lymphozyten (CD In ´+%)\",\"Bas.Gran. (CD In %)\",\"Eos.Gran. (CD In %)\",\"Monozyten (CD In %)\",\"Gamma-GT 37°C In U/l)\",\"GPT 37°C In ´+U/l\",\"Alk.Phosphatase 37°C In U/l\",\"Serumglucose In mg/dl\",\"Kreatinin In mg/dl\",\"Harnsäure In ´+mg/dl\",\"Kalium In ´+mmol/l\",\"Serumeisen In ug/dl\",\"Cholesterin gesamt In ´+mg/dl\",\"Triglyceride In mg/dl\",\"FreiesTrijodthyronin i.S. In pg/ml\",\"FreiesThyroxin i.S. (CLIA In pmol/l)\",\"Basal i.S.\",\"Basal i.S. (CLIA In µIU/ml)                \",\"Basal i.S. (CLIA In µIU/ml) \",\"Quick In %\",\"INR-Wert In \",\"PTT In s\",\"Natrium In mmol/l\",\"Cholinesterase 37°C In kU/l\",\"Blutzucker im Serum In mg/dl\",\"HDL-geb Colesterin In mg/dl\",\"Blutdruck Systolisch In mmHg\",\"Blutdruck Diastolisch In mmHg\",\"Magnesium In mmol/l\",\"CRP quantitativ In mg/l\",\"RF-IgM quantitativ In IU/mi\",\"Calzium In mmol/l\",\"Cholesterin/HDL-Cholest. In\" ))";
_list6.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Leukozyten In Tsd/µl","Erythrozyten In Mio/µl","Hämoglobin In g/dl","Hämatokrit In %","MCV (mittl. Ery","Vol In fl","MCH (HbE In pg/Ery)","MCHC (mittl.Hb-Konz In ´-g/dl)","Thrombozyten In Tsd/µl","Neutrophile (CD In ´-%)","Lymphozyten (CD In ´+%)","Bas.Gran. (CD In %)","Eos.Gran. (CD In %)","Monozyten (CD In %)","Gamma-GT 37°C In U/l)","GPT 37°C In ´+U/l","Alk.Phosphatase 37°C In U/l","Serumglucose In mg/dl","Kreatinin In mg/dl","Harnsäure In ´+mg/dl","Kalium In ´+mmol/l","Serumeisen In ug/dl","Cholesterin gesamt In ´+mg/dl","Triglyceride In mg/dl","FreiesTrijodthyronin i.S. In pg/ml","FreiesThyroxin i.S. (CLIA In pmol/l)","Basal i.S.","Basal i.S. (CLIA In µIU/ml)                ","Basal i.S. (CLIA In µIU/ml) ","Quick In %","INR-Wert In ","PTT In s","Natrium In mmol/l","Cholinesterase 37°C In kU/l","Blutzucker im Serum In mg/dl","HDL-geb Colesterin In mg/dl","Blutdruck Systolisch In mmHg","Blutdruck Diastolisch In mmHg","Magnesium In mmol/l","CRP quantitativ In mg/l","RF-IgM quantitativ In IU/mi","Calzium In mmol/l","Cholesterin/HDL-Cholest. In"}));
 //BA.debugLineNum = 106;BA.debugLine="List4.AddAll(Array As String(\"Körpergröße\",\"Unterarm l.\",\"Unterarm r.\",\"Oberarm l.\",\"Oberarm r.\",\"Hals (unter Kehle\",\"Schulterumfang\",\"Brustumfang \",\"Taille (unter Rippen)\",\"Bauch (Nabelhöhe)\",\"Hüfte \",\"Oberschenkel l.\",\"Oberschenkel r.\",\"Wade l.\",\"Wade r.\",\"Brustfalte\",\"Schulterfalte\",\"Achselfalte\",\"Trizepsfalte\",\"Bauchfalte\",\"Hüftfalte\",\"Oberschenkelfalte\",\"Summe der 7 Falten\",\"Fettgehalt lt. 7 Falten\",\"Gewicht ohne Fett\",\"Knochenmasse\",\"Wasseranteil\",\"Organmasse\",\"Muskelmasse\",\"Körperfett In kg laut Waage\",\"US Navy Fettrechner %\",\"Fettgehalt Durchschnitt All:\",\"Handgelenk r.\",\"Taille /Hüfte Quotient.\",\"Waist-To-Hip-Ratio (WHR)\",\"Bankdrücken Max:\",\"Links-rechts-Symmetrie\",\"Links-rechts-Symmetrie\",\"Oben-Unten-Symmetrie\",\"Oben-Unten-Symmetrie\",\"\",\"Sehtest\",\"Hörtest\"))";
_list4.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Körpergröße","Unterarm l.","Unterarm r.","Oberarm l.","Oberarm r.","Hals (unter Kehle","Schulterumfang","Brustumfang ","Taille (unter Rippen)","Bauch (Nabelhöhe)","Hüfte ","Oberschenkel l.","Oberschenkel r.","Wade l.","Wade r.","Brustfalte","Schulterfalte","Achselfalte","Trizepsfalte","Bauchfalte","Hüftfalte","Oberschenkelfalte","Summe der 7 Falten","Fettgehalt lt. 7 Falten","Gewicht ohne Fett","Knochenmasse","Wasseranteil","Organmasse","Muskelmasse","Körperfett In kg laut Waage","US Navy Fettrechner %","Fettgehalt Durchschnitt All:","Handgelenk r.","Taille /Hüfte Quotient.","Waist-To-Hip-Ratio (WHR)","Bankdrücken Max:","Links-rechts-Symmetrie","Links-rechts-Symmetrie","Oben-Unten-Symmetrie","Oben-Unten-Symmetrie","","Sehtest","Hörtest"}));
 //BA.debugLineNum = 107;BA.debugLine="List5.AddAll(Array As String(\"1.89\",\"36.564\",\"36.564\",\"44.022\",\"44.022\",\"46.86\",\"146.322\",\"122.364\",\"93.72\",\"91.608\",\"110.022\",\"66\",\"66\",\"44.022\",\"44.022\",\"10\",\"10\",\"10\",\"10\",\"10\",\"10\",\"10\",\"70\",\"12\",\"9058785241343\",\"19.074\",\"0.851829634073185\",\"normales Herzinfarkt- risiko\",\"77\",\"1\",\"Super\",\" Ideal\",\"17\",\"Super\",\" Ideal\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"))";
_list5.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"1.89","36.564","36.564","44.022","44.022","46.86","146.322","122.364","93.72","91.608","110.022","66","66","44.022","44.022","10","10","10","10","10","10","10","70","12","9058785241343","19.074","0.851829634073185","normales Herzinfarkt- risiko","77","1","Super"," Ideal","17","Super"," Ideal","","","","","","","",""}));
 //BA.debugLineNum = 109;BA.debugLine="For i = 0 To List1.Size - 1";
{
final int step60 = 1;
final int limit60 = (int) (_list1.getSize()-1);
for (_i = (int) (0); (step60 > 0 && _i <= limit60) || (step60 < 0 && _i >= limit60); _i = ((int)(0 + _i + step60))) {
 //BA.debugLineNum = 113;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, List1.Get(i), 18, Colors.Black, List1.Get(i))";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),BA.ObjectToString(_list1.Get(_i)),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,BA.ObjectToString(_list1.Get(_i)));
 //BA.debugLineNum = 114;BA.debugLine="VMA.AddTextBox(40%x, -1, -90, 9%y, \"ZU\", \"A\"&i, 18, \"SOLL\", False, VMA.DataType_Uppercase_Words, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 15, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),(int) (-1),(int) (-90),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA),"ZU","A"+BA.NumberToString(_i),(float) (18),"SOLL",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_uppercase_words(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (15),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 116;BA.debugLine="VMA.Enabled(\"Zu\", False)";
mostCurrent._vma._enabled("Zu",anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 148;BA.debugLine="For i = 0 To list2.Size - 1";
{
final int step65 = 1;
final int limit65 = (int) (_list2.getSize()-1);
for (_i = (int) (0); (step65 > 0 && _i <= limit65) || (step65 < 0 && _i >= limit65); _i = ((int)(0 + _i + step65))) {
 //BA.debugLineNum = 150;BA.debugLine="VMN.AddLabel(VMN.Padding, -2, 1, 1, list2.Get(i), 18, Colors.Black, list2.Get(i))";
mostCurrent._vmn._addlabel(mostCurrent._vmn._padding,(int) (-2),(int) (1),(int) (1),BA.ObjectToString(_list2.Get(_i)),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,BA.ObjectToString(_list2.Get(_i)));
 //BA.debugLineNum = 151;BA.debugLine="VMN.AddTextBox(60%x, -1, -40, 9%y, \"ZU\", \"\", 18, i, False, VMN.DataType_Uppercase_Words, \"\", VMN.ActionBtn_Next, \"\", Me, 0, 5, Null)";
mostCurrent._vmn._addtextbox(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),(int) (-1),(int) (-40),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA),"ZU","",(float) (18),BA.NumberToString(_i),anywheresoftware.b4a.keywords.Common.False,mostCurrent._vmn._datatype_uppercase_words(),"",mostCurrent._vmn._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (5),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 152;BA.debugLine="VMN.AddTextBox(-2, -1, -1, 9%y, \"IST\", \"\", 18, i, False, VMN.DataType_Num, \"\", VMN.ActionBtn_Next, \"\", Me, 0, 5, Null)";
mostCurrent._vmn._addtextbox((int) (-2),(int) (-1),(int) (-1),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA),"IST","",(float) (18),BA.NumberToString(_i),anywheresoftware.b4a.keywords.Common.False,mostCurrent._vmn._datatype_num(),"",mostCurrent._vmn._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (5),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 153;BA.debugLine="VMN.Enabled(\"Zu\" & 1, False)";
mostCurrent._vmn._enabled("Zu"+BA.NumberToString(1),anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 168;BA.debugLine="For i = 0 To List3.Size - 1";
{
final int step71 = 1;
final int limit71 = (int) (_list3.getSize()-1);
for (_i = (int) (0); (step71 > 0 && _i <= limit71) || (step71 < 0 && _i >= limit71); _i = ((int)(0 + _i + step71))) {
 //BA.debugLineNum = 170;BA.debugLine="VMM.AddLabel(VMM.Padding, -2, 1, 1, List3.Get(i), 18, Colors.Black, List3.Get(i))";
mostCurrent._vmm._addlabel(mostCurrent._vmm._padding,(int) (-2),(int) (1),(int) (1),BA.ObjectToString(_list3.Get(_i)),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,BA.ObjectToString(_list3.Get(_i)));
 //BA.debugLineNum = 171;BA.debugLine="VMM.AddTextBox(60%x, -1, -60, 9%y, \"ZU\", \"\", 18, \"Soll\"& i, True, VMM.DataType_Uppercase_Words, \"\", VMM.ActionBtn_Next, \"\", Me, 0, 100, Null)";
mostCurrent._vmm._addtextbox(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),(int) (-1),(int) (-60),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA),"ZU","",(float) (18),"Soll"+BA.NumberToString(_i),anywheresoftware.b4a.keywords.Common.True,mostCurrent._vmm._datatype_uppercase_words(),"",mostCurrent._vmm._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (100),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 173;BA.debugLine="VMM.Enabled(\"Zu\" & 1, False)";
mostCurrent._vmm._enabled("Zu"+BA.NumberToString(1),anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 185;BA.debugLine="For i = 0 To List6.Size - 1";
{
final int step76 = 1;
final int limit76 = (int) (_list6.getSize()-1);
for (_i = (int) (0); (step76 > 0 && _i <= limit76) || (step76 < 0 && _i >= limit76); _i = ((int)(0 + _i + step76))) {
 //BA.debugLineNum = 187;BA.debugLine="VMM.AddLabel(VMM.Padding, -2, 1, 1, List6.Get(i), 18, Colors.Black, List6.Get(i))";
mostCurrent._vmm._addlabel(mostCurrent._vmm._padding,(int) (-2),(int) (1),(int) (1),BA.ObjectToString(_list6.Get(_i)),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,BA.ObjectToString(_list6.Get(_i)));
 //BA.debugLineNum = 188;BA.debugLine="VMM.AddTextBox(60%x, -1, -40, 1, \"ZU\", \"\", 18, i , True, VMM.DataType_Num, \"\", VMM.ActionBtn_Next, \"\", Me, 0, 100, Null)";
mostCurrent._vmm._addtextbox(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),(int) (-1),(int) (-40),(int) (1),"ZU","",(float) (18),BA.NumberToString(_i),anywheresoftware.b4a.keywords.Common.True,mostCurrent._vmm._datatype_num(),"",mostCurrent._vmm._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (100),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 189;BA.debugLine="VMM.AddTextBox(-2, -1, -1, 9%y, \"IST\", \"\", 18, i, False, VMM.DataType_Num, \"\", VMM.ActionBtn_Next, \"\", Me, 0, 5, Null)";
mostCurrent._vmm._addtextbox((int) (-2),(int) (-1),(int) (-1),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA),"IST","",(float) (18),BA.NumberToString(_i),anywheresoftware.b4a.keywords.Common.False,mostCurrent._vmm._datatype_num(),"",mostCurrent._vmm._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (5),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 190;BA.debugLine="VMM.Enabled(\"Zu\" & 1, False)";
mostCurrent._vmm._enabled("Zu"+BA.NumberToString(1),anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 204;BA.debugLine="For i = 0 To List4.Size - 1";
{
final int step82 = 1;
final int limit82 = (int) (_list4.getSize()-1);
for (_i = (int) (0); (step82 > 0 && _i <= limit82) || (step82 < 0 && _i >= limit82); _i = ((int)(0 + _i + step82))) {
 //BA.debugLineNum = 210;BA.debugLine="VML.AddLabel(VML.Padding, -2, 1, 1, List4.Get(i), 18, Colors.Black, List4.Get(i))";
mostCurrent._vml._addlabel(mostCurrent._vml._padding,(int) (-2),(int) (1),(int) (1),BA.ObjectToString(_list4.Get(_i)),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,BA.ObjectToString(_list4.Get(_i)));
 //BA.debugLineNum = 211;BA.debugLine="VML.AddTextBox(60%x, -1, -40, 5%y, List5.Get(i), \"\", 18, List5.Get(i), False, VML.DataType_Uppercase_Words, \"\", VML.ActionBtn_Next, \"\", Me, 0, 15, Null)";
mostCurrent._vml._addtextbox(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),(int) (-1),(int) (-40),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA),BA.ObjectToString(_list5.Get(_i)),"",(float) (18),BA.ObjectToString(_list5.Get(_i)),anywheresoftware.b4a.keywords.Common.False,mostCurrent._vml._datatype_uppercase_words(),"",mostCurrent._vml._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (15),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 212;BA.debugLine="VML.AddTextBox(-2, -1, -1, 5%y, \"IST\", \"\", 18, i, False, VML.DataType_Num, \"\", VML.ActionBtn_Next, \"\", Me, 0, 5, Null)";
mostCurrent._vml._addtextbox((int) (-2),(int) (-1),(int) (-1),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA),"IST","",(float) (18),BA.NumberToString(_i),anywheresoftware.b4a.keywords.Common.False,mostCurrent._vml._datatype_num(),"",mostCurrent._vml._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (5),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 213;BA.debugLine="VML.Enabled(\"Zu\" & 1, False)";
mostCurrent._vml._enabled("Zu"+BA.NumberToString(1),anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 230;BA.debugLine="VMA.Enabled(\"Kasten\" & 1, False)";
mostCurrent._vma._enabled("Kasten"+BA.NumberToString(1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 232;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Gewicht nach 30:\", 18, Colors.Black, \"Gewich\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Gewicht nach 30:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Gewich");
 //BA.debugLineNum = 233;BA.debugLine="VMA.AddTextBox(60%x, -1, -50, 1, \"Kaste1\", \"\", 18, \"Aptr\", False, VMA.DataType_Uppercase_Words, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 15, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),(int) (-1),(int) (-50),(int) (1),"Kaste1","",(float) (18),"Aptr",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_uppercase_words(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (15),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 234;BA.debugLine="VMA.AddTextBox(-2, -1, -1, 1, \"Zip\", \"\", 18, \"Zipr\", False, VMA.DataType_Num, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 5, Null)";
mostCurrent._vma._addtextbox((int) (-2),(int) (-1),(int) (-1),(int) (1),"Zip","",(float) (18),"Zipr",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_num(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (5),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 235;BA.debugLine="VMA.Enabled(\"Kaste1\" & 1, False)";
mostCurrent._vma._enabled("Kaste1"+BA.NumberToString(1),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 237;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Gewicht nach 0T:\", 18, Colors.Black, \"Gewicht\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Gewicht nach 0T:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Gewicht");
 //BA.debugLineNum = 238;BA.debugLine="VMA.AddTextBox(60%x, -1, -50, 1, \"Kasten2\", \"\", 18, \"Apt\", False, VMA.DataType_Uppercase_Words, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 15, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),(int) (-1),(int) (-50),(int) (1),"Kasten2","",(float) (18),"Apt",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_uppercase_words(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (15),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 239;BA.debugLine="VMA.AddTextBox(-2, -1, -1, 1, \"Zip\", \"\", 18, \"Zip\", False, VMA.DataType_Num, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 5, Null)";
mostCurrent._vma._addtextbox((int) (-2),(int) (-1),(int) (-1),(int) (1),"Zip","",(float) (18),"Zip",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_num(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (5),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 240;BA.debugLine="VMA.Enabled(\"Kasten\" & 2, False)";
mostCurrent._vma._enabled("Kasten"+BA.NumberToString(2),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 242;BA.debugLine="VMA.AddLabel(VMA.Padding, -1, 1, 1, \"First Name:\", 18, Colors.Black, \"FirstName\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-1),(int) (1),(int) (1),"First Name:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"FirstName");
 //BA.debugLineNum = 243;BA.debugLine="VMA.AddTextBox(140dip, -1, -1, 1, \"FirstName\", \"\", 18, \"First Name\", False, VMA.DataType_Text, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 30, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"FirstName","",(float) (18),"First Name",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_text(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (30),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 244;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Middle Name:\", 18, Colors.Black, \"MiddleName\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Middle Name:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"MiddleName");
 //BA.debugLineNum = 245;BA.debugLine="VMA.AddTextBox(140dip, -1, -1, 1, \"MiddleName\", \"\", 18, \"Middle Name\", False, VMA.DataType_Text, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 30, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"MiddleName","",(float) (18),"Middle Name",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_text(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (30),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 246;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Last Name:\", 18, Colors.Black, \"LastName\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Last Name:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"LastName");
 //BA.debugLineNum = 247;BA.debugLine="VMA.AddTextBox(140dip, -1, -1, 1, \"LastName\", \"\", 18, \"Last Name\", False, VMA.DataType_Text, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 30, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"LastName","",(float) (18),"Last Name",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_text(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (30),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 248;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Alias:\", 18, Colors.Black, \"Alias\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Alias:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Alias");
 //BA.debugLineNum = 249;BA.debugLine="VMA.AddTextBox(140dip, -1, -1, 1, \"Alias\", \"\", 18, \"Alias\", False, VMA.DataType_Text, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 30, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"Alias","",(float) (18),"Alias",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_text(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (30),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 251;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"DOB:\", 18, Colors.Black, \"DOB\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"DOB:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"DOB");
 //BA.debugLineNum = 252;BA.debugLine="VMA.AddTextBox(140dip, -1, -50, 1, \"DOB\", DateText, 18, \"\", False, 0, \"\",0, \"\", Me, 0, 10, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-50),(int) (1),"DOB",_datetext,(float) (18),"",anywheresoftware.b4a.keywords.Common.False,(int) (0),"",(int) (0),"",tagebuch.getObject(),(int) (0),(int) (10),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 253;BA.debugLine="VMA.Enabled(\"DOB\", False)";
mostCurrent._vma._enabled("DOB",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 254;BA.debugLine="VMA.AddButton(-2, -1, -1, 1, \"DateBtn\", \"Set Date\", 16, Colors.Black, \"Get_Date\", Me)";
mostCurrent._vma._addbutton((int) (-2),(int) (-1),(int) (-1),(int) (1),"DateBtn","Set Date",(float) (16),anywheresoftware.b4a.keywords.Common.Colors.Black,"Get_Date",tagebuch.getObject());
 //BA.debugLineNum = 256;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Hair Color:\", 18, Colors.Black, \"Hair\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Hair Color:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Hair");
 //BA.debugLineNum = 257;BA.debugLine="VMA.AddComboBox(140dip, -1, -1, 1, \"Hair\", \"Select Hair Color\", File.ReadList(File.DirAssets, \"partyhair.txt\"), 18, Colors.Black, \"\", Me, 20)";
mostCurrent._vma._addcombobox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"Hair","Select Hair Color",anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"partyhair.txt"),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"",tagebuch.getObject(),(int) (20));
 //BA.debugLineNum = 258;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Eye Color:\", 18, Colors.Black, \"Eyes\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Eye Color:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Eyes");
 //BA.debugLineNum = 259;BA.debugLine="VMA.AddComboBox(140dip, -1, -1, 1, \"Eyes\", \"Select Eye Color\", File.ReadList(File.DirAssets, \"partyeyes.txt\"), 18, Colors.Black, \"\", Me, 10)";
mostCurrent._vma._addcombobox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"Eyes","Select Eye Color",anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"partyeyes.txt"),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"",tagebuch.getObject(),(int) (10));
 //BA.debugLineNum = 260;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Race:\", 18, Colors.Black, \"Race\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Race:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Race");
 //BA.debugLineNum = 261;BA.debugLine="VMA.AddComboBox(140dip, -1, -1, 1, \"Race\", \"Select Race\", File.ReadList(File.DirAssets, \"partyrace.txt\"), 18, Colors.Black, \"\", Me, 20)";
mostCurrent._vma._addcombobox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"Race","Select Race",anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"partyrace.txt"),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"",tagebuch.getObject(),(int) (20));
 //BA.debugLineNum = 262;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Sex:\", 18, Colors.Black, \"Sex\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Sex:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Sex");
 //BA.debugLineNum = 263;BA.debugLine="VMA.AddComboBox(140dip, -1, -1, 1, \"Sex\", \"Select Sex\", Array As String(\"M\", \"F\", \"X\"), 18, Colors.Black, \"\", Me, 1)";
mostCurrent._vma._addcombobox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"Sex","Select Sex",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"M","F","X"}),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"",tagebuch.getObject(),(int) (1));
 //BA.debugLineNum = 265;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Height (Ft):\", 18, Colors.Black, \"HeightFt\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Height (Ft):",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"HeightFt");
 //BA.debugLineNum = 266;BA.debugLine="VMA.AddTextBox(140dip, -1, -1, 1, \"HeightFt\", \"\", 18, \"\", False, VMA.DataType_Num, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 2, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"HeightFt","",(float) (18),"",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_num(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (2),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 267;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Height (In):\", 18, Colors.Black, \"HeightIn\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Height (In):",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"HeightIn");
 //BA.debugLineNum = 268;BA.debugLine="VMA.AddTextBox(140dip, -1, -1, 1, \"HeightIn\", \"\", 18, \"\", False, VMA.DataType_Num, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 2, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"HeightIn","",(float) (18),"",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_num(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (2),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 269;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Weight:\", 18, Colors.Black, \"Weight\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Weight:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Weight");
 //BA.debugLineNum = 270;BA.debugLine="VMA.AddTextBox(140dip, -1, -1, 1, \"Weight\", \"\", 18, \"\", False, VMA.DataType_Num, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 4, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (140)),(int) (-1),(int) (-1),(int) (1),"Weight","",(float) (18),"",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_num(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (4),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 272;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Street:\", 18, Colors.Black, \"Street\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Street:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Street");
 //BA.debugLineNum = 273;BA.debugLine="VMA.AddTextBox(77dip, -1, -1, 1, \"Street\", \"\", 18, \"Street\", False, VMA.DataType_Uppercase_Words, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 50, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (77)),(int) (-1),(int) (-1),(int) (1),"Street","",(float) (18),"Street",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_uppercase_words(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (50),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 274;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Apt:\", 18, Colors.Black, \"Apt\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Apt:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Apt");
 //BA.debugLineNum = 275;BA.debugLine="VMA.AddTextBox(77dip, -1, -50, 1, \"Apt\", \"\", 18, \"Apt\", False, VMA.DataType_Uppercase_Words, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 15, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (77)),(int) (-1),(int) (-50),(int) (1),"Apt","",(float) (18),"Apt",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_uppercase_words(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (15),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 276;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"City:\", 18, Colors.Black, \"City\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"City:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"City");
 //BA.debugLineNum = 277;BA.debugLine="VMA.AddTextBox(77dip, -1, -1, 1, \"City\", \"\", 18, \"City\", False, VMA.DataType_Uppercase_Words, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 30, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (77)),(int) (-1),(int) (-1),(int) (1),"City","",(float) (18),"City",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_uppercase_words(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (30),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 278;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"State:\", 18, Colors.Black, \"State\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"State:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"State");
 //BA.debugLineNum = 279;BA.debugLine="VMA.AddComboBox(77dip, -1, -33, 1, \"State\", \"Select a State\", File.ReadList(File.DirAssets, \"states.txt\"), 18, Colors.Black, \"\", Me, 2)";
mostCurrent._vma._addcombobox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (77)),(int) (-1),(int) (-33),(int) (1),"State","Select a State",anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"states.txt"),(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"",tagebuch.getObject(),(int) (2));
 //BA.debugLineNum = 280;BA.debugLine="VMA.AddLabel(-2, -1, 1, 1, \"Zip:\", 18, Colors.Black, \"Zip\")";
mostCurrent._vma._addlabel((int) (-2),(int) (-1),(int) (1),(int) (1),"Zip:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Zip");
 //BA.debugLineNum = 281;BA.debugLine="VMA.AddTextBox(-2, -1, -1, 1, \"Zip\", \"\", 18, \"Zip\", False, VMA.DataType_Num, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 5, Null)";
mostCurrent._vma._addtextbox((int) (-2),(int) (-1),(int) (-1),(int) (1),"Zip","",(float) (18),"Zip",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_num(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (5),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 282;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Phone:\", 18, Colors.Black, \"Phone\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Phone:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Phone");
 //BA.debugLineNum = 283;BA.debugLine="VMA.AddTextBox(77dip, -1, -1, 1, \"Phone\", \"\", 18, \"Phone\", False, VMA.DataType_Num, \"\", VMA.ActionBtn_Next, \"\", Me, 0, 30, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (77)),(int) (-1),(int) (-1),(int) (1),"Phone","",(float) (18),"Phone",anywheresoftware.b4a.keywords.Common.False,mostCurrent._vma._datatype_num(),"",mostCurrent._vma._actionbtn_next(),"",tagebuch.getObject(),(int) (0),(int) (30),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 285;BA.debugLine="VMA.AddLabel(VMA.Padding, -2, 1, 1, \"Notes:\", 18, Colors.Black, \"Notes\")";
mostCurrent._vma._addlabel(mostCurrent._vma._padding,(int) (-2),(int) (1),(int) (1),"Notes:",(float) (18),anywheresoftware.b4a.keywords.Common.Colors.Black,"Notes");
 //BA.debugLineNum = 286;BA.debugLine="VMA.AddTextBox(77dip, -1, -1, 25%y, \"Notes\", \"\", 18, \"Notes\", True, VMA.DataType_Text, \"\", VMA.ActionBtn_Done, \"\", Me, 0, 500, Null)";
mostCurrent._vma._addtextbox(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (77)),(int) (-1),(int) (-1),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA),"Notes","",(float) (18),"Notes",anywheresoftware.b4a.keywords.Common.True,mostCurrent._vma._datatype_text(),"",mostCurrent._vma._actionbtn_done(),"",tagebuch.getObject(),(int) (0),(int) (500),(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 288;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 346;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 347;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 348;BA.debugLine="curValues.Clear";
_curvalues.Clear();
 }else {
 //BA.debugLineNum = 350;BA.debugLine="curValues = VMA.SaveState";
_curvalues = mostCurrent._vma._savestate();
 };
 //BA.debugLineNum = 352;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 333;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 335;BA.debugLine="svAllgemein.Height = Activity.Height";
mostCurrent._svallgemein.setHeight(mostCurrent._activity.getHeight());
 //BA.debugLineNum = 343;BA.debugLine="If curValues.IsInitialized Then VMA.RestoreState(curValues)";
if (_curvalues.IsInitialized()) { 
mostCurrent._vma._restorestate(_curvalues);};
 //BA.debugLineNum = 344;BA.debugLine="End Sub";
return "";
}
public static String  _get_date() throws Exception{
 //BA.debugLineNum = 369;BA.debugLine="Sub Get_Date";
 //BA.debugLineNum = 370;BA.debugLine="If DOB = 9223372036854775807 Then dd.DateTicks= DateTime.Now Else dd.DateTicks = DOB";
if (_dob==9223372036854775807L) { 
mostCurrent._dd.setDateTicks(anywheresoftware.b4a.keywords.Common.DateTime.getNow());}
else {
mostCurrent._dd.setDateTicks(_dob);};
 //BA.debugLineNum = 371;BA.debugLine="If dd.Show(\"Please Select Incident Date\", \"Incident Date\", \"Ok\", \"Cancel\", \"\", Null) = DialogResponse.POSITIVE Then";
if (mostCurrent._dd.Show("Please Select Incident Date","Incident Date","Ok","Cancel","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null))==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 372;BA.debugLine="DOB = DateTime.DateParse(NumberFormat(dd.Month, 2, 0) & \"/\" & NumberFormat(dd.DayOfMonth, 2, 0) & \"/\" & NumberFormat2(dd.Year, 4, 0, 0, False))";
_dob = anywheresoftware.b4a.keywords.Common.DateTime.DateParse(anywheresoftware.b4a.keywords.Common.NumberFormat(mostCurrent._dd.getMonth(),(int) (2),(int) (0))+"/"+anywheresoftware.b4a.keywords.Common.NumberFormat(mostCurrent._dd.getDayOfMonth(),(int) (2),(int) (0))+"/"+anywheresoftware.b4a.keywords.Common.NumberFormat2(mostCurrent._dd.getYear(),(int) (4),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 373;BA.debugLine="VMA.SetText(\"DOB\", DateTime.Date(DOB))";
mostCurrent._vma._settext("DOB",(Object)(anywheresoftware.b4a.keywords.Common.DateTime.Date(_dob)));
 }else {
 //BA.debugLineNum = 375;BA.debugLine="If Msgbox2(\"Do you wish to set the Date to N/A?\", \"Clear Date\", \"Yes\", \"\", \"No\", Null) = DialogResponse.POSITIVE Then";
if (anywheresoftware.b4a.keywords.Common.Msgbox2("Do you wish to set the Date to N/A?","Clear Date","Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 376;BA.debugLine="DOB= 9223372036854775807";
_dob = (long) (9223372036854775807L);
 //BA.debugLineNum = 377;BA.debugLine="VMA.SetText(\"DOB\", \"N/A\")";
mostCurrent._vma._settext("DOB",(Object)("N/A"));
 };
 };
 //BA.debugLineNum = 380;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Dim IME As IME";
mostCurrent._ime = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 25;BA.debugLine="Dim svAllgemein, svNahrung, svMedizin, svAntropometrie As ScrollView";
mostCurrent._svallgemein = new anywheresoftware.b4a.objects.ScrollViewWrapper();
mostCurrent._svnahrung = new anywheresoftware.b4a.objects.ScrollViewWrapper();
mostCurrent._svmedizin = new anywheresoftware.b4a.objects.ScrollViewWrapper();
mostCurrent._svantropometrie = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim VMA,VMN,VMM, VML As ViewMgr";
mostCurrent._vma = new de.watchkido.mama.viewmgr();
mostCurrent._vmn = new de.watchkido.mama.viewmgr();
mostCurrent._vmm = new de.watchkido.mama.viewmgr();
mostCurrent._vml = new de.watchkido.mama.viewmgr();
 //BA.debugLineNum = 27;BA.debugLine="Dim dd As DateDialog";
mostCurrent._dd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog();
 //BA.debugLineNum = 28;BA.debugLine="Dim Tabhost1 As TabHost";
mostCurrent._tabhost1 = new anywheresoftware.b4a.objects.TabHostWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _ime_heightchanged(int _newheight,int _oldheight) throws Exception{
 //BA.debugLineNum = 354;BA.debugLine="Sub IME_HeightChanged (NewHeight As Int, OldHeight As Int)";
 //BA.debugLineNum = 355;BA.debugLine="svAllgemein.Height = NewHeight";
mostCurrent._svallgemein.setHeight(_newheight);
 //BA.debugLineNum = 356;BA.debugLine="svNahrung.Height = NewHeight";
mostCurrent._svnahrung.setHeight(_newheight);
 //BA.debugLineNum = 357;BA.debugLine="svMedizin.Height = NewHeight";
mostCurrent._svmedizin.setHeight(_newheight);
 //BA.debugLineNum = 358;BA.debugLine="svAntropometrie.Height = NewHeight";
mostCurrent._svantropometrie.setHeight(_newheight);
 //BA.debugLineNum = 367;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 292;BA.debugLine="Sub ListView1_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 293;BA.debugLine="Activity.Title = Value";
mostCurrent._activity.setTitle(_value);
 //BA.debugLineNum = 330;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim curValues As Map";
_curvalues = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 18;BA.debugLine="Dim DOB As Long";
_dob = 0L;
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
}
