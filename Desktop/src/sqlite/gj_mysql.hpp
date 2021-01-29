#ifndef GJ_MYSQL_H_
#define GJ_MYSQL_H_



namespace GJSQL {

#define SQL_TABLE_BOOKINGS "bookings"
//extern std::string g_databasename;
//extern std::string g_username;/
//extern std::string g_password;
//extern std::string g_connection;
extern sqlite3 *g_db;

bool init_create_db();

class BasicTypes {



};

}

#endif // GS_MYSQL_H
