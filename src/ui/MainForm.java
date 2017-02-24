package ui;

import java.io.File;

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
		shlEfileparser.setSize(new Point(470, 447));
		shlEfileparser.setVisible(true);
		shlEfileparser.setText("efileParser");

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
					textLog.setText(String.format("Parsed:%d tag(s).", nps));
					Edata edt = pr.getEdata();
					for (String key : edt.keySet()) {
						combTags.add(key);
					}
				}
			}
		});
		btnParse.setBounds(377, 9, 80, 27);
		btnParse.setText("Parse");

		textPath = new Text(shlEfileparser, SWT.BORDER);
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
		QueryResult rds = pr.getEdata().queryAll(combTags.getText(),
				combKeys.getText(), textKeyValue.getText());
		if (rds.size() <= 0)
			textLog.setText("No record.");
		else {
			textLog.setText(rds.toString(pr.getEdata().get_eng2cn(
					combTags.getText())));
		}
	}
}
