package de.watchkido.mama.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_tempate1{

public static void LS_general(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
String _delta="";
String _rate="";
String _scale="";
String _b="";
String _pad="";
_delta = BA.NumberToString((((100d / 100 * width)+(100d / 100 * height))/((320d * scale)+(480d * scale))-1d));
_rate = "0.2";
_scale = BA.NumberToString(1d+Double.parseDouble(_rate)*Double.parseDouble(_delta));
views.get("pnltop").setLeft((int)(0d));
views.get("pnltop").setWidth((int)((100d / 100 * width) - (0d)));
views.get("pnltop").setTop((int)(0d));
views.get("pnltop").setHeight((int)((views.get("pnltop").getTop() + views.get("pnltop").getHeight())*Double.parseDouble(_scale) - (0d)));
views.get("pnlright").setLeft((int)((100d / 100 * width)-(views.get("pnlright").getWidth())*Double.parseDouble(_scale)));
views.get("pnlright").setWidth((int)((100d / 100 * width) - ((100d / 100 * width)-(views.get("pnlright").getWidth())*Double.parseDouble(_scale))));
views.get("pnldown").setLeft((int)(0d));
views.get("pnldown").setWidth((int)((100d / 100 * width) - (0d)));
views.get("pnldown").setTop((int)((100d / 100 * height)-(views.get("pnldown").getHeight())*Double.parseDouble(_scale)));
views.get("pnldown").setHeight((int)((100d / 100 * height) - ((100d / 100 * height)-(views.get("pnldown").getHeight())*Double.parseDouble(_scale))));
views.get("pnlright").setTop((int)((views.get("pnltop").getTop() + views.get("pnltop").getHeight())));
views.get("pnlright").setHeight((int)((views.get("pnldown").getTop()) - ((views.get("pnltop").getTop() + views.get("pnltop").getHeight()))));
views.get("scrollview1").setTop((int)((views.get("pnltop").getTop() + views.get("pnltop").getHeight())));
views.get("scrollview1").setHeight((int)((views.get("pnldown").getTop()) - ((views.get("pnltop").getTop() + views.get("pnltop").getHeight()))));
views.get("scrollview1").setLeft((int)(0d));
views.get("scrollview1").setWidth((int)((views.get("pnlright").getLeft()) - (0d)));
_b = BA.NumberToString((33d / 100 * width));
_pad = BA.NumberToString((4d * scale));
views.get("btndown1").setLeft((int)(0d));
views.get("btndown1").setWidth((int)(Double.parseDouble(_b) - (0d)));
views.get("btndown1").setTop((int)(Double.parseDouble(_pad)));
views.get("btndown1").setHeight((int)((views.get("pnldown").getHeight())-Double.parseDouble(_pad) - (Double.parseDouble(_pad))));
views.get("btndown2").setLeft((int)(Double.parseDouble(_b)));
views.get("btndown2").setWidth((int)(2d*Double.parseDouble(_b) - (Double.parseDouble(_b))));
views.get("btndown2").setTop((int)(Double.parseDouble(_pad)));
views.get("btndown2").setHeight((int)((views.get("pnldown").getHeight())-Double.parseDouble(_pad) - (Double.parseDouble(_pad))));
views.get("btndown3").setLeft((int)(2d*Double.parseDouble(_b)));
views.get("btndown3").setWidth((int)(3d*Double.parseDouble(_b) - (2d*Double.parseDouble(_b))));
views.get("btndown3").setTop((int)(Double.parseDouble(_pad)));
views.get("btndown3").setHeight((int)((views.get("pnldown").getHeight())-Double.parseDouble(_pad) - (Double.parseDouble(_pad))));
views.get("btnright1").setLeft((int)((views.get("pnlright").getWidth())/2d - (views.get("btnright1").getWidth() / 2)));
views.get("btnright2").setLeft((int)((views.get("pnlright").getWidth())/2d - (views.get("btnright2").getWidth() / 2)));
views.get("btnright3").setLeft((int)((views.get("pnlright").getWidth())/2d - (views.get("btnright3").getWidth() / 2)));

}
}