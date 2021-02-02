#include "cMenuOptionNode.hpp"


bool cMenuOptionNode::getDisplayString(std::string &result)
{

    getTranslatedName(result);

    if(result.size() == 0)
        result = m_attributes[NODE_ATTRIBUTE_NAME];

    if(result.size() == 0)
        result = m_xmlname;
}
