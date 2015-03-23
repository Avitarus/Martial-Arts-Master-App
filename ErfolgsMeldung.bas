Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: True
	#IncludeTitle: False
#End Region

'Activity module
Sub Process_Globals
	Dim manager As AHPreferenceManager
	Dim Bildschirm1 As AHPreferenceScreen

	'Dim mp As MediaPlayerStream
End Sub

Sub Globals


	Dim Label1, Label2 As Label
    Dim AktuellerUnterordner As String : AktuellerUnterordner =  "/mama/Daten"
	Dim camera1 As AdvancedCamera
	Dim Button1 As Button 'button 1
	
	Dim ImageView2 As ImageView
	Dim ImageView1 As ImageView

	Dim Label1 As Label



	Dim Label3 As Label
	Dim ProgressBar1 As ProgressBar
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	If FirstTime Then
		CreatePreferenceScreen
		If manager.GetAll.Size = 0 Then SetDefaults
	End If
	Activity.LoadLayout("Erfolgsmeldung")
'	Button1.Initialize("Button1")
	Button1.Text = Main.Vorname & " ´" & Main.Spitzname  & "´ " & Main.Nachname
	Button1.TextSize = 20
	
'	Label1.Initialize("Label1")
	
	
	Dim txt0,txt1 As String

	txt0 = " Spitzname: "& CRLF & "  " & Main.Spitzname & CRLF
	txt0 = txt0 &" Heimatort: " & CRLF& "  " & Main.Heimatort & CRLF
	txt0 = txt0 &" Alter: " & Main.Alter & CRLF
	txt0 = txt0 &"Altersklasse: "& CRLF & "  " & Main.Altersklasse & CRLF
	txt0 = txt0 &" Größe: " & Main.Groeße& CRLF
	txt0 = txt0 &" Gewicht: " & Main.Gewicht & CRLF
	txt0 = txt0 &" Gewichtsklasse: "& CRLF & "  " & Main.Gewichtsklasse & CRLF
	'txt0 = txt0 &" Schule: "& CRLF & Main.Schule& CRLF
	'txt0 = txt0 &" Abschluss: "& CRLF & Main.Abschluss& CRLF
	txt0 = txt0 &" Sportart: "& CRLF & "  " & Main.Sportart & CRLF
	txt0 = txt0 &" Verein: "& CRLF & "  " & Main.Verein& CRLF
	'txt0 = txt0 &" Verband: "& CRLF & Main.Verband& CRLF
	Label1.Text = txt0
	Label1.TextSize = 14
	Label1.TextColor = Colors.Gray		
		
		
	'Label2.Initialize("Label2")
	Label2.Text = "Bruce Lee:" & CRLF & ">>Ich fürchte, nicht den Mann, der einmal 10.000 Tritte geübt hat, aber ich fürchte den Mann, der einen Kick 10.000 mal praktiziert hat.<<"
	Label2.TextSize = 14
	Label2.TextColor = Colors.Gray
		
	
	'ImageView1.Initialize("imageview1")
	ImageView1.Bitmap = LoadBitmap(File.DirAssets, "ErfolgFoto.jpg")

	ImageView2.Initialize("imageview2")
	ImageView2.Bitmap = LoadBitmap(File.DirAssets,"Kampfkunstmeisterlogo150x212.jpg")

'	Activity.AddView(Button1, 10, 5, 300, 50)' (Left As Int, Top As Int, Width As Int, Height As Int)
'	Activity.AddView(Label1, 10, 55, 300, 180)
'	Activity.AddView(Label2, 10, 415, 300, 60)
	
	Label3.Visible = False
	ProgressBar1.Visible = False
	
	
End Sub

Sub SetDefaults
	'defaults are only set on the first run.
'	manager.SetBoolean("Facebook", False)
'	manager.SetBoolean("Twitter", False)
'	manager.SetBoolean("Blogger", False)
'	manager.SetBoolean("Tumblr", False)
'	manager.SetBoolean("Yahoo!", False)
'	manager.SetBoolean("MAMA-Programmierer", True)
'	manager.SetBoolean("Info", True)
''	manager.SetBoolean("Twitter", False)
	manager.SetString("FacebookEmail", "- @facebook.com")
	manager.SetString("TwitterEmail", "- @twitter.com")
	manager.SetString("BloggerEmail", "- @blogger.com")
	manager.SetString("TumblrEmail", "")
	manager.SetString("Yahoo!Email", "")
	manager.SetString("StumbleUponEmail", "")
	manager.SetString("MAMAEmail", "mama@watchkido.de")
	
End Sub

Sub Button1_Click
	StartActivity(Bildschirm1.CreateIntent)
End Sub


