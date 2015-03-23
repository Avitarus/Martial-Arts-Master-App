Type=Class
Version=3
@EndOfDesignText@
'Class module
Private Sub Class_Globals
    Type MyTextbox(DataType As Int, ActionBtn As Int, ActionSub As String, ActionSubModule As Object, MinChar As Int, MaxChar As Int)
    Type MyActionSub(ActionSub As String, ActionSubModule As Object)
    Type MyCombobox(CurSelection As Int, Items As List, Prompt As String, ActionSub As String, ActionSubModule As Object)
    Type ComboItem(Text As String, Value As Object)
    Private IME As IME ' Put here instead of below so class is considered an Activity
    Private MyViews As List
    Private NameMap As Map
    Private CurFocus As View
    Public Padding As Int
    Public MinMaxWarn As Boolean
    Public ScaleViews As Boolean
    Public OverrideFontScale As Boolean
    Private FontScale As Float
    Private ScaleLabel As Label
    Private Parent As Panel
    Public ParentWidth As Int
    Private CurX As Int
    Private NextX As Int
    Private CurY As Int
    Private NextY As Int
End Sub

' Initialize the Class
' Container: Panel to place all controls in
' ShowMinMaxWarn: Set if Toast Messages are to show when Min and Max Char requirements aren't met
' ViewScaling: Sets if Views are to be scaled to fit the text they contain
' OverrideFontScaling: Set if You wish to override the user's font size scale
Public Sub Initialize(Container As Panel, ContainerWidth As Int, ShowMinMaxWarn As Boolean, ViewScaling As Boolean, OverrideFontScaling As Boolean)
Dim Ref As Reflector

    MyViews.Initialize
    NameMap.Initialize
    IME.Initialize("IME")
    ScaleLabel.Initialize("")
    ScaleLabel.Visible = False
    Container.AddView(ScaleLabel, 0, 0, 1, 1)
    Padding = 5dip
    MinMaxWarn = ShowMinMaxWarn
    ScaleViews = ViewScaling
    OverrideFontScale = OverrideFontScaling
    Parent = Container
    ParentWidth = ContainerWidth
    CurX = 5dip
    NextX = 5dip
    CurY = 5dip
    NextY = 5dip
    
    Ref.Target = Ref.GetContext
    Ref.Target = Ref.RunMethod("getResources")
    Ref.Target=Ref.RunMethod("getConfiguration")
    FontScale= Ref.GetField("fontScale")
End Sub

#Region Constants
' General Text Data [Type_Class_Text]
Sub DataType_Text As Int
    Return 1
End Sub

' Numbers Only [Type_Class_Number]
Sub DataType_Num As Int
    Return 2
End Sub

' Floating Point Numbers with sign and decimal [Type_Class_Number | Type_Number_Flag_Decimal | Type_Number_Flag_Signed]
Sub DataType_Float As Int
    Return 12290
End Sub

' DateTime Text [Type_Class_DateTime]
Sub DataType_DateTime As Int
    Return 4
End Sub

' Date Text [Type_Class_DateTime | Type_DateTime_Variation_Date]
Sub DataType_Date As Int
    Return 20
End Sub

' Time Text [Type_Class_DateTime | Type_DateTime_Variation_Time]
Sub DataType_Time As Int
    Return 36
End Sub

' Password [Type_Class_Text | Type_Text_Variation_Password]
Sub DataType_Password As Int
    Return 129
End Sub

' Email Text [Type_Class_Text | Type_Text_Variation_Email_Address]
Sub DataType_Email As Int
    Return 33
End Sub

' URL Text [Type_Class_Text | Type_Text_Variation_URI]
Sub DataType_URL As Int
    Return 17
End Sub

' Phone Number Text [Type_Class_Phone]
Sub DataType_Phone As Int
    Return 3
End Sub

' Name Text [Type_Class_Text | Type_Text_Variation_Person_Name | Type_Text_Flag_Cap_Words]
Sub DataType_Name As Int
    Return 8289
End Sub

' Uppercase Every Char [Type_Class_Text | Type_Text_Cap_Characters]
' (Overrides all other Uppercase Flags)
Sub DataType_Uppercase_All As Int
    Return 4097
End Sub

' Uppercase First Letter of Each Sentence [Type_Class_Text | Type_Text_Cap_Sentences]
Sub DataType_Uppercase_Sentences As Int
    Return 16385
End Sub

' Uppercase First Letter of Each Word [Type_Class_Text | Type_Text_Cap_Words]
' (Overrides Sentences Uppercase Flag)
Sub DataType_Uppercase_Words As Int
    Return 8193
End Sub

' Text should have Auto Correction Applied [Type_Class_Text | Type_Text_Flag_Auto_Correct]
Sub DataType_Auto_Correct As Int
    Return 32769
End Sub

' No Suggestions/Auto Correct [Type_Class_Text | Type_Text_Flag_No_Suggestions]
Sub DataType_No_Suggestions As Int
    Return 524289
End Sub

' Shows Next Button and will navigate to Next View
Sub ActionBtn_Next As Int
    Return 5
