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

public class startabfragen extends Activity implements B4AActivity{
	public static startabfragen mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.startabfragen");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (startabfragen).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.startabfragen");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.startabfragen", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (startabfragen) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (startabfragen) Resume **");
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
		return startabfragen.class;
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
        BA.LogInfo("** Activity (startabfragen) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (startabfragen) Resume **");
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
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _image = null;
public static com.AB.ABZipUnzip.ABZipUnzip _myzip = null;
public static String _strlizensnummer = "";
public static String _strpassword = "";
public anywheresoftware.b4a.objects.ButtonWrapper _btnpasswort = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnnutzerbedingungen = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnlizensnummer = null;
public anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _trlizens = null;
public anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _trpassword = null;
public anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _twlizens = null;
public anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _twpassword = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog _inputtextpass = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog _inputtextlizens = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndownload = null;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
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

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 30;BA.debugLine="Activity.Title = \"Lizens, Nutzerbedingungen, Passwort\"";
mostCurrent._activity.setTitle((Object)("Lizens, Nutzerbedingungen, Passwort"));
 //BA.debugLineNum = 33;BA.debugLine="If File.ExternalWritable = False Then";
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 34;BA.debugLine="Msgbox(\"Ich kann nicht auf die SD-Karte schreiben.\", \"\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Ich kann nicht auf die SD-Karte schreiben.","",mostCurrent.activityBA);
 //BA.debugLineNum = 35;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 45;BA.debugLine="Passwortabfrage";
_passwortabfrage();
 //BA.debugLineNum = 46;BA.debugLine="Lizensnummernabfrage";
_lizensnummernabfrage();
 //BA.debugLineNum = 47;BA.debugLine="Nutzerbedingungen";
