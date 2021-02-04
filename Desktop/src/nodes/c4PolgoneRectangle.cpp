#include "c4PolgoneRectangle.hpp"
#include <math.h>
#include <stdlib.h>
#include "../math/geometry.h"

c4PolgoneRectangle::c4PolgoneRectangle()
{
}

c4PolgoneRectangle::~c4PolgoneRectangle()
{
/*	if(m_point1 != NULL)
		delete m_point1;// = new wxPoint(0,0);
 	if(m_point2 != NULL)
		delete m_point2;// = new wxPoint(0,0);
 	if(m_point3 != NULL)
		delete m_point3;// = new wxPoint(0,0);
 	if(m_point4 != NULL)
		delete m_point4;// = new wxPoint(0,0);

	if(m_list)
		delete m_list;*/
}

void c4PolgoneRectangle::OnUpdatePositions()
{
	int x = 0,y = 0;
	float rot = 0;
	int widht = 0, height = 100;

	std::string name;
	std::string propname = NODE_PROPERTY_X;
	if(getPropertyValue(propname, name))
		x = std::atoi(name.c_str());

	propname = NODE_PROPERTY_Y;
	if(getPropertyValue(propname, name))
		y = std::atoi(name.c_str());
	propname = NODE_PROPERTY_ROT;
	if(getPropertyValue(propname, name))
		rot = std::atof(name.c_str());
	propname = NODE_PROPERTY_WIDTH;
	if(getPropertyValue(propname, name))
		widht = std::atoi(name.c_str());
	propname = NODE_PROPERTY_HEIGHT;
	if(getPropertyValue(propname, name))
		height = std::atoi(name.c_str());

	float l = (float)widht*0.5;
	float h = (float)height*0.5;

 /*	if(m_point1 == NULL)
		m_point1 = new wxPoint(0,0);
 	if(m_point2 == NULL)
		m_point2 = new wxPoint(0,0);
 	if(m_point3 == NULL)
		m_point3 = new wxPoint(0,0);
 	if(m_point4 == NULL)
		m_point4 = new wxPoint(0,0);

	if(m_list == NULL)
		 {
			 m_list = new wxList();
			 m_list->Append((wxObject*)m_point1);
			 m_list->Append((wxObject*)m_point2);
			 m_list->Append((wxObject*)m_point3);
			 m_list->Append((wxObject*)m_point4);
		 }*/


	m_point[0].x = x + cos(rot)*l - sin(rot)*h;
	m_point[0].y = y + sin(rot)*l + cos(rot)*h;

	m_point[1].x = x + cos(rot)*l + sin(rot)*h;
	m_point[1].y = y + sin(rot)*l - cos(rot)*h;

	m_point[2].x = x -cos(rot)*l + sin(rot)*h;
	m_point[2].y = y -sin(rot)*l - cos(rot)*h;

	m_point[3].x = x -cos(rot)*l - sin(rot)*h;
	m_point[3].y = y -sin(rot)*l + cos(rot)*h;

	m_x = x;
	m_y = y;

	m_radius = sqrt(l*l+h*h);
}

bool c4PolgoneRectangle::HitTest(wxPoint &point)
{
	float distx, disty;

	distx = point.x - m_x;
	disty = point.y - m_y;

	float radius = sqrt(distx * distx + disty*disty);
	if(radius>m_radius)
		return false;

	if(isInside(m_point, 4, point))
		return true;

}

int c4PolgoneRectangle::HitTestSide(wxPoint &point)
{
	float distx, disty;

	distx = point.x - m_x;
	disty = point.y - m_y;

	float radius = sqrt(distx * distx + disty*disty);
	if(radius>m_radius)
		return false;

	////	return true;
	m_side = IsInsideLineSegment4Points(m_point, 4, point);
	return m_side;
}

wxPoint g_tmppoints[4];

void c4PolgoneRectangle::OnPaint(wxDC *dc, int offsetx, int offsety)
{
	if(true)
	{
		if(!m_finitilized)
			OnUpdatePositions();

		m_finitilized = true;

		g_tmppoints[0].x = m_point[0].x + offsetx;
		g_tmppoints[0].y = m_point[0].y + offsety;

		g_tmppoints[1].x = m_point[1].x + offsetx;
		g_tmppoints[1].y = m_point[1].y + offsety;

        g_tmppoints[2].x = m_point[2].x + offsetx;
		g_tmppoints[2].y = m_point[2].y + offsety;

        g_tmppoints[3].x = m_point[3].x + offsetx;
		g_tmppoints[3].y = m_point[3].y + offsety;

		dc->SetBrush( wxBrush( m_filling ) ); // green filling
		dc->SetPen( wxPen( m_color, 4) ); // 5-pixels-thick red outline
		dc->DrawPolygon(WXSIZEOF(g_tmppoints), g_tmppoints);

		m_textcolor = wxColor(255,255,155);

		dc->SetTextForeground(m_textcolor);
		dc->SetTextBackground(m_textcolorbackground);
		//dc->SetPen( wxPen( , 5) ); // 5-pixels-thick red outline
		dc->DrawText(m_text, wxPoint(m_x + offsetx, m_y + offsety) );
	}
}
