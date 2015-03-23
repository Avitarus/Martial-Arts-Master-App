Type=Activity
Version=3
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: False
#End Region

'Algen
'Alkohol
'Aufstriche
'Backtriebmittel
'Brot
'Brote und Backmischungen
'Brühe
'Diätische Lebensmittel
'Eier und Milchprodukte
'Eier/Teigwaren
'Eierspeisen
'Ei-Gerichte
'Eintopf
'Feinback/Torte/Kuchen
'Fertigprodukte
'Fett/Butter/Öl
'Fisch/Muschel/Krustentier
'Fleisch
'Frühstückscerealien
'Gelier- und Bindemittel
'Gemüse
'Getr.,Mehl und Milchprod.
'Geträne ohne Alk
'Getränke ohne Alk
'Getreide
'Getreide und andere stärkehaltige Produkte
'Hülsenfrüchte
'Joghurt- und Sahne- (Obers-) Alternativen
'Kartoffelgerichte
'Käse
'Kindernahrung
'Konfitüre
'Kräuter und Gewürze
'Margarine
'Milch
'Milcherzeugnisse
'Mineralwasser
'Nudelgerichte
'Nudeln
'Nüsse/Samen/Hülsenfrüchte
'Nussmuse
'Obst
'Öle und Fette
'Pflanzendrinks
'Pilze
'Reisgerichte
'Salat
'Schlachtplatten/Sülzen
'Schnellgerichte
'Speisen aus Milchprodukten
'Speisezutaten
'Sprossen
'Suppe
'Suppeneinlage
'Supplemente
'Süße Aufstriche, fruchtig
'Süße Austriche, schokoladig
'Süßigkeiten
'Süssspeisen/Desserts
'Süsswaren
'Tofu, Seitan, Tempeh, Burger …
'Vegetarischer Käse
'Vegetarisches
'Waffeln und Snacks
'Wild/Geflügel
'Wurst
'Würze, Ketchup, Hefe, Miso …
'Zucker, Sirup, Fruchtsüße





'Activity module
Sub Process_Globals

	Dim SQL1 As SQL
	Dim Cursor1 As Cursor
	Dim SpracheID As String
	Dim Text2(), Text4(),Text5(), Text6(), Text8() As String
	Dim  Text3(),	Text7(),	Text9(),	Text10(),	Text11(),	Text12(),	Text14(),	Text15(),	Text16(),	Text17() As Int
	Dim Text35(),	Text40(), Text44(),	Text45(),	Text46(),	Text48(),	Text49(),	Text50(),	Text51()	As Int
	Dim Text52(),	Text53(),	Text54(),	Text55(),	Text56(),	Text57(),	Text58(),	Text60(),	Text61(),	Text62(),	Text63(),	Text64(),	Text65(),	Text66(),	Text67() As Int
	Dim Text69(),	Text70(),	Text71(),	Text72(),	Text73(),	Text74(), Text76(),	Text77(),	Text78(),	Text79(),	Text80(),	Text81(),	Text82(),	Text83(),	Text84(),	Text85()	As Int
	Dim Text86(),	Text87(),	Text88(),	Text89(),	Text90(),	Text91(),	Text92(),	Text93(),	Text94(), Text96(),	Text97(),	Text98(),	Text99(),	Text100(),	Text101(),	Text102()	As Int
	Dim Text103(),	Text104(),	Text105(),	Text106(),	Text107(),	Text108(),	Text109(),	Text110(),	Text111(),	Text112(),	Text113(),	Text114(),	Text115() As Int

	Dim dbName As String					: dbName = "MAMA.sqlite"
	Dim dbTabellenname As String			: dbTabellenname = "Nahrungsmittel"
	Dim dbTabellenname1 As String			: dbTabellenname1 = "Tageswerte"
	
End Sub

Sub Globals

	Dim Label1, Label2 As Label
	'Dim Label8, Label9, Label10, Label11, Label12 As Label

	Dim EditText1 As EditText
	Dim ListView1 As ListView
	Dim Button1 As Button
	Dim Auswahl As Int
	Dim manager As AHPreferenceManager
	Dim	Gesamtgewicht, Tageskalorien, TagesEiweiss, TagesFett, TagesKohlenhydrate, TagesWasser As Int
	Dim	TagesGewicht,	TagesLebensmittel,	TagesIDNahrung,	TagesPreis,	TagesCode,	TagesGruppe As Int
	Dim	Tageskcal,	TageskJ,	TagesEW,	TagesFett,	TagesKH,	TagesCol,	TagesBE,	TagesPfl	As Int
	Dim	TagesFavorit,	TagesWasser0,	TagesAufgenommeneMenge,	TagesHilfe0,	TagesTkcal,	TagesTKJ	As Int
	Dim	TagesTEW,	TagesTFett,	TagesTKH,	TagesTCol,	TagesTBE,	TagesTTier,	TagesTPflanz	As Int
	Dim	TagesTPreis,	TagesTHO,	TagesWasser1,	TagesTEWPfl,	TagesTEWTier,	TagesBestLM As Int
	Dim	TagesHilfe1,	TagesAlkohol,	TagesAluminium,	TagesAmmonium,	TagesAntimon,	TagesArsen	As Int
	Dim	TagesBallaststoffe,	TagesBlei,	TagesBor,	TagesCadmium,	TagesCarotin,	TagesChlorid As Int
	Dim	TagesChrom,	TagesCyanid,	TagesEisen,	TagesEphidrin,	TagesmuFettsäuren,	TagesFluor As Int
	Dim	TagesFructose,	TagesGlucose,	TagesHarnsäure,	TagesJod,	TagesKalium,	TagesKalzium As Int
	Dim	TagesKoffein,	TagesKohlenwasserstoffe,	TagesKupfer,	TagesLactose,	TagesLutein As Int
	Dim	TagesMagnesium,	TagesMaltose,	TagesMangan,	TagesMolybdän,	TagesNatrium,	TagesNickel	 As Int
	Dim	TagesNitrat,	TagesNitrit,	TagesOmega3Fett,	TagesOmega6Fett,	TagesPhospholipide As Int
	Dim	TagesPhosphor,	TagesQuecksilber,	TagesSaccharose,	TagesSelen,	TagesSilizium,	TagesStickstoff As Int
	Dim	TagesSulfat,	TagesVARetinol,	TagesVB1Thiamin,	TagesVB2Riboflaffin,	TagesVB3Niacin As Int
	Dim	TagesVB5Pantothensäure,	TagesVB6Pyridoxin,	TagesVB7Biotin,	TagesVB9Folsäure As Int
	Dim	TagesVB12Kobalamin,	TagesVCAscorbinsäure,	TagesVDCholekalziferol,	TagesVETokopherol As Int
	Dim	TagesVKPhyllochinon,	TagesZink,	TagesHilfe2,	TagesaLinolensäure,	TagesIsoleucin As Int
	Dim	TagesLeucin,	TagesLysin,	TagesMethionin,	TagesPhenylalanin,	TagesThreonin,	TagesTryptophan	 As Int
	Dim	TagesValin,	TagesArginin,	TagesCrysin,	TagesHistidin,	TagesTyrosin,	TagesAlanin As Int
	Dim	TagesAsparaginsäure,	TagesGlutaminsäure,	TagesGlysin,	TagesProlin,	TagesSerin As Int
	'Dim	TagesMeistInhalt,	TagesHilfe3,

	
	
End Sub


