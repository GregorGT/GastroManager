package com.gastromanager.service.impl;

import com.gastromanager.models.*;
import com.gastromanager.models.xml.Choice;
import com.gastromanager.models.xml.Item;
import com.gastromanager.models.xml.Option;
import com.gastromanager.print.PrintService;
import com.gastromanager.print.PrintServiceImpl;
import com.gastromanager.service.OrderService;
import com.gastromanager.util.DbUtil;
import com.gastromanager.util.XmlUtil;

import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private MenuDetail menuDetail;

    @Override
    public List<OrderItemInfo> retrieveOrderItems(OrderDetailQuery orderDetailQuery) {
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderDetailQuery, false);
        List<OrderItemInfo> orderItemArrayList =  null;
        if(orderItems != null) {
            orderItemArrayList = new ArrayList<>();
            for(OrderItem orderItem: orderItems) {
                orderItem.setXmlText(XmlUtil.formatOrderText(orderItem));
                OrderItemInfo orderItemInfo = new OrderItemInfo(orderItem);
                orderItemArrayList.add(orderItemInfo);
            }

            System.out.println("order items count "+orderItemArrayList.size());
        } else {
            System.out.println("No order items found");
        }

        return orderItemArrayList;
    }

    @Override
    public void addOrderItem(SelectedOrderItem selectedOrderItem) {
        OrderItem dbOrderItem = null;
        try {
            dbOrderItem = buildOrderItemEntry(selectedOrderItem);
            Integer noOfRowsInserted = DbUtil.insertOrder(dbOrderItem);
            System.out.println((noOfRowsInserted ==1) ? "Order Inserted": " Order not inserted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeOrderItem(OrderItemInfo orderItemInfo) {
        Boolean isItemDeleted = DbUtil.removeOrderItem(orderItemInfo);
        System.out.println("Item removed with id "+orderItemInfo.getItemId() + " is removed "+isItemDeleted);
    }

    @Override
    public Boolean signOffOrder(SignOffOrderInfo signOffOrderInfo) {
        PrintService printService = new PrintServiceImpl();
        return printService.print(signOffOrderInfo.getOrderDetailQuery());
    }

    @Override
    public Integer getStartingHumanReadableId(HumanReadableIdQuery humanReadableIdQuery) {
        return DbUtil.getStartingHumanReadableOrderId(humanReadableIdQuery.getFloorId(),
                humanReadableIdQuery.getTableId());
    }

    private OrderItem buildOrderItemEntry(SelectedOrderItem selectedOrderItem) throws Exception {
        OrderPrice orderPrice = new OrderPrice((double) 0, false);
        OrderItem orderItem = new OrderItem();
        String xmlItemInfo = createXmlItemInfo(selectedOrderItem, orderPrice, orderItem);
        System.out.println("Price choice: "+orderPrice.getChoicePrice() +
                "price: "+orderPrice.getPrice());
        selectedOrderItem.setPrice(
                (orderPrice.getPrice() != null ? orderPrice.getPrice() : (double) 0) +
                        (orderPrice.getChoicePrice() != null ? orderPrice.getChoicePrice() : (double) 0));
        //Order
        Order order = new Order();
        order.setId(Integer.valueOf(selectedOrderItem.getOrderId()));
        //order.setAmount(selectedOrderItem.getPrice()); //TODO need to be sum
        order.setAmount(selectedOrderItem.getPrice());
        order.setDateTime(LocalDateTime.now());
        order.setFloorId(selectedOrderItem.getFloorId());
        order.setTableId(selectedOrderItem.getTableId());
        order.setHumanReadableId(selectedOrderItem.getOrderId());
        order.setDateTime(LocalDateTime.now());


        //Orderitem
        //OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(Integer.valueOf(selectedOrderItem.getOrderId()));
        orderItem.setQuantity(1);
        //orderItem.setPrice(selectedOrderItem.getPrice());
        orderItem.setPrice(selectedOrderItem.getPrice());
        //orderItem.setItemId(0);
        orderItem.setRemark("");
        orderItem.setDateTime(LocalDateTime.now());
        orderItem.setPrintStatus(0);
        orderItem.setPayed(0);
        orderItem.setXmlText(xmlItemInfo);
        orderItem.setStatus(0);
        orderItem.setOrder(order);

        return orderItem;
    }

    private String createXmlItemInfo(SelectedOrderItem item, OrderPrice orderPrice, OrderItem orderItem) {
        String xmlContent = null;
        Menu menu = menuDetail.getMenu();
        Map<String, DrillDownMenuItemDetail> menuMap = menu.getItemMap();
        DrillDownMenuItemDetail menuItemDetail = menuMap.get(item.getTarget());
        Map<String, Double> optionPriceMap = new HashMap<>();
        Map<String, Double> choicePriceMap = new HashMap<>();
        Item xmlMainItem = createItem(item, menuItemDetail, orderPrice, optionPriceMap, choicePriceMap, orderItem);
        StringWriter sw = new StringWriter();
        JAXB.marshal(xmlMainItem, sw);
        String xmlString = sw.toString();
        System.out.println("xml "+xmlString);
        return xmlString;

    }

    private Item createItem(SelectedOrderItem item, DrillDownMenuItemDetail itemDetail,  OrderPrice orderPrice,
                            Map<String, Double> optionPriceMap, Map<String, Double> choicePriceMap, OrderItem orderItem) {
        Item xmlMainItem = null;
        if(itemDetail != null) {
            Map<String, DrillDownMenuItemOptionDetail> itemOptionDetailMap = itemDetail.getOptionsMap();
            xmlMainItem = new Item();
            xmlMainItem.setName(item.getItemName());
            if(itemDetail.getPrice() != null) {
                xmlMainItem.setPrice(itemDetail.getPrice().toString());
            }
            DrillDownMenuItemOptionDetail optionDetail = null;
            SelectedOrderItemOption selectedOrderItemOption = item.getOption();
            if(selectedOrderItemOption != null) {
                Option option = new Option();
                option.setId(selectedOrderItemOption.getId());
                option.setName(selectedOrderItemOption.getName());
                optionDetail = (itemOptionDetailMap == null) ? null : itemOptionDetailMap.get(selectedOrderItemOption.getName());
                if (optionDetail != null) {
                    if (optionDetail.getOverwritePrice()) {
                        option.setOverwritePrice(optionDetail.getPrice().toString());
                        optionPriceMap.put(option.getId(), item.getPrice());
                        orderPrice.setPrice(optionDetail.getPrice());
                        System.out.println(" Price set to in overwrite "+orderPrice.getPrice() );
                        orderPrice.setOverwritePrice(true);
                    } else {
                        option.setPrice(optionDetail.getPrice().toString());
                        //If already added we need to update by subtracting the earlier price
                        Double currentOptionPrice = optionPriceMap.get(option.getId()+option.getName());
                        System.out.println("Existing price "+currentOptionPrice);
                        Double newPrice = (double) 0;
                        if(currentOptionPrice == null) {
                            newPrice = optionDetail.getPrice();
                        } else {
                            newPrice = optionDetail.getPrice() - currentOptionPrice;
                        }
                        //update
                        optionPriceMap.put(option.getId()+option.getName(), optionDetail.getPrice());

                        if(!orderPrice.getOverwritePrice()) {
                            orderPrice.setPrice(orderPrice.getPrice() + newPrice);
                            System.out.println(" Price set to  "+orderPrice.getPrice() );
                        }
                    }
                    DrillDownMenuItemOptionChoiceDetail choiceDetail = optionDetail.getChoice();

                    if (choiceDetail != null) {
                        Choice choice = new Choice();
                        choice.setName(choiceDetail.getName());
                        choice.setPrice(choiceDetail.getPrice().toString());
                        orderPrice.setChoicePrice(choiceDetail.getPrice()); //choiceDetail.getPrice() - optionDetail.getPrice()
                        System.out.println(" Price set to in choice "+orderPrice.getChoicePrice() + " optionPrice "+optionDetail.getPrice() );
                        //item.setPrice(Double.valueOf(item.getPrice() + choiceDetail.getPrice() - optionDetail.getPrice()));
                        option.setChoice(choice);
                        choicePriceMap.put(choiceDetail.getName(), choiceDetail.getPrice());
                    }
                }
                xmlMainItem.setOption(option);
            }
            //sub items
            if(item.getSubItems() != null) {
                List<Item> xmlSubItems = new ArrayList<>();
                for (SelectedOrderItem subItem : item.getSubItems()) {
                    Item item1 = createItem(subItem, itemDetail.getSubItems().stream().filter(menuSubItem ->
                                    menuSubItem.getMenuItemName().equals(subItem.getItemName())
                            //menuSubItem.getUuid().equals(subItem.getTarget())
                    ).findAny().get(), orderPrice, optionPriceMap, choicePriceMap, orderItem);
                    xmlSubItems.add(item1);
                    //totalPrice = totalPrice + totalPrice;
                    //item.setPrice(Double.valueOf(item.getPrice()+ (subItem.getPrice() == null ? Double.valueOf(0):subItem.getPrice())));
                }
                xmlMainItem.setItem(xmlSubItems);
            } else {
                if(optionDetail != null) {
                    orderItem.setItemId(optionDetail.getUuid() == null ? null : Long.valueOf(optionDetail.getUuid()));
                } else {
                    System.out.println(" orderItem "+itemDetail.getMenuItemName()+ " price " +orderPrice.getPrice());
                    if(orderPrice.getPrice() != null && orderPrice.getPrice() == 0.0 && itemDetail.getPrice() != null) {
                        System.out.println("price set from item "+itemDetail.getPrice());
                        orderPrice.setPrice(orderPrice.getPrice() + itemDetail.getPrice());
                    }
                    orderItem.setItemId(itemDetail.getUuid() == null ? null : Long.valueOf(itemDetail.getUuid()));
                }

            }

        }

        return xmlMainItem;
    }

    public MenuDetail getMenuDetail() {
        return menuDetail;
    }

    public void setMenuDetail(MenuDetail menuDetail) {
        this.menuDetail = menuDetail;
    }
}
