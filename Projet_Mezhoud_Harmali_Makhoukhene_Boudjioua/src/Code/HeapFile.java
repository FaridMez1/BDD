package Code;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

class HeapFile {
	private RelDef relDef;

	public HeapFile(RelDef relDef) {
		this.relDef = relDef;
	}

	BufferManager bufferManager = BufferManager.getInstance();
	Diskmanager diskManager = Diskmanager.getInstance();

	public RelDef getRelDef() {
		return relDef;
	}

	public void creatNewOnDisk() throws IOException {

		diskManager.createFile(relDef.getFileIdx());
		PageId headPage = new PageId(relDef.getFileIdx(), 0);
		diskManager.addPage(relDef.getFileIdx(), headPage);
		byte[] buffer = bufferManager.getPage(headPage);
		ByteBuffer b = ByteBuffer.wrap(buffer);
		for (int i = 0; i < Constantes.pageSize; i++) {
			b.put((byte) 0);

		}
		bufferManager.freePage(headPage, 1);
	}

	// Ajouter une Page de données a un fichier
	public PageId addDataPage(PageId pageId) throws IOException {
		PageId headPage = new PageId(relDef.getFileIdx(), 0);
		byte[] headbyteBuffer = bufferManager.getPage(headPage);

		HeaderPage headerPage = new HeaderPage();
		// recuperer le contenu de headerPage
		ByteBuffer bbBuffer = ByteBuffer.wrap(headbyteBuffer);
		int nbrDataPage = bbBuffer.getInt();
		for (int i = 0; i < nbrDataPage; i++) {
			headerPage.addDataP(bbBuffer.getInt());

		}

		// getSlotCount ------------------------------------------------------------a
		// verfier
		Diskmanager.getInstance().addPage(relDef.getFileIdx(), pageId);
		headerPage.addDataP(Constantes.pageSize - relDef.getRecordSize());
		ArrayList<Integer> list;
		
		list = headerPage.getDataPages();
		headerPage.setDataPages(list);
		headerPage.setNbrDataPage(headerPage.getNbrDataPage() + 1);
		// ecriture dans le headerPage

		bbBuffer.putInt(nbrDataPage);
		for (int i = 0; i < nbrDataPage; i++) {
			bbBuffer.putInt(list.get(i));

		}

		byte[] dataPageBuffer = bufferManager.getPage(pageId);

		for (int i = 0; i < relDef.getSlotCount(); i++) {

			dataPageBuffer[i] = 0;

		}
		bufferManager.freePage(pageId, 1);
		return pageId;

	}

	public PageId getFreeDataPageId() throws IOException {
		PageId pageId = new PageId(0, 0);
		PageId headPage = new PageId(relDef.getFileIdx(), 0);
		byte[] headbyteBuffer = bufferManager.getPage(headPage);
		HeaderPage headerPage = new HeaderPage();
		// recuperer le contenu de headerPage
		ByteBuffer bbBuffer = ByteBuffer.wrap(headbyteBuffer);
		int nbrDataPage = bbBuffer.getInt();
		for (int i = 0; i < nbrDataPage; i++) {
			headerPage.addDataP(bbBuffer.getInt());
		}
		ArrayList<Integer> list;
		list = headerPage.getDataPages();

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i) > 0) {

				pageId.setFileIdx(relDef.getFileIdx());
				pageId.setPageIdx(i);

				bufferManager.freePage(headPage, 1);
				return pageId;

			}

		}
		addDataPage(pageId);
		return pageId;

	}

	public Rid writeRecordToDataPage(Record record, PageId pageId) throws IOException {
		record=new Record(this.relDef);
		PageId headPage = new PageId(relDef.getFileIdx(), 0);
		byte[] buff = bufferManager.getPage(pageId);
		byte[] bufferhead = bufferManager.getPage(headPage);
		// lecture de header page
		HeaderPage head = new HeaderPage();
		ArrayList<Integer> list;
		list = head.getDataPages();
		ByteBuffer bbBuffer = ByteBuffer.wrap(bufferhead);
		int nbrDataPage = bbBuffer.getInt();
		for (int i = 0; i < nbrDataPage; i++) {
			head.addDataP(bbBuffer.getInt());

		}

		// ------------------------------------------------
		for (int i = 0; i < relDef.getSlotCount(); i++) {
			if (buff[i] == (byte) 0) {
				record.writeToBuffer(buff, i);
				buff[i] = (byte) 1;

				Rid rid = new Rid(pageId, i);

				// recopie dans le headerpage
				bbBuffer.putInt(nbrDataPage);
				for (i = 0; i < nbrDataPage; i++) {

					bbBuffer.putInt(list.get(i) - 1);

				}
				bufferManager.freePage(headPage, 1);
				bufferManager.freePage(pageId, 1);
				return rid;
			}

		}
		return null;

	}

	// recup les records dans chaque page -----------------------
	public ArrayList<Record> getRecordsInDataPage(PageId pageId) throws IOException {
		ArrayList<Record> records = new ArrayList<Record>();

		byte[] buffer = bufferManager.getPage(pageId);

		for (int i = 0; i < relDef.getSlotCount(); i++) {

			if (buffer[i] == (byte) 1) {

				Record record = new Record(relDef);
				record = record.readFromBuffer(buffer, i);

				records.add(record);

			}
		}
		return records;
	}

	public Rid insertRecord(Record record) throws IOException {
		record=new Record(this.relDef);
		PageId page = new PageId(-1, -1);
		try {
			page = getFreeDataPageId();
		} catch (IOException e) {
			System.err.println("Impossible de trouver une page");
		}
		Rid rid = new Rid(null, 0);
		rid = writeRecordToDataPage(record, page);

		return rid;

	}

	public ArrayList<Record> getAllRecords() throws IOException {
		ArrayList<Record> listRecord = new ArrayList<Record>();
		PageId page = new PageId(relDef.getFileIdx(), 0);
		HeaderPage head = new HeaderPage();
		int nbDataPage = head.getNbrDataPage();
		for (int i = 1; i < nbDataPage; i++) {
			page.setPageIdx(i);

			listRecord.addAll(i, getRecordsInDataPage(page));
		}

		return listRecord;
	}

}
