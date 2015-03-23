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
	
	Dim Timedelay As Int 	: 	Timedelay = 3000
	Dim Runden As Int 		:	Runden = 3
	Dim Kampfzeit As Int	:	Kampfzeit = 10000 '120000 = 2 min
	Dim Pause As Int		:	Pause = 10000 ' 60000 = 1 min ; 1000 = 1 sec
	Dim WettkampftimerLautstaerke As Int
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim tabs As AHViewPagerTabs
	Dim line As Panel
	Dim spnVorgabe, spnTimedelay, spnKampfzeit, spnPause, spnRunden As Spinner
	
	
End Sub

Sub Activity_Create(FirstTime As Boolean)
		Activity.title = "Einstellungen Timer"
		Activity.LoadLayout("EinstellungenWettkampf")

				spnVorgabe.Prompt = "Vorgegebene Zeiten:"
				spnVorgabe.Add("06:30 = 5 Runden á 30s")
				spnVorgabe.Add("08:00 = 6 Runden á 30s")
				spnVorgabe.Add("09:30 = 7 Runden á 30s")
				spnVorgabe.Add("11:00 = 8 Runden á 30s")
				spnVorgabe.Add("12:30 = 9 Runden á 30s")
				spnVorgabe.Add("14:00 = 10 Runden á 30s")
				spnVorgabe.Add("15:30 = 11 Runden á 30s")
				spnVorgabe.Add("17:00 = 12 Runden á 30s")
				spnVorgabe.Add("01:30 = 1 Runden á 1 min")
				spnVorgabe.Add("02:30 = 1 Runden á 2 min")
				spnVorgabe.Add("03:30 = 1 Runden á 3 min")
				spnVorgabe.Add("03:30 = 2 Runden á 1 min")
				spnVorgabe.Add("04:30 = 1 Runden á 4 min")
				spnVorgabe.Add("05:30 = 2 Runden á 2 min")
				spnVorgabe.Add("05:30 = 3 Runden á 1 min")
				spnVorgabe.Add("07:30 = 2 Runden á 3 min")
				spnVorgabe.Add("07:30 = 4 Runden á 1 min")
				spnVorgabe.Add("08:30 = 3 Runden á 2 min")
				spnVorgabe.Add("09:30 = 2 Runden á 4 min")
				spnVorgabe.Add("09:30 = 5 Runden á 1 min")
				spnVorgabe.Add("11:30 = 3 Runden á 3 min")
				spnVorgabe.Add("11:30 = 4 Runden á 2 min")
				spnVorgabe.Add("11:30 = 6 Runden á 1 min")
				spnVorgabe.Add("13:30 = 7 Runden á 1 min")
				spnVorgabe.Add("14:30 = 3 Runden á 4 min")
				spnVorgabe.Add("14:30 = 5 Runden á 2 min")
				spnVorgabe.Add("15:30 = 4 Runden á 3 min")
				spnVorgabe.Add("17:30 = 6 Runden á 2 min")
				spnVorgabe.Add("19:30 = 4 Runden á 4 min")
				spnVorgabe.Add("19:30 = 5 Runden á 3 min")
				spnVorgabe.Add("20:30 = 7 Runden á 2 min")
				spnVorgabe.Add("23:30 = 6 Runden á 3 min")
				spnVorgabe.Add("24:30 = 5 Runden á 4 min")
				spnVorgabe.Add("27:30 = 7 Runden á 3 min")
				spnVorgabe.Add("29:30 = 6 Runden á 4 min")
				spnVorgabe.Add("34:30 = 7 Runden á 4 min")
				spnVorgabe.SelectedIndex = 23
				
				spnTimedelay.Prompt = "Kampfbeginn:"
				spnTimedelay.add("in 3 sec")
				spnTimedelay.add("in 10 sec")
				spnTimedelay.add("in 20 sec")
				spnTimedelay.add("in 30 sec")
				spnTimedelay.add("in 40 sec")
				spnTimedelay.add("in 50 sec")
				spnTimedelay.add("in 1 min")
				spnTimedelay.SelectedIndex = 0
				
				spnRunden.Prompt = "Rundenanzahl:"
				spnRunden.add("1 Runde")
				spnRunden.add("2 Runden")
				spnRunden.add("3 Runden")
				spnRunden.add("4 Runden")
				spnRunden.add("5 Runden")
				spnRunden.add("6 Runden")
				spnRunden.add("7 Runden")
				spnRunden.add("8 Runden")
				spnRunden.add("9 Runden")
				spnRunden.add("10 Runden")
				spnRunden.add("11 Runden")
				spnRunden.add("12 Runden")
				spnRunden.add("13 Runden")
				spnRunden.add("14 Runden")
				spnRunden.add("15 Runden")
				spnRunden.add("16 Runden")
				spnRunden.add("17 Runden")
				spnRunden.add("18 Runden")
				spnRunden.add("19 Runden")
				spnRunden.SelectedIndex = 2
				
				spnKampfzeit.Prompt = "Kampfzeit:"
				spnKampfzeit.Add("10 sec")
				spnKampfzeit.Add("20 sec")
				spnKampfzeit.Add("30 sec")
				spnKampfzeit.Add("40 sec")
				spnKampfzeit.Add("50 sec")
				spnKampfzeit.Add("1 min")
				spnKampfzeit.Add("2 min")
				spnKampfzeit.Add("3 min")
				spnKampfzeit.Add("4 min")
				spnKampfzeit.Add("5 min")
				spnKampfzeit.SelectedIndex = 6
				
				spnPause.Prompt = "Pausenzeit:"
			  	spnPause.Add("15 sec")
				spnPause.Add("30 sec")
				spnPause.Add("45 sec")
				spnPause.Add("01:00 min")
				spnPause.Add("01:15 min")
				spnPause.Add("01:30 min")
				spnPause.Add("01:45 min")
				spnPause.Add("02:00 min")
			  	spnPause.Add("02:15 min")
				spnPause.Add("02:30 min")
				spnPause.Add("02:45 min")				
				spnPause.Add("03:00 min")
			  	spnPause.Add("03:15 min")
				spnPause.Add("03:30 min")
				spnPause.Add("03:45 min")				
				spnPause.Add("04:00 min")
			  	spnPause.Add("04:15 min")
				spnPause.Add("04:30 min")
				spnPause.Add("04:45 min")				
				spnPause.Add("05:00 min")
				spnPause.SelectedIndex = 3

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Sub spnVorgabe_ItemClick (Position As Int, Value As Object)

		Timedelay = 10000 '10 sec
		Pause = 60000'60 sec
		
		Select Position
		
			Case 0 	
				Runden = 5 
				Kampfzeit = 30000
			Case 1 
				Runden = 6 
				Kampfzeit = 30000
			Case 2 
				Runden = 7 
				Kampfzeit = 30000
			Case 3 
				Runden = 8 
				Kampfzeit = 30000
			Case 4 
				Runden = 9 
				Kampfzeit = 30000
			Case 5 
				Runden = 10 
				Kampfzeit = 30000
			Case 6 
				Runden = 11 
				Kampfzeit = 30000
			Case 7 
				Runden = 12 
				Kampfzeit = 30000
			Case 8 
				Runden = 1 
				Kampfzeit = 60000
			Case 9 
				Runden = 1 
				Kampfzeit = 120000
			Case 10 
				Runden = 1 
				Kampfzeit = 180000
			Case 11 
				Runden = 2 
				Kampfzeit = 60000
			Case 12 
				Runden = 1 
				Kampfzeit = 240000
			Case 13 
				Runden = 2 
				Kampfzeit = 120000
			Case 14 
				Runden = 3 
				Kampfzeit = 60000
			Case 15 
				Runden = 2 
				Kampfzeit = 180000
			Case 16 
				Runden = 4 
				Kampfzeit = 60000
			Case 17 
				Runden = 3 
				Kampfzeit = 120000
			Case 18 
				Runden = 2 
				Kampfzeit = 240000
			Case 19 
				Runden = 5 
				Kampfzeit = 60000
			Case 20 
				Runden = 3 
				Kampfzeit = 180000
			Case 21 
				Runden = 4 
				Kampfzeit = 120000
			Case 22 
				Runden = 6 
				Kampfzeit = 60000
			Case 23 
				Runden = 7 
				Kampfzeit = 60000
			Case 24 
				Runden = 3 
				Kampfzeit = 240000
			Case 25 
				Runden = 5 
				Kampfzeit = 120000
			Case 26 
				Runden = 4 
				Kampfzeit = 180000
			Case 27 
				Runden = 6 
				Kampfzeit = 120000
			Case 28 
				Runden = 4 
				Kampfzeit = 240000
			Case 29 
				Runden = 5 
				Kampfzeit = 180000
			Case 30 
				Runden = 7 
				Kampfzeit = 120000
			Case 31 
				Runden = 6 
				Kampfzeit = 180000
			Case 32 
				Runden = 5 
				Kampfzeit = 240000
			Case 33 
				Runden = 7 
				Kampfzeit = 180000
			Case 34 
				Runden = 6 
				Kampfzeit = 240000
			Case 35 
				Runden = 7 
				Kampfzeit = 240000
			Case Else
				Msgbox("","fehler")

		End Select
	

	
	
