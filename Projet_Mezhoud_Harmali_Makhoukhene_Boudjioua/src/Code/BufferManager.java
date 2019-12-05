package Code;

import java.io.IOException;
import java.util.ArrayList;

public class BufferManager {
	ArrayList<Frame> file;
	public Diskmanager diskManager;
	ArrayList<Frame> array;

	/** constructeur **/
	private BufferManager() {
		file = new ArrayList<Frame>();
		diskManager = Diskmanager.getInstance();
		array = new ArrayList<Frame>();
		init();
		//System.out.println("constructeur" + array.size());
	}

	/** Instance unique pré-initialisée */
	private static BufferManager INSTANCE = new BufferManager();

	/** Point d'accès pour l'instance unique du singleton */
	public static BufferManager getInstance() {
		return INSTANCE;

	}

	public void init() {
		for (int i = 0; i < Constantes.frameCount; i++) {
			array.add(new Frame());
		}
	}

	public byte[] getPage(PageId iPageId) throws IOException {

		// si y a l element dans un
		for (int i = 0; i < array.size(); i++) {
			Frame fr = array.get(i);

			if (fr.getPageid() != null && (array.get(i).getPageid().getPageIdx()) == (iPageId.getPageIdx())
					&& (array.get(i).getPageid().getFileIdx() == iPageId.getFileIdx())) {
				array.get(i).setPincount(array.get(i).getPincount() + 1);

				return array.get(i).getBuffer();

			}
		}
		// si la page n'est pas dans l'une des frames

		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).getPageid() == null) {

				diskManager.readPage(iPageId, array.get(i).getBuffer());
				array.get(i).setBuffer(array.get(i).getBuffer());
				array.get(i).setPageid(iPageId);
				array.get(i).setPincount(array.get(i).getPincount() + 1);
				array.get(i).setCompteur(0);
				return array.get(i).getBuffer();// on l'a rajouter

			}

		}

		Frame s = null;

		for (int i = 0; i < file.size(); i++) {
			if (file.get(i).getPincount() == 0) {

				s = file.get(i);
				break;

			}
		}
		for (int j = 0; j < array.size(); j++) {
			if (array.get(j) == s) {
				if (array.get(j).getDirty() == 1) {
					diskManager.writePage(array.get(j).getPageid(), array.get(j).getBuffer());
				}

				diskManager.readPage(iPageId, array.get(j).getBuffer());

				array.get(j).setPincount(array.get(j).getPincount() + 1);
				array.get(j).setBuffer(array.get(j).getBuffer());
				array.get(j).setPageid(iPageId);
				array.get(j).setDirty(0);
				array.get(j).setCompteur(0);

				return array.get(j).getBuffer();
			}
		}

		return null;
	}

	public void freePage(PageId iPageId, int iIsDirty) {

		for (int i = 0; i < array.size(); i++) {

			if (array.get(i).getPageid() != null && array.get(i).getPageid().getPageIdx() == iPageId.getPageIdx()
					&& array.get(i).getPageid().getFileIdx() == iPageId.getFileIdx()) {
				if (iIsDirty == 1) {

					array.get(i).setDirty(iIsDirty);

				}

				array.get(i).setPincount(array.get(i).getPincount() - 1);
				if (array.get(i).getPincount() == 0) {

					array.get(i).setCompteur(array.get(i).getCompteur() + 1);
					file.add(array.get(i));

					for (int j = 0; j < file.size(); j++) {
						if ((file.get(j).getPageid() == array.get(i).getPageid() && array.get(i).getCompteur() > 1)) {

							file.remove(j);

							break;
						}
					}

				}

				break;
			}
		}
	}

	public void flushAll() throws IOException {

		for (int i = 0; i < array.size(); i++) {

			if ((array.get(i).getDirty() == 1)) {

				diskManager.writePage(array.get(i).getPageid(), array.get(i).getBuffer());
				array.get(i).setDirty(0);

			}
		}

	}

	public void reset() throws IOException {

		flushAll();
	}
}