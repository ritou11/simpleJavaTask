package ui;

import java.io.File;
import java.util.List;

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

import utils.Parser;

import org.eclipse.swt.graphics.Point;

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
		if(pr.LoadConfig("F:\\Users\\ThinkPad\\workspace\\EFileParser\\toParse.config")< 0){
			System.exit(0);
		}
		List<String> tpr = pr.getToParse();
		for(int i=0;i<tpr.size();i++){
			combTags.add(tpr.get(i));
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
		shlEfileparser = new Shell();
		shlEfileparser.setSize(new Point(483, 300));
		shlEfileparser.setVisible(true);
		shlEfileparser.setText("efileParser");
		
		combTags = new Combo(shlEfileparser, SWT.NONE);
		combTags.setBounds(76, 40, 88, 25);
		
		combKeys = new Combo(shlEfileparser, SWT.NONE);
		combKeys.setBounds(197, 40, 88, 25);
		
		Button btnParse = new Button(shlEfileparser, SWT.NONE);
		btnParse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(pr.ParseFile(textPath.getText()));
			}
		});
		btnParse.setBounds(377, 9, 80, 27);
		btnParse.setText("Parse");
		
		textPath = new Text(shlEfileparser, SWT.BORDER);
		textPath.setBounds(66, 11, 219, 23);
		
		textLog = new Text(shlEfileparser, SWT.BORDER | SWT.V_SCROLL);
		textLog.setBounds(10, 71, 447, 180);
		
		Button btnBrowse = new Button(shlEfileparser, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileFilter filter = new FileNameExtensionFilter("QS File", "QS");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileFilter(filter);
				int rtv = fileChooser.showOpenDialog(null);
				if(rtv == JFileChooser.APPROVE_OPTION){
					textPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
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
				textLog.setText(pr.getEdata().query(combTags.getText(), textKeyValue.getText()).toString());
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
		textKeyValue.setBounds(291, 42, 80, 23);

	}
}
