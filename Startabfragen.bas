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
	Dim image As Bitmap
	Dim myZip As ABZipUnzip
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim strLizensnummer, strPassword As String
	Dim btnPasswort, btnNutzerbedingungen, btnLizensnummer As Button
	Dim trLizens, trPassword As TextReader
	Dim twLizens, twPassword As TextWriter
	Dim InputTextPass, InputTextLizens As InputDialog
	Dim btnDownload As Button


End Sub

Sub Activity_Create(FirstTime As Boolean)

	
		Activity.Title = "Lizens, Nutzerbedingungen, Passwort"
	

		If File.ExternalWritable = False Then
	     		Msgbox("Ich kann nicht auf die SD-Karte schreiben.", "")
	      	Return
		End If
		
'			Activity.LoadLayout("1")
'			image.Initialize(
'			'check if we already loaded the image previously.
'			If image.IsInitialized Then
'				Activity.SetBackgroundImage(image)
'			End If
		'OrdnerEinrichten
		Passwortabfrage
		Lizensnummernabfrage
		Nutzerbedingungen
		
		



End Sub

Sub Activity_Resume
	'check if download has finished while the activity was paused
	If btnDownload.Enabled = False AND DownloadService.JobStatus = DownloadService.STATUS_DONE Then
		FinishDownload
	End If





End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub



Sub Passwortabfrage
'	StartActivity(Werbung)
'	DoEvents
	
	Activity.Title = Main.ActivityTitel
	
		If File.Exists(File.DirRootExternal & Main.Unterordner1, "password_file.txt") = True Then 'Check to see if there is already a password file saved and therefore a custom password
			trPassword.Initialize(File.OpenInput(File.DirAssets, "password_file.txt")) 
			strPassword = trPassword.ReadLine
			trPassword.Close ' Load the password into strPassword
		Else
			strPassword = "0000" ' If there is no password file then set strPassword to default
		End If
		
		Do While InputTextPass.Input <> strPassword ' Keep displaying the input password input dialog until the correct password has been entered
			If InputTextPass.Show("Bitte geben Sie das Passwort ein.","Passwort","Prüfen","","Exit",Null) = DialogResponse.NEGATIVE Then ' If the user selects exit then close the activity
				
			End If
			If InputTextPass.Input <> strPassword Then ' Display wrong password message if wrong password entered
				ToastMessageShow("Falsches Passwort, Nochmal versuchen", False)
				ExitApplication
			End If
		Loop
	
	'Button Passworteingabe
'	btnPasswort.Initialize("btnPasswort") 
'	btnPasswort.Text = "Password andern"
'	Activity.AddView(btnPasswort,0,40,100%x,50dip)' Display button btnPasswort
End Sub
'
'Sub btnPasswort_Click
'	If InputTextPass.Show("Bitte geben Sie Ihr Passwort ein.","Passwort","Senden","","Exit",Null) = DialogResponse.POSITIVE Then ' Store the contents of the new password textbox, overwriting anything else already there
'		twPassword.Initialize(File.OpenOutput(File.DirRootExternal & Main.Unterordner1, "password_file.txt", False))
'		twPassword.WriteLine(InputTextPass.Input)
'		twPassword.Close
'		ToastMessageShow("Das Passwort ist erneuert", False)
'	Else ' If the user selects exit then close the activity
'		
'	End If
'End Sub


Sub Activity_KeyPress (KeyCode As Int) As Boolean                            
    If KeyCode = KeyCodes.KEYCODE_BACK Then                                    

		ExitApplication
    Else
        Return False
    End If 
End Sub



Sub Lizensnummernabfrage
'	StartActivity(Werbung)
'	DoEvents
	
	Activity.Title = Main.ActivityTitel
	
		If File.Exists(File.DirRootExternal & Main.Unterordner1, "Lizens_file.txt") = True Then 'Check to see if there is already a password file saved and therefore a custom password
			trLizens.Initialize(File.OpenInput(File.DirAssets, "Lizens_file.txt")) 
			strLizensnummer = trLizens.ReadLine
			trLizens.Close ' Load the password into strPassword
		Else
			strLizensnummer = "aaa" ' If there is no password file then set strPassword to default
		End If
		
		Do While InputTextLizens.Input <> strLizensnummer ' Keep displaying the input password input dialog until the correct password has been entered
			If InputTextLizens.Show("Bitte geben Sie bitte Ihre 30 stellige Lizensnummer ein. (a)","Lizensnummer","Prüfen","","Beenden",Null) = DialogResponse.NEGATIVE Then ' If the user selects exit then close the activity
					
			End If
			If InputTextLizens.Input <> strLizensnummer Then ' Display wrong password message if wrong password entered
				ToastMessageShow("Falsche Lizensnummer, Nochmal versuchen", False)
				ExitApplication
			End If
		Loop
	
'	Button Lizensnummereingabe neu
'	btnLizensnummer.Initialize("btnLizensnummer") 
'	btnLizensnummer.Text = "Lizensnummer ändern"
'	Activity.AddView(btnLizensnummer,0,0,100%x,50dip)' Display button btnLizensnummer
End Sub
'
'Sub btnLizensnummer_Click
'	If InputTextLizens.Show("Bitte geben Sie Ihre Lizensnummer ein.","Lizensnummer","Prüfen","","Beenden",Null) = DialogResponse.POSITIVE Then ' Store the contents of the new password textbox, overwriting anything else already there
'		twLizens.Initialize(File.OpenOutput(File.DirRootExternal & Main.Unterordner1, "Lizens_file.txt", False))
'		twLizens.WriteLine(InputTextLizens.Input)
'		twLizens.Close
'		ToastMessageShow("Lizensnummer erneuert", False)
'	Else ' If the user selects exit then close the activity
'
'		
'	End If
'End Sub




