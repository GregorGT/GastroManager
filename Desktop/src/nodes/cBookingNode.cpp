#include "cBookingNode.hpp"


int cBookingNode::isValidDateTime( std::map<long, std::shared_ptr<cBookingNode> > &tablenodes , wxDateTime &newbegin, wxDateTime &newend)
{

    for(auto iter = tablenodes.begin(); iter != tablenodes.end() ; ++iter)
    {

        if(iter->second->m_begin.GetTicks() < m_begin.GetTicks() && iter->second->m_end.GetTicks() > m_begin.GetTicks())
        {


            return 1;
        }

    }

   // if(m_begin.GetAsDOS() < )


}

void cBookingNode::storeToSQL()
{

}
