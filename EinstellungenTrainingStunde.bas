Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals

	Dim Zeit(18) As Int
	
End Sub

Sub Globals

	Dim AnzahlDerEinstellungen As Int			: AnzahlDerEinstellungen = 18 ' auch bei Zeit oben ändern!!!!
	Dim Panelhoehe As Int						: Panelhoehe = 200dip
	Dim Panels As Panel
	Dim btnSpeichern, btnLaden, btnHoch, btnWeiter As Button
	Dim scvHintergrund As ScrollView
	Dim GesamtzeitAnzeige As ProgressBar
	Dim Gesamttrainingszeit As Int
	Dim Pausen, Partneruebungen, Dehnung, CoolDown As Int
	Dim Setupdatei As String					:	Setupdatei = "EinstellungenTrainingStunde.txt"
	Dim lblTafel(AnzahlDerEinstellungen) As Label
	Dim lblZeitanzeige(AnzahlDerEinstellungen) As Label
	Dim ToggelJaNein(AnzahlDerEinstellungen) As ToggleButton
	Dim sebZeitValue(19) As Int
	Dim Ausgleichszahl As Int 					: Ausgleichszahl = -3599950
	Dim lblGesamtzeit As Label
	
End Sub

Sub Activity_Create(FirstTime As Boolean)

	Activity.Title = "Einstellungen Training Stunde"

	If File.ExternalWritable = False Then
        	ToastMessageShow("Ich kann nicht von der SD-Card lesen.",True)
        Return
    End If


	Dim sb As StringBuilder
	Dim Map2 As Map
	Dim result As Int
	Dim lblUeberschrift As Label 
	
	Map2.Initialize
		If File.Exists(File.DirRootExternal & Main.Unterordner1, Setupdatei) = True Then
			Map2 = File.ReadMap(File.DirRootExternal & Main.Unterordner1, Setupdatei)
		Else	
			Msgbox("Standardwerte werden genutzt","A C H T U N G")
		End If
	
    sb.Initialize
    sb.Append("Die Werte im Speicher sind:").Append(CRLF)
    For i = 0 To Map2.Size - 1
  
		sb.Append(Map2.GetKeyAt(i)).append(" = ").Append(Map2.GetValueAt(i)).append(CRLF)

    Next
  		
		sebZeitValue(0) = Map2.GetDefault("Gesamttrainingzeit",3600000)
		sebZeitValue(1) = Map2.GetDefault("ErwaermungAllgemein",600000)
		sebZeitValue(2) = Map2.GetDefault("ErwaermungAllgemeinUebung",15000)
		sebZeitValue(3) = Map2.GetDefault("ErwaermungSpeziell",900000)
		sebZeitValue(4) = Map2.GetDefault("ErwaermungSpeziellUebung",15000)
		sebZeitValue(5) = Map2.GetDefault("EinzelUebungen",600000)
		sebZeitValue(6) = Map2.GetDefault("EinzelUebungenPausen",60000)
		sebZeitValue(7) = Map2.GetDefault("EinzelUebungenUebungen",60000)
		sebZeitValue(8) = Map2.GetDefault("PartnerUebungen",600000)
		sebZeitValue(9) = Map2.GetDefault("PartnerUebungenPausen",60000)
		sebZeitValue(10) = Map2.GetDefault("PartnerUebungenUebungen",60000)			
		sebZeitValue(11) = Map2.GetDefault("KraftUebungen",600000)
		sebZeitValue(12) = Map2.GetDefault("KraftUebungenPausen",60000)
		sebZeitValue(13) = Map2.GetDefault("KraftUebungenUebungen",120000)			
		sebZeitValue(14) = Map2.GetDefault("Dehnung",300000)
		sebZeitValue(15) = Map2.GetDefault("DehnungUebung",15000)			
		sebZeitValue(16) = Map2.GetDefault("CoolDown",300000)
		sebZeitValue(17) = Map2.GetDefault("CooldownUebung",30000)			
	
	
	Panels.Initialize("panels")
	Panels.Color = Colors.Black ' RGB(Rnd(250, 255), Rnd(250, 255), Rnd(250, 255))
	lblUeberschrift.Initialize("")
	lblUeberschrift.Text = "Einstellungen" & " Kickboxen"
	lblUeberschrift.Gravity = Gravity.CENTER_HORIZONTAL
	lblUeberschrift.TextSize = 25
	lblUeberschrift.TextColor = Colors.White
	Panels.AddView(lblUeberschrift, 5dip, 10dip, 100%x - 5dip, 60dip)
	Activity.AddView(Panels, 0, 0, 100%x, 100%y) 'add the panel to the layout
	
