#ifndef _SETTINGS_H_
#define _SETTINGS_H_


#include <unordered_map>
#include <string>
//#include "nodes/cLevelsNode.hpp"
//#include "nodes/cMenusNode.hpp"

#define SETTINGS_FILE_NAME "config.txt"
#define SETTING_LANGUAGE "language"
#define SETTING_TIMEFORMAT "timeformat"
#define SETTING_MYSQL_SERVERADDRESS "database_address"
#define SETTING_MYSQL_USERNAME "database_username"
#define SETTING_MYSQL_USERPASSWORD "database_userpassword"
#define SETTING_MYSQL_DATABASENAME "database_name"
#define SETTING_SQLITE_DATABASEFILENAME "database_filename"

#define TRANSLATED_DISPLAYNAME "displayname_"
#define PRICE "price"
#define OVERWRITE_PRICE "overwrite_price"
#define ID "id"
#define NODE_PROPERTY_WIDTH "width"
#define NODE_PROPERTY_HEIGHT "height"
#define NODE_PROPERTY_X "x"
#define NODE_PROPERTY_Y "y"
#define NODE_PROPERTY_ROT "rot"
#define NODE_ATTRIBUTE_UUID "uuid"
#define NODE_IMAGE "image"


//#define NODE_PROPERTY_WIDTH "width"
//#define NODE_PROPERTY_HEIGHT "height"

#define CHAR_BITS 8

extern std::unordered_map<std::string, std::string> g_settingsmap;
extern std::unordered_map<std::string, std::string> g_translationmap;

//extern std::weak_ptr<cLevelsNode> g_levelsnode;
//extern std::weak_ptr<cMenuesNode> g_menuesnode;

void readConfigurationFile(std::string &file);
void readLanguageFile(std::string &file);

void loadXMLFile(std::string &file);
bool getTranslation(std::string &input, std::string &output);

bool getMySQLConnectionString(std::string &result);
bool getMySQLUserName(std::string &username);
bool getMySQLUserPassword(std::string &password);
bool getMySQLDatabasename(std::string &dbname);

bool MessageBoxWithTranslation(std::string stringin);

#endif // _SETTINGS_H_

