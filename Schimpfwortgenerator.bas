Type=Activity
Version=2.30
FullScreen=False
IncludeTitle=True
@EndOfDesignText@





'Activity module
Sub Process_Globals

	Dim SQL1 As SQL
	Dim Cursor1 As Cursor
	Dim SpracheID As String
	Dim Text0(), Text1(), Text2(), Text3(), Text4(), Text5(), Text6(), Text7() As String
	Dim Text8(), Text9(), Text10(), Text11(), Text12() As String
	Dim Text13(), Text14(), Text15(), Text16(), Text17(),Text18()As String', Text19() As String
	Dim dbName As String					: dbName = "MAMA.sqlite"
	Dim dbOrdner As String					: dbOrdner = "Lebensmittel"
	
End Sub

Sub Globals

	Dim Label1, Label2, Label3, Label4, Label5, Label6, Label7 As Label
	'Dim Label8, Label9, Label10, Label11, Label12 As Label
	Dim Fehlerliste As List
	Dim aa As Int
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	ProgressDialogShow("DATA LOADING ...")
	Activity.LoadLayout("Schimpfwortgenerator")
	Fehlerliste.Initialize
	
	If FirstTime = True Then
		

			SQL1.Initialize(File.DirRootExternal,dbName,True)
			SQL1.BeginTransaction
			
			    Cursor1 = SQL1.ExecQuery("SELECT * FROM "& dbOrdner)
				
				Dim a = Cursor1.RowCount As Int
			
				Dim Text1(a) As String
				Dim Text2(a) As String
				Dim Text3(a) As String
				Dim Text4(a) As String
				Dim Text5(a) As String
				Dim Text6(a) As String
				Dim Text7(a) As String
				Dim Text8(a) As String
				Dim Text9(a) As String
				Dim Text10(a) As String
				Dim Text11(a) As String
				Dim Text12(a) As String
				Dim Text13(a) As String
				Dim Text14(a) As String
				Dim Text15(a) As String
				Dim Text16(a) As String
				Dim Text17(a) As String
				Dim Text18(a) As String
				'Dim Text19(a) As String
				
				
			    For i = 0 To Cursor1.RowCount - 1
				
			        Cursor1.Position = i
					Text1(i) = Cursor1.GetString("male0")
					Text7(i) = Cursor1.GetString("male1")
					Text8(i) = Cursor1.GetString("male2")
					Text9(i) = Cursor1.GetString("male3")
					Text10(i) = Cursor1.GetString("male4")
					Text11(i) = Cursor1.GetString("male5")
					Text12(i) = Cursor1.GetString("male6")
					'Text13(i) = Cursor1.GetString("male7")
					'Text14(i) = Cursor1.GetString("male8")
					
					Text2(i) = Cursor1.GetString("femaledictum0")
					Text15(i) = Cursor1.GetString("femaledictum1")
					Text16(i) = Cursor1.GetString("femaledictum2")
					Text17(i) = Cursor1.GetString("femaledictum3")
					Text18(i) = Cursor1.GetString("femaledictum4")
					'Text19(i) = Cursor1.GetString("femaledictum5")
					
					Text3(i) = Cursor1.GetString("AdjektiveM")
					Text4(i) = Cursor1.GetString("AdjektiveF")
					Text5(i) = Cursor1.GetString("Spruch")
					Text6(i) = Cursor1.GetString("Konterspruch")
					
			    Next
			    Cursor1.Close
				SQL1.TransactionSuccessful
			SQL1.EndTransaction
		ProgressDialogHide
	End If


	Anzeige


End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)
	StateManager.SaveState(Activity, "Schimpfwortgenerator")

End Sub

