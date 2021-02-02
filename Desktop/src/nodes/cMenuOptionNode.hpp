#ifndef MENUOPTIONSNODE_H
#define MENUOPTIONSNODE_H


#include "cTreeNode.hpp"


class cMenuOptionNode : public cTreeNode {

	bool getDisplayString(std::string &result) override;
};


#endif
