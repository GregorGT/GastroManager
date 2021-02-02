#ifndef MENUITEMNODE_H
#define MENUITEMNODE_H

#include "cTreeNode.hpp"


class cMenuItemNode : public cTreeNode {

	bool getDisplayString(std::string &result) override;

};


#endif // MENUITEMNODE_H
