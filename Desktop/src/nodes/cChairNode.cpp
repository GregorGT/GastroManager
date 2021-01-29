#include "cChairNode.hpp"

std::weak_ptr<cChairNode> g_activeChair;


cChairNode::cChairNode()
{
}

cChairNode::~cChairNode()
{
}

void cChairNode::OnNodeActivated()
{
		auto tn = m_this.lock();
		
		if(tn)
			g_activeChair = std::dynamic_pointer_cast<cChairNode>(tn);
}