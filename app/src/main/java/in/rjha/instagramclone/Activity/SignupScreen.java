package in.rjha.instagramclone.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import in.rjha.instagramclone.Model.UploadPost;
import in.rjha.instagramclone.Model.User;
import in.rjha.instagramclone.R;

public class SignupScreen extends AppCompatActivity {

    EditText edtemail,edtpassword,edtname,edtmobile,edtabout;
    String email,password,mobile,name,about ;
    Button bsignup;
    StorageReference postrefrance;
    Uri uri;
    CircleImageView profile_image;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        bsignup=findViewById(R.id.bttnsignup);
        edtemail=findViewById(R.id.editText_emailAddress);
        edtpassword=findViewById(R.id.editText_password);
        edtmobile=findViewById(R.id.editText_mobile);
        edtname=findViewById(R.id.editText_name);
        edtabout=findViewById(R.id.about);
        profile_image=findViewById(R.id.profile_image);
        progressDialog=new ProgressDialog(this);
        //get url of firebase
        firebaseAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        postrefrance= FirebaseStorage.getInstance().getReference("userpics");
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickIMage();
            }
        });
        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }
    void pickIMage()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(Intent.createChooser(intent,"select image"),1002);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1002){
            try {
                uri=data.getData();
                Bitmap bm= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                profile_image.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
            }
        }
        //the end  onActivityResult
    }
    public void Register()
    {
        email = edtemail.getText().toString();
        password = edtpassword.getText().toString();
        name=edtname.getText().toString();
        mobile=edtmobile.getText().toString();
        about=edtabout.getText().toString();
        if (email.isEmpty()) {

        }

        else  if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "plzz Enter Password", Toast.LENGTH_SHORT).show();

        }
        else {

            progressDialog.setMessage("Registering User....");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                if (uri!=null)
                                {
                                    senduserdetails(uri);
                                }

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(SignupScreen.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            // ...
                        }
                    });

        }

    }
    public void senduserdetails(Uri uri)
    {


        final FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String id=currentuser.getUid();
        final ProgressDialog progressDialog=new ProgressDialog(SignupScreen.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        UploadTask uploadTask;
        StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/jpeg").build();
        uploadTask=postrefrance.child("insta_"+id).putFile(uri,metadata);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                 final User user = new User(id, name,email,mobile,url,about);
                 mDatabase.child("Users").child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "User Added ", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(SignupScreen.this, HomeActivity.class);
                        startActivity(i);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
