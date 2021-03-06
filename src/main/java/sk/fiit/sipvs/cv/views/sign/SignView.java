package sk.fiit.sipvs.cv.views.sign;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.fiit.sipvs.cv.controllers.MainClass;
import sk.fiit.sipvs.cv.controllers.SignController;
import sk.fiit.sipvs.cv.controllers.TransformController;

public class SignView {

	private static final String MISSING_FILE_USER = "file has not been chosen. Please choose the missing file and try again.";
	private static final String MISSING_XML_FILE_LOGGER = "Transformation error. Missing XML file.";
	private static final String MISSING_XSD_FILE_LOGGER = "Transformation error. Missing XSD file.";
	private static final String MISSING_XSL_FILE_LOGGER = "Transformation error. Missing XSL file.";
	private static final String CANNOT_SAVE_FILE = "File cannot be saved. Please try again.";

	private JFrame window;

	private JLabel xmlFilePathLabel;
	private JLabel xsdFilePathLabel;
	private JLabel xslFilePathLabel;
	private JLabel documentSignedLabel;

	private final JFileChooser xmlFileChooser;
	private final JFileChooser xsdFileChooser;
	private final JFileChooser xslFileChooser;

	private File xmlFile;
	private File xsdFile;
	private File xslFile;
	private File lastDirectory = new File(System.getProperty("user.dir"));

	private JTextArea textArea;

	private JButton signBtn;
	private JButton signBtnTS;

	private Logger logger = LogManager.getLogger(SignView.class.getName());

