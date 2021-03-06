/*
 * TCSS 360 Software Development
 * Auction Central Project
 * Group 6 
 */

package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.Bidder;
import model.Calendar;
import model.NonProfit;
import model.Staff;
import model.User;


/**
 * Used to build the login JPanel.
 * 
 * @author Colin Casey
 */
public class Login extends JPanel {

	/**
	 * Used to keep track of data
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Used to tell users that they need to login.
	 */
	private JPanel myTextPanel;
	
	/**
	 * Used to hold all Buttons.
	 */
	private JPanel myButtons;

	/**
	 * Frame that all panels are put in.
	 */
	private final JFrame myFrame;

	/**
	 * Used to give button access to whole class.
	 */
	private JButton myLogin;

	/**
	 * Used to give button access to whole class.
	 */
	private JButton myExit;
	
	
	/**
	 * Is a list of all of the users.
	 */
	private List<User> myUsers;
	
	/**
	 * Is used to gain access  to user passwords.
	 */
	private HashMap<String, String> myUsersLogins;
	
	/**
	 * Gives access to calendar object to the rest of the class.
	 */
	private Calendar myCalendar;
	
	/**
	 * Used to give access of login user to other classes.
	 */
	private User myUser;

	/**
	 * Used to make panel.
	 * @param theFrame
	 */
	public Login(JFrame theFrame, List<User> theUsers, HashMap<String, String> theLogins) {
		myUser = null;
		myUsers = theUsers;
		myUsersLogins = theLogins;
		myFrame = theFrame;
		makeButtonLogin();
		makeButtonExit();
		makeTextPanel();
		makeButtonPanel();
		setLayout(new BorderLayout());
		add(myTextPanel, BorderLayout.NORTH);
		add(myButtons, BorderLayout.SOUTH);
	}

	/**
	 * Used to make a button panel all buttons are stored
	 * in.
	 */
	private void makeButtonPanel() {
		myButtons = new JPanel();
		myButtons.add(myExit);
		myButtons.add(myLogin);
	}

	/**
	 * Used to make a login message.
	 */
	private void makeTextPanel() {
		myTextPanel = new JPanel();
		JLabel Jlabel = new JLabel("Welcome to Auction Central");
		Jlabel.setFont(new Font("Sans Serif", Font.BOLD, 18));
		Jlabel.setHorizontalAlignment(SwingConstants.LEFT);
		myTextPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		myTextPanel.add(Jlabel);
	}

	/**
	 * Used to make exit button.
	 */
	private void makeButtonExit() {
		myExit = new JButton("Exit");
		myExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				System.exit(1);
			}
		});
	}

	/**
	 * Used to make login button.
	 */
	private void makeButtonLogin() {
		myLogin = new JButton("Login");
		myLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				//Make JPanels
				JPanel holder = new JPanel(new BorderLayout(10, 10));
				JPanel question = new JPanel(new GridLayout(0, 1, 2, 2));
				//Fill questions
				question.add(new JLabel("Username", SwingConstants.RIGHT));
				question.add(new JLabel("Password", SwingConstants.RIGHT));
				//Fill Holder
				holder.add(question, BorderLayout.WEST);
				//Make panel/textField
				JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
				JTextField username = new JTextField();
				//Fill fields
				controls.add(username);
				JPasswordField password = new JPasswordField();
				controls.add(password);
				holder.add(controls, BorderLayout.CENTER);
				//The pop up
				JOptionPane.showMessageDialog(myFrame, holder, "Login To Auction Central", 
						JOptionPane.QUESTION_MESSAGE);
				
				//Could not get cancel to work.
				//JOptionPane.showMessageDialog(myFrame, holder, "Login", JOptionPane.OK_CANCEL_OPTION);
				final String currentUser = username.getText();
				final String currentPassword = new String(password.getPassword());
				
				User user = login(currentUser, currentPassword);	
				
				
				if(user != null) {
					popupPass(user);
				} else {
					popupFail(true, true);
				}
				//System.out.println(currentUser);
				//System.out.println(currentPassword);
			}
		});
	}
	
	
	/**
	 * Checks if user is able to login.
	 * 
	 * @param username person username
	 * @param password person password
	 * @return if it happen
	 */
	private User login(String username, String password) {
		
		User theUser = null;
		
			if (password.equals(myUsersLogins.get(username))) {
				
				// return the type of user
				for (User user: myUsers) {					
					if (user.getUserName().equals(username)) {						
						theUser = user;
					}	
				}
			}
		setUser(theUser);
		return theUser;		
		
		
	}
	
	/**
	 * Used to tell user if the failed or passed
	 */
	private void popupPass(User theLoggedInUser) {
		if(theLoggedInUser instanceof Staff) {
			firePropertyChange("Staff", "Staff", "Login");
		}
		if(theLoggedInUser instanceof Bidder) {
			//firePropertyChange("NonProfit", "Bidder", "Login");
			firePropertyChange("Bidder", "Bidder", "Login");
		}
		if(theLoggedInUser instanceof NonProfit) {
			firePropertyChange("NonProfit", "Non Profit", "Login");
		}
		
		
		JOptionPane.showMessageDialog(myFrame, "You are now logged in");
	}
	
	/**
	 * Used to tell user why they failed.
	 * @param thePassword if password is wrong
	 * @param theUsername if username is wrong
	 */
	private void popupFail(boolean thePassword, boolean theUsername) {
		String outPut = "Your";
		if (thePassword) {
			outPut += " password";
		} if (theUsername && thePassword) {
			outPut += " and";
		} if (theUsername) {
			outPut += " username";
		}
		outPut += " failed.";
		JOptionPane.showMessageDialog(myFrame, outPut, "Login Error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Used to set login user.
	 * 
	 * @param theUser who is current login
	 */
	private void setUser(User theUser) {
		myUser = theUser;
	}
	
	/**
	 * Used to get login user.
	 * 
	 * @return who is currently login 
	 */
	public User getUser() {
		return myUser;
		
	}
}
