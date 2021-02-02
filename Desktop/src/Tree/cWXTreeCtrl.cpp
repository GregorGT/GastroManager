#include "cWXTreeCtrl.h"
#include "../nodes/cTreeNode.hpp"
#include <memory>





namespace TREES {

std::unordered_map<size_t, std::weak_ptr<cPrimitiveNode> > g_treeitemid2primitivenode;


void cWXTreeCtrl::appendRoot(std::shared_ptr<cTreeNode> &node, std::string &name)
{
	if(m_root)
		return;

	m_root = std::shared_ptr<cTreeNode>( new cTreeNode );

	wxString mystring(name.c_str(), wxConvUTF8);
	m_root->m_itemID = AddRoot(mystring);
	m_root->m_this = m_root;
	m_root->m_treeCtrl = this;

	node = m_root;

	g_treeitemid2primitivenode[(size_t)m_root->m_itemID.GetID()] = m_root;

	node = m_root;
}



}
