package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.util.HashMap;
import java.util.LinkedList;

public class GiocoOcaWindowListener implements ActionListener {
	
	public static final String CONNECT = "CONNECT";
	public static final String DISCONNECT = "DISCONNECT";
	public static final String LOAD = "LOAD";
	public static final String START = "START";
	public static final String STOP = "STOP";
	
	private GiocoOcaWindow view;
	
	public GiocoOcaWindowListener(GiocoOcaWindow view) {
		this.view = view;
	}
	
	boolean connected;
	boolean transmitting;
	
	private Socket sock;
	private Scanner sc;
	private PrintWriter pw;

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals(CONNECT)) {
			try {
				this.sock = new Socket(this.view.addressField.getText(),
						Integer.parseInt(this.view.portField.getText()));
				this.sc = new Scanner(this.sock.getInputStream());
				this.pw = new PrintWriter(this.sock.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this.view, "Impossibile connettersi");
				return;
			} catch (NumberFormatException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(this.view, "Il numero di porta non è corretto");
				return;
			}
			connected = true;
			transmitting = false;
		} else if (e.getActionCommand().equals(DISCONNECT)) {
			this.pw.println("DISCONNECT");
			this.pw.flush();
			this.sc.close();
			this.pw.close();
			try {
				this.sock.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.view.setCenterPanel(null);
			
			connected = false;
			transmitting = false;
		} else if (e.getActionCommand().equals(START)) {
			if (this.view.userField.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(view, "Indicare una lista di utenti separati da virgola");
				return;
			}
			this.pw.println("START " + this.view.userField.getText().trim());
			this.pw.flush();
			
			String linea = this.sc.nextLine();
			while (!linea.equals("TABELLONE")) {
				linea = this.sc.nextLine();
			}
			
			int numberOfSlots = Integer.parseInt(this.sc.nextLine());
			LinkedList<String> tabellone = new LinkedList<String>();
			for (int i = 0; i < numberOfSlots; i++) {
				tabellone.add(sc.nextLine());
			}
			System.out.println("Slots: " + numberOfSlots);
			this.view.setCenterPanel(tabellone);
			
			this.view.posizioneGiocatori = new HashMap<String, Casella>();
			Thread t1 = new Thread(new StreamingThread(this.view, this.sc, this));
			t1.start();
			transmitting = true;
		} else if (e.getActionCommand().equals(STOP)) {
			this.pw.println("INTERRUPT");
			this.pw.flush();
			transmitting = false;
		}
		this.view.setButtons(connected, transmitting);
	}

}
