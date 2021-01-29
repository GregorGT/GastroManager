///////////////////////////////////////////////////////////////////////////
// C++ code generated with wxFormBuilder (version Jun 17 2015)
// http://www.wxformbuilder.org/
//
// PLEASE DO "NOT" EDIT THIS FILE!
///////////////////////////////////////////////////////////////////////////

#include "wxtextinputdialog.hpp"

///////////////////////////////////////////////////////////////////////////

MyDialogTextInput::MyDialogTextInput( wxWindow* parent, wxWindowID id, const wxString& title, const wxPoint& pos, const wxSize& size, long style ) : wxDialog( parent, id, title, pos, size, style )
{
	this->SetSizeHints( wxDefaultSize, wxDefaultSize );

	wxBoxSizer* bSizer16;
	bSizer16 = new wxBoxSizer( wxVERTICAL );

	wxBoxSizer* bSizer17;
	bSizer17 = new wxBoxSizer( wxHORIZONTAL );

	m_textCtrlInput = new wxTextCtrl( this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, 0 );
	bSizer17->Add( m_textCtrlInput, 0, wxALL, 5 );

	m_buttonOk = new wxButton( this, wxID_ANY, wxT("Ok"), wxDefaultPosition, wxDefaultSize, 0 );
	bSizer17->Add( m_buttonOk, 0, wxALL, 5 );

	m_buttonCancel = new wxButton( this, wxID_ANY, wxT("Cancel"), wxDefaultPosition, wxDefaultSize, 0 );
	bSizer17->Add( m_buttonCancel, 0, wxALL, 5 );


	bSizer16->Add( bSizer17, 1, wxEXPAND, 5 );


	this->SetSizer( bSizer16 );
	this->Layout();
	bSizer16->Fit( this );

	this->Centre( wxBOTH );
}

MyDialogTextInput::~MyDialogTextInput()
{
}
