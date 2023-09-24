public class User{
    String userName,passWord,fullName;
    User(String userName,String passWord,String fullName){
        this.userName = userName;
        this.passWord = passWord;
        this.fullName = fullName;
    }
    public String getFullName(){
        return this.fullName;
    }
}