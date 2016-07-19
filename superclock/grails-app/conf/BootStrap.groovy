import com.mathifonseca.superclock.Authority
import com.mathifonseca.superclock.Role
import com.mathifonseca.superclock.User
import com.mathifonseca.superclock.UserRole
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        if (Environment.current != Environment.PRODUCTION) {
            createUserAndRole('user', Authority.USER.toString())
            createUserAndRole('user2', Authority.USER.toString())
            createUserAndRole('admin', Authority.ADMIN.toString())
        }
    }

    private void createUserAndRole(String username, String authority) {

        Role role = Role.findByAuthority(authority)

        if (!role) {
            role = new Role(authority: authority)
            role.save(flush: true)
        }

        User user = User.findByUsername(username)

        if (!user) {
            user = new User(username: username, password: username)
            user.save(flush: true)
        }

        UserRole userRole = UserRole.findByUserAndRole(user, role)

        if (!userRole) {
            userRole = new UserRole(user: user, role: role)
            userRole.save(flush: true)
        }

    }

    def destroy = {
    }
}