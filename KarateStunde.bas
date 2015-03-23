Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

'Activity module

Sub Process_Globals

	Dim Bildertimer, Zaehltimer As Timer
	Dim SP As SoundPool
	Dim LoadId0, PlayId0, LoadId1, PlayId1, LoadId2, PlayId2, LoadId3, PlayId3 As Int
	Dim LoadId4, PlayId4, LoadId5, PlayId5, LoadId6, PlayId6 As Int
	
End Sub

Sub Globals

	Dim StartVerzeichnis, AktuellesBild, AktuellesBildAnzeige As String 
	Dim ImageView1 As ImageView
	Dim SecZaehler, Count, MengeAllerDateien, Bildnummer  As Int
	Dim PicsArray() As String
	Dim pws As PhoneWakeState
	Dim p As Phone
	Dim sebLaut As SeekBar
	Dim ListeDerBilder As List
	Dim lbltrainingsanzeige, Label1, Label2, Label3 As Label
	Dim Anzeigezeit As Int : Anzeigezeit = 20000
	'Dim tracker As AnalyticsTracker
End Sub

Sub Activity_Create(FirstTime As Boolean)
'If Main.ueberwachung Then tracker.Trackevent("atesStunde","g","k",114)


    Activity.LoadLayout("KarateStunde")
	Activity.Title = "Training Karate"
	
	'Die Sounds "fight" "stop" "shak hands" laden	
	If FirstTime Then
		SP.Initialize(4)
		LoadId0 = SP.Load(File.DirRootExternal & Main.Unterordner17, "ShakeHands2.mp3")
		LoadId1 = SP.Load(File.DirRootExternal & Main.Unterordner17, "Fight2.mp3")
		LoadId2 = SP.Load(File.DirRootExternal & Main.Unterordner17, "Stop2.mp3")
		LoadId3 = SP.Load(File.DirRootExternal & Main.Unterordner17, "Break1.mp3")
		LoadId4 = SP.Load(File.DirRootExternal & Main.Unterordner17, "Glocke1.wav")
		LoadId5 = SP.Load(File.DirRootExternal & Main.Unterordner17, "Glocke3.mp3")
		LoadId6 = SP.Load(File.DirRootExternal & Main.Unterordner17, "Glocke7.wav")
		
	End If
		sebLaut.Initialize("seblaut")
		Activity.Color = Colors.Green
		pws.KeepAlive(True) ' angeschaltet lassen
'			Gesamttrainingszeit = EinstellungenTrainingStunde.Zeit(0) 
'			ErwärmungAllgemein = EinstellungenTrainingStunde.Zeit(1) 
'				ZeitproErwärmungsuebungA = EinstellungenTrainingStunde.Zeit(2) 
'				
'			ErwärmungSpeziell = EinstellungenTrainingStunde.Zeit(3)  
'				ZeitproErwärmungsuebungS = EinstellungenTrainingStunde.Zeit(4)   
'				
'			Training = EinstellungenTrainingStunde.Zeit(5) 
'				Trainingspausen = EinstellungenTrainingStunde.Zeit(6) 
'				ZeitProUebungTraining = EinstellungenTrainingStunde.Zeit(7)  
'				
'			Partneruebungen = EinstellungenTrainingStunde.Zeit(8)  
'				PausenzwischenPartneruebungen = EinstellungenTrainingStunde.Zeit(9) 
'				ZeitProPartneruebung = EinstellungenTrainingStunde.Zeit(10) 
'				
'			KreisKrafttraining = EinstellungenTrainingStunde.Zeit(11)   
'				PausenzwischendenKraftuebungen = EinstellungenTrainingStunde.Zeit(12)   
'				ZeitProUebungKraft = EinstellungenTrainingStunde.Zeit(13)  
'				
'			Dehnung = EinstellungenTrainingStunde.Zeit(14)  
'				ZeitDehnungsuebung = EinstellungenTrainingStunde.Zeit(15) 
'				
'			CoolDown = EinstellungenTrainingStunde.Zeit(16)  
'				CoolDownuebung = EinstellungenTrainingStunde.Zeit(17)  
'			'x = Zeit(18)
'			
'			If Gesamttrainingszeit < 1000 Then Gesamttrainingszeit = 10000
'			If ErwärmungAllgemein < 1000 Then ErwärmungAllgemein = 1000
'			If ZeitproErwärmungsuebungA < 1000 Then ZeitproErwärmungsuebungA = 1000
'			If ErwärmungSpeziell < 1000 Then ErwärmungSpeziell  = 1000
'			If ZeitproErwärmungsuebungS < 1000 Then ZeitproErwärmungsuebungS = 1000
'			If Training < 1000 Then Training = 1000
'			If Trainingspausen < 1000 Then Trainingspausen = 1000
'			If ZeitProUebungTraining < 1000 Then ZeitProUebungTraining = 1000
'			If Partneruebungen < 1000 Then Partneruebungen = 1000
'			If PausenzwischenPartneruebungen < 1000 Then PausenzwischenPartneruebungen = 1000
'			If ZeitProPartneruebung < 1000 Then  ZeitProPartneruebung = 1000
'			If KreisKrafttraining < 1000 Then KreisKrafttraining = 1000
'			If PausenzwischendenKraftuebungen < 1000 Then PausenzwischendenKraftuebungen  = 1000
'				
			Bildnummer = 0
	'	    MengeAllerDateien = 0
		    ListeDerBilder.Initialize  
			StartVerzeichnis = File.DirRootExternal & Main.Unterordner6
		    ListeDerBilder = File.ListFiles (StartVerzeichnis)
			    If ListeDerBilder.Size > 0 Then
				    MengeAllerDateien = ListeDerBilder.Size 
			    End If

				'PicsArray = Array As String("1.jpg", "2.jpg", "3.jpg", "4.jpg")
				

				
