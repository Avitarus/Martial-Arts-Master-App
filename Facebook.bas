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
				
				'###################################################################
	   '# Create a facebook app at: https://developers.facebook.com/apps/ #
				'###################################################################
				
	   Dim appID As String 
	   Dim appSecret As String

	   'This is a URL you put on SITE URL when you created your Facebook App
	   Dim RedirectUri As String 
	
	   Dim AccessToken As String
	   Dim DataRequestLink As String 

	   Dim hc As HttpClient
				Dim req As HttpRequest				

End Sub

Sub Globals
	
	   'These global variables will be redeclared each time the activity is created.
	   'These variables can only be accessed from this module.
				
				Dim MainPanel As Panel
				Dim FacebookPage As WebView
				Dim CameraPanel As Panel
				Dim ControlsPanel As Panel
				Dim TakePictureButton As Button
				Dim CloseButton As Button
				
				Dim Cam As AdvancedCamera
				
				Dim FakePanel As Panel
				Dim DescriptionBox As EditText
				Dim SendButton As Button
				Dim CancelButton As Button

End Sub

Sub Activity_Create(FirstTime As Boolean)

    'Don't change this line
	   DataRequestLink = "https://graph.facebook.com/me/photos"
				
				appID = ""
    appSecret = ""
				RedirectUri = "https://sites.google.com/site/martialartsmasterapp/" 'e.g. http://www.mywebsite.com/  <-- Add slash at the end
				
				hc.Initialize("hc")

    Activity.LoadLayout("Facebook")
				
				ProgressDialogShow("Activating camera...")
				
				MainPanel.Left = (100%x - MainPanel.Width) / 2
				
    Cam.Initialize(CameraPanel, "Cam")
				
				Cam.StopPreview				

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause(UserClosed As Boolean)

End Sub

'##########################
'######## Controls ########
'##########################

Sub CameraPanel_Touch(Action As Int, X As Float, Y As Float) As Boolean 'Return True to consume the event

    Return True
	
End Sub

Sub ControlsPanel_Touch(Action As Int, X As Float, Y As Float) As Boolean 'Return True to consume the event

    Return True
	
End Sub

Sub TakePictureButton_Click

    TakePictureButton.Enabled = False
				
				Cam.TakePicture
	
End Sub

Sub CloseButton_Click

    Activity.Finish
	
End Sub

Sub FakePanel_Touch(Action As Int, X As Float, Y As Float) As Boolean 'Return True to consume the event
	
	   Return True
				
End Sub

Sub SendButton_Click

	   If AccessToken = "" Then

		     Dim scope As String

       'Ask for special permission to publish
       scope = "publish_stream"

		     FacebookPage.LoadUrl("https://m.facebook.com/dialog/oauth?client_id=" & appID & "&redirect_uri=" & RedirectUri & "&scope=" & scope & "&response_type=token")
							
	   Else

		     GetFeeds
	
	   End If
	
End Sub

Sub CancelButton_Click

    Cam.StartPreview
				
				FakePanel.Visible = False
				
				TakePictureButton.Enabled = True
	
End Sub

Sub FacebookPage_PageFinished(Url As String)

    FacebookPage.Visible = False

				If Url.StartsWith("http://m.facebook.com/login.php") Then
		
		     ProgressDialogHide

							FacebookPage.Visible = True
	
	   End If

	   If Url.StartsWith(RedirectUri & "#access_token=") Then
		
		     FacebookPage.Visible = False
		
		     AccessToken = Url.SubString((RedirectUri & "#access_token=").Length)
		     AccessToken = AccessToken.SubString2(0, AccessToken.IndexOf("&"))
		
		     GetFeeds
	
	   End If

End Sub

'##########################
'######## Routines ########
'##########################

Sub Cam_Ready(Success As Boolean)

    If Success Then
				
				   Cam.StartPreview
							
							TakePictureButton.Enabled = True
			
				Else
				
				   ToastMessageShow("Cannot open camera.", True)
							
				End If
				
				ProgressDialogHide
	
End Sub

Sub Cam_PictureTaken(Data() As Byte)

    Dim out As OutputStream
				
				out = File.OpenOutput(File.DirRootExternal, "SnapShot.png", False)
				out.WriteBytes(Data, 0, Data.Length)
				out.Close

				FakePanel.Visible = True
				
End Sub

Sub GetFeeds
 
    'Add files
    Dim files As List

	   files.Initialize
  
    Dim fd As FileData

	   fd.Initialize
    fd.Dir = File.DirRootExternal

    fd.FileName	= "SnapShot.png"
    fd.KeyName = "upfile"
    fd.ContentType = "application/octet-stream"
    files.Add(fd)

   	'Add Description
    Dim nv As Map
    
				nv.Initialize
    nv.Put("message", DescriptionBox.Text.Trim)
	
	   ProgressDialogShow("Uploading to Facebook...")
	
    req = MultipartPost.CreatePostRequest(DataRequestLink & "?access_token=" & AccessToken, nv, files)
				hc.Execute(req, 1)
				
End Sub

Sub hc_ResponseSuccess(Response As HttpResponse, TaskId As Int)
	
	   Response.Release
				
				ProgressDialogHide
		 
	   ToastMessageShow("Photo uploaded to Facebook.", True)
				
				FakePanel.Visible = False
				
				CancelButton_Click

End Sub

Sub hc_ResponseError(Response As HttpResponse, Reason As String, StatusCode As Int, TaskId As Int)
    
				If Response <> Null Then

							Msgbox("Error: " & Response.GetString("UTF8") & " - " & Reason, "Error")
							
							Response.Release
							
    End If
				
End Sub