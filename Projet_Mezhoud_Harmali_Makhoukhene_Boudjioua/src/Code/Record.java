package Code;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Record {

	RelDef relDef;
	List<String> values;

	public Record(RelDef relDef) {
		this.relDef = relDef;
		values = new ArrayList<String>();
	}

	public Record() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<String> getValues() {
		return (ArrayList<String>) values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	/**
	 * 
	 * @param buff
	 *            un buffer pour ecrire les valeurs des records .
	 * @param position
	 *            indice du buffer .
	 */

	public void writeToBuffer(byte[] buff, int position) {
      
		ByteBuffer bBuff = ByteBuffer.wrap(buff);
		bBuff.position(position);

		for (int i = 0; i < values.size(); i++) {
			if (relDef.getTypeColonnes().get(i).equals("float")) {
				bBuff.putFloat(Float.parseFloat(values.get(i)));
			} else if (relDef.getTypeColonnes().get(i).equals("int")) {
				bBuff.putInt(Integer.parseInt(values.get(i)));
			} else if (relDef.getTypeColonnes().get(i).contains("string")) {
				String[] sTab = relDef.getTypeColonnes().get(i).split("string");
				for (int j = 0; j < Integer.valueOf(sTab[1]); j++) {
					bBuff.putChar(values.get(i).charAt(j));

				}
			}
		}
	}

	/**
	 * 
	 * @param buff
	 *            un flux binaire a lire
	 * @param position
	 *            position du flux
	 */

	public Record readFromBuffer(byte[] buff, int position) {
		ByteBuffer bBuff = ByteBuffer.wrap(buff);
		bBuff.position(position);
		String str = "";
		Record record = new Record(relDef);
		List<String> li = new ArrayList<String>();

		for (int i = 0; i < relDef.getTypeColonnes().size(); i++) {
			if (relDef.getTypeColonnes().get(i).equals("float")) {
				str = "" + bBuff.getFloat();
				li.add(str);
				record.setValues(li);
			} else if (relDef.getTypeColonnes().get(i).equals("int")) {
				li.clear();
				str = "" + bBuff.getInt();
				li.add(str);
				record.setValues(li);
			} else if (relDef.getTypeColonnes().get(i).equals("string")) {
				li.clear();
				String[] sTab = relDef.getTypeColonnes().get(i).split("string");
				int k = Integer.valueOf(sTab[1]);
				String str1 = "";

				for (int j = 0; j < k; j++) {
					str1 = str1 + bBuff.getChar();
				}
				li.add(str1);
				record.setValues(li);
			}
		}

		return record;
	}

}
