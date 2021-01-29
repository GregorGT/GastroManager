#include "wxcrafter.h"
#include "nodes/cSettingsNode.h"
#include "nodes/cLevelNode.hpp"
#include "nodes/cLevelsNode.hpp"
#include "nodes/cMenusNode.hpp"
#include "nodes/cTableNode.hpp"
#include "nodes/cChairNode.hpp"
#include "config.h"
#include "sqlite/gj_mysql.hpp"

#include "wx/wx.h"
#include "wx/sizer.h"

#include <sys/stat.h>



// Declare the bitmap loading function
extern void wxC9ED9InitBitmapResources();

MainFrameBaseClass *g_mainframebaseclass = NULL;
static bool bBitmapLoaded = false;

std::weak_ptr<cTreeNode> g_levelsnode;
std::weak_ptr<cTreeNode> g_menuesnode;
std::shared_ptr<TREES::cPrimitiveNode> g_root;

std::map<long, std::map< long, std::shared_ptr< cBookingNode > > > g_bookingsmap;
std::shared_ptr< cBookingNode >  g_newbookingsnode;



inline bool file_exists (const std::string& name) {
  struct stat buffer;
  return (stat (name.c_str(), &buffer) == 0);
}

MainFrameBaseClass::MainFrameBaseClass(wxWindow* parent, wxWindowID id, const wxString& title, const wxPoint& pos, const wxSize& size, long style)
    : wxFrame(parent, id, title, pos, size, style)
{
    g_mainframebaseclass  = this;

    this->SetSizeHints( wxDefaultSize, wxDefaultSize );

	m_statusBar1 = this->CreateStatusBar( 1, wxST_SIZEGRIP, wxID_ANY );
	wxStaticBoxSizer* sbSizer1;
	sbSizer1 = new wxStaticBoxSizer( new wxStaticBox( this, wxID_ANY, wxT("label") ), wxHORIZONTAL );

	m_splitter1 = new wxSplitterWindow( sbSizer1->GetStaticBox(), wxID_ANY, wxDefaultPosition, wxDefaultSize, wxSP_3D );
	//m_splitter1->SetSashSize( 5 );
	m_splitter1->Connect( wxEVT_IDLE, wxIdleEventHandler( MainFrameBaseClass::m_splitter1OnIdle ), NULL, this );

	m_panel3 = new wxPanel( m_splitter1, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxTAB_TRAVERSAL );
	wxBoxSizer* bSizer6;
	bSizer6 = new wxBoxSizer( wxVERTICAL );

	m_treeCtrlMain = new TREES::cWXTreeCtrl( m_panel3, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxTR_DEFAULT_STYLE );
	bSizer6->Add( m_treeCtrlMain, 1, wxALL|wxEXPAND, 5 );


	m_panel3->SetSizer( bSizer6 );
	m_panel3->Layout();
	bSizer6->Fit( m_panel3 );
	m_panel4 = new wxPanel( m_splitter1, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxTAB_TRAVERSAL );
	wxBoxSizer* bSizer5;
	bSizer5 = new wxBoxSizer( wxVERTICAL );

	m_notebook = new wxNotebook( m_panel4, wxID_ANY, wxDefaultPosition, wxDefaultSize, 0 );
	m_panelMenu = new wxPanel( m_notebook, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxTAB_TRAVERSAL );
	m_notebook->AddPage( m_panelMenu, wxT("Menu"), false );
	m_panelLayout = new wxPanel( m_notebook, wxID_ANY, wxDefaultPosition, wxSize( -1,10 ), wxTAB_TRAVERSAL );
	wxBoxSizer* bSizerLayout;
	bSizerLayout = new wxBoxSizer( wxVERTICAL );

	wxBoxSizer* bSizerLayout2;
	bSizerLayout2 = new wxBoxSizer( wxVERTICAL );

	wxBoxSizer* bSizerLayout3;
	bSizerLayout3 = new wxBoxSizer( wxHORIZONTAL );

	m_buttonCreateNewFloor = new wxButton( m_panelLayout, wxID_ANY, wxT("New Floor"), wxDefaultPosition, wxSize( -1,80 ), 0 );
	bSizerLayout3->Add( m_buttonCreateNewFloor, 0, wxALL, 5 );

	m_buttonNewTable = new wxButton( m_panelLayout, wxID_ANY, wxT("New Table"), wxDefaultPosition, wxSize( -1,80 ), 0 );
	bSizerLayout3->Add( m_buttonNewTable, 0, wxALL, 5 );

	m_buttonNewChair = new wxButton( m_panelLayout, wxID_ANY, wxT("New Chair"), wxDefaultPosition, wxSize( -1,80 ), 0 );
	bSizerLayout3->Add( m_buttonNewChair, 0, wxALL, 5 );


	bSizerLayout2->Add( bSizerLayout3, 1, wxEXPAND, 5 );

	wxBoxSizer* bSizerLayout4;
	bSizerLayout4 = new wxBoxSizer( wxHORIZONTAL );

	m_staticTextLayoutBmp = new wxStaticText( m_panelLayout, wxID_ANY, wxT("map image"), wxDefaultPosition, wxDefaultSize, 0 );
	m_staticTextLayoutBmp->Wrap( -1 );
	bSizerLayout4->Add( m_staticTextLayoutBmp, 0, wxALL|wxALIGN_CENTER_VERTICAL, 5 );

	m_filePickerFloorPlan = new wxFilePickerCtrl( m_panelLayout, wxID_ANY, wxEmptyString, wxT("Select a file"), wxT("*.*"), wxDefaultPosition, wxDefaultSize, wxFLP_DEFAULT_STYLE );
	bSizerLayout4->Add( m_filePickerFloorPlan, 1, wxALL|wxALIGN_CENTER_VERTICAL, 5 );


	bSizerLayout2->Add( bSizerLayout4, 1, wxEXPAND, 5 );


	bSizerLayout->Add( bSizerLayout2, 1, wxEXPAND, 5 );

	m_panelLevelMap = new wxPanel( m_panelLayout, wxID_ANY, wxDefaultPosition, wxSize( 1000,100 ), wxHSCROLL|wxVSCROLL );
	//m_panelLevelMap->SetScrollRate( 1, 1 );
	//m_panelLevelMap->SetMinSize( wxSize( 100,1000 ) );

	bSizerLayout->Add( m_panelLevelMap, 1, wxEXPAND | wxALL, 5 );


	m_panelLayout->SetSizer( bSizerLayout );
	m_panelLayout->Layout();
	m_notebook->AddPage( m_panelLayout, wxT("Layout"), false );
	m_scrolledWindowSelectionMenues = new wxPanel( m_notebook, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxHSCROLL|wxVSCROLL );
//	m_scrolledWindowSelectionMenues->SetScrollRate( 5, 5 );
	wxBoxSizer* bSizerSelectionMenu1;
	bSizerSelectionMenu1 = new wxBoxSizer( wxVERTICAL );


	m_scrolledWindowSelectionMenues->SetSizer( bSizerSelectionMenu1 );
	m_scrolledWindowSelectionMenues->Layout();
	bSizerSelectionMenu1->Fit( m_scrolledWindowSelectionMenues );
	m_notebook->AddPage( m_scrolledWindowSelectionMenues, wxT("Selection Menu"), false );
	m_panel6 = new wxPanel( m_notebook, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxTAB_TRAVERSAL );
	m_notebook->AddPage( m_panel6, wxT("a page"), false );
	m_scrolledWindowBookings = new wxPanel( m_notebook, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxHSCROLL|wxVSCROLL );
//	m_scrolledWindowBookings->SetScrollRate( 5, 5 );
	wxBoxSizer* bSizer11;
	bSizer11 = new wxBoxSizer( wxVERTICAL );

	wxBoxSizer* bSizer12;
	bSizer12 = new wxBoxSizer( wxHORIZONTAL );

	m_calendarBookings = new wxCalendarCtrl( m_scrolledWindowBookings, wxID_ANY, wxDefaultDateTime, wxDefaultPosition, wxDefaultSize, wxCAL_SHOW_HOLIDAYS );
	bSizer12->Add( m_calendarBookings, 0, wxALL, 5 );

	m_buttonNewBooking = new wxButton( m_scrolledWindowBookings, wxID_ANY, wxT("New booking"), wxDefaultPosition, wxDefaultSize, 0 );
	bSizer12->Add( m_buttonNewBooking, 0, wxALL, 5 );


	bSizer11->Add( bSizer12, 1, wxEXPAND, 5 );

	m_panelBookingsPaintPanel = new wxPanel( m_scrolledWindowBookings, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxTAB_TRAVERSAL );
	bSizer11->Add( m_panelBookingsPaintPanel, 1, wxEXPAND | wxALL, 5 );


	m_scrolledWindowBookings->SetSizer( bSizer11 );
	m_scrolledWindowBookings->Layout();
	bSizer11->Fit( m_scrolledWindowBookings );
	m_notebook->AddPage( m_scrolledWindowBookings, wxT("bookings"), true );

	bSizer5->Add( m_notebook, 1, wxALL|wxEXPAND, 5 );


	m_panel4->SetSizer( bSizer5 );
	m_panel4->Layout();
	bSizer5->Fit( m_panel4 );
	m_splitter1->SplitVertically( m_panel3, m_panel4, 0 );
	sbSizer1->Add( m_splitter1, 1, wxEXPAND, 5 );


	this->SetSizer( sbSizer1 );
	this->Layout();
	m_timerTimer.SetOwner( this, wxID_ANY );
	m_timerTimer.Start( 1000 );

	m_menuTable = new wxMenu();
	wxMenuItem* m_menuItemTranslate;
	m_menuItemTranslate = new wxMenuItem( m_menuTable, wxID_ANY, wxString( wxT("translate") ) , wxEmptyString, wxITEM_NORMAL );
	m_menuTable->Append( m_menuItemTranslate );

	wxMenuItem* m_menuItemRotate;
	m_menuItemRotate = new wxMenuItem( m_menuTable, wxID_ANY, wxString( wxT("rotate") ) , wxEmptyString, wxITEM_NORMAL );
	m_menuTable->Append( m_menuItemRotate );

	wxMenuItem* m_menuItemResize;
	m_menuItemResize = new wxMenuItem( m_menuTable, wxID_ANY, wxString( wxT("resize") ) , wxEmptyString, wxITEM_NORMAL );
	m_menuTable->Append( m_menuItemResize );

	m_menuTable->AppendSeparator();

	wxMenuItem* m_menuItemActivate;
	m_menuItemActivate = new wxMenuItem( m_menuTable, wxID_ANY, wxString( wxT("activate") ) , wxEmptyString, wxITEM_NORMAL );
	m_menuTable->Append( m_menuItemActivate );

//	this->Connect( wxEVT_RIGHT_DOWN, wxMouseEventHandler( MainFrameBaseClass::MainFrameBaseClassOnContextMenu ), NULL, this );

	m_menubar1 = new wxMenuBar( 0 );
	m_menu_new = new wxMenu();
	wxMenuItem* m_menuItemLoad;
	m_menuItemLoad = new wxMenuItem( m_menu_new, wxID_ANY, wxString( wxT("LoadFile") ) , wxEmptyString, wxITEM_NORMAL );
	m_menu_new->Append( m_menuItemLoad );

	m_menubar1->Append( m_menu_new, wxT("New") );

	m_menuAbout = new wxMenu();
	m_menubar1->Append( m_menuAbout, wxT("About") );

	this->SetMenuBar( m_menubar1 );


	this->Centre( wxBOTH );

	// Connect Events
	m_treeCtrlMain->Connect( wxEVT_COMMAND_TREE_ITEM_ACTIVATED, wxTreeEventHandler( MainFrameBaseClass::OnTreeItemActivated ), NULL, this );
	m_treeCtrlMain->Connect( wxEVT_COMMAND_TREE_ITEM_MENU, wxTreeEventHandler( MainFrameBaseClass::OnTreeItemMenu ), NULL, this );
	m_treeCtrlMain->Connect( wxEVT_COMMAND_TREE_SEL_CHANGED, wxTreeEventHandler( MainFrameBaseClass::OnTreeSelChanged ), NULL, this );
	m_buttonCreateNewFloor->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MainFrameBaseClass::OnNewFloorClick ), NULL, this );
	m_buttonNewTable->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MainFrameBaseClass::OnNewTableClick ), NULL, this );
	m_buttonNewChair->Connect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MainFrameBaseClass::OnButtonNewChairClick ), NULL, this );
