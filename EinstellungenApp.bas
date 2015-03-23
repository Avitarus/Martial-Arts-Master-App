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
	Dim screen, screenPersonal, screenTraining, screenNetzwerke As AHPreferenceScreen

	'Dim mp As MediaPlayerStream
End Sub

Sub Globals
	Dim ListView1 As ListView
	Dim Anfangsgewicht, Zielgewicht, Kontrolltag, Zeitzone As Int
	Dim Schriftfarbe, Hintergrundfarbe As Int
	Dim MaxLohn, MinLohn, Ausgabepreis,StrafLohn As Double
	Dim Name, Gewicht, Waehrung As String
	Dim Starttag, Zieltag, Startzeit, Zielzeit As String
	Dim AktuellerUnterordner As String : AktuellerUnterordner = "/mama/Daten"
	Dim Speicherort As String
	Dim Liste1, Liste2 As List
	
	
End Sub

Sub Activity_Create(FirstTime As Boolean)
	If FirstTime Then
		CreatePreferenceScreen
		If manager.GetAll.Size = 0 Then SetDefaults
	End If
	
	Dim Map1 As Map
	Dim label1 As Label
	
	
	If File.ExternalWritable = False Then
	
        	Msgbox("Ich kann nicht auf die SD-Card schreiben.", "A c h t u n g")
			
        Return
    End If
	
	Map1.Initialize
		If File.Exists(File.DirRootExternal & AktuellerUnterordner, "SetupApp.txt") = True Then
		
			Map1 = File.ReadMap(File.DirRootExternal & AktuellerUnterordner, "SetupApp.txt")
			
		End If

	
    ListView1.Initialize("ListView1")
	label1 = ListView1.SingleLineLayout.Label 'set the label to the model label.
	label1.TextSize = 30
	label1.TextColor = Colors.Cyan
	label1.Gravity = Gravity.CENTER
	label1.Typeface = Typeface.DEFAULT_BOLD
	
	'Set the widget to update every 0 minutes.
	Zeitzone = Map1.GetDefault("Zeitzone",2)'2
	Anfangsgewicht = Map1.GetDefault("Anfangsgewicht",99)'* startgewicht99
	Zielgewicht =  Map1.GetDefault("Zielgewicht",86)'86
	Name = Map1.GetDefault("Name","Avita") 'Name = "Avi"
	Ausgabepreis = Map1.GetDefault("Ausgabepreis",300)'300
	MinLohn = Map1.GetDefault("MinLohn",0.2)'0.20
	MaxLohn = Map1.GetDefault("MaxLohn",5.0)'5
	Gewicht = Map1.GetDefault("Gewicht"," kg") ' kg oder lbs
	Waehrung = Map1.GetDefault("Waehrung","€")
	'Hintergrundfarbe = Map1.GetDefault("Hintergrundfarbe")
	'Schriftfarbe = Map1.GetDefault("Schriftfarbe")
	Kontrolltag = Map1.GetDefault("Kontrolltag",1)'2
	Starttag = Map1.GetDefault("Starttag","04/07/2015")'"04/07/2012" 'Starttag
	Startzeit = Map1.GetDefault("Startzeit","00:00:01")'"00:00:01"
	Zieltag = Map1.GetDefault("Zieltag","07/31/2015")'"07/31/2012" 'gibts den tag?
	Zielzeit = Map1.GetDefault("Zielzeit","23:59:59")'"23:59:59"
	StrafLohn = Map1.GetDefault("StrafLohn",1)'1		
	
	Auswahl'sprung zum listview
	
    Activity.AddView(ListView1, 0, 0, 100%x, 100%y)
	


End Sub

Sub SetDefaults
	'defaults are only set on the first run.
	manager.SetBoolean("check1", True)
	manager.SetBoolean("check2", False)
	manager.SetString("edit1", "Hello!")
	manager.SetString("list1", "Black")
End Sub


