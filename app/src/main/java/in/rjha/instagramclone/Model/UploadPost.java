package in.rjha.instagramclone.Model;

public class UploadPost {
    public String id;
    public String desc;
    public String email;

    public  String url;
    public UploadPost() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UploadPost(String id, String desc,String email,String url) {
        this.id = id;
        this.desc = desc;
        this.email=email;

        this.url=url;
    }
}
