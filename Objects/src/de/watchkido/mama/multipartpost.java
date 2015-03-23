package de.watchkido.mama;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class multipartpost {
private static multipartpost mostCurrent = new multipartpost();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
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
public static class _filedata{
public boolean IsInitialized;
public String Dir;
public String FileName;
public String KeyName;
public String ContentType;
public void Initialize() {
IsInitialized = true;
Dir = "";
FileName = "";
KeyName = "";
ContentType = "";
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static anywheresoftware.b4a.http.HttpClientWrapper.HttpUriRequestWrapper  _createpostrequest(anywheresoftware.b4a.BA _ba,String _url,anywheresoftware.b4a.objects.collections.Map _namevalues,anywheresoftware.b4a.objects.collections.List _files) throws Exception{
String _boundary = "";
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _stream = null;
String _eol = "";
byte[] _b = null;
String _key = "";
String _value = "";
int _i = 0;
de.watchkido.mama.multipartpost._filedata _fd = null;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _in = null;
anywheresoftware.b4a.http.HttpClientWrapper.HttpUriRequestWrapper _request = null;
 //BA.debugLineNum = 11;BA.debugLine="Sub CreatePostRequest(URL As String, NameValues As Map, Files As List) As HttpRequest";
 //BA.debugLineNum = 13;BA.debugLine="Dim boundary As String";
_boundary = "";
 //BA.debugLineNum = 15;BA.debugLine="boundary = \"---------------------------1461124740692\"";
_boundary = "---------------------------1461124740692";
 //BA.debugLineNum = 17;BA.debugLine="Dim stream As OutputStream";
_stream = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 19;BA.debugLine="stream.InitializeToBytesArray(20)";
_stream.InitializeToBytesArray((int) (20));
 //BA.debugLineNum = 21;BA.debugLine="Dim EOL As String";
_eol = "";
 //BA.debugLineNum = 23;BA.debugLine="EOL = Chr(13) & Chr(10) 'CRLF constant matches Android end of line character which is chr(10).";
_eol = BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (13)))+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (10)));
 //BA.debugLineNum = 25;BA.debugLine="Dim b() As Byte";
_b = new byte[(int) (0)];
;
 //BA.debugLineNum = 27;BA.debugLine="If NameValues <> Null AND NameValues.IsInitialized Then";
if (_namevalues!= null && _namevalues.IsInitialized()) { 
 //BA.debugLineNum = 30;BA.debugLine="Dim key, value As String";
_key = "";
_value = "";
 //BA.debugLineNum = 32;BA.debugLine="For I	= 0 To NameValues.Size - 1";
{
final int step13 = 1;
final int limit13 = (int) (_namevalues.getSize()-1);
for (_i = (int) (0); (step13 > 0 && _i <= limit13) || (step13 < 0 && _i >= limit13); _i = ((int)(0 + _i + step13))) {
 //BA.debugLineNum = 34;BA.debugLine="key = NameValues.GetKeyAt(I)";
_key = BA.ObjectToString(_namevalues.GetKeyAt(_i));
 //BA.debugLineNum = 35;BA.debugLine="value = NameValues.GetValueAt(I)";
_value = BA.ObjectToString(_namevalues.GetValueAt(_i));
 //BA.debugLineNum = 36;BA.debugLine="b = (\"--\" & boundary & EOL & \"Content-Disposition: form-data; name=\"	& QUOTE & key & QUOTE & EOL & EOL & value & EOL).GetBytes(\"UTF8\")";
_b = ("--"+_boundary+_eol+"Content-Disposition: form-data; name="+anywheresoftware.b4a.keywords.Common.QUOTE+_key+anywheresoftware.b4a.keywords.Common.QUOTE+_eol+_eol+_value+_eol).getBytes("UTF8");
 //BA.debugLineNum = 37;BA.debugLine="stream.WriteBytes(b, 0, b.Length)";
_stream.WriteBytes(_b,(int) (0),_b.length);
 }
};
 };
 //BA.debugLineNum = 43;BA.debugLine="If Files <> Null AND Files.IsInitialized Then";
