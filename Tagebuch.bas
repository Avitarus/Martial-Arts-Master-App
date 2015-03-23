Type=Activity
Version=3
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: true
	#IncludeTitle: false
#End Region


'xml DAtei zusatz: 
'AddPermission(android.permission.CHANGE_WIFI_STATE)
'SetActivityAttribute(main, android:windowSoftInputMode, "stateVisible|adjustResize")



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
	Dim svAllgemein, svNahrung, svMedizin, svAntropometrie As ScrollView
	Dim VMA,VMN,VMM, VML As ViewMgr
	Dim dd As DateDialog
	Dim Tabhost1 As TabHost
	Dim Panel1 As Panel
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	
	
	'Activity.LoadLayout("Tagebuch")
    Dim bmp1, bmp2 As Bitmap
    bmp1 = LoadBitmap(File.DirAssets, "package-x-generic.png")
    bmp2 = LoadBitmap(File.DirAssets, "package-x-generic-2.png")
    Tabhost1.Initialize("Tabhost1")
	
   ' Tabhost1.AddTabWithIcon ("Name0", bmp1, bmp2, "TagebuchAllgemein") 'load the layout file of each page
'	Tabhost1.AddTabWithIcon ("Name1", bmp1, bmp2, "page1")
'	Tabhost1.AddTabWithIcon ("Name2", bmp1, bmp2, "page1")
'	Tabhost1.AddTabWithIcon ("Name3", bmp1, bmp2, "page1")
'	Tabhost1.AddTabWithIcon ("Name4", bmp1, bmp2, "page1")
'	
'	
'	Tabhost1.AddTabWithIcon ("Name", bmp1, bmp2, "page1")
	
	Panel1.Initialize("www")
	
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
	Activity.Title = "Diary"
	svAllgemein.Initialize(Activity.Height)
	svNahrung.Initialize(Activity.Height -20%y)
	svMedizin.Initialize(Activity.Height -20%y)
	svAntropometrie.Initialize(Activity.Height -20%y)
	'---------------------------------
	'War Activity
	'pgFrame.SetLayout(0, 0, Activity.Width, Activity.Height)
	'Activity.AddView(pgFrame, 0, 0, Activity.Width, Activity.Height)

	Tabhost1.AddTab2 ("Allgemein",svAllgemein)', 0, 0, Activity.Width, Activity.Height)
	Tabhost1.AddTab2 ("Nahrung",svNahrung)
	Tabhost1.AddTab2 ("Medizin",svMedizin)
	Tabhost1.AddTab2 ("Antropometrie",svAntropometrie)
	
	
	Activity.AddView(Tabhost1,0, 0, Activity.Width, Activity.Height)
	'--------------------------------------------------------
	VMA.Initialize(svAllgemein.Panel, Activity.Width, True, True, True)
	VMN.Initialize(svNahrung.Panel, Activity.Width, True, True, True)
	VMM.Initialize(svMedizin.Panel, Activity.Width, True, True, True)
	VML.Initialize(svAntropometrie.Panel, Activity.Width, True, True, True)
	'Tabhost1.AddTab2 ("Nahrung",VMA)
	
	VMA.Padding = 8dip
	VMN.Padding = 8dip
	VMM.Padding = 8dip
	VML.Padding = 8dip
	
	
	Dim List1,list2,List3, List4, List5, List6 As List
		List1.Initialize
		list2.Initialize
		List3.Initialize
		List4.Initialize
		List5.Initialize
		List6.Initialize
		
		List1.AddAll(Array As String("Physisch:","Emotional:","Mental:","Mondphase:","Alter","Telefonnummern","Eltern","Notrufnummer","Privat","Adresse","Vorname","2. Vorname","Rufname","Nachname","Strasse","Hausummer","Stadt","Bundesland","Land","Schule","Rang-Gruppe","Geworbene Mitglieder","Patenschaften","Lehrgänge","Teamleiter","Ideomot. Training","Wettkämpfe","Kampfrichterliz.","Prüferlizens","Gruppenmitglied","Verein","Verband","Trainer","Vorsitzender","Telefonnummer","Kampfsportart"))
		list2.AddAll(Array As String("Körpergewicht","Körperfettanteil","Aufwach-Ruhepuls","Bettruhe","sehr leichte Tätigkeit","leichte Tätigkeit","mittelschwere Arbeit","schwere Arbeit","Training","Nährwerte","Gewichtszunahme in 30 Tage","Kilo Kalorien","Kilo Joule","Eiweiss (g)","Kohlenhydrate (g)","Fett (g)","Cholesterin","EW/kg Körpergewicht","Öl Zugabe (g)","Eiweiss (%)","Kohlenhydrate (%)","Fett (%)","Tierische Nahrung","Pflanzlich Nahrung","Tierische Eiweisse","Pflanzliche Eiweisse","Makronährstoffe (g)","Nahrung Gewicht (g)","Flüssigkeitsaufnahme","Nahrungspreis","Preis Körperfett:","Fett reicht (Tage):","Alkohol (g)","Aluminium (mg)","Amonium (mg)","Antimon (mg)","Arsen (mg)","Ballaststoffe (g)","Blei (mg)","Bor (mg)","Cadmium (mg)","Carotin (mg)","Chlorid (mg)","Chrom (µg)","Cyanid (mg)","Eisen (mg)","Ephidrin (mg)","Fettsäuren"," mehrfach ungesättigte (g)","Fluor (µg)","Fructose (g)","Glucose (g)","Harnsäure (mg)","Jod (µg)","Kalium (mg)","Kalzium (mg)","Koffein (mg)","Kohlenwasserstoffe (mg)","Kupfer (mg)","Lactose (mg)","Lutein (mg)","Magnesium (mg)","Maltose (mg)","Mangan (mg)","Molybdän (µg)","Natrium (mg)","Nickel (mg)","Nitrat (mg)","Nitrit (mg)","Omega-3-Fett (g)","Omega-6-Fett (g)","Phospholipide (g)","Phosphor (mg)","Quecksilber (mg)","Saccharose (g)","Selen (µg)","Silizium (mg)","Stickstoff (mg)","Sulfat (mg)","Vitamin A -Retinol (µg)","Vitamin B 01-Thiamin (mg)","Vitamin B 02-Riboflavin(mg)","Vitamin B 03-Niazin (mg)","Vitamin B 05-Pantothensäure (µg)","Vitamin B 06-Pyridoxin (mg)","Vitamin B 07-Biotin (µg)","Vitamin B 09-Folsäure (µg)","Vitamin B 12)Kobalamin (µg)","Vitamin C-Ascorbinsäure (mg)","Vitamin D Cholekalziferol (µg)","Vitamin E Tokopherol (mg)","Vitamin K Phyllochinon (µg)","Zink (mg)","a-Linolensäure","Isoleucin","Leucin","Lysin","Methionin","Phenylalanin","Threonin","Tryptophan","Valin","Arginin","Cystein","Histidin","Tyrosin","Alanin","Asparaginsäure","Glutaminsäure","Glycin","Proli9n","Serin","Grundumsatz In kcal:","Arbeitsumsatz In kcal:","Maximalumsatz zum Muskelaufbau","Minimalumsatz für den Waschbrettbauch"))
		List3.AddAll(Array As String("Krankheit","Art","Grad","Ort","Beginn","Ende","Arzt:","Trainingsbeginn","Wettkampfbeginn","Verletzung","Art","Körperteil","Grad","Beginn","Ende","Arztname","Trainingsbeginn","Wettkampfbeginn","Therapie","Erneute Wettkampfteilname","Gesundheitskontrolle","Erneute Trainingsaufnahme","Antidoping Test","Schmerztagebuch","Organ","Folgen","Seite","Körperteil","Auswirkungen","Seite","Schmerzart","Zeit","Verursacher","Bemerkungen","Blutbild","Labor","Arzt","Arzt Adresse"))
		List6.AddAll(Array As String("Leukozyten In Tsd/µl","Erythrozyten In Mio/µl","Hämoglobin In g/dl","Hämatokrit In %","MCV (mittl. Ery","Vol In fl","MCH (HbE In pg/Ery)","MCHC (mittl.Hb-Konz In ´-g/dl)","Thrombozyten In Tsd/µl","Neutrophile (CD In ´-%)","Lymphozyten (CD In ´+%)","Bas.Gran. (CD In %)","Eos.Gran. (CD In %)","Monozyten (CD In %)","Gamma-GT 37°C In U/l)","GPT 37°C In ´+U/l","Alk.Phosphatase 37°C In U/l","Serumglucose In mg/dl","Kreatinin In mg/dl","Harnsäure In ´+mg/dl","Kalium In ´+mmol/l","Serumeisen In ug/dl","Cholesterin gesamt In ´+mg/dl","Triglyceride In mg/dl","FreiesTrijodthyronin i.S. In pg/ml","FreiesThyroxin i.S. (CLIA In pmol/l)","Basal i.S.","Basal i.S. (CLIA In µIU/ml)                ","Basal i.S. (CLIA In µIU/ml) ","Quick In %","INR-Wert In ","PTT In s","Natrium In mmol/l","Cholinesterase 37°C In kU/l","Blutzucker im Serum In mg/dl","HDL-geb Colesterin In mg/dl","Blutdruck Systolisch In mmHg","Blutdruck Diastolisch In mmHg","Magnesium In mmol/l","CRP quantitativ In mg/l","RF-IgM quantitativ In IU/mi","Calzium In mmol/l","Cholesterin/HDL-Cholest. In" ))
		List4.AddAll(Array As String("Körpergröße","Unterarm l.","Unterarm r.","Oberarm l.","Oberarm r.","Hals (unter Kehle","Schulterumfang","Brustumfang ","Taille (unter Rippen)","Bauch (Nabelhöhe)","Hüfte ","Oberschenkel l.","Oberschenkel r.","Wade l.","Wade r.","Brustfalte","Schulterfalte","Achselfalte","Trizepsfalte","Bauchfalte","Hüftfalte","Oberschenkelfalte","Summe der 7 Falten","Fettgehalt lt. 7 Falten","Gewicht ohne Fett","Knochenmasse","Wasseranteil","Organmasse","Muskelmasse","Körperfett In kg laut Waage","US Navy Fettrechner %","Fettgehalt Durchschnitt All:","Handgelenk r.","Taille /Hüfte Quotient.","Waist-To-Hip-Ratio (WHR)","Bankdrücken Max:","Links-rechts-Symmetrie","Links-rechts-Symmetrie","Oben-Unten-Symmetrie","Oben-Unten-Symmetrie","","Sehtest","Hörtest"))
		List5.AddAll(Array As String("1.89","36.564","36.564","44.022","44.022","46.86","146.322","122.364","93.72","91.608","110.022","66","66","44.022","44.022","10","10","10","10","10","10","10","70","12","9058785241343","19.074","0.851829634073185","normales Herzinfarkt- risiko","77","1","Super"," Ideal","17","Super"," Ideal","","","","","","","",""))

		For i = 0 To List1.Size - 1
		'	VM.AddLabel(-2, -1, 1, 1, List1.Get(i), 18, Colors.Black, List1.Get(i))	
		'	VM.AddTextBox(-2, -1, -1, 1, List1.Get(i), ", 18, List1.Get(i), false, vm.datatype_num, "", vm.actionbtn_done, "", me, 0, 5, null)

			VMA.AddLabel(VMA.Padding, -2, 1, 1, List1.Get(i), 18, Colors.Black, List1.Get(i))
			VMA.AddTextBox(40%x, -1, -90, 9%y, "ZU", "A"&i, 18, "SOLL", False, VMA.DataType_Uppercase_Words, "", VMA.ActionBtn_Next, "", Me, 0, 15, Null)
			'VMA.AddTextBox(-2, -1, -1, 1, "IST", "Ai"&i, 18, "IST", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 5, Null)
			VMA.Enabled("Zu", False)
		
		Next
			
