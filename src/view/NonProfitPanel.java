/*
 * TCSS 360 Software Development
 * Auction Central Project
 * Group 6 
 */

package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Auction;
import model.Calendar;
import model.Date;
import model.NonProfit;

/**
 * Used to build the non-profit JPanel.
 * 
 * @author Colin Casey
 */
public class NonProfitPanel extends JPanel implements Observer, PropertyChangeListener{
	
	/**
	 * Used to save data.
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * Used to set min size of window.
     */
    private static final Dimension MY_SIZE = new Dimension(650, 600);

	/**
	 * Used to hold all Buttons.
	 */
	private JPanel myButtons;
	
	/**
	 * Used to give button access to all of the class.
	 */
	private JButton myCancelAuction;
	
	/**
	 * Used to give button access to all of the class.
	 */
	private JButton myAddAuction;
	
	/**
	 * Used to give button access to all of the class.
	 */
	private JButton myRemoveItem;
	
	/**
	 * Used to give button access to all of the class.
	 */
	private JButton myAddItem;
	
	/**
	 * Used to give button access to whole class.
	 */
	private JButton myLogout;
	
	/**
	 * The calendar for being currently used. 
	 */
	private Calendar myCalendar; 
	
	private JFrame myFrame;
	
	private LocalDate myLocalDate;	
	private Date myCurrDate;
	
	
//	/**
//	 * Label used to display the the current non profit information.
//	 */
//	private JPanel myAuctionInfo;
	
	private NonProfitInfoPanel myAuctionInfo;

	/**
	 * The current non profit user.
	 */
	private NonProfit myCurrNonProfit;
	
	private ItemsPanel myInventory;
	
	private int myItemNumber;
	
	private Auction myAuction;


	
	/**
	 * Used to build the JPanel.
	 * 
	 * @param theFrame the frame everything is loaded into
	 */
	public NonProfitPanel(final JFrame theFrame, final Calendar theCalendar) {
		myFrame = theFrame;
		setLayout(new BorderLayout());
		myButtons = new JPanel();
		myCalendar = theCalendar;
		myItemNumber = -1;
		
		myLocalDate = LocalDate.now();		
		myCurrDate = new Date(myLocalDate.getDayOfMonth(), myLocalDate.getMonth().toString(), myLocalDate.getYear());
		
		
		
		//Makes all buttons
		
		makeButtonCancelAuction();
		makeButtonAddAuction();
		makeButtonRemoveItem();
		makeButtonAddItem();
		makeButtonLogout();
		makeAuctionInfoLabel();
		
		//Adds all buttons to button of JPanel
		add(myButtons, BorderLayout.PAGE_END);
		
	}

	/**
	 * This method is used to cancel an auction request.
	 */
	private void makeButtonCancelAuction() {
		myCancelAuction = new JButton("Cancel Auction Request");			
	
		myCancelAuction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				
				
			}
		});		
		myButtons.add(myCancelAuction);
	}
	
	/**
	 * This method is used to add an auction request.
	 */
	private void makeButtonAddAuction() {
		myAddAuction = new JButton("Auction Request");	
		
		myAddAuction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				
			}
		});
		myButtons.add(myAddAuction);		
	}
	
	/**
	 * This method is used to remove an inventory item.
	 */
	private void makeButtonRemoveItem() {
		myRemoveItem = new JButton("Remove Item");
		myRemoveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				
				if (myItemNumber != -1) {
					
					int selected = JOptionPane.showConfirmDialog(myFrame, "Are you sure you want "
							+ "to delete the selected item (item# " + myItemNumber + ")?");
					
					if (selected == JOptionPane.YES_OPTION) {						
						boolean r = myAuction.removeItem(myCurrNonProfit, myItemNumber, myAuction.getDate(), myCurrDate);
						myItemNumber = -1;								
												
						remove(myInventory);
						setAuctionInfo();					
					}
				}				
			}
		});
		
		myButtons.add(myRemoveItem);
	}
	
	/**
	 * This method is used to add an inventory item.
	 */
	private void makeButtonAddItem() {
		myAddItem = new JButton("Add Item");
		myAddItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				
							
				
			}
		});
		myButtons.add(myAddItem);
	}
	
	/**
	 * Used to make logout button.
	 */
	private void makeButtonLogout() {
		myLogout = new JButton("Logout");
		myLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				remove(myInventory);
				firePropertyChange("LOGIN", "Bidder", "Login");
				
			}
		});
		myButtons.add(myLogout);
	}
	
	private void makeAuctionInfoLabel() {
		myAuctionInfo = new NonProfitInfoPanel(myCalendar);
		this.add(myAuctionInfo, BorderLayout.NORTH);
		
		
	}

	
	private void setAuctionInfo() {		
				
		if (myCalendar.getAuctionForOrganization(myCurrNonProfit) != null) {			

			myAuction = myCalendar.getAuctionForOrganization(myCurrNonProfit);
			myAuctionInfo.setNonProfit(myCurrNonProfit);
			myAuctionInfo.setTextHasAuction();			
			myInventory = new ItemsPanel(myCurrNonProfit, myCalendar);
			myInventory.addPropertyChangeListener(this);
			this.add(myInventory, BorderLayout.CENTER);
			myInventory.displayItems();

		} else {	
			myAuctionInfo.setTextNoAuction();			
		}		
	}
	
	
	
	public void setUpNonProfitInfo() {
		setAuctionInfo();
	}
	
	public void setUser(NonProfit theNonProfit) {
		
		myCurrNonProfit = theNonProfit;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent theEvent) {

		if(theEvent.getPropertyName().equals(ItemsPanel.NEW_ITEM_SELECTED)) {
			myItemNumber = (int)theEvent.getNewValue();
		}
	}
}
