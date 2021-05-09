package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import pl.dotnet.main.dao.model.User;
import pl.dotnet.main.dto.User.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToDto(User user);
}
