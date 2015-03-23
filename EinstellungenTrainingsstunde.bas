Type=Activity
Version=2.00
FullScreen=False
IncludeTitle=True
@EndOfDesignText@
Sub Process_Globals
	Dim DirData As String			
'	DirData = File.DirDefaultExternal
	DirData = File.DirInternal
End Sub

Sub Globals

	Dim Bitmap1 As Bitmap
	Dim Fragenanzahl As Int			: Fragenanzahl=1160
	Dim PanelHeight As Int			: PanelHeight=130dip
	Dim Panels As Panel
	Dim btnSpeichern, Button2, btnTop As Button
	Dim scvHIntergrund As ScrollView
	Dim lblÜberschrift As Label
	Dim sbStärke As SeekBar
	Dim Fragen As List
	Dim s, b, e As Int
	Dim Werteliste As List
End Sub

Sub Activity_Create(FirstTime As Boolean)
	If FirstTime Then
		If File.ExternalWritable = False Then
     	Msgbox("Ich kann nicht auf die SD-Karte schreiben.", "")
      	Return
    End If

'		Fragen = File.ReadList(File.DirAssets, "Einhornapp.txt")
'
'		If File.Exists(DirData, "Werteliste.txt") = False Then
''			Werteliste = File.ReadList(File.DirAssets, "Werteliste.txt")
'			Werteliste.Initialize
'			For i = 0 To Fragenanzahl - 1
'				Werteliste.Add(Rnd(0, 101))
'			Next
'		Else
'			Werteliste = File.ReadList(DirData, "Werteliste.txt")
'		End If 
'	End If

'Überschrift erstellen
	
	Panels.Initialize("panels")
	Panels.Color = Colors.Gray ' RGB(Rnd(250, 255), Rnd(250, 255), Rnd(250, 255))
	lblÜberschrift.Initialize("")
	lblÜberschrift.Text = "Testperson:"
	lblÜberschrift.TextSize = 30
	lblÜberschrift.TextColor = Colors.White
	Panels.AddView(lblÜberschrift, 55dip, 10dip, 100%x - 5dip, 60dip)
	Activity.AddView(Panels, 0, 0, 100%x, 100%y) 'add the panel to the layout
	Activity.AddMenuItem("Saugworte", "Menu")
	
'Scrollview erstellen und aufs Panel pappen
	scvHIntergrund.Initialize(500)
	Panels.AddView(scvHIntergrund, 0, 50dip, 100%x - 5dip, 100%y - 110dip)
	
'Buttons erstellen und auf Panel Pappen
	btnSpeichern.Initialize ("btnSpeichern")
	Button2.Initialize ("Button2")
	btnTop.Initialize ("btnTop")
	btnSpeichern.Tag=1
	Button2.Tag=2
	btnTop.Tag=3
	btnSpeichern.Text="Speichern"
	Button2.Text=""
	btnTop.Text="Top"
	Panels.AddView (btnSpeichern, 0, 100%y - 50dip, 33%x, 50dip)
	Panels.AddView (Button2, 33%x, 100%y - 50dip, 33%x, 50dip)
	Panels.AddView (btnTop, 66%X, 100%y - 50dip, 33%x, 50dip)
	
'lange Liste erzeugen, solange wie die Buttons nicht funktionieren
	b = 0
	s = 1160
	Listenerzeugung
	End If
End Sub

Sub Listenerzeugung
	Dim i, val, val1 As Int
	'Alte Liste Löschen
	
	'XXX????
	
'Neue Liste erzeugen
	For i = 0 To Fragenanzahl - 1 'originalzeile
	'For i=b To s 'neue zeile
		Dim pnlEinheit As Panel
		Dim lblTafel As Label
		Dim edtEingabefeld As EditText
		
	'Panel auf Scrollview pappen
		pnlEinheit.Initialize("pnlEinheit")
		scvHIntergrund.Panel.AddView(pnlEinheit, 0 , 5dip + i * PanelHeight, 100%x - 5dip, PanelHeight)
		pnlEinheit.Tag = i
		
	'Tafel auf Panel pappen		
		lblTafel.Initialize("lblTafel")
		pnlEinheit.AddView(lblTafel, 3%x, 0dip, 95%x, 120dip)
		lblTafel.Tag=i
		lblTafel.TextSize=20
		lblTafel.color = Colors.LightGray
		lblTafel.textColor = Colors.Black
		lblTafel.Text = Fragen.Get(i)
		
	'Eingebafeld auf Tafel	pappen	
'		edtEingabefeld.Initialize("edtEingabefeld")
'		'original: pnlEinheit.AddView(edtEingabefeld,90dip, 5dip, 110dip, 40dip)
'		pnlEinheit.AddView(edtEingabefeld, 100%x - 80dip, 60dip, 70dip, 50dip)
'		edtEingabefeld.Tag=i
'		edtEingabefeld.ForceDoneButton=True
'		edtEingabefeld.Text = i
'		
	'Seekbar auf Tafel pappen
		sbStärke.Initialize("sbStärke")
		pnlEinheit.AddView(sbStärke, 10%x,75dip,80%x,30dip)
		sbStärke.Tag = i
		val1 = Werteliste.get(i)
		val = val1
		sbStärke.value = val
'		sbStärke.value = Rnd(0, 101)
		
	'Button auf Tafel	pappen
	'		btnKonstanten.Initialize("btnKonstanten")
	'		pnlEinheit.AddView(btnKonstanten,240dip, 5dip, 70dip, 40dip)
	'		btnKonstanten.Tag=i
	'		btnKonstanten.Text="Konstanten "&i
	
	Next
	'Scrollview höhe festlegen
	scvHIntergrund.Panel.Height = Fragenanzahl * PanelHeight
End Sub

Sub btnSpeichern_click
	'Seekbox einstellungen abspeichern um sie beim Neustart wieder zu laden
	Dim List1 As List
	Dim i, x As Int
	
	List1.Initialize

'	1. liste erzeugen aus den Seekwerten
	For i = 0 To Fragenanzahl - 1
'	2. liste in datei packen
		Dim pnl As Panel
		pnl = scvHIntergrund.Panel.GetView(i)
		Dim skb As SeekBar
		skb = pnl.GetView(1)
		x = skb.value
		List1.Add(x)
	Next
'	3. datei abspeichern
	File.WriteList(DirData, "Werteliste.txt", List1)
End Sub


Sub Button2_click
	
'	b = 0
'	s = 400
'	Listenerzeugung
	
End Sub


Sub btnTop_click
	
'	b = 400
'	s = 800
'	Listenerzeugung
	scvHIntergrund.ScrollPosition = 0
End Sub

Sub Button4_click
	
'	b = 800
'	s = 1160
'	Listenerzeugung

End Sub
