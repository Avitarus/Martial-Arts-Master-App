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

public class checkliste extends Activity implements B4AActivity{
	public static checkliste mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.checkliste");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (checkliste).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.checkliste");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.checkliste", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (checkliste) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (checkliste) Resume **");
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
		return checkliste.class;
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
        BA.LogInfo("** Activity (checkliste) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (checkliste) Resume **");
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
public static String _dirdata = "";
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap1 = null;
public static int _fragenanzahl = 0;
public static int _panelheight = 0;
public anywheresoftware.b4a.objects.PanelWrapper _panels = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnspeichern = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btntop = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvhintergrund = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblüberschrift = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _sbstärke = null;
public anywheresoftware.b4a.objects.collections.List _fragen = null;
public static int _s = 0;
public static int _b = 0;
public static int _e = 0;
public anywheresoftware.b4a.objects.collections.List _werteliste = null;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
public de.watchkido.mama.startabfragen _startabfragen = null;
public de.watchkido.mama.einstellungenapp _einstellungenapp = null;
public de.watchkido.mama.erfolgsmeldung _erfolgsmeldung = null;
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
int _i = 0;
 //BA.debugLineNum = 27;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 28;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 29;BA.debugLine="If File.ExternalWritable = False Then";
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 30;BA.debugLine="Msgbox(\"Ich kann nicht auf die SD-Karte schreiben.\", \"\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Ich kann nicht auf die SD-Karte schreiben.","",mostCurrent.activityBA);
 //BA.debugLineNum = 31;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 34;BA.debugLine="Fragen = File.ReadList(File.DirAssets, \"Einhornapp.txt\") 'ändern durch vorgabe";
mostCurrent._fragen = anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Einhornapp.txt");
 //BA.debugLineNum = 36;BA.debugLine="If File.Exists(DirData, \"Werteliste.txt\") = False Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_dirdata,"Werteliste.txt")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 38;BA.debugLine="Werteliste.Initialize";
mostCurrent._werteliste.Initialize();
 //BA.debugLineNum = 39;BA.debugLine="For i = 0 To Fragenanzahl - 1";
{
final int step28 = 1;
final int limit28 = (int) (_fragenanzahl-1);
for (_i = (int) (0); (step28 > 0 && _i <= limit28) || (step28 < 0 && _i >= limit28); _i = ((int)(0 + _i + step28))) {
 //BA.debugLineNum = 40;BA.debugLine="Werteliste.Add(Rnd(0, 101))";
mostCurrent._werteliste.Add((Object)(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (101))));
 }
};
 }else {
 //BA.debugLineNum = 43;BA.debugLine="Werteliste = File.ReadList(DirData, \"Werteliste.txt\")";
mostCurrent._werteliste = anywheresoftware.b4a.keywords.Common.File.ReadList(_dirdata,"Werteliste.txt");
 };
 };
 //BA.debugLineNum = 49;BA.debugLine="Panels.Initialize(\"panels\")";
mostCurrent._panels.Initialize(mostCurrent.activityBA,"panels");
 //BA.debugLineNum = 50;BA.debugLine="Panels.Color = Colors.Gray ' RGB(Rnd(250, 255), Rnd(250, 255), Rnd(250, 255))";
mostCurrent._panels.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 51;BA.debugLine="lblÜberschrift.Initialize(\"\")";
mostCurrent._lblüberschrift.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 52;BA.debugLine="lblÜberschrift.Text = \"Richtigmacher\"";
mostCurrent._lblüberschrift.setText((Object)("Richtigmacher"));
 //BA.debugLineNum = 53;BA.debugLine="lblÜberschrift.TextSize = 30";
mostCurrent._lblüberschrift.setTextSize((float) (30));
 //BA.debugLineNum = 54;BA.debugLine="lblÜberschrift.TextColor = Colors.White";
mostCurrent._lblüberschrift.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 55;BA.debugLine="Panels.AddView(lblÜberschrift, 55dip, 10dip, 100%x - 5dip, 60dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._lblüberschrift.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 56;BA.debugLine="Activity.AddView(Panels, 0, 0, 100%x, 100%y) 'add the panel to the layout";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panels.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 57;BA.debugLine="Activity.AddMenuItem(\"Saugworte\", \"Menu\")";
mostCurrent._activity.AddMenuItem("Saugworte","Menu");
 //BA.debugLineNum = 60;BA.debugLine="scvHIntergrund.Initialize(500)";
mostCurrent._scvhintergrund.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 61;BA.debugLine="Panels.AddView(scvHIntergrund, 0, 50dip, 100%x - 5dip, 100%y - 110dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._scvhintergrund.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (110))));
 //BA.debugLineNum = 64;BA.debugLine="btnSpeichern.Initialize (\"btnSpeichern\")";
mostCurrent._btnspeichern.Initialize(mostCurrent.activityBA,"btnSpeichern");
 //BA.debugLineNum = 65;BA.debugLine="Button2.Initialize (\"Button2\")";
mostCurrent._button2.Initialize(mostCurrent.activityBA,"Button2");
 //BA.debugLineNum = 66;BA.debugLine="btnTop.Initialize (\"btnTop\")";
