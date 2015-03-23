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

public class erfolgsmeldung extends Activity implements B4AActivity{
	public static erfolgsmeldung mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "de.watchkido.mama", "de.watchkido.mama.erfolgsmeldung");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (erfolgsmeldung).");
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
		activityBA = new BA(this, layout, processBA, "de.watchkido.mama", "de.watchkido.mama.erfolgsmeldung");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.shellMode) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.erfolgsmeldung", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (erfolgsmeldung) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (erfolgsmeldung) Resume **");
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
		return erfolgsmeldung.class;
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
        BA.LogInfo("** Activity (erfolgsmeldung) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (erfolgsmeldung) Resume **");
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
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceManager _manager = null;
public static anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _bildschirm1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public static String _aktuellerunterordner = "";
public xvs.ACL.ACL _camera1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
public de.watchkido.mama.startabfragen _startabfragen = null;
public de.watchkido.mama.einstellungenapp _einstellungenapp = null;
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
String _txt0 = "";
String _txt1 = "";
 //BA.debugLineNum = 33;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 35;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 36;BA.debugLine="CreatePreferenceScreen";
_createpreferencescreen();
 //BA.debugLineNum = 37;BA.debugLine="If manager.GetAll.Size = 0 Then SetDefaults";
if (_manager.GetAll().getSize()==0) { 
_setdefaults();};
 };
 //BA.debugLineNum = 39;BA.debugLine="Activity.LoadLayout(\"Erfolgsmeldung\")";
mostCurrent._activity.LoadLayout("Erfolgsmeldung",mostCurrent.activityBA);
 //BA.debugLineNum = 41;BA.debugLine="Button1.Text = Main.Vorname & \" ´\" & Main.Spitzname  & \"´ \" & Main.Nachname";
mostCurrent._button1.setText((Object)(mostCurrent._main._vorname+" ´"+mostCurrent._main._spitzname+"´ "+mostCurrent._main._nachname));
 //BA.debugLineNum = 42;BA.debugLine="Button1.TextSize = 20";
mostCurrent._button1.setTextSize((float) (20));
 //BA.debugLineNum = 47;BA.debugLine="Dim txt0,txt1 As String";
_txt0 = "";
_txt1 = "";
 //BA.debugLineNum = 49;BA.debugLine="txt0 = \" Spitzname: \"& CRLF & \"  \" & Main.Spitzname & CRLF";
_txt0 = " Spitzname: "+anywheresoftware.b4a.keywords.Common.CRLF+"  "+mostCurrent._main._spitzname+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 50;BA.debugLine="txt0 = txt0 &\" Heimatort: \" & CRLF& \"  \" & Main.Heimatort & CRLF";
_txt0 = _txt0+" Heimatort: "+anywheresoftware.b4a.keywords.Common.CRLF+"  "+mostCurrent._main._heimatort+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 51;BA.debugLine="txt0 = txt0 &\" Alter: \" & Main.Alter & CRLF";
_txt0 = _txt0+" Alter: "+BA.NumberToString(mostCurrent._main._alter)+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 52;BA.debugLine="txt0 = txt0 &\"Altersklasse: \"& CRLF & \"  \" & Main.Altersklasse & CRLF";
_txt0 = _txt0+"Altersklasse: "+anywheresoftware.b4a.keywords.Common.CRLF+"  "+mostCurrent._main._altersklasse+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 53;BA.debugLine="txt0 = txt0 &\" Größe: \" & Main.Groeße& CRLF";
_txt0 = _txt0+" Größe: "+BA.NumberToString(mostCurrent._main._groeße)+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 54;BA.debugLine="txt0 = txt0 &\" Gewicht: \" & Main.Gewicht & CRLF";
_txt0 = _txt0+" Gewicht: "+BA.NumberToString(mostCurrent._main._gewicht)+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 55;BA.debugLine="txt0 = txt0 &\" Gewichtsklasse: \"& CRLF & \"  \" & Main.Gewichtsklasse & CRLF";
_txt0 = _txt0+" Gewichtsklasse: "+anywheresoftware.b4a.keywords.Common.CRLF+"  "+mostCurrent._main._gewichtsklasse+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 58;BA.debugLine="txt0 = txt0 &\" Sportart: \"& CRLF & \"  \" & Main.Sportart & CRLF";
_txt0 = _txt0+" Sportart: "+anywheresoftware.b4a.keywords.Common.CRLF+"  "+mostCurrent._main._sportart+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 59;BA.debugLine="txt0 = txt0 &\" Verein: \"& CRLF & \"  \" & Main.Verein& CRLF";
_txt0 = _txt0+" Verein: "+anywheresoftware.b4a.keywords.Common.CRLF+"  "+mostCurrent._main._verein+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 61;BA.debugLine="Label1.Text = txt0";
mostCurrent._label1.setText((Object)(_txt0));
 //BA.debugLineNum = 62;BA.debugLine="Label1.TextSize = 14";
