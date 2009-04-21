package gdi1sokoban.logic;

import java.io.Serializable;

import org.lwjgl.opengl.DisplayMode;

public class GlobalConfig implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String _resolution;
	private boolean _fullscrean;
	private boolean _reflections;
	private boolean _sound;
	
	public GlobalConfig(String resolution, boolean fullscrean, boolean reflections, boolean sound) {
		_resolution = resolution;
		_fullscrean = fullscrean;
		_reflections = reflections;
		_sound = sound;
	}
	
	public static String DisplayModeToString(DisplayMode mode) {
		return mode.getWidth() + "x" + mode.getHeight() + " @" + mode.getFrequency() + ", " + mode.getBitsPerPixel() + "bpp";
	}
	
	public static int DisplayModeToInt(String mode, int i) {
		return Integer.parseInt(mode.split("x| @|, |bpp")[i]);
	}
	
	public String getResolution() {
		return _resolution;
	}
	
	public void setResolution(String resolution) {
		_resolution = resolution;
	}
	
	public boolean isFullscreen() {
		return _fullscrean;
	}
	
	public void setFullscreen(boolean fullscrean) {
		_fullscrean = fullscrean;
	}
	
	public boolean isReflections() {
		return _reflections;
	}
	
	public void setReflections(boolean reflections) {
		_reflections = reflections;
	}
	
	public boolean isSound() {
		return _sound;
	}
	
	public void setSound(boolean sound) {
		_sound = sound;
	}
}
