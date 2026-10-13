package api.validation;

import api.dto.TrackingUpdateRequest;

public class TrackingUpdateValidator {

    public static void validate(TrackingUpdateRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required.");
        }

        if (request.getShipmentId() <= 0) {
            throw new IllegalArgumentException("Shipment ID must be a positive number.");
        }

        if (request.getLocation() == null || request.getLocation().isBlank()) {

            throw new IllegalArgumentException("Location is required.");
        }

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            throw new IllegalArgumentException("Status is required.");
        }
        String status = normalizeStatus(request.getStatus());

        if (!status.equals("IN_TRANSIT") && !status.equals("ARRIVED")) {
            throw new IllegalArgumentException("Invalid status. Use IN_TRANSIT or ARRIVED.");
        }
    }

    public static String normalizeStatus(String status) {

        return status.trim().toUpperCase().replace(" ", "_");
    }
}