Sub Auswahl

	Dim bmp As Bitmap 
	bmp = LoadBitmap(File.dirassets,"mamalogo.png")

		
		ListView1.Clear
		
		ListView1.AddSingleLine("Personalien")	
			ListView1.AddTwoLinesAndBitmap("Personalien","Name, Wohnort, Verein, ...",bmp)
			ListView1.AddTwoLinesAndBitmap("Training","Intensität, Sportart, Ziele, ...",bmp)
			
		'ListView1.AddSingleLine("Netzwerke")	
		'	ListView1.AddTwoLinesAndBitmap("Netzwerke","Facebook, Twitter, Google+, ...",bmp)
			
		ListView1.AddSingleLine("Applikation")		
			'ListView1.AddTwoLinesAndBitmap("Einheiten","Gewichte, Maße, Währungen",bmp)
			ListView1.AddTwoLinesAndBitmap("Anfangsgewicht",Anfangsgewicht & Gewicht,bmp)
			ListView1.AddTwoLinesAndBitmap("Zielgewicht",Zielgewicht & Gewicht,bmp)
			'ListView1.AddTwoLinesAndBitmap("Lohn","Bezahlung für Kontrollöre",bmp)
			'ListView1.AddTwoLinesAndBitmap("Zeiten","Starttage,Trainingszeitenerien,...",bmp)
			'ListView1.AddTwoLinesAndBitmap("Extras","Eula,Farben,Lizensschlüssel",bmp)
			'ListView1.AddTwoLinesAndBitmap("Spenden","Paypal, ...",bmp)
			'ListView1.AddTwoLinesAndBitmap("Widget","Anzeigen, ...",bmp)
				
		'ListView1.AddSingleLine("Gewichte")	
			
			'ListView1.AddTwoLinesAndBitmap("screen0","",bmp)
			'ListView1.AddTwoLinesAndBitmap("screen1","",bmp)
			
		'ListView1.AddSingleLine("Gewichte")	
			'ListView1.AddTwoLinesAndBitmap("Gewicht", Gewicht,bmp)
			'ListView1.AddTwoLinesAndBitmap("Anfangsgewicht",Anfangsgewicht & Gewicht,bmp)
			'ListView1.AddTwoLinesAndBitmap("Zielgewicht",Zielgewicht & Gewicht,bmp)
			
	  	'ListView1.AddSingleLine("Preise")	
			'ListView1.AddTwoLinesAndBitmap("Währung", Waehrung,bmp)
			'ListView1.AddTwoLinesAndBitmap("Ausgabepreis gesamt",Ausgabepreis & Waehrung,bmp)
			'ListView1.AddTwoLinesAndBitmap("Minimaler Lohn",MinLohn & Waehrung,bmp)
			'ListView1.AddTwoLinesAndBitmap("Maximaler Lohn",MaxLohn & Waehrung,bmp)	
			'ListView1.AddTwoLinesAndBitmap("StrafLohn",StrafLohn & Waehrung,bmp)
			
'	  	ListView1.AddSingleLine("Zeiten")	
'			ListView1.AddTwoLinesAndBitmap("Kontrolltag Rythmus",Kontrolltag,bmp)
'			ListView1.AddTwoLinesAndBitmap("Starttag (MM/TT/JJJJ)",Starttag,bmp)
'			ListView1.AddTwoLinesAndBitmap("Startzeit",Startzeit,bmp)
'			ListView1.AddTwoLinesAndBitmap("Zieltag (MM/TT/JJJJ)",Zieltag,bmp)
'			ListView1.AddTwoLinesAndBitmap("Zielzeit",Zielzeit,bmp)
'			ListView1.AddTwoLinesAndBitmap("Zeitzone",Zeitzone,bmp)

    	ListView1.AddSingleLine("Extras")	
			ListView1.AddTwoLinesAndBitmap ("Eula","",bmp)
			ListView1.AddTwoLinesAndBitmap ("Farben","",bmp)
			ListView1.AddTwoLinesAndBitmap ("eMail-Feedback","",bmp)
			ListView1.AddTwoLinesAndBitmap ("Lizensschlüssel","" ,bmp)
			ListView1.AddTwoLinesAndBitmap ("Passwort","" ,bmp)
			ListView1.AddTwoLinesAndBitmap ("Spenden","",bmp)
		'ListView1.AddSingleLine ("Widgeds")
			'ListView1.AddTwoLinesAndBitmap ("Erfolge","",bmp)
			'ListView1.AddTwoLinesAndBitmap ("Ziele","",bmp)
		
    	ListView1.AddSingleLine("Farben")

			ListView1.AddTwoLinesAndBitmap("Hintergrundfarbe",Hintergrundfarbe,bmp)
			ListView1.AddTwoLinesAndBitmap("Schriftfarbe",Schriftfarbe,bmp)
			
		ListView1.AddSingleLine("Speichern")
			'ListView1.AddTwoLinesAndBitmap("Speicherort",Speicherort,bmp)
			ListView1.AddTwoLinesAndBitmap("Jetzt Abspeichern","Klicken Sie hier zum speichern", LoadBitmap(File.DirAssets,"mamaLogo.png"))
   