End Sub

' Shows Previous Button and will navigate to Previous View
Sub ActionBtn_Previous As Int
    Return 7
End Sub

' Shows Done Button and has option to execute ActionSub
Sub ActionBtn_Done As Int
    Return 6
End Sub

' Shows Go Button and has option to execute ActionSub
Sub ActionBtn_Go As Int
    Return 2
End Sub

' Shows Search Button and has option to execute ActionSub
Sub ActionBtn_Search As Int
    Return 3
End Sub

' Shows Send Button and has option to execute ActionSub
Sub ActionBtn_Send As Int
    Return 4
End Sub

' Upper and Lower Case Alpha Chars
' Returns: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
Sub CharFilter_Alpha As String
    Return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
End Sub

' Uppercase Alpha Chars
' Returns: "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
Sub CharFilter_Upper_Alpha As String
    Return "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
End Sub

' Number Chars
' Returns: "0123456789"
Sub CharFilter_Numeric As String
    Return "0123456789"
End Sub
#End Region

' Add a view of your own (Not Managed by the Class) to the Flow/Layout of views managed by the class
' Left/Top: Position to place view, -1 to Place in Current Flow Position, -2 to Place in Next Flow Position.
' Width/Height: Width/Height of View. -1 to Fit to Full Width/Height. -2 to -99 for percent of width
Sub Add2Flow(NewView As View, Left As Int, Top As Int, Width As Int, Height As Int) As Rect
Dim viewRect As Rect

    If Top = -2 Then
        Top = NextY
    Else If Top = -1 Then
        Top = CurY
        If Left > 0 AND Left < NextX Then Left = NextX
    End If
    If Left = -2 Then
        Left = NextX
    Else If Left = -1 Then
        Left = CurX
    End If
    
    If Width < 1 Then
        If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left
    End If
    If Height < 1 Then
        If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top
    End If
    
    Parent.AddView(NewView, Left, Top, Width, Height)
    
    CurX = Left
    NextX = Left + NewView.Width + Padding
    CurY = Top
    NextY = Max(NextY, Top + NewView.Height + Padding)
    If NextY > Parent.Height Then Parent.Height = NextY
    
    viewRect.Initialize(Left, Top, Left + NewView.Width, Top + NewView.Height)
    Return viewRect
End Sub

' Left/Top: Position to place view, -1 to Place in Current Flow Position, -2 to Place in Next Flow Position.
' Width/Height: Width/Height of View. If ScaleViews is True it will adjust to fit font Height (Set Width= 1 to Scale width). -1 to Fit to Full Width/Height. -2 to -99 for percent of width.
' Text: Text of Label.
' Color: Color of Label
' LinkedView: Name String for View that will get focus when Label is Clicked
Sub AddLabel(Left As Int, Top As Int, Width As Int, Height As Int, Text As String, TextSize As Float, Color As Int, LinkedView As String) As Rect
Dim myControl As Label
Dim viewRect As Rect
Dim ref As Reflector

    myControl.Initialize("Label")
    myControl.Gravity = Bit.OR(Gravity.Left, Gravity.Top)
    myControl.Text = Text
    myControl.TextColor = Color
    myControl.TextSize = TextSize
    myControl.Typeface = Typeface.DEFAULT_BOLD
    ref.Target = myControl
    ref.RunMethod2("setLines", 1, "java.lang.int")
    ref.RunMethod2("setHorizontallyScrolling", True, "java.lang.boolean")
    ref.RunMethod2("setEllipsize", "MARQUEE", "android.text.TextUtils$TruncateAt")
    ref.RunMethod2("setMarqueeRepeatLimit", -1, "java.lang.int")
    ref.RunMethod2("setSelected", True, "java.lang.boolean")
    
    myControl.Tag = LinkedView
    
    If Top = -2 Then
        Top = NextY
    Else If Top = -1 Then
        Top = CurY
        If Left > 0 AND Left < NextX Then Left = NextX
    End If
    If Left = -2 Then
        Left = NextX
    Else If Left = -1 Then
        Left = CurX
    End If
    
    If Width < 1 Then
        If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left
    End If
    If Height < 1 Then
        If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top
    End If
    
    If OverrideFontScale = True Then myControl.TextSize = myControl.TextSize / FontScale
    
    If ScaleViews = True Then ' Scale View to Size required for Font
        Dim charRect As Rect
        charRect = GetCharSize(myControl.Typeface, myControl.TextSize, Text)
        
        If Height < charRect.Bottom Then Height = charRect.Bottom
        If Text.Length > 0 AND Width = 1 Then ' Calculate Min Width
            Width = Min(charRect.Right, ParentWidth - Padding - Left)
        Else
            Width = Min(Width, ParentWidth - Padding - Left)
        End If
    End If
    
    Parent.AddView(myControl, Left, Top, Width, Height)
    
    CurX = Left
    NextX = Left + myControl.Width + Padding
    CurY = Top
    NextY = Max(NextY, Top + myControl.Height + Padding)
    If NextY > Parent.Height Then Parent.Height = NextY
    
    viewRect.Initialize(Left, Top, Left + myControl.Width, Top + myControl.Height)
    Return viewRect
