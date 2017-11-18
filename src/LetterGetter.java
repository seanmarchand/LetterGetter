import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class LetterGetter extends JPanel implements MouseListener, MouseMotionListener {
	File zero;
	private double ratio;
	private char []list=new char[5000];
	private int listIndex=0;
	private int y;
	private int index = 0;
	private int index2 = 0;
	private int y1;
	private int highestPoint = 500;
	private int lowestPoint = 0;
	private int height;
	private int third;
	private int TLpointx = 7000;
	private int TLpointy = 7000;
	private boolean pointEntered = false;
	// The most right point in the top third
	private int TRpointx = -7000;
	private int TRpointy = 7000;
	// The most left point in the middle third
	private int MLpointx = 7000;
	private int MLpointy = 7000;
	// The most right point in the middle third
	private int MRpointx = -7000;
	private int MRpointy = 7000;
	// The most left point in the bottom
	private int BLpointx = 7000;
	private int BLpointy = 7000;
	// The most right point in the bottom
	private int BRpointx = -7000;
	private int BRpointy = 7000;
	private Point[] arr = new Point[100000];
	private Point[] arr2 = new Point[100000];
	public boolean trigger = false;
	public boolean trigger2 = false;

	public LetterGetter(String name) {
		super();
		index = 0;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setLayout(new BorderLayout());
		JFrame fr = new JFrame(name);
		fr.add(this);
		fr.setLocation((dim.width/2) - 250, (dim.height/2)-250);
		fr.setSize(500, 500);
		setBackground(Color.white);
		JButton btn = new JButton("Done");
		JButton btn2 = new JButton("Reset");
		fr.add(btn, BorderLayout.EAST);
		fr.add(btn2, BorderLayout.WEST);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setVisible(true);
		fr.setResizable(false);
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				arr = new Point[100000];
				index = 0;
				arr2 = new Point[100000];
				index2 = 0;
				repaint();
				trigger = false;
				trigger2 = false;
				pointEntered = false;
			}
		});
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// display/center the jdialog when the button is pressed
				/*
				 * JDialog d = new JDialog(fr, "Hey"); d.setSize(300,200);
				 * d.setLocationRelativeTo(fr); d.setVisible(true);
				 */
				String s;
				if (pointEntered) {
					JOptionPane x = new JOptionPane();
					analyze();
					int answer = guess();
					if (JOptionPane.showConfirmDialog(null, "Is your number " + answer + "?", "Guess",
							JOptionPane.YES_NO_OPTION) == 1) {
						s = (String) JOptionPane.showInputDialog(fr, "Input the number you entered: ", "Hi",
								x.PLAIN_MESSAGE, null, null, "");
						if (s != null) {
							try {
								y = Integer.parseInt(s);
								toFile();
								trigger2 = true;
								fr.dispatchEvent(new WindowEvent(fr, WindowEvent.WINDOW_CLOSING));
							} catch (Exception ez) {
								JPanel pan = new JPanel();
								pan.setLayout(new FlowLayout());
								pan.add(new JLabel("Error please enter number"));
								JDialog d = new JDialog(fr, "Error");
								d.add(pan);
								d.setSize(300, 200);
								d.setLocationRelativeTo(fr);
								d.setVisible(true);
							}
						}

					}
					else
					{
						y = answer;
						toFile();
						trigger2 = true;
						fr.dispatchEvent(new WindowEvent(fr, WindowEvent.WINDOW_CLOSING));
					}
				}
				else {
					JPanel pan = new JPanel();
					pan.setLayout(new FlowLayout());
					pan.add(new JLabel("Please draw something on the panel provided"));
					JDialog d = new JDialog(fr, "Error");
					d.add(pan);
					d.setSize(300, 200);
					d.setLocationRelativeTo(fr);
					d.setVisible(true);
				}
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		if (trigger == false) {
			for (int i = 0; i < index - 1; i++)
				g.drawLine(arr[i].x, arr[i].y, arr[i + 1].x, arr[i + 1].y);
		} else {
			for (int i = 0; i < index - 1; i++) {
				g.drawLine(arr[i].x, arr[i].y, arr[i + 1].x, arr[i + 1].y);
			}
			for (int i = 0; i < index2 - 1; i++)
				g.drawLine(arr2[i].x, arr2[i].y, arr2[i + 1].x, arr2[i + 1].y);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (trigger == false) {
			arr[index] = new Point(e.getX(), e.getY());
			y1 = arr[index].y;
			index++;
			repaint();
		} else if (trigger == true && trigger2 == false) {
			arr2[index2] = new Point(e.getX(), e.getY());
			y1 = arr2[index2].y;
			index2++;
			repaint();
		} else {

		}
		if (y1 < highestPoint) {
			highestPoint = y1;
		}
		if (y1 > lowestPoint) {
			lowestPoint = y1;
		}
	}

	public void mousePressed(MouseEvent e) {
		pointEntered = true;
		if (trigger == false) {
			arr[index] = new Point(e.getX(), e.getY());
			index++;
			repaint();
		} else if (trigger == true && trigger2 == false) {
			arr2[index2] = new Point(e.getX(), e.getY());
			index2++;
			repaint();
		} else {

		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		if (trigger == false) {
			trigger = true;
		} else {
			if (trigger2 != true) {
				trigger2 = true;
				analyze();
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void analyze() {
		height = lowestPoint - highestPoint;
		third = height / 3;
		for (int x = 0; x < index; x++) {
			if (arr[x].x < TLpointx && arr[x].y <= highestPoint + third) {
				TLpointx = arr[x].x;
				TLpointy = arr[x].y;
			}
			if (arr[x].x < MLpointx && arr[x].y > highestPoint + third && arr[x].y <= highestPoint + (2 * third)) {
				MLpointx = arr[x].x;
				MLpointy = arr[x].y;
			}
			if (arr[x].x < BLpointx && arr[x].y > highestPoint + (2 * third)) {
				BLpointx = arr[x].x;
				BLpointy = arr[x].y;
			}
			if (arr[x].x > TRpointx && arr[x].y <= highestPoint + third) {
				TRpointx = arr[x].x;
				TRpointy = arr[x].y;
			}
			if (arr[x].x > MRpointx && arr[x].y > highestPoint + third && arr[x].y <= highestPoint + (2 * third)) {
				MRpointx = arr[x].x;
				MRpointy = arr[x].y;
			}
			if (arr[x].x > BRpointx && arr[x].y > highestPoint + (2 * third)) {
				BRpointx = arr[x].x;
				BRpointy = arr[x].y;
			}
		}
		for (int x = 0; x < index2; x++) {
			if (arr2[x].x < TLpointx && arr2[x].y <= highestPoint + third) {
				TLpointx = arr2[x].x;
				TLpointy = arr2[x].y;
			}
			if (arr2[x].x < MLpointx && arr2[x].y > highestPoint + third && arr2[x].y <= highestPoint + (2 * third)) {
				MLpointx = arr2[x].x;
				MLpointy = arr2[x].y;
			}
			if (arr2[x].x < BLpointx && arr2[x].y > highestPoint + (2 * third)) {
				BLpointx = arr2[x].x;
				BLpointy = arr2[x].y;
			}
			if (arr2[x].x > TRpointx && arr2[x].y <= highestPoint + third) {
				TRpointx = arr2[x].x;
				TRpointy = arr2[x].y;
			}
			if (arr2[x].x > MRpointx && arr2[x].y > highestPoint + third && arr2[x].y <= highestPoint + (2 * third)) {
				MRpointx = arr2[x].x;
				MRpointy = arr2[x].y;
			}
			if (arr2[x].x > BRpointx && arr2[x].y > highestPoint + (2 * third)) {
				BRpointx = arr2[x].x;
				BRpointy = arr2[x].y;
			}
		}
		
		System.out.println("Lowest: " + lowestPoint);
		System.out.println("Highest: " + highestPoint);
		// System.out.println("y1: "+y1);
		System.out.println("Height: " + height);
		System.out.println("Third: " + third);
		System.out.println(pointEntered);
		ratio = lowestPoint/highestPoint;
	}

	public void toFile() {
		for (int x = 0; x < 10; x++) {
			try {
				if (x == y) {
					File zero = new File(Integer.toString(x) + ".txt");
					BufferedWriter writer = new BufferedWriter(new FileWriter(zero, true));
					
					writer.write(String.valueOf(TLpointx));
					writer.write(" ");
					writer.write(String.valueOf(TLpointy));
					writer.write(" ");
					writer.write(String.valueOf(TRpointx));
					writer.write(" ");
					writer.write(String.valueOf(TRpointy));
					writer.write(" ");
					writer.write(String.valueOf(MLpointx));
					writer.write(" ");
					writer.write(String.valueOf(MLpointy));
					writer.write(" ");
					writer.write(String.valueOf(MRpointx));
					writer.write(" ");
					writer.write(String.valueOf(MRpointy));
					writer.write(" ");
					writer.write(String.valueOf(BLpointx));
					writer.write(" ");
					writer.write(String.valueOf(BLpointy));
					writer.write(" ");
					writer.write(String.valueOf(BRpointx));
					writer.write(" ");
					writer.write(String.valueOf(BRpointy));
					writer.write(" ");
					//writer.write(String.valueOf(height));
					writer.write(String.valueOf(ratio));
					writer.newLine();
					writer.close();
				}
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
	}

	public int guess() {
		if (TLpointx < MLpointx && TLpointx < BLpointx) {
		// Top is smallest
			System.out.println("Hello top");
			MLpointx -= TLpointx;
			BLpointx -= TLpointx;
			TLpointx = 0;
			} else if (MLpointx <= TLpointx && MLpointx <= BLpointx) {
				System.out.println("Hello mid");
				TLpointx -= MLpointx;
				BLpointx -= MLpointx;
				MLpointx = 0;
			} else if (BLpointx <= TLpointx && BLpointx <= MLpointx) {
				System.out.println("Hello bot");
				TLpointx -= BLpointx;
				MLpointx -= BLpointx;
				BLpointx = 0;
				}
			if(TLpointy<=TRpointy)
			{
				TRpointy-=TLpointy;
				BLpointy-=TLpointy;
				BRpointy-=TLpointy;
				MLpointy-=TLpointy;
				MRpointy-=TLpointy;
				TLpointy=0;
			}
				else if(TRpointy<TLpointy)
				{
				TLpointy-=TRpointy;
				BLpointy-=TRpointy;
				BRpointy-=TRpointy;
				MLpointy-=TRpointy;
				MRpointy-=TRpointy;
				TRpointy=0;
			}
		String line;
		int numAnalyzed[]=new int [10];
		int[][] counters = new int[10][50];
		int error=10;
		for (int x = 0; x < 10; x++) {
			int count = 0;
			try {
				BufferedReader in = new BufferedReader(new FileReader(Integer.toString(x) + ".txt"));
				while ((line = in.readLine()) != null) {
				
					String[] parts = line.split(" ");
					System.out.println(TLpointx);
					if (Math.abs(Integer.parseInt(parts[0]) - TLpointx) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[1]) - TLpointy) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[2]) - TRpointx) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[3]) - TRpointy) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[4]) - MLpointx) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[5]) - MLpointy) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[6]) - MRpointy) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[7]) - MRpointy) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[8]) - BLpointy) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[9]) - BLpointy) < error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[10]) - BRpointy) <error) {
						counters[x][count]++;
					}
					if (Math.abs(Integer.parseInt(parts[11]) - BRpointy) < error) {
						counters[x][count]++;
					}
					count++;
					numAnalyzed[x]++;
				//	}
				}
			} catch (Exception e) {
				System.out.println("broke" + x);
			}
		}
		//System.out.println(thing[0]);
		System.out.println("Top Left: " + TLpointx + " ," + TLpointy);
		System.out.println("Top Right:" + TRpointx + " ," + TRpointy);
		System.out.println("Mid Left: " + MLpointx + " ," + MLpointy);
		System.out.println("Mid Right:" + MRpointx + " ," + MRpointy);
		System.out.println("Bot Left: " + BLpointx + " ," + BLpointy);
		System.out.println("Bot Right: " + BRpointx + " ," + BRpointy);
		int first = 0;
		double[] average = new double[10];
		for (int i = 0; i < 10; i++) {
			for (int q = 0; q < 50; q++) {
				average[i] += counters[i][q];
			}
			average[i] /= numAnalyzed[i];
			//average[i]+=0.1;
			System.out.println(i + " average is " + average[i]);
			if (average[i] >= average[first]) {
				first = i;
			}
		}
		return first;
	}


	public static void main(String[] args) {
		LetterGetter mouse = new LetterGetter("Mouse");
	}
}