Type=Activity
Version=2.00
FullScreen=False
IncludeTitle=True
@EndOfDesignText@
'Activity module
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim tabs As AHViewPagerTabs
	Dim line As Panel
	Dim spTheme, spnErwaermung, spnTraining, spnPause As Spinner

	
End Sub

Sub Activity_Create(FirstTime As Boolean)

		Activity.LoadLayout("EinstellungenStunde")

'ßßßßßßßßßßßßßßßßßßßßßßßßßßßßßßß
'bilder rotieren lassen!!!!
'ßßßßßßßßßßßßßßßßßßßßßßßßßßßßßß







'Dim pan As Panel
'	Dim pi As PanelInfo
'	
'	pan = Page
'	pi = pan.Tag
'	
'	Select pi.PanelType
'		Case TYPE_EINSTELLUNGEN
'			If Not(pi.LayoutLoaded) Then
'				pan.LoadLayout("settings")
'				pi.LayoutLoaded = True
				spTheme.Add("Dark")
				spTheme.Add("Light")
				spTheme.SelectedIndex = Main.CurrentTheme
				
				spnErwaermung.add("10 sec")
				spnErwaermung.add("15 sec")
				spnErwaermung.add("20 sec")
				spnErwaermung.add("30 sec")
				spnErwaermung.add("40 sec")
				spnErwaermung.add("50 sec")
				spnErwaermung.add("1 min")
				spnErwaermung.add("2 min")
				spnErwaermung.add("3 min")
				spnErwaermung.add("4 min")
				spnErwaermung.add("5 min")
				spnErwaermung.SelectedIndex = Main.ZeitErwaermung
				
				
				spnTraining.Add("10 sec")
				spnTraining.Add("15 sec")
				spnTraining.Add("20 sec")
				spnTraining.Add("30 sec")
				spnTraining.Add("40 sec")
				spnTraining.Add("50 sec")
				spnTraining.Add("1 min")
				spnTraining.Add("2 min")
				spnTraining.Add("3 min")
				spnTraining.Add("4 min")
				spnTraining.Add("5 min")
				spnTraining.SelectedIndex = Main.ZeitProUebung
				
			  	spnPause.Add("10 sec")
				spnPause.Add("15 sec")
				spnPause.Add("20 sec")
				spnPause.Add("30 sec")
				spnPause.Add("40 sec")
				spnPause.Add("50 sec")
				spnPause.Add("1 min")
				spnPause.Add("2 min")
				spnPause.Add("3 min")
				spnPause.Add("4 min")
				spnPause.Add("5 min")
				spnPause.SelectedIndex = Main.ZeitPause
'				
'				
'			End If
'	End Select		
'	
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Sub spTheme_ItemClick (Position As Int, Value As Object)
	Main.CurrentTheme = Position
	SetTheme(Main.CurrentTheme)
End Sub

Sub SetTheme(Theme As Int)
	Select Theme
		Case 0
			tabs.Color = Colors.Black
			tabs.BackgroundColorPressed = Colors.Blue
			tabs.LineColorCenter = Colors.Green
			tabs.TextColor = Colors.LightGray
			tabs.TextColorCenter = Colors.Green
			line.Color = Colors.Green
		Case 1
			tabs.Color = Colors.White
			tabs.BackgroundColorPressed = Colors.Blue
			tabs.LineColorCenter = Colors.DarkGray
			tabs.TextColor = Colors.LightGray
			tabs.TextColorCenter = Colors.DarkGray
			line.Color = Colors.DarkGray
	End Select
End Sub

Sub spnErwaermung_ItemClick (Position As Int, Value As Object)
	Main.ZeitErwaermung = Position
	SetzeZeitErwaermung(Main.ZeitErwaermung)
End Sub

Sub SetzeZeitErwaermung(i As Int)

	Select i
		Case 0
			Main.Zeit = 10000
		Case 1
			Main.Zeit = 15000
		Case 2
			Main.Zeit = 20000
		Case 3
			Main.Zeit = 30000
		Case 4
			Main.Zeit = 40000
		Case 5
			Main.Zeit = 50000
		Case 6
			Main.Zeit = 60000
		Case 7
			Main.Zeit = 120000
		Case 8
			Main.Zeit = 180000
		Case 9
			Main.Zeit = 240000	
		Case 10	
			Main.Zeit = 300000
			
		Case Else
		    Main.Zeit = 5000
		
	End Select
End Sub

Sub spnTraining_ItemClick (Position As Int, Value As Object)
	
	Main.ZeitProUebung = Position
	SetzeZeitTraining(Main.ZeitProUebung)
End Sub

Sub SetzeZeitTraining(i As Int)

	Select i
		Case 0
			Main.Zeit = 10000
		Case 1
			Main.Zeit = 15000
		Case 2
			Main.Zeit = 20000
		Case 3
			Main.Zeit = 30000
		Case 4
			Main.Zeit = 40000
		Case 5
			Main.Zeit = 50000
		Case 6
			Main.Zeit = 60000
		Case 7
			Main.Zeit = 120000
		Case 8
			Main.Zeit = 180000
		Case 9
			Main.Zeit = 240000	
		Case 10	
			Main.Zeit = 300000
		Case Else
		    Main.Zeit = 5000
		
	End Select
End Sub

Sub spnPause_ItemClick (Position As Int, Value As Object)
	
	Main.ZeitPause = Position
	SetzeZeitPause(Main.ZeitPause)
End Sub

Sub SetzeZeitPause(i As Int)

	Select i
		Case 0
			Main.Zeit = 10000
		Case 1
			Main.Zeit = 15000
		Case 2
			Main.Zeit = 20000
		Case 3
			Main.Zeit = 30000
		Case 4
			Main.Zeit = 40000
		Case 5
			Main.Zeit = 50000
		Case 6
			Main.Zeit = 60000
		Case 7
			Main.Zeit = 120000
		Case 8
			Main.Zeit = 180000
		Case 9
			Main.Zeit = 240000	
		Case 10	
			Main.Zeit = 300000
		Case Else
		    Main.Zeit = 5000
		
	End Select
End Sub

Sub btnSpeichern_Click
				StartActivity(Training)
				'Activity.Finish
				
End Sub