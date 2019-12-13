package Code;

import java.util.ArrayList;

public class HeaderPage {
	private int nbrDataPage;
	 int slotLibre=4096;
	private ArrayList<Integer> dataPages;

	public HeaderPage() {
		nbrDataPage = 0;
		this.dataPages = new ArrayList<Integer>();
	}

	public int getNbrDataPage() {
		return nbrDataPage;

	}

	public void setNbrDataPage(int nbrDataPage) {
		this.nbrDataPage = nbrDataPage;
	}

	public ArrayList<Integer> getDataPages() {
		return dataPages;

	}

	public void setDataPages(int dataPage) {
		this.dataPages.add(dataPage);

	//public void addDataP( int slotLibre) {

		//this.dataPages.add(slotLibre);
       //setDataPages(dataPages);
	}
}
