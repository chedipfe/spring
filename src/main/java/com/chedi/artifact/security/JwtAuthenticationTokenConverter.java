package com.chedi.artifact.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// On extrait les GrantedAuthority depuis l'access token
// Les GrantedAuthority sont les rôles de l'utilisateur authentifié
// On va extraire les roles depuis les claims scope et resource_access dans l'access token, on va les concatèner dans une liste Granted Authorities
// Puis on retourne cette liste

@Component
public class JwtAuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Value("${jwt.auth.converter.principal-attribute}")
    private String principalAttribute;

    @Override
    // Extrait les roles de l'utilisateur authentifié à partir du claim scope qui se trouve dans son access token 
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities =
                Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream())
                        .collect(Collectors.toSet());
        
        // # Le claim dans l'access token qui identifie l'utilisateur déja authentifié au idp, 
        // # s'il n'est pas défini l'api identifie l'utilisateur par le claim sub               
        String claimName = principalAttribute == null ? JwtClaimNames.SUB : principalAttribute;
        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(claimName));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
    
    // Extrait les roles de l'utilisateur authentifié à partir du claim resource_access qui se trouve dans son access token 
    
    // "resource_access": {
    // "client-ID": {
    //   "roles": [
    //     "USER"
    //   ]
    // },
    
    Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(resourceId)) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
            return Collections.emptySet();
        }

        
    // il retourne une liste de role [... ROLE_ concatiné avec le role extrait]

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}