Sub Anzeige


	Dim Male(9), Female(5) As String
	Dim a,b,c,d,e,f,g,h,i As Int
	Dim Rnd1,rnd2,rnd3,rnd4,rnd5,rnd6,rnd7,rnd8,rnd9,rnd10 As Int
	
	a = Rnd(1,999)
	b = Rnd(1,999)
	c = Rnd(1,999)
	d = Rnd(1,999)
	e = Rnd(1,999)
	f = Rnd(1,999)
	g = Rnd(1,705)
	'h = Rnd(1,999)
	'i = Rnd(1,999)
	Male = Array As String(Text1(a), Text7(b),Text8(c),Text9(d),Text10(e),Text11(f),Text12(g))',Text13(h),Text14(i))

	a = Rnd(1,999)
	b = Rnd(1,999)
	c = Rnd(1,999)
	d = Rnd(1,999)
	e = Rnd(1,804)
	'f = Rnd(1,999)
	Female = Array As String(Text2(a),Text15(b),Text16(c),Text17(d),Text18(e))',Text19(f))


	' MAnn adjektiv Male
	
	
	
	Do While Rnd1 = rnd2
		Rnd1 = Rnd(0,6)
		rnd2 = Rnd(0,6)
	Loop
	
	Do While rnd3 = rnd4
		rnd3 = Rnd(0,198)
		rnd4 = Rnd(0,198)
	Loop
	
	Label1.Text = " " & Text3(rnd3) & Male(Rnd1)
	Label2.Text = " " & Text3(rnd4) & Male(rnd2)
	
	'Frau adjektiv Female
	Do While rnd5 = rnd6
		rnd5 = Rnd(0,4)
		rnd6 = Rnd(0,4)
	Loop
	
	Do While rnd7 = rnd8
		rnd7 = Rnd(0,198)
		rnd8 = Rnd(0,198)
	Loop	
	
	Label3.Text = " " & Text4(rnd7)& Female(rnd5)
	Label4.Text = " " & Text4(rnd8)& Female(rnd6)	
	
	'Spruch Konter konter
	Do While rnd9 = rnd10
		rnd10 = Rnd(0,450)
		rnd9 = Rnd(0,450)
	Loop	
	
	Label5.Text = Text5(Rnd(1,378))
	Label6.Text = Text6(rnd9)
	Label7.Text = Text6(rnd10)

	
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean                            
    Dim Antw As Int
	
	If KeyCode = KeyCodes.KEYCODE_BACK Then ' Hardware-Zurück Taste gedrückt
        
		If Label1.Visible = True  Then
			Antw = Msgbox2("Probleme und Anregungen bitte an: www.watchkido.de" & CRLF & CRLF & "Wollen Sie das Programm verlassen und ihre Einstellungen abspeichern?", "A C H T U N G", "Ja", "", "Nein",Null)
			If Antw = DialogResponse.POSITIVE Then
				
				If Fehlerliste.Size < 5 Then 
					Fehlerliste.Clear
				Else
					Fehlerliste.Sort(True)
					Msgbox(Fehlerliste,"FehlerlisteX.txt")
					File.WriteList(File.DirRootExternal,"Fehlerliste"&(Rnd(1,10000))&".txt",Fehlerliste)
					Fehlerliste.Clear
				End If
				Return False
			Else
				Return True
			End If
		Else
    		Return False
    	End If 
	End If 
	
	
	
End Sub


Sub Label1_LongClick
	
	Fehlerliste.Add(Label1.text)
End Sub
Sub Label2_LongClick
	Fehlerliste.Add(Label2.text)
End Sub
Sub Label3_LongClick
	Fehlerliste.Add(Label3.text)
End Sub
Sub Label4_LongClick
	Fehlerliste.Add(Label4.text)
End Sub
Sub Label5_LongClick
	Fehlerliste.Add(Label4.text)
End Sub
Sub Label6_LongClick
	Fehlerliste.Add(Label6.text)
End Sub
Sub Label7_LongClick
	Fehlerliste.Add(Label7.text)
End Sub
Sub Label1_Click
	Anzeige
End Sub
Sub Label2_Click
	Anzeige
End Sub
Sub Label3_Click
	Anzeige
End Sub
Sub Label4_Click
	Anzeige
End Sub
Sub Label5_Click
	Anzeige
End Sub
Sub Label6_Click
	Anzeige
End Sub
Sub Label7_Click
	Anzeige
End Sub