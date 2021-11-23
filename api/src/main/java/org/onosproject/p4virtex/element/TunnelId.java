package org.onosproject.p4virtex.element;

import org.onlab.util.Identifier;

public final class TunnelId extends Identifier<String> {

    /**
     * Creates an tunnel identifier from the specified tunnel.
     *
     * @param value string value
     * @return tunnel identifier
     */
    public static TunnelId valueOf(String value) {
        return new TunnelId(value);
    }

    /**
     * Constructor for serializer.
     */
    TunnelId() {
        super("0");
    }

    /**
     * Constructs the ID corresponding to a given string value.
     *
     * @param value the underlying value of this ID
     */
    TunnelId(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return id();
    }

}
