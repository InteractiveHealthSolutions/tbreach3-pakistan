/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

package com.ihsinformatics.tbr3.treatmentregister;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import com.ihsinformatics.tbr3.treatmentregister.util.SwingUtil;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class TreatmentRegisterMain extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4067091232845466212L;
	private JPanel contentPane;

	private JLabel lblUsername;
	private JLabel lblPassword;

	private JTextField usernameTextField;
	private JPasswordField passwordField;

	private JButton loginButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TreatmentRegisterMain frame = new TreatmentRegisterMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TreatmentRegisterMain() {
		setTitle("TB Treatment Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 11, 80, 14);
		contentPane.add(lblUsername);

		usernameTextField = new JTextField();
		usernameTextField.setToolTipText("Enter your OpenMRS username");
		usernameTextField.setBounds(100, 4, 170, 28);
		contentPane.add(usernameTextField);
		usernameTextField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setToolTipText("Enter password for your OpenMRS account");
		passwordField.setBounds(100, 37, 170, 28);
		contentPane.add(passwordField);

		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 44, 80, 14);
		contentPane.add(lblPassword);

		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		loginButton.setBounds(100, 77, 170, 28);
		contentPane.add(loginButton);
	}

	public boolean validateForm() {
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		String username = SwingUtil.get(usernameTextField);
		String password = SwingUtil.get(passwordField);
		if (username.equals("") || password.equals("")) {
			message.append("Username and Password must be provided.\n");
			valid = false;
		}
		if (!valid) {
			JOptionPane.showMessageDialog(new JFrame(), message.toString(),
					"Empty fields!", JOptionPane.ERROR_MESSAGE);
		}
		return valid;
	}
	
	public void login() {
		// Authenticate
		
		this.dispose();
		
		// New form
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			if(validateForm()) {
				login();
			}
		}
	}
}
