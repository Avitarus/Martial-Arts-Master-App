Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

'
Sub Process_Globals
    Dim tmrCounter, tmrGesamttrainingszeit, tmrGruppe, tmrUebung, tmrPause As Timer
	Dim SP As SoundPool
	Dim LoadId0, PlayId0, LoadId1, PlayId1, LoadId2, PlayId2, LoadId3, PlayId3 As Int
	Dim LoadId4, PlayId4, LoadId5, PlayId5, LoadId6, PlayId6 As Int
End Sub

Sub Globals
    Dim Dateiverzeichnis As String : Dateiverzeichnis = File.DirRootExternal
	Dim AktuellerUnterordner As String 
    Dim ListeDerBilder As List
    Dim BildNummer As Int 
    Dim MengeAllerDateien As Int 
    Dim ImageView1 As ImageView
	Dim pws As PhoneWakeState
	Dim AktuellesBild As String 
	
	Dim Gesamttrainingszeit, ErwärmungAllgemein, ZeitproErwärmungsuebungA, ErwärmungSpeziell As Int 
	Dim ZeitproErwärmungsuebungS, Training, Trainingspausen, ZeitProUebungTraining, Partneruebungen As Int
	Dim PausenzwischenPartneruebungen, ZeitProPartneruebung, KreisKrafttraining, PausenzwischendenKraftuebungen As Int
	Dim ZeitProUebungKraft, Dehnung, ZeitDehnungsuebung, CoolDown, CoolDownuebung As Int 
	Dim zeitGruppe, ZeitUebung, ZeitPause As Int
	Dim Counter1, Counter2 As Int 
	Dim Startzeit0, Startzeit1, Startzeit2, Startzeit3, Startzeit4, Startzeit5, Startzeit6, Startzeit7, Startzeit8 As Int
	
	Dim lbltrainingsanzeige As Label
	Dim AktuellesBild, AktuellesBildAnzeige As String 
	
	Dim btnStart As Button
	Dim ProgressBar1 As ProgressBar
	Dim lblProgressbar1 As Label
	Dim lblZeiten As Label
	Dim lblCounter As Label
	Dim sebLaut As SeekBar
	Dim p As Phone
	Dim lblPfad As Label
	'Dim tracker As AnalyticsTracker
	
End Sub

