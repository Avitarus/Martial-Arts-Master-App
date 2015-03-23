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
	Dim strPassword As String
	Dim tr As TextReader
	Dim tw As TextWriter
	Dim inputText As InputDialog
	Dim cmdChangePass As Button

End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	Activity.Title = "Passwortabfrage"
	
	If FirstTime Then
		If File.Exists(File.DirDefaultExternal & "/mama/Daten", "password_file.txt") = True Then 'Check to see if there is already a password file saved and therefore a custom password
			tr.Initialize(File.OpenInput(File.DirDefaultExternal & "/mama/Daten", "password_file.txt")) 
			strPassword = tr.ReadLine
			tr.Close ' Load the password into strPassword
		Else
			strPassword = "0000" ' If there is no password file then set strPassword to default
		End If
		
		Do While inputText.Input <> strPassword ' Keep displaying the input password input dialog until the correct password has been entered
			If inputText.Show("Bitte geben Sie Ihr Passwort ein. (0000)","Passwort","Sende","","Beenden",Null) = DialogResponse.NEGATIVE Then ' If the user selects exit then close the activity
				Activity.Finish
			End If
			If inputText.Input <> strPassword Then ' Display wrong password message if wrong password entered
				'ToastMessageShow("Falsches Passwort.", False)
				ExitApplication
			End If
		Loop
	End If


Activity.Finish

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


