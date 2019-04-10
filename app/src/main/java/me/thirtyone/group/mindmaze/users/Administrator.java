package me.thirtyone.group.mindmaze.users;

import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.modules.Module;

import java.util.Iterator;
import java.util.List;

class Administrator extends User {

    public Administrator(String username, String password, String email, String id) {
        super(username, password, email, id);
    }

    public List<Module> viewAllModules() {
        return AccountRegistry.getInstance().getModules();
    }

    public List<Module> viewModulesForStudent(Student student) {
        return student.getModules();
    }

    public void deleteStudentAccount(String studentId) {
        Iterator<User> iter = AccountRegistry.getInstance().getUsers().iterator();

        while (iter.hasNext()) {
            User u = iter.next();
            if (u.getId().equals(studentId)) {
                iter.remove();
                DatabaseAbstracter.getInstance().removeUser(u);
                return;
            }
        }
    }
}