#include "cTreeNode.hpp"
#include "cPropertyNode.hpp"



std::unordered_map<long, std::weak_ptr<cTreeNode> > g_nodemap;


long generateUUID()
{
    long r = 0;
    for (int i = 0; i < sizeof(long)/sizeof(int); i++)
    {
        r = r << (sizeof(int) * CHAR_BITS);
        r |= rand();
    }

return r;

}

long generateNodeUUID()
{
    int tries = 3;
    long result = -1;
    while(tries >= 0)
    {
        long rand = generateUUID();

        if(g_nodemap.find(rand) == g_nodemap.end())
            return rand;

        --tries;
    }

    return result;
}

cTreeNode::cTreeNode()
{
}

cTreeNode::~cTreeNode()
{
}

int cTreeNode::getChildID()
{

    int result = 0;
    auto parent = m_parent.lock();
    if(!parent)
    {
    //    result = -1;
        return -1;
    }

    for(auto iter = parent->m_children.begin(); iter != parent->m_children.end();++iter)
    {
        if((size_t)iter->get() == (size_t)this)
            return result;

        ++result;
    }

    return -1;
}

void cTreeNode::printToXMLString(std::string &xmlstring)
{
    xmlstring += "<";
    xmlstring += m_xmlname;
    xmlstring += " ";

    for(auto iter = m_attributes.begin() ; iter != m_attributes.end(); ++iter)
    {
     //   std::string tmp ( (*iter).first().c_str() );
     //   xmlstring += tmp;
        xmlstring += "=";
        xmlstring += "\"";
      //  tmp = iter->second();
      //  xmlstring += tmp;
        xmlstring += "\" ";
    }

    if(m_children.size()==0)
        xmlstring += "/>";
    else
        xmlstring += ">";

    for(auto child = m_children.begin(); child !=m_children.begin(); ++child)
    {
        (*child)->printToXMLString(xmlstring);
    }

    if(m_children.size()!=0){
        xmlstring += "</";
        xmlstring += m_xmlname;
        xmlstring += ">";
    }

}

bool cTreeNode::setChildXMLValue(std::string &xmlname, std::string &value, bool create)
{
    std::shared_ptr<cTreeNode> xmlnode;

    if(m_childxmlmap.find(xmlname) != m_childxmlmap.end())
    {
        xmlnode = m_childxmlmap[xmlname].lock();
    }

    if(!xmlnode)
        for(auto iter = m_children.begin(); iter != m_children.end();++iter)
        {
            auto pnode = std::dynamic_pointer_cast<cTreeNode>(*iter);
            if(pnode->m_xmlname == xmlname)
            {
                xmlnode = pnode;
                m_childxmlmap[xmlname] = pnode;
                break;
            }
        }

        if(!xmlnode)
        {
            xmlnode = std::shared_ptr<cTreeNode>( new cTreeNode );
            xmlnode->m_this = xmlnode;
            xmlnode->m_xmlname = xmlname;
            xmlnode->m_value = value;
            m_childxmlmap[xmlname] = xmlnode;
            if(!getTranslation(xmlname,xmlnode->m_name))
                xmlnode->m_name = xmlname;

            auto dc = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(xmlnode);
            appendExistingChild(dc, xmlname);
        }else
        {
            xmlnode->m_value = value;
        }

        xmlnode->updateDisplay();

        return true;
}

bool cTreeNode::getChildXMLValue(std::string &xmlname, std::string &value)
{
    std::shared_ptr<cTreeNode> xmlnode;

    if(m_childxmlmap.find(xmlname) != m_childxmlmap.end())
    {
        xmlnode = m_childxmlmap[xmlname].lock();
    }

    if(!xmlnode)
        for(auto iter = m_children.begin(); iter != m_children.end();++iter)
        {
            auto cp = std::dynamic_pointer_cast<cTreeNode>(*iter);
            if(cp->m_xmlname == xmlname)
            {
                xmlnode = cp;
                m_childxmlmap[xmlname] = cp;
                break;
            }
        }

    if(xmlnode)
    {
        value = xmlnode->m_value;
        return true;
    }

    return false;
}

/// returns the translated name
bool cTreeNode::getTranslatedName(std::string &result)
{
	if(g_settingsmap.find(SETTING_LANGUAGE) != g_settingsmap.end())
	{
		auto language = g_settingsmap[SETTING_LANGUAGE];
		std::string name = TRANSLATED_DISPLAYNAME;
		name += language;

		if(m_attributes.find(name) != m_attributes.end())
		{
			result = m_attributes[name];
			return true;
		}
	}

	return false;
}

/// returns the entire translation
bool cTreeNode::getTranslatedTagName(std::string &result)
{
	if(m_xmlname.size() == 0)
		return false;

	//if(g_settingsmap.find(SETTING_LANGUAGE) != g_settingsmap.end())
	{
	//	auto language = g_settingsmap[SETTING_LANGUAGE];

		if(g_translationmap.find(m_xmlname) != g_translationmap.end())
		{
			result = g_translationmap[m_xmlname];
			return true;

		}

	}

	return false;
}

