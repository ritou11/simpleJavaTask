package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	private String filepath;
	private String configpath;
	private List<String> toParse = new ArrayList<String>();
	private Pattern p_tag = Pattern.compile("<(.*)::.*"); // the pattern to
															// search for
	private Edata edt = new Edata();

	public Parser(String fp, String cp) {
		filepath = fp;
		configpath = cp;
	}

	public Parser() {
	}

	public int LoadConfig() {
		try (BufferedReader br = new BufferedReader(new FileReader(configpath))) {
			String line = br.readLine();
			while (line != null) {
				toParse.add(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return toParse.size();
	}

	public int LoadConfig(String cp) {
		configpath = cp;
		return LoadConfig();
	}

	public int ParseFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line = br.readLine();
			String tag = "";
			List<String> names = new ArrayList<String>();
			int n_tags = 0;
			while (line != null) {
				if (line.startsWith("</")) {
					tag = "";
				} else if (line.startsWith("<")) {
					Matcher m = p_tag.matcher(line);
					if (m.find()) {
						tag = m.group(1);
						names.clear();
						n_tags = 0;
					}
				} else if (line.startsWith("@")) {
					String[] ts = line.split("\\s+");
					for (int i = 1; i < ts.length; i++) {
						names.add(ts[i]);
					}
					n_tags = ts.length - 1;
				} else if (line.startsWith("#")) {
					if (!tag.equals("") && n_tags > 0 && toParse.contains(tag)) {
						String[] ds = line.split("\\s+");
						Record hm = new Record();
						for (int i = 0; i < n_tags; i++) {
							hm.put(names.get(i), ds[i + 1]);
						}
						edt.insert(tag, hm);
						// System.out.println(new
						// String(hm.toString().getBytes("gbk"),"UTF-8"));
					}
				} else if (line.startsWith("//")) {
					if (!tag.equals("") && n_tags > 0 && toParse.contains(tag)) {
						String[] ds = line.split("\\s+");
						for (int i = 0; i < n_tags; i++) {
							edt.put_cn(tag ,names.get(i), ds[i + 1]);
						}
					}
				} else {
					// jump this line
				}
				line = br.readLine();
			}
			return edt.size();
		} catch (IOException e) {
			//e.printStackTrace();
			return -1;
		}
	}

	public int ParseFile(String fp) {
		filepath = fp;
		return ParseFile();
	}

	public List<String> getToParse() {
		return toParse;
	}

	public Edata getEdata() {
		return edt;
	}
}