End Sub


Sub spnTimedelay_ItemClick (Position As Int, Value As Object)
	
	Timedelay = Position * 10000
	If Position = 0 Then Timedelay = 3000
	
	
End Sub

Sub spnRunden_ItemClick (Position As Int, Value As Object)

	Runden = Position + 1
	
End Sub
Sub spnKampfzeit_ItemClick (Position As Int, Value As Object)

	
	Select Position
		Case 0,1,2,3,4,5
			Kampfzeit = (Position + 1) * 10000
		Case 6
			Kampfzeit = 2*60000
		Case 7
			Kampfzeit = 3*60000
		Case 8
			Kampfzeit = 4*60000
		Case 9
			Kampfzeit = 5*60000
		Case Else
		    Msgbox("","Fehler")
		
	End Select
	

	
End Sub



Sub spnPause_ItemClick (Position As Int, Value As Object)
	
Pause = (Position + 1) * 15000


End Sub

Sub btnSpeichern_Click

		Dim AktuellerUnterordner As String : AktuellerUnterordner = "/mama/Daten"

		Dim result As Int
			
			Dim Map1 As Map
	    		Map1.Initialize
	   			Map1.Put("Timedelay",Timedelay)
				Map1.Put("Runden",Runden)
	   			Map1.Put("Kampfzeit",Kampfzeit)
				Map1.Put("Pause",Pause)

			
			result = Msgbox2("Timedelay = " & (Timedelay/1000) &" s"& CRLF & 	"Runden = " & Runden& CRLF & "Kampfzeit = " & (Kampfzeit/1000) &" s"&  CRLF & "Pause = " & (Pause/1000)&" s", "Speichern und Weiter" , "Ja","Nein","",LoadBitmap(File.DirAssets,"mamaLogo.png"))
			If result = DialogResponse.POSITIVE Then 
				File.WriteMap(File.DirRootExternal & AktuellerUnterordner, "Wettkampf.txt", Map1)
				ToastMessageShow("Erfolgreich gespeichert",False)
				StartActivity(Wettkampf)
			Else
			End If
		

				
				
				'Activity.Finish
				