//	m_comboBoxSelectLevel->Connect( wxEVT_COMMAND_COMBOBOX_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnComboBoxFloor ), NULL, this );
//	m_comboBoxTable->Connect( wxEVT_COMMAND_COMBOBOX_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnComboBoxSelectTable ), NULL, this );
//	m_comboBoxChair->Connect( wxEVT_COMMAND_COMBOBOX_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnComboboxChair ), NULL, this );
	m_filePickerFloorPlan->Connect( wxEVT_COMMAND_FILEPICKER_CHANGED, wxFileDirPickerEventHandler( MainFrameBaseClass::OnFloorPlanFileChanged ), NULL, this );

	m_panelLevelMap->Connect( wxEVT_PAINT, wxPaintEventHandler( MainFrameBaseClass::OnLevelMapPaint ), NULL, this );
	m_panelLevelMap->Connect( wxEVT_LEFT_DCLICK, wxMouseEventHandler( MainFrameBaseClass::OnFloorPlanLeftDblDown ), NULL, this );
	m_panelLevelMap->Connect( wxEVT_LEFT_DOWN, wxMouseEventHandler( MainFrameBaseClass::OnFloorPlanLeftDown ), NULL, this );
	m_panelLevelMap->Connect( wxEVT_LEFT_UP, wxMouseEventHandler( MainFrameBaseClass::OnFloorPlanLeftUp ), NULL, this );
	m_panelLevelMap->Connect( wxEVT_RIGHT_UP, wxMouseEventHandler( MainFrameBaseClass::OnFloorPlanRightUp ), NULL, this );
	m_panelLevelMap->Connect( wxEVT_MOTION, wxMouseEventHandler( MainFrameBaseClass::OnMouseMotion ), NULL, this );

    m_panelBookingsPaintPanel->Connect(wxEVT_LEFT_DOWN, wxMouseEventHandler(MainFrameBaseClass::OnBookingsLeftDown), NULL, this);
    m_panelBookingsPaintPanel->Connect(wxEVT_LEFT_DCLICK, wxMouseEventHandler(MainFrameBaseClass::OnBookingsDblLeftClick), NULL, this);
    m_panelBookingsPaintPanel->Connect( wxEVT_LEFT_UP, wxMouseEventHandler( MainFrameBaseClass::OnBookingsLeftUp ), NULL, this );
	m_panelBookingsPaintPanel->Connect( wxEVT_MOTION, wxMouseEventHandler( MainFrameBaseClass::OnMouseMotionBookings ), NULL, this );
	//m_scrolledWindowBookings->Connect( wxEVT_PAINT, wxPaintEventHandler( MainFrameBaseClass::OnBookingsPanelPaint ), NULL, this );

	m_treeCtrlMain->Connect( wxEVT_LEFT_DCLICK, wxMouseEventHandler( MainFrameBaseClass::OnTreeLeftButtonClick ), NULL, this);
	m_treeCtrlMain->Connect( wxEVT_RIGHT_DOWN, wxMouseEventHandler( MainFrameBaseClass::OnTreeRightButtonClick ), NULL, this);
	m_treeCtrlMain->Connect( wxEVT_LEFT_DCLICK, wxMouseEventHandler( MainFrameBaseClass::OnTreeLeftButtonDClick ), NULL, this);

    m_calendarBookings->Connect( wxEVT_CALENDAR_DOUBLECLICKED, wxCalendarEventHandler( MainFrameBaseClass::OnCalendarBookings ), NULL, this );
	m_calendarBookings->Connect( wxEVT_CALENDAR_SEL_CHANGED, wxCalendarEventHandler( MainFrameBaseClass::OnCalendarSelChangedBookings ), NULL, this );
	m_panelBookingsPaintPanel->Connect( wxEVT_PAINT, wxPaintEventHandler( MainFrameBaseClass::OnBookingsPanelPaint ), NULL, this );
   /* m_scrollBarVertical->Connect( wxEVT_SCROLL_TOP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Connect( wxEVT_SCROLL_BOTTOM, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Connect( wxEVT_SCROLL_LINEUP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Connect( wxEVT_SCROLL_LINEDOWN, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Connect( wxEVT_SCROLL_PAGEUP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Connect( wxEVT_SCROLL_PAGEDOWN, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Connect( wxEVT_SCROLL_THUMBTRACK, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Connect( wxEVT_SCROLL_THUMBRELEASE, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Connect( wxEVT_SCROLL_CHANGED, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_TOP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_BOTTOM, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_LINEUP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_LINEDOWN, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_PAGEUP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_PAGEDOWN, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_THUMBTRACK, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_THUMBRELEASE, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Connect( wxEVT_SCROLL_CHANGED, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
*/
	this->Connect( wxID_ANY, wxEVT_TIMER, wxTimerEventHandler( MainFrameBaseClass::OnTimer ) );

	this->Connect( m_menuItemTranslate->GetId(), wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnTableTranslate ) );
	this->Connect( m_menuItemRotate->GetId(), wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnTableRotate ) );
	this->Connect( m_menuItemResize->GetId(), wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnTableResize ) );
	this->Connect( m_menuItemActivate->GetId(), wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnTableActivate ) );

	this->Connect( m_menuItemLoad->GetId(), wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnLoadFile ) );

	InitTree();
}