End Sub

Private Sub Label_Click
Dim Name As String
Dim MyControl As Label
Dim LV As View
    
    MyControl = Sender
    Name = MyControl.Tag
    If Name.Length > 0 Then
        LV = NameMap.Get(Name)
        If LV.IsInitialized = True Then
            If CurFocus = LV Then
                If LV Is AutoCompleteEditText Then
                    Dim act As AutoCompleteEditText
                    act = LV
                    act.ShowDropDown
                End If
            Else
                LV.RequestFocus
                CurFocus = LV
            End If
        End If
    End If
End Sub

' Left/Top: Position to place view, -1 to Place in Current Flow Position, -2 to Place in Next Flow Position.
' Width/Height: Width/Height of View. If ScaleViews is True it will adjust to fit font Height (Set Width= 1 to Scale width). -1 to Fit to Full Width/Height. -2 to -99 for percent of width.
' Text: Text of Label.
' Color: Color of Label
' ActionSub: Name of Sub to call when Button Clicked
' ActionSubModule: Activity/Module that Sub is in.  (Usually [Me] can be used if Sub is in Activity Module the View resides in).
Sub AddButton(Left As Int, Top As Int, Width As Int, Height As Int, Name As String, Text As String, TextSize As Float, Color As Int, ActionSub As String, ActionSubModule As Object) As Rect
Dim myControl As Label
Dim viewRect As Rect
Dim ref As Reflector
Dim newActionSub As MyActionSub

    myControl.Initialize("Button")
    myControl.Gravity = Gravity.CENTER
    myControl.Text = Text
    myControl.TextColor = Color
    myControl.TextSize = TextSize
    myControl.Typeface = Typeface.DEFAULT_BOLD
    newActionSub.Initialize
    newActionSub.ActionSub = ActionSub
    newActionSub.ActionSubModule = ActionSubModule
    myControl.Tag = newActionSub
    
    If Top = -2 Then
        Top = NextY
    Else If Top = -1 Then
        Top = CurY
        If Left > 0 AND Left < NextX Then Left = NextX
    End If
    If Left = -2 Then
        Left = NextX
    Else If Left = -1 Then
        Left = CurX
    End If
    
    If Width < 1 Then
        If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left
    End If
    If Height < 1 Then
        If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top
    End If
    
    If OverrideFontScale = True Then myControl.TextSize = myControl.TextSize / FontScale
    
    If ScaleViews = True Then ' Scale View to Size required for Font
        Dim charRect As Rect
        charRect = GetCharSize(myControl.Typeface, myControl.TextSize, Text)
        
        If Height < charRect.Bottom + 15dip Then Height = charRect.Bottom + 15dip
        If Text.Length > 0 AND Width = 1 Then ' Calculate Min Width
            Width = Min(charRect.Right + 15dip, ParentWidth - Padding - Left)
        Else
            Width = Min(Width, ParentWidth - Padding - Left)
        End If
    End If
    
    Parent.AddView(myControl, Left, Top, Width, Height)
    myControl.Background = SetNinePatchDrawable("buttonup")
    ref.Target = myControl
    ref.SetOnTouchListener("Button_Touch")
    
    CurX = Left
    NextX = Left + myControl.Width + Padding
    CurY = Top
    NextY = Max(NextY, Top + myControl.Height + Padding)
    If NextY > Parent.Height Then Parent.Height = NextY
    If Name.Length > 0 Then NameMap.Put(Name, myControl)
    
    viewRect.Initialize(Left, Top, Left + myControl.Width, Top + myControl.Height)
    Return viewRect
End Sub

Private Sub Button_Click
Dim myControl As Label
Dim tagActionSub As MyActionSub

    myControl = Sender
    tagActionSub = myControl.Tag
    If tagActionSub.IsInitialized Then
        If tagActionSub.ActionSub.Length > 0 Then CallSubDelayed(tagActionSub.ActionSubModule, tagActionSub.ActionSub)
    End If
End Sub

Private Sub Button_Touch(viewtag As Object, action As Int, x As Float, y As Float, moveEvent As Object) As Boolean
Dim myControl As Label

    myControl = Sender
    If action = 0 Then myControl.Background = SetNinePatchDrawable("buttondown")
    If action = 1 OR x < 0 OR y < 0 OR x > myControl.Width OR y > myControl.Height Then myControl.Background = SetNinePatchDrawable("buttonup")
End Sub

