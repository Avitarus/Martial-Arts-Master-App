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

public class einstellungentrainingkick extends Activity implements B4AActivity{
	public static einstellungentrainingkick mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.einstellungentrainingkick");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (einstellungentrainingkick).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.einstellungentrainingkick");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.einstellungentrainingkick", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (einstellungentrainingkick) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (einstellungentrainingkick) Resume **");
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
		return einstellungentrainingkick.class;
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
        BA.LogInfo("** Activity (einstellungentrainingkick) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (einstellungentrainingkick) Resume **");
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
public static int _trainingszeit1 = 0;
public static int _pause1 = 0;
public static int _gesamttrainingszeit = 0;
public static boolean _pausemachen = false;
public anywheresoftware.b4a.objects.SpinnerWrapper _spntraining1 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnpause1 = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _seekbar1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label8 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.ToggleButtonWrapper _togglebutton1 = null;
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
 //BA.debugLineNum = 31;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 33;BA.debugLine="Activity.Title = \"Einstellungen Training Stunde\"";
mostCurrent._activity.setTitle((Object)("Einstellungen Training Stunde"));
 //BA.debugLineNum = 35;BA.debugLine="Activity.LoadLayout(\"EinstellungenTrainingKick\")";
mostCurrent._activity.LoadLayout("EinstellungenTrainingKick",mostCurrent.activityBA);
 //BA.debugLineNum = 36;BA.debugLine="Label1.Text = Main.Ueberschrift' & CRLF & \"Einstellungen\"";
mostCurrent._label1.setText((Object)(mostCurrent._main._ueberschrift));
 //BA.debugLineNum = 37;BA.debugLine="Seekbar1.Value = 45";
mostCurrent._seekbar1.setValue((int) (45));
 //BA.debugLineNum = 40;BA.debugLine="spnTraining1.Prompt = \"Übung:\"";
mostCurrent._spntraining1.setPrompt("Übung:");
 //BA.debugLineNum = 41;BA.debugLine="spnTraining1.Add(\"10 sec\")";
mostCurrent._spntraining1.Add("10 sec");
 //BA.debugLineNum = 42;BA.debugLine="spnTraining1.Add(\"20 sec\")";
mostCurrent._spntraining1.Add("20 sec");
 //BA.debugLineNum = 43;BA.debugLine="spnTraining1.Add(\"30 sec\")";
mostCurrent._spntraining1.Add("30 sec");
 //BA.debugLineNum = 44;BA.debugLine="spnTraining1.Add(\"40 sec\")";
mostCurrent._spntraining1.Add("40 sec");
 //BA.debugLineNum = 45;BA.debugLine="spnTraining1.Add(\"50 sec\")";
mostCurrent._spntraining1.Add("50 sec");
 //BA.debugLineNum = 46;BA.debugLine="spnTraining1.Add(\"1 min\")";
mostCurrent._spntraining1.Add("1 min");
 //BA.debugLineNum = 47;BA.debugLine="spnTraining1.Add(\"2 min\")";
mostCurrent._spntraining1.Add("2 min");
 //BA.debugLineNum = 48;BA.debugLine="spnTraining1.Add(\"3 min\")";
mostCurrent._spntraining1.Add("3 min");
 //BA.debugLineNum = 49;BA.debugLine="spnTraining1.Add(\"4 min\")";
mostCurrent._spntraining1.Add("4 min");
 //BA.debugLineNum = 50;BA.debugLine="spnTraining1.Add(\"5 min\")";
mostCurrent._spntraining1.Add("5 min");
 //BA.debugLineNum = 51;BA.debugLine="spnTraining1.SelectedIndex = 2";
mostCurrent._spntraining1.setSelectedIndex((int) (2));
 //BA.debugLineNum = 52;BA.debugLine="Trainingszeit1 = (2 +1) * 10000";
_trainingszeit1 = (int) ((2+1)*10000);
 //BA.debugLineNum = 53;BA.debugLine="If 2 > 4 Then Trainingszeit1 = (2 - 4) * 60000";
if (2>4) { 
_trainingszeit1 = (int) ((2-4)*60000);};
 //BA.debugLineNum = 56;BA.debugLine="spnPause1.Prompt = \"Pause:\"";
mostCurrent._spnpause1.setPrompt("Pause:");
 //BA.debugLineNum = 57;BA.debugLine="spnPause1.Add(\"10 sec\")";
mostCurrent._spnpause1.Add("10 sec");
 //BA.debugLineNum = 58;BA.debugLine="spnPause1.Add(\"20 sec\")";
mostCurrent._spnpause1.Add("20 sec");
 //BA.debugLineNum = 59;BA.debugLine="spnPause1.Add(\"30 sec\")";
mostCurrent._spnpause1.Add("30 sec");
 //BA.debugLineNum = 60;BA.debugLine="spnPause1.Add(\"40 sec\")";
mostCurrent._spnpause1.Add("40 sec");
 //BA.debugLineNum = 61;BA.debugLine="spnPause1.Add(\"50 sec\")";
mostCurrent._spnpause1.Add("50 sec");
 //BA.debugLineNum = 62;BA.debugLine="spnPause1.Add(\"1 min\")";
mostCurrent._spnpause1.Add("1 min");
 //BA.debugLineNum = 63;BA.debugLine="spnPause1.Add(\"2 min\")";
mostCurrent._spnpause1.Add("2 min");
 //BA.debugLineNum = 64;BA.debugLine="spnPause1.Add(\"3 min\")";