MainFrameBaseClass::~MainFrameBaseClass()
{
	// Disconnect Events

	m_treeCtrlMain->Disconnect( wxEVT_COMMAND_TREE_ITEM_ACTIVATED, wxTreeEventHandler( MainFrameBaseClass::OnTreeItemActivated ), NULL, this );
	m_treeCtrlMain->Disconnect( wxEVT_COMMAND_TREE_ITEM_MENU, wxTreeEventHandler( MainFrameBaseClass::OnTreeItemMenu ), NULL, this );
	m_treeCtrlMain->Disconnect( wxEVT_COMMAND_TREE_SEL_CHANGED, wxTreeEventHandler( MainFrameBaseClass::OnTreeSelChanged ), NULL, this );
	m_buttonCreateNewFloor->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MainFrameBaseClass::OnNewFloorClick ), NULL, this );
	m_buttonNewTable->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MainFrameBaseClass::OnNewTableClick ), NULL, this );
	m_buttonNewChair->Disconnect( wxEVT_COMMAND_BUTTON_CLICKED, wxCommandEventHandler( MainFrameBaseClass::OnButtonNewChairClick ), NULL, this );
//	m_comboBoxSelectLevel->Disconnect( wxEVT_COMMAND_COMBOBOX_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnComboBoxFloor ), NULL, this );
//	m_comboBoxTable->Disconnect( wxEVT_COMMAND_COMBOBOX_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnComboBoxSelectTable ), NULL, this );
//	m_comboBoxChair->Disconnect( wxEVT_COMMAND_COMBOBOX_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnComboboxChair ), NULL, this );
	m_filePickerFloorPlan->Disconnect( wxEVT_COMMAND_FILEPICKER_CHANGED, wxFileDirPickerEventHandler( MainFrameBaseClass::OnFloorPlanFileChanged ), NULL, this );
	m_panelLevelMap->Disconnect( wxEVT_PAINT, wxPaintEventHandler( MainFrameBaseClass::OnLevelMapPaint ), NULL, this );

	m_treeCtrlMain->Disconnect( wxEVT_LEFT_DCLICK, wxMouseEventHandler( MainFrameBaseClass::OnTreeLeftButtonClick ), NULL, this);
	m_treeCtrlMain->Disconnect( wxEVT_RIGHT_DOWN, wxMouseEventHandler( MainFrameBaseClass::OnTreeRightButtonClick ), NULL, this);
	m_treeCtrlMain->Disconnect( wxEVT_LEFT_DCLICK, wxMouseEventHandler( MainFrameBaseClass::OnTreeLeftButtonDClick ), NULL, this);

	m_panelLevelMap->Disconnect( wxEVT_LEFT_DCLICK, wxMouseEventHandler( MainFrameBaseClass::OnFloorPlanLeftDblDown ), NULL, this );
	m_panelLevelMap->Disconnect( wxEVT_LEFT_DOWN, wxMouseEventHandler( MainFrameBaseClass::OnFloorPlanLeftDown ), NULL, this );
	m_panelLevelMap->Disconnect( wxEVT_LEFT_UP, wxMouseEventHandler( MainFrameBaseClass::OnFloorPlanLeftUp ), NULL, this );
	m_panelLevelMap->Disconnect( wxEVT_RIGHT_UP, wxMouseEventHandler( MainFrameBaseClass::OnFloorPlanRightUp ), NULL, this );
	m_panelLevelMap->Disconnect( wxEVT_MOTION, wxMouseEventHandler( MainFrameBaseClass::OnMouseMotion ), NULL, this );

	m_calendarBookings->Disconnect( wxEVT_CALENDAR_DOUBLECLICKED, wxCalendarEventHandler( MainFrameBaseClass::OnCalendarBookings ), NULL, this );
	m_calendarBookings->Disconnect( wxEVT_CALENDAR_SEL_CHANGED, wxCalendarEventHandler( MainFrameBaseClass::OnCalendarSelChangedBookings ), NULL, this );
	m_panelBookingsPaintPanel->Disconnect( wxEVT_PAINT, wxPaintEventHandler( MainFrameBaseClass::OnBookingsPanelPaint ), NULL, this );


	m_panelBookingsPaintPanel->Disconnect( wxEVT_LEFT_DOWN, wxMouseEventHandler( MainFrameBaseClass::OnBookingsLeftDown ), NULL, this );
	m_panelBookingsPaintPanel->Disconnect( wxEVT_LEFT_UP, wxMouseEventHandler( MainFrameBaseClass::OnBookingsLeftUp ), NULL, this );
	m_panelBookingsPaintPanel->Disconnect( wxEVT_MOTION, wxMouseEventHandler( MainFrameBaseClass::OnMouseMotionBookings ), NULL, this );
    m_panelBookingsPaintPanel->Disconnect( wxEVT_LEFT_DCLICK, wxMouseEventHandler(MainFrameBaseClass::OnBookingsDblLeftClick), NULL, this);

    //m_scrolledWindowBookings->Disconnect( wxEVT_PAINT, wxPaintEventHandler( MainFrameBaseClass::OnBookingsPanelPaint ), NULL, this );

