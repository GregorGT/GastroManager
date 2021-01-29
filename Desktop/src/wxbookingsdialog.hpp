///////////////////////////////////////////////////////////////////////////
// C++ code generated with wxFormBuilder (version Jun 17 2015)
// http://www.wxformbuilder.org/
//
// PLEASE DO "NOT" EDIT THIS FILE!
///////////////////////////////////////////////////////////////////////////

#ifndef __WXBOOOKINGSDIALOG_H__
#define __WXBOOOKINGSDIALOG_H__

#include <wx/artprov.h>
#include <wx/xrc/xmlres.h>
#include <wx/string.h>
#include <wx/stattext.h>
#include <wx/gdicmn.h>
#include <wx/font.h>
#include <wx/colour.h>
#include <wx/settings.h>
#include <wx/sizer.h>
#include <wx/button.h>
#include <wx/statline.h>
#include <wx/dialog.h>
#include <wx/timectrl.h>
#include <wx/dateevt.h>

#include <memory>
///////////////////////////////////////////////////////////////////////////

class MyDialogTextInput;
class cBookingNode;

///////////////////////////////////////////////////////////////////////////////
/// Class MyDialogEditBooking
///////////////////////////////////////////////////////////////////////////////
class MyDialogEditBooking : public wxDialog
{
	private:

	public:
		wxStaticText* m_staticTextLevel;
		wxStaticText* m_staticTextLevelNumber;
		wxStaticText* m_staticTextTable;
		wxStaticText* m_staticTextTableNumber;
		wxButton* m_buttonEditTables;
		wxStaticText* m_staticTextTimeBegin;
		wxTimePickerCtrl* m_staticTextTimeBeginTime;
		wxStaticText* m_staticTextTimeEnd;
		wxTimePickerCtrl* m_staticTextTimeEndTime;
		wxStaticText* m_staticTextName;
		wxStaticText* m_staticTextNameEntry;
		wxButton* m_buttonNameEdit;
		wxStaticLine* m_staticline1;
		wxButton* m_buttonOk;
		wxButton* m_buttonCancel;

		MyDialogTextInput *m_textinputName;
		MyDialogTextInput *m_textinputTables;
		// Virtual event handlers, overide them in your derived class
		virtual void OnButtonEditTables( wxCommandEvent& event );// { event.Skip(); }
		virtual void OnButtonEditName( wxCommandEvent& event );// { event.Skip(); }
		virtual void OnButtonOk( wxCommandEvent& event );// { event.Skip(); }
		virtual void OnButtonCancel( wxCommandEvent& event );// { event.Skip(); }

//	virtual void OnBeginTimeChanged( wxDateEvent &event);
//		virtual void OnEndTimeChanged( wxDateEvent &event);
        std::shared_ptr<cBookingNode> m_bookingNode;

	public:

		MyDialogEditBooking( wxWindow* parent, wxWindowID id = wxID_ANY, const wxString& title = wxT("Edit booking"), const wxPoint& pos = wxDefaultPosition, const wxSize& size = wxDefaultSize, long style = wxDEFAULT_DIALOG_STYLE );
		~MyDialogEditBooking();

};

#endif //__NONAME_H__
