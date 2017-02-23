package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Edata extends HashMap<String, List<HashMap<String,String>>> {
	private static final long serialVersionUID = 1L;
	
	public void insert(String TagName, HashMap<String,String> single){
		List<HashMap<String,String>> lhm = this.get(TagName);
		if(lhm == null){
			lhm = new ArrayList<HashMap<String,String>>();
			lhm.add(single);
			this.put(TagName, lhm);
		}else{
			lhm.add(single);
		}
	}
	public HashMap<String,String> query(String TagName, String id){
		List<HashMap<String,String>> lhm = this.get(TagName);
		if(lhm == null) return null;
		for(int i= 0;i<lhm.size();i++){
			HashMap<String, String> hm = lhm.get(i);
			String sid = hm.get("id");
			if(sid==null) continue;
			else if(sid.equals(id)) return hm;			
		}
		return null;
	}
}
