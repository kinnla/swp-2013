package rogue;

import jade.core.World;
import jade.ui.TiledTermPanel;
import jade.util.datatype.ColoredChar;

import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import rogue.creature.Monster;
import rogue.creature.Player;
import rogue.level.Level;

public class Rogue extends JApplet implements KeyEventDispatcher {
	public static void main(String[] args) throws InterruptedException {
		TiledTermPanel term = TiledTermPanel.getFramedTerminal("Jade Rogue");
		term.registerTile("dungeon.png", 5, 59, ColoredChar.create('#'));
		term.registerTile("dungeon.png", 3, 60, ColoredChar.create('.'));
		term.registerTile("dungeon.png", 5, 20, ColoredChar.create('@'));
		term.registerTile("dungeon.png", 14, 30,
				ColoredChar.create('D', Color.red));

		Player player = new Player(term);
		World world = new Level(69, 24, player);
		world.addActor(new Monster(ColoredChar.create('D', Color.red)));
		term.registerCamera(player, 5, 5);

		// hallo

		while (!player.expired()) {
			term.clearBuffer();
			for (int x = 0; x < world.width(); x++)
				for (int y = 0; y < world.height(); y++)
					term.bufferChar(x + 11, y, world.look(x, y));
			term.bufferCameras();
			term.refreshScreen();

			world.tick();
		}

		System.exit(0);
	}

	Player player;
	World world;
	TiledTermPanel term;
	Thread t;

	@Override
	public void init() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(this);

		term = new TiledTermPanel();
		getContentPane().add(term.panel());

		player = new Player(term);
		world = new Level(69, 24, player);
		world.addActor(new Monster(ColoredChar.create('D', Color.red)));
		term.registerCamera(player, 5, 5);

		System.out.println("applet initialized");
	}

	@Override
	public void start() {

		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!player.expired()) {
					term.clearBuffer();
					for (int x = 0; x < world.width(); x++)
						for (int y = 0; y < world.height(); y++)
							term.bufferChar(x + 11, y, world.look(x, y));
					term.bufferCameras();
					term.refreshScreen();
					world.tick();
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
		});
		t.start();
		System.out.println("applet started");
	}

	@Override
	public void stop() {
		System.out.println("applet stopped");
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		final KeyEvent ev = e;
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
//				term.keyPressed(ev);
//			}
//		});
		term.keyPressed(ev);
		return true;
	}

}
