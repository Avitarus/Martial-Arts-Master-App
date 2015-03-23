Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

'Einstellungen Trainingszeit Kickboxen 

'Okay



'Activity module
Sub Process_Globals

	Dim Trainingszeit1 As Int		: Trainingszeit1 = 50000
	Dim Pause1 As Int				: Pause1 = 60000
	Dim Gesamttrainingszeit As Int	: Gesamttrainingszeit = 60 * 60000
	Dim PauseMachen As Boolean

End Sub

Sub Globals

	Dim spnTraining1, spnPause1 As Spinner
	Dim Seekbar1 As SeekBar: 
	Dim Label8, Label1 As Label
	
	Dim ToggleButton1 As ToggleButton
End Sub

Sub Activity_Create(FirstTime As Boolean)

	Activity.Title = "Einstellungen Training Stunde"
		
	Activity.LoadLayout("EinstellungenTrainingKick")
	Label1.Text = Main.Ueberschrift' & CRLF & "Einstellungen"
	Seekbar1.Value = 45
			
				
				spnTraining1.Prompt = "Übung:"
				spnTraining1.Add("10 sec")
				spnTraining1.Add("20 sec")
				spnTraining1.Add("30 sec")
				spnTraining1.Add("40 sec")
				spnTraining1.Add("50 sec")
				spnTraining1.Add("1 min")
				spnTraining1.Add("2 min")
				spnTraining1.Add("3 min")
				spnTraining1.Add("4 min")
				spnTraining1.Add("5 min")
				spnTraining1.SelectedIndex = 2
				Trainingszeit1 = (2 +1) * 10000
				If 2 > 4 Then Trainingszeit1 = (2 - 4) * 60000				
				
				
				spnPause1.Prompt = "Pause:"
				spnPause1.Add("10 sec")
				spnPause1.Add("20 sec")
				spnPause1.Add("30 sec")
				spnPause1.Add("40 sec")
				spnPause1.Add("50 sec")
				spnPause1.Add("1 min")
				spnPause1.Add("2 min")
				spnPause1.Add("3 min")
				spnPause1.Add("4 min")
				spnPause1.Add("5 min")
				spnPause1.SelectedIndex = 5			
				Pause1 = (5 +1) * 10000
				If 5 > 4 Then Pause1 = (5 - 4) * 60000
		
				Label8.Text = "Gesamttrainingszeit: " & Seekbar1.Value & " min"
				
				PauseMachen = True
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub spnTraining1_ItemClick (Position As Int, Value As Object)
	
	Trainingszeit1 = (Position +1) * 10000
	If Position > 4 Then Trainingszeit1 = (Position - 4) * 60000
	
End Sub


Sub spnPause1_ItemClick (Position As Int, Value As Object)
	
	Pause1 = (Position +1) * 10000
	If Position > 4 Then Pause1 = (Position - 4) * 60000
	
End Sub



Sub SeekBar1_ValueChanged (Value As Int, UserChanged As Boolean)
	
	Label8.Text = "Gesamttrainingszeit: " & Seekbar1.Value & " min"
	Gesamttrainingszeit = Seekbar1.Value * 60000
	If Gesamttrainingszeit = 0 Then Gesamttrainingszeit = Gesamttrainingszeit + 1
	
	
End Sub

Sub btnStart_Click

	If Gesamttrainingszeit < 1.01 Then 
		ToastMessageShow("Gesamttrainingszeit einstellen",False)
	Else
		StartActivity(TrainingKickboxen)
	End If
	

	
End Sub

Sub ToggleButton1_CheckedChange(Checked As Boolean)
	
	If Checked = False Then 
		PauseMachen =  False
	Else
		PauseMachen = True
	End If
	
	
	
End Sub