Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region


Sub Process_Globals
 
	Dim SP As SoundPool
	Dim LoadId0, PlayId0, LoadId1, PlayId1, LoadId2, PlayId2, LoadId3, PlayId3 As Int
	Dim LoadId4, PlayId4, LoadId5, PlayId5, LoadId6, PlayId6 As Int
	Dim tmrTraining1, tmrGesamtzeit, tmrCounter, tmrPause As Timer
	
End Sub

Sub Globals
    Dim StartVerzeichnis As String 
    Dim ListeDerBilder As List
    Dim Bildnummer, MengeAllerDateien, Count1, Count2, Fortschritt As Int 
    Dim ImageView1 As ImageView
	Dim pws As PhoneWakeState
	Dim lbltrainingsanzeige, Label1, Label2, Label3 As Label
	Dim sebLaut As SeekBar
	Dim p As Phone
	Dim AktuellesBild, AktuellesBildAnzeige As String 
	Dim ProgressBar1 As ProgressBar
	'Dim tracker As AnalyticsTracker
End Sub

Sub Activity_Create(FirstTime As Boolean)

	'If Main.ueberwachung Then tracker.TrackEvent("MAMATrain","Stt","Kuckbraining",110)
 	Activity.LoadLayout("TrainingKickboxen")
	Activity.Title = "Training Kickboxen"
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
	   
		tmrTraining1.Initialize("tmrTraining1",EinstellungenTrainingKick.Trainingszeit1)
		tmrGesamtzeit.Initialize("tmrGesamtzeit",EinstellungenTrainingKick.Gesamttrainingszeit)
		tmrCounter.Initialize("tmrCounter",1000)
		tmrPause.Initialize("tmrPause",EinstellungenTrainingKick.Pause1)
		
		Count1 = EinstellungenTrainingKick.Trainingszeit1 / 1000
		Count2 = EinstellungenTrainingKick.Gesamttrainingszeit / 1000
		Fortschritt = EinstellungenTrainingKick.Gesamttrainingszeit / 1000
		
	    Bildnummer = 0
'	    MengeAllerDateien = 0
	    ListeDerBilder.Initialize        
	    StartVerzeichnis = File.DirRootExternal & Main.AktuellerUnterordner
	    ListeDerBilder = File.ListFiles (StartVerzeichnis )
		    If ListeDerBilder.Size > 0 Then
			    MengeAllerDateien = ListeDerBilder.Size 
			   ' Bildnummer =-1
'			    tmrTraining1.Enabled = True
'				tmrGesamtzeit.Enabled = True
'				tmrCounter.Enabled =True
		    End If
	
	

'		Bildnummer = Bildnummer + 1
	'	    If Bildnummer +1 > MengeAllerDateien Then        
	'	      Bildnummer=0
	'	    End If
		AktuellesBild= ListeDerBilder.Get (Bildnummer)
		ImageView1.Bitmap=LoadBitmap(StartVerzeichnis,AktuellesBild)
	
		
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		lbltrainingsanzeige.Text = AktuellesBildAnzeige
	
		
		sebLaut.Value = p.GetVolume(p.VOLUME_MUSIC)

		
		Activity.AddMenuItem("Starten","Button0")
		'Activity.AddMenuItem("Bild zurück","Button1")
		Activity.AddMenuItem("Bild weiter","Button2")
		
		Activity.AddMenuItem("Einstellungen","Button5")
		Activity.AddMenuItem("Timer Stop","Button3")
		Activity.AddMenuItem("Timer weiter","Button4")
		
		Activity.AddMenuItem("Fehler melden","Button6")
                
End Sub

Sub tmrCounter_tick
	
	Count1 = Count1 - 1
	Count2 = Count2 - 1
	
	
	Label2.text = Count1
	
	Label3.text = "Insgesamt noch ca." & NumberFormat((Count2/60),1,0) & " min"
	
	ProgressBar1.Progress = 100 - (100 / (Fortschritt / Count2))
	
	
End Sub

	Sub tmrTraining1_Tick
		'Spielt den Sound "Fight" ab
	
	If EinstellungenTrainingKick.PauseMachen Then 
		PlayId2 = SP.Play(LoadId2, 1, 1, 1, 0, 1)
		Activity.Color = Colors.Red
	Else	
		PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1)
		Activity.Color = Colors.Green
	End If
'		Dim b As Beeper
'			b.Initialize(300, 500)
'			b.Beep

		
		Count1 = EinstellungenTrainingKick.Pause1 /1000
		Bildnummer = Bildnummer + 1
		    If Bildnummer +1 > MengeAllerDateien Then        
		      Bildnummer=0
		    End If
		AktuellesBild= ListeDerBilder.Get (Bildnummer)
		ImageView1.Bitmap=LoadBitmap(StartVerzeichnis,AktuellesBild)
			

		
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		lbltrainingsanzeige.Text = AktuellesBildAnzeige
	

		
		If EinstellungenTrainingKick.Pausemachen Then
				tmrPause.Enabled = True
				tmrTraining1.Enabled = False		
		Else
		
		End If
		
		
		
		
		
	End Sub

Sub tmrPause_tick

		tmrTraining1.Enabled = True
		tmrPause.Enabled = False
		Count1 = EinstellungenTrainingKick.Trainingszeit1 /1000
		
		PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1)
'		Dim b As Beeper
'			b.Initialize(400, 600)
'			b.Beep

		Activity.Color = Colors.Green
End Sub
	
	Sub tmrGesamtzeit_Tick
	
	
	PlayId2 = SP.Play(LoadId2, 1, 1, 1, 0, 1)
	PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1)
	ToastMessageShow("Training Beendet",False)
	
	tmrCounter.Enabled =False  
	tmrTraining1.Enabled = False
	tmrGesamtzeit.Enabled = False
	tmrGesamtzeit.Enabled = False
	
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		lbltrainingsanzeige.Text = AktuellesBildAnzeige
	
	
	
	End Sub


Sub Activity_Resume
'If Main.ueberwachung Then tracker.start
    pws.KeepAlive(False)' ausgeschaltet beim beenden?
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
'If Main.ueberwachung Then tracker.Stop
    pws.ReleaseKeepAlive
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
	    tmrTraining1.Enabled = True
		tmrGesamtzeit.Enabled = True
		tmrCounter.Enabled =True
		PlayId0 = SP.Play(LoadId0, 1, 1, 1, 0, 1)
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1)
		
		
End Sub




Sub Button1_Click
    

		    Bildnummer = Bildnummer - 1
	    If Bildnummer -1 < 0 Then        
	      Bildnummer = MengeAllerDateien
	    End If
		    AktuellesBild= ListeDerBilder.Get (Bildnummer)
		    ImageView1.Bitmap=LoadBitmap(StartVerzeichnis,AktuellesBild)
			


		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		lbltrainingsanzeige.Text = AktuellesBildAnzeige


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
		lbltrainingsanzeige.Text = AktuellesBildAnzeige	
			
			
			
End Sub

Sub Button3_Click
	tmrTraining1.Enabled = False
	tmrPause.Enabled = False
	tmrGesamtzeit.Enabled = False
	tmrCounter.Enabled = False
End Sub

Sub Button4_Click
	tmrTraining1.Enabled = True
	tmrGesamtzeit.Enabled = True
	tmrCounter.Enabled = True
End Sub

Sub button5_Click

End Sub



Sub Button6_click
	StartActivity(Benachrichtigung)
End Sub

