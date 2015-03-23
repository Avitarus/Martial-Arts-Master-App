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
	Dim tmrTimer1, tmrShakeHands, tmrTimedelay, tmrKampfZeit, tmrPausenZeit As Timer
	Dim LoadId0, PlayId0, LoadId2, PlayId2, LoadId3, PlayId3 As Int
	Dim LoadId4, PlayId4, LoadId5, PlayId5, LoadId1, PlayId1 As Int
	Dim LoadId7, PlayId7, LoadId8, PlayId8, LoadId6, PlayId6 As Int
	Dim SP As SoundPool
	
	
End Sub

Sub Globals
	Dim lblUeberschrift, lblCounter, lblDaten,lblGesamtzeit, lblLaut  As Label
	Dim btnBeenden As Button
	Dim Kampfende, Counter, Runde, Kampfpause As Int
	Dim strLabelText As String
	Dim p As Phone
	Dim i As Int
	Dim sebLaut As SeekBar
	Dim pws As PhoneWakeState ' in Sub Globals WACHBLEIBEN
	'Dim tracker As AnalyticsTracker

	
	
	
End Sub

Sub Activity_Create(FirstTime As Boolean)

	Activity.LoadLayout("Wettkampf")
	Activity.Title = "MAMA´s Wettkampf Timer"
	
	pws.KeepAlive(True) ' in Activity-Create WACHBLEIBEN
	
	If FirstTime Then
		SP.Initialize(6)
		LoadId0 = SP.Load(File.DirAssets, "ShakeHands1.mp3")
		LoadId1 = SP.Load(File.DirAssets, "Fight1.mp3")
		LoadId2 = SP.Load(File.DirAssets, "Break1.mp3")
		LoadId3 = SP.Load(File.DirAssets, "Glocke1.wav")
		LoadId4 = SP.Load(File.DirAssets, "Glocke3.mp3")
		LoadId5 = SP.Load(File.DirAssets, "Glocke7.wav")
		LoadId6 = SP.Load(File.DirAssets, "Stop1.mp3")
	End If
	
	
	
	
	lblUeberschrift.TextSize = 40
	lblCounter.TextSize = 60
	lblGesamtzeit.textsize = 30
	lblDaten.TextSize = 12
	
	
		'If EinstellungenWettkampf.Timedelay < 10000 Then EinstellungenWettkampf.Timedelay = 3001
		
		tmrTimer1.Initialize("tmrTimer1", 1000 )' 1 sec
	    tmrTimedelay.Initialize("tmrTimedelay", EinstellungenWettkampf.Timedelay) '3000 = die abgezogene Shakehandzeit
		tmrKampfZeit.Initialize("tmrKampfZeit", EinstellungenWettkampf.Kampfzeit )
		tmrPausenZeit.Initialize("tmrPausenZeit", EinstellungenWettkampf.Pause )
		tmrShakeHands.Initialize("tmrShakeHands", 2000)
		Kampfzeitberechnung ' brechnung der gesamtKampfzeit
		'lblUeberschrift.Text = "Vorbereiten!"
		strLabelText = "Vorbereitung"
		
		lblDaten.Text = "Gesamtkampfzeit:  "& Kampfende &" sec"& CRLF &"Zeit zum Kampf:  "& (EinstellungenWettkampf.Timedelay/1000) &" sec"& CRLF & "Kampfzeit:  " & (EinstellungenWettkampf.Kampfzeit/1000) & " sec"& CRLF & "Pausenzeit:  " & (EinstellungenWettkampf.Pause/1000) & " sec"& CRLF & "Runden:  " & EinstellungenWettkampf.Runden
		'Msgbox("Zeit bis zum Start: "& (EinstellungenWettkampf.Timedelay/1000)  &" sec" & CRLF & "Kampfzeit: " & (EinstellungenWettkampf.Kampfzeit/1000) & " sec" & CRLF & "Pausenzeit: " & (EinstellungenWettkampf.Pause/1000) & " sec" & CRLF & "Runden: " & EinstellungenWettkampf.Runden,"daten")
		
		'Runde = EinstellungenWettkampf.Runden
		
		sebLaut.Value = p.GetVolume(p.VOLUME_MUSIC)
		
		
		
		'message

End Sub


Sub tmrTimer1_Tick
		
		Kampfende = Kampfende - 1
		Counter = Counter - 1	
		
		lblCounter.Text = Counter  & " s "' &  strLabelText 
		lblGesamtzeit.Text = "Noch: "  & (Kampfende) & " sec"
		


End Sub




Sub tmrTimedelay_Tick


	PlayId0 = SP.Play(LoadId0, 1, 1, 1, 0, 1) 'shake hands
	tmrTimedelay.Enabled = False
	tmrShakeHands.Enabled =  True
	lblUeberschrift.Text = "Vorbereiten!"
	strLabelText = "Vorbereitung"
'	Counter = EinstellungenWettkampf.Timedelay
'	Counter = Counter /1000
	'	message

End Sub

Sub tmrShakeHands_Tick

	PlayId3 = SP.Play(LoadId3, 1, 1, 1, 0, 1) ' Glocke1
	PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1) ' fight
	tmrShakeHands.Enabled = False
	tmrKampfZeit.Enabled = True
	lblUeberschrift.Text = "Fight "& (Runde)& "/"& EinstellungenWettkampf.Runden 
	Counter = EinstellungenWettkampf.Kampfzeit /1000
	strLabelText = "Runde"
	Activity.Color = Colors.Green
	lblCounter.TextColor = Colors.Black
	lblUeberschrift.TextColor = Colors.Black
	lblDaten.TextColor = Colors.Black
	lblGesamtzeit.TextColor = Colors.Black
	lblLaut.TextColor = Colors.Black
		'message	
End Sub


