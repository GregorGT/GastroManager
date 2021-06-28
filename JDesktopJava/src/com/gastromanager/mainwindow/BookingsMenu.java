package com.gastromanager.mainwindow;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdatepicker.JDatePicker;

import com.gastromanager.db.DbConnection;

public class BookingsMenu extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private static final String RANDOM_TABLE_FROM_LOCATION =  "SELECT * "
			+ "FROM"
			+ "    (SELECT floor_id, table_id"
			+ "    FROM location l"
			+ "    EXCEPT"
			+ "    SELECT floor_id, table_id"
			+ "    FROM reservations"
			+ "    WHERE (start_date<=? AND end_date>=?)"
			+ "    OR (start_date<=? AND end_date>=?))"
			+ "ORDER BY RANDOM() LIMIT 1";
	
	private static final String INSERT_INTO_RESERVATIONS = "INSERT INTO reservations "
														 + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_MAX_ID = "SELECT max(id)+1 FROM reservations";
	private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM reservations WHERE start_date LIKE ? ORDER BY start_date, end_date";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private JDatePicker datePicker;
	private JDatePicker dateForTable;
	private JComboBox timesComboBox;
	private JComboBox durationComboBox;
	private JSpinner spinner;
	private List<Map.Entry<Integer, Integer>> reservations = new ArrayList<>();
	private Connection connection = DbConnection.getDbConnection().gastroDbConnection;
	private JTable jTable;
	private String columns[] = {"Date","Start time","End time", "Floor", "Table", "Name"};  
	private JLabel result;
	private String floorId = "";
	private String tableId = "";
	private String[] dates = new String[2];
	private DefaultTableModel model;
	private boolean canBeInserted = false;
	private JTextField textField;
	
	BookingsMenu() {
		
		this.setLayout(new BorderLayout(5,5));	
		
	    GridLayout gridLayout = new GridLayout(2,4);
	    gridLayout.setHgap(55);
	
	    JPanel northPanel = new JPanel();
		northPanel.setLayout(gridLayout);
		
		JLabel dateLabel = new JLabel("Date: ");
	    northPanel.add(dateLabel);
		
	    JLabel timeLabel = new JLabel("Time: ");
	    northPanel.add(timeLabel);
	    
	    JLabel durationLabel = new JLabel("Duration: ");
	    northPanel.add(durationLabel);
	    
	    JLabel peopleLabel = new JLabel("People: ");
	    northPanel.add(peopleLabel);
	    
	    JLabel nameLabel = new JLabel("Name: ");
	    northPanel.add(nameLabel);
	    
	    
		datePicker = new JDatePicker();
		northPanel.add(datePicker);

		timesComboBox = new JComboBox();
		for (int i=0; i<24; ++i) {
			if (i < 10) {
				timesComboBox.addItem("0"+String.valueOf(i)+":00");
				timesComboBox.addItem("0"+String.valueOf(i)+":30");
			} else {
				timesComboBox.addItem(String.valueOf(i)+":00");
				timesComboBox.addItem(String.valueOf(i)+":30");	
			}
		}
		northPanel.add(timesComboBox);
		
		durationComboBox = new JComboBox();
		durationComboBox.addItem("30 min");
		durationComboBox.addItem("45 min");
		durationComboBox.addItem("1 hour");
		durationComboBox.addItem("1.30 hour");
		durationComboBox.addItem("2 hours");
		durationComboBox.addItem("2.30 hours");
		durationComboBox.addItem("3 hours");
		northPanel.add(durationComboBox);
		
		
		SpinnerModel value =  
	             new SpinnerNumberModel(0, //initial value  
	                0, //minimum value  
	                20, //maximum value  
	                1); //step  
	    spinner = new JSpinner(value);   
	            spinner.setBounds(100,100,50,30); 
	    northPanel.add(spinner);
	    
	    textField = new JTextField();
	    northPanel.add(textField);
	    
	    
	    JPanel eastPanel = new JPanel();
	    BoxLayout boxLayout = new BoxLayout(eastPanel, BoxLayout.Y_AXIS);
	    eastPanel.setLayout(boxLayout);
	    
	    JButton checkAvailability = new JButton("<html>Check Availability<html/>");
	    eastPanel.add(checkAvailability);
	    checkAvailability.addActionListener(checkAvailabilityActionListener());
	    JButton addReservation = new JButton("<html>Add reservation<html/>");
	    eastPanel.add(addReservation);
	    addReservation.addActionListener(addReservationListener());
	    
	    result = new JLabel();
	    eastPanel.add(result);
	    
	    jTable = new JTable(new DefaultTableModel(null, columns));
	    JScrollPane scrollPane = new JScrollPane(jTable);    
	    model = (DefaultTableModel) jTable.getModel();
	    
	    TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
        sorter.setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
	    
	    List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
	    sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
	    sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
	    sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
	    sorter.setSortKeys(sortKeys);
	    jTable.setRowSorter(sorter);
	    
	    fillTable();
	    
	    dateForTable = new JDatePicker();
	    dateForTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fillTable();
			}
	    });

	    scrollPane.add(dateForTable);
	    
	    JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    BoxLayout box = new BoxLayout(westPanel, BoxLayout.Y_AXIS);
	    westPanel.setLayout(box);
	    
	    
	    westPanel.add(dateForTable);
	    westPanel.add(scrollPane);
	    
	    this.add(westPanel, BorderLayout.CENTER);  
	    this.add(eastPanel, BorderLayout.EAST);
		this.add(northPanel, BorderLayout.NORTH);
	}
	
	
	private void fillTable() {
		for(int i = model.getRowCount() - 1; i >= 0; --i) {
		    model.removeRow(i);
		}
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SELECT_ALL_RESERVATIONS);
			String dat = getDate()+"%";
			preparedStatement.setString(1, dat);

			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				String date = rs.getString("start_date");
				String endDate = rs.getString("end_date");
				String floorId = rs.getString("floor_id");
				String tableId = rs.getString("table_id");
				String name = rs.getString("name");
				model.addRow(new Object[] {date.substring(0, 10), date.substring(11, date.length()), endDate.substring(11, endDate.length()), floorId, tableId, name});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private String getDate() {
		String date;
		GregorianCalendar gc;
		Date d;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			gc = (GregorianCalendar) dateForTable.getModel().getValue();
			d = Date.from(gc.toInstant());
		} catch (NullPointerException e) {
			d = new Date();
		}

		date = sdf.format(d);
		return date;
	}
	
	
	private ActionListener checkAvailabilityActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				if(!checkDatePicker()) {
					JOptionPane.showMessageDialog(null, "Please select a date.");
					return;
				}
				
				getStartingAndEndingDate();
				
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				try {
					if (sdf.parse(dates[0]).compareTo(sdf.parse(sdf.format(new Date()) ))< 0 ) {
						JOptionPane.showMessageDialog(null, "You cannot enter a past date.");
						return;
					}
				} catch (ParseException e) {
					System.err.println("ParseException: file: BookingsMenu.java method: checkAvailabilityActionListener()");
				}
				
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(RANDOM_TABLE_FROM_LOCATION);
	
					preparedStatement.setString(1, dates[0]);
					preparedStatement.setString(2, dates[0]);
					preparedStatement.setString(3, dates[1]);
					preparedStatement.setString(4, dates[1]);
					
					ResultSet rs = preparedStatement.executeQuery();

					if (rs.next()) {
						floorId = rs.getString("floor_id");
						tableId = rs.getString("table_id");
						result.setText("<html>Floor: " + floorId + "<br/>Table:" + tableId + "</html>");
						canBeInserted = true;
					} else {
						result.setText("No available tables");
						canBeInserted = false;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}		
			}
			
		};
	}
	
	private ActionListener addReservationListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (!checkName()) {
					JOptionPane.showMessageDialog(null, "Please enter a name.");
					return;					
				}
				
				if (floorId == null || tableId == null || result.getText().equals("No available tables") || !canBeInserted) {
					return;
				}
				getStartingAndEndingDate();
				
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MAX_ID);
					int id = preparedStatement.executeQuery().getInt(1);
					
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT); 
					
					preparedStatement = connection.prepareStatement(INSERT_INTO_RESERVATIONS);
					preparedStatement.setInt(1, id);
					preparedStatement.setString(2, spinner.getValue().toString());
					preparedStatement.setString(3, "");
					preparedStatement.setString(4,  dates[0]);
					preparedStatement.setInt(5, Integer.parseInt(floorId));
					preparedStatement.setInt(6, Integer.parseInt(tableId));
					preparedStatement.setString(7, dates[1]);
					preparedStatement.setString(8, textField.getText());
					
					preparedStatement.executeUpdate();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				try {
					GregorianCalendar g = (GregorianCalendar) datePicker.getModel().getValue();
					GregorianCalendar gc = (GregorianCalendar) dateForTable.getModel().getValue();
					
					
					if (g.get(Calendar.YEAR) == gc.get(Calendar.YEAR) && g.get(Calendar.MONTH) == gc.get(Calendar.MONTH) && g.get(Calendar.DAY_OF_MONTH) == gc.get(Calendar.DAY_OF_MONTH)) {
						model.addRow(new Object[]{dates[0].substring(0, 10), dates[0].substring(11, dates[0].length()), dates[1].substring(11, dates[1].length()), floorId, tableId, textField.getText()});		
					}
				} catch (NullPointerException e) {
					if (datePicker.getModel().getValue().equals(new GregorianCalendar())) {
						model.addRow(new Object[]{dates[0].substring(0, 10), dates[0].substring(11, dates[0].length()), dates[1].substring(11, dates[1].length()), floorId, tableId, textField.getText()});	
					}	
				}
				
				canBeInserted = false;
				
			}
			
				
		};
	}
	
	private boolean checkName() {
		return textField.getText().trim().equals("") ? false: true;
	}
	
	private boolean checkDatePicker() {
		GregorianCalendar gd;
		gd = (GregorianCalendar) datePicker.getModel().getValue();
		return (gd == null) ? false: true;
	}
	
	private void getStartingAndEndingDate() {
		GregorianCalendar gd;
		gd = (GregorianCalendar) datePicker.getModel().getValue();
		
		Date d = Date.from(gd.toInstant());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");					
		String startDate = sdf.format(d);

		startDate += " "+timesComboBox.getSelectedItem().toString()+":00";

		sdf = new SimpleDateFormat(DATE_FORMAT);
		Date da = null;
		try {
			da = sdf.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
        calendar.setTime(da);
        
		String endDate;
		Date da2 = null;
		long timeInSecs = calendar.getTimeInMillis();
		
		switch(durationComboBox.getSelectedItem().toString()) {
		case "30 min":
			da2 = new Date(timeInSecs + (30 * 60 * 1000));
			break;
		case "45 min":
			da2 = new Date(timeInSecs + (45 * 60 * 1000));
			break;
		case "1 hour":
			da2 = new Date(timeInSecs + (60 * 60 * 1000));
			break;
		case "1.30 hour":
			da2 = new Date(timeInSecs + (90 * 60 * 1000));
			break;	
		case "2 hours":
			da2 = new Date(timeInSecs + (120 * 60 * 1000));
			break;	
		case "2.30 hours":
			da2 = new Date(timeInSecs + (150 * 60 * 1000));
			break;	
		case "3 hours":
			da2 = new Date(timeInSecs + (180 * 60 * 1000));
		break;	
				
		default:
			break;
		}
		endDate = sdf.format(da2);
		
		dates[0] = startDate;
		dates[1] = endDate;
	}
	
}