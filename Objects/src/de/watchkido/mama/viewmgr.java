package de.watchkido.mama;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class viewmgr extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "de.watchkido.mama.viewmgr");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            if (ba.getClass().getName().endsWith("ShellBA")) {
			    ba.raiseEvent2(null, true, "CREATE", true, "de.watchkido.mama.viewmgr",
                    ba);
		    }
        }
        ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.IME _ime = null;
public anywheresoftware.b4a.objects.collections.List _myviews = null;
public anywheresoftware.b4a.objects.collections.Map _namemap = null;
public anywheresoftware.b4a.objects.ConcreteViewWrapper _curfocus = null;
public int _padding = 0;
public boolean _minmaxwarn = false;
public boolean _scaleviews = false;
public boolean _overridefontscale = false;
public float _fontscale = 0f;
public anywheresoftware.b4a.objects.LabelWrapper _scalelabel = null;
public anywheresoftware.b4a.objects.PanelWrapper _parent = null;
public int _parentwidth = 0;
public int _curx = 0;
public int _nextx = 0;
public int _cury = 0;
public int _nexty = 0;
public de.watchkido.mama.main _main = null;
public de.watchkido.mama.spiele _spiele = null;
public de.watchkido.mama.trainingkickboxen _trainingkickboxen = null;
public de.watchkido.mama.startabfragen _startabfragen = null;
public de.watchkido.mama.einstellungenapp _einstellungenapp = null;
public de.watchkido.mama.erfolgsmeldung _erfolgsmeldung = null;
public de.watchkido.mama.checkliste _checkliste = null;
public de.watchkido.mama.wettkampf _wettkampf = null;
public de.watchkido.mama.einstellungenwettkampf _einstellungenwettkampf = null;
public de.watchkido.mama.einstellungen _einstellungen = null;
public de.watchkido.mama.einstellungenah _einstellungenah = null;
public de.watchkido.mama.training _training = null;
public de.watchkido.mama.einstellungenstunde _einstellungenstunde = null;
public de.watchkido.mama.facebook _facebook = null;
public de.watchkido.mama.multipartpost _multipartpost = null;
public de.watchkido.mama.einstellungentrainingkick _einstellungentrainingkick = null;
public de.watchkido.mama.einstellungentrainingstunde _einstellungentrainingstunde = null;
public de.watchkido.mama.benachrichtigung _benachrichtigung = null;
public de.watchkido.mama.downloadservice _downloadservice = null;
public de.watchkido.mama.kampfsportlexikon _kampfsportlexikon = null;
public de.watchkido.mama.karatestunde _karatestunde = null;
public de.watchkido.mama.tts _tts = null;
public de.watchkido.mama.lebensmittel _lebensmittel = null;
public de.watchkido.mama.statemanager _statemanager = null;
public de.watchkido.mama.tagebuch _tagebuch = null;
public static class _mytextbox{
public boolean IsInitialized;
public int DataType;
public int ActionBtn;
public String ActionSub;
public Object ActionSubModule;
public int MinChar;
public int MaxChar;
public void Initialize() {
IsInitialized = true;
DataType = 0;
ActionBtn = 0;
ActionSub = "";
ActionSubModule = new Object();
MinChar = 0;
MaxChar = 0;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _myactionsub{
public boolean IsInitialized;
public String ActionSub;
public Object ActionSubModule;
public void Initialize() {
IsInitialized = true;
ActionSub = "";
ActionSubModule = new Object();
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _mycombobox{
public boolean IsInitialized;
public int CurSelection;
public anywheresoftware.b4a.objects.collections.List Items;
public String Prompt;
public String ActionSub;
public Object ActionSubModule;
public void Initialize() {
IsInitialized = true;
CurSelection = 0;
Items = new anywheresoftware.b4a.objects.collections.List();
Prompt = "";
ActionSub = "";
ActionSubModule = new Object();
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _comboitem{
public boolean IsInitialized;
public String Text;
public Object Value;
public void Initialize() {
IsInitialized = true;
Text = "";
Value = new Object();
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public int  _actionbtn_done() throws Exception{
 //BA.debugLineNum = 150;BA.debugLine="Sub ActionBtn_Done As Int";
 //BA.debugLineNum = 151;BA.debugLine="Return 6";
if (true) return (int) (6);
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return 0;
}
public int  _actionbtn_go() throws Exception{
 //BA.debugLineNum = 155;BA.debugLine="Sub ActionBtn_Go As Int";
 //BA.debugLineNum = 156;BA.debugLine="Return 2";
if (true) return (int) (2);
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return 0;
}
public int  _actionbtn_next() throws Exception{
 //BA.debugLineNum = 140;BA.debugLine="Sub ActionBtn_Next As Int";
 //BA.debugLineNum = 141;BA.debugLine="Return 5";
if (true) return (int) (5);
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return 0;
}
public int  _actionbtn_previous() throws Exception{
 //BA.debugLineNum = 145;BA.debugLine="Sub ActionBtn_Previous As Int";
 //BA.debugLineNum = 146;BA.debugLine="Return 7";
if (true) return (int) (7);
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return 0;
}
public int  _actionbtn_search() throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub ActionBtn_Search As Int";
 //BA.debugLineNum = 161;BA.debugLine="Return 3";
if (true) return (int) (3);
 //BA.debugLineNum = 162;BA.debugLine="End Sub";
return 0;
}
public int  _actionbtn_send() throws Exception{
 //BA.debugLineNum = 165;BA.debugLine="Sub ActionBtn_Send As Int";
 //BA.debugLineNum = 166;BA.debugLine="Return 4";
if (true) return (int) (4);
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return 0;
}
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper  _add2flow(anywheresoftware.b4a.objects.ConcreteViewWrapper _newview,int _left,int _top,int _width,int _height) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _viewrect = null;
 //BA.debugLineNum = 191;BA.debugLine="Sub Add2Flow(NewView As View, Left As Int, Top As Int, Width As Int, Height As Int) As Rect";
 //BA.debugLineNum = 192;BA.debugLine="Dim viewRect As Rect";
_viewrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 194;BA.debugLine="If Top = -2 Then";
if (_top==-2) { 
 //BA.debugLineNum = 195;BA.debugLine="Top = NextY";
_top = _nexty;
 }else if(_top==-1) { 
 //BA.debugLineNum = 197;BA.debugLine="Top = CurY";
_top = _cury;
 //BA.debugLineNum = 198;BA.debugLine="If Left > 0 AND Left < NextX Then Left = NextX";
if (_left>0 && _left<_nextx) { 
_left = _nextx;};
 };
 //BA.debugLineNum = 200;BA.debugLine="If Left = -2 Then";
if (_left==-2) { 
 //BA.debugLineNum = 201;BA.debugLine="Left = NextX";
_left = _nextx;
 }else if(_left==-1) { 
 //BA.debugLineNum = 203;BA.debugLine="Left = CurX";
_left = _curx;
 };
 //BA.debugLineNum = 206;BA.debugLine="If Width < 1 Then";
if (_width<1) { 
 //BA.debugLineNum = 207;BA.debugLine="If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left";
if (_width<-1 && _width>-100) { 
_width = (int) (__c.Abs(_width)*.01*(_parentwidth-_padding-_left));}
else {
_width = (int) (_parentwidth-_padding-_left);};
 };
 //BA.debugLineNum = 209;BA.debugLine="If Height < 1 Then";
if (_height<1) { 
 //BA.debugLineNum = 210;BA.debugLine="If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top";
if (_height<-1 && _height>-100) { 
_height = (int) (__c.Abs(_height)*.01*(_parent.getHeight()-_padding-_top));}
else {
_height = (int) (_parent.getHeight()-_padding-_top);};
 };
 //BA.debugLineNum = 213;BA.debugLine="Parent.AddView(NewView, Left, Top, Width, Height)";
_parent.AddView((android.view.View)(_newview.getObject()),_left,_top,_width,_height);
 //BA.debugLineNum = 215;BA.debugLine="CurX = Left";
_curx = _left;
 //BA.debugLineNum = 216;BA.debugLine="NextX = Left + NewView.Width + Padding";
_nextx = (int) (_left+_newview.getWidth()+_padding);
 //BA.debugLineNum = 217;BA.debugLine="CurY = Top";
_cury = _top;
 //BA.debugLineNum = 218;BA.debugLine="NextY = Max(NextY, Top + NewView.Height + Padding)";
_nexty = (int) (__c.Max(_nexty,_top+_newview.getHeight()+_padding));
 //BA.debugLineNum = 219;BA.debugLine="If NextY > Parent.Height Then Parent.Height = NextY";
if (_nexty>_parent.getHeight()) { 
_parent.setHeight(_nexty);};
 //BA.debugLineNum = 221;BA.debugLine="viewRect.Initialize(Left, Top, Left + NewView.Width, Top + NewView.Height)";
_viewrect.Initialize(_left,_top,(int) (_left+_newview.getWidth()),(int) (_top+_newview.getHeight()));
 //BA.debugLineNum = 222;BA.debugLine="Return viewRect";
if (true) return _viewrect;
 //BA.debugLineNum = 223;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper  _addbutton(int _left,int _top,int _width,int _height,String _name,String _text,float _textsize,int _color,String _actionsub,Object _actionsubmodule) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _mycontrol = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _viewrect = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
de.watchkido.mama.viewmgr._myactionsub _newactionsub = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _charrect = null;
 //BA.debugLineNum = 325;BA.debugLine="Sub AddButton(Left As Int, Top As Int, Width As Int, Height As Int, Name As String, Text As String, TextSize As Float, Color As Int, ActionSub As String, ActionSubModule As Object) As Rect";
 //BA.debugLineNum = 326;BA.debugLine="Dim myControl As Label";
_mycontrol = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 327;BA.debugLine="Dim viewRect As Rect";
_viewrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 328;BA.debugLine="Dim ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 329;BA.debugLine="Dim newActionSub As MyActionSub";
_newactionsub = new de.watchkido.mama.viewmgr._myactionsub();
 //BA.debugLineNum = 331;BA.debugLine="myControl.Initialize(\"Button\")";
_mycontrol.Initialize(ba,"Button");
 //BA.debugLineNum = 332;BA.debugLine="myControl.Gravity = Gravity.CENTER";
_mycontrol.setGravity(__c.Gravity.CENTER);
 //BA.debugLineNum = 333;BA.debugLine="myControl.Text = Text";
_mycontrol.setText((Object)(_text));
 //BA.debugLineNum = 334;BA.debugLine="myControl.TextColor = Color";
_mycontrol.setTextColor(_color);
 //BA.debugLineNum = 335;BA.debugLine="myControl.TextSize = TextSize";
_mycontrol.setTextSize(_textsize);
 //BA.debugLineNum = 336;BA.debugLine="myControl.Typeface = Typeface.DEFAULT_BOLD";
_mycontrol.setTypeface(__c.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 337;BA.debugLine="newActionSub.Initialize";
_newactionsub.Initialize();
 //BA.debugLineNum = 338;BA.debugLine="newActionSub.ActionSub = ActionSub";
_newactionsub.ActionSub = _actionsub;
 //BA.debugLineNum = 339;BA.debugLine="newActionSub.ActionSubModule = ActionSubModule";
_newactionsub.ActionSubModule = _actionsubmodule;
 //BA.debugLineNum = 340;BA.debugLine="myControl.Tag = newActionSub";
_mycontrol.setTag((Object)(_newactionsub));
 //BA.debugLineNum = 342;BA.debugLine="If Top = -2 Then";
if (_top==-2) { 
 //BA.debugLineNum = 343;BA.debugLine="Top = NextY";
_top = _nexty;
 }else if(_top==-1) { 
 //BA.debugLineNum = 345;BA.debugLine="Top = CurY";
_top = _cury;
 //BA.debugLineNum = 346;BA.debugLine="If Left > 0 AND Left < NextX Then Left = NextX";
if (_left>0 && _left<_nextx) { 
_left = _nextx;};
 };
 //BA.debugLineNum = 348;BA.debugLine="If Left = -2 Then";
if (_left==-2) { 
 //BA.debugLineNum = 349;BA.debugLine="Left = NextX";
_left = _nextx;
 }else if(_left==-1) { 
 //BA.debugLineNum = 351;BA.debugLine="Left = CurX";
_left = _curx;
 };
 //BA.debugLineNum = 354;BA.debugLine="If Width < 1 Then";
if (_width<1) { 
 //BA.debugLineNum = 355;BA.debugLine="If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left";
if (_width<-1 && _width>-100) { 
_width = (int) (__c.Abs(_width)*.01*(_parentwidth-_padding-_left));}
else {
_width = (int) (_parentwidth-_padding-_left);};
 };
 //BA.debugLineNum = 357;BA.debugLine="If Height < 1 Then";
if (_height<1) { 
 //BA.debugLineNum = 358;BA.debugLine="If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top";
if (_height<-1 && _height>-100) { 
_height = (int) (__c.Abs(_height)*.01*(_parent.getHeight()-_padding-_top));}
else {
_height = (int) (_parent.getHeight()-_padding-_top);};
 };
 //BA.debugLineNum = 361;BA.debugLine="If OverrideFontScale = True Then myControl.TextSize = myControl.TextSize / FontScale";
if (_overridefontscale==__c.True) { 
_mycontrol.setTextSize((float) (_mycontrol.getTextSize()/(double)_fontscale));};
 //BA.debugLineNum = 363;BA.debugLine="If ScaleViews = True Then ' Scale View to Size required for Font";
if (_scaleviews==__c.True) { 
 //BA.debugLineNum = 364;BA.debugLine="Dim charRect As Rect";
_charrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 365;BA.debugLine="charRect = GetCharSize(myControl.Typeface, myControl.TextSize, Text)";
_charrect = _getcharsize((anywheresoftware.b4a.keywords.constants.TypefaceWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.constants.TypefaceWrapper(), (android.graphics.Typeface)(_mycontrol.getTypeface())),_mycontrol.getTextSize(),_text);
 //BA.debugLineNum = 367;BA.debugLine="If Height < charRect.Bottom + 15dip Then Height = charRect.Bottom + 15dip";
if (_height<_charrect.getBottom()+__c.DipToCurrent((int) (15))) { 
_height = (int) (_charrect.getBottom()+__c.DipToCurrent((int) (15)));};
 //BA.debugLineNum = 368;BA.debugLine="If Text.Length > 0 AND Width = 1 Then ' Calculate Min Width";
if (_text.length()>0 && _width==1) { 
 //BA.debugLineNum = 369;BA.debugLine="Width = Min(charRect.Right + 15dip, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_charrect.getRight()+__c.DipToCurrent((int) (15)),_parentwidth-_padding-_left));
 }else {
 //BA.debugLineNum = 371;BA.debugLine="Width = Min(Width, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_width,_parentwidth-_padding-_left));
 };
 };
 //BA.debugLineNum = 375;BA.debugLine="Parent.AddView(myControl, Left, Top, Width, Height)";
_parent.AddView((android.view.View)(_mycontrol.getObject()),_left,_top,_width,_height);
 //BA.debugLineNum = 376;BA.debugLine="myControl.Background = SetNinePatchDrawable(\"buttonup\")";
_mycontrol.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("buttonup")));
 //BA.debugLineNum = 377;BA.debugLine="ref.Target = myControl";
_ref.Target = (Object)(_mycontrol.getObject());
 //BA.debugLineNum = 378;BA.debugLine="ref.SetOnTouchListener(\"Button_Touch\")";
_ref.SetOnTouchListener(ba,"Button_Touch");
 //BA.debugLineNum = 380;BA.debugLine="CurX = Left";
_curx = _left;
 //BA.debugLineNum = 381;BA.debugLine="NextX = Left + myControl.Width + Padding";
_nextx = (int) (_left+_mycontrol.getWidth()+_padding);
 //BA.debugLineNum = 382;BA.debugLine="CurY = Top";
_cury = _top;
 //BA.debugLineNum = 383;BA.debugLine="NextY = Max(NextY, Top + myControl.Height + Padding)";
_nexty = (int) (__c.Max(_nexty,_top+_mycontrol.getHeight()+_padding));
 //BA.debugLineNum = 384;BA.debugLine="If NextY > Parent.Height Then Parent.Height = NextY";
if (_nexty>_parent.getHeight()) { 
_parent.setHeight(_nexty);};
 //BA.debugLineNum = 385;BA.debugLine="If Name.Length > 0 Then NameMap.Put(Name, myControl)";
if (_name.length()>0) { 
_namemap.Put((Object)(_name),(Object)(_mycontrol.getObject()));};
 //BA.debugLineNum = 387;BA.debugLine="viewRect.Initialize(Left, Top, Left + myControl.Width, Top + myControl.Height)";
_viewrect.Initialize(_left,_top,(int) (_left+_mycontrol.getWidth()),(int) (_top+_mycontrol.getHeight()));
 //BA.debugLineNum = 388;BA.debugLine="Return viewRect";
if (true) return _viewrect;
 //BA.debugLineNum = 389;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper  _addcombobox(int _left,int _top,int _width,int _height,String _name,String _prompt,anywheresoftware.b4a.objects.collections.List _items,float _textsize,int _color,String _selectionchangesub,Object _selectionchangesubmodule,int _viewchar) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _mycontrol = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _viewrect = null;
de.watchkido.mama.viewmgr._mycombobox _newcombobox = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _charrect = null;
 //BA.debugLineNum = 416;BA.debugLine="Public Sub AddComboBox(Left As Int, Top As Int, Width As Int, Height As Int, Name As String, Prompt As String, Items As List, TextSize As Float, Color As Int, SelectionChangeSub As String, SelectionChangeSubModule As Object, ViewChar As Int) As Rect";
 //BA.debugLineNum = 417;BA.debugLine="Dim myControl As Label";
_mycontrol = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 418;BA.debugLine="Dim ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 419;BA.debugLine="Dim viewRect As Rect";
_viewrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 420;BA.debugLine="Dim newCombobox As MyCombobox";
_newcombobox = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 422;BA.debugLine="newCombobox.Initialize";
_newcombobox.Initialize();
 //BA.debugLineNum = 423;BA.debugLine="myControl.Initialize(\"Combobox\")";
_mycontrol.Initialize(ba,"Combobox");
 //BA.debugLineNum = 424;BA.debugLine="newCombobox.CurSelection = -1";
_newcombobox.CurSelection = (int) (-1);
 //BA.debugLineNum = 425;BA.debugLine="newCombobox.Prompt = Prompt";
_newcombobox.Prompt = _prompt;
 //BA.debugLineNum = 426;BA.debugLine="newCombobox.ActionSub = SelectionChangeSub";
_newcombobox.ActionSub = _selectionchangesub;
 //BA.debugLineNum = 427;BA.debugLine="newCombobox.ActionSubModule = SelectionChangeSubModule";
_newcombobox.ActionSubModule = _selectionchangesubmodule;
 //BA.debugLineNum = 428;BA.debugLine="newCombobox.Items.Initialize";
_newcombobox.Items.Initialize();
 //BA.debugLineNum = 429;BA.debugLine="If Items.IsInitialized = True AND Items.Size > 0 Then newCombobox.Items.AddAll(Items)";
if (_items.IsInitialized()==__c.True && _items.getSize()>0) { 
_newcombobox.Items.AddAll(_items);};
 //BA.debugLineNum = 430;BA.debugLine="newCombobox.CurSelection = -1";
_newcombobox.CurSelection = (int) (-1);
 //BA.debugLineNum = 431;BA.debugLine="myControl.Text = Prompt";
_mycontrol.setText((Object)(_prompt));
 //BA.debugLineNum = 432;BA.debugLine="myControl.Tag = newCombobox";
_mycontrol.setTag((Object)(_newcombobox));
 //BA.debugLineNum = 433;BA.debugLine="myControl.TextColor = Color";
_mycontrol.setTextColor(_color);
 //BA.debugLineNum = 434;BA.debugLine="myControl.TextSize = TextSize";
_mycontrol.setTextSize(_textsize);
 //BA.debugLineNum = 435;BA.debugLine="myControl.Gravity = Bit.OR(Gravity.Left, Gravity.CENTER_VERTICAL)";
_mycontrol.setGravity(__c.Bit.Or(__c.Gravity.LEFT,__c.Gravity.CENTER_VERTICAL));
 //BA.debugLineNum = 436;BA.debugLine="ref.Target = myControl";
_ref.Target = (Object)(_mycontrol.getObject());
 //BA.debugLineNum = 437;BA.debugLine="ref.RunMethod2(\"setLines\", 1, \"java.lang.int\")";
_ref.RunMethod2("setLines",BA.NumberToString(1),"java.lang.int");
 //BA.debugLineNum = 438;BA.debugLine="ref.RunMethod2(\"setHorizontallyScrolling\", True, \"java.lang.boolean\")";
_ref.RunMethod2("setHorizontallyScrolling",BA.ObjectToString(__c.True),"java.lang.boolean");
 //BA.debugLineNum = 439;BA.debugLine="ref.RunMethod2(\"setEllipsize\", \"MARQUEE\", \"android.text.TextUtils$TruncateAt\")";
_ref.RunMethod2("setEllipsize","MARQUEE","android.text.TextUtils$TruncateAt");
 //BA.debugLineNum = 440;BA.debugLine="ref.RunMethod2(\"setMarqueeRepeatLimit\", -1, \"java.lang.int\")";
_ref.RunMethod2("setMarqueeRepeatLimit",BA.NumberToString(-1),"java.lang.int");
 //BA.debugLineNum = 441;BA.debugLine="ref.RunMethod2(\"setSelected\", True, \"java.lang.boolean\")";
_ref.RunMethod2("setSelected",BA.ObjectToString(__c.True),"java.lang.boolean");
 //BA.debugLineNum = 443;BA.debugLine="If Top = -2 Then";
if (_top==-2) { 
 //BA.debugLineNum = 444;BA.debugLine="Top = NextY";
_top = _nexty;
 }else if(_top==-1) { 
 //BA.debugLineNum = 446;BA.debugLine="Top = CurY";
_top = _cury;
 //BA.debugLineNum = 447;BA.debugLine="If Left > 0 AND Left < NextX Then Left = NextX";
if (_left>0 && _left<_nextx) { 
_left = _nextx;};
 };
 //BA.debugLineNum = 449;BA.debugLine="If Left = -2 Then";
if (_left==-2) { 
 //BA.debugLineNum = 450;BA.debugLine="Left = NextX";
_left = _nextx;
 }else if(_left==-1) { 
 //BA.debugLineNum = 452;BA.debugLine="Left = CurX";
_left = _curx;
 };
 //BA.debugLineNum = 455;BA.debugLine="If Width < 1 Then";
if (_width<1) { 
 //BA.debugLineNum = 456;BA.debugLine="If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left";
if (_width<-1 && _width>-100) { 
_width = (int) (__c.Abs(_width)*.01*(_parentwidth-_padding-_left));}
else {
_width = (int) (_parentwidth-_padding-_left);};
 };
 //BA.debugLineNum = 458;BA.debugLine="If Height < 1 Then";
if (_height<1) { 
 //BA.debugLineNum = 459;BA.debugLine="If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top";
if (_height<-1 && _height>-100) { 
_height = (int) (__c.Abs(_height)*.01*(_parent.getHeight()-_padding-_top));}
else {
_height = (int) (_parent.getHeight()-_padding-_top);};
 };
 //BA.debugLineNum = 462;BA.debugLine="If OverrideFontScale = True Then myControl.TextSize = myControl.TextSize / FontScale";
if (_overridefontscale==__c.True) { 
_mycontrol.setTextSize((float) (_mycontrol.getTextSize()/(double)_fontscale));};
 //BA.debugLineNum = 464;BA.debugLine="If ScaleViews = True Then ' Scale View to Size required for Font";
if (_scaleviews==__c.True) { 
 //BA.debugLineNum = 465;BA.debugLine="Dim charRect As Rect";
_charrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 466;BA.debugLine="charRect = GetCharSize(Typeface.DEFAULT, myControl.TextSize, \"\")";
_charrect = _getcharsize((anywheresoftware.b4a.keywords.constants.TypefaceWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.constants.TypefaceWrapper(), (android.graphics.Typeface)(__c.Typeface.DEFAULT)),_mycontrol.getTextSize(),"");
 //BA.debugLineNum = 467;BA.debugLine="If Height < charRect.Bottom + 11dip Then Height = charRect.Bottom + 11dip";
if (_height<_charrect.getBottom()+__c.DipToCurrent((int) (11))) { 
_height = (int) (_charrect.getBottom()+__c.DipToCurrent((int) (11)));};
 //BA.debugLineNum = 468;BA.debugLine="If ViewChar > 0 AND Width < 2 Then ' Calculate Min Width";
if (_viewchar>0 && _width<2) { 
 //BA.debugLineNum = 469;BA.debugLine="Width = Min(charRect.Right * ViewChar + 50dip, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_charrect.getRight()*_viewchar+__c.DipToCurrent((int) (50)),_parentwidth-_padding-_left));
 }else {
 //BA.debugLineNum = 471;BA.debugLine="Width = Min(Width, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_width,_parentwidth-_padding-_left));
 };
 };
 //BA.debugLineNum = 475;BA.debugLine="Parent.AddView(myControl, Left, Top, Width, Height)";
_parent.AddView((android.view.View)(_mycontrol.getObject()),_left,_top,_width,_height);
 //BA.debugLineNum = 476;BA.debugLine="myControl.Background = SetNinePatchDrawable(\"comboboxenabled\")";
_mycontrol.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("comboboxenabled")));
 //BA.debugLineNum = 477;BA.debugLine="ref.Target = myControl";
_ref.Target = (Object)(_mycontrol.getObject());
 //BA.debugLineNum = 478;BA.debugLine="ref.RunMethod4(\"setPadding\", Array As Object(6dip, 6dip, 44dip, 5dip), Array As String(\"java.lang.int\", \"java.lang.int\", \"java.lang.int\", \"java.lang.int\"))";
_ref.RunMethod4("setPadding",new Object[]{(Object)(__c.DipToCurrent((int) (6))),(Object)(__c.DipToCurrent((int) (6))),(Object)(__c.DipToCurrent((int) (44))),(Object)(__c.DipToCurrent((int) (5)))},new String[]{"java.lang.int","java.lang.int","java.lang.int","java.lang.int"});
 //BA.debugLineNum = 480;BA.debugLine="CurX = Left";
_curx = _left;
 //BA.debugLineNum = 481;BA.debugLine="NextX = Left + myControl.Width + Padding";
_nextx = (int) (_left+_mycontrol.getWidth()+_padding);
 //BA.debugLineNum = 482;BA.debugLine="CurY = Top";
_cury = _top;
 //BA.debugLineNum = 483;BA.debugLine="NextY = Max(NextY, Top + myControl.Height + Padding)";
_nexty = (int) (__c.Max(_nexty,_top+_mycontrol.getHeight()+_padding));
 //BA.debugLineNum = 484;BA.debugLine="If NextY > Parent.Height Then Parent.Height = NextY";
if (_nexty>_parent.getHeight()) { 
_parent.setHeight(_nexty);};
 //BA.debugLineNum = 486;BA.debugLine="If Name.Length > 0 Then NameMap.Put(Name, myControl)";
if (_name.length()>0) { 
_namemap.Put((Object)(_name),(Object)(_mycontrol.getObject()));};
 //BA.debugLineNum = 488;BA.debugLine="viewRect.Initialize(Left, Top, Left + myControl.Width, Top + myControl.Height)";
_viewrect.Initialize(_left,_top,(int) (_left+_mycontrol.getWidth()),(int) (_top+_mycontrol.getHeight()));
 //BA.debugLineNum = 489;BA.debugLine="Return viewRect";
if (true) return _viewrect;
 //BA.debugLineNum = 490;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper  _addlabel(int _left,int _top,int _width,int _height,String _text,float _textsize,int _color,String _linkedview) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _mycontrol = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _viewrect = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _charrect = null;
 //BA.debugLineNum = 230;BA.debugLine="Sub AddLabel(Left As Int, Top As Int, Width As Int, Height As Int, Text As String, TextSize As Float, Color As Int, LinkedView As String) As Rect";
 //BA.debugLineNum = 231;BA.debugLine="Dim myControl As Label";
_mycontrol = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 232;BA.debugLine="Dim viewRect As Rect";
_viewrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 233;BA.debugLine="Dim ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 235;BA.debugLine="myControl.Initialize(\"Label\")";
_mycontrol.Initialize(ba,"Label");
 //BA.debugLineNum = 236;BA.debugLine="myControl.Gravity = Bit.OR(Gravity.Left, Gravity.Top)";
_mycontrol.setGravity(__c.Bit.Or(__c.Gravity.LEFT,__c.Gravity.TOP));
 //BA.debugLineNum = 237;BA.debugLine="myControl.Text = Text";
_mycontrol.setText((Object)(_text));
 //BA.debugLineNum = 238;BA.debugLine="myControl.TextColor = Color";
_mycontrol.setTextColor(_color);
 //BA.debugLineNum = 239;BA.debugLine="myControl.TextSize = TextSize";
_mycontrol.setTextSize(_textsize);
 //BA.debugLineNum = 240;BA.debugLine="myControl.Typeface = Typeface.DEFAULT_BOLD";
_mycontrol.setTypeface(__c.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 241;BA.debugLine="ref.Target = myControl";
_ref.Target = (Object)(_mycontrol.getObject());
 //BA.debugLineNum = 242;BA.debugLine="ref.RunMethod2(\"setLines\", 1, \"java.lang.int\")";
_ref.RunMethod2("setLines",BA.NumberToString(1),"java.lang.int");
 //BA.debugLineNum = 243;BA.debugLine="ref.RunMethod2(\"setHorizontallyScrolling\", True, \"java.lang.boolean\")";
_ref.RunMethod2("setHorizontallyScrolling",BA.ObjectToString(__c.True),"java.lang.boolean");
 //BA.debugLineNum = 244;BA.debugLine="ref.RunMethod2(\"setEllipsize\", \"MARQUEE\", \"android.text.TextUtils$TruncateAt\")";
_ref.RunMethod2("setEllipsize","MARQUEE","android.text.TextUtils$TruncateAt");
 //BA.debugLineNum = 245;BA.debugLine="ref.RunMethod2(\"setMarqueeRepeatLimit\", -1, \"java.lang.int\")";
_ref.RunMethod2("setMarqueeRepeatLimit",BA.NumberToString(-1),"java.lang.int");
 //BA.debugLineNum = 246;BA.debugLine="ref.RunMethod2(\"setSelected\", True, \"java.lang.boolean\")";
_ref.RunMethod2("setSelected",BA.ObjectToString(__c.True),"java.lang.boolean");
 //BA.debugLineNum = 248;BA.debugLine="myControl.Tag = LinkedView";
_mycontrol.setTag((Object)(_linkedview));
 //BA.debugLineNum = 250;BA.debugLine="If Top = -2 Then";
if (_top==-2) { 
 //BA.debugLineNum = 251;BA.debugLine="Top = NextY";
_top = _nexty;
 }else if(_top==-1) { 
 //BA.debugLineNum = 253;BA.debugLine="Top = CurY";
_top = _cury;
 //BA.debugLineNum = 254;BA.debugLine="If Left > 0 AND Left < NextX Then Left = NextX";
if (_left>0 && _left<_nextx) { 
_left = _nextx;};
 };
 //BA.debugLineNum = 256;BA.debugLine="If Left = -2 Then";
if (_left==-2) { 
 //BA.debugLineNum = 257;BA.debugLine="Left = NextX";
_left = _nextx;
 }else if(_left==-1) { 
 //BA.debugLineNum = 259;BA.debugLine="Left = CurX";
_left = _curx;
 };
 //BA.debugLineNum = 262;BA.debugLine="If Width < 1 Then";
if (_width<1) { 
 //BA.debugLineNum = 263;BA.debugLine="If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left";
if (_width<-1 && _width>-100) { 
_width = (int) (__c.Abs(_width)*.01*(_parentwidth-_padding-_left));}
else {
_width = (int) (_parentwidth-_padding-_left);};
 };
 //BA.debugLineNum = 265;BA.debugLine="If Height < 1 Then";
if (_height<1) { 
 //BA.debugLineNum = 266;BA.debugLine="If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top";
if (_height<-1 && _height>-100) { 
_height = (int) (__c.Abs(_height)*.01*(_parent.getHeight()-_padding-_top));}
else {
_height = (int) (_parent.getHeight()-_padding-_top);};
 };
 //BA.debugLineNum = 269;BA.debugLine="If OverrideFontScale = True Then myControl.TextSize = myControl.TextSize / FontScale";
if (_overridefontscale==__c.True) { 
_mycontrol.setTextSize((float) (_mycontrol.getTextSize()/(double)_fontscale));};
 //BA.debugLineNum = 271;BA.debugLine="If ScaleViews = True Then ' Scale View to Size required for Font";
if (_scaleviews==__c.True) { 
 //BA.debugLineNum = 272;BA.debugLine="Dim charRect As Rect";
_charrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 273;BA.debugLine="charRect = GetCharSize(myControl.Typeface, myControl.TextSize, Text)";
_charrect = _getcharsize((anywheresoftware.b4a.keywords.constants.TypefaceWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.constants.TypefaceWrapper(), (android.graphics.Typeface)(_mycontrol.getTypeface())),_mycontrol.getTextSize(),_text);
 //BA.debugLineNum = 275;BA.debugLine="If Height < charRect.Bottom Then Height = charRect.Bottom";
if (_height<_charrect.getBottom()) { 
_height = _charrect.getBottom();};
 //BA.debugLineNum = 276;BA.debugLine="If Text.Length > 0 AND Width = 1 Then ' Calculate Min Width";
if (_text.length()>0 && _width==1) { 
 //BA.debugLineNum = 277;BA.debugLine="Width = Min(charRect.Right, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_charrect.getRight(),_parentwidth-_padding-_left));
 }else {
 //BA.debugLineNum = 279;BA.debugLine="Width = Min(Width, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_width,_parentwidth-_padding-_left));
 };
 };
 //BA.debugLineNum = 283;BA.debugLine="Parent.AddView(myControl, Left, Top, Width, Height)";
_parent.AddView((android.view.View)(_mycontrol.getObject()),_left,_top,_width,_height);
 //BA.debugLineNum = 285;BA.debugLine="CurX = Left";
_curx = _left;
 //BA.debugLineNum = 286;BA.debugLine="NextX = Left + myControl.Width + Padding";
_nextx = (int) (_left+_mycontrol.getWidth()+_padding);
 //BA.debugLineNum = 287;BA.debugLine="CurY = Top";
_cury = _top;
 //BA.debugLineNum = 288;BA.debugLine="NextY = Max(NextY, Top + myControl.Height + Padding)";
_nexty = (int) (__c.Max(_nexty,_top+_mycontrol.getHeight()+_padding));
 //BA.debugLineNum = 289;BA.debugLine="If NextY > Parent.Height Then Parent.Height = NextY";
if (_nexty>_parent.getHeight()) { 
_parent.setHeight(_nexty);};
 //BA.debugLineNum = 291;BA.debugLine="viewRect.Initialize(Left, Top, Left + myControl.Width, Top + myControl.Height)";
_viewrect.Initialize(_left,_top,(int) (_left+_mycontrol.getWidth()),(int) (_top+_mycontrol.getHeight()));
 //BA.debugLineNum = 292;BA.debugLine="Return viewRect";
if (true) return _viewrect;
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper  _addtextbox(int _left,int _top,int _width,int _height,String _name,String _text,float _textsize,String _hint,boolean _multiline,int _datatype,String _charfilter,int _actionbtn,String _actionsub,Object _actionsubmodule,int _minchar,int _maxchar,anywheresoftware.b4a.objects.collections.List _autocompleteitems) throws Exception{
anywheresoftware.b4a.objects.AutoCompleteEditTextWrapper _mycontrol = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _viewrect = null;
de.watchkido.mama.viewmgr._mytextbox _newtextbox = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _charrect = null;
 //BA.debugLineNum = 547;BA.debugLine="Public Sub AddTextBox(Left As Int, Top As Int, Width As Int, Height As Int, Name As String, Text As String, TextSize As Float, Hint As String, MultiLine As Boolean, DataType As Int, CharFilter As String, ActionBtn As Int, ActionSub As String, ActionSubModule As Object, MinChar As Int, MaxChar As Int, AutoCompleteItems As List) As Rect";
 //BA.debugLineNum = 548;BA.debugLine="Dim myControl As AutoCompleteEditText";
_mycontrol = new anywheresoftware.b4a.objects.AutoCompleteEditTextWrapper();
 //BA.debugLineNum = 549;BA.debugLine="Dim ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 550;BA.debugLine="Dim viewRect As Rect";
_viewrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 551;BA.debugLine="Dim newTextBox As MyTextbox";
_newtextbox = new de.watchkido.mama.viewmgr._mytextbox();
 //BA.debugLineNum = 553;BA.debugLine="newTextBox.Initialize";
_newtextbox.Initialize();
 //BA.debugLineNum = 554;BA.debugLine="myControl.Initialize(\"Edit\")";
_mycontrol.Initialize(ba,"Edit");
 //BA.debugLineNum = 555;BA.debugLine="newTextBox.DataType= DataType";
_newtextbox.DataType = _datatype;
 //BA.debugLineNum = 556;BA.debugLine="newTextBox.ActionBtn= ActionBtn";
_newtextbox.ActionBtn = _actionbtn;
 //BA.debugLineNum = 557;BA.debugLine="newTextBox.ActionSub= ActionSub";
_newtextbox.ActionSub = _actionsub;
 //BA.debugLineNum = 558;BA.debugLine="newTextBox.ActionSubModule= ActionSubModule";
_newtextbox.ActionSubModule = _actionsubmodule;
 //BA.debugLineNum = 559;BA.debugLine="newTextBox.MinChar= MinChar";
_newtextbox.MinChar = _minchar;
 //BA.debugLineNum = 560;BA.debugLine="newTextBox.MaxChar= MaxChar";
_newtextbox.MaxChar = _maxchar;
 //BA.debugLineNum = 561;BA.debugLine="myControl.Tag= newTextBox";
_mycontrol.setTag((Object)(_newtextbox));
 //BA.debugLineNum = 562;BA.debugLine="myControl.Text= Text";
_mycontrol.setText((Object)(_text));
 //BA.debugLineNum = 563;BA.debugLine="myControl.Hint= Hint";
_mycontrol.setHint(_hint);
 //BA.debugLineNum = 564;BA.debugLine="myControl.Typeface= Typeface.DEFAULT";
_mycontrol.setTypeface(__c.Typeface.DEFAULT);
 //BA.debugLineNum = 565;BA.debugLine="myControl.TextSize= TextSize";
_mycontrol.setTextSize(_textsize);
 //BA.debugLineNum = 567;BA.debugLine="If Top = -2 Then";
if (_top==-2) { 
 //BA.debugLineNum = 568;BA.debugLine="Top = NextY";
_top = _nexty;
 }else if(_top==-1) { 
 //BA.debugLineNum = 570;BA.debugLine="Top = CurY";
_top = _cury;
 //BA.debugLineNum = 571;BA.debugLine="If Left > 0 AND Left < NextX Then Left = NextX";
if (_left>0 && _left<_nextx) { 
_left = _nextx;};
 };
 //BA.debugLineNum = 573;BA.debugLine="If Left = -2 Then";
if (_left==-2) { 
 //BA.debugLineNum = 574;BA.debugLine="Left = NextX";
_left = _nextx;
 }else if(_left==-1) { 
 //BA.debugLineNum = 576;BA.debugLine="Left = CurX";
_left = _curx;
 };
 //BA.debugLineNum = 579;BA.debugLine="If Width < 1 Then";
if (_width<1) { 
 //BA.debugLineNum = 580;BA.debugLine="If Width < -1 AND Width > -100 Then Width = Abs(Width) * .01 * (ParentWidth - Padding - Left) Else Width = ParentWidth - Padding - Left";
if (_width<-1 && _width>-100) { 
_width = (int) (__c.Abs(_width)*.01*(_parentwidth-_padding-_left));}
else {
_width = (int) (_parentwidth-_padding-_left);};
 };
 //BA.debugLineNum = 582;BA.debugLine="If Height < 1 Then";
if (_height<1) { 
 //BA.debugLineNum = 583;BA.debugLine="If Height < -1 AND Height > -100 Then Height = Abs(Height) * .01 * (Parent.Height - Padding - Top) Else Height = Parent.Height - Padding - Top";
if (_height<-1 && _height>-100) { 
_height = (int) (__c.Abs(_height)*.01*(_parent.getHeight()-_padding-_top));}
else {
_height = (int) (_parent.getHeight()-_padding-_top);};
 };
 //BA.debugLineNum = 586;BA.debugLine="ref.Target = myControl";
_ref.Target = (Object)(_mycontrol.getObject());
 //BA.debugLineNum = 587;BA.debugLine="If MultiLine Then";
if (_multiline) { 
 //BA.debugLineNum = 588;BA.debugLine="myControl.SingleLine = False";
_mycontrol.setSingleLine(__c.False);
 //BA.debugLineNum = 589;BA.debugLine="myControl.Wrap= True";
_mycontrol.setWrap(__c.True);
 //BA.debugLineNum = 590;BA.debugLine="myControl.Gravity= Bit.OR(Gravity.Left, Gravity.Top)";
_mycontrol.setGravity(__c.Bit.Or(__c.Gravity.LEFT,__c.Gravity.TOP));
 //BA.debugLineNum = 591;BA.debugLine="newTextBox.DataType = Bit.OR(newTextBox.DataType, 131072) ' Multiline Text Flag";
_newtextbox.DataType = __c.Bit.Or(_newtextbox.DataType,(int) (131072));
 //BA.debugLineNum = 592;BA.debugLine="ref.RunMethod2(\"setImeOptions\", Bit.OR(1073741824, newTextBox.ActionBtn), \"java.lang.int\") 'flagNoEnterAction= 1073741824";
_ref.RunMethod2("setImeOptions",BA.NumberToString(__c.Bit.Or((int) (1073741824),_newtextbox.ActionBtn)),"java.lang.int");
 //BA.debugLineNum = 593;BA.debugLine="ref.SetOnTouchListener(\"Edit_Touched\")";
_ref.SetOnTouchListener(ba,"Edit_Touched");
 }else {
 //BA.debugLineNum = 595;BA.debugLine="myControl.SingleLine = True";
_mycontrol.setSingleLine(__c.True);
 //BA.debugLineNum = 596;BA.debugLine="myControl.Wrap= False";
_mycontrol.setWrap(__c.False);
 //BA.debugLineNum = 597;BA.debugLine="ref.RunMethod2(\"setImeOptions\", Bit.OR(268435456, newTextBox.ActionBtn), \"java.lang.int\") 'flagNoExtractUi= 268435456";
_ref.RunMethod2("setImeOptions",BA.NumberToString(__c.Bit.Or((int) (268435456),_newtextbox.ActionBtn)),"java.lang.int");
 };
 //BA.debugLineNum = 599;BA.debugLine="myControl.InputType = newTextBox.DataType";
_mycontrol.setInputType(_newtextbox.DataType);
 //BA.debugLineNum = 601;BA.debugLine="If OverrideFontScale = True Then myControl.TextSize = myControl.TextSize / FontScale";
if (_overridefontscale==__c.True) { 
_mycontrol.setTextSize((float) (_mycontrol.getTextSize()/(double)_fontscale));};
 //BA.debugLineNum = 603;BA.debugLine="If ScaleViews = True Then ' Scale View to Size required for Font";
if (_scaleviews==__c.True) { 
 //BA.debugLineNum = 604;BA.debugLine="Dim charRect As Rect";
_charrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 605;BA.debugLine="charRect = GetCharSize(myControl.Typeface, myControl.TextSize, \"\")";
_charrect = _getcharsize((anywheresoftware.b4a.keywords.constants.TypefaceWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.constants.TypefaceWrapper(), (android.graphics.Typeface)(_mycontrol.getTypeface())),_mycontrol.getTextSize(),"");
 //BA.debugLineNum = 606;BA.debugLine="If Height < charRect.Bottom + 7dip Then Height = charRect.Bottom + 7dip";
if (_height<_charrect.getBottom()+__c.DipToCurrent((int) (7))) { 
_height = (int) (_charrect.getBottom()+__c.DipToCurrent((int) (7)));};
 //BA.debugLineNum = 607;BA.debugLine="If Width < 3 Then ' Calculate Min Width";
if (_width<3) { 
 //BA.debugLineNum = 608;BA.debugLine="If Width = 1 Then";
if (_width==1) { 
 //BA.debugLineNum = 609;BA.debugLine="Width = Min(charRect.Right * MinChar + 7dip, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_charrect.getRight()*_minchar+__c.DipToCurrent((int) (7)),_parentwidth-_padding-_left));
 }else {
 //BA.debugLineNum = 611;BA.debugLine="Width = Min(charRect.Right * MaxChar + 7dip, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_charrect.getRight()*_maxchar+__c.DipToCurrent((int) (7)),_parentwidth-_padding-_left));
 };
 }else {
 //BA.debugLineNum = 614;BA.debugLine="Width = Min(Width, ParentWidth - Padding - Left)";
_width = (int) (__c.Min(_width,_parentwidth-_padding-_left));
 };
 };
 //BA.debugLineNum = 618;BA.debugLine="Parent.AddView(myControl, Left, Top, Width, Height)";
_parent.AddView((android.view.View)(_mycontrol.getObject()),_left,_top,_width,_height);
 //BA.debugLineNum = 619;BA.debugLine="myControl.Background = SetNinePatchDrawable(\"editenabled\")";
_mycontrol.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("editenabled")));
 //BA.debugLineNum = 621;BA.debugLine="CurX = Left";
_curx = _left;
 //BA.debugLineNum = 622;BA.debugLine="NextX = Left + myControl.Width + Padding";
_nextx = (int) (_left+_mycontrol.getWidth()+_padding);
 //BA.debugLineNum = 623;BA.debugLine="CurY = Top";
_cury = _top;
 //BA.debugLineNum = 624;BA.debugLine="NextY = Max(NextY, Top + myControl.Height + Padding)";
_nexty = (int) (__c.Max(_nexty,_top+_mycontrol.getHeight()+_padding));
 //BA.debugLineNum = 625;BA.debugLine="If NextY > Parent.Height Then Parent.Height = NextY";
if (_nexty>_parent.getHeight()) { 
_parent.setHeight(_nexty);};
 //BA.debugLineNum = 627;BA.debugLine="If CharFilter.Length = 0 Then";
if (_charfilter.length()==0) { 
 //BA.debugLineNum = 628;BA.debugLine="If Bit.AND(DataType, DataType_Phone) = DataType_Phone Then";
if (__c.Bit.And(_datatype,_datatype_phone())==_datatype_phone()) { 
 //BA.debugLineNum = 629;BA.debugLine="CharFilter = CharFilter_Numeric & \"*#+-P()N,/ \"";
_charfilter = _charfilter_numeric()+"*#+-P()N,/ ";
 }else if(__c.Bit.And(_datatype,_datatype_url())==_datatype_url()) { 
 //BA.debugLineNum = 631;BA.debugLine="CharFilter = CharFilter_Alpha & CharFilter_Numeric & \"$.,'-+!*_~:%()\"";
_charfilter = _charfilter_alpha()+_charfilter_numeric()+"$.,'-+!*_~:%()";
 }else if(__c.Bit.And(_datatype,_datatype_email())==_datatype_email()) { 
 //BA.debugLineNum = 633;BA.debugLine="CharFilter = CharFilter_Alpha & CharFilter_Numeric & \"!#$%&'*+-/=?^_`{|}~.\"";
_charfilter = _charfilter_alpha()+_charfilter_numeric()+"!#$%&'*+-/=?^_`{|}~.";
 }else if(__c.Bit.And(_datatype,_datatype_float())==_datatype_float()) { 
 //BA.debugLineNum = 635;BA.debugLine="CharFilter = CharFilter_Numeric & \".-\"";
_charfilter = _charfilter_numeric()+".-";
 }else if(__c.Bit.And(_datatype,_datatype_num())==_datatype_num()) { 
 //BA.debugLineNum = 637;BA.debugLine="CharFilter = CharFilter_Numeric";
_charfilter = _charfilter_numeric();
 };
 };
 //BA.debugLineNum = 640;BA.debugLine="If CharFilter.Length > 0 Then IME.SetCustomFilter(myControl, myControl.InputType, CharFilter)";
if (_charfilter.length()>0) { 
_ime.SetCustomFilter((android.widget.EditText)(_mycontrol.getObject()),_mycontrol.getInputType(),_charfilter);};
 //BA.debugLineNum = 642;BA.debugLine="IME.AddHandleActionEvent(myControl)";
_ime.AddHandleActionEvent((android.widget.EditText)(_mycontrol.getObject()),ba);
 //BA.debugLineNum = 644;BA.debugLine="If AutoCompleteItems.IsInitialized Then myControl.SetItems(AutoCompleteItems)";
if (_autocompleteitems.IsInitialized()) { 
_mycontrol.SetItems(ba,_autocompleteitems);};
 //BA.debugLineNum = 645;BA.debugLine="MyViews.Add(myControl)";
_myviews.Add((Object)(_mycontrol.getObject()));
 //BA.debugLineNum = 646;BA.debugLine="If Name.Length > 0 Then NameMap.Put(Name, myControl)";
if (_name.length()>0) { 
_namemap.Put((Object)(_name),(Object)(_mycontrol.getObject()));};
 //BA.debugLineNum = 648;BA.debugLine="viewRect.Initialize(Left, Top, Left + myControl.Width, Top + myControl.Height)";
_viewrect.Initialize(_left,_top,(int) (_left+_mycontrol.getWidth()),(int) (_top+_mycontrol.getHeight()));
 //BA.debugLineNum = 649;BA.debugLine="Return viewRect";
if (true) return _viewrect;
 //BA.debugLineNum = 650;BA.debugLine="End Sub";
return null;
}
public String  _button_click() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _mycontrol = null;
de.watchkido.mama.viewmgr._myactionsub _tagactionsub = null;
 //BA.debugLineNum = 391;BA.debugLine="Private Sub Button_Click";
 //BA.debugLineNum = 392;BA.debugLine="Dim myControl As Label";
_mycontrol = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 393;BA.debugLine="Dim tagActionSub As MyActionSub";
_tagactionsub = new de.watchkido.mama.viewmgr._myactionsub();
 //BA.debugLineNum = 395;BA.debugLine="myControl = Sender";
_mycontrol.setObject((android.widget.TextView)(__c.Sender(ba)));
 //BA.debugLineNum = 396;BA.debugLine="tagActionSub = myControl.Tag";
_tagactionsub = (de.watchkido.mama.viewmgr._myactionsub)(_mycontrol.getTag());
 //BA.debugLineNum = 397;BA.debugLine="If tagActionSub.IsInitialized Then";
if (_tagactionsub.IsInitialized) { 
 //BA.debugLineNum = 398;BA.debugLine="If tagActionSub.ActionSub.Length > 0 Then CallSubDelayed(tagActionSub.ActionSubModule, tagActionSub.ActionSub)";
if (_tagactionsub.ActionSub.length()>0) { 
__c.CallSubDelayed(ba,_tagactionsub.ActionSubModule,_tagactionsub.ActionSub);};
 };
 //BA.debugLineNum = 400;BA.debugLine="End Sub";
return "";
}
public boolean  _button_touch(Object _viewtag,int _action,float _x,float _y,Object _moveevent) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _mycontrol = null;
 //BA.debugLineNum = 402;BA.debugLine="Private Sub Button_Touch(viewtag As Object, action As Int, x As Float, y As Float, moveEvent As Object) As Boolean";
 //BA.debugLineNum = 403;BA.debugLine="Dim myControl As Label";
_mycontrol = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 405;BA.debugLine="myControl = Sender";
_mycontrol.setObject((android.widget.TextView)(__c.Sender(ba)));
 //BA.debugLineNum = 406;BA.debugLine="If action = 0 Then myControl.Background = SetNinePatchDrawable(\"buttondown\")";
if (_action==0) { 
_mycontrol.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("buttondown")));};
 //BA.debugLineNum = 407;BA.debugLine="If action = 1 OR x < 0 OR y < 0 OR x > myControl.Width OR y > myControl.Height Then myControl.Background = SetNinePatchDrawable(\"buttonup\")";
if (_action==1 || _x<0 || _y<0 || _x>_mycontrol.getWidth() || _y>_mycontrol.getHeight()) { 
_mycontrol.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("buttonup")));};
 //BA.debugLineNum = 408;BA.debugLine="End Sub";
