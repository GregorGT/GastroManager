#ifndef CTREENODE_HPP
#define CTREENODE_HPP

#include "../Tree/cWXTreeCtrl.h"
#include "../config.h"
#include <unordered_map>

enum TREENODE_FLAGS {
		IS_ACTIVATALBE = 1,
		IS_ACTIVE = 2,

};

class cTreeNode : public TREES::cPrimitiveNode
{

public:
	int m_flags;

	std::string m_xmlname;
	std::unordered_map<std::string, std::string> m_attributes;
	std::unordered_map<std::string, std::weak_ptr<cTreeNode> > m_property;
	std::unordered_map<std::string, std::weak_ptr<cTreeNode> > m_childxmlmap;
	std::weak_ptr<cTreeNode> m_activeChild;

	std::string m_value;
	std::string m_name;

	long m_uuid;

	cTreeNode();
	~cTreeNode();

	//void loadFromFile(std::string &filename);
	//void saveToFile(std::string &filename);
	/// returns the translated name
	bool getTranslatedName(std::string &result);
	/// tag name
	bool getTranslatedTagName(std::string &result);
	/// returns the entire translation
	virtual bool getDisplayString(std::string &result);

	bool getProperty(std::string &name, std::shared_ptr<cTreeNode> &result);
	bool getPropertyName(std::string &name, std::string &result);
	bool getPropertyValue(std::string &name, std::string &value);
	virtual void setNewProperty(std::string &name, std::string &value) override;
    virtual void setProperty(std::string &name, std::string &value);

    virtual bool setChildXMLValue(std::string &xmlname, std::string &value, bool create = true);
    virtual bool getChildXMLValue(std::string &xmlanme, std::string &value);

    virtual void updateDisplay();

    virtual void printToXMLString(std::string &result);

	virtual void Activate(bool force = false);
	virtual void Deactivate();

	bool isActive();

	virtual void OnNodeActivated() {};
	virtual void OnNodeDeactivated() {};
	virtual bool OnSelect() {};

	virtual void OnInit();

	int getChildID();

//	virtual int getChildIndex();

	void iterateTroughChildrenSetIDs();

};

extern std::unordered_map<long, std::weak_ptr<cTreeNode> > g_nodemap;

long generateNodeUUID();

#endif // CTREENODE_HPP
