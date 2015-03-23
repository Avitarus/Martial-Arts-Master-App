Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim ProgName As String			: ProgName = "Kampfsport-Spiele-Sammlung"
	Dim ProgVersion As String		: ProgVersion = "V 1.0"
	Dim ProgAuthor As String		: ProgAuthor = "Frank Albrecht"
	Dim ProgDate As String			: ProgDate = "Aug 2012"
	Dim HelpIndex As Int			: HelpIndex = 0
	Dim HelpIndexMax As Int			: HelpIndexMax = 22
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim pnlHelp As Panel
	Dim btnHelpPrev, btnHelpNext, btnHelpLast,btnHelpFirst, btnHelpOK  As Button
	Dim lblHelp1, lblHelp2, lblHelp3 As Label
	Dim scvHelp As ScrollView
	Dim StrUtils As StringUtils
	'Dim tracker As AnalyticsTracker
End Sub

Sub Activity_Create(FirstTime As Boolean)

'If Main.ueberwachung Then tracker.TrackEvent("Spiet","Starhj","Kampfsportiele",100)



	pnlHelp.Initialize("")
	Activity.AddView(pnlHelp, 0, 0, 100%x, 100%y)
	pnlHelp.LoadLayout("KampfsportSpiele") 'ehemals main
	Activity.Title = "Kampfsport Spiele"
	scvHelp.Panel.LoadLayout("KampfsportSpieleUeberschrift")'scrollview") 
End Sub

Sub Activity_Resume
'If Main.ueberwachung Then tracker.Start




	InitHelp
End Sub

Sub Activity_Pause (UserClosed As Boolean)

'If Main.ueberwachung Then tracker.Stop

End Sub

Sub InitHelp
	scvHelp.Top = btnHelpOK.Top + btnHelpOK.Height + 5dip
	scvHelp.Left = 5dip
	scvHelp.Height = Activity.Height - scvHelp.Top - 5dip

	scvHelp.Width = Activity.Width - 2 * scvHelp.Left
	scvHelp.Panel.Width = scvHelp.Width
	
	pnlHelp.Invalidate
	Activity.Invalidate

	btnHelpOK.Left = Activity.Width - btnHelpOK.Width - 5dip
	
	If Activity.Width / Activity.Height < 1 Then
		lblHelp1.Left = lblHelp2.Left
		lblHelp2.Top = lblHelp1.Top + lblHelp1.Height
		lblHelp1.Gravity = Gravity.LEFT
	Else
		lblHelp1.Gravity = Gravity.RIGHT
		lblHelp1.Left = scvHelp.Width - lblHelp1.Width - 2 * lblHelp2.Left
	End If
	lblHelp2.Gravity = Gravity.LEFT
	lblHelp3.Top = lblHelp2.Top + lblHelp2.Height
	lblHelp3.Left = 10dip
	lblHelp3.Width = scvHelp.Panel.Width - 20dip
	lblHelp3.Height = -2
	scvHelp.Invalidate
	
	If HelpIndex = 0 Then
		btnHelpPrev.Enabled = False
		btnHelpFirst.Enabled = False
		btnHelpNext.Enabled = True
		btnHelpLast.Enabled = True
	Else If HelpIndex = 0 Then
		btnHelpPrev.Enabled = True
		btnHelpFirst.Enabled = True
		btnHelpNext.Enabled = False
		btnHelpLast.Enabled = False
	Else
		btnHelpPrev.Enabled = True
		btnHelpFirst.Enabled = True
		btnHelpNext.Enabled = True
		btnHelpLast.Enabled = True
	End If
	
	DisplayHelp
End Sub

Sub DisplayHelp
' displaying the help texts
	Dim Reader As TextReader
	Dim txt1, txt2, txt3 As String
	Dim Height As Float
	
	scvHelp.ScrollPosition = 0

	Reader.Initialize(File.OpenInput(File.DirAssets,"spiel"&HelpIndex&".txt"))
	txt1 = Reader.ReadLine
	txt2 = Reader.ReadLine
	txt3 = Reader.ReadAll
	Reader.Close
	If HelpIndex = HelpIndexMax Then
		txt3 = "Program:" & TAB & TAB & ProgName & TAB & TAB & ProgVersion & CRLF
		txt3 = txt3 & "Date:" & TAB & TAB & TAB & ProgDate & CRLF
		txt3 = txt3 & "Geschrieben von:" & TAB & ProgAuthor & CRLF & CRLF
		txt3 = txt3 & "MAMA ist mit Hilfe der B4A Gemeinde programmiert worden." & CRLF
		txt3 = txt3 & "Mein Dank gilt auch: Nicolas Iven." & CRLF
		txt3 = txt3 & "Für mehr Details besuchen Sie: http://www.watchkido.de" & CRLF & CRLF
		txt3 = txt3 & "Library adapted for B4Android by Andrew Graham" & CRLF & CRLF
		
	End If

	lblHelp1.Text = txt1
	lblHelp2.Text = txt2
	lblHelp3.Text = txt3
	
	Height = StrUtils.MeasureMultilineTextHeight(lblHelp3, txt3)
	scvHelp.Panel.Height = lblHelp3.Top + Height + 10dip
	lblHelp3.Height = Height
	
End Sub

Sub btnHelpOK_Click
'	pnlHelp.Visible=False
	Activity.Finish
End Sub

Sub btnHelpMove_Click
' help button maganement

	Dim Send As View, btn As String
	
	Send=Sender
	btn=Send.Tag
	
	If HelpIndex > 0 AND btn = "Prev" Then
		HelpIndex = HelpIndex - 1
	Else If HelpIndex < HelpIndexMax AND btn = "Next" Then
		HelpIndex = HelpIndex + 1
	Else If btn = "First" Then
		HelpIndex = 0
	Else If btn = "Last" Then
		HelpIndex = HelpIndexMax
	End If

	If HelpIndex = 0 Then
		btnHelpPrev.Enabled = False
		btnHelpFirst.Enabled = False
	Else
		btnHelpPrev.Enabled = True
		btnHelpFirst.Enabled = True
	End If

	If HelpIndex=HelpIndexMax Then
		btnHelpNext.Enabled = False
		btnHelpLast.Enabled = False
	Else
		btnHelpNext.Enabled = True
		btnHelpLast.Enabled = True
	End If

	DisplayHelp
End Sub