' Left/Top: Position to place view, -1 to Place in Current Flow Position, -2 to Place in Next Flow Position.
' Width/Height: Width/Height of View. If ScaleViews is True it will adjust to fit font Height (Set Width= 1 to Scale width to ViewChar). -1 to Fit to Full Width/Height. -2 to -99 for percent of width.
' Name: Name given to the control for reference or use in a Select Case Block.
' ActionSub: Name of Sub to call when Item Selected- Sub YourSub(Position As Int, Value As Object) as Boolean. Return True to prevent change.
' ActionSubModule: Activity/Module that Sub is in.  (Usually [Me] can be used if Sub is in Activity Module the View resides in).
' ViewChar: Set to desired number of chars visible.
Public Sub AddComboBox(Left As Int, Top As Int, Width As Int, Height As Int, Name As String, Prompt As String, Items As List, TextSize As Float, Color As Int, SelectionChangeSub As String, SelectionChangeSubModule As Object, ViewChar As Int) As Rect
Dim myControl As Label
Dim ref As Reflector
Dim viewRect As Rect
Dim newCombobox As MyCombobox

    newCombobox.Initialize
    myControl.Initialize("Combobox")
    newCombobox.CurSelection = -1
    newCombobox.Prompt = Prompt
    newCombobox.ActionSub = SelectionChangeSub
    newCombobox.ActionSubModule = SelectionChangeSubModule
    newCombobox.Items.Initialize
    If Items.IsInitialized = True AND Items.Size > 0 Then newCombobox.Items.AddAll(Items)
    newCombobox.CurSelection = -1
    myControl.Text = Prompt
    myControl.Tag = newCombobox
    myControl.TextColor = Color
    myControl.TextSize = TextSize
    myControl.Gravity = Bit.OR(Gravity.Left, Gravity.CENTER_VERTICAL)
    ref.Target = myControl
    ref.RunMethod2("setLines", 1, "java.lang.int")
    ref.RunMethod2("setHorizontallyScrolling", True, "java.lang.boolean")
    ref.RunMethod2("setEllipsize", "MARQUEE", "android.text.TextUtils$TruncateAt")
    ref.RunMethod2("setMarqueeRepeatLimit", -1, "java.lang.int")
    ref.RunMethod2("setSelected", True, "java.lang.boolean")
    
    If Top = -2 Then
        Top = NextY
    Else If Top = -1 Then
        Top = CurY
        If Left > 0 AND Left < NextX Then Left = NextX
    End If
    If Left = -2 Then
        Left = NextX
    Else If Left = -1 Then
        Left = CurX
    End If
    
    If Width < 1 Then
        If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left
    End If
    If Height < 1 Then
        If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top
    End If
    
    If OverrideFontScale = True Then myControl.TextSize = myControl.TextSize / FontScale
    
    If ScaleViews = True Then ' Scale View to Size required for Font
        Dim charRect As Rect
        charRect = GetCharSize(Typeface.DEFAULT, myControl.TextSize, "")
        If Height < charRect.Bottom + 11dip Then Height = charRect.Bottom + 11dip
        If ViewChar > 0 AND Width < 2 Then ' Calculate Min Width
            Width = Min(charRect.Right * ViewChar + 50dip, ParentWidth - Padding - Left)
        Else
            Width = Min(Width, ParentWidth - Padding - Left)
        End If
    End If
    
    Parent.AddView(myControl, Left, Top, Width, Height)
    myControl.Background = SetNinePatchDrawable("comboboxenabled")
    ref.Target = myControl
    ref.RunMethod4("setPadding", Array As Object(6dip, 6dip, 44dip, 5dip), Array As String("java.lang.int", "java.lang.int", "java.lang.int", "java.lang.int"))
    
    CurX = Left
    NextX = Left + myControl.Width + Padding
    CurY = Top
    NextY = Max(NextY, Top + myControl.Height + Padding)
    If NextY > Parent.Height Then Parent.Height = NextY
    
    If Name.Length > 0 Then NameMap.Put(Name, myControl)
    
    viewRect.Initialize(Left, Top, Left + myControl.Width, Top + myControl.Height)
    Return viewRect
End Sub

Private Sub Combobox_Click
Dim MyLabel As Label
Dim MCB As MyCombobox
Dim MyComboItem As ComboItem
Dim items As List
Dim selection As Int

    MyLabel = Sender
    MCB = MyLabel.Tag
    If MCB.items.Size > 0 Then
        items.Initialize
        For i = 0 To MCB.items.Size - 1
            If MCB.items.Get(i) Is ComboItem Then
                MyComboItem = MCB.items.Get(i)
                items.Add(MyComboItem.Text)
            Else
                items.Add(MCB.items.Get(i))
            End If
        Next
        selection= InputList(items, MCB.Prompt, MCB.CurSelection)
        If selection < 0 Then Return
        MCB.CurSelection= selection
        If MCB.items.Get(selection) Is ComboItem Then
            MyComboItem = MCB.items.Get(selection)
            MyLabel.Text= MyComboItem.Text
            If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, selection, MyComboItem.Value)
        Else
            MyLabel.Text= MCB.items.Get(selection)
            If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, selection, MCB.items.Get(selection))
        End If
    Else
        ToastMessageShow("No Items to Select", True)
    End If
End Sub

' Create a Special Text/Value Combo to be used in a Combobox List
Public Sub MakeComboItem(Text As String, Value As Object) As ComboItem
Dim MyComboItem As ComboItem

    MyComboItem.Initialize
    MyComboItem.Text = Text
    MyComboItem.Value = Value
    
    Return MyComboItem
