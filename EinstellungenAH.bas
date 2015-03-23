Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

'Activity module
Sub Process_Globals
	Dim manager As AHPreferenceManager
	Dim screen As AHPreferenceScreen

	'Dim mp As MediaPlayerStream
End Sub

Sub Globals






End Sub

Sub Activity_Create(FirstTime As Boolean)
	If FirstTime Then
		CreatePreferenceScreen
		
		If manager.GetAll.Size = 0 Then SetDefaults
	End If
	
	
'	Dim btn As Button
'	btn.Initialize("btn")
'	btn.Text = "Settings"
'	Activity.AddView(btn, 10dip, 10dip, 200dip, 100dip)

	StartActivity(screen.CreateIntent)

End Sub

Sub SetDefaults
	'defaults are only set on the first run.
	manager.SetBoolean("Schalter1", True)
	manager.SetBoolean("Schalter2", False)
	manager.SetString("Text1", "Hello!")
	manager.SetString("Auswahl1", "Black")
End Sub

Sub btn_Click
	StartActivity(screen.CreateIntent)
End Sub


Sub CreatePreferenceScreen
	screen.Initialize("Settings", "")
	Dim intentCat, cat1, cat2 As AHPreferenceCategory
	Dim intentScreen As AHPreferenceScreen
	
	intentCat.Initialize("Intent Settings")
	intentCat.AddCheckBox("intentenable", "Enable Intent Settings", "Intent settings are enabled", "Intent settings are disabled", True, "")
	intentScreen.Initialize("Intents", "Examples with Intents")
	intentScreen.AddCheckBox("chkwifi", "Enable Wifi Settings", "Wifi settings enabled", "Wifi settings disabled", False, "")

	' Use an B4A Intent
	Dim In As Intent
	In.Initialize("android.settings.WIFI_SETTINGS", "")
	intentScreen.AddIntent( "Wifi Settings", "Example for custom Intent", In, "chkwifi")

	' Intent from GPS-Library
	'intentScreen.AddIntent("GPS Settings", "Start Android GPS settings", g.LocationSettingsIntent, "")

	' Call installed Application
	Dim pm As PackageManager
	Dim pl As List
	pl = pm.GetInstalledPackages
	' If the Calculator is installed, call it
	If pl.IndexOf("com.android.calculator2") > 0 Then
		intentScreen.AddIntent("Calculator", "Open calculator", pm.GetApplicationIntent("com.android.calculator2"), "")
	End If
	
	' Open Webbrowser using PhoneIntents
	Dim pi As PhoneIntents
	intentScreen.AddIntent("Browser", "Open http://www.google.de", pi.OpenBrowser("http://www.google.com"), "")

	' Add the screen with the intents calls to the category
	intentCat.AddPreferenceScreen(intentScreen, "intentenable")
	
	cat1.Initialize("Examples")
	cat1.AddCheckBox("Schalter1", "Checkbox1", "This is Checkbox1 without second summary", "", True, "")
	cat1.AddEditText("Text1", "EditText1", "This is EditText1", "", "Schalter1")
	cat1.AddPassword("pwd1", "Password1", "This is a password", "", "")
	cat1.AddRingtone("ring1", "Ringtone1", "This is a Ringtone", "", "", cat1.RT_NOTIFICATION)

	cat2.Initialize("Set Background Color")
	Dim m As Map
	m.Initialize
	m.Put("black", "I want a black background")
	m.Put("red", "No, make it red")
	m.Put("green", "I like it green")
	m.Put("blue", "Blue is best")
	cat2.AddList2("Background Color", "Choose color", "Choose color", "black", "", m)
	
		
	'add the categories to the main screen
	screen.AddPreferenceCategory(intentCat)
	screen.AddPreferenceCategory(cat1)
	screen.AddPreferenceCategory(cat2)
End Sub


Sub Activity_Resume
	HandleSettings
End Sub

Sub HandleSettings
	Log(manager.GetAll)
	Select manager.GetString("Background Color")
		Case "black"
			Activity.Color = Colors.Black
		Case "red"
			Activity.Color = Colors.Red
		Case "green"
			Activity.Color = Colors.Green
		Case "blue"
			Activity.Color = Colors.Blue
	End Select
	
	If manager.GetString("ring1") <> "" Then
		'mp.Initialize("mp")
	'	mp.Load(manager.GetString("ring1"))
	End If
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

