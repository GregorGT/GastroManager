#include "cLevelNode.hpp"

std::weak_ptr<cLevelNode> g_activeLevel;


cLevelNode::cLevelNode()
{
}

cLevelNode::~cLevelNode()
{
}

void cLevelNode::OnNodeActivated()
{
		auto tn = m_this.lock();

		if(tn)
			g_activeLevel = std::dynamic_pointer_cast<cLevelNode>(tn);

        std::string value;
        std::string name = NODE_IMAGE;

        if(getChildXMLValue(name, value))
        {



        }
}
