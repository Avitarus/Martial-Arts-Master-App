Type=Activity
Version=2.00
FullScreen=False
IncludeTitle=True
@EndOfDesignText@
'Activity module
Sub Process_Globals
    'These global variables will be declared once when the application starts.
    'These variables can be accessed from all modules.
    Dim Timer1 As Timer
End Sub

Sub Globals
    'These global variables will be redeclared each time the activity is created.
    'These variables can only be accessed from this module.

End Sub

Sub Activity_Create(FirstTime As Boolean)
    Timer1.Initialize("Timer1",60000)
    SetTimer
End Sub

Sub Activity_Resume

End Sub
Sub Activity_Pause (UserClosed As Boolean)
    Timer1.Enabled=False
End Sub
Sub SetTimer
    Dim Minute As Int
    Dim Hour As Int
	Dim now As Long
    'Determine the interval to the next 15 minute trigger and set the timer interval

    now=DateTime.now
    Minute=DateTime.GetMinute(now)
    Log (Minute)
    
    Select True
        Case (Minute < 15)
            Timer1.Interval=(15-Minute)*60000
            
        Case (Minute < 30)
            Timer1.Interval=(30-Minute)*60000
            
        Case (Minute < 45)
            Timer1.Interval=(45-Minute)*60000
            
        Case (Minute <= 59)
            Timer1.Interval=(60-Minute)*60000

        Case Else
            Log("Error setting timer")    
    End Select
    
    Log((Timer1.Interval/1000/60)&" Mins to go")

        Timer1.Enabled=True
    
End Sub

Sub Timer1_Tick
    ToastMessageShow("Timer Tick "&DateTime.Time(DateTime.Now),False)
    
    'Do whatever
    '.
    '.
    '.
    
    'Set interval for next 15 minute slot.
    Timer1.Interval=15*60000
End Sub