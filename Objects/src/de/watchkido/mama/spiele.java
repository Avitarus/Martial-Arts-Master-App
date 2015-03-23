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

public class spiele extends Activity implements B4AActivity{
	public static spiele mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.spiele");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (spiele).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.spiele");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.spiele", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (spiele) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (spiele) Resume **");
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
		return spiele.class;
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
        BA.LogInfo("** Activity (spiele) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (spiele) Resume **");
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
public static String _progname = "";
public static String _progversion = "";
public static String _progauthor = "";
public static String _progdate = "";
public static int _helpindex = 0;
public static int _helpindexmax = 0;
public anywheresoftware.b4a.objects.PanelWrapper _pnlhelp = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnhelpprev = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnhelpnext = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnhelplast = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnhelpfirst = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnhelpok = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblhelp1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblhelp2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblhelp3 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvhelp = null;
public anywheresoftware.b4a.objects.StringUtils _strutils = null;
public de.watchkido.mama.main _main = null;
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

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 34;BA.debugLine="pnlHelp.Initialize(\"\")";
mostCurrent._pnlhelp.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 35;BA.debugLine="Activity.AddView(pnlHelp, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnlhelp.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 36;BA.debugLine="pnlHelp.LoadLayout(\"KampfsportSpiele\") 'ehemals main";
mostCurrent._pnlhelp.LoadLayout("KampfsportSpiele",mostCurrent.activityBA);
 //BA.debugLineNum = 37;BA.debugLine="Activity.Title = \"Kampfsport Spiele\"";
mostCurrent._activity.setTitle((Object)("Kampfsport Spiele"));
 //BA.debugLineNum = 38;BA.debugLine="scvHelp.Panel.LoadLayout(\"KampfsportSpieleUeberschrift\")'scrollview\")";
mostCurrent._scvhelp.getPanel().LoadLayout("KampfsportSpieleUeberschrift",mostCurrent.activityBA);
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 47;BA.debugLine="InitHelp";
_inithelp();
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _btnhelpmove_click() throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _send = null;
String _btn = "";
 //BA.debugLineNum = 143;BA.debugLine="Sub btnHelpMove_Click";
 //BA.debugLineNum = 146;BA.debugLine="Dim Send As View, btn As String";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
_btn = "";
 //BA.debugLineNum = 148;BA.debugLine="Send=Sender";
_send.setObject((android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 149;BA.debugLine="btn=Send.Tag";
_btn = BA.ObjectToString(_send.getTag());
 //BA.debugLineNum = 151;BA.debugLine="If HelpIndex > 0 AND btn = \"Prev\" Then";
if (_helpindex>0 && (_btn).equals("Prev")) { 
 //BA.debugLineNum = 152;BA.debugLine="HelpIndex = HelpIndex - 1";
_helpindex = (int) (_helpindex-1);
 }else if(_helpindex<_helpindexmax && (_btn).equals("Next")) { 
 //BA.debugLineNum = 154;BA.debugLine="HelpIndex = HelpIndex + 1";
_helpindex = (int) (_helpindex+1);
 }else if((_btn).equals("First")) { 
 //BA.debugLineNum = 156;BA.debugLine="HelpIndex = 0";
_helpindex = (int) (0);
 }else if((_btn).equals("Last")) { 
 //BA.debugLineNum = 158;BA.debugLine="HelpIndex = HelpIndexMax";
_helpindex = _helpindexmax;
 };
 //BA.debugLineNum = 161;BA.debugLine="If HelpIndex = 0 Then";
if (_helpindex==0) { 
 //BA.debugLineNum = 162;BA.debugLine="btnHelpPrev.Enabled = False";
mostCurrent._btnhelpprev.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 163;BA.debugLine="btnHelpFirst.Enabled = False";
mostCurrent._btnhelpfirst.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 165;BA.debugLine="btnHelpPrev.Enabled = True";
mostCurrent._btnhelpprev.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 166;BA.debugLine="btnHelpFirst.Enabled = True";
mostCurrent._btnhelpfirst.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 169;BA.debugLine="If HelpIndex=HelpIndexMax Then";
if (_helpindex==_helpindexmax) { 
 //BA.debugLineNum = 170;BA.debugLine="btnHelpNext.Enabled = False";
mostCurrent._btnhelpnext.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="btnHelpLast.Enabled = False";
mostCurrent._btnhelplast.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 173;BA.debugLine="btnHelpNext.Enabled = True";
mostCurrent._btnhelpnext.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 174;BA.debugLine="btnHelpLast.Enabled = True";
mostCurrent._btnhelplast.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 177;BA.debugLine="DisplayHelp";
_displayhelp();
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _btnhelpok_click() throws Exception{
 //BA.debugLineNum = 138;BA.debugLine="Sub btnHelpOK_Click";
 //BA.debugLineNum = 140;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
public static String  _displayhelp() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _reader = null;
String _txt1 = "";
String _txt2 = "";
String _txt3 = "";
float _height = 0f;
 //BA.debugLineNum = 104;BA.debugLine="Sub DisplayHelp";
 //BA.debugLineNum = 106;BA.debugLine="Dim Reader As TextReader";
_reader = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Dim txt1, txt2, txt3 As String";
_txt1 = "";
_txt2 = "";
_txt3 = "";
 //BA.debugLineNum = 108;BA.debugLine="Dim Height As Float";
_height = 0f;
 //BA.debugLineNum = 110;BA.debugLine="scvHelp.ScrollPosition = 0";
mostCurrent._scvhelp.setScrollPosition((int) (0));
 //BA.debugLineNum = 112;BA.debugLine="Reader.Initialize(File.OpenInput(File.DirAssets,\"spiel\"&HelpIndex&\".txt\"))";
_reader.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"spiel"+BA.NumberToString(_helpindex)+".txt").getObject()));
 //BA.debugLineNum = 113;BA.debugLine="txt1 = Reader.ReadLine";
_txt1 = _reader.ReadLine();
 //BA.debugLineNum = 114;BA.debugLine="txt2 = Reader.ReadLine";
_txt2 = _reader.ReadLine();
 //BA.debugLineNum = 115;BA.debugLine="txt3 = Reader.ReadAll";
_txt3 = _reader.ReadAll();
 //BA.debugLineNum = 116;BA.debugLine="Reader.Close";
_reader.Close();
 //BA.debugLineNum = 117;BA.debugLine="If HelpIndex = HelpIndexMax Then";
if (_helpindex==_helpindexmax) { 
 //BA.debugLineNum = 118;BA.debugLine="txt3 = \"Program:\" & TAB & TAB & ProgName & TAB & TAB & ProgVersion & CRLF";
_txt3 = "Program:"+anywheresoftware.b4a.keywords.Common.TAB+anywheresoftware.b4a.keywords.Common.TAB+_progname+anywheresoftware.b4a.keywords.Common.TAB+anywheresoftware.b4a.keywords.Common.TAB+_progversion+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 119;BA.debugLine="txt3 = txt3 & \"Date:\" & TAB & TAB & TAB & ProgDate & CRLF";
_txt3 = _txt3+"Date:"+anywheresoftware.b4a.keywords.Common.TAB+anywheresoftware.b4a.keywords.Common.TAB+anywheresoftware.b4a.keywords.Common.TAB+_progdate+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 120;BA.debugLine="txt3 = txt3 & \"Geschrieben von:\" & TAB & ProgAuthor & CRLF & CRLF";
_txt3 = _txt3+"Geschrieben von:"+anywheresoftware.b4a.keywords.Common.TAB+_progauthor+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 121;BA.debugLine="txt3 = txt3 & \"MAMA ist mit Hilfe der B4A Gemeinde programmiert worden.\" & CRLF";
_txt3 = _txt3+"MAMA ist mit Hilfe der B4A Gemeinde programmiert worden."+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 122;BA.debugLine="txt3 = txt3 & \"Mein Dank gilt auch: Nicolas Iven.\" & CRLF";
_txt3 = _txt3+"Mein Dank gilt auch: Nicolas Iven."+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 123;BA.debugLine="txt3 = txt3 & \"Für mehr Details besuchen Sie: http://www.watchkido.de\" & CRLF & CRLF";
_txt3 = _txt3+"Für mehr Details besuchen Sie: http://www.watchkido.de"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 124;BA.debugLine="txt3 = txt3 & \"Library adapted for B4Android by Andrew Graham\" & CRLF & CRLF";
_txt3 = _txt3+"Library adapted for B4Android by Andrew Graham"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 };
 //BA.debugLineNum = 128;BA.debugLine="lblHelp1.Text = txt1";
mostCurrent._lblhelp1.setText((Object)(_txt1));
 //BA.debugLineNum = 129;BA.debugLine="lblHelp2.Text = txt2";
mostCurrent._lblhelp2.setText((Object)(_txt2));
 //BA.debugLineNum = 130;BA.debugLine="lblHelp3.Text = txt3";
mostCurrent._lblhelp3.setText((Object)(_txt3));
 //BA.debugLineNum = 132;BA.debugLine="Height = StrUtils.MeasureMultilineTextHeight(lblHelp3, txt3)";
_height = (float) (mostCurrent._strutils.MeasureMultilineTextHeight((android.widget.TextView)(mostCurrent._lblhelp3.getObject()),_txt3));
 //BA.debugLineNum = 133;BA.debugLine="scvHelp.Panel.Height = lblHelp3.Top + Height + 10dip";
mostCurrent._scvhelp.getPanel().setHeight((int) (mostCurrent._lblhelp3.getTop()+_height+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 134;BA.debugLine="lblHelp3.Height = Height";
mostCurrent._lblhelp3.setHeight((int) (_height));
 //BA.debugLineNum = 136;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 20;BA.debugLine="Dim pnlHelp As Panel";
mostCurrent._pnlhelp = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim btnHelpPrev, btnHelpNext, btnHelpLast,btnHelpFirst, btnHelpOK  As Button";
mostCurrent._btnhelpprev = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnhelpnext = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnhelplast = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnhelpfirst = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnhelpok = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim lblHelp1, lblHelp2, lblHelp3 As Label";
mostCurrent._lblhelp1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblhelp2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblhelp3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim scvHelp As ScrollView";
mostCurrent._scvhelp = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim StrUtils As StringUtils";
mostCurrent._strutils = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _inithelp() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub InitHelp";
 //BA.debugLineNum = 57;BA.debugLine="scvHelp.Top = btnHelpOK.Top + btnHelpOK.Height + 5dip";
mostCurrent._scvhelp.setTop((int) (mostCurrent._btnhelpok.getTop()+mostCurrent._btnhelpok.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 58;BA.debugLine="scvHelp.Left = 5dip";
mostCurrent._scvhelp.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 59;BA.debugLine="scvHelp.Height = Activity.Height - scvHelp.Top - 5dip";
mostCurrent._scvhelp.setHeight((int) (mostCurrent._activity.getHeight()-mostCurrent._scvhelp.getTop()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 61;BA.debugLine="scvHelp.Width = Activity.Width - 2 * scvHelp.Left";
mostCurrent._scvhelp.setWidth((int) (mostCurrent._activity.getWidth()-2*mostCurrent._scvhelp.getLeft()));
 //BA.debugLineNum = 62;BA.debugLine="scvHelp.Panel.Width = scvHelp.Width";
mostCurrent._scvhelp.getPanel().setWidth(mostCurrent._scvhelp.getWidth());
 //BA.debugLineNum = 64;BA.debugLine="pnlHelp.Invalidate";
mostCurrent._pnlhelp.Invalidate();
 //BA.debugLineNum = 65;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 67;BA.debugLine="btnHelpOK.Left = Activity.Width - btnHelpOK.Width - 5dip";
mostCurrent._btnhelpok.setLeft((int) (mostCurrent._activity.getWidth()-mostCurrent._btnhelpok.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 69;BA.debugLine="If Activity.Width / Activity.Height < 1 Then";
if (mostCurrent._activity.getWidth()/(double)mostCurrent._activity.getHeight()<1) { 
 //BA.debugLineNum = 70;BA.debugLine="lblHelp1.Left = lblHelp2.Left";
mostCurrent._lblhelp1.setLeft(mostCurrent._lblhelp2.getLeft());
 //BA.debugLineNum = 71;BA.debugLine="lblHelp2.Top = lblHelp1.Top + lblHelp1.Height";
mostCurrent._lblhelp2.setTop((int) (mostCurrent._lblhelp1.getTop()+mostCurrent._lblhelp1.getHeight()));
 //BA.debugLineNum = 72;BA.debugLine="lblHelp1.Gravity = Gravity.LEFT";
mostCurrent._lblhelp1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.LEFT);
 }else {
 //BA.debugLineNum = 74;BA.debugLine="lblHelp1.Gravity = Gravity.RIGHT";
mostCurrent._lblhelp1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.RIGHT);
 //BA.debugLineNum = 75;BA.debugLine="lblHelp1.Left = scvHelp.Width - lblHelp1.Width - 2 * lblHelp2.Left";
mostCurrent._lblhelp1.setLeft((int) (mostCurrent._scvhelp.getWidth()-mostCurrent._lblhelp1.getWidth()-2*mostCurrent._lblhelp2.getLeft()));
 };
 //BA.debugLineNum = 77;BA.debugLine="lblHelp2.Gravity = Gravity.LEFT";
mostCurrent._lblhelp2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.LEFT);
 //BA.debugLineNum = 78;BA.debugLine="lblHelp3.Top = lblHelp2.Top + lblHelp2.Height";
mostCurrent._lblhelp3.setTop((int) (mostCurrent._lblhelp2.getTop()+mostCurrent._lblhelp2.getHeight()));
 //BA.debugLineNum = 79;BA.debugLine="lblHelp3.Left = 10dip";
mostCurrent._lblhelp3.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 80;BA.debugLine="lblHelp3.Width = scvHelp.Panel.Width - 20dip";
mostCurrent._lblhelp3.setWidth((int) (mostCurrent._scvhelp.getPanel().getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))));
 //BA.debugLineNum = 81;BA.debugLine="lblHelp3.Height = -2";
mostCurrent._lblhelp3.setHeight((int) (-2));
 //BA.debugLineNum = 82;BA.debugLine="scvHelp.Invalidate";
mostCurrent._scvhelp.Invalidate();
 //BA.debugLineNum = 84;BA.debugLine="If HelpIndex = 0 Then";
if (_helpindex==0) { 
 //BA.debugLineNum = 85;BA.debugLine="btnHelpPrev.Enabled = False";
mostCurrent._btnhelpprev.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 86;BA.debugLine="btnHelpFirst.Enabled = False";
mostCurrent._btnhelpfirst.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 87;BA.debugLine="btnHelpNext.Enabled = True";
mostCurrent._btnhelpnext.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 88;BA.debugLine="btnHelpLast.Enabled = True";
mostCurrent._btnhelplast.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else if(_helpindex==0) { 
 //BA.debugLineNum = 90;BA.debugLine="btnHelpPrev.Enabled = True";
mostCurrent._btnhelpprev.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 91;BA.debugLine="btnHelpFirst.Enabled = True";
mostCurrent._btnhelpfirst.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 92;BA.debugLine="btnHelpNext.Enabled = False";
mostCurrent._btnhelpnext.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 93;BA.debugLine="btnHelpLast.Enabled = False";
mostCurrent._btnhelplast.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 95;BA.debugLine="btnHelpPrev.Enabled = True";
mostCurrent._btnhelpprev.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 96;BA.debugLine="btnHelpFirst.Enabled = True";
mostCurrent._btnhelpfirst.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 97;BA.debugLine="btnHelpNext.Enabled = True";
mostCurrent._btnhelpnext.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 98;BA.debugLine="btnHelpLast.Enabled = True";
mostCurrent._btnhelplast.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 101;BA.debugLine="DisplayHelp";
_displayhelp();
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim ProgName As String			: ProgName = \"Kampfsport-Spiele-Sammlung\"";
_progname = "";
 //BA.debugLineNum = 9;BA.debugLine="Dim ProgName As String			: ProgName = \"Kampfsport-Spiele-Sammlung\"";
_progname = "Kampfsport-Spiele-Sammlung";
 //BA.debugLineNum = 10;BA.debugLine="Dim ProgVersion As String		: ProgVersion = \"V 1.0\"";
_progversion = "";
 //BA.debugLineNum = 10;BA.debugLine="Dim ProgVersion As String		: ProgVersion = \"V 1.0\"";
_progversion = "V 1.0";
 //BA.debugLineNum = 11;BA.debugLine="Dim ProgAuthor As String		: ProgAuthor = \"Frank Albrecht\"";
_progauthor = "";
 //BA.debugLineNum = 11;BA.debugLine="Dim ProgAuthor As String		: ProgAuthor = \"Frank Albrecht\"";
_progauthor = "Frank Albrecht";
 //BA.debugLineNum = 12;BA.debugLine="Dim ProgDate As String			: ProgDate = \"Aug 2012\"";
_progdate = "";
 //BA.debugLineNum = 12;BA.debugLine="Dim ProgDate As String			: ProgDate = \"Aug 2012\"";
_progdate = "Aug 2012";
 //BA.debugLineNum = 13;BA.debugLine="Dim HelpIndex As Int			: HelpIndex = 0";
_helpindex = 0;
 //BA.debugLineNum = 13;BA.debugLine="Dim HelpIndex As Int			: HelpIndex = 0";
_helpindex = (int) (0);
 //BA.debugLineNum = 14;BA.debugLine="Dim HelpIndexMax As Int			: HelpIndexMax = 22";
_helpindexmax = 0;
 //BA.debugLineNum = 14;BA.debugLine="Dim HelpIndexMax As Int			: HelpIndexMax = 22";
_helpindexmax = (int) (22);
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
}