/*  m_scrollBarVertical->Disconnect( wxEVT_SCROLL_TOP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Disconnect( wxEVT_SCROLL_BOTTOM, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Disconnect( wxEVT_SCROLL_LINEUP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Disconnect( wxEVT_SCROLL_LINEDOWN, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Disconnect( wxEVT_SCROLL_PAGEUP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Disconnect( wxEVT_SCROLL_PAGEDOWN, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Disconnect( wxEVT_SCROLL_THUMBTRACK, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Disconnect( wxEVT_SCROLL_THUMBRELEASE, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarVertical->Disconnect( wxEVT_SCROLL_CHANGED, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutVertical ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_TOP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_BOTTOM, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_LINEUP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_LINEDOWN, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_PAGEUP, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_PAGEDOWN, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_THUMBTRACK, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_THUMBRELEASE, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
	m_scrollBarHorizontal->Disconnect( wxEVT_SCROLL_CHANGED, wxScrollEventHandler( MainFrameBaseClass::OnScrollLayoutHorizontal ), NULL, this );
*/
    this->Disconnect( wxID_ANY, wxEVT_TIMER, wxTimerEventHandler( MainFrameBaseClass::OnTimer ) );

	this->Disconnect( wxID_ANY, wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnTableTranslate ) );
	this->Disconnect( wxID_ANY, wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnTableRotate ) );
	this->Disconnect( wxID_ANY, wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnTableResize ) );
	this->Disconnect( wxID_ANY, wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnTableActivate ) );

	this->Disconnect( wxID_ANY, wxEVT_COMMAND_MENU_SELECTED, wxCommandEventHandler( MainFrameBaseClass::OnLoadFile ) );

	delete m_menuTable;
	delete m_bookingsdialog;
}

void MainFrameBaseClass::NewBookingClick( wxCommandEvent& event ){
     event.Skip(); }


void MainFrameBaseClass::OnCalendarBookings( wxCalendarEvent& event ) {
     event.Skip(); }
void MainFrameBaseClass::OnCalendarSelChangedBookings( wxCalendarEvent& event ) {
     event.Skip(); }


void MainFrameBaseClass::OnBookingsDblLeftClick(wxMouseEvent& event)
{
    wxDateTime time;
    getTimeFromBookingsClick(event.GetY(), time);

    if(!m_bookingsdialog)
    {
        m_bookingsdialog = new MyDialogEditBooking( this );
    }

    m_bookingsdialog->CentreOnParent();
    m_bookingsdialog->m_staticTextTimeBeginTime->SetTime( time.GetHour(), time.GetMinute(), time.GetSecond() );
    m_bookingsdialog->m_staticTextTimeEndTime->SetTime( time.GetHour() + 2, time.GetMinute(), time.GetSecond() );
    m_bookingsdialog->Show();

    event.Skip();
}

void MainFrameBaseClass::OnBookingsLeftDown( wxMouseEvent& event )
 {

     event.Skip(); }
void MainFrameBaseClass::OnBookingsLeftUp( wxMouseEvent& event ) {

    m_fstartedToScrollBooking = false;
    event.Skip(); }
void MainFrameBaseClass::OnMouseMotionBookings( wxMouseEvent& event ) {

    if(event.LeftIsDown())
    {

        if(!m_fstartedToScrollBooking)
           {

               m_startScrollPosBooking.x = event.GetX();
               m_startScrollPosBooking.y = event.GetY();
               m_oldscrolloffsetbookingsx = m_scrolloffsetbookingsx;
               m_oldscrolloffsetbookingsy = m_scrolloffsetbookingsy;

               m_fstartedToScrollBooking = true;

           }

           m_scrolloffsetbookingsx = event.GetX() - m_startScrollPosBooking.x + m_oldscrolloffsetbookingsx;
           m_scrolloffsetbookingsy = event.GetY() - m_startScrollPosBooking.y + m_oldscrolloffsetbookingsy;

           Refresh();
    }

    event.Skip(); }

void MainFrameBaseClass::OnTimer( wxTimerEvent& event )
{

    if(m_panelBookingsPaintPanel->IsShown())
    {

        //
        loadBookings();

    }

     event.Skip();
}

void MainFrameBaseClass::loadBookings()
{

    GJSQL::init_create_db();

}

void MainFrameBaseClass::setLayoutImagePath(std::string &path)
{
    m_filePickerFloorPlan->SetPath(wxString(path));
}

void MainFrameBaseClass::OnScrollLayoutVertical( wxScrollEvent& event )
 {
     m_scrolloffsety = event.GetPosition();

     event.Skip(); }
void MainFrameBaseClass::OnScrollLayoutHorizontal( wxScrollEvent& event )
 {
     m_scrolloffsetx = event.GetPosition();

     event.Skip(); }


