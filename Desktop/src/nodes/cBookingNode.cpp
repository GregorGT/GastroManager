#include "cBookingNode.hpp"


int cBookingNode::isValidDateTime( std::map<long, std::shared_ptr<cBookingNode> > &tablenodes , wxDateTime &newbegin, wxDateTime &newend)
{

    for(auto iter = tablenodes.begin(); iter != tablenodes.end() ; ++iter)
    {

        if(iter->second->m_begin.GetAsDos() < m_begin.GetAsDOS() &&
            iter->second->m_end.GetAsDos() > m_begin.GetAsDOS())
        {


            return 1;
        }

    }

   // if(m_begin.GetAsDOS() < )


}

void cBookingNode::storeToSQL()
{

}
