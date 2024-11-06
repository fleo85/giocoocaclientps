package gui;

import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class StreamingThread implements Runnable {
	private GiocoOcaWindow window;
	private GiocoOcaWindowListener listener;
	private Scanner sc;
	
	public StreamingThread(GiocoOcaWindow window, Scanner sc,
			GiocoOcaWindowListener listener) {
		this.window = window;
		this.sc = sc;
		this.listener = listener;
	}
	
	@Override
	public void run() {
		while (true) {
			String nuovaLinea = this.sc.nextLine();
			if (nuovaLinea.equals("END")) {
				this.listener.transmitting = false;
				this.window.setButtons(this.listener.connected, this.listener.transmitting);
				break;
			} else {
				System.out.println(nuovaLinea);
				String[] nuovaLineaSplit = nuovaLinea.split(" ");
				if (nuovaLineaSplit[1].trim().equalsIgnoreCase("FINE")) {
					this.listener.transmitting = false;
					this.window.setButtons(this.listener.connected, this.listener.transmitting);
					JOptionPane.showMessageDialog(window, nuovaLineaSplit[0] + " ha vinto!!!");
					break;
				} else {
					Casella etichetta = this.window.griglia.get(nuovaLineaSplit[2]);
					etichetta.giocatore.add(nuovaLineaSplit[0]);
					etichetta.updateText();
					
					if (this.window.posizioneGiocatori.containsKey(nuovaLineaSplit[0])) {
						Casella vecchiaCasella = this.window.posizioneGiocatori.get(nuovaLineaSplit[0]);
						vecchiaCasella.giocatore.remove(nuovaLineaSplit[0]);
						vecchiaCasella.updateText();
					}
					
					this.window.posizioneGiocatori.put(nuovaLineaSplit[0], etichetta);
				}
			}
		}
		System.out.println("EXITING THREAD");
	}

}
