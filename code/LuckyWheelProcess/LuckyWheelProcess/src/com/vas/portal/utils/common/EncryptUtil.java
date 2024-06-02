/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.log4j.Logger;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.util.encoders.Base64;

public class EncryptUtil {

    private static final Logger log = Logger.getLogger(EncryptUtil.class);

    private static String AESKeyGen() throws NoSuchAlgorithmException {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();
            return byteToHex(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException noSuchAlgo) {
            log.error(noSuchAlgo.getMessage());
        }
        return null;
    }

    private static String byteToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private static byte[] hexToBytes(char[] hex) {
        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            int value = (high << 4) | low;
            if (value > 127) {
                value -= 256;
            }
            raw[i] = (byte) value;
        }
        return raw;
    }

    private static byte[] hexToBytes(String hex) {
        return hexToBytes(hex.toCharArray());
    }

    private static String encryptAES(String data, String key) throws Exception {
        String dataEncrypted = new String();
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            byte[] raw = hexToBytes(key);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] byteDataToEncrypt = data.getBytes();
            byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
            dataEncrypted = DatatypeConverter.printBase64Binary(byteCipherText);
            return dataEncrypted;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return dataEncrypted;
    }

    private static RSAKeyParameters getBCPublicKeyFromString(String strPublicKey) {
        try {
            PublicKey prvKey = getPublicKeyFromString(strPublicKey);
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pkSpec = keyFac.getKeySpec(prvKey, RSAPublicKeySpec.class);
            RSAKeyParameters pub = new RSAKeyParameters(false, pkSpec.getModulus(), pkSpec.getPublicExponent());
            return pub;
        } catch (Exception e) {
            log.info("Exception : " + e);
            return null;
        }
    }

    private static PublicKey getPublicKeyFromString(String key) throws Exception {
        PublicKey publicKey = null;
        try {
            PEMReader reader = new PEMReader(new StringReader(key), null, "SunRsaSign");
            publicKey = (PublicKey) reader.readObject();
        } catch (Exception e) {
            log.error("error getPublicKeyFromString ", e);
        }
        return publicKey;
    }

    private static String encryptRSA(String toEncrypt, String strPublicKey) {
        RSAKeyParameters rsaPbKey = getBCPublicKeyFromString(strPublicKey);
        if (rsaPbKey == null) {
            log.info("RSAPublicKey == null");
            return null;
        }
        try {
            AsymmetricBlockCipher theEngine = new RSAEngine();
            theEngine = new PKCS1Encoding(theEngine);
            theEngine.init(true, rsaPbKey);
            return new String(Base64.encode(theEngine.processBlock(toEncrypt.getBytes(), 0, toEncrypt.getBytes().length)));
        } catch (InvalidCipherTextException ex) {
            log.error("encryptRSA error ", ex);
        }
        return null;
    }

    private static String createMsgSignature(String data, String strPrivateKey) {
        String encryptData = "";
        try {
            PrivateKey privateKey = getPrivateKeyFromString(strPrivateKey);
            java.security.Signature s = java.security.Signature.getInstance("SHA1withRSA");
            s.initSign(privateKey);
            s.update(data.getBytes());
            byte[] signature = s.sign();
            encryptData = new String(Base64.encode(signature));                             //Encrypt data
        } catch (Exception e) {
            log.error("createMsgSignature", e);
        }
        return encryptData;
    }

    private static PrivateKey getPrivateKeyFromString(String key) throws Exception {
        PrivateKey privateKey = null;
        try {
            KeyPair pemPair;
            PEMReader reader = new PEMReader(new StringReader(key), null, "SunRsaSign");
            pemPair = (KeyPair) reader.readObject();

            privateKey = (PrivateKey) pemPair.getPrivate();
        } catch (Exception e) {
            log.info("error getPrivateKeyFromString " + e);
        }
        return privateKey;

    }

    private static boolean verifyMsgSignature(String encodeText, String strPublicKey, String input) {

        try {
            PublicKey publicKey = getPublicKeyFromString(strPublicKey);
            byte[] base64Bytes = Base64.decode(encodeText);                                 // decode base64
            java.security.Signature sig = java.security.Signature.getInstance("SHA1WithRSA");
            sig.initVerify(publicKey);
            sig.update(input.getBytes());

            return sig.verify(base64Bytes);
        } catch (Exception e) {
            log.error("verifyMsgSignature", e);
        }
        return false;
    }
    private static final Map<String, String> KEY_MPS = new HashMap<String, String>();

    private static String getKeyFile(String filePath) {
        String value = KEY_MPS.get(filePath);
        if (value != null) {
            return value;
        }

        File file = new File(filePath);
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = reader.readLine()) != null) {
                contents.append(text)
                        .append(System.getProperty(
                                "line.separator"));
            }
        } catch (IOException e) {
            log.error("getKeyFile", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("getKeyFile", e);
            }

            value = contents.toString();
            if (!value.isEmpty()) {
                KEY_MPS.put(filePath, value);
            }
        }

        return value;
    }

    private static String decryptRSA(String toDecrypt, String strPrivateKey) {

        RSAPrivateCrtKeyParameters rsaPrKey = getBCPrivateKeyFromString(strPrivateKey);
        if (rsaPrKey == null) {
            log.info("RSAPrivateKey == null");
            return null;
        }

        try {
            AsymmetricBlockCipher theEngine = new RSAEngine();
            theEngine = new PKCS1Encoding(theEngine);
            theEngine.init(false, rsaPrKey);
            return new String(theEngine.processBlock(Base64.decode(toDecrypt), 0, Base64.decode(toDecrypt).length));
        } catch (Exception ex) {
            log.error("decryptRSA", ex);
        }
        return null;
    }

    private static RSAPrivateCrtKeyParameters getBCPrivateKeyFromString(String strPrivateKey) {
        try {
            PrivateKey prvKey = getPrivateKeyFromString(strPrivateKey);
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            RSAPrivateCrtKeySpec pkSpec = keyFac.getKeySpec(prvKey, RSAPrivateCrtKeySpec.class);
            RSAPrivateCrtKeyParameters priv = new RSAPrivateCrtKeyParameters(pkSpec.getModulus(),
                    pkSpec.getPublicExponent(), pkSpec.getPrivateExponent(), pkSpec.getPrimeP(),
                    pkSpec.getPrimeQ(), pkSpec.getPrimeExponentP(), pkSpec.getPrimeExponentQ(),
                    pkSpec.getCrtCoefficient());
            return priv;
        } catch (Exception e) {
            return null;
        }
    }

    private static String decryptAES(String dataEncrypt, String key) throws Exception {
        String dataDecrypted = new String();
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            byte[] raw = hexToBytes(key);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            byte[] decordedValue = DatatypeConverter.parseBase64Binary(dataEncrypt);
            aesCipher.init(Cipher.DECRYPT_MODE, skeySpec, aesCipher.getParameters());
            byte[] byteDecryptedText = aesCipher.doFinal(decordedValue);
            dataDecrypted = new String(byteDecryptedText);
            return dataDecrypted;
        } catch (Exception ex) {
            log.error("Error decryptAES", ex);
        }
        return dataDecrypted;
    }

    public static String makeMpsRequestUrl(String pro, String service,
            String subService, String cmd, String url, Map<String, String> params, String path) {
        try {
            String url_wap_mobile = url;
            String dir = path == null ? "../etc/key/" : path;
            String publicKeyViettel = getKeyFile(dir + "PublicKeyVT.pem");
//            log.info("PublicKeyVT: " + publicKeyViettel);
            String privateKeyCP = getKeyFile(dir + "PrivateKeyCP.pem");
//            log.info("PrivateKeyCP: " + privateKeyCP);
            String publicKeyCP = getKeyFile(dir + "PublicKeyCP.pem");
//            log.info("PublicKeyCP: " + publicKeyCP);
            String keyAES = AESKeyGen();
            StringBuffer input = new StringBuffer("");
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (i > 0) {
                    input.append("&");
                }
                input.append(key).append("=").append(value);
                i++;
            }
            log.info("Input of request: " + input.toString());

            String input_with_key_AES = "value=" + encryptAES(input.toString(), keyAES) + "&key=" + keyAES;

            String data_encrypted_RSA = (encryptRSA(input_with_key_AES, publicKeyViettel));

            String signature = URLEncoder.encode(createMsgSignature(data_encrypted_RSA, privateKeyCP));

            boolean kq = verifyMsgSignature(URLDecoder.decode(signature), publicKeyCP, data_encrypted_RSA);

            String url_request = url_wap_mobile + "PRO=" + pro + "&SER=" + service + "&SUB=" + subService + "&CMD="
                    + cmd + "&DATA=" + URLEncoder.encode(data_encrypted_RSA) + "&SIG=" + signature;
            return url_request;
        } catch (Exception ex) {
            log.error("Error makeMpsRequestUrl", ex);
        }

        return null;
    }

    public static String decodeMpsResponse(String strTemp, String path) throws Exception {
        String dir = path == null ? "../etc/key/" : path;
        String publicKeyViettel = getKeyFile(dir + "PublicKeyVT.pem");
        String privateKeyCP = getKeyFile(dir + "PrivateKeyCP.pem");

        String[] s = strTemp.split("&");

        String dataN = s[0].substring(5);
        String sigN = s[1].substring(4);

        boolean verify_return = verifyMsgSignature(URLDecoder.decode(sigN), publicKeyViettel, dataN);
        String data_decrypt_RSAN = decryptRSA(dataN, privateKeyCP);
        String value_return = data_decrypt_RSAN.split("&")[0].substring(6);
        String key_return = data_decrypt_RSAN.split("&")[1].substring(4);
        String data_complete = decryptAES(value_return, key_return);
        return data_complete;
    }

    public static String analyseCodeReturn(String input) {
        String value = "1|unknown";
        String[] a = input.split("&");
        for (String a1 : a) {
            if (a1.split("=")[0].equals("RES")) {
                String key = a1.split("=")[1];
                value = key + "|" + StaticCode.getdiscription(key);
                break;
            }
        }
        return value;
    }
}