End Sub




Sub ListView1_ItemClick (Position As Int, Value As Object)


		Dim txt As String
		Dim cd, cdsf As ColorDialogHSV
		Dim color As Int
		Dim ret As Int
		Dim fd As FileDialog
		Dim Dd As DateDialog
		Dim td As TimeDialog
		Dim nd As NumberDialog
		Dim Id As InputDialog
		Dim Bmp As Bitmap
		Bmp.Initialize(File.DirAssets, "mamaLogo.png")
		
	Select Value
		
		
		Case "Personalien"
			
			StartActivity(screenPersonal.CreateIntent)
		
		
		
		Case "Training"
			StartActivity(screenTraining.CreateIntent)
				
		Case "Netzwerke"		
			StartActivity(screenNetzwerke.CreateIntent)
		
		
		
		
		
		Case "Zeiten"
			'Msgbox(":","Erklärung der Zeiten")
			
		Case "Gewichte"	
			'Msgbox(":","Erklärung der Gewichte")

		Case "screen0"
		
		
			StartActivity(screen.CreateIntent)	
			
		Case "screen1"
		
		
			StartActivity(screen.CreateIntent)	
			
			
			
			
		Case "Gewicht"
			Liste1.Initialize
			Liste1.Add("kg")
			Liste1.Add("lbs")
			ret = InputList(Liste1, "Einheit",0)
			
			Select ret
				Case 0
					Gewicht = " kg"
				Case 1
					Gewicht = " lbs"
				Case Else
					Msgbox("?","?")
					
				End Select
				
		Case "Währung"
			Liste2.Initialize
			Liste2.Add("Euro")
			Liste2.Add("Dollar")
			Liste2.Add("Pounds")
			ret = InputList(Liste2, "Geld Einheit",0)
			
			Select ret
				Case 0
					Waehrung = " €"
				Case 1
					Waehrung = " $"
				Case 2	
					Waehrung = " £"
				Case Else
					Msgbox("?","?")
					
				End Select		

		Case "Zeitzone"
			nd.Digits = 3
			nd.Number = Zeitzone
			nd.Decimal = 0
			nd.ShowSign = True
			ret = nd.Show("Zeitzone", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & nd.Number, False)
			Zeitzone = nd.Number
			'Anzeige.Text = "X" & nd.Number / Power(10, nd.Decimal)
		
		Case "Anfangsgewicht"
			nd.Digits = 3
			nd.Number = Anfangsgewicht
			nd.Decimal = 0
			'nd.ShowSign = True
			ret = nd.Show("Anfangsgewicht", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & nd.Number, False)
			Anfangsgewicht = nd.number / Power(10, nd.Decimal)
		
		Case "Zielgewicht"
			nd.Digits = 3
			nd.Number = Zielgewicht
			nd.Decimal = 0
			'nd.ShowSign = True
			ret = nd.Show("Zielgewicht", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & nd.number, False)
			Zielgewicht = nd.number / Power(10, nd.Decimal)
			
		Case "Name"
			Id.PasswordMode = False
			'Id.InputType = Id.INPUT_TYPE_DECIMAL_NUMBERS
			'Id.InputType = Id.INPUT_TYPE_NUMBERS
			'Id.InputType = Id.INPUT_TYPE_PHONE
			Id.InputType = Id.INPUT_TYPE_TEXT
			Id.input = Name
			Id.Hint = "Bitte Name eingeben!"
			Id.HintColor = Colors.ARGB(196, 255, 140, 0)
			ret = DialogResponse.CANCEL
			ret = Id.Show("Geben sie einen kurzen Text ein", "Fette Euro's", "Eingabe", "Abbruch", "", Bmp)
			'ToastMessageShow(ret & " : " & Id.input, False)	
			'ToastMessageShow(Id.input, False)
			Name = Id.input
		
		Case "Preise"
			'Msgbox(":","Preise")
		
		Case "Ausgabepreis gesamt" 
			nd.Digits = 5
			nd.Number = Ausgabepreis
			nd.Decimal = 0
			'nd.ShowSign = True
			ret = nd.Show("Ausgabepreis gesamt", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & nd.number, False)
			Ausgabepreis = nd.Number / Power(10, nd.Decimal)
			
		Case "StrafLohn" 
			nd.Digits = 5
			nd.Number = StrafLohn
			nd.Decimal = 2
			'nd.ShowSign = True
			ret = nd.Show("Starflohn", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & nd.number, False)
			StrafLohn = nd.Number / Power(10, nd.Decimal)	
		
		Case "Minimaler Lohn"			
			'Dim nd As NumberDialog
			nd.Digits = 5
			nd.Number = MinLohn
			nd.Decimal = 2
			'nd.ShowSign = True
			ret = nd.Show("Minimaler Tageslohn", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & nd.number, False)
			MinLohn = nd.number / Power(10, nd.Decimal)

		Case "Maximaler Lohn"
			'Dim nd As NumberDialog
			nd.Digits = 5
			nd.Number = MaxLohn
			nd.Decimal = 2
			'nd.ShowSign = True
			ret = nd.Show("Maximaler Tageslohn", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & nd.number, False)
			MaxLohn = nd.Number / Power(10, nd.Decimal)
		
		Case "Kontrolltag Rythmus"
			'Dim nd As NumberDialog
			nd.Digits = 2
			nd.Number = Kontrolltag
			nd.Decimal = 0
			'nd.ShowSign = True
			ret = nd.Show("Kontrolltag Rythmus", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & nd.number, False)
			Kontrolltag = nd.number / Power(10, nd.Decimal)
			
		Case "Starttag (MM/TT/JJJJ)"
			Dd.Year = DateTime.GetYear(DateTime.Now)
			Dd.Month = DateTime.GetMonth(DateTime.Now)	
			Dd.DayOfMonth = DateTime.GetDayOfMonth(DateTime.Now)
			ret = Dd.Show("Setzen Sie den Starttag", "Fette Euro's", "Eingabe", "Abbruch", "", Bmp)
			'ToastMessageShow(ret & " : " & Dd.DayOfMonth & "/" & Dd.Month & "/" & Dd.Year , False)
			Starttag = Dd.Month & "/" & Dd.DayOfMonth & "/" & Dd.Year

		Case "Startzeit"
			td.Hour = DateTime.GetHour(DateTime.Now)
			td.Minute = DateTime.GetMinute(DateTime.Now)
			td.Is24Hours = True
			ret = td.Show("Setzen Sie die Startzeit", "Fette Euro's", "Eingabe", "Abbruch", "", Bmp)
			'ToastMessageShow(ret & " : " & td.Hour & ":" & td.Minute, False)
			
			Startzeit = td.Hour & ":" & td.Minute
		
		Case "Zieltag (MM/TT/JJJJ)"
			Dd.Year = DateTime.GetYear(DateTime.Now)
			Dd.Month = DateTime.GetMonth(DateTime.Now)
			Dd.DayOfMonth = DateTime.GetDayOfMonth(DateTime.Now)
			ret = Dd.Show("Setze das Zieldatum", "Fette Euro's", "Eingabe", "Abbruch", "", Bmp)
			'ToastMessageShow(ret & " : " & Dd.DayOfMonth & "/" & Dd.Month & "/" & Dd.Year , False)
			Zieltag = Dd.Month & "/" & Dd.DayOfMonth & "/" & Dd.Year
		
		Case "Zielzeit"

			td.Hour = DateTime.GetHour(DateTime.Now)
			td.Minute = DateTime.GetMinute(DateTime.Now)
			td.Is24Hours = True
			ret = td.Show("Setzen Sie die Zielzeit", "Fette Euro#s", "Eingabe", "Abbruch", "", Bmp)
			'ToastMessageShow(ret & " : " & td.Hour & ":" & td.Minute, False)
			Zielzeit = td.Hour & ":" & td.Minute
		
		
		Case "Farben"
			'Msgbox(":","Erklärung der Farben")
		
		Case "Hintergrundfarbe"
		
			cd.Hue = 180
			cd.Saturation = 0.5
			cd.Value = 0.5
			ret = cd.Show("Hintergrundfarbe", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & cd.RGB, False)
			Activity.color = cd.RGB
			Hintergrundfarbe = cd.rgb
			
		Case "Schriftfarbe"
			'	Dim cd As ColorDialogHSV
			'	Dim color As Int
			cdsf.Hue = 180
			cdsf.Saturation = 0.5
			cdsf.Value = 0.5
			ret = cdsf.Show("Schriftfarbe", "Eingabe", "Abbruch", "", Bmp)	
			'ToastMessageShow(ret & " : " & cdsf.RGB, True)
			Schriftfarbe = cdsf.rgb
			
		Case "Speichern"
			'Msgbox(":","Abspeichern?")
			
			
		Case "Speicherort"
			
			fd.FastScroll = True
			fd.FilePath = File.DirRootExternal & AktuellerUnterordner ' also sets ChosenName to an emtpy string
			'fd.ShowOnlyFolders = true
			fd.FileFilter = "*.*" ' for example or ".jpg,.png" for multiple file types
			ret = fd.Show("Datei Abspeichern", "Eingabe", "Abbruch", "", Bmp)	
			ToastMessageShow(ret & " : Path : " & fd.FilePath & CRLF & "File : " & fd.ChosenName, False)
			Speicherort = fd.FilePath & "/" & fd.ChosenName
		
		Case "Jetzt Abspeichern"
			Dim result As Int
			
			Dim Map1 As Map
	    		Map1.Initialize
	   			Map1.Put("Name",Name)
				Map1.Put("Gewicht",Gewicht)
	   			Map1.Put("Anfangsgewicht",Anfangsgewicht)
				Map1.Put("Zielgewicht",Zielgewicht)
	   		'	Map1.Put("Waehrung",Waehrung)
			'	Map1.Put("Ausgabepreis",Ausgabepreis)
			'	Map1.Put("MinLohn",MinLohn)
			'	Map1.Put("MaxLohn",MaxLohn)
			'	Map1.Put("Kontrolltag",Kontrolltag)
	   			Map1.Put("Starttag",Starttag)
				Map1.Put("Startzeit",Startzeit)
				Map1.Put("Zieltag",Zieltag)
				Map1.Put("Zielzeit",Zielzeit)
				Map1.Put("Zeitzone",Zeitzone)
			'	Map1.Put("Straflohn",StrafLohn)
				Map1.Put("Hintergrundfarbe",Hintergrundfarbe)
				Map1.Put("Schriftfarbe",Schriftfarbe)
	 

			
			result = Msgbox2("Starttag = " & Starttag& CRLF & "Startzeit = " & Startzeit& CRLF & "Zieltag = " & Zieltag& CRLF & "Zielzeit = " & Zielzeit& CRLF & "Zeitzone = " & Zeitzone , "Speichern und Beenden" , "Ja","Nein","",LoadBitmap(File.DirAssets,"mamaLogo.png"))
			If result = DialogResponse.POSITIVE Then 
				File.WriteMap(File.DirRootExternal & AktuellerUnterordner, "SetupApp.txt", Map1)
				ToastMessageShow("Erfolgreich gespeichert",False)
				Activity.Finish
			Else
			End If
		
		Case Else
		
		ToastMessageShow("Noch in Bearbeitung",False)

	End Select
		
		Auswahl
	
	
