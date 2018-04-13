/*
 * Copyright 2018 Netherlands eScience Center
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
package ibis.constellation;

import java.io.Serializable;

import ibis.constellation.impl.IdChecker;
import ibis.constellation.util.ByteBuffers;

/**
 * An <code>Event</code> can be used for communication between {@link Activity activities}. A common usage is to notify an
 * activity that certain data is available, or that some processing steps have been finished. The data of an event may implement
 * the {@link ByteBuffers} interface, to send/receive any {@link java.nio.ByteBuffer}s it contains. Constellation will then call
 * the methods of this interface when needed.
 */
public final class Event implements Serializable {

    private static final long serialVersionUID = 8672434537078611592L;

    /** The source activity of this event. */
    private final ActivityIdentifier source;

    /** The destination activity of this event. */
    private final ActivityIdentifier target;

    /** The data of this event. */
    private final Object data;

    /**
     * Constructs an event with the specified parameters: a source, a target, and its data.
     *
     * @param source
     *            the source activity of this event
     * @param target
     *            the target activity for this event
     * @param data
     *            the data of this event, may be <code>null</code>
     * @throws IllegalArgumentException
     *             when either source or target is null or otherwise an illegal activity identifier (not generated by
     *             constellation). It is also thrown if the data is not null and not serializable.
     */
    public Event(ActivityIdentifier source, ActivityIdentifier target, Object data) {
        IdChecker.checkActivityIdentifier(source, "source identifier of event");
        IdChecker.checkActivityIdentifier(target, "target identifier of event");

        if (data != null && !(data instanceof Serializable)) {
            throw new IllegalArgumentException("data of event is not serializable");
        }

        this.source = source;
        this.target = target;
        this.data = data;
    }

    @Override
    public String toString() {
        String s = "source: " + getSource().toString();
        s += "; target: " + getTarget().toString();
        s += "; data = ";
        if (getData() != null) {
            s += getData().toString();
        } else {
            s += "none";
        }
        return s;
    }

    /**
     * Returns the identifier of the source activity of this event.
     *
     * @return the source activity identifier
     */
    public ActivityIdentifier getSource() {
        return source;
    }

    /**
     * Returns the identifier of the target activity of this event.
     *
     * @return the target activity identifier
     */
    public ActivityIdentifier getTarget() {
        return target;
    }

    /**
     * Returns the data object of this event.
     *
     * @return the data object
     */
    public Object getData() {
        return data;
    }
}
