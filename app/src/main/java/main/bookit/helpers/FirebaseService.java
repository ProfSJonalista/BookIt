package main.bookit.helpers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import main.bookit.model.Book;
import main.bookit.model.UserBook;

public class FirebaseService {
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    public FirebaseService(final String TAG){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out.");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public String getUserId(){
        return mAuth.getCurrentUser().getUid();
    }

    public void AddOrUpdateUserBook(UserBook userBookToAdd) {
        myRef.child(Children.USER_BOOKS).child(userBookToAdd.getUserId()).child(userBookToAdd.getBookId()).setValue(userBookToAdd);
    }

    public void AddOrUpdateBook(Book book) {
        myRef.child(Children.BOOKS).child(book.getId()).setValue(book);
    }

    public void onStart() {
        //adds auth listener to the auth manager
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        //if listener is not null, it removes auth listener
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }
}