Sub Activity_Create(FirstTime As Boolean)
	Dim now As Long
		now = DateTime.now
	ProgressDialogShow("Lebensmittel laden ...")
	
	'DoEvents

	'Activity.LoadLayout("Lebensmittel")
	
	Label1.Initialize("")
	Label2.Initialize("")
	EditText1.Initialize("EditText1")
	ListView1.Initialize("ListView1")
	'Button1.Initialize("")
	'left,top,wide hih
	
	EditText1.Hint = "Gramm"
	EditText1.InputType = EditText1.INPUT_TYPE_NUMBERS
	
	ListView1.FastScrollEnabled = True
	'ListView1.Clear
	ListView1.FastScrollEnabled = True
	
	Activity.AddView(Label1,0,0,100%x,10%y)
	Activity.AddView(EditText1,0,10%y,30%x,10%y)
	Activity.AddView(Label2,30%x,10%y,50%x,10%y)
	Activity.AddView(ListView1,0,20%y,100%x,80%y)
	'Activity.AddView(Button1,80%x,10%y,20%x,10%y)
	
	Activity.Title = "MAMA - Lebensmitteldatenbank"
	Activity.TitleColor = Colors.White
	tageswerteLaden
	
	Label2.Gravity = Gravity.LEFT
	
	Dim kcal As Int
	kcal = manager.GetString("kCal")
	'Msgbox(manager.GetString("kCal"),kcal)
	If Tageskalorien >  kcal Then 
		Label2.Color = Colors.Red
		Label2.TextColor = Colors.Black
	Else
		Label2.Color = Colors.Black
		Label2.TextColor = Colors.Green
	End If
	EditText1.Visible = False
	Label2.Text = " Tagesaufnahme: " &Gesamtgewicht &"g"& CRLF & " Kcal: "& Tageskalorien & "  EW: " & TagesEiweiss & CRLF &" FE: " & TagesFett & "  KH: " & TagesKohlenhydrate & "  WA: "& TagesWasser



	'If FirstTime = True Then

		SQL1.Initialize(File.DirRootExternal & Main.Unterordner18,dbName,True)
		SQL1.BeginTransaction
		
		'Alles
		Cursor1 = SQL1.ExecQuery("SELECT * FROM "& dbTabellenname) 'funzt
		'Ohne Bioläden
	    'Cursor1 = SQL1.ExecQuery("SELECT Lebensmittel, kcal, EW, Fett, KH, Wasser0 FROM "& dbTabellenname)  '   WHERE NOT (Lebensmittel Like 'Allos%' OR Lebensmittel Like 'Alnatura%' OR Lebensmittel Like 'Alpro%' OR Lebensmittel LIKE 'Alnatura%'	OR Lebensmittel LIKE 'Alpro%' OR Lebensmittel LIKE 'Bauckhof%' OR Lebensmittel LIKE'Berief%'  OR Lebensmittel LIKE'Bio.k%'  OR Lebensmittel LIKE'Bruno%'  OR Lebensmittel LIKE'Byodo%'  OR Lebensmittel LIKE'Dr. Ritter%'  OR Lebensmittel LIKE'Joya%'  OR Lebensmittel LIKE'Lima%'  OR Lebensmittel LIKE'Lubs%'  OR Lebensmittel LIKE'Manner%'  OR Lebensmittel LIKE'Martin%'  OR Lebensmittel LIKE'Perlinger%'  OR Lebensmittel LIKE'Provamel%'  OR Lebensmittel LIKE'Rapunzel%'  OR Lebensmittel LIKE'Soja%'  OR Lebensmittel LIKE'Sojvita%'  OR Lebensmittel LIKE'Sonnentor%'  OR Lebensmittel LIKE'Soto%'  OR Lebensmittel LIKE'Sojana%'  OR Lebensmittel LIKE'Taifun%'  OR Lebensmittel LIKE'Tartex%'  OR Lebensmittel LIKE'Tofu%'  OR Lebensmittel LIKE'Topas%'  OR Lebensmittel LIKE'Ulmafit%'  OR Lebensmittel LIKE'Vegavita%'  OR Lebensmittel LIKE'Veggie%'  OR Lebensmittel LIKE'Viana%'  OR Lebensmittel LIKE'Vitaquell%'  OR Lebensmittel LIKE'Werz%'  OR Lebensmittel LIKE'Zwergenwiese%')")
	    'nur 1 lebensmitel
		'Cursor1 = SQL1.ExecQuery("SELECT * FROM "& dbTabellenname & " WHERE Lebensmittel IN ('Maggi')")','Alnatura*','Alpro*','Bauckhof*','Berief','Bio.k','Bruno*','Byodo*','Dr.Ritter*','Joya*','Lima*','Lubs*','Manner*','Martin*','Perlinger*','Provamel*','Rapunzel*','Soja*','Sojvita*','Sonnentor*','Soto*','Sojana*','Taifun*','Tartex*','Tofu*','Topas*','Ulmafit*','Vegavita*','Veggie*','Viana*','Vitaquell*','Werz*','Zwergenwiese*')")

		'nur Vegetarisches
		
		
		Dim a = Cursor1.RowCount As Int
	

		Dim Text2(a), Text4(a),Text5(a), Text6(a), Text8(a) As String

		Dim  Text3(a),	Text7(a),	Text9(a),	Text10(a),	Text11(a),	Text12(a),	Text14(a),	Text15(a),	Text16(a),	Text17(a) As Int
		Dim Text35(a),	Text40(a), Text44(a),	Text45(a),	Text46(a),	Text48(a),	Text49(a),	Text50(a),	Text51(a)	As Int
		Dim Text52(a),	Text53(a),	Text54(a),	Text55(a),	Text56(a),	Text57(a),	Text58(a),	Text60(a),	Text61(a),	Text62(a),	Text63(a),	Text64(a),	Text65(a),	Text66(a),	Text67(a) As Int
		Dim Text69(a),	Text70(a),	Text71(a),	Text72(a),	Text73(a),	Text74(a), Text76(a),	Text77(a),	Text78(a),	Text79(a),	Text80(a),	Text81(a),	Text82(a),	Text83(a),	Text84(a),	Text85(a)	As Int
		Dim Text86(a),	Text87(a),	Text88(a),	Text89(a),	Text90(a),	Text91(a),	Text92(a),	Text93(a),	Text94(a), Text96(a),	Text97(a),	Text98(a),	Text99(a),	Text100(a),	Text101(a),	Text102(a)	As Int
		Dim Text103(a),	Text104(a),	Text105(a),	Text106(a),	Text107(a),	Text108(a),	Text109(a),	Text110(a),	Text111(a),	Text112(a),	Text113(a),	Text114(a),	Text115(a) As Int



		
		

	    For i = 0 To Cursor1.RowCount - 1
		    Cursor1.Position = i
			'Text1(i) = Cursor1.GetInt("Gewicht")
			Text2(i) = Cursor1.GetString("Lebensmittel")
			Text3(i) = i'Cursor1.Getint("IDNahrung")
			Text4(i) = Cursor1.GetString("Preis")
			Text5(i) = Cursor1.GetString("Code")
			Text6(i) = Cursor1.GetString("Gruppe")
			Text7(i) = Cursor1.Getint("kcal")
			'Text8(i) = Cursor1.GetString("kJ")
			Text9(i) = Cursor1.Getint("EW")
			Text10(i) = Cursor1.Getint("Fett")
			Text11(i) = Cursor1.Getint("KH")
			Text12(i) = Cursor1.GetString("Col")
			'Text13(i) = Cursor1.GetString("BE")
			Text14(i) = Cursor1.GetString("Pfl")
			Text15(i) = Cursor1.GetString("Favorit")
			Text16(i) = Cursor1.Getint("Wasser0")
			'Text17(i) = Cursor1.Getint("AufgenommeneMenge")
			'Text18(i) = Cursor1.Getint("Hilfe0")
			'Text19(i) = Cursor1.Getint("T-kcal")
			'Text20(i) = Cursor1.Getint("T-KJ")
			'Text21(i) = Cursor1.Getint("T-EW")
			'Text22(i) = Cursor1.Getint("T-Fett")
			'Text23(i) = Cursor1.Getint("T-KH")
			'Text24(i) = Cursor1.Getint("T-Col")
			'Text25(i) = Cursor1.Getint("T-BE")
			'Text26(i) = Cursor1.Getint("T-Tier")
			'Text27(i) = Cursor1.Getint("T-Pflanz")
			'Text28(i) = Cursor1.Getint("T-Preis")
			'Text29(i) = Cursor1.Getint("T-H²O")
			'Text30(i) = Cursor1.Getint("Wasser1")
			'Text31(i) = Cursor1.Getint("T-EW-Pfl")
			'Text32(i) = Cursor1.Getint("T-EW-Tier")
			'Text33(i) = Cursor1.Getint("Best-LM")
			'Text34(i) = Cursor1.Getint("Hilfe1")
			Text35(i) = Cursor1.Getint("Alkohol")
			'Text36(i) = Cursor1.Getint("Aluminium")
			'Text37(i) = Cursor1.Getint("Ammonium")
			'Text38(i) = Cursor1.Getint("Antimon")
			'Text39(i) = Cursor1.Getint("Arsen")
			Text40(i) = Cursor1.Getint("Ballaststoffe")
			'Text41(i) = Cursor1.Getint("Blei")
			'Text42(i) = Cursor1.Getint("Bor")
			'Text43(i) = Cursor1.Getint("Cadmium")
			Text44(i) = Cursor1.Getint("Carotin")
			Text45(i) = Cursor1.Getint("Chlorid")
			Text46(i) = Cursor1.Getint("Chrom")
			'Text47(i) = Cursor1.Getint("Cyanid")
			Text48(i) = Cursor1.Getint("Eisen")
			Text49(i) = Cursor1.Getint("Ephidrin")
			Text50(i) = Cursor1.Getint("muFettsäuren")
			Text51(i) = Cursor1.Getint("Fluor")
			Text52(i) = Cursor1.Getint("Fructose")
			Text53(i) = Cursor1.Getint("Glucose")
			Text54(i) = Cursor1.Getint("Harnsäure")
			Text55(i) = Cursor1.Getint("Jod")
			Text56(i) = Cursor1.Getint("Kalium")
			Text57(i) = Cursor1.Getint("Kalzium")
			Text58(i) = Cursor1.Getint("Koffein")
			'Text59(i) = Cursor1.Getint("Kohlenwasserstoffe")
			Text60(i) = Cursor1.Getint("Kupfer")
			Text61(i) = Cursor1.Getint("Lactose")
			Text62(i) = Cursor1.Getint("Lutein")
			Text63(i) = Cursor1.Getint("Magnesium")
			Text64(i) = Cursor1.Getint("Maltose")
			Text65(i) = Cursor1.Getint("Mangan")
			Text66(i) = Cursor1.Getint("Molybdän")
			Text67(i) = Cursor1.Getint("Natrium")
			'Text68(i) = Cursor1.Getint("Nickel")
			Text69(i) = Cursor1.Getint("Nitrat")
			Text70(i) = Cursor1.Getint("Nitrit")
			Text71(i) = Cursor1.Getint("Omega3Fett")
			Text72(i) = Cursor1.Getint("Omega6Fett")
			Text73(i) = Cursor1.Getint("Phospholipide")
			Text74(i) = Cursor1.Getint("Phosphor")
			'Text75(i) = Cursor1.Getint("Quecksilber")
			Text76(i) = Cursor1.Getint("Saccharose")
			Text77(i) = Cursor1.Getint("Selen")
			Text78(i) = Cursor1.Getint("Silizium")
			Text79(i) = Cursor1.Getint("Stickstoff")
			Text80(i) = Cursor1.Getint("Sulfat")
			Text81(i) = Cursor1.Getint("V-A-Retinol")
			Text82(i) = Cursor1.Getint("V-B1-Thiamin")
			Text83(i) = Cursor1.Getint("V-B2-Riboflaffin")
			Text84(i) = Cursor1.Getint("V-B3-Niacin")
			Text85(i) = Cursor1.Getint("V-B5-Pantothensäure")
			Text86(i) = Cursor1.Getint("V-B6-Pyridoxin")
			Text87(i) = Cursor1.Getint("V-B7-Biotin")
			Text88(i) = Cursor1.Getint("V-B9-Folsäure")
			Text89(i) = Cursor1.Getint("V-B12-Kobalamin")
			Text90(i) = Cursor1.Getint("V-C-Ascorbinsäure")
			Text91(i) = Cursor1.Getint("V-D-Cholekalziferol")
			Text92(i) = Cursor1.Getint("V-E-Tokopherol")
			Text93(i) = Cursor1.Getint("V-K-Phyllochinon")
			Text94(i) = Cursor1.Getint("Zink")
			'Text95(i) = Cursor1.Getint("Hilfe2")
			Text96(i) = Cursor1.Getint("a-Linolensäure")
			Text97(i) = Cursor1.Getint("Isoleucin")
			Text98(i) = Cursor1.Getint("Leucin")
			Text99(i) = Cursor1.Getint("Lysin")
			Text100(i) = Cursor1.Getint("Methionin")
			Text101(i) = Cursor1.Getint("Phenylalanin")
			Text102(i) = Cursor1.Getint("Threonin")
			Text103(i) = Cursor1.Getint("Tryptophan")
			Text104(i) = Cursor1.Getint("Valin")
			Text105(i) = Cursor1.Getint("Arginin")
			Text106(i) = Cursor1.Getint("Crysin")
			Text107(i) = Cursor1.Getint("Histidin")
			Text108(i) = Cursor1.Getint("Tyrosin")
			Text109(i) = Cursor1.Getint("Alanin")
			Text110(i) = Cursor1.Getint("Asparaginsäure")
			Text111(i) = Cursor1.Getint("Glutaminsäure")
			Text112(i) = Cursor1.Getint("Glysin")
			Text113(i) = Cursor1.Getint("Prolin")
			Text114(i) = Cursor1.Getint("Serin")
			Text115(i) = Cursor1.Getint("Meist-Inhalt")
			'Text116(i) = Cursor1.Getint("Hilfe3")


			
	    Next

		'ListView1...ItemHeight = 60
		ProgressDialogShow("Auswahl laden ...")
		Dim Text2ersatz As String
		
	    For i = 0 To Cursor1.RowCount -1
			If Text2(i).Length < 32 Then
				Text2ersatz = Text2(i).SubString2(0,Text2(i).Length)
			Else
				Text2ersatz = Text2(i).SubString2(0,32)
			'	ListView1.AddTwoLines2(i & ". " & Text2(i).SubString2(0,25), "(" & Text1(i)&"g) Kal: " & Round(Text7(i)) & " E: " & Round(Text9(i))& " F: " & Round(Text10(i)) & " K: " & Round(Text11(i)) & " W: " & Round(Text16(i)),Text3(i))
			End If
	'		ListView1.AddTwoLines2(Text2ersatz,"(" & Text1(i) & "g)   Kal: " & Text7(i) & "   E: "&Text9(i)& "   F: " & Text10(i) & "   K: " & Text11(i) & " W: " & Text16(i),Text3(i) )
			ListView1.AddTwoLines2(Text2ersatz,"kCal: " & Text7(i) & "   Ew: "&Text9(i)& "   Fe: " & Text10(i) & "   Kh: " & Text11(i) & "   Wa: " & Text16(i),Text3(i) )

		
		Next
		Cursor1.close
		
		SQL1.TransactionSuccessful
		SQL1.EndTransaction
			
	'End If

	ProgressDialogHide
	ProgressDialogHide
	ToastMessageShow("Ladezeit: "& ((DateTime.now - now)/1000) & "sec",True)
	