End Sub

' Left/Top: Position to place view, -1 to Place in Current Flow Position, -2 to Place in Next Flow Position.
' Width/Height: Width/Height of View. If ScaleViews is True it will adjust to fit font (1 for Min and 2 for Max Char Width). -1 to Fit to Full Width/Height. -2 to -99 for percent of width.
' Name: Name given to the control for reference or use in a Select Case Block.
' DataType: Use one of the Constants within this Class to Specify the Data Type for the View (Can combine with OR).
' CharFilter: Use one of the Constants within this Class and/or your own string to Specify the Text Filter for the View's Allowed Chars (Join Strings with &amp;).
' ActionBtn: Use one of the Constants within this Class to Specify the Action Button for the View's Keyboard.
' ActionSub: Name of Sub to call for Action Buttons other than Next and Previous.
' ActionSubModule: Activity/Module that Sub is in.  (Usually [Me] can be used if Sub is in Activity Module the View resides in).
' Min/Max Char: Set to desired value or 0 for no restriction.
Public Sub AddTextBox(Left As Int, Top As Int, Width As Int, Height As Int, Name As String, Text As String, TextSize As Float, Hint As String, MultiLine As Boolean, DataType As Int, CharFilter As String, ActionBtn As Int, ActionSub As String, ActionSubModule As Object, MinChar As Int, MaxChar As Int, AutoCompleteItems As List) As Rect
Dim myControl As AutoCompleteEditText
Dim ref As Reflector
Dim viewRect As Rect
Dim newTextBox As MyTextbox

    newTextBox.Initialize
    myControl.Initialize("Edit")
    newTextBox.DataType= DataType
    newTextBox.ActionBtn= ActionBtn
    newTextBox.ActionSub= ActionSub
    newTextBox.ActionSubModule= ActionSubModule
    newTextBox.MinChar= MinChar
    newTextBox.MaxChar= MaxChar
    myControl.Tag= newTextBox
    myControl.Text= Text
    myControl.Hint= Hint
    myControl.Typeface= Typeface.DEFAULT
    myControl.TextSize= TextSize
    
    If Top = -2 Then
        Top = NextY
    Else If Top = -1 Then
        Top = CurY
        If Left > 0 AND Left < NextX Then Left = NextX
    End If
    If Left = -2 Then
        Left = NextX
    Else If Left = -1 Then
        Left = CurX
    End If
    
    If Width < 1 Then
        If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left
    End If
    If Height < 1 Then
        If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top
    End If
    
    ref.Target = myControl
    If MultiLine Then
        myControl.SingleLine = False
        myControl.Wrap= True
        myControl.Gravity= Bit.OR(Gravity.Left, Gravity.Top)
        newTextBox.DataType = Bit.OR(newTextBox.DataType, 131072) ' Multiline Text Flag
        ref.RunMethod2("setImeOptions", Bit.OR(1073741824, newTextBox.ActionBtn), "java.lang.int") 'flagNoEnterAction= 1073741824
        ref.SetOnTouchListener("Edit_Touched")
    Else
        myControl.SingleLine = True
        myControl.Wrap= False
        ref.RunMethod2("setImeOptions", Bit.OR(268435456, newTextBox.ActionBtn), "java.lang.int") 'flagNoExtractUi= 268435456
    End If
    myControl.InputType = newTextBox.DataType
    
    If OverrideFontScale = True Then myControl.TextSize = myControl.TextSize / FontScale
    
    If ScaleViews = True Then ' Scale View to Size required for Font
        Dim charRect As Rect
        charRect = GetCharSize(myControl.Typeface, myControl.TextSize, "")
        If Height < charRect.Bottom + 7dip Then Height = charRect.Bottom + 7dip
        If Width < 3 Then ' Calculate Min Width
            If Width = 1 Then
                Width = Min(charRect.Right * MinChar + 7dip, ParentWidth - Padding - Left)
            Else ' Width = 2
                Width = Min(charRect.Right * MaxChar + 7dip, ParentWidth - Padding - Left)
            End If
        Else
            Width = Min(Width, ParentWidth - Padding - Left)
        End If
    End If
    
    Parent.AddView(myControl, Left, Top, Width, Height)
    myControl.Background = SetNinePatchDrawable("editenabled")
    
    CurX = Left
    NextX = Left + myControl.Width + Padding
    CurY = Top
    NextY = Max(NextY, Top + myControl.Height + Padding)
    If NextY > Parent.Height Then Parent.Height = NextY
    
    If CharFilter.Length = 0 Then
        If Bit.AND(DataType, DataType_Phone) = DataType_Phone Then
            CharFilter = CharFilter_Numeric & "*#+-P()N,/ "
        Else If Bit.AND(DataType, DataType_URL) = DataType_URL Then
            CharFilter = CharFilter_Alpha & CharFilter_Numeric & "$.,'-+!*_~:%()"
        Else If Bit.AND(DataType, DataType_Email) = DataType_Email Then
            CharFilter = CharFilter_Alpha & CharFilter_Numeric & "!#$%&'*+-/=?^_`{|}~."
        Else If Bit.AND(DataType, DataType_Float) = DataType_Float Then
            CharFilter = CharFilter_Numeric & ".-"
        Else If Bit.AND(DataType, DataType_Num) = DataType_Num Then
            CharFilter = CharFilter_Numeric
        End If
    End If
    If CharFilter.Length > 0 Then IME.SetCustomFilter(myControl, myControl.InputType, CharFilter)
    
    IME.AddHandleActionEvent(myControl)
    
    If AutoCompleteItems.IsInitialized Then myControl.SetItems(AutoCompleteItems)
    MyViews.Add(myControl)
    If Name.Length > 0 Then NameMap.Put(Name, myControl)
    
    viewRect.Initialize(Left, Top, Left + myControl.Width, Top + myControl.Height)
    Return viewRect
