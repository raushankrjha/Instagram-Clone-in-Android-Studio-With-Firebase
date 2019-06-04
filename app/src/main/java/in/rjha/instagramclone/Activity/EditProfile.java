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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import in.rjha.instagramclone.Model.User;
import in.rjha.instagramclone.R;

public class EditProfile extends AppCompatActivity {
    EditText edtname,edtmobile,edtabout;
    String mobile,name,about ;
    Button bupdate;
    StorageReference postrefrance;
    Uri uri;
    CircleImageView profile_image;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    FirebaseUser currentuser;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        bupdate=findViewById(R.id.bttnupdate);
        edtmobile=findViewById(R.id.editText_mobile);
        edtname=findViewById(R.id.editText_name);
        edtabout=findViewById(R.id.about);
        profile_image=findViewById(R.id.profile_image);
        progressDialog=new ProgressDialog(this);
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        id =currentuser.getUid();
        //get url of firebase
        firebaseAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        postrefrance= FirebaseStorage.getInstance().getReference("userpics");
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickIMage();
            }
        });
        bupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                edtname.setText(user.name);
                edtabout.setText(user.about);
                edtmobile.setText(user.mobile);
                Picasso.get()
                        .load(user.url)
                        .placeholder(R.drawable.userpic)
                        .error(R.drawable.userpic)
                        .into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //open gallery
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

    //update data into firebase database

    public void Update()
    {

        name=edtname.getText().toString();
        mobile=edtmobile.getText().toString();
        about=edtabout.getText().toString();
        senduserdetails(uri);


    }
    public void senduserdetails(Uri uri)
    {


        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        id =currentuser.getUid();
        final String email=currentuser.getEmail();
        final ProgressDialog progressDialog=new ProgressDialog(EditProfile.this);
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
                mDatabase.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "User Added ", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(EditProfile.this, HomeActivity.class);
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
