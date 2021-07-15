package com.gastromanager.mainwindow;

import com.gastromanager.models.Order;
import com.gastromanager.models.OrderDetailQuery;
import com.gastromanager.models.OrderItemInfo;
import com.gastromanager.service.PaymentService;
import com.gastromanager.service.impl.PaymentServiceImpl;
import com.gastromanager.ui.OrderItemListTableModel;

import javax.swing.*;
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
	private PaymentService paymentService;
	private List<Order> orderList;
	private List<OrderItemInfo> orderItemInfoList;
	private List<OrderItemInfo> selectedOrderItemInfoList;
	private JTable orderItemsListTable;
	private JTable selectedOrderItemsListTable;

	public PaymentMenu() {
		paymentService = new PaymentServiceImpl();

	    //leftItemsList = new JTextArea();
		leftItemListPanel = new JPanel();
		leftItemListPanel.setLayout(new BorderLayout());
		//leftItemsList.setBounds(50, 220, 250, 300);
		leftItemListPanel.setBounds(50, 220, 250, 300);
		//leftItemsList.setLineWrap(true);
		//leftItemsList.setSelectionColor(Color.green);
		//this.add(leftItemsList);
		orderItemsListTable = new JTable(new OrderItemListTableModel(orderItemInfoList));
		orderItemsListTable.setRowHeight(65);
		orderItemsListTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		//hide the item id column
		orderItemsListTable.getColumnModel().getColumn(2).setWidth(0);
		orderItemsListTable.getColumnModel().getColumn(2).setMinWidth(0);
		orderItemsListTable.getColumnModel().getColumn(2).setMaxWidth(0);
		JScrollPane scrollPane = new JScrollPane(orderItemsListTable);
		//leftItemsList.add(scrollPane, BorderLayout.CENTER);
		leftItemListPanel.add(scrollPane, BorderLayout.CENTER);
		leftItemListPanel.setVisible(true);
		this.add(leftItemListPanel);

		selectedItemsListPanel = new JPanel();
		selectedItemsListPanel.setLayout(new BorderLayout());
		selectedItemsListPanel.setBounds(390, 220, 250, 300);
		selectedOrderItemsListTable = new JTable(new OrderItemListTableModel(selectedOrderItemInfoList));
		selectedOrderItemsListTable.setRowHeight(65);
		selectedOrderItemsListTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		//hide the item id column
		selectedOrderItemsListTable.getColumnModel().getColumn(2).setWidth(0);
		selectedOrderItemsListTable.getColumnModel().getColumn(2).setMinWidth(0);
		selectedOrderItemsListTable.getColumnModel().getColumn(2).setMaxWidth(0);
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
				System.out.println("table row selected "+orderItemsListTable.getSelectedRow());
				Long selectedOrderItemId = Long.parseLong(orderItemsListTable.getValueAt(orderItemsListTable.getSelectedRow(), 2).toString());
				System.out.println("selected item id "+orderItemsListTable.getValueAt(orderItemsListTable.getSelectedRow(), 2));
				loadSelectedOrderItemTable(selectedOrderItemId);
			}
		});
		this.add(addButton);

		JButton removButton = new JButton("Remove");
		removButton.setBounds(305, 345, 80, 30);
		this.add(removButton);

		JButton undoButton = new JButton("Undo");
		undoButton.setBounds(135, 530, 80, 30);
		this.add(undoButton);

		JButton payedButton = new JButton("Payed");
		payedButton.setBounds(480, 530, 80, 30);
		this.add(payedButton);

	}

	private void loadSelectedOrderItemTable(Long itemId){
		OrderItemInfo orderItemInfo = getSelectedOrderItem(itemId);
		selectedOrderItemInfoList = ((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).getOrderItemInfoList();
		if(selectedOrderItemInfoList == null) {
			selectedOrderItemInfoList = new ArrayList<>();
			((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).setOrderItemInfoList(selectedOrderItemInfoList);
		}
		selectedOrderItemInfoList.add(orderItemInfo);
		((OrderItemListTableModel) selectedOrderItemsListTable.getModel()).fireTableDataChanged();
		System.out.println("loaded the selected item "+itemId);
	}

	private OrderItemInfo getSelectedOrderItem(Long itemId) {
		return orderItemInfoList.stream().filter(orderItemInfo ->
				orderItemInfo.getItemId().equals(itemId)).findAny().orElse(null);
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
			//loadLeftOrderItemList(orderItemInfoList);
			//orderItemsListTable.setModel(new OrderItemListTableModel(orderItemInfoList)); ** WORKS
			((OrderItemListTableModel) orderItemsListTable.getModel()).setOrderItemInfoList(orderItemInfoList);
			((OrderItemListTableModel) orderItemsListTable.getModel()).fireTableDataChanged();
			System.out.println("show table "+orderItemsListTable.getSelectedRow());
			//orderItemsListTable.setVisible(true);
		}
	}

	public void setLayout(Object object) {
		// TODO Auto-generated method stub

	}

	/*private void loadLeftOrderItemList(List<OrderItemInfo> orderItemInfoList) {
		OrderItemListTableModel model  = new OrderItemListTableModel(orderItemInfoList);
		int rowCount = model.getRowCount();
		String[] headers = model.getColumnNames();
		StringBuilder headerRow = new StringBuilder();
		for(int currColHeaderIndex = 0 ; currColHeaderIndex < headers.length ; currColHeaderIndex++) {
			switch (currColHeaderIndex) {
				case 0: headerRow.append(headers[currColHeaderIndex]);
					break;
				case 1: headerRow.append("\t"+"\t");
					headerRow.append(headers[currColHeaderIndex]);
					break;
			}
		}
		leftItemsList.append(headerRow.toString()+"\n");

		//load data
		for(int rowIndex = 0 ; rowIndex < rowCount ; rowIndex++) {
			leftItemsList.append(model.getValueAt(rowIndex, 0).toString());
			leftItemsList.append(model.getValueAt(rowIndex, 1).toString());
			leftItemsList.append("\n");
		}
		*//*model.addTableModelListener(new TableModelListener() {
										@Override
										public void tableChanged(TableModelEvent e) {
											int rowCount = model.getRowCount();
											String[] headers = model.getColumnNames();
											StringBuilder headerRow = new StringBuilder();
											for(int currColHeaderIndex = 0 ; currColHeaderIndex < headers.length ; currColHeaderIndex++) {
													switch (currColHeaderIndex) {
														case 0: headerRow.append(headers[currColHeaderIndex]);
														        break;
														case 1: headerRow.append("\t"+"\t");
														        headerRow.append(headers[currColHeaderIndex]);
														        break;
													}
											}
											leftItemsList.append(headerRow.toString()+"\n");

											//load data
											for(int rowIndex = 0 ; rowIndex < rowCount ; rowIndex++) {
												leftItemsList.append(model.getValueAt(rowIndex, 0).toString());
												leftItemsList.append(model.getValueAt(rowIndex, 1).toString());
												leftItemsList.append("\n");
											}
										}
									}

		);*//*
		*//*orderItemsListTable = new JTable(model);
		this.add(orderItemsListTable);*//*
	}*/

}