if (_files!= null && _files.IsInitialized()) { 
 //BA.debugLineNum = 46;BA.debugLine="Dim fd As FileData";
_fd = new de.watchkido.mama.multipartpost._filedata();
 //BA.debugLineNum = 48;BA.debugLine="For I = 0 To Files.Size - 1";
{
final int step22 = 1;
final int limit22 = (int) (_files.getSize()-1);
for (_i = (int) (0); (step22 > 0 && _i <= limit22) || (step22 < 0 && _i >= limit22); _i = ((int)(0 + _i + step22))) {
 //BA.debugLineNum = 50;BA.debugLine="fd = Files.Get(I)";
_fd = (de.watchkido.mama.multipartpost._filedata)(_files.Get(_i));
 //BA.debugLineNum = 52;BA.debugLine="b = (\"--\" & boundary & EOL & \"Content-Disposition: form-data; name=\" & QUOTE & fd.KeyName & QUOTE & \"; filename=\" & QUOTE & fd.FileName & QUOTE	& \";\" & \"Content-Type: \" & EOL & fd.ContentType & EOL & EOL).GetBytes(\"UTF8\")";
_b = ("--"+_boundary+_eol+"Content-Disposition: form-data; name="+anywheresoftware.b4a.keywords.Common.QUOTE+_fd.KeyName+anywheresoftware.b4a.keywords.Common.QUOTE+"; filename="+anywheresoftware.b4a.keywords.Common.QUOTE+_fd.FileName+anywheresoftware.b4a.keywords.Common.QUOTE+";"+"Content-Type: "+_eol+_fd.ContentType+_eol+_eol).getBytes("UTF8");
 //BA.debugLineNum = 54;BA.debugLine="stream.WriteBytes(b, 0, b.Length)";
_stream.WriteBytes(_b,(int) (0),_b.length);
 //BA.debugLineNum = 56;BA.debugLine="Dim In As InputStream";
_in = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 58;BA.debugLine="In = File.OpenInput(fd.Dir, fd.FileName)";
_in = anywheresoftware.b4a.keywords.Common.File.OpenInput(_fd.Dir,_fd.FileName);
 //BA.debugLineNum = 60;BA.debugLine="File.Copy2(In, stream) 'Read the file and write it to the stream";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(_in.getObject()),(java.io.OutputStream)(_stream.getObject()));
 //BA.debugLineNum = 61;BA.debugLine="b = EOL.GetBytes(\"UTF8\")";
_b = _eol.getBytes("UTF8");
 //BA.debugLineNum = 62;BA.debugLine="stream.WriteBytes(b, 0, b.Length)";
_stream.WriteBytes(_b,(int) (0),_b.length);
 }
};
 };
 //BA.debugLineNum = 68;BA.debugLine="b = (EOL & \"--\" & boundary & \"--\" & EOL).GetBytes(\"UTF8\")";
_b = (_eol+"--"+_boundary+"--"+_eol).getBytes("UTF8");
 //BA.debugLineNum = 69;BA.debugLine="stream.WriteBytes(b, 0, b.Length)";
_stream.WriteBytes(_b,(int) (0),_b.length);
 //BA.debugLineNum = 70;BA.debugLine="b = stream.ToBytesArray";
_b = _stream.ToBytesArray();
 //BA.debugLineNum = 72;BA.debugLine="Dim request As HttpRequest";
_request = new anywheresoftware.b4a.http.HttpClientWrapper.HttpUriRequestWrapper();
 //BA.debugLineNum = 74;BA.debugLine="request.InitializePost2(URL, b)";
_request.InitializePost2(_url,_b);
 //BA.debugLineNum = 75;BA.debugLine="request.SetContentType(\"multipart/form-data; boundary=\" & boundary)";
_request.SetContentType("multipart/form-data; boundary="+_boundary);
 //BA.debugLineNum = 76;BA.debugLine="request.SetContentEncoding(\"UTF8\")";
_request.SetContentEncoding("UTF8");
 //BA.debugLineNum = 78;BA.debugLine="Return request";
if (true) return _request;
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return null;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Type FileData(Dir As String, FileName As String, KeyName As String, ContentType As String)";
;
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
}
