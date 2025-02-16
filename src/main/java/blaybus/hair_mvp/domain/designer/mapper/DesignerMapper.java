package blaybus.hair_mvp.domain.designer.mapper;

import blaybus.hair_mvp.domain.designer.dto.DesignerResponse;
import blaybus.hair_mvp.domain.designer.entity.Designer;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DesignerMapper {
    DesignerResponse toResponse(Designer designer);
    List<DesignerResponse> toResponseList(List<Designer> designers);
}