'Scrollview erstellen und aufs Panel pappen
	scvHintergrund.Initialize(500)
	Panels.AddView(scvHintergrund, 0, 50dip, 100%x - 5dip, 100%y - 150dip)
	
'Buttons erstellen und auf Panel Pappen
	btnSpeichern.Initialize ("btnSpeichern")
	btnHoch.Initialize ("btnHoch")
	btnWeiter.Initialize ("btnWeiter")
	GesamtzeitAnzeige.Initialize ("Prozessbar1")
	lblGesamtzeit.Initialize("lblGesamtzeit")
	GesamtzeitAnzeige.Progress = 50
	
	btnSpeichern.Tag=1
	btnHoch.Tag=2
	btnWeiter.Tag=3
	btnSpeichern.Text="Speichern"
	btnHoch.Text="Nach Oben"
	btnWeiter.Text="Weiter"
	
	lblGesamtzeit.Gravity = Gravity.CENTER_HORIZONTAL
	lblGesamtzeit.TextColor = Colors.black
	lblGesamtzeit.TextSize = 18
	
	
	Panels.AddView (GesamtzeitAnzeige, 10, 100%y - 80dip, 100%x - 20dip, 25dip)
	Panels.AddView (lblGesamtzeit, 10, 100%y - 80dip, 100%x - 20dip, 25dip)
	Panels.AddView (btnSpeichern, 0, 100%y - 50dip, 33%x, 50dip)
	Panels.AddView (btnHoch, 33%x, 100%y - 50dip, 33%x, 50dip)
	Panels.AddView (btnWeiter, 66%X, 100%y - 50dip, 33%x, 50dip)
	

	Listenerzeugung
	

End Sub


Sub Listenerzeugung
	Dim i As Int

	For i = 0 To AnzahlDerEinstellungen - 1 'originalzeile

		Dim pnlEinheit As Panel
		Dim edtEingabefeld As EditText
		Dim sebZeit(AnzahlDerEinstellungen) As SeekBar 
		
		
		
		'Panel auf Scrollview pappen
		pnlEinheit.Initialize("pnlEinheit")
		scvHintergrund.Panel.AddView(pnlEinheit, 0 , i * Panelhoehe, 100%x - 5dip, Panelhoehe)'5dip + i * Panelhoehe, 100%x - 5dip, Panelhoehe)
		pnlEinheit.Tag = i
		
		'Tafel auf Panel pappen		
		lblTafel(i).Initialize("lblTafel")
		pnlEinheit.AddView(lblTafel(i), 3%x, 0dip, 95%x, Panelhoehe- 5dip)
		lblTafel(i).Tag=i
		lblTafel(i).TextSize=20
		lblTafel(i).color = Colors.LightGray
		lblTafel(i).textColor = Colors.Black
		lblTafel(i).Text = i
		
		
		ToggelJaNein(i).Initialize("ToggelJaNein")
		ToggelJaNein(i).Checked = True
		ToggelJaNein(i).TextOff = "AUS"
		ToggelJaNein(i).TextOn = "AN"		
		
		If i =0 Then
		
		sebZeit(i).Initialize("SeekbarZeit")
		pnlEinheit.AddView(sebZeit(i), 5%x, 140dip, 90%x, 30dip)
		sebZeit(i).Tag = i
		
		
		
		Else
			
			If i = 1 OR i = 3 OR i = 5 OR i = 8 OR i = 11 OR i = 14 OR i = 16 Then
				'ToggelJaNein(i).Initialize("ToggelJaNein")
