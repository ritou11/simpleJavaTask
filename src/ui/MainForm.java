package ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import utils.Parser;
import org.eclipse.swt.graphics.Point;

public class MainForm {

	protected Shell shlEfileparser;
	private Text text;
	private Text text_1;
	private Text text_2;

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
		
		Combo combo = new Combo(shlEfileparser, SWT.NONE);
		combo.setBounds(76, 40, 88, 25);
		
		Combo combo_1 = new Combo(shlEfileparser, SWT.NONE);
		combo_1.setBounds(197, 40, 88, 25);
		
		Button btnParse = new Button(shlEfileparser, SWT.NONE);
		btnParse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Parser pr = new Parser("F:\\Users\\ThinkPad\\workspace\\EFileParser\\example.QS","F:\\Users\\ThinkPad\\workspace\\EFileParser\\toParse.config");
				System.out.println(pr.LoadConfig());
				System.out.println(pr.ParseFile());
			}
		});
		btnParse.setBounds(377, 9, 80, 27);
		btnParse.setText("Parse");
		
		text = new Text(shlEfileparser, SWT.BORDER);
		text.setBounds(66, 11, 219, 23);
		
		text_1 = new Text(shlEfileparser, SWT.BORDER | SWT.V_SCROLL);
		text_1.setBounds(10, 71, 447, 180);
		
		Button btnBrowse = new Button(shlEfileparser, SWT.NONE);
		btnBrowse.setBounds(291, 9, 80, 27);
		btnBrowse.setText("Browse");
		
		Label lblQsFile = new Label(shlEfileparser, SWT.NONE);
		lblQsFile.setBounds(10, 14, 49, 17);
		lblQsFile.setText("QS File");
		
		Button btnQuery = new Button(shlEfileparser, SWT.NONE);
		btnQuery.setBounds(377, 40, 80, 27);
		btnQuery.setText("Query");
		
		Label lblDataType = new Label(shlEfileparser, SWT.NONE);
		lblDataType.setBounds(10, 43, 61, 17);
		lblDataType.setText("Data Type");
		
		Label lblKey = new Label(shlEfileparser, SWT.NONE);
		lblKey.setBounds(170, 43, 30, 17);
		lblKey.setText("Key");
		
		text_2 = new Text(shlEfileparser, SWT.BORDER);
		text_2.setBounds(291, 42, 80, 23);

	}
}