void MainFrameBaseClass::OnTableTranslate( wxCommandEvent& event )
{
     auto ptr = m_selectedTmpNode.lock();

     if(ptr)
     {
         if(!ptr->isActive())
            ptr->Activate(true);

        m_fTranslateNode = true;
        m_fMouseStarted = false;
     }
     event.Skip();
}
void MainFrameBaseClass::OnTableRotate( wxCommandEvent& event )
{
     auto ptr = m_selectedTmpNode.lock();

     if(ptr)
     {
         if(!ptr->isActive())
            ptr->Activate(true);

        m_fRotatedNode = true;
        m_fMouseStarted = false;
     }
     event.Skip();
}
void MainFrameBaseClass::OnTableResize( wxCommandEvent& event )
{
     auto ptr = m_selectedTmpNode.lock();

     if(ptr)
     {
         if(!ptr->isActive())
            ptr->Activate(true);

        m_fResizeNode = true;
        m_fMouseStarted = false;
     }
     event.Skip();
}
void MainFrameBaseClass::OnTableActivate( wxCommandEvent& event )
 { event.Skip(); }


void MainFrameBaseClass::OnFloorPlanLeftDblDown( wxMouseEvent& event ) {
	event.Skip();

	auto floor = g_activeLevel.lock();
	if(!floor)
		return;

	wxPoint point(event.GetX()+m_scrolloffsetx, event.GetY()+m_scrolloffsety);

		auto iter = floor->m_children.begin();
		for(; iter != floor->m_children.end(); ++iter)
		{
            auto ptr = (*iter).get();
            cTableNode *bPtr = dynamic_cast<cTableNode*>(ptr);
            if ( bPtr )
            {
				auto casted = std::dynamic_pointer_cast<cTableNode>(*iter);

				if(casted->HitTest(point))
				{
					casted->Activate(true);
					break;
				}
			}
		}

	}
void MainFrameBaseClass::OnFloorPlanLeftDown( wxMouseEvent& event ) {

    if(m_fTranslateNode || m_fRotatedNode || m_fResizeNode)
    {
       auto table = m_selectedTmpNode.lock();

        if(!table){
            event.Skip();
            return;
        }

        auto polygonnode = std::dynamic_pointer_cast<c4PolgoneRectangle>(table);

        if(!m_fMouseStarted)
        {
            wxPoint pos(event.GetX()-m_scrolloffsetx, event.GetY()-m_scrolloffsety);

            if(!polygonnode->HitTest(pos))
            {
                m_fTranslateNode = false;
                m_fMouseStarted = false;
                deactivatePolygonMotion();
                event.Skip();
                return;
            }

            m_fMouseStarted = true;
            m_startPoint.x = event.GetX();
            m_startPoint.y = event.GetY();



            std::string prop = NODE_PROPERTY_ROT;
            std::string value = "0";
            polygonnode->getPropertyValue(prop,value);
            m_startingRot = atof(value.c_str());

            prop = NODE_PROPERTY_WIDTH;
            value = "100";
             polygonnode->getPropertyValue(prop,value);
            m_istartWidth = atoi(value.c_str());

            prop = NODE_PROPERTY_HEIGHT;
            value = "100";
            polygonnode->getPropertyValue(prop,value);
            m_istartHeight = atoi(value.c_str());

            m_nodeStartPosition.x = polygonnode->m_x;
            m_nodeStartPosition.y = polygonnode->m_y;

            int distx = m_nodeStartPosition.x - event.GetX();
            int disty = m_nodeStartPosition.y - event.GetY();
            m_startingDist = sqrt(distx*distx + disty*disty);
            //std::cout << "starting at:" << std::endl;
            //std::cout << m_startPoint.x << std::endl;

        }



    }

	event.Skip();

	}

void MainFrameBaseClass::OnMouseMotion( wxMouseEvent& event )
 {
     if(event.LeftIsDown())
     {

         if(m_fTranslateNode)
           {
            auto table = m_selectedTmpNode.lock();
            auto tmppolgyon = std::dynamic_pointer_cast<c4PolgoneRectangle>(table);
            if(!table)
            {
                event.Skip();
                return;
            }

            int offsetx = event.GetX() - m_startPoint.x;
            int offsety = event.GetY() - m_startPoint.y;

            //std::cout << offsetx << std::endl;

            if(offsetx > 0)
                int testing = 0;

            tmppolgyon->m_x = m_nodeStartPosition.x + offsetx;
            tmppolgyon->m_y = m_nodeStartPosition.y + offsety;
            std::string prop = NODE_PROPERTY_X;
            std::string value = std::to_string(tmppolgyon->m_x);
            table->setProperty(prop, value);
            prop = NODE_PROPERTY_Y;
            value = std::to_string(tmppolgyon->m_y);
            tmppolgyon->setProperty(prop, value);
            tmppolgyon->OnUpdatePositions();

            Refresh();
           }
           else if(m_fRotatedNode)
           {
            auto table = m_selectedTmpNode.lock();
            auto tmppolgyon = std::dynamic_pointer_cast<c4PolgoneRectangle>(table);
            if(!table)
            {
                event.Skip();
                return;
            }

            int offsetx = event.GetX() - m_startPoint.x;
            int offsety = event.GetY() - m_startPoint.y;

            //std::cout << offsetx << std::endl;

            if(offsetx > 0)
                int testing = 0;

            //tmppolgyon->m_x = m_nodeStartPosition.x + offsetx;
            //tmppolgyon->m_y = m_nodeStartPosition.y + offsety;
           // tmpo
            std::string prop = NODE_PROPERTY_ROT;
            float rot = m_startingRot + sqrt((float)(offsetx*offsetx) + (float)(offsety*offsety))/250.0;
            std::string value = std::to_string(rot);
            table->setProperty(prop, value);
            tmppolgyon->OnUpdatePositions();
            Refresh();
           }else if (m_fResizeNode)
           {


           }else
           {
               if(!m_fstartedToScroll)
               {

                   m_startScrollPos.x = event.GetX();
                   m_startScrollPos.y = event.GetY();
                   m_oldscrolloffsetx = m_scrolloffsetx;
                   m_oldscrolloffsety = m_scrolloffsety;

                   m_fstartedToScroll = true;

               }

               m_scrolloffsetx = event.GetX() - m_startScrollPos.x + m_oldscrolloffsetx;
               m_scrolloffsety = event.GetY() - m_startScrollPos.y + m_oldscrolloffsety;

               Refresh();
           }
    }

    if(m_fResizeNode)
    {
        auto node = m_selectedTmpNode.lock();
        if(node)
        {
            wxPoint pos(event.GetX()-m_scrolloffsetx, event.GetY()-m_scrolloffsety);
            int iside = node->HitTestSide(pos);
            if(iside > 0)
            {
                m_ipaintSide = iside;
            }

            if(m_ipaintSide > 0)
            {
                int offsetx = event.GetX() - m_startPoint.x;
                int offsety = event.GetY() - m_startPoint.y;

                int distx = node->m_x - event.GetX();
                int disty = node->m_y - event.GetY();
                if(event.LeftIsDown())
                {
                    if(iside == 1 || iside == 3)
                    {
                        std::string prop = NODE_PROPERTY_WIDTH;

                        float dist = sqrt((float)(distx*distx) + (float)(disty*disty));
                        int width;

                        if(dist>m_startingDist)
                            width = m_istartWidth + sqrt((float)(offsetx*offsetx) + (float)(offsety*offsety))*2;
                        else
                            width = m_istartWidth - sqrt((float)(offsetx*offsetx) + (float)(offsety*offsety))*2;

                        std::string value = std::to_string(width);
                        node->setProperty(prop, value);
                    }else if(iside == 2 || iside == 4)
                     {
                        std::string prop = NODE_PROPERTY_HEIGHT;
                        float dist = sqrt((float)(distx*distx) + (float)(disty*disty));
                        int height;

                        if(dist>m_startingDist)
                            height = m_istartWidth + sqrt((float)(offsetx*offsetx) + (float)(offsety*offsety))*2;
                        else
                            height = m_istartWidth - sqrt((float)(offsetx*offsetx) + (float)(offsety*offsety))*2;

                        std::string value = std::to_string(height);
                        node->setProperty(prop, value);
                     }

                    node->OnUpdatePositions();
                }
            }

        }

        Refresh();
    }


     event.Skip();
}

