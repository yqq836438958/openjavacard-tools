package org.openjavacard.gp.scp;

import org.openjavacard.gp.crypto.GPBouncy;
import org.openjavacard.gp.keys.GPKey;
import org.openjavacard.gp.keys.GPKeyCipher;
import org.openjavacard.gp.keys.GPKeySet;
import org.openjavacard.gp.keys.GPKeyType;
import org.openjavacard.util.ArrayUtil;
import org.openjavacard.util.HexUtil;

import java.util.Hashtable;

/**
 * SCP03 session key derivation
 */
public class SCP03Derivation {

    /** All key types in SCP03 */
    private static final GPKeyType[] KEY_TYPES = {
            GPKeyType.ENC, GPKeyType.MAC, GPKeyType.RMAC, GPKeyType.KEK
    };

    /** Key derivation constants */
    private static final Hashtable<GPKeyType, Byte> DERIVATION_CONSTANTS = new Hashtable<>();
    static {
        DERIVATION_CONSTANTS.put(GPKeyType.ENC,  (byte)0x04);
        DERIVATION_CONSTANTS.put(GPKeyType.MAC,  (byte)0x06);
        DERIVATION_CONSTANTS.put(GPKeyType.RMAC, (byte)0x07);
        // no derivation for the KEK
    }

    /**
     * Derive SCP03 session keys
     *
     * @param staticKeys to derive from
     * @param sequence for derivation
     * @param hostChallenge for derivation
     * @param cardChallenge for derivation
     * @return keyset containing session keys
     */
    public static GPKeySet deriveSessionKeys(GPKeySet staticKeys, byte[] sequence, byte[] hostChallenge, byte[] cardChallenge) {
        // synthesize a name for the new keyset
        String name = staticKeys.getName() + "-SCP03:" + HexUtil.bytesToHex(sequence);
        // build derivation context
        byte[] context = ArrayUtil.concatenate(hostChallenge, cardChallenge);
        // create the new keyset
        GPKeySet derivedSet = new GPKeySet(name, staticKeys.getKeyVersion(), staticKeys.getDiversification());
        // go through all supported key types
        for (GPKeyType type : KEY_TYPES) {
            // get the static key for the type
            GPKey staticKey = staticKeys.getKeyByType(type);
            if(staticKey != null) {
                GPKey sessionKey;
                // derive or copy
                if(DERIVATION_CONSTANTS.containsKey(type)) {
                    // perform derivation
                    byte constant = DERIVATION_CONSTANTS.get(type);
                    byte[] sessionSecret = GPBouncy.scp03_kdf(staticKey, constant, context, staticKey.getLength() * 8);
                    sessionKey = new GPKey(type, staticKey.getId(), GPKeyCipher.AES, sessionSecret);
                } else {
                    // copy keys that need no derivation
                    sessionKey = new GPKey(type, staticKey.getId(), GPKeyCipher.AES, staticKey.getSecret());
                }
                // add key to new set
                derivedSet.putKey(sessionKey);
            }
        }
        // return the new set
        return derivedSet;
    }

}