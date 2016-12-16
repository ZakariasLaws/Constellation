/**
 * Copyright 2013 Netherlands eScience Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ibis.constellation.impl;

import ibis.constellation.ActivityIdentifier;

/**
 * @version 1.0
 * @since 1.0
 *
 */
public class ImplUtil {

    public static ConstellationIdentifierImpl createConstellationIdentifier(int nodeId, int localId) {
        return new ConstellationIdentifierImpl(nodeId, localId);
    }

    public static ActivityIdentifier createActivityIdentifier(ConstellationIdentifierImpl cid, long aid, boolean expectsEvents) {
        return ActivityIdentifierImpl.createActivityIdentifier(cid, aid, expectsEvents);
    }

    public static ActivityIdentifier createActivityIdentifier(int nodeId, int localId, long aid, boolean expectsEvents) {
        return ActivityIdentifierImpl.createActivityIdentifier(createConstellationIdentifier(nodeId, localId), aid,
                expectsEvents);
    }

}
