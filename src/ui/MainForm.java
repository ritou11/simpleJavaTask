package ui;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import utils.Edata;
import utils.Parser;
import utils.QueryResult;
import utils.Record;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainForm {

	protected Shell shlEfileparser;
	private Text textPath;
	private Text textLog;
	private Text textKeyValue;
	Combo combTags;
	Combo combKeys;
	private Parser pr;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainForm window = new MainForm();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		pr = new Parser();
		if (pr.LoadConfig("F:\\Users\\ThinkPad\\workspace\\EFileParser\\toParse.config") < 0) {
			System.exit(0);
		}

		while (!shlEfileparser.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlEfileparser = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		shlEfileparser.setImage(SWTResourceManager.getImage(MainForm.class, "/com/sun/javafx/scene/web/skin/FontBackgroundColor_16x16_JFX.png"));
		shlEfileparser.setSize(new Point(470, 447));
		shlEfileparser.setVisible(true);
		shlEfileparser.setText("QS File Parser");

		combTags = new Combo(shlEfileparser, SWT.READ_ONLY);
		combTags.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				combKeys.removeAll();
				Record rd = pr.getEdata().get(combTags.getText()).get(0);
				for (String key : rd.keySet()) {
					combKeys.add(key);
				}
			}
		});
		combTags.setBounds(76, 40, 88, 25);

		combKeys = new Combo(shlEfileparser, SWT.READ_ONLY);
		combKeys.setBounds(197, 40, 88, 25);

		Button btnParse = new Button(shlEfileparser, SWT.NONE);
		btnParse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int nps = pr.ParseFile(textPath.getText());
				if (nps < 0)
					textLog.setText("No file!");
				else {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("Parsed:%d tag(s), including:\n",
							nps));
					Edata edt = pr.getEdata();
					for (Map.Entry<String, List<Record>> entry : edt.entrySet()) {
						sb.append("--- ");
						sb.append(entry.getKey());
						sb.append(String.format(", with %d records;\n", entry
								.getValue().size()));
					}
					if (!edt.containsKey("Bus")) {
						sb.append("No tag called \"Bus\"!\n");
					} else {
						int n220 = edt.queryAll("Bus", "volt", "220").size();
						int n500 = edt.queryAll("Bus", "volt", "500").size();
						sb.append(String
								.format("Information of Bus:\n--- 220kV: %d\n--- 500kV: %d\n",
										n220, n500));
					}
					if (!edt.containsKey("ACline")) {
						sb.append("No tag called \"ACline\"!\n");
					} else {
						HashSet<Record> hs = new HashSet<Record>();
						hs.addAll(edt.queryAll("ACline", "I_node", "''"));
						hs.addAll(edt.queryAll("ACline", "J_node", "''"));
						sb.append(String
								.format("Information of ACLine:\n--- Suspended line: %d\n",
										hs.size()));
					}
					if (!edt.containsKey("DCline")) {
						sb.append("No tag called \"DCline\"!\n");
					} else {
						HashSet<Record> hs = new HashSet<Record>();
						hs.addAll(edt.queryAll("DCline", "I_node", "''"));
						hs.addAll(edt.queryAll("DCline", "J_node", "''"));
						sb.append(String
								.format("Information of DCLine:\n--- Suspended line: %d\n",
										hs.size()));
					}
					textLog.setText(sb.toString());
					for (String key : edt.keySet()) {
						combTags.add(key);
					}
				}
			}
		});
		btnParse.setBounds(377, 9, 80, 27);
		btnParse.setText("Parse");

		textPath = new Text(shlEfileparser, SWT.BORDER);
		textPath.setText("F:\\Users\\ThinkPad\\workspace\\EFileParser\\example.QS");
		textPath.setBounds(66, 11, 219, 23);

		textLog = new Text(shlEfileparser, SWT.BORDER | SWT.V_SCROLL);
		textLog.setBounds(10, 71, 447, 337);

		Button btnBrowse = new Button(shlEfileparser, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				FileFilter filter = new FileNameExtensionFilter("QS File", "QS");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileFilter(filter);
				int rtv = fileChooser.showOpenDialog(null);
				if (rtv == JFileChooser.APPROVE_OPTION) {
					textPath.setText(fileChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		btnBrowse.setBounds(291, 9, 80, 27);
		btnBrowse.setText("Browse");

		Label lblQsFile = new Label(shlEfileparser, SWT.NONE);
		lblQsFile.setBounds(10, 14, 49, 17);
		lblQsFile.setText("QS File");

		Button btnQuery = new Button(shlEfileparser, SWT.NONE);
		btnQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doQuery();
			}
		});
		btnQuery.setBounds(377, 40, 80, 27);
		btnQuery.setText("Query");

		Label lblDataType = new Label(shlEfileparser, SWT.NONE);
		lblDataType.setBounds(10, 43, 61, 17);
		lblDataType.setText("Data Type");

		Label lblKey = new Label(shlEfileparser, SWT.NONE);
		lblKey.setBounds(170, 43, 30, 17);
		lblKey.setText("Key");

		textKeyValue = new Text(shlEfileparser, SWT.BORDER);
		textKeyValue.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					doQuery();
				}
			}
		});
		textKeyValue.setBounds(291, 42, 80, 23);

	}

	private void doQuery() {
		QueryResult rds;
		if (textKeyValue.getText().equals(""))
			rds = pr.getEdata().queryAll(combTags.getText(),
					combKeys.getText(), null);
		else
			rds = pr.getEdata().queryAll(combTags.getText(),
					combKeys.getText(), textKeyValue.getText());
		if (rds.size() <= 0)
			textLog.setText("No record.");
		else {
			textLog.setText(rds.toString(pr.getEdata().get_eng2cn(
					combTags.getText())));
		}
	}
}
