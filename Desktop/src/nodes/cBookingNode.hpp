#ifndef CBOOKINGNODE_HPP
#define CBOOKINGNODE_HPP

#include "c4PolgoneRectangle.hpp"
#include <wx/wx.h>
#include <wx/datetime.h>
#include <map>

class cBookingNode : public c4PolgoneRectangle {


public:
    wxDateTime m_begin;
    wxDateTime m_end;
    std::string m_reservationname;
    std::string m_tables;

    int isValidDateTime( std::map<long, std::shared_ptr<cBookingNode> > &tablenodes , wxDateTime &newbegin, wxDateTime &newend);
    void storeToSQL();
};

#endif