Sub tmrKampfZeit_Tick
Dim i As Int
i= 1
	
	
	 If Runde <= EinstellungenWettkampf.Runden - 1 Then
		tmrKampfZeit.Enabled = False
		tmrPausenZeit.Enabled = True
		lblUeberschrift.Text = "Pause "& Kampfpause & "/"& ( EinstellungenWettkampf.Runden - 1)
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId4 = SP.Play(LoadId4, 1, 1, 1, 0, 1) ' Glocke3
		Activity.Color = Colors.Red
		Counter = EinstellungenWettkampf.Pause /1000
		strLabelText = "Pause"
		Runde = Runde + 1
		
		Activity.Color = Colors.Red

	Else
		tmrPausenZeit.Enabled = False
		tmrKampfZeit.Enabled = False
		PlayId5 = SP.Play(LoadId5, 1, 1, 1, 0, 1) ' Glocke
		PlayId6 = SP.Play(LoadId6, 1, 1, 1, 0, 1) ' Stop
		PlayId5 = SP.Play(LoadId5, 1, 1, 1, 0, 1) ' Glocke7
		lblCounter.Text = "Ende"
		tmrTimer1.Enabled = False
		lblUeberschrift.Text = "Kampfende"
		Activity.Color = Colors.Black
		lblCounter.TextSize = 60
	End If

		lblCounter.TextColor = Colors.white
		lblUeberschrift.TextColor = Colors.white
		lblDaten.TextColor = Colors.White
		lblGesamtzeit.TextColor = Colors.white
		lblLaut.TextColor = Colors.White
	'message
	
End Sub

Sub tmrPausenZeit_Tick

	PlayId3 = SP.Play(LoadId3, 1, 1, 1, 0, 1) ' Glocke1
	PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1) ' fight
	tmrPausenZeit.Enabled = False
	tmrKampfZeit.Enabled = True
	lblUeberschrift.Text = "Fight "& (Runde)& "/"& EinstellungenWettkampf.Runden
	Counter = EinstellungenWettkampf.Kampfzeit /1000
	Kampfpause = Kampfpause + 1
	strLabelText = "Runde"
	Activity.Color = Colors.Green
	lblCounter.TextColor = Colors.Black
	lblUeberschrift.TextColor = Colors.Black
	lblDaten.TextColor = Colors.Black
	lblGesamtzeit.TextColor = Colors.Black
	lblLaut.TextColor = Colors.Black
		'message	
End Sub



Sub btnBeenden_Click
	tmrTimer1.Enabled = False
	tmrPausenZeit.Enabled = False
	tmrKampfZeit.Enabled = False
	tmrShakeHands.Enabled = False
	tmrTimedelay.Enabled = False
	'message
	Activity.Finish
End Sub


Sub Activity_Resume
'If Main.ueberwachung Then tracker.start

	pws.KeepAlive(False)' Activity Resume WACHBLEIBEN
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
'If Main.ueberwachung Then tracker.Stop
	pws.ReleaseKeepAlive ' Activity_Pause WACHBLEIBEN

'	tmrTimer1.Enabled = False
'	tmrKampfZeit.Enabled = False
'	tmrPausenZeit.Enabled = False
'	tmrShakeHands.Enabled = False
'	tmrTimedelay.Enabled = False
	
	
End Sub



Sub btnNeustart_Click

	'If Main.ueberwachung Then tracker.TrackEvent("ettkampf","wer","wer",113)
	tmrTimer1.Enabled = False
	tmrKampfZeit.Enabled = False
	tmrPausenZeit.Enabled = False
	tmrShakeHands.Enabled = False
	tmrTimedelay.Enabled = False
	Kampfzeitberechnung ' berechnung der gesamtkampfzeit
	Counter = EinstellungenWettkampf.Timedelay /1000
	Counter = Counter + 3 '3 sec shakehands
	lblUeberschrift.Text = "Vorbereiten!"
	Runde = 1
	Kampfpause = 1
	Activity.Color = Colors.Blue
	lblCounter.TextColor = Colors.White
	lblUeberschrift.TextColor = Colors.White
	lblDaten.TextColor = Colors.White
	lblGesamtzeit.TextColor = Colors.white
	lblLaut.TextColor = Colors.White
	tmrTimer1.Enabled = True
	tmrTimedelay.enabled = True
	lblCounter.TextSize = 120
	
	
	
	
	'message
End Sub


Sub Kampfzeitberechnung

		Counter = EinstellungenWettkampf.Timedelay /1000
		Kampfende = (EinstellungenWettkampf.Timedelay + 2000)+(EinstellungenWettkampf.Kampfzeit * EinstellungenWettkampf.Runden)
		Kampfende = Kampfende + EinstellungenWettkampf.Pause * (EinstellungenWettkampf.Runden - 1)
		Kampfende = (Kampfende/1000)'/60

End Sub

Sub message
 	Dim a As String
		
		'a = "Counter = " & Counter & CRLF 
		'a = a & "strlabeltext = "& strLabelText &CRLF
		'a = a & "Timedelay = "&EinstellungenWettkampf.Timedelay & CRLF
		'a = a & "Kampfzeit = "& EinstellungenWettkampf.Kampfzeit & CRLF
		a = a & "Runden gesamt = "& EinstellungenWettkampf.Runden & CRLF
		a = a & "Kampfpause = "& Kampfpause& CRLF
		'a = a & "Kampfende = "& Kampfende& CRLF
		a = a & "Noch Runden = " & Runde & CRLF
		a = a & " sebLautstärke"& sebLaut.Value& CRLF
'		a = a & " "& & CRLF
'		a = a & " "& & CRLF
'		a = a & " "& & CRLF
'		a = a & " "& & CRLF



       'Msgbox(a,"Daten")
		ToastMessageShow(a,False)

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