#include "cPropertyNode.hpp"

cPropertyNode::cPropertyNode()
{
}

cPropertyNode::~cPropertyNode()
{
}

bool cPropertyNode::getDisplayString(std::string &result)
{
	result = m_name;
	result += " = ";
	result += m_value;
	
	return true;
}

