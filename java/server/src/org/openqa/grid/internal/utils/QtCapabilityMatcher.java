package org.openqa.grid.internal.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.qtwebkit.QtWebKitDriver;

import java.util.Iterator;
import java.util.Map;

/**
 * Custom implementation of the capability matcher for QtWebKitDriver.
 * <p/>
 * This matcher will look at the key "hybrid" and
 * will try to find a node that has those capabilities.
 */
public class QtCapabilityMatcher extends DefaultCapabilityMatcher {

    public QtCapabilityMatcher() {
        super();
    }

    public boolean matches(Map<String, Object> nodeCapability,
                           Map<String, Object> requestedCapability) {

        for (String key : requestedCapability.keySet()) {
            if (requestedCapability.get(key) != null) {
                if (key.equalsIgnoreCase(QtWebKitDriver.HYBRID)) {
                    JSONObject nodeTypes = (JSONObject) nodeCapability.get(key);
                    JSONObject requestedTypes = (JSONObject) requestedCapability.get(key);
                    String caps = null;
                    Iterator<String> iter = requestedTypes.keys();
                    while (iter.hasNext()) {
                        caps = iter.next();
                        if (caps.equalsIgnoreCase("qtVersion")) {
                            String value = null;
                            try {
                                value = requestedTypes.get(caps).toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (!("ANY".equalsIgnoreCase(value) || "".equals(value) || "*".equals(value))) {
                                try {
                                    if (value.charAt(0) != (nodeTypes.get(caps)).toString().charAt(0)) {
                                        return false;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }  else {
                            try {
                                if (!(requestedTypes.get(caps)).equals(nodeTypes.get(caps))) {
                                    return false;
                                }
                            } catch (JSONException e) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return super.matches(nodeCapability, requestedCapability);
    }

}
