package security.backend.server.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import security.backend.server.model.AuthenticationRequest;
import security.backend.server.model.AuthenticationResponse;
import security.backend.server.model.Employee;
import security.backend.server.model.RequestRefreshToken;
import security.backend.server.service.EmployeeService;
import security.backend.server.service.JwtService;

@RestController
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final JwtService jwtService;


    public AuthenticationController(AuthenticationManager authenticationManager, EmployeeService employeeService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
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
        Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(refreshToken.getRefreshToken()).getBody();

        String username = claims.getSubject();

        RequestRefreshToken response = new RequestRefreshToken();
        response.setRefreshToken(jwtService.generateToken(employeeService.loadUserByUsername(username),"access"));
        return ResponseEntity.ok(response);
    }

}
