import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.text.LabelView;

public class MapFrame {
	
	Map<Point, Integer> map = new HashMap<>();
	private final int ROAD = 0;
	private final int BRICKWALL = 1;
	private final int DIAMOND = 2;
	private final int HEART = 3;
	private int hp = 100;
	private final int WIN = 0;
	private final int LOSE = 1;
	private JProgressBar progressBar;
	private JLabel labelView[][];
	private JFrame frame;
	private JPanel onlyPanel;
	
	public MapFrame() {
		getMap();
		map = ReadMapFile.getRecordMap();

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setTitle("電流急急棒");
		onlyPanel = new JPanel();
		onlyPanel.setLayout(new GridLayout((int) findWidthAndHeight(map).getX(), (int) findWidthAndHeight(map).getY()));

		progressBar = new JProgressBar();
		progressBar.setValue(hp);
		progressBar.setPreferredSize(new Dimension(0, 10));
		frame.add(progressBar, BorderLayout.NORTH);
		
		ImageIcon brickwallIcon = new ImageIcon(getClass().getResource("brickwall.png"));
		ImageIcon diamondIcon = new ImageIcon(getClass().getResource("diamond.png"));
		ImageIcon heartIcon = new ImageIcon(getClass().getResource("heart.png"));
		ImageIcon roadIcon = new ImageIcon(getClass().getResource("road.png"));

		labelView = new JLabel[(int) findWidthAndHeight(map).getX()][(int) findWidthAndHeight(map).getY()];
		
		for (int i = 0; i < (int) findWidthAndHeight(map).getX(); i++) {
			for (int j = 0; j < (int) findWidthAndHeight(map).getY(); j++) {
				if (map.get(new Point(j, i)) == 1) {
					JLabel label = new JLabel(resizeImageIcon(brickwallIcon));
					labelView[j][i] = label;
					onlyPanel.add(labelView[j][i]);
				} else if (map.get(new Point(j, i)) == 0) {
					JLabel label = new JLabel(resizeImageIcon(roadIcon));
					labelView[j][i] = label;
					onlyPanel.add(labelView[j][i]);
				} else if (map.get(new Point(j, i)) == 2) {
					JLabel label = new JLabel(resizeImageIcon(diamondIcon));
					labelView[j][i] = label;
					onlyPanel.add(labelView[j][i]);
				} else if (map.get(new Point(j, i)) == 3) {
					JLabel label = new JLabel(resizeImageIcon(heartIcon));
					labelView[j][i] = label;
					onlyPanel.add(labelView[j][i]);
				}
			}
		}
		onlyPanel.addMouseMotionListener(new MouseHandler());
		frame.add(onlyPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}

	public static void getMap() {
		ReadMapFile.openFile("map.txt");
		ReadMapFile.readRecords();
		ReadMapFile.closeFile();
	}

	private Point findWidthAndHeight(Map<Point, Integer> map) {
		int maxX = 0, maxY = 0;
		for (Point point : map.keySet()) {
			if (maxX < point.x)
				maxX = point.x;
			if (maxY < point.y)
				maxY = point.y;
		}
		return new Point(maxX + 1, maxY + 1);
	}
	
	private void changeHp(Point point) {
		if (map.containsKey(point)) {
			switch (map.get(point)) {
			case ROAD:
				hp -= 5;
				break;
			case BRICKWALL:
				hp -= 20;
				break;
			case DIAMOND:
				gameOver(WIN);
				break;
			case HEART:
				hp += 10;
				map.put(point, ROAD);
				labelView[(int)point.getX()][(int)point.getY()].setIcon(null);
				
				break;
			}
			progressBar.setValue(hp);
			if (hp <= 0) {
				gameOver(LOSE);
			}
		}
	}
	
	private void gameOver(int winOrLose) {
		if (winOrLose == WIN) {
			JOptionPane.showMessageDialog(null, "恭喜獲勝");
		} else if (winOrLose == LOSE) {
			JOptionPane.showMessageDialog(null, "請再接再厲");
		}
			System.exit(0);
	}
	
	private class MouseHandler extends MouseAdapter implements MouseMotionListener {
		Point currentPoint = new Point();
		int x, y;
		public void mouseMoved(MouseEvent e) {
			x = e.getX() / 64;
			y = e.getY() / 64;
			if (currentPoint.x != x || currentPoint.y != y) {
				currentPoint.x = x;
				currentPoint.y = y;
				changeHp(currentPoint);
				System.out.println("[" + x + ", " + y + "]");
			}
		}
	}

	public ImageIcon resizeImageIcon(ImageIcon imageIcon) {
		Image image = imageIcon.getImage(); // transform it
		Image newimg = image.getScaledInstance(64, 64, Image.SCALE_DEFAULT); // scale it the smooth way
		imageIcon = new ImageIcon(newimg); // transform it back

		return imageIcon;
	}

}