mostCurrent._label1.setTextSize((float) (14));
 //BA.debugLineNum = 63;BA.debugLine="Label1.TextColor = Colors.Gray";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 67;BA.debugLine="Label2.Text = \"Bruce Lee:\" & CRLF & \">>Ich fürchte, nicht den Mann, der einmal 10.000 Tritte geübt hat, aber ich fürchte den Mann, der einen Kick 10.000 mal praktiziert hat.<<\"";
mostCurrent._label2.setText((Object)("Bruce Lee:"+anywheresoftware.b4a.keywords.Common.CRLF+">>Ich fürchte, nicht den Mann, der einmal 10.000 Tritte geübt hat, aber ich fürchte den Mann, der einen Kick 10.000 mal praktiziert hat.<<"));
 //BA.debugLineNum = 68;BA.debugLine="Label2.TextSize = 14";
mostCurrent._label2.setTextSize((float) (14));
 //BA.debugLineNum = 69;BA.debugLine="Label2.TextColor = Colors.Gray";
mostCurrent._label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 73;BA.debugLine="ImageView1.Bitmap = LoadBitmap(File.DirAssets, \"ErfolgFoto.jpg\")";
mostCurrent._imageview1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ErfolgFoto.jpg").getObject()));
 //BA.debugLineNum = 75;BA.debugLine="ImageView2.Initialize(\"imageview2\")";
mostCurrent._imageview2.Initialize(mostCurrent.activityBA,"imageview2");
 //BA.debugLineNum = 76;BA.debugLine="ImageView2.Bitmap = LoadBitmap(File.DirAssets,\"Kampfkunstmeisterlogo150x212.jpg\")";
mostCurrent._imageview2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Kampfkunstmeisterlogo150x212.jpg").getObject()));
 //BA.debugLineNum = 82;BA.debugLine="Label3.Visible = False";
mostCurrent._label3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 83;BA.debugLine="ProgressBar1.Visible = False";
mostCurrent._progressbar1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 331;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 333;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 257;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 258;BA.debugLine="HandleSettings";
_handlesettings();
 //BA.debugLineNum = 259;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 108;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 109;BA.debugLine="StartActivity(Bildschirm1.CreateIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_bildschirm1.CreateIntent()));
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _obj1 = null;
anywheresoftware.b4a.agraham.reflection.Reflection _obj2 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper _c = null;
long _now = 0L;
long _i = 0L;
String _dt = "";
Object[] _args = null;
String[] _types = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
 //BA.debugLineNum = 349;BA.debugLine="Sub Button2_Click '(Activity As Activity)";
 //BA.debugLineNum = 352;BA.debugLine="Dim Obj1, Obj2 As Reflector";
_obj1 = new anywheresoftware.b4a.agraham.reflection.Reflection();
_obj2 = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 353;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 354;BA.debugLine="Dim c As Canvas";
_c = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 355;BA.debugLine="Dim now, i As Long";
_now = 0L;
_i = 0L;
 //BA.debugLineNum = 356;BA.debugLine="Dim dt As String";
_dt = "";
 //BA.debugLineNum = 357;BA.debugLine="DateTime.DateFormat =\"yy-MM-dd-HH-mm-ss\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yy-MM-dd-HH-mm-ss");
 //BA.debugLineNum = 358;BA.debugLine="now = DateTime.now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 359;BA.debugLine="dt = \"Erfolg-\" & DateTime.Date(now) ' e.g.: \"110812150355\" is Aug.12, 2011, 3:03:55 p.m.";
_dt = "Erfolg-"+anywheresoftware.b4a.keywords.Common.DateTime.Date(_now);
 //BA.debugLineNum = 360;BA.debugLine="Obj1.Target = Obj1.GetActivityBA";
_obj1.Target = (Object)(_obj1.GetActivityBA(processBA));
 //BA.debugLineNum = 361;BA.debugLine="Obj1.Target = Obj1.GetField(\"vg\")";
_obj1.Target = _obj1.GetField("vg");
 //BA.debugLineNum = 362;BA.debugLine="bmp.InitializeMutable(Activity.Width, Activity.Height)";
_bmp.InitializeMutable(mostCurrent._activity.getWidth(),mostCurrent._activity.getHeight());
 //BA.debugLineNum = 363;BA.debugLine="c.Initialize2(bmp)";
