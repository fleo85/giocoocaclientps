package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GiocoOcaWindow extends JFrame {
	JTextField addressField = new JTextField(20);
	JTextField portField = new JTextField(5);
	JTextField userField = new JTextField(20);
	private JButton connectButton = new JButton("Connect");
	private JButton disconnectButton = new JButton("Disconnect");
	private JButton start = new JButton("Start Simulation");
	private JButton stop = new JButton("Stop Simulation"); 
	private JPanel centerPanel;
	
	HashMap<String, Casella> griglia;
	HashMap<String, Casella> posizioneGiocatori;
	public GiocoOcaWindow() {
		super("Nome Cognome 1234567");
		
		GiocoOcaWindowListener listener = new GiocoOcaWindowListener(this);
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		northPanel.add(new JLabel("Server address"));
		northPanel.add(addressField);
		addressField.setText("localhost");
		northPanel.add(new JLabel("Port"));
		northPanel.add(portField);
		portField.setText("4400");
		connectButton.setActionCommand(GiocoOcaWindowListener.CONNECT);
		connectButton.addActionListener(listener);
		northPanel.add(connectButton);
		disconnectButton.setActionCommand(GiocoOcaWindowListener.DISCONNECT);
		disconnectButton.addActionListener(listener);
		northPanel.add(disconnectButton);
		this.getContentPane().add(northPanel, BorderLayout.NORTH);
		
		this.setCenterPanel(null);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		southPanel.add(new JLabel("Lista utenti"));
		southPanel.add(userField);
		start.setActionCommand(GiocoOcaWindowListener.START);
		start.addActionListener(listener);
		southPanel.add(start);
		stop.setActionCommand(GiocoOcaWindowListener.STOP);
		stop.addActionListener(listener);
		southPanel.add(stop);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		
		this.pack();
		this.setButtons(false, false);
		this.setVisible(true);
		
	}
	
	public void setButtons(boolean connected, boolean transmitting) {
		connectButton.setEnabled(!connected);
		disconnectButton.setEnabled(connected && !transmitting);
		userField.setEnabled(!transmitting && connected);
		start.setEnabled(!transmitting && connected);
		stop.setEnabled(transmitting && connected);
		this.setDefaultCloseOperation(connected ? JFrame.DO_NOTHING_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
	}
	
	public void setCenterPanel(LinkedList<String> caselle) {
		if (centerPanel != null) {
			this.getContentPane().remove(centerPanel);
		}
		centerPanel = new JPanel();
		if (caselle == null) {
			this.griglia = null;
			centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			centerPanel.add(new JLabel("Inserire una lista di giocatori separata da virgola ed avviare la simulazione"));
		} else {
			this.griglia = new HashMap<String, Casella>();
			centerPanel.setLayout(new GridLayout(1, caselle.size()));
			for (String s: caselle) {
				Casella toAdd = new Casella(s);
				centerPanel.add(toAdd);
				griglia.put(s, toAdd);
			}
		}
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		this.pack();
		this.setSize(this.getWidth(), this.getHeight() + 100);
	}
}