_nutzerbedingungen();
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 115;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 116;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 118;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 //BA.debugLineNum = 120;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 122;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 67;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 57;BA.debugLine="If btnDownload.Enabled = False AND DownloadService.JobStatus = DownloadService.STATUS_DONE Then";
if (mostCurrent._btndownload.getEnabled()==anywheresoftware.b4a.keywords.Common.False && mostCurrent._downloadservice._jobstatus==mostCurrent._downloadservice._status_done) { 
 //BA.debugLineNum = 58;BA.debugLine="FinishDownload";
_finishdownload();
 };
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _finishdownload() throws Exception{
 //BA.debugLineNum = 291;BA.debugLine="Sub FinishDownload";
 //BA.debugLineNum = 293;BA.debugLine="If DownloadService.DoneSuccessfully = True Then";
if (mostCurrent._downloadservice._donesuccessfully==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 295;BA.debugLine="myZip.ABUnzip(File.DirRootExternal & \"mama.zip\",File.DirRootExternal)'  & \"target\")";
_myzip.ABUnzip(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"mama.zip",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal());
 //BA.debugLineNum = 297;BA.debugLine="Msgbox(\"´mama.zip´ ist entpackt. Sie können mit der Software arbeiten.\",\"Download komplett\")";
anywheresoftware.b4a.keywords.Common.Msgbox("´mama.zip´ ist entpackt. Sie können mit der Software arbeiten.","Download komplett",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 302;BA.debugLine="btnDownload.Enabled = True";
mostCurrent._btndownload.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 303;BA.debugLine="DownloadService.JobStatus = DownloadService.STATUS_NONE";
mostCurrent._downloadservice._jobstatus = mostCurrent._downloadservice._status_none;
 //BA.debugLineNum = 305;BA.debugLine="Main.IckWarAllHier = True";
mostCurrent._main._ickwarallhier = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 307;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 311;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim strLizensnummer, strPassword As String";
mostCurrent._strlizensnummer = "";
mostCurrent._strpassword = "";
 //BA.debugLineNum = 18;BA.debugLine="Dim btnPasswort, btnNutzerbedingungen, btnLizensnummer As Button";
mostCurrent._btnpasswort = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnnutzerbedingungen = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnlizensnummer = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim trLizens, trPassword As TextReader";
mostCurrent._trlizens = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
mostCurrent._trpassword = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim twLizens, twPassword As TextWriter";
mostCurrent._twlizens = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
mostCurrent._twpassword = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim InputTextPass, InputTextLizens As InputDialog";
mostCurrent._inputtextpass = new anywheresoftware.b4a.agraham.dialogs.InputDialog();
mostCurrent._inputtextlizens = new anywheresoftware.b4a.agraham.dialogs.InputDialog();
 //BA.debugLineNum = 22;BA.debugLine="Dim btnDownload As Button";
mostCurrent._btndownload = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _lizensnummernabfrage() throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Sub Lizensnummernabfrage";
 //BA.debugLineNum = 130;BA.debugLine="Activity.Title = Main.ActivityTitel";
mostCurrent._activity.setTitle((Object)(mostCurrent._main._activitytitel));
 //BA.debugLineNum = 132;BA.debugLine="If File.Exists(File.DirRootExternal & Main.Unterordner1, \"Lizens_file.txt\") = True Then 'Check to see if there is already a password file saved and therefore a custom password";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner1,"Lizens_file.txt")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 133;BA.debugLine="trLizens.Initialize(File.OpenInput(File.DirAssets, \"Lizens_file.txt\"))";
mostCurrent._trlizens.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Lizens_file.txt").getObject()));
 //BA.debugLineNum = 134;BA.debugLine="strLizensnummer = trLizens.ReadLine";
mostCurrent._strlizensnummer = mostCurrent._trlizens.ReadLine();
 //BA.debugLineNum = 135;BA.debugLine="trLizens.Close ' Load the password into strPassword";
mostCurrent._trlizens.Close();
 }else {
 //BA.debugLineNum = 137;BA.debugLine="strLizensnummer = \"aaa\" ' If there is no password file then set strPassword to default";
mostCurrent._strlizensnummer = "aaa";
 };
 //BA.debugLineNum = 140;BA.debugLine="Do While InputTextLizens.Input <> strLizensnummer ' Keep displaying the input password input dialog until the correct password has been entered";
while ((mostCurrent._inputtextlizens.getInput()).equals(mostCurrent._strlizensnummer) == false) {
 //BA.debugLineNum = 141;BA.debugLine="If InputTextLizens.Show(\"Bitte geben Sie bitte Ihre 30 stellige Lizensnummer ein. (a)\",\"Lizensnummer\",\"Prüfen\",\"\",\"Beenden\",Null) = DialogResponse.NEGATIVE Then ' If the user selects exit then close the activity";
if (mostCurrent._inputtextlizens.Show("Bitte geben Sie bitte Ihre 30 stellige Lizensnummer ein. (a)","Lizensnummer","Prüfen","","Beenden",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null))==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 };
 //BA.debugLineNum = 144;BA.debugLine="If InputTextLizens.Input <> strLizensnummer Then ' Display wrong password message if wrong password entered";
if ((mostCurrent._inputtextlizens.getInput()).equals(mostCurrent._strlizensnummer) == false) { 
 //BA.debugLineNum = 145;BA.debugLine="ToastMessageShow(\"Falsche Lizensnummer, Nochmal versuchen\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Falsche Lizensnummer, Nochmal versuchen",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 146;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 };
 }
;
 //BA.debugLineNum = 154;BA.debugLine="End Sub";
return "";
}
public static String  _nutzerbedingungen() throws Exception{
String _txt = "";
int _result = 0;
 //BA.debugLineNum = 171;BA.debugLine="Sub Nutzerbedingungen";
 //BA.debugLineNum = 174;BA.debugLine="Dim TXT As String";
_txt = "";
 //BA.debugLineNum = 175;BA.debugLine="TXT = \"Nutzerbedingungen\" & CRLF";
_txt = "Nutzerbedingungen"+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 176;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 177;BA.debugLine="result = Msgbox2(File.ReadString(File.DirAssets,\"Nutzerbedingungen.txt\"),TXT,\"Annehmen\", \"Abbruch\", \"Ablehnen\", Null)";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Nutzerbedingungen.txt"),_txt,"Annehmen","Abbruch","Ablehnen",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 179;BA.debugLine="If result = DialogResponse.CANCEL Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 180;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 //BA.debugLineNum = 185;BA.debugLine="If result = DialogResponse.Positive Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 186;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 };
 //BA.debugLineNum = 190;BA.debugLine="If result = DialogResponse.NEGATIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 191;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 //BA.debugLineNum = 197;BA.debugLine="End Sub";
return "";
}
public static String  _ordnereinrichten() throws Exception{
String _txt1 = "";
String _txt = "";
int _result = 0;
 //BA.debugLineNum = 200;BA.debugLine="Sub OrdnerEinrichten";
 //BA.debugLineNum = 230;BA.debugLine="If File.Exists(File.DirRootExternal  & Main.Unterordner1,\"SetupApp.txt\") = False Then File.MakeDir(File.DirRootExternal , Main.Hauptordner0)";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner1,"SetupApp.txt")==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._hauptordner0);};
 //BA.debugLineNum = 231;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner1 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner1)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner1)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner1);};
 //BA.debugLineNum = 232;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner2 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner2)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner2)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner2);};
 //BA.debugLineNum = 233;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner3 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner3)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner3)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner3);};
 //BA.debugLineNum = 234;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner4 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner4)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner4)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner4);};
 //BA.debugLineNum = 235;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner5 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner5)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner5)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner5);};
 //BA.debugLineNum = 236;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner6 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner6)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner6)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner6);};
 //BA.debugLineNum = 237;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner7 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner7)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner7)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner7);};
 //BA.debugLineNum = 238;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner8 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner8)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner8)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner8);};
 //BA.debugLineNum = 239;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner9 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner9)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner9)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner9);};
 //BA.debugLineNum = 240;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner10 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner10)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner10)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner10);};
 //BA.debugLineNum = 241;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner11 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner11)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner11)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner11);};
 //BA.debugLineNum = 242;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner12 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner12)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner12)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner12);};
 //BA.debugLineNum = 243;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner13 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner13)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner13)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner13);};
 //BA.debugLineNum = 244;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner14 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner14)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner14)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner14);};
 //BA.debugLineNum = 245;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner15 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner15)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner15)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner15);};
 //BA.debugLineNum = 250;BA.debugLine="If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner20 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner20)";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner20)==anywheresoftware.b4a.keywords.Common.False) { 
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._main._unterordner20);};
 //BA.debugLineNum = 252;BA.debugLine="Dim txt1, txt As String";
