package security.frontend.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import security.frontend.client.Model.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Controller
public class ClientController {
    private final RestTemplate restTemplate;

    @Value("${backend.url}/register")
    private String REGISTRATION_URL;
    @Value("${backend.url}/authenticate")
    private String AUTHENTICATION_URL;
    @Value("${backend.url}/refreshtoken")
    private String REFRESH_TOKEN;
    @Value("${backend.url}/customers")
    private String CUSTOMERS_URL;
    @Value("${backend.url}/customer/")
    private String CUSTOMER_ID_URL;
    @Value("${backend.url}/changePassword")
    private String CHANGE_PASSWORD_URL;
    @Value("${backend.url}/customers/search")
    private String SEARCH_CUSTOMER_URL;

    private static final String HOME_URL = "redirect:/home";
    private static final String LOGIN_URL = "redirect:/";
    private static final String ERROR_URL = "redirect:/error";


    private String secret;
    private int jwtExpirationInMs;
    private int refreshExpirationDateInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Value("${jwt.refreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    public ClientController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String login(Model model, HttpServletResponse response) {
        model.addAttribute("employee", new Employee());
        return "login";
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request, HttpServletResponse responseHttp, Model model) throws JsonProcessingException {
        if (isRefreshNeeded(request)) {
            refreshToken(request, responseHttp);
        }

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        RequestAccessToken accessToken = new RequestAccessToken(token);
        String body = getBody(accessToken);

        HttpHeaders headers = getHeaders();
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<CustomersList> customerList = restTemplate.exchange(CUSTOMERS_URL, HttpMethod.POST, entity, CustomersList.class);

        String response = "";
        if (customerList.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("customers", customerList.getBody().getCustomers());
            model.addAttribute("newPassword", new NewPassword());
            response = "home";
        } else {
            response = "error";
        }

        return response;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("employee", new Employee());
        return "register";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/customer/{id}")
    public String getCustomer(@PathVariable Long id, HttpServletRequest request, HttpServletResponse responseHttp, Model model) throws JsonProcessingException {
        if (isRefreshNeeded(request)) {
            refreshToken(request, responseHttp);
        }

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        RequestAccessToken accessToken = new RequestAccessToken(token);
        String body = getBody(accessToken);

        HttpHeaders headers = getHeaders();
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Customer> customer = restTemplate.exchange(CUSTOMER_ID_URL + id, HttpMethod.POST, entity, Customer.class);

        String response = "";
        if (customer.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("customer", customer.getBody());
            response = "customerPage";
        } else {
            response = "error";
        }

        return response;
    }

    @PostMapping("/changePassword")
    public String changePassword(NewPassword newPassword, HttpServletRequest requestHttp, HttpServletResponse responseHttp) throws JsonProcessingException {
        String response = "";
        String newPasswordBody = getBody(newPassword);

        HttpHeaders newPasswordHeaders = getHeaders();
        HttpEntity<String> newPasswordEntity = new HttpEntity<>(newPasswordBody, newPasswordHeaders);
        try {
            ResponseEntity<String> newPasswordResponse = restTemplate.exchange(CHANGE_PASSWORD_URL, HttpMethod.POST, newPasswordEntity, String.class);

            if (newPasswordResponse.getStatusCode().equals(HttpStatus.OK)) {
                response = LOGIN_URL;
            } else {
                response = ERROR_URL;
            }
        } catch (Exception e) {
            response = ERROR_URL;
        }
        return response;
    }

    @GetMapping("/customers/search")
    public String searchCustomers(@RequestParam("param") String param, HttpServletRequest requestHttp, HttpServletResponse responseHttp, Model model) throws JsonProcessingException {
        if (isRefreshNeeded(requestHttp)) {
            refreshToken(requestHttp, responseHttp);
        }

        String token = null;
        if (requestHttp.getCookies() != null) {
            for (Cookie cookie : requestHttp.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        SearchRequest searchRequest = new SearchRequest(token, param);
        String body = getBody(searchRequest);

        HttpHeaders headers = getHeaders();
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<CustomersList> customerList = restTemplate.exchange(SEARCH_CUSTOMER_URL, HttpMethod.POST, entity, CustomersList.class);

        String response = "";
        if (customerList.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("customers", customerList.getBody().getCustomers());
            response = "home :: searchResults";
        } else {
            response = "error";
        }

        return response;
    }

    @GetMapping("/adminPage")
    public String adminPage(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        if (isRefreshNeeded(request)) {
            refreshToken(request, response);
        }
        return "adminPage";
    }

    @PostMapping("/registration")
    public String registration(Employee employee) throws JsonProcessingException {
        String response = null;
        // convert the user registration object to JSON
        String registrationBody = getBody(employee);

        // create headers specifying that it is JSON request
        HttpHeaders registrationHeaders = getHeaders();
        HttpEntity<String> registrationEntity = new HttpEntity<>(registrationBody, registrationHeaders);

        try {
            // Register User
            ResponseEntity<String> registrationResponse = restTemplate.exchange(REGISTRATION_URL, HttpMethod.POST,
                    registrationEntity, String.class);

            if (registrationResponse.getStatusCode().equals(HttpStatus.OK)) {
                response = LOGIN_URL;
            } else {
                response = ERROR_URL;
            }
        } catch (Exception e) {
            response = ERROR_URL;
        }
        return response;
    }

    @PostMapping("/login")
    public String handleLogin(Employee employee, HttpServletResponse responseHttp) throws JsonProcessingException {
        String response;
        // convert the user registration object to JSON
        String loginBody = getBody(employee);

        // create headers specifying that it is JSON request
        HttpHeaders loginHeaders = getHeaders();
        HttpEntity<String> loginEntity = new HttpEntity<>(loginBody, loginHeaders);
        try {
            // Login User
            ResponseEntity<ResponseToken> loginResponse = restTemplate.exchange(AUTHENTICATION_URL, HttpMethod.POST,
                    loginEntity, ResponseToken.class);

            String accesstoken = Objects.requireNonNull(loginResponse.getBody()).getAccessToken();
            String refreshtoken = Objects.requireNonNull(loginResponse.getBody()).getRefreshToken();
            if (loginResponse.getStatusCode().equals(HttpStatus.OK)) {
                Cookie jwtAccessTokenCookie = new Cookie("accessToken", accesstoken);
                jwtAccessTokenCookie.setHttpOnly(true);
                jwtAccessTokenCookie.setSecure(true);
                jwtAccessTokenCookie.setPath("/");
                jwtAccessTokenCookie.setMaxAge(jwtExpirationInMs / 1000); //ms to seconds
                responseHttp.addCookie(jwtAccessTokenCookie);

                Cookie jwtRefreshTokenCookie = new Cookie("refreshToken", refreshtoken);
                jwtRefreshTokenCookie.setHttpOnly(true);
                jwtRefreshTokenCookie.setSecure(true);
                jwtRefreshTokenCookie.setPath("/");
                jwtRefreshTokenCookie.setMaxAge(refreshExpirationDateInMs / 1000); //ms to seconds
                responseHttp.addCookie(jwtRefreshTokenCookie);

                response = HOME_URL;
            } else {
                response = ERROR_URL;
            }
        } catch (Exception e) {
            response = ERROR_URL;
        }
        return response;
    }

    private boolean isRefreshNeeded(HttpServletRequest request) {
        Boolean b = (Boolean) request.getAttribute("isRefreshNeeded");
        return b != null && b;
    }

    private void refreshToken(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        // convert the user registration object to JSON
        RequestRefreshToken refreshToken = new RequestRefreshToken(request.getAttribute("refreshToken").toString());
        String refreshBody = getBody(refreshToken);

        // create headers specifying that it is JSON request
        HttpHeaders headers = getHeaders();
        HttpEntity<String> refreshEntity = new HttpEntity<>(refreshBody, headers);

        ResponseEntity<RequestRefreshToken> refreshResponse = restTemplate.exchange(REFRESH_TOKEN, HttpMethod.POST,
                refreshEntity, RequestRefreshToken.class);

        String accesstoken = Objects.requireNonNull(refreshResponse.getBody()).getRefreshToken();
        System.out.println("new access token" + accesstoken);
        Cookie jwtAccessTokenCookie = new Cookie("accessToken", accesstoken);
        jwtAccessTokenCookie.setHttpOnly(true);
        jwtAccessTokenCookie.setSecure(true);
        jwtAccessTokenCookie.setPath("/");
        jwtAccessTokenCookie.setMaxAge(jwtExpirationInMs / 1000); //ms to seconds
        response.addCookie(jwtAccessTokenCookie);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String getBody(final Employee employee) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(employee);
    }

    private String getBody(final RequestRefreshToken refreshToken) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(refreshToken);
    }

    private String getBody(final RequestAccessToken accessToken) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(accessToken);
    }

    private String getBody(final NewPassword newPassword) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(newPassword);
    }

    private String getBody(final SearchRequest searchRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(searchRequest);
    }
}
