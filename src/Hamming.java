import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * M1INFO Université du Havre
 * TP RESEAU
 * Le code de Hamming
 * @author Florian Alline
 *
 */
public class Hamming extends JFrame implements ActionListener{
	
	private ButtonGroup gp;
	private JRadioButton calcul;
	private JRadioButton verif;
	private JTextField tfBinaire;
	private JButton valider;
	
	private JLabel labGauche;
	private JLabel labMid;
	private JLabel labDroite;
	
	private JPanel centre;
	private Font fontEntered;

	public Hamming () {
		
		this.setSize(900, 250);
		this.setTitle("Code de Hamming");
		this.setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		fontEntered = new Font(Font.DIALOG, Font.LAYOUT_LEFT_TO_RIGHT, 28);
		
		gp = new ButtonGroup();
		
		/*NRZ, NRZI, MAnchester, Machester diff, Miller*/
		
		calcul = new JRadioButton("Calcul du code de Hamming");
		verif = new JRadioButton("Verification du code de Hamming");
		
		gp.add(calcul);
		gp.add(verif);;
		
		calcul.setSelected(true);
		
		tfBinaire = new JTextField();
		
		valider = new JButton("Valider");
		valider.addActionListener(this);
		
		JPanel pNord = new JPanel(new GridLayout(1,6));
		pNord.add(calcul);
		pNord.add(verif);
		pNord.add(tfBinaire);
		pNord.add(valider);
		
		centre = new JPanel(new GridLayout(3,1));
		
		labGauche = new JLabel();
		labGauche.setFont(fontEntered);
		labGauche.setHorizontalAlignment(SwingConstants.CENTER);
		
		labMid = new JLabel();
		labMid.setFont(fontEntered);
		labMid.setHorizontalAlignment(SwingConstants.CENTER);
		
		labDroite = new JLabel();
		labDroite.setFont(fontEntered);
		labDroite.setHorizontalAlignment(SwingConstants.CENTER);
		
		centre.add(labGauche);
		centre.add(labMid);
		centre.add(labDroite);
		
		this.add(centre,BorderLayout.CENTER);
		this.add(pNord,BorderLayout.NORTH);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == valider) {
			
			labGauche.setText("");
			labMid.setText("");
			labDroite.setText("");
			
			int[] tabCode;
			
			if(!tfBinaire.getText().equals("")) {
				
				boolean verif = false;
				
				String[] codeB = tfBinaire.getText().split("");
				
				int taille = codeB.length-1;
				
				tabCode = new int[taille];
				
				
				for (int x = 1; x < codeB.length; x++) {
					
					if(!codeB[x].equals("0") && !codeB[x].equals("1")) verif = true;
				}
				
				
				if(verif) {
					
					tfBinaire.setBorder(BorderFactory.createLineBorder(Color.RED));
					
				}
				else {
					
					String[] codeB2 = tfBinaire.getText().split("");
					
					for (int x = 1; x < codeB2.length; x++) {
						
						String temp = codeB2[x];
						
						int nb = Integer.parseInt(temp);
						
						tabCode[codeB2.length-x-1] = nb;
					}
					
					if(calcul.isSelected()) {
					
					int hamming[] = genererCodeHamming(tabCode);
					
					String lab = "Code de Hamming : ";
					for(int i=0 ; i < hamming.length ; i++) {
						lab += hamming[hamming.length-i-1];
					}
					labMid.setText(lab);
					
					}
					else {
						
						int i=0;
						int nbParite=0;
						
						while(i < tabCode.length) {
							
							if(Math.pow(2,nbParite) == i+nbParite + 1) {
								nbParite++;
							}
							else {
								i++;
							}
						}
						
						verifHamming(tabCode, nbParite);
						
					}
				}
			}
		}
		
		centre.updateUI();
		centre.revalidate();
	}

	private int[] genererCodeHamming(int[] tabCode) {
		// TODO Auto-generated method stub
		
				int hamming[];
				
				int i=0;
				int nbParite=0; 
				int j=0;
				int k=0;
				
				while(i < tabCode.length) {
					
					if(Math.pow(2,nbParite) == i+nbParite + 1) {
						nbParite++;
					}
					else {
						i++;
					}
				}
				
				hamming = new int[tabCode.length + nbParite];
				
				for(i=1 ; i <= hamming.length ; i++) {
					if(Math.pow(2, j) == i) {
						
						hamming[i-1] = 2;
						j++;
					}
					else {
						hamming[k+j] = tabCode[k++];
					}
				}
				for(i=0 ; i < nbParite ; i++) {
					
					hamming[((int) Math.pow(2, i))-1] = getParite(hamming, i);
				}
				return hamming;
	}
	
	int getParite(int b[], int puisance) {
		
		int parite = 0;
		
		for(int i=0 ; i < b.length ; i++) {
			
			if(b[i] != 2) {
				
				int k = i+1;
				String s = Integer.toBinaryString(k);
				
				int x = ((Integer.parseInt(s))/((int) Math.pow(10, puisance)))%10;
				if(x == 1) {
					if(b[i] == 1) {
						parite = (parite+1)%2;
					}
				}
			}
		}
		return parite;
	}
	
	private void verifHamming(int hamming[], int nbParite) {
		
		int puissance;
		
		int tabParite[] = new int[nbParite];
		
		String s = "";
		
		for(puissance=0 ; puissance < nbParite ; puissance++) {
			
			for(int i=0 ; i < hamming.length ; i++) {
				
				int k = i+1;
				
				String str = Integer.toBinaryString(k);
				
				int bit = ((Integer.parseInt(str))/((int) Math.pow(10, puissance)))%10;
				
				if(bit == 1) {
					if(hamming[i] == 1) {
						tabParite[puissance] = (tabParite[puissance]+1)%2;
					}
				}
			}
			s = tabParite[puissance] + s;
		}
		
		int indexErreur = Integer.parseInt(s, 2);
		
		String lab = "Code : ";
		for(int i=0 ; i < hamming.length ; i++) {
			lab += hamming[hamming.length-i-1];
		}
		labGauche.setText(lab);
		
		if(indexErreur != 0) {
			
			labMid.setText("Erreur à la position " + indexErreur);
			
			hamming[indexErreur-1] = (hamming[indexErreur-1]+1)%2;
			
			String lab2 = "Correction : ";
			for(int i=0 ; i < hamming.length ; i++) {
				
				lab2 += hamming[hamming.length-i-1];
			}
			labDroite.setText(lab2);
		}
		else {
			labMid.setText("Il n'y a pas d'erreur dans le code");
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Hamming();
	}

}
