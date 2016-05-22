package com.iscm.core.db.util;

import org.hibernate.transform.BasicTransformerAdapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AliasToMapResultTransformer extends BasicTransformerAdapter implements Serializable {

    public static final AliasToMapResultTransformer INSTANCE = new AliasToMapResultTransformer();

    /**
     * Disallow instantiation of AliasToMapResultTransformer.
     */
    private AliasToMapResultTransformer() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map result = new HashMap(tuple.length);
        for ( int i=0; i<tuple.length; i++ ) {
            String alias = aliases[i];
            if ( alias!=null ) {
                result.put( alias.toLowerCase(), tuple[i]==null?"":tuple[i]);
            }
        }
        return result;
    }

    /**
     * Serialization hook for ensuring singleton uniqueing.
     *
     * @return The singleton instance : {@link #INSTANCE}
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