'				ToggelJaNein(i).TextOff = "AUS"
'				ToggelJaNein(i).TextOn = "AN"
'				ToggelJaNein(i).Checked = True
				pnlEinheit.AddView (ToggelJaNein(i), 30%x, 65dip, 70dip, 60dip)
			Else
			
			End If
			
			'Seekbar auf Tafel pappen
			sebZeit(i).Initialize("SeekbarZeit")
			pnlEinheit.AddView(sebZeit(i), 30%x, 140dip, 65%x,30dip)
			sebZeit(i).Tag = i

		End If
'		
		
		lblZeitanzeige(i).Initialize("lblZeitanzeige")
		pnlEinheit.AddView(lblZeitanzeige(i), 30%x + 75dip, 75dip, 44%x ,40dip)
		lblZeitanzeige(i).tag = i
		lblZeitanzeige(i).Color = Colors.black
		lblZeitanzeige(i).TextSize = 30
		lblZeitanzeige(i).Gravity = Gravity.CENTER


		DateTime.TimeFormat = "HH:mm:ss"


		
		Select i
			Case 0
				
				lblTafel(i).Color = Colors.White
				sebZeit(i).Max = 120 * 60000
				sebZeit(i).Value = sebZeitValue(0)
				lblTafel(i).Text = "  Gesamttrainingszeit: " 
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
			Case 1
				lblTafel(i).Color = Colors.Green
				sebZeit(i).Max = 20 * 60000
				sebZeit(i).Value = sebZeitValue(1)
				lblTafel(i).Text = "  Erwärmung Allgemein: " 
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				
			Case 2	
				
				lblTafel(i).Color = Colors.Green
				sebZeit(i).Max = 40000
				sebZeit(i).Value = sebZeitValue(2)
				lblTafel(i).Text = "  Zeit pro Erwärmungsübung(A): "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
			Case 3
				
				lblTafel(i).Color = Colors.Yellow
				sebZeit(i).Max = 20 * 60000
				sebZeit(i).Value = sebZeitValue(3)
				lblTafel(i).Text = "  Erwärmung Speziell"
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				
			Case 4	
				
				lblTafel(i).Color = Colors.Yellow
				sebZeit(i).Max = 60000
				sebZeit(i).Value = sebZeitValue(4)
				lblTafel(i).Text = "  Zeit pro Erwärmungsübung(S) " 
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				

			Case 5
				
				lblTafel(i).Color = Colors.Red
				sebZeit(i).Max = 60 * 60000
				sebZeit(i).Value = sebZeitValue(5)
				lblTafel(i).Text = "  Einzelübungen: "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				
			Case 6
				lblTafel(i).Color = Colors.Red
				sebZeit(i).Max = 3 * 60000
				sebZeit(i).Value = sebZeitValue(6)
				lblTafel(i).Text = "  Pausen zwischen Übungen: "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				

			Case 7	
				lblTafel(i).Color = Colors.Red
				sebZeit(i).Max = 5 * 60000
				sebZeit(i).Value = sebZeitValue(7)
				lblTafel(i).Text = "  Zeit pro Übung: "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				
			Case 8
					
				lblTafel(i).Color = Colors.Blue
				sebZeit(i).Max = 60 * 60000
				sebZeit(i).Value = sebZeitValue(8)
				lblTafel(i).Text = "  Partnerübungen: "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				
			Case 9
				lblTafel(i).Color = Colors.Blue
				sebZeit(i).Max = 3 * 60000
				sebZeit(i).Value = sebZeitValue(9)
				lblTafel(i).Text = "  Pausen zwischen den Partnerübungen: " 
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)

			Case 10	
				
				lblTafel(i).Color = Colors.Blue
				sebZeit(i).Max = 5 * 60000
				sebZeit(i).Value = sebZeitValue(10)
				lblTafel(i).Text = "  Zeit pro Übung: "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)

				
			Case 11
				
				lblTafel(i).Color = Colors.Cyan
				sebZeit(i).Max = 60 * 60000
				sebZeit(i).Value = sebZeitValue(11)
				lblTafel(i).Text = "  Kreis / Kraftübungen: " 
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				
			Case 12
				lblTafel(i).Color = Colors.Cyan
				sebZeit(i).Max = 3 * 60000
				sebZeit(i).Value = sebZeitValue(12)
				lblTafel(i).Text = "  Pausen: "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)

			Case 13
				
				lblTafel(i).Color = Colors.cyan
				sebZeit(i).Max = 5 * 60000 
				sebZeit(i).Value = sebZeitValue(13)
				lblTafel(i).Text = "  Zeit pro Kraftübung: "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)

				
			Case 14
				
				lblTafel(i).Color = Colors.Yellow
				sebZeit(i).Max = 10 * 60000
				sebZeit(i).Value = sebZeitValue(14)
				lblTafel(i).Text = "  Dehnung: "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
			Case 15
				
				lblTafel(i).Color = Colors.Yellow
				sebZeit(i).Max = 60000
				sebZeit(i).Value = sebZeitValue(15)
				lblTafel(i).Text = "  Zeit pro Dehnungsübung "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
			Case 16
				
				lblTafel(i).Color = Colors.Green
				sebZeit(i).Max = 10 * 60000
				sebZeit(i).Value = sebZeitValue(16)
				lblTafel(i).Text = "  Cool Down "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
			Case 17
				
				lblTafel(i).Color = Colors.Green
				sebZeit(i).Max = 60000
				sebZeit(i).Value = sebZeitValue(17)
				lblTafel(i).Text = "  Zeit pro  Cool Down Übung"
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
			Case 18
			
				
				lblTafel(i).Color = Colors.Black
				sebZeit(i).Max = 6000 'KleineZeit
				sebZeit(i).Value = 6000 'KleineZeit
				lblTafel(i).Text = "  11 "
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + sebZeit(i).Value)
				
			Case Else
				Msgbox("Listenerzeugung","Fehler in:")
					
				
		End Select	
			


	Next
	

	
	'Scrollview höhe festlegen
	scvHintergrund.Panel.Height = AnzahlDerEinstellungen * Panelhoehe
