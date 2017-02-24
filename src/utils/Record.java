package utils;

import java.util.HashMap;
import java.util.Map;

public class Record extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (Map.Entry<String, String> entry : this.entrySet()) {
			sb.append("    ");
			sb.append(entry.getKey());
			sb.append(" : ");
			sb.append(entry.getValue());
			sb.append(",\n");
		}
		sb.append("}");
		return sb.toString();
	}

	public String toString(HashMap<String, String> eng2cn) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (Map.Entry<String, String> entry : this.entrySet()) {
			String cn = eng2cn.get(entry.getKey());
			sb.append("    ");
			sb.append(entry.getKey());
			if (cn != null) {
				sb.append("(");
				sb.append(cn);
				sb.append(")");
			}
			sb.append(" : ");
			sb.append(entry.getValue());
			sb.append(",\n");
		}
		sb.append("}");
		return sb.toString();
	}
}