End Sub

Private Sub IME_HandleAction As Boolean
Dim ET As View
Dim MTB As MyTextbox
Dim index As Int

    ET = Sender
    MTB = ET.Tag
    index = MyViews.IndexOf(ET)
    If index > -1 Then
        Select Case MTB.ActionBtn
            Case ActionBtn_Next
                index= index + 1
                Do While index < MyViews.Size
                    ET= MyViews.Get(index)
                    If ET.Enabled = True Then
                        ET.RequestFocus
                        Exit
                    End If
                    index= index + 1
                Loop
            Case ActionBtn_Previous
                index= index - 1
                Do While index > -1
                    ET= MyViews.Get(index)
                    If ET.Enabled = True Then
                        ET.RequestFocus
                        Exit
                    End If
                    index= index - 1
                Loop
            Case Else ' Done, Go, Search, Send
                If MTB.ActionSub.Length > 0 Then CallSubDelayed(MTB.ActionSubModule, MTB.ActionSub)
                Return False
        End Select
        Return True
    End If
End Sub

' Prevent Scrollview Scrolling for Multiline Textboxes
Private Sub Edit_Touched(viewTag As Object, action As Int, x As Float, y As Float, motionEvent As Object) As Boolean
    If action = 2 Then '= MOVE
        Dim ref As Reflector
        ref.Target = Parent
        ref.RunMethod2("requestDisallowInterceptTouchEvent", True, "java.lang.boolean")
    End If
End Sub

Private Sub Edit_FocusChanged (HasFocus As Boolean)
    If HasFocus = True Then
        'IME.ShowKeyboard(Sender)
    Else
        If MinMaxWarn = True Then
            Dim ET As EditText
            Dim MTB As MyTextbox
            Dim index As Int
            
            ET = Sender
            index = MyViews.IndexOf(ET)
            If index > -1 Then
                MTB = ET.Tag
                If ET.Text.Length < MTB.MinChar Then ToastMessageShow("Value Needs to be at least " & MTB.MinChar & " Characters Long", True)
            End If
        End If
    End If
End Sub

Private Sub Edit_TextChanged (Old As String, New As String)
Dim cursorPOS As Int
Dim ET As EditText
Dim MTB As MyTextbox

    If New.CompareTo(Old) <> 0 Then
        ET = Sender
        MTB = ET.Tag
        cursorPOS = ET.SelectionStart
        If MTB.MaxChar > 0 Then
            If New.Length > MTB.MaxChar AND MinMaxWarn = True Then ToastMessageShow("Too many Characters input", True)
            ET.Text = New.SubString2(0, Min(MTB.MaxChar, New.Length))
        End If
        ET.SelectionStart = Min(cursorPOS, ET.Text.Length)
    End If
End Sub

' Set the Items in a Combobox
Public Sub SetItems(Name As String, Items As List)
Dim FoundName As View

    FoundName = NameMap.Get(Name)
    If FoundName.IsInitialized = True AND FoundName.Tag Is MyCombobox Then
        Dim MCB As MyCombobox
        Dim MyLabel As Label
        MyLabel= FoundName
        MCB = FoundName.Tag
        MCB.Items.Clear
        MCB.CurSelection= -1
        MyLabel.Text = MCB.Prompt
        If Items.IsInitialized AND Items.Size > 0 Then MCB.Items.AddAll(Items)
    End If
End Sub

' Get Current Index of Combobox or Text Selection of a Textbox
Public Sub GetCurSelection(Name As String) As Object
Dim FoundName As View

    FoundName = NameMap.Get(Name)
    If FoundName.IsInitialized = False Then Return ""
    If FoundName Is Label Then
        If FoundName.Tag Is MyCombobox Then
            Dim MCB As MyCombobox
            MCB = FoundName.Tag
            Return MCB.CurSelection
        Else If FoundName Is AutoCompleteEditText Then
            Dim SelStart, SelEnd As Int
            Dim ref As Reflector
            Dim MyEdit As AutoCompleteEditText
            MyEdit= FoundName
            SelStart = MyEdit.SelectionStart
            ref.Target= MyEdit
            SelEnd= ref.RunMethod("getSelectionEnd")
            If SelStart = -1 OR SelEnd = -1 Then Return "" Else Return MyEdit.Text.SubString2(Min(SelStart, SelEnd), Max(SelStart, SelEnd))
        End If
    End If
