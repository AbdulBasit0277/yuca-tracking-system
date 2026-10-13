package api;

import api.dto.BatchResponse;
import api.dto.CassavaJourneyResponse;
import api.dto.FarmerResponse;
import api.dto.ShipmentResponse;
import api.dto.TrackingUpdateResponse;
import model.CassavaJourney;
import model.TrackingUpdate;
import service.TrackingService;
import utils.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/journeys")
public class JourneyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String batchIdParameter = request.getParameter("batchId");

        if (batchIdParameter == null || batchIdParameter.isBlank()) {

            ApiResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "Batch ID is required.");

            return;
        }

        long batchId;

        try {

            batchId = Long.parseLong(batchIdParameter);

            if (batchId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            ApiResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "Batch ID must be a positive number.");

            return;
        }

        try {

            CassavaJourney journey = TrackingService.viewCassavaJourney(batchId);

            if (journey == null) {

                ApiResponse.error(
                        response, HttpServletResponse.SC_NOT_FOUND, "Cassava journey not found.");

                return;
            }

            CassavaJourneyResponse result =
                    new CassavaJourneyResponse();

            // Batch
            if (journey.getBatch() != null) {

                BatchResponse batchResponse =
                        new BatchResponse(
                                journey.getBatch().getId(),
                                journey.getBatch().getBatchCode(),
                                journey.getBatch().getFarmerId(),
                                journey.getBatch().getQuantity(),
                                journey.getBatch().getHarvestDate(),
                                journey.getBatch().getStatus());

                result.setBatch(batchResponse);
            }

            // Farmer
            if (journey.getFarmer() != null) {
                FarmerResponse farmerResponse = new FarmerResponse(journey.getFarmer().getId(), journey.getFarmer().getName(), journey.getFarmer().getPhoneNumber(), journey.getFarmer().getLocation());

                result.setFarmer(farmerResponse);
            }

            // Shipment
            if (journey.getShipment() != null) {
                ShipmentResponse shipmentResponse = new ShipmentResponse(
                                journey.getShipment().getId(),
                                journey.getShipment().getBatchId(),
                                journey.getShipment().getDriverName(),
                                journey.getShipment().getVehicleNumber(),
                                journey.getShipment().getDestination(),
                                journey.getShipment().getDepartureTime(),
                                journey.getShipment().getArrivalTime(),
                                journey.getShipment().getCurrentLocation(),
                                journey.getShipment().getStatus(),
                                journey.getShipment().getUpdateTime());

                result.setShipment(shipmentResponse);
            }

            // Tracking history
            List<TrackingUpdateResponse> history = new ArrayList<>();

            for (TrackingUpdate update : journey.getTrackingHistory()) {
                TrackingUpdateResponse updateResponse = new TrackingUpdateResponse(update.getId(), update.getShipmentId(), update.getLocation(), update.getStatus(), update.getUpdateTime());

                history.add(updateResponse);
            }
            result.setTrackingHistory(history);
            ApiResponse.success(response, HttpServletResponse.SC_OK, result);

        } catch (Exception e) {
            e.printStackTrace();

            ApiResponse.error(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while retrieving the cassava journey.");
        }
    }
}
