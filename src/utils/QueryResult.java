package utils;

import java.util.ArrayList;
import java.util.HashMap;

public class QueryResult extends ArrayList<Record> {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.size(); i++) {
			if (this.size() > 1)
				sb.append(String.format("%d.\n", i + 1));
			sb.append(this.get(i).toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public String toString(HashMap<String, String> hm) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.size(); i++) {
			if (this.size() > 1)
				sb.append(String.format("%d.\n", i + 1));
			sb.append(this.get(i).toString(hm));
			sb.append("\n");
		}
		return sb.toString();
	}
}
