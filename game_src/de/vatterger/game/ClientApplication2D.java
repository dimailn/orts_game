package de.vatterger.game;

import java.io.File;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

import de.vatterger.engine.handler.asset.AtlasHandler;
import de.vatterger.game.screen.manager.ScreenManager;

public class ClientApplication2D extends Game {
	
	@Override
	public void create() {
		
		for (Monitor monitor : Gdx.graphics.getMonitors()) {
			System.out.println(monitor.name + " at (" + monitor.virtualX + ", " + monitor.virtualY + ")");
		}
		
		System.out.println();
		
		System.out.println(Gdx.graphics.getGLVersion().getDebugVersionString());
		
		System.out.println();
		
		System.out.println("Working directory: " + new File("").getAbsolutePath());
		
		System.out.println();
		
		final Runtime runtime = Runtime.getRuntime();
		
		System.out.println("JVM Memory (Xmx): " + (runtime.maxMemory() / 1024 / 1024) + " MB");
		
		System.out.println();
		
		System.out.println("Core Count: " + runtime.availableProcessors());

		System.out.println();
		
		System.out.println("JVM Version: " + Runtime.version());
		
		System.out.println();
		
		IntBuffer texSizeMaxBuffer = BufferUtils.createIntBuffer(16);
		
		Gdx.gl.glGetIntegerv(GL20.GL_MAX_TEXTURE_SIZE, texSizeMaxBuffer.position(0));
		System.out.println("GL20.GL_MAX_TEXTURE_SIZE: " + texSizeMaxBuffer.get());

		System.out.println();

		Gdx.gl.glGetIntegerv(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, texSizeMaxBuffer.position(0));
		System.out.println("GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS: " + texSizeMaxBuffer.get());

		Gdx.gl.glGetIntegerv(GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, texSizeMaxBuffer.position(0));
		System.out.println("GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS: " + texSizeMaxBuffer.get());
		
		Gdx.gl.glGetIntegerv(GL20.GL_MAX_TEXTURE_IMAGE_UNITS, texSizeMaxBuffer.position(0));
		System.out.println("GL20.GL_MAX_TEXTURE_IMAGE_UNITS: " + texSizeMaxBuffer.get());

		System.out.println();

		ScreenManager.initialize(this);
		
		ScreenManager.setScreen(ScreenManager.MAIN);

		Gdx.graphics.setResizable(true);
		
		//Gdx.graphics.setWindowedMode(1024, 768);
		
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	}
	
	@Override 
	public void dispose() {

		super.dispose();
		
		AtlasHandler.dispose();
	}

	@Override
	public void render() {
		
		//Setting window title causes crashes and significant lag on Ubuntu
		//Gdx.graphics.setTitle("ORTS - " + (int)((1f/Gdx.graphics.getRawDeltaTime()) + 0.5f));
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
		
		super.render();
	}

	public static void main(String[] args) {
		
		//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		
		LwjglApplicationConfiguration configWindow = new LwjglApplicationConfiguration();
		
		DisplayMode[] modes = LwjglApplicationConfiguration.getDisplayModes();
		DisplayMode desktopMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		
		System.out.println();
		System.out.println("Available display modes");
		
		for (DisplayMode mode : modes) {
			System.out.println(mode.toString());
		}
		
		System.out.println();
		System.out.println("Desktop mode: " + desktopMode.toString());
		System.out.println();
		
		configWindow.title = "ORTS";
		
		configWindow.width = 640;//desktopMode.width;
		configWindow.height = 480;//desktopMode.height;
		configWindow.fullscreen = false;
		configWindow.resizable = true;
		configWindow.vSyncEnabled = true;
		
		//configWindow.setFromDisplayMode(desktopMode);
		
		configWindow.samples = 0;
		
		configWindow.useGL30 = true;
		
		configWindow.addIcon("assets/icon32.png", FileType.Internal);
		
		new LwjglApplication(new ClientApplication2D(), configWindow);
	}
}
