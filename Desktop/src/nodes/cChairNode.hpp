#ifndef CCHAIRNODE_HPP
#define CCHAIRNODE_HPP

#include "c4PolgoneRectangle.hpp"

class cChairNode : public c4PolgoneRectangle
{
public:
	cChairNode();
	~cChairNode();
	
	virtual void OnNodeActivated() override;
};

extern std::weak_ptr<cChairNode> g_activeChair;

#endif // CCHAIRNODE_HPP
