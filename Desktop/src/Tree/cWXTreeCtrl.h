/*
 * C3DTrees.h
 *
 *  Created on: Nov 22, 2013
 *      Author: gregor
 */

#ifndef C3DSTANDARTTREES_H_
#define C3DSTANDARTTREES_H_

#include <memory>
#include <vector>
#include <string>
#include <unordered_map>
#include <wx/treectrl.h>
#include <wx/wx.h>

class cTreeNode;

namespace TREES {

class cPrimitiveNode;

extern std::unordered_map<size_t, std::weak_ptr<cPrimitiveNode> > g_treeitemid2primitivenode;

/* this is a primitive node class */
class cPrimitiveNode {

public:
	wxTreeCtrl *m_treeCtrl;
    /// responsible for me
    std::weak_ptr<cPrimitiveNode> m_parent;
    std::weak_ptr<cPrimitiveNode> m_nextSibling;
    std::weak_ptr<cPrimitiveNode> m_prevSibling;
	std::weak_ptr<cPrimitiveNode> m_this;
    /// linkis which are related in terms of visual relations - I may see you etc...
    /// responsibilities
    std::vector< std::shared_ptr< cPrimitiveNode> > m_children;

	wxTreeItemId m_itemID;
   // int m_childIndex;

	/// compiler problem fix
	virtual void setNewProperty(std::string &name, std::string &value){};

    bool appendNewChild(std::shared_ptr<cPrimitiveNode> &nodeout, std::string &name) {

		nodeout = std::shared_ptr<cPrimitiveNode>( new cPrimitiveNode);
		return appendExistingChild(nodeout, name);
    }

    bool appendExistingChild(std::shared_ptr<cPrimitiveNode> &nodeout, std::string &name) {

		//nodeout = std::shared_ptr<cPrimitiveNode>( new cPrimitiveNode);
		if(!nodeout)
			return false;

		m_children.push_back(nodeout);

        if(m_children.size() >= 2) {
            m_children[m_children.size() - 2]->m_nextSibling = nodeout;
            nodeout->m_prevSibling =  m_children[m_children.size() - 2];
            //node->m_nextSibling;
        }
       // nodeout->m_childIndex = m_children.size() - 1;
		nodeout->m_parent = m_this.lock();
		nodeout->m_treeCtrl = m_treeCtrl;
		nodeout->m_this = nodeout;

		wxString mystring(name.c_str(), wxConvUTF8);
		nodeout->m_itemID = m_treeCtrl->AppendItem(m_itemID, mystring);

		g_treeitemid2primitivenode[(size_t)nodeout->m_itemID.GetID()] = nodeout;

		return true;
    }


    inline std::shared_ptr<cPrimitiveNode> getFirstChild() {
        if(m_children.size() > 0)
          return m_children[0];
    }

    inline std::shared_ptr<cPrimitiveNode> getNextSibiling() {
        if(hasNextSibiling())
            return m_nextSibling.lock();

        std::shared_ptr<cPrimitiveNode> result;
        return result;
    }

    inline std::shared_ptr<cPrimitiveNode> getPrevSibiling() {
        if(hasPrevSibiling())
            return m_prevSibling.lock();

        std::shared_ptr<cPrimitiveNode> result;
        return result;
    }

    inline bool hasChildren()
    { return m_children.size(); }

    inline bool hasNextSibiling() {
        if (!m_nextSibling.expired())
            return true;
            return false;
    }

    inline bool hasPrevSibiling() {
        if (!m_prevSibling.expired() > 0)
            return true;
            return false;
    }

	void removeItemFromTree()
	{
		if(m_itemID == 0)
			return;

		m_treeCtrl->Delete(m_itemID);
		m_itemID = 0;
	}

    void removeMe(){

        std::shared_ptr<cPrimitiveNode> tmp(m_this.lock());

		if(!tmp)
			return;
		if(m_itemID == 0)
			return;

		m_treeCtrl->Delete(m_itemID);
		auto treeitemiter = g_treeitemid2primitivenode.find((size_t)m_itemID.GetID());
		if(treeitemiter != g_treeitemid2primitivenode.end())
			g_treeitemid2primitivenode.erase(treeitemiter);

		m_itemID = 0;
        if(tmp->hasNextSibiling())
        {
           // --tmp->m_childIndex;
            tmp = tmp->m_nextSibling.lock();
        }

        if(hasNextSibiling() && hasPrevSibiling())
        {
                m_nextSibling.lock()->m_prevSibling = m_prevSibling.lock();
                m_prevSibling.lock()->m_nextSibling = m_nextSibling.lock();
        } else if(hasNextSibiling() && !hasPrevSibiling())
        {
            m_nextSibling.lock()->m_prevSibling.reset();
        }else if(!hasNextSibiling() && hasPrevSibiling())
        {
            m_prevSibling.lock()->m_nextSibling.reset();
        }
		auto ithis = m_this.lock();
		auto parent = m_parent.lock();
		if(ithis and parent)
		{
			for(auto iter = parent->m_children.begin(); iter != parent->m_children.end(); ++iter)
			{
				if((*iter).get() == ithis.get())
				{
					parent->m_children.erase(iter);
					break;
				}
			}
		}


        //if(!m_parent.expired())
        //    m_parent.lock()->m_children.erase(m_parent.lock()->m_children.begin() + m_childIndex);
    }

	virtual void OnNodeActivated() {};
	virtual bool OnSelect() {};
	virtual void OnPaint(wxDC *dc, int offsetx, int offsety) {};
	virtual void printToXMLString(std::string &) {};
};


class cWXTreeCtrl : public wxTreeCtrl {

	public:

	std::shared_ptr<cTreeNode> m_root;

	cWXTreeCtrl(wxWindow *parent, wxWindowID id = wxID_ANY,
               const wxPoint& pos = wxDefaultPosition,
               const wxSize& size = wxDefaultSize,
               long style = wxTR_DEFAULT_STYLE,
               const wxValidator &validator = wxDefaultValidator,
               const wxString& name = wxTreeCtrlNameStr) : wxTreeCtrl(parent, id, pos, size, style, validator, name) {};


	void appendRoot(std::shared_ptr<cTreeNode> &node, std::string &name);


};



}


#endif /* C3DTREES_H_ */
