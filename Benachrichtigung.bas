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

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Dim Label1 As Label
	Dim EditText1 As EditText
	Dim btnSMS As Button
	Dim btnEmail As Button
	Dim btnAbbruch As Button
	
	

End Sub

Sub Activity_Create(FirstTime As Boolean)

	Activity.LoadLayout("benachrichtigungProgrammierer")


End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub btnSMS_Click

	Dim p As PhoneSms
	
	If EditText1.Text.Length < 160 Then
		p.Send (Main.MeineNummer, EditText1.Text)
	Else
		SendLargeSms(Main.MeineNummer,EditText1.Text)
	End If
		

End Sub


Sub SendLargeSms(Destination As String, Message As String)
    Dim r As Reflector
    r.Target = r.RunStaticMethod("android.telephony.SmsManager", "getDefault", Null, Null)
    Dim parts As Object
    parts = r.RunMethod2("divideMessage", Message, "java.lang.String")
    r.RunMethod4("sendMultipartTextMessage", _
        Array As Object(Destination, Null, parts, Null, Null), _
        Array As String("java.lang.String", "java.lang.String", _
            "java.util.ArrayList", "java.util.ArrayList", "java.util.ArrayList"))
			
	ToastMessageShow("nachricht gesendet",False)		
			
End Sub	
	

Sub btnEmail_Click
	
	
	Dim Message As Email
	Message.To.Add(Main.MeineEmail)
	Message.Body = EditText1.Text
	Message.Subject = "Nachricht an den Programmierer"
	'Message.Attachments.Add(File.Combine(File.DirRootExternal, EditText1.text))
	StartActivity(Message.GetIntent)
	
	
End Sub
Sub btnAbbruch_Click
	
	Activity.Finish
	
End Sub


Sub Activity_KeyPress (KeyCode As Int) As Boolean                            
    Dim Antw As Int
	
	If KeyCode = KeyCodes.KEYCODE_BACK Then                                    ' Hardware-Zur�ck Taste gedr�ckt
        
		
			Antw = Msgbox2("Wollen Sie Beenden?", "A C H T U N G", "Ja", "", "Nein",Null)
			If Antw = DialogResponse.POSITIVE Then
				Return False
			Else
				Return True
			End If

   
	End If 
	
	
End Sub


