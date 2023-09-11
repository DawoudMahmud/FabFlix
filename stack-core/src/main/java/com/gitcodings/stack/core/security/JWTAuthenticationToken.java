package com.gitcodings.stack.core.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthenticationToken extends AbstractAuthenticationToken
{
    private final SignedJWT principal;

    private JWTAuthenticationToken(
        Collection<? extends GrantedAuthority> authorities,
        SignedJWT principal)
    {
        super(authorities);

        this.principal = principal;
    }

    /**
     * @param serializedToken the users serialized token to parse
     * @return an instance of {@link JWTAuthenticationToken} with the {@link SignedJWT} build from
     * parsing the serializedToken
     *
     * @throws ParseException in case of an invalid serializedToken
     */
    public static JWTAuthenticationToken fromSerializedToken(String serializedToken)
        throws ParseException
    {
        SignedJWT    signedJWT    = SignedJWT.parse(serializedToken);
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        List<String> roleClaims   = jwtClaimsSet.getStringListClaim(JWTManager.CLAIM_ROLES);

        Collection<GrantedAuthority> authorities =
            roleClaims.stream()
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());

        return new JWTAuthenticationToken(
            authorities,
            signedJWT
        );
    }

    /**
     * There are no credentials for this type of Authentication
     *
     * @return the empty string {@code ""}
     */
    @Override
    public Object getCredentials()
    {
        return "";
    }

    /**
     * @return the users {@link SignedJWT}
     */
    @Override
    public SignedJWT getPrincipal()
    {
        return principal;
    }
}
