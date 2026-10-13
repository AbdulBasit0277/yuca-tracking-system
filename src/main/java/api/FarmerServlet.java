package api;

import api.dto.FarmerCreateRequest;
import api.dto.FarmerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Farmer;
import service.FarmerService;
import utils.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/farmers")
public class FarmerServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Farmer> farmers =
                    FarmerService.viewRegisteredFarmer();

            List<FarmerResponse> result =
                    new ArrayList<>();

            for (Farmer farmer : farmers) {

                result.add(
                        new FarmerResponse(
                                farmer.getId(),
                                farmer.getName(),
                                farmer.getPhoneNumber(),
                                farmer.getLocation()
                        )
                );
            }

            ApiResponse.success(
                    response,
                    HttpServletResponse.SC_OK,
                    result
            );

        } catch (Exception e) {

            e.printStackTrace();

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while retrieving farmers."
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            FarmerCreateRequest farmerRequest =
                    objectMapper.readValue(
                            request.getReader(),
                            FarmerCreateRequest.class
                    );

            if (farmerRequest == null) {
                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Request body is required."
                );
                return;
            }

            if (farmerRequest.getName() == null
                    || farmerRequest.getName().isBlank()) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Farmer name is required."
                );
                return;
            }

            if (farmerRequest.getPhoneNumber() == null
                    || farmerRequest.getPhoneNumber().isBlank()) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Phone number is required."
                );
                return;
            }

            if (farmerRequest.getLocation() == null
                    || farmerRequest.getLocation().isBlank()) {

                ApiResponse.error(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Location is required."
                );
                return;
            }

            Farmer farmer = new Farmer(
                    farmerRequest.getName().trim(),
                    farmerRequest.getPhoneNumber().trim(),
                    farmerRequest.getLocation().trim()
            );

            FarmerService.registerFarmer(farmer);

            ApiResponse.success(
                    response,
                    HttpServletResponse.SC_CREATED,
                    "Farmer registered successfully."
            );

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid JSON request body."
            );

        } catch (Exception e) {

            e.printStackTrace();

            ApiResponse.error(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while registering the farmer."
            );
        }
    }
}