End Sub

' Set Current Selection in Combobox
Public Sub SetCurSelection(Name As String, Index As Int)
Dim FoundName As View

    FoundName = NameMap.Get(Name)
    If FoundName.IsInitialized = True Then
        If FoundName.Tag Is MyCombobox Then
            Dim MCB As MyCombobox
            Dim MyLabel As Label
            MyLabel= FoundName
            MCB= FoundName.Tag
            If Index > -1 AND Index < MCB.Items.Size Then
                MCB.CurSelection= Index
                If MCB.items.Get(Index) Is ComboItem Then
                    Dim MyComboItem As ComboItem
                    MyComboItem = MCB.items.Get(Index)
                    MyLabel.Text= MyComboItem.Text
                    If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, Index, MyComboItem.Value)
                Else
                    MyLabel.Text= MCB.items.Get(Index)
                    If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, Index, MCB.items.Get(Index))
                End If
            Else
                MCB.CurSelection= -1
                MyLabel.Text= MCB.Prompt
                If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, -1, "")
            End If
        End If
    End If
End Sub

' Get Value of Named Views
Public Sub GetValue(Name As String) As Object
Dim FoundName As View

    FoundName = NameMap.Get(Name)
    If FoundName.IsInitialized = False Then
        Return ""
    Else
        If FoundName Is Label Then
            Dim myLabel As Label
            myLabel = FoundName
            If myLabel.Tag Is MyCombobox Then
                Dim MCB As MyCombobox
                MCB = myLabel.Tag
                If MCB.CurSelection > -1 Then
                    If MCB.Items.Get(MCB.CurSelection) Is ComboItem Then
                        Dim MyComboItem As ComboItem
                        MyComboItem= MCB.Items.Get(MCB.CurSelection)
                        Return MyComboItem.Value
                    Else
                        Return MCB.Items.Get(MCB.CurSelection)
                    End If
                Else
                    Return ""
                End If
            Else
                Return myLabel.Text
            End If
        Else
            Return ""
        End If
    End If
End Sub

' Set Text of Named Views
Public Sub SetText(Name As String, Value As Object)
Dim FoundName As View

    FoundName = NameMap.Get(Name)
    If FoundName.IsInitialized = True Then
        If FoundName Is Label Then
            Dim myLabel As Label
            myLabel = FoundName
            If myLabel.Tag Is MyCombobox Then
                Dim MCB As MyCombobox
                Dim MyComboItem As ComboItem
                MCB = myLabel.Tag
                MCB.CurSelection= -1
                For i= 0 To MCB.Items.Size - 1
                    If MCB.Items.Get(i) Is ComboItem Then
                        MyComboItem= MCB.Items.Get(i)
                        If MyComboItem.Value = Value OR (IsNumber(Value) AND MyComboItem.Value = Bit.ParseInt(Value, 10)) Then
                            MCB.CurSelection= i
                            myLabel.Text= MyComboItem.Text
                            If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, i, MyComboItem.Value)
                            Exit
                        End If
                    Else
                        If MCB.Items.Get(i) = Value Then
                            MCB.CurSelection = i
                            myLabel.Text = Value
                            If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, i, MCB.items.Get(i))
                            Exit
                        End If
                    End If
                Next
                If MCB.CurSelection = -1 Then myLabel.Text= MCB.Prompt
            Else
                myLabel.Text = Value
            End If
        End If
    End If
End Sub

' Get Map of all Named Views Values
Public Sub SaveState As Map
Dim newMap As Map
Dim curValue As Object

    newMap.Initialize
    For i = 0 To NameMap.Size - 1
        If NameMap.GetValueAt(i) Is Label Then
            Dim myLabel As Label
            myLabel = NameMap.GetValueAt(i)
            If myLabel.Tag Is MyCombobox Then
                Dim MCB As MyCombobox
                MCB = myLabel.Tag
                curValue = MCB.CurSelection
            Else
                curValue = myLabel.Text
            End If
        Else
            curValue = ""
        End If
        newMap.Put(NameMap.GetKeyAt(i), curValue)
    Next
    Return newMap
End Sub

' Set Values of all Named Views with Provided Map
Public Sub RestoreState(Values As Map)
Dim myView As View

    For i = 0 To Values.Size - 1
        myView = NameMap.Get(Values.GetKeyAt(i))
        If myView.IsInitialized Then
            If myView Is Label Then
                Dim myLabel As Label
                myLabel = myView
                If myLabel.Tag Is MyCombobox Then
                    Dim MCB As MyCombobox
                    MCB = myLabel.Tag
                    If Values.GetValueAt(i) Is Int AND Values.GetValueAt(i) > -1 AND Values.GetValueAt(i) < MCB.Items.Size Then
                        MCB.CurSelection= Values.GetValueAt(i)
                        If MCB.Items.Get(MCB.CurSelection) Is ComboItem Then
                            Dim MyComboItem As ComboItem
                            MyComboItem= MCB.Items.Get(MCB.CurSelection)
                            myLabel.Text= MyComboItem.Text
                        Else
                            myLabel.Text= MCB.Items.Get(MCB.CurSelection)
                        End If
                    Else
                        MCB.CurSelection= -1
                        myLabel.Text = MCB.Prompt
                    End If
                Else
                    myLabel.Text = Values.GetValueAt(i)
                End If
            End If
        End If
    Next
