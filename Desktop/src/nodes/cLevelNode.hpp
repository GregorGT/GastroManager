#ifndef CLEVELNODE_HPP
#define CLEVELNODE_HPP

#include "cTreeNode.hpp"

class cLevelNode : public cTreeNode
{
public:
	cLevelNode();
	~cLevelNode();

	virtual void OnNodeActivated() override;

};

extern std::weak_ptr<cLevelNode> g_activeLevel;


#endif // CLEVELNODE_HPP
