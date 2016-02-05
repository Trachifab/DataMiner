package fr.eni.dataminer.ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JComboBox;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import fr.eni.dataminer.exceptions.MalFormedProxyException;
import fr.eni.dataminer.model.DataType;
import fr.eni.dataminer.model.Entity;
import fr.eni.dataminer.model.EntityParser;
import fr.eni.dataminer.service.EntityService;
import fr.eni.dataminer.service.ExportService;
import fr.eni.dataminer.service.URLAnalyserService;
import fr.eni.dataminer.service.URLMinerService;
import fr.eni.dataminer.service.tools.LoggingEngine;

import javax.swing.JSpinner;
import javax.swing.JCheckBox;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textField_rootURL;
	private JTextField textField_textToFind;

	private JLabel lblRootUrl;
	private JLabel lblDataType;
	private JLabel lblMaxUrls;
	private JLabel lblTextToFind;
	private JLabel lblStatus;
	private JLabel lblPort;
	private JLabel lblIpProxy;

	private JSpinner spinner_maxURL;

	private JComboBox comboBox_dataType;
	
	private JCheckBox chckbxEnableProxy;

	private static final LoggingEngine loggerEngine = LoggingEngine
			.getInstance();
	
	private URLMinerService ums = URLMinerService.getInstance();
	private URLAnalyserService uas = URLAnalyserService.getInstance();
	private JTextField textField_proxyPort;
	private JTextField textField_ipProxy;

	/**
	 * Create the main frame.
	 */
	public MainWindow() {
		loggerEngine.logger.log(Level.INFO, "Initialisation of the main window");
		setTitle("URL Data Miner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 548, 362);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		generateTextFields();
		generateLabels();
		generateButtons();

		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 274, 520, 16);
		contentPane.add(progressBar);

	}

	/**
	 * 
	 */
	private void generateTextFields() {
		
		loggerEngine.logger.log(Level.INFO, "Generating TextFields");
		
		textField_ipProxy = new JTextField();
		textField_ipProxy.setEnabled(false);
		textField_ipProxy.setBounds(106, 142, 124, 20);
		contentPane.add(textField_ipProxy);
		textField_ipProxy.setColumns(10);
		
		textField_proxyPort = new JTextField();
		textField_proxyPort.setEnabled(false);
		textField_proxyPort.setBounds(290, 142, 86, 20);
		contentPane.add(textField_proxyPort);
		textField_proxyPort.setColumns(10);

		textField_rootURL = new JTextField();
		textField_rootURL.setToolTipText("Enter the root URL");
		textField_rootURL.setBounds(106, 27, 424, 20);
		contentPane.add(textField_rootURL);
		textField_rootURL.setColumns(10);

		textField_textToFind = new JTextField();
		textField_textToFind.setToolTipText("Select the text to find");
		textField_textToFind.setBounds(329, 103, 201, 20);
		contentPane.add(textField_textToFind);
		textField_textToFind.setColumns(10);
		textField_textToFind.setVisible(false);
	}

	/**
	 * 
	 */
	private void generateLabels() {
		
		loggerEngine.logger.log(Level.INFO, "Generating labels");
		
		lblPort = new JLabel("Port :");
		lblPort.setEnabled(false);
		lblPort.setBounds(247, 145, 33, 14);
		contentPane.add(lblPort);

		lblRootUrl = new JLabel("Root URL :");
		lblRootUrl.setBounds(10, 30, 87, 14);
		contentPane.add(lblRootUrl);

		lblDataType = new JLabel("Data type :");
		lblDataType.setBounds(10, 106, 71, 14);
		contentPane.add(lblDataType);

		lblMaxUrls = new JLabel("Max URLs :");
		lblMaxUrls.setBounds(10, 70, 75, 14);
		contentPane.add(lblMaxUrls);

		lblTextToFind = new JLabel("Text to find :");
		lblTextToFind.setBounds(247, 106, 72, 14);
		contentPane.add(lblTextToFind);
		lblTextToFind.setVisible(false);

		lblStatus = new JLabel("Status");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(10, 249, 520, 14);
		contentPane.add(lblStatus);
		
		lblIpProxy = new JLabel("IP proxy : ");
		lblIpProxy.setEnabled(false);
		lblIpProxy.setBounds(10, 145, 71, 14);
		contentPane.add(lblIpProxy);
	}

	/**
	 * 
	 */
	private void generateButtons() {

		loggerEngine.logger.log(Level.INFO, "Generating buttons");
		
		comboBox_dataType = new JComboBox();
		comboBox_dataType.setBounds(106, 102, 107, 22);
		contentPane.add(comboBox_dataType);
		DefaultComboBoxModel dcm = new DefaultComboBoxModel(DataType.values());
		comboBox_dataType.setModel(dcm);
		comboBox_dataType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_dataType.getModel().getSelectedItem()
						.equals(DataType.TEXT)) {
					textField_textToFind.setText("");
					lblTextToFind.setVisible(true);
					textField_textToFind.setVisible(true);
				} else {
					textField_textToFind.setText("");
					lblTextToFind.setVisible(false);
					textField_textToFind.setVisible(false);
				}
			}
		});

		JButton btnNewButton = new JButton("Launch datamining");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launchMining();
			}
		});
		btnNewButton.setBounds(10, 301, 520, 23);
		contentPane.add(btnNewButton);

		spinner_maxURL = new JSpinner();
		spinner_maxURL.setBounds(106, 68, 107, 18);
		contentPane.add(spinner_maxURL);
		
		chckbxEnableProxy = new JCheckBox("Enable proxy");
		chckbxEnableProxy.setBounds(406, 141, 124, 23);
		contentPane.add(chckbxEnableProxy);
		chckbxEnableProxy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean checked = chckbxEnableProxy.isSelected();
				if(checked){
					textField_proxyPort.setEnabled(true);
					textField_ipProxy.setEnabled(true);
					lblIpProxy.setEnabled(true);
					lblPort.setEnabled(true);
				}
				else{
					textField_proxyPort.setEnabled(false);
					textField_ipProxy.setEnabled(false);
					lblIpProxy.setEnabled(false);
					lblPort.setEnabled(false);
				}
			}
		});

	}

	/**
	 * Launch the datamining from url
	 */
	private void launchMining() {

		loggerEngine.logger.log(Level.INFO, "Launching dataminig");
		String url = this.textField_rootURL.getText();
		int max_url = (int) this.spinner_maxURL.getValue();
		EntityParser parser = new EntityParser(
				(DataType) this.comboBox_dataType.getModel().getSelectedItem());
		if (this.comboBox_dataType.getModel().getSelectedItem()
				.equals(DataType.TEXT)) {
			parser.setSearchString(textField_textToFind.getText());
		}
		if(chckbxEnableProxy.isSelected()){
			String ip = textField_ipProxy.getText();
			String port = textField_proxyPort.getText();
			try {
				uas.setProxy(ip, port);
				URLMinerService ums = new URLMinerService();
				ums.setMAX_PAGES_TO_SEARCH(max_url);
				uas.setProxy(ip, port);
				List<Entity> resultList = ums.search(url, parser);
				EntityService.getInstance().setEntityList(resultList);
				try {
					ExportService.exportCSV();
				} catch (IOException e) {
					loggerEngine.logger.log(Level.SEVERE, "Error while exporting results to CSV");
					e.printStackTrace();
				}
			} catch (MalFormedProxyException e) {
				loggerEngine.logger.log(Level.INFO, "Malformed Proxy infos : " +e.getMessage());
				JOptionPane.showMessageDialog(this, "Your proxy is invalid, please verify it.");
			}
		}
	}
}