End Sub

' Set Values of all Named Views with Provided Cursor Where the columns have the same names as the views
Public Sub RestoreStateDB(dbCursor As Cursor)
    If dbCursor.RowCount = 1 Then
        dbCursor.Position = 0
        For i = 0 To dbCursor.ColumnCount - 1
            SetText(dbCursor.GetColumnName(i), dbCursor.GetString2(i))
        Next
    End If
End Sub

' Get Copy of the Map of all Named Views
Public Sub GetViews As Map
Dim newMap As Map

    newMap.Initialize
    For i = 0 To NameMap.Size - 1
        newMap.Put(NameMap.GetKeyAt(i), NameMap.GetValueAt(i))
    Next
    Return newMap
End Sub

' Get the View with the given Name
Public Sub GetView(Name As String) As View
    Return NameMap.Get(Name)
End Sub

' Set Enabled status of Named View
Public Sub Enabled(Name As String, EnableView As Boolean)
Dim FoundName As View

    FoundName = NameMap.Get(Name)
    If FoundName.IsInitialized = True Then
        If FoundName Is EditText Then
            Dim ref As Reflector

            FoundName.Enabled = EnableView
            ref.Target = FoundName
            If EnableView = True Then
                FoundName.Background = SetNinePatchDrawable("editenabled")
                ref.RunMethod2("setFocusable", "True", "java.lang.boolean")
                ref.RunMethod2("setFocusableInTouchMode", "True", "java.lang.boolean")
            Else
                FoundName.Background = SetNinePatchDrawable("editdisabled")
                ref.RunMethod2("setFocusable", "False", "java.lang.boolean")
                ref.RunMethod2("setFocusableInTouchMode", "False", "java.lang.boolean")
            End If
        Else If FoundName Is Label Then
            FoundName.Enabled = EnableView
            If FoundName.Tag Is MyCombobox Then
                If EnableView = True Then
                    FoundName.Background = SetNinePatchDrawable("comboboxenabled")
                Else
                    FoundName.Background = SetNinePatchDrawable("comboboxdisabled")
                End If
            End If
        End If
    End If
End Sub

' Get the Max Width and Height of a Char or of SampleText if given
Public Sub GetCharSize(ViewTypeface As Typeface, ViewFontsize As Float, SampleText As String) As Rect
Dim testCanvas As Canvas
Dim strUtil As StringUtils
Dim testString As String
Dim CharFrame As Rect

    ScaleLabel.Typeface = ViewTypeface
    ScaleLabel.TextSize = ViewFontsize
    testCanvas.Initialize(ScaleLabel)
    CharFrame.Initialize(0,0,0,0)
    If SampleText.Length = 0 Then testString = CharFilter_Alpha & CharFilter_Numeric & "`~!@#$%^&*()_+=-[]\\{}|;':,./<>?" & QUOTE Else testString = SampleText
    ScaleLabel.Width = testCanvas.MeasureStringWidth(testString, ScaleLabel.Typeface, ScaleLabel.TextSize) + 2dip
    CharFrame.Bottom = strUtil.MeasureMultilineTextHeight(ScaleLabel, testString)
    If SampleText.Length > 0 Then
        CharFrame.Right = ScaleLabel.Width
    Else
        For x = 0 To testString.Length - 1
            CharFrame.Right = Max(CharFrame.Right, testCanvas.MeasureStringWidth(testString.CharAt(x), ScaleLabel.Typeface, ScaleLabel.TextSize))
        Next
    End If
    Return CharFrame
End Sub

Private Sub SetNinePatchDrawable(ImageName As String) As Object
    Dim r As Reflector
    Dim package As String
    Dim id As Int
    package = r.GetStaticField("anywheresoftware.b4a.BA", "packageName")
    id = r.GetStaticField(package & ".R$drawable", ImageName)
    r.Target = r.GetContext
    r.Target = r.RunMethod("getResources")
    Return r.RunMethod2("getDrawable", id, "java.lang.int")
End Sub

' Test if Value in View meets Min Character Requirements
Public Sub isValid(Name As String) As Boolean
Dim FoundName As View
Dim MCB As MyCombobox

    FoundName = NameMap.Get(Name)
    If FoundName.IsInitialized = True Then
        If FoundName Is EditText Then
            Dim myEdit As EditText
            Dim MTB As MyTextbox
            
            myEdit = FoundName
            MTB = myEdit.Tag
            Return myEdit.Text.Length >= MTB.MinChar
        Else If FoundName Is Label AND FoundName.Tag Is MyCombobox Then
            MCB = FoundName.Tag
            Return MCB.CurSelection > -1
        End If
    End If
    Return False
End Sub