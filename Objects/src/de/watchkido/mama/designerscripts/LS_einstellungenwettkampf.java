package de.watchkido.mama.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_einstellungenwettkampf{

public static void LS_480x320_1(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
String _delta="";
String _rate="";
String _scale="";
//BA.debugLineNum = 3;BA.debugLine="delta = ((100%x + 100%y) / (320dip + 480dip) - 1)"[einstellungenwettkampf/480x320,scale=1]
_delta = BA.NumberToString((((100d / 100 * width)+(100d / 100 * height))/((320d * scale)+(480d * scale))-1d));
//BA.debugLineNum = 4;BA.debugLine="rate = 0.2"[einstellungenwettkampf/480x320,scale=1]
_rate = "0.2";
//BA.debugLineNum = 5;BA.debugLine="scale = 1 + rate * delta"[einstellungenwettkampf/480x320,scale=1]
_scale = BA.NumberToString(1d+Double.parseDouble(_rate)*Double.parseDouble(_delta));
//BA.debugLineNum = 7;BA.debugLine="Label1.Bottom = 10%y"[einstellungenwettkampf/480x320,scale=1]
views.get("label1").setTop((int)((10d / 100 * height) - (views.get("label1").getHeight())));
//BA.debugLineNum = 8;BA.debugLine="Label1.Left = 0%x"[einstellungenwettkampf/480x320,scale=1]
views.get("label1").setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 9;BA.debugLine="Label1.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("label1").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 10;BA.debugLine="Label1.Width = 100%x"[einstellungenwettkampf/480x320,scale=1]
views.get("label1").setWidth((int)((100d / 100 * width)));
//BA.debugLineNum = 11;BA.debugLine="Label1.Top = 0%y"[einstellungenwettkampf/480x320,scale=1]
views.get("label1").setTop((int)((0d / 100 * height)));
//BA.debugLineNum = 12;BA.debugLine="Label1.TextSize = Label1.TextSize * scale"[einstellungenwettkampf/480x320,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("label1")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("label1")).getTextSize())*Double.parseDouble(_scale)));
//BA.debugLineNum = 14;BA.debugLine="lblRunden.Bottom = 10%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblrunden").setTop((int)((10d / 100 * height) - (views.get("lblrunden").getHeight())));
//BA.debugLineNum = 15;BA.debugLine="lblRunden.Left = 5%x"[einstellungenwettkampf/480x320,scale=1]
views.get("lblrunden").setLeft((int)((5d / 100 * width)));
//BA.debugLineNum = 16;BA.debugLine="lblRunden.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblrunden").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 17;BA.debugLine="lblRunden.Width = 40%x"[einstellungenwettkampf/480x320,scale=1]
views.get("lblrunden").setWidth((int)((40d / 100 * width)));
//BA.debugLineNum = 18;BA.debugLine="lblRunden.Top = 100%y - 35%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblrunden").setTop((int)((100d / 100 * height)-(35d / 100 * height)));
//BA.debugLineNum = 19;BA.debugLine="lblRunden.TextSize = lblRunden.TextSize * scale"[einstellungenwettkampf/480x320,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblrunden")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblrunden")).getTextSize())*Double.parseDouble(_scale)));
//BA.debugLineNum = 21;BA.debugLine="spnRunden.Bottom = 100%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnrunden").setTop((int)((100d / 100 * height) - (views.get("spnrunden").getHeight())));
//BA.debugLineNum = 22;BA.debugLine="spnRunden.Left = 50%x"[einstellungenwettkampf/480x320,scale=1]
views.get("spnrunden").setLeft((int)((50d / 100 * width)));
//BA.debugLineNum = 23;BA.debugLine="spnRunden.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnrunden").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 24;BA.debugLine="spnRunden.Width = 45%x"[einstellungenwettkampf/480x320,scale=1]
views.get("spnrunden").setWidth((int)((45d / 100 * width)));
//BA.debugLineNum = 25;BA.debugLine="spnRunden.Top = 100%y - 35%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnrunden").setTop((int)((100d / 100 * height)-(35d / 100 * height)));
//BA.debugLineNum = 28;BA.debugLine="lblPause.Bottom = 10%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblpause").setTop((int)((10d / 100 * height) - (views.get("lblpause").getHeight())));
//BA.debugLineNum = 29;BA.debugLine="lblPause.Left = 5%x"[einstellungenwettkampf/480x320,scale=1]
views.get("lblpause").setLeft((int)((5d / 100 * width)));
//BA.debugLineNum = 30;BA.debugLine="lblPause.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblpause").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 31;BA.debugLine="lblPause.Width = 40%x"[einstellungenwettkampf/480x320,scale=1]
views.get("lblpause").setWidth((int)((40d / 100 * width)));
//BA.debugLineNum = 32;BA.debugLine="lblPause.Top = 100%y - 50%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblpause").setTop((int)((100d / 100 * height)-(50d / 100 * height)));
//BA.debugLineNum = 33;BA.debugLine="lblPause.TextSize = lblPause.TextSize * scale"[einstellungenwettkampf/480x320,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblpause")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblpause")).getTextSize())*Double.parseDouble(_scale)));
//BA.debugLineNum = 35;BA.debugLine="spnPause.Bottom = 100%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnpause").setTop((int)((100d / 100 * height) - (views.get("spnpause").getHeight())));
//BA.debugLineNum = 36;BA.debugLine="spnPause.Left = 50%x"[einstellungenwettkampf/480x320,scale=1]
views.get("spnpause").setLeft((int)((50d / 100 * width)));
//BA.debugLineNum = 37;BA.debugLine="spnPause.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnpause").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 38;BA.debugLine="spnPause.Width = 45%x"[einstellungenwettkampf/480x320,scale=1]
views.get("spnpause").setWidth((int)((45d / 100 * width)));
//BA.debugLineNum = 39;BA.debugLine="spnPause.Top = 100%y - 50%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnpause").setTop((int)((100d / 100 * height)-(50d / 100 * height)));
//BA.debugLineNum = 41;BA.debugLine="lblKampfzeit.Bottom = 10%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblkampfzeit").setTop((int)((10d / 100 * height) - (views.get("lblkampfzeit").getHeight())));
//BA.debugLineNum = 42;BA.debugLine="lblKampfzeit.Left = 5%x"[einstellungenwettkampf/480x320,scale=1]
views.get("lblkampfzeit").setLeft((int)((5d / 100 * width)));
//BA.debugLineNum = 43;BA.debugLine="lblKampfzeit.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblkampfzeit").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 44;BA.debugLine="lblKampfzeit.Width = 40%x"[einstellungenwettkampf/480x320,scale=1]
views.get("lblkampfzeit").setWidth((int)((40d / 100 * width)));
//BA.debugLineNum = 45;BA.debugLine="lblKampfzeit.Top = 100%y - 65%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lblkampfzeit").setTop((int)((100d / 100 * height)-(65d / 100 * height)));
//BA.debugLineNum = 46;BA.debugLine="lblKampfzeit.TextSize = lblKampfzeit.TextSize * scale"[einstellungenwettkampf/480x320,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).getTextSize())*Double.parseDouble(_scale)));
//BA.debugLineNum = 48;BA.debugLine="spnKampfzeit.Bottom = 100%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnkampfzeit").setTop((int)((100d / 100 * height) - (views.get("spnkampfzeit").getHeight())));
//BA.debugLineNum = 49;BA.debugLine="spnKampfzeit.Left = 50%x"[einstellungenwettkampf/480x320,scale=1]
views.get("spnkampfzeit").setLeft((int)((50d / 100 * width)));
//BA.debugLineNum = 50;BA.debugLine="spnKampfzeit.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnkampfzeit").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 51;BA.debugLine="spnKampfzeit.Width = 45%x"[einstellungenwettkampf/480x320,scale=1]
views.get("spnkampfzeit").setWidth((int)((45d / 100 * width)));
//BA.debugLineNum = 52;BA.debugLine="spnKampfzeit.Top = 100%y - 65%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spnkampfzeit").setTop((int)((100d / 100 * height)-(65d / 100 * height)));
//BA.debugLineNum = 54;BA.debugLine="lblTimedelay.Bottom = 10%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lbltimedelay").setTop((int)((10d / 100 * height) - (views.get("lbltimedelay").getHeight())));
//BA.debugLineNum = 55;BA.debugLine="lblTimedelay.Left = 5%x"[einstellungenwettkampf/480x320,scale=1]
views.get("lbltimedelay").setLeft((int)((5d / 100 * width)));
//BA.debugLineNum = 56;BA.debugLine="lblTimedelay.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lbltimedelay").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 57;BA.debugLine="lblTimedelay.Width = 40%x"[einstellungenwettkampf/480x320,scale=1]
views.get("lbltimedelay").setWidth((int)((40d / 100 * width)));
//BA.debugLineNum = 58;BA.debugLine="lblTimedelay.Top = 100%y - 80%y"[einstellungenwettkampf/480x320,scale=1]
views.get("lbltimedelay").setTop((int)((100d / 100 * height)-(80d / 100 * height)));
//BA.debugLineNum = 59;BA.debugLine="lblTimedelay.TextSize = lblKampfzeit.TextSize * scale"[einstellungenwettkampf/480x320,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lbltimedelay")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).getTextSize())*Double.parseDouble(_scale)));
//BA.debugLineNum = 61;BA.debugLine="spnTimedelay.Bottom = 100%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spntimedelay").setTop((int)((100d / 100 * height) - (views.get("spntimedelay").getHeight())));
//BA.debugLineNum = 62;BA.debugLine="spnTimedelay.Left = 50%x"[einstellungenwettkampf/480x320,scale=1]
views.get("spntimedelay").setLeft((int)((50d / 100 * width)));
//BA.debugLineNum = 63;BA.debugLine="spnTimedelay.Height = 15%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spntimedelay").setHeight((int)((15d / 100 * height)));
//BA.debugLineNum = 64;BA.debugLine="spnTimedelay.Width = 45%x"[einstellungenwettkampf/480x320,scale=1]
views.get("spntimedelay").setWidth((int)((45d / 100 * width)));
//BA.debugLineNum = 65;BA.debugLine="spnTimedelay.Top = 100%y - 80%y"[einstellungenwettkampf/480x320,scale=1]
views.get("spntimedelay").setTop((int)((100d / 100 * height)-(80d / 100 * height)));
//BA.debugLineNum = 67;BA.debugLine="btnladen.Bottom = 100%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnladen").setTop((int)((100d / 100 * height) - (views.get("btnladen").getHeight())));
//BA.debugLineNum = 68;BA.debugLine="btnladen.Left = 3%x"[einstellungenwettkampf/480x320,scale=1]
views.get("btnladen").setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 69;BA.debugLine="btnladen.Height = 20%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnladen").setHeight((int)((20d / 100 * height)));
//BA.debugLineNum = 70;BA.debugLine="btnladen.Width = 30%x"[einstellungenwettkampf/480x320,scale=1]
views.get("btnladen").setWidth((int)((30d / 100 * width)));
//BA.debugLineNum = 71;BA.debugLine="btnladen.Top = 100%y - 20%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnladen").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
//BA.debugLineNum = 72;BA.debugLine="btnladen.TextSize = btnladen.TextSize * scale"[einstellungenwettkampf/480x320,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).getTextSize())*Double.parseDouble(_scale)));
//BA.debugLineNum = 74;BA.debugLine="btnWeiter.Bottom = 100%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnweiter").setTop((int)((100d / 100 * height) - (views.get("btnweiter").getHeight())));
//BA.debugLineNum = 75;BA.debugLine="btnWeiter.Left = 34.5%x"[einstellungenwettkampf/480x320,scale=1]
views.get("btnweiter").setLeft((int)((34.5d / 100 * width)));
//BA.debugLineNum = 76;BA.debugLine="btnWeiter.Height = 20%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnweiter").setHeight((int)((20d / 100 * height)));
//BA.debugLineNum = 77;BA.debugLine="btnWeiter.Width = 30%x"[einstellungenwettkampf/480x320,scale=1]
views.get("btnweiter").setWidth((int)((30d / 100 * width)));
//BA.debugLineNum = 78;BA.debugLine="btnWeiter.Top = 100%y - 20%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnweiter").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
//BA.debugLineNum = 79;BA.debugLine="btnWeiter.TextSize = btnWeiter.TextSize * scale"[einstellungenwettkampf/480x320,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnweiter")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnweiter")).getTextSize())*Double.parseDouble(_scale)));
//BA.debugLineNum = 81;BA.debugLine="btnSpeichern.Bottom = 100%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnspeichern").setTop((int)((100d / 100 * height) - (views.get("btnspeichern").getHeight())));
//BA.debugLineNum = 82;BA.debugLine="btnSpeichern.Left = 66%x"[einstellungenwettkampf/480x320,scale=1]
views.get("btnspeichern").setLeft((int)((66d / 100 * width)));
//BA.debugLineNum = 83;BA.debugLine="btnSpeichern.Height = 20%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnspeichern").setHeight((int)((20d / 100 * height)));
//BA.debugLineNum = 84;BA.debugLine="btnSpeichern.Width = 30%x"[einstellungenwettkampf/480x320,scale=1]
views.get("btnspeichern").setWidth((int)((30d / 100 * width)));
//BA.debugLineNum = 85;BA.debugLine="btnSpeichern.Top = 100%y - 20%y"[einstellungenwettkampf/480x320,scale=1]
views.get("btnspeichern").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
//BA.debugLineNum = 86;BA.debugLine="btnSpeichern.TextSize = btnSpeichern.TextSize * scale"[einstellungenwettkampf/480x320,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnspeichern")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnspeichern")).getTextSize())*Double.parseDouble(_scale)));

}
public static void LS_800x480_1(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
String _delta="";
String _rate="";
String _scale="";
_delta = BA.NumberToString((((100d / 100 * width)+(100d / 100 * height))/((320d * scale)+(480d * scale))-1d));
_rate = "0.2";
_scale = BA.NumberToString(1d+Double.parseDouble(_rate)*Double.parseDouble(_delta));
views.get("label1").setTop((int)((10d / 100 * height) - (views.get("label1").getHeight())));
views.get("label1").setLeft((int)((0d / 100 * width)));
views.get("label1").setHeight((int)((15d / 100 * height)));
views.get("label1").setWidth((int)((100d / 100 * width)));
views.get("label1").setTop((int)((0d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("label1")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("label1")).getTextSize())*Double.parseDouble(_scale)));
views.get("lblrunden").setTop((int)((10d / 100 * height) - (views.get("lblrunden").getHeight())));
views.get("lblrunden").setLeft((int)((5d / 100 * width)));
views.get("lblrunden").setHeight((int)((15d / 100 * height)));
views.get("lblrunden").setWidth((int)((40d / 100 * width)));
views.get("lblrunden").setTop((int)((100d / 100 * height)-(35d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblrunden")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblrunden")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnrunden").setTop((int)((100d / 100 * height) - (views.get("spnrunden").getHeight())));
views.get("spnrunden").setLeft((int)((50d / 100 * width)));
views.get("spnrunden").setHeight((int)((15d / 100 * height)));
views.get("spnrunden").setWidth((int)((45d / 100 * width)));
views.get("spnrunden").setTop((int)((100d / 100 * height)-(35d / 100 * height)));
views.get("lblpause").setTop((int)((10d / 100 * height) - (views.get("lblpause").getHeight())));
views.get("lblpause").setLeft((int)((5d / 100 * width)));
views.get("lblpause").setHeight((int)((15d / 100 * height)));
views.get("lblpause").setWidth((int)((40d / 100 * width)));
views.get("lblpause").setTop((int)((100d / 100 * height)-(50d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblpause")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblpause")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnpause").setTop((int)((100d / 100 * height) - (views.get("spnpause").getHeight())));
views.get("spnpause").setLeft((int)((50d / 100 * width)));
views.get("spnpause").setHeight((int)((15d / 100 * height)));
views.get("spnpause").setWidth((int)((45d / 100 * width)));
views.get("spnpause").setTop((int)((100d / 100 * height)-(50d / 100 * height)));
views.get("lblkampfzeit").setTop((int)((10d / 100 * height) - (views.get("lblkampfzeit").getHeight())));
views.get("lblkampfzeit").setLeft((int)((5d / 100 * width)));
views.get("lblkampfzeit").setHeight((int)((15d / 100 * height)));
views.get("lblkampfzeit").setWidth((int)((40d / 100 * width)));
views.get("lblkampfzeit").setTop((int)((100d / 100 * height)-(65d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnkampfzeit").setTop((int)((100d / 100 * height) - (views.get("spnkampfzeit").getHeight())));
views.get("spnkampfzeit").setLeft((int)((50d / 100 * width)));
views.get("spnkampfzeit").setHeight((int)((15d / 100 * height)));
views.get("spnkampfzeit").setWidth((int)((45d / 100 * width)));
views.get("spnkampfzeit").setTop((int)((100d / 100 * height)-(65d / 100 * height)));
views.get("lbltimedelay").setTop((int)((10d / 100 * height) - (views.get("lbltimedelay").getHeight())));
views.get("lbltimedelay").setLeft((int)((5d / 100 * width)));
views.get("lbltimedelay").setHeight((int)((15d / 100 * height)));
views.get("lbltimedelay").setWidth((int)((40d / 100 * width)));
views.get("lbltimedelay").setTop((int)((100d / 100 * height)-(80d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lbltimedelay")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).getTextSize())*Double.parseDouble(_scale)));
views.get("spntimedelay").setTop((int)((100d / 100 * height) - (views.get("spntimedelay").getHeight())));
views.get("spntimedelay").setLeft((int)((50d / 100 * width)));
views.get("spntimedelay").setHeight((int)((15d / 100 * height)));
views.get("spntimedelay").setWidth((int)((45d / 100 * width)));
views.get("spntimedelay").setTop((int)((100d / 100 * height)-(80d / 100 * height)));
views.get("btnladen").setTop((int)((100d / 100 * height) - (views.get("btnladen").getHeight())));
views.get("btnladen").setLeft((int)((3d / 100 * width)));
views.get("btnladen").setHeight((int)((20d / 100 * height)));
views.get("btnladen").setWidth((int)((30d / 100 * width)));
views.get("btnladen").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).getTextSize())*Double.parseDouble(_scale)));
views.get("btnweiter").setTop((int)((100d / 100 * height) - (views.get("btnweiter").getHeight())));
views.get("btnweiter").setLeft((int)((34.5d / 100 * width)));
views.get("btnweiter").setHeight((int)((20d / 100 * height)));
views.get("btnweiter").setWidth((int)((30d / 100 * width)));
views.get("btnweiter").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnweiter")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnweiter")).getTextSize())*Double.parseDouble(_scale)));
views.get("btnspeichern").setTop((int)((100d / 100 * height) - (views.get("btnspeichern").getHeight())));
views.get("btnspeichern").setLeft((int)((66d / 100 * width)));
views.get("btnspeichern").setHeight((int)((20d / 100 * height)));
views.get("btnspeichern").setWidth((int)((30d / 100 * width)));
views.get("btnspeichern").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
//BA.debugLineNum = 87;BA.debugLine="btnSpeichern.TextSize = btnSpeichern.TextSize * scale"[einstellungenwettkampf/800x480,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnspeichern")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnspeichern")).getTextSize())*Double.parseDouble(_scale)));

}
public static void LS_1280x800_1(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
String _delta="";
String _rate="";
String _scale="";
_delta = BA.NumberToString((((100d / 100 * width)+(100d / 100 * height))/((320d * scale)+(480d * scale))-1d));
_rate = "0.2";
_scale = BA.NumberToString(1d+Double.parseDouble(_rate)*Double.parseDouble(_delta));
views.get("label1").setTop((int)((10d / 100 * height) - (views.get("label1").getHeight())));
views.get("label1").setLeft((int)((0d / 100 * width)));
views.get("label1").setHeight((int)((15d / 100 * height)));
views.get("label1").setWidth((int)((100d / 100 * width)));
views.get("label1").setTop((int)((0d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("label1")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("label1")).getTextSize())*Double.parseDouble(_scale)));
views.get("lblrunden").setTop((int)((10d / 100 * height) - (views.get("lblrunden").getHeight())));
views.get("lblrunden").setLeft((int)((5d / 100 * width)));
views.get("lblrunden").setHeight((int)((15d / 100 * height)));
views.get("lblrunden").setWidth((int)((40d / 100 * width)));
views.get("lblrunden").setTop((int)((100d / 100 * height)-(35d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblrunden")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblrunden")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnrunden").setTop((int)((100d / 100 * height) - (views.get("spnrunden").getHeight())));
views.get("spnrunden").setLeft((int)((50d / 100 * width)));
views.get("spnrunden").setHeight((int)((15d / 100 * height)));
views.get("spnrunden").setWidth((int)((45d / 100 * width)));
views.get("spnrunden").setTop((int)((100d / 100 * height)-(35d / 100 * height)));
views.get("lblpause").setTop((int)((10d / 100 * height) - (views.get("lblpause").getHeight())));
views.get("lblpause").setLeft((int)((5d / 100 * width)));
views.get("lblpause").setHeight((int)((15d / 100 * height)));
views.get("lblpause").setWidth((int)((40d / 100 * width)));
views.get("lblpause").setTop((int)((100d / 100 * height)-(50d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblpause")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblpause")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnpause").setTop((int)((100d / 100 * height) - (views.get("spnpause").getHeight())));
views.get("spnpause").setLeft((int)((50d / 100 * width)));
views.get("spnpause").setHeight((int)((15d / 100 * height)));
views.get("spnpause").setWidth((int)((45d / 100 * width)));
views.get("spnpause").setTop((int)((100d / 100 * height)-(50d / 100 * height)));
views.get("lblkampfzeit").setTop((int)((10d / 100 * height) - (views.get("lblkampfzeit").getHeight())));
views.get("lblkampfzeit").setLeft((int)((5d / 100 * width)));
views.get("lblkampfzeit").setHeight((int)((15d / 100 * height)));
views.get("lblkampfzeit").setWidth((int)((40d / 100 * width)));
views.get("lblkampfzeit").setTop((int)((100d / 100 * height)-(65d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnkampfzeit").setTop((int)((100d / 100 * height) - (views.get("spnkampfzeit").getHeight())));
views.get("spnkampfzeit").setLeft((int)((50d / 100 * width)));
views.get("spnkampfzeit").setHeight((int)((15d / 100 * height)));
views.get("spnkampfzeit").setWidth((int)((45d / 100 * width)));
views.get("spnkampfzeit").setTop((int)((100d / 100 * height)-(65d / 100 * height)));
views.get("lbltimedelay").setTop((int)((10d / 100 * height) - (views.get("lbltimedelay").getHeight())));
views.get("lbltimedelay").setLeft((int)((5d / 100 * width)));
views.get("lbltimedelay").setHeight((int)((15d / 100 * height)));
views.get("lbltimedelay").setWidth((int)((40d / 100 * width)));
views.get("lbltimedelay").setTop((int)((100d / 100 * height)-(80d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lbltimedelay")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).getTextSize())*Double.parseDouble(_scale)));
views.get("spntimedelay").setTop((int)((100d / 100 * height) - (views.get("spntimedelay").getHeight())));
views.get("spntimedelay").setLeft((int)((50d / 100 * width)));
views.get("spntimedelay").setHeight((int)((15d / 100 * height)));
views.get("spntimedelay").setWidth((int)((45d / 100 * width)));
views.get("spntimedelay").setTop((int)((100d / 100 * height)-(80d / 100 * height)));
views.get("btnladen").setTop((int)((100d / 100 * height) - (views.get("btnladen").getHeight())));
views.get("btnladen").setLeft((int)((3d / 100 * width)));
views.get("btnladen").setHeight((int)((20d / 100 * height)));
views.get("btnladen").setWidth((int)((30d / 100 * width)));
views.get("btnladen").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).getTextSize())*Double.parseDouble(_scale)));
views.get("btnweiter").setTop((int)((100d / 100 * height) - (views.get("btnweiter").getHeight())));
views.get("btnweiter").setLeft((int)((34.5d / 100 * width)));
views.get("btnweiter").setHeight((int)((20d / 100 * height)));
views.get("btnweiter").setWidth((int)((30d / 100 * width)));
views.get("btnweiter").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnweiter")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnweiter")).getTextSize())*Double.parseDouble(_scale)));
views.get("btnspeichern").setTop((int)((100d / 100 * height) - (views.get("btnspeichern").getHeight())));
views.get("btnspeichern").setLeft((int)((66d / 100 * width)));
views.get("btnspeichern").setHeight((int)((20d / 100 * height)));
views.get("btnspeichern").setWidth((int)((30d / 100 * width)));
views.get("btnspeichern").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
//BA.debugLineNum = 86;BA.debugLine="btnSpeichern.TextSize = btnSpeichern.TextSize * scale"[einstellungenwettkampf/1280x800,scale=1]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnspeichern")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnspeichern")).getTextSize())*Double.parseDouble(_scale)));

}
public static void LS_540x960_1_5(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
String _delta="";
String _rate="";
String _scale="";
_delta = BA.NumberToString((((100d / 100 * width)+(100d / 100 * height))/((320d * scale)+(480d * scale))-1d));
_rate = "0.2";
_scale = BA.NumberToString(1d+Double.parseDouble(_rate)*Double.parseDouble(_delta));
views.get("label1").setTop((int)((10d / 100 * height) - (views.get("label1").getHeight())));
views.get("label1").setLeft((int)((0d / 100 * width)));
views.get("label1").setHeight((int)((15d / 100 * height)));
views.get("label1").setWidth((int)((100d / 100 * width)));
views.get("label1").setTop((int)((0d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("label1")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("label1")).getTextSize())*Double.parseDouble(_scale)));
views.get("lblrunden").setTop((int)((10d / 100 * height) - (views.get("lblrunden").getHeight())));
views.get("lblrunden").setLeft((int)((5d / 100 * width)));
views.get("lblrunden").setHeight((int)((15d / 100 * height)));
views.get("lblrunden").setWidth((int)((40d / 100 * width)));
views.get("lblrunden").setTop((int)((100d / 100 * height)-(35d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblrunden")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblrunden")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnrunden").setTop((int)((100d / 100 * height) - (views.get("spnrunden").getHeight())));
views.get("spnrunden").setLeft((int)((50d / 100 * width)));
views.get("spnrunden").setHeight((int)((15d / 100 * height)));
views.get("spnrunden").setWidth((int)((45d / 100 * width)));
views.get("spnrunden").setTop((int)((100d / 100 * height)-(35d / 100 * height)));
views.get("lblpause").setTop((int)((10d / 100 * height) - (views.get("lblpause").getHeight())));
views.get("lblpause").setLeft((int)((5d / 100 * width)));
views.get("lblpause").setHeight((int)((15d / 100 * height)));
views.get("lblpause").setWidth((int)((40d / 100 * width)));
views.get("lblpause").setTop((int)((100d / 100 * height)-(50d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblpause")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblpause")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnpause").setTop((int)((100d / 100 * height) - (views.get("spnpause").getHeight())));
views.get("spnpause").setLeft((int)((50d / 100 * width)));
views.get("spnpause").setHeight((int)((15d / 100 * height)));
views.get("spnpause").setWidth((int)((45d / 100 * width)));
views.get("spnpause").setTop((int)((100d / 100 * height)-(50d / 100 * height)));
views.get("lblkampfzeit").setTop((int)((10d / 100 * height) - (views.get("lblkampfzeit").getHeight())));
views.get("lblkampfzeit").setLeft((int)((5d / 100 * width)));
views.get("lblkampfzeit").setHeight((int)((15d / 100 * height)));
views.get("lblkampfzeit").setWidth((int)((40d / 100 * width)));
views.get("lblkampfzeit").setTop((int)((100d / 100 * height)-(65d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).getTextSize())*Double.parseDouble(_scale)));
views.get("spnkampfzeit").setTop((int)((100d / 100 * height) - (views.get("spnkampfzeit").getHeight())));
views.get("spnkampfzeit").setLeft((int)((50d / 100 * width)));
views.get("spnkampfzeit").setHeight((int)((15d / 100 * height)));
views.get("spnkampfzeit").setWidth((int)((45d / 100 * width)));
views.get("spnkampfzeit").setTop((int)((100d / 100 * height)-(65d / 100 * height)));
views.get("lbltimedelay").setTop((int)((10d / 100 * height) - (views.get("lbltimedelay").getHeight())));
views.get("lbltimedelay").setLeft((int)((5d / 100 * width)));
views.get("lbltimedelay").setHeight((int)((15d / 100 * height)));
views.get("lbltimedelay").setWidth((int)((40d / 100 * width)));
views.get("lbltimedelay").setTop((int)((100d / 100 * height)-(80d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lbltimedelay")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("lblkampfzeit")).getTextSize())*Double.parseDouble(_scale)));
views.get("spntimedelay").setTop((int)((100d / 100 * height) - (views.get("spntimedelay").getHeight())));
views.get("spntimedelay").setLeft((int)((50d / 100 * width)));
views.get("spntimedelay").setHeight((int)((15d / 100 * height)));
views.get("spntimedelay").setWidth((int)((45d / 100 * width)));
views.get("spntimedelay").setTop((int)((100d / 100 * height)-(80d / 100 * height)));
views.get("btnladen").setTop((int)((100d / 100 * height) - (views.get("btnladen").getHeight())));
views.get("btnladen").setLeft((int)((3d / 100 * width)));
views.get("btnladen").setHeight((int)((20d / 100 * height)));
views.get("btnladen").setWidth((int)((30d / 100 * width)));
views.get("btnladen").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).getTextSize())*Double.parseDouble(_scale)));
views.get("btnweiter").setTop((int)((100d / 100 * height) - (views.get("btnweiter").getHeight())));
views.get("btnweiter").setLeft((int)((34.5d / 100 * width)));
views.get("btnweiter").setHeight((int)((20d / 100 * height)));
views.get("btnweiter").setWidth((int)((30d / 100 * width)));
views.get("btnweiter").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnweiter")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnweiter")).getTextSize())*Double.parseDouble(_scale)));
views.get("btnspeichern").setTop((int)((100d / 100 * height) - (views.get("btnspeichern").getHeight())));
views.get("btnspeichern").setLeft((int)((66d / 100 * width)));
views.get("btnspeichern").setHeight((int)((20d / 100 * height)));
views.get("btnspeichern").setWidth((int)((30d / 100 * width)));
views.get("btnspeichern").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
//BA.debugLineNum = 86;BA.debugLine="btnSpeichern.TextSize = btnSpeichern.TextSize * scale"[einstellungenwettkampf/540x960,scale=1.5]
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnspeichern")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnspeichern")).getTextSize())*Double.parseDouble(_scale)));

}
public static void LS_general(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
String _delta="";
String _rate="";
String _scale="";
_delta = BA.NumberToString((((100d / 100 * width)+(100d / 100 * height))/((320d * scale)+(480d * scale))-1d));
_rate = "0.2";
_scale = BA.NumberToString(1d+Double.parseDouble(_rate)*Double.parseDouble(_delta));
views.get("btnladen").setTop((int)((100d / 100 * height) - (views.get("btnladen").getHeight())));
views.get("btnladen").setLeft((int)((3d / 100 * width)));
views.get("btnladen").setHeight((int)((20d / 100 * height)));
views.get("btnladen").setWidth((int)((30d / 100 * width)));
views.get("btnladen").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).setTextSize((float)((((anywheresoftware.b4a.keywords.LayoutBuilder.DesignerTextSizeMethod)views.get("btnladen")).getTextSize())*Double.parseDouble(_scale)));
views.get("btnweiter").setTop((int)((100d / 100 * height) - (views.get("btnweiter").getHeight())));
views.get("btnweiter").setLeft((int)((34.5d / 100 * width)));
views.get("btnweiter").setHeight((int)((20d / 100 * height)));
views.get("btnweiter").setWidth((int)((30d / 100 * width)));
views.get("btnweiter").setTop((int)((100d / 100 * height)-(20d / 100 * height)));
views.get("btnspeichern").setTop((int)((100d / 100 * height) - (views.get("btnspeichern").getHeight())));
views.get("btnspeichern").setLeft((int)((66d / 100 * width)));
views.get("btnspeichern").setHeight((int)((20d / 100 * height)));
views.get("btnspeichern").setWidth((int)((30d / 100 * width)));
views.get("btnspeichern").setTop((int)((100d / 100 * height)-(20d / 100 * height)));

}
}