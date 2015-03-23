Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region


'Activity module
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	Dim ListView1 As ListView
	Dim Anfangsgewicht, Zielgewicht, Kontrolltag, Zeitzone As Int
	'Dim Schriftfarbe, Hintergrundfarbe As Int
	Dim MaxLohn, MinLohn, Ausgabepreis,StrafLohn As Double
	Dim Name, Gewicht, Waehrung As String
	Dim Starttag, Zieltag, Startzeit, Zielzeit As String
	Dim AktuellerUnterordner As String : AktuellerUnterordner = "/mama/Daten"
	'dim Speicherort As String
	Dim Liste1, Liste2 As List
	
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
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
	label1.TextColor = Colors.blue
	label1.Gravity = Gravity.CENTER
	
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
	Starttag = Map1.GetDefault("Starttag","04/07/2012")'"04/07/2012" 'Starttag
	Startzeit = Map1.GetDefault("Startzeit","00:00:01")'"00:00:01"
	Zieltag = Map1.GetDefault("Zieltag","07/31/2012")'"07/31/2012" 'gibts den tag?
	Zielzeit = Map1.GetDefault("Zielzeit","23:59:59")'"23:59:59"
	StrafLohn = Map1.GetDefault("StrafLohn",1)'1		
	
	Auswahl'sprung zum listview
	
    Activity.AddView(ListView1, 0, 0, 100%x, 100%y)
    
End Sub

Sub ListView1_ItemClick (Position As Int, Value As Object)


		'Dim txt As String
		'Dim cd, cdsf As ColorDialogHSV
		'Dim color As Int
		Dim ret As Int
		Dim fd As FileDialog
		Dim Dd As DateDialog
		Dim td As TimeDialog
		Dim nd As NumberDialog
		Dim Id As InputDialog
		Dim Bmp As Bitmap
		Bmp.Initialize(File.DirAssets, "mamaLogo.png")
		
	Select Value
		Case "Einstellungen"
				Msgbox(":","Einstellungen")
		
		Case "Zeiten"
			Msgbox(":","Erklärung der Zeiten")
			
		Case "Gewichte"	
			Msgbox(":","Erklärung der Gewichte")
			
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
			Msgbox(":","Preise")
		
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
		
		
	'	Case "Farben"
	'		Msgbox(":","Erklärung der Farben")
	'	
	'	Case "Hintergrundfarbe"
	'	
	'		cd.Hue = 180
	'		cd.Saturation = 0.5
	'		cd.Value = 0.5
	'		ret = cd.Show("Hintergrundfarbe", "Eingabe", "Abbruch", "", Bmp)	
	'		'ToastMessageShow(ret & " : " & cd.RGB, False)
	'		Activity.color = cd.RGB
	'		Hintergrundfarbe = cd.rgb
	'		
	'	Case "Schriftfarbe"
	'		'	Dim cd As ColorDialogHSV
	'		'	Dim color As Int
	'		cdsf.Hue = 180
	'		cdsf.Saturation = 0.5
	'		cdsf.Value = 0.5
	'		ret = cdsf.Show("Schriftfarbe", "Eingabe", "Abbruch", "", Bmp)	
	'		'ToastMessageShow(ret & " : " & cdsf.RGB, True)
	'		Schriftfarbe = cdsf.rgb
			
		Case "Speichern"
			Msgbox(":","Abspeichern?")
			
			
	'	Case "Speicherort"
	'		
	'		fd.FastScroll = True
	'		fd.FilePath = File.DirRootExternal & AktuellerUnterordner ' also sets ChosenName to an emtpy string
	'		'fd.ShowOnlyFolders = true
	'		fd.FileFilter = ".txt" ' for example or ".jpg,.png" for multiple file types
	'		ret = fd.Show("Datei Abspeichern", "Eingabe", "Abbruch", "", Bmp)	
	'		ToastMessageShow(ret & " : Path : " & fd.FilePath & CRLF & "File : " & fd.ChosenName, False)
	'		Speicherort = fd.FilePath & "/" & fd.ChosenName
		
		Case "Jetzt Abspeichern"
			Dim result As Int
			
			Dim Map1 As Map
	    		Map1.Initialize
	   			Map1.Put("Name",Name)
				Map1.Put("Gewicht",Gewicht)
	   			Map1.Put("Anfangsgewicht",Anfangsgewicht)
				Map1.Put("Zielgewicht",Zielgewicht)
	   			Map1.Put("Waehrung",Waehrung)
				Map1.Put("Ausgabepreis",Ausgabepreis)
				Map1.Put("MinLohn",MinLohn)
				Map1.Put("MaxLohn",MaxLohn)
				Map1.Put("Kontrolltag",Kontrolltag)
	   			Map1.Put("Starttag",Starttag)
				Map1.Put("Startzeit",Startzeit)
				Map1.Put("Zieltag",Zieltag)
				Map1.Put("Zielzeit",Zielzeit)
				Map1.Put("Zeitzone",Zeitzone)
				Map1.Put("Straflohn",StrafLohn)
	'			Map1.Put("Hintergrundfarbe",Hintergrundfarbe)
	'			Map1.Put("Schriftfarbe",Schriftfarbe)
	 

			
			result = Msgbox2("Name = " & Name& CRLF & 	"Gewicht = " & Gewicht& CRLF & "Anfangsgewicht = " & Anfangsgewicht& CRLF & "Zielgewicht = " & Zielgewicht& CRLF & "Waehrung = " & Waehrung& CRLF & "Ausgabepreis = " & Ausgabepreis& CRLF &"MinLohn = " & MinLohn& CRLF & "MaxLohn = " & MaxLohn& CRLF & "Kontrolltag = " & Kontrolltag& CRLF & "Starttag = " & Starttag& CRLF & "Startzeit = " & Startzeit& CRLF & "Zieltag = " & Zieltag& CRLF & "Zielzeit = " & Zielzeit& CRLF & "Zeitzone = " & Zeitzone & CRLF & "Straflohn = " & StrafLohn, "Speichern und Beenden" , "Ja","Nein","",LoadBitmap(File.DirAssets,"mamaLogo.png"))
			If result = DialogResponse.POSITIVE Then 
				File.WriteMap(File.DirRootExternal & AktuellerUnterordner, "SetupApp.txt", Map1)
				ToastMessageShow("Erfolgreich gespeichert",False)
				Activity.Finish
			Else
			End If
		
		Case Else
		
		ToastMessageShow("Fehler",False)

	End Select
		
		Auswahl
	
	