'			VMA.AddLabel(VMA.Padding, -2, 1, 1, "Gewicht nach 0T:", 18, Colors.Black, "Gewicht")
'			VMA.AddTextBox(60%x, -1, -50, 1, "Kasten2", "", 18, "Apt", False, VMA.DataType_Uppercase_Words, "", VMA.ActionBtn_Next, "", Me, 0, 15, Null)
'			VMA.AddLabel(VMA.Padding, -1, 1, 1, "First Name:", 18, Colors.Black, "FirstName")
'			VMA.AddTextBox(140dip, -1, -1, 1, "FirstName", "", 18, "First Name", False, VMA.DataType_Text, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)
'			VMA.AddLabel(VMA.Padding, -2, 1, 1, "Middle Name:", 18, Colors.Black, "MiddleName")
'			VMA.AddTextBox(140dip, -1, -1, 1, "MiddleName", "", 18, "Middle Name", False, VMA.DataType_Text, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)
'			VMA.AddLabel(VMA.Padding, -2, 1, 1, "Last Name:", 18, Colors.Black, "LastName")
'			VMA.AddTextBox(140dip, -1, -1, 1, "LastName", "", 18, "Last Name", False, VMA.DataType_Text, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)

		
		
'		
'			
'			VMA.AddComboBox(77dip, -1, -33, 1, "Ziel", "Wähle ein Ziel", File.ReadList(File.DirAssets, "Ziele.txt"), 27, "", Me, 2)
	
