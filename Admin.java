public class Admin extends User{
    int adminID;
    Admin(String userName,String passWord,String fullName,int adminID){
        super(userName, passWord, fullName);
        this.adminID = adminID;
    }
}
