package group2.cs542.wpi.privateaudio.database;

/**
 * Created by sylor on 3/25/17.
 */

public abstract class SQLCommand {
//    //list all data in books table
//    public static String QUERY_1 = "select lbcallnum, lbtitle from libbook";
//    //List the call numbers of books with the title ‘Database Management’
//    public static String QUERY_2 = "select lbcallnum from libbook where lbtitle like '%Database Management%'";
//    public static String QUERY_3 = "select lbcallnum from libbook where lbtitle like '%Software Engineering%'";
//    public static String QUERY_4 = "select lbcallnum from libbook where lbtitle like '%Attitudes and Behavior%'";
//    public static String QUERY_5 = "select lbcallnum from libbook where lbtitle like '%Fifth Discipline%'";
//    public static String QUERY_6 = "select lbcallnum from libbook where lbtitle like '%Organizations%'";
//    public static String QUERY_7 = "select lbcallnum from libbook where lbtitle like '%Psychology%'";
//    //Query with parameters
//    public static String RETURN_BOOK = "update checkout set coreturned=? where stid=? and lbcallnum=?";
//    public static String CHECK_BOOK = "insert into checkout(stid,lbcallnum,coduedate,coreturned) values(?,?,?,?)";

    // user login check
    public static String Login_Check = "select uid, count(*) as num_match " +
                                       "from user " +
                                       "where account = ? " +
                                       "and password = ?";

    // remove exising active user
    public static String Remove_Act = "delete from active_user " +
                                      "where uid = ?";

    // update active user date
    public static String Update_Act = "insert into active_user (uid, time) " +
                                      "values (?, ?)";

    // logout
    public static String Logout = "delete from active_user " +
                                  "where uid = ?";

    // register
    public static String Register = "insert into user (account, password) " +
                                    "values (?, ?)";

    // init query friend audio
    public static String Friend_Audio = "select u2.account as acc, v.vid as _id, v.tag as tag, v.time as time, v.audio as path " +
                                        "from user u, user u2, friends f, voice v " +
                                        "where u.uid = f.uid and f.friend = v.uid " +
                                        "and f.friend = u2.uid and u.account = ? " +
                                        "order by v.vid asc;";
    // filt query friend audio
    public static String Friend_Filt = "select u2.account as acc, v.vid as _id, v.tag as tag, v.time as time, v.audio as path " +
                                       "from user u, user u2, friends f, voice v " +
                                       "where u.uid = f.uid and f.friend = v.uid " +
                                       "and f.friend = u2.uid and u.account = ? " +
                                       "and u2.account = ? and v.tag = ? " +
                                       "and v.time = ? order by v.vid asc;";
    // init query personal audio
    public static String Self_Audio = "select u.account as acc, v.vid as _id, v.tag as tag, v.time as time, v.audio as path " +
                                      "from user u, voice v " +
                                      "where u.uid = v.uid " +
                                      "and u.account = ?";
    // filt query personal audio
    public static String Self_Filt = "select u.account as acc, v.vid as _id, v.tag as tag, v.time as time, v.audio as path " +
                                     "from user u, voice v " +
                                     "where u.uid = v.uid " +
                                     "and u.account = ? " +
                                     "and v.tag = ? " +
                                     "and v.time = ?";
    // init query neighbor audio
    public static String Neighbor_Audio = "select u.account as acc, v.vid as _id, v.tag as tag, v.time as time, v.audio as path " +
                                          "from user u, voice v, active_user a " +
                                          "where u.uid = v.uid and v.latitude - a.latitude <= 2 " +
                                          "and v.longitude - a.longitude <= 2 and a.uid = ? " +
                                          "and u.uid != ?";
    // init get marker locations
    public static String Neighbor_Marker = "select u.account, v.latitude, v.longitude " +
                                           "from user u, voice v, active_user a " +
                                           "where u.uid = v.uid and v.latitude - a.latitude <= 2 " +
                                           "and v.longitude - a.longitude <= 2 and a.uid = ? " +
                                           "and u.uid != ?";
    // filt query neighbor audio
    public static String Neighbor_Filt = "select u.account as acc, v.vid as _id, v.tag as tag, v.time as time, v.audio as path " +
                                         "from user u, voice v, active_user a " +
                                         "where u.uid = v.uid and u.uid != ? and a.uid = ? and v.tag = ? " +
                                         "and (v.latitude - a.latitude)*(v.latitude - a.latitude)*10000 + " +
                                         "(v.longitude - a.longitude)*(v.longitude - a.longitude)*6400 " +
                                         "<= (? * ?)";
    // filt query neighbor audio
    public static String Filt_Marker = "select u.account, v.latitude, v.longitude " +
                                       "from user u, voice v, active_user a " +
                                       "where u.uid = v.uid and u.uid != ? and a.uid = ? and v.tag = ? " +
                                       "and (v.latitude - a.latitude)*(v.latitude - a.latitude)*10000 + " +
                                       "(v.longitude - a.longitude)*(v.longitude - a.longitude)*6400 " +
                                       "<= (? * ?)";
    // get location
    public static String Get_Location = "select latitude, longitude " +
                                        "from active_user " +
                                        "where uid = ?";
    // update location
    public static String Update_Loc = "update active_user " +
                                      "set latitude = ?, longitude = ? " +
                                      "where uid = ?";
}