'
'		VMA.AddLabel(VMA.Padding, -2, 1, 1, "Modus:", 18, Colors.Black, "Modus")	
'		VMA.AddComboBox(77dip, -1, -33, 1, "Modus", "Wähle einen Modus", File.ReadList(File.DirAssets, "Modus.txt"), 27, "", Me, 2)
'
'		VMA.AddLabel(VMA.Padding, -2, 1, 1, "Gürtel:", 18, Colors.Black, "Gürtel")	
'		VMA.AddComboBox(77dip, -1, -33, 1, "Gürtel", "Wähle einen Gürtel", File.ReadList(File.DirAssets, "Gürtel.txt"), 27, "", Me, 2)
'
'		VMA.AddLabel(VMA.Padding, -2, 1, 1, "Meditationen", 18, Colors.Black, "Meditationen")	
'		VMA.AddComboBox(77dip, -1, -33, 1, "Meditation", "Wähle eine Meditation", File.ReadList(File.DirAssets, "Meditation.txt"), 27, "", Me, 2)
'		

'------------------------------------
		
		For i = 0 To list2.Size - 1

			VMN.AddLabel(VMN.Padding, -2, 1, 1, list2.Get(i), 18, Colors.Black, list2.Get(i))
			VMN.AddTextBox(60%x, -1, -40, 9%y, "ZU", "", 18, i, False, VMN.DataType_Uppercase_Words, "", VMN.ActionBtn_Next, "", Me, 0, 5, Null)
			VMN.AddTextBox(-2, -1, -1, 9%y, "IST", "", 18, i, False, VMN.DataType_Num, "", VMN.ActionBtn_Next, "", Me, 0, 5, Null)
			VMN.Enabled("Zu" & 1, False)
		