return false;
}
public String  _charfilter_alpha() throws Exception{
 //BA.debugLineNum = 171;BA.debugLine="Sub CharFilter_Alpha As String";
 //BA.debugLineNum = 172;BA.debugLine="Return \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\"";
if (true) return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public String  _charfilter_numeric() throws Exception{
 //BA.debugLineNum = 183;BA.debugLine="Sub CharFilter_Numeric As String";
 //BA.debugLineNum = 184;BA.debugLine="Return \"0123456789\"";
if (true) return "0123456789";
 //BA.debugLineNum = 185;BA.debugLine="End Sub";
return "";
}
public String  _charfilter_upper_alpha() throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub CharFilter_Upper_Alpha As String";
 //BA.debugLineNum = 178;BA.debugLine="Return \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\"";
if (true) return "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 //BA.debugLineNum = 179;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Private Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Type MyTextbox(DataType As Int, ActionBtn As Int, ActionSub As String, ActionSubModule As Object, MinChar As Int, MaxChar As Int)";
;
 //BA.debugLineNum = 4;BA.debugLine="Type MyActionSub(ActionSub As String, ActionSubModule As Object)";
;
 //BA.debugLineNum = 5;BA.debugLine="Type MyCombobox(CurSelection As Int, Items As List, Prompt As String, ActionSub As String, ActionSubModule As Object)";
;
 //BA.debugLineNum = 6;BA.debugLine="Type ComboItem(Text As String, Value As Object)";
;
 //BA.debugLineNum = 7;BA.debugLine="Private IME As IME ' Put here instead of below so class is considered an Activity";
_ime = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 8;BA.debugLine="Private MyViews As List";
_myviews = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 9;BA.debugLine="Private NameMap As Map";
_namemap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 10;BA.debugLine="Private CurFocus As View";
_curfocus = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Public Padding As Int";
_padding = 0;
 //BA.debugLineNum = 12;BA.debugLine="Public MinMaxWarn As Boolean";
_minmaxwarn = false;
 //BA.debugLineNum = 13;BA.debugLine="Public ScaleViews As Boolean";
_scaleviews = false;
 //BA.debugLineNum = 14;BA.debugLine="Public OverrideFontScale As Boolean";
_overridefontscale = false;
 //BA.debugLineNum = 15;BA.debugLine="Private FontScale As Float";
_fontscale = 0f;
 //BA.debugLineNum = 16;BA.debugLine="Private ScaleLabel As Label";
_scalelabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private Parent As Panel";
_parent = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Public ParentWidth As Int";
_parentwidth = 0;
 //BA.debugLineNum = 19;BA.debugLine="Private CurX As Int";
_curx = 0;
 //BA.debugLineNum = 20;BA.debugLine="Private NextX As Int";
_nextx = 0;
 //BA.debugLineNum = 21;BA.debugLine="Private CurY As Int";
_cury = 0;
 //BA.debugLineNum = 22;BA.debugLine="Private NextY As Int";
_nexty = 0;
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public String  _combobox_click() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _mylabel = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
de.watchkido.mama.viewmgr._comboitem _mycomboitem = null;
anywheresoftware.b4a.objects.collections.List _items = null;
int _selection = 0;
int _i = 0;
 //BA.debugLineNum = 492;BA.debugLine="Private Sub Combobox_Click";
 //BA.debugLineNum = 493;BA.debugLine="Dim MyLabel As Label";
_mylabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 494;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 495;BA.debugLine="Dim MyComboItem As ComboItem";
_mycomboitem = new de.watchkido.mama.viewmgr._comboitem();
 //BA.debugLineNum = 496;BA.debugLine="Dim items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 497;BA.debugLine="Dim selection As Int";
_selection = 0;
 //BA.debugLineNum = 499;BA.debugLine="MyLabel = Sender";
_mylabel.setObject((android.widget.TextView)(__c.Sender(ba)));
 //BA.debugLineNum = 500;BA.debugLine="MCB = MyLabel.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_mylabel.getTag());
 //BA.debugLineNum = 501;BA.debugLine="If MCB.items.Size > 0 Then";
if (_mcb.Items.getSize()>0) { 
 //BA.debugLineNum = 502;BA.debugLine="items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 503;BA.debugLine="For i = 0 To MCB.items.Size - 1";
{
final int step371 = 1;
final int limit371 = (int) (_mcb.Items.getSize()-1);
for (_i = (int) (0); (step371 > 0 && _i <= limit371) || (step371 < 0 && _i >= limit371); _i = ((int)(0 + _i + step371))) {
 //BA.debugLineNum = 504;BA.debugLine="If MCB.items.Get(i) Is ComboItem Then";
if (_mcb.Items.Get(_i) instanceof de.watchkido.mama.viewmgr._comboitem) { 
 //BA.debugLineNum = 505;BA.debugLine="MyComboItem = MCB.items.Get(i)";
_mycomboitem = (de.watchkido.mama.viewmgr._comboitem)(_mcb.Items.Get(_i));
 //BA.debugLineNum = 506;BA.debugLine="items.Add(MyComboItem.Text)";
_items.Add((Object)(_mycomboitem.Text));
 }else {
 //BA.debugLineNum = 508;BA.debugLine="items.Add(MCB.items.Get(i))";
_items.Add(_mcb.Items.Get(_i));
 };
 }
};
 //BA.debugLineNum = 511;BA.debugLine="selection= InputList(items, MCB.Prompt, MCB.CurSelection)";
_selection = __c.InputList(_items,_mcb.Prompt,_mcb.CurSelection,ba);
 //BA.debugLineNum = 512;BA.debugLine="If selection < 0 Then Return";
if (_selection<0) { 
if (true) return "";};
 //BA.debugLineNum = 513;BA.debugLine="MCB.CurSelection= selection";
_mcb.CurSelection = _selection;
 //BA.debugLineNum = 514;BA.debugLine="If MCB.items.Get(selection) Is ComboItem Then";
if (_mcb.Items.Get(_selection) instanceof de.watchkido.mama.viewmgr._comboitem) { 
 //BA.debugLineNum = 515;BA.debugLine="MyComboItem = MCB.items.Get(selection)";
_mycomboitem = (de.watchkido.mama.viewmgr._comboitem)(_mcb.Items.Get(_selection));
 //BA.debugLineNum = 516;BA.debugLine="MyLabel.Text= MyComboItem.Text";
_mylabel.setText((Object)(_mycomboitem.Text));
 //BA.debugLineNum = 517;BA.debugLine="If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, selection, MyComboItem.Value)";
if (_mcb.ActionSub.length()>0) { 
__c.CallSubDelayed3(ba,_mcb.ActionSubModule,_mcb.ActionSub,(Object)(_selection),_mycomboitem.Value);};
 }else {
 //BA.debugLineNum = 519;BA.debugLine="MyLabel.Text= MCB.items.Get(selection)";
_mylabel.setText(_mcb.Items.Get(_selection));
 //BA.debugLineNum = 520;BA.debugLine="If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, selection, MCB.items.Get(selection))";
if (_mcb.ActionSub.length()>0) { 
__c.CallSubDelayed3(ba,_mcb.ActionSubModule,_mcb.ActionSub,(Object)(_selection),_mcb.Items.Get(_selection));};
 };
 }else {
 //BA.debugLineNum = 523;BA.debugLine="ToastMessageShow(\"No Items to Select\", True)";
__c.ToastMessageShow("No Items to Select",__c.True);
 };
 //BA.debugLineNum = 525;BA.debugLine="End Sub";
return "";
}
public int  _datatype_auto_correct() throws Exception{
 //BA.debugLineNum = 130;BA.debugLine="Sub DataType_Auto_Correct As Int";
 //BA.debugLineNum = 131;BA.debugLine="Return 32769";
if (true) return (int) (32769);
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_date() throws Exception{
 //BA.debugLineNum = 78;BA.debugLine="Sub DataType_Date As Int";
 //BA.debugLineNum = 79;BA.debugLine="Return 20";
if (true) return (int) (20);
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_datetime() throws Exception{
 //BA.debugLineNum = 73;BA.debugLine="Sub DataType_DateTime As Int";
 //BA.debugLineNum = 74;BA.debugLine="Return 4";
if (true) return (int) (4);
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_email() throws Exception{
 //BA.debugLineNum = 93;BA.debugLine="Sub DataType_Email As Int";
 //BA.debugLineNum = 94;BA.debugLine="Return 33";
if (true) return (int) (33);
 //BA.debugLineNum = 95;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_float() throws Exception{
 //BA.debugLineNum = 68;BA.debugLine="Sub DataType_Float As Int";
 //BA.debugLineNum = 69;BA.debugLine="Return 12290";
if (true) return (int) (12290);
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_name() throws Exception{
 //BA.debugLineNum = 108;BA.debugLine="Sub DataType_Name As Int";
 //BA.debugLineNum = 109;BA.debugLine="Return 8289";
if (true) return (int) (8289);
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_no_suggestions() throws Exception{
 //BA.debugLineNum = 135;BA.debugLine="Sub DataType_No_Suggestions As Int";
 //BA.debugLineNum = 136;BA.debugLine="Return 524289";
if (true) return (int) (524289);
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_num() throws Exception{
 //BA.debugLineNum = 63;BA.debugLine="Sub DataType_Num As Int";
 //BA.debugLineNum = 64;BA.debugLine="Return 2";
if (true) return (int) (2);
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_password() throws Exception{
 //BA.debugLineNum = 88;BA.debugLine="Sub DataType_Password As Int";
 //BA.debugLineNum = 89;BA.debugLine="Return 129";
if (true) return (int) (129);
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_phone() throws Exception{
 //BA.debugLineNum = 103;BA.debugLine="Sub DataType_Phone As Int";
 //BA.debugLineNum = 104;BA.debugLine="Return 3";
if (true) return (int) (3);
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_text() throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Sub DataType_Text As Int";
 //BA.debugLineNum = 59;BA.debugLine="Return 1";
if (true) return (int) (1);
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_time() throws Exception{
 //BA.debugLineNum = 83;BA.debugLine="Sub DataType_Time As Int";
 //BA.debugLineNum = 84;BA.debugLine="Return 36";
if (true) return (int) (36);
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_uppercase_all() throws Exception{
 //BA.debugLineNum = 114;BA.debugLine="Sub DataType_Uppercase_All As Int";
 //BA.debugLineNum = 115;BA.debugLine="Return 4097";
if (true) return (int) (4097);
 //BA.debugLineNum = 116;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_uppercase_sentences() throws Exception{
 //BA.debugLineNum = 119;BA.debugLine="Sub DataType_Uppercase_Sentences As Int";
 //BA.debugLineNum = 120;BA.debugLine="Return 16385";
if (true) return (int) (16385);
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_uppercase_words() throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Sub DataType_Uppercase_Words As Int";
 //BA.debugLineNum = 126;BA.debugLine="Return 8193";
if (true) return (int) (8193);
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return 0;
}
public int  _datatype_url() throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub DataType_URL As Int";
 //BA.debugLineNum = 99;BA.debugLine="Return 17";
if (true) return (int) (17);
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return 0;
}
public String  _edit_focuschanged(boolean _hasfocus) throws Exception{
anywheresoftware.b4a.objects.EditTextWrapper _et = null;
de.watchkido.mama.viewmgr._mytextbox _mtb = null;
int _index = 0;
 //BA.debugLineNum = 699;BA.debugLine="Private Sub Edit_FocusChanged (HasFocus As Boolean)";
 //BA.debugLineNum = 700;BA.debugLine="If HasFocus = True Then";
if (_hasfocus==__c.True) { 
 }else {
 //BA.debugLineNum = 703;BA.debugLine="If MinMaxWarn = True Then";
if (_minmaxwarn==__c.True) { 
 //BA.debugLineNum = 704;BA.debugLine="Dim ET As EditText";
_et = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 705;BA.debugLine="Dim MTB As MyTextbox";
_mtb = new de.watchkido.mama.viewmgr._mytextbox();
 //BA.debugLineNum = 706;BA.debugLine="Dim index As Int";
_index = 0;
 //BA.debugLineNum = 708;BA.debugLine="ET = Sender";
_et.setObject((android.widget.EditText)(__c.Sender(ba)));
 //BA.debugLineNum = 709;BA.debugLine="index = MyViews.IndexOf(ET)";
_index = _myviews.IndexOf((Object)(_et.getObject()));
 //BA.debugLineNum = 710;BA.debugLine="If index > -1 Then";
if (_index>-1) { 
 //BA.debugLineNum = 711;BA.debugLine="MTB = ET.Tag";
_mtb = (de.watchkido.mama.viewmgr._mytextbox)(_et.getTag());
 //BA.debugLineNum = 712;BA.debugLine="If ET.Text.Length < MTB.MinChar Then ToastMessageShow(\"Value Needs to be at least \" & MTB.MinChar & \" Characters Long\", True)";
if (_et.getText().length()<_mtb.MinChar) { 
__c.ToastMessageShow("Value Needs to be at least "+BA.NumberToString(_mtb.MinChar)+" Characters Long",__c.True);};
 };
 };
 };
 //BA.debugLineNum = 716;BA.debugLine="End Sub";
return "";
}
public String  _edit_textchanged(String _old,String _new) throws Exception{
int _cursorpos = 0;
anywheresoftware.b4a.objects.EditTextWrapper _et = null;
de.watchkido.mama.viewmgr._mytextbox _mtb = null;
 //BA.debugLineNum = 718;BA.debugLine="Private Sub Edit_TextChanged (Old As String, New As String)";
 //BA.debugLineNum = 719;BA.debugLine="Dim cursorPOS As Int";
_cursorpos = 0;
 //BA.debugLineNum = 720;BA.debugLine="Dim ET As EditText";
_et = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 721;BA.debugLine="Dim MTB As MyTextbox";
_mtb = new de.watchkido.mama.viewmgr._mytextbox();
 //BA.debugLineNum = 723;BA.debugLine="If New.CompareTo(Old) <> 0 Then";
if (_new.compareTo(_old)!=0) { 
 //BA.debugLineNum = 724;BA.debugLine="ET = Sender";
_et.setObject((android.widget.EditText)(__c.Sender(ba)));
 //BA.debugLineNum = 725;BA.debugLine="MTB = ET.Tag";
_mtb = (de.watchkido.mama.viewmgr._mytextbox)(_et.getTag());
 //BA.debugLineNum = 726;BA.debugLine="cursorPOS = ET.SelectionStart";
_cursorpos = _et.getSelectionStart();
 //BA.debugLineNum = 727;BA.debugLine="If MTB.MaxChar > 0 Then";
if (_mtb.MaxChar>0) { 
 //BA.debugLineNum = 728;BA.debugLine="If New.Length > MTB.MaxChar AND MinMaxWarn = True Then ToastMessageShow(\"Too many Characters input\", True)";
if (_new.length()>_mtb.MaxChar && _minmaxwarn==__c.True) { 
__c.ToastMessageShow("Too many Characters input",__c.True);};
 //BA.debugLineNum = 729;BA.debugLine="ET.Text = New.SubString2(0, Min(MTB.MaxChar, New.Length))";
_et.setText((Object)(_new.substring((int) (0),(int) (__c.Min(_mtb.MaxChar,_new.length())))));
 };
 //BA.debugLineNum = 731;BA.debugLine="ET.SelectionStart = Min(cursorPOS, ET.Text.Length)";
_et.setSelectionStart((int) (__c.Min(_cursorpos,_et.getText().length())));
 };
 //BA.debugLineNum = 733;BA.debugLine="End Sub";
return "";
}
public boolean  _edit_touched(Object _viewtag,int _action,float _x,float _y,Object _motionevent) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
 //BA.debugLineNum = 691;BA.debugLine="Private Sub Edit_Touched(viewTag As Object, action As Int, x As Float, y As Float, motionEvent As Object) As Boolean";
 //BA.debugLineNum = 692;BA.debugLine="If action = 2 Then '= MOVE";
if (_action==2) { 
 //BA.debugLineNum = 693;BA.debugLine="Dim ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 694;BA.debugLine="ref.Target = Parent";
_ref.Target = (Object)(_parent.getObject());
 //BA.debugLineNum = 695;BA.debugLine="ref.RunMethod2(\"requestDisallowInterceptTouchEvent\", True, \"java.lang.boolean\")";
_ref.RunMethod2("requestDisallowInterceptTouchEvent",BA.ObjectToString(__c.True),"java.lang.boolean");
 };
 //BA.debugLineNum = 697;BA.debugLine="End Sub";
return false;
}
public String  _enabled(String _name,boolean _enableview) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _foundname = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
 //BA.debugLineNum = 967;BA.debugLine="Public Sub Enabled(Name As String, EnableView As Boolean)";
 //BA.debugLineNum = 968;BA.debugLine="Dim FoundName As View";
_foundname = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 970;BA.debugLine="FoundName = NameMap.Get(Name)";
_foundname.setObject((android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 971;BA.debugLine="If FoundName.IsInitialized = True Then";
if (_foundname.IsInitialized()==__c.True) { 
 //BA.debugLineNum = 972;BA.debugLine="If FoundName Is EditText Then";
if (_foundname.getObjectOrNull() instanceof android.widget.EditText) { 
 //BA.debugLineNum = 973;BA.debugLine="Dim ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 975;BA.debugLine="FoundName.Enabled = EnableView";
_foundname.setEnabled(_enableview);
 //BA.debugLineNum = 976;BA.debugLine="ref.Target = FoundName";
_ref.Target = (Object)(_foundname.getObject());
 //BA.debugLineNum = 977;BA.debugLine="If EnableView = True Then";
if (_enableview==__c.True) { 
 //BA.debugLineNum = 978;BA.debugLine="FoundName.Background = SetNinePatchDrawable(\"editenabled\")";
_foundname.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("editenabled")));
 //BA.debugLineNum = 979;BA.debugLine="ref.RunMethod2(\"setFocusable\", \"True\", \"java.lang.boolean\")";
_ref.RunMethod2("setFocusable","True","java.lang.boolean");
 //BA.debugLineNum = 980;BA.debugLine="ref.RunMethod2(\"setFocusableInTouchMode\", \"True\", \"java.lang.boolean\")";
_ref.RunMethod2("setFocusableInTouchMode","True","java.lang.boolean");
 }else {
 //BA.debugLineNum = 982;BA.debugLine="FoundName.Background = SetNinePatchDrawable(\"editdisabled\")";
_foundname.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("editdisabled")));
 //BA.debugLineNum = 983;BA.debugLine="ref.RunMethod2(\"setFocusable\", \"False\", \"java.lang.boolean\")";
_ref.RunMethod2("setFocusable","False","java.lang.boolean");
 //BA.debugLineNum = 984;BA.debugLine="ref.RunMethod2(\"setFocusableInTouchMode\", \"False\", \"java.lang.boolean\")";
_ref.RunMethod2("setFocusableInTouchMode","False","java.lang.boolean");
 };
 }else if(_foundname.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 987;BA.debugLine="FoundName.Enabled = EnableView";
_foundname.setEnabled(_enableview);
 //BA.debugLineNum = 988;BA.debugLine="If FoundName.Tag Is MyCombobox Then";
if (_foundname.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 989;BA.debugLine="If EnableView = True Then";
if (_enableview==__c.True) { 
 //BA.debugLineNum = 990;BA.debugLine="FoundName.Background = SetNinePatchDrawable(\"comboboxenabled\")";
_foundname.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("comboboxenabled")));
 }else {
 //BA.debugLineNum = 992;BA.debugLine="FoundName.Background = SetNinePatchDrawable(\"comboboxdisabled\")";
_foundname.setBackground((android.graphics.drawable.Drawable)(_setninepatchdrawable("comboboxdisabled")));
 };
 };
 };
 };
 //BA.debugLineNum = 997;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper  _getcharsize(anywheresoftware.b4a.keywords.constants.TypefaceWrapper _viewtypeface,float _viewfontsize,String _sampletext) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper _testcanvas = null;
anywheresoftware.b4a.objects.StringUtils _strutil = null;
String _teststring = "";
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _charframe = null;
int _x = 0;
 //BA.debugLineNum = 1000;BA.debugLine="Public Sub GetCharSize(ViewTypeface As Typeface, ViewFontsize As Float, SampleText As String) As Rect";
 //BA.debugLineNum = 1001;BA.debugLine="Dim testCanvas As Canvas";
_testcanvas = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 1002;BA.debugLine="Dim strUtil As StringUtils";
_strutil = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 1003;BA.debugLine="Dim testString As String";
_teststring = "";
 //BA.debugLineNum = 1004;BA.debugLine="Dim CharFrame As Rect";
_charframe = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 1006;BA.debugLine="ScaleLabel.Typeface = ViewTypeface";
_scalelabel.setTypeface((android.graphics.Typeface)(_viewtypeface.getObject()));
 //BA.debugLineNum = 1007;BA.debugLine="ScaleLabel.TextSize = ViewFontsize";
_scalelabel.setTextSize(_viewfontsize);
 //BA.debugLineNum = 1008;BA.debugLine="testCanvas.Initialize(ScaleLabel)";
_testcanvas.Initialize((android.view.View)(_scalelabel.getObject()));
 //BA.debugLineNum = 1009;BA.debugLine="CharFrame.Initialize(0,0,0,0)";
_charframe.Initialize((int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 1010;BA.debugLine="If SampleText.Length = 0 Then testString = CharFilter_Alpha & CharFilter_Numeric & \"`~!@#$%^&*()_+=-[]\\\\{}|;':,./<>?\" & QUOTE Else testString = SampleText";
if (_sampletext.length()==0) { 
_teststring = _charfilter_alpha()+_charfilter_numeric()+"`~!@#$%^&*()_+=-[]\\\\{}|;':,./<>?"+__c.QUOTE;}
else {
_teststring = _sampletext;};
 //BA.debugLineNum = 1011;BA.debugLine="ScaleLabel.Width = testCanvas.MeasureStringWidth(testString, ScaleLabel.Typeface, ScaleLabel.TextSize) + 2dip";
_scalelabel.setWidth((int) (_testcanvas.MeasureStringWidth(_teststring,_scalelabel.getTypeface(),_scalelabel.getTextSize())+__c.DipToCurrent((int) (2))));
 //BA.debugLineNum = 1012;BA.debugLine="CharFrame.Bottom = strUtil.MeasureMultilineTextHeight(ScaleLabel, testString)";
_charframe.setBottom(_strutil.MeasureMultilineTextHeight((android.widget.TextView)(_scalelabel.getObject()),_teststring));
 //BA.debugLineNum = 1013;BA.debugLine="If SampleText.Length > 0 Then";
if (_sampletext.length()>0) { 
 //BA.debugLineNum = 1014;BA.debugLine="CharFrame.Right = ScaleLabel.Width";
_charframe.setRight(_scalelabel.getWidth());
 }else {
 //BA.debugLineNum = 1016;BA.debugLine="For x = 0 To testString.Length - 1";
{
final int step814 = 1;
final int limit814 = (int) (_teststring.length()-1);
for (_x = (int) (0); (step814 > 0 && _x <= limit814) || (step814 < 0 && _x >= limit814); _x = ((int)(0 + _x + step814))) {
 //BA.debugLineNum = 1017;BA.debugLine="CharFrame.Right = Max(CharFrame.Right, testCanvas.MeasureStringWidth(testString.CharAt(x), ScaleLabel.Typeface, ScaleLabel.TextSize))";
_charframe.setRight((int) (__c.Max(_charframe.getRight(),_testcanvas.MeasureStringWidth(BA.ObjectToString(_teststring.charAt(_x)),_scalelabel.getTypeface(),_scalelabel.getTextSize()))));
 }
};
 };
 //BA.debugLineNum = 1020;BA.debugLine="Return CharFrame";
if (true) return _charframe;
 //BA.debugLineNum = 1021;BA.debugLine="End Sub";
return null;
}
public Object  _getcurselection(String _name) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _foundname = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
int _selstart = 0;
int _selend = 0;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
anywheresoftware.b4a.objects.AutoCompleteEditTextWrapper _myedit = null;
 //BA.debugLineNum = 753;BA.debugLine="Public Sub GetCurSelection(Name As String) As Object";
 //BA.debugLineNum = 754;BA.debugLine="Dim FoundName As View";
_foundname = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 756;BA.debugLine="FoundName = NameMap.Get(Name)";
_foundname.setObject((android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 757;BA.debugLine="If FoundName.IsInitialized = False Then Return \"\"";
if (_foundname.IsInitialized()==__c.False) { 
if (true) return (Object)("");};
 //BA.debugLineNum = 758;BA.debugLine="If FoundName Is Label Then";
if (_foundname.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 759;BA.debugLine="If FoundName.Tag Is MyCombobox Then";
if (_foundname.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 760;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 761;BA.debugLine="MCB = FoundName.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_foundname.getTag());
 //BA.debugLineNum = 762;BA.debugLine="Return MCB.CurSelection";
if (true) return (Object)(_mcb.CurSelection);
 }else if(_foundname.getObjectOrNull() instanceof android.widget.EditText) { 
 //BA.debugLineNum = 764;BA.debugLine="Dim SelStart, SelEnd As Int";
_selstart = 0;
_selend = 0;
 //BA.debugLineNum = 765;BA.debugLine="Dim ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 766;BA.debugLine="Dim MyEdit As AutoCompleteEditText";
_myedit = new anywheresoftware.b4a.objects.AutoCompleteEditTextWrapper();
 //BA.debugLineNum = 767;BA.debugLine="MyEdit= FoundName";
_myedit.setObject((android.widget.EditText)(_foundname.getObject()));
 //BA.debugLineNum = 768;BA.debugLine="SelStart = MyEdit.SelectionStart";
_selstart = _myedit.getSelectionStart();
 //BA.debugLineNum = 769;BA.debugLine="ref.Target= MyEdit";
_ref.Target = (Object)(_myedit.getObject());
 //BA.debugLineNum = 770;BA.debugLine="SelEnd= ref.RunMethod(\"getSelectionEnd\")";
_selend = (int)(BA.ObjectToNumber(_ref.RunMethod("getSelectionEnd")));
 //BA.debugLineNum = 771;BA.debugLine="If SelStart = -1 OR SelEnd = -1 Then Return \"\" Else Return MyEdit.Text.SubString2(Min(SelStart, SelEnd), Max(SelStart, SelEnd))";
if (_selstart==-1 || _selend==-1) { 
if (true) return (Object)("");}
else {
if (true) return (Object)(_myedit.getText().substring((int) (__c.Min(_selstart,_selend)),(int) (__c.Max(_selstart,_selend))));};
 };
 };
 //BA.debugLineNum = 774;BA.debugLine="End Sub";
return null;
}
public Object  _getvalue(String _name) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _foundname = null;
anywheresoftware.b4a.objects.LabelWrapper _mylabel = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
de.watchkido.mama.viewmgr._comboitem _mycomboitem = null;
 //BA.debugLineNum = 808;BA.debugLine="Public Sub GetValue(Name As String) As Object";
 //BA.debugLineNum = 809;BA.debugLine="Dim FoundName As View";
_foundname = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 811;BA.debugLine="FoundName = NameMap.Get(Name)";
_foundname.setObject((android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 812;BA.debugLine="If FoundName.IsInitialized = False Then";
if (_foundname.IsInitialized()==__c.False) { 
 //BA.debugLineNum = 813;BA.debugLine="Return \"\"";
if (true) return (Object)("");
 }else {
 //BA.debugLineNum = 815;BA.debugLine="If FoundName Is Label Then";
if (_foundname.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 816;BA.debugLine="Dim myLabel As Label";
_mylabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 817;BA.debugLine="myLabel = FoundName";
_mylabel.setObject((android.widget.TextView)(_foundname.getObject()));
 //BA.debugLineNum = 818;BA.debugLine="If myLabel.Tag Is MyCombobox Then";
if (_mylabel.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 819;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 820;BA.debugLine="MCB = myLabel.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_mylabel.getTag());
 //BA.debugLineNum = 821;BA.debugLine="If MCB.CurSelection > -1 Then";
if (_mcb.CurSelection>-1) { 
 //BA.debugLineNum = 822;BA.debugLine="If MCB.Items.Get(MCB.CurSelection) Is ComboItem Then";
if (_mcb.Items.Get(_mcb.CurSelection) instanceof de.watchkido.mama.viewmgr._comboitem) { 
 //BA.debugLineNum = 823;BA.debugLine="Dim MyComboItem As ComboItem";
_mycomboitem = new de.watchkido.mama.viewmgr._comboitem();
 //BA.debugLineNum = 824;BA.debugLine="MyComboItem= MCB.Items.Get(MCB.CurSelection)";
_mycomboitem = (de.watchkido.mama.viewmgr._comboitem)(_mcb.Items.Get(_mcb.CurSelection));
 //BA.debugLineNum = 825;BA.debugLine="Return MyComboItem.Value";
if (true) return _mycomboitem.Value;
 }else {
 //BA.debugLineNum = 827;BA.debugLine="Return MCB.Items.Get(MCB.CurSelection)";
if (true) return _mcb.Items.Get(_mcb.CurSelection);
 };
 }else {
 //BA.debugLineNum = 830;BA.debugLine="Return \"\"";
if (true) return (Object)("");
 };
 }else {
 //BA.debugLineNum = 833;BA.debugLine="Return myLabel.Text";
if (true) return (Object)(_mylabel.getText());
 };
 }else {
 //BA.debugLineNum = 836;BA.debugLine="Return \"\"";
if (true) return (Object)("");
 };
 };
 //BA.debugLineNum = 839;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.ConcreteViewWrapper  _getview(String _name) throws Exception{
 //BA.debugLineNum = 962;BA.debugLine="Public Sub GetView(Name As String) As View";
 //BA.debugLineNum = 963;BA.debugLine="Return NameMap.Get(Name)";
if (true) return (anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 964;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.collections.Map  _getviews() throws Exception{
anywheresoftware.b4a.objects.collections.Map _newmap = null;
int _i = 0;
 //BA.debugLineNum = 951;BA.debugLine="Public Sub GetViews As Map";
 //BA.debugLineNum = 952;BA.debugLine="Dim newMap As Map";
_newmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 954;BA.debugLine="newMap.Initialize";
_newmap.Initialize();
 //BA.debugLineNum = 955;BA.debugLine="For i = 0 To NameMap.Size - 1";
{
final int step762 = 1;
final int limit762 = (int) (_namemap.getSize()-1);
for (_i = (int) (0); (step762 > 0 && _i <= limit762) || (step762 < 0 && _i >= limit762); _i = ((int)(0 + _i + step762))) {
 //BA.debugLineNum = 956;BA.debugLine="newMap.Put(NameMap.GetKeyAt(i), NameMap.GetValueAt(i))";
_newmap.Put(_namemap.GetKeyAt(_i),_namemap.GetValueAt(_i));
 }
};
 //BA.debugLineNum = 958;BA.debugLine="Return newMap";
if (true) return _newmap;
 //BA.debugLineNum = 959;BA.debugLine="End Sub";
return null;
}
public boolean  _ime_handleaction() throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _et = null;
de.watchkido.mama.viewmgr._mytextbox _mtb = null;
int _index = 0;
 //BA.debugLineNum = 652;BA.debugLine="Private Sub IME_HandleAction As Boolean";
 //BA.debugLineNum = 653;BA.debugLine="Dim ET As View";
_et = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 654;BA.debugLine="Dim MTB As MyTextbox";
_mtb = new de.watchkido.mama.viewmgr._mytextbox();
 //BA.debugLineNum = 655;BA.debugLine="Dim index As Int";
_index = 0;
 //BA.debugLineNum = 657;BA.debugLine="ET = Sender";
_et.setObject((android.view.View)(__c.Sender(ba)));
 //BA.debugLineNum = 658;BA.debugLine="MTB = ET.Tag";
_mtb = (de.watchkido.mama.viewmgr._mytextbox)(_et.getTag());
 //BA.debugLineNum = 659;BA.debugLine="index = MyViews.IndexOf(ET)";
_index = _myviews.IndexOf((Object)(_et.getObject()));
 //BA.debugLineNum = 660;BA.debugLine="If index > -1 Then";
if (_index>-1) { 
 //BA.debugLineNum = 661;BA.debugLine="Select Case MTB.ActionBtn";
switch (BA.switchObjectToInt(_mtb.ActionBtn,_actionbtn_next(),_actionbtn_previous())) {
case 0:
 //BA.debugLineNum = 663;BA.debugLine="index= index + 1";
_index = (int) (_index+1);
 //BA.debugLineNum = 664;BA.debugLine="Do While index < MyViews.Size";
while (_index<_myviews.getSize()) {
 //BA.debugLineNum = 665;BA.debugLine="ET= MyViews.Get(index)";
_et.setObject((android.view.View)(_myviews.Get(_index)));
 //BA.debugLineNum = 666;BA.debugLine="If ET.Enabled = True Then";
if (_et.getEnabled()==__c.True) { 
 //BA.debugLineNum = 667;BA.debugLine="ET.RequestFocus";
_et.RequestFocus();
 //BA.debugLineNum = 668;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 670;BA.debugLine="index= index + 1";
_index = (int) (_index+1);
 }
;
 break;
case 1:
 //BA.debugLineNum = 673;BA.debugLine="index= index - 1";
_index = (int) (_index-1);
 //BA.debugLineNum = 674;BA.debugLine="Do While index > -1";
while (_index>-1) {
 //BA.debugLineNum = 675;BA.debugLine="ET= MyViews.Get(index)";
_et.setObject((android.view.View)(_myviews.Get(_index)));
 //BA.debugLineNum = 676;BA.debugLine="If ET.Enabled = True Then";
if (_et.getEnabled()==__c.True) { 
 //BA.debugLineNum = 677;BA.debugLine="ET.RequestFocus";
_et.RequestFocus();
 //BA.debugLineNum = 678;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 680;BA.debugLine="index= index - 1";
_index = (int) (_index-1);
 }
;
 break;
default:
 //BA.debugLineNum = 683;BA.debugLine="If MTB.ActionSub.Length > 0 Then CallSubDelayed(MTB.ActionSubModule, MTB.ActionSub)";
if (_mtb.ActionSub.length()>0) { 
__c.CallSubDelayed(ba,_mtb.ActionSubModule,_mtb.ActionSub);};
 //BA.debugLineNum = 684;BA.debugLine="Return False";
if (true) return __c.False;
 break;
}
;
 //BA.debugLineNum = 686;BA.debugLine="Return True";
if (true) return __c.True;
 };
 //BA.debugLineNum = 688;BA.debugLine="End Sub";
return false;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.PanelWrapper _container,int _containerwidth,boolean _showminmaxwarn,boolean _viewscaling,boolean _overridefontscaling) throws Exception{
innerInitialize(_ba);
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
 //BA.debugLineNum = 30;BA.debugLine="Public Sub Initialize(Container As Panel, ContainerWidth As Int, ShowMinMaxWarn As Boolean, ViewScaling As Boolean, OverrideFontScaling As Boolean)";
 //BA.debugLineNum = 31;BA.debugLine="Dim Ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 33;BA.debugLine="MyViews.Initialize";
_myviews.Initialize();
 //BA.debugLineNum = 34;BA.debugLine="NameMap.Initialize";
_namemap.Initialize();
 //BA.debugLineNum = 35;BA.debugLine="IME.Initialize(\"IME\")";
_ime.Initialize("IME");
 //BA.debugLineNum = 36;BA.debugLine="ScaleLabel.Initialize(\"\")";
_scalelabel.Initialize(ba,"");
 //BA.debugLineNum = 37;BA.debugLine="ScaleLabel.Visible = False";
_scalelabel.setVisible(__c.False);
 //BA.debugLineNum = 38;BA.debugLine="Container.AddView(ScaleLabel, 0, 0, 1, 1)";
_container.AddView((android.view.View)(_scalelabel.getObject()),(int) (0),(int) (0),(int) (1),(int) (1));
 //BA.debugLineNum = 39;BA.debugLine="Padding = 5dip";
_padding = __c.DipToCurrent((int) (5));
 //BA.debugLineNum = 40;BA.debugLine="MinMaxWarn = ShowMinMaxWarn";
_minmaxwarn = _showminmaxwarn;
 //BA.debugLineNum = 41;BA.debugLine="ScaleViews = ViewScaling";
_scaleviews = _viewscaling;
 //BA.debugLineNum = 42;BA.debugLine="OverrideFontScale = OverrideFontScaling";
_overridefontscale = _overridefontscaling;
 //BA.debugLineNum = 43;BA.debugLine="Parent = Container";
_parent = _container;
 //BA.debugLineNum = 44;BA.debugLine="ParentWidth = ContainerWidth";
_parentwidth = _containerwidth;
 //BA.debugLineNum = 45;BA.debugLine="CurX = 5dip";
_curx = __c.DipToCurrent((int) (5));
 //BA.debugLineNum = 46;BA.debugLine="NextX = 5dip";
_nextx = __c.DipToCurrent((int) (5));
 //BA.debugLineNum = 47;BA.debugLine="CurY = 5dip";
_cury = __c.DipToCurrent((int) (5));
 //BA.debugLineNum = 48;BA.debugLine="NextY = 5dip";
_nexty = __c.DipToCurrent((int) (5));
 //BA.debugLineNum = 50;BA.debugLine="Ref.Target = Ref.GetContext";
_ref.Target = (Object)(_ref.GetContext(ba));
 //BA.debugLineNum = 51;BA.debugLine="Ref.Target = Ref.RunMethod(\"getResources\")";
_ref.Target = _ref.RunMethod("getResources");
 //BA.debugLineNum = 52;BA.debugLine="Ref.Target=Ref.RunMethod(\"getConfiguration\")";
_ref.Target = _ref.RunMethod("getConfiguration");
 //BA.debugLineNum = 53;BA.debugLine="FontScale= Ref.GetField(\"fontScale\")";
_fontscale = (float)(BA.ObjectToNumber(_ref.GetField("fontScale")));
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public boolean  _isvalid(String _name) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _foundname = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
anywheresoftware.b4a.objects.EditTextWrapper _myedit = null;
de.watchkido.mama.viewmgr._mytextbox _mtb = null;
 //BA.debugLineNum = 1035;BA.debugLine="Public Sub isValid(Name As String) As Boolean";
 //BA.debugLineNum = 1036;BA.debugLine="Dim FoundName As View";
_foundname = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 1037;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 1039;BA.debugLine="FoundName = NameMap.Get(Name)";
_foundname.setObject((android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 1040;BA.debugLine="If FoundName.IsInitialized = True Then";
if (_foundname.IsInitialized()==__c.True) { 
 //BA.debugLineNum = 1041;BA.debugLine="If FoundName Is EditText Then";
if (_foundname.getObjectOrNull() instanceof android.widget.EditText) { 
 //BA.debugLineNum = 1042;BA.debugLine="Dim myEdit As EditText";
_myedit = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 1043;BA.debugLine="Dim MTB As MyTextbox";
_mtb = new de.watchkido.mama.viewmgr._mytextbox();
 //BA.debugLineNum = 1045;BA.debugLine="myEdit = FoundName";
_myedit.setObject((android.widget.EditText)(_foundname.getObject()));
 //BA.debugLineNum = 1046;BA.debugLine="MTB = myEdit.Tag";
_mtb = (de.watchkido.mama.viewmgr._mytextbox)(_myedit.getTag());
 //BA.debugLineNum = 1047;BA.debugLine="Return myEdit.Text.Length >= MTB.MinChar";
if (true) return _myedit.getText().length()>=_mtb.MinChar;
 }else if(_foundname.getObjectOrNull() instanceof android.widget.TextView && _foundname.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 1049;BA.debugLine="MCB = FoundName.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_foundname.getTag());
 //BA.debugLineNum = 1050;BA.debugLine="Return MCB.CurSelection > -1";
if (true) return _mcb.CurSelection>-1;
 };
 };
 //BA.debugLineNum = 1053;BA.debugLine="Return False";
if (true) return __c.False;
 //BA.debugLineNum = 1054;BA.debugLine="End Sub";
return false;
}
public String  _label_click() throws Exception{
String _name = "";
anywheresoftware.b4a.objects.LabelWrapper _mycontrol = null;
anywheresoftware.b4a.objects.ConcreteViewWrapper _lv = null;
anywheresoftware.b4a.objects.AutoCompleteEditTextWrapper _act = null;
 //BA.debugLineNum = 295;BA.debugLine="Private Sub Label_Click";
 //BA.debugLineNum = 296;BA.debugLine="Dim Name As String";
_name = "";
 //BA.debugLineNum = 297;BA.debugLine="Dim MyControl As Label";
_mycontrol = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 298;BA.debugLine="Dim LV As View";
_lv = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 300;BA.debugLine="MyControl = Sender";
_mycontrol.setObject((android.widget.TextView)(__c.Sender(ba)));
 //BA.debugLineNum = 301;BA.debugLine="Name = MyControl.Tag";
_name = BA.ObjectToString(_mycontrol.getTag());
 //BA.debugLineNum = 302;BA.debugLine="If Name.Length > 0 Then";
if (_name.length()>0) { 
 //BA.debugLineNum = 303;BA.debugLine="LV = NameMap.Get(Name)";
_lv.setObject((android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 304;BA.debugLine="If LV.IsInitialized = True Then";
if (_lv.IsInitialized()==__c.True) { 
 //BA.debugLineNum = 305;BA.debugLine="If CurFocus = LV Then";
if ((_curfocus).equals(_lv)) { 
 //BA.debugLineNum = 306;BA.debugLine="If LV Is AutoCompleteEditText Then";
if (_lv.getObjectOrNull() instanceof android.widget.EditText) { 
 //BA.debugLineNum = 307;BA.debugLine="Dim act As AutoCompleteEditText";
_act = new anywheresoftware.b4a.objects.AutoCompleteEditTextWrapper();
 //BA.debugLineNum = 308;BA.debugLine="act = LV";
_act.setObject((android.widget.EditText)(_lv.getObject()));
 //BA.debugLineNum = 309;BA.debugLine="act.ShowDropDown";
_act.ShowDropDown();
 };
 }else {
 //BA.debugLineNum = 312;BA.debugLine="LV.RequestFocus";
_lv.RequestFocus();
 //BA.debugLineNum = 313;BA.debugLine="CurFocus = LV";
_curfocus = _lv;
 };
 };
 };
 //BA.debugLineNum = 317;BA.debugLine="End Sub";
return "";
}
public de.watchkido.mama.viewmgr._comboitem  _makecomboitem(String _text,Object _value) throws Exception{
de.watchkido.mama.viewmgr._comboitem _mycomboitem = null;
 //BA.debugLineNum = 528;BA.debugLine="Public Sub MakeComboItem(Text As String, Value As Object) As ComboItem";
 //BA.debugLineNum = 529;BA.debugLine="Dim MyComboItem As ComboItem";
_mycomboitem = new de.watchkido.mama.viewmgr._comboitem();
 //BA.debugLineNum = 531;BA.debugLine="MyComboItem.Initialize";
_mycomboitem.Initialize();
 //BA.debugLineNum = 532;BA.debugLine="MyComboItem.Text = Text";
_mycomboitem.Text = _text;
 //BA.debugLineNum = 533;BA.debugLine="MyComboItem.Value = Value";
_mycomboitem.Value = _value;
 //BA.debugLineNum = 535;BA.debugLine="Return MyComboItem";
if (true) return _mycomboitem;
 //BA.debugLineNum = 536;BA.debugLine="End Sub";
return null;
}
public String  _restorestate(anywheresoftware.b4a.objects.collections.Map _values) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _myview = null;
int _i = 0;
anywheresoftware.b4a.objects.LabelWrapper _mylabel = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
de.watchkido.mama.viewmgr._comboitem _mycomboitem = null;
 //BA.debugLineNum = 907;BA.debugLine="Public Sub RestoreState(Values As Map)";
 //BA.debugLineNum = 908;BA.debugLine="Dim myView As View";
_myview = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 910;BA.debugLine="For i = 0 To Values.Size - 1";
{
final int step722 = 1;
final int limit722 = (int) (_values.getSize()-1);
for (_i = (int) (0); (step722 > 0 && _i <= limit722) || (step722 < 0 && _i >= limit722); _i = ((int)(0 + _i + step722))) {
 //BA.debugLineNum = 911;BA.debugLine="myView = NameMap.Get(Values.GetKeyAt(i))";
_myview.setObject((android.view.View)(_namemap.Get(_values.GetKeyAt(_i))));
 //BA.debugLineNum = 912;BA.debugLine="If myView.IsInitialized Then";
if (_myview.IsInitialized()) { 
 //BA.debugLineNum = 913;BA.debugLine="If myView Is Label Then";
if (_myview.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 914;BA.debugLine="Dim myLabel As Label";
_mylabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 915;BA.debugLine="myLabel = myView";
_mylabel.setObject((android.widget.TextView)(_myview.getObject()));
 //BA.debugLineNum = 916;BA.debugLine="If myLabel.Tag Is MyCombobox Then";
if (_mylabel.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 917;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 918;BA.debugLine="MCB = myLabel.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_mylabel.getTag());
 //BA.debugLineNum = 919;BA.debugLine="If Values.GetValueAt(i) Is Int AND Values.GetValueAt(i) > -1 AND Values.GetValueAt(i) < MCB.Items.Size Then";
if (_values.GetValueAt(_i) instanceof Integer && (double)(BA.ObjectToNumber(_values.GetValueAt(_i)))>-1 && (double)(BA.ObjectToNumber(_values.GetValueAt(_i)))<_mcb.Items.getSize()) { 
 //BA.debugLineNum = 920;BA.debugLine="MCB.CurSelection= Values.GetValueAt(i)";
_mcb.CurSelection = (int)(BA.ObjectToNumber(_values.GetValueAt(_i)));
 //BA.debugLineNum = 921;BA.debugLine="If MCB.Items.Get(MCB.CurSelection) Is ComboItem Then";
if (_mcb.Items.Get(_mcb.CurSelection) instanceof de.watchkido.mama.viewmgr._comboitem) { 
 //BA.debugLineNum = 922;BA.debugLine="Dim MyComboItem As ComboItem";
_mycomboitem = new de.watchkido.mama.viewmgr._comboitem();
 //BA.debugLineNum = 923;BA.debugLine="MyComboItem= MCB.Items.Get(MCB.CurSelection)";
_mycomboitem = (de.watchkido.mama.viewmgr._comboitem)(_mcb.Items.Get(_mcb.CurSelection));
 //BA.debugLineNum = 924;BA.debugLine="myLabel.Text= MyComboItem.Text";
_mylabel.setText((Object)(_mycomboitem.Text));
 }else {
 //BA.debugLineNum = 926;BA.debugLine="myLabel.Text= MCB.Items.Get(MCB.CurSelection)";
_mylabel.setText(_mcb.Items.Get(_mcb.CurSelection));
 };
 }else {
 //BA.debugLineNum = 929;BA.debugLine="MCB.CurSelection= -1";
_mcb.CurSelection = (int) (-1);
 //BA.debugLineNum = 930;BA.debugLine="myLabel.Text = MCB.Prompt";
_mylabel.setText((Object)(_mcb.Prompt));
 };
 }else {
 //BA.debugLineNum = 933;BA.debugLine="myLabel.Text = Values.GetValueAt(i)";
_mylabel.setText(_values.GetValueAt(_i));
 };
 };
 };
 }
};
 //BA.debugLineNum = 938;BA.debugLine="End Sub";
return "";
}
public String  _restorestatedb(anywheresoftware.b4a.sql.SQL.CursorWrapper _dbcursor) throws Exception{
int _i = 0;
 //BA.debugLineNum = 941;BA.debugLine="Public Sub RestoreStateDB(dbCursor As Cursor)";
 //BA.debugLineNum = 942;BA.debugLine="If dbCursor.RowCount = 1 Then";
if (_dbcursor.getRowCount()==1) { 
 //BA.debugLineNum = 943;BA.debugLine="dbCursor.Position = 0";
_dbcursor.setPosition((int) (0));
 //BA.debugLineNum = 944;BA.debugLine="For i = 0 To dbCursor.ColumnCount - 1";
{
final int step754 = 1;
final int limit754 = (int) (_dbcursor.getColumnCount()-1);
for (_i = (int) (0); (step754 > 0 && _i <= limit754) || (step754 < 0 && _i >= limit754); _i = ((int)(0 + _i + step754))) {
 //BA.debugLineNum = 945;BA.debugLine="SetText(dbCursor.GetColumnName(i), dbCursor.GetString2(i))";
_settext(_dbcursor.GetColumnName(_i),(Object)(_dbcursor.GetString2(_i)));
 }
};
 };
 //BA.debugLineNum = 948;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.collections.Map  _savestate() throws Exception{
anywheresoftware.b4a.objects.collections.Map _newmap = null;
Object _curvalue = null;
int _i = 0;
anywheresoftware.b4a.objects.LabelWrapper _mylabel = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
 //BA.debugLineNum = 882;BA.debugLine="Public Sub SaveState As Map";
 //BA.debugLineNum = 883;BA.debugLine="Dim newMap As Map";
_newmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 884;BA.debugLine="Dim curValue As Object";
_curvalue = new Object();
 //BA.debugLineNum = 886;BA.debugLine="newMap.Initialize";
_newmap.Initialize();
 //BA.debugLineNum = 887;BA.debugLine="For i = 0 To NameMap.Size - 1";
{
final int step702 = 1;
final int limit702 = (int) (_namemap.getSize()-1);
for (_i = (int) (0); (step702 > 0 && _i <= limit702) || (step702 < 0 && _i >= limit702); _i = ((int)(0 + _i + step702))) {
 //BA.debugLineNum = 888;BA.debugLine="If NameMap.GetValueAt(i) Is Label Then";
if (_namemap.GetValueAt(_i) instanceof android.widget.TextView) { 
 //BA.debugLineNum = 889;BA.debugLine="Dim myLabel As Label";
_mylabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 890;BA.debugLine="myLabel = NameMap.GetValueAt(i)";
_mylabel.setObject((android.widget.TextView)(_namemap.GetValueAt(_i)));
 //BA.debugLineNum = 891;BA.debugLine="If myLabel.Tag Is MyCombobox Then";
if (_mylabel.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 892;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 893;BA.debugLine="MCB = myLabel.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_mylabel.getTag());
 //BA.debugLineNum = 894;BA.debugLine="curValue = MCB.CurSelection";
_curvalue = (Object)(_mcb.CurSelection);
 }else {
 //BA.debugLineNum = 896;BA.debugLine="curValue = myLabel.Text";
_curvalue = (Object)(_mylabel.getText());
 };
 }else {
 //BA.debugLineNum = 899;BA.debugLine="curValue = \"\"";
_curvalue = (Object)("");
 };
 //BA.debugLineNum = 901;BA.debugLine="newMap.Put(NameMap.GetKeyAt(i), curValue)";
_newmap.Put(_namemap.GetKeyAt(_i),_curvalue);
 }
};
 //BA.debugLineNum = 903;BA.debugLine="Return newMap";
if (true) return _newmap;
 //BA.debugLineNum = 904;BA.debugLine="End Sub";
return null;
}
public String  _setcurselection(String _name,int _index) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _foundname = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
anywheresoftware.b4a.objects.LabelWrapper _mylabel = null;
de.watchkido.mama.viewmgr._comboitem _mycomboitem = null;
 //BA.debugLineNum = 777;BA.debugLine="Public Sub SetCurSelection(Name As String, Index As Int)";
 //BA.debugLineNum = 778;BA.debugLine="Dim FoundName As View";
_foundname = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 780;BA.debugLine="FoundName = NameMap.Get(Name)";
_foundname.setObject((android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 781;BA.debugLine="If FoundName.IsInitialized = True Then";
if (_foundname.IsInitialized()==__c.True) { 
 //BA.debugLineNum = 782;BA.debugLine="If FoundName.Tag Is MyCombobox Then";
if (_foundname.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 783;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 784;BA.debugLine="Dim MyLabel As Label";
_mylabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 785;BA.debugLine="MyLabel= FoundName";
_mylabel.setObject((android.widget.TextView)(_foundname.getObject()));
 //BA.debugLineNum = 786;BA.debugLine="MCB= FoundName.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_foundname.getTag());
 //BA.debugLineNum = 787;BA.debugLine="If Index > -1 AND Index < MCB.Items.Size Then";
if (_index>-1 && _index<_mcb.Items.getSize()) { 
 //BA.debugLineNum = 788;BA.debugLine="MCB.CurSelection= Index";
_mcb.CurSelection = _index;
 //BA.debugLineNum = 789;BA.debugLine="If MCB.items.Get(Index) Is ComboItem Then";
if (_mcb.Items.Get(_index) instanceof de.watchkido.mama.viewmgr._comboitem) { 
 //BA.debugLineNum = 790;BA.debugLine="Dim MyComboItem As ComboItem";
_mycomboitem = new de.watchkido.mama.viewmgr._comboitem();
 //BA.debugLineNum = 791;BA.debugLine="MyComboItem = MCB.items.Get(Index)";
_mycomboitem = (de.watchkido.mama.viewmgr._comboitem)(_mcb.Items.Get(_index));
 //BA.debugLineNum = 792;BA.debugLine="MyLabel.Text= MyComboItem.Text";
_mylabel.setText((Object)(_mycomboitem.Text));
 //BA.debugLineNum = 793;BA.debugLine="If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, Index, MyComboItem.Value)";
if (_mcb.ActionSub.length()>0) { 
__c.CallSubDelayed3(ba,_mcb.ActionSubModule,_mcb.ActionSub,(Object)(_index),_mycomboitem.Value);};
 }else {
 //BA.debugLineNum = 795;BA.debugLine="MyLabel.Text= MCB.items.Get(Index)";
_mylabel.setText(_mcb.Items.Get(_index));
 //BA.debugLineNum = 796;BA.debugLine="If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, Index, MCB.items.Get(Index))";
if (_mcb.ActionSub.length()>0) { 
__c.CallSubDelayed3(ba,_mcb.ActionSubModule,_mcb.ActionSub,(Object)(_index),_mcb.Items.Get(_index));};
 };
 }else {
 //BA.debugLineNum = 799;BA.debugLine="MCB.CurSelection= -1";
_mcb.CurSelection = (int) (-1);
 //BA.debugLineNum = 800;BA.debugLine="MyLabel.Text= MCB.Prompt";
_mylabel.setText((Object)(_mcb.Prompt));
 //BA.debugLineNum = 801;BA.debugLine="If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, -1, \"\")";
if (_mcb.ActionSub.length()>0) { 
__c.CallSubDelayed3(ba,_mcb.ActionSubModule,_mcb.ActionSub,(Object)(-1),(Object)(""));};
 };
 };
 };
 //BA.debugLineNum = 805;BA.debugLine="End Sub";
return "";
}
public String  _setitems(String _name,anywheresoftware.b4a.objects.collections.List _items) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _foundname = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
anywheresoftware.b4a.objects.LabelWrapper _mylabel = null;
 //BA.debugLineNum = 736;BA.debugLine="Public Sub SetItems(Name As String, Items As List)";
 //BA.debugLineNum = 737;BA.debugLine="Dim FoundName As View";
_foundname = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 739;BA.debugLine="FoundName = NameMap.Get(Name)";
_foundname.setObject((android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 740;BA.debugLine="If FoundName.IsInitialized = True AND FoundName.Tag Is MyCombobox Then";
if (_foundname.IsInitialized()==__c.True && _foundname.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 741;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 742;BA.debugLine="Dim MyLabel As Label";
_mylabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 743;BA.debugLine="MyLabel= FoundName";
_mylabel.setObject((android.widget.TextView)(_foundname.getObject()));
 //BA.debugLineNum = 744;BA.debugLine="MCB = FoundName.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_foundname.getTag());
 //BA.debugLineNum = 745;BA.debugLine="MCB.Items.Clear";
_mcb.Items.Clear();
 //BA.debugLineNum = 746;BA.debugLine="MCB.CurSelection= -1";
_mcb.CurSelection = (int) (-1);
 //BA.debugLineNum = 747;BA.debugLine="MyLabel.Text = MCB.Prompt";
_mylabel.setText((Object)(_mcb.Prompt));
 //BA.debugLineNum = 748;BA.debugLine="If Items.IsInitialized AND Items.Size > 0 Then MCB.Items.AddAll(Items)";
if (_items.IsInitialized() && _items.getSize()>0) { 
_mcb.Items.AddAll(_items);};
 };
 //BA.debugLineNum = 750;BA.debugLine="End Sub";
return "";
}
public Object  _setninepatchdrawable(String _imagename) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _id = 0;
 //BA.debugLineNum = 1023;BA.debugLine="Private Sub SetNinePatchDrawable(ImageName As String) As Object";
 //BA.debugLineNum = 1024;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 1025;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 1026;BA.debugLine="Dim id As Int";
_id = 0;
 //BA.debugLineNum = 1027;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.BA\", \"packageName\")";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 1028;BA.debugLine="id = r.GetStaticField(package & \".R$drawable\", ImageName)";
_id = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$drawable",_imagename)));
 //BA.debugLineNum = 1029;BA.debugLine="r.Target = r.GetContext";
_r.Target = (Object)(_r.GetContext(ba));
 //BA.debugLineNum = 1030;BA.debugLine="r.Target = r.RunMethod(\"getResources\")";
_r.Target = _r.RunMethod("getResources");
 //BA.debugLineNum = 1031;BA.debugLine="Return r.RunMethod2(\"getDrawable\", id, \"java.lang.int\")";
if (true) return _r.RunMethod2("getDrawable",BA.NumberToString(_id),"java.lang.int");
 //BA.debugLineNum = 1032;BA.debugLine="End Sub";
return null;
}
public String  _settext(String _name,Object _value) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _foundname = null;
anywheresoftware.b4a.objects.LabelWrapper _mylabel = null;
de.watchkido.mama.viewmgr._mycombobox _mcb = null;
de.watchkido.mama.viewmgr._comboitem _mycomboitem = null;
int _i = 0;
 //BA.debugLineNum = 842;BA.debugLine="Public Sub SetText(Name As String, Value As Object)";
 //BA.debugLineNum = 843;BA.debugLine="Dim FoundName As View";
_foundname = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 845;BA.debugLine="FoundName = NameMap.Get(Name)";
_foundname.setObject((android.view.View)(_namemap.Get((Object)(_name))));
 //BA.debugLineNum = 846;BA.debugLine="If FoundName.IsInitialized = True Then";
if (_foundname.IsInitialized()==__c.True) { 
 //BA.debugLineNum = 847;BA.debugLine="If FoundName Is Label Then";
if (_foundname.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 848;BA.debugLine="Dim myLabel As Label";
_mylabel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 849;BA.debugLine="myLabel = FoundName";
_mylabel.setObject((android.widget.TextView)(_foundname.getObject()));
 //BA.debugLineNum = 850;BA.debugLine="If myLabel.Tag Is MyCombobox Then";
if (_mylabel.getTag() instanceof de.watchkido.mama.viewmgr._mycombobox) { 
 //BA.debugLineNum = 851;BA.debugLine="Dim MCB As MyCombobox";
_mcb = new de.watchkido.mama.viewmgr._mycombobox();
 //BA.debugLineNum = 852;BA.debugLine="Dim MyComboItem As ComboItem";
_mycomboitem = new de.watchkido.mama.viewmgr._comboitem();
 //BA.debugLineNum = 853;BA.debugLine="MCB = myLabel.Tag";
_mcb = (de.watchkido.mama.viewmgr._mycombobox)(_mylabel.getTag());
 //BA.debugLineNum = 854;BA.debugLine="MCB.CurSelection= -1";
_mcb.CurSelection = (int) (-1);
 //BA.debugLineNum = 855;BA.debugLine="For i= 0 To MCB.Items.Size - 1";
{
final int step673 = 1;
final int limit673 = (int) (_mcb.Items.getSize()-1);
for (_i = (int) (0); (step673 > 0 && _i <= limit673) || (step673 < 0 && _i >= limit673); _i = ((int)(0 + _i + step673))) {
 //BA.debugLineNum = 856;BA.debugLine="If MCB.Items.Get(i) Is ComboItem Then";
if (_mcb.Items.Get(_i) instanceof de.watchkido.mama.viewmgr._comboitem) { 
 //BA.debugLineNum = 857;BA.debugLine="MyComboItem= MCB.Items.Get(i)";
_mycomboitem = (de.watchkido.mama.viewmgr._comboitem)(_mcb.Items.Get(_i));
 //BA.debugLineNum = 858;BA.debugLine="If MyComboItem.Value = Value OR (IsNumber(Value) AND MyComboItem.Value = Bit.ParseInt(Value, 10)) Then";
if ((_mycomboitem.Value).equals(_value) || (__c.IsNumber(BA.ObjectToString(_value)) && (_mycomboitem.Value).equals((Object)(__c.Bit.ParseInt(BA.ObjectToString(_value),(int) (10)))))) { 
 //BA.debugLineNum = 859;BA.debugLine="MCB.CurSelection= i";
_mcb.CurSelection = _i;
 //BA.debugLineNum = 860;BA.debugLine="myLabel.Text= MyComboItem.Text";
_mylabel.setText((Object)(_mycomboitem.Text));
 //BA.debugLineNum = 861;BA.debugLine="If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, i, MyComboItem.Value)";
if (_mcb.ActionSub.length()>0) { 
__c.CallSubDelayed3(ba,_mcb.ActionSubModule,_mcb.ActionSub,(Object)(_i),_mycomboitem.Value);};
 //BA.debugLineNum = 862;BA.debugLine="Exit";
if (true) break;
 };
 }else {
 //BA.debugLineNum = 865;BA.debugLine="If MCB.Items.Get(i) = Value Then";
if ((_mcb.Items.Get(_i)).equals(_value)) { 
 //BA.debugLineNum = 866;BA.debugLine="MCB.CurSelection = i";
_mcb.CurSelection = _i;
 //BA.debugLineNum = 867;BA.debugLine="myLabel.Text = Value";
_mylabel.setText(_value);
 //BA.debugLineNum = 868;BA.debugLine="If MCB.ActionSub.Length > 0 Then CallSubDelayed3(MCB.ActionSubModule, MCB.ActionSub, i, MCB.items.Get(i))";
if (_mcb.ActionSub.length()>0) { 
__c.CallSubDelayed3(ba,_mcb.ActionSubModule,_mcb.ActionSub,(Object)(_i),_mcb.Items.Get(_i));};
 //BA.debugLineNum = 869;BA.debugLine="Exit";
if (true) break;
 };
 };
 }
};
 //BA.debugLineNum = 873;BA.debugLine="If MCB.CurSelection = -1 Then myLabel.Text= MCB.Prompt";
if (_mcb.CurSelection==-1) { 
_mylabel.setText((Object)(_mcb.Prompt));};
 }else {
 //BA.debugLineNum = 875;BA.debugLine="myLabel.Text = Value";
_mylabel.setText(_value);
 };
 };
 };
 //BA.debugLineNum = 879;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
ba.sharedProcessBA.sender = sender;
return BA.SubDelegator.SubNotFound;
}
}