End Sub

Sub Auswahl



	ListView1.Clear
	
	
    	ListView1.AddSingleLine("Einstellungen")
			ListView1.AddTwoLines("Name",Name)
		ListView1.AddSingleLine("Gewichte")	
			ListView1.AddTwoLines("Gewicht", Gewicht)
			ListView1.AddTwoLines("Anfangsgewicht",Anfangsgewicht & Gewicht)
			ListView1.AddTwoLines("Zielgewicht",Zielgewicht & Gewicht)
	  	ListView1.AddSingleLine("Preise")	
			ListView1.AddTwoLines("Währung", Waehrung)
			ListView1.AddTwoLines("Ausgabepreis gesamt",Ausgabepreis & Waehrung)
			ListView1.AddTwoLines("Minimaler Lohn",MinLohn & Waehrung)
			ListView1.AddTwoLines("Maximaler Lohn",MaxLohn & Waehrung)	
			ListView1.AddTwoLines("StrafLohn",StrafLohn & Waehrung)	
	  	ListView1.AddSingleLine("Zeiten")	
			ListView1.AddTwoLines("Kontrolltag Rythmus",Kontrolltag)
			ListView1.AddTwoLines("Starttag (MM/TT/JJJJ)",Starttag)
			ListView1.AddTwoLines("Startzeit",Startzeit)
			ListView1.AddTwoLines("Zieltag (MM/TT/JJJJ)",Zieltag)
			ListView1.AddTwoLines("Zielzeit",Zielzeit)
			ListView1.AddTwoLines("Zeitzone",Zeitzone)
    	ListView1.AddSingleLine("Extras")	
			ListView1.AddTwoLines2 ("Eula","",4301)
			ListView1.AddTwoLines2 ("Farben","",4302 )
			ListView1.AddTwoLines2 ("eMail-Feedback","",4303 )
			ListView1.AddTwoLines2 ("Lizensschlüssel","",4304 )
			ListView1.AddTwoLines2 ("Passwort","",4305 )
			ListView1.AddTwoLines2 ("Spenden","",4306)
			ListView1.AddSingleLine2 ("Widgeds",3000)
			ListView1.AddTwoLines2 ("Erfolge","",4308 )
			ListView1.AddTwoLines2 ("Ziele","",4309)
			ListView1.AddSingleLine2 ("Soziale Netzwerke",3000)
			ListView1.AddTwoLines2 ("Facebook","",4309)
			ListView1.AddTwoLines2 ("Wer-kennt-Wen","",4309)
			ListView1.AddTwoLines2 ("Xing","",4309)
			ListView1.AddTwoLines2 ("Linkedin","",4309)
			ListView1.AddTwoLines2 ("Twitter","",4309)
			ListView1.AddTwoLines2 ("Kampfkunstforum","",4309)
			ListView1.AddTwoLines2 ("Internetseite","",4309)
			ListView1.AddTwoLines2 ("Youtube","",4309)
			ListView1.AddTwoLines2 ("KampfsportTV","",4309)
			ListView1.AddTwoLines2 ("OpenSports","",4309)
			ListView1.AddTwoLines2 ("Skype","",4309)
			ListView1.AddTwoLines2 ("Jappi","",4309)

'    	ListView1.AddSingleLine("Farben")

'			ListView1.AddTwoLines("Hintergrundfarbe",Hintergrundfarbe)
'			ListView1.AddTwoLines("Schriftfarbe",Schriftfarbe)
			ListView1.AddSingleLine("Speichern")
			'ListView1.AddTwoLines("Speicherort",Speicherort)
			ListView1.AddTwoLinesAndBitmap("Jetzt Abspeichern","Klicken Sie hier zum speichern", LoadBitmap(File.DirAssets,"mamaLogo.png"))
   
   ' Activity.AddView(ListView1, 0, 0, 100%x, 100%y)





End Sub


Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub




