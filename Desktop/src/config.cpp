#include <fstream>
#include <string>       // std::string
#include <iostream>     // std::cout
#include <sstream>      // std::istringstream
#include <iostream>
#include <iterator>
#include <numeric>
#include <algorithm>
#include <vector>
#include <streambuf>

#include "config.h"
#include "filesystem/rapidxml.hpp"
#include "nodes/cSettingsNode.h"
#include "nodes/cLevelNode.hpp"
#include "nodes/cLevelsNode.hpp"
#include "nodes/cTableNode.hpp"
#include "nodes/cChairNode.hpp"
#include "nodes/cMenuItemNode.hpp"
#include "nodes/cMenusNode.hpp"
#include "nodes/cMenuOptionNode.hpp"
#include "wxcrafter.h"

std::unordered_map<std::string, std::string> g_settingsmap;
std::unordered_map<std::string, std::string> g_translationmap;

std::unordered_map<std::string, int> g_propertyxmlnames = {{"x", 1},
														   {"y", 1},
														   {"z", 1},
														   {"rot", 1},
														   {"width", 1},
														   {"height", 1}};


void readConfigurationFile(std::string &file)
{
	std::ifstream infile(file);

	if(!infile.is_open())
		return;

	g_settingsmap.clear();

	std::string line;
	while (std::getline(infile, line))
	{
		//std::istringstream iss(line);
		//int a, b;

		// process pair (a,b)
		std::string s = line;
		std::string delimiter = "=";

		size_t pos = 0;
		size_t lastpos = 0;
		std::string token;
		std::vector<std::string> tokens;
		while ((pos = s.find(delimiter)) != std::string::npos) {
			token = s.substr(0, pos);
			tokens.push_back(token);
			//std::cout << token << std::endl;
			s.erase(0, pos + delimiter.length());
			lastpos = pos;
		}

		if(s.size()>0)
		{
			tokens.push_back(s);
		}



		if(tokens.size() == 2)
			g_settingsmap[ tokens[0].c_str() ] = tokens[1];

	}

	infile.close();
}


void readLanguageFile(std::string &file)
{

	std::ifstream infile(file);

	if(!infile.is_open())
		return;

	g_translationmap.clear();

	std::string line;
	while (std::getline(infile, line))
	{
		//std::istringstream iss(line);
		//int a, b;

		// process pair (a,b)
		std::string s = line;
		std::string delimiter = "=";

		size_t pos = 0;
		size_t lastpos = 0;
		std::string token;
		std::vector<std::string> tokens;
		while ((pos = s.find(delimiter)) != std::string::npos) {
			token = s.substr(0, pos);
			tokens.push_back(token);
			//std::cout << token << std::endl;
			s.erase(0, pos + delimiter.length());
			lastpos = pos;
		}

		if(s.size()>0)
		{
			tokens.push_back(s);
		}



		if(tokens.size() == 2)
			g_translationmap[ tokens[0].c_str() ] = tokens[1];

	}

	infile.close();

}

void clearGlobalTree()
{
	auto &children = g_root->m_children;
	for(auto iter = children.begin(); iter != children.end(); ++iter)
	{
		(*iter)->removeItemFromTree();
	}
	children.clear();
}

using namespace rapidxml;

void readXMLAttributes(xml_node<> *node, std::shared_ptr<cTreeNode> &currentnode)
{
	for (xml_attribute<> *attr = node->first_attribute();
     attr; attr = attr->next_attribute())
	{
		std::string name  = attr->name();
		std::string value = attr->value();

		std::transform(name.begin(), name.end(), name.begin(),
						[](unsigned char c){ return std::tolower(c); });

		std::transform(value.begin(), value.end(), value.begin(),
						[](unsigned char c){ return std::tolower(c); });

		currentnode->m_attributes[name] = value;

		//std::cout << "Node foobar has attribute " << attr->name() << " ";
		//std::cout << "with value " << attr->value() << "\n";
	}
}

void readProperties(xml_node<> *node, std::shared_ptr<TREES::cPrimitiveNode> &currentnode)
{

}

void loadXMLFileRecursiveMenues(xml_node<> *node, std::shared_ptr<TREES::cPrimitiveNode> &currentnode, int depth = 0)
{
	std::string name = node->name();

	std::transform(name.begin(), name.end(), name.begin(),
						[](unsigned char c){ return std::tolower(c); });

	std::shared_ptr<TREES::cPrimitiveNode> newnode;
	if(name == "option")
	{
			auto level = std::shared_ptr<cLevelNode>( new cLevelNode );
			auto tl = std::dynamic_pointer_cast<cTreeNode>(level);
			readXMLAttributes(node, tl);
			newnode = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(tl);
			tl->m_xmlname = "level";

			std::string nodename;
			level->getDisplayString(nodename);
			currentnode->appendExistingChild(newnode, nodename);
	}
	else if(name == "ingredient")
    {


    }
	else if(name == "image")
    {


    }
	else if(name == "item")
	{
			auto level = std::shared_ptr<cMenuItemNode>( new cMenuItemNode );
			auto tl = std::dynamic_pointer_cast<cTreeNode>(level);
			readXMLAttributes(node, tl);
			newnode = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(tl);
			tl->m_xmlname = "level";

			std::string nodename;
			level->getDisplayString(nodename);
			currentnode->appendExistingChild(newnode, nodename);

	}else if(g_propertyxmlnames.find(name) != g_propertyxmlnames.end())
	{
			std::string nvalue = node->value();
			//auto tmpnode = std::dynamic_pointer_cast<cTreeNode>(currentnode);
			//if(tmpnode)
			currentnode->setNewProperty(name, nvalue);
	}


	auto child = node->first_node();
	if(child)
	{
		if(newnode)
		{
			auto tn = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(newnode);
			loadXMLFileRecursiveMenues(child, tn, depth + 1);
		}
		else
			loadXMLFileRecursiveMenues(child, currentnode, depth + 1);
	}
	auto next = node->next_sibling();
	if(next)
	{
		if(newnode)
		{
			auto tn = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(newnode);
			loadXMLFileRecursiveMenues(next, tn, depth + 1);
		}
		else
			loadXMLFileRecursiveMenues(next, currentnode, depth + 1);

	}

}

void loadXMLFileRecursiveLayout(xml_node<> *node, std::shared_ptr<TREES::cPrimitiveNode> &currentnode, int depth = 0)
{
	std::string name = node->name();

	std::transform(name.begin(), name.end(), name.begin(),
						[](unsigned char c){ return std::tolower(c); });

	std::shared_ptr<TREES::cPrimitiveNode> newnode;
	if(name == "level")
	{
			auto level = std::shared_ptr<cLevelNode>( new cLevelNode );
			auto tl = std::dynamic_pointer_cast<cTreeNode>(level);
			readXMLAttributes(node, tl);
			newnode = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(tl);
			tl->m_xmlname = "level";

			std::string nodename;
			level->getDisplayString(nodename);
			currentnode->appendExistingChild(newnode, nodename);
	}
	else if(name == "table")
	{
			auto level = std::shared_ptr<cTableNode>( new cTableNode );
			auto tl = std::dynamic_pointer_cast<cTreeNode>(level);
			readXMLAttributes(node, tl);
			newnode = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(tl);
			tl->m_xmlname = "level";

			std::string nodename;
			level->getDisplayString(nodename);
			currentnode->appendExistingChild(newnode, nodename);

	}else if(g_propertyxmlnames.find(name) != g_propertyxmlnames.end())
	{
			std::string nvalue = node->value();
			//auto tmpnode = std::dynamic_pointer_cast<cTreeNode>(currentnode);
			//if(tmpnode)
			currentnode->setNewProperty(name, nvalue);
	}


	auto child = node->first_node();
	if(child)
	{
		if(newnode)
		{
			auto tn = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(newnode);
			loadXMLFileRecursiveLayout(child, tn, depth + 1);
		}
		else
			loadXMLFileRecursiveLayout(child, currentnode, depth + 1);
	}
	auto next = node->next_sibling();
	if(next)
	{
		if(newnode)
		{
			auto tn = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(newnode);
			loadXMLFileRecursiveLayout(next, tn, depth + 1);
		}
		else
			loadXMLFileRecursiveLayout(next, currentnode, depth + 1);

	}
}

bool getMySQLConnectionString(std::string &result)
{
    auto iter = g_settingsmap.find(SETTING_MYSQL_SERVERADDRESS);

    if(iter == g_settingsmap.end())
        return false;

    result = iter->second;

    return true;
}

bool getMySQLUserName(std::string &username)
{
    auto iter = g_settingsmap.find(SETTING_MYSQL_USERNAME);

    if(iter == g_settingsmap.end())
        return false;

    username = iter->second;

    return true;
}

bool getMySQLUserPassword(std::string &password)
{
    auto iter = g_settingsmap.find(SETTING_MYSQL_USERPASSWORD);

    if(iter == g_settingsmap.end())
        return false;

    password = iter->second;

    return true;
}

bool getMySQLDatabasename(std::string &dbname)
{
    auto iter = g_settingsmap.find(SETTING_MYSQL_DATABASENAME);

    if(iter == g_settingsmap.end())
        return false;

    dbname = iter->second;

    return true;
}



bool MessageBoxWithTranslation(std::string stringin)
{

    std::string translation;
    if(getTranslation(stringin, translation))
    {
        wxMessageBox( wxString(translation) );

    }else
    {
        wxMessageBox( wxString(stringin) );

    }
}

bool getTranslation(std::string &input, std::string &output)
{

    if(g_translationmap.find(input) == g_translationmap.end())
        return false;

    output = g_translationmap[input];

}


void loadXMLFileRecursive(xml_node<> *node, std::shared_ptr<TREES::cPrimitiveNode> &currentnode, int depth = 0)
{

	std::string name = node->name();
	std::transform(name.begin(), name.end(), name.begin(),
						[](unsigned char c){ return std::tolower(c); });

	bool donotparse = false;

	if(depth == 1 and name == "menues")
	{
			donotparse = true;
			loadXMLFileRecursiveMenues(node, currentnode, depth);
	}
	else
	if(depth == 1 and name == "layout")
	{

			donotparse = true;

			auto level = std::shared_ptr<cLevelsNode>( new cLevelsNode );
			g_levelsnode = level;
			auto tl = std::dynamic_pointer_cast<cTreeNode>(level);
			readXMLAttributes(node, tl);
			auto newnode = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(tl);
			tl->m_xmlname = "layout";
            std::string name;
            getTranslation(tl->m_xmlname, name);
			currentnode->appendExistingChild(newnode, name);
			loadXMLFileRecursiveLayout(node, newnode, depth);
	}


	auto child = node->first_node();
	if(child and !donotparse)
		loadXMLFileRecursive(child, currentnode, depth + 1);

	auto next = node->next_sibling();
	if(next)
		loadXMLFileRecursive(next, currentnode, depth);


}

void loadXMLFile(std::string &file)
{

	std::ifstream t(file);
	if(!t.is_open())
		return;

	std::string str((std::istreambuf_iterator<char>(t)),
					 std::istreambuf_iterator<char>());

	xml_document<> doc;    // character type defaults to char
	char *tmp = (char*)str.c_str();
	doc.parse<0>( tmp );    // 0 means default parse flags

	auto *node = doc.first_node();

	clearGlobalTree();

	std::string name = "Settings";
	std::shared_ptr<cSettingsNode> settings = std::shared_ptr<cSettingsNode>( new cSettingsNode );
	std::shared_ptr<TREES::cPrimitiveNode> pn = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(settings);
	g_root->appendExistingChild(pn, name);
	settings->loadSettings();

	loadXMLFileRecursive(node, g_root);

	t.close();
}