_c.Initialize2((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 364;BA.debugLine="Dim args(1) As Object";
_args = new Object[(int) (1)];
{
int d0 = _args.length;
for (int i0 = 0;i0 < d0;i0++) {
_args[i0] = new Object();
}
}
;
 //BA.debugLineNum = 365;BA.debugLine="Dim types(1) As String";
_types = new String[(int) (1)];
java.util.Arrays.fill(_types,"");
 //BA.debugLineNum = 366;BA.debugLine="Obj2.Target = c";
_obj2.Target = (Object)(_c);
 //BA.debugLineNum = 367;BA.debugLine="Obj2.Target = Obj2.GetField(\"canvas\")";
_obj2.Target = _obj2.GetField("canvas");
 //BA.debugLineNum = 368;BA.debugLine="args(0) = Obj2.Target";
_args[(int) (0)] = _obj2.Target;
 //BA.debugLineNum = 369;BA.debugLine="types(0) = \"android.graphics.Canvas\"";
_types[(int) (0)] = "android.graphics.Canvas";
 //BA.debugLineNum = 370;BA.debugLine="Obj1.RunMethod4(\"draw\", args, types)";
_obj1.RunMethod4("draw",_args,_types);
 //BA.debugLineNum = 371;BA.debugLine="Dim Out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 372;BA.debugLine="Out = File.OpenOutput(File.DirRootExternal,AktuellerUnterordner & dt & \".png\", False)";
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._aktuellerunterordner+_dt+".png",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 373;BA.debugLine="bmp.WriteToStream(Out, 100, \"PNG\")";
_bmp.WriteToStream((java.io.OutputStream)(_out.getObject()),(int) (100),BA.getEnumFromString(android.graphics.Bitmap.CompressFormat.class,"PNG"));
 //BA.debugLineNum = 374;BA.debugLine="Out.Close";
_out.Close();
 //BA.debugLineNum = 375;BA.debugLine="Msgbox(\"Mit den Netzwerken:   \"& \"und gespeichert als: \" & File.DirRootExternal & AktuellerUnterordner & CRLF & dt & \".png\", \"Geteilt\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Mit den Netzwerken:   "+"und gespeichert als: "+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._aktuellerunterordner+anywheresoftware.b4a.keywords.Common.CRLF+_dt+".png","Geteilt",mostCurrent.activityBA);
 //BA.debugLineNum = 377;BA.debugLine="End Sub";
return "";
}
public static String  _createpreferencescreen() throws Exception{
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _kategorie1 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _kategorie2 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _kategorie3 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper _kategorie4 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _untermenu1 = null;
anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper _untermenu2 = null;
anywheresoftware.b4a.phone.Phone.PhoneIntents _pi = null;
anywheresoftware.b4a.objects.collections.Map _m = null;
anywheresoftware.b4a.objects.collections.Map _s = null;
 //BA.debugLineNum = 113;BA.debugLine="Sub CreatePreferenceScreen";
 //BA.debugLineNum = 114;BA.debugLine="Bildschirm1.Initialize(\"Einstellunen\", \"\")";
_bildschirm1.Initialize("Einstellunen","");
 //BA.debugLineNum = 116;BA.debugLine="Dim Kategorie1, Kategorie2, Kategorie3, Kategorie4 As AHPreferenceCategory";
_kategorie1 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_kategorie2 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_kategorie3 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
_kategorie4 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceCategoryWrapper();
 //BA.debugLineNum = 117;BA.debugLine="Dim Untermenu1, Untermenu2 As AHPreferenceScreen";
_untermenu1 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
_untermenu2 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 118;BA.debugLine="Dim pi As PhoneIntents";
_pi = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 121;BA.debugLine="Kategorie1.Initialize(\"Woher bekomme ich meine Zugangsdaten?\")";
_kategorie1.Initialize("Woher bekomme ich meine Zugangsdaten?");
 //BA.debugLineNum = 122;BA.debugLine="Kategorie1.AddCheckBox(\"OnOff1\", \"Informationen\", \"Zugangsinfos EIN\", \"Zugangsinfos AUS\", True, \"\")";
_kategorie1.AddCheckBox("OnOff1","Informationen","Zugangsinfos EIN","Zugangsinfos AUS",anywheresoftware.b4a.keywords.Common.True,"");
 //BA.debugLineNum = 123;BA.debugLine="Untermenu1.Initialize(\"HELP-Seiten der Anbieter\", \"Wie bekomme ich Zugang\")";
_untermenu1.Initialize("HELP-Seiten der Anbieter","Wie bekomme ich Zugang");
 //BA.debugLineNum = 125;BA.debugLine="Untermenu1.AddIntent(\"Blogger\", \"Öffne Blogger.com\", pi.OpenBrowser(\"http://www.blogger.com/home\"), \"OnOff1\")";
_untermenu1.AddIntent("Blogger","Öffne Blogger.com",_pi.OpenBrowser("http://www.blogger.com/home"),"OnOff1");
 //BA.debugLineNum = 126;BA.debugLine="Untermenu1.AddIntent(\"Blogg\", \"Öffne Blogg.de\", pi.OpenBrowser(\"http://www.blogg.de\"), \"OnOff1\")";
_untermenu1.AddIntent("Blogg","Öffne Blogg.de",_pi.OpenBrowser("http://www.blogg.de"),"OnOff1");
 //BA.debugLineNum = 127;BA.debugLine="Untermenu1.AddIntent(\"Facebook\", \"Öffne Facebook.com\", pi.OpenBrowser(\"http://www.facebook.com/help/?faq=210153612350847\"), \"OnOff1\")";
_untermenu1.AddIntent("Facebook","Öffne Facebook.com",_pi.OpenBrowser("http://www.facebook.com/help/?faq=210153612350847"),"OnOff1");
 //BA.debugLineNum = 128;BA.debugLine="Untermenu1.AddIntent(\"Google+\", \"Öffne Google+\", pi.OpenBrowser(\"m.google.de\"), \"OnOff1\")";
_untermenu1.AddIntent("Google+","Öffne Google+",_pi.OpenBrowser("m.google.de"),"OnOff1");
 //BA.debugLineNum = 129;BA.debugLine="Untermenu1.AddIntent(\"LinkedIN\", \"Öffne Linkedin.com\", pi.OpenBrowser(\"http://www.linkedin.com/\"), \"OnOff1\")";
_untermenu1.AddIntent("LinkedIN","Öffne Linkedin.com",_pi.OpenBrowser("http://www.linkedin.com/"),"OnOff1");
 //BA.debugLineNum = 130;BA.debugLine="Untermenu1.AddIntent(\"Lokalisten\", \"Öffne Lokalisten.de\", pi.OpenBrowser(\"mobile.lokalisten.de\"), \"OnOff1\")";
_untermenu1.AddIntent("Lokalisten","Öffne Lokalisten.de",_pi.OpenBrowser("mobile.lokalisten.de"),"OnOff1");
 //BA.debugLineNum = 131;BA.debugLine="Untermenu1.AddIntent(\"Tumblr\", \"Öffne Tumblr.com\", pi.OpenBrowser(\"http://www.tumblr.com/\"), \"OnOff1\")";
_untermenu1.AddIntent("Tumblr","Öffne Tumblr.com",_pi.OpenBrowser("http://www.tumblr.com/"),"OnOff1");
 //BA.debugLineNum = 132;BA.debugLine="Untermenu1.AddIntent(\"Twitter\", \"Öffne Twittermail.com\", pi.OpenBrowser(\"http://twittercounter.com/pages/twittermail/\"), \"OnOff1\")";
_untermenu1.AddIntent("Twitter","Öffne Twittermail.com",_pi.OpenBrowser("http://twittercounter.com/pages/twittermail/"),"OnOff1");
 //BA.debugLineNum = 133;BA.debugLine="Untermenu1.AddIntent(\"Wer-Kennt-Wen\", \"Öffnet Wer-Kennt-Wen.de\", pi.OpenBrowser(\"http://www.wer-kennt-wen.de/hilfe/\"), \"OnOff1\")";
_untermenu1.AddIntent("Wer-Kennt-Wen","Öffnet Wer-Kennt-Wen.de",_pi.OpenBrowser("http://www.wer-kennt-wen.de/hilfe/"),"OnOff1");
 //BA.debugLineNum = 134;BA.debugLine="Untermenu1.AddIntent(\"Yahoo!\", \"Öffne Yahoo.com\", pi.OpenBrowser(\"http://www.yahoo.com\"), \"OnOff1\")";
_untermenu1.AddIntent("Yahoo!","Öffne Yahoo.com",_pi.OpenBrowser("http://www.yahoo.com"),"OnOff1");
 //BA.debugLineNum = 136;BA.debugLine="Kategorie1.AddPreferenceScreen(Untermenu1, \"OnOff1\")";
_kategorie1.AddPreferenceScreen(_untermenu1,"OnOff1");
 //BA.debugLineNum = 142;BA.debugLine="Kategorie2.Initialize(\"Soziale Netzwerke\")";
_kategorie2.Initialize("Soziale Netzwerke");
 //BA.debugLineNum = 143;BA.debugLine="Kategorie2.AddCheckBox(\"OnOff2\", \"Soziale Netzwerke\", \"Zugang EIN\", \"Zugang AUS\", False, \"\")";
_kategorie2.AddCheckBox("OnOff2","Soziale Netzwerke","Zugang EIN","Zugang AUS",anywheresoftware.b4a.keywords.Common.False,"");
 //BA.debugLineNum = 144;BA.debugLine="Untermenu2.Initialize(\"Webseiten der Anbieter\", \"Hier Zugangsdaten eintragen\")";
_untermenu2.Initialize("Webseiten der Anbieter","Hier Zugangsdaten eintragen");
 //BA.debugLineNum = 146;BA.debugLine="Untermenu2.AddCheckBox(\"Blogger\", \"Blogger\", \"Erfolgsmeldung wird geteil\", \"Erfolgsmeldung wird NICHT geteilt\", False, \"OnOff2\")";
_untermenu2.AddCheckBox("Blogger","Blogger","Erfolgsmeldung wird geteil","Erfolgsmeldung wird NICHT geteilt",anywheresoftware.b4a.keywords.Common.False,"OnOff2");
 //BA.debugLineNum = 147;BA.debugLine="Untermenu2.AddEditText(\"BloggerEmail\", \"Blogger Email\", \"Hier private Blogger eMail-Adresse eintragen\", \"BloggerEmail\", \"Blogger\")'hier wird mit chek ausgeschaltet";
_untermenu2.AddEditText("BloggerEmail","Blogger Email","Hier private Blogger eMail-Adresse eintragen","BloggerEmail","Blogger");
 //BA.debugLineNum = 149;BA.debugLine="Untermenu2.AddCheckBox(\"Facebook1\", \"Facebook\", \"Erfolgsmeldung wird geteil\", \"Erfolgsmeldung wird NICHT geteilt\", False, \"OnOff2\")";
_untermenu2.AddCheckBox("Facebook1","Facebook","Erfolgsmeldung wird geteil","Erfolgsmeldung wird NICHT geteilt",anywheresoftware.b4a.keywords.Common.False,"OnOff2");
 //BA.debugLineNum = 150;BA.debugLine="Untermenu2.AddEditText(\"FacebookEmail\", \"Facebook Email\", \"Hier private Facebook eMail-Adresse eintragen\", \"FacebookEmail\", \"Facebook1\")'hier wird mit chek ausgeschaltet";
_untermenu2.AddEditText("FacebookEmail","Facebook Email","Hier private Facebook eMail-Adresse eintragen","FacebookEmail","Facebook1");
 //BA.debugLineNum = 152;BA.debugLine="Untermenu2.AddCheckBox(\"MAMA-Programmierer\", \"MAMA-Programmierer\", \"Erfolgsmeldung wird geteil\", \"Erfolgsmeldung wird NICHT geteilt\", False, \"OnOff2\")";
_untermenu2.AddCheckBox("MAMA-Programmierer","MAMA-Programmierer","Erfolgsmeldung wird geteil","Erfolgsmeldung wird NICHT geteilt",anywheresoftware.b4a.keywords.Common.False,"OnOff2");
 //BA.debugLineNum = 153;BA.debugLine="Untermenu2.AddEditText(\"MAMA-P-Email\", \"MAMA-Programmierer Email\", \"Hier private MAMA-Programmierer eMail-Adresse eintragen\", \"MAMAEmail\", \"MAMA-Programmierer\")'hier wird mit chek ausgeschaltet";
_untermenu2.AddEditText("MAMA-P-Email","MAMA-Programmierer Email","Hier private MAMA-Programmierer eMail-Adresse eintragen","MAMAEmail","MAMA-Programmierer");
 //BA.debugLineNum = 155;BA.debugLine="Untermenu2.AddCheckBox(\"StumbleUpon\", \"StumbleUpon\", \"Erfolgsmeldung wird geteil\", \"Erfolgsmeldung wird NICHT geteilt\", False, \"OnOff2\")";
_untermenu2.AddCheckBox("StumbleUpon","StumbleUpon","Erfolgsmeldung wird geteil","Erfolgsmeldung wird NICHT geteilt",anywheresoftware.b4a.keywords.Common.False,"OnOff2");
 //BA.debugLineNum = 156;BA.debugLine="Untermenu2.AddEditText(\"StumbleUponEmail\", \"StumbleUpon Email\", \"Hier private StumbleUpon eMail-Adresse eintragen\", \"StumleUponEmail\", \"StumbleUpon\")'hier wird mit chek ausgeschaltet";
_untermenu2.AddEditText("StumbleUponEmail","StumbleUpon Email","Hier private StumbleUpon eMail-Adresse eintragen","StumleUponEmail","StumbleUpon");
 //BA.debugLineNum = 158;BA.debugLine="Untermenu2.AddCheckBox(\"Tumblr\", \"Tumblr\", \"Erfolgsmeldung wird geteil\", \"Erfolgsmeldung wird NICHT geteilt\", False, \"OnOff2\")";
_untermenu2.AddCheckBox("Tumblr","Tumblr","Erfolgsmeldung wird geteil","Erfolgsmeldung wird NICHT geteilt",anywheresoftware.b4a.keywords.Common.False,"OnOff2");
 //BA.debugLineNum = 159;BA.debugLine="Untermenu2.AddEditText(\"TumblrEmail\", \"Tumblr Email\", \"Hier private Tumblr eMail-Adresse eintragen\", \"\", \"TumblrEmail\")'hier wird mit chek ausgeschaltet";
_untermenu2.AddEditText("TumblrEmail","Tumblr Email","Hier private Tumblr eMail-Adresse eintragen","","TumblrEmail");
 //BA.debugLineNum = 161;BA.debugLine="Untermenu2.AddCheckBox(\"Twitter\", \"Twitter\", \"Erfolgsmeldung wird geteil\", \"Erfolgsmeldung wird NICHT geteilt\", False, \"OnOff2\")";
_untermenu2.AddCheckBox("Twitter","Twitter","Erfolgsmeldung wird geteil","Erfolgsmeldung wird NICHT geteilt",anywheresoftware.b4a.keywords.Common.False,"OnOff2");
 //BA.debugLineNum = 162;BA.debugLine="Untermenu2.AddEditText(\"TwitterEmail\", \"Twitter Email\", \"Hier private Twitter eMail-Adresse eintragen\", \"TwitterEmail\", \"Twitter\")'hier wird mit chek ausgeschaltet";
_untermenu2.AddEditText("TwitterEmail","Twitter Email","Hier private Twitter eMail-Adresse eintragen","TwitterEmail","Twitter");
 //BA.debugLineNum = 164;BA.debugLine="Untermenu2.AddCheckBox(\"Yahoo!\", \"Yahoo!\", \"Erfolgsmeldung wird geteil\", \"Erfolgsmeldung wird NICHT geteilt\", False, \"OnOff2\")";
_untermenu2.AddCheckBox("Yahoo!","Yahoo!","Erfolgsmeldung wird geteil","Erfolgsmeldung wird NICHT geteilt",anywheresoftware.b4a.keywords.Common.False,"OnOff2");
 //BA.debugLineNum = 165;BA.debugLine="Untermenu2.AddEditText(\"Yahoo!Email\", \"Yahoo! Email\", \"Hier private Yahoo! eMail-Adresse eintragen\", \"\", \"Yahoo!Email\")'hier wird mit chek ausgeschaltet";
_untermenu2.AddEditText("Yahoo!Email","Yahoo! Email","Hier private Yahoo! eMail-Adresse eintragen","","Yahoo!Email");
 //BA.debugLineNum = 169;BA.debugLine="Kategorie2.AddPreferenceScreen(Untermenu2, \"OnOff2\")";
_kategorie2.AddPreferenceScreen(_untermenu2,"OnOff2");
 //BA.debugLineNum = 174;BA.debugLine="Kategorie3.Initialize(\"Farben der Erfolgsmeldung\")";
_kategorie3.Initialize("Farben der Erfolgsmeldung");
 //BA.debugLineNum = 175;BA.debugLine="Dim m,S As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
_s = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 176;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 177;BA.debugLine="m.Put(\"0\", \"Zufall\")";
_m.Put((Object)("0"),(Object)("Zufall"));
 //BA.debugLineNum = 178;BA.debugLine="m.Put(\"1\", \"Schwarz\")";
_m.Put((Object)("1"),(Object)("Schwarz"));
 //BA.debugLineNum = 179;BA.debugLine="m.Put(\"2\", \"Blau\")";
_m.Put((Object)("2"),(Object)("Blau"));
 //BA.debugLineNum = 180;BA.debugLine="m.Put(\"3\", \"Cyan\")";
_m.Put((Object)("3"),(Object)("Cyan"));
 //BA.debugLineNum = 181;BA.debugLine="m.Put(\"4\", \"Grau\")";
_m.Put((Object)("4"),(Object)("Grau"));
 //BA.debugLineNum = 182;BA.debugLine="m.Put(\"5\", \"Grün\")";
_m.Put((Object)("5"),(Object)("Grün"));
 //BA.debugLineNum = 183;BA.debugLine="m.Put(\"6\", \"Magenta\")";
_m.Put((Object)("6"),(Object)("Magenta"));
 //BA.debugLineNum = 184;BA.debugLine="m.Put(\"7\", \"Rot\")";
_m.Put((Object)("7"),(Object)("Rot"));
 //BA.debugLineNum = 185;BA.debugLine="m.Put(\"8\", \"Weiss\")";
_m.Put((Object)("8"),(Object)("Weiss"));
 //BA.debugLineNum = 186;BA.debugLine="m.Put(\"9\", \"Gelb\")";
_m.Put((Object)("9"),(Object)("Gelb"));
 //BA.debugLineNum = 188;BA.debugLine="Kategorie3.AddList2(\"HFarbe\", \"Hintergrundfarbe\", \"Setzt die Farbe des Hintergrundes\", \"1\", \"\", m)";
_kategorie3.AddList2("HFarbe","Hintergrundfarbe","Setzt die Farbe des Hintergrundes","1","",_m);
 //BA.debugLineNum = 190;BA.debugLine="S.Initialize";
_s.Initialize();
 //BA.debugLineNum = 191;BA.debugLine="S.Put(\"10\", \"Zufall\")";
_s.Put((Object)("10"),(Object)("Zufall"));
 //BA.debugLineNum = 192;BA.debugLine="S.Put(\"11\", \"Schwarz\")";
_s.Put((Object)("11"),(Object)("Schwarz"));
 //BA.debugLineNum = 193;BA.debugLine="S.Put(\"12\", \"Blau\")";
_s.Put((Object)("12"),(Object)("Blau"));
 //BA.debugLineNum = 194;BA.debugLine="S.Put(\"13\", \"Cyan\")";
_s.Put((Object)("13"),(Object)("Cyan"));
 //BA.debugLineNum = 195;BA.debugLine="S.Put(\"14\", \"Grau\")";
_s.Put((Object)("14"),(Object)("Grau"));
 //BA.debugLineNum = 196;BA.debugLine="S.Put(\"15\", \"Grün\")";
_s.Put((Object)("15"),(Object)("Grün"));
 //BA.debugLineNum = 197;BA.debugLine="S.Put(\"16\", \"Magenta\")";
_s.Put((Object)("16"),(Object)("Magenta"));
 //BA.debugLineNum = 198;BA.debugLine="S.Put(\"17\", \"Rot\")";
_s.Put((Object)("17"),(Object)("Rot"));
 //BA.debugLineNum = 199;BA.debugLine="S.Put(\"18\", \"Weiss\")";
_s.Put((Object)("18"),(Object)("Weiss"));
 //BA.debugLineNum = 200;BA.debugLine="S.Put(\"19\", \"Gelb\")";
_s.Put((Object)("19"),(Object)("Gelb"));
 //BA.debugLineNum = 201;BA.debugLine="Kategorie3.AddList2(\"SFarbe\", \"Schriftfarbe\", \"Setzt die Schriftfarbe\", \"0\", \"\", S)";
_kategorie3.AddList2("SFarbe","Schriftfarbe","Setzt die Schriftfarbe","0","",_s);
 //BA.debugLineNum = 218;BA.debugLine="Bildschirm1.AddPreferenceCategory(Kategorie1)";
_bildschirm1.AddPreferenceCategory(_kategorie1);
 //BA.debugLineNum = 219;BA.debugLine="Bildschirm1.AddPreferenceCategory(Kategorie2)";
_bildschirm1.AddPreferenceCategory(_kategorie2);
 //BA.debugLineNum = 220;BA.debugLine="Bildschirm1.AddPreferenceCategory(Kategorie3)";
_bildschirm1.AddPreferenceCategory(_kategorie3);
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim Label1, Label2 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner =  \"/mama/Daten\"";
mostCurrent._aktuellerunterordner = "";
 //BA.debugLineNum = 18;BA.debugLine="Dim AktuellerUnterordner As String : AktuellerUnterordner =  \"/mama/Daten\"";
mostCurrent._aktuellerunterordner = "/mama/Daten";
 //BA.debugLineNum = 19;BA.debugLine="Dim camera1 As AdvancedCamera";
mostCurrent._camera1 = new xvs.ACL.ACL();
 //BA.debugLineNum = 20;BA.debugLine="Dim Button1 As Button 'button 1";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim ImageView2 As ImageView";
mostCurrent._imageview2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim ImageView1 As ImageView";
mostCurrent._imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim Label3 As Label";
mostCurrent._label3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim ProgressBar1 As ProgressBar";
mostCurrent._progressbar1 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static String  _handlesettings() throws Exception{
 //BA.debugLineNum = 261;BA.debugLine="Sub HandleSettings";
 //BA.debugLineNum = 262;BA.debugLine="Log(manager.GetAll)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(_manager.GetAll()));
 //BA.debugLineNum = 263;BA.debugLine="Select manager.GetString(\"HFarbe\")";
switch (BA.switchObjectToInt(_manager.GetString("HFarbe"),"0","1","2","3","4","5","6","7","8","9")) {
case 0:
 //BA.debugLineNum = 265;BA.debugLine="Activity.Color = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 break;
case 1:
 //BA.debugLineNum = 267;BA.debugLine="Activity.Color = Colors.Black";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 break;
case 2:
 //BA.debugLineNum = 269;BA.debugLine="Activity.Color = Colors.Blue";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 break;
case 3:
 //BA.debugLineNum = 271;BA.debugLine="Activity.Color = Colors.Cyan";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 break;
case 4:
 //BA.debugLineNum = 273;BA.debugLine="Activity.Color = Colors.Gray";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 break;
case 5:
 //BA.debugLineNum = 275;BA.debugLine="Activity.Color = Colors.Green";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 break;
case 6:
 //BA.debugLineNum = 277;BA.debugLine="Activity.Color = Colors.Magenta";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Magenta);
 break;
case 7:
 //BA.debugLineNum = 279;BA.debugLine="Activity.Color = Colors.Red";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 break;
case 8:
 //BA.debugLineNum = 281;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 break;
case 9:
 //BA.debugLineNum = 283;BA.debugLine="Activity.Color = Colors.Yellow";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 break;
}
;
 //BA.debugLineNum = 288;BA.debugLine="Select manager.GetString(\"SFarbe\")";
switch (BA.switchObjectToInt(_manager.GetString("SFarbe"),"10","11","12","13","14","15","16","17","18","19")) {
case 0:
 //BA.debugLineNum = 291;BA.debugLine="Label1.TextColor = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 break;
case 1:
 //BA.debugLineNum = 294;BA.debugLine="Label1.TextColor = Colors.Black";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 break;
case 2:
 //BA.debugLineNum = 297;BA.debugLine="Label1.TextColor = Colors.Blue";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 break;
case 3:
 //BA.debugLineNum = 300;BA.debugLine="Label1.TextColor = Colors.Cyan";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 break;
case 4:
 //BA.debugLineNum = 303;BA.debugLine="Label1.TextColor = Colors.Gray";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 break;
case 5:
 //BA.debugLineNum = 306;BA.debugLine="Label1.TextColor = Colors.Green";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 break;
case 6:
 //BA.debugLineNum = 309;BA.debugLine="Label1.TextColor = Colors.Magenta";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Magenta);
 break;
case 7:
 //BA.debugLineNum = 312;BA.debugLine="Label1.TextColor = Colors.Red";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 break;
case 8:
 //BA.debugLineNum = 315;BA.debugLine="Label1.TextColor = Colors.White";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 break;
case 9:
 //BA.debugLineNum = 318;BA.debugLine="Label1.TextColor = Colors.Yellow";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 break;
}
;
 //BA.debugLineNum = 324;BA.debugLine="Label2.TextColor = Label1.TextColor";
mostCurrent._label2.setTextColor(mostCurrent._label1.getTextColor());
 //BA.debugLineNum = 329;BA.debugLine="End Sub";
return "";
}
public static String  _mp_streambuffer(int _percentage) throws Exception{
 //BA.debugLineNum = 345;BA.debugLine="Sub mp_StreamBuffer(Percentage As Int)";
 //BA.debugLineNum = 346;BA.debugLine="Log(Percentage)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_percentage));
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return "";
}
public static String  _mp_streamerror(String _errorcode,int _extradata) throws Exception{
 //BA.debugLineNum = 340;BA.debugLine="Sub mp_StreamError (ErrorCode As String, ExtraData As Int)";
 //BA.debugLineNum = 341;BA.debugLine="Log(\"Error: \" & ErrorCode & \", \" & ExtraData)";
anywheresoftware.b4a.keywords.Common.Log("Error: "+_errorcode+", "+BA.NumberToString(_extradata));
 //BA.debugLineNum = 342;BA.debugLine="ToastMessageShow(\"Error: \" & ErrorCode & \", \" & ExtraData, True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error: "+_errorcode+", "+BA.NumberToString(_extradata),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 343;BA.debugLine="End Sub";
return "";
}
public static String  _mp_streamready() throws Exception{
 //BA.debugLineNum = 335;BA.debugLine="Sub mp_StreamReady";
 //BA.debugLineNum = 336;BA.debugLine="Log(\"starts playing\")";
anywheresoftware.b4a.keywords.Common.Log("starts playing");
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim manager As AHPreferenceManager";
_manager = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceManager();
 //BA.debugLineNum = 9;BA.debugLine="Dim Bildschirm1 As AHPreferenceScreen";
_bildschirm1 = new anywheresoftware.b4a.objects.preferenceactivity.PreferenceScreenWrapper();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _setdefaults() throws Exception{
 //BA.debugLineNum = 88;BA.debugLine="Sub SetDefaults";
 //BA.debugLineNum = 98;BA.debugLine="manager.SetString(\"FacebookEmail\", \"- @facebook.com\")";
_manager.SetString("FacebookEmail","- @facebook.com");
 //BA.debugLineNum = 99;BA.debugLine="manager.SetString(\"TwitterEmail\", \"- @twitter.com\")";
_manager.SetString("TwitterEmail","- @twitter.com");
 //BA.debugLineNum = 100;BA.debugLine="manager.SetString(\"BloggerEmail\", \"- @blogger.com\")";
_manager.SetString("BloggerEmail","- @blogger.com");
 //BA.debugLineNum = 101;BA.debugLine="manager.SetString(\"TumblrEmail\", \"\")";
_manager.SetString("TumblrEmail","");
 //BA.debugLineNum = 102;BA.debugLine="manager.SetString(\"Yahoo!Email\", \"\")";
_manager.SetString("Yahoo!Email","");
 //BA.debugLineNum = 103;BA.debugLine="manager.SetString(\"StumbleUponEmail\", \"\")";
_manager.SetString("StumbleUponEmail","");
 //BA.debugLineNum = 104;BA.debugLine="manager.SetString(\"MAMAEmail\", \"mama@watchkido.de\")";
_manager.SetString("MAMAEmail","mama@watchkido.de");
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
return "";
}
}
