package gdi1sokoban.graphic.base;

import gdi1sokoban.base.ResourceDescriptor;

public class FontDescriptor extends ResourceDescriptor<Font> {

	private java.awt.Font _font;
	private float[] _fgColor;
	private float[] _bgColor;
	
	public FontDescriptor(java.awt.Font font, float[] fgColorArray, float[] bgColorArray) {
		_font = font;
		_fgColor = fgColorArray;
		_bgColor = bgColorArray;
	}
	
	@Override
	public Font build() throws Exception {
		return new Font(_font, _fgColor, _bgColor);
	}

	@Override
	public String getId() {
		return Font.buildId(_font, _fgColor, _bgColor);
	}

}