Sub Activity_Create(FirstTime As Boolean)

	Dim i As String
	
	Activity.LoadLayout("TrainingEinfach")
	Activity.Title = "Trainingsstunde"
	
	'Die Sounds "fight" "stop" "shak hands" laden	
	If FirstTime Then
		SP.Initialize(4)
		LoadId0 = SP.Load(File.DirAssets, "ShakeHands1.mp3")
		LoadId1 = SP.Load(File.DirAssets, "Fight1.mp3")
		LoadId2 = SP.Load(File.DirAssets, "Break1.mp3")
		LoadId3 = SP.Load(File.DirAssets, "Glocke1.wav")
		LoadId4 = SP.Load(File.DirAssets, "Glocke3.mp3")
		LoadId5 = SP.Load(File.DirAssets, "Glocke7.wav")
		LoadId6 = SP.Load(File.DirAssets, "Stop1.mp3")
	End If
	
	sebLaut.Value = p.GetVolume(p.VOLUME_MUSIC)
	Counter2 = 0 ' der counter für die Gruppe in der wir gerade sind
	pws.KeepAlive(True) ' angeschaltet lassen
	
	
			Gesamttrainingszeit = EinstellungenTrainingStunde.Zeit(0) 
			ErwärmungAllgemein = EinstellungenTrainingStunde.Zeit(1) 
				ZeitproErwärmungsuebungA = EinstellungenTrainingStunde.Zeit(2) 
				
			ErwärmungSpeziell = EinstellungenTrainingStunde.Zeit(3)  
				ZeitproErwärmungsuebungS = EinstellungenTrainingStunde.Zeit(4)   
				
			Training = EinstellungenTrainingStunde.Zeit(5) 
				Trainingspausen = EinstellungenTrainingStunde.Zeit(6) 
				ZeitProUebungTraining = EinstellungenTrainingStunde.Zeit(7)  
				
			Partneruebungen = EinstellungenTrainingStunde.Zeit(8)  
				PausenzwischenPartneruebungen = EinstellungenTrainingStunde.Zeit(9) 
				ZeitProPartneruebung = EinstellungenTrainingStunde.Zeit(10) 
				
			KreisKrafttraining = EinstellungenTrainingStunde.Zeit(11)   
				PausenzwischendenKraftuebungen = EinstellungenTrainingStunde.Zeit(12)   
				ZeitProUebungKraft = EinstellungenTrainingStunde.Zeit(13)  
				
			Dehnung = EinstellungenTrainingStunde.Zeit(14)  
				ZeitDehnungsuebung = EinstellungenTrainingStunde.Zeit(15) 
				
			CoolDown = EinstellungenTrainingStunde.Zeit(16)  
				CoolDownuebung = EinstellungenTrainingStunde.Zeit(17)  
			'x = Zeit(18)
			
			If Gesamttrainingszeit < 1000 Then Gesamttrainingszeit = 10000
			If ErwärmungAllgemein < 1000 Then ErwärmungAllgemein = 1000
			If ZeitproErwärmungsuebungA < 1000 Then ZeitproErwärmungsuebungA = 1000
			If ErwärmungSpeziell < 1000 Then ErwärmungSpeziell  = 1000
			If ZeitproErwärmungsuebungS < 1000 Then ZeitproErwärmungsuebungS = 1000
			If Training < 1000 Then Training = 1000
			If Trainingspausen < 1000 Then Trainingspausen = 1000
			If ZeitProUebungTraining < 1000 Then ZeitProUebungTraining = 1000
			If Partneruebungen < 1000 Then Partneruebungen = 1000
			If PausenzwischenPartneruebungen < 1000 Then PausenzwischenPartneruebungen = 1000
			If ZeitProPartneruebung < 1000 Then  ZeitProPartneruebung = 1000
			If KreisKrafttraining < 1000 Then KreisKrafttraining = 1000
			If PausenzwischendenKraftuebungen < 1000 Then PausenzwischendenKraftuebungen  = 1000
				
'		lbltrainingsanzeige.TextSize = 20
'		AktuellesBildAnzeige = AktuellesBild.SubString(3)
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
'		lbltrainingsanzeige.Text = AktuellesBildAnzeige
'		
		
		
		
		Startzeit0 = 0 ' beginn ErwärmungAllgemein
		Startzeit1 = Startzeit0 + ErwärmungAllgemein/1000' beginn ErwärmungSpeziell
		Startzeit2 = Startzeit1 + ErwärmungSpeziell/1000 ' beginn Training
		Startzeit3 = Startzeit2 + Training /1000' beginn Partneruebungen
		Startzeit4 = Startzeit3 + Partneruebungen /1000' beginn KreisKrafttraining
		Startzeit5 = Startzeit4 + KreisKrafttraining /1000' beginn Dehnung
		Startzeit6 = Startzeit5 + Dehnung /1000' beginn CoolDown
		Startzeit7 = Startzeit6 + CoolDown/1000 ' Ende des trainings
		
		
		
	
			
			
		' timer initialisieren
		tmrCounter.Initialize("tmrCounter",1000)
		tmrGesamttrainingszeit.Initialize("tmrGesamttrainingszeit",EinstellungenTrainingStunde.Zeit(0))
		tmrGruppe.Initialize("tmrGruppe",EinstellungenTrainingStunde.Zeit(10))
		tmrUebung.Initialize("tmrUebung",EinstellungenTrainingStunde.Zeit(10))
		tmrPause.Initialize("tmrPause",EinstellungenTrainingStunde.Zeit(10))


		' Timer auf 0 setzen
		tmrCounter.Enabled = False 
		tmrGesamttrainingszeit.Enabled = False 
		tmrGruppe.Enabled = False 
		tmrUebung.Enabled = False 
		tmrPause.Enabled = False 




	   