mostCurrent._btntop.Initialize(mostCurrent.activityBA,"btnTop");
 //BA.debugLineNum = 67;BA.debugLine="btnSpeichern.Tag=1";
mostCurrent._btnspeichern.setTag((Object)(1));
 //BA.debugLineNum = 68;BA.debugLine="Button2.Tag=2";
mostCurrent._button2.setTag((Object)(2));
 //BA.debugLineNum = 69;BA.debugLine="btnTop.Tag=3";
mostCurrent._btntop.setTag((Object)(3));
 //BA.debugLineNum = 70;BA.debugLine="btnSpeichern.Text=\"Speichern\"";
mostCurrent._btnspeichern.setText((Object)("Speichern"));
 //BA.debugLineNum = 71;BA.debugLine="Button2.Text=\"\"";
mostCurrent._button2.setText((Object)(""));
 //BA.debugLineNum = 72;BA.debugLine="btnTop.Text=\"Top\"";
mostCurrent._btntop.setText((Object)("Top"));
 //BA.debugLineNum = 73;BA.debugLine="Panels.AddView (btnSpeichern, 0, 100%y - 50dip, 33%x, 50dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._btnspeichern.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 74;BA.debugLine="Panels.AddView (Button2, 33%x, 100%y - 50dip, 33%x, 50dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._button2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 75;BA.debugLine="Panels.AddView (btnTop, 66%X, 100%y - 50dip, 33%x, 50dip)";
mostCurrent._panels.AddView((android.view.View)(mostCurrent._btntop.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (66),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 78;BA.debugLine="b = 0";
_b = (int) (0);
 //BA.debugLineNum = 79;BA.debugLine="s = 1160";
_s = (int) (1160);
 //BA.debugLineNum = 80;BA.debugLine="Listenerzeugung";
_listenerzeugung();
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _btnspeichern_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _list1 = null;
int _i = 0;
int _x = 0;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.SeekBarWrapper _skb = null;
 //BA.debugLineNum = 138;BA.debugLine="Sub btnSpeichern_click";
 //BA.debugLineNum = 140;BA.debugLine="Dim List1 As List";
_list1 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 141;BA.debugLine="Dim i, x As Int";
_i = 0;
_x = 0;
 //BA.debugLineNum = 143;BA.debugLine="List1.Initialize";
_list1.Initialize();
 //BA.debugLineNum = 146;BA.debugLine="For i = 0 To Fragenanzahl - 1";
{
final int step91 = 1;
final int limit91 = (int) (_fragenanzahl-1);
for (_i = (int) (0); (step91 > 0 && _i <= limit91) || (step91 < 0 && _i >= limit91); _i = ((int)(0 + _i + step91))) {
 //BA.debugLineNum = 148;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 149;BA.debugLine="pnl = scvHIntergrund.Panel.GetView(i)";
_pnl.setObject((android.view.ViewGroup)(mostCurrent._scvhintergrund.getPanel().GetView(_i).getObject()));
 //BA.debugLineNum = 150;BA.debugLine="Dim skb As SeekBar";
_skb = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 151;BA.debugLine="skb = pnl.GetView(1)";
_skb.setObject((android.widget.SeekBar)(_pnl.GetView((int) (1)).getObject()));
 //BA.debugLineNum = 152;BA.debugLine="x = skb.value";
_x = _skb.getValue();
 //BA.debugLineNum = 153;BA.debugLine="List1.Add(x)";
_list1.Add((Object)(_x));
 }
};
 //BA.debugLineNum = 156;BA.debugLine="File.WriteList(DirData, \"Werteliste.txt\", List1)";
anywheresoftware.b4a.keywords.Common.File.WriteList(_dirdata,"Werteliste.txt",_list1);
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static String  _btntop_click() throws Exception{
 //BA.debugLineNum = 169;BA.debugLine="Sub btnTop_click";
 //BA.debugLineNum = 174;BA.debugLine="scvHIntergrund.ScrollPosition = 0";
mostCurrent._scvhintergrund.setScrollPosition((int) (0));
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub Button2_click";
 //BA.debugLineNum = 166;BA.debugLine="End Sub";
return "";
}
public static String  _button4_click() throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub Button4_click";
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 14;BA.debugLine="Dim Bitmap1 As Bitmap";
mostCurrent._bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim Fragenanzahl As Int			: Fragenanzahl=1160";
_fragenanzahl = 0;
 //BA.debugLineNum = 15;BA.debugLine="Dim Fragenanzahl As Int			: Fragenanzahl=1160";
_fragenanzahl = (int) (1160);
 //BA.debugLineNum = 16;BA.debugLine="Dim PanelHeight As Int			: PanelHeight=130dip";
_panelheight = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim PanelHeight As Int			: PanelHeight=130dip";
_panelheight = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (130));
 //BA.debugLineNum = 17;BA.debugLine="Dim Panels As Panel";
mostCurrent._panels = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim btnSpeichern, Button2, btnTop As Button";
mostCurrent._btnspeichern = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._button2 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btntop = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim scvHIntergrund As ScrollView";
mostCurrent._scvhintergrund = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim lblÜberschrift As Label";
mostCurrent._lblüberschrift = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim sbStärke As SeekBar";
mostCurrent._sbstärke = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim Fragen As List";
mostCurrent._fragen = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 23;BA.debugLine="Dim s, b, e As Int";
_s = 0;
_b = 0;
_e = 0;
 //BA.debugLineNum = 24;BA.debugLine="Dim Werteliste As List";
mostCurrent._werteliste = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _listenerzeugung() throws Exception{
int _i = 0;
int _val = 0;
anywheresoftware.b4a.objects.PanelWrapper _pnleinheit = null;
anywheresoftware.b4a.objects.LabelWrapper _lbltafel = null;
anywheresoftware.b4a.objects.EditTextWrapper _edteingabefeld = null;
String _val1 = "";
 //BA.debugLineNum = 83;BA.debugLine="Sub Listenerzeugung";
 //BA.debugLineNum = 84;BA.debugLine="Dim i, val As Int";
_i = 0;
_val = 0;
 //BA.debugLineNum = 90;BA.debugLine="For i = 0 To Fragenanzahl - 1 'originalzeile";
{
final int step64 = 1;
final int limit64 = (int) (_fragenanzahl-1);
for (_i = (int) (0); (step64 > 0 && _i <= limit64) || (step64 < 0 && _i >= limit64); _i = ((int)(0 + _i + step64))) {
 //BA.debugLineNum = 92;BA.debugLine="Dim pnlEinheit As Panel";
_pnleinheit = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Dim lblTafel As Label";
_lbltafel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Dim edtEingabefeld As EditText";
_edteingabefeld = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 97;BA.debugLine="pnlEinheit.Initialize(\"pnlEinheit\")";
_pnleinheit.Initialize(mostCurrent.activityBA,"pnlEinheit");
 //BA.debugLineNum = 98;BA.debugLine="scvHIntergrund.Panel.AddView(pnlEinheit, 0 , 5dip + i * PanelHeight, 100%x - 5dip, PanelHeight)";
mostCurrent._scvhintergrund.getPanel().AddView((android.view.View)(_pnleinheit.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))+_i*_panelheight),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),_panelheight);
 //BA.debugLineNum = 99;BA.debugLine="pnlEinheit.Tag = i";
_pnleinheit.setTag((Object)(_i));
 //BA.debugLineNum = 102;BA.debugLine="lblTafel.Initialize(\"lblTafel\")";
_lbltafel.Initialize(mostCurrent.activityBA,"lblTafel");
 //BA.debugLineNum = 103;BA.debugLine="pnlEinheit.AddView(lblTafel, 3%x, 0dip, 95%x, 120dip)";
_pnleinheit.AddView((android.view.View)(_lbltafel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (95),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)));
 //BA.debugLineNum = 104;BA.debugLine="lblTafel.Tag=i";