void MainFrameBaseClass::deactivatePolygonMotion()
{
     m_fTranslateNode = false;
     m_fMouseStarted = false;
     m_fRotatedNode = false;
     m_fResizeNode = false;
     m_fstartedToScroll = false;
     m_ipaintSide = -1;
     auto ptr = m_selectedTmpNode.lock();

     if(ptr)
     {
        auto polygon = std::dynamic_pointer_cast<c4PolgoneRectangle>(ptr);
        if(polygon)
        {
            polygon->m_color = polygon->m_filling;
            Refresh();
        }
     }
}

void MainFrameBaseClass::OnFloorPlanLeftUp(wxMouseEvent &event)
 {
     deactivatePolygonMotion();
     event.Skip();
 }

void MainFrameBaseClass::OnFloorPlanRightUp( wxMouseEvent& event ) {
	event.Skip();

	auto floor = g_activeLevel.lock();
	if(!floor)
		return;

		bool quit = false;

		wxPoint point(event.GetX()-m_scrolloffsetx, event.GetY()-m_scrolloffsety);

		auto iter = floor->m_children.begin();
		for(; iter != floor->m_children.end() and !quit; ++iter)
		{
			auto ptr = (*iter).get();
            c4PolgoneRectangle *bPtr = dynamic_cast<c4PolgoneRectangle*>(ptr);
            if ( bPtr )
			{

                for(auto titer = bPtr->m_children.begin(); titer != bPtr->m_children.end(); ++titer)
                {
                    c4PolgoneRectangle *btptr = dynamic_cast<c4PolgoneRectangle*>(titer->get());
                    if(btptr)
                    {
                        if(btptr->HitTest(point))
                        {
                            m_selectedTmpNode = std::dynamic_pointer_cast<c4PolgoneRectangle>(*titer);

                            btptr->m_color = wxColor(0, 255, 0);
                            wxPoint contextPoint = ScreenToClient( wxGetMousePosition() ); //ScreenToClient( point );
                            PopupMenu(m_menuTable, contextPoint.x, contextPoint.y);
                            //(*iter)->Activate();
                            //break;
                            Refresh();

                            quit = true;
                            break;
                        }


                    }
                }

                if(quit)
                    break;
				//auto casted = std::dynamic_pointer_cast<cTableNode>(*iter);

				if(bPtr->HitTest(point))
				{
				    m_selectedTmpNode = std::dynamic_pointer_cast<c4PolgoneRectangle>(*iter);

                    bPtr->m_color = wxColor(0, 255, 0);
					wxPoint contextPoint = ScreenToClient( wxGetMousePosition() ); //ScreenToClient( point );
					PopupMenu(m_menuTable, contextPoint.x, contextPoint.y);
					//(*iter)->Activate();
					//break;
					Refresh();
				}
			}
		}

	}

void MainFrameBaseClass::OnLevelMapPaint( wxPaintEvent& event )
{
	wxPaintDC dc( m_panelLevelMap );
    renderLayout(dc);
}

/*
 * Here we do the actual rendering. I put it in a separate
 * method so that it can work no matter what type of DC
 * (e.g. wxPaintDC or wxClientDC) is used.
 */
void MainFrameBaseClass::renderLayout(wxDC&  dc)
{
    // draw some text
    //dc.DrawText(wxT("Testing"), 40, 60);

    if(m_floorbitmap.IsOk())
        dc.DrawBitmap(m_floorbitmap, wxPoint(m_scrolloffsetx,m_scrolloffsety));
	//dc.DrawBitmap();
    // draw a circle
    //dc.SetBrush(*wxGREEN_BRUSH); // green filling
   // dc.SetPen( wxPen( wxColor(255,0,0), 5 ) ); // 5-pixels-thick red outline
   // dc.DrawCircle( wxPoint(200,100), 25 /* radius */ );

    // draw a rectangle
    //dc.SetBrush(*wxBLUE_BRUSH); // blue filling
    //dc.SetPen( wxPen( wxColor(255,175,175), 10 ) ); // 10-pixels-thick pink outline
    //dc.DrawRectangle( 300, 100, 400, 200 );

    // draw a line
    //dc.SetPen( wxPen( wxColor(0,0,0), 3 ) ); // black line, 3 pixels thick
    //dc.DrawLine( 300, 100, 700, 300 ); // draw line across the rectangle

	//dc.DrawPolygon()
	auto node = g_activeLevel.lock();
	if(node)
	{
		auto iter = node->m_children.begin();
		for(; iter != node->m_children.end(); ++iter)
		{
		    for(auto titer = (*iter)->m_children.begin(); titer != (*iter)->m_children.end(); ++titer)
                (*titer)->OnPaint(&dc, m_scrolloffsetx, m_scrolloffsety);

			(*iter)->OnPaint(&dc, m_scrolloffsetx, m_scrolloffsety);
		}
	}

	if(m_ipaintSide)
    {

        auto pn = m_selectedTmpNode.lock();
        if(m_ipaintSide == 1)
        {
             dc.SetPen( wxPen( wxColor(0,0,255), 3));
             dc.DrawLine(pn->m_point[0].x + m_scrolloffsetx, pn->m_point[0].y + m_scrolloffsety,
                            pn->m_point[1].x + m_scrolloffsetx, pn->m_point[1].y  + m_scrolloffsety);
        }else if(m_ipaintSide == 2)
        {
             dc.SetPen( wxPen( wxColor(0,0,255), 3));
             dc.DrawLine(pn->m_point[1].x + m_scrolloffsetx, pn->m_point[1].y  + m_scrolloffsety,
                            pn->m_point[2].x + m_scrolloffsetx, pn->m_point[2].y + m_scrolloffsety);
        }else if(m_ipaintSide == 3)
        {
             dc.SetPen( wxPen( wxColor(0,0,255), 3));
             dc.DrawLine(pn->m_point[2].x + m_scrolloffsetx, pn->m_point[2].y + m_scrolloffsety,
                            pn->m_point[3].x + m_scrolloffsetx, pn->m_point[3].y + m_scrolloffsety);

        }else if(m_ipaintSide == 4)
        {
             dc.SetPen( wxPen( wxColor(0,0,255), 3));
             dc.DrawLine(pn->m_point[3].x+ m_scrolloffsetx, pn->m_point[3].y+ m_scrolloffsety,
                            pn->m_point[0].x+ m_scrolloffsetx, pn->m_point[0].y+ m_scrolloffsety);

        }
    }
    // Look at the wxDC docs to learn how to draw other stuff
}