End Sub


Sub CreatePreferenceScreen
	screen.Initialize("Einstellungen", "xxx")
	screenPersonal.Initialize("Personalien","")
	screenTraining.Initialize("screen1","")
	
	

	Dim intentCat, cat1, cat2,KatNetzwerke, KatPersonal, KatTraining As AHPreferenceCategory
	Dim intentscreen, intentscreen1 As AHPreferenceScreen
	
	intentCat.Initialize("Geräte einstellungen")
	intentCat.AddCheckBox("intentenable", "Enable Intent Settings", "Intent settings are enabled", "Intent settings are disabled", True, "")
	intentscreen.Initialize("Intents", "Examples with Intents")
	intentscreen.AddCheckBox("chkwifi", "Enable Wifi Settings", "Wifi settings enabled", "Wifi settings disabled", False, "")

	' Use an B4A Intent
	Dim In As Intent
	In.Initialize("android.settings.WIFI_SETTINGS", "")
	intentscreen.AddIntent( "Wifi Settings", "Example for custom Intent", In, "chkwifi")

	' Intent from GPS-Library
	'intentScreen.AddIntent("GPS Settings", "Start Android GPS settings", g.LocationSettingsIntent, "")

	' Call installed Application
	Dim pm As PackageManager
	Dim pl As List
	pl = pm.GetInstalledPackages
	' If the Calculator is installed, call it
	If pl.IndexOf("com.android.calculator2") > 0 Then
		intentscreen.AddIntent("Calculator", "Open calculator", pm.GetApplicationIntent("com.android.calculator2"), "")
	End If
	
	' Open Webbrowser using PhoneIntents
	Dim pi As PhoneIntents
	intentscreen.AddIntent("Browser", "Open http://www.google.de", pi.OpenBrowser("http://www.google.com"), "")

	' Add the screen with the intents calls to the category
	intentCat.AddPreferenceScreen(intentscreen, "intentenable")
	
	cat1.Initialize("Examples")
	cat1.AddCheckBox("check1", "Checkbox1", "This is Checkbox1 without second summary", "", True, "")
	cat1.AddEditText("edit1", "EditText1", "This is EditText1", "", "check1")
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
	
	
	
	'---------------------------------liste zum kopieren--------------
	
	cat1.Initialize("Examples")
	cat1.AddCheckBox("check1", "Checkbox1", "This is Checkbox1 without second summary", "", True, "")
	cat1.AddEditText("edit1", "EditText1", "This is EditText1", "", "check1")
	cat1.AddPassword("pwd1", "Password1", "This is a password", "", "")
	cat1.AddRingtone("ring1", "Ringtone1", "This is a Ringtone", "", "", cat1.RT_NOTIFICATION)
	
	
	
	
	