'	i = Gesamttrainingszeit & CRLF & " : " & ErwärmungAllgemein & CRLF & " : " & ZeitproErwärmungsuebungA & CRLF & " : " & ErwärmungSpeziell& CRLF & " : "
'	i = i & ZeitproErwärmungsuebungS & CRLF & " : " & Training & CRLF & " : " & Trainingspausen & CRLF & " : " & ZeitProUebungTraining & CRLF & " : " & Partneruebungen& CRLF & " : "
'	i = i & PausenzwischenPartneruebungen & CRLF & " : " & ZeitProPartneruebung & CRLF & " : " & KreisKrafttraining & CRLF & " : " & PausenzwischendenKraftuebungen& CRLF & " : "
	'i = Startzeit0& CRLF & " : "& Startzeit1& CRLF & " : "& Startzeit2& CRLF & " : "& Startzeit3& CRLF & " : "& Startzeit4& CRLF & " : "& Startzeit5& CRLF & " : "& Startzeit6& CRLF & " : "& Startzeit7
	'Msgbox(i,"Werte")
                
End Sub

Sub tmrCounter_tick
    
'	Dim AktuellerUnterordner As String   : AktuellerUnterordner =  "/mama/KickErwaermung"	
'	Dim Hauptordner0 As String : Hauptordner0 = "/mama"
'	Dim Unterordner1 As String : Unterordner1 = "/mama/Daten"
'	Dim Unterordner2 As String : Unterordner2 = "/mama/AllgBodyWeightExercices"
'	Dim Unterordner3 As String : Unterordner3 = "/mama/AllgCoolDown"
'	Dim Unterordner4 As String : Unterordner4 = "/mama/AllgDehnung"
'	Dim Unterordner5 As String : Unterordner5 = "/mama/AllgErwaermung"
'	Dim Unterordner6 As String : Unterordner6 = "/mama/KarateEinzel"
'	Dim Unterordner7 As String : Unterordner7 = "/mama/KarateErwaermung"
'	Dim Unterordner8 As String : Unterordner8 = "/mama/KarateKata"
'	Dim Unterordner9 As String : Unterordner9 = "/mama/KarateKumite"
'	Dim Unterordner10 As String : Unterordner10 = "/mama/KarateKihon"
'	Dim Unterordner11 As String : Unterordner11 = "/mama/Karate"
'	Dim Unterordner12 As String	: Unterordner12 = "/mama/KickErwaermung"
'	Dim Unterordner13 As String	: Unterordner13 = "/mama/KickEinzel"
'	Dim Unterordner14 As String	: Unterordner14 = "/mama/KickPartner"
'	Dim Unterordner15 As String	: Unterordner15 = "/mama/"
'	Dim Unterordner16 As String	: Unterordner16 = "/mama/"
'	Dim Unterordner17 As String	: Unterordner17 = "/mama/"
'	Dim Unterordner18 As String	: Unterordner18 = "/mama/"
'	Dim Unterordner19 As String	: Unterordner19 = "/mama/"
'	Dim Unterordner20 As String	: Unterordner20 = "/mama/ATS"

	
	ProgressBar1.Progress = Counter1 *(100/Startzeit7)
	
	
	
	If Counter1 = Startzeit0 Then ' beginn erwärmung allgemein
	
		tmrUebung.Enabled = False
		tmrPause.Enabled = False 
		tmrGesamttrainingszeit.Interval = Gesamttrainingszeit
		tmrGesamttrainingszeit.Enabled = True
		AktuellerUnterordner = Main.Unterordner5
		tmrUebung.Interval = ZeitproErwärmungsuebungA
		tmrUebung.Enabled = True		
		tmrPause.Interval = 1
		
		
		
		
		  ListeDerBilder.Initialize        
		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
	    If ListeDerBilder.Size > 0 Then
		    MengeAllerDateien = ListeDerBilder.Size 
		    BildNummer =-1	
	    End If
		
		Log(Counter1 & AktuellerUnterordner)
		
	Else
			
	End If 
	
	DoEvents
	
	If Counter1 = Startzeit1 Then ' beginn erwärmung speziell
		tmrUebung.Enabled = False
		tmrPause.Enabled = False 
		AktuellerUnterordner = Main.Unterordner12
		tmrUebung.Interval = ZeitproErwärmungsuebungS
		tmrUebung.Enabled = True		
		tmrPause.Interval = 1
		
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock
		
		ListeDerBilder.Initialize        
		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
	    If ListeDerBilder.Size > 0 Then
		    MengeAllerDateien = ListeDerBilder.Size 
		    BildNummer =-1	
	    End If
		Log(Counter1 & AktuellerUnterordner)
	
		
	Else
			
	End If 
	If Counter1 = Startzeit2 Then ' tRAINING EINZEL
		tmrUebung.Enabled = False
		tmrPause.Enabled = False 
		AktuellerUnterordner = Main.Unterordner13
		tmrUebung.Interval = ZeitProUebungTraining
		tmrUebung.Enabled = True		
		tmrPause.Interval = Trainingspausen
		
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock		
		
		ListeDerBilder.Initialize        
		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
	    If ListeDerBilder.Size > 0 Then
		    MengeAllerDateien = ListeDerBilder.Size 
		    BildNummer =-1	
	    End If
		Log(Counter1 & AktuellerUnterordner)
	Else
			
	End If
	DoEvents
	If Counter1 = Startzeit3 Then ' beginn Partnerübungen
		tmrUebung.Enabled = False
		tmrPause.Enabled = False 
		AktuellerUnterordner = Main.Unterordner14
		tmrUebung.Interval = ZeitProUebungTraining
		tmrUebung.Enabled = True		
		tmrPause.Interval = Trainingspausen
		
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock	
		
		ListeDerBilder.Initialize        
		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
	    If ListeDerBilder.Size > 0 Then
		    MengeAllerDateien = ListeDerBilder.Size 
		    BildNummer =-1	
	    End If
		Log(Counter1 & AktuellerUnterordner)
	Else
			
	End If 
	If Counter1 = Startzeit4 Then 'bODYWEIGHTEXERCISES
		tmrUebung.Enabled = False
		tmrPause.Enabled = False 
		AktuellerUnterordner = Main.Unterordner2
		tmrUebung.Interval = ZeitProUebungKraft
		tmrUebung.Enabled = True		
		tmrPause.Interval = PausenzwischendenKraftuebungen
		
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock	
		
		ListeDerBilder.Initialize        
		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
	    If ListeDerBilder.Size > 0 Then
		    MengeAllerDateien = ListeDerBilder.Size 
		    BildNummer =-1	
	    End If
		Log(Counter1 & AktuellerUnterordner)
	Else
			
	End If 
	If Counter1 = Startzeit5 Then ' beginn dehnung
		tmrUebung.Enabled = False
		tmrPause.Enabled = False 
		AktuellerUnterordner = Main.Unterordner4
		tmrUebung.Interval = ZeitProUebungKraft
		tmrUebung.Enabled = True		
		tmrPause.Interval = PausenzwischendenKraftuebungen
		
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock
'		
		ListeDerBilder.Initialize        
		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
	    If ListeDerBilder.Size > 0 Then
		    MengeAllerDateien = ListeDerBilder.Size 
		    BildNummer =-1	
	    End If	
		Log(Counter1 & AktuellerUnterordner)
	Else
			
	End If 
	If Counter1 = Startzeit6 Then ' COOL DOWN
		tmrUebung.Enabled = False
		tmrPause.Enabled = False 
		AktuellerUnterordner = Main.Unterordner3
		tmrUebung.Interval = CoolDownuebung
		tmrUebung.Enabled = True
		tmrPause.Interval = 1
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock	
		
		ListeDerBilder.Initialize        
		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
	    If ListeDerBilder.Size > 0 Then
		    MengeAllerDateien = ListeDerBilder.Size 
		    BildNummer =-1	
	    End If		
		Log(Counter1 & AktuellerUnterordner)
	Else
			
	End If 
	