void MainFrameBaseClass::OnBookingsPanelPaint( wxPaintEvent &event)
{
	wxPaintDC dc( m_panelBookingsPaintPanel );
    renderBookings(dc);
}

void MainFrameBaseClass::getTimeFromBookingsClick(int y_position_panel, wxDateTime &result)
{
    if (y_position_panel - m_scrolloffsetbookingsy < 50)
        return;
    if (y_position_panel - m_scrolloffsetbookingsy > 50+35*24)
        return;

    result = m_calendarBookings->GetDate();
    float i = ((float)y_position_panel - 50.0 - (float)m_scrolloffsetbookingsy)/35.0;
    int j = i; /// cut off the eccress
    result.SetHour(i);
    if(i-(float)j > 0)
    result.SetMinute((i-(float)j)*60.0);

    std::cout << result.GetHour() << std::endl;
    std::cout << result.GetMinute() << std::endl;

    int testing = 0;
}

int MainFrameBaseClass::getYPositionBookingsFromTime(wxDateTime& datetime)
{

}

void MainFrameBaseClass::getTableFromBookingsXPos(int x_positon_panel, int &tableid)
{


}

void MainFrameBaseClass::getXPositionFromTableId(int id, int &position)
{

}


void MainFrameBaseClass::renderBookings(wxDC &dc)
{



    int format = 12;
    if(g_settingsmap.find(SETTING_TIMEFORMAT) != g_settingsmap.end())
    {
        format = std::atoi(g_settingsmap[SETTING_TIMEFORMAT].c_str());
    }

    if(format == 12)
    {
            for(int i = 0; i<=12;++i)
            {
                std::string tmp = std::to_string(i);
                dc.DrawText(wxString(tmp), 10+m_scrolloffsetbookingsx, i*35+50+m_scrolloffsetbookingsy);
            }
           for(int i = 1; i<=12;++i)
            {
                std::string tmp = std::to_string(i);
                dc.DrawText(wxString(tmp), 10+m_scrolloffsetbookingsx, (i+12)*35+50+m_scrolloffsetbookingsy);
            }

    }else if(format == 24)
    {
        for (int i = 0; i <= 24; ++i)
        {
            std::string tmp = std::to_string(i);
            dc.DrawText(wxString(tmp), 10 + m_scrolloffsetbookingsx, i * 35 + 50 + m_scrolloffsetbookingsy);
        }
    }

    /// now draw the tables
    auto floor = g_activeLevel.lock();

    if(floor)
    {


        int id = 0;
        for(auto iter = floor->m_children.begin(); iter != floor->m_children.end(); ++iter)
        {
            auto ptr = std::dynamic_pointer_cast<cTableNode>(*iter);
            /// cast was successfull
            if(ptr)
            {
                int position = id*50+50;
                std::string tmp = std::to_string(id);
                dc.DrawText(wxString(tmp), position + m_scrolloffsetbookingsx, m_scrolloffsetbookingsy);
                dc.DrawLine( position + m_scrolloffsetbookingsx + 25, m_scrolloffsetbookingsy,  position + m_scrolloffsetbookingsx + 25,
                                    24 * 35 + 50 + m_scrolloffsetbookingsy);
                ++id;
            }
        }
    }
}


void MainFrameBaseClass::OnTreeRightButtonClick( wxMouseEvent& event )
{
    int flags;

    wxTreeItemId itemId = m_treeCtrlMain->HitTest( wxPoint(event.GetX(), event.GetY() )
                                       , flags
                                       );

    if ( wxTREE_HITTEST_ONITEMICON & flags )
    {
	}
}

void MainFrameBaseClass::OnTreeLeftButtonClick( wxMouseEvent& event )
{
    int flags;

    wxTreeItemId itemId = m_treeCtrlMain->HitTest( wxPoint(event.GetX(), event.GetY() )
                                       , flags
                                       );

    if ( wxTREE_HITTEST_ONITEMICON & flags )
    {
        //Don't pass the event to other controls!!!

      //  CMyTreeItemData *item = (CMyTreeItemData *)m_treeCtrlMain->GetItemData( itemId );

       // CheckItemsRecursively( itemId
                             //, !( item->IsChecked() )
                             //);

       // UpdateCountField();

        //wxLogMessage( _T( "Hit wxTREE_HITTEST_ONITEMICON" ) );
		//wxMessageBox(_T("message"),_T("message"));
	}
	else
	{
        //Pass the event to other controls
        event.Skip();
	}
}   /* CMyTreeCtrl::OnLeftButtonClick */

void MainFrameBaseClass::OnTreeLeftButtonDClick( wxMouseEvent& event )
{
    int flags;

    wxTreeItemId itemId = m_treeCtrlMain->HitTest( wxPoint(event.GetX(), event.GetY() )
                                       , flags
                                       );

    if ( !( wxTREE_HITTEST_ONITEMICON & flags ) )
    {
		auto itn = TREES::g_treeitemid2primitivenode.find( (size_t)itemId.GetID() );
        if(itn != TREES::g_treeitemid2primitivenode.end())
		{
			auto tn = itn->second.lock();
			if(tn)
            {
                auto ptn = std::dynamic_pointer_cast<cTreeNode>(tn);
                if(ptn)
                    ptn->Activate(true);

                Refresh();

            }
		}
		//Pass the event to other controls, since the double click is not coming
        //from the image area.
        //wxLogMessage( _T( "On LeftButton DoubleClick skipped" ) );
        event.Skip();
    }

    //Don't pass the event to other controls, if the double click is coming from the
    //image area.
    //wxLogMessage( _T( "On LeftButton DoubleClick" ) );
}

 void MainFrameBaseClass::OnTreeItemActivated( wxTreeEvent& event ) { event.Skip(); }
 void MainFrameBaseClass::OnTreeItemMenu( wxTreeEvent& event ) { event.Skip(); }
 void MainFrameBaseClass::OnTreeSelChanged( wxTreeEvent& event ) { event.Skip(); }

 void MainFrameBaseClass::OnComboBoxFloor( wxCommandEvent& event ) { event.Skip(); }
 void MainFrameBaseClass::OnComboBoxSelectTable( wxCommandEvent& event ) { event.Skip(); }
 void MainFrameBaseClass::OnComboboxChair( wxCommandEvent& event ) { event.Skip(); }


