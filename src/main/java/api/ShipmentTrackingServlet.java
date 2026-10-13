package api;

import api.dto.ShipmentCreateRequest;
import api.dto.ShipmentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Shipping;
import service.ShipmentService;
import utils.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/shipments")
public class ShipmentTrackingServlet extends HttpServlet {

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

            ShipmentResponse shipmentResponse =
                    new ShipmentResponse(
                            shipment.getId(),
                            shipment.getBatchId(),
                            shipment.getDriverName(),
                            shipment.getVehicleNumber(),
                            shipment.getDestination(),
                            shipment.getDepartureTime(),
                            shipment.getArrivalTime(),
                            shipment.getCurrentLocation(),
                            shipment.getStatus(),
                            shipment.getUpdateTime()
                    );

            ApiResponse.success(response, HttpServletResponse.SC_OK, shipmentResponse);

        } catch (Exception e) {
            e.printStackTrace();

            ApiResponse.error(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while retrieving the shipment.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        ShipmentCreateRequest data;

        try {
            data = objectMapper.readValue(
                    request.getReader(),
                    ShipmentCreateRequest.class
            );
        } catch (Exception e) {
            ApiResponse.error(response, 400, "Invalid JSON request body.");
            return;
        }

        if (data.getBatchId() <= 0) {
            ApiResponse.error(response, 400, "A valid batch ID is required.");
            return;
        }

        if (isBlank(data.getDriverName())
                || isBlank(data.getVehicleNumber())
                || isBlank(data.getDestination())
                || isBlank(data.getCurrentLocation())) {

            ApiResponse.error(
                    response,
                    400,
                    "Driver name, vehicle number, destination, and current location are required."
            );
            return;
        }

        Shipping shipment = new Shipping(
                data.getBatchId(),
                data.getDriverName().trim(),
                data.getVehicleNumber().trim(),
                data.getDestination().trim()
        );

        shipment.setCurrentLocation(data.getCurrentLocation().trim());

        try {
            ShipmentService.createShipment(shipment);

            ApiResponse.success(
                    response,
                    HttpServletResponse.SC_CREATED,
                    "Shipment created successfully."
            );

        } catch (IllegalArgumentException e) {
            ApiResponse.error(response, 400,
                    "The batch is invalid or does not exist.");

        } catch (Exception e) {
            e.printStackTrace();

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to create shipment."
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
