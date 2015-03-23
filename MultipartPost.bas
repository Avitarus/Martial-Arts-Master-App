Type=StaticCode
Version=3
@EndOfDesignText@
'Code module

'Subs in this code module will be accessible from all modules.

Sub Process_Globals

	   Type FileData(Dir As String, FileName As String, KeyName As String, ContentType As String)

End Sub

Sub CreatePostRequest(URL As String, NameValues As Map, Files As List) As HttpRequest

	   Dim boundary As String
	
	   boundary = "---------------------------1461124740692"
	
	   Dim stream As OutputStream
	
	   stream.InitializeToBytesArray(20)
	
	   Dim EOL As String
	
	   EOL = Chr(13) & Chr(10) 'CRLF constant matches Android end of line character which is chr(10).
	
	   Dim b() As Byte
	
	   If NameValues <> Null AND NameValues.IsInitialized Then
		
		     'Write the name/value pairs
		     Dim key, value As String
		
		     For I	= 0 To NameValues.Size - 1
			
			        key = NameValues.GetKeyAt(I)
			        value = NameValues.GetValueAt(I)
			        b = ("--" & boundary & EOL & "Content-Disposition: form-data; name="	& QUOTE & key & QUOTE & EOL & EOL & value & EOL).GetBytes("UTF8")
			        stream.WriteBytes(b, 0, b.Length)
		
		     Next
							
	    End If
	
	    If Files <> Null AND Files.IsInitialized Then
		
		      'Write the files
		      Dim fd As FileData

		      For I = 0 To Files.Size - 1

         			fd = Files.Get(I)
			        
											 b = ("--" & boundary & EOL & "Content-Disposition: form-data; name=" & QUOTE & fd.KeyName & QUOTE & "; filename=" & QUOTE & fd.FileName & QUOTE	& ";" & "Content-Type: " & EOL & fd.ContentType & EOL & EOL).GetBytes("UTF8")
			
			         stream.WriteBytes(b, 0, b.Length)
			
			         Dim In As InputStream
			
			         In = File.OpenInput(fd.Dir, fd.FileName)
			
			         File.Copy2(In, stream) 'Read the file and write it to the stream
			         b = EOL.GetBytes("UTF8")
			         stream.WriteBytes(b, 0, b.Length)
		
		      Next
								
	    End If
					
	    b = (EOL & "--" & boundary & "--" & EOL).GetBytes("UTF8")
	    stream.WriteBytes(b, 0, b.Length)
	    b = stream.ToBytesArray

	    Dim request As HttpRequest
	    
					request.InitializePost2(URL, b)
	    request.SetContentType("multipart/form-data; boundary=" & boundary)
	    request.SetContentEncoding("UTF8")
	
	    Return request
					
End Sub