'		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
'		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glock
'		
'		ListeDerBilder.Initialize        
'		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
'	    If ListeDerBilder.Size > 0 Then
'		    MengeAllerDateien = ListeDerBilder.Size 
'		    BildNummer =-1	
'	    End If
'		Log(Counter1 & AktuellerUnterordner)
	
	If Counter1 = Startzeit7 Then ' ende der Trainingszeit
			tmrGesamttrainingszeit.Enabled = False
	Else
			
	End If 
	If Counter1 = Startzeit8 Then
			
	Else
			
	End If 

	Log(Counter1)

	Counter1 = Counter1 + 1

	
	lblPfad.Text = AktuellerUnterordner 
	
	
		
		lblCounter.Text = DateTime.Time(-3599950 + Counter1*1000)
End Sub



Sub tmrUebung_tick


	If AktuellerUnterordner = Main.Unterordner13 OR AktuellerUnterordner = Main.Unterordner14 Then
		
			BildNummer = Rnd(0,MengeAllerDateien)
		Else
			BildNummer = BildNummer + 1
			    If BildNummer +1 > MengeAllerDateien Then        
			      BildNummer=0
			    End If
	End If	
	
	    AktuellesBild= ListeDerBilder.Get (BildNummer)
	    ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner  ,AktuellesBild)


