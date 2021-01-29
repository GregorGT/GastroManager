
#include "gj_mysql.hpp"
#include "../config.h"
#include "sqlite3.h"
#include "../nodes/cBookingNode.hpp"

namespace GJSQL {

sqlite3 *g_db = NULL;


bool g_tables_created = false;

std::string g_databasename;
std::string g_username;
std::string g_password;
std::string g_connection;

static int callback(void* NotUsed, int argc, char** argv, char** azColName) {
    int i;
    for (i = 0; i < argc; i++) {
        printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
    }
    printf("\n");
    return 0;
}

void store_booking_node(std::shared_ptr<cBookingNode> &node)
{
    if(!node)
        return;
// '2007-01-01 10:00:00'
    std::string sqls = "INSERT INTO ";
    sqls += SQL_TABLE_BOOKINGS;
    sqls += " (floors, tables, name, begin, end) VALUES(";
    sqls +=
    sqls += ");";
}

bool create_db_and_tables()
{
    if( g_tables_created)
    {
       return true;
    }
    g_tables_created = true;

/// check for the existance of the database
      std::string databasename;

      if(!getMySQLDatabasename(databasename))
      {
            MessageBoxWithTranslation(std::string("No sql database name entry found in config.txt"));
            return false;

      }

      /// database should be setup now
      std::string bookingstable = "CREATE TABLE IF NOT EXISTS '";
      bookingstable += SQL_TABLE_BOOKINGS;
      bookingstable += "' (";

      bookingstable +="'id' INTEGER PRIMARY KEY AUTOINCREMENT,";
      bookingstable +="'floors' int(11)  NOT NULL default 1,";
      bookingstable +="'tables' varchar(250)  NOT NULL default '',";
      bookingstable +="'name'  varchar(250)  NOT NULL default '',";
      bookingstable +="'begin'  DATETIME NOT NULL default NOW,";
      bookingstable +="'end' DATETIME NOT NULL default NOW";
    //  bookingstable +="INDEX (floors, tables, begin, end, name)";
      bookingstable += ");";

      char *zErrMsg = NULL;
      //std::string sqlstatement = "SELECT COUNT FROM BLA;";
      int rc = sqlite3_exec(g_db, bookingstable.c_str(),  callback, 0, &zErrMsg);
      if( rc!=SQLITE_OK ){
            fprintf(stderr, "SQL error: %s\n", zErrMsg);
            sqlite3_free(zErrMsg);
            return false;
      }

      // now create the index
      bookingstable = "CREATE INDEX IF NOT EXISTS idx_contacts ON bookings (floors, tables, name, begin, end);";
      //g_sqlres = g_sqlstmt->executeQuery(sqlstatement);
      // zErrMsg = NULL;
      //std::string sqlstatement = "SELECT COUNT FROM BLA;";
      rc = sqlite3_exec(g_db, bookingstable.c_str(), callback, 0, &zErrMsg);
      if (rc != SQLITE_OK) {
          fprintf(stderr, "SQL error: %s\n", zErrMsg);
          sqlite3_free(zErrMsg);
          false;
      }


      return true;

}



bool establish_connection()
{
//    if(g_sqlcon) // dont init the sql connection twice
  //      return true;
      if(g_db)
        return true;

      std::string addressstring;
      addressstring;

    //  if(!getMySQLConnectionString(addressstring))
      {
     //       MessageBoxWithTranslation(std::string("No sql address entry found in config.txt"));
     //       return false;
      }

     // std::string username, password, databasename;

   /*   if(!getMySQLUserName(username))
      {
           MessageBoxWithTranslation(std::string("No sql username entry found in config.txt"));
            return false;

      }

      if(!getMySQLUserPassword(password))
      {
           MessageBoxWithTranslation(std::string("No sql password entry found in config.txt"));
            return false;

      }

      if(!getMySQLDatabasename(databasename))
      {
           MessageBoxWithTranslation(std::string("No sql database name entry found in config.txt"));
            return false;

      }*/

      std::string filename = "tmp";
      auto iter = g_settingsmap.find(SETTING_SQLITE_DATABASEFILENAME);

      if(iter != g_settingsmap.end())
        filename = iter->second;

      int rc = sqlite3_open(filename.c_str(), &g_db);

      if( rc ){
          fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(g_db));
          sqlite3_close(g_db);
          return(1);
       }

      //char *zErrMsg = NULL;
      //std::string sqlstatement = "SELECT COUNT FROM BLA;";
      //rc = sqlite3_exec(g_db, sqlstatement.c_str(),  callback, 0, &zErrMsg);
      //if( rc!=SQLITE_OK ){
        //    fprintf(stderr, "SQL error: %s\n", zErrMsg);
          //  sqlite3_free(zErrMsg);
      //}

     // sqlite3_close(g_db);
      /* Create a connection */
     // return false;
     // try{
     // g_sqldriver = get_driver_instance();
      //auto *con = g_sqldriver->connect("tcp://127.0.0.1:3306", "root", "root");

      //g_sqlcon    = g_sqldriver->connect(g_connection.c_str(), g_username.c_str(), g_password.c_str());
      //return g_sqlcon->isValid();

      //} catch (sql::SQLException &e) {

//          std::cout << "# ERR: " << e.what();
  //        std::cout << " (MySQL error code: " << e.getErrorCode();
    //      std::cout << ", SQLState: " << e.getSQLState() << " )" << std::endl;
     // }
      return true;
      //return true;
      /// create init

}

bool init_create_db()
{
    if(establish_connection())
    {
        create_db_and_tables();
    }

}


}
