package com.webserver.evrentalsystem.controller.renter;

import com.webserver.evrentalsystem.model.dto.entitydto.RatingDto;
import com.webserver.evrentalsystem.model.dto.entitydto.StaffRatingDto;
import com.webserver.evrentalsystem.model.dto.request.RatingRequest;
import com.webserver.evrentalsystem.service.renter.RatingRenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/renter/rating")
@Tag(name = "2.5. Renter Rating", description = "API quản lý đánh giá của renter")
@SecurityRequirement(name = "bearerAuth")
public class RatingRenterController {

    @Autowired
    private RatingRenterService ratingRenterService;

    @Operation(
            summary = "Đánh giá chuyến đi (xe thuê)",
            description = "Renter gửi đánh giá cho chuyến đi sau khi hoàn tất. Bao gồm số sao và nhận xét.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Đánh giá thành công"),
                    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Không có quyền truy cập", content = @Content)
            }
    )
    @PostMapping("/trip")
    public ResponseEntity<RatingDto> rateTrip(@RequestBody RatingRequest request) {
        return ResponseEntity.ok(ratingRenterService.rateTrip(request));
    }

    @Operation(
            summary = "Đánh giá nhân viên",
            description = "Renter gửi đánh giá cho nhân viên phục vụ trong chuyến đi. Bao gồm số sao và nhận xét.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Đánh giá nhân viên thành công"),
                    @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Không có quyền truy cập", content = @Content)
            }
    )
    @PostMapping("/staff")
    public ResponseEntity<StaffRatingDto> rateStaff(@RequestBody RatingRequest request) {
        return ResponseEntity.ok(ratingRenterService.rateStaff(request));
    }
}
