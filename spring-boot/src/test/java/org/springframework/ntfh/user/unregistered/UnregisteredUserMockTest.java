package org.springframework.ntfh.user.unregistered;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ntfh.entity.user.unregistered.UnregisteredUser;
import org.springframework.ntfh.entity.user.unregistered.UnregisteredUserRepository;
import org.springframework.ntfh.entity.user.unregistered.UnregisteredUserService;

/**
 * @author alegestor
 */
@ExtendWith(MockitoExtension.class)
public class UnregisteredUserMockTest {
    
    @Mock
    private UnregisteredUserRepository unregisteredUserRepository;

    protected UnregisteredUserService unregisteredUserService;

    @BeforeEach
    void setup() {
        unregisteredUserService = new UnregisteredUserService(unregisteredUserRepository);
    }

    @Test
    void testCreateUnregisteredUser() {
        UnregisteredUser tester = unregisteredUserService.create();
        
        Collection<UnregisteredUser> sampleUnregisteredUsers = new ArrayList<UnregisteredUser>();
        sampleUnregisteredUsers.add(tester);
        when(unregisteredUserRepository.findAll()).thenReturn(sampleUnregisteredUsers);

        Collection<UnregisteredUser> unregisteredUsers = (Collection<UnregisteredUser>) this.unregisteredUserService.findAll();

        assertEquals(1, unregisteredUsers.size());
        UnregisteredUser unregisteredUser = unregisteredUsers.iterator().next();
        assertEquals(unregisteredUser.getUsername(), tester.getUsername());
    }

    @Test
    void testDeleteUnregisteredUser() {
        UnregisteredUser tester = unregisteredUserService.create();
        
        Collection<UnregisteredUser> sampleUnregisteredUsers = new ArrayList<UnregisteredUser>();
        sampleUnregisteredUsers.add(tester);
        when(unregisteredUserRepository.findAll()).thenReturn(sampleUnregisteredUsers);

        Collection<UnregisteredUser> unregisteredUsers = (Collection<UnregisteredUser>) this.unregisteredUserService.findAll();

        assertEquals(1, unregisteredUsers.size());
        UnregisteredUser unregisteredUser = unregisteredUsers.iterator().next();
        unregisteredUserService.delete(unregisteredUser);
        Boolean res = false;
        if(!unregisteredUserService.findUnregisteredUserById(tester.getUsername()).isPresent()) res = true;
        verify(unregisteredUserRepository, times(1)).delete(unregisteredUser);
        assertEquals(true, res);
    }


}
