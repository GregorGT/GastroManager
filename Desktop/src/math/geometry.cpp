
#include "geometry.h"
#include <math.h>

using namespace std;

// Given three colinear points p, q, r, the function checks if
// point q lies on line segment 'pr'
bool onSegment(wxPoint p, wxPoint q, wxPoint r)
{
    if (q.x <= max(p.x, r.x) && q.x >= min(p.x, r.x) &&
            q.y <= max(p.y, r.y) && q.y >= min(p.y, r.y))
        return true;
    return false;
}

// To find orientation of ordered triplet (p, q, r).
// The function returns following values
// 0 --> p, q and r are colinear
// 1 --> Clockwise
// 2 --> Counterclockwise
int orientation(wxPoint p, wxPoint q, wxPoint r)
{
    int val = (q.y - p.y) * (r.x - q.x) -
            (q.x - p.x) * (r.y - q.y);

    if (val == 0) return 0; // colinear
    return (val > 0)? 1: 2; // clock or counterclock wise
}

// The function that returns true if line segment 'p1q1'
// and 'p2q2' intersect.
bool doIntersect(wxPoint p1, wxPoint q1, wxPoint p2, wxPoint q2)
{
    // Find the four orientations needed for general and
    // special cases
    int o1 = orientation(p1, q1, p2);
    int o2 = orientation(p1, q1, q2);
    int o3 = orientation(p2, q2, p1);
    int o4 = orientation(p2, q2, q1);

    // General case
    if (o1 != o2 && o3 != o4)
        return true;

    // Special Cases
    // p1, q1 and p2 are colinear and p2 lies on segment p1q1
    if (o1 == 0 && onSegment(p1, p2, q1)) return true;

    // p1, q1 and p2 are colinear and q2 lies on segment p1q1
    if (o2 == 0 && onSegment(p1, q2, q1)) return true;

    // p2, q2 and p1 are colinear and p1 lies on segment p2q2
    if (o3 == 0 && onSegment(p2, p1, q2)) return true;

    // p2, q2 and q1 are colinear and q1 lies on segment p2q2
    if (o4 == 0 && onSegment(p2, q1, q2)) return true;

    return false; // Doesn't fall in any of the above cases
}

// Returns true if the point p lies inside the polygon[] with n vertices
bool isInside(wxPoint polygon[], int n, wxPoint p)
{
    // There must be at least 3 vertices in polygon[]
    if (n < 3) return false;

    // Create a point for line segment from p to infinite
    wxPoint extreme = {INF, p.y};

    // Count intersections of the above line with sides of polygon
    int count = 0, i = 0;
    do
    {
        int next = (i+1)%n;

        // Check if the line segment from 'p' to 'extreme' intersects
        // with the line segment from 'polygon[i]' to 'polygon[next]'
        if (doIntersect(polygon[i], polygon[next], p, extreme))
        {
            // If the point 'p' is colinear with line segment 'i-next',
            // then check if it lies on segment. If it lies, return true,
            // otherwise false
            if (orientation(polygon[i], p, polygon[next]) == 0)
            return onSegment(polygon[i], p, polygon[next]);

            count++;
        }
        i = next;
    } while (i != 0);

    // Return true if count is odd, false otherwise
    return count&1; // Same as (count%2 == 1)
}

wxPoint m_point1;
wxPoint m_point2;
float m_A;
float m_B;
float m_C;

void CalculateLine()
{
	m_A = m_point1.y - m_point2.y;
	m_B = m_point2.x - m_point1.x;
	m_C = m_point1.x * m_point2.y - m_point2.x * m_point1.y;
	if(m_A == 0 && m_B == 0)
	{
		//Debug.LogError("Line error: A & B = 0");
	}
}

float Distance2DPointToLine(wxPoint point)
{
	if(m_A * m_A + m_B * m_B < 0.000001)
		return 100000;

    return abs(m_A * point.x + m_B * point.y + m_C) /
        sqrt(m_A * m_A + m_B * m_B);
}

int IsInsideLineSegment4Points(wxPoint polygon[], int n, wxPoint p)
{
	if(n != 4)
		return 0;

	int smoothoffset = 10;

	m_point1 = polygon[0];
	m_point2 = polygon[1];

	CalculateLine();
	float dist = Distance2DPointToLine(p);

	if(dist<smoothoffset)
		return 1;

	m_point1 = polygon[1];
	m_point2 = polygon[2];
	CalculateLine();
	dist = Distance2DPointToLine(p);

	if(dist<smoothoffset)
		return 2;

	m_point1 = polygon[2];
	m_point2 = polygon[3];
	CalculateLine();
	dist = Distance2DPointToLine(p);

	if(dist<smoothoffset)
		return 3;

	m_point1 = polygon[3];
	m_point2 = polygon[0];
	CalculateLine();
	dist = Distance2DPointToLine(p);

	if(dist<smoothoffset)
		return 4;

	return 0;
}