_txt1 = "";
_txt = "";
 //BA.debugLineNum = 253;BA.debugLine="txt1 = \" Wollen Sie jetzt die zur App gehörende Datei ´mama.zip´ kostenlos von meiner Homepage ´mama.watchkido.de´ aus dem Internet laden und in den Ordner ´/mama´ entpacken? ( <25MB ca. 10 min nur über WLAN!) Diese App funktioniert nur mit diesen Bildern. Der Ordner ist laut ´ZONE-ALARM by Check Point´ virenfrei.\"";
_txt1 = " Wollen Sie jetzt die zur App gehörende Datei ´mama.zip´ kostenlos von meiner Homepage ´mama.watchkido.de´ aus dem Internet laden und in den Ordner ´/mama´ entpacken? ( <25MB ca. 10 min nur über WLAN!) Diese App funktioniert nur mit diesen Bildern. Der Ordner ist laut ´ZONE-ALARM by Check Point´ virenfrei.";
 //BA.debugLineNum = 254;BA.debugLine="txt = \"Bilder-Ordner downloaden?\"";
_txt = "Bilder-Ordner downloaden?";
 //BA.debugLineNum = 255;BA.debugLine="Dim result As Int";
_result = 0;
 //BA.debugLineNum = 256;BA.debugLine="result = Msgbox2(txt1,txt,\"JA\", \"Abbruch\", \"NEIN\", Null)";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(_txt1,_txt,"JA","Abbruch","NEIN",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 258;BA.debugLine="If result = DialogResponse.CANCEL Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 259;BA.debugLine="Main.IckWarAllHier = True";
