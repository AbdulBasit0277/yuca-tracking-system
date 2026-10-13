package api;

import api.dto.BatchCreateRequest;
import api.dto.BatchResponse;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.CassavaBatch;
import service.CassavaBatchService;
import utils.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/batches")
public class BatchServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String batchCode = request.getParameter("batchCode");

        // Search by batch code
        if (batchCode != null && !batchCode.isBlank()) {

            try {

                List<CassavaBatch> batches =
                        CassavaBatchService.searchByBatchCode(batchCode.trim());

                ApiResponse.success(
                        response,
                        HttpServletResponse.SC_OK,
                        batches
                );

            } catch (Exception e) {

                e.printStackTrace();

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred while searching for the batch."
                );
            }

            return;
        }

        // Get batch by ID
        String batchIdParameter =
                request.getParameter("batchId");

        if (batchIdParameter == null
                || batchIdParameter.isBlank()) {

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Batch ID is required."
            );

            return;
        }

        long batchId;

        try {

            batchId = Long.parseLong(batchIdParameter);

            if (batchId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Batch ID must be a positive number."
            );

            return;
        }

        try {

            CassavaBatch batch =
                    CassavaBatchService.getBatchById(batchId);

            if (batch == null) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_NOT_FOUND,
                        "Batch not found."
                );

                return;
            }

            BatchResponse batchResponse =
                    new BatchResponse(
                            batch.getId(),
                            batch.getBatchCode(),
                            batch.getFarmerId(),
                            batch.getQuantity(),
                            batch.getHarvestDate(),
                            batch.getStatus()
                    );

            ApiResponse.success(
                    response,
                    HttpServletResponse.SC_OK,
                    batchResponse
            );

        } catch (Exception e) {

            e.printStackTrace();

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while retrieving the batch."
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            BatchCreateRequest batchRequest =
                    objectMapper.readValue(
                            request.getReader(),
                            BatchCreateRequest.class
                    );

            if (batchRequest == null) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Request body is required."
                );

                return;
            }

            if (batchRequest.getBatchCode() == null
                    || batchRequest.getBatchCode().isBlank()) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Batch code is required."
                );

                return;
            }

            if (batchRequest.getFarmerId() <= 0) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Farmer ID must be a positive number."
                );

                return;
            }

            if (batchRequest.getQuantity() <= 0) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Quantity must be greater than 0."
                );

                return;
            }

            if (batchRequest.getHarvestDate() == null) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Harvest date is required."
                );

                return;
            }

            CassavaBatch batch =
                    new CassavaBatch(
                            batchRequest.getBatchCode().trim(),
                            batchRequest.getFarmerId(),
                            batchRequest.getQuantity(),
                            batchRequest.getHarvestDate()
                    );

            long batchId =
                    CassavaBatchService.createBatch(batch);

            if (batchId <= 0) {
                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Batch could not be created."
                );
                return;
            }

            ApiResponse.success(
                    response,
                    HttpServletResponse.SC_CREATED,
                    "Cassava batch created successfully. Batch ID: " + batchId
            );

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid JSON request body."
            );

        } catch (IllegalArgumentException e) {

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );

        } catch (java.sql.SQLException e) {

            if ("23505".equals(e.getSQLState())) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_CONFLICT,
                        "Batch code already exists."
                );

            } else {

                e.printStackTrace();

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred while creating the batch."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while creating the batch."
            );
        }
    }
}