package Code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileManager extends Object {

	private ArrayList<HeapFile> heapFilesList;

	private FileManager() {
		heapFilesList = new ArrayList<HeapFile>();

	}

	private static FileManager FileManagerInstance = new FileManager();

	public static FileManager getInstance() {
		return FileManagerInstance;
	}

	public ArrayList<HeapFile> getHeapFiles() {
		return heapFilesList;
	}

	public void setHeapFiles(ArrayList<HeapFile> heapFiles) {
		this.heapFilesList = heapFiles;
	}

	public void Init() {
		// a faire ............................
		for (int i = 0; i < DBDef.getInstance().getListRelDef().size(); i++) {
			HeapFile heapFile = new HeapFile((DBDef.getInstance().getListRelDef().get(i)));
			heapFilesList.add(heapFile);
		}
	}

	public void createRelationFile(RelDef relDef) throws IOException {
		HeapFile heapFile = new HeapFile(relDef);
		heapFilesList.add(heapFile);
		heapFile.creatNewOnDisk();
	
	}

	public Rid InsertRecordInRelation(Record record, String relName) throws IOException {
		
		Rid rid = new Rid(null, 0);

		for (int i = 0; i < heapFilesList.size(); i++) {
			if (heapFilesList.get(i).getRelDef().getNomRelation().equals(relName)) {
				rid = heapFilesList.get(i).insertRecord(record);
				return rid;
			}
		}
		return null;
	}

	public ArrayList<Record> SelectAllFromRelation(String relName) throws IOException {

		ArrayList<Record> recordList = new ArrayList<Record>();

		for (int i = 0; i < heapFilesList.size(); i++) {
			String relN = heapFilesList.get(i).getRelDef().getNomRelation();
			if (relN.equals(relName)) {
				recordList.addAll( (Collection<? extends Record>) heapFilesList.get(i).getRelDef());
			}
		}

		return recordList;
	}

	public ArrayList<Record> SelectFromRelation(String relName, int idxCol, String valeur) throws IOException {

		ArrayList<Record> listRecord = new ArrayList<Record>();
		ArrayList<Record> list = new ArrayList<Record>();
		list = SelectAllFromRelation(relName);

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getValues().get(idxCol).equals(valeur)) {
				Record record = list.get(i);
				listRecord.add(record);
			}
		}
		return listRecord;
	}

	public void reset() {
		this.heapFilesList.clear();
	}

}