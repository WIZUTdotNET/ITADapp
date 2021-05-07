//package pl.dotnet.main.mapper;
//
//import org.mapstruct.InheritInverseConfiguration;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import pl.dotnet.main.dao.model.User;
//import pl.dotnet.main.dto.UserDTO;
//
//@Mapper
//public interface UserMapper {
//
//    @Mapping(target = "id", source = "userId")
//    UserDTO userToDto(User user);
//
//    @Mapping(target = "userId", source = "id")
//    @InheritInverseConfiguration
//    User dtoToUser(UserDTO userDto);
//}
