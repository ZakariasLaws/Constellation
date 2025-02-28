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
package ibis.constellation.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ibis.constellation.Constellation;
import ibis.constellation.Context;
import ibis.constellation.CrashActivity;
import ibis.constellation.Event;
import ibis.constellation.FakeActivity;

/**
 * @version 1.0
 * @since 1.0
 *
 */
public class ActivityRecordTest {

    @Test
    public void testIdentifier() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertEquals(id, r.identifier());
    }

    @Test
    public void testContext() {

        Context c = new Context("A");

        FakeActivity a = new FakeActivity(c);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertEquals(c, r.getContext());
    }

    @Test
    public void testPendingEvents1() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertEquals(0, r.pendingEvents());
    }

    @Test
    public void testPendingEvents2() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        Event e = new Event(id, id, "Hello");

        r.enqueue(e);

        assertEquals(1, r.pendingEvents());
    }

    @Test(expected = IllegalStateException.class)
    public void testPendingEvents3() {

        FakeActivity a = new FakeActivity(new Context("A"), false);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        r.pendingEvents();
    }

    @Test(expected = IllegalStateException.class)
    public void testEnqueueEvent1() {

        FakeActivity a = new FakeActivity(new Context("A"), false);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, false);
        ActivityRecord r = new ActivityRecord(a, id);

        Event e = new Event(id, id, "Hello");

        r.enqueue(e);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnqueueEvent2() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, false);
        ActivityRecord r = new ActivityRecord(a, id);

        r.enqueue(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEvent1() {

        FakeActivity a = new FakeActivity(new Context("A"), false);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, false);
        ActivityRecord r = new ActivityRecord(a, id);

        r.dequeue();
    }

    @Test
    public void testDequeueEvent2() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        Event e = r.dequeue();

        assertEquals(null, e);
    }

    @Test
    public void testEnqueueDequeueEvent1() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        Event e1 = new Event(id, id, "Hello");
        r.enqueue(e1);
        Event e2 = r.dequeue();

        assertEquals(e1, e2);
        assertEquals(0, r.pendingEvents());
    }

    @Test
    public void testRestrictToLocal1() {

        FakeActivity a = new FakeActivity(new Context("A"), false, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertTrue(r.isRestrictedToLocal());
    }

    @Test
    public void testRestrictToLocal2() {

        FakeActivity a = new FakeActivity(new Context("A"), true, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertFalse(r.isRestrictedToLocal());
    }

    @Test
    public void testIsRemote1() {

        FakeActivity a = new FakeActivity(new Context("A"), true, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertFalse(r.isRemote());
    }

    @Test
    public void testIsRemote2() {

        FakeActivity a = new FakeActivity(new Context("A"), true, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        r.setRemote(true);
        assertTrue(r.isRemote());
    }

    @Test
    public void testIsRelocated1() {

        FakeActivity a = new FakeActivity(new Context("A"), true, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertFalse(r.isRelocated());
    }

    @Test
    public void testIsRelocated2() {

        FakeActivity a = new FakeActivity(new Context("A"), true, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        r.setRelocated(true);
        assertTrue(r.isRelocated());
    }

    @Test
    public void testIsStolen1() {

        FakeActivity a = new FakeActivity(new Context("A"), true, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertFalse(r.isStolen());
    }

    @Test
    public void testIsStolen2() {

        FakeActivity a = new FakeActivity(new Context("A"), true, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        r.setStolen(true);
        assertTrue(r.isStolen());
    }

    @Test
    public void testStateIsFresh1() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertTrue(r.isFresh());
    }

    @Test
    public void testStateNeedsToRun() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertTrue(r.needsToRun());
    }

    @Test
    public void testStateIsNotDone() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertFalse(r.isDone());
    }

    @Test
    public void testStateTransition1() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertTrue(r.isFresh());

        r.run(fc);

        assertTrue(a.initialized);
        assertFalse(a.gotEvent);
        assertFalse(a.clean);

        assertFalse(r.isFresh());
        assertFalse(r.isDone());
        assertFalse(r.needsToRun());
    }

    @Test
    public void testStateTransition2() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertTrue(r.isFresh());

        r.run(fc);

        assertTrue(a.initialized);
        assertFalse(a.gotEvent);
        assertFalse(a.clean);

        assertFalse(r.isFresh());
        assertFalse(r.isDone());
        assertFalse(r.needsToRun());

        Event e = new Event(id, id, "Hello");
        r.enqueue(e);

        assertTrue(r.setRunnable());

        r.run(fc);

        assertTrue(a.initialized);
        assertTrue(a.gotEvent);
        assertEquals(e, a.event);
        assertFalse(a.clean);

        assertFalse(r.isFresh());
        assertFalse(r.isDone());
        assertTrue(r.needsToRun());
    }

    @Test
    public void testStateTransition3() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertTrue(r.isFresh());

        r.run(fc);

        assertTrue(a.initialized);
        assertFalse(a.gotEvent);
        assertFalse(a.clean);

        assertFalse(r.isFresh());
        assertFalse(r.isDone());
        assertFalse(r.needsToRun());

        Event e = new Event(id, id, "Hello");
        r.enqueue(e);

        assertTrue(r.setRunnable());

        r.run(fc);

        assertTrue(a.initialized);
        assertTrue(a.gotEvent);
        assertEquals(e, a.event);
        assertFalse(a.clean);

        assertFalse(r.isFresh());
        assertFalse(r.isDone());
        assertTrue(r.needsToRun());

        r.run(fc);

        assertTrue(a.initialized);
        assertTrue(a.gotEvent);
        assertEquals(e, a.event);
        assertTrue(a.clean);

        assertFalse(r.isFresh());
        assertTrue(r.isDone());
        assertFalse(r.needsToRun());
    }

    @Test
    public void testStateTransition4() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertTrue(r.isFresh());

        r.run(fc);

        assertTrue(r.setRunnable());

        // This run expects an event to be queued
        r.run(fc);

        assertTrue(r.isError());
    }

    @Test
    public void testStateTransition5() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertTrue(r.isFresh());

        r.run(fc);

        // This requires r.setRunnable to be invoked first
        r.run(fc);

        assertTrue(r.isError());
    }

    @Test
    public void testStateTransition6() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        Event e = new Event(id, id, "Hello");
        r.enqueue(e);

        r.run(fc);

        // Should not go to suspend if an event is already queued before initialize
        assertTrue(r.needsToRun());
    }

    @Test(expected = IllegalStateException.class)
    public void testStateTransition7() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        Event e = new Event(id, id, "Hello");
        r.enqueue(e);

        r.run(fc);
        r.run(fc);

        // Should fail with an illegalstate
        r.enqueue(e);
    }

    @Test(expected = IllegalStateException.class)
    public void testStateTransition8() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        Event e = new Event(id, id, "Hello");
        r.enqueue(e);

        r.run(fc);
        r.run(fc);
        r.run(fc);

        // Should fail with an illegalstate
        r.enqueue(e);
    }

    @Test
    public void testCrashActivity1() {

        Constellation fc = ImplUtil.createFakeConstellation();

        CrashActivity a = new CrashActivity(new Context("A"), true, false, false);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        r.run(fc);

        assertTrue(r.isError());
    }

    @Test
    public void testCrashActivity2() {

        Constellation fc = ImplUtil.createFakeConstellation();

        CrashActivity a = new CrashActivity(new Context("A"), false, true, false);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        r.run(fc);

        Event e = new Event(id, id, "Hello");
        r.enqueue(e);

        assertTrue(r.setRunnable());

        r.run(fc);

        assertTrue(r.isDone());
        assertTrue(r.isError());
    }

    @Test
    public void testCrashActivity3() {

        Constellation fc = ImplUtil.createFakeConstellation();

        CrashActivity a = new CrashActivity(new Context("A"), false, false, true);

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        r.run(fc);

        Event e = new Event(id, id, "Hello");
        r.enqueue(e);

        assertTrue(r.setRunnable());

        r.run(fc);

        assertTrue(r.needsToRun());

        r.run(fc);

        assertTrue(r.isDone());
        assertTrue(r.isError());
    }

    @Test
    public void testSetRunnable() {

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertFalse(r.setRunnable());
    }

    @Test
    public void testSetRunnable2() {

        Constellation fc = ImplUtil.createFakeConstellation();

        FakeActivity a = new FakeActivity(new Context("A"));

        ActivityIdentifierImpl id = (ActivityIdentifierImpl) ImplUtil.createActivityIdentifier(1, 42, 1001, true);
        ActivityRecord r = new ActivityRecord(a, id);

        assertFalse(r.setRunnable());

        r.run(fc);

        assertTrue(r.setRunnable());
    }

}
