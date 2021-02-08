///////////////////////////////////////////////////////////////////////////
// C++ code generated with wxFormBuilder (version Jun 17 2015)
// http://www.wxformbuilder.org/
//
// PLEASE DO "NOT" EDIT THIS FILE!
///////////////////////////////////////////////////////////////////////////

#ifndef __WXTEXTINPUTDIALOG_H__
#define __WXTEXTINPUTDIALOG_H__

#include <wx/artprov.h>
#include <wx/xrc/xmlres.h>
#include <wx/string.h>
#include <wx/textctrl.h>
#include <wx/gdicmn.h>
#include <wx/font.h>
#include <wx/colour.h>
#include <wx/settings.h>
#include <wx/button.h>
#include <wx/sizer.h>
#include <wx/dialog.h>

///////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////////////
/// Class MyDialogTextInput
///////////////////////////////////////////////////////////////////////////////
class MyDialogTextInput : public wxDialog
{
	private:

	public:
		wxTextCtrl* m_textCtrlInput;
		wxButton* m_buttonOk;
		wxButton* m_buttonCancel;

	public:

		MyDialogTextInput( wxWindow* parent, wxWindowID id = wxID_ANY, const wxString& title = wxEmptyString, const wxPoint& pos = wxDefaultPosition, const wxSize& size = wxDefaultSize, long style = wxDEFAULT_DIALOG_STYLE );
		~MyDialogTextInput();

};

#endif
