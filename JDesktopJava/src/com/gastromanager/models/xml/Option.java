/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


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