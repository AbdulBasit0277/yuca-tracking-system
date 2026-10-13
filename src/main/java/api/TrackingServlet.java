package api;

import api.dto.ShipmentTrackingResponse;
import api.dto.TrackingUpdateRequest;
import api.dto.TrackingUpdateResponse;
import api.validation.TrackingUpdateValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Shipping;
import model.TrackingUpdate;
import service.ShipmentService;
import service.TrackingService;
import utils.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TrackingServlet", urlPatterns = {"/api/tracking"})
public class TrackingServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String shipmentIdParameter = request.getParameter("shipmentId");

        if (shipmentIdParameter == null || shipmentIdParameter.isBlank()) {

            ApiResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "Shipment ID is required.");

            return;
        }

        long shipmentId;

        try {
            shipmentId = Long.parseLong(shipmentIdParameter);

            if (shipmentId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            ApiResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "Shipment ID must be a positive number.");

            return;
        }

        try {
            Shipping shipment = ShipmentService.getShipmentById(shipmentId);

            if (shipment == null) {

                ApiResponse.error(response, HttpServletResponse.SC_NOT_FOUND, "Shipment not found.");
                return;
            }

            List<TrackingUpdate> trackingHistory = TrackingService.viewTrackingHistory(shipmentId);

            ShipmentTrackingResponse result = new ShipmentTrackingResponse();

            result.setShipmentId(shipment.getId());
            result.setBatchId(shipment.getBatchId());
            result.setDriverName(shipment.getDriverName());
            result.setVehicleNumber(shipment.getVehicleNumber());
            result.setDestination(shipment.getDestination());
            result.setDepartureTime(shipment.getDepartureTime());
            result.setArrivalTime(shipment.getArrivalTime());
            result.setCurrentLocation(shipment.getCurrentLocation());
            result.setStatus(shipment.getStatus());
            result.setUpdateTime(shipment.getUpdateTime());

            List<TrackingUpdateResponse> history = new ArrayList<>();

            for (TrackingUpdate update : trackingHistory) {

                TrackingUpdateResponse updateResponse = new TrackingUpdateResponse(update.getId(), update.getShipmentId(), update.getLocation(), update.getStatus(), update.getUpdateTime());

                history.add(updateResponse);
            }

            result.setTrackingHistory(history);

            ApiResponse.success(response, HttpServletResponse.SC_OK, result);

        } catch (Exception e) {
            e.printStackTrace();

            ApiResponse.error(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while retrieving tracking information.");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            TrackingUpdateRequest trackingRequest = objectMapper.readValue(request.getReader(), TrackingUpdateRequest.class);

            // Validate incoming API request
            TrackingUpdateValidator.validate(trackingRequest);

            String normalizedStatus = TrackingUpdateValidator.normalizeStatus(trackingRequest.getStatus());

            TrackingUpdate trackingUpdate = new TrackingUpdate(trackingRequest.getShipmentId(), trackingRequest.getLocation().trim(), normalizedStatus);

            TrackingService.addTrackingUpdate(trackingUpdate);

            Map<String, Object> result = new HashMap<>();

            result.put("message", "Tracking information updated successfully.");

            result.put("shipmentId", trackingRequest.getShipmentId());

            result.put("location", trackingRequest.getLocation().trim());

            result.put("status", normalizedStatus);

            ApiResponse.success(response, HttpServletResponse.SC_CREATED, result);

        } catch (IllegalArgumentException | IllegalStateException e) {

            ApiResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {

            ApiResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON request body.");

        } catch (Exception e) {
            e.printStackTrace();

            ApiResponse.error(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the tracking update.");
        }
    }
}
