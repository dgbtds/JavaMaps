package Config;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JEditorPane;

public class DAQ_MAPS {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DAQ_MAPS window = new DAQ_MAPS();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DAQ_MAPS() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 790, 766);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(67, 74, 121, 40);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("IP");
		lblNewLabel.setBounds(23, 73, 70, 40);
		frame.getContentPane().add(lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(200, 74, 121, 40);
		frame.getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(340, 74, 121, 40);
		frame.getContentPane().add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(473, 74, 121, 40);
		frame.getContentPane().add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(614, 74, 121, 40);
		frame.getContentPane().add(textField_4);
		
		JLabel lblTs = new JLabel("TS");
		lblTs.setBounds(27, 137, 70, 40);
		frame.getContentPane().add(lblTs);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(67, 137, 121, 40);
		frame.getContentPane().add(textField_5);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(200, 137, 121, 40);
		frame.getContentPane().add(textField_6);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(340, 137, 121, 40);
		frame.getContentPane().add(textField_7);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(473, 137, 121, 40);
		frame.getContentPane().add(textField_8);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(614, 137, 121, 40);
		frame.getContentPane().add(textField_9);
		
		JLabel lblSi = new JLabel("SI");
		lblSi.setBounds(23, 196, 70, 40);
		frame.getContentPane().add(lblSi);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(63, 196, 121, 40);
		frame.getContentPane().add(textField_10);
		
		textField_11 = new JTextField();
		textField_11.setColumns(10);
		textField_11.setBounds(196, 196, 121, 40);
		frame.getContentPane().add(textField_11);
		
		textField_12 = new JTextField();
		textField_12.setColumns(10);
		textField_12.setBounds(336, 196, 121, 40);
		frame.getContentPane().add(textField_12);
		
		textField_13 = new JTextField();
		textField_13.setColumns(10);
		textField_13.setBounds(469, 196, 121, 40);
		frame.getContentPane().add(textField_13);
		
		textField_14 = new JTextField();
		textField_14.setColumns(10);
		textField_14.setBounds(610, 196, 121, 40);
		frame.getContentPane().add(textField_14);
		
		JLabel label_3 = new JLabel("Status");
		label_3.setBounds(645, 12, 90, 43);
		frame.getContentPane().add(label_3);
		
		JLabel label = new JLabel("Status");
		label.setBounds(500, 12, 90, 43);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("Status");
		label_1.setBounds(361, 12, 90, 43);
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("Status");
		label_2.setBounds(227, 12, 90, 43);
		frame.getContentPane().add(label_2);
		
		JLabel label_4 = new JLabel("Status");
		label_4.setBounds(87, 12, 90, 43);
		frame.getContentPane().add(label_4);
		
		JButton button = new JButton("Run");
		button.setBounds(216, 248, 90, 43);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("Run");
		button_1.setBounds(361, 248, 90, 43);
		frame.getContentPane().add(button_1);
		
		JButton button_2 = new JButton("Run");
		button_2.setBounds(479, 248, 90, 43);
		frame.getContentPane().add(button_2);
		
		JButton button_3 = new JButton("Run");
		button_3.setBounds(620, 248, 90, 43);
		frame.getContentPane().add(button_3);
		
		JButton button_4 = new JButton("Run");
		button_4.setBounds(73, 248, 90, 43);
		frame.getContentPane().add(button_4);
		
		JButton btnNewButton = new JButton("Reset");
		btnNewButton.setBounds(29, 337, 99, 51);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(29, 416, 99, 51);
		frame.getContentPane().add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(29, 493, 99, 51);
		frame.getContentPane().add(btnStop);
	}
}