'
'			VMN.AddLabel(-2, -1, 1, 1, list2.Get(i), 18, Colors.Black, list2.Get(i))
'			VMN.AddTextBox(140dip,-2, -1, -1, 1, list2.Get(i), "", 18, List1.Get(i), False, VMN.datatype_num, "", VMN.actionbtn_done, "", Me, 0, 5, Null)
			'VMN.AddLabel( -2, 1, 1, "Alias:", 18, Colors.Black, "Alias")
			'VMN.AddTextBox( -1, -1, 1, "Alias", "", 18, "Alias", False, VMN.DataType_Text, "", VMN.ActionBtn_Next, "", Me, 0, 30, Null)

		Next
''--------------------------------------


		For i = 0 To List3.Size - 1
		 'Dim text As string
		 	VMM.AddLabel(VMM.Padding, -2, 1, 1, List3.Get(i), 18, Colors.Black, List3.Get(i))
			VMM.AddTextBox(60%x, -1, -60, 9%y, "ZU", "", 18, "Soll"& i, True, VMM.DataType_Uppercase_Words, "", VMM.ActionBtn_Next, "", Me, 0, 100, Null)
			'VMM.AddTextBox(-2, -1, -1, 1, "IST", "", 18, "IST", False, VMM.DataType_Num, "", VMM.ActionBtn_Next, "", Me, 0, 5, Null)
			VMM.Enabled("Zu" & 1, False)
		
		 'bei 70 stadt 80 scrollt basal
		 
		 
		 
