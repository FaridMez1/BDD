package Code;

public class Frame {
	private byte[] buffer;
	private PageId pageid;
	private int pincount;
	private int dirty;
	private int compteur;

	public Frame() {
		this.buffer = new byte[Constantes.pageSize];
		this.pageid = null;
		this.setPincount(0);
		this.setDirty(0);
		this.setCompteur(0);
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public PageId getPageid() {
		return pageid;
	}

	public void setPageid(PageId pageid) {
		this.pageid = pageid;
	}

	public int getPincount() {
		return pincount;

	}

	public void setPincount(int pincount) {
		this.pincount = pincount;
	}

	public int getDirty() {
		return dirty;

	}

	public void setDirty(int dirty) {
		this.dirty = dirty;
	}

	public int getCompteur() {
		return compteur;

	}

	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}

}
