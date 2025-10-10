package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Complaint;
import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RentalMapper.class})
public interface ComplaintMapper {
    @Mapping(source = "status.value", target = "status") // enum ComplaintStatus -> string
    ComplaintDto toComplaintDto(Complaint complaint);
}
