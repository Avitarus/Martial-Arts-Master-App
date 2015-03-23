Type=Activity
Version=2.00
FullScreen=False
IncludeTitle=True
@EndOfDesignText@
'Activity module
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim SP As SoundPool
	Dim LoadId1, PlayId1, LoadId2, PlayId2 As Int
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Dim Button1 As Button
	Dim Button2 As Button
End Sub

Sub Activity_Create(FirstTime As Boolean)
	If FirstTime Then
		SP.Initialize(4)
		LoadId1 = SP.Load(File.DirAssets, "explode.wav")
		LoadId2 = SP.Load(File.DirAssets, "bounce.wav")
	End If
	Activity.LoadLayout("1")
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Button1_Click
	PlayId1 = SP.Play(LoadId1, 1, 1, 1, 0, 1)
End Sub

Sub Button2_Click
	PlayId2 = SP.Play(LoadId2, 1, 1, 1, 0, 1)	
End Sub

