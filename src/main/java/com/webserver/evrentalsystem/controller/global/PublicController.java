package com.webserver.evrentalsystem.controller.global;

import com.webserver.evrentalsystem.model.dto.entitydto.StationDto;
import com.webserver.evrentalsystem.model.dto.entitydto.VehicleDto;
import com.webserver.evrentalsystem.service.global.PublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/public")
@Tag(name = "0. Public", description = "API công khai")
public class PublicController {

    @Autowired
    private PublicService publicService;

    @Operation(
            summary = "Xem danh sách trạm",
            description = "Trả về danh sách các trạm cho thuê xe cùng vị trí (latitude, longitude)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách trạm",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StationDto.class)))
            }
    )
    @GetMapping("/stations")
    public ResponseEntity<List<StationDto>> getStations() {
        List<StationDto> stations = publicService.getStations();
        return ResponseEntity.ok(stations);
    }

    @Operation(
            summary = "Xem danh sách xe có sẵn",
            description = "Trả về danh sách xe có sẵn, hỗ trợ lọc theo loại, trạm, giá",
            parameters = {
                    @Parameter(name = "type", description = "Loại xe (motorbike | car)"),
                    @Parameter(name = "station_id", description = "ID trạm"),
                    @Parameter(name = "price_min", description = "Giá thấp nhất"),
                    @Parameter(name = "price_max", description = "Giá cao nhất"),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách xe",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VehicleDto.class)))
            }
    )
    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleDto>> getVehicles(
            @RequestParam(required = false) String type,
            @RequestParam(required = false, name = "station_id") Long stationId,
            @RequestParam(required = false, name = "price_min") Double priceMin,
            @RequestParam(required = false, name = "price_max") Double priceMax
    ) {
        List<VehicleDto> vehicles = publicService.getVehicles(type, stationId, priceMin, priceMax);
        return ResponseEntity.ok(vehicles);
    }
}
