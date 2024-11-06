package gui;

import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JLabel;

public class Casella extends JLabel {
	public HashSet<String> giocatore = new HashSet<String>();
	public String nome;
	
	public Casella(String nome) {
		this.nome = nome;
		this.setText(this.generateText());
	}
	
	public void updateText() {
		this.setText(this.generateText());
	}
	
	private String generateText() {
		String res = "<html><center>" + nome;
		for (String s: giocatore) {
			res += "<br>" + s;
		}
		res += "</center></html>";
		return res;
	}
}
