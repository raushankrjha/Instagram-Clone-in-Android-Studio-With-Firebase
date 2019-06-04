package in.rjha.instagramclone.Model;

public class Post {
    public String id;
    public String desc;
    public String email;
    public  String url;
    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String geturl() {
        return url;
    }

    public void seturl(String url) {
        this.url = url;
    }

    public Post(String id, String desc, String email, String url) {
        this.id = id;
        this.desc = desc;
        this.email=email;
        this.url=url;
    }
}
