package org.example.starter.startup

import com.netgrif.application.engine.auth.domain.Authority
import com.netgrif.application.engine.auth.domain.IUser
import com.netgrif.application.engine.auth.domain.User
import com.netgrif.application.engine.auth.domain.UserState
import com.netgrif.application.engine.auth.service.interfaces.IAuthorityService
import com.netgrif.application.engine.auth.service.interfaces.IUserService
import com.netgrif.application.engine.petrinet.domain.roles.ProcessRole
import com.netgrif.application.engine.petrinet.service.interfaces.IProcessRoleService
import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Slf4j
@Component
class TestUserRunner extends AbstractOrderedCommandLineRunner {

    private final IAuthorityService authorityService
    private final IUserService userService
    private final IProcessRoleService processRoleService

    TestUserRunner(IAuthorityService authorityService, IUserService userService, IProcessRoleService processRoleService) {
        this.authorityService = authorityService
        this.userService = userService
        this.processRoleService = processRoleService
    }

    @Value('${nae.starter.test.admins}')
    private long admins
    @Value('${nae.starter.test.users}')
    private long users

    @Override
    void run(String... args) throws Exception {
        Authority adminAuthority = authorityService.getOrCreate(Authority.admin)
        Authority systemAuthority = authorityService.getOrCreate(Authority.systemAdmin)
        Authority userAuthority = authorityService.getOrCreate(Authority.user)
        Set<ProcessRole> allRoles = processRoleService.findAll() as Set<ProcessRole>

        users.times { id ->
            String email = "user${id}@netgrif.com"
            IUser user = userService.findByEmail(email, false)
            if (user == null) {
                userService.saveNew(new User(
                        name: "Test",
                        surname: "User${id}",
                        email: email,
                        password: "password",
                        state: UserState.ACTIVE,
                        authorities: [userAuthority] as Set<Authority>,
                        processRoles: [] as Set<ProcessRole>))
            }
        }
        admins.times { id ->
            String email = "admin${id}@netgrif.com"
            IUser user = userService.findByEmail(email, false)
            if (user == null) {
                userService.saveNew(new User(
                        name: "Test",
                        surname: "Admin${id}",
                        email: email,
                        password: "password",
                        state: UserState.ACTIVE,
                        authorities: [userAuthority, adminAuthority, systemAuthority] as Set<Authority>,
                        processRoles: allRoles))
            }
        }
    }
}
