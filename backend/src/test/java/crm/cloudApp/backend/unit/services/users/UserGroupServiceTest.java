package crm.cloudApp.backend.unit.services.users;

import crm.cloudApp.backend.config.AppConstants;
import crm.cloudApp.backend.dto.users.UserGroupDTO;
import crm.cloudApp.backend.mappers.users.UserGroupMapper;
import crm.cloudApp.backend.models.users.User;
import crm.cloudApp.backend.models.users.UserGroup;
import crm.cloudApp.backend.repositories.users.UserGroupRepository;
import crm.cloudApp.backend.services.users.UserGroupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserGroupServiceTest {

    @InjectMocks
    UserGroupService userGroupService;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private UserGroupMapper userGroupMapper;

    @Before
    public void setUp() {
        User user1 = User.builder()
                .username("user1")
                .email("user1@mail.com")
                .password("12345")
                .status(AppConstants.Types.UserStatus.enabled)
                .country(null)
                .build();

        User user2 =  User.builder()
                .username("user2")
                .email("user1@mail.com")
                .password("12345")
                .status(AppConstants.Types.UserStatus.enabled)
                .country(null)
                .build();

         user1.setId(1L);
        user2.setId(2L);
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        UserGroup userGroup = new UserGroup("User group 1","User group 1",0,users,null,null,  AppConstants.Types.UserGroupStatus.enabled);
        userGroup.setId(1L);

        when(userGroupRepository.findById(userGroup.getId())).thenReturn(Optional.of(userGroup));
        when(
                userGroupMapper.map(userGroup)
        ).thenReturn( new UserGroupDTO("User group 1","User group 1",0,null,true) );


        List<UserGroup> userGroups = new ArrayList<>();
        userGroups.add(userGroup);
        Page<UserGroup> userGroupsPage = new PageImpl<UserGroup>(userGroups);
        Pageable pageable = PageRequest.of(0, 20);
        List<UserGroupDTO> userGroupDTOsList =  new ArrayList<>();
        userGroupDTOsList.add(
                new UserGroupDTO("User group 1","User group 1",0,new ArrayList<>(),true)
        );
        Page<UserGroupDTO> userGroupsDTOPage = new PageImpl<UserGroupDTO>(userGroupDTOsList);

        when(userGroupRepository.findAll(pageable)).thenReturn(userGroupsPage);
        when(userGroupMapper.map(userGroupsPage)).thenReturn(userGroupsDTOPage);
    }

    @Test
    public void getUserGroup_test() {

        UserGroupDTO userGroupDTO = userGroupService.getUserGroup(1L);
        assertThat(userGroupDTO.getName())
                .isEqualTo("User group 1");
    }

    @Test
    public void getUserGroups_test() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<UserGroupDTO> userGroupDTOsPage = userGroupService.getUserGroups(pageable);
        List<UserGroupDTO> userGroupDTOs = userGroupDTOsPage.get().collect(Collectors.toList());
        assertThat(userGroupDTOsPage.getTotalElements())
                .isEqualTo(1);

        assertThat(userGroupDTOs.get(0).getUsers().size()).isEqualTo(0);
    }

}
