package br.com.siged.app;

import static com.nimbusds.jose.JWSAlgorithm.HS256;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.util.DateUtils;

public final class JwtUtils {

    @SuppressWarnings("unused")
	private final static String AUDIENCE_UNKNOWN = "unknown";
    @SuppressWarnings("unused")
	private final static String AUDIENCE_WEB = "web";
    @SuppressWarnings("unused")
	private final static String AUDIENCE_TABLET = "tablet";
    private final static String AUDIENCE_MOBILE = "mobile";
    private final static String ROLES_CLAIM = "roles";
    public final static int TOKEN_EXPIRATION_MINUTES = 1*2; //24*60;

    public static String generateHMACToken(String subject, Collection<GrantedAuthority> roles, String secret, int expirationInMinutes) throws JOSEException {
        return generateHMACToken(subject, AuthorityListToCommaSeparatedString(roles), secret, expirationInMinutes);
    }

    public static String generateHMACToken(String subject, String roles, String secret, int expirationInMinutes) throws JOSEException {
        JWSSigner signer = new MACSigner(secret);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(currentDate())
                .expirationTime(expirationDate(expirationInMinutes))
                .claim(ROLES_CLAIM, roles)
                .audience(AUDIENCE_MOBILE)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    private static Date expirationDate(int expirationInMinutes) {
        return new Date(System.currentTimeMillis() + expirationInMinutes*60*1000);
    }

    public static void assertNotExpired(SignedJWT jwt) throws ParseException {
        if(DateUtils.isBefore(jwt.getJWTClaimsSet().getExpirationTime(), currentDate(), 60)) {
            throw new RuntimeException("Token expirado");
        }
    }

    public static void assertValidSignature(SignedJWT jwt, String secret) throws ParseException, JOSEException {
        if(!verifyHMACToken(jwt, secret)) {
            throw new RuntimeException("Token inválido");
        }
    }

    public static SignedJWT parse(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    public static boolean verifyHMACToken(SignedJWT jwt, String secret) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(secret);
        return jwt.verify(verifier);
    }

    public static String AuthorityListToCommaSeparatedString(Collection<GrantedAuthority> authorities) {
        Set<String> authoritiesAsSetOfString = AuthorityUtils.authorityListToSet(authorities);
        return StringUtils.join(authoritiesAsSetOfString, ", ");
    }

    public static String getUsername(SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getSubject();
    }

    public static Collection<GrantedAuthority> getRoles(SignedJWT jwt) throws ParseException {
        Collection<GrantedAuthority> authorities;
        String roles = jwt.getJWTClaimsSet().getStringClaim(ROLES_CLAIM);
        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
        return authorities;
    }

    public static Date getIssueTime(SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getIssueTime();
    }
    
    public static String getSecretKey(){
    	return "ksfdlkvopiurutueijflkdsvf,cjnjnxjnvsoifoiropiezaropioezkvf,k,c kv,ckvdkfjgvorieoigfopziefpozepfiezikfozkfldsmvflkvdldvl,fdvk,fdkv,dkfgkjfdgkjvicooiviuzieopiztpoirpotimldkflg,vlkfckjshayauiueruergregierpogipdfogiklxvcjnxvjnfvjdsmkfmslfklgkfgoirgjitrooritjg";
    }

}
