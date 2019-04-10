package me.thirtyone.group.mindmaze.data.loaders;

import com.google.firebase.database.DataSnapshot;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.core.Reminder;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.modules.Invite;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.modules.Priority;
import me.thirtyone.group.mindmaze.users.Student;
import me.thirtyone.group.mindmaze.utils.DatabaseUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Abdullah on 23/03/2019 19:49
 * <p>
 * Provides some helper methods to set up users, invites and reminders from the DatabaseAbstracter class.
 */
public class UserLoader {


    public static void addUsersFromDatabase(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            // Need to loop through elements of each child as well
            String email = "";
            String id = "";
            String password = "";
            String username = "";

            List<Priority> priorityList = new LinkedList<>();

            for (DataSnapshot gds : ds.getChildren()) {
                if (gds.getKey().equals("email"))
                    email = gds.getValue().toString();
                if (gds.getKey().equals("id"))
                    id = gds.getValue().toString();
                if (gds.getKey().equals("password"))
                    password = gds.getValue().toString();
                if (gds.getKey().equals("username"))
                    username = gds.getValue().toString();
                if (gds.getKey().equals("modules"))
                    addModulesToUser(gds, priorityList);
            }
            Student s = new Student(username, password, email, id);
            s.setPrioritiesList(priorityList);
            AccountRegistry.getInstance()
                    .addUser(s);
        }
    }

    /**
     * Given a data snapshot pointing to a list of modules for a user, it will populate the list with priorities.
     *
     * @param dataSnapshot The DataSnapshot object that contains our data
     * @param priorities   The (hopefully) empty list of priorities to populate
     */
    private static synchronized void addModulesToUser(DataSnapshot dataSnapshot, List<Priority> priorities) {
        for (DataSnapshot moduleId : dataSnapshot.getChildren()) {
            Module m = AccountRegistry.getInstance().getModuleById(moduleId.getKey());
            if (m == null)
                continue;
            Priority.Value priVal = DatabaseUtils.parsePriorityFromString(moduleId.child("priority").getValue().toString());
            priorities.add(new Priority(m, priVal));

        }
    }

    public static void setupInvitesAndReminders(DataSnapshot dataSnapshot) {

        // For every user
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Student s = (Student) AccountRegistry.getInstance().getUserById(child.getKey());
            List<Reminder> reminderList = new LinkedList<>();
            List<Invite> inviteList = new LinkedList<>();
            // For every child in user
            for (DataSnapshot grandChild : child.getChildren()) {
                // Only concerned with invites. If the key is not invites then continue.
                if (grandChild.getKey().equals("invites"))
                    addInvitesToUser(grandChild, inviteList); // Add all invites from the data snapshot into the list
                if (grandChild.getKey().equals("reminders"))
                    addRemindersToUser(grandChild, s, reminderList);
            }
            s.setInvites(inviteList);
            s.setReminders(reminderList);

        }
    }

    /**
     * Loads the invites from the database into the user object
     *
     * @param dataSnapshot The data snapshot to use to load the objects in
     */
    private static void addInvitesToUser(DataSnapshot dataSnapshot, List<Invite> inviteList) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            String inviteId = child.getKey();
            String senderId = child.child("senderId").getValue().toString();
            String receiverId = child.child("receiverId").getValue().toString();
            String moduleId = child.child("moduleId").getValue().toString();

            Student receiver = (Student) AccountRegistry.getInstance().getUserById(receiverId);
            Student sender = (Student) AccountRegistry.getInstance().getUserById(senderId);
            Module module = AccountRegistry.getInstance().getModuleById(moduleId);

            if (module == null)
                DatabaseAbstracter.getInstance().deleteInviteById(receiverId, inviteId);
            else
                inviteList.add(new Invite(inviteId, sender, receiver, module));
        }
    }

    private static void addRemindersToUser(DataSnapshot dataSnapshot, Student student, List<Reminder> reminderList) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            String reminderId = child.getKey();
            String date = child.child("date").getValue().toString();
            String message = child.child("message").getValue().toString();
            boolean repeating = Boolean.parseBoolean(child.child("isRepeating").getValue().toString());

            reminderList.add(new Reminder(message, new Date(Long.parseLong(date)), reminderId, student, repeating));
        }
    }
}