mostCurrent._spnpause1.Add("3 min");
 //BA.debugLineNum = 65;BA.debugLine="spnPause1.Add(\"4 min\")";
mostCurrent._spnpause1.Add("4 min");
 //BA.debugLineNum = 66;BA.debugLine="spnPause1.Add(\"5 min\")";
mostCurrent._spnpause1.Add("5 min");
 //BA.debugLineNum = 67;BA.debugLine="spnPause1.SelectedIndex = 5";
mostCurrent._spnpause1.setSelectedIndex((int) (5));
 //BA.debugLineNum = 68;BA.debugLine="Pause1 = (5 +1) * 10000";
_pause1 = (int) ((5+1)*10000);
 //BA.debugLineNum = 69;BA.debugLine="If 5 > 4 Then Pause1 = (5 - 4) * 60000";
if (5>4) { 
_pause1 = (int) ((5-4)*60000);};
 //BA.debugLineNum = 71;BA.debugLine="Label8.Text = \"Gesamttrainingszeit: \" & Seekbar1.Value & \" min\"";
mostCurrent._label8.setText((Object)("Gesamttrainingszeit: "+BA.NumberToString(mostCurrent._seekbar1.getValue())+" min"));
 //BA.debugLineNum = 73;BA.debugLine="PauseMachen = True";
_pausemachen = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _btnstart_click() throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Sub btnStart_Click";
 //BA.debugLineNum = 112;BA.debugLine="If Gesamttrainingszeit < 1.01 Then";
if (_gesamttrainingszeit<1.01) { 
 //BA.debugLineNum = 113;BA.debugLine="ToastMessageShow(\"Gesamttrainingszeit einstellen\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Gesamttrainingszeit einstellen",anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 115;BA.debugLine="StartActivity(TrainingKickboxen)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._trainingkickboxen.getObject()));
 };
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Dim spnTraining1, spnPause1 As Spinner";
mostCurrent._spntraining1 = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spnpause1 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim Seekbar1 As SeekBar:";
mostCurrent._seekbar1 = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim Label8, Label1 As Label";
mostCurrent._label8 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim ToggleButton1 As ToggleButton";
mostCurrent._togglebutton1 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.ToggleButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim Trainingszeit1 As Int		: Trainingszeit1 = 50000";
_trainingszeit1 = 0;
 //BA.debugLineNum = 15;BA.debugLine="Dim Trainingszeit1 As Int		: Trainingszeit1 = 50000";
_trainingszeit1 = (int) (50000);
 //BA.debugLineNum = 16;BA.debugLine="Dim Pause1 As Int				: Pause1 = 60000";
_pause1 = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim Pause1 As Int				: Pause1 = 60000";
_pause1 = (int) (60000);
 //BA.debugLineNum = 17;BA.debugLine="Dim Gesamttrainingszeit As Int	: Gesamttrainingszeit = 60 * 60000";
_gesamttrainingszeit = 0;
 //BA.debugLineNum = 17;BA.debugLine="Dim Gesamttrainingszeit As Int	: Gesamttrainingszeit = 60 * 60000";
_gesamttrainingszeit = (int) (60*60000);
 //BA.debugLineNum = 18;BA.debugLine="Dim PauseMachen As Boolean";
_pausemachen = false;
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _seekbar1_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 101;BA.debugLine="Sub SeekBar1_ValueChanged (Value As Int, UserChanged As Boolean)";
 //BA.debugLineNum = 103;BA.debugLine="Label8.Text = \"Gesamttrainingszeit: \" & Seekbar1.Value & \" min\"";
mostCurrent._label8.setText((Object)("Gesamttrainingszeit: "+BA.NumberToString(mostCurrent._seekbar1.getValue())+" min"));
 //BA.debugLineNum = 104;BA.debugLine="Gesamttrainingszeit = Seekbar1.Value * 60000";
_gesamttrainingszeit = (int) (mostCurrent._seekbar1.getValue()*60000);
 //BA.debugLineNum = 105;BA.debugLine="If Gesamttrainingszeit = 0 Then Gesamttrainingszeit = Gesamttrainingszeit + 1";
if (_gesamttrainingszeit==0) { 
_gesamttrainingszeit = (int) (_gesamttrainingszeit+1);};
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _spnpause1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 92;BA.debugLine="Sub spnPause1_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 94;BA.debugLine="Pause1 = (Position +1) * 10000";
_pause1 = (int) ((_position+1)*10000);
 //BA.debugLineNum = 95;BA.debugLine="If Position > 4 Then Pause1 = (Position - 4) * 60000";
if (_position>4) { 
_pause1 = (int) ((_position-4)*60000);};
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _spntraining1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 84;BA.debugLine="Sub spnTraining1_ItemClick (Position As Int, Value As Object)";
 //BA.debugLineNum = 86;BA.debugLine="Trainingszeit1 = (Position +1) * 10000";
_trainingszeit1 = (int) ((_position+1)*10000);
 //BA.debugLineNum = 87;BA.debugLine="If Position > 4 Then Trainingszeit1 = (Position - 4) * 60000";
if (_position>4) { 
_trainingszeit1 = (int) ((_position-4)*60000);};
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
public static String  _togglebutton1_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 122;BA.debugLine="Sub ToggleButton1_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 124;BA.debugLine="If Checked = False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 125;BA.debugLine="PauseMachen =  False";
_pausemachen = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 127;BA.debugLine="PauseMachen = True";
_pausemachen = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
}
