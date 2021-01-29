#ifndef WXCRAFTER_H
#define WXCRAFTER_H

#include <wx/artprov.h>
#include <wx/xrc/xmlres.h>
#include <wx/statusbr.h>
#include <wx/gdicmn.h>
#include <wx/font.h>
#include <wx/colour.h>
#include <wx/settings.h>
#include <wx/string.h>
#include <wx/treectrl.h>
#include <wx/sizer.h>
#include <wx/panel.h>
#include <wx/bitmap.h>
#include <wx/image.h>
#include <wx/icon.h>
#include <wx/button.h>
#include <wx/combobox.h>
#include <wx/filepicker.h>
#include <wx/statbmp.h>
#include <wx/notebook.h>
#include <wx/splitter.h>
#include <wx/statbox.h>
#include <wx/menu.h>
#include <wx/calctrl.h>
#include <wx/timer.h>
#include <wx/frame.h>
#include <wx/filedlg.h>
#include "Tree/cWXTreeCtrl.h"
#include "nodes/cTableNode.hpp"
#include "wxbookingsdialog.hpp"
#include "nodes/cBookingNode.hpp"

extern std::shared_ptr<TREES::cPrimitiveNode> g_root;
extern std::weak_ptr<cTreeNode> g_levelsnode;
extern std::weak_ptr<cTreeNode> g_menuesnode;

class  MainFrameBaseClass; // *g_mainframebaseclass;
extern MainFrameBaseClass *g_mainframebaseclass;
extern std::map<long, std::map< long, std::shared_ptr< cBookingNode > > > g_bookingsmap;
extern std::shared_ptr< cBookingNode >  g_newbookingsnode;

class MainFrameBaseClass : public wxFrame
{
protected:

		TREES::cWXTreeCtrl* m_treeCtrlMain;

        wxStatusBar* m_statusBar1;
		wxSplitterWindow* m_splitter1;
		wxPanel* m_panel3;
		wxPanel* m_panel4;
		wxNotebook* m_notebook;
		wxPanel* m_panelMenu;
		wxPanel* m_panelLayout;
		wxButton* m_buttonCreateNewFloor;
		wxButton* m_buttonNewTable;
		wxButton* m_buttonNewChair;
		wxStaticText* m_staticTextLayoutBmp;
		wxFilePickerCtrl* m_filePickerFloorPlan;
		wxPanel* m_panelLevelMap;
		wxPanel* m_scrolledWindowSelectionMenues;
		wxPanel* m_panel6;
		wxPanel* m_scrolledWindowBookings;
		wxCalendarCtrl* m_calendarBookings;
		wxButton* m_buttonNewBooking;
		wxPanel* m_panelBookingsPaintPanel;
		wxTimer m_timerTimer;
		wxMenu* m_menuTable;
		wxMenuBar* m_menubar1;
		wxMenu* m_menu_new;
		wxMenu* m_menuAbout;


		//wxFilePickerCtrl *m_filePicker;


		wxBitmap m_floorbitmap;
        MyDialogEditBooking *m_bookingsdialog = NULL;


		bool m_fTranslateNode = false;
		bool m_fRotatedNode  = false;
        bool m_fResizeNode = false;
		wxPoint m_startPoint;
		wxPoint m_nodeStartPosition;
		float m_startingRot;
		float m_startingDist;
		int  m_istartWidth = -1;
		int  m_istartHeight = -1;
		int  m_ipaintSide = -1;
		bool m_fMouseStarted = false;

		int m_scrolloffsetx = 0;
		int m_scrolloffsety = 0;
		int m_oldscrolloffsetx = 0;
		int m_oldscrolloffsety = 0;

		bool    m_fstartedToScroll = false;
        wxPoint m_startScrollPos;

        wxBitmap m_floorImage;

        int m_scrolloffsetbookingsx = 0;
        int m_scrolloffsetbookingsy = 0;
        int m_oldscrolloffsetbookingsx = 0;
        int m_oldscrolloffsetbookingsy = 0;

        bool m_fstartedToScrollBooking = false;
        wxPoint m_startScrollPosBooking;


		std::weak_ptr<c4PolgoneRectangle> m_selectedTmpNode;
protected:
//	virtual void OnLoadFile( wxCommandEvent& event );

