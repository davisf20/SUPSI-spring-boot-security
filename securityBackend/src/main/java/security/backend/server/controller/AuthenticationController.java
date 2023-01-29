package security.backend.server.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import security.backend.server.model.*;
import security.backend.server.service.CustomerService;
import security.backend.server.service.EmployeeService;
import security.backend.server.service.JwtService;

@RestController
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final JwtService jwtService;

    private String secret;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }


    public AuthenticationController(AuthenticationManager authenticationManager, EmployeeService employeeService, CustomerService customerService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userdetails = employeeService.loadUserByUsername(authenticationRequest.getUsername());

        String accessToken = jwtService.generateToken(userdetails,"access");
        String refreshToken = jwtService.generateToken(userdetails,"refresh");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(accessToken, refreshToken);
        System.out.println(authenticationResponse.getAccessToken() + " " + authenticationResponse.getRefreshToken());
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Employee> saveUser(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.save(employee));
    }


    @PostMapping(value = "/refreshtoken")
    public ResponseEntity<RequestRefreshToken> refreshToken(@RequestBody RequestRefreshToken refreshToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken.getRefreshToken()).getBody();

        String username = claims.getSubject();

        RequestRefreshToken response = new RequestRefreshToken();
        response.setRefreshToken(jwtService.generateToken(employeeService.loadUserByUsername(username),"access"));
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/customers")
    public ResponseEntity<CustomersList> getCustomers(@RequestBody RequestAccessToken accessToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(accessToken.getAccessToken()).getBody();
        String username = claims.getSubject();

        //check if the token is expired
        if(claims.getExpiration().before(new java.util.Date())){
            //return error
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(new CustomersList(customerService.getCustomers(employeeService.getByUsername(username))));
    }

    @PostMapping(value = "/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@RequestBody RequestAccessToken accessToken, @PathVariable Long id) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(accessToken.getAccessToken()).getBody();

        //check if the token is expired
        if(claims.getExpiration().before(new java.util.Date())){
            //return error
            return ResponseEntity.ok(null);
        }

        if (customerService.exists(id)) {
            return ResponseEntity.ok(customerService.getById(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<Boolean> changePassword(@RequestBody NewPassword newPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    newPassword.getUsername(), newPassword.getOldPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        return ResponseEntity.ok(employeeService.changePassword(newPassword.getUsername(), newPassword.getNewPassword()));
    }

    @PostMapping(value = "/customers/search")
    public ResponseEntity<CustomersList> search(@RequestBody SearchRequest searchRequest) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(searchRequest.getAccessToken()).getBody();
        String username = claims.getSubject();

        String param = searchRequest.getParam();

        if(claims.getExpiration().before(new java.util.Date())){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(new CustomersList(customerService.search(employeeService.getByUsername(username), param)));
    }
}