'		Dim b As Beeper
'		b.Initialize(600, 600)
'		b.Beep
	If tmrPause.Interval = 1 Then
	
	Else
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glocke3
	End If	
		
		DoEvents
		
		Activity.Color = Colors.Red
		
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		lbltrainingsanzeige.Text = AktuellesBildAnzeige
		
		tmrUebung.Enabled = False
		tmrPause.Enabled = True
		

End Sub

Sub tmrPause_tick

'	If AktuellerUnterordner = Main.Unterordner13 OR AktuellerUnterordner = Main.Unterordner14 Then
'		
'			BildNummer = Rnd(0,MengeAllerDateien)
'		Else
'			BildNummer = BildNummer + 1
'		    If BildNummer +1 > MengeAllerDateien Then        
'		      BildNummer=0
'		    End If
'	End If	
'	
'	    AktuellesBild= ListeDerBilder.Get (BildNummer)
'	    ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner  ,AktuellesBild)
		
		tmrUebung.Enabled = True
		tmrPause.Enabled = False
		
'			
		If tmrPause.Interval = 1 Then
				Dim b As Beeper
				b.Initialize(300, 600)
				b.Beep	
			Else
				PlayId3 = SP.Play(LoadId3, 1, 1, 1, 0, 1) ' Glocke1
				PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1) ' fight
		End If
		
		DoEvents
		
		Activity.Color = Colors.Green
'		
'		AktuellesBildAnzeige = AktuellesBild.SubString(3)
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
'		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
'		lbltrainingsanzeige.Text = AktuellesBildAnzeige
'		

			
End Sub

Sub tmrGruppe_Tick
	
	Select Counter2
		
		Case 0
			tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(1) 
			tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(2) 
			tmrPause.Interval = 1
		Case 1
			tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(3)  
			tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(4)   
			tmrPause.Interval = 1
		Case 2		
			tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(5) 
			tmrPause.Interval = EinstellungenTrainingStunde.Zeit(6) 
			tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(7)  
		Case 3		
			tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(8)  
			tmrPause.Interval = EinstellungenTrainingStunde.Zeit(9) 
			tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(10) 
		Case 4		
			tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(11)   
			tmrPause.Interval = EinstellungenTrainingStunde.Zeit(12)   
			tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(13)  
		Case 5		
			tmrGruppe.Interval = EinstellungenTrainingStunde.Zeit(14)  
			tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(15) 
			tmrPause.Interval = 1
		Case 6	
			CoolDown = EinstellungenTrainingStunde.Zeit(16)  
			tmrUebung.Interval = EinstellungenTrainingStunde.Zeit(17)
			tmrPause.Interval = 1
		Case Else	
			tmrCounter.Enabled = False 
			tmrGesamttrainingszeit.Enabled = False 
			tmrGruppe.Enabled = False 
			tmrUebung.Enabled = False 
			tmrPause.Enabled = False 

			lbltrainingsanzeige.Text = "Training beendet"
			
			PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
			PlayId5 = SP.Play(LoadId5, 1, 1, 1, 0, 1) ' Glocke7
			

	End Select
	
	DoEvents
	
	Counter2 = Counter2 + 1