'---------------------eigene dateien---------------	
	
	KatPersonal.Initialize("Personalien")
	KatPersonal.AddEditText("Vorname", "Vorname", "Hier den Vornamen eintragen", "", "")
	KatPersonal.AddEditText("Nachname", "Nachname", "Hier den Nachnamen eintragen", "", "")
	KatPersonal.AddEditText("Telefonnummer", "Telefonnummer", "HIer die Telefonnummer eintragen", "", "")
	KatPersonal.AddPassword("Passwort", "Passwort", "Hier das Passwort eintragen", "", "")
	KatTraining.Initialize("Training")
	Dim mapIntensität As Map
	mapIntensität.Initialize
	mapIntensität.Put(0, "Abtrainieren - Modus")
	mapIntensität.Put(1, "Mobilisieren - Modus")
	mapIntensität.Put(2, "Ohne Geräte - Modus")
	mapIntensität.Put(3, "Fitness - Modus")
	mapIntensität.Put(4, "Knallhart - Modus")
	mapIntensität.Put(5, "Wettkampf - Modus")
	mapIntensität.Put(6, "Bundeslandmeister - Modus")
	mapIntensität.Put(7, "Deutscher Meister - Modus")
	mapIntensität.Put(8, "Bruce Lee - Modus")
	mapIntensität.Put(9, "Europameister - Modus")
	mapIntensität.Put(10, "Muhammad Ali - WeltmeisterModus")
	mapIntensität.Put(11, "Chuck Norris - GalaxyModus")
	KatTraining.AddList2("Intensitaet", "Intensität", "Wie hart soll das Training sein?", 3, "", mapIntensität)
	
	'---------------------------------------------
	
	KatNetzwerke.Initialize("Netzwerke")
	KatNetzwerke.AddCheckBox("Facebook", "Facebook", "Facebook wird genutzt", "Facebook wird nicht genutzt", True, "")
	intentscreen1.Initialize("Facebook Einstellungen", "Zugang, Erlaubnis, ...")
