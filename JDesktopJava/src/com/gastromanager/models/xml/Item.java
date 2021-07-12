package com.gastromanager.models.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String menu_id;

    @XmlAttribute
    private String price;

    @XmlElement
    private Option option;

    private List<Item> item;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getMenu_id ()
    {
        return menu_id;
    }

    public void setMenu_id (String menu_id)
    {
        this.menu_id = menu_id;
    }

    public Option getOption ()
    {
        return option;
    }

    public void setOption (Option option)
    {
        this.option = option;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", menu_id = "+menu_id+", option = "+option+"]";
    }
}
