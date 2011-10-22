package javafun.utils;

public class DomainContext {
    public static Domain getCurrentDomain() {
        Domain domain = new Domain();
        //                domain.setDriver("com.mysql.jdbc.Driver");
        domain.setDriver("org.gjt.mm.mysql.Driver");
        //        domain.setDriver("org.logicalcobwebs.proxool.ProxoolDriver");
        domain.setUrl("jdbc:mysql://localhost:3308/timetracking_dev");
        domain.setUser("root");
        domain.setPwd("root");
        return domain;
    }

}