End Sub


Sub btnLaden_click

	If File.ExternalWritable = False Then
        	ToastMessageShow("Ich kann nicht von der SD-Card lesen.",True)
        Return
    End If


	Dim sb As StringBuilder
	Dim Map2 As Map
	Dim AktuellerUnterordner As String : AktuellerUnterordner = "/mama/Daten"
	Dim result As Int
	
	
	
	Map2.Initialize
		If File.Exists(File.DirRootExternal & AktuellerUnterordner, "Wettkampf.txt") = True Then
			Map2 = File.ReadMap(File.DirRootExternal & AktuellerUnterordner, "Wettkampf.txt")
		End If
	
    sb.Initialize
    sb.Append("Die Werte in der Map2 sind:").Append(CRLF)
    For i = 0 To Map2.Size - 1
  
		sb.Append(Map2.GetKeyAt(i)).append(" = ").Append(Map2.GetValueAt(i)).append(CRLF)

    Next
  	
    			
			'Set the widget to update every 0 minutes.		
			Timedelay = Map2.GetDefault("Timedelay",100)'2
			Runden = Map2.GetDefault("Runden",17)'* startgewicht99
			Kampfzeit =  Map2.GetDefault("Kampfzeit",120000)'86
			Pause = Map2.GetDefault("Pause",60000) 'Name = "Avi"
			WettkampftimerLautstaerke = Map2.GetDefault("WettkampftimerLautstaerke",5) 'Name = "Avi"
			
	result = Msgbox2("Timedelay = " & (Timedelay/1000) & " s"& CRLF & 	"Runden = " & Runden & CRLF & "Kampfzeit = " & (Kampfzeit/1000) & " s"& CRLF & "Pause = " & (Pause/1000)& " s", "Geladene Daten" , "Ja","Nein","",LoadBitmap(File.DirAssets,"mamaLogo.png"))
			If result = DialogResponse.POSITIVE Then 
				ToastMessageShow("Erfolgreich geladen",False)
				StartActivity(Wettkampf)
			Else
			End If







End Sub


Sub btnWeiter_Click

				StartActivity(Wettkampf)
				'Activity.Finish
				
End Sub