'			VMM.AddLabel(VMM.Padding, -2, 1, 1, List3.Get(i), 18, Colors.Black, List3.Get(i))	
'			VMM.AddTextBox(77dip, -1, -50, 1, List3.Get(i), "", 18, List3.Get(i), False, VMM.DataType_Uppercase_Words, "", VMM.ActionBtn_Next, "", Me, 0, 15, Null)	
'			VMM.AddTextBox(-2, -1, -1, 1, List3.Get(i), "", 18, List3.Get(i), False, VMM.DataType_Num, "", VMM.ActionBtn_Done, "", Me, 0, 5, Null) 
		Next

'------------------------------------
		For i = 0 To List6.Size - 1
		 'Dim text As string
		 	VMM.AddLabel(VMM.Padding, -2, 1, 1, List6.Get(i), 18, Colors.Black, List6.Get(i))
			VMM.AddTextBox(60%x, -1, -40, 1, "ZU", "", 18, i , True, VMM.DataType_Num, "", VMM.ActionBtn_Next, "", Me, 0, 100, Null)
			VMM.AddTextBox(-2, -1, -1, 9%y, "IST", "", 18, i, False, VMM.DataType_Num, "", VMM.ActionBtn_Next, "", Me, 0, 5, Null)
			VMM.Enabled("Zu" & 1, False)
		
		 
		 
		 
		 
'			VMM.AddLabel(VMM.Padding, -2, 1, 1, List3.Get(i), 18, Colors.Black, List3.Get(i))	
'			VMM.AddTextBox(77dip, -1, -50, 1, List3.Get(i), "", 18, List3.Get(i), False, VMM.DataType_Uppercase_Words, "", VMM.ActionBtn_Next, "", Me, 0, 15, Null)	
'			VMM.AddTextBox(-2, -1, -1, 1, List3.Get(i), "", 18, List3.Get(i), False, VMM.DataType_Num, "", VMM.ActionBtn_Done, "", Me, 0, 5, Null) 
		Next

'------------------------------------	

		
		For i = 0 To List4.Size - 1
'			 'Dim text As string
'				VML.AddLabel(VML.Padding, -2, 1, 1, List4.Get(i), 18, Colors.Black, List4.Get(i))	
'				VML.AddTextBox(77dip, -1, -50, 1, List5.Get(i), "", 18, List5.Get(i), False, VML.DataType_Uppercase_Words, "", VML.ActionBtn_Next, "", Me, 0, 15, Null)	
'				VML.AddTextBox(-2, -1, -1, 1, List5.Get(i), "", 18, "Krankheit", False, VML.DataType_Num, "", VML.ActionBtn_Done, "", Me, 0, 5, Null) 

			VML.AddLabel(VML.Padding, -2, 1, 1, List4.Get(i), 18, Colors.Black, List4.Get(i))
			VML.AddTextBox(60%x, -1, -40, 5%y, List5.Get(i), "", 18, List5.Get(i), False, VML.DataType_Uppercase_Words, "", VML.ActionBtn_Next, "", Me, 0, 15, Null)
			VML.AddTextBox(-2, -1, -1, 5%y, "IST", "", 18, i, False, VML.DataType_Num, "", VML.ActionBtn_Next, "", Me, 0, 5, Null)
			VML.Enabled("Zu" & 1, False)

		Next










