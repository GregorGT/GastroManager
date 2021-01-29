#include "cTableNode.hpp"
#include <string>

std::weak_ptr<cTableNode> g_activeTable;

cTableNode::cTableNode()
{
}

cTableNode::~cTableNode()
{
}

void cTableNode::OnPaint(wxDC *dc, int offsetx, int offsety)
{
	c4PolgoneRectangle::OnPaint(dc, offsetx, offsety);
}

int cTableNode::getTableID(){

}

void cTableNode::OnNodeActivated()
{
		auto tn = m_this.lock();

		if(tn)
			g_activeTable = std::dynamic_pointer_cast<cTableNode>(tn);
}