End Sub

Sub Activity_Resume


End Sub

Sub Activity_Pause (UserClosed As Boolean)
	
	'StateManager.SaveState(Activity, "Schimpfwortgenerator")
    If UserClosed Then
       StateManager.ResetState("Lebensmittel")
    Else
        StateManager.SaveState(Activity, "Lebensmittel")
    End If
    StateManager.SaveSettings

End Sub

Sub tageswerteLaden
   ' If StateManager..RestoreState(Activity, "Lebensmittel", 0) = False Then
	
		Tageskalorien = StateManager.GetSetting2("Tageskalorien",1)
		TagesEiweiss = StateManager.GetSetting2("TagesEiweiss",1)
		TagesFett = StateManager.GetSetting2("TagesFett",1) 
		TagesKohlenhydrate = StateManager.Getsetting2("TagesKohlenhydrate",1)  
		TagesWasser = StateManager.Getsetting2("TagesWasser",1)  
		Gesamtgewicht = StateManager.Getsetting2("Gesamtgewicht",1)
		
		'TagesGewicht = StateManager.GetSetting2("TagesGewicht",1)
		'TagesLebensmittel = StateManager.GetSetting2("TagesLebensmittel",1)
		'TagesIDNahrung = StateManager.GetSetting2("TagesIDNahrung",1)
		TagesPreis = StateManager.GetSetting2("TagesPreis",1)
		'TagesCode = StateManager.GetSetting2("TagesCode",1)
		'TagesGruppe = StateManager.GetSetting2("TagesGruppe",1)
		Tageskcal = StateManager.GetSetting2("Tageskcal",1)
		'TageskJ = StateManager.GetSetting2("TageskJ",1)
		'TagesEW = StateManager.GetSetting2("TagesEW",1)
		'TagesFett = StateManager.GetSetting2("TagesFett",1)
		'TagesKH = StateManager.GetSetting2("TagesKH",1)
		TagesCol = StateManager.GetSetting2("TagesCol",1)
		TagesBE = StateManager.GetSetting2("TagesBE",1)
		TagesPfl = StateManager.GetSetting2("TagesPfl",1)
		'TagesFavorit = StateManager.GetSetting2("TagesFavorit",1)
		'TagesWasser0 = StateManager.GetSetting2("TagesWasser0",1)
		'TagesAufgenommeneMenge = StateManager.GetSetting2("TagesAufgenommeneMenge",1)
		'TagesHilfe0 = StateManager.GetSetting2("TagesHilfe0",1)
		'TagesTkcal = StateManager.GetSetting2("TagesTkcal",1)
		'TagesTKJ = StateManager.GetSetting2("TagesTKJ",1)
		'TagesTEW = StateManager.GetSetting2("TagesTEW",1)
		'TagesTFett = StateManager.GetSetting2("TagesTFett",1)
		'TagesTKH = StateManager.GetSetting2("TagesTKH",1)
		'TagesTCol = StateManager.GetSetting2("TagesTCol",1)
		'TagesTBE = StateManager.GetSetting2("TagesTBE",1)
		'TagesTTier = StateManager.GetSetting2("TagesTTier",1)
		'TagesTPflanz = StateManager.GetSetting2("TagesTPflanz",1)
		'TagesTPreis = StateManager.GetSetting2("TagesTPreis",1)
		'TagesTHO = StateManager.GetSetting2("TagesTHO",1)
		'TagesWasser1 = StateManager.GetSetting2("TagesWasser1",1)
		'TagesTEWPfl = StateManager.GetSetting2("TagesTEWPfl",1)
		'TagesTEWTier = StateManager.GetSetting2("TagesTEWTier",1)
		'TagesBestLM = StateManager.GetSetting2("TagesBestLM",1)
		'TagesHilfe1 = StateManager.GetSetting2("TagesHilfe1",1)
		TagesAlkohol = StateManager.GetSetting2("TagesAlkohol",1)
		'TagesAluminium = StateManager.GetSetting2("TagesAluminium",1)
		'TagesAmmonium = StateManager.GetSetting2("TagesAmmonium",1)
		'TagesAntimon = StateManager.GetSetting2("TagesAntimon",1)
		'TagesArsen = StateManager.GetSetting2("TagesArsen",1)
		TagesBallaststoffe = StateManager.GetSetting2("TagesBallaststoffe",1)
		'TagesBlei = StateManager.GetSetting2("TagesBlei",1)
		'TagesBor = StateManager.GetSetting2("TagesBor",1)
		'TagesCadmium = StateManager.GetSetting2("TagesCadmium",1)
		TagesCarotin = StateManager.GetSetting2("TagesCarotin",1)
		TagesChlorid = StateManager.GetSetting2("TagesChlorid",1)
		TagesChrom = StateManager.GetSetting2("TagesChrom",1)
		'TagesCyanid = StateManager.GetSetting2("TagesCyanid",1)
		TagesEisen = StateManager.GetSetting2("TagesEisen",1)
		TagesEphidrin = StateManager.GetSetting2("TagesEphidrin",1)
		TagesmuFettsäuren = StateManager.GetSetting2("TagesmuFettsäuren",1)
		TagesFluor = StateManager.GetSetting2("TagesFluor",1)
		TagesFructose = StateManager.GetSetting2("TagesFructose",1)
		TagesGlucose = StateManager.GetSetting2("TagesGlucose",1)
		TagesHarnsäure = StateManager.GetSetting2("TagesHarnsäure",1)
		TagesJod = StateManager.GetSetting2("TagesJod",1)
		TagesKalium = StateManager.GetSetting2("TagesKalium",1)
		TagesKalzium = StateManager.GetSetting2("TagesKalzium",1)
		TagesKoffein = StateManager.GetSetting2("TagesKoffein",1)
		'TagesKohlenwasserstoffe = StateManager.GetSetting2("TagesKohlenwasserstoffe",1)
		TagesKupfer = StateManager.GetSetting2("TagesKupfer",1)
		TagesLactose = StateManager.GetSetting2("TagesLactose",1)
		TagesLutein = StateManager.GetSetting2("TagesLutein",1)
		TagesMagnesium = StateManager.GetSetting2("TagesMagnesium",1)
		TagesMaltose = StateManager.GetSetting2("TagesMaltose",1)
		TagesMangan = StateManager.GetSetting2("TagesMangan",1)
		TagesMolybdän = StateManager.GetSetting2("TagesMolybdän",1)
		TagesNatrium = StateManager.GetSetting2("TagesNatrium",1)
		'TagesNickel = StateManager.GetSetting2("TagesNickel",1)
		TagesNitrat = StateManager.GetSetting2("TagesNitrat",1)
		TagesNitrit = StateManager.GetSetting2("TagesNitrit",1)
		TagesOmega3Fett = StateManager.GetSetting2("TagesOmega3Fett",1)
		TagesOmega6Fett = StateManager.GetSetting2("TagesOmega6Fett",1)
		TagesPhospholipide = StateManager.GetSetting2("TagesPhospholipide",1)
		TagesPhosphor = StateManager.GetSetting2("TagesPhosphor",1)
		'TagesQuecksilber = StateManager.GetSetting2("TagesQuecksilber",1)
		TagesSaccharose = StateManager.GetSetting2("TagesSaccharose",1)
		TagesSelen = StateManager.GetSetting2("TagesSelen",1)
		TagesSilizium = StateManager.GetSetting2("TagesSilizium",1)
		TagesStickstoff = StateManager.GetSetting2("TagesStickstoff",1)
		TagesSulfat = StateManager.GetSetting2("TagesSulfat",1)
		TagesVARetinol = StateManager.GetSetting2("TagesVARetinol",1)
		TagesVB1Thiamin = StateManager.GetSetting2("TagesVB1Thiamin",1)
		TagesVB2Riboflaffin = StateManager.GetSetting2("TagesVB2Riboflaffin",1)
		TagesVB3Niacin = StateManager.GetSetting2("TagesVB3Niacin",1)
		TagesVB5Pantothensäure = StateManager.GetSetting2("TagesVB5Pantothensäure",1)
		TagesVB6Pyridoxin = StateManager.GetSetting2("TagesVB6Pyridoxin",1)
		TagesVB7Biotin = StateManager.GetSetting2("TagesVB7Biotin",1)
		TagesVB9Folsäure = StateManager.GetSetting2("TagesVB9Folsäure",1)
		TagesVB12Kobalamin = StateManager.GetSetting2("TagesVB12Kobalamin",1)
		TagesVCAscorbinsäure = StateManager.GetSetting2("TagesVCAscorbinsäure",1)
		TagesVDCholekalziferol = StateManager.GetSetting2("TagesVDCholekalziferol",1)
		TagesVETokopherol = StateManager.GetSetting2("TagesVETokopherol",1)
		TagesVKPhyllochinon = StateManager.GetSetting2("TagesVKPhyllochinon",1)
		TagesZink = StateManager.GetSetting2("TagesZink",1)
		'TagesHilfe2 = StateManager.GetSetting2("TagesHilfe2",1)
		TagesaLinolensäure = StateManager.GetSetting2("TagesaLinolensäure",1)
		TagesIsoleucin = StateManager.GetSetting2("TagesIsoleucin",1)
		TagesLeucin = StateManager.GetSetting2("TagesLeucin",1)
		TagesLysin = StateManager.GetSetting2("TagesLysin",1)
		TagesMethionin = StateManager.GetSetting2("TagesMethionin",1)
		TagesPhenylalanin = StateManager.GetSetting2("TagesPhenylalanin",1)
		TagesThreonin = StateManager.GetSetting2("TagesThreonin",1)
		TagesTryptophan = StateManager.GetSetting2("TagesTryptophan",1)
		TagesValin = StateManager.GetSetting2("TagesValin",1)
		TagesArginin = StateManager.GetSetting2("TagesArginin",1)
		TagesCrysin = StateManager.GetSetting2("TagesCrysin",1)
		TagesHistidin = StateManager.GetSetting2("TagesHistidin",1)
		TagesTyrosin = StateManager.GetSetting2("TagesTyrosin",1)
		TagesAlanin = StateManager.GetSetting2("TagesAlanin",1)
		TagesAsparaginsäure = StateManager.GetSetting2("TagesAsparaginsäure",1)
		TagesGlutaminsäure = StateManager.GetSetting2("TagesGlutaminsäure",1)
		TagesGlysin = StateManager.GetSetting2("TagesGlysin",1)
		TagesProlin = StateManager.GetSetting2("TagesProlin",1)
		TagesSerin = StateManager.GetSetting2("TagesSerin",1)
		'TagesMeistInhalt = StateManager.GetSetting2("TagesMeistInhalt",1)
		'TagesHilfe3 = StateManager.GetSetting2("TagesHilfe3",1)

		
   '	End If


