package com.truckplatform.bookings.service;

import com.truckplatform.bookings.dto.BookingRequest;
import com.truckplatform.bookings.dto.BookingResponse;
import com.truckplatform.bookings.entity.Booking;
import com.truckplatform.bookings.entity.BookingStatus;
import com.truckplatform.bookings.repository.BookingRepository;
import com.truckplatform.cities.entity.City;
import com.truckplatform.cities.service.CityService;
import com.truckplatform.pricing.dto.PricingRequest;
import com.truckplatform.pricing.dto.PricingResponse;
import com.truckplatform.pricing.service.PricingService;
import com.truckplatform.transporters.entity.Transporter;
import com.truckplatform.transporters.repository.TransporterRepository;
import com.truckplatform.trucks.entity.Truck;
import com.truckplatform.trucks.entity.TruckStatus;
import com.truckplatform.trucks.repository.TruckRepository;
import com.truckplatform.users.entity.User;
import com.truckplatform.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private PricingService pricingService;

    @Autowired
    private CityService cityService;

    @Autowired
    private TransporterRepository transporterRepository;

    /**
     * Create a new booking
     *
     * @param bookingRequest the booking request containing customer, truck, source, destination, and weight
     * @return the created booking response
     * @throws IllegalArgumentException if customer or truck is not found
     */
    public BookingResponse createBooking(String customerEmail, BookingRequest bookingRequest) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Validate truck exists
        Truck truck = truckRepository.findById(bookingRequest.getTruckId())
                .orElseThrow(() -> new IllegalArgumentException("Truck not found"));

        if (truck.getStatus() != TruckStatus.AVAILABLE) {
            throw new IllegalArgumentException("Selected truck is not available");
        }

        if (bookingRequest.getWeight() > truck.getCapacityKg()) {
            throw new IllegalArgumentException("Selected truck cannot carry the requested weight");
        }

        City sourceCity = cityService.getByName(bookingRequest.getSource());
        City destinationCity = cityService.getByName(bookingRequest.getDestination());

        PricingRequest pricingRequest = new PricingRequest();
        pricingRequest.setDistanceKm(calculateDistanceKm(sourceCity, destinationCity));
        pricingRequest.setWeightKg(bookingRequest.getWeight());
        pricingRequest.setTruckType(truck.getTruckType().name());
        PricingResponse pricingResponse = pricingService.calculatePrice(pricingRequest);

        // Create new booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTruck(truck);
        booking.setSource(bookingRequest.getSource());
        booking.setDestination(bookingRequest.getDestination());
        booking.setWeight(bookingRequest.getWeight());
        booking.setStatus(BookingStatus.CREATED);
        booking.setPrice(pricingResponse.getTotalFare());

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }

    /**
     * Get booking by ID
     *
     * @param bookingId the booking ID
     * @return the booking response
     */
    public BookingResponse getBooking(Authentication authentication, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        validateBookingAccess(authentication, booking);
        return mapToResponse(booking);
    }

    /**
     * Get all bookings for a customer
     *
     * @param customerId the customer ID
     * @return list of booking responses
     */
    public List<BookingResponse> getBookingsByCustomer(String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        List<Booking> bookings = bookingRepository.findByCustomerId(customer.getId());
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all bookings for a truck
     *
     * @param truckId the truck ID
     * @return list of booking responses
     */
    public List<BookingResponse> getBookingsByTransporter(String transporterEmail) {
        Transporter transporter = getTransporterByEmail(transporterEmail);
        List<Booking> bookings = bookingRepository.findByTruckTransporterId(transporter.getId());
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all bookings with a specific status
     *
     * @param status the booking status
     * @return list of booking responses
     */
    public List<BookingResponse> getBookingsByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepository.findByStatus(status);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Accept a booking (change status from CREATED to ACCEPTED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    public BookingResponse acceptBooking(String transporterEmail, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        validateTransporterOwnership(transporterEmail, booking);

        if (booking.getStatus() != BookingStatus.CREATED) {
            throw new IllegalArgumentException("Only bookings with CREATED status can be accepted");
        }

        booking.setStatus(BookingStatus.ACCEPTED);
        booking.getTruck().setStatus(TruckStatus.OFFLINE);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    /**
     * Start a booking (change status from ACCEPTED to IN_TRANSIT)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    public BookingResponse startBooking(String transporterEmail, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        validateTransporterOwnership(transporterEmail, booking);

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new IllegalArgumentException("Only bookings with ACCEPTED status can be started");
        }

        booking.setStatus(BookingStatus.IN_TRANSIT);
        booking.getTruck().setStatus(TruckStatus.IN_TRANSIT);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    /**
     * Complete a booking (change status from IN_TRANSIT to DELIVERED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    public BookingResponse completeBooking(String transporterEmail, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        validateTransporterOwnership(transporterEmail, booking);

        if (booking.getStatus() != BookingStatus.IN_TRANSIT) {
            throw new IllegalArgumentException("Only bookings with IN_TRANSIT status can be completed");
        }

        booking.setStatus(BookingStatus.DELIVERED);
        booking.getTruck().setStatus(TruckStatus.AVAILABLE);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    /**
     * Cancel a booking (change status to CANCELLED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    public BookingResponse cancelBooking(Authentication authentication, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        validateCancelAccess(authentication, booking);

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.DELIVERED) {
            throw new IllegalArgumentException("Booking cannot be cancelled in current status");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getTruck().setStatus(TruckStatus.AVAILABLE);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    /**
     * Map Booking entity to BookingResponse DTO
     *
     * @param booking the booking entity
     * @return the booking response
     */
    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setCustomerId(booking.getCustomer().getId());
        response.setTruckId(booking.getTruck().getId());
        response.setSource(booking.getSource());
        response.setDestination(booking.getDestination());
        response.setWeight(booking.getWeight());
        response.setPrice(booking.getPrice());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());
        return response;
    }

    private Transporter getTransporterByEmail(String transporterEmail) {
        return transporterRepository.findByEmailIgnoreCase(transporterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));
    }

    private void validateTransporterOwnership(String transporterEmail, Booking booking) {
        Transporter transporter = getTransporterByEmail(transporterEmail);
        if (!booking.getTruck().getTransporter().getId().equals(transporter.getId())) {
            throw new IllegalArgumentException("This booking does not belong to the logged-in transporter");
        }
    }

    private void validateBookingAccess(Authentication authentication, Booking booking) {
        if (authentication.getAuthorities().stream().anyMatch(authority -> "TRANSPORTER".equals(authority.getAuthority()))) {
            validateTransporterOwnership(authentication.getName(), booking);
            return;
        }

        if (!booking.getCustomer().getEmail().equalsIgnoreCase(authentication.getName())) {
            throw new IllegalArgumentException("This booking does not belong to the logged-in customer");
        }
    }

    private void validateCancelAccess(Authentication authentication, Booking booking) {
        if (authentication.getAuthorities().stream().anyMatch(authority -> "TRANSPORTER".equals(authority.getAuthority()))) {
            validateTransporterOwnership(authentication.getName(), booking);
            return;
        }

        if (!booking.getCustomer().getEmail().equalsIgnoreCase(authentication.getName())) {
            throw new IllegalArgumentException("This booking does not belong to the logged-in customer");
        }
    }

    private BigDecimal calculateDistanceKm(City sourceCity, City destinationCity) {
        double earthRadiusKm = 6371.0;
        double latDistance = Math.toRadians(destinationCity.getLatitude() - sourceCity.getLatitude());
        double lonDistance = Math.toRadians(destinationCity.getLongitude() - sourceCity.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(sourceCity.getLatitude()))
                * Math.cos(Math.toRadians(destinationCity.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return BigDecimal.valueOf(earthRadiusKm * c);
    }
}