	public SignView() {
		// Window
		window = new JFrame();
		window.setTitle("Sign XML");
		window.setBounds(100, 100, 1000, 450);
		window.getContentPane().setLayout(null);

		// File Chooser with Filters
		FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files (.xml)", "xml");
		FileNameExtensionFilter xsdFilter = new FileNameExtensionFilter("XML Schema files (.xsd)", "xsd");
		FileNameExtensionFilter xslFilter = new FileNameExtensionFilter("XSLT Stylesheet (.xsl)", "xsl");

		xmlFileChooser = new JFileChooser();
		xmlFileChooser.setAcceptAllFileFilterUsed(false);
		xmlFileChooser.setFileFilter(xmlFilter);

		xsdFileChooser = new JFileChooser();
		xsdFileChooser.setAcceptAllFileFilterUsed(false);
		xsdFileChooser.setFileFilter(xsdFilter);

		xslFileChooser = new JFileChooser();
		xslFileChooser.setAcceptAllFileFilterUsed(false);
		xslFileChooser.setFileFilter(xslFilter);

		// XML File
		final JLabel xmlFileLabel = MainClass.createBoldLabel("XML File");
		xmlFileLabel.setBounds(32, 20, 150, 24);
		window.getContentPane().add(xmlFileLabel);

		JButton xmlFileBtn = new JButton("Choose file");
		xmlFileBtn.setBounds(32, 20 + 24 + MainClass.VERTICAL_SPACING, 100, 24);
		window.getContentPane().add(xmlFileBtn);
		xmlFileBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				disableSignButtons(true);

				// Set current directory to last opened directory
				xmlFileChooser.setCurrentDirectory(lastDirectory);

				int returnVal = xmlFileChooser.showOpenDialog(window);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					xmlFile = xmlFileChooser.getSelectedFile();

					xmlFilePathLabel.setText(xmlFile.getName());

					lastDirectory = xmlFile;
				} else {
					xmlFilePathLabel.setText(MainClass.NO_FILE_CHOSEN);
					xmlFile = null;
				}
			}
		});

		xmlFilePathLabel = new JLabel(MainClass.NO_FILE_CHOSEN);
		xmlFilePathLabel.setBounds(32 + 100 + MainClass.HORIZONTAL_SPACING, 20 + 24 + MainClass.VERTICAL_SPACING, 200,
				24);
		window.getContentPane().add(xmlFilePathLabel);

		// XSD File
		final JLabel xsdFileLabel = MainClass.createBoldLabel("XML Schema File");
		xsdFileLabel.setBounds(32, 20 + 2 * 24 + 4 * MainClass.VERTICAL_SPACING, 150, 24);
		window.getContentPane().add(xsdFileLabel);

		JButton xsdFileBtn = new JButton("Choose file");
		xsdFileBtn.setBounds(32, 20 + 3 * 24 + 5 * MainClass.VERTICAL_SPACING, 100, 24);
		window.getContentPane().add(xsdFileBtn);
		xsdFileBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				disableSignButtons(true);

				// Set current directory to last opened directory
				xsdFileChooser.setCurrentDirectory(lastDirectory);

				int returnVal = xsdFileChooser.showOpenDialog(window);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					xsdFile = xsdFileChooser.getSelectedFile();

					xsdFilePathLabel.setText(xsdFile.getName());

					lastDirectory = xsdFile;
				} else {
					xsdFilePathLabel.setText(MainClass.NO_FILE_CHOSEN);
					xsdFile = null;
				}
			}
		});

		xsdFilePathLabel = new JLabel(MainClass.NO_FILE_CHOSEN);
		xsdFilePathLabel.setBounds(32 + 100 + MainClass.HORIZONTAL_SPACING,
				20 + 3 * 24 + 5 * MainClass.VERTICAL_SPACING, 200, 24);
		window.getContentPane().add(xsdFilePathLabel);

		// XSL File
		final JLabel xslFileLabel = MainClass.createBoldLabel("XSLT Stylesheet File");
		xslFileLabel.setBounds(32, 20 + 4 * 24 + 8 * MainClass.VERTICAL_SPACING, 150, 24);
		window.getContentPane().add(xslFileLabel);

		JButton xslFileBtn = new JButton("Choose file");
		xslFileBtn.setBounds(32, 20 + 5 * 24 + 9 * MainClass.VERTICAL_SPACING, 100, 24);
		window.getContentPane().add(xslFileBtn);
		xslFileBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				disableSignButtons(true);

				// Set current directory to last opened directory
				xslFileChooser.setCurrentDirectory(lastDirectory);

				int returnVal = xslFileChooser.showOpenDialog(window);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					xslFile = xslFileChooser.getSelectedFile();

					xslFilePathLabel.setText(xslFile.getName());

					lastDirectory = xslFile;
				} else {
					xslFilePathLabel.setText(MainClass.NO_FILE_CHOSEN);
					xslFile = null;
				}
			}
		});

		xslFilePathLabel = new JLabel(MainClass.NO_FILE_CHOSEN);
		xslFilePathLabel.setBounds(32 + 100 + MainClass.HORIZONTAL_SPACING,
				20 + 5 * 24 + 9 * MainClass.VERTICAL_SPACING, 200, 24);
		window.getContentPane().add(xslFilePathLabel);

		// Show document
		final JLabel showLabel = MainClass.createBoldLabel("Tools");
		showLabel.setBounds(32, 20 + 6 * 24 + 12 * MainClass.VERTICAL_SPACING, 150, 24);
		window.getContentPane().add(showLabel);

		JButton showBtn = new JButton("Prepare document");
		showBtn.setBounds(32, 20 + 7 * 24 + 13 * MainClass.VERTICAL_SPACING, 150, 24);
		window.getContentPane().add(showBtn);
		showBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disableSignButtons(true);
				setDocumentLabel(false);

				if (xmlFile != null && xsdFile != null && xslFile != null) {

					logger.info("Transformation started...");
					TransformController tc = new TransformController();

					try {
						textArea.setText(tc.transform(xmlFile, xslFile, null));
						disableSignButtons(false);
						logger.info("Transformation finished.");
					} catch (TransformerException e1) {
						logger.error("An error occured during transformation", e1);
						JOptionPane.showMessageDialog(window, "An error occured during transformation.",
								"Transformation error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (xmlFile == null) {
					logger.error(MISSING_XML_FILE_LOGGER);
					JOptionPane.showMessageDialog(window, "XML " + MISSING_FILE_USER, MainClass.MISSING_FILE,
							JOptionPane.ERROR_MESSAGE);
				} else if (xsdFile == null) {
					logger.error(MISSING_XSD_FILE_LOGGER);
					JOptionPane.showMessageDialog(window, "XSD " + MISSING_FILE_USER, MainClass.MISSING_FILE,
							JOptionPane.ERROR_MESSAGE);
				} else {
					logger.error(MISSING_XSL_FILE_LOGGER);
					JOptionPane.showMessageDialog(window, "XSL " + MISSING_FILE_USER, MainClass.MISSING_FILE,
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Sign XML
		signBtn = new JButton("Sign");
		signBtn.setBounds(980 - 32 - 2 * 100 - 150 - 2 * MainClass.HORIZONTAL_SPACING, 410 - 20 - 24, 100, 24);
		signBtn.setEnabled(false);
		window.getContentPane().add(signBtn);
		signBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SignController sc = new SignController();
				String xmlOutput = sc.signDocument(xmlFile, xsdFile, xslFile);

				// Save file
				final FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files (.xml)", "xml");

				final JFileChooser fc = new JFileChooser();
				fc.setAcceptAllFileFilterUsed(false);
				fc.setFileFilter(xmlFilter);
				int returnVal = fc.showSaveDialog(window);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filename = fc.getSelectedFile().toString();

					if (sc.saveDocument(xmlOutput, filename)) {
						setDocumentLabel(true);
						disableSignButtons(true);
					} else {
						JOptionPane.showMessageDialog(window, CANNOT_SAVE_FILE, MainClass.SAVE_ERROR,
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		// Sign XML + add Timestamp
		signBtnTS = new JButton("Sign with Timestamp");
		signBtnTS.setBounds(980 - 32 - 100 - 150 - MainClass.HORIZONTAL_SPACING, 410 - 20 - 24, 150, 24);
		signBtnTS.setEnabled(false);
		window.getContentPane().add(signBtnTS);
		signBtnTS.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SignController sc = new SignController();
				String xmlOutput = sc.signDocumentWithTimeStamp(xmlFile, xsdFile, xslFile);

				// Save file
				final FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files (.xml)", "xml");

				final JFileChooser fc = new JFileChooser();
				fc.setAcceptAllFileFilterUsed(false);
				fc.setFileFilter(xmlFilter);
				int returnVal = fc.showSaveDialog(window);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filename = fc.getSelectedFile().toString();

					if (sc.saveDocument(xmlOutput, filename)) {
						setDocumentLabel(true);
						disableSignButtons(true);
					} else {
						JOptionPane.showMessageDialog(window, CANNOT_SAVE_FILE, MainClass.SAVE_ERROR,
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		// Cancel
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setBounds(980 - 32 - 100 + 5, 410 - 20 - 24, 100, 24);
		window.getContentPane().add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
			}
		});

		// Output text area
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32 + 100 + 200 + 4 * MainClass.HORIZONTAL_SPACING, 20, 980 - 400, 380 - 55);
		window.getContentPane().add(scrollPane);

		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setFont(new Font("Arial", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);

		// Document sign status
		Image iconImg = ((ImageIcon) UIManager.getIcon("OptionPane.informationIcon")).getImage();
		Icon infoIcon = new ImageIcon(iconImg.getScaledInstance(24, 24, Image.SCALE_AREA_AVERAGING));

		documentSignedLabel = new JLabel(infoIcon);
		documentSignedLabel.setBounds(32 + 100 + 200 + 4 * MainClass.HORIZONTAL_SPACING, 410 - 20 - 24, 200, 24);
		documentSignedLabel.setHorizontalAlignment(SwingConstants.LEFT);
		setDocumentLabel(false);
		window.getContentPane().add(documentSignedLabel);
	}

	public void show() {
		window.setVisible(true);
		window.repaint();
	}

	private void setDocumentLabel(Boolean isSigned) {
		if (isSigned) {
			documentSignedLabel.setText("Document is signed.");
			documentSignedLabel.setForeground(new Color(27, 128, 29));
		} else {
			documentSignedLabel.setText("Document is not signed.");
			documentSignedLabel.setForeground(Color.RED);
		}
	}

	private void disableSignButtons(Boolean disable) {
		if (disable) {
			signBtn.setEnabled(false);
			signBtnTS.setEnabled(false);
		} else {
			signBtn.setEnabled(true);
			signBtnTS.setEnabled(true);
		}
	}
}
