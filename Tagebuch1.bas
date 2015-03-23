Type=Activity
Version=2.5
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: true
	#IncludeTitle: false
#End Region



'Activity module
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim curValues As Map
	Dim DOB As Long
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim IME As IME
	Dim pgFrame As ScrollView
	Dim VM As ViewMgr
	Dim dd As DateDialog
End Sub

Sub Activity_Create(FirstTime As Boolean)
Dim DateText As String
	If FirstTime = True Then curValues.Initialize
	DateTime.TimeFormat = "yyyy-MM-dd HH:mm"
	DateTime.DateFormat = "MM/dd/yyyy"
	If curValues.Size = 0 Then
		DOB = DateTime.Now
	End If
	IME.Initialize("IME")
	IME.AddHeightChangedEvent
	If DOB = 9223372036854775807 Then DateText= "N/A" Else DateText= DateTime.Date(DOB)
	Activity.Color = Colors.RGB(255, 222, 173) ' Navajo White
	Activity.Title = "Targets"
	pgFrame.Initialize(Activity.Height)
	Activity.AddView(pgFrame, 0, 0, Activity.Width, Activity.Height)
	VM.Initialize(pgFrame.Panel, Activity.Width, True, True, True)
	VM.Padding = 8dip

'	VM.AddLabel(VM.Padding, -2, 1, 1, "Gewicht nach 30T:", 18, Colors.Black, "Gewicht")
'	VM.AddTextBox(60%x, -1, -50, 1, "Kasten1", "", 18, "Apt", False, VM.DataType_Uppercase_Words, "", VM.ActionBtn_Next, "", Me, 0, 15, Null)
'	VM.AddTextBox(-2, -1, -1, 1, "Zip", "", 18, "Zip", False, VM.DataType_Num, "", VM.ActionBtn_Next, "", Me, 0, 5, Null)
'	VM.Enabled("Kasten" & 1, False)
'	
'	VM.AddLabel(VM.Padding, -2, 1, 1, "Gewicht nach 30:", 18, Colors.Black, "Gewich")
'	VM.AddTextBox(60%x, -1, -50, 1, "Kaste1", "", 18, "Aptr", False, VM.DataType_Uppercase_Words, "", VM.ActionBtn_Next, "", Me, 0, 15, Null)
'	VM.AddTextBox(-2, -1, -1, 1, "Zip", "", 18, "Zipr", False, VM.DataType_Num, "", VM.ActionBtn_Next, "", Me, 0, 5, Null)
'	VM.Enabled("Kaste1" & 1, False)
'	
'	VM.AddLabel(VM.Padding, -2, 1, 1, "Gewicht nach 0T:", 18, Colors.Black, "Gewicht")
'	VM.AddTextBox(60%x, -1, -50, 1, "Kasten2", "", 18, "Apt", False, VM.DataType_Uppercase_Words, "", VM.ActionBtn_Next, "", Me, 0, 15, Null)
'	VM.AddTextBox(-2, -1, -1, 1, "Zip", "", 18, "Zip", False, VM.DataType_Num, "", VM.ActionBtn_Next, "", Me, 0, 5, Null)
'	VM.Enabled("Kasten" & 2, False)
	
	VM.AddLabel(VM.Padding, -1, 1, 1, "First Name:", 18, Colors.Black, "FirstName")
	VM.AddTextBox(140dip, -1, -1, 1, "FirstName", "", 18, "First Name", False, VM.DataType_Text, "", VM.ActionBtn_Next, "", Me, 0, 30, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Middle Name:", 18, Colors.Black, "MiddleName")
	VM.AddTextBox(140dip, -1, -1, 1, "MiddleName", "", 18, "Middle Name", False, VM.DataType_Text, "", VM.ActionBtn_Next, "", Me, 0, 30, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Last Name:", 18, Colors.Black, "LastName")
	VM.AddTextBox(140dip, -1, -1, 1, "LastName", "", 18, "Last Name", False, VM.DataType_Text, "", VM.ActionBtn_Next, "", Me, 0, 30, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Alias:", 18, Colors.Black, "Alias")
	VM.AddTextBox(140dip, -1, -1, 1, "Alias", "", 18, "Alias", False, VM.DataType_Text, "", VM.ActionBtn_Next, "", Me, 0, 30, Null)
	
	VM.AddLabel(VM.Padding, -2, 1, 1, "DOB:", 18, Colors.Black, "DOB")
	VM.AddTextBox(140dip, -1, -50, 1, "DOB", DateText, 18, "", False, 0, "",0, "", Me, 0, 10, Null)
	VM.Enabled("DOB", False)
	VM.AddButton(-2, -1, -1, 1, "DateBtn", "Set Date", 16, Colors.Black, "Get_Date", Me)
	
	VM.AddLabel(VM.Padding, -2, 1, 1, "Hair Color:", 18, Colors.Black, "Hair")
	VM.AddComboBox(140dip, -1, -1, 1, "Hair", "Select Hair Color", File.ReadList(File.DirAssets, "partyhair.txt"), 18, Colors.Black, "", Me, 20)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Eye Color:", 18, Colors.Black, "Eyes")
	VM.AddComboBox(140dip, -1, -1, 1, "Eyes", "Select Eye Color", File.ReadList(File.DirAssets, "partyeyes.txt"), 18, Colors.Black, "", Me, 10)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Race:", 18, Colors.Black, "Race")
	VM.AddComboBox(140dip, -1, -1, 1, "Race", "Select Race", File.ReadList(File.DirAssets, "partyrace.txt"), 18, Colors.Black, "", Me, 20)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Sex:", 18, Colors.Black, "Sex")
	VM.AddComboBox(140dip, -1, -1, 1, "Sex", "Select Sex", Array As String("M", "F", "X"), 18, Colors.Black, "", Me, 1)
	
	VM.AddLabel(VM.Padding, -2, 1, 1, "Height (Ft):", 18, Colors.Black, "HeightFt")
	VM.AddTextBox(140dip, -1, -1, 1, "HeightFt", "", 18, "", False, VM.DataType_Num, "", VM.ActionBtn_Next, "", Me, 0, 2, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Height (In):", 18, Colors.Black, "HeightIn")
	VM.AddTextBox(140dip, -1, -1, 1, "HeightIn", "", 18, "", False, VM.DataType_Num, "", VM.ActionBtn_Next, "", Me, 0, 2, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Weight:", 18, Colors.Black, "Weight")
	VM.AddTextBox(140dip, -1, -1, 1, "Weight", "", 18, "", False, VM.DataType_Num, "", VM.ActionBtn_Next, "", Me, 0, 4, Null)
	
	VM.AddLabel(VM.Padding, -2, 1, 1, "Street:", 18, Colors.Black, "Street")
	VM.AddTextBox(77dip, -1, -1, 1, "Street", "", 18, "Street", False, VM.DataType_Uppercase_Words, "", VM.ActionBtn_Next, "", Me, 0, 50, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Apt:", 18, Colors.Black, "Apt")
	VM.AddTextBox(77dip, -1, -50, 1, "Apt", "", 18, "Apt", False, VM.DataType_Uppercase_Words, "", VM.ActionBtn_Next, "", Me, 0, 15, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "City:", 18, Colors.Black, "City")
	VM.AddTextBox(77dip, -1, -1, 1, "City", "", 18, "City", False, VM.DataType_Uppercase_Words, "", VM.ActionBtn_Next, "", Me, 0, 30, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "State:", 18, Colors.Black, "State")
	VM.AddComboBox(77dip, -1, -33, 1, "State", "Select a State", File.ReadList(File.DirAssets, "states.txt"), 18, Colors.Black, "", Me, 2)
	VM.AddLabel(-2, -1, 1, 1, "Zip:", 18, Colors.Black, "Zip")
	VM.AddTextBox(-2, -1, -1, 1, "Zip", "", 18, "Zip", False, VM.DataType_Num, "", VM.ActionBtn_Next, "", Me, 0, 5, Null)
	VM.AddLabel(VM.Padding, -2, 1, 1, "Phone:", 18, Colors.Black, "Phone")
	VM.AddTextBox(77dip, -1, -1, 1, "Phone", "", 18, "Phone", False, VM.DataType_Num, "", VM.ActionBtn_Next, "", Me, 0, 30, Null)
	
	VM.AddLabel(VM.Padding, -2, 1, 1, "Notes:", 18, Colors.Black, "Notes")
	VM.AddTextBox(77dip, -1, -1, 25%y, "Notes", "", 18, "Notes", True, VM.DataType_Text, "", VM.ActionBtn_Done, "", Me, 0, 500, Null)
End Sub

Sub Activity_Resume

	pgFrame.Height = Activity.Height
	If curValues.IsInitialized Then VM.RestoreState(curValues)
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		curValues.Clear
	Else
		curValues = VM.SaveState
	End If
End Sub

Sub IME_HeightChanged (NewHeight As Int, OldHeight As Int)
	pgFrame.Height = NewHeight
End Sub

Sub Get_Date
	If DOB = 9223372036854775807 Then dd.DateTicks= DateTime.Now Else dd.DateTicks = DOB
	If dd.Show("Please Select Incident Date", "Incident Date", "Ok", "Cancel", "", Null) = DialogResponse.POSITIVE Then
		DOB = DateTime.DateParse(NumberFormat(dd.Month, 2, 0) & "/" & NumberFormat(dd.DayOfMonth, 2, 0) & "/" & NumberFormat2(dd.Year, 4, 0, 0, False))
		VM.SetText("DOB", DateTime.Date(DOB))
	Else
		If Msgbox2("Do you wish to set the Date to N/A?", "Clear Date", "Yes", "", "No", Null) = DialogResponse.POSITIVE Then
			DOB= 9223372036854775807
			VM.SetText("DOB", "N/A")
		End If
	End If
End Sub
