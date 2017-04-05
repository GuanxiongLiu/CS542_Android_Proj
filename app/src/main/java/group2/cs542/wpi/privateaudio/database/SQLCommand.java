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
    public static String Login_Check = "select uid, count(*) as num_match from user where account = ? and password = ?";
    // update active user date
    public static String Update_Date = "update active_user set time = ? where uid = ?";
    // init query friend audio
    public static String Friend_Audio = "select u2.account as acc, v.vid as _id, v.tag as tag, v.time as time " +
                                        "from user u, user u2, friends f, voice v " +
                                        "where u.uid = f.uid and f.friend = v.uid " +
                                        "and f.friend = u2.uid and u.account = ? " +
                                        "order by v.vid asc;";
    // filt query friend audio
    public static String Friend_Filt = "select u2.account as acc, v.vid as _id, v.tag as tag, v.time as time " +
                                       "from user u, user u2, friends f, voice v " +
                                       "where u.uid = f.uid and f.friend = v.uid " +
                                       "and f.friend = u2.uid and u.account = ? " +
                                       "and u2.account = ? and v.tag = ? " +
                                       "and v.time = ? order by v.vid asc;";
    // init query personal audio
    public static String Self_Audio = "select u.account as acc, v.vid as _id, v.tag as tag, v.time as time " +
                                      "from user u, voice v " +
                                      "where u.uid = v.uid " +
                                      "and u.account = ?";
    // filt query personal audio
    public static String Self_Filt = "select u.account as acc, v.vid as _id, v.tag as tag, v.time as time " +
                                     "from user u, voice v " +
                                     "where u.uid = v.uid " +
                                     "and u.account = ? " +
                                     "and v.tag = ? " +
                                     "and v.time = ?";
    // init query neighbor audio
    public static String Neighbor_Audio = "select u.account as acc, v.vid as _id, v.tag as tag, v.time as time " +
                                          "from user u, voice v, active_user a " +
                                          "where u.uid = v.uid and v.latitude - a.latitude <= 2 " +
                                          "and v.longitude - a.longitude <= 2 and a.uid = ? " +
                                          "and u.uid != ?";
    // init get marker locations
    public static String Neighbor_Marker = "select v.latitude, v.longitude " +
                                           "from user u, voice v, active_user a " +
                                           "where u.uid = v.uid and v.latitude - a.latitude <= 2 " +
                                           "and v.longitude - a.longitude <= 2 and a.uid = ? " +
                                           "and u.uid != ?";
}
