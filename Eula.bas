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

End Sub

Sub Activity_Create(FirstTime As Boolean)

			Activity.Title = "Nutzerbedingungen"
			
			If FirstTime Then
			
			Dim TXT,TXT1 As String
			TXT = "Nutzerbedingungen" & CRLF

			'load a from sv card
			'If a = 0 Then
			'Msgbox2(TXT, "Nutzervertrag","Annehmen", "Ablehnen", "Webseite", Null)
			Msgbox2(File.ReadString(File.DirRootExternal & "/mama/Daten","Nutzerbedingungen.txt"),TXT,"Annehmen", "Ablehnen", "Webseite", Null)
			
			Else			
			Activity.Finish
			
			End If
			
			Activity.Finish

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


