#ifndef C4POLGONERECTANGLE_HPP
#define C4POLGONERECTANGLE_HPP
#include "cTreeNode.hpp"
#include <wx/wx.h>

class c4PolgoneRectangle : public cTreeNode
{
public:
	wxString m_text;
	wxColor  m_color;
	wxColor  m_filling;
	wxColor  m_textcolor;
	wxColor  m_textcolorbackground;

	wxPoint m_point[4];// = new wxPoint(0,200);
	bool 	m_finitilized = false;

	//wxList  *m_list = NULL;

	int      m_x,m_y, m_radius;
	int 	 m_side;

	c4PolgoneRectangle();
	~c4PolgoneRectangle();

	//virtual int getWidth();
	//virtual int getHeight();
	//virtual float getRotation();

	virtual void OnUpdatePositions();
	virtual void OnPaint(wxDC *dc, int offsetx, int offsety) override;

	// return 0 if no hit, 1 if is inside, 2 if is inside edge
	virtual bool HitTest(wxPoint &xy);
	// return 0 if no hit, 1 if is close to line 0-1, 2 if it is close to 1-2, 3 > 2-3 and 4 > 3-0
	virtual int  HitTestSide(wxPoint &xy);

	virtual void OnNodeActivated() override {};
};

#endif // C4POLGONERECTANGLE_HPP
