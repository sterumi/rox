package rox.main.database;


public class BaseContent {

    private String[] tableList;

    public BaseContent(){
        tableList = new String[]{"users", "mc_servers"};
}

    public String[] getStandardTables(){
        return tableList;
    }

}