End Sub


Sub TageswerteSpeichern

'liste der variablen in der Tabelle Tageswerte: DateTime.date(DateTime.Now),Tageskalorien,TagesEiweiss,TagesFett,TagesKohlenhydrate,TagesWasser,Gesamtgewicht,TagesGewicht, TagesPreis,TageskJ,TagesCol,TagesBE,TagesPfl,TagesFavorit,TagesWasser0,TagesAufgenommeneMenge,	TagesAlkohol,TagesAluminium,TagesAmmonium,TagesAntimon,TagesArsen,TagesBallaststoffe,TagesBlei,TagesBor,TagesCadmium,TagesCarotin,TagesChlorid,TagesChrom,TagesCyanid,TagesEisen,TagesEphidrin,	TagesmuFettsäuren,TagesFluor,TagesFructose,TagesGlucose,TagesHarnsäure,TagesJod,TagesKalium, TagesKalzium,TagesKoffein,TagesKohlenwasserstoffe,TagesKupfer,TagesLactose,TagesLutein, TagesMagnesium,TagesMaltose,TagesMangan,TagesMolybdän,TagesNatrium,TagesNickel,TagesNitrat,TagesNitrit,TagesOmega3Fett,TagesOmega6Fett,TagesPhospholipide,	TagesPhosphor,TagesQuecksilber,TagesSaccharose,TagesSelen,TagesSilizium,TagesStickstoff,TagesSulfat,TagesVARetinol,TagesVB1Thiamin,TagesVB2Riboflaffin,TagesVB3Niacin,TagesVB5Pantothensäure,TagesVB6Pyridoxin,TagesVB7Biotin,TagesVB9Folsäure, TagesVB12Kobalamin,TagesVCAscorbinsäure,TagesVDCholekalziferol,TagesVETokopherol,TagesVKPhyllochinon,TagesZink,TagesHilfe2,TagesaLinolensäure,TagesIsoleucin,TagesLeucin,TagesLysin,TagesMethionin,TagesPhenylalanin,TagesThreonin, TagesTryptophan,TagesValin,TagesArginin,TagesCrysin,TagesHistidin,TagesTyrosin,TagesAlanin,TagesAsparaginsäure,TagesGlutaminsäure,TagesGlysin,TagesProlin,TagesSerin

	StateManager.SetSetting("Tageskalorien",Tageskalorien)
	StateManager.SetSetting("TagesEiweiss",TagesEiweiss)
	StateManager.SetSetting("TagesFett",TagesFett)
	StateManager.SetSetting("TagesKohlenhydrate",TagesKohlenhydrate)
	StateManager.SetSetting("TagesWasser",TagesWasser)
	StateManager.SetSetting("Gesamtgewicht",Gesamtgewicht)
	StateManager.SetSetting("TagesGewicht",TagesGewicht)
	'StateManager.SetSetting("TagesLebensmittel",TagesLebensmittel)
	'StateManager.SetSetting("TagesIDNahrung",TagesIDNahrung)
	StateManager.SetSetting("TagesPreis",TagesPreis)
	'StateManager.SetSetting("TagesCode",TagesCode)
	'StateManager.SetSetting("TagesGruppe",TagesGruppe)
	'StateManager.SetSetting("Tageskcal",Tageskcal)
	'StateManager.SetSetting("TageskJ",TageskJ)
	'StateManager.SetSetting("TagesEW",TagesEW)
	'StateManager.SetSetting("TagesFett",TagesFett)
	'StateManager.SetSetting("TagesKH",TagesKH)
	StateManager.SetSetting("TagesCol",TagesCol)
	'StateManager.SetSetting("TagesBE",TagesBE)
	StateManager.SetSetting("TagesPfl",TagesPfl)
	StateManager.SetSetting("TagesFavorit",TagesFavorit)
	'StateManager.SetSetting("TagesWasser0",TagesWasser0)
	'StateManager.SetSetting("TagesAufgenommeneMenge",TagesAufgenommeneMenge)
	'StateManager.SetSetting("TagesHilfe0",TagesHilfe0)
	'StateManager.SetSetting("TagesTkcal",TagesTkcal)
	'StateManager.SetSetting("TagesTKJ",TagesTKJ)
	'StateManager.SetSetting("TagesTEW",TagesTEW)
	'StateManager.SetSetting("TagesTFett",TagesTFett)
	'StateManager.SetSetting("TagesTKH",TagesTKH)
	'StateManager.SetSetting("TagesTCol",TagesTCol)
	'StateManager.SetSetting("TagesTBE",TagesTBE)
	'StateManager.SetSetting("TagesTTier",TagesTTier)
	'StateManager.SetSetting("TagesTPflanz",TagesTPflanz)
	'StateManager.SetSetting("TagesTPreis",TagesTPreis)
	'StateManager.SetSetting("TagesTH²O",TagesTHO)
	'StateManager.SetSetting("TagesWasser1",TagesWasser1)
	'StateManager.SetSetting("TagesTEWPfl",TagesTEWPfl)
	'StateManager.SetSetting("TagesTEWTier",TagesTEWTier)
	'StateManager.SetSetting("TagesBestLM",TagesBestLM)
	'StateManager.SetSetting("TagesHilfe1",TagesHilfe1)
	'StateManager.SetSetting("TagesAlkohol",TagesAlkohol)
	'StateManager.SetSetting("TagesAluminium",TagesAluminium)
	'StateManager.SetSetting("TagesAmmonium",TagesAmmonium)
	'StateManager.SetSetting("TagesAntimon",TagesAntimon)
	'StateManager.SetSetting("TagesArsen",TagesArsen)
	'StateManager.SetSetting("TagesBallaststoffe",TagesBallaststoffe)
	'StateManager.SetSetting("TagesBlei",TagesBlei)
	'StateManager.SetSetting("TagesBor",TagesBor)
	'StateManager.SetSetting("TagesCadmium",TagesCadmium)
	StateManager.SetSetting("TagesCarotin",TagesCarotin)
	StateManager.SetSetting("TagesChlorid",TagesChlorid)
	StateManager.SetSetting("TagesChrom",TagesChrom)
	StateManager.SetSetting("TagesCyanid",TagesCyanid)
	StateManager.SetSetting("TagesEisen",TagesEisen)
	StateManager.SetSetting("TagesEphidrin",TagesEphidrin)
	StateManager.SetSetting("TagesmuFettsäuren",TagesmuFettsäuren)
	StateManager.SetSetting("TagesFluor",TagesFluor)
	StateManager.SetSetting("TagesFructose",TagesFructose)
	StateManager.SetSetting("TagesGlucose",TagesGlucose)
	StateManager.SetSetting("TagesHarnsäure",TagesHarnsäure)
	StateManager.SetSetting("TagesJod",TagesJod)
	StateManager.SetSetting("TagesKalium",TagesKalium)
	StateManager.SetSetting("TagesKalzium",TagesKalzium)
	StateManager.SetSetting("TagesKoffein",TagesKoffein)
	'StateManager.SetSetting("TagesKohlenwasserstoffe",TagesKohlenwasserstoffe)
	StateManager.SetSetting("TagesKupfer",TagesKupfer)
	StateManager.SetSetting("TagesLactose",TagesLactose)
	StateManager.SetSetting("TagesLutein",TagesLutein)
	StateManager.SetSetting("TagesMagnesium",TagesMagnesium)
	StateManager.SetSetting("TagesMaltose",TagesMaltose)
	StateManager.SetSetting("TagesMangan",TagesMangan)
	StateManager.SetSetting("TagesMolybdän",TagesMolybdän)
	StateManager.SetSetting("TagesNatrium",TagesNatrium)
	StateManager.SetSetting("TagesNickel",TagesNickel)
	StateManager.SetSetting("TagesNitrat",TagesNitrat)
	StateManager.SetSetting("TagesNitrit",TagesNitrit)
	StateManager.SetSetting("TagesOmega3Fett",TagesOmega3Fett)
	StateManager.SetSetting("TagesOmega6Fett",TagesOmega6Fett)
	StateManager.SetSetting("TagesPhospholipide",TagesPhospholipide)
	StateManager.SetSetting("TagesPhosphor",TagesPhosphor)
	'StateManager.SetSetting("TagesQuecksilber",TagesQuecksilber)
	StateManager.SetSetting("TagesSaccharose",TagesSaccharose)
	StateManager.SetSetting("TagesSelen",TagesSelen)
	StateManager.SetSetting("TagesSilizium",TagesSilizium)
	StateManager.SetSetting("TagesStickstoff",TagesStickstoff)
	StateManager.SetSetting("TagesSulfat",TagesSulfat)
	StateManager.SetSetting("TagesVARetinol",TagesVARetinol)
	StateManager.SetSetting("TagesVB1Thiamin",TagesVB1Thiamin)
	StateManager.SetSetting("TagesVB2Riboflaffin",TagesVB2Riboflaffin)
	StateManager.SetSetting("TagesVB3Niacin",TagesVB3Niacin)
	StateManager.SetSetting("TagesVB5Pantothensäure",TagesVB5Pantothensäure)
	StateManager.SetSetting("TagesVB6Pyridoxin",TagesVB6Pyridoxin)
	StateManager.SetSetting("TagesVB7Biotin",TagesVB7Biotin)
	StateManager.SetSetting("TagesVB9Folsäure",TagesVB9Folsäure)
	StateManager.SetSetting("TagesVB12Kobalamin",TagesVB12Kobalamin)
	StateManager.SetSetting("TagesVCAscorbinsäure",TagesVCAscorbinsäure)
	StateManager.SetSetting("TagesVDCholekalziferol",TagesVDCholekalziferol)
	StateManager.SetSetting("TagesVETokopherol",TagesVETokopherol)
	StateManager.SetSetting("TagesVKPhyllochinon",TagesVKPhyllochinon)
	StateManager.SetSetting("TagesZink",TagesZink)
	StateManager.SetSetting("TagesHilfe2",TagesHilfe2)
	StateManager.SetSetting("TagesaLinolensäure",TagesaLinolensäure)
	StateManager.SetSetting("TagesIsoleucin",TagesIsoleucin)
	StateManager.SetSetting("TagesLeucin",TagesLeucin)
	StateManager.SetSetting("TagesLysin",TagesLysin)
	StateManager.SetSetting("TagesMethionin",TagesMethionin)
	StateManager.SetSetting("TagesPhenylalanin",TagesPhenylalanin)
	StateManager.SetSetting("TagesThreonin",TagesThreonin)
	StateManager.SetSetting("TagesTryptophan",TagesTryptophan)
	StateManager.SetSetting("TagesValin",TagesValin)
	StateManager.SetSetting("TagesArginin",TagesArginin)
	StateManager.SetSetting("TagesCrysin",TagesCrysin)
	StateManager.SetSetting("TagesHistidin",TagesHistidin)
	StateManager.SetSetting("TagesTyrosin",TagesTyrosin)
	StateManager.SetSetting("TagesAlanin",TagesAlanin)
	StateManager.SetSetting("TagesAsparaginsäure",TagesAsparaginsäure)
	StateManager.SetSetting("TagesGlutaminsäure",TagesGlutaminsäure)
	StateManager.SetSetting("TagesGlysin",TagesGlysin)
	StateManager.SetSetting("TagesProlin",TagesProlin)
	StateManager.SetSetting("TagesSerin",TagesSerin)
	'StateManager.SetSetting("TagesMeistInhalt",TagesMeistInhalt)

	StateManager.SaveState(Activity,"Lebensmittel")
	StateManager.SaveSettings
	'Msgbox(StateManager.GetSetting("Tageskalorien"),"Liste")

