package net.sf.gridarta.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Implements the XTEA algorithm.
 * @author Andreas Kirschbaum
 */
public class Xtea {

    /**
     * The number of rounds.
     */
    private static final int NUM_ROUNDS = 32;

    /**
     * The magic constant (1<<32)/PHI.
     */
    private static final int DELTA = 0x9E3779B9;

    /**
     * The state.
     */
    @NotNull
    private final int[] k = new int[4];

    /**
     * Creates a new instance.
     * @param key the key to use; must be 16 bytes
     */
    public Xtea(@NotNull final byte[] key) {
        if (key.length != 16) {
            throw new IllegalArgumentException("invalid key length");
        }

        k[0] = getInt(key, 0);
        k[1] = getInt(key, 4);
        k[2] = getInt(key, 8);
        k[3] = getInt(key, 12);
    }

    /**
     * Encrypts a data block.
     * @param plaintext the block to encrypt; must be 8 bytes
     * @param ciphertext the encrypted block; must be 8 bytes
     */
    public void encrypt(@NotNull final byte[] plaintext, @NotNull final byte[] ciphertext) {
        if (plaintext.length != 8) {
            throw new IllegalArgumentException();
        }
        if (ciphertext.length != 8) {
            throw new IllegalArgumentException();
        }
        int v0 = getInt(plaintext, 0);
        int v1 = getInt(plaintext, 4);
        int sum = 0;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            v0 += (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + k[sum & 3]);
            sum += DELTA;
            v1 += (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + k[(sum >>> 11) & 3]);
        }
        putInt(v0, ciphertext, 0);
        putInt(v1, ciphertext, 4);
    }

    /**
     * Decrypts a data block.
     * @param ciphertext the block to decrypt; must be 8 bytes
     * @param plaintext the decrypted block; must be 8 bytes
     */
    public void decrypt(@NotNull final byte[] ciphertext, @NotNull final byte[] plaintext) {
        if (ciphertext.length != 8) {
            throw new IllegalArgumentException();
        }
        if (plaintext.length != 8) {
            throw new IllegalArgumentException();
        }
        int v0 = getInt(ciphertext, 0);
        int v1 = getInt(ciphertext, 4);
        //noinspection NumericOverflow
        int sum = DELTA * NUM_ROUNDS;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            v1 -= (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + k[(sum >>> 11) & 3]);
            sum -= DELTA;
            v0 -= (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + k[sum & 3]);
        }
        putInt(v0, plaintext, 0);
        putInt(v1, plaintext, 4);
    }

    /**
     * Converts 4 data bytes to an <code>int</code>.
     * @param data the data array
     * @param offset the starting offset into <code>data</code>
     * @return the <code>int</code> value
     */
    private static int getInt(@NotNull final byte[] data, final int offset) {
        return ((data[offset + 3] & 255) << 24) | ((data[offset + 2] & 255) << 16) | ((data[offset + 1] & 255) << 8) | data[offset] & 255;
    }

    /**
     * Converts an <code>int</code> value into 4 data bytes.
     * @param value the <code>int</code> value
     * @param data the data array
     * @param offset the starting offset into <code>data</code>
     */
    private static void putInt(final int value, @NotNull final byte[] data, final int offset) {
        data[offset + 3] = (byte) (value >>> 24);
        data[offset + 2] = (byte) (value >>> 16);
        data[offset + 1] = (byte) (value >>> 8);
        data[offset] = (byte) value;
    }

}
