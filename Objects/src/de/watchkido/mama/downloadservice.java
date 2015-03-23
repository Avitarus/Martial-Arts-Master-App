package de.watchkido.mama;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class downloadservice extends android.app.Service {
	public static class downloadservice_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			android.content.Intent in = new android.content.Intent(context, downloadservice.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
			context.startService(in);
		}

	}
    static downloadservice mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return downloadservice.class;
	}
	@Override
	public void onCreate() {
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "de.watchkido.mama", "de.watchkido.mama.downloadservice");
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        processBA.setActivityPaused(false);
        if (BA.shellMode) {
			processBA.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.downloadservice", processBA, _service);
		}
        BA.LogInfo("** Service (downloadservice) Create **");
        processBA.raiseEvent(null, "service_create");
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		handleStart(intent);
    }
    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
    	handleStart(intent);
		return android.app.Service.START_NOT_STICKY;
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (downloadservice) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = new anywheresoftware.b4a.objects.IntentWrapper();
    			if (intent != null) {
    				if (intent.hasExtra("b4a_internal_intent"))
    					iw.setObject((android.content.Intent) intent.getParcelableExtra("b4a_internal_intent"));
    				else
    					iw.setObject(intent);
    			}
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}
	@Override
	public void onDestroy() {
        BA.LogInfo("** Service (downloadservice) Destroy **");
		processBA.raiseEvent(null, "service_destroy");
        processBA.service = null;
		mostCurrent = null;
		processBA.setActivityPaused(true);
	}
public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.http.HttpClientWrapper _hc = null;
public static String _url = "";
public static anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _target = null;
public static int _jobstatus = 0;
public static int _status_none = 0;
public static int _status_working = 0;
public static int _status_done = 0;
public static boolean _donesuccessfully = false;
public static anywheresoftware.b4a.objects.NotificationWrapper _notification1 = null;
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
public de.watchkido.mama.kampfsportlexikon _kampfsportlexikon = null;
public de.watchkido.mama.karatestunde _karatestunde = null;
public de.watchkido.mama.tts _tts = null;
public de.watchkido.mama.lebensmittel _lebensmittel = null;
public de.watchkido.mama.statemanager _statemanager = null;
public de.watchkido.mama.tagebuch _tagebuch = null;
public static String  _finish() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Sub Finish";
 //BA.debugLineNum = 62;BA.debugLine="Log(\"Service finished downloading\")";
anywheresoftware.b4a.keywords.Common.Log("Service finished downloading");
 //BA.debugLineNum = 63;BA.debugLine="JobStatus = STATUS_DONE";
_jobstatus = _status_done;
 //BA.debugLineNum = 66;BA.debugLine="CallSub(Main, \"FinishDownload\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._main.getObject()),"FinishDownload");
 //BA.debugLineNum = 67;BA.debugLine="Service.StopForeground(1) 'Return the service to the \"background\" (also removes the ongoing notification)";
mostCurrent._service.StopForeground((int) (1));
 //BA.debugLineNum = 68;BA.debugLine="If IsPaused(Main) Then";
