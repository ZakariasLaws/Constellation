package test.pipeline.inbalance;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Constellation;
import ibis.constellation.util.SimpleActivity;
import ibis.constellation.Context;

public class Stage2 extends SimpleActivity {

    private static final long serialVersionUID = -3987089095770723454L;

    private final long sleep;
    private final Data data;

    public Stage2(ActivityIdentifier parent, long sleep, Data data) {

        super(parent, new Context("B", data.index));
        this.sleep = sleep;
        this.data = data;
    }

    @Override
    public void simpleActivity(Constellation c) {

        if (sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (Exception e) {
                // ignored
            }
        }

        // Submit stage5 first, as it it used to gather the results from
        // stage3&4
        ActivityIdentifier id = c.submit(new Stage5(getParent(), data.index, 100));

        c.submit(new Stage3(id, 1000, data));
        c.submit(new Stage4(id, 600, data));

    }
}
