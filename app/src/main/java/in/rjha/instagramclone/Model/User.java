package in.rjha.instagramclone.Model;

public class User {
    public String id;
    public String name;
    public String email;
    public String mobile;
    public  String url;
    public  String about;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String id, String name, String email,String mobile,String url,String about) {
        this.id = id;
        this.name = name;
        this.email=email;
        this.mobile=mobile;
        this.url=url;
        this.about=about;
    }
}
