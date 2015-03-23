Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	Dim TTS1 As TTS
End Sub

Sub Globals
	Dim barPitch As SeekBar
	Dim barSpeechRate As SeekBar
	Dim btnSpeak As Button
	Dim EditText1 As EditText
	Dim spnrLanguages As Spinner
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("1")
	spnrLanguages.AddAll(Array As String("en", "fr", "de"))
End Sub
Sub TTS1_Ready (Success As Boolean)
	If Success Then
		'enable all views
		For i = 0 To Activity.NumberOfViews - 1
			Activity.GetView(i).Enabled = True
		Next
		btnSpeak_Click 'play first sentence
	Else
		Msgbox("Error initializing TTS engine.", "")
	End If
End Sub
Sub Activity_Resume
	If TTS1.IsInitialized = False Then
		TTS1.Initialize("TTS1")
	End If
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	TTS1.Release
End Sub

Sub btnSpeak_Click
	If EditText1.Text.Length > 0 Then
		TTS1.Speak(EditText1.Text, True)
		EditText1.SelectAll
	End If
End Sub
Sub barSpeechRate_ValueChanged (Value As Int, UserChanged As Boolean)
	If UserChanged Then
		TTS1.SpeechRate = Value / 10
	End If
End Sub
Sub barPitch_ValueChanged (Value As Int, UserChanged As Boolean)
	If UserChanged Then
		TTS1.Pitch = Value / 10
	End If
End Sub
Sub spnrLanguages_ItemClick (Position As Int, Value As Object)
	If btnSpeak.Enabled = False Then Return
	If TTS1.SetLanguage(Value, "") = False Then
		ToastMessageShow("Language data not found.", True)
		Return
	End If
End Sub