if (anywheresoftware.b4a.keywords.Common.IsPaused(processBA,(Object)(mostCurrent._main.getObject()))) { 
 //BA.debugLineNum = 71;BA.debugLine="Notification1.Sound = True";
_notification1.setSound(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 72;BA.debugLine="Notification1.SetInfo(\"Download Service\", \"Download complete\", Main)";
_notification1.SetInfo(processBA,"Download Service","Download complete",(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 73;BA.debugLine="Notification1.AutoCancel = True";
_notification1.setAutoCancel(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 74;BA.debugLine="Notification1.Notify(1)";
_notification1.Notify((int) (1));
 };
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static String  _hc_responseerror(String _reason,int _statuscode,int _taskid) throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub HC_ResponseError (Reason As String, StatusCode As Int, TaskId As Int)";
 //BA.debugLineNum = 41;BA.debugLine="ToastMessageShow(\"Error downloading file: \" & Reason, True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error downloading file: "+_reason,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 42;BA.debugLine="DoneSuccessfully = False";
_donesuccessfully = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 43;BA.debugLine="Finish";
_finish();
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _hc_responsesuccess(anywheresoftware.b4a.http.HttpClientWrapper.HttpResponeWrapper _response,int _taskid) throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub HC_ResponseSuccess (Response As HttpResponse, TaskId As Int)";
 //BA.debugLineNum = 48;BA.debugLine="Response.GetAsynchronously(\"Response\", Target, True, TaskId)";
_response.GetAsynchronously(processBA,"Response",(java.io.OutputStream)(_target.getObject()),anywheresoftware.b4a.keywords.Common.True,_taskid);
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim HC As HttpClient";
_hc = new anywheresoftware.b4a.http.HttpClientWrapper();
 //BA.debugLineNum = 9;BA.debugLine="Dim URL As String";
_url = "";
 //BA.debugLineNum = 10;BA.debugLine="Dim Target As OutputStream";
_target = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Dim JobStatus As Int";
_jobstatus = 0;
 //BA.debugLineNum = 12;BA.debugLine="Dim STATUS_NONE, STATUS_WORKING, STATUS_DONE As Int";
_status_none = 0;
_status_working = 0;
_status_done = 0;
 //BA.debugLineNum = 13;BA.debugLine="STATUS_NONE = 0";
_status_none = (int) (0);
 //BA.debugLineNum = 14;BA.debugLine="STATUS_WORKING = 1";
_status_working = (int) (1);
 //BA.debugLineNum = 15;BA.debugLine="STATUS_DONE = 2";
_status_done = (int) (2);
 //BA.debugLineNum = 16;BA.debugLine="Dim DoneSuccessfully As Boolean";
_donesuccessfully = false;
 //BA.debugLineNum = 17;BA.debugLine="Dim Notification1 As Notification";
_notification1 = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static String  _response_streamfinish(boolean _success,int _taskid) throws Exception{
 //BA.debugLineNum = 51;BA.debugLine="Sub Response_StreamFinish (Success As Boolean, TaskId As Int)";
 //BA.debugLineNum = 52;BA.debugLine="If Success = False Then";
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 53;BA.debugLine="ToastMessageShow(\"Error downloading file: \" & LastException.Message, True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error downloading file: "+anywheresoftware.b4a.keywords.Common.LastException(processBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 55;BA.debugLine="ToastMessageShow(\"Download successfully.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Download successfully.",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 57;BA.debugLine="DoneSuccessfully = Success";
_donesuccessfully = _success;
 //BA.debugLineNum = 58;BA.debugLine="Finish";
_finish();
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 19;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 20;BA.debugLine="HC.Initialize(\"HC\")";
_hc.Initialize("HC");
 //BA.debugLineNum = 21;BA.debugLine="Notification1.Initialize";
_notification1.Initialize();
 //BA.debugLineNum = 22;BA.debugLine="Notification1.Icon = \"icon\" 'use the application icon file for the notification";
_notification1.setIcon("icon");
 //BA.debugLineNum = 23;BA.debugLine="Notification1.Vibrate = False";
_notification1.setVibrate(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 77;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public static String  _service_start() throws Exception{
anywheresoftware.b4a.http.HttpClientWrapper.HttpUriRequestWrapper _request = null;
 //BA.debugLineNum = 26;BA.debugLine="Sub Service_Start";
 //BA.debugLineNum = 28;BA.debugLine="Dim request As HttpRequest";
_request = new anywheresoftware.b4a.http.HttpClientWrapper.HttpUriRequestWrapper();
 //BA.debugLineNum = 29;BA.debugLine="request.InitializeGet(URL)";
_request.InitializeGet(_url);
 //BA.debugLineNum = 30;BA.debugLine="HC.Execute(request, 1)";
_hc.Execute(processBA,_request,(int) (1));
 //BA.debugLineNum = 31;BA.debugLine="JobStatus = STATUS_WORKING";
_jobstatus = _status_working;
 //BA.debugLineNum = 32;BA.debugLine="Notification1.SetInfo(\"Download Service example\", \"Downloading: \" & URL, Main)";
_notification1.SetInfo(processBA,"Download Service example","Downloading: "+_url,(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 33;BA.debugLine="Notification1.Sound = False";
_notification1.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 37;BA.debugLine="Service.StartForeground(1, Notification1)";
mostCurrent._service.StartForeground((int) (1),(android.app.Notification)(_notification1.getObject()));
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
}