End Sub

Sub Tageswerte0Setzen

	DateTime.DateFormat = "dd.MM.yyyy" 
	'Msgbox(DateTime.date(DateTime.Now),"datum")
		SQL1.Initialize(File.DirRootExternal & Main.Unterordner18,dbName,True)
		SQL1.BeginTransaction
	'Try
		SQL1.ExecNonQuery2("INSERT INTO " & dbTabellenname1 & " VALUES(?,?,?,?,?,?,?)",Array As Object(DateTime.date(DateTime.Now),Tageskalorien,TagesEiweiss,TagesFett,TagesKohlenhydrate,TagesWasser,Gesamtgewicht))
'	Catch
'		SQL1.ExecNonQuery2("UPDATE " & dbTabellenname1 & " SET col1 = "& DateTime.date(DateTime.Now), col2 Tageskalorien,TagesEiweiss,TagesFett,TagesKohlenhydrate,TagesWasser,Gesamtgewicht))
'	End Try
	
	SQL1.EndTransaction


	StateManager.SetSetting("Tageskalorien",0)
	StateManager.SetSetting("TagesEiweiss",0)
	StateManager.SetSetting("TagesFett",0)
	StateManager.SetSetting("TagesKohlenhydrate",0)
	StateManager.SetSetting("TagesWasser",0)
	StateManager.SetSetting("Gesamtgewicht",0)
	StateManager.SetSetting("TagesGewicht",0)
	'StateManager.SetSetting("TagesLebensmittel",0)
	'StateManager.SetSetting("TagesIDNahrung",0)
	StateManager.SetSetting("TagesPreis",0)
	'StateManager.SetSetting("TagesCode",0)
	'StateManager.SetSetting("TagesGruppe",0)
	'StateManager.SetSetting("Tageskcal",0)
	'StateManager.SetSetting("TageskJ",0)
	'StateManager.SetSetting("TagesEW",0)
	'StateManager.SetSetting("TagesFett",0)
	'StateManager.SetSetting("TagesKH",0)
	StateManager.SetSetting("TagesCol",0)
	StateManager.SetSetting("TagesBE",0)
	StateManager.SetSetting("TagesPfl",0)
	StateManager.SetSetting("TagesFavorit",0)
	'StateManager.SetSetting("TagesWasser0",0)
	'StateManager.SetSetting("TagesAufgenommeneMenge",0)
	'StateManager.SetSetting("TagesHilfe0",0)
	'StateManager.SetSetting("TagesTkcal",0)
