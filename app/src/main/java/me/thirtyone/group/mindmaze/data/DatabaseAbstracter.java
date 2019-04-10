package me.thirtyone.group.mindmaze.data;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.firebase.database.*;
import me.thirtyone.group.mindmaze.data.loaders.ModuleLoader;
import me.thirtyone.group.mindmaze.data.loaders.UserLoader;
import me.thirtyone.group.mindmaze.modules.Invite;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.modules.Priority;
import me.thirtyone.group.mindmaze.resources.Image;
import me.thirtyone.group.mindmaze.resources.Note;
import me.thirtyone.group.mindmaze.users.Student;
import me.thirtyone.group.mindmaze.users.User;

import java.util.Date;

/**
 * Created by Abdullah on 13/03/2019 00:03
 * <p>
 * This class intends to make interaction simpler with the firebase database that this application uses.
 * <p>
 * The idea is that we don't want to perform any database operations outside of this class - it
 * just aims to allow the other classes to interface with the database.
 * <p>
 * This is why the instance variable called standard, the firebase database reference is private.
 * <p>
 * Fair warning: the code in this class is not pleasant. There is a lot of parsing from the database done here.
 * In fact I have had to create two classes to separate loading modules from the database and loading users from the database.
 * These two classes will deal with processing the data snapshots and putting them into the account registry.
 * <p>
 * The idea is that I put all the ugly code in here so that other classes can have a pleasant time. I think that is what abstraction is :)
 * Even with the comments provided it might not be so easy to understand unless you are familiar with JSON and/or firebase database.
 * Rest assured a lot of time and thought has been put into this class.
 */
public class DatabaseAbstracter {
    private static DatabaseReference standard = FirebaseDatabase.getInstance().getReference();
    private static DatabaseAbstracter instance;
    private static final String USER_DB_REF = "users";
    private static final String MODULE_DB_REF = "modules";

    private static final String TAG = "DatabaseAbstracter";

    private DatabaseAbstracter() {

        // The below event listener will be called once.
        addModulesFromDatabase();

        addUsersFromDatabase();
    }

    /**
     * To be called exactly once. Calling it thereafter is useless.
     * This method was simply created for clarity in the PreloadActivity class.
     */
    public static void init() {
        if (instance != null)
            return;
        instance = new DatabaseAbstracter();
    }

    public static DatabaseAbstracter getInstance() {
        if (instance == null) {
            instance = new DatabaseAbstracter();
        }
        return instance;
    }

    /**
     * Adds a user to the database
     * Called when user registers
     *
     * @param user The user object to be added to the database
     */
    public void addUser(User user) {
        standard.child(USER_DB_REF).child(user.getId()).setValue(user);
    }

    public void removeUser(User user) {
        standard.child(USER_DB_REF).child(user.getId()).removeValue();
    }

    public void addModule(Module m) {
        standard.child(MODULE_DB_REF).child(m.getId()).setValue(m);
    }

    public void setPriorityValue(User u, Priority p) {
        DatabaseReference moduleRef = standard.child(USER_DB_REF).child(u.getId()).child("modules").child(p.getModule().getId());
        moduleRef.child("priority").setValue(p.getPriority());
    }

    private void addUsersFromDatabase() {
        standard.child(USER_DB_REF).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Loading entries from database into singleton");
                UserLoader.addUsersFromDatabase(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Add the modules from the database to the singleton
     */
    private synchronized void addModulesFromDatabase() {
        standard.child("modules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModuleLoader.addModulesFromDatabase(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * This is called after the modules and users are loaded. We now want to add permissions to the modules for each user.
     */
    public void setupModulePermissions() {
        standard.child(MODULE_DB_REF).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModuleLoader.setupModulePermissions(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * We add the invites and reminders to the user after everything else.
     * Both users and modules need to be cached into the registry so we do this later the operation that creates the registry
     */
    public void setupInvitesAndReminders() {
        standard.child(USER_DB_REF).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserLoader.setupInvitesAndReminders(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setupModuleResources() {
        standard.child(MODULE_DB_REF).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModuleLoader.setupModuleResources(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Updates the permission in the database
     *
     * @param m The module to update permissions for
     * @param s The student (used for id) who has the permission being updated
     * @param p The permission to add
     */
    public void setPermission(Module m, Student s, Module.Permission p) {
        standard.child(MODULE_DB_REF).child(m.getId()).child("permissions").child(s.getId()).setValue(p);
    }

    public void changePassword(Student s, String newPass) {
        standard.child(s.getId()).child("password").setValue(newPass);
    }

    /**
     * Removes a module from a student in the database
     *
     * @param s The student to remove the module from
     * @param m The module to remove from the student
     */
    public void removeModuleFromStudent(Student s, Module m) {
        standard.child(USER_DB_REF).child(s.getId()).child("modules").child(m.getId()).removeValue(); // Removing the module from the user in the user reference
        standard.child(MODULE_DB_REF).child(m.getId()).child("permissions").child(s.getId()).removeValue(); // Removing the permission from the user in the module reference
    }

    public void deleteModule(Module m) {
        standard.child(MODULE_DB_REF).child(m.getId()).removeValue();
    }

    /**
     * Creates an invite in the receiver's database entry
     *
     * @param invite The invite object to pass into the database
     */
    public void createInvite(Invite invite) {
        DatabaseReference inviteRef = standard.child(USER_DB_REF).child(invite.getReceiver().getId()).child("invites").child(invite.getId()); // Reference to this invite's slot
        inviteRef.child("senderId").setValue(invite.getSender().getId());
        inviteRef.child("receiverId").setValue(invite.getReceiver().getId());
        inviteRef.child("moduleId").setValue(invite.getModule().getId());
    }

    public void deleteInviteById(String receiverId, String inviteId) {
        standard.child(USER_DB_REF).child(receiverId).child("invites").child(inviteId).removeValue();
    }

    public void addModuleToStudent(Module module, Student student, Priority.Value priorityValue) {
        standard.child(USER_DB_REF).child(student.getId()).child("modules").child(module.getId()).child("priority").setValue(priorityValue.toString());
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createReminder(String id, String studentId, String message, boolean isRepeating, Date date) {
        standard.child(USER_DB_REF).child(studentId).child("reminders").child(id).child("message").setValue(message);
        standard.child(USER_DB_REF).child(studentId).child("reminders").child(id).child("isRepeating").setValue(isRepeating);
        standard.child(USER_DB_REF).child(studentId).child("reminders").child(id).child("date").setValue(date.toInstant().toEpochMilli());
    }

    /**
     * Add a note to the database
     *
     * @param note The note to add to the database
     */
    public void addNote(Note note) {
        DatabaseReference noteRef = standard.child(MODULE_DB_REF).child(note.getModule().getId()).child("resources").child("notes").child(note.getId());
        noteRef.child("name").setValue(note.getName());
        noteRef.child("content").setValue(note.getContent());
        noteRef.child("id").setValue(note.getId());
        noteRef.child("uploaderId").setValue(note.getUploader().getId());

    }

    public void createImageReference(Image image) {
        DatabaseReference imageRef = standard.child(MODULE_DB_REF).child(image.getModule().getId()).child("resources").child("images").child(image.getId());
        imageRef.child("name").setValue(image.getName());
        imageRef.child("id").setValue(image.getId());
        imageRef.child("uploaderId").setValue(image.getUploader().getId());
        imageRef.child("location").setValue(image.getURL());
    }

}
