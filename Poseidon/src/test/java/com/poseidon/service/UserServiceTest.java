package com.poseidon.service;

import com.poseidon.domain.Trade;
import com.poseidon.domain.User;
import com.poseidon.repositories.TradeRepository;
import com.poseidon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

        @Mock
        private UserRepository userRepository;
        @InjectMocks
        private UserService userService;

        @Test
        void findAllTest(){
            User user1 = new User("name","password","user");
            User user2 = new User("name","password","admin");
            when(userRepository.findAll()).thenReturn(Arrays.asList(user1,user2));
            assertEquals(2,userService.findAll().size());

        }

        @Test
        void findByIdTest(){
            User user = new User("name","password","user");
            user.setId(1);

            when(userRepository.findById(1)).thenReturn(Optional.of(user));

            User result = userService.findById(1);
            assertEquals(1, result.getId());
        }

        @Test
        void FindByIDNotFoundTest() {
            when(userRepository.findById(1)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> {
                userService.findById(1);
            });
        }

        @Test
        void addUserTest(){
            User user = new User("name","password","user");
            userService.addUser(user);
            verify(userRepository,times(1)).save(user);

        }

        @Test
        void deleteByIdTest(){
            userService.deleteById(1);
            verify(userRepository, times(1)).deleteById(1);
        }


    }

