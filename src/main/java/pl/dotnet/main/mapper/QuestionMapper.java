package pl.dotnet.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.Question;
import pl.dotnet.main.dto.Question.QuestionDTO;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "userId", source = "question.user.userId")
    @Mapping(target = "name", source = "question.user.name")
    @Mapping(target = "surname", source = "question.user.surname")
    @Mapping(target = "lectureId", source = "question.lecture.lectureId")
    QuestionDTO questionToDTO(Question question);
}
