package com.gastromanager.mainwindow;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.gastromanager.models.*;
import com.gastromanager.service.impl.OrderServiceImpl;
import com.gastromanager.util.DbUtil;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import com.gastromanager.db.DbConnection;
import com.gastromanager.service.impl.MenuServiceImpl;
import com.gastromanager.util.Util;
import org.xml.sax.SAXException;

public class OrderingMenu extends JPanel {

	private JTextField txtFieldTable;
	private JTextField txtFieldFloor;
	private JTextField txtFieldOrderID;
	private JTextField tfMenuID;
	private JList<OrderItemInfo> list;
	private JComboBox<String> ddChoice;
	private DefaultListModel<OrderItemInfo> listModel = new DefaultListModel<>();
	private Connection connection;
	private JScrollPane scrollPaneSubItems;
	private MenuServiceImpl menuService;
	private MenuDetail menuDetail;
	private JScrollPane scrollPaneItems;
	private JPanel subItemsPanel;
	private ItemOptionComboBox comboBox;
	private List<ItemOptionComboBox> itemOptions;
	private List<ItemButton> itemButtons;
	private HashMap<String, SelectedOrderItem> menuIdMap = new HashMap<>();
	private Map<String, SelectedOrderItem> menuUuidMap = new HashMap<>();
	private MainWindow mainWindow;
	private SelectedOrderItem selectedOrderItem = new SelectedOrderItem();

