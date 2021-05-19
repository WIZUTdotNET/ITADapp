package pl.dotnet.main.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dotnet.main.dao.model.Event;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dao.repository.UserRepository;
import pl.dotnet.main.dto.Event.EventDTO;
import pl.dotnet.main.dto.Ticket.TicketDTO;
import pl.dotnet.main.dto.User.UpdateUserDTO;
import pl.dotnet.main.dto.User.UserDTO;
import pl.dotnet.main.expections.NotFoundRequestException;
import pl.dotnet.main.expections.UnauthorizedRequestException;
import pl.dotnet.main.mapper.EventMapper;
import pl.dotnet.main.mapper.TicketMapper;
import pl.dotnet.main.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final TicketMapper ticketMapper;
    private final PasswordEncoder passwordEncoder;

    public String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public User getCurrentUser() {
        String username = getCurrentUserName();
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundRequestException("Co ty tu robisz?"));
    }

    public User getCurrentUserOrNull() {
        String username = getCurrentUserName();
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean validateUser(User user) {
        Optional<User> userFromDBUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> userFromDBEmail = userRepository.findByEmail(user.getEmail());

        return userFromDBUsername.isEmpty() && userFromDBEmail.isEmpty();
    }

    public void isCurrentUserNotTheOwnerOfThisEvent(Event event) {
        User currentUser = userRepository.findByUsername(getCurrentUserName()).orElseThrow(() -> new NotFoundRequestException("User not found"));
        if (!userRepository.findById(event.getOwner().getUserId()).orElseThrow(() -> new UnauthorizedRequestException("User is not the owner of this event")).equals(currentUser)) {
            throw new UnauthorizedRequestException("User is not the owner of this event");
        }
    }

    public ResponseEntity<List<EventDTO>> getAllUserEvents() {
        User currentUser = getCurrentUser();

        return new ResponseEntity<>(currentUser.getOwnedEvents().stream()
                .map(eventMapper::eventToDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    public ResponseEntity<List<EventDTO>> getAllRegisteredEvents() {
        User currentUser = getCurrentUser();
        return new ResponseEntity<>(currentUser.getRegisteredOnEvents().stream()
                .map(ticket -> eventMapper.eventToDto(ticket.getEvent()))
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    public ResponseEntity<List<TicketDTO>> getAllUserTickets() {
        User currentUser = getCurrentUser();

        return new ResponseEntity<>(currentUser.getRegisteredOnEvents().stream()
                .map(ticketMapper::ticketToDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> getUser() {
        User currentUser = getCurrentUser();
        return new ResponseEntity<>(userMapper.userToDto(currentUser), HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> updateUser(UpdateUserDTO userDTO) {
        User currentUser = getCurrentUser();

        userDTO = validUpdatedUser(userDTO, currentUser);

        currentUser.setEmail(userDTO.getEmail());
        currentUser.setName(userDTO.getName());
        currentUser.setSurname(userDTO.getSurname());

        if (!passwordEncoder.matches(userDTO.getPassword(), currentUser.getPassword())) {
            throw new UnauthorizedRequestException("Password is not valid");
        }

        log.info("Activation email sent!!");
        return new ResponseEntity<>(userMapper.userToDto(currentUser), HttpStatus.OK);
    }

    public ResponseEntity<UUID> getCurrentUserUUID() {
        User currentUser = getCurrentUser();
        return new ResponseEntity<>(currentUser.getUserUUID(), HttpStatus.OK);
    }

    public UpdateUserDTO validUpdatedUser(UpdateUserDTO userDTO, User currentUser) {
        if (userDTO.getEmail().isEmpty()) {
            userDTO.setEmail(currentUser.getEmail());
        }
        if (userDTO.getSurname().isEmpty()) {
            userDTO.setName(currentUser.getSurname());
        }
        if (userDTO.getName().isEmpty()) {
            userDTO.setSurname(currentUser.getName());
        }
        if (userDTO.getPassword().isEmpty()) {
            userDTO.setPassword(currentUser.getPassword());
        }
        return userDTO;
    }
}