			// Virtual event handlers, overide them in your derived class
	virtual void OnTreeItemActivated( wxTreeEvent& event );//{ event.Skip(); }
	virtual void OnTreeItemMenu( wxTreeEvent& event );// { event.Skip(); }
	virtual void OnTreeSelChanged( wxTreeEvent& event );// { event.Skip(); }
	virtual void OnNewFloorClick( wxCommandEvent& event );// { event.Skip(); }
	virtual void OnNewTableClick( wxCommandEvent& event );// { event.Skip(); }
	virtual void OnButtonNewChairClick( wxCommandEvent& event );// { event.Skip(); }
	virtual void OnComboBoxFloor( wxCommandEvent& event );// { event.Skip(); }
	virtual void OnComboBoxSelectTable( wxCommandEvent& event );// { event.Skip(); }
	virtual void OnComboboxChair( wxCommandEvent& event );// { event.Skip(); }
	virtual void OnFloorPlanFileChanged( wxFileDirPickerEvent& event );// { event.Skip(); }
	virtual void OnLoadFile( wxCommandEvent& event );// { event.Skip(); }
	virtual void OnLevelMapPaint( wxPaintEvent& event ); // { event.Skip(); }

    virtual void OnScrollLayoutVertical( wxScrollEvent& event ); // { event.Skip(); }
    virtual void OnScrollLayoutHorizontal( wxScrollEvent& event );// { event.Skip(); }

    virtual void OnBookingsLeftDown( wxMouseEvent& event );// { event.Skip(); }
	virtual void OnBookingsDblLeftClick(wxMouseEvent& event);
    virtual void OnBookingsLeftUp( wxMouseEvent& event );// { event.Skip(); }
    virtual void OnMouseMotionBookings( wxMouseEvent& event );// { event.Skip(); }

	virtual void OnFloorPlanLeftDblDown( wxMouseEvent& event );// { event.Skip(); }
	virtual void OnFloorPlanLeftDown( wxMouseEvent& event );// { event.Skip(); }
	virtual void OnFloorPlanLeftUp( wxMouseEvent& event );// { event.Skip(); }
	virtual void OnFloorPlanRightUp( wxMouseEvent& event );// { event.Skip(); }
    virtual void OnMouseMotion( wxMouseEvent& event );// { event.Skip(); }

	virtual void OnTableTranslate( wxCommandEvent& event ); // { event.Skip(); }
	virtual void OnTableRotate( wxCommandEvent& event ); // { event.Skip(); }
	virtual void OnTableResize( wxCommandEvent& event ); // { event.Skip(); }
	virtual void OnTableActivate( wxCommandEvent& event ); // { event.Skip(); }

    virtual void NewBookingClick( wxCommandEvent& event ); //{ event.Skip(); }

    virtual void OnBookingsPanelPaint( wxPaintEvent& event );// { event.Skip(); }
    virtual void OnCalendarBookings( wxCalendarEvent& event );// { event.Skip(); }
    virtual void OnCalendarSelChangedBookings( wxCalendarEvent& event );// { event.Skip(); }

	void OnTreeLeftButtonClick( wxMouseEvent& event );
    void OnTreeLeftButtonDClick( wxMouseEvent& event );
    void OnTreeRightButtonClick( wxMouseEvent& event );

    virtual void OnTimer( wxTimerEvent& event ); //) { event.Skip(); }

    void deactivatePolygonMotion();

	void getTimeFromBookingsClick(int y_position_panel, wxDateTime& result);
	int getYPositionBookingsFromTime(wxDateTime& datetime);

	void getTableFromBookingsXPos(int x_positon_panel, int &tableid);
	void getXPositionFromTableId(int id, int &position);

public:

    void setLayoutImagePath(std::string &path);

    MainFrameBaseClass(wxWindow* parent, wxWindowID id = wxID_ANY, const wxString& title = _("My Frame"), const wxPoint& pos = wxDefaultPosition, const wxSize& size = wxSize(-1,-1), long style = wxCAPTION|wxRESIZE_BORDER|wxMAXIMIZE_BOX|wxMINIMIZE_BOX|wxSYSTEM_MENU|wxCLOSE_BOX);
    virtual ~MainFrameBaseClass();

	void m_splitter1OnIdle( wxIdleEvent& )
	{
		m_splitter1->SetSashPosition( 0 );
		m_splitter1->Disconnect( wxEVT_IDLE, wxIdleEventHandler( MainFrameBaseClass::m_splitter1OnIdle ), NULL, this );
	}

	void renderLayout(wxDC&  dc);
	void renderBookings(wxDC &dc);
	void InitTree();
	void loadBookings();
};

#endif