'	StateManager.SetSetting("TagesTKJ",0)
'	StateManager.SetSetting("TagesTEW",0)
'	StateManager.SetSetting("TagesTFett",0)
'	StateManager.SetSetting("TagesTKH",0)
'	StateManager.SetSetting("TagesTCol",0)
'	StateManager.SetSetting("TagesTBE",0)
'	StateManager.SetSetting("TagesTTier",0)
'	StateManager.SetSetting("TagesTPflanz",0)
'	StateManager.SetSetting("TagesTPreis",0)
'	StateManager.SetSetting("TagesTH²O",0)
'	StateManager.SetSetting("TagesWasser1",0)
'	StateManager.SetSetting("TagesTEWPfl",0)
'	StateManager.SetSetting("TagesTEWTier",0)
'	StateManager.SetSetting("TagesBestLM",0)
'	StateManager.SetSetting("TagesHilfe1",0)
	StateManager.SetSetting("TagesAlkohol",0)
	'StateManager.SetSetting("TagesAluminium",0)
	StateManager.SetSetting("TagesAmmonium",0)
	StateManager.SetSetting("TagesAntimon",0)
	'StateManager.SetSetting("TagesArsen",0)
	StateManager.SetSetting("TagesBallaststoffe",0)
	'StateManager.SetSetting("TagesBlei",0)
	'StateManager.SetSetting("TagesBor",0)
	'StateManager.SetSetting("TagesCadmium",0)
	StateManager.SetSetting("TagesCarotin",0)
	StateManager.SetSetting("TagesChlorid",0)
	StateManager.SetSetting("TagesChrom",0)
	StateManager.SetSetting("TagesCyanid",0)
	StateManager.SetSetting("TagesEisen",0)
	StateManager.SetSetting("TagesEphidrin",0)
	StateManager.SetSetting("TagesmuFettsäuren",0)
	StateManager.SetSetting("TagesFluor",0)
	StateManager.SetSetting("TagesFructose",0)
	StateManager.SetSetting("TagesGlucose",0)
	StateManager.SetSetting("TagesHarnsäure",0)
	StateManager.SetSetting("TagesJod",0)
	StateManager.SetSetting("TagesKalium",0)
	StateManager.SetSetting("TagesKalzium",0)
	StateManager.SetSetting("TagesKoffein",0)
	'StateManager.SetSetting("TagesKohlenwasserstoffe",0)
	StateManager.SetSetting("TagesKupfer",0)
	StateManager.SetSetting("TagesLactose",0)
	StateManager.SetSetting("TagesLutein",0)
	StateManager.SetSetting("TagesMagnesium",0)
	StateManager.SetSetting("TagesMaltose",0)
	StateManager.SetSetting("TagesMangan",0)
	StateManager.SetSetting("TagesMolybdän",0)
	StateManager.SetSetting("TagesNatrium",0)
	StateManager.SetSetting("TagesNickel",0)
	StateManager.SetSetting("TagesNitrat",0)
	StateManager.SetSetting("TagesNitrit",0)
	StateManager.SetSetting("TagesOmega3Fett",0)
	StateManager.SetSetting("TagesOmega6Fett",0)
	StateManager.SetSetting("TagesPhospholipide",0)
	StateManager.SetSetting("TagesPhosphor",0)
	'StateManager.SetSetting("TagesQuecksilber",0)
	StateManager.SetSetting("TagesSaccharose",0)
	StateManager.SetSetting("TagesSelen",0)
	StateManager.SetSetting("TagesSilizium",0)
	StateManager.SetSetting("TagesStickstoff",0)
	StateManager.SetSetting("TagesSulfat",0)
	StateManager.SetSetting("TagesVARetinol",0)
	StateManager.SetSetting("TagesVB1Thiamin",0)
	StateManager.SetSetting("TagesVB2Riboflaffin",0)
	StateManager.SetSetting("TagesVB3Niacin",0)
	StateManager.SetSetting("TagesVB5Pantothensäure",0)
	StateManager.SetSetting("TagesVB6Pyridoxin",0)
	StateManager.SetSetting("TagesVB7Biotin",0)
	StateManager.SetSetting("TagesVB9Folsäure",0)
	StateManager.SetSetting("TagesVB12Kobalamin",0)
	StateManager.SetSetting("TagesVCAscorbinsäure",0)
	StateManager.SetSetting("TagesVDCholekalziferol",0)
	StateManager.SetSetting("TagesVETokopherol",0)
	StateManager.SetSetting("TagesVKPhyllochinon",0)
	StateManager.SetSetting("TagesZink",0)
	'StateManager.SetSetting("TagesHilfe2",0)
	StateManager.SetSetting("TagesaLinolensäure",0)
	StateManager.SetSetting("TagesIsoleucin",0)
	StateManager.SetSetting("TagesLeucin",0)
	StateManager.SetSetting("TagesLysin",0)
	StateManager.SetSetting("TagesMethionin",0)
	StateManager.SetSetting("TagesPhenylalanin",0)
	StateManager.SetSetting("TagesThreonin",0)
	StateManager.SetSetting("TagesTryptophan",0)
	StateManager.SetSetting("TagesValin",0)
	StateManager.SetSetting("TagesArginin",0)
	StateManager.SetSetting("TagesCrysin",0)
	StateManager.SetSetting("TagesHistidin",0)
	StateManager.SetSetting("TagesTyrosin",0)
	StateManager.SetSetting("TagesAlanin",0)
	StateManager.SetSetting("TagesAsparaginsäure",0)
	StateManager.SetSetting("TagesGlutaminsäure",0)
	StateManager.SetSetting("TagesGlysin",0)
	StateManager.SetSetting("TagesProlin",0)
	StateManager.SetSetting("TagesSerin",0)
	StateManager.SetSetting("TagesMeistInhalt",0)
	'StateManager.SetSetting("TagesHilfe3",0)

	StateManager.SaveState(Activity,"Lebensmittel")
	StateManager.SaveSettings