/// returns the entire translation
bool cTreeNode::getDisplayString(std::string &result)
{
	std::string name;
	getTranslatedName(name);

	if(name.size() == 0)
		getTranslatedTagName(name);

	if(result.size() == 0)
    {
    	result = m_xmlname;
        getTranslation(result, result);
    }

    if(m_value.size() > 0)
    {

        if(result.size() > 0)
            result += " = ";

        result += m_value;
    }

	if(result.size() == 0)
		result = "empty";

	return true;
}

bool cTreeNode::getProperty(std::string &name, std::shared_ptr<cTreeNode> &result)
{
		if(m_property.find(name) == m_property.end())
			return false;

		auto iter = m_property[name].lock();

		if(!iter)
			return false;

		result = std::dynamic_pointer_cast<cTreeNode>(iter);
		return true;
}

bool cTreeNode::getPropertyName(std::string &name, std::string &result)
{
	std::shared_ptr<cTreeNode> node;
	if(getProperty(name, node))
	{
		return node->getTranslatedName(result);
	}
	return false;
}

bool cTreeNode::getPropertyValue(std::string &name, std::string &value)
{
	std::shared_ptr<cTreeNode> node;
	if(getProperty(name, node))
	{
		value = node->m_value;
		return true;
	}
	return false;
}

void cTreeNode::iterateTroughChildrenSetIDs()
{

    auto iter = m_children.begin();
    int counter = 0;

}

void cTreeNode::setNewProperty(std::string &name, std::string &value)
{

	std::shared_ptr<cPropertyNode> prop = std::shared_ptr<cPropertyNode>(new cPropertyNode);
	prop->m_name = name;
	prop->m_value = value;

	m_property[name] = prop;

	auto node = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(prop);
	std::string ds;
	prop->getDisplayString(ds);
	appendExistingChild(node, ds);

}

void cTreeNode::updateDisplay()
{

    std::string dspl;
    if(getDisplayString(dspl))
    {
        wxString wxdspl(dspl);
        if(isActive())
        {
            m_treeCtrl->SetItemText( m_itemID, wxdspl );
            m_treeCtrl->SetItemBold( m_itemID, true);

        }else
        {
            m_treeCtrl->SetItemText( m_itemID, wxdspl );
            m_treeCtrl->SetItemBold( m_itemID, false);
        }
    }
}

void cTreeNode::OnInit()
{
    std::string atrn = NODE_ATTRIBUTE_UUID;
    if(m_attributes.find(atrn) != m_attributes.end())
    {
        m_uuid = atol(m_attributes[atrn].c_str());
    }
    else
    {
        m_uuid = generateNodeUUID();
    }

    auto tptr = m_this.lock();
    if(tptr)
    {
        auto cptr = std::dynamic_pointer_cast<cTreeNode>(tptr);
        g_nodemap[m_uuid] = cptr;
    }

}

void cTreeNode::setProperty(std::string &name, std::string &value)
{

	std::shared_ptr<cTreeNode> prop; // = std::shared_ptr<cPropertyNode>(new cPropertyNode);

	if(!getProperty(name, prop))
        return;

	//prop->m_name = name;
	//prop->m_value = value;

	m_property[name] = prop;
    prop->m_value = value;

	auto node = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(prop);
	std::string ds;
	prop->getDisplayString(ds);
	prop->updateDisplay();
	//appendExistingChild(node, ds);

}


void cTreeNode::Activate(bool force)
{

    if(!(m_flags & IS_ACTIVATALBE) && !force)
        return;

	auto parent = m_parent.lock();

	if(!parent)
		return;

	auto rparent = std::dynamic_pointer_cast<cTreeNode>(parent);
    if(!rparent)
        return;

	auto ac = rparent->m_activeChild.lock();
	if(ac)
		ac->Deactivate();

	auto tp =  m_this.lock();

	if(tp)
        rparent->m_activeChild = std::dynamic_pointer_cast<cTreeNode>(tp);

	m_flags |= IS_ACTIVE;

	OnNodeActivated();

	updateDisplay();

}

bool cTreeNode::isActive()
{
    if(IS_ACTIVE & m_flags)
        return true;

    return false;

}

void cTreeNode::Deactivate()
{

	auto parent = m_parent.lock();

	if(!parent)
		return;

	auto rparent = std::dynamic_pointer_cast<cTreeNode>(parent);

	auto ac = rparent->m_activeChild.lock();
	auto ts = m_this.lock();

	if((size_t)ts.get() == (size_t)ac.get())
		rparent->m_activeChild.reset();

	m_flags &= ~IS_ACTIVE;


	OnNodeDeactivated();

	updateDisplay();
}
