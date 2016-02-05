package fr.eni.dataminer.app;

import java.awt.EventQueue;
import fr.eni.dataminer.ihm.MainWindow;

public class AppLauncher {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
