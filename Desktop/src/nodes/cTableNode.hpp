#ifndef CTABLENODE_HPP
#define CTABLENODE_HPP

#include "c4PolgoneRectangle.hpp"
#include <wx/listimpl.cpp>
#include "cBookingNode.hpp"

class cTableNode :  public c4PolgoneRectangle
{
public:

    std::map<long, std::weak_ptr<cBookingNode> > m_bookings;

	cTableNode();
	~cTableNode();

	virtual void OnNodeActivated() override;
    virtual int getTableID();
	//virtual void OnTablePaint(wxPaintDC *dc);
	virtual void OnPaint(wxDC *dc, int offsetx, int offsety) override;

};

extern std::weak_ptr<cTableNode> g_activeTable;

#endif // CTABLENODE_HPP