Sub CreatePreferenceScreen
	Bildschirm1.Initialize("Einstellunen", "")
	'Bildschirm2.Initialize("Settings", "")
	Dim Kategorie1, Kategorie2, Kategorie3, Kategorie4 As AHPreferenceCategory
	Dim Untermenu1, Untermenu2 As AHPreferenceScreen
	Dim pi As PhoneIntents
	
	
	Kategorie1.Initialize("Woher bekomme ich meine Zugangsdaten?")
	Kategorie1.AddCheckBox("OnOff1", "Informationen", "Zugangsinfos EIN", "Zugangsinfos AUS", True, "")
	Untermenu1.Initialize("HELP-Seiten der Anbieter", "Wie bekomme ich Zugang")

	Untermenu1.AddIntent("Blogger", "Öffne Blogger.com", pi.OpenBrowser("http://www.blogger.com/home"), "OnOff1")
	Untermenu1.AddIntent("Blogg", "Öffne Blogg.de", pi.OpenBrowser("http://www.blogg.de"), "OnOff1")
	Untermenu1.AddIntent("Facebook", "Öffne Facebook.com", pi.OpenBrowser("http://www.facebook.com/help/?faq=210153612350847"), "OnOff1")
	Untermenu1.AddIntent("Google+", "Öffne Google+", pi.OpenBrowser("m.google.de"), "OnOff1")
	Untermenu1.AddIntent("LinkedIN", "Öffne Linkedin.com", pi.OpenBrowser("http://www.linkedin.com/"), "OnOff1")
	Untermenu1.AddIntent("Lokalisten", "Öffne Lokalisten.de", pi.OpenBrowser("mobile.lokalisten.de"), "OnOff1")
	Untermenu1.AddIntent("Tumblr", "Öffne Tumblr.com", pi.OpenBrowser("http://www.tumblr.com/"), "OnOff1")
	Untermenu1.AddIntent("Twitter", "Öffne Twittermail.com", pi.OpenBrowser("http://twittercounter.com/pages/twittermail/"), "OnOff1")
	Untermenu1.AddIntent("Wer-Kennt-Wen", "Öffnet Wer-Kennt-Wen.de", pi.OpenBrowser("http://www.wer-kennt-wen.de/hilfe/"), "OnOff1")
	Untermenu1.AddIntent("Yahoo!", "Öffne Yahoo.com", pi.OpenBrowser("http://www.yahoo.com"), "OnOff1")
	
	Kategorie1.AddPreferenceScreen(Untermenu1, "OnOff1")


	
	
	
	Kategorie2.Initialize("Soziale Netzwerke")
	Kategorie2.AddCheckBox("OnOff2", "Soziale Netzwerke", "Zugang EIN", "Zugang AUS", False, "")
	Untermenu2.Initialize("Webseiten der Anbieter", "Hier Zugangsdaten eintragen")
	
	Untermenu2.AddCheckBox("Blogger", "Blogger", "Erfolgsmeldung wird geteil", "Erfolgsmeldung wird NICHT geteilt", False, "OnOff2")
	Untermenu2.AddEditText("BloggerEmail", "Blogger Email", "Hier private Blogger eMail-Adresse eintragen", "BloggerEmail", "Blogger")'hier wird mit chek ausgeschaltet

	Untermenu2.AddCheckBox("Facebook1", "Facebook", "Erfolgsmeldung wird geteil", "Erfolgsmeldung wird NICHT geteilt", False, "OnOff2")
	Untermenu2.AddEditText("FacebookEmail", "Facebook Email", "Hier private Facebook eMail-Adresse eintragen", "FacebookEmail", "Facebook1")'hier wird mit chek ausgeschaltet
	
	Untermenu2.AddCheckBox("MAMA-Programmierer", "MAMA-Programmierer", "Erfolgsmeldung wird geteil", "Erfolgsmeldung wird NICHT geteilt", False, "OnOff2")
	Untermenu2.AddEditText("MAMA-P-Email", "MAMA-Programmierer Email", "Hier private MAMA-Programmierer eMail-Adresse eintragen", "MAMAEmail", "MAMA-Programmierer")'hier wird mit chek ausgeschaltet
	
	Untermenu2.AddCheckBox("StumbleUpon", "StumbleUpon", "Erfolgsmeldung wird geteil", "Erfolgsmeldung wird NICHT geteilt", False, "OnOff2")
	Untermenu2.AddEditText("StumbleUponEmail", "StumbleUpon Email", "Hier private StumbleUpon eMail-Adresse eintragen", "StumleUponEmail", "StumbleUpon")'hier wird mit chek ausgeschaltet
	
	Untermenu2.AddCheckBox("Tumblr", "Tumblr", "Erfolgsmeldung wird geteil", "Erfolgsmeldung wird NICHT geteilt", False, "OnOff2")
	Untermenu2.AddEditText("TumblrEmail", "Tumblr Email", "Hier private Tumblr eMail-Adresse eintragen", "", "TumblrEmail")'hier wird mit chek ausgeschaltet
	
	Untermenu2.AddCheckBox("Twitter", "Twitter", "Erfolgsmeldung wird geteil", "Erfolgsmeldung wird NICHT geteilt", False, "OnOff2")
	Untermenu2.AddEditText("TwitterEmail", "Twitter Email", "Hier private Twitter eMail-Adresse eintragen", "TwitterEmail", "Twitter")'hier wird mit chek ausgeschaltet

	Untermenu2.AddCheckBox("Yahoo!", "Yahoo!", "Erfolgsmeldung wird geteil", "Erfolgsmeldung wird NICHT geteilt", False, "OnOff2")
	Untermenu2.AddEditText("Yahoo!Email", "Yahoo! Email", "Hier private Yahoo! eMail-Adresse eintragen", "", "Yahoo!Email")'hier wird mit chek ausgeschaltet



	Kategorie2.AddPreferenceScreen(Untermenu2, "OnOff2")

	


	Kategorie3.Initialize("Farben der Erfolgsmeldung")
	Dim m,S As Map
	m.Initialize
	m.Put("0", "Zufall")
	m.Put("1", "Schwarz")
	m.Put("2", "Blau")
	m.Put("3", "Cyan")
	m.Put("4", "Grau")
	m.Put("5", "Grün")
	m.Put("6", "Magenta")
	m.Put("7", "Rot")
	m.Put("8", "Weiss")
	m.Put("9", "Gelb")

	Kategorie3.AddList2("HFarbe", "Hintergrundfarbe", "Setzt die Farbe des Hintergrundes", "1", "", m)
	
	S.Initialize
	S.Put("10", "Zufall")
	S.Put("11", "Schwarz")
	S.Put("12", "Blau")
	S.Put("13", "Cyan")
	S.Put("14", "Grau")
	S.Put("15", "Grün")
	S.Put("16", "Magenta")
	S.Put("17", "Rot")
	S.Put("18", "Weiss")
	S.Put("19", "Gelb")
	Kategorie3.AddList2("SFarbe", "Schriftfarbe", "Setzt die Schriftfarbe", "0", "", S)