	public OrderingMenu(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		recursiveFunctionReadingTheXml();

		try {
			connection = DbConnection.getDbConnection().gastroDbConnection;
		} catch (Exception e) {
			System.err.println("Failed to connect to database.\nClass: OrderingMenu.java");
		}

		JLabel lblTable = new JLabel("Table: ");
		lblTable.setBounds(20, 11, 56, 14);
		this.add(lblTable);

		txtFieldTable = new JTextField();
		txtFieldTable.setBounds(20, 24, 60, 20);
		this.add(txtFieldTable);
		txtFieldTable.setColumns(10);

		JLabel lblFloor= new JLabel("Floor: ");
		lblFloor.setBounds(90, 11, 46, 14);
		this.add(lblFloor);

		txtFieldFloor = new JTextField();
		txtFieldFloor.setBounds(90, 24, 60, 20);
		this.add(txtFieldFloor);
		txtFieldFloor.setColumns(10);

		list = new JList<>(listModel);
		list.setCellRenderer(new ListItemStyler());

		JScrollPane sPane = new JScrollPane(list);
		sPane.setBounds(10, 55, 236, 276);
		this.add(sPane);

		JLabel lblOrderID = new JLabel("Order ID");
		lblOrderID.setBounds(256, 56, 68, 14);
		this.add(lblOrderID);

		txtFieldOrderID = new JTextField();
		txtFieldOrderID.setBounds(256, 76, 109, 20);

		this.add(txtFieldOrderID);
		txtFieldOrderID.setColumns(10);

		txtFieldOrderID.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtFieldFloor.getText().trim().isEmpty() || txtFieldFloor.getText().trim() == null ||
					txtFieldTable.getText().trim().isEmpty() || txtFieldTable.getText().trim() == null) {
					JOptionPane.showMessageDialog(OrderingMenu.this, "Enter floor id and table id first.");
					txtFieldOrderID.setFocusable(false);
					txtFieldOrderID.setFocusable(true);
				} else {
					try {
						txtFieldOrderID.setText(String.valueOf(DbUtil.getStartingHumanReadableOrderId(Integer.parseInt(txtFieldFloor.getText().trim()), Integer.parseInt(txtFieldTable.getText().trim()))));
					} catch (NumberFormatException numberFormatException) {
						JOptionPane.showMessageDialog(OrderingMenu.this, "Enter valid values for floor id and table id please.");
					}
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("focus lost");
			}
		});

		JButton lblSelectOrderID = new JButton("Select Order ID");
		lblSelectOrderID.setBounds(256, 101, 109, 23);
		lblSelectOrderID.addActionListener(selectOrderActionListener());

		this.add(lblSelectOrderID);

		JButton btnPrevious = new JButton("<-");
		btnPrevious.setBounds(256, 135, 50, 23);
		btnPrevious.addActionListener(e -> {
			String inputOrderId = (txtFieldOrderID.getText() != null) ?
					txtFieldOrderID.getText().toString() : null;
			if ( Util.isNumeric(inputOrderId) ) {
				Integer currOrderId = Integer.valueOf(inputOrderId);
				currOrderId = (currOrderId - 1 < 0) ? 0 : currOrderId - 1;
				txtFieldOrderID.setText(String.valueOf(currOrderId));
				inputOrderId = (txtFieldOrderID.getText() != null) ?
						txtFieldOrderID.getText().toString() : null;
			}
			buildAndSendOrderQuery(inputOrderId);
		});
		this.add(btnPrevious);

		JButton btnNext = new JButton("->");
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String inputOrderId = (txtFieldOrderID.getText() != null) ? txtFieldOrderID.getText() : null;
				if (Util.isNumeric(inputOrderId)) {
					Integer currOrderId = Integer.valueOf(inputOrderId);
					txtFieldOrderID.setText(String.valueOf(currOrderId + 1));
					inputOrderId = (txtFieldOrderID.getText() != null) ? txtFieldOrderID.getText() : null;
				}

				buildAndSendOrderQuery(inputOrderId);
			}
		});
		btnNext.setBounds(315, 135, 50, 23);
		this.add(btnNext);

		JButton btnNewOrderID = new JButton("New Order ID");
		btnNewOrderID.setBounds(256, 169, 111, 50);
		btnNewOrderID.addActionListener(e -> {
			if (!listModel.isEmpty()) {
				listModel.removeAllElements();
			}
			txtFieldOrderID.setText(String.valueOf(DbUtil.getNewHumanReadableOrderId()));
//			getAndSetNextOrderId();
		});
		this.add(btnNewOrderID);

		JButton addToOrder = new JButton("Add to order");
		addToOrder.setBounds(256, 229, 111, 50);
		addToOrder.addActionListener(event -> {
			if (!readyForOrder())
				return;
			submitLastOrder();
			showToList();
			subItemsPanel.removeAll();
			subItemsPanel.repaint();
			itemButtons.forEach(ItemButton::unselect);
		});
		this.add(addToOrder);

		JLabel lblDrillDownOpts = new JLabel("Drill Down Options: ");
		lblDrillDownOpts.setBounds(406, 27, 120, 14);
		this.add(lblDrillDownOpts);

		menuService = new MenuServiceImpl();
		menuDetail = menuService.loadMenu();

		ddChoice = new JComboBox<>();
		ddChoice.setBounds(406, 50, 120, 20);
		List<DrillDownMenuType> ddMenus = menuDetail.getDrillDownMenus().getDrillDownMenuTypes();
		ddMenus.forEach(i -> ddChoice.addItem(i.getName()));
		this.add(ddChoice);

		tfMenuID = new JTextField();
		tfMenuID.setBounds(406, 102, 120, 20);
		this.add(tfMenuID);
		tfMenuID.setColumns(10);

		JLabel lblMenuID = new JLabel("Menu ID:");
		lblMenuID.setBounds(406, 79, 70, 14);
		this.add(lblMenuID);

		JButton btnSelectMenuID = new JButton("Select Menu ID");
		btnSelectMenuID.addActionListener(event -> {
			if (!readyForOrder()) {
				return;
			}
			makeOrderFromMenuId(tfMenuID.getText().trim());
			showToList();
		});
		btnSelectMenuID.setBounds(406, 135, 120, 23);
		this.add(btnSelectMenuID);

		JButton btnSignOff = new JButton("Sign off");
		btnSignOff.setBounds(406, 165, 120, 23);
		this.add(btnSignOff);
		btnSignOff.addActionListener(e -> {
			String inputOrderId = (txtFieldOrderID.getText() != null) ? txtFieldOrderID.getText().toString() : null;
			if(inputOrderId != null) {
				OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
				orderDetailQuery.setHumanreadableId(inputOrderId);
				if (txtFieldFloor.getText() != null) {
					orderDetailQuery.setFloorId(txtFieldFloor.getText().toString());
				}
				if (txtFieldTable.getText() != null) {
					orderDetailQuery.setTableId(txtFieldTable.getText().toString());
				}
				SignOffOrderInfo signOffOrderInfo = new SignOffOrderInfo();
				signOffOrderInfo.setOrderDetailQuery(orderDetailQuery);
				OrderServiceImpl orderService = new OrderServiceImpl();
				boolean response = orderService.signOffOrder(signOffOrderInfo);
				System.out.println("isOrderPrinted " + response);
				buildAndSendOrderQuery(txtFieldOrderID.getText());
			} else {
				JOptionPane.showMessageDialog(this, "Order Id not provided !!");
			}
		});

		scrollPaneItems = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneItems.setBounds(10, 340, 300, 300);
		this.add(scrollPaneItems);

		scrollPaneSubItems = new JScrollPane();
		scrollPaneSubItems.setBounds(getWidth() / 2 + 20, 340, getWidth() / 2, 300);
		this.add(scrollPaneSubItems);

		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				scrollPaneItems.setBounds(10, 340, getWidth() / 2, getHeight() - 350);
				if (getWidth() < 455) {
					scrollPaneSubItems.setBounds(getWidth() / 2 + 20, 340, getWidth() / 2 - 40, getHeight() - 350);
				} else if (getWidth() < 700) {
					scrollPaneSubItems.setBounds(getWidth() / 2 + 20, 285, getWidth() / 2 - 40, getHeight() - 295);
				} else if (getWidth() < 1035){
					scrollPaneSubItems.setBounds(getWidth() / 2 + 20, 200, getWidth() / 2 - 40, getHeight() - 210);
				} else {
					scrollPaneSubItems.setBounds(getWidth() / 2 + 20, 10, getWidth() / 2 - 40, getHeight() - 20);
				}
				if (itemOptions != null) {
					itemOptions.forEach(i -> {
						if (i != null) {
							i.setMaximumSize(new Dimension(scrollPaneSubItems.getWidth() / 2, 30));
						}
					});
				}
				if (itemButtons != null) {
					itemButtons.forEach(i -> {
						if (i != null) {
							i.setMaximumSize(new Dimension(scrollPaneSubItems.getWidth() / 2, 30));
						}
					});
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		ddChoice.addActionListener( e -> {
			List<DrillDownMenuType> menuTypeList = menuDetail.getDrillDownMenus().getDrillDownMenuTypes();
			for (DrillDownMenuType menuType : menuTypeList) {
				if (ddChoice.getSelectedItem().toString().equalsIgnoreCase(menuType.getName()) ) {
					loadMenuItems(menuType);
					break;
				}
			}
		});
		loadMenuItems(ddMenus.get(0));

		list.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if ( SwingUtilities.isLeftMouseButton(e) ) {
					list.setSelectedIndex(list.locationToIndex(e.getPoint()));


					JPopupMenu menu = new JPopupMenu();
					JMenuItem itemRemove = new JMenuItem("Delete");
					itemRemove.addActionListener( event -> {
						//todo: implement the delete operation
//						System.out.println(list.getSelectedValue().getAttributes().getNamedItem("name").getFirstChild().getNodeValue());
						deleteItem(list.getSelectedValue());
						buildAndSendOrderQuery(txtFieldOrderID.getText());
					});
					menu.add(itemRemove);
					menu.show(list, e.getPoint().x, e.getPoint().y);
				}
			}
		});
	}

	private void deleteItem(OrderItemInfo item) {
		if (txtFieldOrderID.getText().isEmpty() || txtFieldOrderID.getText().trim().isEmpty() || txtFieldOrderID.getText() == null) {
			return;
		}
		if (item.getPrintStatus() == 1) {
			JOptionPane.showMessageDialog(this, "Cannot delete a printed item");
			return;
		}
		OrderServiceImpl orderService = new OrderServiceImpl();
		orderService.removeOrderItem(item);
	}

	private void loadMenuItems(DrillDownMenuType menuType) {
		List<DrillDownMenuButton> buttons = menuType.getButtons();
		DrillDownGroup drillDownGroup = new DrillDownGroup(Integer.parseInt(menuType.getWidth()), Integer.parseInt(menuType.getHeight()), menuType.getName());

		itemButtons = new ArrayList<>();

		for (DrillDownMenuButton btn: buttons) {
			ItemButton newButton = new ItemButton();
			newButton.setName(btn.getName());
			newButton.setText(btn.getName());
			newButton.setLocation(new Point(Integer.parseInt(btn.getxPosition()), Integer.parseInt(btn.getyPosition())));
			newButton.setSize(new Dimension(Integer.parseInt(btn.getWidth()), Integer.parseInt(btn.getHeight())));
			newButton.addActionListener(itemSelectedLeftHandSideMenu());
			newButton.setMainItem(true);
			newButton.setTarget(btn.getTarget());
			drillDownGroup.add(newButton);
			itemButtons.add(newButton);
		}

		drillDownGroup.repaint();
		drillDownGroup.revalidate();
		scrollPaneItems.setViewportView(drillDownGroup);
		scrollPaneItems.repaint();
		scrollPaneItems.revalidate();
	}

	private void showMenu(String item) {
		Map<String, DrillDownMenuItemDetail> itemMap = menuDetail.getMenu().getItemMap();
		itemMap.forEach((key, value) -> {
			if (value.getMenuItemName().equalsIgnoreCase(item)) {
				showOptions(value, item);
				showSubItems(value);
			}
		});
	}

	private void showOptions(DrillDownMenuItemDetail item, String name) {
		Map<String, DrillDownMenuItemOptionDetail> optionsMap = item.getOptionsMap();
		if (optionsMap == null)	{
			comboBox = null;
			return;
		}

		comboBox = new ItemOptionComboBox();
		comboBox.addItem("-----(" + name + ")-----");
		comboBox.setName(name);
		optionsMap.forEach( (key, value) -> {
			comboBox.addItem(value.getName());
			comboBox.addPair(value.getName(), value.getUuid());
		});
		comboBox.setAlignmentX(0.5f);
		comboBox.setMaximumSize(new Dimension(scrollPaneSubItems.getWidth() / 2, 30));
		itemOptions.add(comboBox);
		comboBox.addActionListener(l -> {
			ItemOptionComboBox iocb = (ItemOptionComboBox) l.getSource();
			if (iocb.getSelectedItem().equals("-----(" + name + ")-----")) {
				return;
			}
			selectedOrderItem = menuUuidMap.get(iocb.getNameUuid().get(iocb.getSelectedItem()));
		});
		subItemsPanel.add(comboBox);
		scrollPaneSubItems.setViewportView(subItemsPanel);
	}

	private void showSubItems(DrillDownMenuItemDetail item) {
		List<DrillDownMenuItemDetail> subItems = item.getSubItems();
		if (subItems == null) return;
		subItems.forEach(i -> {
			ItemButton btn = new ItemButton(i.getMenuItemName());
			btn.setName(i.getMenuItemName());
			btn.addActionListener(itemSelectedRightHandSideMenu());
			btn.setMaximumSize(new Dimension(scrollPaneSubItems.getWidth() / 2, 30));
			btn.setAlignmentX(0.5f);
			btn.setMainItem(false);
			btn.setTarget(i.getUuid());
			subItemsPanel.add(btn);
			itemButtons.add(btn);
			if (i.getOptionsMap() != null) {
				btn.setAssociatedOptions(comboBox);
				for (Map.Entry<String, DrillDownMenuItemOptionDetail> v : i.getOptionsMap().entrySet()) {
					btn.setOptionId(v.getValue().getId());
					break;
				}
			}
		});
		subItemsPanel.repaint();
		subItemsPanel.revalidate();
		scrollPaneSubItems.setViewportView(subItemsPanel);
		scrollPaneSubItems.repaint();
		scrollPaneSubItems.revalidate();
	}

	private ActionListener itemSelectedLeftHandSideMenu() {
		return e -> {
			subItemsPanel = new JPanel();
			itemOptions = new ArrayList<>();
			BoxLayout boxLayout = new BoxLayout(subItemsPanel, BoxLayout.Y_AXIS);
			subItemsPanel.setLayout(boxLayout);
			itemButtons.forEach(ItemButton::unselect);
			ItemButton b = (ItemButton) e.getSource();
			b.click();
			showMenu(b.getName());
		};
	}

	private ActionListener itemSelectedRightHandSideMenu() {
		return event -> {
			ItemButton btn = (ItemButton) event.getSource();
			btn.click();
//			s.setItemName(btn.getName());
			if (btn.isPressedOnce()) {
				return;
			}
			showMenu(btn.getName());
			btn.setPressedOnce(true);
			selectedOrderItem= menuUuidMap.get(btn.getTarget());
		};
	}

	private void submitLastOrder() {
		OrderServiceImpl orderService = new OrderServiceImpl();
		orderService.setMenuDetail(menuDetail);
		selectedOrderItem.setOrderId(txtFieldOrderID.getText().trim());
		selectedOrderItem.setFloorId(txtFieldFloor.getText().trim());
		selectedOrderItem.setTableId(txtFieldTable.getText().trim());
		orderService.addOrderItem(selectedOrderItem);
	}

	private boolean readyForOrder() {
		return txtFieldOrderID != null && !txtFieldOrderID.getText().trim().isEmpty() &&
				txtFieldTable != null && !txtFieldTable.getText().trim().isEmpty() &&
				txtFieldFloor != null && !txtFieldFloor.getText().trim().isEmpty();
	}

	private void makeOrderFromMenuId(String menuId) {
		if (menuId == null || menuId.isEmpty() || menuIdMap == null || menuIdMap.size() == 0) {
			return;
		}
		for (Map.Entry<String, SelectedOrderItem> i : menuIdMap.entrySet()) {
			if (i.getKey().equals(menuId)) {

				if (!checkIfItemIsReadyForSelection(i.getValue())) {
					OrderServiceImpl orderService = new OrderServiceImpl();
					orderService.setMenuDetail(menuDetail);
					i.getValue().setOrderId(txtFieldOrderID.getText().trim());
					i.getValue().setFloorId(txtFieldFloor.getText().trim());
					i.getValue().setTableId(txtFieldTable.getText().trim());
					orderService.addOrderItem(i.getValue());
					return;
				} else {
					System.out.println("Not ready for selection");
					subItemsPanel = new JPanel();
					itemOptions = new ArrayList<>();
					BoxLayout boxLayout = new BoxLayout(subItemsPanel, BoxLayout.Y_AXIS);
					subItemsPanel.setLayout(boxLayout);
					itemButtons.forEach(ItemButton::unselect);
					itemButtons.forEach(b -> {
						if (b.getName().equals(i.getValue().getItemName())) {
							b.click();
							showMenu(b.getName());
						}
					});
				}
			}
		}
	}

	private Boolean checkIfItemIsReadyForSelection(SelectedOrderItem s) {
		return (s.getSubItems() != null && s.getSubItems().size() == 1 && s.getSubItems().get(0).getAllOptions() == null);
	}

	private void showToList() {
		String inputOrderId = (txtFieldOrderID.getText() != null) ? txtFieldOrderID.getText().toString() : null;
		buildAndSendOrderQuery(inputOrderId);
	}

	private ActionListener selectOrderActionListener() {
		return e -> {
			showToList();
		};
	}

	private void buildAndSendOrderQuery(String inputOrderId) {
		listModel.removeAllElements();
		if(inputOrderId != null) {
			System.out.println("get details for order id " + inputOrderId);
			OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
			orderDetailQuery.setHumanreadableId(inputOrderId);
			if (txtFieldFloor.getText() != null) {
				orderDetailQuery.setFloorId(txtFieldFloor.getText());
			}
			if (txtFieldTable.getText() != null) {
				orderDetailQuery.setTableId(txtFieldTable.getText());
			}
			if (inputOrderId != null && !inputOrderId.isEmpty()) {
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
					orderDetailQuery.setHumanreadableId(inputOrderId);

					OrderServiceImpl orderService = new OrderServiceImpl();
					List<OrderItemInfo> fetchedItems = orderService.retrieveOrderItems(orderDetailQuery);
					if (fetchedItems != null) {
						fetchedItems.forEach(i -> {
							System.out.println(i.getQuantity());
							System.out.println((i.getXmlText()));
							listModel.addElement(i);
						});
					}
//					makeQuery(inputOrderId);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("Order details could not be fetched");
				}
			}
		} else {
			System.out.println("Order Id is required");
		}
	}

	private void parseXmlFromQuery(List<Map.Entry<String, Integer>> queryResults) {
		queryResults.forEach((item) -> {
			Document doc = convertStringToDocument(item.getKey());
			for (int i = 0; i < item.getValue(); ++i) {
				documentCons(doc.getFirstChild());
			}
		});
	}

	private static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void documentCons(Node node) {
		if (node.getNodeName().contains("#"))
			return;
//		listModel.addElement(node);
		while (node.getNextSibling() != null) {
			documentCons(node.getNextSibling());
		}
	}

	private void recursiveFunctionReadingTheXml() {
		String file = "C:\\Users\\Admin\\IdeaProjects\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml";
//		String file = "/home/panagiotis/repos/GastroManager/JDesktopJava/data/sample_tempalte.xml";

		final DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		final DocumentBuilder docBuilder;
		final Document doc;
		try {
			docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.parse(file);
			buildMenuIdMap(doc, doc.getDocumentElement());
			buildMenuUuidMap(doc, doc.getDocumentElement());
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}
	}

	/*Builds the menu id items and stores them in a hashmap*/
	private void buildMenuIdMap(final Document doc, final Element e) {
		final NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				if (n.getAttributes().getNamedItem("menu_id") != null) {
					SelectedOrderItem s = new SelectedOrderItem();

					Node cur = n;
					Stack<Node> stack = new Stack<>();
					stack.push(cur);
					while (!cur.getNodeName().equals("menues")) {
						cur = cur.getParentNode();
						stack.push(cur);
					}

					if (stack.size() == 0) {
						continue;
					}

					stack.pop();
					Node nodeItem = stack.pop();
					s.setItemName(nodeItem.getAttributes().getNamedItem("name").getNodeValue());

					if (nodeItem.getAttributes().getNamedItem("uuid") != null) {
						s.setTarget(nodeItem.getAttributes().getNamedItem("uuid").getNodeValue());
					}

					Map<String, SelectedOrderItemOption> allOptions;

					SelectedOrderItem tempItem = s;
					while (!stack.isEmpty()) {
						SelectedOrderItem s2 = new SelectedOrderItem();
						List<SelectedOrderItem> selectedOrderItemList = new ArrayList<>();
						nodeItem = stack.pop();
						s2.setItemName(nodeItem.getAttributes().getNamedItem("name").getNodeValue());
						if (nodeItem.getAttributes().getNamedItem("uuid") != null) {
							s2.setTarget(nodeItem.getAttributes().getNamedItem("uuid").getNodeValue());
						}
						selectedOrderItemList.add(s2);
						if (nodeItem.getNodeName().equals("option")) {
							SelectedOrderItemOption selectedOrderItemOption = new SelectedOrderItemOption();
							selectedOrderItemOption.setId(nodeItem.getAttributes().getNamedItem("id").getNodeValue());
							selectedOrderItemOption.setName(nodeItem.getAttributes().getNamedItem("name").getNodeValue());
							allOptions = new HashMap<>();
							allOptions.put(nodeItem.getAttributes().getNamedItem("name").getNodeValue(), selectedOrderItemOption);
							tempItem.setOption(selectedOrderItemOption);
							tempItem.setAllOptions(allOptions);
						} else {
							tempItem.setSubItems(selectedOrderItemList);
						}

						tempItem = s2;
					}

					menuIdMap.put(n.getAttributes().getNamedItem("menu_id").getNodeValue(), s);
				}

				buildMenuIdMap(doc, (Element) n);
			}
		}
	}

	/*Builds the uuid items and stores them in a hashmap*/
	private void buildMenuUuidMap(final Document doc, final Element e) {
		final NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
//				if (n.getNodeName().equals("menues")) {
				if (n.getAttributes().getNamedItem("uuid") != null) {
					SelectedOrderItem s = new SelectedOrderItem();

					Node cur = n;
					Stack<Node> stack = new Stack<>();
					stack.push(cur);
					while (!cur.getNodeName().equals("menues")) {
						cur = cur.getParentNode();
						stack.push(cur);
					}

					if (stack.size() == 0) {
						continue;
					}

					stack.pop();
					Node nodeItem = stack.pop();
					s.setItemName(nodeItem.getAttributes().getNamedItem("name").getNodeValue());

					if (nodeItem.getAttributes().getNamedItem("uuid") != null) {
						s.setTarget(nodeItem.getAttributes().getNamedItem("uuid").getNodeValue());
					}

					Map<String, SelectedOrderItemOption> allOptions;

					SelectedOrderItem tempItem = s;
					while (!stack.isEmpty()) {
						SelectedOrderItem s2 = new SelectedOrderItem();
						List<SelectedOrderItem> selectedOrderItemList = new ArrayList<>();
						nodeItem = stack.pop();
						s2.setItemName(nodeItem.getAttributes().getNamedItem("name").getNodeValue());
						if (nodeItem.getAttributes().getNamedItem("uuid") != null) {
							s2.setTarget(nodeItem.getAttributes().getNamedItem("uuid").getNodeValue());
						}
						selectedOrderItemList.add(s2);
						if (nodeItem.getNodeName().equals("option")) {
							SelectedOrderItemOption selectedOrderItemOption = new SelectedOrderItemOption();
							selectedOrderItemOption.setId(nodeItem.getAttributes().getNamedItem("id").getNodeValue());
							selectedOrderItemOption.setName(nodeItem.getAttributes().getNamedItem("name").getNodeValue());
							allOptions = new HashMap<>();
							allOptions.put(nodeItem.getAttributes().getNamedItem("name").getNodeValue(), selectedOrderItemOption);
							tempItem.setOption(selectedOrderItemOption);
							tempItem.setAllOptions(allOptions);
						} else {
							tempItem.setSubItems(selectedOrderItemList);
						}

						tempItem = s2;
					}

					menuUuidMap.put(n.getAttributes().getNamedItem("uuid").getNodeValue(), s);
				}
				buildMenuUuidMap(doc, (Element) n);
//				}
			}
		}
	}

	private class ListItemStyler extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			OrderItemInfo orderItemInfo = (OrderItemInfo) value;
			setText(constructLabelText(orderItemInfo.getXmlText()));
			if (isSelected) {
				setBackground(Color.CYAN);
			} else if (orderItemInfo.getPrintStatus() == 1) {
				setBackground(Color.YELLOW);
			} else {
				setBackground(Color.WHITE);
			}
			return this;
		}

		private String constructLabelText(String text) {
			String labelText = "<html>";
			String[] words = text.replaceAll("^[.,\\s]+", "").split("[.,\\s]+");
			String spaces6 = "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;";
			String spaces1 = "&nbsp;";

			labelText += words[0] + "<br/>" + "          ";
			labelText += spaces6;
			for (int i = 1; i < words.length; ++i) {
				labelText += words[i] + spaces1;
			}
			labelText += "</html>";
			return labelText;
		}
	}
}