'	intentscreen1.AddCheckBox("Datensendung", "Daten senden", "Daten an Facebook senden", "Keine Daten an Facebook senden", False, "Facebook")
'		Dim Face As PhoneIntents
'		intentscreen1.AddIntent("Facebook", "Open http://www.facebook.com", pi.OpenBrowser("http://www.Facebook.com"), "")
		
'	KatNetzwerke.AddPreferenceScreen(intentscreen1, "Facebook")
		
'			ListView1.AddTwoLinesAndBitmap ("Wer-kennt-Wen","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("Xing","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("Linkedin","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("Twitter","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("Kampfkunstforum","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("Internetseite","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("Youtube","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("KampfsportTV","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("OpenSports","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("Skype","",bmp)
'			ListView1.AddTwoLinesAndBitmap ("Jappi","",bmp)
'	
	
	
		
	'add the categories to the main screen
	

		
			screen.AddPreferenceCategory(intentCat)
			screen.AddPreferenceCategory(cat1)
			screen.AddPreferenceCategory(cat2)
			
			'screen1.AddPreferenceCategory(cat2)

			screenPersonal.AddPreferenceCategory(KatPersonal)
			'screenPersonal.AddPreferenceCategory(intentCat)
			
			screenTraining.AddPreferenceCategory(KatTraining)
			'screenTraining.AddPreferenceCategory(cat1)
	
			'screenNetzwerke.AddPreferenceCategory(KatNetzwerke)
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
