package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Edata extends HashMap<String, List<Record>> {
	private static final long serialVersionUID = 1L;
	private HashMap<String, HashMap<String, String>> eng2cn = new HashMap<String, HashMap<String, String>>();

	public void put_cn(String tag, String eng, String cn) {
		HashMap<String, String> hm = eng2cn.get(tag);
		if (hm != null)
			hm.put(eng, cn);
		else {
			hm = new HashMap<String, String>();
			hm.put(eng, cn);
			eng2cn.put(tag, hm);
		}
	}

	public HashMap<String, String> get_eng2cn(String tag) {
		return eng2cn.get(tag);
	}

	public void insert(String TagName, Record single) {
		List<Record> lhm = this.get(TagName);
		if (lhm == null) {
			lhm = new ArrayList<Record>();
			lhm.add(single);
			this.put(TagName, lhm);
		} else {
			lhm.add(single);
		}
	}

	public Record query(String TagName, String KeyName, String value) {
		List<Record> lhm = this.get(TagName);
		if (lhm == null)
			return null;
		for (int i = 0; i < lhm.size(); i++) {
			Record hm = lhm.get(i);
			String sid = hm.get(KeyName);
			if (sid == null)
				continue;
			else if (sid.equals(value))
				return hm;
		}
		return null;
	}

	public QueryResult queryAll(String TagName, String KeyName, String value) {
		QueryResult rds = new QueryResult();
		List<Record> lhm = this.get(TagName);
		if (lhm == null)
			return rds;
		if(value == null) {
			rds.addAll(lhm);
			return rds;
		}
		for (int i = 0; i < lhm.size(); i++) {
			Record hm = lhm.get(i);
			String sid = hm.get(KeyName);
			if (sid == null)
				continue;
			else if (sid.equals(value))
				rds.add(hm);
		}
		return rds;
	}
}
