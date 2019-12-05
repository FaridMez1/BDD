package Code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DBDef implements Serializable {
	private ArrayList<RelDef> Dbdef;
	private int compteur;

	public DBDef() {
		Dbdef = new ArrayList<RelDef>();
		this.compteur = 0;
	}

	private static DBDef DbdefInstance = new DBDef();

	public static DBDef getInstance() {
		return DbdefInstance;
	}

	public ArrayList<RelDef> getListRelDef() {
		return Dbdef;
	}

	public ArrayList<RelDef> getListDbdef() {
		return Dbdef;
	}

	public int getCompteur() {
		return compteur;
	}

	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}

	public void setListRelDef(ArrayList<RelDef> Dbdef) {
		this.Dbdef = Dbdef;
	}

	public void Init() {

		try {
			File save = new File(Main.chemin + "Catalog.def");
			FileInputStream fi = new FileInputStream(save);
			ObjectInputStream ob = new ObjectInputStream(fi);
			DBDef df = ((DBDef) ob.readObject());
			this.Dbdef = df.Dbdef;
			this.compteur = df.compteur;
			fi.close();
			ob.close();

		} catch (FileNotFoundException e) {
			System.err.println("Impossible de trouver le fichier !");
		} catch (IOException e) {
			System.err.println("Impossible d'ouvrir le fichier !");
		} catch (ClassNotFoundException e) {
			System.err.println("Impossible de trouver le fichier");
		}

	}

	public void Finish() {
		try (FileOutputStream fos = new FileOutputStream(Main.chemin + "Catalog.def");
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(this);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public void addRellation(RelDef relation) {
		this.Dbdef.add(relation);

		compteur++;
	}

	public void reset() {
		this.compteur = 0;
		this.Dbdef.clear();
	}

}