'
'	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Gewicht nach 30T:", 18, Colors.Black, "Gewicht")
'	VMA.AddTextBox(60%x, -1, -50, 1, "Kasten1", "", 18, "Apt", False, VMA.DataType_Uppercase_Words, "", VMA.ActionBtn_Next, "", Me, 0, 15, Null)
'	VMA.AddTextBox(-2, -1, -1, 1, "Zip", "", 18, "Zip", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 5, Null)
	VMA.Enabled("Kasten" & 1, False)
	
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Gewicht nach 30:", 18, Colors.Black, "Gewich")
	VMA.AddTextBox(60%x, -1, -50, 1, "Kaste1", "", 18, "Aptr", False, VMA.DataType_Uppercase_Words, "", VMA.ActionBtn_Next, "", Me, 0, 15, Null)
	VMA.AddTextBox(-2, -1, -1, 1, "Zip", "", 18, "Zipr", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 5, Null)
	VMA.Enabled("Kaste1" & 1, False)
	
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Gewicht nach 0T:", 18, Colors.Black, "Gewicht")
	VMA.AddTextBox(60%x, -1, -50, 1, "Kasten2", "", 18, "Apt", False, VMA.DataType_Uppercase_Words, "", VMA.ActionBtn_Next, "", Me, 0, 15, Null)
	VMA.AddTextBox(-2, -1, -1, 1, "Zip", "", 18, "Zip", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 5, Null)
	VMA.Enabled("Kasten" & 2, False)
