#include "../config.h"
#include "cSettingsNode.h"

void cSettingsNode::loadSettings()
{

    g_settingsmap.clear();
    g_translationmap.clear();

	std::string filename = SETTINGS_FILE_NAME;
	readConfigurationFile(filename);

	if(g_settingsmap.find(SETTING_LANGUAGE) != g_settingsmap.end())
	{
		std::string filename = "lang_";
		filename += g_settingsmap[SETTING_LANGUAGE];
		filename += ".txt";
		readLanguageFile(filename);
	}

}
