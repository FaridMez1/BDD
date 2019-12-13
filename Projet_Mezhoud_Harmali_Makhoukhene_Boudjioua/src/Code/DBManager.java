package Code;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
	DBDef dbDef = DBDef.getInstance();
	BufferManager bufferManager = BufferManager.getInstance();
	FileManager fileManager = FileManager.getInstance();

	private static DBManager DbmInstance = new DBManager();

	public static DBManager getInstance() {
		return DbmInstance;
	}

	public void Init() {
		DBDef.getInstance().Init();
		FileManager.getInstance().Init();

	}

	public void Finish() {
		DBDef.getInstance().Finish();
	}

	public void ProcessCommand(ArrayList<String> maCommande) throws IOException {

		switch (maCommande.get(0)) {
		case "clean":
			clean();
			break;
		case "create":
			create(maCommande);
			break;

		case "select":
			select(maCommande);
			break;

		case "selectAll":
			selectAll(maCommande);
			break;

		case "insert":
			insert(maCommande);
			break;

		case "insertAll":
			insertAll(maCommande);
			break;

		default:
			System.out.println("Votre commande est incorrecte ! ");
			break;
		}

	}

	//
	public void create(ArrayList<String> maCommande) throws IOException {
		String relName = maCommande.get(1);
		int nbColonnes = Integer.valueOf(maCommande.get(2));

		String nomTypeColonne;

		ArrayList<String> array = new ArrayList<String>();
		for (int i = 0; i < nbColonnes; i++) {

			nomTypeColonne = maCommande.get(3 + i);
			array.add(nomTypeColonne);

		}
		createRelation(relName, nbColonnes, array);
	}

	public void createRelation(String nomRelation, int nbnbColonnes, List<String> typesnbColonnes) throws IOException {
		RelDef reldef = new RelDef(nomRelation, nbnbColonnes, typesnbColonnes);
		// DBDef.getInstance().addRellation(reldef);
		reldef.setNbColonnes(nbnbColonnes);
		reldef.setNomRelation(nomRelation);
		reldef.setTypeColonnes(typesnbColonnes);
		// calculer le recordSizex²

		int sommeRecord = 0;
		for (int i = 0; i < typesnbColonnes.size(); i++) {
			if (reldef.getTypeColonnes().get(i).equals("string")) {
				String[] sTab = reldef.getTypeColonnes().get(i).split("string");

				int somme = Integer.valueOf(sTab[1]);

				sommeRecord = sommeRecord + (2 * somme);
			}

			else if (reldef.getTypeColonnes().get(i).equals("int")) {
				sommeRecord = sommeRecord + 4;
			}

			else if (reldef.getTypeColonnes().get(i).equals("float")) {
				sommeRecord = sommeRecord + 4;
			}
		}

		// calculer slotCount
		int slotCount = (int) (Constantes.pageSize / sommeRecord);
		// DBDef.getInstance().addRellation(reldef);

		int bytmap = slotCount / sommeRecord;
		slotCount = slotCount - bytmap;
		reldef.setRecordSize(sommeRecord);

		reldef.setSlotCount(slotCount);

		/////// modifier la valeur de filedIdx

		// tp4 a les brobros
		reldef.setFileIdx(dbDef.getCompteur());
		dbDef.addRellation(reldef);

		fileManager.createRelationFile(reldef);
	}
	//// methode similaire a createRelation ....
	// public void create(ArrayList<String>commande) {
	// //StringTokenizer st=null ;
	// String relName = commande.get(1);
	// int nbColonnes = Integer.valueOf(commande.get(2));
	// ArrayList<String> typesnbColonnes = null;
	// typesnbColonnes=(ArrayList<String>) commande.subList(0, 3);
	//
	// System.out.println(nbColonnes);
	//
	// do {
	// int i=0;
	// typesnbColonnes.toArray()[i]=st.nextToken();
	// i++;
	// }while(st.hasMoreTokens());
	// createRelation(relName, nbColonnes, typesnbColonnes);
	// }

	// la methode clean appelle les methodes reset de chaque classe pour remetre
	// tout a 0
	public void clean() throws IOException {

		// supprimer les fichiers .rf et le fichier Catalog.def

		File fileCatlg = new File("/home/mezhoud/Projet_Mezhoud_Harmali_Makhoukhene_Bouredjioua/DB/" + "catalog.def");
		fileCatlg.delete();

		File f = new File("/home/mezhoud/Projet_Mezhoud_Harmali_Makhoukhene_Bouredjioua/DB");

		File[] listeFichiers = f.listFiles();

		for (int i = 0; i < listeFichiers.length; i++) {
			listeFichiers[i].delete();
		}

		dbDef.reset();
		bufferManager.reset();
		fileManager.reset();
	}

	// select tous les records qui ont une valeur sur une colonne donnée
	public void select(ArrayList<String> commande) throws IOException {
		String nomRelation = commande.get(1);
		int indiceCommande = Integer.valueOf(commande.get(2)) - 1;
		String valeur = commande.get(3);
		ArrayList<Record> listRecords = new ArrayList<Record>();
		listRecords = (ArrayList<Record>) fileManager.SelectFromRelation(nomRelation, indiceCommande, valeur);

		for (int i = 0; i < listRecords.size(); i++) {
			System.out.println(listRecords.get(i).getValues().toString());
		}
		System.out.println("Total records : " + listRecords.size());

	}

	// la commande selectAll ...

	// prends en param une chaine de caracteres qui est le nom de la relation ..

	public void selectAll(ArrayList<String> commande) throws IOException {

		String relName = commande.get(1);
		ArrayList<Record> allRecords = new ArrayList<Record>();
		// RelDef reldef = new RelDef(relName, 0, null);
		// HeapFile heapFile = new HeapFile(reldef);
		for (int i = 0; i < fileManager.getHeapFiles().size(); i++) {
			allRecords.addAll(fileManager.getHeapFiles().get(i).getAllRecords());
		}
		for (int i = 0; i < allRecords.size(); i++) {
			for (int k = 0; k < allRecords.get(i).getValues().size(); k++) {
				System.out.println(allRecords.get(i).getValues().get(k).toString() + " ; ");
			}
		}
		System.out.println("Total records = " + allRecords.size());
	}

	public void insert(ArrayList<String> commande) throws IOException {
		Record record = new Record();
		FileManager fileManager = FileManager.getInstance();

		String relName = commande.get(1);

		// j'ai met celui en commentaire car je pense on a pas besoin

		// record.relDef.setNomRelation(relName);
		List<String> li = new ArrayList<String>();
		for (int i = 2; i < commande.size(); i++) {
			li.add(commande.get(i));
		}
        
		record.setValues(li);
		fileManager.InsertRecordInRelation(record, relName);
	}

	public void insertAll(ArrayList<String> commande) throws IOException {

		Record record = new Record();

		String relName = commande.get(1);
		String fileName = commande.get(2);

		File file = new File(Main.chemin + fileName);

		String separ = ",";
		String ligne;

		List<String> recordLines = new ArrayList<String>();
		List<Record> recordsList = new ArrayList<Record>();

		FileManager fileManager = FileManager.getInstance();

		// commenter .....
		FileReader fileReader = new FileReader(file);
		BufferedReader bReader = new BufferedReader(fileReader);

		while ((ligne = bReader.readLine()) != null) {
			recordLines.add(ligne.toString());
		}

		// inserer chaque ligne dans le record...
		for (int i = 0; i < recordLines.size(); i++) {
			String[] tab = recordLines.get(i).split(separ);
			// inserer les nbColonnes du record dans une liste
			List<String> li = new ArrayList<String>();
			for (String rec : tab) {
				li.add(rec.toString());
			}
			// ajouter tout les elements du tableau comme records ...
			record.setValues(li);
			// ajouter le record dans la liste des record .....
			recordsList.add(record);
			System.out.println(recordsList.toString());
		}

		for (Record rec : recordsList) {
			fileManager.InsertRecordInRelation(rec, relName);
			// System.out.println(fileManager.SelectAllFromRelation(relName));
		}

	}

	// methode delete supprime tous les records qui ont une valeur sur une colonne
	// donnée
	public void delete(ArrayList<String> commande) {
		try {
			String nomRelation = commande.get(1);
			int indiceCommande = Integer.valueOf(commande.get(2)) - 1;
			String valeur = commande.get(3);
			ArrayList<Record> listRecord = new ArrayList<Record>();
			listRecord = (ArrayList<Record>) fileManager.SelectFromRelation(nomRelation, indiceCommande, valeur);
			for (int i = 0; i < listRecord.size(); i++) {
				int somme = 0;
				if ((listRecord.remove(i).getValues()) != null) {
					somme++;
					System.out.println(listRecord.get(i).getValues().toString());
					System.out.println("\n");
				}
				System.out.println("Total deleted records = " + somme);

			}

		} catch (IOException e) {
			System.err.println("Impossible d'accéder au fichier, Erreur E/S ");
		}

	}

}