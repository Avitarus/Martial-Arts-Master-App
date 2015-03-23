Type=Service
Version=2.00
StartAtBoot=False
@EndOfDesignText@
'Service module
Sub Process_Globals
	Dim rv As RemoteViews
	Dim Gewinnprozente, Wahrschein, seconds, minutes, hours, days, Stunde, Zeitzone As Int
	Dim Zielgewicht, AusgabePreis As Int
	Dim date0, date1, date2, elapsed As Long
	Dim AVIgewicht, SOLLGewicht, Nochgewicht, Genaugewicht, Optimalgewicht,Anfangsgewicht, Lohn, Money As Double
	Dim Zieltag, Starttag,Name, Leer  As String


End Sub

Sub Service_Create
	'Set the widget to update every 0 minutes.
	rv = ConfigureHomeWidget("Zieltimer", "rv", 0, "DayCounter",True)
	Zeitzone = 2
	Gewinnprozente = 29 'für so hoch halte ich die gewinnwahrscheinlichkeit
	Anfangsgewicht = 99
	Zielgewicht = 86
	Name = "Avi"
	AusgabePreis = 300
	Starttag = "10/06/2011" 'Starttag
	Zieltag = "06/06/2013" 'gibts den tag?
	'Optimalgewicht = 188-100-10% '

End Sub

Sub Service_Start (StartingIntent As Intent)

	If rv.HandleWidgetEvents(StartingIntent) Then Return
	
End Sub

Sub rv_RequestUpdate

	rv.UpdateWidget
	
End Sub

Sub rv_Disabled
	StopService("")
End Sub

Sub Service_Destroy

End Sub

Sub Label1_Click
	
	Rechner
	ToastMessageShow(" + " & seconds & " sec", True)
	
End Sub

Sub Label2_Click

	Rechner
	ToastMessageShow("Präzises Sollgewicht: " & Genaugewicht,True)

End Sub



Sub Label4_Click

	Rechner
	ToastMessageShow("Es sind ca.: " & Money & " € zu holen.",True)

End Sub


Sub Button1_Click

	Rechner
	ToastMessageShow("Danke für die Prüfung!"& CRLF &"Du erhälst " & Lohn & " € dafür."& CRLF &"Für jede 100g die " & Name & " über dem Gewicht liegt, bekommst du nochmal 1 Euro!",True)
	
End Sub

Sub Rechner
	date0 = DateTime.DateParse(Starttag) - DateTime.TimeParse("00:00:01")
 	date1 = DateTime.DateParse(DateTime.Date(DateTime.Now)) + DateTime.TimeParse(DateTime.Time(DateTime.Now))
	date2 = DateTime.DateParse(Zieltag) + DateTime.TimeParse("23:59:59")'7
	elapsed = date2 - date1
	seconds = Round(elapsed / 1000)
	minutes = Floor(seconds / 60) Mod  60
	hours = Floor(seconds / 3600)
	days = Floor(hours / 24)
	hours = hours Mod 24
	hours = hours '+ Zeitzone ' zeitumstellung eingerechnet
	seconds = seconds Mod 60
	

	SOLLGewicht = Zielgewicht + (days*24 + hours + Zeitzone)*((Anfangsgewicht-Zielgewicht)/2700)'2725 die stunden ab jetzt
	Genaugewicht = SOLLGewicht
	Genaugewicht = NumberFormat2(SOLLGewicht, 0, 7, 7, False)
	SOLLGewicht = NumberFormat2(SOLLGewicht, 0, 1, 1, False)
	Nochgewicht = SOLLGewicht - Zielgewicht
	Nochgewicht = NumberFormat2(Nochgewicht, 0, 1, 1, False)
	Lohn = 0.20 * Rnd(1,25)'zwischen 20 cent und 5 euro
	Lohn = NumberFormat2(Lohn, 0, 2, 2, False)
	Money = (days*24 + hours + Zeitzone)*( AusgabePreis / 2720)
	Money = NumberFormat2(Money, 0, 2, 2, False)
	AVIgewicht = SOLLGewicht - 3
	
	
	rv.SetText("Label1", days & " Tage " & hours & " h " & minutes & " min bis 6.6.13") '1.8.12")
	rv.SetText("Label2", SOLLGewicht & " kg")' für NIC mit SOLLgewicht ersetzen
	rv.SetText("Label4","-"& Nochgewicht & " kg" & CRLF & AVIgewicht & " kg")
	rv.SetProgress("ProgressBar2", 100 - days)' Rnd(1,100)) '100 * (currentImage + 1) / imageFiles.Length)
	rv.SetText("Button1",Lohn & " fette Euro´s geholt!")
	rv.UpdateWidget
	 
End Sub