End Sub


Sub Activity_KeyPress (KeyCode As Int) As Boolean                            
    Dim Antw As Int
	
	If KeyCode = KeyCodes.KEYCODE_BACK Then ' Hardware-Zurück Taste gedrückt
        
		If Label1.Visible = True  Then
			Antw = Msgbox2("Wollen Sie ihre Tagesdaten abspeichern?", "A C H T U N G", "Ja", "", "Nein, Tagesabschluss",Null)
			If Antw = DialogResponse.POSITIVE Then
				TageswerteSpeichern
				Activity.Finish
			Else
				Label2.Text = " Neuer Tag"'agesaufnahme: " &Gesamtgewicht &"g"& CRLF & " Kcal: "& Tageskalorien & "  EW: " & TagesEiweiss & CRLF &" FE: " & TagesFett & "  KH: " & TagesKohlenhydrate & "  WA: "& TagesWasser
				Tageswerte0Setzen
				Activity.Finish
			End If
		Else
    		Return False
    	End If 
	End If 
	
	
	
	
End Sub


Sub Label1_Click
	
End Sub
Sub Label1_LongClick
	
End Sub
Sub EditText1_EnterPressed
	
	Dim t,g As Int
	g = Main.Gewicht * 27
	Gesamtgewicht = Gesamtgewicht + EditText1.text
	
	t = (Text7(Auswahl)/100)*EditText1.Text
	Tageskalorien = Tageskalorien + t
	
	t = (Text9(Auswahl)/100)*EditText1.Text
	TagesEiweiss = TagesEiweiss + t
	
	t = (Text10(Auswahl)/100)*EditText1.Text
	TagesFett = TagesFett + t
	
	t = (Text11(Auswahl)/100)*EditText1.Text
	TagesKohlenhydrate = TagesKohlenhydrate + t
	
	t = (Text16(Auswahl)/100)*EditText1.Text
	TagesWasser = TagesWasser + t
	
'	t = (Text1(Auswahl)/100)*EditText1.text     
'	TagesGewicht = TagesGewicht + t
'	t = (Text2(Auswahl)/100)*EditText1.text     
'	TagesLebensmittel = TagesLebensmittel + t
'	t = (Text3(Auswahl)/100)*EditText1.text     
'	TagesIDNahrung = TagesIDNahrung + t
'	t = (Text4(Auswahl)/100)*EditText1.text     
	TagesPreis = TagesPreis + t
	t = (Text5(Auswahl)/100)*EditText1.text     
	'TagesCode = TagesCode + t
'	t = (Text6(Auswahl)/100)*EditText1.text     
'	TagesGruppe = TagesGruppe + t
'	t = (Text7(Auswahl)/100)*EditText1.text     
'	Tageskcal = Tageskcal + t
'	t = (Text8(Auswahl)/100)*EditText1.text     
'	TageskJ = TageskJ + t
'	t = (Text9(Auswahl)/100)*EditText1.text     
'	TagesEW = TagesEW + t
'	t = (Text10(Auswahl)/100)*EditText1.text     
'	TagesFett = TagesFett + t
'	t = (Text11(Auswahl)/100)*EditText1.text     
'	TagesKH = TagesKH + t
	t = (Text12(Auswahl)/100)*EditText1.text     
	TagesCol = TagesCol + t
'	t = (Text13(Auswahl)/100)*EditText1.text     
'	TagesBE = TagesBE + t
'	t = (Text14(Auswahl)/100)*EditText1.text     
'	TagesPfl = TagesPfl + t
'	t = (Text15(Auswahl)/100)*EditText1.text     
'	TagesFavorit = TagesFavorit + t
'	t = (Text16(Auswahl)/100)*EditText1.text     
'	TagesWasser0 = TagesWasser0 + t
''	t = (Text17(Auswahl)/100)*EditText1.text     
'	TagesAufgenommeneMenge = TagesAufgenommeneMenge + t
'	t = (Text18(Auswahl)/100)*EditText1.text     
'	TagesHilfe0 = TagesHilfe0 + t
'	t = (Text19(Auswahl)/100)*EditText1.text     
'	TagesTkcal = TagesTkcal + t
'	t = (Text20(Auswahl)/100)*EditText1.text     
'	TagesTKJ = TagesTKJ + t
'	t = (Text21(Auswahl)/100)*EditText1.text     
'	TagesTEW = TagesTEW + t
'	t = (Text22(Auswahl)/100)*EditText1.text     
'	TagesTFett = TagesTFett + t
'	t = (Text23(Auswahl)/100)*EditText1.text     
'	TagesTKH = TagesTKH + t
'	t = (Text24(Auswahl)/100)*EditText1.text     
'	TagesTCol = TagesTCol + t
'	t = (Text25(Auswahl)/100)*EditText1.text     
'	TagesTBE = TagesTBE + t
'	t = (Text26(Auswahl)/100)*EditText1.text     
'	TagesTTier = TagesTTier + t
'	t = (Text27(Auswahl)/100)*EditText1.text     
'	TagesTPflanz = TagesTPflanz + t
'	t = (Text28(Auswahl)/100)*EditText1.text     
'	TagesTPreis = TagesTPreis + t
'	t = (Text29(Auswahl)/100)*EditText1.text     
'	TagesTH²O = TagesTH²O + t
'	t = (Text30(Auswahl)/100)*EditText1.text     
'	TagesWasser1 = TagesWasser1 + t
'	t = (Text31(Auswahl)/100)*EditText1.text     
'	TagesTEWPfl = TagesTEWPfl + t
'	t = (Text32(Auswahl)/100)*EditText1.text     
'	TagesTEWTier = TagesTEWTier + t
'	t = (Text33(Auswahl)/100)*EditText1.text     
'	TagesBestLM = TagesBestLM + t
'	t = (Text34(Auswahl)/100)*EditText1.text     
'	TagesHilfe1 = TagesHilfe1 + t
	t = (Text35(Auswahl)/100)*EditText1.text     
	TagesAlkohol = TagesAlkohol + t
'	t = (Text36(Auswahl)/100)*EditText1.text     
'	TagesAluminium = TagesAluminium + t
'	t = (Text37(Auswahl)/100)*EditText1.text     
'	TagesAmmonium = TagesAmmonium + t
'	t = (Text38(Auswahl)/100)*EditText1.text     
'	TagesAntimon = TagesAntimon + t
'	t = (Text39(Auswahl)/100)*EditText1.text     
'	TagesArsen = TagesArsen + t
'	t = (Text40(Auswahl)/100)*EditText1.text     
''	TagesBallaststoffe = TagesBallaststoffe + t
'	t = (Text41(Auswahl)/100)*EditText1.text     
'	TagesBlei = TagesBlei + t
'	t = (Text42(Auswahl)/100)*EditText1.text     
'	TagesBor = TagesBor + t
'	t = (Text43(Auswahl)/100)*EditText1.text     
'	TagesCadmium = TagesCadmium + t
	t = (Text44(Auswahl)/100)*EditText1.text     
	TagesCarotin = TagesCarotin + t
	t = (Text45(Auswahl)/100)*EditText1.text     
	TagesChlorid = TagesChlorid + t
	t = (Text46(Auswahl)/100)*EditText1.text     
	TagesChrom = TagesChrom + t
'	t = (Text47(Auswahl)/100)*EditText1.text     
'	TagesCyanid = TagesCyanid + t
	t = (Text48(Auswahl)/100)*EditText1.text     
	TagesEisen = TagesEisen + t
	t = (Text49(Auswahl)/100)*EditText1.text     
	TagesEphidrin = TagesEphidrin + t
	t = (Text50(Auswahl)/100)*EditText1.text     
	TagesmuFettsäuren = TagesmuFettsäuren + t
	t = (Text51(Auswahl)/100)*EditText1.text     
	TagesFluor = TagesFluor + t
	t = (Text52(Auswahl)/100)*EditText1.text     
	TagesFructose = TagesFructose + t
	t = (Text53(Auswahl)/100)*EditText1.text     
	TagesGlucose = TagesGlucose + t
	t = (Text54(Auswahl)/100)*EditText1.text     
	TagesHarnsäure = TagesHarnsäure + t
	t = (Text55(Auswahl)/100)*EditText1.text     
	TagesJod = TagesJod + t
	t = (Text56(Auswahl)/100)*EditText1.text     
	TagesKalium = TagesKalium + t
	t = (Text57(Auswahl)/100)*EditText1.text     
	TagesKalzium = TagesKalzium + t
	t = (Text58(Auswahl)/100)*EditText1.text     
	TagesKoffein = TagesKoffein + t
