package com.gastromanager.models.xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="option")
@XmlAccessorType(XmlAccessType.FIELD)
public class Option
{
    @XmlAttribute
    private String price;

    @XmlAttribute(name="overwrite_price")
    private String overwritePrice;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String id;

    @XmlElement
    private Choice choice;

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getOverwritePrice() {
        return overwritePrice;
    }

    public void setOverwritePrice(String overwrite_price) {
        this.overwritePrice = overwrite_price;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Choice getChoice ()
    {
        return choice;
    }

    public void setChoice (Choice choice)
    {
        this.choice = choice;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [price = "+price+", name = "+name+", id = "+id+", choice = "+choice+"]";
    }
}