End Sub


Sub btnSpeichern_Click



		Dim result As Int
			
			Dim Map1 As Map
	    		Map1.Initialize
	    		Map1.Put("Gesamttrainingzeit",Zeit(0))
				Map1.Put("ErwaermungAllgemein",Zeit(1))
				Map1.Put("ErwaermungAllgemeinUebung",Zeit(2))
				Map1.Put("ErwaermungSpeziell",Zeit(3))
				Map1.Put("ErwaermungSpeziellUebung",Zeit(4))
				Map1.Put("EinzelUebungen",Zeit(5))
				Map1.Put("EinzelUebungenPausen",Zeit(6))
				Map1.Put("EinzelUebungenUebungen",Zeit(7))
				Map1.Put("PartnerUebungen",Zeit(8))
				Map1.Put("PartnerUebungenPausen",Zeit(9))
				Map1.Put("PartnerUebungenUebungen",Zeit(10))			
				Map1.Put("KraftUebungen",Zeit(11))
				Map1.Put("KraftUebungenPausen",Zeit(12))
				Map1.Put("KraftUebungenUebungen",Zeit(13))			
				Map1.Put("Dehnung",Zeit(14))
				Map1.Put("DehnungUebung",Zeit(15))			
				Map1.Put("CoolDown",Zeit(16))
				Map1.Put("CooldownUebung",Zeit(17))

			
			result = Msgbox2(" Gesamttrainingzeit= " & (Zeit(0)/60000) & CRLF & " ErwaermungAllgemein= " & (Zeit(1)/60000) & CRLF & " ErwaermungAllgemeinUebung= " & (Zeit(2)/60000) & CRLF & " ErwaermungSpeziell= " & (Zeit(3)/60000) & CRLF & " ErwaermungSpeziellUebung= " & (Zeit(4)/60000) & CRLF & " EinzelUebungen= " & (Zeit(5)/60000) & CRLF & " EinzelUebungenPausen= " & (Zeit(6)/60000) & CRLF & " EinzelUebungenUebungen= " & (Zeit(7)/60000) & CRLF & " PartnerUebungen= " & (Zeit(8)/60000) & CRLF & " PartnerUebungenPausen= " & (Zeit(9)/60000) & CRLF & " PartnerUebungenUebungen= " & (Zeit(10)/60000) & CRLF & " KraftUebungen= " & (Zeit(11)/60000) & CRLF & " KraftUebungenPausen= " & (Zeit(12)/60000) & CRLF & " KraftUebungenUebungen= " & (Zeit(13)/60000) & CRLF & " Dehnung= " & (Zeit(14)/60000) & CRLF & " DehnungUebung= " & (Zeit(15)/60000) & CRLF & " CoolDown= " & (Zeit(16)/60000) & CRLF & " CooldownUebung= " & (Zeit(17)/60000), "Speichern und Weiter" , "Ja","Nein","",LoadBitmap(File.DirAssets,"mamaLogo.png"))

		'	result = Msgbox2("Timedelay = " & (Timedelay/1000) &" s"& CRLF & 	"Runden = " & Runden& CRLF & "Kampfzeit = " & (Kampfzeit/1000) &" s"&  CRLF & "Pause = " & (Pause/1000)&" s", "Speichern und Weiter" , "Ja","Nein","",LoadBitmap(File.DirAssets,"mamaLogo.png"))
			If result = DialogResponse.POSITIVE Then 
				File.WriteMap(File.DirRootExternal & Main.Unterordner1, Setupdatei, Map1)
				ToastMessageShow("Erfolgreich gespeichert",False)
				StartActivity(Training)
			Else
			End If

