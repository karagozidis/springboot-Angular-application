package crm.cloudApp.backend.unit.repositories.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.repositories.users.UserRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserGroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    User user1;

    @Before
    public void setUp() {
        user1 = new User();
        user1 = User.builder()
                .username("user1")
                .email("user1@mail.com")
                .password("12345")
                .status(AppConstants.Types.UserStatus.enabled)
                .country(null)
                .rolesCrm("admin")
                .rolesMarket("admin")
                .modulesCrm("hyppoio")
                .modulesMarket("market")
                .build();

        user1.setId(1L);
        user1.setCreatedBy("");
        user1.setCreatedOn(Instant.now());
        user1.setModifiedBy("");
        user1.setModifiedOn(Instant.now());


        User user2 = new User();
        user2= User.builder()
                .username("user2")
                .email("user2@mail.com")
                .password("12345")
                .status(AppConstants.Types.UserStatus.enabled)
                .country(null)
                .rolesCrm("admin")
                .rolesMarket("admin")
                .modulesCrm("hyppoio")
                .modulesMarket("market")
                .build();

        user2.setId(2L);
        user2.setCreatedBy("");
        user2.setCreatedOn(Instant.now());
        user2.setModifiedBy("");
        user2.setModifiedOn(Instant.now());

        List<User> users =  new ArrayList<>();
        users.add(user1);
        users.add(user2);

        UserGroup userGroup = new UserGroup("User group 1","User group 1",0,users,null,null,  AppConstants.Types.UserGroupStatus.enabled);
        userGroup.setCreatedBy("");
        userGroup.setCreatedOn(Instant.now());
        userGroup.setModifiedBy("");
        userGroup.setModifiedOn(Instant.now());

        List<User> users2 =  new ArrayList<>();
        users2.add(user2);

        UserGroup userGroup2 = new UserGroup("User group 2","User group 2",0,users2,null,null,  AppConstants.Types.UserGroupStatus.enabled);
        userGroup2.setCreatedBy("");
        userGroup2.setCreatedOn(Instant.now());
        userGroup2.setModifiedBy("");
        userGroup2.setModifiedOn(Instant.now());

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(userGroup);
        entityManager.persist(userGroup2);

    }

    @Test
    public void findById_test() {

        Optional<UserGroup> optionalUserGroupFound = userGroupRepository.findById(1L);

        assertThat(optionalUserGroupFound.isEmpty()).isEqualTo(false);

        assertThat(optionalUserGroupFound.get().getName())
                .isEqualTo("User group 1");

        assertThat(optionalUserGroupFound.get().getUsers().size())
                .isEqualTo(2);

        AssertionsForClassTypes.assertThat(optionalUserGroupFound.get().getUsers().get(0).getId())
                .isEqualTo(2001L);

    }

    @Test
    public void findAll_test() {

        Collection<UserGroup> userGroups = userGroupRepository.findAll();

        assertThat(userGroups.size())
                .isEqualTo(2);
    }

    @Test
    public void findUserGroupsByUserId_test() {

        Collection<UserGroup> a = userGroupRepository.findAll();
        Collection<UserGroup> userGroups = userGroupRepository.findUserGroupsByUserId(user1.getId());

        assertThat(userGroups.size())
                .isEqualTo(1);
    }




}
