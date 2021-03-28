package pl.dotnet.main.mapper;


import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dotnet.main.dao.model.Lecture;
import pl.dotnet.main.dto.LectureDto;

@Mapper
public interface LectureMapper {

    @Mapping(target = "id", source = "lectureId")
    LectureDto lectureToDto(Lecture lecture);

    @Mapping(target = "lectureId", source = "id")
    @InheritInverseConfiguration
    Lecture dtoToLecture(LectureDto lectureDto);
}