End Sub



Sub btnHoch_click

	scvHintergrund.ScrollPosition = 0
End Sub


Sub btnWeiter_Click
	StartActivity(Training)
End Sub



Sub SeekbarZeit_ValueChanged (Value As Int, UserChanged As Boolean)
	
	Dim i As Int
	Dim sb As SeekBar
	sb = Sender
	i = sb.Tag
	
		Select i
		
			Case 0
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 1
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 2
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))	
			Case 3
				Zeit(i) = Value 
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 4
				Zeit(i) = Value 
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 5
			 	Zeit(i) = Value	
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i)) 
			Case 6
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 7
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 8
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 9
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 10
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i)) 		
			Case 11
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 12
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 13
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 14
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 15
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i)) 
			Case 16
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i)) 	
			Case 17
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i))
			Case 18
				Zeit(i) = Value
				If ToggelJaNein(i).Checked = False Then Zeit(i) = 1
				lblZeitanzeige(i).Text = DateTime.Time(Ausgleichszahl + Zeit(i)) 

				
		Case Else
		
		End Select

	i = Zeit(1) + Zeit(3) + Zeit(5) + Zeit(8) + Zeit(11) + Zeit(14) + Zeit(16)
	GesamtzeitAnzeige.Progress = (100/Zeit(0)) * (Zeit(1) + Zeit(3) + Zeit(5) +  Zeit(8) + Zeit(11) + Zeit(14) + Zeit(16))
	lblGesamtzeit.Gravity = Gravity.CENTER
	lblGesamtzeit.Text = "IST: " & DateTime.Time(Ausgleichszahl + i) & "      SOLL: " & DateTime.Time(Ausgleichszahl + Zeit(0))
	

End Sub