'		AktuellesBildAnzeige = AktuellesBild.SubString(3)
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
'		lbltrainingsanzeige.Text = AktuellesBildAnzeige	
				
		
		
		Activity.AddMenuItem("Starten","Button0")
		'Activity.AddMenuItem("Bild zurück","Button1")
		Activity.AddMenuItem("Bild weiter","Button2")
		Activity.AddMenuItem("Einstellungen","Button5")	
		Activity.AddMenuItem("Timer Stop","Button3")
		Activity.AddMenuItem("Timer weiter","Button4")
		Activity.AddMenuItem("Fehler melden","Button6")
		
		Bildertimer.Initialize("Bildertimer", Anzeigezeit)
		Zaehltimer.Initialize("Zaehltimer",1000)	
				
	
		'Bildertimer_Tick 'Start the rotation right away






End Sub

Sub Activity_Resume
'If Main.ueberwachung Then tracker.Start
    pws.KeepAlive(False)' ausgeschaltet beim beenden?
	Zaehltimer.Enabled = False
	Bildertimer.Enabled = False
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	'If Main.ueberwachung Then tracker.Stop
	pws.ReleaseKeepAlive ' Activity_Pause WACHBLEIBEN
End Sub

Sub Zaehltimer_tick
	Label2.Text = SecZaehler
	SecZaehler = Anzeigezeit/1000 -1
	

End Sub

Sub Bildertimer_Tick

		If Count + 1 > MengeAllerDateien Then 
			Count = 0
			'Bildnummer = 0
		Else
		End If
		
		AktuellesBild = ListeDerBilder.Get (Count)
		ImageView1.Bitmap = LoadBitmap(StartVerzeichnis,AktuellesBild)
		
		
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".JPG","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".GIF","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".PNG","")
		lbltrainingsanzeige.Text = Count & ". "& AktuellesBildAnzeige
		
		Count = Count + 1
		SecZaehler = 0
		
		PlayId0 = SP.Play(LoadId0, 1, 1, 1, 0, 1)
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1)
		
				
End Sub



Sub sebLaut_ValueChanged (Value As Int, UserChanged As Boolean)
	
	Dim AktuellerUnterordner As String : AktuellerUnterordner = "/mama/Daten"
	Dim result As Int
	Dim Map1 As Map
	
		'EinstellungenWettkampf.WettkampftimerLautstaerke = Value
		p.SetVolume(p.VOLUME_MUSIC, Value, False)



'	Abspeichern der Lautstärke
'		Map1.Initialize
'		Map1.Put("WettkampftimerLautstaerke",EinstellungenWettkampf.WettkampftimerLautstaerke)
'		File.WriteMap(File.DirRootExternal & AktuellerUnterordner, "SetupApp.txt", Map1)
'		'ToastMessageShow("Wert gespeichert",False)
		

	
End Sub

Sub Button0_click


	Bildertimer.Enabled = True
	Zaehltimer.Enabled = True
	PlayId0 = SP.Play(LoadId0, 1, 1, 1, 0, 1)
	PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1)	


	Bildertimer_Tick 'Start the rotation right away
	
	
	
	
End Sub


Sub Button1_Click
    

		Bildnummer = Bildnummer - 1
	    If Bildnummer -1 < 0 Then        
	      Bildnummer = MengeAllerDateien
	    End If
		    AktuellesBild = ListeDerBilder.Get (Bildnummer)
		    ImageView1.Bitmap=LoadBitmap(StartVerzeichnis,AktuellesBild)
			


		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".JPG","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".GIF","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".PNG","")
		lbltrainingsanzeige.Text = Count & ". " & AktuellesBildAnzeige	


End Sub

Sub Button2_Click
		
		
	    Bildnummer = Bildnummer + 1
		    If Bildnummer +1 > MengeAllerDateien Then        
		      Bildnummer = 0
		    End If
	    AktuellesBild = ListeDerBilder.Get (Bildnummer)
	    ImageView1.Bitmap=LoadBitmap(StartVerzeichnis,AktuellesBild)

		
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".JPG","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".GIF","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".PNG","")
		lbltrainingsanzeige.Text = Count & ". " & AktuellesBildAnzeige
		
		

			
			
			
End Sub

Sub Button3_Click
		Bildertimer.Enabled = False
		Zaehltimer.Enabled = False
End Sub

Sub Button4_Click
		Bildertimer.Enabled = True
		Zaehltimer.Enabled = True
End Sub


Sub Button5_click

End Sub


Sub Button6_click
	StartActivity(Benachrichtigung)
End Sub


