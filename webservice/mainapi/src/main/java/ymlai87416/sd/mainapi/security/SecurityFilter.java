package ymlai87416.sd.mainapi.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ymlai87416.sd.dto.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ymlai87416.sd.mainapi.ReadApi;
import ymlai87416.sd.mainapi.WriteApi;
import ymlai87416.sd.mainapi.security.model.Credentials;

@Component
@Slf4j
public class SecurityFilter  extends OncePerRequestFilter {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Autowired
    private ReadApi readApi;

    @Autowired
    private WriteApi writeApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        authorize(request);
        filterChain.doFilter(request, response);
    }

    private void authorize(HttpServletRequest request) {
        FirebaseToken decodedToken = null;

        String token = securityService.getBearerToken(request);
        try {
            if (token != null && !token.equals("null")
                    && !token.equalsIgnoreCase("undefined")) {
                decodedToken = firebaseAuth.verifyIdToken(token);
            }
        } catch (FirebaseAuthException e) {
            log.error("Firebase Exception:: ", e.getLocalizedMessage());
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        User user = firebaseTokenToUserDto(decodedToken);
        // Handle roles
        if (user != null) {
            // Handle Other roles
            decodedToken.getClaims().forEach((k, v) -> authorities.add(new SimpleGrantedAuthority(k)));
            // Set security context
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                    new Credentials(decodedToken, token), authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private User firebaseTokenToUserDto(FirebaseToken decodedToken) {
        User user = null;
        if (decodedToken != null) {
            user = readApi.getUserInfoByName(decodedToken.getUid());
            if(user == null){
                user = new User(0, decodedToken.getUid(), decodedToken.getEmail(), "", new Date());
                log.info("Create new user: " + decodedToken.getUid() + " "+ decodedToken.getEmail());
                user = writeApi.createUser(user);
            }
        }
        return user;
    }
}
