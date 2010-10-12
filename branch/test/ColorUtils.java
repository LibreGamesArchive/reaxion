package com.googlecode.reaxion.game.util;

import com.jme.renderer.ColorRGBA;

public class ColorUtils {
	
	public static ColorRGBA darker(ColorRGBA color, double factor) {
		float red = Float.parseFloat("" + fixRange(Math.round(color.r * 255 * (1.0 - factor))) / 255 + "f");
		float blue = Float.parseFloat("" + fixRange(Math.round(color.b * 255 * (1.0 - factor))) / 255 + "f");
		float green = Float.parseFloat("" + fixRange(Math.round(color.g * 255 * (1.0 - factor))) / 255 + "f");
		
		return new ColorRGBA(red, green, blue, color.a);
	}
	
	public static ColorRGBA lighter(ColorRGBA color, double factor) {
		float red = Float.parseFloat("" + fixRange(Math.round(color.r * 255 * (1.0 + factor))) / 255 + "f");
		float blue = Float.parseFloat("" + fixRange(Math.round(color.b * 255 * (1.0 + factor))) / 255 + "f");
		float green = Float.parseFloat("" + fixRange(Math.round(color.g * 255 * (1.0 + factor))) / 255 + "f");
		
		return new ColorRGBA(red, green, blue, color.a);
	}
	
	private static double fixRange(double c) {
		return Math.min(Math.max(c, 0), 255);
	}
	
}