'	Kategorie4.Initialize("Woher bekomme ich die Zugangsadressen?")
'	Kategorie4.AddCheckBox("Info", "Erfolge Teilen", "Erfolgsmeldung wird geteil", "Erfolgsmeldung wird NICHT geteilt", False, "")
'	Kategorie4.AddIntent("Google+ eMail-Infos", "Öffne Google+", pi.OpenBrowser("m.google.de"), "Info")
'	Kategorie4.AddIntent("Facebook eMail-Infos", "Öffne Facebook.com", pi.OpenBrowser("http://www.facebook.com/help/?faq=210153612350847"), "Info")
'	Kategorie4.AddIntent("Twitter eMail-Infos", "Öffne Twittermail.com", pi.OpenBrowser("http://twittercounter.com/pages/twittermail/"), "Info")
'	Kategorie4.AddIntent("LinkedIN eMail-Infos", "Öffne Linkedin.com", pi.OpenBrowser("http://www.linkedin.com/"), "Info")
'	Kategorie4.AddIntent("Wer-Kennt-Wen eMail-Infos", "Öffnet Wer-Kennt-Wen.de", pi.OpenBrowser("http://www.wer-kennt-wen.de/hilfe/"), "Info")
'	Kategorie4.AddIntent("Lokalisten eMail-Infos", "Öffne Lokalisten.de", pi.OpenBrowser("mobile.lokalisten.de"), "Info")
'	Kategorie4.AddIntent("Blogger eMail-Infos", "Öffne Blogger.com", pi.OpenBrowser("http://www.blogger.com/home"), "Info")
'	Kategorie4.AddIntent("Blogg eMail-Infos", "Öffne Blogg.de", pi.OpenBrowser("http://www.blogg.de"), "Info")

	
	Bildschirm1.AddPreferenceCategory(Kategorie1)
	Bildschirm1.AddPreferenceCategory(Kategorie2)
	Bildschirm1.AddPreferenceCategory(Kategorie3)
	'Bildschirm1.AddPreferenceCategory(Kategorie4)
	
	
		' Use an B4A Intent
'	Dim in As Intent
'	in.Initialize("android.settings.WIFI_SETTINGS", "")
'	Untermenu1.AddIntent( "Wifi Einstellungen", "Beispiel für einstellungen", in, "chkwifi")

