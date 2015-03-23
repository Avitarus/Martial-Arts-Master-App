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

public class facebook extends Activity implements B4AActivity{
	public static facebook mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.facebook");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (facebook).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.facebook");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.facebook", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (facebook) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (facebook) Resume **");
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
		return facebook.class;
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
        BA.LogInfo("** Activity (facebook) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (facebook) Resume **");
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
public static String _appid = "";
public static String _appsecret = "";
public static String _redirecturi = "";
public static String _accesstoken = "";
public static String _datarequestlink = "";
public static anywheresoftware.b4a.http.HttpClientWrapper _hc = null;
public static anywheresoftware.b4a.http.HttpClientWrapper.HttpUriRequestWrapper _req = null;
public anywheresoftware.b4a.objects.PanelWrapper _mainpanel = null;
public anywheresoftware.b4a.objects.WebViewWrapper _facebookpage = null;
public anywheresoftware.b4a.objects.PanelWrapper _camerapanel = null;
public anywheresoftware.b4a.objects.PanelWrapper _controlspanel = null;
public anywheresoftware.b4a.objects.ButtonWrapper _takepicturebutton = null;
public anywheresoftware.b4a.objects.ButtonWrapper _closebutton = null;
public xvs.ACL.ACL _cam = null;
public anywheresoftware.b4a.objects.PanelWrapper _fakepanel = null;
public anywheresoftware.b4a.objects.EditTextWrapper _descriptionbox = null;
public anywheresoftware.b4a.objects.ButtonWrapper _sendbutton = null;
public anywheresoftware.b4a.objects.ButtonWrapper _cancelbutton = null;
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
 //BA.debugLineNum = 52;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 55;BA.debugLine="DataRequestLink = \"https://graph.facebook.com/me/photos\"";
_datarequestlink = "https://graph.facebook.com/me/photos";
 //BA.debugLineNum = 57;BA.debugLine="appID = \"\"";
_appid = "";
 //BA.debugLineNum = 58;BA.debugLine="appSecret = \"\"";
_appsecret = "";
 //BA.debugLineNum = 59;BA.debugLine="RedirectUri = \"https://sites.google.com/site/martialartsmasterapp/\" 'e.g. http://www.mywebsite.com/  <-- Add slash at the end";
_redirecturi = "https://sites.google.com/site/martialartsmasterapp/";
 //BA.debugLineNum = 61;BA.debugLine="hc.Initialize(\"hc\")";
_hc.Initialize("hc");
 //BA.debugLineNum = 63;BA.debugLine="Activity.LoadLayout(\"Facebook\")";
mostCurrent._activity.LoadLayout("Facebook",mostCurrent.activityBA);
 //BA.debugLineNum = 65;BA.debugLine="ProgressDialogShow(\"Activating camera...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Activating camera...");
 //BA.debugLineNum = 67;BA.debugLine="MainPanel.Left = (100%x - MainPanel.Width) / 2";
mostCurrent._mainpanel.setLeft((int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._mainpanel.getWidth())/(double)2));
 //BA.debugLineNum = 69;BA.debugLine="Cam.Initialize(CameraPanel, \"Cam\")";
mostCurrent._cam.Initialize(mostCurrent.activityBA,(android.view.ViewGroup)(mostCurrent._camerapanel.getObject()),"Cam");
 //BA.debugLineNum = 71;BA.debugLine="Cam.StopPreview";
mostCurrent._cam.StopPreview();
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 79;BA.debugLine="Sub Activity_Pause(UserClosed As Boolean)";
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 75;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 77;BA.debugLine="End Sub";
return "";
}
public static String  _cam_picturetaken(byte[] _data) throws Exception{
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
 //BA.debugLineNum = 195;BA.debugLine="Sub Cam_PictureTaken(Data() As Byte)";
 //BA.debugLineNum = 197;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 199;BA.debugLine="out = File.OpenOutput(File.DirRootExternal, \"SnapShot.png\", False)";
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"SnapShot.png",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 200;BA.debugLine="out.WriteBytes(Data, 0, Data.Length)";
_out.WriteBytes(_data,(int) (0),_data.length);
 //BA.debugLineNum = 201;BA.debugLine="out.Close";
_out.Close();
 //BA.debugLineNum = 203;BA.debugLine="FakePanel.Visible = True";
mostCurrent._fakepanel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public static String  _cam_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub Cam_Ready(Success As Boolean)";
 //BA.debugLineNum = 179;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 181;BA.debugLine="Cam.StartPreview";
mostCurrent._cam.StartPreview();
 //BA.debugLineNum = 183;BA.debugLine="TakePictureButton.Enabled = True";
mostCurrent._takepicturebutton.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 187;BA.debugLine="ToastMessageShow(\"Cannot open camera.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Cannot open camera.",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 191;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 193;BA.debugLine="End Sub";
return "";
}
public static boolean  _camerapanel_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 87;BA.debugLine="Sub CameraPanel_Touch(Action As Int, X As Float, Y As Float) As Boolean 'Return True to consume the event";
 //BA.debugLineNum = 89;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 91;BA.debugLine="End Sub";