mostCurrent._main._ickwarallhier = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 260;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 };
 //BA.debugLineNum = 265;BA.debugLine="If result = DialogResponse.Positive Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 266;BA.debugLine="Activity.Color = Colors.Black";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 267;BA.debugLine="DownloadService.URL = \"https://sites.google.com/site/\"";
mostCurrent._downloadservice._url = "https://sites.google.com/site/";
 //BA.debugLineNum = 268;BA.debugLine="DownloadService.Target = File.OpenOutput(File.DirRootExternal , \"mama.zip\", False)";
mostCurrent._downloadservice._target = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"mama.zip",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 269;BA.debugLine="StartService(DownloadService)";
anywheresoftware.b4a.keywords.Common.StartService(mostCurrent.activityBA,(Object)(mostCurrent._downloadservice.getObject()));
 //BA.debugLineNum = 270;BA.debugLine="btnDownload.Enabled = False";
mostCurrent._btndownload.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 272;BA.debugLine="ToastMessageShow(\"MAMA-Ordner wird auf SD-Card Eingerichtet\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("MAMA-Ordner wird auf SD-Card Eingerichtet",anywheresoftware.b4a.keywords.Common.False);
 }else {
 };
 //BA.debugLineNum = 277;BA.debugLine="If result = DialogResponse.NEGATIVE Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 278;BA.debugLine="Main.IckWarAllHier = True";
mostCurrent._main._ickwarallhier = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 279;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 };
 //BA.debugLineNum = 289;BA.debugLine="End Sub";
return "";
}
public static String  _passwortabfrage() throws Exception{
 //BA.debugLineNum = 73;BA.debugLine="Sub Passwortabfrage";
 //BA.debugLineNum = 77;BA.debugLine="Activity.Title = Main.ActivityTitel";
mostCurrent._activity.setTitle((Object)(mostCurrent._main._activitytitel));
 //BA.debugLineNum = 79;BA.debugLine="If File.Exists(File.DirRootExternal & Main.Unterordner1, \"password_file.txt\") = True Then 'Check to see if there is already a password file saved and therefore a custom password";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._main._unterordner1,"password_file.txt")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 80;BA.debugLine="trPassword.Initialize(File.OpenInput(File.DirAssets, \"password_file.txt\"))";
mostCurrent._trpassword.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"password_file.txt").getObject()));
 //BA.debugLineNum = 81;BA.debugLine="strPassword = trPassword.ReadLine";
mostCurrent._strpassword = mostCurrent._trpassword.ReadLine();
 //BA.debugLineNum = 82;BA.debugLine="trPassword.Close ' Load the password into strPassword";
mostCurrent._trpassword.Close();
 }else {
 //BA.debugLineNum = 84;BA.debugLine="strPassword = \"0000\" ' If there is no password file then set strPassword to default";
mostCurrent._strpassword = "0000";
 };
 //BA.debugLineNum = 87;BA.debugLine="Do While InputTextPass.Input <> strPassword ' Keep displaying the input password input dialog until the correct password has been entered";
while ((mostCurrent._inputtextpass.getInput()).equals(mostCurrent._strpassword) == false) {
 //BA.debugLineNum = 88;BA.debugLine="If InputTextPass.Show(\"Bitte geben Sie das Passwort ein.\",\"Passwort\",\"Prüfen\",\"\",\"Exit\",Null) = DialogResponse.NEGATIVE Then ' If the user selects exit then close the activity";
if (mostCurrent._inputtextpass.Show("Bitte geben Sie das Passwort ein.","Passwort","Prüfen","","Exit",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null))==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 };
 //BA.debugLineNum = 91;BA.debugLine="If InputTextPass.Input <> strPassword Then ' Display wrong password message if wrong password entered";
if ((mostCurrent._inputtextpass.getInput()).equals(mostCurrent._strpassword) == false) { 
 //BA.debugLineNum = 92;BA.debugLine="ToastMessageShow(\"Falsches Passwort, Nochmal versuchen\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Falsches Passwort, Nochmal versuchen",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 93;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 };
 }
;
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Dim image As Bitmap";
_image = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Dim myZip As ABZipUnzip";
_myzip = new com.AB.ABZipUnzip.ABZipUnzip();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
}