_lbltafel.setTag((Object)(_i));
 //BA.debugLineNum = 105;BA.debugLine="lblTafel.TextSize=20";
_lbltafel.setTextSize((float) (20));
 //BA.debugLineNum = 106;BA.debugLine="lblTafel.color = Colors.LightGray";
_lbltafel.setColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 107;BA.debugLine="lblTafel.textColor = Colors.Black";
_lbltafel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 108;BA.debugLine="lblTafel.Text = Fragen.Get(i)";
_lbltafel.setText(mostCurrent._fragen.Get(_i));
 //BA.debugLineNum = 119;BA.debugLine="sbStärke.Initialize(\"sbStärke\")";
mostCurrent._sbstärke.Initialize(mostCurrent.activityBA,"sbStärke");
 //BA.debugLineNum = 120;BA.debugLine="pnlEinheit.AddView(sbStärke, 10%x,75dip,80%x,30dip)";
_pnleinheit.AddView((android.view.View)(mostCurrent._sbstärke.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (75)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 121;BA.debugLine="sbStärke.Tag = i";
mostCurrent._sbstärke.setTag((Object)(_i));
 //BA.debugLineNum = 122;BA.debugLine="val1 = Werteliste.get(i)";
_val1 = BA.ObjectToString(mostCurrent._werteliste.Get(_i));
 //BA.debugLineNum = 123;BA.debugLine="val = val1";
_val = (int)(Double.parseDouble(_val1));
 //BA.debugLineNum = 124;BA.debugLine="sbStärke.value = val";
mostCurrent._sbstärke.setValue(_val);
 }
};
 //BA.debugLineNum = 135;BA.debugLine="scvHIntergrund.Panel.Height = Fragenanzahl * PanelHeight";
mostCurrent._scvhintergrund.getPanel().setHeight((int) (_fragenanzahl*_panelheight));
 //BA.debugLineNum = 136;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim DirData As String";
_dirdata = "";
 //BA.debugLineNum = 9;BA.debugLine="DirData = File.DirInternal";
_dirdata = anywheresoftware.b4a.keywords.Common.File.getDirInternal();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
