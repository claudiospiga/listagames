package Lista;

import javax.swing.*;



import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Gioco extends JFrame {
	//definizione degli attributi per il database
	private static final  String DB_URL ="jdbc:mysql://localhost:3306/videogiochi";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "marina97!";
	
	
	//definizione del blocco di query per la manipolazione dei record
	
	//CREAZIONE QUERY PER INSERIMENTO DATI NELLA TABELLA
	private static final String INSERT_QUERY = "INSERT INTO gioco(nome,tipologia,piattaforma,completato) VALUES(?,?,?,?)";

	//CREAZIONE QUERY PER LEGGERE DATI NELLA TABELLA
	private static final String SELECT_ALL_QUERY= "SELECT * FROM gioco";
	
	//AGGIORNARE
	private static final String UPDATE_QUERY = "UPDATE gioco SET nome =?,completato =? WHERE id=?";
	
	//CANCELLARE
	private static final String DELETE_QUERY = "DELETE FROM gioco WHERE id= ?";

	//ATTRIBUTO PER OUTPUT DATI DA DATABASE SU INTERFACCIA GRAFICA(GUI)
	private JTextArea outputArea;
	
	public  Gioco() {
		JFrame frame = new JFrame("videogiochi");
		setTitle("giochi");
		setSize(900, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//inizializzare l'oggetto di output
		outputArea = new JTextArea();
		outputArea.setEditable(false);//non modificabile la casella di testo
		
		//creazione del panello per contenere nel frame il nostro output
		JScrollPane scrollPane = new JScrollPane(outputArea);//creiamo la sua istanza
		
		//definizione & creazione dei bottoni necessari per la gestione del gestionale
		JButton addButton = new JButton("Aggiungi gioco");
		JButton viewButton = new JButton("Visualizza i giochi");
		JButton updateButton = new JButton(" segna completato il gioco");
		JButton deleteButton = new JButton("Elimina gioco");
		//gestione degli eventi onClick(eventi scatenanti dal click del bottone)
		
				addButton.addActionListener(new ActionListener() {//questo per intercettare il click dell utente 
					@Override
					public void actionPerformed(ActionEvent e) {
						aggiungi(); 
						
					}	
				});
				
				viewButton.addActionListener(new ActionListener() {//questo per intercettare il click dell utente 
					@Override
					public void actionPerformed(ActionEvent e) {
						printGioco();
					}	
				});
				
				updateButton.addActionListener(new ActionListener() {//questo per intercettare il click dell utente 
					@Override
					public void actionPerformed(ActionEvent e) {
						modifica();
						
					}	
				});
				
				deleteButton.addActionListener(new ActionListener() {//questo per intercettare il click dell utente 
					@Override
					public void actionPerformed(ActionEvent e) {
						elimina();
						
					}	
				});
				
				//creazione del panello(finestra) per la gestione dei bottoni 
				JPanel buttonPanel= new JPanel();
				
				buttonPanel.setLayout(new GridLayout(1,4));// creazione delle colonne per i bottoni, primo numero è la riga, il secnodo le colonnr
				
				//aggiunta dei bottoni al pannello creato
				buttonPanel.add(addButton);
				buttonPanel.add(viewButton);
				buttonPanel.add(updateButton);
				buttonPanel.add(deleteButton);
				
				//aggiunta dei pannelli jscrollpane e buttonpanel al frame( finestra principale)
				getContentPane().setLayout(new BorderLayout());
				getContentPane().add(scrollPane,BorderLayout.CENTER);
				getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		
	}
	
	//metodo inserimento
		private void  insertGioco(String nome, String tipologia,String piattaforma,boolean completato) {
			try {
				Connection conn= DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement pstmt= conn.prepareStatement(INSERT_QUERY);
				pstmt.setString(1,nome);
				pstmt.setString(2,tipologia);
				pstmt.setString(3,piattaforma);
				pstmt.setBoolean(4,completato);
				pstmt.executeUpdate();
				
				
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		//METODO MODIFICA
		private void updateGioco(String nome, boolean completato, int id) {
			try {
				Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(UPDATE_QUERY);
				pstmt.setString(1, nome);
				pstmt.setBoolean(2, completato);
				pstmt.setInt(3, id);
				pstmt.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		private void deleteGioco(int id) {
			try {
				Connection conn= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(DELETE_QUERY);
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		private void printGioco() {
			outputArea.setText("");
			try {
				Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_ALL_QUERY);
				//ciclo oer far stampare i dati
				while(rs.next()) {
					int id = rs.getInt("id");
					String nome = rs.getString("nome");
					String tipologia = rs.getString("tipologia");
					String piattaforma = rs.getString("piattaforma");
					boolean completato = rs.getBoolean("completato");
					if(completato == true) {
						outputArea.append("ID" + id + " \ngioco: " + nome + " \ntipologia " +tipologia+ "\npiattaforma " +piattaforma +"\ncompleatato? NON COMPLETATO" + "\n");
					}else {
						outputArea.append("ID" + id + " \ngioco: " + nome + " \ntipologia " +tipologia+ "\npiattaforma " +piattaforma +"\ncompleatato?  COMPLETATO" + "\n");
					}
				
				
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		//METODO AGGIUNGI
		private void aggiungi() {
			String nome = JOptionPane.showInputDialog("Inserisci il nome del gioco:");
			String tipologia = JOptionPane.showInputDialog("Inserisci la tipologia del gioco:");
			String piattaforma = JOptionPane.showInputDialog("Inserisci la piattaforma del gioco:");
			//per far inserire all'uente per forza qualcosa
			if(nome != null && !nome.isEmpty()) {
				//perche dobbiamo convertire il Boolean in una stringa		
				boolean completato = false;
				insertGioco(nome,tipologia,piattaforma,completato);
				outputArea.append("\ngioco aggiunto: "+ nome +"\ntipologia " + tipologia +"\npiattaforma " + piattaforma + " - \ncompletato? non completato" + "\n");//per scrivere nel pannello
			}
		}
		
		private void modifica() {
			int id= Integer.parseInt(JOptionPane.showInputDialog("inserisci l id del gioco che hai completato"));
			String nome = JOptionPane.showInputDialog("Inserisci il  nome del gioco");
			//perche dobbiamo convertire il booleano in una stringa
			boolean completato =false;
			
			String risposta = JOptionPane.showInputDialog("Inserisci SI se l hai completato, e NO se non l hai completato");
			if(risposta.equalsIgnoreCase("SI")) {
				completato = false;
			}else{
				completato = true;
			}
			
			updateGioco(nome,completato,id);
			//per scrivere nel pannello
			outputArea.append("gioco compleatato: ID "+id +" Nome: "+  nome +" - completato: "+ risposta + "\n");
		}
		
		//METODO ELIMINA
		private void elimina() {
			//perche dobbiamo convertire Int in una stringa
			int id = Integer.parseInt(JOptionPane.showInputDialog("Insersci ID del gioco da eliminare"));
			deleteGioco(id);
			//per scrivere nel pannello
			outputArea.append("gioco eliminato: ID "+ id+"\n");
		}
		
		//metodo main 
		public static void main(String[] args) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					Gioco pm = new Gioco();
					pm.setVisible(true);
					
				}
			});
			
		}
		
		
	

}
