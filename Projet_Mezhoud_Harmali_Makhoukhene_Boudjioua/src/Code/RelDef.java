package Code;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.VoiceStatus;

//a refaire 
public class RelDef {
	private int fileIdx, recordSize, slotCount;
	private String nomRelation;
	private int nbcolonnes;
	private List<String> typesColonnes;

	public RelDef(String nomRelation, int nbcolonnes, List<String> typesColonnes) {
		typesColonnes = new ArrayList<String>();
		nomRelation = null;
		this.nbcolonnes = nbcolonnes;
		this.fileIdx = fileIdx;
		this.recordSize = recordSize;
		this.slotCount = slotCount;
	}

	public List<String> getTypeColonnes() {
		return typesColonnes;
	}

	public void setTypeColonnes(List<String> list) {
		this.typesColonnes = list;
	}

	public String getNomRelation() {
		return nomRelation;
	}

	public void setNomRelation(String relName) {
		this.nomRelation = relName;
	}

	public int getNbColonnes() {
		return nbcolonnes;
	}

	public void setNbColonnes(int nbC) {
		this.nbcolonnes = nbC;
	}

	public int getFileIdx() {
		return fileIdx;
	}

	public int getRecordSize() {
		return recordSize;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setFileIdx(int fIdx) {
		fileIdx = fIdx;
	}

	public void setRecordSize(int rdSize) {
		recordSize = rdSize;
	}

	public void setSlotCount(int slotC) {
		slotCount = slotC;
	}

}