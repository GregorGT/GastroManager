package com.gastromanager.mainwindow;

import com.gastromanager.comparator.OrderItemComparator;
import com.gastromanager.models.Order;
import com.gastromanager.models.OrderDetailQuery;
import com.gastromanager.models.OrderInfo;
import com.gastromanager.models.OrderItemInfo;
import com.gastromanager.models.OrderItemTransactionInfo;
import com.gastromanager.models.TransactionInfo;
import com.gastromanager.print.MainPrintService;
import com.gastromanager.print.PrintService;
import com.gastromanager.print.PrintServiceImpl;
import com.gastromanager.service.PaymentService;
import com.gastromanager.service.impl.PaymentServiceImpl;
import com.gastromanager.ui.OrderItemListTableModel;
import com.gastromanager.util.DbUtil;
import com.gastromanager.util.GastroManagerConstants;
import com.gastromanager.util.PropertiesUtil;
import com.gastromanager.util.Util;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PaymentMenu  extends Panel{

	private JPanel leftItemListPanel;
	private JPanel selectedItemsListPanel;

	private JTextField txtFieldFloor;
	private JTextField txtFieldTableID;
	private JTextField txtFieldOrderID;
	private JTextField txtFieldTipAmount;
	private JLabel subtotalAmountField;
	private JLabel taxesAmountField;
	private JLabel ltotalAmountField;
	
	private PaymentService paymentService;
	private List<Order> orderList;
	private List<OrderItemInfo> orderItemInfoList;
	private List<OrderItemInfo> selectedOrderItemInfoList;
	private JTable orderItemsListTable;
	private JTable selectedOrderItemsListTable;
	private JLabel floorId;
	private JLabel tableId;
	private JLabel orderId;
	List<BigDecimal> totals = new ArrayList<>();
	private boolean isSelectionFlow;
	private DbUtil db;

	public PaymentMenu() {
		paymentService = new PaymentServiceImpl();
		isSelectionFlow = false;
		leftItemListPanel = new JPanel();
		leftItemListPanel.setLayout(new BorderLayout());
		leftItemListPanel.setBounds(50, 220, 250, 300);
		orderItemsListTable = new JTable(new OrderItemListTableModel(orderItemInfoList)) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				//orderItemsListTable.addRowSelectionInterval(row, row);
				System.out.println("Row : "+row + " Col: "+ col);
				Component c = super.prepareRenderer(renderer, row, col);
				Integer payed = (Integer) getValueAt(row, 3);
				Color color = UIManager.getColor("Table.selectionBackground");
				System.out.println("Row "+row + " value "+payed);
				if(payed == 1) {
					c.setBackground(Color.GREEN);
				} else {
					c.setBackground(Color.WHITE);
				}
				return c;
			}
		};
		//orderItemsListTable.setRowSelectionAllowed(true);
		//orderItemsListTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		orderItemsListTable.setRowHeight(15);
		if(orderItemsListTable.getColumnModel().getColumnCount() > 0) {
			orderItemsListTable.getColumnModel().getColumn(0).setPreferredWidth(100);
			//hide the item id column
			orderItemsListTable.getColumnModel().getColumn(2).setWidth(0);
			orderItemsListTable.getColumnModel().getColumn(2).setMinWidth(0);
			orderItemsListTable.getColumnModel().getColumn(2).setMaxWidth(0);
			orderItemsListTable.getColumnModel().getColumn(3).setWidth(0);
			orderItemsListTable.getColumnModel().getColumn(3).setMinWidth(0);
			orderItemsListTable.getColumnModel().getColumn(3).setMaxWidth(0);
		}
		JScrollPane scrollPane = new JScrollPane(orderItemsListTable);
		leftItemListPanel.add(scrollPane, BorderLayout.CENTER);
		leftItemListPanel.setVisible(true);
		this.add(leftItemListPanel);

		selectedItemsListPanel = new JPanel();
		selectedItemsListPanel.setLayout(new BorderLayout());
		selectedItemsListPanel.setBounds(390, 220, 250, 300);
		selectedOrderItemInfoList = new ArrayList<>();
		selectedOrderItemsListTable = new JTable(new OrderItemListTableModel(selectedOrderItemInfoList)){
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return (colIndex == 1 && rowIndex != selectedOrderItemsListTable.getModel().getRowCount()-1) ? true : false;
			}

		};

		selectedOrderItemsListTable.setRowHeight(20);
		selectedOrderItemsListTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		//hide the item id column
		selectedOrderItemsListTable.getColumnModel().getColumn(2).setWidth(0);
		selectedOrderItemsListTable.getColumnModel().getColumn(2).setMinWidth(0);
		selectedOrderItemsListTable.getColumnModel().getColumn(2).setMaxWidth(0);
		selectedOrderItemsListTable.getColumnModel().getColumn(3).setWidth(0);
		selectedOrderItemsListTable.getColumnModel().getColumn(3).setMinWidth(0);
		selectedOrderItemsListTable.getColumnModel().getColumn(3).setMaxWidth(0);

		JScrollPane selectedOrderItemsListScrollPane = new JScrollPane(selectedOrderItemsListTable);
		//leftItemsList.add(scrollPane, BorderLayout.CENTER);
		selectedItemsListPanel.add(selectedOrderItemsListScrollPane, BorderLayout.CENTER);
		selectedItemsListPanel.setVisible(true);
		this.add(selectedItemsListPanel);

		floorId = new JLabel("Floor No");
		floorId.setBounds(50, 5, 150, 30);
	    txtFieldFloor = new JTextField("Floor No");
		txtFieldFloor.setBounds(50, 30, 150, 30);
		this.add(floorId);
		this.add(txtFieldFloor);
		txtFieldFloor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtFieldFloor.setText("");
			}
		});

		tableId = new JLabel("Table No");
		tableId.setBounds(220, 5, 150, 30);
	    txtFieldTableID = new JTextField("Table No");
		txtFieldTableID.setBounds(220, 30, 150, 30);
		this.add(tableId);
		this.add(txtFieldTableID);
		txtFieldTableID.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtFieldTableID.setText("");
			}
		});

		orderId = new JLabel("Order ID");
		orderId.setBounds(390, 5, 150, 30);
		txtFieldOrderID = new JTextField("Order ID");
		txtFieldOrderID.setBounds(390, 30, 150, 30);
		this.add(orderId);
		this.add(txtFieldOrderID);
		txtFieldOrderID.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtFieldOrderID.setText("");
			}
		});

		JButton selectButton2 = new JButton("Select");
		selectButton2.setBounds(390, 65, 150, 30);
		selectButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadOrderItems(false, false);
			}
		});
		this.add(selectButton2);

		//main buttons
		JButton addButton = new JButton("Add");
		addButton.setBounds(305, 295, 80, 30);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("table rows selected "+orderItemsListTable.getSelectedRows());
				processOperation(false);
				/*int[] selectedRows = orderItemsListTable.getSelectedRows();
				for(int i=0; i<selectedRows.length ; i++) {
					Long selectedOrderItemId = Long.parseLong(orderItemsListTable.getValueAt(i, 2).toString());
					System.out.println("selected item id "+orderItemsListTable.getValueAt(i, 2));
					//loadSelectedOrderItemTable(selectedOrderItemId);
					processOperation(false);
				}*/
			}
		});
		this.add(addButton);

		JButton calcTotalButton = new JButton("Add All");
		calcTotalButton.setBounds(305, 400, 80, 30);

		JButton removeButton = new JButton("Remove");
		removeButton.setBounds(305, 345, 80, 30);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("table rows selected "+selectedOrderItemsListTable.getSelectedRows());
				processOperation(true);
				int[] selectedRows = selectedOrderItemsListTable.getSelectedRows();
				for(int i=0; i<selectedRows.length ; i++) {
					Long selectedOrderItemId = Long.parseLong(selectedOrderItemsListTable.getValueAt(i, 2).toString());
					System.out.println("selected item id "+selectedOrderItemsListTable.getValueAt(i, 2));
					//loadSelectedOrderItemTable(selectedOrderItemId);
					processOperation(true);
				}
			}
		});
		this.add(removeButton);

		JButton undoButton = new JButton("Undo");
		undoButton.setBounds(135, 530, 80, 30);
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRowIndexes = orderItemsListTable.getSelectedRows();
				for(int i=0 ; i<selectedRowIndexes.length; i++) {
					Long selectedOrderItemId = Long.parseLong(orderItemsListTable.getValueAt(selectedRowIndexes[i], 2).toString());
					OrderItemInfo orderItemInfo = getSelectedOrderItem(selectedOrderItemId, false);
					System.out.println("Undo payment for order item "+orderItemInfo.getItemId());
					paymentService.undoPayment(orderItemInfo);
				}
				loadOrderItems(false, false);
				((OrderItemListTableModel) orderItemsListTable.getModel()).fireTableDataChanged();
			}
		});
		//this.add(undoButton);
		JLabel subtotalLabel = new JLabel("SubTotal: ");
		JLabel taxesLabel = new JLabel("Taxes("+PropertiesUtil.getPropertyValue("salsetax")+"%): ");
		JLabel totalLabel = new JLabel("Total: ");
		subtotalAmountField = new JLabel("0");
		subtotalAmountField.setBounds(480, 520, 50, 30);
		subtotalAmountField.setForeground(Color.RED);
		subtotalAmountField.setFont(new Font("", Font.HANGING_BASELINE, 12));
		taxesAmountField = new JLabel("0");
		taxesAmountField.setBounds(480, 540, 50, 30);
		taxesAmountField.setForeground(Color.RED);
		taxesAmountField.setFont(new Font("", Font.HANGING_BASELINE, 12));
		ltotalAmountField = new JLabel("0");
		ltotalAmountField.setBounds(480, 560, 50, 30);
		ltotalAmountField.setForeground(Color.RED);
		ltotalAmountField.setFont(new Font("", Font.HANGING_BASELINE, 12));
		
		
		subtotalLabel.setBounds(390, 520, 80, 30);
		subtotalLabel.setFont(new Font("", Font.HANGING_BASELINE, 12));
		taxesLabel.setBounds(390, 540, 80, 30);
		taxesLabel.setFont(new Font("", Font.HANGING_BASELINE, 12));
		totalLabel.setBounds(390, 560, 80, 30);
		totalLabel.setFont(new Font("", Font.HANGING_BASELINE, 12));
		
		
		
		JButton payedButton = new JButton("Payed");
		payedButton.setBounds(560, 520, 80, 30);
		payedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(PropertiesUtil.getPropertyValue("salsetax"));

				
				OrderItemTransactionInfo orderItemTransactionInfo = null;
				List<OrderItemInfo> orderItemInfoList = null;
				orderItemInfoList = ((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).getOrderItemInfoList();
				orderItemTransactionInfo = new OrderItemTransactionInfo();
				orderItemTransactionInfo.setOrderItemInfo(orderItemInfoList);
				orderItemTransactionInfo.setAddTransaction(true);
				orderItemTransactionInfo.setOrderId(txtFieldOrderID.getText());
				List<OrderItemInfo> itemList = paymentService.processTransactionInfo(orderItemTransactionInfo);
				((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).getOrderItemInfoList().removeAll(orderItemInfoList);
				((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).fireTableDataChanged();
				
				
//				StringBuilder bill = new StringBuilder();
//				
//				writeFileToBill("resources/billinghead.txt", bill);
//				
//				PrintServiceImpl printService = new PrintServiceImpl();
//				
//				bill.append("\n");
//				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//				Date date = new Date();
//				
//				bill.append("Time: " + dateFormat.format(date) + "\n");
//				//fileWriter.append("Server(terminal): " + ServerSocketMenu.serverTextField.getText() + "\n");//printService.getPrintInfo(txtFieldOrderID.getText(), "Server name", GastroManagerConstants.PRINT_RECEIPT));
//				bill.append("\n");
//				bill.append("\n");
//				
//				bill.append(printService.getPrintInfo(txtFieldOrderID.getText(), "", GastroManagerConstants.PRINT_RECEIPT));
//				bill.append("\n");
//				
//				Double totalPrice = db.getTotalPrice(txtFieldOrderID.getText());
//				Double salsetax = Double.parseDouble(PropertiesUtil.getPropertyValue("salsetax"));
//				Double finalPrice = totalPrice + (totalPrice * (salsetax / 100));
//			
//				bill.append("--------------------------------\n");
//				bill.append("SubTotal: " + GastroManagerConstants.FOUR_SPACES + totalPrice +"\n");
//				bill.append("Taxes: " + GastroManagerConstants.FOUR_SPACES + PropertiesUtil.getPropertyValue("salsetax") + "%\n");
//				bill.append("Total: " + GastroManagerConstants.FOUR_SPACES + finalPrice + PropertiesUtil.getPropertyValue("currency") + "\n");
//				bill.append("--------------------------------\n");
//				
//				writeFileToBill("resources/billingfooter.txt", bill);
//								
//				System.out.println("This is the final bill\n\n\n\n");
//				System.out.println(bill);
//				try {
//					printService.executePrintOverNetwork(bill.toString(), PropertiesUtil.getPropertyValue("networkPrinter.ip.billing"), Integer.parseInt(PropertiesUtil.getPropertyValue("networkPrinter.port.billing")));
//				} catch( Exception ex)
//				{
//					System.out.print(ex.toString());
//				}
//				MainPrintService mainPrintService = new MainPrintService();
//				try {
//					mainPrintService.printString(PropertiesUtil.getPropertyValue("printer.billing"), bill.toString());
//				}catch(Exception ex)
//				{
//					System.out.print(ex.toString());
//				}
			}
		});
		this.add(payedButton);
		this.add(subtotalLabel);
		this.add(taxesLabel);
		this.add(totalLabel);
		this.add(subtotalAmountField);
		this.add(taxesAmountField);
		this.add(ltotalAmountField);
		
		this.add(txtFieldOrderID);

		orderItemsListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
			}
		});


		calcTotalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableModel leftTableModel = orderItemsListTable.getModel();
				int counter=0;
				for(int i=leftTableModel.getRowCount(); i> 0; i--) {
					Long selectedOrderItemId = Long.parseLong(leftTableModel.getValueAt(leftTableModel.getRowCount() - i, 2).toString());
					BigDecimal price = BigDecimal.valueOf((Double) leftTableModel.getValueAt(leftTableModel.getRowCount() - i, 1));
					Integer paid = (Integer) leftTableModel.getValueAt(leftTableModel.getRowCount() - i, 3);
					if(paid != 1) {
						totals.add(price);
						OrderItemInfo orderItemInfo = getSelectedOrderItem(selectedOrderItemId, false);
						addItemToTable(orderItemInfo, selectedOrderItemsListTable);
						removeItemFromTable(orderItemInfo, orderItemsListTable);
						counter++;
					}
				}
				BigDecimal total = totals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
				subtotalAmountField.setText(total.toString());
				updateTaxesAndTotalAmount();
				
				
				/// here comes the code
			}
		});
		this.add(calcTotalButton);

	}
	
	private void updateTaxesAndTotalAmount()
	{
		double total = Double.parseDouble(subtotalAmountField.getText());
		
		Double totalPrice = total;
		Double salsetax = Double.parseDouble(PropertiesUtil.getPropertyValue("salsetax"));
		
		double tmpvalue = (totalPrice * (salsetax / 100));
		tmpvalue = Util.roundDouble(tmpvalue, 2);
		double taxes = tmpvalue;
				
		Double finalPrice = totalPrice + taxes;
		finalPrice = Util.roundDouble(finalPrice, 2);
		
		taxesAmountField.setText(String.valueOf(taxes));
		ltotalAmountField.setText(String.valueOf(finalPrice));
		
	}

	private void writeFileToBill(String path, StringBuilder bill) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    bill.append(everything);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addItemToTable(OrderItemInfo orderItemInfo, JTable table) {
		List<OrderItemInfo> tableItems = ((OrderItemListTableModel) table.getModel()).getOrderItemInfoList();
		if(tableItems == null) {
			tableItems = new ArrayList<>();
			((OrderItemListTableModel) table.getModel()).setOrderItemInfoList(tableItems);
		}
		tableItems.add(orderItemInfo);
		((OrderItemListTableModel) table.getModel()).fireTableDataChanged();
	}

	private void removeItemFromTable(OrderItemInfo orderItemInfo, JTable table) {
		List<OrderItemInfo> tableItems = ((OrderItemListTableModel) table.getModel()).getOrderItemInfoList();
		tableItems.remove(orderItemInfo);
		((OrderItemListTableModel) table.getModel()).fireTableDataChanged();
	}

	private void processOperation(Boolean isRemove){
		JTable leftTable = orderItemsListTable;
		JTable rightTable  = selectedOrderItemsListTable;

		if(!isRemove) { //add
			int[] selectedRowIndexes = leftTable.getSelectedRows();
			TableModel tm = leftTable.getModel();
			int counter = 0;
			for(int i=0 ; i<selectedRowIndexes.length; i++) {
				Long selectedOrderItemId = Long.parseLong(leftTable.getValueAt(selectedRowIndexes[i]-counter, 2).toString());
				BigDecimal price = BigDecimal.valueOf((Double) tm.getValueAt(selectedRowIndexes[i]-counter, 1));
				Integer paid = (Integer) leftTable.getValueAt(selectedRowIndexes[i]-counter, 3);
				if(paid != 1) {
					totals.add(price);
					OrderItemInfo orderItemInfo = getSelectedOrderItem(selectedOrderItemId, false);
					addItemToTable(orderItemInfo, selectedOrderItemsListTable);
					removeItemFromTable(orderItemInfo, orderItemsListTable);
					counter++;
				}
			}

		} else {
			int[] selectedRowIndexes = rightTable.getSelectedRows();
			for(int i=0 ; i<selectedRowIndexes.length; i++) {
				Long selectedOrderItemId = Long.parseLong(rightTable.getValueAt(selectedRowIndexes[i]-i, 2).toString());
				totals.remove(selectedRowIndexes[i]-i);
				OrderItemInfo orderItemInfo = getSelectedOrderItem(selectedOrderItemId, isRemove);
				addItemToTable(orderItemInfo, leftTable);
				removeItemFromTable(orderItemInfo, rightTable);
			}
		}
		BigDecimal total = totals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		subtotalAmountField.setText(total.toString());
		updateTaxesAndTotalAmount();
	}

	private OrderItemInfo getSelectedOrderItem(Long itemId, Boolean isRemove) {
		OrderItemInfo orderItemInfo = null;
		if(isRemove) {
			orderItemInfo = selectedOrderItemInfoList.stream().filter(currOrderItemInfo ->
					currOrderItemInfo.getItemId().equals(itemId)).findAny().orElse(null);
		} else {
			orderItemInfo = orderItemInfoList.stream().filter(currOrderItemInfo ->
					currOrderItemInfo.getItemId().equals(itemId)).findAny().orElse(null);
		}

		return orderItemInfo;
	}

	private void loadOrderItems(Boolean isPrev, Boolean isNext) {
		String floorId = txtFieldFloor.getText();
		String tableId = txtFieldTableID.getText();
		String orderId = txtFieldOrderID.getText();
		if(floorId!=null && !floorId.trim().equals("")
				&& tableId != null && !tableId.trim().equals("")
				&& orderId != null && !orderId.trim().equals("")) {
			OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
			orderDetailQuery.setFloorId(floorId.trim());
			orderDetailQuery.setTableId(tableId.trim());
			Integer humanReadableId = Integer.parseInt(orderId);
			if(isPrev) {
				humanReadableId = humanReadableId - 1;
			}
			if(isNext) {
				humanReadableId = humanReadableId + 1;
			}
			txtFieldOrderID.setText(humanReadableId.toString());
			orderDetailQuery.setHumanreadableId(humanReadableId.toString());
			orderItemInfoList = paymentService.retrieveOrderItems(orderDetailQuery);
			if(orderItemInfoList != null) {
				orderItemInfoList.sort(new OrderItemComparator());
			}
			//loadLeftOrderItemList(orderItemInfoList);
			//orderItemsListTable.setModel(new OrderItemListTableModel(orderItemInfoList)); ** WORKS
			((OrderItemListTableModel) orderItemsListTable.getModel()).setOrderItemInfoList(orderItemInfoList);
			((OrderItemListTableModel) orderItemsListTable.getModel()).fireTableDataChanged();
			int numberOfRows = selectedOrderItemsListTable.getRowCount();
			if(numberOfRows > 0) {
				for(int i=numberOfRows; i> 0; i--) {
					Long selectedOrderItemId = Long.parseLong(selectedOrderItemsListTable.getValueAt(numberOfRows-i, 2).toString());
					OrderItemInfo orderItemInfo = getSelectedOrderItem(selectedOrderItemId, true);
					removeItemFromTable(orderItemInfo, selectedOrderItemsListTable);
				}
				((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).fireTableDataChanged();
			}
			subtotalAmountField.setText("0");
			totals = new ArrayList<>();
		}
	}

	public void setLayout(Object object) {
		// TODO Auto-generated method stub

	}

}