'	' Intent from GPS-Library
'	Dim g As GPS
'	g.Initialize("")
'	Untermenu1.AddIntent("GPS Einstellungen", "Start Android GPS Einstellungen", g.LocationSettingsIntent, "")

'	' Call installed Application
'	Dim pm As PackageManager
'	Dim pl As List
'	pl = pm.GetInstalledPackages
'	' If the Calculator is installed, call it
'	If pl.IndexOf("com.android.calculator2") > 0 Then
'		Untermenu1.AddIntent("Rechner", "INterner Rechner", pm.GetApplicationIntent("com.android.calculator2"), "")
'	End If
	
	' Open Webbrowser using PhoneIntents
	
'	Untermenu1.AddIntent("Browser", "Öffne http://www.google.de", pi.OpenBrowser("http://www.google.com"), "")

	' Add the screen with the intents calls To the category
	
		
'	Kategorie2.AddPassword("pwd1", "Password1", "Das ist ein password", "", "")
'	Kategorie2.AddRingtone("ring1", "Ringtone1", "Das ist a Ringtone", "", "", Kategorie2.RT_NOTIFICATION)


	
End Sub

Sub Activity_Resume
	HandleSettings
End Sub

Sub HandleSettings
	Log(manager.GetAll)
	Select manager.GetString("HFarbe")
		Case "0"
			Activity.Color = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))
		Case "1"
			Activity.Color = Colors.Black
		Case "2"
			Activity.Color = Colors.Blue
		Case "3"
			Activity.Color = Colors.Cyan
		Case "4"
			Activity.Color = Colors.Gray
		Case "5"
			Activity.Color = Colors.Green
		Case "6"
			Activity.Color = Colors.Magenta
		Case "7"
			Activity.Color = Colors.Red
		Case "8"
			Activity.Color = Colors.White
		Case "9"
			Activity.Color = Colors.Yellow
			
			
	End Select		
			
	Select manager.GetString("SFarbe")	
			
		Case "10"
			Label1.TextColor = Colors.RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))
			
		Case "11"
			Label1.TextColor = Colors.Black
			
		Case "12"
			Label1.TextColor = Colors.Blue
			
		Case "13"
			Label1.TextColor = Colors.Cyan
			
		Case "14"
			Label1.TextColor = Colors.Gray
			
		Case "15"
			Label1.TextColor = Colors.Green
			
		Case "16"
			Label1.TextColor = Colors.Magenta
			
		Case "17"
			Label1.TextColor = Colors.Red
			
		Case "18"
			Label1.TextColor = Colors.White
			 
		Case "19"
			Label1.TextColor = Colors.Yellow
			
			
			

	End Select
	Label2.TextColor = Label1.TextColor
'	If manager.GetString("ring1") <> "" Then
'		'mp.Initialize("mp")
'	'	mp.Load(manager.GetString("ring1"))
'	End If
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub mp_StreamReady
    Log("starts playing")
   ' mp.Play
End Sub

Sub mp_StreamError (ErrorCode As String, ExtraData As Int)
    Log("Error: " & ErrorCode & ", " & ExtraData)
    ToastMessageShow("Error: " & ErrorCode & ", " & ExtraData, True)
End Sub

Sub mp_StreamBuffer(Percentage As Int)
    Log(Percentage)
End Sub 

Sub Button2_Click '(Activity As Activity)

   ' Take a screenshot.
   Dim Obj1, Obj2 As Reflector
   Dim bmp As Bitmap
   Dim c As Canvas
   Dim now, i As Long
   Dim dt As String
   DateTime.DateFormat ="yy-MM-dd-HH-mm-ss"
   now = DateTime.now
   dt = "Erfolg-" & DateTime.Date(now) ' e.g.: "110812150355" is Aug.12, 2011, 3:03:55 p.m.
   Obj1.Target = Obj1.GetActivityBA
   Obj1.Target = Obj1.GetField("vg")
   bmp.InitializeMutable(Activity.Width, Activity.Height)
   c.Initialize2(bmp)
   Dim args(1) As Object
   Dim types(1) As String
   Obj2.Target = c
   Obj2.Target = Obj2.GetField("canvas")
   args(0) = Obj2.Target
   types(0) = "android.graphics.Canvas"
   Obj1.RunMethod4("draw", args, types)
   Dim Out As OutputStream
   Out = File.OpenOutput(File.DirRootExternal,AktuellerUnterordner & dt & ".png", False)
   bmp.WriteToStream(Out, 100, "PNG")
   Out.Close
   Msgbox("Mit den Netzwerken:   "& "und gespeichert als: " & File.DirRootExternal & AktuellerUnterordner & CRLF & dt & ".png", "Geteilt")
  ' StartActivity(screen.CreateIntent)
End Sub
