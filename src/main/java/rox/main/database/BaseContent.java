package rox.main.database;


public class BaseContent {

    private String[] tableList;

    public BaseContent(){
        tableList = new String[]{"users", "gameserver"};
}

    public String[] getStandardTables(){
        return tableList;
    }

    public String[] getTableList() {
        return tableList;
    }

    public void setTableList(String[] tableList) {
        this.tableList = tableList;
    }

}
