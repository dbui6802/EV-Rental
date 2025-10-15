package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Complaint;
import com.webserver.evrentalsystem.entity.ComplaintStatus;
import com.webserver.evrentalsystem.model.dto.entitydto.ComplaintDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T10:58:28+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class ComplaintMapperImpl implements ComplaintMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RentalMapper rentalMapper;

    @Override
    public ComplaintDto toComplaintDto(Complaint complaint) {
        if ( complaint == null ) {
            return null;
        }

        ComplaintDto.ComplaintDtoBuilder complaintDto = ComplaintDto.builder();

        complaintDto.status( complaintStatusValue( complaint ) );
        complaintDto.id( complaint.getId() );
        complaintDto.rental( rentalMapper.toRentalDto( complaint.getRental() ) );
        complaintDto.renter( userMapper.toUserDto( complaint.getRenter() ) );
        complaintDto.staff( userMapper.toUserDto( complaint.getStaff() ) );
        complaintDto.admin( userMapper.toUserDto( complaint.getAdmin() ) );
        complaintDto.description( complaint.getDescription() );
        complaintDto.resolution( complaint.getResolution() );
        complaintDto.createdAt( complaint.getCreatedAt() );
        complaintDto.resolvedAt( complaint.getResolvedAt() );

        return complaintDto.build();
    }

    private String complaintStatusValue(Complaint complaint) {
        if ( complaint == null ) {
            return null;
        }
        ComplaintStatus status = complaint.getStatus();
        if ( status == null ) {
            return null;
        }
        String value = status.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }
}
