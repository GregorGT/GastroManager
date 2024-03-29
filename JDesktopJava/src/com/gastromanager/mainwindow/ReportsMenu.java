/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.mainwindow;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.jdatepicker.JDatePanel;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import com.gastromanager.reports.*;
import com.gastromanager.util.PropertiesUtil;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;

public class ReportsMenu extends JPanel{

	public ReportsMenu() { 
		JPanel panelDates = new JPanel();
		panelDates.setLayout(new BoxLayout(panelDates, BoxLayout.Y_AXIS));
		panelDates.setBorder(new TitledBorder(null, "Starting and ending dates", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		
		JLabel dateFrom= new JLabel("From: ");
	    panelDates.add(dateFrom);
		
	    UtilDateModel startDateModel = new UtilDateModel();
	    Properties startDateProperties = new Properties();
	    startDateProperties.put("text.today", "Today");
	    startDateProperties.put("text.month", "Month");
	    startDateProperties.put("text.year", "Year");
		JDatePanel startDatePanel = new JDatePanel(startDateModel);
		JDatePicker startDatePicker = new JDatePicker();
		panelDates.add(startDatePicker);

		JLabel dateUntil = new JLabel("Until: ");
		panelDates.add(dateUntil);
		
		UtilDateModel endDateModel = new UtilDateModel();
		Properties endDateProperties = new Properties();
		endDateProperties.put("text.today", "Today");
		endDateProperties.put("text.month", "Month");
		endDateProperties.put("text.year", "Year");
		JDatePanel endDatePanel = new JDatePanel(endDateModel);
		JDatePicker endDatePicker = new JDatePicker();
		panelDates.add(endDatePicker);
		
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBorder(new TitledBorder(null, "Generate...", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridLayout gridLayout = new GridLayout(5, 1);
		gridLayout.setVgap(10);
		panelButtons.setLayout(gridLayout);
		
		JButton getReport = new JButton("Generate report");
		getReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				GregorianCalendar start;
				GregorianCalendar end;
				
				start = (GregorianCalendar) startDatePicker.getModel().getValue();
				end = (GregorianCalendar) endDatePicker.getModel().getValue();
				
				Instant firstDate = null;
				Instant secondDate = null;
				
				try {
					firstDate = start.toInstant();
					secondDate = end.toInstant();
				}
				catch  (NullPointerException e) {
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "You cannot generate a report without setting the starting"
							+ " and the ending dates first.");	
					return;
				}
				
				Date startDate = Date.from(firstDate);
				Date endDate = Date.from(secondDate);
				
				String []dates = new String[2];
				try {
					dates[0] = startDate.toString();
					dates[1] = endDate.toString();
					
					if (userLimitsForTables()) {
						new UserInputReport().main(dates);
					}
				} catch (NullPointerException e) {
					System.err.println("Null");
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "You cannot generate a report without setting the starting"
							+ " and the ending dates first.");	
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (UnknownPropertyException e) {
					e.printStackTrace();
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				} catch (WrappedTargetException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
				startDatePicker.getModel().setValue(null);
				endDatePicker.getModel().setValue(null);
			}
		});
		
		panelButtons.add(getReport);
		
		JButton getDailyReport = new JButton("Generate today's report");
		getDailyReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					if (userLimitsForTables()) {
						new DailyFinancialReport().main(null);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (UnknownPropertyException e) {
					e.printStackTrace();
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				} catch (WrappedTargetException e) {
					e.printStackTrace();
				}
			}
			
		});
		panelButtons.add(getDailyReport);
		
		JButton getMonthlyReport = new JButton("Generate monthly report");
		getMonthlyReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					if (userLimitsForTables()) {
						new MonthlyFinancialReport().main(null);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (UnknownPropertyException e) {
					e.printStackTrace();
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				} catch (WrappedTargetException e) {
					e.printStackTrace();
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				}
			}
			
		});
		panelButtons.add(getMonthlyReport);
		
		JButton getQuarterlyReport = new JButton("Generate quarterly report");
		getQuarterlyReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					if (userLimitsForTables()) {
						new QuarterlyFinancialReport().main(null);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (UnknownPropertyException e) {
					e.printStackTrace();
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				} catch (WrappedTargetException e) {
					e.printStackTrace();
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				}
			}
			
		});
		panelButtons.add(getQuarterlyReport);
		
		JButton getYearlyReport = new JButton("Generate yearly report");
		getYearlyReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					if (userLimitsForTables() ) {
						new YearlyFinancialReport().main(null);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (UnknownPropertyException e) {
					e.printStackTrace();
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				} catch (WrappedTargetException e) {
					e.printStackTrace();
				}
			}
			
		});
		panelButtons.add(getYearlyReport);
		
		
		this.add(panelDates);
		this.add(panelButtons);

	}
	
	private boolean userLimitsForTables() {
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel questionForAllItems = new JLabel("How many items do you want to be displayed?");
		JLabel questionForMBItems = new JLabel("How many items do you want to be displayed as the most bought items?");
		JLabel questionForLBItems = new JLabel("How many items do you want to be displayed as the least bought items?");
		
		ArrayList<String> choices = new ArrayList<>();
		choices.add("All");
		for (int i=1; i<31; ++i) {
			choices.add(String.valueOf(i));
		}
		
		String[] ch = choices.toArray(new String[0]);
		JComboBox comboBox1 = new JComboBox(ch);
		comboBox1.setSelectedIndex(0);
		JComboBox comboBox2 = new JComboBox(ch);
		comboBox2.setSelectedIndex(0);
		JComboBox comboBox3 = new JComboBox(ch);
		comboBox3.setSelectedIndex(0);
		
		panel.add(questionForAllItems);
		panel.add(comboBox1);
		panel.add(questionForMBItems);
		panel.add(comboBox2);
		panel.add(questionForLBItems);
		panel.add(comboBox3);
		

		int option = JOptionPane.showOptionDialog(null, panel, "Total items per category", JOptionPane.OK_CANCEL_OPTION,
		        					JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (option == JOptionPane.OK_OPTION) {
		 	modifyPropertiesForTableLimits(comboBox1.getSelectedItem().toString(),
								 		   comboBox2.getSelectedItem().toString(),
								 		   comboBox3.getSelectedItem().toString());
		 	return true;
		}	
		return false;
	}
	
	private void modifyPropertiesForTableLimits(String incomeTableLimit, String mbiLimit, String lbiLimit) {
		if (incomeTableLimit.equals("All")) {
			incomeTableLimit = "-1";
		}
		if (mbiLimit.equals("All")) {
			mbiLimit = "-1";
		}
		if (lbiLimit.equals("All")) {
			lbiLimit = "-1";
		}
		PropertiesUtil.setPropertyValue("allItemCountReport", incomeTableLimit);
		PropertiesUtil.setPropertyValue("moustBoughItemsReport", mbiLimit);
		PropertiesUtil.setPropertyValue("leastBoughItemsReport", lbiLimit);
	}
	
	public ReportsMenu(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public ReportsMenu(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public ReportsMenu(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}
}
