///////////////////////////////////////////////////////////////////////////
// C++ code generated with wxFormBuilder (version Jun 17 2015)
// http://www.wxformbuilder.org/
//
// PLEASE DO "NOT" EDIT THIS FILE!
///////////////////////////////////////////////////////////////////////////

#include "wxbookingsdialog.hpp"
#include "wxtextinputdialog.hpp"
#include <wx/dateevt.h>

///////////////////////////////////////////////////////////////////////////

MyDialogEditBooking::MyDialogEditBooking( wxWindow* parent, wxWindowID id, const wxString& title, const wxPoint& pos, const wxSize& size, long style ) : wxDialog( parent, id, title, pos, size, style )
{
	this->SetSizeHints( wxDefaultSize, wxDefaultSize );

	wxBoxSizer* bSizer13;
	bSizer13 = new wxBoxSizer( wxVERTICAL );

	wxBoxSizer* bSizer14;
	bSizer14 = new wxBoxSizer( wxHORIZONTAL );

	m_staticTextLevel = new wxStaticText( this, wxID_ANY, wxT("floor:"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextLevel->Wrap( -1 );
	bSizer14->Add( m_staticTextLevel, 0, wxALL, 5 );

	m_staticTextLevelNumber = new wxStaticText( this, wxID_ANY, wxT("21"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextLevelNumber->Wrap( -1 );
	bSizer14->Add( m_staticTextLevelNumber, 0, wxALL, 5 );


	bSizer13->Add( bSizer14, 1, wxEXPAND, 5 );

	wxBoxSizer* bSizer15;
	bSizer15 = new wxBoxSizer( wxHORIZONTAL );

	m_staticTextTable = new wxStaticText( this, wxID_ANY, wxT("tables:"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextTable->Wrap( -1 );
	bSizer15->Add( m_staticTextTable, 0, wxALL, 5 );

	m_staticTextTableNumber = new wxStaticText( this, wxID_ANY, wxT("1,2"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextTableNumber->Wrap( -1 );
	bSizer15->Add( m_staticTextTableNumber, 0, wxALL, 5 );

	m_buttonEditTables = new wxButton( this, wxID_ANY, wxT("Edit"), wxDefaultPosition, wxDefaultSize, 0 );
	bSizer15->Add( m_buttonEditTables, 0, wxALL, 5 );


	bSizer13->Add( bSizer15, 1, wxEXPAND, 5 );

	wxBoxSizer* bSizer18;
	bSizer18 = new wxBoxSizer( wxHORIZONTAL );

	m_staticTextTimeBegin = new wxStaticText( this, wxID_ANY, wxT("Time begin"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextTimeBegin->Wrap( -1 );
	bSizer18->Add( m_staticTextTimeBegin, 0, wxALL, 5 );

	m_staticTextTimeBeginTime = new wxTimePickerCtrl( this, wxID_ANY); //, wxT("1:12"), wxDefaultPosition, wxDefaultSize, 0 );
//	m_staticTextTimeBeginTime->Wrap( -1 );
	bSizer18->Add( m_staticTextTimeBeginTime, 0, wxALL, 5 );


	bSizer13->Add( bSizer18, 1, wxEXPAND, 5 );

	wxBoxSizer* bSizer19;
	bSizer19 = new wxBoxSizer( wxHORIZONTAL );

	m_staticTextTimeEnd = new wxStaticText( this, wxID_ANY, wxT("Time end"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextTimeEnd->Wrap( -1 );
	bSizer19->Add( m_staticTextTimeEnd, 0, wxALL, 5 );

	// should be replaced by a time picker
	m_staticTextTimeEndTime = new wxTimePickerCtrl( this, wxID_ANY );//, wxT("2:12"), wxDefaultPosition, wxDefaultSize, 0 );
//	m_staticTextTimeEndTime->Wrap( -1 );
	bSizer19->Add( m_staticTextTimeEndTime, 0, wxALL, 5 );


	bSizer13->Add( bSizer19, 1, wxEXPAND, 5 );

	wxBoxSizer* bSizer20;
	bSizer20 = new wxBoxSizer( wxHORIZONTAL );

	m_staticTextName = new wxStaticText( this, wxID_ANY, wxT("Name:"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextName->Wrap( -1 );
	bSizer20->Add( m_staticTextName, 0, wxALL, 5 );

	m_staticTextNameEntry = new wxStaticText( this, wxID_ANY, wxT("Gustav"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextNameEntry->Wrap( -1 );
	bSizer20->Add( m_staticTextNameEntry, 0, wxALL, 5 );

	m_buttonNameEdit = new wxButton( this, wxID_ANY, wxT("Edit"), wxDefaultPosition, wxDefaultSize, 0 );
	bSizer20->Add( m_buttonNameEdit, 0, wxALL, 5 );


	bSizer13->Add( bSizer20, 1, wxEXPAND, 5 );

	m_staticline1 = new wxStaticLine( this, wxID_ANY, wxDefaultPosition, wxSize( -1,15 ), wxLI_HORIZONTAL );
	bSizer13->Add( m_staticline1, 0, wxEXPAND | wxALL, 5 );

	wxBoxSizer* bSizer21;
	bSizer21 = new wxBoxSizer( wxHORIZONTAL );

	m_buttonOk = new wxButton( this, wxID_ANY, wxT("Ok"), wxDefaultPosition, wxDefaultSize, 0 );
	bSizer21->Add( m_buttonOk, 0, wxALL, 5 );

	m_buttonCancel = new wxButton( this, wxID_ANY, wxT("Cancel"), wxDefaultPosition, wxDefaultSize, 0 );
	bSizer21->Add( m_buttonCancel, 0, wxALL, 5 );


	bSizer13->Add( bSizer21, 1, wxEXPAND, 5 );


	this->SetSizer( bSizer13 );
	this->Layout();
	bSizer13->Fit( this );

	this->Centre( wxBOTH );

	// Connect Events
	m_buttonEditTables->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MyDialogEditBooking::OnButtonEditTables ), NULL, this );
	m_buttonNameEdit->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MyDialogEditBooking::OnButtonEditName ), NULL, this );
	m_buttonOk->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MyDialogEditBooking::OnButtonOk ), NULL, this );
	m_buttonCancel->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MyDialogEditBooking::OnButtonCancel ), NULL, this );

	m_textinputName = new MyDialogTextInput(this);
	m_textinputTables = new MyDialogTextInput(this);
  //  m_staticTextTimeBeginTime->Connect( wxEVT_TIME_CHANGED, wxDateEvent( MyDialogEditBooking::OnBeginTimeChanged ), NULL, this);
  //  m_staticTextTimeEndTime->Connect( wxEVT_TIME_CHANGED, wxCommandEventHandler( MyDialogEditBooking::OnEndTimeChanged ), NULL, this);

}

MyDialogEditBooking::~MyDialogEditBooking()
{
	// Disconnect Events
	m_buttonEditTables->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MyDialogEditBooking::OnButtonEditTables ), NULL, this );
	m_buttonNameEdit->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MyDialogEditBooking::OnButtonEditName ), NULL, this );
	m_buttonOk->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MyDialogEditBooking::OnButtonOk ), NULL, this );
	m_buttonCancel->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MyDialogEditBooking::OnButtonCancel ), NULL, this );

	if(m_textinputName)
        delete m_textinputName;
    if(m_textinputTables)
        delete m_textinputTables;

    m_textinputName = NULL;
    m_textinputTables = NULL;
  //  m_staticTextTimeBeginTime->Disconnect( EVT_TIME_CHANGED, wxDateEvent( MyDialogEditBooking::OnBeginTimeChanged ), NULL, this);
  //  m_staticTextTimeEndTime->Disconnect( EVT_TIME_CHANGED, wxDateEvent( MyDialogEditBooking::OnEndTimeChanged ), NULL, this);
}

void MyDialogEditBooking::OnButtonEditTables( wxCommandEvent& event )
 {
     wxString tmp = m_staticTextLevelNumber->GetLabel();
     m_textinputTables->m_textCtrlInput->SetLabel(tmp);

     m_textinputTables->Show();
     event.Skip(); }
void MyDialogEditBooking::OnButtonEditName( wxCommandEvent& event )
{
     wxString tmp = m_staticTextNameEntry->GetLabel();
     m_textinputName->m_textCtrlInput->SetLabel(tmp);
     m_textinputName->Show();
     event.Skip();
}
void MyDialogEditBooking::OnButtonOk( wxCommandEvent& event )
 {

     event.Skip();
 }
void MyDialogEditBooking::OnButtonCancel( wxCommandEvent& event )
 { event.Skip(); }