Sub Nutzerbedingungen
'	
	
			Dim TXT As String
			TXT = "Nutzerbedingungen" & CRLF
			Dim result As Int
					result = Msgbox2(File.ReadString(File.DirAssets,"Nutzerbedingungen.txt"),TXT,"Annehmen", "Abbruch", "Ablehnen", Null)
			
			If result = DialogResponse.CANCEL Then
					ExitApplication
						
				Else
			End If
			
			If result = DialogResponse.Positive Then 
					Activity.Finish
				Else
			End If

			If result = DialogResponse.NEGATIVE Then 
					ExitApplication
				Else
			End If

	
	
End Sub


Sub OrdnerEinrichten


'----------------- Ordner erstellen und füllen ------------------
	
		'	Main.Unterordner =  "/mama/Erwaermung"	
		'	Main.Hauptordner0 = "/mama"
		'	Main.Unterordner1 = "/mama/Daten"
		'	Main.Unterordner2 = "/mama/AllgBodyWeightExercices"
		'	Main.Unterordner3 = "/mama/AllgCoolDown"
		'	Main.Unterordner4 = "/mama/AllgDehnung"
		'	Main.Unterordner5 = "/mama/AllgErwaermung"
		'	Main.Unterordner6 = "/mama/KarateEinzel"
		'	Main.Unterordner7 = "/mama/KarateErwaermung"
		'	Main.Unterordner8 = "/mama/KarateKata"
		'	Main.Unterordner9 = "/mama/KarateKumite"
		'	Main.Unterordner10 = "/mama/KarateKihon"
		'	Main.Unterordner11 = "/mama/Karate"
		'	Main.Unterordner12 = "/mama/KickErwaermung"
		'	Main.Unterordner13 = "/mama/KickEinzel"
		'	Main.Unterordner14 = "/mama/Kickpartner"
		'	Main.Unterordner15 = "/mama/"
		'	Main.Unterordner16 = "/mama/"
		'	Main.Unterordner17 = "/mama/"
		'	Main.Unterordner18 = "/mama/"
		'	Main.Unterordner19 = "/mama/"
		'	Main.Unterordner20 = "/mama/ATS"
			
	
	
		If File.Exists(File.DirRootExternal  & Main.Unterordner1,"SetupApp.txt") = False Then File.MakeDir(File.DirRootExternal , Main.Hauptordner0)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner1 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner1)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner2 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner2)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner3 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner3)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner4 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner4)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner5 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner5)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner6 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner6)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner7 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner7)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner8 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner8)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner9 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner9)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner10 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner10)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner11 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner11)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner12 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner12)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner13 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner13)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner14 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner14)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner15 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner15)
'		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner16 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner16)
'		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner17 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner17)
'		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner18 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner18)
'		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner19 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner19)
		If	File.IsDirectory(File.DirRootExternal  , Main.Unterordner20 ) = False Then File.MakeDir(File.DirRootExternal  , Main.Unterordner20)
		
		Dim txt1, txt As String
		txt1 = " Wollen Sie jetzt die zur App gehörende Datei ´mama.zip´ kostenlos von meiner Homepage ´mama.watchkido.de´ aus dem Internet laden und in den Ordner ´/mama´ entpacken? ( <25MB ca. 10 min nur über WLAN!) Diese App funktioniert nur mit diesen Bildern. Der Ordner ist laut ´ZONE-ALARM by Check Point´ virenfrei."
		txt = "Bilder-Ordner downloaden?"
		Dim result As Int
					result = Msgbox2(txt1,txt,"JA", "Abbruch", "NEIN", Null)
			
			If result = DialogResponse.CANCEL Then
					Main.IckWarAllHier = True
					Activity.Finish
						
				Else
			End If
			
			If result = DialogResponse.Positive Then 
					Activity.Color = Colors.Black
					DownloadService.URL = "https://sites.google.com/site/"
					DownloadService.Target = File.OpenOutput(File.DirRootExternal , "mama.zip", False)
					StartService(DownloadService)
					btnDownload.Enabled = False
					
					ToastMessageShow("MAMA-Ordner wird auf SD-Card Eingerichtet", False)
					
				Else
			End If

			If result = DialogResponse.NEGATIVE Then 
					Main.IckWarAllHier = True
					Activity.Finish
				Else
				
			End If


			



End Sub

Sub FinishDownload
	'Load the saved image
	If DownloadService.DoneSuccessfully = True Then
	
	myZip.ABUnzip(File.DirRootExternal & "mama.zip",File.DirRootExternal)'  & "target")
	
	Msgbox("´mama.zip´ ist entpackt. Sie können mit der Software arbeiten.","Download komplett")
'		image = LoadBitmapSample(File.DirInternalCache, "image.png", _
'		 100%x, 100%y)
'		Activity.SetBackgroundImage(image)
	End If
	btnDownload.Enabled = True
	DownloadService.JobStatus = DownloadService.STATUS_NONE
	
	Main.IckWarAllHier = True
	
	Activity.Finish
	
	
	
End Sub