End Sub

Sub tmrGesamttrainingszeit_Tick



		tmrCounter.Enabled = False 
		tmrGesamttrainingszeit.Enabled = False 
		tmrGruppe.Enabled = False 
		tmrUebung.Enabled = False 
		tmrPause.Enabled = False 

		lbltrainingsanzeige.Text = "Training beendet"
		
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId5 = SP.Play(LoadId5, 1, 1, 1, 0, 1) ' Glocke7
		
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


Sub Button2_Click
		
			BildNummer = BildNummer + 1
	    If BildNummer +1 > MengeAllerDateien Then        
	      	BildNummer=0
	    End If
		    AktuellesBild= ListeDerBilder.Get (BildNummer)
		    ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner ,AktuellesBild)
			
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		lbltrainingsanzeige.Text = AktuellesBildAnzeige	
			
			
			
			
			
End Sub

Sub Button3_Click
	tmrCounter.Enabled = False
End Sub

Sub Button4_Click
	tmrCounter.Enabled = True
End Sub
Sub Button1_Click
    

			BildNummer = BildNummer - 1
	    If BildNummer -1 > MengeAllerDateien Then        
	      	BildNummer=0
	    End If
		    AktuellesBild= ListeDerBilder.Get (BildNummer)
		ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner ,AktuellesBild)
		
		
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		lbltrainingsanzeige.Text = AktuellesBildAnzeige





End Sub





Sub btnStart_Click
	'If Main.ueberwachung Then tracker.TrackEvent("ining","buttn","wertz",114)

			MengeAllerDateien =0
	
			AktuellerUnterordner = Main.Unterordner5

	
		    ListeDerBilder.Initialize        
		    ListeDerBilder = File.ListFiles (Dateiverzeichnis & AktuellerUnterordner )
	    If ListeDerBilder.Size > 0 Then
		    MengeAllerDateien = ListeDerBilder.Size 
		    BildNummer =-1	
			tmrCounter.Enabled = True
	    End If
	

	
	
	    Dim AktuellesBild As String 
			BildNummer = BildNummer + 1
	    If BildNummer +1 > MengeAllerDateien Then        
	      	BildNummer=0
	    End If
		    AktuellesBild= ListeDerBilder.Get (BildNummer)
		    ImageView1.Bitmap=LoadBitmap(Dateiverzeichnis & AktuellerUnterordner, AktuellesBild)
	
		AktuellesBildAnzeige = AktuellesBild.SubString(3)
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".jpg","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".gif","")
		AktuellesBildAnzeige = AktuellesBildAnzeige.Replace(".png","")
		lbltrainingsanzeige.Text = AktuellesBildAnzeige



	
End Sub


Sub sebLaut_ValueChanged (Value As Int, UserChanged As Boolean)
	
	'Dim AktuellerUnterordner As String : AktuellerUnterordner = "/mama/Daten"
	'Dim result As Int
	'Dim Map1 As Map
	
		'EinstellungenWettkampf.WettkampftimerLautstaerke = Value
		p.SetVolume(p.VOLUME_MUSIC, Value, False)



'	Abspeichern der Lautstärke
'		Map1.Initialize
'		Map1.Put("WettkampftimerLautstaerke",EinstellungenWettkampf.WettkampftimerLautstaerke)
'		File.WriteMap(File.DirRootExternal & AktuellerUnterordner, "SetupApp.txt", Map1)
'		'ToastMessageShow("Wert gespeichert",False)
		

	
End Sub