'	t = (Text59(Auswahl)/100)*EditText1.text     
'	TagesKohlenwasserstoffe = TagesKohlenwasserstoffe + t
	t = (Text60(Auswahl)/100)*EditText1.text     
	TagesKupfer = TagesKupfer + t
	t = (Text61(Auswahl)/100)*EditText1.text     
	TagesLactose = TagesLactose + t
	t = (Text62(Auswahl)/100)*EditText1.text     
	TagesLutein = TagesLutein + t
	t = (Text63(Auswahl)/100)*EditText1.text     
	TagesMagnesium = TagesMagnesium + t
	t = (Text64(Auswahl)/100)*EditText1.text     
	TagesMaltose = TagesMaltose + t
	t = (Text65(Auswahl)/100)*EditText1.text     
	TagesMangan = TagesMangan + t
	t = (Text66(Auswahl)/100)*EditText1.text     
	TagesMolybdän = TagesMolybdän + t
	t = (Text67(Auswahl)/100)*EditText1.text     
	TagesNatrium = TagesNatrium + t
'	t = (Text68(Auswahl)/100)*EditText1.text     
'	TagesNickel = TagesNickel + t
	t = (Text69(Auswahl)/100)*EditText1.text     
	TagesNitrat = TagesNitrat + t
	t = (Text70(Auswahl)/100)*EditText1.text     
	TagesNitrit = TagesNitrit + t
	t = (Text71(Auswahl)/100)*EditText1.text     
	TagesOmega3Fett = TagesOmega3Fett + t
	t = (Text72(Auswahl)/100)*EditText1.text     
	TagesOmega6Fett = TagesOmega6Fett + t
	t = (Text73(Auswahl)/100)*EditText1.text     
	TagesPhospholipide = TagesPhospholipide + t
	t = (Text74(Auswahl)/100)*EditText1.text     
	TagesPhosphor = TagesPhosphor + t
'	t = (Text75(Auswahl)/100)*EditText1.text     
'	TagesQuecksilber = TagesQuecksilber + t
	t = (Text76(Auswahl)/100)*EditText1.text     
	TagesSaccharose = TagesSaccharose + t
	t = (Text77(Auswahl)/100)*EditText1.text     
	TagesSelen = TagesSelen + t
	t = (Text78(Auswahl)/100)*EditText1.text     
	TagesSilizium = TagesSilizium + t
	t = (Text79(Auswahl)/100)*EditText1.text     
	TagesStickstoff = TagesStickstoff + t
	t = (Text80(Auswahl)/100)*EditText1.text     
	TagesSulfat = TagesSulfat + t
	t = (Text81(Auswahl)/100)*EditText1.text     
	TagesVARetinol = TagesVARetinol + t
	t = (Text82(Auswahl)/100)*EditText1.text     
	TagesVB1Thiamin = TagesVB1Thiamin + t
	t = (Text83(Auswahl)/100)*EditText1.text     
	TagesVB2Riboflaffin = TagesVB2Riboflaffin + t
	t = (Text84(Auswahl)/100)*EditText1.text     
	TagesVB3Niacin = TagesVB3Niacin + t
	t = (Text85(Auswahl)/100)*EditText1.text     
	TagesVB5Pantothensäure = TagesVB5Pantothensäure + t
	t = (Text86(Auswahl)/100)*EditText1.text     
	TagesVB6Pyridoxin = TagesVB6Pyridoxin + t
	t = (Text87(Auswahl)/100)*EditText1.text     
	TagesVB7Biotin = TagesVB7Biotin + t
	t = (Text88(Auswahl)/100)*EditText1.text     
	TagesVB9Folsäure = TagesVB9Folsäure + t
	t = (Text89(Auswahl)/100)*EditText1.text     
	TagesVB12Kobalamin = TagesVB12Kobalamin + t
	t = (Text90(Auswahl)/100)*EditText1.text     
	TagesVCAscorbinsäure = TagesVCAscorbinsäure + t
	t = (Text91(Auswahl)/100)*EditText1.text     
	TagesVDCholekalziferol = TagesVDCholekalziferol + t
	t = (Text92(Auswahl)/100)*EditText1.text     
	TagesVETokopherol = TagesVETokopherol + t
	t = (Text93(Auswahl)/100)*EditText1.text     
	TagesVKPhyllochinon = TagesVKPhyllochinon + t
	t = (Text94(Auswahl)/100)*EditText1.text     
	TagesZink = TagesZink + t
'	t = (Text95(Auswahl)/100)*EditText1.text     
'	TagesHilfe2 = TagesHilfe2 + t
	t = (Text96(Auswahl)/100)*EditText1.text     
	TagesaLinolensäure = TagesaLinolensäure + t
	t = (Text97(Auswahl)/100)*EditText1.text     
	TagesIsoleucin = TagesIsoleucin + t
	t = (Text98(Auswahl)/100)*EditText1.text     
	TagesLeucin = TagesLeucin + t
	t = (Text99(Auswahl)/100)*EditText1.text     
	TagesLysin = TagesLysin + t
	t = (Text100(Auswahl)/100)*EditText1.text     
	TagesMethionin = TagesMethionin + t
	t = (Text101(Auswahl)/100)*EditText1.text     
	TagesPhenylalanin = TagesPhenylalanin + t
	t = (Text102(Auswahl)/100)*EditText1.text     
	TagesThreonin = TagesThreonin + t
	t = (Text103(Auswahl)/100)*EditText1.text     
	TagesTryptophan = TagesTryptophan + t
	t = (Text104(Auswahl)/100)*EditText1.text     
	TagesValin = TagesValin + t
	t = (Text105(Auswahl)/100)*EditText1.text     
	TagesArginin = TagesArginin + t
	t = (Text106(Auswahl)/100)*EditText1.text     
	TagesCrysin = TagesCrysin + t
	t = (Text107(Auswahl)/100)*EditText1.text     
	TagesHistidin = TagesHistidin + t
	t = (Text108(Auswahl)/100)*EditText1.text     
	TagesTyrosin = TagesTyrosin + t
	t = (Text109(Auswahl)/100)*EditText1.text     
	TagesAlanin = TagesAlanin + t
	t = (Text110(Auswahl)/100)*EditText1.text     
	TagesAsparaginsäure = TagesAsparaginsäure + t
	t = (Text111(Auswahl)/100)*EditText1.text     
	TagesGlutaminsäure = TagesGlutaminsäure + t
	t = (Text112(Auswahl)/100)*EditText1.text     
	TagesGlysin = TagesGlysin + t
	t = (Text113(Auswahl)/100)*EditText1.text     
	TagesProlin = TagesProlin + t
	t = (Text114(Auswahl)/100)*EditText1.text     
	TagesSerin = TagesSerin + t
'	t = (Text115(Auswahl)/100)*EditText1.text     
'	TagesMeistInhalt = TagesMeistInhalt + t
'	t = (Text116(Auswahl)/100)*EditText1.text     
'	TagesHilfe3 = TagesHilfe3 + t

	
	If Tageskalorien > g Then 
		Label2.Color = Colors.Red
		Label2.TextColor = Colors.Black
	Else
		Label2.Color = Colors.Black
		Label2.TextColor = Colors.Green
	End If
	
	Label2.Text = " Tagesaufnahme: " & Gesamtgewicht & " g" & CRLF & " Kcal: " & Tageskalorien & "/" &g& "  EW: " & TagesEiweiss & CRLF &" F: " & TagesFett & "  KH: " & TagesKohlenhydrate & "  WA: "& TagesWasser

	'Text1(Auswahl) = Text1(Auswahl) + EditText1.Text
	EditText1.Text = ""
	EditText1.Visible = False
	Dim p As Phone 
	p.HideKeyboard(Activity)
	
	TageswerteSpeichern

End Sub
Sub EditText1_FocusChanged (HasFocus As Boolean)
	
End Sub
Sub Label2_Click
	
End Sub
Sub Label2_LongClick
	
End Sub
Sub ListView1_ItemClick (Position As Int, Value As Object)
	EditText1.Visible = True
	'Activity.Title = Value
	Auswahl =  Value' - 1
	'Msgbox(Value,"listviewauswahl")
	If Text2(Auswahl).Length < 16 Then
	  	Label1.TextSize = 40
	Else
	  	Label1.TextSize = 20
		
	End If
	
	Label1.Gravity = Gravity.CENTER
	Label1.Text = Text2(Auswahl)' &CRLF&"kCal: " & Text7(i) & "   E: "&Text9(i)& "   FE: " & Text10(i) & "   K: " & Text11(i) & " WA: " & Text16(i)
	  
	TageswerteSpeichern
	  
	  
End Sub
Sub ListView1_ItemLongClick (Position As Int, Value As Object)
	Dim Antw As Int
	Antw = Msgbox2("Dieses Lebensmittel als Favoriten behandeln?","FAVORIT?","Ja","","Nein",Null)
	If Antw = DialogResponse.POSITIVE Then
		
	Else

	End If
End Sub
Sub Button1_Click
	
End Sub
Sub Button1_LongClick
	
	
End Sub