'	
	VMA.AddLabel(VMA.Padding, -1, 1, 1, "First Name:", 18, Colors.Black, "FirstName")
	VMA.AddTextBox(140dip, -1, -1, 1, "FirstName", "", 18, "First Name", False, VMA.DataType_Text, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Middle Name:", 18, Colors.Black, "MiddleName")
	VMA.AddTextBox(140dip, -1, -1, 1, "MiddleName", "", 18, "Middle Name", False, VMA.DataType_Text, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Last Name:", 18, Colors.Black, "LastName")
	VMA.AddTextBox(140dip, -1, -1, 1, "LastName", "", 18, "Last Name", False, VMA.DataType_Text, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Alias:", 18, Colors.Black, "Alias")
	VMA.AddTextBox(140dip, -1, -1, 1, "Alias", "", 18, "Alias", False, VMA.DataType_Text, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)
	
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "DOB:", 18, Colors.Black, "DOB")
	VMA.AddTextBox(140dip, -1, -50, 1, "DOB", DateText, 18, "", False, 0, "",0, "", Me, 0, 10, Null)
	VMA.Enabled("DOB", False)
	VMA.AddButton(-2, -1, -1, 1, "DateBtn", "Set Date", 16, Colors.Black, "Get_Date", Me)
	
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Hair Color:", 18, Colors.Black, "Hair")
	VMA.AddComboBox(140dip, -1, -1, 1, "Hair", "Select Hair Color", File.ReadList(File.DirAssets, "partyhair.txt"), 18, Colors.Black, "", Me, 20)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Eye Color:", 18, Colors.Black, "Eyes")
	VMA.AddComboBox(140dip, -1, -1, 1, "Eyes", "Select Eye Color", File.ReadList(File.DirAssets, "partyeyes.txt"), 18, Colors.Black, "", Me, 10)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Race:", 18, Colors.Black, "Race")
	VMA.AddComboBox(140dip, -1, -1, 1, "Race", "Select Race", File.ReadList(File.DirAssets, "partyrace.txt"), 18, Colors.Black, "", Me, 20)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Sex:", 18, Colors.Black, "Sex")
	VMA.AddComboBox(140dip, -1, -1, 1, "Sex", "Select Sex", Array As String("M", "F", "X"), 18, Colors.Black, "", Me, 1)
	
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Height (Ft):", 18, Colors.Black, "HeightFt")
	VMA.AddTextBox(140dip, -1, -1, 1, "HeightFt", "", 18, "", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 2, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Height (In):", 18, Colors.Black, "HeightIn")
	VMA.AddTextBox(140dip, -1, -1, 1, "HeightIn", "", 18, "", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 2, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Weight:", 18, Colors.Black, "Weight")
	VMA.AddTextBox(140dip, -1, -1, 1, "Weight", "", 18, "", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 4, Null)
	
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Street:", 18, Colors.Black, "Street")
	VMA.AddTextBox(77dip, -1, -1, 1, "Street", "", 18, "Street", False, VMA.DataType_Uppercase_Words, "", VMA.ActionBtn_Next, "", Me, 0, 50, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Apt:", 18, Colors.Black, "Apt")
	VMA.AddTextBox(77dip, -1, -50, 1, "Apt", "", 18, "Apt", False, VMA.DataType_Uppercase_Words, "", VMA.ActionBtn_Next, "", Me, 0, 15, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "City:", 18, Colors.Black, "City")
	VMA.AddTextBox(77dip, -1, -1, 1, "City", "", 18, "City", False, VMA.DataType_Uppercase_Words, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "State:", 18, Colors.Black, "State")
	VMA.AddComboBox(77dip, -1, -33, 1, "State", "Select a State", File.ReadList(File.DirAssets, "states.txt"), 18, Colors.Black, "", Me, 2)
	VMA.AddLabel(-2, -1, 1, 1, "Zip:", 18, Colors.Black, "Zip")
	VMA.AddTextBox(-2, -1, -1, 1, "Zip", "", 18, "Zip", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 5, Null)
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Phone:", 18, Colors.Black, "Phone")
	VMA.AddTextBox(77dip, -1, -1, 1, "Phone", "", 18, "Phone", False, VMA.DataType_Num, "", VMA.ActionBtn_Next, "", Me, 0, 30, Null)
	
	VMA.AddLabel(VMA.Padding, -2, 1, 1, "Notes:", 18, Colors.Black, "Notes")
	VMA.AddTextBox(77dip, -1, -1, 25%y, "Notes", "", 18, "Notes", True, VMA.DataType_Text, "", VMA.ActionBtn_Done, "", Me, 0, 500, Null)

End Sub



Sub ListView1_ItemClick (Position As Int, Value As Object)
    Activity.Title = Value
'	
'	Select Value
'	
'	    Case 1 ' ListView1.AddSingleLine("Allgemein")'1
'			
'		
'		Case 2 ' ListView1.AddSingleLine("Nahrung")
'		
'
'			
'			
'		Case 3' ListView1.AddSingleLine("Medizin")'3
'		
'
'	
'		Case 4' ListView1.AddSingleLine("Antropometrie")
''					Dim List1, List2 As List
''			List1.Initialize
'			List2.Initialize
'			List1.AddAll(Array As String(Antropometrie,	Maße und Gewichte,	Körpergröße,	Unterarm l.,	Unterarm r.,	Oberarm l.,	Oberarm r.,	Hals (unter Kehle),	Schulterumfang,	Brustumfang ,	Taille (unter Rippen),	Bauch (Nabelhöhe),	Hüfte ,	Oberschenkel l.,	Oberschenkel r.,	Wade l.,	Wade r.,	Brustfalte,	Schulterfalte,	Achselfalte,	Trizepsfalte,	Bauchfalte,	Hüftfalte,	Oberschenkelfalte,	Summe der 7 Falten,	Fettgehalt lt. 7 Falten,	Durchschnittliches Körpergewicht ohne Fett,	Knochenmasse,	Wasseranteil,	Organmasse,	Muskelmasse,	Körperfett In kg laut Waage,	US Navy Fettrechner %,	Fettgehalt laut den 3 Rechenmethoden,	Handgelenk r.,	Taille /Hüfte Quotient.,	Waist-To-Hip-Ratio (WHR),	Bankdrücken Max:,	Links-rechts-Symmetrie,	Links-rechts-Symmetrie,	Oben-Unten-Symmetrie,	Oben-Unten-Symmetrie,	,	Sehtest,	Hörtest,
'			List2.AddAll(Array As String(Soll-Wert,	,	1,89,	36,564,	36,564,	44,022,	44,022,	46,86,	146,322,	122,364,	93,72,	91,608,	110,022,	66,	66,	44,022,	44,022,	10,	10,	10,	10,	10,	10,	10,	70,								12,9058785241343,		19,074,	0,851829634073185,	normales Herzinfarkt- risiko,	77,803605313093,	1,	Super, Ideal,	1,00099980003999,	Super, Ideal,	,	,	,
'		
'			For i = 0 To List1.Size - 1
'			 'Dim text As string
'				VM.AddLabel(VM.Padding, -2, 1, 1, List1.Get(i), 18, Colors.Black, List1.Get(i))	
'				VM.AddTextBox(77dip, -1, -50, 1, List2.Get(i), "", 18, List2.Get(i), False, VM.DataType_Uppercase_Words, "", VM.ActionBtn_Next, "", Me, 0, 15, Null)	
'				VM.AddTextBox(-2, -1, -1, 1, List2.Get(i), "", 18, "Krankheit", False, VM.DataType_Num, "", VM.ActionBtn_Done, "", Me, 0, 5, Null) 
'			Next
'		
'		Case 5' ListView1.AddSingleLine("Leistungsdiagnostik")'5
'		Case 6 ' ListView1.AddDoubleLine("Sport","BB, KArate, Kickbox,...")'6
'		Case 7 ' ListView1.AddSingleLine("Ziele")'7
'	End Select
	

	
End Sub


Sub Activity_Resume

	svAllgemein.Height = Activity.Height
	'svNahrung.Height = Activity.Height
	'svMedizin.Height = Activity.Height
	'svAntropometrie.Height = Activity.Height
	
	
	
	
	If curValues.IsInitialized Then VMA.RestoreState(curValues)
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		curValues.Clear
	Else
		curValues = VMA.SaveState
	End If
End Sub

Sub IME_HeightChanged (NewHeight As Int, OldHeight As Int)
	svAllgemein.Height = NewHeight
	svNahrung.Height = NewHeight
	svMedizin.Height = NewHeight
	svAntropometrie.Height = NewHeight
	'svAllgemein.Height = NewHeight
	
	
	
	
	
	
	
End Sub

Sub Get_Date
	If DOB = 9223372036854775807 Then dd.DateTicks= DateTime.Now Else dd.DateTicks = DOB
	If dd.Show("Please Select Incident Date", "Incident Date", "Ok", "Cancel", "", Null) = DialogResponse.POSITIVE Then
		DOB = DateTime.DateParse(NumberFormat(dd.Month, 2, 0) & "/" & NumberFormat(dd.DayOfMonth, 2, 0) & "/" & NumberFormat2(dd.Year, 4, 0, 0, False))
		VMA.SetText("DOB", DateTime.Date(DOB))
	Else
		If Msgbox2("Do you wish to set the Date to N/A?", "Clear Date", "Yes", "", "No", Null) = DialogResponse.POSITIVE Then
			DOB= 9223372036854775807
			VMA.SetText("DOB", "N/A")
		End If
	End If
End Sub
