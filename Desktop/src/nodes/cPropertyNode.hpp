#ifndef CPROPERTYNODE_HPP
#define CPROPERTYNODE_HPP

#include "cTreeNode.hpp"

class cPropertyNode : public cTreeNode
{
public:
	cPropertyNode();
	~cPropertyNode();
	
	virtual bool getDisplayString(std::string &result) override;
};

#endif // CPROPERTYNODE_HPP
