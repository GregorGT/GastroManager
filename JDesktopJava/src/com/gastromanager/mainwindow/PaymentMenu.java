package com.gastromanager.mainwindow;

import com.gastromanager.comparator.OrderItemComparator;
import com.gastromanager.models.Order;
import com.gastromanager.models.OrderDetailQuery;
import com.gastromanager.models.OrderItemInfo;
import com.gastromanager.models.OrderItemTransactionInfo;
import com.gastromanager.service.PaymentService;
import com.gastromanager.service.impl.PaymentServiceImpl;
import com.gastromanager.ui.OrderItemListTableModel;
import com.gastromanager.util.Util;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PaymentMenu  extends Panel{

	private JPanel leftItemListPanel;
	private JPanel selectedItemsListPanel;

	private JTextField txtFieldFloor;
	private JTextField txtFieldTableID;
	private JTextField txtFieldOrderID;
	private JTextField txtFieldTipAmount;
	private PaymentService paymentService;
	private List<Order> orderList;
	private List<OrderItemInfo> orderItemInfoList;
	private List<OrderItemInfo> selectedOrderItemInfoList;
	private JTable orderItemsListTable;
	private JTable selectedOrderItemsListTable;

	public PaymentMenu() {
		paymentService = new PaymentServiceImpl();

		leftItemListPanel = new JPanel();
		leftItemListPanel.setLayout(new BorderLayout());
		leftItemListPanel.setBounds(50, 220, 250, 300);
		orderItemsListTable = new JTable(new OrderItemListTableModel(orderItemInfoList)) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component c = super.prepareRenderer(renderer, row, col);
				Integer payed = (Integer) getValueAt(row, 3);
				System.out.println("Row "+row + " value "+payed);
				if(payed == 1) {
					c.setBackground(Color.GREEN);
				} else {
					c.setBackground(Color.WHITE);
				}
				return c;
			}
		};

		orderItemsListTable.setRowHeight(65);
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

		selectedOrderItemsListTable.setRowHeight(65);
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

	    txtFieldFloor = new JTextField("Floor");
		txtFieldFloor.setBounds(50, 30, 150, 30);
		this.add(txtFieldFloor);

	    txtFieldTableID = new JTextField("Table ID");
		txtFieldTableID.setBounds(50, 100, 150, 30);
		this.add(txtFieldTableID);

		txtFieldOrderID = new JTextField("Display(Order)ID");
		txtFieldOrderID.setBounds(220, 30, 150, 30);
		this.add(txtFieldOrderID);

		//Edit Buttons for the floor
		JButton selectButton1 = new JButton("Select");
		selectButton1.setBounds(50, 135, 150, 30);
		this.add(selectButton1);

		JButton selectButton2 = new JButton("Select");
		selectButton2.setBounds(220, 65, 150, 30);
		selectButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadOrderItems(false, false);
			}
		});
		this.add(selectButton2);


		JButton prevButton = new JButton("<<");
		prevButton.setBounds(50, 60, 70, 30);
		this.add(prevButton);

		JButton nextButton = new JButton(">>");
		nextButton.setBounds(130, 60, 70, 30);
		this.add(nextButton);

		//edit buttons table ID
		JButton prevButton2 = new JButton("<<");
		prevButton2.setBounds(50, 165, 70, 30);
		this.add(prevButton2);

		JButton nextButton2 = new JButton(">>");
		nextButton2.setBounds(130, 165, 70, 30);

		//Display(order)ID edit buttons
		JButton prevButton3 = new JButton("<<");
		prevButton3.setBounds(220, 95, 70, 30);
		prevButton3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadOrderItems(true, false);
			}
		});
		this.add(prevButton3);

		JButton nextButton3 = new JButton(">>");
		nextButton3.setBounds(300, 95, 70, 30);
		nextButton3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadOrderItems(false, true);
			}
		});
		this.add(nextButton3);
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

		JButton removeButton = new JButton("Remove");
		removeButton.setBounds(305, 345, 80, 30);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("table rows selected "+selectedOrderItemsListTable.getSelectedRows());
				processOperation(true);
				/*int[] selectedRows = selectedOrderItemsListTable.getSelectedRows();
				for(int i=0; i<selectedRows.length ; i++) {
					Long selectedOrderItemId = Long.parseLong(selectedOrderItemsListTable.getValueAt(i, 2).toString());
					System.out.println("selected item id "+selectedOrderItemsListTable.getValueAt(i, 2));
					//loadSelectedOrderItemTable(selectedOrderItemId);
					processOperation(true);
				}*/
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
		this.add(undoButton);

		JButton payedButton = new JButton("Payed");
		payedButton.setBounds(480, 530, 80, 30);
		payedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OrderItemTransactionInfo orderItemTransactionInfo = null;
				List<OrderItemInfo> orderItemInfoList = null;
				orderItemInfoList = ((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).getOrderItemInfoList();
				orderItemTransactionInfo = new OrderItemTransactionInfo();
				orderItemTransactionInfo.setOrderItemInfo(orderItemInfoList);
				orderItemTransactionInfo.setAddTransaction(true);
				paymentService.processTransactionInfo(orderItemTransactionInfo);
				((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).getOrderItemInfoList().removeAll(orderItemInfoList);
				((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).fireTableDataChanged();
			}
		});
		this.add(payedButton);


		txtFieldTipAmount = new JTextField("Tip Amount");
		txtFieldTipAmount.setBounds(420, 30, 150, 30);
		this.add(txtFieldOrderID);
		JButton addTipButton = new JButton("Add Tip");
		addTipButton.setBounds(420, 60, 150, 30);
		addTipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Util.isNumeric(txtFieldTipAmount.getText())) {
					((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).addTip(Double.parseDouble(txtFieldTipAmount.getText()));
					((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).fireTableDataChanged();
				}
			}
		});

		JButton calcTotalButton = new JButton("Calculate Total");
		calcTotalButton.setBounds(420, 90, 150, 30);
		calcTotalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).addTotal();
				((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).fireTableDataChanged();
			}
		});

		this.add(txtFieldTipAmount);
		this.add(addTipButton);
		this.add(calcTotalButton);

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
			for(int i=0 ; i<selectedRowIndexes.length; i++) {
				Long selectedOrderItemId = Long.parseLong(leftTable.getValueAt(selectedRowIndexes[i], 2).toString());
				OrderItemInfo orderItemInfo = getSelectedOrderItem(selectedOrderItemId, isRemove);
				addItemToTable(orderItemInfo, rightTable);
				removeItemFromTable(orderItemInfo, leftTable);
			}
		} else {
			int[] selectedRowIndexes = rightTable.getSelectedRows();
			for(int i=0 ; i<selectedRowIndexes.length; i++) {
				Long selectedOrderItemId = Long.parseLong(rightTable.getValueAt(selectedRowIndexes[i], 2).toString());
				OrderItemInfo orderItemInfo = getSelectedOrderItem(selectedOrderItemId, isRemove);
				addItemToTable(orderItemInfo, leftTable);
				removeItemFromTable(orderItemInfo, rightTable);
			}
		}

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
		}
	}

	public void setLayout(Object object) {
		// TODO Auto-generated method stub

	}

}