return false;
}
public static String  _cancelbutton_click() throws Exception{
 //BA.debugLineNum = 138;BA.debugLine="Sub CancelButton_Click";
 //BA.debugLineNum = 140;BA.debugLine="Cam.StartPreview";
mostCurrent._cam.StartPreview();
 //BA.debugLineNum = 142;BA.debugLine="FakePanel.Visible = False";
mostCurrent._fakepanel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 144;BA.debugLine="TakePictureButton.Enabled = True";
mostCurrent._takepicturebutton.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="End Sub";
return "";
}
public static String  _closebutton_click() throws Exception{
 //BA.debugLineNum = 107;BA.debugLine="Sub CloseButton_Click";
 //BA.debugLineNum = 109;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
public static boolean  _controlspanel_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 93;BA.debugLine="Sub ControlsPanel_Touch(Action As Int, X As Float, Y As Float) As Boolean 'Return True to consume the event";
 //BA.debugLineNum = 95;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return false;
}
public static String  _facebookpage_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 148;BA.debugLine="Sub FacebookPage_PageFinished(Url As String)";
 //BA.debugLineNum = 150;BA.debugLine="FacebookPage.Visible = False";
mostCurrent._facebookpage.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 152;BA.debugLine="If Url.StartsWith(\"http://m.facebook.com/login.php\") Then";
if (_url.startsWith("http://m.facebook.com/login.php")) { 
 //BA.debugLineNum = 154;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 156;BA.debugLine="FacebookPage.Visible = True";
mostCurrent._facebookpage.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 160;BA.debugLine="If Url.StartsWith(RedirectUri & \"#access_token=\") Then";
if (_url.startsWith(_redirecturi+"#access_token=")) { 
 //BA.debugLineNum = 162;BA.debugLine="FacebookPage.Visible = False";
mostCurrent._facebookpage.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 164;BA.debugLine="AccessToken = Url.SubString((RedirectUri & \"#access_token=\").Length)";
_accesstoken = _url.substring((_redirecturi+"#access_token=").length());
 //BA.debugLineNum = 165;BA.debugLine="AccessToken = AccessToken.SubString2(0, AccessToken.IndexOf(\"&\"))";
_accesstoken = _accesstoken.substring((int) (0),_accesstoken.indexOf("&"));
 //BA.debugLineNum = 167;BA.debugLine="GetFeeds";
_getfeeds();
 };
 //BA.debugLineNum = 171;BA.debugLine="End Sub";
return "";
}
public static boolean  _fakepanel_touch(int _action,float _x,float _y) throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub FakePanel_Touch(Action As Int, X As Float, Y As Float) As Boolean 'Return True to consume the event";
 //BA.debugLineNum = 115;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return false;
}
public static String  _getfeeds() throws Exception{
anywheresoftware.b4a.objects.collections.List _files = null;
de.watchkido.mama.multipartpost._filedata _fd = null;
anywheresoftware.b4a.objects.collections.Map _nv = null;
 //BA.debugLineNum = 207;BA.debugLine="Sub GetFeeds";
 //BA.debugLineNum = 210;BA.debugLine="Dim files As List";
_files = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 212;BA.debugLine="files.Initialize";
_files.Initialize();
 //BA.debugLineNum = 214;BA.debugLine="Dim fd As FileData";
_fd = new de.watchkido.mama.multipartpost._filedata();
 //BA.debugLineNum = 216;BA.debugLine="fd.Initialize";
_fd.Initialize();
 //BA.debugLineNum = 217;BA.debugLine="fd.Dir = File.DirRootExternal";
_fd.Dir = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal();
 //BA.debugLineNum = 219;BA.debugLine="fd.FileName	= \"SnapShot.png\"";
_fd.FileName = "SnapShot.png";
 //BA.debugLineNum = 220;BA.debugLine="fd.KeyName = \"upfile\"";
_fd.KeyName = "upfile";
 //BA.debugLineNum = 221;BA.debugLine="fd.ContentType = \"application/octet-stream\"";
_fd.ContentType = "application/octet-stream";
 //BA.debugLineNum = 222;BA.debugLine="files.Add(fd)";
_files.Add((Object)(_fd));
 //BA.debugLineNum = 225;BA.debugLine="Dim nv As Map";
_nv = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 227;BA.debugLine="nv.Initialize";
_nv.Initialize();
 //BA.debugLineNum = 228;BA.debugLine="nv.Put(\"message\", DescriptionBox.Text.Trim)";
_nv.Put((Object)("message"),(Object)(mostCurrent._descriptionbox.getText().trim()));
 //BA.debugLineNum = 230;BA.debugLine="ProgressDialogShow(\"Uploading to Facebook...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Uploading to Facebook...");
 //BA.debugLineNum = 232;BA.debugLine="req = MultipartPost.CreatePostRequest(DataRequestLink & \"?access_token=\" & AccessToken, nv, files)";
_req = mostCurrent._multipartpost._createpostrequest(mostCurrent.activityBA,_datarequestlink+"?access_token="+_accesstoken,_nv,_files);
 //BA.debugLineNum = 233;BA.debugLine="hc.Execute(req, 1)";
_hc.Execute(processBA,_req,(int) (1));
 //BA.debugLineNum = 235;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 36;BA.debugLine="Dim MainPanel As Panel";
mostCurrent._mainpanel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim FacebookPage As WebView";
mostCurrent._facebookpage = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim CameraPanel As Panel";
mostCurrent._camerapanel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim ControlsPanel As Panel";
mostCurrent._controlspanel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim TakePictureButton As Button";
mostCurrent._takepicturebutton = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim CloseButton As Button";
mostCurrent._closebutton = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Dim Cam As AdvancedCamera";
mostCurrent._cam = new xvs.ACL.ACL();
 //BA.debugLineNum = 45;BA.debugLine="Dim FakePanel As Panel";
mostCurrent._fakepanel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Dim DescriptionBox As EditText";
mostCurrent._descriptionbox = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Dim SendButton As Button";
mostCurrent._sendbutton = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Dim CancelButton As Button";
mostCurrent._cancelbutton = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _hc_responseerror(anywheresoftware.b4a.http.HttpClientWrapper.HttpResponeWrapper _response,String _reason,int _statuscode,int _taskid) throws Exception{
 //BA.debugLineNum = 251;BA.debugLine="Sub hc_ResponseError(Response As HttpResponse, Reason As String, StatusCode As Int, TaskId As Int)";
 //BA.debugLineNum = 253;BA.debugLine="If Response <> Null Then";
if (_response!= null) { 
 //BA.debugLineNum = 255;BA.debugLine="Msgbox(\"Error: \" & Response.GetString(\"UTF8\") & \" - \" & Reason, \"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: "+_response.GetString("UTF8")+" - "+_reason,"Error",mostCurrent.activityBA);
 //BA.debugLineNum = 257;BA.debugLine="Response.Release";
_response.Release();
 };
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return "";
}
public static String  _hc_responsesuccess(anywheresoftware.b4a.http.HttpClientWrapper.HttpResponeWrapper _response,int _taskid) throws Exception{
 //BA.debugLineNum = 237;BA.debugLine="Sub hc_ResponseSuccess(Response As HttpResponse, TaskId As Int)";
 //BA.debugLineNum = 239;BA.debugLine="Response.Release";
_response.Release();
 //BA.debugLineNum = 241;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 243;BA.debugLine="ToastMessageShow(\"Photo uploaded to Facebook.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Photo uploaded to Facebook.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 245;BA.debugLine="FakePanel.Visible = False";
mostCurrent._fakepanel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 247;BA.debugLine="CancelButton_Click";
_cancelbutton_click();
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim appID As String";
_appid = "";
 //BA.debugLineNum = 18;BA.debugLine="Dim appSecret As String";
_appsecret = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim RedirectUri As String";
_redirecturi = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim AccessToken As String";
_accesstoken = "";
 //BA.debugLineNum = 24;BA.debugLine="Dim DataRequestLink As String";
_datarequestlink = "";
 //BA.debugLineNum = 26;BA.debugLine="Dim hc As HttpClient";
_hc = new anywheresoftware.b4a.http.HttpClientWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim req As HttpRequest";
_req = new anywheresoftware.b4a.http.HttpClientWrapper.HttpUriRequestWrapper();
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _sendbutton_click() throws Exception{
String _scope = "";
 //BA.debugLineNum = 119;BA.debugLine="Sub SendButton_Click";
 //BA.debugLineNum = 121;BA.debugLine="If AccessToken = \"\" Then";
if ((_accesstoken).equals("")) { 
 //BA.debugLineNum = 123;BA.debugLine="Dim scope As String";
_scope = "";
 //BA.debugLineNum = 126;BA.debugLine="scope = \"publish_stream\"";
_scope = "publish_stream";
 //BA.debugLineNum = 128;BA.debugLine="FacebookPage.LoadUrl(\"https://m.facebook.com/dialog/oauth?client_id=\" & appID & \"&redirect_uri=\" & RedirectUri & \"&scope=\" & scope & \"&response_type=token\")";
mostCurrent._facebookpage.LoadUrl("https://m.facebook.com/dialog/oauth?client_id="+_appid+"&redirect_uri="+_redirecturi+"&scope="+_scope+"&response_type=token");
 }else {
 //BA.debugLineNum = 132;BA.debugLine="GetFeeds";
_getfeeds();
 };
 //BA.debugLineNum = 136;BA.debugLine="End Sub";
return "";
}
public static String  _takepicturebutton_click() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub TakePictureButton_Click";
 //BA.debugLineNum = 101;BA.debugLine="TakePictureButton.Enabled = False";
mostCurrent._takepicturebutton.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 103;BA.debugLine="Cam.TakePicture";
mostCurrent._cam.TakePicture();
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
}