void MainFrameBaseClass::OnNewFloorClick( wxCommandEvent& event )
{
    std::shared_ptr<cLevelNode> m_newlevel = std::shared_ptr<cLevelNode>( new cLevelNode );

    auto tpl = g_levelsnode.lock();

    if(tpl)
    {
        std::shared_ptr<cLevelNode> newlevel = std::shared_ptr< cLevelNode >( new cLevelNode );
        auto tp = std::dynamic_pointer_cast<TREES::cPrimitiveNode>( newlevel );
        std::string name = "new floor";
        getTranslation(name, name);
        tp->m_this = tp;
        tpl->appendExistingChild(tp, name);
        newlevel->Activate(true);
    }

    event.Skip(); }
void MainFrameBaseClass::OnNewTableClick( wxCommandEvent& event ) {

   // std::shared_ptr<cLevelNode> m_newlevel = std::shared_ptr<cLevelNode>( new cLevelNode );

    auto tpl = g_levelsnode.lock();

    if(tpl)
    {
        auto level = tpl->m_activeChild.lock();

        if(level)
        {


            std::shared_ptr<cTableNode> newtable = std::shared_ptr< cTableNode >( new cTableNode );
            auto tp = std::dynamic_pointer_cast<TREES::cPrimitiveNode>( newtable );
            std::string name = "new table";
            getTranslation(name, name);
            tp->m_this = tp;
            level->appendExistingChild(tp, name);
            newtable->Activate(true);

            name = NODE_PROPERTY_X;
            std::string value = "100";
            newtable->setNewProperty(name, value);

            name = NODE_PROPERTY_Y;
            value = "100";
            newtable->setNewProperty(name, value);

            name = NODE_PROPERTY_ROT;
            value = "0";
            newtable->setNewProperty(name, value);

            name = NODE_PROPERTY_WIDTH;
            value = "100";
            newtable->setNewProperty(name, value);

            name = NODE_PROPERTY_HEIGHT;
            value = "100";
            newtable->setNewProperty(name, value);
        }
    }
    event.Skip();
}

void MainFrameBaseClass::OnButtonNewChairClick( wxCommandEvent& event ) {

    auto tpl = g_levelsnode.lock();

    if(tpl)
    {
        auto level = tpl->m_activeChild.lock();

        if(level)
        {

            auto table = level->m_activeChild.lock();
            auto casted = std::dynamic_pointer_cast<cTableNode>(table);
            if(table and casted)
            {




            std::shared_ptr<cChairNode> newchair = std::shared_ptr< cChairNode >( new cChairNode );
            auto tp = std::dynamic_pointer_cast<TREES::cPrimitiveNode>( newchair );
            std::string name = "new chair";
            getTranslation(name, name);
            tp->m_this = tp;
            table->appendExistingChild(tp, name);
            newchair->Activate(true);

            name = NODE_PROPERTY_X;
            std::string value = "100";

            value = std::to_string(casted->m_point[0].x);
            newchair->setNewProperty(name, value);

            name = NODE_PROPERTY_Y;
            value = std::to_string(casted->m_point[0].y);
            newchair->setNewProperty(name, value);

            name = NODE_PROPERTY_ROT;
            value = "0";
            newchair->setNewProperty(name, value);

            name = NODE_PROPERTY_WIDTH;
            value = "20";
            newchair->setNewProperty(name, value);

            name = NODE_PROPERTY_HEIGHT;
            value = "20";
            newchair->setNewProperty(name, value);

            }
        }
    }


    event.Skip();
}


 void MainFrameBaseClass::OnFloorPlanFileChanged( wxFileDirPickerEvent& event ) {

    wxString path = event.GetPath();
    std::string spath(path); //if()

    auto activelayer = g_activeLevel.lock();

    if(!activelayer)
        return;


    if(file_exists(spath))
    {
        std::string tmpname = NODE_IMAGE;
        activelayer->setChildXMLValue(tmpname, spath, true);

        m_floorbitmap.LoadFile(spath, wxBITMAP_TYPE_JPEG);
        //m_bitmapPanelMap->(path);

        if(m_floorbitmap.IsOk())
        {
            int width = m_floorbitmap.GetWidth();
            int height = m_floorbitmap.GetHeight();

           // m_panelLevelMap->SetScale( width, height);
//            m_scrollBarHorizontal->SetScrollbar(0, 0, width, 100);
  //          m_scrollBarVertical->SetScrollbar(0,0, height, 100);


      //      m_panelLevelMap->SetScrollbars(1, 1, width, height);
      //      m_panelLevelMap->SetScrollRate(1,1);
      //      m_panelLevelMap->SetMaxSize(wxSize(width, height));
           // m_panelLevelMap->SetScrollPageSize(wxHORIZONTAL , m_floorbitmap.GetWidth());
           // m_panelLevelMap->SetScrollPageSize(wxVERTICAL, m_floorbitmap.GetHeight());
          //  m_panelLevelMap->Set
            Refresh();
        }
    }

      event.Skip();


}


void MainFrameBaseClass::OnLoadFile( wxCommandEvent& event )
{
	event.Skip();

	//m_filePicker = new wxFilePickerCtrl(this, wxID_ANY,
    //                                    wxEmptyString,
    //                                    "Please select an xml file", "*",
    //                                    wxDefaultPosition, wxDefaultSize,
    //                                    wxFLP_OPEN);

    wxFileDialog
        openFileDialog(this, _("Open XYZ file"), "", "",
                       "XYZ files (*.xml)|*.xml", wxFD_OPEN|wxFD_FILE_MUST_EXIST);
    if (openFileDialog.ShowModal() == wxID_CANCEL)
        return;     // the user changed idea...

    // proceed loading the file chosen by the user;
    // this can be done with e.g. wxWidgets input streams:
    std::string filename(openFileDialog.GetPath().mb_str());

	loadXMLFile(filename);

	int test = 0;

}

void MainFrameBaseClass::InitTree()
{
	std::string name = "root";
	std::shared_ptr<TREES::cPrimitiveNode> root;
	std::shared_ptr<TREES::cPrimitiveNode> menu;
	std::shared_ptr<TREES::cPrimitiveNode> layout;
	//std::shared_ptr<TREES::cPrimitiveNode> settings;
	std::shared_ptr<TREES::cPrimitiveNode> bookings;
	//std::shared_ptr<TREES::cPrimitiveNode> ;

	m_treeCtrlMain->appendRoot(root, name);
	g_root = root;

	name = "Menue";
	root->appendNewChild(menu, name);

	name = "Settings";
	std::shared_ptr<cSettingsNode> settings = std::shared_ptr<cSettingsNode>( new cSettingsNode );
	std::shared_ptr<TREES::cPrimitiveNode> pn = std::dynamic_pointer_cast<TREES::cPrimitiveNode>(settings);
	root->appendExistingChild(pn, name);
	settings->loadSettings();

	//std::string xmlfilename = "sample_tempalte.xml";
	//loadXMLFile(
	//root->appendChild("
}
