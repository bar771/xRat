package com.rat.hacker;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.io.IOException;

import java.util.Scanner;

public class Rat {
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private String host;
	private int port;
	
	public Rat() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Type an IP Address:");
		this.host = scan.nextLine();
		System.out.println("Type a port:");
		this.port = scan.nextInt();
		try {
			this.socket = new Socket(host, port);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);
			
			Client c = new Client(in, out, socket);
			c.start();
			
		} catch(IOException e) { e.printStackTrace(); }
	}
	
	public static void main(String[] args) {
		new Rat();
	}
	
}