package ibis.constellation.extra;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibis.constellation.ActivityContext;
import ibis.constellation.ExecutorContext;
import ibis.constellation.StealStrategy;
import ibis.constellation.context.OrActivityContext;
import ibis.constellation.context.OrExecutorContext;
import ibis.constellation.context.UnitActivityContext;
import ibis.constellation.context.UnitExecutorContext;
import ibis.constellation.impl.ActivityIdentifierImpl;
import ibis.constellation.impl.ActivityRecord;

public class SmartSortedWorkQueue extends WorkQueue {

    public static final Logger log = LoggerFactory
            .getLogger(SmartSortedWorkQueue.class);

    // We maintain two lists here, which reflect the relative complexity of
    // the context associated with the jobs:
    //
    // 'UNIT' jobs are likely to have limited suitable locations, but
    // their context matching is easy
    // 'OR' jobs may have more suitable locations, but their context matching
    // is more expensive

    protected final HashMap<ActivityIdentifierImpl, ActivityRecord> ids = new HashMap<ActivityIdentifierImpl, ActivityRecord>();

    protected final HashMap<String, SortedList> unit = new HashMap<String, SortedList>();

    protected final HashMap<String, SortedList> or = new HashMap<String, SortedList>();

    protected int size;

    public SmartSortedWorkQueue(String id) {
        super(id);
    }

    @Override
    public synchronized int size() {
        return size;
    }

    private ActivityRecord getUnit(UnitExecutorContext c, StealStrategy s) {

        SortedList tmp = unit.get(c.name);

        if (tmp == null) {
            return null;
        }

        if (log.isInfoEnabled()) {
            log.info("Matching context string: " + c.name);
        }

        assert tmp.size() > 0;

        ActivityRecord a = null;

        switch (s.strategy) {
        case StealStrategy._BIGGEST:
        case StealStrategy._ANY:
            a = tmp.removeTail();
            break;

        case StealStrategy._SMALLEST:
            a = tmp.removeHead();
            break;

        case StealStrategy._VALUE:
        case StealStrategy._RANGE:
            a = tmp.removeOneInRange(s.start, s.end);
            break;
        }

        assert a != null;

        if (tmp.size() == 0) {
            unit.remove(c.name);
        }

        size--;

        ids.remove(a.identifier());

        return a;
    }

    private ActivityRecord getOr(UnitExecutorContext c, StealStrategy s) {

        SortedList tmp = or.get(c.name);

        if (tmp == null) {
            return null;
        }

        if (log.isInfoEnabled()) {
            log.info("Matching context string: " + c.name);
        }

        assert (tmp.size() > 0);

        ActivityRecord a = null;

        switch (s.strategy) {
        case StealStrategy._BIGGEST:
        case StealStrategy._ANY:
            a = tmp.removeTail();
            break;

        case StealStrategy._SMALLEST:
            a = tmp.removeHead();
            break;

        case StealStrategy._VALUE:
        case StealStrategy._RANGE:
            a = tmp.removeOneInRange(s.start, s.end);
            break;
        }

        assert a != null;

        if (tmp.size() == 0) {
            or.remove(c.name);
        }

        // Remove entry for this ActivityRecord from all lists....
        OrActivityContext cntx = (OrActivityContext) a.activity.getContext();

        for (int i = 0; i < cntx.size(); i++) {

            UnitActivityContext u = cntx.get(i);

            // Remove this activity from all entries in the 'or' table
            tmp = or.get(u.name);

            if (tmp != null) {
                tmp.removeByReference(a);

                if (tmp.size() == 0) {
                    or.remove(u.name);
                }
            }
        }

        size--;

        ids.remove(a.identifier());

        return a;
    }

    private void enqueueUnit(UnitActivityContext c, ActivityRecord a) {

        SortedList tmp = unit.get(c.name);

        if (tmp == null) {
            tmp = new SortedList(c.name);
            unit.put(c.name, tmp);
        }

        tmp.insert(a, c.rank);
        size++;
        ids.put(a.identifierImpl(), a);
    }

    private void enqueueOr(OrActivityContext c, ActivityRecord a) {

        for (int i = 0; i < c.size(); i++) {

            UnitActivityContext uc = c.get(i);

            SortedList tmp = or.get(uc.name);

            if (tmp == null) {
                tmp = new SortedList(uc.name);
                or.put(uc.name, tmp);
            }

            tmp.insert(a, uc.rank);
        }

        size++;
        ids.put(a.identifierImpl(), a);
    }

    @Override
    public synchronized void enqueue(ActivityRecord a) {

        ActivityContext c = a.activity.getContext();

        if (c.isUnit()) {
            enqueueUnit((UnitActivityContext) c, a);
            return;
        }

        assert (c.isOr());
        enqueueOr((OrActivityContext) c, a);
    }

    @Override
    public synchronized ActivityRecord steal(ExecutorContext c,
            StealStrategy s) {

        if (c.isUnit()) {

            UnitExecutorContext tmp = (UnitExecutorContext) c;

            ActivityRecord a = getUnit(tmp, s);

            if (a == null) {
                a = getOr(tmp, s);
            }

            return a;
        }

        assert (c.isOr());

        OrExecutorContext o = (OrExecutorContext) c;

        for (int i = 0; i < o.size(); i++) {

            UnitExecutorContext ctx = o.get(i);

            ActivityRecord a = getUnit(ctx, s);

            if (a != null) {
                return a;
            }

            a = getOr(ctx, s);

            if (a != null) {
                return a;
            }
        }

        return null;
    }
}
