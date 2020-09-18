package crm.cloudApp.backend.unit.repositories.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.models.users.Contact;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.repositories.users.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    List<User> users = new ArrayList<>();
    List<Contact> contacts = new ArrayList<>();
    List<UserGroup> userGroups = new ArrayList<>();

    @Before
    public void setUp() {


        User user = createRandomUser();
        user.setStatus(AppConstants.Types.UserStatus.deleted);
        this.users.add(user);
        entityManager.persist(user);

        user = createRandomUser();
        this.users.add(user);
        entityManager.persist(user);

        user = createRandomUser();
        this.users.add(user);
        entityManager.persist(user);

        Contact contact = createRandomContact(users.get(0),users.get(1));
        entityManager.persist(contact);
        contacts.add(contact);

        contact = createRandomContact(users.get(0),users.get(2));
        entityManager.persist(contact);
        contacts.add(contact);

        UserGroup userGroup = createRandomUserGroup(this.users);
        entityManager.persist(userGroup);
        userGroups.add(userGroup);
    }

    private User createRandomUser() {

        User user = new User();
        user = User.builder()
                .username(  RandomStringUtils.random(10, true, true))
                .email( RandomStringUtils.random(5, true, true)+"@mail.com")
                .password( RandomStringUtils.random(10, true, true))
                .status(AppConstants.Types.UserStatus.enabled)
                .country(null)
                .country(null)
                .rolesCrm("admin")
                .rolesMarket("admin")
                .modulesCrm("hyppoio")
                .modulesMarket("market")
                .build();

        user.setCreatedBy("");
        user.setCreatedOn(Instant.now());
        user.setModifiedBy("");
        user.setModifiedOn(Instant.now());

        return user;
    }

    private Contact createRandomContact(User sender, User receiver) {

        Contact contact = new Contact(
                AppConstants.Types.ContactStatus.accepted,
                sender,
                receiver
        );

        contact.setCreatedBy("");
        contact.setCreatedOn(Instant.now());
        contact.setModifiedBy("");
        contact.setModifiedOn(Instant.now());

        return contact;
    }

    private UserGroup createRandomUserGroup(List<User> users) {

        UserGroup userGroup = new UserGroup(
                RandomStringUtils.random(10, true, true),
                RandomStringUtils.random(10, true, true),
                0,
                users,
                null,
                null,
                AppConstants.Types.UserGroupStatus.enabled);


        userGroup.setCreatedBy("");
        userGroup.setCreatedOn(Instant.now());
        userGroup.setModifiedBy("");
        userGroup.setModifiedOn(Instant.now());


        return userGroup;
    }


    @Test
    public void userExists_test() {
            Boolean deletedUserExists = userRepository.userExists(this.users.get(0).getUsername());
            assertThat(deletedUserExists).isEqualTo(false);

            Boolean userExists = userRepository.userExists(this.users.get(1).getUsername());
            assertThat(userExists).isEqualTo(true);
        }


    @Test
    public void findUserById_test() {

        User deletedUser = userRepository.findUserById(this.users.get(0).getId());
        assertThat(deletedUser.getUsername()).isEqualTo(this.users.get(0).getUsername());

        User user = userRepository.findUserById(this.users.get(1).getId());
        assertThat(user.getUsername()).isEqualTo(this.users.get(1).getUsername());
    }

    @Test
    public void findAllById_test() {

        List<Long> ids = new ArrayList<>();
        ids.add(this.users.get(0).getId());
        ids.add(this.users.get(2).getId());
        ids.add(90L);

        List<User> users = userRepository.findAllById(ids);
        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    public void getOnlyContactsOfUserId_test() {

        List<Long> ids = new ArrayList<>();
        ids.add(this.users.get(1).getId());
        ids.add(this.users.get(2).getId());
        ids.add(90L);

        List<User> users = userRepository.getOnlyContactsOfUserId(ids,this.users.get(0).getId());
        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    public void findAllByStatusIsNotLike_test() {
        Collection<User> users = userRepository.findAllByStatusIsNotLike(AppConstants.Types.UserStatus.enabled);
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void findAllNotDeleted_test() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<User> users = userRepository.findAllNotDeleted(pageable);
        assertThat(users.getTotalElements()).isEqualTo(3);
    }

    @Test
    public void findUsersByUserGroupId_test() {
        Pageable pageable = PageRequest.of(0, 20);
        Collection<User> users = userRepository.findUsersByUserGroupId(this.userGroups.get(0).getId());

        long countNotDeleted =  this.userGroups.get(0).getUsers().stream().filter(c -> c.getStatus() != AppConstants.Types.UserStatus.deleted).count();
        assertThat(users.size()).isEqualTo(countNotDeleted);
    }

    @Test
    public void findByUsername_test() {
        Pageable pageable = PageRequest.of(0, 20);
        Optional<User> optionalUser = userRepository.findByUsername(this.users.get(0).getUsername());
        AssertionsForClassTypes.assertThat(optionalUser.get().getId()).isEqualTo(this.users.get(0).getId());
    }


}
