package pl.dotnet.main.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dotnet.main.dto.Event.EventDTO;
import pl.dotnet.main.dto.Ticket.TicketDTO;
import pl.dotnet.main.dto.User.UpdateUserDTO;
import pl.dotnet.main.dto.User.UserDTO;
import pl.dotnet.main.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/UserEvents")
    public ResponseEntity<List<EventDTO>> getAllUserEvents() {
        return userService.getAllUserEvents();
    }

    @GetMapping("/RegisteredEvents")
    public ResponseEntity<List<EventDTO>> getAllRegisteredEvents() {
        return userService.getAllRegisteredEvents();
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getAllUserTickets() {
        return userService.getAllUserTickets();
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUser() {
        return userService.getUser();
    }

    @PutMapping
    public ResponseEntity<UserDTO> editUser(@RequestBody UpdateUserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @GetMapping("/getUUID")
    public ResponseEntity<UUID> getUserUUID() {
        return userService.getCurrentUserUUID();
    }
}
