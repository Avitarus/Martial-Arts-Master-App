Type=Activity
Version=2.00
FullScreen=False
IncludeTitle=True
@EndOfDesignText@
''Activity module
'Sub Process_Globals
'	Dim manager As PreferenceManager
'	Dim screen As PreferenceScreen
'End Sub
'
'Sub Globals
'
'End Sub
'
'Sub Activity_Create(FirstTime As Boolean)
'	If FirstTime Then
'		CreatePreferenceScreen
'		If manager.GetAll.Size = 0 Then SetDefaults
'	End If
'	Activity.Color = Colors.Red
'	StartActivity(screen.CreateIntent)
'	Dim btn As Button
'	btn.Initialize("btn")
'	btn.Text = "Settings"
'	Activity.AddView(btn, 10dip, 10dip, 200dip, 100dip)
'End Sub
'
'Sub SetDefaults
'	'defaults are only set on the first run.
'	manager.SetBoolean("check1", True)
'	manager.SetBoolean("check2", False)
'	manager.SetString("edit1", "Hello!")
'	manager.SetString("list1", "Black")
'	
'End Sub
'
'Sub btn_Click
'	StartActivity(screen.CreateIntent)
'End Sub
'
'
'Sub CreatePreferenceScreen
'	screen.Initialize("Einstellungen", "")
'	'create two categories
'	Dim k1,k2,k3,k4,k5,k6,k7,k8,k9 As PreferenceCategory
'	
'	
'	
'	
'	k1.Initialize("                Person")
'		k1.AddCheckBox("check1", "Checkbox1", "This is Checkbox1", True)
'		k1.AddCheckBox("check2", "Checkbox2", "This is Checkbox2", False)
'		k1.AddEditText("edit1", "EditText1", "This is EditText1", "")
'	
'
'	
'	k2.Initialize("---------------Stunde--------------")
'		k2.AddList("list1", "List1", "This is List1", "", Array As String("Black", "Red", "Green", "Blue"))
'		
'		
'	k3.Initialize("Tag")
'		k3.AddCheckBox("check4", "Chewew", "This is Checkbox1", True)
'		k3.AddCheckBox("check5", "Checgsrvwsv", "This is Checkbox2", True)
'		k3.AddEditText("edit1", "Editsgsg", "This is Edisfhsh1", "")
'		k3.AddList("list4", "List4", "This i4List1", "", Array As String("Black1", "Red1", "Green1", "Blue1"))	
'		
'	k4.Initialize("Woche")
'		k4.AddCheckBox("check4", "Chewew", "This is Checkbox1", True)
'		k4.AddCheckBox("check5", "Checgsrvwsv", "This is Checkbox2", True)
'		k4.AddEditText("edit1", "Editsgsg", "This is Edisfhsh1", "")
'		k4.AddList("list4", "List4", "This i4List1", "", Array As String("Black1", "Red1", "Green1", "Blue1"))	
'		
'	k5.Initialize("Monat")
'		k5.AddCheckBox("check4", "Chewew", "This is Checkbox1", True)
'		k5.AddCheckBox("check5", "Checgsrvwsv", "This is Checkbox2", True)
'		k5.AddEditText("edit1", "Editsgsg", "This is Edisfhsh1", "")
'		k5.AddList("list4", "List4", "This i4List1", "", Array As String("Black1", "Red1", "Green1", "Blue1"))	
'		
'	k6.Initialize("Quartal")
'		k6.AddCheckBox("check4", "Chewew", "This is Checkbox1", True)
'		k6.AddCheckBox("check5", "Checgsrvwsv", "This is Checkbox2", True)
'		k6.AddEditText("edit1", "Editsgsg", "This is Edisfhsh1", "")
'		k6.AddList("list4", "List4", "This i4List1", "", Array As String("Black1", "Red1", "Green1", "Blue1"))	
'			
'	k7.Initialize("Jahr")
'		k7.AddCheckBox("check4", "Chewew", "This is Checkbox1", True)
'		k7.AddCheckBox("check5", "Checgsrvwsv", "This is Checkbox2", True)
'		k7.AddEditText("edit1", "Editsgsg", "This is Edisfhsh1", "")
'		k7.AddList("list4", "List4", "This i4List1", "", Array As String("Black1", "Red1", "Green1", "Blue1"))		
'			
'	k8.Initialize("4 Jahre")
'		k8.AddCheckBox("check4", "Chewew", "This is Checkbox1", True)
'		k8.AddCheckBox("check5", "Checgsrvwsv", "This is Checkbox2", True)
'		k8.AddEditText("edit1", "Editsgsg", "This is Edisfhsh1", "")
'		k8.AddList("list4", "List4", "This i4List1", "",Array As String("Black1", "Red1", "Green1", "Blue1"))		
'			
'	k9.Initialize("Trainingsplan")
''		k9.AddCheckBox("check1", "Checkbox1", "This is Checkbox1", True)
''		k9.AddCheckBox("check2", "Checkbox2", "This is Checkbox2", False)
'		k9.AddEditText("TrainingsplanName", "Bezeichnung des Planes", "Nutzen Sie einen bezeichnenden Namen", "")	
'		k2.AddList("TrainingsplanAuswahl", "Training für", "", "Einzelperson", Array As String("Einzelperson", "Gruppe (gemischt)", "Gruppe (Gleich)"))
'		
'		
'		
'		
'		
'		
'		
''	add the categories To the Main screen
'	screen.AddPreferenceCategory(k1)
'	screen.AddPreferenceCategory(k2)
'	screen.AddPreferenceCategory(k3)
'	screen.AddPreferenceCategory(k4)
'	screen.AddPreferenceCategory(k5)
'	screen.AddPreferenceCategory(k6)
'	screen.AddPreferenceCategory(k7)
'	screen.AddPreferenceCategory(k8)
'	screen.AddPreferenceCategory(k9)
'	
'	
'End Sub
'
'Sub Activity_Resume
'	HandleSettings
'End Sub
'
'Sub HandleSettings
'	Select manager.GetString("list1")
'		Case "Black"
'			Activity.Color = Colors.Black
'		Case "Red"
'			Activity.Color = Colors.Red
'		Case "Green"
'			Activity.Color = Colors.Green
'		Case "Blue"
'			Activity.Color = Colors.Blue
'		Case Else
'			
'	End Select
'End Sub
'
'Sub Activity_Pause (UserClosed As Boolean)
'
'End Sub
'
'
'