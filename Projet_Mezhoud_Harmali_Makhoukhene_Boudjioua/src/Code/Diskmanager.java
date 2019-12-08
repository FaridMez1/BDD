package Code;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Diskmanager {
	public File f;

	private static Diskmanager instancediskmanager = new Diskmanager();
	private PageId oPageId;
	public static Diskmanager getInstance() {
		return instancediskmanager;
	}

	public void createFile(int fileIdx) throws IOException {
		File fichier = new File("/home/mezhoud/Projet_Mezhoud_Harmali_Makhoukhene_Bouredjioua/DB"+"Data"+ fileIdx + ".rf");
        fichier.createNewFile();
	}

	public void addPage(int fileIdx, PageId oPageId) throws IOException {

		RandomAccessFile file = new RandomAccessFile("/home/mezhoud/Projet_Mezhoud_Harmali_Makhoukhene_Bouredjioua/DB" +"Data" + fileIdx + ".rf", "rw");
		  
		byte[] b = new byte[Constantes.pageSize];

		double pageidx = ((file.length()) / Constantes.pageSize);
		int i = (int) ((pageidx) * Constantes.pageSize);

		file.seek(i);
		file.write(b);

		oPageId.setFileIdx(fileIdx);
		oPageId.setPageIdx((int) pageidx);
		file.close();
	}

	public void readPage(PageId xPageId, byte[] obuff) throws IOException {
		
		RandomAccessFile file = new RandomAccessFile("/home/mezhoud/Projet_Mezhoud_Harmali_Makhoukhene_Bouredjioua/DB"+"Data" + xPageId.getFileIdx() + ".rf", "r");
		
		int i = (xPageId.getPageIdx() * Constantes.pageSize);
		file.seek(i);
		file.read(obuff);
		file.close();

	}

	public void writePage(PageId xPageId, byte[] buff) throws IOException {
		RandomAccessFile file = new RandomAccessFile("/home/mezhoud/Projet_Mezhoud_Harmali_Makhoukhene_Bouredjioua/DB" + "Data" + xPageId.getFileIdx() + ".rf", "rw");

		int i = xPageId.getPageIdx() * Constantes.pageSize;

		file.seek(i);
		file.write(buff);

		file.close();
	}

}
