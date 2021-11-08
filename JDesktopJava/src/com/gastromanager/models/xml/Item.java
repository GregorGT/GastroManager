/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


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
