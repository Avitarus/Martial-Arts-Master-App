Type=Activity
Version=1.90
FullScreen=False
IncludeTitle=True
@EndOfDesignText@

Sub Process_Globals
    Dim timer2 As Timer
	
End Sub

Sub Globals
    Dim exRoute As String 
    Dim List1 As List
    Dim FileNumber As Int 
    Dim FileCount As Int 
    Dim ImageView1 As ImageView
	Dim pws As PhoneWakeState
	
End Sub

Sub Activity_Create(FirstTime As Boolean)
		
		pws.KeepAlive(True) ' angeschaltet lassen
	    Activity.LoadLayout("viewtest")
	    timer2.Initialize("timer2",5000)
	    timer2.Enabled =False    
	    FileCount =0
	    List1.Initialize        
	    exRoute = File.DirRootExternal & "/Kampfkunstmeister"
	    List1 = File.ListFiles (exRoute )
    If List1.Size > 0 Then
	    FileCount = List1.Size 
	    FileNumber =-1
	    timer2.Enabled = True
    End If
                
End Sub

Sub timer2_tick
    Dim sFile As String 
		    FileNumber = FileNumber + 1
	    If FileNumber +1 > FileCount Then        
	      FileNumber=0
	    End If
		    sFile= List1.Get (FileNumber)
		    ImageView1.Bitmap=LoadBitmap(exRoute,sFile)
End Sub

Sub Activity_Resume

    pws.KeepAlive(True)
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
    pws.ReleaseKeepAlive
End Sub


Sub Button2_Click
	
End Sub
Sub Button1_Click
	
End Sub