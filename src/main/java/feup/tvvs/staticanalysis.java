package feup.tvvs;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import org.h2.tools.DeleteDbFiles;

class Staticanalysis {

    public static void main(String[] args) {

        Staticanalysis.insertAndPrint();

    }

    public static Integer insertAndPrint() {

        Double total = 0.0;
        Integer count = 0;

        try {
            try (Connection conn = DriverManager.getConnection("jdbc:h2:~/test")) {
                String servername;
                try (Statement stat = conn.createStatement()) {
                    servername = InetAddress.getLocalHost().getHostName();

                    try {
                        ResultSet rs = stat.executeQuery("Select STARTTIME, ENDTIME, ACTIVITYNAME, SERVERNAME from ACTIVITY where SERVERNAME = '" + servername + "'");
                        while (rs.next()) {

                            count++;

                            Date start = rs.getTimestamp(1);
                            Date end = rs.getTimestamp(2);
                            String activityName = rs.getString(3);
                            String serverName = rs.getString(4);

                            //print query result to console
                            System.out.println("activity: " + activityName);
                            System.out.println("local: " + serverName);
                            System.out.println("start: " + start);
                            System.out.println("end: " + end);
                            if (total != 0) {
                                System.out.println("% done: " + (count / total) * 100.00);
                            } else {
                                System.out.println("Total is 0");
                            }

                            System.out.println("--------------------------");
                        }
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // delete the database named 'test' in the user home directory
                    DeleteDbFiles.execute("~", "test", true);
                    //create table
                    stat.execute("CREATE TABLE ACTIVITY (ID INTEGER, STARTTIME datetime, ENDTIME datetime, SERVERNAME VARCHAR(200), ACTIVITYNAME VARCHAR(200), PRIMARY KEY (ID))");
                }

                //prepared statement
                try (PreparedStatement prep = conn.prepareStatement("INSERT INTO ACTIVITY (ID, STARTTIME, ENDTIME, SERVERNAME, ACTIVITYNAME) VALUES (?,?,?,?,?)")) {

                    //insert 10 row data
                    for (int i = 1; i <= 10; i++) {
                        prep.setLong(1, i);
                        prep.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        Thread.sleep(500);
                        prep.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                        prep.setString(4, servername);
                        prep.setString(5, "Activity-" + i);

                        //batch insert
                        prep.addBatch();
                    }
                    conn.setAutoCommit(false);
                    prep.executeBatch();
                }
                conn.setAutoCommit(true);

                //query to database

                //close connection
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}



