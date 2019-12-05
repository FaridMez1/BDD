package Code;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static Diskmanager diskmanager;
	private static byte[] buff;
	private static PageId page;
	public static String chemin;

	public static void main(String[] args) throws IOException {
		Constantes.chemin = "Projet_Mezhoud_Harmali_Makhoukhene_Bouredjioua/DB";
		/*DBManager dbManager = DBManager.getInstance();
		File file = new File("/home/bouredjioua/Bureau/BDD/S1-1.csv");
		String[] tab = { "insert", "R", "1", "aab", "2" };
		
		ArrayList<String> liste = new ArrayList<String>();
		for (int i = 3; i < tab.length; i++) {
			liste.add(tab[i]);
		}
		try {
			dbManager.insert(liste);
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		// DBManager dbm= new DBManager();
		/*
		 * DBManager dbm= DBManager.getInstance(); dbm.Init();
		 * 
		 * Scanner sc= new Scanner(System.in); String chaine = null; do {
		 * 
		 * System.out.println("taper une commande"); chaine = sc.next(); if
		 * (chaine.equals("exit")) dbm.Finish(); else dbm.ProcessCommand(chaine); }
		 * while (!(chaine.equals("exit")));
		 */
		/*
		 * Scanner sc = new Scanner(System.in); String chaine = null;
		 * 
		 * System.out.println("taper une commande"); chaine = sc.next();
		 * 
		 * diskmanager = Diskmanager.getInstance(); diskmanager.createFile(0); page =
		 * new PageId(0, 0);
		 * 
		 * buff = chaine.getBytes(); diskmanager.addPage(0, page);
		 * diskmanager.writePage(page, buff);
		 * 
		 */
		
		Constantes.chemin ="Projet_Mezhoud_Harmali_Makhoukhene_Bouredjioua/DB";
		// DBManager dbm= new DBManager();
		DBManager dbm = DBManager.getInstance();
		dbm.Init();

		Scanner sc = new Scanner(System.in);
		String commande;

		ArrayList<String> chaine;

		do {

			System.out.println("taper une commande");
			commande = sc.nextLine();
			if (commande.equals("exit")) {
				dbm.Finish();
			}

			else {

				try {
					String[] tab = commande.split(" ");
					chaine = new ArrayList<String>();
					System.out.println(tab);
					for (int i = 0; i < tab.length; i++) {
						chaine.add(tab[i]);

					}

					dbm.ProcessCommand(chaine);
				} catch (IOException e) {
					System.err.println("Impossible de lire la commande");
				}
			}
		} while (!(commande.equals("exit")));

	

	}
}