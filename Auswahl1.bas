Type=Activity
Version=3
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region


Sub Process_Globals


End Sub
Sub Globals
    Dim ListView2 As ListView
	Dim Clubbesitzer As Boolean : Clubbesitzer = True
End Sub

Sub Activity_Create(FirstTime As Boolean)
    
	 Activity.Title = "Sie sind hier: Startseite > " & Main.strListview1Ergebnis
	ListView2.Initialize("ListView2")
	
			ListView2.FastScrollEnabled = True 'schnellscrollgriff
			ListView2.SingleLineLayout.Label.Gravity = Gravity.CENTER
			ListView2.SingleLineLayout.Label.TextSize = 20
			ListView2.SingleLineLayout.ItemHeight = 35dip
			ListView2.SingleLineLayout.Label.TextColor = Colors.red'RGB(Rnd(0, 150), Rnd(0,150), Rnd(0,150))
			ListView2.SingleLineLayout.Label.Color = Colors.RGB(255, 165, 0)
			ListView2.SingleLineLayout.Label.Typeface = Typeface.DEFAULT_BOLD
	
	
	
	
	
	If Main.strListview1Ergebnis = "Training" Then Training
	If Main.strListview1Ergebnis = "Statistik" Then Statistik
	If Main.strListview1Ergebnis = "Pläne" Then Plaene
	If Main.strListview1Ergebnis = "Einstellungen Training" Then EinstellungenTraining
	If Main.strListview1Ergebnis = "Einstellungen App" Then EinstellungenApp
	If Main.strListview1Ergebnis = "1.Hilfe / Psychologie" Then Psychologie
	If Main.strListview1Ergebnis = "Leistungsdiagnostik" Then Leistungsdiagnostik
	If Main.strListview1Ergebnis = "Recht und Gesetz" Then Recht
	If Main.strListview1Ergebnis = "Business" Then Business
	

    
    Activity.AddView(ListView2, 0, 0, 100%x, 100%y)
End Sub
Sub ListView1_ItemClick (Position As Int, Value As Object)
    Activity.Title = Value
	
	
	
	
End Sub


Sub Statistik
	ListView2.Initialize("Listview2")
    
        ListView2.AddSingleLine("Training")

    
        ListView2.AddSingleLine("Training")
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub
Sub Training
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub
Sub Plaene
	ListView2.Initialize("Listview2")
    
        ListView2.AddSingleLine("Training")
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub



Sub EinstellungenTraining
	
    
        ListView2.AddSingleLine("Training")
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub

Sub EinstellungenApp
	
    
        ListView2.AddSingleLine("Training")
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub
Sub Psychologie
	
    
        ListView2.AddSingleLine("Training")
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub

Sub Leistungsdiagnostik
	
    
        ListView2.AddSingleLine("Training")
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub
Sub Recht
	
    
        ListView2.AddSingleLine("Training")
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub
Sub Business
	
    
        ListView2.AddSingleLine("Training")
		ListView2.AddSingleLine("Statistik")
		ListView2.AddSingleLine("Pläne")
		ListView2.AddSingleLine("Personendatenbank")
		ListView2.AddSingleLine("Trainingstagebuch")
		ListView2.AddSingleLine("Tauschring")
		ListView2.AddSingleLine("Shop")
		ListView2.AddSingleLine("Einstellungen Training")	
		ListView2.AddSingleLine("Einstellungen App")
		If Clubbesitzer Then
			ListView2.AddSingleLine("1. Hilfe / Psychologie")
			ListView2.AddSingleLine("Leistungsdiagnostik")
			ListView2.AddSingleLine("Recht und Gesetz")
			ListView2.AddSingleLine("Business")
		End If	



End Sub


Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


